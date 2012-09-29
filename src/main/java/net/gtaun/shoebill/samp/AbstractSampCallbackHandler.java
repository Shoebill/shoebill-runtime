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

/**
 * @author MK124
 *
 */

public abstract class AbstractSampCallbackHandler implements SampCallbackHandler
{
	protected AbstractSampCallbackHandler()
	{
		
	}
	
	
	@Override
	public int onGameModeInit()
	{
		return 1;
	}

	@Override
	public int onGameModeExit()
	{
		return 1;
	}

	@Override
	public int onPlayerConnect( int playerid )
	{
		return 1;
	}

	@Override
	public int onPlayerDisconnect( int playerid, int reason )
	{
		return 1;
	}

	@Override
	public int onPlayerSpawn( int playerid )
	{
		return 1;
	}

	@Override
	public int onPlayerDeath( int playerid, int killerid, int reason )
	{
		return 1;
	}

	@Override
	public int onVehicleSpawn( int vehicleid )
	{
		return 1;
	}

	@Override
	public int onVehicleDeath( int vehicleid, int killerid )
	{
		return 1;
	}

	@Override
	public int onPlayerText( int playerid, String text )
	{
		return 1;
	}

	@Override
	public int onPlayerCommandText( int playerid, String cmdtext )
	{
		return 0;
	}

	@Override
	public int onPlayerRequestClass( int playerid, int classid )
	{
		return 1;
	}

	@Override
	public int onPlayerEnterVehicle( int playerid, int vehicleid, int ispassenger )
	{
		return 1;
	}

	@Override
	public int onPlayerExitVehicle( int playerid, int vehicleid )
	{
		return 1;
	}

	@Override
	public int onPlayerStateChange( int playerid, int newstate, int oldstate )
	{
		return 1;
	}

	@Override
	public int onPlayerEnterCheckpoint( int playerid )
	{
		return 1;
	}

	@Override
	public int onPlayerLeaveCheckpoint( int playerid )
	{
		return 1;
	}

	@Override
	public int onPlayerEnterRaceCheckpoint( int playerid )
	{
		return 1;
	}

	@Override
	public int onPlayerLeaveRaceCheckpoint( int playerid )
	{
		return 1;
	}

	@Override
	public int onRconCommand( String cmd )
	{
		return 0;
	}

	@Override
	public int onPlayerRequestSpawn( int playerid )
	{
		return 1;
	}

	@Override
	public int onObjectMoved( int objectid )
	{
		return 1;
	}

	@Override
	public int onPlayerObjectMoved( int playerid, int objectid )
	{
		return 1;
	}
	
	@Override
	public int onPlayerPickUpPickup( int playerid, int pickupid )
	{
		return 1;
	}

	@Override
	public int onVehicleMod( int playerid, int vehicleid, int componentid )
	{
		return 1;
	}

	@Override
	public int onEnterExitModShop( int playerid, int enterexit, int interiorid )
	{
		return 1;
	}

	@Override
	public int onVehiclePaintjob( int playerid, int vehicleid, int paintjobid )
	{
		return 1;
	}

	@Override
	public int onVehicleRespray( int playerid, int vehicleid, int color1, int color2 )
	{
		return 1;
	}

	@Override
	public int onVehicleDamageStatusUpdate( int vehicleid, int playerid )
	{
		return 1;
	}

	@Override
	public int onUnoccupiedVehicleUpdate( int vehicleid, int playerid, int passenger_seat )
	{
		return 1;
	}

	@Override
	public int onPlayerSelectedMenuRow( int playerid, int row )
	{
		return 1;
	}

	@Override
	public int onPlayerExitedMenu( int playerid )
	{
		return 1;
	}

	@Override
	public int onPlayerInteriorChange( int playerid, int newinteriorid, int oldinteriorid )
	{
		return 1;
	}

	@Override
	public int onPlayerKeyStateChange( int playerid, int newkeys, int oldkeys )
	{
		return 1;
	}

	@Override
	public int onRconLoginAttempt( String ip, String password, int success )
	{
		return 1;
	}

	@Override
	public int onPlayerUpdate( int playerid )
	{
		return 1;
	}

	@Override
	public int onPlayerStreamIn( int playerid, int forplayerid )
	{
		return 1;
	}

	@Override
	public int onPlayerStreamOut( int playerid, int forplayerid )
	{
		return 1;
	}

	@Override
	public int onVehicleStreamIn( int vehicleid, int forplayerid )
	{
		return 1;
	}

	@Override
	public int onVehicleStreamOut( int vehicleid, int forplayerid )
	{
		return 1;
	}

	@Override
	public int onDialogResponse( int playerid, int dialogid, int response, int listitem, String inputtext )
	{
		return 0;
	}

	@Override
	public int onPlayerTakeDamage( int playerId, int issuerId, float amount, int weaponId )
	{
		return 1;
	}

	@Override
	public int onPlayerGiveDamage( int playerId, int damagedId, float amount, int weaponId )
	{
		return 1;
	}

	@Override
	public int onPlayerClickMap( int playerId, float x, float y, float z )
	{
		return 1;
	}
	
	@Override
	public int onPlayerClickPlayer( int playerid, int clickedplayerid, int source )
	{
		return 1;
	}

	@Override
	public void onProcessTick()
	{
		
	}
}
