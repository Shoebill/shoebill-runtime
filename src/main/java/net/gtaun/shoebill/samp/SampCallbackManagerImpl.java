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

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.gtaun.shoebill.util.TryUtils;

/**
 *
 *
 * @author MK124
 */
public class SampCallbackManagerImpl implements SampCallbackManager
{
	private Queue<SampCallbackHandler> callbackHandlers;


	public SampCallbackManagerImpl()
	{
		callbackHandlers = new ConcurrentLinkedQueue<>();
	}

	@Override
	public void registerCallbackHandler(SampCallbackHandler handler)
	{
		callbackHandlers.add(handler);
	}

	@Override
	public void unregisterCallbackHandler(SampCallbackHandler handler)
	{
		callbackHandlers.remove(handler);
	}

	@Override
	public boolean hasCallbackHandler(SampCallbackHandler handler)
	{
		return callbackHandlers.contains(handler);
	}

	@Override
	public SampCallbackHandler getMasterCallbackHandler()
	{
		return callbackHandler;
	}

	private SampCallbackHandler callbackHandler = new SampCallbackHandler()
	{
		@Override
		public void onProcessTick()
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onProcessTick());
		}

		@Override
		public void onAmxLoad(int handle)
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onAmxLoad(handle));
		}

		@Override
		public void onAmxUnload(int handle)
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onAmxUnload(handle));
		}

		@Override
		public int onGameModeInit()
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onGameModeInit());
			return 1;
		}

		@Override
		public int onGameModeExit()
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onGameModeExit());
			return 1;
		}

		@Override
		public int onPlayerConnect(int playerid)
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onPlayerConnect(playerid));
			return 1;
		}

		@Override
		public int onPlayerDisconnect(int playerid, int reason)
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onPlayerDisconnect(playerid, reason));
			return 1;
		}

		@Override
		public int onPlayerSpawn(int playerid)
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onPlayerSpawn(playerid));
			return 1;
		}

		@Override
		public int onPlayerDeath(int playerid, int killerid, int reason)
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onPlayerDeath(playerid, killerid, reason));
			return 1;
		}

		@Override
		public int onVehicleSpawn(int vehicleid)
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onVehicleSpawn(vehicleid));
			return 1;
		}

		@Override
		public int onVehicleDeath(int vehicleid, int killerid)
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onVehicleDeath(vehicleid, killerid));
			return 1;
		}

		@Override
		public int onPlayerText(int playerid, String text)
		{
			int[] ret = {1};
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> ret[0] &= handler.onPlayerText(playerid, text));
			return ret[0];
		}

		@Override
		public int onPlayerCommandText(int playerid, String cmdtext)
		{
			int[] ret = {1};
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> ret[0] |= handler.onPlayerCommandText(playerid, cmdtext));
			return ret[0];
		}

		@Override
		public int onPlayerRequestClass(int playerid, int classid)
		{
			int[] ret = {1};
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> ret[0] &= handler.onPlayerRequestClass(playerid, classid));
			return ret[0];
		}

		@Override
		public int onPlayerEnterVehicle(int playerid, int vehicleid, int ispassenger)
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onPlayerEnterVehicle(playerid, vehicleid, ispassenger));
			return 1;
		}

		@Override
		public int onPlayerExitVehicle(int playerid, int vehicleid)
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onPlayerExitVehicle(playerid, vehicleid));
			return 1;
		}

		@Override
		public int onPlayerStateChange(int playerid, int newstate, int oldstate)
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onPlayerStateChange(playerid, newstate, oldstate));
			return 1;
		}

		@Override
		public int onPlayerEnterCheckpoint(int playerid)
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onPlayerEnterCheckpoint(playerid));
			return 1;
		}

		@Override
		public int onPlayerLeaveCheckpoint(int playerid)
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onPlayerLeaveCheckpoint(playerid));
			return 1;
		}

		@Override
		public int onPlayerEnterRaceCheckpoint(int playerid)
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onPlayerEnterRaceCheckpoint(playerid));
			return 1;
		}

		@Override
		public int onPlayerLeaveRaceCheckpoint(int playerid)
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onPlayerLeaveRaceCheckpoint(playerid));
			return 1;
		}

		@Override
		public int onRconCommand(String cmd)
		{
			int[] ret = {0};
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> ret[0] |= handler.onRconCommand(cmd));
			return ret[0];
		}

		@Override
		public int onPlayerRequestSpawn(int playerid)
		{
			int[] ret = {1};
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> ret[0] &= handler.onPlayerRequestSpawn(playerid));
			return ret[0];
		}

		@Override
		public int onObjectMoved(int objectid)
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onObjectMoved(objectid));
			return 1;
		}

		@Override
		public int onPlayerObjectMoved(int playerid, int objectid)
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onPlayerObjectMoved(playerid, objectid));
			return 1;
		}

		@Override
		public int onPlayerPickUpPickup(int playerid, int pickupid)
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onPlayerPickUpPickup(playerid, pickupid));
			return 1;
		}

		@Override
		public int onVehicleMod(int playerid, int vehicleid, int componentid)
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onVehicleMod(playerid, vehicleid, componentid));
			return 1;
		}

		@Override
		public int onEnterExitModShop(int playerid, int enterexit, int interiorid)
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onEnterExitModShop(playerid, enterexit, interiorid));
			return 1;
		}

		@Override
		public int onVehiclePaintjob(int playerid, int vehicleid, int paintjobid)
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onVehiclePaintjob(playerid, vehicleid, paintjobid));
			return 1;
		}

		@Override
		public int onVehicleRespray(int playerid, int vehicleid, int color1, int color2)
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onVehicleRespray(playerid, vehicleid, color1, color2));
			return 1;
		}

		@Override
		public int onVehicleDamageStatusUpdate(int vehicleid, int playerid)
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onVehicleDamageStatusUpdate(vehicleid, playerid));
			return 1;
		}

		@Override
		public int onUnoccupiedVehicleUpdate(int vehicleid, int playerid, int passengerSeat, float newX, float newY, float newZ)
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onUnoccupiedVehicleUpdate(vehicleid, playerid, passengerSeat, newX, newY, newZ));
			return 1;
		}

		@Override
		public int onPlayerSelectedMenuRow(int playerid, int row)
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onPlayerSelectedMenuRow(playerid, row));
			return 1;
		}

		@Override
		public int onPlayerExitedMenu(int playerid)
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onPlayerExitedMenu(playerid));
			return 1;
		}

		@Override
		public int onPlayerInteriorChange(int playerid, int newinteriorid, int oldinteriorid)
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onPlayerInteriorChange(playerid, newinteriorid, oldinteriorid));
			return 1;
		}

		@Override
		public int onPlayerKeyStateChange(int playerid, int newkeys, int oldkeys)
		{
			int[] ret = {1};
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> ret[0] &= handler.onPlayerKeyStateChange(playerid, newkeys, oldkeys));
			return ret[0];
		}

		@Override
		public int onRconLoginAttempt(String ip, String password, int success)
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onRconLoginAttempt(ip, password, success));
			return 1;
		}

		@Override
		public int onPlayerUpdate(int playerid)
		{
			int[] ret = {1};
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> ret[0] &= handler.onPlayerUpdate(playerid));
			return ret[0];
		}

		@Override
		public int onPlayerStreamIn(int playerid, int forplayerid)
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onPlayerStreamIn(playerid, forplayerid));
			return 1;
		}

		@Override
		public int onPlayerStreamOut(int playerid, int forplayerid)
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onPlayerStreamOut(playerid, forplayerid));
			return 1;
		}

		@Override
		public int onVehicleStreamIn(int vehicleid, int forplayerid)
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onVehicleStreamIn(vehicleid, forplayerid));
			return 1;
		}

		@Override
		public int onVehicleStreamOut(int vehicleid, int forplayerid)
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onVehicleStreamOut(vehicleid, forplayerid));
			return 1;
		}

		@Override
		public int onDialogResponse(int playerid, int dialogid, int response, int listitem, String inputtext)
		{
			int[] ret = {0};
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> ret[0] |= handler.onDialogResponse(playerid, dialogid, response, listitem, inputtext));
			return ret[0];
		}

		@Override
		public int onPlayerTakeDamage(int playerId, int issuerId, float amount, int weaponId, int bodypart)
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onPlayerTakeDamage(playerId, issuerId, amount, weaponId, bodypart));
			return 1;
		}

		@Override
		public int onPlayerGiveDamage(int playerId, int damagedId, float amount, int weaponId, int bodypart)
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onPlayerGiveDamage(playerId, damagedId, amount, weaponId, bodypart));
			return 1;
		}

		@Override
		public int onPlayerClickMap(int playerId, float x, float y, float z)
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onPlayerClickMap(playerId, x, y, z));
			return 1;
		}

		@Override
		public int onPlayerClickTextDraw(int playerid, int clickedid)
		{
			int[] ret = {0};
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> ret[0] |= handler.onPlayerClickPlayerTextDraw(playerid, clickedid));
			return ret[0];
		}

		@Override
		public int onPlayerClickPlayerTextDraw(int playerid, int playertextid)
		{
			int[] ret = {0};
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> ret[0] |= handler.onPlayerClickPlayerTextDraw(playerid, playertextid));
			return ret[0];
		}

		@Override
		public int onPlayerClickPlayer(int playerid, int clickedplayerid, int source)
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onPlayerClickPlayer(playerid, clickedplayerid, source));
			return 1;
		}

		@Override
		public int onPlayerEditObject(int playerid, int playerobject, int objectid, int response, float fX, float fY, float fZ, float fRotX, float fRotY, float fRotZ)
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onPlayerEditObject(playerid, playerobject, objectid, response, fX, fY, fZ, fRotX, fRotY, fRotZ));
			return 1;
		}

		@Override
		public int onPlayerEditAttachedObject(int playerid, int response, int index, int modelid, int boneid, float fOffsetX, float fOffsetY, float fOffsetZ, float fRotX, float fRotY, float fRotZ, float fScaleX, float fScaleY, float fScaleZ)
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onPlayerEditAttachedObject(playerid, response, index, modelid, boneid, fOffsetX, fOffsetY, fOffsetZ, fRotX, fRotY, fRotZ, fScaleX, fScaleY, fScaleZ));
			return 1;
		}

		@Override
		public int onPlayerSelectObject(int playerid, int type, int objectid, int modelid, float fX, float fY, float fZ)
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onPlayerSelectObject(playerid, type, objectid, modelid, fX, fY, fZ));
			return 1;
		}

		@Override
		public int onPlayerWeaponShot(int playerid, int weaponid, int hittype, int hitid, float fX, float fY, float fZ)
		{
			int[] ret = {1};
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> ret[0] &= handler.onPlayerWeaponShot(playerid, weaponid, hittype, hitid, fX, fY, fZ));
			return ret[0];
		}

		@Override
		public int onIncomingConnection(int playerid, String ipAddress, int port)
		{
			for (SampCallbackHandler handler : callbackHandlers) TryUtils.tryTo(() -> handler.onIncomingConnection(playerid, ipAddress, port));
			return 1;
		}
	};
}
