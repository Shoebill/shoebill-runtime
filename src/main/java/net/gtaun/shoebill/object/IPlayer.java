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

package net.gtaun.shoebill.object;

import net.gtaun.shoebill.data.Area;
import net.gtaun.shoebill.data.Color;
import net.gtaun.shoebill.data.KeyState;
import net.gtaun.shoebill.data.Point;
import net.gtaun.shoebill.data.PointAngle;
import net.gtaun.shoebill.data.SpawnInfo;
import net.gtaun.shoebill.data.Time;
import net.gtaun.shoebill.data.Velocity;
import net.gtaun.shoebill.exception.AlreadyExistException;
import net.gtaun.shoebill.exception.IllegalLengthException;
import net.gtaun.shoebill.util.event.IEventDispatcher;

/**
 * @author MK124
 *
 */

public interface IPlayer
{
	IEventDispatcher getEventDispatcher();
	
	int getId();
	int getPing();
	int getTeam();
	int getSkin();
	int getWantedLevel();
	int getCodepage();;
	String getIp();
	String getName();
	SpawnInfo getSpawnInfo();
	Color getColor();

	int getUpdateTick();
	float getHealth();
	float getArmour();
	int getWeapon();
	int getAmmo();
	int getMoney();
	int getScore();
	int getWeather();
	int getCameraMode();
	int getFightingStyle();
	IVehicle getVehicle();
	int getVehicleSeat();
	int getSpecialAction();
	IPlayer getSpectatingPlayer();
	IVehicle getSpectatingVehicle();
	
	PointAngle getPosition();
	Area getWorldBound();
	Velocity getVelocity();
	int getState();
	KeyState getKeyState();
	IPlayerAttach getPlayerAttach();
	IPlayerSkill getSkill();
	ICheckpoint getCheckpoint();
	IRaceCheckpoint getRaceCheckpoint();
	
	IDialog getDialog();

	boolean isOnline();
	boolean isStuntBonusEnabled();
	boolean isSpectating();
	boolean isRecording();
	boolean isControllable();
	
	void setCodepage( int codepage );
	void setName( String name ) throws IllegalArgumentException, IllegalLengthException, AlreadyExistException;
	void setSpawnInfo( float x, float y, float z, int interiorId, int worldId, float angle, int skin, int team, int weapon1, int ammo1, int weapon2, int ammo2, int weapon3, int ammo3 );
	void setSpawnInfo( SpawnInfo info );
	void setColor( Color color );
	void setHealth( float health );
	void setArmour( float armour);
	void setAmmo( int weaponslot, int ammo );
	void setMoney( int money );
	void giveMoney( int money );
	void setScore( int score );
	void setWeather( int weather );
	void setFightingStyle( int style );
	void setVehicle( IVehicle vehicle, int seat );
	void setVehicle( IVehicle vehicle );
	void setPosition( float x, float y, float z );
	void setPositionFindZ( float x, float y, float z );
	void setPosition( Point position );
	void setPositionFindZ( Point position );
	void setPosition( PointAngle position );
	void setPositionFindZ( PointAngle position );
	void setAngle( float angle );
	void setInterior( int interior );
	void setWorld( int world );
	void setWorldBound( Area bound );
	void setSpeed( Velocity speed );

	void sendMessage( Color color, String message );
	void sendMessage( Color color, String format, Object... args );
	void sendChat( IPlayer player, String message );
	void sendChatToAll( String message );
	void sendDeathMessage( IPlayer killer, int reason );
	void sendGameText( int time, int style, String text );
	void sendGameText( int time, int style, String format, Object... args );
	void spawn();
	void setDrunkLevel( int level );
	int getDrunkLevel();
	void applyAnimation( String animlib, String animname, float delta, int loop, int lockX, int lockY, int freeze, int time, int forcesync );
	void clearAnimations( int forcesync );
	int getAnimationIndex();
	void allowTeleport( boolean allow );
	void playSound( int sound, float x, float y, float z );
	void playSound( int sound, Point point );
	void markerForPlayer( IPlayer player, Color color );
	void showNameTagForPlayer( IPlayer player, boolean show );
	void kick();
	void ban();
	void banEx( String reason );
	IMenu getMenu();
	void setCameraPos( float x, float y, float z );
	void setCameraPos( Point pos );
	void setCameraLookAt( float x, float y, float z );
	void setCameraLookAt( Point lookat );
	void setCameraBehind();
	Point getCameraPos();
	Point getCameraFrontVector();
	boolean isInAnyVehicle();
	boolean isInVehicle( IVehicle vehicle );
	boolean isAdmin();
	boolean isInRangeOfPoint(Point point, int range);
	boolean isStreamedIn(IPlayer forplayer);
	void setCheckpoint( ICheckpoint checkpoint );
	void disableCheckpoint();
	void setRaceCheckpoint( IRaceCheckpoint checkpoint );
	void disableRaceCheckpoint();
	int getWeaponState();
	void setTeam( int team );
	void setSkin( int skin );
	void giveWeapon( int weaponid, int ammo );
	void resetWeapons();
	void setTime( int hour, int minute );
	Time getTime();
	void toggleClock( boolean toggle );
	void forceClassSelection();
	void setWantedLevel( int level );
	void playCrimeReport( int suspectid, int crimeid );
	void setShopName( String name );
	IVehicle getSurfingVehicle();
	void removeFromVehicle();
	void toggleControllable( boolean toggle );
	void setSpecialAction( int action );
	void setMapIcon( int iconid, Point point, int markertype, Color color, int style );
	void removeMapIcon( int iconid );
	void enableStuntBonus( boolean enable );
	void toggleSpectating( boolean toggle );
	void spectatePlayer(IPlayer player, int mode);
	void spectateVehicle(IVehicle vehicle, int mode);
	void startRecord( int type, String recordName );
	void stopRecord();
	IObject getSurfingObject();
	String getNetworkStats();

	void showDialog( IDialog dialog, String caption, String text, String button1, String button2 );
	void cancelDialog();
}
