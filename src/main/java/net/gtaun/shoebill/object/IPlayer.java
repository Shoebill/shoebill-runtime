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
	
	public void setCodepage( int codepage );
	public void setName( String name ) throws IllegalArgumentException, IllegalLengthException, AlreadyExistException;
	public void setSpawnInfo( float x, float y, float z, int interiorId, int worldId, float angle, int skin, int team, int weapon1, int ammo1, int weapon2, int ammo2, int weapon3, int ammo3 );
	public void setSpawnInfo( SpawnInfo info );
	public void setColor( Color color );
	public void setHealth( float health );
	public void setArmour( float armour);
	public void setAmmo( int weaponslot, int ammo );
	public void setMoney( int money );
	public void giveMoney( int money );
	public void setScore( int score );
	public void setWeather( int weather );
	public void setFightingStyle( int style );
	public void setVehicle( IVehicle vehicle, int seat );
	public void setVehicle( IVehicle vehicle );
	public void setPosition( float x, float y, float z );
	public void setPositionFindZ( float x, float y, float z );
	public void setPosition( Point position );
	public void setPositionFindZ( Point position );
	public void setPosition( PointAngle position );
	public void setPositionFindZ( PointAngle position );
	public void setAngle( float angle );
	public void setInterior( int interior );
	public void setWorld( int world );
	public void setWorldBound( Area bound );
	public void setSpeed( Velocity speed );

	public void sendMessage( Color color, String message );
	public void sendMessage( Color color, String format, Object... args );
	public void sendChat( IPlayer player, String message );
	public void sendChatToAll( String message );
	public void sendDeathMessage( IPlayer killer, int reason );
	public void sendGameText( int time, int style, String text );
	public void sendGameText( int time, int style, String format, Object... args );
	public void spawn();
	public void setDrunkLevel( int level );
	public int getDrunkLevel();
	public void applyAnimation( String animlib, String animname, float delta, int loop, int lockX, int lockY, int freeze, int time, int forcesync );
	public void clearAnimations( int forcesync );
	public int getAnimationIndex();
	public void allowTeleport( boolean allow );
	public void playSound( int sound, float x, float y, float z );
	public void playSound( int sound, Point point );
	public void markerForPlayer( IPlayer player, Color color );
	public void showNameTagForPlayer( IPlayer player, boolean show );
	public void kick();
	public void ban();
	public void banEx( String reason );
	public IMenu getMenu();
	public void setCameraPos( float x, float y, float z );
	public void setCameraPos( Point pos );
	public void setCameraLookAt( float x, float y, float z );
	public void setCameraLookAt( Point lookat );
	public void setCameraBehind();
	public Point getCameraPos();
	public Point getCameraFrontVector();
	public boolean isInAnyVehicle();
	public boolean isInVehicle( IVehicle vehicle );
	public boolean isAdmin();
	public boolean isInRangeOfPoint(Point point, int range);
	public boolean isStreamedIn(IPlayer forplayer);
	public void setCheckpoint( ICheckpoint checkpoint );
	public void disableCheckpoint();
	public void setRaceCheckpoint( IRaceCheckpoint checkpoint );
	public void disableRaceCheckpoint();
	public int getWeaponState();
	public void setTeam( int team );
	public void setSkin( int skin );
	public void giveWeapon( int weaponid, int ammo );
	public void resetWeapons();
	public void setTime( int hour, int minute );
	public Time getTime();
	public void toggleClock( boolean toggle );
	public void forceClassSelection();
	public void setWantedLevel( int level );
	public void playCrimeReport( int suspectid, int crimeid );
	public void setShopName( String name );
	public IVehicle getSurfingVehicle();
	public void removeFromVehicle();
	public void toggleControllable( boolean toggle );
	public void setSpecialAction( int action );
	public void setMapIcon( int iconid, Point point, int markertype, Color color, int style );
	public void removeMapIcon( int iconid );
	public void enableStuntBonus( boolean enable );
	public void toggleSpectating( boolean toggle );
	public void spectatePlayer(IPlayer player, int mode);
	public void spectateVehicle(IVehicle vehicle, int mode);
	public void startRecord( int type, String recordName );
	public void stopRecord();
	public IObject getSurfingObject();
	public String getNetworkStats();

	public void showDialog( IDialog dialog, String caption, String text, String button1, String button2 );
	public void cancelDialog();
}
