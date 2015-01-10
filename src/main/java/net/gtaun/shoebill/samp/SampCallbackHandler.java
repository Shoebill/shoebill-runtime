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

    default int onGameModeInit() {
        return 1;
    }

    default int onGameModeExit() {
        return 1;
    }

    default int onPlayerConnect(int playerId) {
        return 1;
    }

    default int onPlayerDisconnect(int playerId, int reason) {
        return 1;
    }

    default int onPlayerSpawn(int playerId) {
        return 1;
    }

    default int onPlayerDeath(int playerId, int killerId, int reason) {
        return 1;
    }

    default int onVehicleSpawn(int vehicleId) {
        return 1;
    }

    default int onVehicleDeath(int vehicleId, int killerId) {
        return 1;
    }

    default int onPlayerText(int playerId, String text) {
        return 1;
    }

    default int onPlayerCommandText(int playerId, String cmdtext) {
        return 0;
    }

    default int onPlayerRequestClass(int playerId, int classId) {
        return 1;
    }

    default int onPlayerEnterVehicle(int playerId, int vehicleId, int isPassenger) {
        return 1;
    }

    default int onPlayerExitVehicle(int playerId, int vehicleId) {
        return 1;
    }

    default int onPlayerStateChange(int playerId, int state, int oldState) {
        return 1;
    }

    default int onPlayerEnterCheckpoint(int playerId) {
        return 1;
    }

    default int onPlayerLeaveCheckpoint(int playerId) {
        return 1;
    }

    default int onPlayerEnterRaceCheckpoint(int playerId) {
        return 1;
    }

    default int onPlayerLeaveRaceCheckpoint(int playerId) {
        return 1;
    }

    default int onRconCommand(String cmd) {
        return 0;
    }

    default int onPlayerRequestSpawn(int playerId) {
        return 1;
    }

    default int onObjectMoved(int objectId) {
        return 1;
    }

    default int onPlayerObjectMoved(int playerId, int objectId) {
        return 1;
    }

    default int onPlayerPickUpPickup(int playerId, int pickupId) {
        return 1;
    }

    default int onVehicleMod(int playerId, int vehicleId, int componentId) {
        return 1;
    }

    default int onEnterExitModShop(int playerId, int enterexit, int interiorId) {
        return 1;
    }

    default int onVehiclePaintjob(int playerId, int vehicleId, int paintjobId) {
        return 1;
    }

    default int onVehicleRespray(int playerId, int vehicleId, int color1, int color2) {
        return 1;
    }

    default int onVehicleDamageStatusUpdate(int vehicleId, int playerId) {
        return 1;
    }

    default int onUnoccupiedVehicleUpdate(int vehicleId, int playerId, int passengerSeat, float newX, float newY, float newZ, float vel_x, float vel_y, float vel_z) {
        return 1;
    }

    default int onPlayerSelectedMenuRow(int playerId, int row) {
        return 1;
    }

    default int onPlayerExitedMenu(int playerId) {
        return 1;
    }

    default int onPlayerInteriorChange(int playerId, int interiorId, int oldInteriorId) {
        return 1;
    }

    default int onPlayerKeyStateChange(int playerId, int keys, int oldKeys) {
        return 1;
    }

    default int onRconLoginAttempt(String ip, String password, int isSuccess) {
        return 1;
    }

    default int onPlayerUpdate(int playerId) {
        return 1;
    }

    default int onPlayerStreamIn(int playerId, int forPlayerId) {
        return 1;
    }

    default int onPlayerStreamOut(int playerId, int forPlayerId) {
        return 1;
    }

    default int onVehicleStreamIn(int vehicleId, int forPlayerId) {
        return 1;
    }

    default int onVehicleStreamOut(int vehicleId, int forPlayerId) {
        return 1;
    }

    default int onDialogResponse(int playerId, int dialogId, int response, int listitem, String inputtext) {
        return 0;
    }

    default int onPlayerTakeDamage(int playerid, int issuerId, float amount, int weaponid, int bodypart) {
        return 1;
    }

    default int onPlayerGiveDamage(int playerId, int damagedId, float amount, int weaponId, int bodypart) {
        return 1;
    }

    default int onPlayerClickMap(int playerId, float x, float y, float z) {
        return 1;
    }

    default int onPlayerClickTextDraw(int playerid, int clickedid) {
        return 0;
    }

    default int onPlayerClickPlayerTextDraw(int playerid, int playertextid) {
        return 0;
    }

    default int onPlayerClickPlayer(int playerId, int clickedPlayerId, int source) {
        return 1;
    }

    default int onPlayerEditObject(int playerid, int playerobject, int objectid, int response, float fX, float fY, float fZ, float fRotX, float fRotY, float fRotZ) {
        return 1;
    }

    default int onPlayerEditAttachedObject(int playerid, int response, int index, int modelid, int boneid, float fOffsetX, float fOffsetY, float fOffsetZ, float fRotX, float fRotY, float fRotZ, float fScaleX, float fScaleY, float fScaleZ) {
        return 1;
    }

    default int onPlayerSelectObject(int playerid, int type, int objectid, int modelid, float fX, float fY, float fZ) {
        return 1;
    }

    default int onPlayerWeaponShot(int playerid, int weaponid, int hittype, int hitid, float fX, float fY, float fZ) {
        return 1;
    }

    default int onIncomingConnection(int playerid, String ipAddress, int port) {
        return 1;
    }

    default int onTrailerUpdate(int playerid, int vehicleid) {
        return 1;
    }

    default int onAmxVehicleCreated(int vehicleid, int modelid, float x, float y, float z, float angle, int interiorid, int worldid, int color1, int color2, int respawn_delay) {
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
}
