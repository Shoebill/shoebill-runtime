/**
 * Copyright (C) 2011-2014 MK124

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.gtaun.shoebill

import net.gtaun.shoebill.data.SpawnInfo
import net.gtaun.shoebill.entities.*
import net.gtaun.shoebill.entities.Timer
import net.gtaun.shoebill.entities.impl.*
import net.gtaun.shoebill.event.dialog.DialogResponseEvent
import net.gtaun.shoebill.event.sampobject.ObjectMovedEvent
import net.gtaun.shoebill.event.sampobject.PlayerObjectMovedEvent
import net.gtaun.shoebill.event.player.PlayerDisconnectEvent
import net.gtaun.shoebill.event.player.PlayerUpdateEvent
import net.gtaun.shoebill.event.vehicle.VehicleModEvent
import net.gtaun.shoebill.event.vehicle.VehicleUpdateDamageEvent
import net.gtaun.util.event.EventManager
import net.gtaun.util.event.HandlerPriority

import java.lang.ref.Reference
import java.lang.ref.WeakReference
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * @author MK124 & 123marvin123
 */
open class SampObjectStoreImpl internal constructor(rootEventManager: EventManager) : SampObjectStore {

    internal val eventManagerNode: EventManager = rootEventManager.createChildNode()

    lateinit override var server: ServerImpl
    lateinit override var world: WorldImpl
    override val players: MutableList<Player> = mutableListOf()
    override val vehicles: MutableList<Vehicle> = mutableListOf()
    override val objects: MutableList<SampObject> = mutableListOf()
    private val playerObjectsArray: MutableList<MutableList<PlayerObject>> = mutableListOf()
    override val playerClasses: MutableList<SpawnInfo> = mutableListOf()
    override val pickups: MutableList<Pickup> = mutableListOf()
    override val labels: MutableList<Label> = mutableListOf()
    private val playerLabelsArray: MutableList<MutableList<PlayerLabel>> = mutableListOf()
    override val textdraws: MutableList<Textdraw> = mutableListOf()
    private val playerTextdrawsArray: MutableList<MutableList<PlayerTextdraw>> = mutableListOf()
    override val menus: MutableList<Menu> = mutableListOf()
    override val zones: MutableList<Zone> = mutableListOf()
    override val actors: MutableList<Actor> = mutableListOf()
    private val timers = ConcurrentLinkedQueue<Reference<TimerImpl>>()
    private val dialogs = ConcurrentHashMap<Int, Reference<DialogIdImpl>>()
    private val staticPickups: MutableList<Reference<PickupImpl>> = mutableListOf()


    init {
        setupObjectEventHandler()
    }

    private fun setupObjectEventHandler() {
        eventManagerNode.registerHandler(PlayerUpdateEvent::class.java, HandlerPriority.MONITOR) { e ->
            val primitive = e.player.primitive
            if (primitive !is PlayerImpl) return@registerHandler

            primitive.onPlayerUpdate()
        }

        eventManagerNode.registerHandler(PlayerDisconnectEvent::class.java, HandlerPriority.BOTTOM) { e ->
            val primitive = e.player.primitive
            if (primitive !is PlayerImpl) return@registerHandler

            primitive.onPlayerDisconnect()
        }

        eventManagerNode.registerHandler(DialogResponseEvent::class.java, HandlerPriority.MONITOR) { e ->
            val primitive = e.player.primitive
            if (primitive !is PlayerImpl) return@registerHandler

            primitive.onDialogResponse()
        }

        eventManagerNode.registerHandler(PlayerObjectMovedEvent::class.java, HandlerPriority.MONITOR) { e ->
            val primitive = e.`object`.primitive
            if (primitive !is PlayerObjectImpl) return@registerHandler

            primitive.onPlayerObjectMoved()
        }

        eventManagerNode.registerHandler(ObjectMovedEvent::class.java, HandlerPriority.MONITOR) { e ->
            val primitive = e.`object`.primitive
            if (primitive !is SampObjectImpl) return@registerHandler

            primitive.onObjectMoved()
        }

        eventManagerNode.registerHandler(VehicleModEvent::class.java, HandlerPriority.MONITOR) { e ->
            val primitive = e.vehicle.primitive
            if (primitive !is VehicleImpl) return@registerHandler

            primitive.onVehicleMod()
        }

        eventManagerNode.registerHandler(VehicleUpdateDamageEvent::class.java, HandlerPriority.MONITOR) { e ->
            val primitive = e.vehicle.primitive
            if (primitive !is VehicleImpl) return@registerHandler

            primitive.onVehicleUpdateDamage()
        }
    }

    override fun getPlayer(id: Int): Player? = players.getOrNull(id)

    override fun getVehicle(id: Int): Vehicle? = vehicles.getOrNull(id)

    override fun getObject(id: Int): SampObject? = objects.getOrNull(id)

    override fun getPickup(id: Int): Pickup? = pickups.getOrNull(id)

    override fun getStaticPickup(id: Int): Pickup? = staticPickups.getOrNull(id)?.get()

    override fun getLabel(id: Int): Label? = labels.getOrNull(id)

    override fun getTextdraw(id: Int): Textdraw? = textdraws.getOrNull(id)

    override fun getZone(id: Int): Zone? = zones.getOrNull(id)

    override fun getMenu(id: Int): Menu? = menus.getOrNull(id)

    override fun getDialog(id: Int): DialogId? = dialogs[id]?.get()

    override val humanPlayers: Collection<Player>
        get() = players.filter { !it.isNpc }.toList()

    override val npcPlayers: Collection<Player>
        get() = players.filter { it.isNpc }.toList()

    override val dialogIds: Collection<DialogId>
        get() = dialogs.values.filter { it.get() != null }.map { it.get() }.toList()

    override fun getActor(id: Int): Actor? = actors.getOrNull(id)

    override val vehiclePoolSize: Int
        get() = SampNativeFunction.getVehiclePoolSize()

    override val playerPoolSize: Int
        get() = SampNativeFunction.getPlayerPoolSize()

    override val actorPoolSize: Int
        get() = SampNativeFunction.getActorPoolSize()

    fun getTimers(): Collection<Timer> {
        clearUnusedReferences(timers)
        return timers.map { it.get() }
    }

    internal fun setPlayer(id: Int, player: PlayerImpl) {
        players[id] = player

        playerObjectsArray[id] = mutableListOf()
        playerLabelsArray[id] = mutableListOf()
        playerTextdrawsArray[id] = mutableListOf()
    }

    fun removePlayer(id: Int) {
        val playerLabels = playerLabelsArray.getOrNull(id)
        val playerObjects = playerObjectsArray.getOrNull(id)
        val playerTextdraws = playerTextdrawsArray.getOrNull(id)

        playerLabels?.filter { !it.isDestroyed }?.forEach { it.destroy() }
        playerObjects?.filter { !it.isDestroyed }?.forEach { it.destroy() }
        playerTextdraws?.filter { !it.isDestroyed }?.forEach { it.destroy() }

        playerLabelsArray.removeAt(id)
        playerObjectsArray.removeAt(id)
        playerTextdrawsArray.removeAt(id)

        players.removeAt(id)
    }

    fun removeVehicle(id: Int) = vehicles.tryRemoveAtIndex(id)

    fun removePlayerObject(player: Player, id: Int) = playerObjectsArray[player.id].tryRemoveAtIndex(id)

    fun removeObject(id: Int) = objects.tryRemoveAtIndex(id)

    fun removePickup(id: Int) = pickups.tryRemoveAtIndex(id)

    fun removeLabel(id: Int) = labels.tryRemoveAtIndex(id)

    fun removePlayerLabel(player: Player, id: Int) = playerLabelsArray[player.id].tryRemoveAtIndex(id)

    fun removeTextdraw(id: Int) = textdraws.tryRemoveAtIndex(id)

    fun removePlayerTextdraw(player: Int, id: Int) = playerTextdrawsArray[player].tryRemoveAtIndex(id)

    fun removeZone(id: Int) = zones.tryRemoveAtIndex(id)

    fun removeMenu(id: Int) = menus.tryRemoveAtIndex(id)

    fun removeActor(id: Int) = actors.tryRemoveAtIndex(id)

    fun setVehicle(id: Int, vehicle: VehicleImpl) {
        vehicles[id] = vehicle
    }

    fun setObject(id: Int, `object`: SampObjectImpl) {
        objects[id] = `object`
    }

    fun setPlayerObject(player: Player, id: Int, `object`: PlayerObject) {
        if (!player.isOnline) return

        playerObjectsArray[player.id][id] = `object`
    }

    fun setPickup(id: Int, pickup: PickupImpl) {
        pickups[id] = pickup
    }

    fun setLabel(id: Int, label: LabelImpl) {
        labels[id] = label
    }

    fun setPlayerLabel(player: Player, id: Int, label: PlayerLabelImpl) {
        if (!player.isOnline) return

        playerLabelsArray[player.id][id] = label
    }

    fun setTextdraw(id: Int, textdraw: TextdrawImpl) {
        textdraws[id] = textdraw
    }

    fun setPlayerTextdraw(player: Player, id: Int, textdraw: PlayerTextdraw) {
        if (!player.isOnline) return

        playerTextdrawsArray[player.id][id] = textdraw
    }

    fun setZone(id: Int, zone: ZoneImpl) {
        zones[id] = zone
    }

    fun setMenu(id: Int, menu: MenuImpl) {
        menus[id] = menu
    }

    fun setPlayerClass(id: Int, playerClass: SpawnInfo) {
        playerClasses[id] = playerClass
    }

    fun setActor(id: Int, actor: ActorImpl) {
        actors[id] = actor
    }

    internal fun putTimer(timer: TimerImpl) {
        clearUnusedReferences(timers)
        timers.add(WeakReference(timer))
    }

    internal fun removeTimer(timer: Timer) {
        clearUnusedReferences(timers)
        timers.apply { remove(find { it.get() === timer }) }
    }

    internal fun putDialog(id: Int, dialog: DialogIdImpl) {
        clearUnusedReferences(dialogs.values)
        dialogs.put(id, WeakReference(dialog))
    }

    internal fun removeDialog(dialog: DialogId) = dialogs.remove(dialog.id)

    override fun getPlayerTextdraws(player: Player): Collection<PlayerTextdraw> =
            playerTextdrawsArray[player.id].toList()

    override fun getPlayerTextdraw(player: Player, id: Int): PlayerTextdraw? =
            getPlayerTextdraws(player).filter { it.id == id }.firstOrNull()

    override fun getPlayerObjects(player: Player): Collection<PlayerObject> =
            playerObjectsArray[player.id].toList()

    override fun getPlayerObject(player: Player, id: Int): PlayerObject? =
            getPlayerObjects(player).filter { it.id == id }.firstOrNull()

    override fun getPlayerLabels(player: Player): Collection<PlayerLabel> =
            playerLabelsArray[player.id].toList()

    override fun getPlayerLabel(player: Player, id: Int): PlayerLabel? =
            getPlayerLabels(player).filter { it.id == id }.firstOrNull()

    override fun getPlayer(name: String): Player? =
            players.filter { it.name == name }.firstOrNull()

    fun addStaticPickup(pickup: PickupImpl) {
        if(!pickup.isStatic) return

        clearUnusedReferences(staticPickups)
        staticPickups.add(WeakReference(pickup))
    }

    fun removeStaticPickup(pickup: Pickup) {
        if(!pickup.isStatic) return

        clearUnusedReferences(staticPickups)
        staticPickups.apply { remove(find { it.get() === pickup }) }
    }

    internal fun getStaticPickups(): List<PickupImpl> {
        clearUnusedReferences(staticPickups)
        return staticPickups.map { it.get() }
    }

    internal val timerImpls: Collection<TimerImpl>
        get() {
            clearUnusedReferences(timers)
            return timers.map { it.get() }
        }


    companion object {

        @JvmField
        val MAX_PLAYERS = 1000

        @JvmStatic
        private val MAX_VEHICLES = 2000

        @JvmStatic
        private val MAX_OBJECTS = 1000

        @JvmStatic
        private val MAX_ZONES = 1024

        @JvmStatic
        private val MAX_TEXT_DRAWS = 2048

        @JvmStatic
        private val MAX_PLAYER_TEXT_DRAWS = 256

        @JvmStatic
        private val MAX_MENUS = 128

        @JvmStatic
        private val MAX_GLOBAL_LABELS = 1024

        @JvmStatic
        private val MAX_PLAYER_LABELS = 1024

        @JvmStatic
        private val MAX_PICKUPS = 4096

        @JvmStatic
        private val MAX_CLASSES = 312

        @JvmStatic
        private val MAX_ACTORS = 1000

        @JvmStatic
        private fun <T> clearUnusedReferences(collection: MutableCollection<Reference<T>>) {
            val iterator = collection.iterator()
            while (iterator.hasNext()) {
                if (iterator.next().get() == null) iterator.remove()
            }
        }
    }
}

fun <T> Collection<T>.indexExists(id: Int): Boolean {
    return id >= 0 && id < this.size
}

fun <T> MutableList<T>.tryRemoveAtIndex(index: Int): Boolean {
    if(!indexExists(index)) return false
    removeAt(index)
    return true
}