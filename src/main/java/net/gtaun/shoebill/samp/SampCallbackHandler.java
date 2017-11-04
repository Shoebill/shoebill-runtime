/**
 * Copyright (C) 2011-2014 MK124
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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

/**
 * @author MK124 & 123marvin123
 */
public interface SampCallbackHandler {

    default void onAmxLoad(int handle) {

    }

    default void onAmxUnload(int handle) {

    }

    default void onProcessTick() {

    }

    default void onShoebillUnload() {
    }

    default void onShoebillLoad() {
    }

    default boolean onGameModeInit() {
        return true;
    }

    default boolean onGameModeExit() {
        return true;
    }

    default boolean onPlayerConnect(int playerId) {
        return true;
    }

    default boolean onPlayerDisconnect(int playerId, int reason) {
        return true;
    }

    default boolean onPlayerSpawn(int playerId) {
        return true;
    }

    default boolean onPlayerDeath(int playerId, int killerId, int reason) {
        return true;
    }

    default boolean onVehicleSpawn(int vehicleId) {
        return true;
    }

    default boolean onVehicleDeath(int vehicleId, int killerId) {
        return true;
    }

    default int onPlayerText(int playerId, String text) {
        return 1;
    }

    default int onPlayerCommandText(int playerId, String cmdtext) {
        return 0;
    }

    default boolean onPlayerRequestClass(int playerId, int classId) {
        return true;
    }

    default boolean onPlayerEnterVehicle(int playerId, int vehicleId, int isPassenger) {
        return true;
    }

    default boolean onPlayerExitVehicle(int playerId, int vehicleId) {
        return true;
    }

    default boolean onPlayerStateChange(int playerId, int state, int oldState) {
        return true;
    }

    default boolean onPlayerEnterCheckpoint(int playerId) {
        return true;
    }

    default boolean onPlayerLeaveCheckpoint(int playerId) {
        return true;
    }

    default boolean onPlayerEnterRaceCheckpoint(int playerId) {
        return true;
    }

    default boolean onPlayerLeaveRaceCheckpoint(int playerId) { return true; }

    default int onRconCommand(String cmd) {
        return 0;
    }

    default boolean onPlayerRequestSpawn(int playerId) {
        return true;
    }

    default boolean onObjectMoved(int objectId) {
        return true;
    }

    default boolean onPlayerObjectMoved(int playerId, int objectId) {
        return true;
    }

    default boolean onPlayerPickUpPickup(int playerId, int pickupId) {
        return true;
    }

    default boolean onVehicleMod(int playerId, int vehicleId, int componentId) {
        return true;
    }

    default boolean onEnterExitModShop(int playerId, int enterexit, int interiorId) {
        return true;
    }

    default boolean onVehiclePaintjob(int playerId, int vehicleId, int paintjobId) {
        return true;
    }

    default boolean onVehicleRespray(int playerId, int vehicleId, int color1, int color2) {
        return true;
    }

    default boolean onVehicleDamageStatusUpdate(int vehicleId, int playerId) {
        return true;
    }

    default boolean onUnoccupiedVehicleUpdate(int vehicleId, int playerId, int passengerSeat, float newX, float newY, float newZ, float vel_x, float vel_y, float vel_z) {
        return true;
    }

    default boolean onPlayerSelectedMenuRow(int playerId, int row) {
        return true;
    }

    default boolean onPlayerExitedMenu(int playerId) {
        return true;
    }

    default boolean onPlayerInteriorChange(int playerId, int interiorId, int oldInteriorId) {
        return true;
    }

    default boolean onPlayerKeyStateChange(int playerId, int keys, int oldKeys) {
        return true;
    }

    default int onRconLoginAttempt(String ip, String password, int isSuccess) {
        return 1;
    }

    default boolean onPlayerUpdate(int playerId) {
        return true;
    }

    default boolean onPlayerStreamIn(int playerId, int forPlayerId) {
        return true;
    }

    default boolean onPlayerStreamOut(int playerId, int forPlayerId) {
        return true;
    }

    default boolean onVehicleStreamIn(int vehicleId, int forPlayerId) {
        return true;
    }

    default boolean onVehicleStreamOut(int vehicleId, int forPlayerId) {
        return true;
    }

    default int onDialogResponse(int playerId, int dialogId, int response, int listitem, String inputtext) {
        return 0;
    }

    default boolean onPlayerTakeDamage(int playerid, int issuerId, float amount, int weaponid, int bodypart) {
        return true;
    }

    default boolean onPlayerGiveDamage(int playerId, int damagedId, float amount, int weaponId, int bodypart) {
        return true;
    }

    default boolean onPlayerClickMap(int playerId, float x, float y, float z) {
        return true;
    }

    default boolean onPlayerClickTextDraw(int playerid, int clickedid) {
        return false;
    }

    default boolean onPlayerClickPlayerTextDraw(int playerid, int playertextid) {
        return false;
    }

    default boolean onPlayerClickPlayer(int playerId, int clickedPlayerId, int source) {
        return true;
    }

    default int onPlayerEditObject(int playerid, int playerobject, int objectid, int response, float fX, float fY, float fZ, float fRotX, float fRotY, float fRotZ) {
        return 1;
    }

    default boolean onPlayerEditAttachedObject(int playerid, int response, int index, int modelid, int boneid, float fOffsetX, float fOffsetY, float fOffsetZ, float fRotX, float fRotY, float fRotZ, float fScaleX, float fScaleY, float fScaleZ) {
        return true;
    }

    default boolean onPlayerSelectObject(int playerid, int type, int objectid, int modelid, float fX, float fY, float fZ) {
        return true;
    }

    default boolean onPlayerWeaponShot(int playerid, int weaponid, int hittype, int hitid, float fX, float fY, float fZ) {
        return true;
    }

    default int onIncomingConnection(int playerid, String ipAddress, int port) {
        return 1;
    }

    default boolean onTrailerUpdate(int playerid, int vehicleid) {
        return true;
    }

    default int[] onHookCall(String name, Object... objects) {
        return new int[]{0, 0};
    }

    default boolean onActorStreamIn(int actor, int playerid) {
        return true;
    }

    default boolean onActorStreamOut(int actor, int playerid) {
        return true;
    }

    default int onPlayerGiveDamageActor(int playerid, int actor, int amount, int weapon, int bodypart) {
        return 1;
    }

    default int onVehicleSirenStateChange(int playerid, int vehicleid, int newstate) {
        return 1;
    }

    default int onRegisteredFunctionCall(int amx, String name, Object[] parameters) { return 1; }
}
