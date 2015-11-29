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
    boolean isActive();

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
        return false;
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

    default int onAmxVehicleCreated(int vehicleid, int modelid, float x, float y, float z, float angle, int interiorid, int worldid, int color1, int color2, int respawn_delay, int addsiren) {
        return 1;
    }

    default int onAmxDestroyVehicle(int vehicleid) {
        return 1;
    }

    default int onAmxSampObjectCreated(int object_Id, int modelid, float x, float y, float z, float rX, float rY, float rZ, int world, int interior, float render_distance) {
        return 1;
    }

    default int onAmxSampObjectDestroyed(int object_id) {
        return 1;
    }

    default int onAmxAttachObjectToVehicle(int object_id, int vehicleid, float x, float y, float z, float rX, float rY, float rZ) {
        return 1;
    }

    default int onAmxAttachObjectToPlayer(int object_id, int playerid, float x, float y, float z, float rX, float rY, float rZ) {
        return 1;
    }

    default int onAmxAttachObjectToObject(int object_id, int other_object_id, float x, float y, float z, float rX, float rY, float rZ) {
        return 1;
    }

    default int onAmxCreatePlayerObject(int playerid, int modelid, float x, float y, float z, float rX, float rY, float rZ, int worldid, int interiorid, float drawdistance, int objectid) {
        return 1;
    }

    default int onAmxDestroyPlayerObject(int playerid, int objectid) {
        return 1;
    }

    default int onAmxSetPlayerAttachedObject(int playerid, int index, int modelid, int bone, float offsetX, float offsetY, float offsetZ, float rotX, float rotY, float rotZ, float scaleX, float scaleY, float scaleZ, int materialcolor1, int materialcolor2) {
        return 1;
    }

    default int onAmxRemovePlayerAttachedObject(int playerid, int index) {
        return 1;
    }

    default int onAmxDestroyPickup(int pickupid) {
        return 1;
    }

    default int onAmxCreatePickup(int model, int type, float posX, float posY, float posZ, int virtualworld, int id) {
        return 1;
    }

    default int onAmxAddStaticPickup(int model, int type, float posX, float posY, float posZ, int virtualworld) {
        return 1;
    }

    default int onAmxCreateLabel(String text, int color, float posX, float posY, float posZ, float drawDistance, int virtualWorld, int testLOS, int id) {
        return 1;
    }

    default int onAmxDeleteLabel(int labelid) {
        return 1;
    }

    default int onAmxUpdateLabel(int labelid, int color, String text) {
        return 1;
    }

    default int onAmxCreatePlayerLabel(int playerid, String text, int color, float posX, float posY, float posZ, float drawDistance, int attachedPlayer, int attachedVehicle, int testLOS, int id) {
        return 1;
    }

    default int onAmxDeletePlayerLabel(int playerid, int labelid) {
        return 1;
    }

    default int onAmxUpdatePlayerLabel(int playerid, int labelid, int color, String text) {
        return 1;
    }

    default int onAmxAttachLabelToPlayer(int labelid, int playerid, float offsetX, float offsetY, float offsetZ) {
        return 1;
    }

    default int onAmxAttachLabelToVehicle(int labelid, int vehicleid, float offsetX, float offsetY, float offsetZ) {
        return 1;
    }

    default int onAmxCreateMenu(String title, int columns, float x, float y, float col1Width, float col2Width, int id) {
        return 1;
    }

    default int onAmxSetMenuColumnHeader(int id, int column, String text) {
        return 1;
    }

    default int onAmxDestroyMenu(int id) {
        return 1;
    }

    default int onAmxGangZoneCreate(float minX, float minY, float maxX, float maxY, int id) {
        return 1;
    }

    default int onAmxGangZoneDestroy(int id) {
        return 1;
    }

    default int onAmxGangZoneShowForPlayer(int playerid, int zone, int color) {
        return 1;
    }

    default int onAmxGangZoneShowForAll(int zone, int color) {
        return 1;
    }

    default int onAmxGangZoneHideForPlayer(int playerid, int zone) {
        return 1;
    }

    default int onAmxGangZoneHideForAll(int zone) {
        return 1;
    }

    default int onAmxGangZoneFlashForPlayer(int playerid, int zone, int flashColor) {
        return 1;
    }

    default int onAmxGangZoneFlashForAll(int zone, int flashColor) {
        return 1;
    }

    default int onAmxGangZoneStopFlashForPlayer(int playerid, int zone) {
        return 1;
    }

    default int onAmxGangZoneStopFlashForAll(int zone) {
        return 1;
    }

    default int onAmxSetSkillLevel(int playerid, int skill, int level) {
        return 1;
    }

    default int onAmxSetPlayerMapIcon(int playerid, int iconid, float x, float y, float z, int markertype, int color, int style) {
        return 1;
    }

    default int onAmxRemovePlayerMapIcon(int playerid, int iconid) {
        return 1;
    }

    default int onAmxShowPlayerDialog(int playerid, int dialogid, int style, String caption, String info, String button1, String button2) {
        return 1;
    }

    default int onAmxSetPlayerWorldBounds(int playerid, float minX, float minY, float maxX, float maxY) {
        return 1;
    }

    default int onAmxSetPlayerWeather(int playerid, int weatherid) {
        return 1;
    }

    default int onAmxSetPlayerCheckpoint(int playerid, float x, float y, float z, float size) {
        return 1;
    }

    default int onAmxDisablePlayerCheckpoint(int playerid) {
        return 1;
    }

    default int onAmxSetPlayerRaceCheckpoint(int playerid, int type, float x, float y, float z, float nextX, float nextY, float nextZ, float size) {
        return 1;
    }

    default int onAmxDisablePlayerRaceCheckpoint(int playerid) {
        return 1;
    }

    default int onAmxTogglePlayerSpectating(int playerid, int toggle) {
        return 1;
    }

    default int onAmxPlayerSpectatePlayer(int playerid, int target, int mode) {
        return 1;
    }

    default int onAmxPlayerSpectateVehicle(int playerid, int target, int mode) {
        return 1;
    }

    default int onAmxEnableStuntBonusForPlayer(int playerid, int toggle) {
        return 1;
    }

    default int onAmxStartRecording(int playerid, int type, String recordName) {
        return 1;
    }

    default int onAmxStopRecording(int playerid) {
        return 1;
    }

    default int onAmxToggleControllable(int playerid, int toggle) {
        return 1;
    }

    default int onAmxTextDrawCreate(float x, float y, String text, int id) {
        return 1;
    }

    default int onAmxTextDrawDestroy(int id) {
        return 1;
    }

    default int onAmxTextDrawSetString(int id, String text) {
        return 1;
    }

    default int onAmxTextDrawShowForPlayer(int playerid, int id) {
        return 1;
    }

    default int onAmxTextDrawHideForPlayer(int playerid, int id) {
        return 1;
    }

    default int onAmxTextDrawShowForAll(int id) {
        return 1;
    }

    default int onAmxTextDrawHideForAll(int id) {
        return 1;
    }

    default int onAmxCreatePlayerTextDraw(int playerid, float x, float y, String text, int id) {
        return 1;
    }

    default int onAmxPlayerTextDrawDestroy(int playerid, int id) {
        return 1;
    }

    default int onAmxPlayerTextDrawSetString(int playerid, int id, String text) {
        return 1;
    }

    default int onAmxPlayerTextDrawShow(int playerid, int id) {
        return 1;
    }

    default int onAmxPlayerTextDrawHide(int playerid, int id) {
        return 1;
    }

    default int onAmxAddVehicleComponent(int vehicleid, int componentid) {
        return 1;
    }

    default int onAmxLinkVehicleToInterior(int vehicleid, int interiorid) {
        return 1;
    }

    default int onAmxChangeVehicleColor(int vehicleid, int color1, int color2) {
        return 1;
    }

    default int onAmxCreateActor(int id, int modelid, float x, float y, float z, float rotation) {
        return 1;
    }

    default int onAmxDestroyActor(int id) {
        return 1;
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

    default int onRegisteredFunctionCall(String name, Object[] parameters) { return 1; }
}
