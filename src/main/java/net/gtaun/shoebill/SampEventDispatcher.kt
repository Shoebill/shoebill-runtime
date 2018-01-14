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

import net.gtaun.shoebill.amx.AmxInstanceManager
import net.gtaun.shoebill.constant.*
import net.gtaun.shoebill.data.Vector3D
import net.gtaun.shoebill.entities.Actor
import net.gtaun.shoebill.entities.Player
import net.gtaun.shoebill.entities.Vehicle
import net.gtaun.shoebill.entities.impl.PlayerImpl
import net.gtaun.shoebill.entities.impl.PlayerKeyStateImpl
import net.gtaun.shoebill.event.actor.ActorStreamInEvent
import net.gtaun.shoebill.event.actor.ActorStreamOutEvent
import net.gtaun.shoebill.event.amx.AmxCallEvent
import net.gtaun.shoebill.event.checkpoint.CheckpointEnterEvent
import net.gtaun.shoebill.event.checkpoint.CheckpointLeaveEvent
import net.gtaun.shoebill.event.checkpoint.RaceCheckpointEnterEvent
import net.gtaun.shoebill.event.checkpoint.RaceCheckpointLeaveEvent
import net.gtaun.shoebill.event.dialog.DialogCloseEvent.DialogCloseType
import net.gtaun.shoebill.event.menu.MenuExitedEvent
import net.gtaun.shoebill.event.menu.MenuSelectedEvent
import net.gtaun.shoebill.event.player.*
import net.gtaun.shoebill.event.rcon.RconCommandEvent
import net.gtaun.shoebill.event.rcon.RconLoginEvent
import net.gtaun.shoebill.event.sampobject.ObjectMovedEvent
import net.gtaun.shoebill.event.sampobject.PlayerObjectMovedEvent
import net.gtaun.shoebill.event.server.GameModeExitEvent
import net.gtaun.shoebill.event.server.GameModeInitEvent
import net.gtaun.shoebill.event.server.IncomingConnectionEvent
import net.gtaun.shoebill.event.vehicle.*
import net.gtaun.shoebill.samp.SampCallbackHandler
import net.gtaun.util.event.EventManagerRoot
import net.gtaun.util.event.ThrowableHandler

/**
 * @author MK124
 * @author Marvin Haschker
 */
class SampEventDispatcher(private val sampObjectStore: SampObjectManagerImpl,
                          private val rootEventManager: EventManagerRoot) : SampCallbackHandler {

    private var lastProcessTimeMillis = System.currentTimeMillis()

    init {
        instance = this
    }

    override fun onProcessTick() {
        sampObjectStore.timerImpls.apply {
            if(size > 0) {
                val nowTick = System.currentTimeMillis()
                val interval = (nowTick - lastProcessTimeMillis).toInt()
                forEach {
                    try {
                        it.tick(interval)
                    } catch(e: Exception) {}
                }
            }
        }
        lastProcessTimeMillis = System.currentTimeMillis()
    }

    override fun onGameModeExit(): Boolean {
        rootEventManager.dispatchEvent(GameModeExitEvent())
        return true
    }

    override fun onGameModeInit(): Boolean {
        rootEventManager.dispatchEvent(GameModeInitEvent())
        return true
    }

    override fun onPlayerConnect(playerId: Int): Boolean {
        val player = sampObjectStore.getPlayer(playerId) ?: return false
        val event = PlayerConnectEvent(player)
        rootEventManager.dispatchEvent(event, player)
        return event.response > 0
    }

    override fun onPlayerDisconnect(playerId: Int, reason: Int): Boolean {
        val player = sampObjectStore.getPlayer(playerId) ?: return false
        if (player.dialog != null) {
            DialogEventUtils.dispatchCloseEvent(rootEventManager, player.dialog!!, player, DialogCloseType.CANCEL)
        }
        val event = PlayerDisconnectEvent(player, DisconnectReason.get(reason) ?: DisconnectReason.LEFT)
        rootEventManager.dispatchEvent(event, player)
        sampObjectStore.removePlayer(playerId)
        return event.response > 0
    }

    override fun onPlayerSpawn(playerId: Int): Boolean {
        val player = sampObjectStore.getPlayer(playerId) ?: return false
        val event = PlayerSpawnEvent(player)
        rootEventManager.dispatchEvent(event, player)
        return event.response > 0
    }

    override fun onPlayerDeath(playerId: Int, killerId: Int, reason: Int): Boolean {
        val player = sampObjectStore.getPlayer(playerId) ?: return false
        val killer = sampObjectStore.getPlayer(killerId)
        if (killer != null) {
            val event = PlayerKillEvent(killer, player, WeaponModel.get(reason) ?: WeaponModel.UNKNOWN)
            rootEventManager.dispatchEvent(event, killer, player)
        }
        val event = PlayerDeathEvent(player, killer, WeaponModel.get(reason) ?: WeaponModel.UNKNOWN)
        rootEventManager.dispatchEvent(event, player)
        return true
    }

    override fun onVehicleSpawn(vehicleId: Int): Boolean {
        val vehicle = sampObjectStore.getVehicle(vehicleId) ?: return false
        val event = VehicleSpawnEvent(vehicle)
        rootEventManager.dispatchEvent(event, vehicle)
        return true
    }

    override fun onVehicleDeath(vehicleId: Int, killerId: Int): Boolean {
        val vehicle = sampObjectStore.getVehicle(vehicleId) ?: return false
        val killer = sampObjectStore.getPlayer(killerId)
        val event = VehicleDeathEvent(vehicle, killer)
        rootEventManager.dispatchEvent(event, vehicle, killer)
        return true
    }

    override fun onPlayerText(playerId: Int, text: String): Int {
        val player = sampObjectStore.getPlayer(playerId) ?: return 0
        val event = PlayerTextEvent(player, text)
        rootEventManager.dispatchEvent(event, player)
        return event.response
    }

    override fun onPlayerCommandText(playerId: Int, commandText: String): Int {
        val player = sampObjectStore.getPlayer(playerId) ?: return 0
        val event = PlayerCommandEvent(player, commandText)
        rootEventManager.dispatchEvent(event, player)
        return event.response
    }

    override fun onPlayerRequestClass(playerId: Int, classId: Int): Boolean {
        val player = sampObjectStore.getPlayer(playerId) ?: return false
        val event = PlayerRequestClassEvent(player, classId)
        rootEventManager.dispatchEvent(event, player)
        return event.response > 0
    }

    override fun onPlayerEnterVehicle(playerId: Int, vehicleId: Int, isPassenger: Int): Boolean {
        val player = sampObjectStore.getPlayer(playerId) ?: return false
        val vehicle = sampObjectStore.getVehicle(vehicleId) ?: return false
        val event = VehicleEnterEvent(vehicle, player, isPassenger != 0)
        rootEventManager.dispatchEvent(event, vehicle, player)
        return true
    }

    override fun onPlayerExitVehicle(playerId: Int, vehicleId: Int): Boolean {
        val player = sampObjectStore.getPlayer(playerId) ?: return false
        val vehicle = sampObjectStore.getVehicle(vehicleId) ?: return false
        val event = VehicleExitEvent(vehicle, player)
        rootEventManager.dispatchEvent(event, vehicle, player)
        return true
    }

    override fun onPlayerStateChange(playerId: Int, state: Int, oldState: Int): Boolean {
        val player = sampObjectStore.getPlayer(playerId) ?: return false
        val event = PlayerStateChangeEvent(player, PlayerState.get(oldState) ?: PlayerState.NONE)
        rootEventManager.dispatchEvent(event, player)
        return true
    }

    override fun onPlayerEnterCheckpoint(playerId: Int): Boolean {
        val player = sampObjectStore.getPlayer(playerId) ?: return false
        val checkpoint = player.checkpoint ?: return false
        checkpoint.onEnter(player)
        val event = CheckpointEnterEvent(player)
        rootEventManager.dispatchEvent(event, checkpoint, player)
        return true
    }

    override fun onPlayerLeaveCheckpoint(playerId: Int): Boolean {
        val player = sampObjectStore.getPlayer(playerId) ?: return false
        val checkpoint = player.checkpoint ?: return false
        checkpoint.onLeave(player)
        val event = CheckpointLeaveEvent(player)
        rootEventManager.dispatchEvent(event, checkpoint, player)
        return true
    }

    override fun onPlayerEnterRaceCheckpoint(playerId: Int): Boolean {
        val player = sampObjectStore.getPlayer(playerId) ?: return false
        val checkpoint = player.raceCheckpoint ?: return false
        checkpoint.onEnter(player)
        val event = RaceCheckpointEnterEvent(player)
        rootEventManager.dispatchEvent(event, checkpoint, player)
        return true
    }

    override fun onPlayerLeaveRaceCheckpoint(playerId: Int): Boolean {
        val player = sampObjectStore.getPlayer(playerId) ?: return false
        val checkpoint = player.raceCheckpoint ?: return false
        checkpoint.onLeave(player)
        val event = RaceCheckpointLeaveEvent(player)
        rootEventManager.dispatchEvent(event, checkpoint, player)
        return true
    }

    override fun onRconCommand(cmd: String): Int {
        val event = RconCommandEvent(cmd)
        rootEventManager.dispatchEvent(event, sampObjectStore.server)
        return event.response
    }

    override fun onPlayerRequestSpawn(playerId: Int): Boolean {
        val player = sampObjectStore.getPlayer(playerId) ?: return false
        val event = PlayerRequestSpawnEvent(player)
        rootEventManager.dispatchEvent(event, player)
        return event.response > 0
    }

    override fun onObjectMoved(objectId: Int): Boolean {
        val `object` = sampObjectStore.getObject(objectId) ?: return false
        val event = ObjectMovedEvent(`object`)
        rootEventManager.dispatchEvent(event, `object`)
        return true
    }

    override fun onPlayerObjectMoved(playerId: Int, objectId: Int): Boolean {
        val player = sampObjectStore.getPlayer(playerId) ?: return false
        val `object` = sampObjectStore.getPlayerObject(player, objectId) ?: return false
        val event = PlayerObjectMovedEvent(`object`)
        rootEventManager.dispatchEvent(event, `object`, player)
        return true
    }

    override fun onPlayerPickUpPickup(playerId: Int, pickupId: Int): Boolean {
        val pickup = sampObjectStore.getPickup(pickupId) ?:
                sampObjectStore.getStaticPickup(pickupId) ?:
                return false

        val player = sampObjectStore.getPlayer(playerId) ?: return false
        val event = PlayerPickupEvent(player, pickup)
        rootEventManager.dispatchEvent(event, pickup, player)
        return true
    }

    override fun onVehicleMod(playerId: Int, vehicleId: Int, componentId: Int): Boolean {
        val player = sampObjectStore.getPlayer(playerId) ?: return false
        val vehicle = sampObjectStore.getVehicle(vehicleId) ?: return false
        val event = VehicleModEvent(vehicle, player, VehicleComponentModel.get(componentId))
        rootEventManager.dispatchEvent(event, vehicle, player)
        return event.response > 0
    }

    override fun onEnterExitModShop(playerId: Int, enterexit: Int, interiorId: Int): Boolean {
        val player = sampObjectStore.getPlayer(playerId) ?: return false
        val event = PlayerEnterExitModShopEvent(player, enterexit, interiorId)
        rootEventManager.dispatchEvent(event, player)
        return true
    }

    override fun onVehiclePaintjob(playerId: Int, vehicleId: Int, paintjobId: Int): Boolean {
        val player = sampObjectStore.getPlayer(playerId)
        val vehicle = sampObjectStore.getVehicle(vehicleId) ?: return false
        val event = VehiclePaintjobEvent(vehicle, paintjobId)
        rootEventManager.dispatchEvent(event, vehicle, player)
        return true
    }

    override fun onVehicleRespray(playerId: Int, vehicleId: Int, color1: Int, color2: Int): Boolean {
        val player = sampObjectStore.getPlayer(playerId)
        val vehicle = sampObjectStore.getVehicle(vehicleId) ?: return false
        val event = VehicleResprayEvent(vehicle, color1, color2)
        rootEventManager.dispatchEvent(event, vehicle, player)
        return event.response > 0
    }

    override fun onVehicleDamageStatusUpdate(vehicleId: Int, playerId: Int): Boolean {
        val vehicle = sampObjectStore.getVehicle(vehicleId) ?: return false
        val player = sampObjectStore.getPlayer(playerId)
        val event = VehicleUpdateDamageEvent(vehicle, player)
        rootEventManager.dispatchEvent(event, vehicle, player)
        return true
    }

    override fun onUnoccupiedVehicleUpdate(vehicleId: Int, playerId: Int, passengerSeat: Int, newX: Float, newY: Float,
                                           newZ: Float, velX: Float, velY: Float, velZ: Float): Boolean {
        val vehicle = sampObjectStore.getVehicle(vehicleId) ?: return false
        val player = sampObjectStore.getPlayer(playerId)
        val location = vehicle.location

        location.set(newX, newY, newZ)
        val event = UnoccupiedVehicleUpdateEvent(vehicle, player, location, Vector3D(velX, velY, velZ))
        rootEventManager.dispatchEvent(event, vehicle, player)
        return event.response > 0
    }

    override fun onPlayerSelectedMenuRow(playerId: Int, row: Int): Boolean {
        val player = sampObjectStore.getPlayer(playerId)!!
        val menu = player.currentMenu ?: return false
        val event = MenuSelectedEvent(menu, player, row)
        rootEventManager.dispatchEvent(event, menu, player)
        return true
    }

    override fun onPlayerExitedMenu(playerId: Int): Boolean {
        val player = sampObjectStore.getPlayer(playerId) ?: return false
        val menu = player.currentMenu ?: return false
        val event = MenuExitedEvent(menu, player)
        rootEventManager.dispatchEvent(event, menu, player)
        return true
    }

    override fun onPlayerInteriorChange(playerId: Int, interiorId: Int, oldInteriorId: Int): Boolean {
        val player = sampObjectStore.getPlayer(playerId) ?: return false
        val event = PlayerInteriorChangeEvent(player, oldInteriorId)
        rootEventManager.dispatchEvent(event, player)
        return true
    }

    override fun onPlayerKeyStateChange(playerId: Int, keys: Int, oldKeys: Int): Boolean {
        val player = sampObjectStore.getPlayer(playerId) ?: return false
        val event = PlayerKeyStateChangeEvent(player, PlayerKeyStateImpl(player, oldKeys))
        rootEventManager.dispatchEvent(event, player)
        return event.response > 0
    }

    override fun onRconLoginAttempt(ip: String, password: String, isSuccess: Int): Int {
        val event = RconLoginEvent(ip, password, isSuccess != 0)
        rootEventManager.dispatchEvent(event)
        return 1
    }

    override fun onPlayerUpdate(playerId: Int): Boolean {
        val player = sampObjectStore.getPlayer(playerId) ?: return false
        val event = PlayerUpdateEvent(player)
        rootEventManager.dispatchEvent(event, player)

        val ret = event.response > 0
        if (!ret) return false

        val vehicle = player.vehicle
        if (player.state == PlayerState.DRIVER && vehicle != null) {
            val vehicleUpdateEvent = VehicleUpdateEvent(vehicle, player, player.vehicleSeat)
            rootEventManager.dispatchEvent(vehicleUpdateEvent, vehicle, player)
        }
        return true
    }

    override fun onPlayerStreamIn(playerId: Int, forPlayerId: Int): Boolean {
        val player = sampObjectStore.getPlayer(playerId) ?: return false
        val forPlayer = sampObjectStore.getPlayer(forPlayerId) ?: return false
        val event = PlayerStreamInEvent(player, forPlayer)
        rootEventManager.dispatchEvent(event, player, forPlayer)
        return true
    }

    override fun onPlayerStreamOut(playerId: Int, forPlayerId: Int): Boolean {
        val player = sampObjectStore.getPlayer(playerId) ?: return false
        val forPlayer = sampObjectStore.getPlayer(forPlayerId) ?: return false
        val event = PlayerStreamOutEvent(player, forPlayer)
        rootEventManager.dispatchEvent(event, player, forPlayer)
        return true
    }

    override fun onVehicleStreamIn(vehicleId: Int, forPlayerId: Int): Boolean {
        val vehicle = sampObjectStore.getVehicle(vehicleId) ?: return false
        val player = sampObjectStore.getPlayer(forPlayerId) ?: return false
        val event = VehicleStreamInEvent(vehicle, player)
        rootEventManager.dispatchEvent(event, vehicle, player)
        return true
    }

    override fun onVehicleStreamOut(vehicleId: Int, forPlayerId: Int): Boolean {
        val vehicle = sampObjectStore.getVehicle(vehicleId) ?: return false
        val player = sampObjectStore.getPlayer(forPlayerId) ?: return false
        val event = VehicleStreamOutEvent(vehicle, player)
        rootEventManager.dispatchEvent(event, vehicle, player)
        return true
    }

    override fun onDialogResponse(playerId: Int, dialogId: Int, response: Int, listitem: Int, inputtext: String): Int {
        val player = sampObjectStore.getPlayer(playerId) ?: return 0
        val dialog = sampObjectStore.getDialog(dialogId) ?: return 0
        if (player is PlayerImpl)
            player.dialog = null
        val ret = DialogEventUtils.dispatchResponseEvent(rootEventManager, dialog, player, response, listitem, inputtext)
        DialogEventUtils.dispatchCloseEvent(rootEventManager, dialog, player, DialogCloseType.RESPOND)
        return ret
    }

    override fun onPlayerTakeDamage(playerId: Int, issuerId: Int, amount: Float, weaponId: Int, bodypart: Int): Boolean {
        val player = sampObjectStore.getPlayer(playerId) ?: return false
        val issuer = sampObjectStore.getPlayer(issuerId)
        val event = PlayerTakeDamageEvent(player, issuer, amount, WeaponModel.get(weaponId) ?: WeaponModel.UNKNOWN,
                bodypart)
        rootEventManager.dispatchEvent(event, player)
        return event.response > 0
    }

    override fun onPlayerGiveDamage(playerId: Int, damagedId: Int, amount: Float, weaponId: Int, bodypart: Int): Boolean {
        val player = sampObjectStore.getPlayer(playerId) ?: return false
        val victim = sampObjectStore.getPlayer(damagedId) ?: return false
        val event = PlayerGiveDamageEvent(player, victim, amount, WeaponModel.get(weaponId) ?: WeaponModel.UNKNOWN,
                bodypart)
        rootEventManager.dispatchEvent(event, player)
        return true
    }

    override fun onPlayerClickMap(playerId: Int, x: Float, y: Float, z: Float): Boolean {
        val player = sampObjectStore.getPlayer(playerId) ?: return false

        val event = PlayerClickMapEvent(player, Vector3D(x, y, z))
        rootEventManager.dispatchEvent(event, player)
        return true
    }

    override fun onPlayerClickTextDraw(playerId: Int, clickedId: Int): Boolean {
        val player = sampObjectStore.getPlayer(playerId) ?: return false
        val textdraw = sampObjectStore.getTextdraw(clickedId) ?: return false
        val event = PlayerClickTextDrawEvent(player, textdraw)
        rootEventManager.dispatchEvent(event, player, textdraw)
        return event.response > 0
    }

    override fun onPlayerClickPlayerTextDraw(playerId: Int, playerTextId: Int): Boolean {
        val player = sampObjectStore.getPlayer(playerId) ?: return false
        val textdraw = sampObjectStore.getPlayerTextdraw(player, playerTextId) ?: return false
        val event = PlayerClickPlayerTextDrawEvent(player, textdraw)
        rootEventManager.dispatchEvent(event, player, textdraw)
        return event.response > 0
    }

    override fun onPlayerClickPlayer(playerId: Int, clickedPlayerId: Int, source: Int): Boolean {
        val player = sampObjectStore.getPlayer(playerId) ?: return false
        val clickedPlayer = sampObjectStore.getPlayer(clickedPlayerId) ?: return false
        val event = PlayerClickPlayerEvent(player, clickedPlayer, source)
        rootEventManager.dispatchEvent(event, player, clickedPlayer)
        return true
    }

    override fun onPlayerEditObject(playerId: Int, playerObject: Int, objectId: Int, response: Int, fX: Float,
                                    fY: Float, fZ: Float, fRotX: Float, fRotY: Float, fRotZ: Float): Int {
        val player = sampObjectStore.getPlayer(playerId) ?: return 0
        val editResponse = ObjectEditResponse.get(response) ?: return 0

        if (playerObject == 0) {
            val `object` = sampObjectStore.getObject(objectId) ?: return 0

            val newLocation = `object`.location.clone()
            newLocation.x = fX
            newLocation.y = fY
            newLocation.z = fZ

            val newRotation = Vector3D(fRotX, fRotY, fRotZ)

            val event = PlayerEditObjectEvent(player, `object`, editResponse, newLocation, newRotation)
            rootEventManager.dispatchEvent(event, player, `object`)
        } else {
            val `object` = sampObjectStore.getPlayerObject(player, objectId) ?: return 0

            val newLocation = `object`.location.clone()
            newLocation.x = fX
            newLocation.y = fY
            newLocation.z = fZ

            val newRotation = Vector3D(fRotX, fRotY, fRotZ)

            val event = PlayerEditPlayerObjectEvent(player, `object`, editResponse, newLocation, newRotation)
            rootEventManager.dispatchEvent(event, player, `object`)
        }

        return 1
    }

    override fun onPlayerEditAttachedObject(playerId: Int, response: Int, index: Int, modelid: Int, boneid: Int,
                                            fOffsetX: Float, fOffsetY: Float, fOffsetZ: Float, fRotX: Float,
                                            fRotY: Float, fRotZ: Float, fScaleX: Float, fScaleY: Float,
                                            fScaleZ: Float): Boolean {
        val player = sampObjectStore.getPlayer(playerId) ?: return false
        val slot = player.attach.
                getSlotByBone(PlayerAttachBone.get(boneid) ?: PlayerAttachBone.NOT_USABLE) ?: return false

        val event = PlayerEditAttachedObjectEvent(player, slot, response,
                Vector3D(fOffsetX, fOffsetY, fOffsetZ),
                Vector3D(fRotX, fRotY, fRotZ),
                Vector3D(fScaleX, fScaleY, fScaleZ))
        rootEventManager.dispatchEvent(event, player)

        return true
    }

    override fun onPlayerSelectObject(playerId: Int, type: Int, objectId: Int, modelId: Int, fX: Float,
                                      fY: Float, fZ: Float): Boolean {
        val player = sampObjectStore.getPlayer(playerId) ?: return false
        if (type == 0) {
            val `object` = sampObjectStore.getObject(objectId) ?: return false
            val event = PlayerSelectObjectEvent(player, `object`)
            rootEventManager.dispatchEvent(event, player, `object`)
        } else {
            val `object` = sampObjectStore.getPlayerObject(player, objectId) ?: return false
            val event = PlayerSelectPlayerObjectEvent(player, `object`)
            rootEventManager.dispatchEvent(event, player, `object`)
        }
        return false
    }

    override fun onPlayerWeaponShot(playerId: Int, weaponId: Int, hitType: Int, hitId: Int, fX: Float, fY: Float,
                                    fZ: Float): Boolean {
        val player = sampObjectStore.getPlayer(playerId) ?: return false
        val event = PlayerWeaponShotEvent(player, WeaponModel.get(weaponId) ?: WeaponModel.UNKNOWN,
                BulletHitType.get(hitType) ?: BulletHitType.NONE, hitId, Vector3D(fX, fY, fZ))
        rootEventManager.dispatchEvent(event, player)
        return event.response > 0
    }

    override fun onIncomingConnection(playerId: Int, ipAddress: String, port: Int): Int {
        rootEventManager.dispatchEvent(IncomingConnectionEvent(playerId, ipAddress, port))
        return 1
    }

    override fun onTrailerUpdate(playerId: Int, vehicleId: Int): Boolean {
        val player = sampObjectStore.getPlayer(playerId) ?: return false
        val vehicle = sampObjectStore.getVehicle(vehicleId) ?: return false
        val event = TrailerUpdateEvent(vehicle, player)
        rootEventManager.dispatchEvent(event, vehicle, player)
        return event.response > 0
    }

    override fun onHookCall(name: String, vararg objects: Any): IntArray {
        val event = AmxCallEvent(name, arrayOf(*objects))
        val hooks = Shoebill.get().amxInstanceManager.amxHooks
        hooks.filter { it.name == name && it.isActivated }.forEach {
            try {
                it.onCall.invoke(event)
            } catch(e: Exception) { e.printStackTrace() }
        }
        return intArrayOf(event.returnValue, if (event.isDisallowed) 1 else 0)
    }

    override fun onActorStreamIn(actor: Int, playerid: Int): Boolean {
        val actorObject = Actor.get(actor) ?: return false
        val player = Player.get(playerid) ?: return false
        val event = ActorStreamInEvent(actorObject, player)
        rootEventManager.dispatchEvent(event, actorObject, player)
        return true
    }

    override fun onActorStreamOut(actor: Int, playerid: Int): Boolean {
        val actorObject = Actor.get(actor) ?: return false
        val player = Player.get(playerid) ?: return false
        val event = ActorStreamOutEvent(actorObject, player)
        rootEventManager.dispatchEvent(event, actorObject, player)
        return true
    }

    override fun onPlayerGiveDamageActor(playerid: Int, actor: Int, amount: Float, weapon: Int, bodypart: Int): Boolean {
        val player = Player.get(playerid) ?: return false
        val actorObject = Actor.get(actor) ?: return false
        val model = WeaponModel.get(weapon) ?: WeaponModel.UNKNOWN
        val event = PlayerDamageActorEvent(player, actorObject, amount, model, bodypart)
        rootEventManager.dispatchEvent(event, player, actorObject, model)
        return true
    }

    override fun onVehicleSirenStateChange(playerid: Int, vehicleid: Int, newstate: Int): Boolean {
        val player = Player.get(playerid) ?: return false
        val vehicle = Vehicle.get(vehicleid) ?: return false
        val event = VehicleSirenStateChangeEvent(vehicle, player, newstate > 0)
        rootEventManager.dispatchEvent(event, vehicle, player, newstate > 0)
        return true
    }

    override fun onRegisteredFunctionCall(amx: Int, name: String, parameters: Array<Any>): Int {
        var returnValue = 0
        AmxInstanceManager.get().amxInstances
                .filter { it.handle == amx && it.isFunctionRegistered(name) }
                .forEach { returnValue = it.callRegisteredFunction(name, parameters) }
        return returnValue
    }

    override fun onPlayerFinishedDownloading(playerId: Int, virtualWorld: Int): Boolean {
        val player = Player.get(playerId) ?: return false
        val event = PlayerFinishedDownloadingEvent(player, virtualWorld)
        rootEventManager.dispatchEvent(event, player, virtualWorld)
        return true
    }

    companion object {
        lateinit var instance: SampEventDispatcher
            private set
    }
}
