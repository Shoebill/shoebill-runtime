/**
 * Copyright (C) 2012-2014 MK124

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

import net.gtaun.shoebill.data.*
import net.gtaun.shoebill.entities.*
import net.gtaun.shoebill.entities.Timer
import net.gtaun.shoebill.entities.impl.*
import net.gtaun.shoebill.event.destroyable.DestroyEvent
import net.gtaun.shoebill.event.dialog.DialogCloseEvent
import net.gtaun.shoebill.event.dialog.DialogResponseEvent
import net.gtaun.shoebill.event.dialog.DialogShowEvent
import net.gtaun.shoebill.event.player.PlayerPickupEvent
import net.gtaun.shoebill.exception.CreationFailedException
import net.gtaun.shoebill.samp.SampCallbackHandler
import net.gtaun.util.event.Attentions
import net.gtaun.util.event.EventHandler
import net.gtaun.util.event.EventManager
import net.gtaun.util.event.HandlerPriority
import java.util.*

/**
 * @author MK124
 * @author Marvin Haschker
 */
class SampObjectManagerImpl(eventManager: EventManager) : SampObjectStoreImpl(eventManager), SampObjectManager {

    private var allocatedDialogId = MAX_DIALOG_ID / 2
    private val recycledDialogIds = LinkedList<Int>()
    private val occupiedDialogIds = TreeSet<Int>()
    val callbackHandler: SampCallbackHandler

    init {
        init()

        callbackHandler = object : SampCallbackHandler {
            override fun onPlayerConnect(playerId: Int): Boolean {
                createPlayer(playerId)
                return true
            }
        }
    }

    private fun init() {
        eventManagerNode.registerHandler(DestroyEvent::class, {
            val vehicle = it.destroyable as Vehicle
            removeVehicle(vehicle.id)
        }, HandlerPriority.BOTTOM, Attentions.create().clazz(Vehicle::class))

        eventManagerNode.registerHandler(DestroyEvent::class, {
            val `object` = it.destroyable as PlayerObject
            removePlayerObject(`object`.player, `object`.id)
        }, HandlerPriority.BOTTOM, Attentions.create().clazz(PlayerObject::class))

        eventManagerNode.registerHandler(DestroyEvent::class, {
            val `object` = it.destroyable as SampObject
            if (`object` is PlayerObject) return@registerHandler
            removeObject(`object`.id)
        }, HandlerPriority.BOTTOM, Attentions.create().clazz(SampObject::class))

        eventManagerNode.registerHandler(DestroyEvent::class, {
            val pickup = it.destroyable as Pickup
            removePickup(pickup.id)
        }, HandlerPriority.BOTTOM, Attentions.create().clazz(Pickup::class))

        eventManagerNode.registerHandler(DestroyEvent::class, {
            val label = it.destroyable as Label
            if (label is PlayerLabel) return@registerHandler
            removeLabel(label.id)
        }, HandlerPriority.BOTTOM, Attentions.create().clazz(Label::class))

        eventManagerNode.registerHandler(DestroyEvent::class, {
            val label = it.destroyable as PlayerLabel
            removePlayerLabel(label.player, label.id)
        }, HandlerPriority.BOTTOM, Attentions.create().clazz(PlayerLabel::class))

        eventManagerNode.registerHandler(DestroyEvent::class, {
            val textdraw = it.destroyable as Textdraw
            if (textdraw.primitive is PlayerTextdraw) return@registerHandler
            removeTextdraw(textdraw.id)
        }, HandlerPriority.BOTTOM, Attentions.create().clazz(Textdraw::class))

        eventManagerNode.registerHandler(DestroyEvent::class, {
            val textdraw = it.destroyable as PlayerTextdraw
            removePlayerTextdraw(textdraw.player.id, textdraw.id)
        }, HandlerPriority.BOTTOM, Attentions.create().clazz(PlayerTextdraw::class))

        eventManagerNode.registerHandler(DestroyEvent::class, {
            val zone = it.destroyable as Zone
            removeZone(zone.id)
        }, HandlerPriority.BOTTOM, Attentions.create().clazz(Zone::class))

        eventManagerNode.registerHandler(DestroyEvent::class, {
            val menu = it.destroyable as Menu
            removeMenu(menu.id)
        }, HandlerPriority.BOTTOM, Attentions.create().clazz(Menu::class))

        eventManagerNode.registerHandler(DestroyEvent::class, {
            val dialog = it.destroyable as DialogId
            super.removeDialog(dialog)
            val dialogId = dialog.id
            recycleDialogId(dialogId)
        }, HandlerPriority.BOTTOM, Attentions.create().clazz(DialogId::class))

        eventManagerNode.registerHandler(DestroyEvent::class, {
            val timer = it.destroyable as Timer
            super.removeTimer(timer)
        }, HandlerPriority.BOTTOM, Attentions.create().clazz(Timer::class))

        eventManagerNode.registerHandler(DestroyEvent::class, {
            val actor = it.destroyable as Actor
            removeActor(actor.id)
        }, HandlerPriority.BOTTOM, Attentions.create().clazz(Actor::class))

        createServer()
        createWorld()
    }

    private fun createWorld(): WorldImpl {
        world = WorldImpl(this)
        return world
    }

    private fun createServer(): ServerImpl {
        server = ServerImpl(this)
        return server
    }

    @Throws(UnsupportedOperationException::class)
    private fun createPlayer(playerId: Int): PlayerImpl {
        val player = PlayerImpl(eventManagerNode, this, playerId)
        super.setPlayer(playerId, player)
        return player
    }

    @Throws(CreationFailedException::class)
    override fun createVehicle(modelId: Int, loc: AngledLocation, color1: Int, color2: Int, respawnDelay: Int,
                               addSiren: Boolean): VehicleImpl =
            VehicleImpl(eventManagerNode, this, modelId, loc, color1, color2, respawnDelay, addSiren)

    @Throws(CreationFailedException::class)
    override fun createObject(modelId: Int, loc: Location, rot: Vector3D, drawDistance: Float): SampObjectImpl =
            SampObjectImpl(eventManagerNode, this, modelId, loc, rot, drawDistance)

    @Throws(CreationFailedException::class)
    override fun createPlayerObject(player: Player, modelId: Int, loc: Location, rot: Vector3D, drawDistance: Float): PlayerObjectImpl {
        if (!player.isOnline) throw CreationFailedException()
        return PlayerObjectImpl(eventManagerNode, this, player, modelId, loc, rot, drawDistance)
    }

    @Throws(CreationFailedException::class)
    override fun createLabel(text: String, color: Color, loc: Location, drawDistance: Float,
                             testLOS: Boolean): LabelImpl =
            LabelImpl(eventManagerNode, this, text, color, loc, drawDistance, testLOS)

    private fun allocateDialogId(): Int {
        var dialogId: Int? = recycledDialogIds.poll()
        if (dialogId == null || occupiedDialogIds.contains(dialogId)) {
            while (allocatedDialogId <= MAX_DIALOG_ID && occupiedDialogIds.contains(allocatedDialogId))
                allocatedDialogId++
            if (allocatedDialogId > MAX_DIALOG_ID) {
                if (recycledDialogIds.isEmpty()) throw CreationFailedException()
                dialogId = recycledDialogIds.poll()
            }

            if (dialogId == null) {
                dialogId = allocatedDialogId
                allocatedDialogId++
            }
        }

        return dialogId
    }

    fun addOccupiedDialogId(dialogId: Int) {
        if (occupiedDialogIds.contains(dialogId)) return
        occupiedDialogIds.add(dialogId)
    }

    private fun recycleDialogId(dialogId: Int) = recycledDialogIds.offer(dialogId)

    @Throws(CreationFailedException::class)
    override fun createTextdraw(pos: Vector2D, text: String): Textdraw =
            TextdrawImpl(eventManagerNode, this, pos.x, pos.y, text)

    @Throws(CreationFailedException::class)
    override fun createPlayerTextdraw(player: Player, pos: Vector2D, text: String): PlayerTextdraw {
        if (!player.isOnline) throw CreationFailedException()

        return PlayerTextdrawImpl(eventManagerNode,  player, pos.x, pos.y, text, this)
    }

    @Throws(CreationFailedException::class)
    override fun createZone(area: Area): Zone =
            ZoneImpl(this, eventManagerNode, area.minX, area.minY, area.maxX, area.maxY)

    @Throws(CreationFailedException::class)
    override fun createMenu(title: String, columns: Int, pos: Vector2D, col1Width: Float, col2Width: Float): Menu =
            MenuImpl(eventManagerNode, this, title, columns, pos.x, pos.y, col1Width, col2Width)

    @Throws(CreationFailedException::class)
    override fun createActor(modelid: Int, location: AngledLocation): Actor =
            ActorImpl(eventManagerNode, this, modelid, location, location.angle)

    @Throws(CreationFailedException::class)
    override fun createDialogId(onResponse: EventHandler<DialogResponseEvent>?, onShow: EventHandler<DialogShowEvent>?,
                                onClose: EventHandler<DialogCloseEvent>?): DialogId {
        val dialog = DialogIdImpl(eventManagerNode, allocateDialogId(), onResponse, onShow, onClose)
        putDialog(dialog.id, dialog)
        return dialog
    }

    @Throws(CreationFailedException::class)
    override fun createPickup(modelId: Int, type: Int, loc: Location, event: EventHandler<PlayerPickupEvent>?): Pickup {
        return PickupImpl(eventManagerNode, this, modelId, type, loc, event)
    }

    @Throws(CreationFailedException::class)
    override fun createPlayerLabel(player: Player, text: String, color: Color, loc: Location, drawDistance: Float,
                                   testLOS: Boolean): PlayerLabel {
        if(!player.isOnline) throw CreationFailedException("Player is not online!")
        return PlayerLabelImpl(eventManagerNode, this, player, text, color, loc, drawDistance, testLOS)
    }

    @Throws(CreationFailedException::class)
    override fun createTimer(interval: Int, count: Int, callback: TimerCallback?): Timer {
        val timer = TimerImpl(eventManagerNode, interval, count, callback)
        putTimer(timer)
        return timer
    }

    companion object {
        @JvmField
        val MAX_DIALOG_ID = 32767
    }
}
