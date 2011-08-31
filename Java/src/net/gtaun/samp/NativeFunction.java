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

package net.gtaun.samp;

import net.gtaun.samp.data.KeyState;
import net.gtaun.samp.data.Point;
import net.gtaun.samp.data.PointRot;
import net.gtaun.samp.data.Quaternions;
import net.gtaun.samp.data.Velocity;
import net.gtaun.samp.data.Time;
import net.gtaun.samp.data.WeaponData;

/**
 * @author MK124
 *
 */

final class NativeFunction
{
	static void loadLibrary()
	{
		System.loadLibrary( "Shoebill" );
	}
	
	
//----------------------------------------------------------
// a_samp.inc
	
	// Basic
	static native void setServerCodepage( int Codepage );
	static native int getServerCodepage();
	
	static native void setPlayerCodepage( int playerid, int Codepage );
	static native int getPlayerCodepage( int playerid );
	
	// Util
	static native void sendClientMessage( int playerid, int color, String message );
	static native void sendClientMessageToAll( int color, String message );
	static native void sendPlayerMessageToPlayer( int playerid, int senderid, String message );
	static native void sendPlayerMessageToAll( int senderid, String message );
	static native void sendDeathMessage( int killerid, int victimid, int reason );
	static native void gameTextForAll( String string, int time, int style );
	static native void gameTextForPlayer( int playerid, String string, int time, int style );
	static native int setTimer( int index, int interval, int repeating );
	static native void killTimer( int timerid );
	static native int getMaxPlayers();

	// Game
	static native void setGameModeText( String string );
	static native void setTeamCount( int count );
	static native int addPlayerClass( int modelid, float spawn_x, float spawn_y, float spawn_z, float z_angle, int weapon1, int weapon1_ammo, int weapon2, int weapon2_ammo, int weapon3, int weapon3_ammo );
	static native int addPlayerClassEx( int teamid, int modelid, float spawn_x, float spawn_y, float spawn_z, float z_angle, int weapon1, int weapon1_ammo, int weapon2, int weapon2_ammo, int weapon3, int weapon3_ammo );
	static native int addStaticVehicle( int modelid, float spawn_x, float spawn_y, float spawn_z, float z_angle, int color1, int color2 );
	static native int addStaticVehicleEx( int modelid, float spawn_x, float spawn_y, float spawn_z, float z_angle, int color1, int color2, int respawn_delay );
	static native int addStaticPickup( int model, int type, float x, float y, float z, int virtualWorld );
	static native int createPickup( int model, int type, float x, float y, float z, int virtualWorld );
	static native void destroyPickup( int pickup );
	static native void showNameTags( boolean enabled );
	static native void showPlayerMarkers( int mode );
	static native void gameModeExit();
	static native void setWorldTime( int hour );
	static native String getWeaponName( int weaponid );
	static native void enableTirePopping( boolean enabled );
	static native void allowInteriorWeapons( boolean allow );
	static native void setWeather( int weatherid );
	static native void setGravity( float gravity );
	static native void allowAdminTeleport( boolean allow );
	static native void setDeathDropAmount( int amount );
	static native void createExplosion( float x, float y, float z, int type, float radius );
	static native void enableZoneNames( boolean enabled );
	static native void usePlayerPedAnims();
	static native void disableInteriorEnterExits();
	static native void setNameTagDrawDistance( float distance );
	static native void disableNameTagLOS();
	static native void limitGlobalChatRadius( float chat_radius  );
	static native void limitPlayerMarkerRadius( float marker_radius );

	// Npc
	static native void connectNPC( String name, String script );
	static native boolean isPlayerNPC( int playerid );

	// Admin
	static native boolean isPlayerAdmin( int playerid );
	static native void kick( int playerid );
	static native void ban( int playerid );
	static native void banEx( int playerid, String reason );
	static native void sendRconCommand( String cmd );
	static native String getServerVarAsString( String varname );
	static native int getServerVarAsInt( String varname );
	static native boolean getServerVarAsBool( String varname );
	static native String getPlayerNetworkStats( int playerid );
	static native String getNetworkStats();

	// Menu
	static native int createMenu( String title, int columns, float x, float y, float col1width, float col2width );
	static native void destroyMenu( int menuid );
	static native void addMenuItem( int menuid, int column, String menutext );
	static native void setMenuColumnHeader( int menuid, int column, String columnheader );
	static native void showMenuForPlayer( int menuid, int playerid );
	static native void hideMenuForPlayer( int menuid, int playerid );
	static native boolean isValidMenu( int menuid );
	static native void disableMenu( int menuid );
	static native void disableMenuRow( int menuid, int row );
	static native int getPlayerMenu( int playerid );

	// Text Draw
	static native int textDrawCreate( float x, float y, String text );
	static native void textDrawDestroy( int textid );
	static native void textDrawLetterSize( int textid, float x, float y );
	static native void textDrawTextSize( int textid, float x, float y );
	static native void textDrawAlignment( int textid, int alignment );
	static native void textDrawColor( int textid, int color );
	static native void textDrawUseBox( int textid, boolean use );
	static native void textDrawBoxColor( int textid, int color );
	static native void textDrawSetShadow( int textid, int size );
	static native void textDrawSetOutline( int textid, int size );
	static native void textDrawBackgroundColor( int textid, int color );
	static native void textDrawFont( int textid, int font );
	static native void textDrawSetProportional( int textid, int set );
	static native void textDrawShowForPlayer( int playerid, int textid );
	static native void textDrawHideForPlayer( int playerid, int textid );
	static native void textDrawShowForAll( int textid );
	static native void textDrawHideForAll( int textid );
	static native void textDrawSetString( int textid, String string );

	// Gang Zones
	static native int gangZoneCreate( float minx, float miny, float maxx, float maxy );
	static native void gangZoneDestroy( int zoneid );
	static native void gangZoneShowForPlayer( int playerid, int zoneid, int color );
	static native void gangZoneShowForAll( int zoneid, int color );
	static native void gangZoneHideForPlayer( int playerid, int zoneid );
	static native void gangZoneHideForAll( int zoneid );
	static native void gangZoneFlashForPlayer( int playerid, int zoneid, int flashcolor );
	static native void gangZoneFlashForAll( int zoneid, int flashcolor );
	static native void gangZoneStopFlashForPlayer( int playerid, int zoneid );
	static native void gangZoneStopFlashForAll( int zoneid );

	// Global 3D Text Labels
	static native int create3DTextLabel( String text, int color, float x, float y, float z, float drawDistance, int worldid, boolean testLOS );
	static native void delete3DTextLabel( int id );
	static native void attach3DTextLabelToPlayer( int id, int playerid, float offsetX, float offsetY, float offsetZ );
	static native void attach3DTextLabelToVehicle( int id, int vehicleid, float offsetX, float offsetY, float offsetZ );
	static native void update3DTextLabelText( int id, int color, String text );

	// Per-player 3D Text Labels
	static native int createPlayer3DTextLabel( int playerid, String text, int color, float x, float y, float z, float drawDistance, int attachedplayerid, int attachedvehicleid, boolean testLOS );
	static native void deletePlayer3DTextLabel( int playerid, int id );
	static native void updatePlayer3DTextLabelText( int playerid, int id, int color, String text );

	// Player GUI Dialog
	static native int showPlayerDialog( int playerid, int dialogid, int style, String caption, String info, String button1, String button2  );

	
//----------------------------------------------------------
// a_players.inc
	static native void setSpawnInfo( int playerid, int teamid, int skinid, float x, float y, float z, float rotation, int weapon1, int weapon1_ammo, int weapon2, int weapon2_ammo, int weapon3, int weapon3_ammo );
	static native void spawnPlayer( int playerid );

	// Player info
	static native void setPlayerPos( int playerid, float x, float y, float z );
	static native void setPlayerPosFindZ( int playerid, float x, float y, float z );
	static native void getPlayerPos( int playerid, Point point );
	static native void setPlayerFacingAngle( int playerid, float angle );
	static native float getPlayerFacingAngle( int playerid );
	static native boolean isPlayerInRangeOfPoint( int playerid, float range, float x, float y, float z );
	static native float getPlayerDistanceFromPoint( int playerid, float x, float y, float z); //0.3c r3
	static native boolean isPlayerStreamedIn( int playerid, int forplayerid );
	static native void setPlayerInterior( int playerid, int interiorid );
	static native int getPlayerInterior( int playerid );
	static native void setPlayerHealth( int playerid, float health );
	static native float getPlayerHealth( int playerid );
	static native void setPlayerArmour( int playerid, float armour );
	static native float getPlayerArmour( int playerid );
	static native void setPlayerAmmo( int playerid, int weaponslot, int ammo );
	static native int getPlayerAmmo( int playerid );
	static native int getPlayerWeaponState( int playerid );
	static native void setPlayerTeam( int playerid, int teamid );
	static native int getPlayerTeam( int playerid );
	static native void setPlayerScore( int playerid, int score );
	static native int getPlayerScore( int playerid );
	static native int getPlayerDrunkLevel( int playerid );
	static native void setPlayerDrunkLevel( int playerid, int level );
	static native void setPlayerColor( int playerid, int color );
	static native int getPlayerColor( int playerid );
	static native void setPlayerSkin( int playerid, int skinid );
	static native int getPlayerSkin( int playerid );
	static native void givePlayerWeapon( int playerid, int weaponid, int ammo );
	static native void resetPlayerWeapons( int playerid );
	static native void setPlayerArmedWeapon( int playerid, int weaponid );
	static native void getPlayerWeaponData( int playerid, int slot, WeaponData weapondata );
	static native void givePlayerMoney( int playerid, int money );
	static native void resetPlayerMoney( int playerid );
	static native int setPlayerName( int playerid, String name );
	static native int getPlayerMoney( int playerid );
	static native int getPlayerState( int playerid );
	static native String getPlayerIp( int playerid );
	static native int getPlayerPing( int playerid );
	static native int getPlayerWeapon( int playerid );
	static native void getPlayerKeys( int playerid, KeyState keystate );
	static native String getPlayerName( int playerid );
	static native void setPlayerTime( int playerid, int hour, int minute );
	static native void getPlayerTime( int playerid, Time time );
	static native void togglePlayerClock( int playerid, boolean toggle );
	static native void setPlayerWeather( int playerid, int weather );
	static native void forceClassSelection( int playerid );
	static native void setPlayerWantedLevel( int playerid, int level );
	static native int getPlayerWantedLevel( int playerid );
	static native void setPlayerFightingStyle( int playerid, int style );
	static native int getPlayerFightingStyle( int playerid );
	static native void setPlayerVelocity( int playerid, float x, float y, float z );
	static native void getPlayerVelocity( int playerid, Velocity velocity );
	static native void playCrimeReportForPlayer( int playerid, int suspectid, int crime );
	static native void setPlayerShopName( int playerid, String shopname );
	static native void setPlayerSkillLevel( int playerid, int skill, int level );
	static native int getPlayerSurfingVehicleID( int playerid );
	static native int getPlayerSurfingObjectID( int playerid ); //0.3c r3
	
	static native boolean setPlayerAttachedObject( int playerid, int index, int modelid, int bone, float offsetX, float offsetY, float offsetZ, float rotX, float rotY, float rotZ, float scaleX, float scaleY, float scaleZ );
	static native boolean removePlayerAttachedObject( int playerid, int index );
	static native boolean isPlayerAttachedObjectSlotUsed( int playerid, int index );
	
	static native void setPlayerChatBubble( int playerid, String text, int color, float drawdistance, int expiretime );

	// Player controls
	static native void putPlayerInVehicle( int playerid, int vehicleid, int seatid );
	static native int getPlayerVehicleID( int playerid );
	static native int getPlayerVehicleSeat( int playerid );
	static native void removePlayerFromVehicle( int playerid );
	static native void togglePlayerControllable( int playerid, boolean toggle );
	static native void playerPlaySound( int playerid, int soundid, float x, float y, float z );
	static native void applyAnimation( int playerid, String animlib, String animname, float delta, int loop, int lockX, int lockY, int freeze, int time, int forcesync );
	static native void clearAnimations( int playerid, int forcesync  );
	static native int getPlayerAnimationIndex( int playerid ); // return the index of any running applied animations ( 0 if none are running )
//	static native int getAnimationName( int index, String animlib, int len1, String animname, int len2 ); // get the animation lib/name for the 
	static native int getPlayerSpecialAction( int playerid );
	static native void setPlayerSpecialAction( int playerid, int actionid );

	// Player map commands
	static native void setPlayerCheckpoint( int playerid, float x, float y, float z, float size );
	static native void disablePlayerCheckpoint( int playerid );
	static native void setPlayerRaceCheckpoint( int playerid, int type, float x, float y, float z, float nextX, float nextY, float nextZ, float size );
	static native void disablePlayerRaceCheckpoint( int playerid );
	static native void setPlayerWorldBounds( int playerid, float x_max, float x_min, float y_max, float y_min );
	static native void setPlayerMarkerForPlayer( int playerid, int showplayerid, int color );
	static native void showPlayerNameTagForPlayer( int playerid, int showplayerid, boolean show );
	static native void setPlayerMapIcon( int playerid, int iconid, float x, float y, float z, int markertype, int color, int style );
	static native void removePlayerMapIcon( int playerid, int iconid );
	static native void allowPlayerTeleport( int playerid, boolean allow );

	// Player camera
	static native void setPlayerCameraPos( int playerid, float x, float y, float z );
	static native void setPlayerCameraLookAt( int playerid, float x, float y, float z );
	static native void setCameraBehindPlayer( int playerid );
	static native void getPlayerCameraPos( int playerid, Point point );
	static native void getPlayerCameraFrontVector( int playerid, Point point );
	static native int getPlayerCameraMode( int playerid );

	// Player conditionals
	static native boolean isPlayerConnected( int playerid );
	static native boolean isPlayerInVehicle( int playerid, int vehicleid );
	static native boolean isPlayerInAnyVehicle( int playerid );
	static native boolean isPlayerInCheckpoint( int playerid );
	static native boolean isPlayerInRaceCheckpoint( int playerid );

	// Virtual Worlds
	static native void setPlayerVirtualWorld( int playerid, int worldid );
	static native int getPlayerVirtualWorld( int playerid );

	// Insane Stunts
	static native void enableStuntBonusForPlayer( int playerid, int enabled );
	static native void enableStuntBonusForAll( boolean enabled );

	// Spectating
	static native void togglePlayerSpectating( int playerid, boolean toggle );
	static native void playerSpectatePlayer( int playerid, int targetplayerid, int mode );
	static native void playerSpectateVehicle( int playerid, int targetvehicleid, int mode );

	// Npc Record
	static native void startRecordingPlayerData( int playerid, int recordtype, String recordname );
	static native void stopRecordingPlayerData( int playerid );
	
	
//----------------------------------------------------------
// a_vehicles.inc
	static native int createVehicle( int model, float x, float y, float z, float rotation, int color1, int color2, int respawnDelay );
	static native void destroyVehicle( int vehicleid );
	static native boolean isVehicleStreamedIn( int vehicleid, int forplayerid );
	static native void getVehiclePos( int vehicleid, Point point );
	static native void setVehiclePos( int vehicleid, float x, float y, float z );
	static native float getVehicleZAngle( int vehicleid );
	static native void getVehicleRotationQuat( int vehicleid, Quaternions quaternions );
	static native float getVehicleDistanceFromPoint( int vehicleid, float x, float y, float z );
	static native void setVehicleZAngle( int vehicleid, float angle );
	static native void setVehicleParamsForPlayer( int vehicleid, int playerid, boolean objective, boolean doorslocked );
	static native void manualVehicleEngineAndLights();
	static native void setVehicleParamsEx( int vehicleid, int engine, int lights, int alarm, int doors, int bonnet, int boot, int objective );
	static native void getVehicleParamsEx( int vehicleid, VehicleParam state );
	static native void setVehicleToRespawn( int vehicleid );
	static native void linkVehicleToInterior( int vehicleid, int interiorid );
	static native void addVehicleComponent( int vehicleid, int componentid );
	static native void removeVehicleComponent( int vehicleid, int componentid );
	static native void changeVehicleColor( int vehicleid, int color1, int color2 );
	static native void changeVehiclePaintjob( int vehicleid, int paintjobid );
	static native void setVehicleHealth( int vehicleid, float health );
	static native float getVehicleHealth( int vehicleid );
	static native void attachTrailerToVehicle( int trailerid, int vehicleid );
	static native void detachTrailerFromVehicle( int vehicleid );
	static native boolean isTrailerAttachedToVehicle( int vehicleid );
	static native int getVehicleTrailer( int vehicleid );
	static native void setVehicleNumberPlate( int vehicleid, String numberplate );
	static native int getVehicleModel( int vehicleid );
	static native int getVehicleComponentInSlot( int vehicleid, int slot ); // There is 1 slot for each CARMODTYPE_*
	static native int getVehicleComponentType( int component ); // Find CARMODTYPE_* for component id
	static native void repairVehicle( int vehicleid ); // Repairs the damage model and resets the health
	static native void getVehicleVelocity( int vehicleid, Velocity velocity );
	static native void setVehicleVelocity( int vehicleid, float x, float y, float z );
	static native void setVehicleAngularVelocity( int vehicleid, float x, float y, float z );
	static native void getVehicleDamageStatus( int vehicleid, VehicleDamage damage );
	static native void updateVehicleDamageStatus( int vehicleid, int panels, int doors, int lights, int tires );

	// Virtual Worlds
	static native void setVehicleVirtualWorld( int vehicleid, int worldid );
	static native int getVehicleVirtualWorld( int vehicleid );

	
//----------------------------------------------------------
// a_objects.inc
	static native int createObject( int modelid, float x, float y, float z, float rX, float rY, float rZ, float drawDistance );
	static native void attachObjectToVehicle( int objectid, int vehicleid, float x, float y, float z, float rX, float rY, float rZ );
	static native void setObjectPos( int objectid, float x, float y, float z );
	static native void getObjectPos( int objectid, Point point );
	static native void setObjectRot( int objectid, float rotX, float rotY, float rotZ );
	static native void getObjectRot( int objectid, PointRot pointrot );
	static native boolean isValidObject( int objectid );
	static native void destroyObject( int objectid );
	static native int moveObject( int objectid, float x, float y, float z, float speed );
	static native void stopObject( int objectid );
	static native int createPlayerObject( int playerid, int modelid, float x, float y, float z, float rX, float rY, float rZ, float drawDistance );
	static native void setPlayerObjectPos( int playerid, int objectid, float x, float y, float z );
	static native void getPlayerObjectPos( int playerid, int objectid, Point point );
	static native void setPlayerObjectRot( int playerid, int objectid, float rotX, float rotY, float rotZ );
	static native void getPlayerObjectRot( int playerid, int objectid, PointRot pointrot );
	static native boolean isValidPlayerObject( int playerid, int objectid );
	static native void destroyPlayerObject( int playerid, int objectid );
	static native void movePlayerObject( int playerid, int objectid, float x, float y, float z, float speed );
	static native void stopPlayerObject( int playerid, int objectid );
	static native void attachObjectToPlayer( int objectid, int playerid, float offsetX, float offsetY, float offsetZ, float rX, float rY, float rZ );
	static native void attachPlayerObjectToPlayer( int playerid, int objectid, int attachplayerid, float offsetX, float offsetY, float offsetZ, float rX, float rY, float rZ );
}
