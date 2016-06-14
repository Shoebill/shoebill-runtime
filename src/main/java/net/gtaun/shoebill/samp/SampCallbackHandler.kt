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

    open fun onAmxLoad(handle: Int) {}
    open fun onAmxUnload(handle: Int) {}
    open fun onProcessTick() {}
    open fun onShoebillUnload() {}
    open fun onShoebillLoad() {}

    open fun onGameModeInit(): Boolean = true
    open fun onGameModeExit(): Boolean = true

    open fun onPlayerConnect(playerId: Int): Boolean = true
    open fun onPlayerDisconnect(playerId: Int, reason: Int): Boolean = true
    open fun onPlayerSpawn(playerId: Int): Boolean = true
    open fun onPlayerDeath(playerId: Int, killerId: Int, reason: Int): Boolean = true

    open fun onVehicleSpawn(vehicleId: Int): Boolean = true
    open fun onVehicleDeath(vehicleId: Int, killerId: Int): Boolean = true

    open fun onPlayerText(playerId: Int, text: String): Int = 1
    open fun onPlayerCommandText(playerId: Int, commandText: String): Int = 0
    open fun onPlayerRequestClass(playerId: Int, classId: Int): Boolean = true
    open fun onPlayerEnterVehicle(playerId: Int, vehicleId: Int, isPassenger: Int): Boolean = true
    open fun onPlayerExitVehicle(playerId: Int, vehicleId: Int): Boolean = true
    open fun onPlayerStateChange(playerId: Int, state: Int, oldState: Int): Boolean = true
    open fun onPlayerEnterCheckpoint(playerId: Int): Boolean = true
    open fun onPlayerLeaveCheckpoint(playerId: Int): Boolean = true
    open fun onPlayerEnterRaceCheckpoint(playerId: Int): Boolean = true
    open fun onPlayerLeaveRaceCheckpoint(playerId: Int): Boolean = true

    open fun onRconCommand(cmd: String): Int = 0

    open fun onPlayerRequestSpawn(playerId: Int): Boolean = true

    open fun onObjectMoved(objectId: Int): Boolean = true

    open fun onPlayerObjectMoved(playerId: Int, objectId: Int): Boolean = true
    open fun onPlayerPickUpPickup(playerId: Int, pickupId: Int): Boolean = true

    open fun onVehicleMod(playerId: Int, vehicleId: Int, componentId: Int): Boolean = true
    open fun onEnterExitModShop(playerId: Int, enterexit: Int, interiorId: Int): Boolean = true
    open fun onVehiclePaintjob(playerId: Int, vehicleId: Int, paintjobId: Int): Boolean = true
    open fun onVehicleRespray(playerId: Int, vehicleId: Int, color1: Int, color2: Int): Boolean = true
    open fun onVehicleDamageStatusUpdate(vehicleId: Int, playerId: Int): Boolean = true
    open fun onUnoccupiedVehicleUpdate(vehicleId: Int, playerId: Int, passengerSeat: Int, newX: Float, newY: Float,
                                       newZ: Float, velX: Float, velY: Float, velZ: Float): Boolean = true

    open fun onPlayerSelectedMenuRow(playerId: Int, row: Int): Boolean = true
    open fun onPlayerExitedMenu(playerId: Int): Boolean = true
    open fun onPlayerInteriorChange(playerId: Int, interiorId: Int, oldInteriorId: Int): Boolean = true
    open fun onPlayerKeyStateChange(playerId: Int, keys: Int, oldKeys: Int): Boolean = true

    open fun onRconLoginAttempt(ip: String, password: String, isSuccess: Int): Int = 1

    open fun onPlayerUpdate(playerId: Int): Boolean = true
    open fun onPlayerStreamIn(playerId: Int, forPlayerId: Int): Boolean = true
    open fun onPlayerStreamOut(playerId: Int, forPlayerId: Int): Boolean = true

    open fun onVehicleStreamIn(vehicleId: Int, forPlayerId: Int): Boolean = true
    open fun onVehicleStreamOut(vehicleId: Int, forPlayerId: Int): Boolean = true

    open fun onDialogResponse(playerId: Int, dialogId: Int, response: Int, listitem: Int, inputtext: String): Int = 0
    open fun onPlayerTakeDamage(playerId: Int, issuerId: Int, amount: Float, weaponId: Int,
                                bodypart: Int): Boolean = true
    open fun onPlayerGiveDamage(playerId: Int, damagedId: Int, amount: Float, weaponId: Int, bodypart: Int): Boolean
            = true
    open fun onPlayerClickMap(playerId: Int, x: Float, y: Float, z: Float): Boolean = true
    open fun onPlayerClickTextDraw(playerId: Int, clickedId: Int): Boolean = false
    open fun onPlayerClickPlayerTextDraw(playerId: Int, playerTextId: Int): Boolean = false
    open fun onPlayerClickPlayer(playerId: Int, clickedPlayerId: Int, source: Int): Boolean = true
    open fun onPlayerEditObject(playerId: Int, playerObject: Int, objectId: Int, response: Int, fX: Float, fY: Float,
                                fZ: Float, fRotX: Float, fRotY: Float, fRotZ: Float): Int = 1
    open fun onPlayerEditAttachedObject(playerId: Int, response: Int, index: Int, modelid: Int, boneid: Int,
                                        fOffsetX: Float, fOffsetY: Float, fOffsetZ: Float, fRotX: Float, fRotY: Float,
                                        fRotZ: Float, fScaleX: Float, fScaleY: Float, fScaleZ: Float): Boolean = true
    open fun onPlayerSelectObject(playerId: Int, type: Int, objectId: Int, modelId: Int, fX: Float,
                                  fY: Float, fZ: Float): Boolean = true
    open fun onPlayerWeaponShot(playerId: Int, weaponId: Int, hitType: Int, hitId: Int, fX: Float, fY: Float,
                                fZ: Float): Boolean = true

    open fun onIncomingConnection(playerId: Int, ipAddress: String, port: Int): Int = 1

    open fun onTrailerUpdate(playerId: Int, vehicleId: Int): Boolean = true

    open fun onHookCall(name: String, vararg objects: Any): IntArray = intArrayOf(0, 0)
    open fun onRegisteredFunctionCall(amx: Int, name: String, parameters: Array<Any>): Int = 1

    open fun onActorStreamIn(actor: Int, playerid: Int): Boolean = true
    open fun onActorStreamOut(actor: Int, playerid: Int): Boolean = true
    open fun onPlayerGiveDamageActor(playerid: Int, actor: Int, amount: Int, weapon: Int, bodypart: Int): Int = 1

    open fun onVehicleSirenStateChange(playerid: Int, vehicleid: Int, newstate: Int): Int = 1
}
