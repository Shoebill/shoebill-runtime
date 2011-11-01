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

import net.gtaun.lungfish.data.KeyState;
import net.gtaun.lungfish.data.Point;
import net.gtaun.lungfish.data.PointRot;
import net.gtaun.lungfish.data.Quaternions;
import net.gtaun.lungfish.data.Time;
import net.gtaun.lungfish.data.Velocity;
import net.gtaun.lungfish.data.WeaponData;
import net.gtaun.lungfish.samp.ISampFunction;
import net.gtaun.shoebill.object.VehicleDamage;
import net.gtaun.shoebill.object.VehicleParam;

/**
 * @author MK124
 *
 */

public class SampNativeFunction implements ISampFunction
{
	public SampNativeFunction()
	{
		System.loadLibrary( "Shoebill" );
	}
	

//----------------------------------------------------------
// Custom
	
	public native void setServerCodepage( int Codepage );
	public native int getServerCodepage();
	
	public native void setPlayerCodepage( int playerid, int Codepage );
	public native int getPlayerCodepage( int playerid );
	
	
//----------------------------------------------------------
// a_samp.inc
	
	// Util
	public native void sendClientMessage( int playerid, int color, String message );
	public native void sendClientMessageToAll( int color, String message );
	public native void sendPlayerMessageToPlayer( int playerid, int senderid, String message );
	public native void sendPlayerMessageToAll( int senderid, String message );
	public native void sendDeathMessage( int killerid, int victimid, int reason );
	public native void gameTextForAll( String string, int time, int style );
	public native void gameTextForPlayer( int playerid, String string, int time, int style );
	public native int setTimer( int index, int interval, int repeating );
	public native void killTimer( int timerid );
	public native int getMaxPlayers();

	// Game
	public native void setGameModeText( String string );
	public native void setTeamCount( int count );
	public native int addPlayerClass( int modelid, float spawn_x, float spawn_y, float spawn_z, float z_angle, int weapon1, int weapon1_ammo, int weapon2, int weapon2_ammo, int weapon3, int weapon3_ammo );
	public native int addPlayerClassEx( int teamid, int modelid, float spawn_x, float spawn_y, float spawn_z, float z_angle, int weapon1, int weapon1_ammo, int weapon2, int weapon2_ammo, int weapon3, int weapon3_ammo );
	public native int addStaticVehicle( int modelid, float spawn_x, float spawn_y, float spawn_z, float z_angle, int color1, int color2 );
	public native int addStaticVehicleEx( int modelid, float spawn_x, float spawn_y, float spawn_z, float z_angle, int color1, int color2, int respawn_delay );
	public native int addStaticPickup( int model, int type, float x, float y, float z, int virtualWorld );
	public native int createPickup( int model, int type, float x, float y, float z, int virtualWorld );
	public native void destroyPickup( int pickup );
	public native void showNameTags( boolean enabled );
	public native void showPlayerMarkers( int mode );
	public native void gameModeExit();
	public native void setWorldTime( int hour );
	public native String getWeaponName( int weaponid );
	public native void enableTirePopping( boolean enabled );
	public native void allowInteriorWeapons( boolean allow );
	public native void setWeather( int weatherid );
	public native void setGravity( float gravity );
	public native void allowAdminTeleport( boolean allow );
	public native void setDeathDropAmount( int amount );
	public native void createExplosion( float x, float y, float z, int type, float radius );
	public native void enableZoneNames( boolean enabled );
	public native void usePlayerPedAnims();
	public native void disableInteriorEnterExits();
	public native void setNameTagDrawDistance( float distance );
	public native void disableNameTagLOS();
	public native void limitGlobalChatRadius( float chat_radius  );
	public native void limitPlayerMarkerRadius( float marker_radius );

	// Npc
	public native void connectNPC( String name, String script );
	public native boolean isPlayerNPC( int playerid );

	// Admin
	public native boolean isPlayerAdmin( int playerid );
	public native void kick( int playerid );
	public native void ban( int playerid );
	public native void banEx( int playerid, String reason );
	public native void sendRconCommand( String cmd );
	public native String getServerVarAsString( String varname );
	public native int getServerVarAsInt( String varname );
	public native boolean getServerVarAsBool( String varname );
	public native String getPlayerNetworkStats( int playerid );
	public native String getNetworkStats();

	// Menu
	public native int createMenu( String title, int columns, float x, float y, float col1width, float col2width );
	public native void destroyMenu( int menuid );
	public native void addMenuItem( int menuid, int column, String menutext );
	public native void setMenuColumnHeader( int menuid, int column, String columnheader );
	public native void showMenuForPlayer( int menuid, int playerid );
	public native void hideMenuForPlayer( int menuid, int playerid );
	public native boolean isValidMenu( int menuid );
	public native void disableMenu( int menuid );
	public native void disableMenuRow( int menuid, int row );
	public native int getPlayerMenu( int playerid );

	// Text Draw
	public native int textDrawCreate( float x, float y, String text );
	public native void textDrawDestroy( int textid );
	public native void textDrawLetterSize( int textid, float x, float y );
	public native void textDrawTextSize( int textid, float x, float y );
	public native void textDrawAlignment( int textid, int alignment );
	public native void textDrawColor( int textid, int color );
	public native void textDrawUseBox( int textid, boolean use );
	public native void textDrawBoxColor( int textid, int color );
	public native void textDrawSetShadow( int textid, int size );
	public native void textDrawSetOutline( int textid, int size );
	public native void textDrawBackgroundColor( int textid, int color );
	public native void textDrawFont( int textid, int font );
	public native void textDrawSetProportional( int textid, int set );
	public native void textDrawShowForPlayer( int playerid, int textid );
	public native void textDrawHideForPlayer( int playerid, int textid );
	public native void textDrawShowForAll( int textid );
	public native void textDrawHideForAll( int textid );
	public native void textDrawSetString( int textid, String string );

	// Gang Zones
	public native int gangZoneCreate( float minx, float miny, float maxx, float maxy );
	public native void gangZoneDestroy( int zoneid );
	public native void gangZoneShowForPlayer( int playerid, int zoneid, int color );
	public native void gangZoneShowForAll( int zoneid, int color );
	public native void gangZoneHideForPlayer( int playerid, int zoneid );
	public native void gangZoneHideForAll( int zoneid );
	public native void gangZoneFlashForPlayer( int playerid, int zoneid, int flashcolor );
	public native void gangZoneFlashForAll( int zoneid, int flashcolor );
	public native void gangZoneStopFlashForPlayer( int playerid, int zoneid );
	public native void gangZoneStopFlashForAll( int zoneid );

	// Global 3D Text Labels
	public native int create3DTextLabel( String text, int color, float x, float y, float z, float drawDistance, int worldid, boolean testLOS );
	public native void delete3DTextLabel( int id );
	public native void attach3DTextLabelToPlayer( int id, int playerid, float offsetX, float offsetY, float offsetZ );
	public native void attach3DTextLabelToVehicle( int id, int vehicleid, float offsetX, float offsetY, float offsetZ );
	public native void update3DTextLabelText( int id, int color, String text );

	// Per-player 3D Text Labels
	public native int createPlayer3DTextLabel( int playerid, String text, int color, float x, float y, float z, float drawDistance, int attachedplayerid, int attachedvehicleid, boolean testLOS );
	public native void deletePlayer3DTextLabel( int playerid, int id );
	public native void updatePlayer3DTextLabelText( int playerid, int id, int color, String text );

	// Player GUI Dialog
	public native int showPlayerDialog( int playerid, int dialogid, int style, String caption, String info, String button1, String button2  );

	
//----------------------------------------------------------
// a_players.inc

	// Player
	public native void setSpawnInfo( int playerid, int teamid, int skinid, float x, float y, float z, float rotation, int weapon1, int weapon1_ammo, int weapon2, int weapon2_ammo, int weapon3, int weapon3_ammo );
	public native void spawnPlayer( int playerid );

	// Player info
	public native void setPlayerPos( int playerid, float x, float y, float z );
	public native void setPlayerPosFindZ( int playerid, float x, float y, float z );
	public native void getPlayerPos( int playerid, Point point );
	public native void setPlayerFacingAngle( int playerid, float angle );
	public native float getPlayerFacingAngle( int playerid );
	public native boolean isPlayerInRangeOfPoint( int playerid, float range, float x, float y, float z );
	public native float getPlayerDistanceFromPoint( int playerid, float x, float y, float z); //0.3c r3
	public native boolean isPlayerStreamedIn( int playerid, int forplayerid );
	public native void setPlayerInterior( int playerid, int interiorid );
	public native int getPlayerInterior( int playerid );
	public native void setPlayerHealth( int playerid, float health );
	public native float getPlayerHealth( int playerid );
	public native void setPlayerArmour( int playerid, float armour );
	public native float getPlayerArmour( int playerid );
	public native void setPlayerAmmo( int playerid, int weaponslot, int ammo );
	public native int getPlayerAmmo( int playerid );
	public native int getPlayerWeaponState( int playerid );
	public native void setPlayerTeam( int playerid, int teamid );
	public native int getPlayerTeam( int playerid );
	public native void setPlayerScore( int playerid, int score );
	public native int getPlayerScore( int playerid );
	public native int getPlayerDrunkLevel( int playerid );
	public native void setPlayerDrunkLevel( int playerid, int level );
	public native void setPlayerColor( int playerid, int color );
	public native int getPlayerColor( int playerid );
	public native void setPlayerSkin( int playerid, int skinid );
	public native int getPlayerSkin( int playerid );
	public native void givePlayerWeapon( int playerid, int weaponid, int ammo );
	public native void resetPlayerWeapons( int playerid );
	public native void setPlayerArmedWeapon( int playerid, int weaponid );
	public native void getPlayerWeaponData( int playerid, int slot, WeaponData weapondata );
	public native void givePlayerMoney( int playerid, int money );
	public native void resetPlayerMoney( int playerid );
	public native int setPlayerName( int playerid, String name );
	public native int getPlayerMoney( int playerid );
	public native int getPlayerState( int playerid );
	public native String getPlayerIp( int playerid );
	public native int getPlayerPing( int playerid );
	public native int getPlayerWeapon( int playerid );
	public native void getPlayerKeys( int playerid, KeyState keystate );
	public native String getPlayerName( int playerid );
	public native void setPlayerTime( int playerid, int hour, int minute );
	public native void getPlayerTime( int playerid, Time time );
	public native void togglePlayerClock( int playerid, boolean toggle );
	public native void setPlayerWeather( int playerid, int weather );
	public native void forceClassSelection( int playerid );
	public native void setPlayerWantedLevel( int playerid, int level );
	public native int getPlayerWantedLevel( int playerid );
	public native void setPlayerFightingStyle( int playerid, int style );
	public native int getPlayerFightingStyle( int playerid );
	public native void setPlayerVelocity( int playerid, float x, float y, float z );
	public native void getPlayerVelocity( int playerid, Velocity velocity );
	public native void playCrimeReportForPlayer( int playerid, int suspectid, int crime );
	public native void setPlayerShopName( int playerid, String shopname );
	public native void setPlayerSkillLevel( int playerid, int skill, int level );
	public native int getPlayerSurfingVehicleID( int playerid );
	public native int getPlayerSurfingObjectID( int playerid ); //0.3c r3

	// Attached to bone objects
	public native boolean setPlayerAttachedObject( int playerid, int index, int modelid, int bone, float offsetX, float offsetY, float offsetZ, float rotX, float rotY, float rotZ, float scaleX, float scaleY, float scaleZ );
	public native boolean removePlayerAttachedObject( int playerid, int index );
	public native boolean isPlayerAttachedObjectSlotUsed( int playerid, int index );

	// Per-player variable system (PVars)
	public native void setPVarInt( int playerid, String varname, int int_value );
	public native int getPVarInt( int playerid, String varname );
	public native void setPVarString( int playerid, String varname, String string_value );
	public native String getPVarString( int playerid, String varname );
	public native void setPVarFloat( int playerid, String varname, float float_value );
	public native float getPVarFloat( int playerid, String varname );
	public native int deletePVar( int playerid, String varname );
	
	public native int getPVarsUpperIndex( int playerid );
	public native String getPVarNameAtIndex( int playerid, int index );
	public native int getPVarType( int playerid, String varname );
	
	// Chat Bubble
	public native void setPlayerChatBubble( int playerid, String text, int color, float drawdistance, int expiretime );

	// Player controls
	public native void putPlayerInVehicle( int playerid, int vehicleid, int seatid );
	public native int getPlayerVehicleID( int playerid );
	public native int getPlayerVehicleSeat( int playerid );
	public native void removePlayerFromVehicle( int playerid );
	public native void togglePlayerControllable( int playerid, boolean toggle );
	public native void playerPlaySound( int playerid, int soundid, float x, float y, float z );
	public native void applyAnimation( int playerid, String animlib, String animname, float delta, int loop, int lockX, int lockY, int freeze, int time, int forcesync );
	public native void clearAnimations( int playerid, int forcesync  );
	public native int getPlayerAnimationIndex( int playerid ); // return the index of any running applied animations ( 0 if none are running )
//	public native int getAnimationName( int index, String animlib, int len1, String animname, int len2 ); // get the animation lib/name for the 
	public native int getPlayerSpecialAction( int playerid );
	public native void setPlayerSpecialAction( int playerid, int actionid );

	// Player world/map related
	public native void setPlayerCheckpoint( int playerid, float x, float y, float z, float size );
	public native void disablePlayerCheckpoint( int playerid );
	public native void setPlayerRaceCheckpoint( int playerid, int type, float x, float y, float z, float nextX, float nextY, float nextZ, float size );
	public native void disablePlayerRaceCheckpoint( int playerid );
	public native void setPlayerWorldBounds( int playerid, float x_max, float x_min, float y_max, float y_min );
	public native void setPlayerMarkerForPlayer( int playerid, int showplayerid, int color );
	public native void showPlayerNameTagForPlayer( int playerid, int showplayerid, boolean show );
	
	public native void setPlayerMapIcon( int playerid, int iconid, float x, float y, float z, int markertype, int color, int style );
	public native void removePlayerMapIcon( int playerid, int iconid );
	
	public native void allowPlayerTeleport( int playerid, boolean allow );

	// Player camera
	public native void setPlayerCameraPos( int playerid, float x, float y, float z );
	public native void setPlayerCameraLookAt( int playerid, float x, float y, float z );
	public native void setCameraBehindPlayer( int playerid );
	public native void getPlayerCameraPos( int playerid, Point point );
	public native void getPlayerCameraFrontVector( int playerid, Point point );
	public native int getPlayerCameraMode( int playerid );

	// Player conditionals
	public native boolean isPlayerConnected( int playerid );
	public native boolean isPlayerInVehicle( int playerid, int vehicleid );
	public native boolean isPlayerInAnyVehicle( int playerid );
	public native boolean isPlayerInCheckpoint( int playerid );
	public native boolean isPlayerInRaceCheckpoint( int playerid );

	// Virtual Worlds
	public native void setPlayerVirtualWorld( int playerid, int worldid );
	public native int getPlayerVirtualWorld( int playerid );

	// Insane Stunts
	public native void enableStuntBonusForPlayer( int playerid, int enabled );
	public native void enableStuntBonusForAll( boolean enabled );

	// Spectating
	public native void togglePlayerSpectating( int playerid, boolean toggle );
	public native void playerSpectatePlayer( int playerid, int targetplayerid, int mode );
	public native void playerSpectateVehicle( int playerid, int targetvehicleid, int mode );

	// Recording for NPC playback
	public native void startRecordingPlayerData( int playerid, int recordtype, String recordname );
	public native void stopRecordingPlayerData( int playerid );
	
	
//----------------------------------------------------------
// a_vehicles.inc

	// Vehicle
	public native int createVehicle( int model, float x, float y, float z, float rotation, int color1, int color2, int respawnDelay );
	public native void destroyVehicle( int vehicleid );
	public native boolean isVehicleStreamedIn( int vehicleid, int forplayerid );
	public native void getVehiclePos( int vehicleid, Point point );
	public native void setVehiclePos( int vehicleid, float x, float y, float z );
	public native float getVehicleZAngle( int vehicleid );
	public native void getVehicleRotationQuat( int vehicleid, Quaternions quaternions );
	public native float getVehicleDistanceFromPoint( int vehicleid, float x, float y, float z );
	public native void setVehicleZAngle( int vehicleid, float angle );
	public native void setVehicleParamsForPlayer( int vehicleid, int playerid, boolean objective, boolean doorslocked );
	public native void manualVehicleEngineAndLights();
	public native void setVehicleParamsEx( int vehicleid, int engine, int lights, int alarm, int doors, int bonnet, int boot, int objective );
	public native void getVehicleParamsEx( int vehicleid, VehicleParam state );
	public native void setVehicleToRespawn( int vehicleid );
	public native void linkVehicleToInterior( int vehicleid, int interiorid );
	public native void addVehicleComponent( int vehicleid, int componentid );
	public native void removeVehicleComponent( int vehicleid, int componentid );
	public native void changeVehicleColor( int vehicleid, int color1, int color2 );
	public native void changeVehiclePaintjob( int vehicleid, int paintjobid );
	public native void setVehicleHealth( int vehicleid, float health );
	public native float getVehicleHealth( int vehicleid );
	public native void attachTrailerToVehicle( int trailerid, int vehicleid );
	public native void detachTrailerFromVehicle( int vehicleid );
	public native boolean isTrailerAttachedToVehicle( int vehicleid );
	public native int getVehicleTrailer( int vehicleid );
	public native void setVehicleNumberPlate( int vehicleid, String numberplate );
	public native int getVehicleModel( int vehicleid );
	public native int getVehicleComponentInSlot( int vehicleid, int slot ); // There is 1 slot for each CARMODTYPE_*
	public native int getVehicleComponentType( int component ); // Find CARMODTYPE_* for component id
	public native void repairVehicle( int vehicleid ); // Repairs the damage model and resets the health
	public native void getVehicleVelocity( int vehicleid, Velocity velocity );
	public native void setVehicleVelocity( int vehicleid, float x, float y, float z );
	public native void setVehicleAngularVelocity( int vehicleid, float x, float y, float z );
	public native void getVehicleDamageStatus( int vehicleid, VehicleDamage damage );
	public native void updateVehicleDamageStatus( int vehicleid, int panels, int doors, int lights, int tires );

	// Virtual Worlds
	public native void setVehicleVirtualWorld( int vehicleid, int worldid );
	public native int getVehicleVirtualWorld( int vehicleid );

	
//----------------------------------------------------------
// a_objects.inc
	
	public native int createObject( int modelid, float x, float y, float z, float rX, float rY, float rZ, float drawDistance );
	public native void attachObjectToVehicle( int objectid, int vehicleid, float x, float y, float z, float rX, float rY, float rZ );
	public native void setObjectPos( int objectid, float x, float y, float z );
	public native void getObjectPos( int objectid, Point point );
	public native void setObjectRot( int objectid, float rotX, float rotY, float rotZ );
	public native void getObjectRot( int objectid, PointRot pointrot );
	public native boolean isValidObject( int objectid );
	public native void destroyObject( int objectid );
	public native int moveObject( int objectid, float x, float y, float z, float speed );
	public native void stopObject( int objectid );
	public native int createPlayerObject( int playerid, int modelid, float x, float y, float z, float rX, float rY, float rZ, float drawDistance );
	public native void setPlayerObjectPos( int playerid, int objectid, float x, float y, float z );
	public native void getPlayerObjectPos( int playerid, int objectid, Point point );
	public native void setPlayerObjectRot( int playerid, int objectid, float rotX, float rotY, float rotZ );
	public native void getPlayerObjectRot( int playerid, int objectid, PointRot pointrot );
	public native boolean isValidPlayerObject( int playerid, int objectid );
	public native void destroyPlayerObject( int playerid, int objectid );
	public native void movePlayerObject( int playerid, int objectid, float x, float y, float z, float speed );
	public native void stopPlayerObject( int playerid, int objectid );
	public native void attachObjectToPlayer( int objectid, int playerid, float offsetX, float offsetY, float offsetZ, float rX, float rY, float rZ );
	public native void attachPlayerObjectToPlayer( int playerid, int objectid, int attachplayerid, float offsetX, float offsetY, float offsetZ, float rX, float rY, float rZ );
}
