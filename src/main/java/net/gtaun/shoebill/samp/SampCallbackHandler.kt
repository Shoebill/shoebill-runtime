/**
 * Copyright (C) 2011-2014 MK124

 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.gtaun.shoebill.samp

/**
 * @author MK124
 * @author Marvin Haschker
 */
interface SampCallbackHandler {

    fun onAmxLoad(handle: Int) {}

    fun onAmxUnload(handle: Int) {}

    fun onProcessTick() {}

    fun onGameModeInit(): Boolean = true
    fun onGameModeExit(): Boolean = true

    fun onPlayerConnect(playerId: Int): Boolean = true
    fun onPlayerDisconnect(playerId: Int, reason: Int): Boolean = true
    fun onPlayerSpawn(playerId: Int): Boolean = true
    fun onPlayerDeath(playerId: Int, killerId: Int, reason: Int): Boolean = true
    fun onPlayerFinishedDownloading(playerId: Int, virtualWorld: Int): Boolean = true

    fun onVehicleSpawn(vehicleId: Int): Boolean = true
    fun onVehicleDeath(vehicleId: Int, killerId: Int): Boolean = true

    fun onPlayerText(playerId: Int, text: String): Int = 1
    fun onPlayerCommandText(playerId: Int, commandText: String): Int = 0
    fun onPlayerRequestClass(playerId: Int, classId: Int): Boolean = true
    fun onPlayerEnterVehicle(playerId: Int, vehicleId: Int, isPassenger: Int): Boolean = true
    fun onPlayerExitVehicle(playerId: Int, vehicleId: Int): Boolean = true
    fun onPlayerStateChange(playerId: Int, state: Int, oldState: Int): Boolean = true
    fun onPlayerEnterCheckpoint(playerId: Int): Boolean = true
    fun onPlayerLeaveCheckpoint(playerId: Int): Boolean = true
    fun onPlayerEnterRaceCheckpoint(playerId: Int): Boolean = true
    fun onPlayerLeaveRaceCheckpoint(playerId: Int): Boolean = true

    fun onRconCommand(cmd: String): Int = 0

    fun onPlayerRequestSpawn(playerId: Int): Boolean = true

    fun onObjectMoved(objectId: Int): Boolean = true

    fun onPlayerObjectMoved(playerId: Int, objectId: Int): Boolean = true
    fun onPlayerPickUpPickup(playerId: Int, pickupId: Int): Boolean = true

    fun onVehicleMod(playerId: Int, vehicleId: Int, componentId: Int): Boolean = true
    fun onEnterExitModShop(playerId: Int, enterexit: Int, interiorId: Int): Boolean = true
    fun onVehiclePaintjob(playerId: Int, vehicleId: Int, paintjobId: Int): Boolean = true
    fun onVehicleRespray(playerId: Int, vehicleId: Int, color1: Int, color2: Int): Boolean = true
    fun onVehicleDamageStatusUpdate(vehicleId: Int, playerId: Int): Boolean = true
    fun onUnoccupiedVehicleUpdate(vehicleId: Int, playerId: Int, passengerSeat: Int, newX: Float, newY: Float,
                                       newZ: Float, velX: Float, velY: Float, velZ: Float): Boolean = true

    fun onPlayerSelectedMenuRow(playerId: Int, row: Int): Boolean = true
    fun onPlayerExitedMenu(playerId: Int): Boolean = true
    fun onPlayerInteriorChange(playerId: Int, interiorId: Int, oldInteriorId: Int): Boolean = true
    fun onPlayerKeyStateChange(playerId: Int, keys: Int, oldKeys: Int): Boolean = true

    fun onRconLoginAttempt(ip: String, password: String, isSuccess: Int): Int = 1

    fun onPlayerUpdate(playerId: Int): Boolean = true
    fun onPlayerStreamIn(playerId: Int, forPlayerId: Int): Boolean = true
    fun onPlayerStreamOut(playerId: Int, forPlayerId: Int): Boolean = true

    fun onVehicleStreamIn(vehicleId: Int, forPlayerId: Int): Boolean = true
    fun onVehicleStreamOut(vehicleId: Int, forPlayerId: Int): Boolean = true

    fun onDialogResponse(playerId: Int, dialogId: Int, response: Int, listitem: Int, inputtext: String): Int = 0
    fun onPlayerTakeDamage(playerId: Int, issuerId: Int, amount: Float, weaponId: Int,
                                bodypart: Int): Boolean = true

    fun onPlayerGiveDamage(playerId: Int, damagedId: Int, amount: Float, weaponId: Int, bodypart: Int): Boolean
            = true

    fun onPlayerClickMap(playerId: Int, x: Float, y: Float, z: Float): Boolean = true
    fun onPlayerClickTextDraw(playerId: Int, clickedId: Int): Boolean = false
    fun onPlayerClickPlayerTextDraw(playerId: Int, playerTextId: Int): Boolean = false
    fun onPlayerClickPlayer(playerId: Int, clickedPlayerId: Int, source: Int): Boolean = true
    fun onPlayerEditObject(playerId: Int, playerObject: Int, objectId: Int, response: Int, fX: Float, fY: Float,
                                fZ: Float, fRotX: Float, fRotY: Float, fRotZ: Float): Int = 1

    fun onPlayerEditAttachedObject(playerId: Int, response: Int, index: Int, modelid: Int, boneid: Int,
                                   fOffsetX: Float, fOffsetY: Float, fOffsetZ: Float, fRotX: Float, fRotY: Float,
                                   fRotZ: Float, fScaleX: Float, fScaleY: Float, fScaleZ: Float): Boolean = true

    fun onPlayerSelectObject(playerId: Int, type: Int, objectId: Int, modelId: Int, fX: Float,
                             fY: Float, fZ: Float): Boolean = true

    fun onPlayerWeaponShot(playerId: Int, weaponId: Int, hitType: Int, hitId: Int, fX: Float, fY: Float,
                           fZ: Float): Boolean = true

    fun onIncomingConnection(playerId: Int, ipAddress: String, port: Int): Int = 1

    fun onTrailerUpdate(playerId: Int, vehicleId: Int): Boolean = true

    fun onHookCall(name: String, vararg objects: Any): IntArray = intArrayOf(0, 0)
    fun onRegisteredFunctionCall(amx: Int, name: String, parameters: Array<Any>): Int = 1

    fun onActorStreamIn(actor: Int, playerid: Int): Boolean = true
    fun onActorStreamOut(actor: Int, playerid: Int): Boolean = true
    fun onPlayerGiveDamageActor(playerid: Int, actor: Int, amount: Float, weapon: Int, bodypart: Int): Boolean = true

    fun onVehicleSirenStateChange(playerid: Int, vehicleid: Int, newstate: Int): Boolean = true
}
