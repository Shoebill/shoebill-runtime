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
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(handler::onProcessTick));
        }

        @Override
        public void onShoebillUnload() {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(handler::onShoebillUnload));
        }

        @Override
        public void onShoebillLoad() {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(handler::onShoebillLoad));
        }

        @Override
        public boolean isActive() {
            return true;
        }

        @Override
        public void onAmxLoad(int handle) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxLoad(handle)));
        }

        @Override
        public void onAmxUnload(int handle) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxUnload(handle)));
        }

        @Override
        public int onGameModeInit() {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(handler::onGameModeInit));
            return 1;
        }

        @Override
        public int onGameModeExit() {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(handler::onGameModeExit));
            return 1;
        }

        @Override
        public int onPlayerConnect(int playerid) {
            int[] ret = {1};
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> ret[0] &= handler.onPlayerConnect(playerid)));
            return ret[0];
        }

        @Override
        public int onPlayerDisconnect(int playerid, int reason) {
            int[] ret = {1};
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> ret[0] &= handler.onPlayerDisconnect(playerid, reason)));
            return ret[0];
        }

        @Override
        public int onPlayerSpawn(int playerid) {
            int[] ret = {1};
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> ret[0] &= handler.onPlayerSpawn(playerid)));
            return ret[0];
        }

        @Override
        public int onPlayerDeath(int playerid, int killerid, int reason) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerDeath(playerid, killerid, reason)));
            return 1;
        }

        @Override
        public int onVehicleSpawn(int vehicleid) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onVehicleSpawn(vehicleid)));
            return 1;
        }

        @Override
        public int onVehicleDeath(int vehicleid, int killerid) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onVehicleDeath(vehicleid, killerid)));
            return 1;
        }

        @Override
        public int onPlayerText(int playerid, String text) {
            int[] ret = {1};
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> ret[0] &= handler.onPlayerText(playerid, text)));
            return ret[0];
        }

        @Override
        public int onPlayerCommandText(int playerid, String cmdtext) {
            int[] ret = {0};
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> ret[0] |= handler.onPlayerCommandText(playerid, cmdtext)));
            return ret[0];
        }

        @Override
        public int onPlayerRequestClass(int playerid, int classid) {
            int[] ret = {1};
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> ret[0] &= handler.onPlayerRequestClass(playerid, classid)));
            return ret[0];
        }

        @Override
        public int onPlayerEnterVehicle(int playerid, int vehicleid, int ispassenger) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerEnterVehicle(playerid, vehicleid, ispassenger)));
            return 1;
        }

        @Override
        public int onPlayerExitVehicle(int playerid, int vehicleid) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerExitVehicle(playerid, vehicleid)));
            return 1;
        }

        @Override
        public int onPlayerStateChange(int playerid, int newstate, int oldstate) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerStateChange(playerid, newstate, oldstate)));
            return 1;
        }

        @Override
        public int onPlayerEnterCheckpoint(int playerid) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerEnterCheckpoint(playerid)));
            return 1;
        }

        @Override
        public int onPlayerLeaveCheckpoint(int playerid) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerLeaveCheckpoint(playerid)));
            return 1;
        }

        @Override
        public int onPlayerEnterRaceCheckpoint(int playerid) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerEnterRaceCheckpoint(playerid)));
            return 1;
        }

        @Override
        public int onPlayerLeaveRaceCheckpoint(int playerid) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerLeaveRaceCheckpoint(playerid)));
            return 1;
        }

        @Override
        public int onRconCommand(String cmd) {
            int[] ret = {0};
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> ret[0] |= handler.onRconCommand(cmd)));
            return ret[0];
        }

        @Override
        public int onPlayerRequestSpawn(int playerid) {
            int[] ret = {1};
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> ret[0] &= handler.onPlayerRequestSpawn(playerid)));
            return ret[0];
        }

        @Override
        public int onObjectMoved(int objectid) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onObjectMoved(objectid)));
            return 1;
        }

        @Override
        public int onPlayerObjectMoved(int playerid, int objectid) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerObjectMoved(playerid, objectid)));
            return 1;
        }

        @Override
        public int onPlayerPickUpPickup(int playerid, int pickupid) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerPickUpPickup(playerid, pickupid)));
            return 1;
        }

        @Override
        public int onVehicleMod(int playerid, int vehicleid, int componentid) {
            int[] ret = {1};
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> ret[0] &= handler.onVehicleMod(playerid, vehicleid, componentid)));
            return ret[0];
        }

        @Override
        public int onEnterExitModShop(int playerid, int enterexit, int interiorid) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onEnterExitModShop(playerid, enterexit, interiorid)));
            return 1;
        }

        @Override
        public int onVehiclePaintjob(int playerid, int vehicleid, int paintjobid) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onVehiclePaintjob(playerid, vehicleid, paintjobid)));
            return 1;
        }

        @Override
        public int onVehicleRespray(int playerid, int vehicleid, int color1, int color2) {
            int[] ret = {1};
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> ret[0] &= handler.onVehicleRespray(playerid, vehicleid, color1, color2)));
            return ret[0];
        }

        @Override
        public int onVehicleDamageStatusUpdate(int vehicleid, int playerid) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onVehicleDamageStatusUpdate(vehicleid, playerid)));
            return 1;
        }

        @Override
        public int onUnoccupiedVehicleUpdate(int vehicleid, int playerid, int passengerSeat, float newX, float newY, float newZ, float vel_x, float vel_y, float vel_z) {
            int[] ret = {1};
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> ret[0] &= handler.onUnoccupiedVehicleUpdate(vehicleid, playerid, passengerSeat, newX, newY, newZ, vel_x, vel_y, vel_z)));
            return ret[0];
        }

        @Override
        public int onPlayerSelectedMenuRow(int playerid, int row) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerSelectedMenuRow(playerid, row)));
            return 1;
        }

        @Override
        public int onPlayerExitedMenu(int playerid) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerExitedMenu(playerid)));
            return 1;
        }

        @Override
        public int onPlayerInteriorChange(int playerid, int newinteriorid, int oldinteriorid) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerInteriorChange(playerid, newinteriorid, oldinteriorid)));
            return 1;
        }

        @Override
        public int onPlayerKeyStateChange(int playerid, int newkeys, int oldkeys) {
            int[] ret = {1};
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> ret[0] &= handler.onPlayerKeyStateChange(playerid, newkeys, oldkeys)));
            return ret[0];
        }

        @Override
        public int onRconLoginAttempt(String ip, String password, int success) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onRconLoginAttempt(ip, password, success)));
            return 1;
        }

        @Override
        public int onPlayerUpdate(int playerid) {
            int[] ret = {1};
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> ret[0] &= handler.onPlayerUpdate(playerid)));
            return ret[0];
        }

        @Override
        public int onPlayerStreamIn(int playerid, int forplayerid) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerStreamIn(playerid, forplayerid)));
            return 1;
        }

        @Override
        public int onPlayerStreamOut(int playerid, int forplayerid) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerStreamOut(playerid, forplayerid)));
            return 1;
        }

        @Override
        public int onVehicleStreamIn(int vehicleid, int forplayerid) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onVehicleStreamIn(vehicleid, forplayerid)));
            return 1;
        }

        @Override
        public int onVehicleStreamOut(int vehicleid, int forplayerid) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onVehicleStreamOut(vehicleid, forplayerid)));
            return 1;
        }

        @Override
        public int onDialogResponse(int playerid, int dialogid, int response, int listitem, String inputtext) {
            int[] ret = {0};
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> ret[0] |= handler.onDialogResponse(playerid, dialogid, response, listitem, inputtext)));
            return ret[0];
        }

        @Override
        public int onPlayerTakeDamage(int playerId, int issuerId, float amount, int weaponId, int bodypart) {
            int[] ret = {0};
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> ret[0] &= handler.onPlayerTakeDamage(playerId, issuerId, amount, weaponId, bodypart)));
            return ret[0];
        }

        @Override
        public int onPlayerGiveDamage(int playerId, int damagedId, float amount, int weaponId, int bodypart) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerGiveDamage(playerId, damagedId, amount, weaponId, bodypart)));
            return 1;
        }

        @Override
        public int onPlayerClickMap(int playerId, float x, float y, float z) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerClickMap(playerId, x, y, z)));
            return 1;
        }

        @Override
        public int onPlayerClickTextDraw(int playerid, int clickedid) {
            int[] ret = {0};
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> ret[0] |= handler.onPlayerClickPlayerTextDraw(playerid, clickedid)));
            return ret[0];
        }

        @Override
        public int onPlayerClickPlayerTextDraw(int playerid, int playertextid) {
            int[] ret = {0};
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> ret[0] |= handler.onPlayerClickPlayerTextDraw(playerid, playertextid)));
            return ret[0];
        }

        @Override
        public int onPlayerClickPlayer(int playerid, int clickedplayerid, int source) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerClickPlayer(playerid, clickedplayerid, source)));
            return 1;
        }

        @Override
        public int onPlayerEditObject(int playerid, int playerobject, int objectid, int response, float fX, float fY, float fZ, float fRotX, float fRotY, float fRotZ) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerEditObject(playerid, playerobject, objectid, response, fX, fY, fZ, fRotX, fRotY, fRotZ)));
            return 1;
        }

        @Override
        public int onPlayerEditAttachedObject(int playerid, int response, int index, int modelid, int boneid, float fOffsetX, float fOffsetY, float fOffsetZ, float fRotX, float fRotY, float fRotZ, float fScaleX, float fScaleY, float fScaleZ) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerEditAttachedObject(playerid, response, index, modelid, boneid, fOffsetX, fOffsetY, fOffsetZ, fRotX, fRotY, fRotZ, fScaleX, fScaleY, fScaleZ)));
            return 1;
        }

        @Override
        public int onPlayerSelectObject(int playerid, int type, int objectid, int modelid, float fX, float fY, float fZ) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onPlayerSelectObject(playerid, type, objectid, modelid, fX, fY, fZ)));
            return 1;
        }

        @Override
        public int onPlayerWeaponShot(int playerid, int weaponid, int hittype, int hitid, float fX, float fY, float fZ) {
            int[] ret = {1};
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> ret[0] &= handler.onPlayerWeaponShot(playerid, weaponid, hittype, hitid, fX, fY, fZ)));
            return ret[0];
        }

        @Override
        public int onIncomingConnection(int playerid, String ipAddress, int port) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onIncomingConnection(playerid, ipAddress, port)));
            return 1;
        }

        @Override
        public int onTrailerUpdate(int playerid, int vehicleid) {
            int[] ret = {0};
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> ret[0] &= handler.onTrailerUpdate(playerid, vehicleid)));
            return ret[0];
        }

        @Override
        public int onAmxVehicleCreated(int vehicleid, int modelid, float x, float y, float z, float angle, int interiorid, int worldid, int color1, int color2, int respawn_delay) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxVehicleCreated(vehicleid, modelid, x, y, z, angle, interiorid, worldid, color1, color2, respawn_delay)));
            return 1;
        }

        @Override
        public int onAmxDestroyVehicle(int vehicleid) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxDestroyVehicle(vehicleid)));
            return 1;
        }

        @Override
        public int onAmxSampObjectDestroyed(int object_id) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxSampObjectDestroyed(object_id)));
            return 1;
        }

        @Override
        public int onAmxSampObjectCreated(int object_id, int modelid, float x, float y, float z, float rX, float rY, float rZ, int world, int interior, float render_distance) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxSampObjectCreated(object_id, modelid, x, y, z, rX, rY, rZ, world, interior, render_distance)));
            return 1;
        }

        @Override
        public int onAmxAttachObjectToVehicle(int object_id, int vehicleid, float x, float y, float z, float rX, float rY, float rZ) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxAttachObjectToVehicle(object_id, vehicleid, x, y, z, rX, rY, rZ)));
            return 1;
        }

        @Override
        public int onAmxAttachObjectToPlayer(int object_id, int playerid, float x, float y, float z, float rX, float rY, float rZ) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxAttachObjectToPlayer(object_id, playerid, x, y, z, rX, rY, rZ)));
            return 1;
        }

        @Override
        public int onAmxAttachObjectToObject(int object_id, int other_object_id, float x, float y, float z, float rX, float rY, float rZ) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxAttachObjectToObject(object_id, other_object_id, x, y, z, rX, rY, rZ)));
            return 1;
        }

        @Override
        public int onAmxCreatePlayerObject(int playerid, int modelid, float x, float y, float z, float rX, float rY, float rZ, int worldid, int interiorid, float drawdistance, int objectid) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxCreatePlayerObject(playerid, modelid, x, y, z, rX, rY, rZ, worldid, interiorid, drawdistance, objectid)));
            return 1;
        }

        @Override
        public int onAmxDestroyPlayerObject(int playerid, int objectid) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onVehicleDeath(playerid, objectid)));
            return 1;
        }

        @Override
        public int onAmxSetPlayerAttachedObject(int playerid, int index, int modelid, int bone, float offsetX, float offsetY, float offsetZ, float rotX, float rotY, float rotZ, float scaleX, float scaleY, float scaleZ, int materialcolor1, int materialcolor2) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxSetPlayerAttachedObject(playerid, index, modelid, bone, offsetX, offsetY, offsetZ, rotX, rotY, rotZ, scaleX, scaleY, scaleZ, materialcolor1, materialcolor2)));
            return 1;
        }

        @Override
        public int onAmxRemovePlayerAttachedObject(int playerid, int index) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxRemovePlayerAttachedObject(playerid, index)));
            return 1;
        }

        @Override
        public int onAmxDestroyPickup(int pickupid) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxDestroyPickup(pickupid)));
            return 1;
        }

        @Override
        public int onAmxCreatePickup(int model, int type, float posX, float posY, float posZ, int virtualworld, int id) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxCreatePickup(model, type, posX, posY, posZ, virtualworld, id)));
            return 1;
        }

        @Override
        public int onAmxAddStaticPickup(int model, int type, float posX, float posY, float posZ, int virtualworld) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxAddStaticPickup(model, type, posX, posY, posZ, virtualworld)));
            return 1;
        }

        @Override
        public int onAmxCreateLabel(String text, int color, float posX, float posY, float posZ, float drawDistance, int virtualWorld, int testLOS, int id) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxCreateLabel(text, color, posX, posY, posZ, drawDistance, virtualWorld, testLOS, id)));
            return 1;
        }

        @Override
        public int onAmxDeleteLabel(int labelid) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxDeleteLabel(labelid)));
            return 1;
        }

        @Override
        public int onAmxUpdateLabel(int labelid, int color, String text) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxUpdateLabel(labelid, color, text)));
            return 1;
        }

        @Override
        public int onAmxCreatePlayerLabel(int playerid, String text, int color, float posX, float posY, float posZ, float drawDistance, int attachedPlayer, int attachedVehicle, int testLOS, int id) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxCreatePlayerLabel(playerid, text, color, posX, posY, posZ, drawDistance, attachedPlayer, attachedVehicle, testLOS, id)));
            return 1;
        }

        @Override
        public int onAmxDeletePlayerLabel(int playerid, int labelid) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxDeletePlayerLabel(playerid, labelid)));
            return 1;
        }

        @Override
        public int onAmxUpdatePlayerLabel(int playerid, int labelid, int color, String text) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxUpdatePlayerLabel(playerid, labelid, color, text)));
            return 1;
        }

        @Override
        public int onAmxAttachLabelToPlayer(int labelid, int playerid, float offsetX, float offsetY, float offsetZ) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxAttachLabelToPlayer(labelid, playerid, offsetX, offsetY, offsetZ)));
            return 1;
        }

        @Override
        public int onAmxAttachLabelToVehicle(int labelid, int vehicleid, float offsetX, float offsetY, float offsetZ) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxAttachLabelToVehicle(labelid, vehicleid, offsetX, offsetY, offsetZ)));
            return 1;
        }

        @Override
        public int onAmxCreateMenu(String title, int columns, float x, float y, float col1Width, float col2Width, int id) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxCreateMenu(title, columns, x, y, col1Width, col2Width, id)));
            return 1;
        }

        @Override
        public int onAmxSetMenuColumnHeader(int id, int column, String text) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxSetMenuColumnHeader(id, column, text)));
            return 1;
        }

        @Override
        public int onAmxDestroyMenu(int id) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxDestroyMenu(id)));
            return 1;
        }

        @Override
        public int onAmxGangZoneCreate(float minX, float minY, float maxX, float maxY, int id) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxGangZoneCreate(minX, minY, maxX, maxY, id)));
            return 1;
        }

        @Override
        public int onAmxGangZoneDestroy(int id) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxGangZoneDestroy(id)));
            return 1;
        }

        @Override
        public int onAmxGangZoneShowForPlayer(int playerid, int zone, int color) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxGangZoneShowForPlayer(playerid, zone, color)));
            return 1;
        }

        @Override
        public int onAmxGangZoneShowForAll(int zone, int color) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxGangZoneShowForAll(zone, color)));
            return 1;
        }

        @Override
        public int onAmxGangZoneHideForPlayer(int playerid, int zone) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxGangZoneHideForPlayer(playerid, zone)));
            return 1;
        }

        @Override
        public int onAmxGangZoneHideForAll(int zone) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxGangZoneHideForAll(zone)));
            return 1;
        }

        @Override
        public int onAmxGangZoneFlashForPlayer(int playerid, int zone, int flashColor) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxGangZoneFlashForPlayer(playerid, zone, flashColor)));
            return 1;
        }

        @Override
        public int onAmxGangZoneFlashForAll(int zone, int flashColor) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxGangZoneFlashForAll(zone, flashColor)));
            return 1;
        }

        @Override
        public int onAmxGangZoneStopFlashForPlayer(int playerid, int zone) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxGangZoneStopFlashForPlayer(playerid, zone)));
            return 1;
        }

        @Override
        public int onAmxGangZoneStopFlashForAll(int zone) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxGangZoneStopFlashForAll(zone)));
            return 1;
        }

        @Override
        public int onAmxSetSkillLevel(int playerid, int skill, int level) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxSetSkillLevel(playerid, skill, level)));
            return 1;
        }

        @Override
        public int onAmxSetPlayerMapIcon(int playerid, int iconid, float x, float y, float z, int markertype, int color, int style) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxSetPlayerMapIcon(playerid, iconid, x, y, z, markertype, color, style)));
            return 1;
        }

        @Override
        public int onAmxRemovePlayerMapIcon(int playerid, int iconid) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxRemovePlayerMapIcon(playerid, iconid)));
            return 1;
        }

        @Override
        public int onAmxShowPlayerDialog(int playerid, int dialogid, int style, String caption, String info, String button1, String button2) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxShowPlayerDialog(playerid, dialogid, style, caption, info, button1, button2)));
            return 1;
        }

        @Override
        public int onAmxSetPlayerWorldBounds(int playerid, float minX, float minY, float maxX, float maxY) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxSetPlayerWorldBounds(playerid, minX, minY, maxX, maxY)));
            return 1;
        }

        @Override
        public int onAmxSetPlayerWeather(int playerid, int weatherid) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxSetPlayerWeather(playerid, weatherid)));
            return 1;
        }

        @Override
        public int onAmxSetPlayerCheckpoint(int playerid, float x, float y, float z, float size) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxSetPlayerCheckpoint(playerid, x, y, z, size)));
            return 1;
        }

        @Override
        public int onAmxDisablePlayerCheckpoint(int playerid) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxDisablePlayerCheckpoint(playerid)));
            return 1;
        }

        @Override
        public int onAmxSetPlayerRaceCheckpoint(int playerid, int type, float x, float y, float z, float nextX, float nextY, float nextZ, float size) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxSetPlayerRaceCheckpoint(playerid, type, x, y, z, nextX, nextY, nextZ, size)));
            return 1;
        }

        @Override
        public int onAmxDisablePlayerRaceCheckpoint(int playerid) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxDisablePlayerRaceCheckpoint(playerid)));
            return 1;
        }

        @Override
        public int onAmxTogglePlayerSpectating(int playerid, int toggle) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxTogglePlayerSpectating(playerid, toggle)));
            return 1;
        }

        @Override
        public int onAmxPlayerSpectatePlayer(int playerid, int target, int mode) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxPlayerSpectatePlayer(playerid, target, mode)));
            return 1;
        }

        @Override
        public int onAmxPlayerSpectateVehicle(int playerid, int target, int mode) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxPlayerSpectateVehicle(playerid, target, mode)));
            return 1;
        }

        @Override
        public int onAmxEnableStuntBonusForPlayer(int playerid, int toggle) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxEnableStuntBonusForPlayer(playerid, toggle)));
            return 1;
        }

        @Override
        public int onAmxStartRecording(int playerid, int type, String recordName) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxStartRecording(playerid, type, recordName)));
            return 1;
        }

        @Override
        public int onAmxStopRecording(int playerid) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxStopRecording(playerid)));
            return 1;
        }

        @Override
        public int onAmxToggleControllable(int playerid, int toggle) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxToggleControllable(playerid, toggle)));
            return 1;
        }

        @Override
        public int onAmxTextDrawCreate(float x, float y, String text, int id) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxTextDrawCreate(x, y, text, id)));
            return 1;
        }

        @Override
        public int onAmxTextDrawDestroy(int id) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxTextDrawDestroy(id)));
            return 1;
        }

        @Override
        public int onAmxTextDrawSetString(int id, String text) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxTextDrawSetString(id, text)));
            return 1;
        }

        @Override
        public int onAmxTextDrawShowForPlayer(int playerid, int id) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxTextDrawShowForPlayer(playerid, id)));
            return 1;
        }

        @Override
        public int onAmxTextDrawHideForPlayer(int playerid, int id) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxTextDrawHideForPlayer(playerid, id)));
            return 1;
        }

        @Override
        public int onAmxTextDrawShowForAll(int id) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxTextDrawShowForAll(id)));
            return 1;
        }

        @Override
        public int onAmxTextDrawHideForAll(int id) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxTextDrawHideForAll(id)));
            return 1;
        }

        @Override
        public int onAmxCreatePlayerTextDraw(int playerid, float x, float y, String text, int id) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxCreatePlayerTextDraw(playerid, x, y, text, id)));
            return 1;
        }

        @Override
        public int onAmxPlayerTextDrawDestroy(int playerid, int id) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxPlayerTextDrawDestroy(playerid, id)));
            return 1;
        }

        @Override
        public int onAmxPlayerTextDrawSetString(int playerid, int id, String text) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxPlayerTextDrawSetString(playerid, id, text)));
            return 1;
        }

        @Override
        public int onAmxPlayerTextDrawShow(int playerid, int id) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxPlayerTextDrawShow(playerid, id)));
            return 1;
        }

        @Override
        public int onAmxPlayerTextDrawHide(int playerid, int id) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxPlayerTextDrawHide(playerid, id)));
            return 1;
        }

        @Override
        public int onAmxAddVehicleComponent(int vehicleid, int componentid) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxAddVehicleComponent(vehicleid, componentid)));
            return 1;
        }

        @Override
        public int onAmxLinkVehicleToInterior(int vehicleid, int interiorid) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxLinkVehicleToInterior(vehicleid, interiorid)));
            return 1;
        }

        @Override
        public int onAmxChangeVehicleColor(int vehicleid, int color1, int color2) {
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> handler.onAmxChangeVehicleColor(vehicleid, color1, color2)));
            return 1;
        }

        @Override
        public int[] onHookCall(String name, Object... objects) {
            Object[] event = {new int[]{0, 0}};
            callbackHandlers.stream().filter(SampCallbackHandler::isActive).forEach(handler -> TryUtils.tryTo(() -> event[0] = handler.onHookCall(name, objects)));
            return (int[]) event[0];
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
