/**
 * Copyright (C) 2011 MK124
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

package net.gtaun.shoebill;

import java.util.List;
import java.util.Vector;

import net.gtaun.shoebill.samp.ISampCallback;

/**
 * @author MK124
 * 
 */

public class SampCallbackManager implements ISampCallbackManager
{
	List<ISampCallback> callbackHandlers;


	public SampCallbackManager()
	{
		callbackHandlers = new Vector<ISampCallback>();
	}

	@Override
	public void registerCallbackHandler( ISampCallback handler )
	{
		callbackHandlers.add( handler );
	}

	@Override
	public void unregisterCallbackHandler( ISampCallback handler )
	{
		callbackHandlers.remove( handler );
	}

	@Override
	public boolean hasCallbackHandler( ISampCallback handler )
	{
		return callbackHandlers.contains( handler );
	}

	@Override
	public ISampCallback getMasterCallbackHandler()
	{
		return sampCallback;
	}
	

	ISampCallback sampCallback = new ISampCallback()
	{
		public int onGameModeInit()
		{
			for( ISampCallback handler : callbackHandlers ) handler.onGameModeInit();
			return 1;
		}

		public int onGameModeExit()
		{
			for( ISampCallback handler : callbackHandlers ) handler.onGameModeExit();
			return 1;
		}

		public int onPlayerConnect( int playerid )
		{
			for( ISampCallback handler : callbackHandlers ) handler.onGameModeExit();
			return 1;
		}

		public int onPlayerDisconnect( int playerid, int reason )
		{
			for( ISampCallback handler : callbackHandlers ) handler.onPlayerDisconnect( playerid, reason );
			return 1;
		}

		public int onPlayerSpawn( int playerid )
		{
			for( ISampCallback handler : callbackHandlers ) handler.onPlayerSpawn( playerid );
			return 1;
		}

		public int onPlayerDeath( int playerid, int killerid, int reason )
		{
			for( ISampCallback handler : callbackHandlers ) handler.onPlayerDeath( playerid, killerid, reason );
			return 1;
		}

		public int onVehicleSpawn( int vehicleid )
		{
			for( ISampCallback handler : callbackHandlers ) handler.onVehicleSpawn( vehicleid );
			return 1;
		}

		public int onVehicleDeath( int vehicleid, int killerid )
		{
			for( ISampCallback handler : callbackHandlers ) handler.onVehicleDeath( vehicleid, killerid );
			return 1;
		}

		public int onPlayerText( int playerid, String text )
		{
			int ret = 1;
			for( ISampCallback handler : callbackHandlers ) ret &= handler.onPlayerText( playerid, text );
			
			return ret;
		}

		public int onPlayerCommandText( int playerid, String cmdtext )
		{
			int ret = 0;
			for( ISampCallback handler : callbackHandlers ) ret |= handler.onPlayerCommandText( playerid, cmdtext );
			
			return ret;
		}

		public int onPlayerRequestClass( int playerid, int classid )
		{
			int ret = 1;
			for( ISampCallback handler : callbackHandlers ) ret &= handler.onPlayerRequestClass( playerid, classid );
			
			return ret;
		}

		public int onPlayerEnterVehicle( int playerid, int vehicleid, int ispassenger )
		{
			for( ISampCallback handler : callbackHandlers ) handler.onPlayerEnterVehicle( playerid, vehicleid, ispassenger );
			return 1;
		}

		public int onPlayerExitVehicle( int playerid, int vehicleid )
		{
			for( ISampCallback handler : callbackHandlers ) handler.onPlayerExitVehicle( playerid, vehicleid );
			return 1;
		}

		public int onPlayerStateChange( int playerid, int newstate, int oldstate )
		{
			for( ISampCallback handler : callbackHandlers ) handler.onPlayerStateChange( playerid, newstate, oldstate );
			return 1;
		}

		public int onPlayerEnterCheckpoint( int playerid )
		{
			for( ISampCallback handler : callbackHandlers ) handler.onPlayerEnterCheckpoint( playerid );
			return 1;
		}

		public int onPlayerLeaveCheckpoint( int playerid )
		{
			for( ISampCallback handler : callbackHandlers ) handler.onPlayerLeaveCheckpoint( playerid );
			return 1;
		}

		public int onPlayerEnterRaceCheckpoint( int playerid )
		{
			for( ISampCallback handler : callbackHandlers ) handler.onPlayerEnterRaceCheckpoint( playerid );
			return 1;
		}

		public int onPlayerLeaveRaceCheckpoint( int playerid )
		{
			for( ISampCallback handler : callbackHandlers ) handler.onPlayerLeaveRaceCheckpoint( playerid );
			return 1;
		}

		public int onRconCommand( String cmd )
		{
			int ret = 0;
			for( ISampCallback handler : callbackHandlers ) ret |= handler.onRconCommand( cmd );
			
			return ret;
		}

		public int onPlayerRequestSpawn( int playerid )
		{
			int ret = 1;
			for( ISampCallback handler : callbackHandlers ) ret &= handler.onPlayerRequestSpawn( playerid );
			
			return ret;
		}

		public int onObjectMoved( int objectid )
		{
			for( ISampCallback handler : callbackHandlers ) handler.onObjectMoved( objectid );
			return 1;
		}

		public int onPlayerObjectMoved( int playerid, int objectid )
		{
			for( ISampCallback handler : callbackHandlers ) handler.onPlayerObjectMoved( playerid, objectid );
			return 1;
		}
		
		public int onPlayerPickUpPickup( int playerid, int pickupid )
		{
			for( ISampCallback handler : callbackHandlers ) handler.onPlayerPickUpPickup( playerid, pickupid );
			return 1;
		}

		public int onVehicleMod( int playerid, int vehicleid, int componentid )
		{
			for( ISampCallback handler : callbackHandlers ) handler.onVehicleMod( playerid, vehicleid, componentid );
			return 1;
		}

		public int onEnterExitModShop( int playerid, int enterexit, int interiorid )
		{
			for( ISampCallback handler : callbackHandlers ) handler.onEnterExitModShop( playerid, enterexit, interiorid );
			return 1;
		}

		public int onVehiclePaintjob( int playerid, int vehicleid, int paintjobid )
		{
			for( ISampCallback handler : callbackHandlers ) handler.onVehiclePaintjob( playerid, vehicleid, paintjobid );
			return 1;
		}

		public int onVehicleRespray( int playerid, int vehicleid, int color1, int color2 )
		{
			for( ISampCallback handler : callbackHandlers ) handler.onVehicleRespray( playerid, vehicleid, color1, color2 );
			return 1;
		}

		public int onVehicleDamageStatusUpdate( int vehicleid, int playerid )
		{
			for( ISampCallback handler : callbackHandlers ) handler.onVehicleDamageStatusUpdate( vehicleid, playerid );
			return 1;
		}

		public int onUnoccupiedVehicleUpdate( int vehicleid, int playerid, int passenger_seat )
		{
			for( ISampCallback handler : callbackHandlers ) handler.onUnoccupiedVehicleUpdate( vehicleid, playerid, passenger_seat );
			return 1;
		}

		public int onPlayerSelectedMenuRow( int playerid, int row )
		{
			for( ISampCallback handler : callbackHandlers ) handler.onPlayerSelectedMenuRow( playerid, row );
			return 1;
		}

		public int onPlayerExitedMenu( int playerid )
		{
			for( ISampCallback handler : callbackHandlers ) handler.onPlayerExitedMenu( playerid );
			return 1;
		}

		public int onPlayerInteriorChange( int playerid, int newinteriorid, int oldinteriorid )
		{
			for( ISampCallback handler : callbackHandlers ) handler.onPlayerInteriorChange( playerid, newinteriorid, oldinteriorid );
			return 1;
		}

		public int onPlayerKeyStateChange( int playerid, int newkeys, int oldkeys )
		{
			int ret = 1;
			for( ISampCallback handler : callbackHandlers ) ret &= handler.onPlayerKeyStateChange( playerid, newkeys, oldkeys );
			
			return ret;
		}

		public int onRconLoginAttempt( String ip, String password, int success )
		{
			for( ISampCallback handler : callbackHandlers ) handler.onRconLoginAttempt( ip, password, success );
			return 1;
		}

		public int onPlayerUpdate( int playerid )
		{
			int ret = 1;
			for( ISampCallback handler : callbackHandlers ) ret &= handler.onPlayerUpdate( playerid );
			
			return ret;
		}

		public int onPlayerStreamIn( int playerid, int forplayerid )
		{
			for( ISampCallback handler : callbackHandlers ) handler.onPlayerStreamIn( playerid, forplayerid );
			return 1;
		}

		public int onPlayerStreamOut( int playerid, int forplayerid )
		{
			for( ISampCallback handler : callbackHandlers ) handler.onPlayerStreamOut( playerid, forplayerid );
			return 1;
		}

		public int onVehicleStreamIn( int vehicleid, int forplayerid )
		{
			for( ISampCallback handler : callbackHandlers ) handler.onVehicleStreamIn( vehicleid, forplayerid );
			return 1;
		}

		public int onVehicleStreamOut( int vehicleid, int forplayerid )
		{
			for( ISampCallback handler : callbackHandlers ) handler.onVehicleStreamOut( vehicleid, forplayerid );
			return 1;
		}

		public int onDialogResponse( int playerid, int dialogid, int response, int listitem, String inputtext )
		{
			int ret = 0;
			for( ISampCallback handler : callbackHandlers ) ret |= handler.onDialogResponse( playerid, dialogid, response, listitem, inputtext );
			
			return ret;
		}

		public int onPlayerClickPlayer( int playerid, int clickedplayerid, int source )
		{
			for( ISampCallback handler : callbackHandlers ) handler.onPlayerClickPlayer( playerid, clickedplayerid, source );
			return 1;
		}

		public void onProcessTick()
		{
			for( ISampCallback handler : callbackHandlers ) handler.onProcessTick();
		}
	};
}
