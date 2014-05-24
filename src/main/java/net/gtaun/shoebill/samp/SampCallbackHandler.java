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
 *
 *
 * @author MK124
 */
public interface SampCallbackHandler
{
	default void onAmxLoad(int handle)
	{

	}

	default void onAmxUnload(int handle)
	{

	}

	default void onProcessTick()
	{

	}

	default int onGameModeInit()
	{
		return 1;
	}

	default int onGameModeExit()
	{
		return 1;
	}

	default int onPlayerConnect(int playerId)
	{
		return 1;
	}

	default int onPlayerDisconnect(int playerId, int reason)
	{
		return 1;
	}

	default int onPlayerSpawn(int playerId)
	{
		return 1;
	}

	default int onPlayerDeath(int playerId, int killerId, int reason)
	{
		return 1;
	}

	default int onVehicleSpawn(int vehicleId)
	{
		return 1;
	}

	default int onVehicleDeath(int vehicleId, int killerId)
	{
		return 1;
	}

	default int onPlayerText(int playerId, String text)
	{
		return 1;
	}

	default int onPlayerCommandText(int playerId, String cmdtext)
	{
		return 0;
	}

	default int onPlayerRequestClass(int playerId, int classId)
	{
		return 1;
	}

	default int onPlayerEnterVehicle(int playerId, int vehicleId, int isPassenger)
	{
		return 1;
	}

	default int onPlayerExitVehicle(int playerId, int vehicleId)
	{
		return 1;
	}

	default int onPlayerStateChange(int playerId, int state, int oldState)
	{
		return 1;
	}

	default int onPlayerEnterCheckpoint(int playerId)
	{
		return 1;
	}

	default int onPlayerLeaveCheckpoint(int playerId)
	{
		return 1;
	}

	default int onPlayerEnterRaceCheckpoint(int playerId)
	{
		return 1;
	}

	default int onPlayerLeaveRaceCheckpoint(int playerId)
	{
		return 1;
	}

	default int onRconCommand(String cmd)
	{
		return 0;
	}

	default int onPlayerRequestSpawn(int playerId)
	{
		return 1;
	}

	default int onObjectMoved(int objectId)
	{
		return 1;
	}

	default int onPlayerObjectMoved(int playerId, int objectId)
	{
		return 1;
	}

	default int onPlayerPickUpPickup(int playerId, int pickupId)
	{
		return 1;
	}

	default int onVehicleMod(int playerId, int vehicleId, int componentId)
	{
		return 1;
	}

	default int onEnterExitModShop(int playerId, int enterexit, int interiorId)
	{
		return 1;
	}

	default int onVehiclePaintjob(int playerId, int vehicleId, int paintjobId)
	{
		return 1;
	}

	default int onVehicleRespray(int playerId, int vehicleId, int color1, int color2)
	{
		return 1;
	}

	default int onVehicleDamageStatusUpdate(int vehicleId, int playerId)
	{
		return 1;
	}

	default int onUnoccupiedVehicleUpdate(int vehicleId, int playerId, int passengerSeat, float newX, float newY, float newZ)
	{
		return 1;
	}

	default int onPlayerSelectedMenuRow(int playerId, int row)
	{
		return 1;
	}

	default int onPlayerExitedMenu(int playerId)
	{
		return 1;
	}

	default int onPlayerInteriorChange(int playerId, int interiorId, int oldInteriorId)
	{
		return 1;
	}

	default int onPlayerKeyStateChange(int playerId, int keys, int oldKeys)
	{
		return 1;
	}

	default int onRconLoginAttempt(String ip, String password, int isSuccess)
	{
		return 1;
	}

	default int onPlayerUpdate(int playerId)
	{
		return 1;
	}

	default int onPlayerStreamIn(int playerId, int forPlayerId)
	{
		return 1;
	}

	default int onPlayerStreamOut(int playerId, int forPlayerId)
	{
		return 1;
	}

	default int onVehicleStreamIn(int vehicleId, int forPlayerId)
	{
		return 1;
	}

	default int onVehicleStreamOut(int vehicleId, int forPlayerId)
	{
		return 1;
	}

	default int onDialogResponse(int playerId, int dialogId, int response, int listitem, String inputtext)
	{
		return 0;
	}

	default int onPlayerTakeDamage(int playerid, int issuerId, float amount, int weaponid, int bodypart)
	{
		return 1;
	}

	default int onPlayerGiveDamage(int playerId, int damagedId, float amount, int weaponId, int bodypart)
	{
		return 1;
	}

	default int onPlayerClickMap(int playerId, float x, float y, float z)
	{
		return 1;
	}

	default int onPlayerClickTextDraw(int playerid, int clickedid)
	{
		return 0;
	}

	default int onPlayerClickPlayerTextDraw(int playerid, int playertextid)
	{
		return 0;
	}

	default int onPlayerClickPlayer(int playerId, int clickedPlayerId, int source)
	{
		return 1;
	}

	default int onPlayerEditObject(int playerid, int playerobject, int objectid, int response, float fX, float fY, float fZ, float fRotX, float fRotY, float fRotZ)
	{
		return 1;
	}

	default int onPlayerEditAttachedObject(int playerid, int response, int index, int modelid, int boneid, float fOffsetX, float fOffsetY, float fOffsetZ, float fRotX, float fRotY, float fRotZ, float fScaleX, float fScaleY, float fScaleZ)
	{
		return 1;
	}

	default int onPlayerSelectObject(int playerid, int type, int objectid, int modelid, float fX, float fY, float fZ)
	{
		return 1;
	}

	default int onPlayerWeaponShot(int playerid, int weaponid, int hittype, int hitid, float fX, float fY, float fZ)
	{
		return 1;
	}

	default int onIncomingConnection(int playerid, String ipAddress, int port)
	{
		return 1;
	}
}
