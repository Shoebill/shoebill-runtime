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

package net.gtaun.shoebill.samp;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author MK124
 * 
 */

public class SampCallbackManagerImpl implements SampCallbackManager
{
	private Queue<SampCallbackHandler> callbackHandlers;
	

	public SampCallbackManagerImpl()
	{
		callbackHandlers = new ConcurrentLinkedQueue<>();
	}

	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
	
	@Override
	public void registerCallbackHandler( SampCallbackHandler handler )
	{
		callbackHandlers.add( handler );
	}

	@Override
	public void unregisterCallbackHandler( SampCallbackHandler handler )
	{
		callbackHandlers.remove( handler );
	}

	@Override
	public boolean hasCallbackHandler( SampCallbackHandler handler )
	{
		return callbackHandlers.contains( handler );
	}

	@Override
	public SampCallbackHandler getMasterCallbackHandler()
	{
		return callbackHandler;
	}
	

	private SampCallbackHandler callbackHandler = new SampCallbackHandler()
	{
		@Override
		public int onGameModeInit()
		{
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				handler.onGameModeInit();
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return 1;
		}

		@Override
		public int onGameModeExit()
		{
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				handler.onGameModeExit();
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return 1;
		}

		@Override
		public int onPlayerConnect( int playerid )
		{
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				handler.onPlayerConnect( playerid );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return 1;
		}

		@Override
		public int onPlayerDisconnect( int playerid, int reason )
		{
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				handler.onPlayerDisconnect( playerid, reason );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return 1;
		}

		@Override
		public int onPlayerSpawn( int playerid )
		{
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				handler.onPlayerSpawn( playerid );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return 1;
		}

		@Override
		public int onPlayerDeath( int playerid, int killerid, int reason )
		{
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				handler.onPlayerDeath( playerid, killerid, reason );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return 1;
		}

		@Override
		public int onVehicleSpawn( int vehicleid )
		{
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				handler.onVehicleSpawn( vehicleid );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return 1;
		}

		@Override
		public int onVehicleDeath( int vehicleid, int killerid )
		{
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				handler.onVehicleDeath( vehicleid, killerid );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return 1;
		}

		@Override
		public int onPlayerText( int playerid, String text )
		{
			int ret = 1;
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				ret &= handler.onPlayerText( playerid, text );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return ret;
		}

		@Override
		public int onPlayerCommandText( int playerid, String cmdtext )
		{
			int ret = 0;
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				ret |= handler.onPlayerCommandText( playerid, cmdtext );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return ret;
		}

		@Override
		public int onPlayerRequestClass( int playerid, int classid )
		{
			int ret = 1;
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				ret &= handler.onPlayerRequestClass( playerid, classid );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return ret;
		}

		@Override
		public int onPlayerEnterVehicle( int playerid, int vehicleid, int ispassenger )
		{
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				handler.onPlayerEnterVehicle( playerid, vehicleid, ispassenger );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return 1;
		}

		@Override
		public int onPlayerExitVehicle( int playerid, int vehicleid )
		{
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				handler.onPlayerExitVehicle( playerid, vehicleid );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return 1;
		}

		@Override
		public int onPlayerStateChange( int playerid, int newstate, int oldstate )
		{
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				handler.onPlayerStateChange( playerid, newstate, oldstate );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return 1;
		}

		@Override
		public int onPlayerEnterCheckpoint( int playerid )
		{
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				handler.onPlayerEnterCheckpoint( playerid );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return 1;
		}

		@Override
		public int onPlayerLeaveCheckpoint( int playerid )
		{
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				handler.onPlayerLeaveCheckpoint( playerid );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return 1;
		}

		@Override
		public int onPlayerEnterRaceCheckpoint( int playerid )
		{
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				handler.onPlayerEnterRaceCheckpoint( playerid );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return 1;
		}

		@Override
		public int onPlayerLeaveRaceCheckpoint( int playerid )
		{
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				handler.onPlayerLeaveRaceCheckpoint( playerid );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return 1;
		}

		@Override
		public int onRconCommand( String cmd )
		{
			int ret = 0;
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				ret |= handler.onRconCommand( cmd );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return ret;
		}

		@Override
		public int onPlayerRequestSpawn( int playerid )
		{
			int ret = 1;
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				ret &= handler.onPlayerRequestSpawn( playerid );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return ret;
		}

		@Override
		public int onObjectMoved( int objectid )
		{
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				handler.onObjectMoved( objectid );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return 1;
		}

		@Override
		public int onPlayerObjectMoved( int playerid, int objectid )
		{
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				handler.onPlayerObjectMoved( playerid, objectid );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return 1;
		}
		
		@Override
		public int onPlayerPickUpPickup( int playerid, int pickupid )
		{
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				handler.onPlayerPickUpPickup( playerid, pickupid );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return 1;
		}

		@Override
		public int onVehicleMod( int playerid, int vehicleid, int componentid )
		{
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				handler.onVehicleMod( playerid, vehicleid, componentid );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return 1;
		}

		@Override
		public int onEnterExitModShop( int playerid, int enterexit, int interiorid )
		{
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				handler.onEnterExitModShop( playerid, enterexit, interiorid );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return 1;
		}

		@Override
		public int onVehiclePaintjob( int playerid, int vehicleid, int paintjobid )
		{
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				handler.onVehiclePaintjob( playerid, vehicleid, paintjobid );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return 1;
		}

		@Override
		public int onVehicleRespray( int playerid, int vehicleid, int color1, int color2 )
		{
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				handler.onVehicleRespray( playerid, vehicleid, color1, color2 );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return 1;
		}

		@Override
		public int onVehicleDamageStatusUpdate( int vehicleid, int playerid )
		{
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				handler.onVehicleDamageStatusUpdate( vehicleid, playerid );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return 1;
		}

		@Override
		public int onUnoccupiedVehicleUpdate( int vehicleid, int playerid, int passenger_seat )
		{
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				handler.onUnoccupiedVehicleUpdate( vehicleid, playerid, passenger_seat );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return 1;
		}

		@Override
		public int onPlayerSelectedMenuRow( int playerid, int row )
		{
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				handler.onPlayerSelectedMenuRow( playerid, row );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return 1;
		}

		@Override
		public int onPlayerExitedMenu( int playerid )
		{
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				handler.onPlayerExitedMenu( playerid );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return 1;
		}

		@Override
		public int onPlayerInteriorChange( int playerid, int newinteriorid, int oldinteriorid )
		{
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				handler.onPlayerInteriorChange( playerid, newinteriorid, oldinteriorid );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return 1;
		}

		@Override
		public int onPlayerKeyStateChange( int playerid, int newkeys, int oldkeys )
		{
			int ret = 1;
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				ret &= handler.onPlayerKeyStateChange( playerid, newkeys, oldkeys );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return ret;
		}

		@Override
		public int onRconLoginAttempt( String ip, String password, int success )
		{
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				handler.onRconLoginAttempt( ip, password, success );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return 1;
		}

		@Override
		public int onPlayerUpdate( int playerid )
		{
			int ret = 1;
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				ret &= handler.onPlayerUpdate( playerid );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return ret;
		}

		@Override
		public int onPlayerStreamIn( int playerid, int forplayerid )
		{
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				handler.onPlayerStreamIn( playerid, forplayerid );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return 1;
		}

		@Override
		public int onPlayerStreamOut( int playerid, int forplayerid )
		{
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				handler.onPlayerStreamOut( playerid, forplayerid );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return 1;
		}

		@Override
		public int onVehicleStreamIn( int vehicleid, int forplayerid )
		{
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				handler.onVehicleStreamIn( vehicleid, forplayerid );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return 1;
		}

		@Override
		public int onVehicleStreamOut( int vehicleid, int forplayerid )
		{
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				handler.onVehicleStreamOut( vehicleid, forplayerid );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return 1;
		}

		@Override
		public int onDialogResponse( int playerid, int dialogid, int response, int listitem, String inputtext )
		{
			int ret = 0;
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				ret |= handler.onDialogResponse( playerid, dialogid, response, listitem, inputtext );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return ret;
		}

		@Override
		public int onPlayerTakeDamage( int playerId, int issuerId, float amount, int weaponId )
		{
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				handler.onPlayerTakeDamage( playerId, issuerId, amount, weaponId );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return 1;
		}

		@Override
		public int onPlayerGiveDamage( int playerId, int damagedId, float amount, int weaponId )
		{
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				handler.onPlayerGiveDamage( playerId, damagedId, amount, weaponId );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return 1;
		}

		@Override
		public int onPlayerClickMap( int playerId, float x, float y, float z )
		{
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				handler.onPlayerClickMap( playerId, x, y, z );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return 1;
		}
		
		@Override
		public int onPlayerClickPlayer( int playerid, int clickedplayerid, int source )
		{
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				handler.onPlayerClickPlayer( playerid, clickedplayerid, source );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			return 1;
		}

		@Override
		public void onProcessTick()
		{
			for( SampCallbackHandler handler : callbackHandlers ) try
			{
				handler.onProcessTick();
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
		}
	};
}
