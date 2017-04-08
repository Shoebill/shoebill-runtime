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
import net.gtaun.shoebill.entities.impl.*
import net.gtaun.shoebill.event.dialog.DialogResponseEvent
import net.gtaun.shoebill.event.player.PlayerDisconnectEvent
import net.gtaun.shoebill.event.player.PlayerUpdateEvent
import net.gtaun.shoebill.event.sampobject.ObjectMovedEvent
import net.gtaun.shoebill.event.sampobject.PlayerObjectMovedEvent
import net.gtaun.shoebill.event.vehicle.VehicleModEvent
import net.gtaun.shoebill.event.vehicle.VehicleUpdateDamageEvent
import net.gtaun.util.event.EventManager
import net.gtaun.util.event.HandlerPriority
import java.lang.ref.Reference
import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * @author MK124 & 123marvin123
 */
open class SampObjectStoreImpl internal constructor(rootEventManager: EventManager) : SampObjectStore {

    internal val eventManagerNode: EventManager = rootEventManager.createChildNode()

    lateinit override var server: ServerImpl
    lateinit override var world: WorldImpl
    override val players: Array<Player?> = arrayOfNulls(MAX_PLAYERS)
    override val vehicles: Array<Vehicle?> = arrayOfNulls(MAX_VEHICLES)
    override val objects: Array<SampObject?> = arrayOfNulls(MAX_OBJECTS)
    private val playerObjectsArray: Array<Array<PlayerObject?>?> = arrayOfNulls(MAX_PLAYERS)
    override val playerClasses: Array<SpawnInfo?> = arrayOfNulls(MAX_CLASSES)
    override val pickups: Array<Pickup?> = arrayOfNulls(MAX_PICKUPS)
    override val labels: Array<Label?> = arrayOfNulls(MAX_GLOBAL_LABELS)
    private val playerLabelsArray: Array<Array<PlayerLabel?>?> = arrayOfNulls(MAX_PLAYERS)
    override val textdraws: Array<Textdraw?> = arrayOfNulls(MAX_TEXT_DRAWS)
    private val playerTextdrawsArray: Array<Array<PlayerTextdraw?>?> = arrayOfNulls(MAX_PLAYERS)
    override val menus: Array<Menu?> = arrayOfNulls(MAX_MENUS)
    override val zones: Array<Zone?> = arrayOfNulls(MAX_ZONES)
    override val actors: Array<Actor?> = arrayOfNulls(MAX_ACTORS)
    private val timers = ConcurrentLinkedQueue<Reference<TimerImpl>>()
    private val dialogs = ConcurrentHashMap<Int, Reference<DialogIdImpl>>()
    private val staticPickups: MutableList<Reference<PickupImpl>> = mutableListOf()


    init {
        setupObjectEventHandler()
    }

    private fun setupObjectEventHandler() {
        eventManagerNode.registerHandler(PlayerUpdateEvent::class, {
            val primitive = it.player.primitive as? PlayerImpl ?: return@registerHandler
            primitive.onPlayerUpdate()
        }, HandlerPriority.MONITOR)

        eventManagerNode.registerHandler(PlayerDisconnectEvent::class, {
            val primitive = it.player.primitive as? PlayerImpl ?: return@registerHandler
            primitive.onPlayerDisconnect()
        }, HandlerPriority.BOTTOM)

        eventManagerNode.registerHandler(DialogResponseEvent::class, {
            val primitive = it.player.primitive as? PlayerImpl ?: return@registerHandler
            primitive.onDialogResponse()
        }, HandlerPriority.MONITOR)

        eventManagerNode.registerHandler(PlayerObjectMovedEvent::class, {
            val primitive = it.`object`.primitive as? PlayerObjectImpl ?: return@registerHandler
            primitive.onPlayerObjectMoved()
        }, HandlerPriority.MONITOR)

        eventManagerNode.registerHandler(ObjectMovedEvent::class, {
            val primitive = it.`object`.primitive as? SampObjectImpl ?: return@registerHandler
            primitive.onObjectMoved()
        }, HandlerPriority.MONITOR)

        eventManagerNode.registerHandler(VehicleModEvent::class, {
            val primitive = it.vehicle.primitive as? VehicleImpl ?: return@registerHandler
            primitive.onVehicleMod()
        }, HandlerPriority.MONITOR)

        eventManagerNode.registerHandler(VehicleUpdateDamageEvent::class, {
            val primitive = it.vehicle.primitive as? VehicleImpl ?: return@registerHandler
            primitive.onVehicleUpdateDamage()
        }, HandlerPriority.MONITOR)
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
        get() = players.filterNotNull().filter { !it.isNpc }

    override val npcPlayers: Collection<Player>
        get() = players.filterNotNull().filter { it.isNpc }

    override val dialogIds: Collection<DialogId>
        get() = dialogs.values.filter { it.get() != null }.map { it.get()!! }

    override fun getActor(id: Int): Actor? = actors.getOrNull(id)

    override val vehiclePoolSize: Int
        get() = SampNativeFunction.getVehiclePoolSize()

    override val playerPoolSize: Int
        get() = SampNativeFunction.getPlayerPoolSize()

    override val actorPoolSize: Int
        get() = SampNativeFunction.getActorPoolSize()

    fun getTimers(): Collection<Timer> {
        clearUnusedReferences(timers)
        return timers.map { it.get()!! }
    }

    internal fun setPlayer(id: Int, player: PlayerImpl) {
        players[id] = player

        playerObjectsArray[id] = arrayOfNulls(MAX_OBJECTS)
        playerLabelsArray[id] = arrayOfNulls(MAX_PLAYER_LABELS)
        playerTextdrawsArray[id] = arrayOfNulls(MAX_PLAYER_TEXT_DRAWS)
    }

    fun removePlayer(id: Int) {
        val playerLabels = playerLabelsArray.getOrNull(id)
        val playerObjects = playerObjectsArray.getOrNull(id)
        val playerTextdraws = playerTextdrawsArray.getOrNull(id)

        playerLabels?.filterNotNull()?.filter { !it.isDestroyed }?.forEach { it.destroy() }
        playerObjects?.filterNotNull()?.filter { !it.isDestroyed }?.forEach { it.destroy() }
        playerTextdraws?.filterNotNull()?.filter { !it.isDestroyed }?.forEach { it.destroy() }

        playerLabelsArray[id] = null
        playerObjectsArray[id] = null
        playerTextdrawsArray[id] = null

        players[id] = null
    }

    fun removeVehicle(id: Int) = vehicles.tryRemoveAtIndex(id)

    fun removePlayerObject(player: Player, id: Int) = playerObjectsArray[player.id]?.tryRemoveAtIndex(id)

    fun removeObject(id: Int) = objects.tryRemoveAtIndex(id)

    fun removePickup(id: Int) = pickups.tryRemoveAtIndex(id)

    fun removeLabel(id: Int) = labels.tryRemoveAtIndex(id)

    fun removePlayerLabel(player: Player, id: Int) = playerLabelsArray[player.id]?.tryRemoveAtIndex(id)

    fun removeTextdraw(id: Int) = textdraws.tryRemoveAtIndex(id)

    fun removePlayerTextdraw(player: Int, id: Int) = playerTextdrawsArray[player]?.tryRemoveAtIndex(id)

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

        val playerObjects = playerObjectsArray[player.id] ?: return
        playerObjects[id] = `object`
    }

    fun setPickup(id: Int, pickup: PickupImpl) {
        pickups[id] = pickup
    }

    fun setLabel(id: Int, label: LabelImpl) {
        labels[id] = label
    }

    fun setPlayerLabel(player: Player, id: Int, label: PlayerLabelImpl) {
        if (!player.isOnline) return

        val playerLabels = playerLabelsArray[player.id] ?: return
        playerLabels[id] = label
    }

    fun setTextdraw(id: Int, textdraw: TextdrawImpl) {
        textdraws[id] = textdraw
    }

    fun setPlayerTextdraw(player: Player, id: Int, textdraw: PlayerTextdraw) {
        if (!player.isOnline) return

        val playerTextdraws = playerTextdrawsArray[player.id] ?: return
        playerTextdraws[id] = textdraw
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
            playerTextdrawsArray[player.id]?.filterNotNull() ?: listOf()

    override fun getPlayerTextdraw(player: Player, id: Int): PlayerTextdraw? =
            getPlayerTextdraws(player).filter { it.id == id }.firstOrNull()

    override fun getPlayerObjects(player: Player): Collection<PlayerObject> =
            playerObjectsArray[player.id]?.filterNotNull() ?: listOf()

    override fun getPlayerObject(player: Player, id: Int): PlayerObject? =
            getPlayerObjects(player).filter { it.id == id }.firstOrNull()

    override fun getPlayerLabels(player: Player): Collection<PlayerLabel> =
            playerLabelsArray[player.id]?.filterNotNull() ?: listOf()

    override fun getPlayerLabel(player: Player, id: Int): PlayerLabel? =
            getPlayerLabels(player).filter { it.id == id }.firstOrNull()

    override fun getPlayer(name: String): Player? =
            players.filterNotNull().filter { it.name == name }.firstOrNull()

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
        return staticPickups.map { it.get()!! }
    }

    internal val timerImpls: Collection<TimerImpl>
        get() {
            clearUnusedReferences(timers)
            return timers.map { it.get()!! }
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
        private val MAX_CLASSES = 320

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

fun <T> Array<T>.indexExists(id: Int): Boolean {
    return id >= 0 && id < this.size
}

fun <T> Array<T?>.tryRemoveAtIndex(index: Int): Boolean {
    if(!indexExists(index)) return false
    this[index] = null
    return true
}