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

public interface ISampCallback
{
	int OnGameModeInit();
	int onGameModeExit();
	int onPlayerRequestClass( int playerid, int classid );
	int onPlayerConnect( int playerid );
	int onPlayerDisconnect( int playerid, int reason );
	int onPlayerSpawn( int playerid );
	int onPlayerDeath( int playerid, int killerid, int reason );
	int onVehicleSpawn( int vehicleid );
	int onVehicleDeath( int vehicleid, int killerid );
	int onPlayerText( int playerid, String text );
	int onPlayerCommandText( int playerid, String cmdtext );
	int onPlayerEnterVehicle( int playerid, int vehicleid, int ispassenger );
	int onPlayerExitVehicle( int playerid, int vehicleid );
	int onPlayerStateChange( int playerid, int newstate, int oldstate );
	int onPlayerEnterCheckpoint( int playerid );
	int onPlayerLeaveCheckpoint( int playerid );
	int onPlayerEnterRaceCheckpoint( int playerid );
	int onPlayerLeaveRaceCheckpoint( int playerid );
	int onRconCommand( String cmd );
	int onPlayerRequestSpawn( int playerid );
	int onObjectMoved( int objectid );
	int onPlayerObjectMoved( int playerid, int objectid );
	int onPlayerPickUpPickup( int playerid, int pickupid );
	int onVehicleMod( int playerid, int vehicleid, int componentid );
	int onVehiclePaintjob( int playerid, int vehicleid, int paintjobid );
	int onVehicleRespray( int playerid, int vehicleid, int color1, int color2 );
	int onPlayerSelectedMenuRow( int playerid, int row );
	int onPlayerExitedMenu( int playerid );
	int onPlayerInteriorChange( int playerid, int newinteriorid, int oldinteriorid );
	int onPlayerKeyStateChange( int playerid, int newkeys, int oldkeys );
	int onRconLoginAttempt( String ip, String password, int success );
	int onPlayerUpdate( int playerid );
	int onPlayerStreamIn( int playerid, int forplayerid );
	int onPlayerStreamOut( int playerid, int forplayerid );
	int onVehicleStreamIn( int vehicleid, int forplayerid );
	int onVehicleStreamOut( int vehicleid, int forplayerid );
	int onDialogResponse( int playerid, int dialogid, int response, int listitem, String inputtext );
	int onPlayerClickPlayer( int playerid, int clickedplayerid, int source );
	int onEnterExitModShop( int playerid, int enterexit, int interiorid );
	int onVehicleDamageStatusUpdate( int vehicleid, int playerid );
	int onUnoccupiedVehicleUpdate( int vehicleid, int playerid, int passenger_seat );
	void onProcessTick();
}
