/**
 * Copyright (C) 2011-2014 MK124
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.gtaun.shoebill.samp;

import net.gtaun.shoebill.util.TryUtils;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author MK124 & 123marvin123
 */
public class SampCallbackManagerImpl implements SampCallbackManager {
    private Queue<SampCallbackHandler> callbackHandlers;
    private SampCallbackHandler callbackHandler = new SampCallbackHandler() {
        @Override
        public void onProcessTick() {
            for (SampCallbackHandler handler : callbackHandlers) {
                try {
                    handler.onProcessTick();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onShoebillUnload() {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(handler::onShoebillUnload));
        }

        @Override
        public void onShoebillLoad() {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(handler::onShoebillLoad));
        }

        @Override
        public void onAmxLoad(int handle) {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> handler.onAmxLoad(handle)));
        }

        @Override
        public void onAmxUnload(int handle) {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> handler.onAmxUnload(handle)));
        }

        @Override
        public boolean onGameModeInit() {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(handler::onGameModeInit));
            return true;
        }

        @Override
        public boolean onGameModeExit() {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(handler::onGameModeExit));
            return true;
        }

        @Override
        public boolean onPlayerConnect(int playerid) {
            boolean[] ret = {true};
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> ret[0] &= handler.onPlayerConnect(playerid)));
            return ret[0];
        }

        @Override
        public boolean onPlayerDisconnect(int playerid, int reason) {
            boolean[] ret = {true};
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> ret[0] &= handler.onPlayerDisconnect(playerid, reason)));
            return ret[0];
        }

        @Override
        public boolean onPlayerSpawn(int playerid) {
            boolean[] ret = {true};
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> ret[0] &= handler.onPlayerSpawn(playerid)));
            return ret[0];
        }

        @Override
        public boolean onPlayerDeath(int playerid, int killerid, int reason) {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerDeath(playerid, killerid, reason)));
            return true;
        }

        @Override
        public boolean onVehicleSpawn(int vehicleid) {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> handler.onVehicleSpawn(vehicleid)));
            return true;
        }

        @Override
        public boolean onVehicleDeath(int vehicleid, int killerid) {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> handler.onVehicleDeath(vehicleid, killerid)));
            return true;
        }

        @Override
        public int onPlayerText(int playerid, String text) {
            int[] ret = {1};
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> ret[0] &= handler.onPlayerText(playerid, text)));
            return ret[0];
        }

        @Override
        public int onPlayerCommandText(int playerid, String cmdtext) {
            int[] ret = {0};
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> ret[0] |= handler.onPlayerCommandText(playerid, cmdtext)));
            return ret[0];
        }

        @Override
        public boolean onPlayerRequestClass(int playerid, int classid) {
            boolean[] ret = {true};
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> ret[0] &= handler.onPlayerRequestClass(playerid, classid)));
            return ret[0];
        }

        @Override
        public boolean onPlayerEnterVehicle(int playerid, int vehicleid, int ispassenger) {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerEnterVehicle(playerid, vehicleid, ispassenger)));
            return true;
        }

        @Override
        public boolean onPlayerExitVehicle(int playerid, int vehicleid) {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerExitVehicle(playerid, vehicleid)));
            return true;
        }

        @Override
        public boolean onPlayerStateChange(int playerid, int newstate, int oldstate) {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerStateChange(playerid, newstate, oldstate)));
            return true;
        }

        @Override
        public boolean onPlayerEnterCheckpoint(int playerid) {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerEnterCheckpoint(playerid)));
            return true;
        }

        @Override
        public boolean onPlayerLeaveCheckpoint(int playerid) {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerLeaveCheckpoint(playerid)));
            return true;
        }

        @Override
        public boolean onPlayerEnterRaceCheckpoint(int playerid) {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerEnterRaceCheckpoint(playerid)));
            return true;
        }

        @Override
        public boolean onPlayerLeaveRaceCheckpoint(int playerid) {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerLeaveRaceCheckpoint(playerid)));
            return true;
        }

        @Override
        public int onRconCommand(String cmd) {
            int[] ret = {0};
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> ret[0] |= handler.onRconCommand(cmd)));
            return ret[0];
        }

        @Override
        public boolean onPlayerRequestSpawn(int playerid) {
            boolean[] ret = {true};
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> ret[0] &= handler.onPlayerRequestSpawn(playerid)));
            return ret[0];
        }

        @Override
        public boolean onObjectMoved(int objectid) {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> handler.onObjectMoved(objectid)));
            return true;
        }

        @Override
        public boolean onPlayerObjectMoved(int playerid, int objectid) {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerObjectMoved(playerid, objectid)));
            return true;
        }

        @Override
        public boolean onPlayerPickUpPickup(int playerid, int pickupid) {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerPickUpPickup(playerid, pickupid)));
            return true;
        }

        @Override
        public boolean onVehicleMod(int playerid, int vehicleid, int componentid) {
            boolean[] ret = {true};
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> ret[0] &= handler.onVehicleMod(playerid, vehicleid, componentid)));
            return ret[0];
        }

        @Override
        public boolean onEnterExitModShop(int playerid, int enterexit, int interiorid) {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> handler.onEnterExitModShop(playerid, enterexit, interiorid)));
            return true;
        }

        @Override
        public boolean onVehiclePaintjob(int playerid, int vehicleid, int paintjobid) {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> handler.onVehiclePaintjob(playerid, vehicleid, paintjobid)));
            return true;
        }

        @Override
        public boolean onVehicleRespray(int playerid, int vehicleid, int color1, int color2) {
            boolean[] ret = {true};
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> ret[0] &= handler.onVehicleRespray(playerid, vehicleid, color1, color2)));
            return ret[0];
        }

        @Override
        public boolean onVehicleDamageStatusUpdate(int vehicleid, int playerid) {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> handler.onVehicleDamageStatusUpdate(vehicleid, playerid)));
            return true;
        }

        @Override
        public boolean onUnoccupiedVehicleUpdate(int vehicleid, int playerid, int passengerSeat, float newX, float newY, float newZ, float vel_x, float vel_y, float vel_z) {
            boolean[] ret = {true};
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> ret[0] &= handler.onUnoccupiedVehicleUpdate(vehicleid, playerid, passengerSeat, newX, newY, newZ, vel_x, vel_y, vel_z)));
            return ret[0];
        }

        @Override
        public boolean onPlayerSelectedMenuRow(int playerid, int row) {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerSelectedMenuRow(playerid, row)));
            return true;
        }

        @Override
        public boolean onPlayerExitedMenu(int playerid) {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerExitedMenu(playerid)));
            return true;
        }

        @Override
        public boolean onPlayerInteriorChange(int playerid, int newinteriorid, int oldinteriorid) {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerInteriorChange(playerid, newinteriorid, oldinteriorid)));
            return true;
        }

        @Override
        public boolean onPlayerKeyStateChange(int playerid, int newkeys, int oldkeys) {
            boolean[] ret = {true};
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> ret[0] &= handler.onPlayerKeyStateChange(playerid, newkeys, oldkeys)));
            return ret[0];
        }

        @Override
        public int onRconLoginAttempt(String ip, String password, int success) {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> handler.onRconLoginAttempt(ip, password, success)));
            return 1;
        }

        @Override
        public boolean onPlayerUpdate(int playerid) {
            boolean[] ret = {true};
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> ret[0] &= handler.onPlayerUpdate(playerid)));
            return ret[0];
        }

        @Override
        public boolean onPlayerStreamIn(int playerid, int forplayerid) {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerStreamIn(playerid, forplayerid)));
            return true;
        }

        @Override
        public boolean onPlayerStreamOut(int playerid, int forplayerid) {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerStreamOut(playerid, forplayerid)));
            return true;
        }

        @Override
        public boolean onVehicleStreamIn(int vehicleid, int forplayerid) {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> handler.onVehicleStreamIn(vehicleid, forplayerid)));
            return true;
        }

        @Override
        public boolean onVehicleStreamOut(int vehicleid, int forplayerid) {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> handler.onVehicleStreamOut(vehicleid, forplayerid)));
            return true;
        }

        @Override
        public int onDialogResponse(int playerid, int dialogid, int response, int listitem, String inputtext) {
            int[] ret = {0};
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> ret[0] |= handler.onDialogResponse(playerid, dialogid, response, listitem, inputtext)));
            return ret[0];
        }

        @Override
        public boolean onPlayerTakeDamage(int playerId, int issuerId, float amount, int weaponId, int bodypart) {
            boolean[] ret = {false};
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> ret[0] &= handler.onPlayerTakeDamage(playerId, issuerId, amount, weaponId, bodypart)));
            return ret[0];
        }

        @Override
        public boolean onPlayerGiveDamage(int playerId, int damagedId, float amount, int weaponId, int bodypart) {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerGiveDamage(playerId, damagedId, amount, weaponId, bodypart)));
            return true;
        }

        @Override
        public boolean onPlayerClickMap(int playerId, float x, float y, float z) {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerClickMap(playerId, x, y, z)));
            return true;
        }

        @Override
        public boolean onPlayerClickTextDraw(int playerid, int clickedid) {
            boolean[] ret = {false};
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> ret[0] |= handler.onPlayerClickTextDraw(playerid, clickedid)));
            return ret[0];
        }

        @Override
        public boolean onPlayerClickPlayerTextDraw(int playerid, int playertextid) {
            boolean[] ret = {false};
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> ret[0] |= handler.onPlayerClickPlayerTextDraw(playerid, playertextid)));
            return ret[0];
        }

        @Override
        public boolean onPlayerClickPlayer(int playerid, int clickedplayerid, int source) {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerClickPlayer(playerid, clickedplayerid, source)));
            return true;
        }

        @Override
        public int onPlayerEditObject(int playerid, int playerobject, int objectid, int response, float fX, float fY, float fZ, float fRotX, float fRotY, float fRotZ) {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerEditObject(playerid, playerobject, objectid, response, fX, fY, fZ, fRotX, fRotY, fRotZ)));
            return 1;
        }

        @Override
        public boolean onPlayerEditAttachedObject(int playerid, int response, int index, int modelid, int boneid, float fOffsetX, float fOffsetY, float fOffsetZ, float fRotX, float fRotY, float fRotZ, float fScaleX, float fScaleY, float fScaleZ) {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerEditAttachedObject(playerid, response, index, modelid, boneid, fOffsetX, fOffsetY, fOffsetZ, fRotX, fRotY, fRotZ, fScaleX, fScaleY, fScaleZ)));
            return true;
        }

        @Override
        public boolean onPlayerSelectObject(int playerid, int type, int objectid, int modelid, float fX, float fY, float fZ) {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerSelectObject(playerid, type, objectid, modelid, fX, fY, fZ)));
            return true;
        }

        @Override
        public boolean onPlayerWeaponShot(int playerid, int weaponid, int hittype, int hitid, float fX, float fY, float fZ) {
            boolean[] ret = {true};
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> ret[0] &= handler.onPlayerWeaponShot(playerid, weaponid, hittype, hitid, fX, fY, fZ)));
            return ret[0];
        }

        @Override
        public int onIncomingConnection(int playerid, String ipAddress, int port) {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> handler.onIncomingConnection(playerid, ipAddress, port)));
            return 1;
        }

        @Override
        public boolean onTrailerUpdate(int playerid, int vehicleid) {
            boolean[] ret = {false};
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> ret[0] &= handler.onTrailerUpdate(playerid, vehicleid)));
            return ret[0];
        }

        @Override
        public int[] onHookCall(String name, Object... objects) {
            Object[] event = {new int[]{0, 0}};
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> event[0] = handler.onHookCall(name, objects)));
            return (int[]) event[0];
        }

        @Override
        public boolean onActorStreamIn(int actor, int playerid) {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> handler.onActorStreamIn(actor, playerid)));
            return true;
        }

        @Override
        public boolean onActorStreamOut(int actor, int playerid) {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> handler.onActorStreamOut(actor, playerid)));
            return true;
        }

        @Override
        public int onPlayerGiveDamageActor(int playerid, int actor, int amount, int weapon, int bodypart) {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerGiveDamageActor(playerid, actor, amount, weapon, bodypart)));
            return 1;
        }

        @Override
        public int onVehicleSirenStateChange(int playerid, int vehicleid, int newstate) {
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> handler.onVehicleSirenStateChange(playerid, vehicleid, newstate)));
            return 1;
        }

        @Override
        public int onRegisteredFunctionCall(int amx, String name, Object[] parameters) {
            int[] returnValue = new int[] { -1 };
            callbackHandlers.forEach(handler -> TryUtils.tryTo(() -> returnValue[0] = handler.onRegisteredFunctionCall(amx, name, parameters)));
            return returnValue[0];
        }
    };

    public SampCallbackManagerImpl() {
        callbackHandlers = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void registerCallbackHandler(SampCallbackHandler handler) {
        callbackHandlers.add(handler);
    }

    @Override
    public void unregisterCallbackHandler(SampCallbackHandler handler) {
        callbackHandlers.remove(handler);
    }

    @Override
    public boolean hasCallbackHandler(SampCallbackHandler handler) {
        return callbackHandlers.contains(handler);
    }

    @Override
    public SampCallbackHandler getMasterCallbackHandler() {
        return callbackHandler;
    }
}
