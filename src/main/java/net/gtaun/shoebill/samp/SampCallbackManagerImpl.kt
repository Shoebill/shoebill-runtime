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

package net.gtaun.shoebill.samp

import net.gtaun.shoebill.util.TryUtils

import java.util.Queue
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * @author MK124
 * @author Marvin Haschker
 */
class SampCallbackManagerImpl : SampCallbackManager {
    private val callbackHandlers: Queue<SampCallbackHandler> = ConcurrentLinkedQueue<SampCallbackHandler>()
    override val masterCallbackHandler: SampCallbackHandler = object : SampCallbackHandler {

        override fun onProcessTick() = callbackHandlers.catchedForEach { it.onProcessTick() }
        override fun onShoebillUnload() = callbackHandlers.catchedForEach { it.onShoebillUnload() }
        override fun onShoebillLoad() = callbackHandlers.catchedForEach { it.onShoebillLoad() }
        override fun onAmxLoad(handle: Int) = callbackHandlers.catchedForEach { it.onAmxLoad(handle) }
        override fun onAmxUnload(handle: Int) = callbackHandlers.catchedForEach { it.onAmxUnload(handle) }

        override fun onGameModeInit(): Boolean {
            callbackHandlers.catchedForEach { it.onGameModeInit() }
            return true
        }

        override fun onGameModeExit(): Boolean {
            callbackHandlers.catchedForEach { it.onGameModeExit() }
            return true
        }

        override fun onPlayerConnect(playerId: Int): Boolean {
            var ret = true
            callbackHandlers.catchedForEach { ret = ret and it.onPlayerConnect(playerId) }
            return ret
        }

        override fun onPlayerDisconnect(playerId: Int, reason: Int): Boolean {
            var ret = true
            callbackHandlers.catchedForEach { ret = ret and it.onPlayerDisconnect(playerId, reason) }
            return ret
        }

        override fun onPlayerSpawn(playerId: Int): Boolean {
            var ret = true
            callbackHandlers.catchedForEach { ret = ret and it.onPlayerSpawn(playerId) }
            return ret
        }

        override fun onPlayerDeath(playerId: Int, killerId: Int, reason: Int): Boolean {
            callbackHandlers.catchedForEach { it.onPlayerDeath(playerId, killerId, reason) }
            return true
        }

        override fun onVehicleSpawn(vehicleId: Int): Boolean {
            callbackHandlers.catchedForEach { it.onVehicleSpawn(vehicleId) }
            return true
        }

        override fun onVehicleDeath(vehicleId: Int, killerId: Int): Boolean {
            callbackHandlers.catchedForEach { it.onVehicleDeath(vehicleId, killerId) }
            return true
        }

        override fun onPlayerText(playerId: Int, text: String): Int {
            var ret = 1
            callbackHandlers.catchedForEach { ret = ret and it.onPlayerText(playerId, text) }
            return ret
        }

        override fun onPlayerCommandText(playerId: Int, commandText: String): Int {
            var ret = 0
            callbackHandlers.catchedForEach { ret = ret and it.onPlayerCommandText(playerId, commandText) }
            return ret
        }

        override fun onPlayerRequestClass(playerId: Int, classId: Int): Boolean {
            var ret = true
            callbackHandlers.catchedForEach { ret = ret and it.onPlayerRequestClass(playerId, classId) }
            return ret
        }

        override fun onPlayerEnterVehicle(playerId: Int, vehicleId: Int, isPassenger: Int): Boolean {
            callbackHandlers.catchedForEach { it.onPlayerEnterVehicle(playerId, vehicleId, isPassenger) }
            return true
        }

        override fun onPlayerExitVehicle(playerId: Int, vehicleId: Int): Boolean {
            callbackHandlers.catchedForEach { it.onPlayerExitVehicle(playerId, vehicleId) }
            return true
        }

        override fun onPlayerStateChange(playerId: Int, state: Int, oldState: Int): Boolean {
            callbackHandlers.catchedForEach { it.onPlayerStateChange(playerId, state, oldState) }
            return true
        }

        override fun onPlayerEnterCheckpoint(playerId: Int): Boolean {
            callbackHandlers.catchedForEach { it.onPlayerEnterCheckpoint(playerId) }
            return true
        }

        override fun onPlayerLeaveCheckpoint(playerId: Int): Boolean {
            callbackHandlers.catchedForEach { it.onPlayerLeaveCheckpoint(playerId) }
            return true
        }

        override fun onPlayerEnterRaceCheckpoint(playerId: Int): Boolean {
            callbackHandlers.catchedForEach { it.onPlayerEnterRaceCheckpoint(playerId) }
            return true
        }

        override fun onPlayerLeaveRaceCheckpoint(playerId: Int): Boolean {
            callbackHandlers.catchedForEach { it.onPlayerLeaveRaceCheckpoint(playerId) }
            return true
        }

        override fun onRconCommand(cmd: String): Int {
            var ret = 0
            callbackHandlers.catchedForEach { ret = ret and it.onRconCommand(cmd) }
            return ret
        }

        override fun onPlayerRequestSpawn(playerId: Int): Boolean {
            var ret = true
            callbackHandlers.catchedForEach { ret = ret and it.onPlayerRequestSpawn(playerId) }
            return ret
        }

        override fun onObjectMoved(objectId: Int): Boolean {
            callbackHandlers.catchedForEach { it.onObjectMoved(objectId) }
            return true
        }

        override fun onPlayerObjectMoved(playerId: Int, objectId: Int): Boolean {
            callbackHandlers.catchedForEach { it.onPlayerObjectMoved(playerId, objectId) }
            return true
        }

        override fun onPlayerPickUpPickup(playerId: Int, pickupId: Int): Boolean {
            callbackHandlers.catchedForEach { it.onPlayerPickUpPickup(playerId, pickupId) }
            return true
        }

        override fun onVehicleMod(playerId: Int, vehicleId: Int, componentId: Int): Boolean {
            var ret = true
            callbackHandlers.catchedForEach { ret = ret and it.onVehicleMod(playerId, vehicleId, componentId) }
            return ret
        }

        override fun onEnterExitModShop(playerId: Int, enterexit: Int, interiorId: Int): Boolean {
            callbackHandlers.catchedForEach { it.onEnterExitModShop(playerId, enterexit, interiorId) }
            return true
        }

        override fun onVehiclePaintjob(playerId: Int, vehicleId: Int, paintjobId: Int): Boolean {
            callbackHandlers.catchedForEach { it.onVehiclePaintjob(playerId, vehicleId, paintjobId) }
            return true
        }

        override fun onVehicleRespray(playerId: Int, vehicleId: Int, color1: Int, color2: Int): Boolean {
            var ret = true
            callbackHandlers.catchedForEach { ret = ret and it.onVehicleRespray(playerId, vehicleId, color1, color2) }
            return ret
        }

        override fun onVehicleDamageStatusUpdate(vehicleId: Int, playerId: Int): Boolean {
            callbackHandlers.catchedForEach { it.onVehicleDamageStatusUpdate(vehicleId, playerId) }
            return true
        }

        override fun onUnoccupiedVehicleUpdate(vehicleId: Int, playerId: Int, passengerSeat: Int, newX: Float,
                                               newY: Float, newZ: Float, velX: Float, velY: Float,
                                               velZ: Float): Boolean {
            var ret = true
            callbackHandlers.catchedForEach { ret = ret and it.onUnoccupiedVehicleUpdate(vehicleId, playerId,
                    passengerSeat, newX, newY, newZ, velX, velY, velZ) }
            return ret
        }

        override fun onPlayerSelectedMenuRow(playerId: Int, row: Int): Boolean {
            callbackHandlers.catchedForEach { it.onPlayerSelectedMenuRow(playerId, row) }
            return true
        }

        override fun onPlayerExitedMenu(playerId: Int): Boolean {
            callbackHandlers.catchedForEach { it.onPlayerExitedMenu(playerId) }
            return true
        }

        override fun onPlayerInteriorChange(playerId: Int, interiorId: Int, oldInteriorId: Int): Boolean {
            callbackHandlers.catchedForEach { it.onPlayerInteriorChange(playerId, interiorId, oldInteriorId) }
            return true
        }

        override fun onPlayerKeyStateChange(playerId: Int, keys: Int, oldKeys: Int): Boolean {
            var ret = true
            callbackHandlers.catchedForEach { ret = ret and it.onPlayerKeyStateChange(playerId, keys, oldKeys) }
            return ret
        }

        override fun onRconLoginAttempt(ip: String, password: String, isSuccess: Int): Int {
            callbackHandlers.catchedForEach { it.onRconLoginAttempt(ip, password, isSuccess) }
            return 1
        }

        override fun onPlayerUpdate(playerId: Int): Boolean {
            var ret = true
            callbackHandlers.forEach { ret = ret and it.onPlayerUpdate(playerId) }
            return ret
        }

        override fun onPlayerStreamIn(playerId: Int, forPlayerId: Int): Boolean {
            callbackHandlers.catchedForEach { it.onPlayerStreamIn(playerId, forPlayerId) }
            return true
        }

        override fun onPlayerStreamOut(playerId: Int, forPlayerId: Int): Boolean {
            callbackHandlers.catchedForEach { it.onPlayerStreamOut(playerId, forPlayerId) }
            return true
        }

        override fun onVehicleStreamIn(vehicleId: Int, forPlayerId: Int): Boolean {
            callbackHandlers.catchedForEach { it.onVehicleStreamIn(vehicleId, forPlayerId) }
            return true
        }

        override fun onVehicleStreamOut(vehicleId: Int, forPlayerId: Int): Boolean {
            callbackHandlers.catchedForEach { it.onVehicleStreamOut(vehicleId, forPlayerId) }
            return true
        }

        override fun onDialogResponse(playerId: Int, dialogId: Int, response: Int, listitem: Int, inputtext: String): Int {
            var ret = 0
            callbackHandlers.catchedForEach { ret = ret and it.onDialogResponse(playerId, dialogId, response,
                    listitem, inputtext) }
            return ret
        }

        override fun onPlayerTakeDamage(playerId: Int, issuerId: Int, amount: Float, weaponId: Int, bodypart: Int): Boolean {
            var ret = false
            callbackHandlers.catchedForEach { ret = ret and it.onPlayerTakeDamage(playerId, issuerId, amount, weaponId, bodypart) }
            return ret
        }

        override fun onPlayerGiveDamage(playerId: Int, damagedId: Int, amount: Float, weaponId: Int, bodypart: Int): Boolean {
            callbackHandlers.catchedForEach { it.onPlayerGiveDamage(playerId, damagedId, amount, weaponId, bodypart) }
            return true
        }

        override fun onPlayerClickMap(playerId: Int, x: Float, y: Float, z: Float): Boolean {
            callbackHandlers.catchedForEach { it.onPlayerClickMap(playerId, x, y, z) }
            return true
        }

        override fun onPlayerClickTextDraw(playerId: Int, clickedId: Int): Boolean {
            var ret = false
            callbackHandlers.catchedForEach { ret = ret and it.onPlayerClickTextDraw(playerId, clickedId) }
            return ret
        }

        override fun onPlayerClickPlayerTextDraw(playerId: Int, playerTextId: Int): Boolean {
            var ret = false
            callbackHandlers.catchedForEach { ret = ret and it.onPlayerClickPlayerTextDraw(playerId, playerTextId) }
            return ret
        }

        override fun onPlayerClickPlayer(playerId: Int, clickedPlayerId: Int, source: Int): Boolean {
            callbackHandlers.catchedForEach { it.onPlayerClickPlayer(playerId, clickedPlayerId, source) }
            return true
        }

        override fun onPlayerEditObject(playerId: Int, playerObject: Int, objectId: Int, response: Int, fX: Float,
                                        fY: Float, fZ: Float, fRotX: Float, fRotY: Float, fRotZ: Float): Int {
            callbackHandlers.catchedForEach { it.onPlayerEditObject(playerId, playerObject, objectId, response, fX, fY,
                    fZ, fRotX, fRotY, fRotZ) }
            return 1
        }

        override fun onPlayerEditAttachedObject(playerId: Int, response: Int, index: Int, modelid: Int, boneid: Int,
                                                fOffsetX: Float, fOffsetY: Float, fOffsetZ: Float, fRotX: Float,
                                                fRotY: Float, fRotZ: Float, fScaleX: Float, fScaleY: Float,
                                                fScaleZ: Float): Boolean {
            callbackHandlers.catchedForEach { it.onPlayerEditAttachedObject(playerId, response, index, modelid, boneid,
                    fOffsetX, fOffsetY, fOffsetZ, fRotX, fRotY, fRotZ, fScaleX, fScaleY, fScaleZ) }
            return true
        }

        override fun onPlayerSelectObject(playerId: Int, type: Int, objectId: Int, modelId: Int, fX: Float, fY: Float,
                                          fZ: Float): Boolean {
            callbackHandlers.catchedForEach { it.onPlayerSelectObject(playerId, type, objectId, modelId, fX, fY, fZ) }
            return true
        }

        override fun onPlayerWeaponShot(playerId: Int, weaponId: Int, hitType: Int, hitId: Int, fX: Float, fY: Float,
                                        fZ: Float): Boolean {
            var ret = true
            callbackHandlers.catchedForEach { ret = ret and it.onPlayerWeaponShot(playerId, weaponId, hitType,
                    hitId, fX, fY, fZ) }
            return ret
        }

        override fun onIncomingConnection(playerId: Int, ipAddress: String, port: Int): Int {
            callbackHandlers.catchedForEach { it.onIncomingConnection(playerId, ipAddress, port) }
            return 1
        }

        override fun onTrailerUpdate(playerId: Int, vehicleId: Int): Boolean {
            var ret = false
            callbackHandlers.catchedForEach { ret = ret and it.onTrailerUpdate(playerId, vehicleId) }
            return ret
        }


        override fun onRegisteredFunctionCall(amx: Int, name: String, parameters: Array<Any>): Int {
            var returnValue = -1
            callbackHandlers.catchedForEach { returnValue = returnValue and it.onRegisteredFunctionCall(amx, name,
                parameters) }
            return returnValue
        }
    }

    override fun registerCallbackHandler(handler: SampCallbackHandler) {
        callbackHandlers.add(handler)
    }

    override fun unregisterCallbackHandler(handler: SampCallbackHandler) {
        callbackHandlers.remove(handler)
    }

    override fun hasCallbackHandler(handler: SampCallbackHandler): Boolean = callbackHandlers.contains(handler)

    private fun Queue<SampCallbackHandler>.catchedForEach(action: (SampCallbackHandler) -> Unit) {
        this.forEach { TryUtils.tryTo { action(it) } }
    }
}
