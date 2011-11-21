/**
 * Copyright (C) 2011 MK124
 * Copyright (C) 2011 JoJLlmAn
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

import java.util.Collection;

import net.gtaun.shoebill.SampObjectPool;
import net.gtaun.shoebill.Shoebill;
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
import net.gtaun.shoebill.samp.SampNativeFunction;
import net.gtaun.shoebill.util.event.EventDispatcher;
import net.gtaun.shoebill.util.event.IEventDispatcher;

/**
 * @author MK124, JoJLlmAn
 *
 */

public class Player
{
	public static final int INVALID_ID =							0xFFFF;
	public static final int PLAYER_NO_TEAM =						255;
	public static final int MAX_NAME_LENGTH =						24;
	
	public static final int STATE_NONE =							0;
	public static final int STATE_ONFOOT =							1;
	public static final int STATE_DRIVER =							2;
	public static final int STATE_PASSENGER =						3;
	public static final int STATE_EXIT_VEHICLE =					4;
	public static final int STATE_ENTER_VEHICLE_DRIVER =			5;
	public static final int STATE_ENTER_VEHICLE_PASSENGER =			6;
	public static final int STATE_WASTED =							7;
	public static final int STATE_SPAWNED =							8;
	public static final int STATE_SPECTATING =						9;
	
	public static final int MARKERS_MODE_OFF =						0;
	public static final int MARKERS_MODE_GLOBAL =					1;
	public static final int MARKERS_MODE_STREAMED =					2;
	
	public static final int SPECIAL_ACTION_NONE =					0;
	public static final int SPECIAL_ACTION_DUCK =					1;
	public static final int SPECIAL_ACTION_USEJETPACK =				2;
	public static final int SPECIAL_ACTION_ENTER_VEHICLE =			3;
	public static final int SPECIAL_ACTION_EXIT_VEHICLE =			4;
	public static final int SPECIAL_ACTION_DANCE1 =					5;
	public static final int SPECIAL_ACTION_DANCE2 =					6;
	public static final int SPECIAL_ACTION_DANCE3 =					7;
	public static final int SPECIAL_ACTION_DANCE4 =					8;
	public static final int SPECIAL_ACTION_HANDSUP =				10;
	public static final int SPECIAL_ACTION_USECELLPHONE =			11;
	public static final int SPECIAL_ACTION_SITTING =				12;
	public static final int SPECIAL_ACTION_STOPUSECELLPHONE =		13;
	public static final int SPECIAL_ACTION_DRINK_BEER =				20;
	public static final int SPECIAL_ACTION_SMOKE_CIGGY =			21;
	public static final int SPECIAL_ACTION_DRINK_WINE =				22;
	public static final int SPECIAL_ACTION_DRINK_SPRUNK =			23;

	public static final int FIGHT_STYLE_NORMAL =					4;
	public static final int FIGHT_STYLE_BOXING =					5;
	public static final int FIGHT_STYLE_KUNGFU =					6;
	public static final int FIGHT_STYLE_KNEEHEAD =					7;
	public static final int FIGHT_STYLE_GRABKICK =					15;
	public static final int FIGHT_STYLE_ELBOW =						16;

	public static final int WEAPONSTATE_UNKNOWN =					-1;
	public static final int WEAPONSTATE_NO_BULLETS =				0;
	public static final int WEAPONSTATE_LAST_BULLET =				1;
	public static final int WEAPONSTATE_MORE_BULLETS =				2;
	public static final int WEAPONSTATE_RELOADING =					3;
	
	public static final int MAPICON_LOCAL =							0;
	public static final int MAPICON_GLOBAL =						1;
	public static final int MAPICON_LOCAL_CHECKPOINT =				2;
	public static final int MAPICON_GLOBAL_CHECKPOINT =				3;

	public static final int SPECTATE_MODE_NORMAL =					1;
	public static final int SPECTATE_MODE_FIXED =					2;
	public static final int SPECTATE_MODE_SIDE =					3;

	public static final int PLAYER_RECORDING_TYPE_NONE =			0;
	public static final int PLAYER_RECORDING_TYPE_DRIVER =			1;
	public static final int PLAYER_RECORDING_TYPE_ONFOOT =			2;
	
	public static final String SHOP_PIZZASTACK = 					"FDPIZA";
	public static final String SHOP_BURGERSHOT = 					"FDBURG";
	public static final String SHOP_CLUCKINBELL = 					"FDCHICK";
	public static final String SHOP_AMMUNATION1 = 					"AMMUN1";
	public static final String SHOP_AMMUNATION2 = 					"AMMUN2";
	public static final String SHOP_AMMUNATION3 = 					"AMMUN3";
	public static final String SHOP_AMMUNATION5 = 					"AMMUN5";
	
	public static final int MAX_PLAYER_ATTACHED_OBJECTS =			5;

	public static final int MAX_CHATBUBBLE_LENGTH =					144;
	
	
//---------------------------------------------------------
	
	public static Collection<Player> get()
	{
		return Shoebill.getInstance().getManagedObjectPool().getPlayers();
	}
	
	public static <T extends Player> Collection<T> get( Class<T> cls )
	{
		return Shoebill.getInstance().getManagedObjectPool().getPlayers( cls );
	}
	
	public static <T extends Player> T get( Class<T> cls, int id )
	{
		return cls.cast( Shoebill.getInstance().getManagedObjectPool().getPlayer(id) );
	}
	
	public static int getMaxPlayers()
	{
		return SampNativeFunction.getMaxPlayers();
	}
	
	public static void enableStuntBonusForAll( boolean enabled )
	{
		SampNativeFunction.enableStuntBonusForAll( enabled );
		for( Player player : get() ) player.isStuntBonusEnabled = enabled;
	}
	
	public static void allowAdminTeleport( boolean allow )
	{
		SampNativeFunction.allowAdminTeleport(allow);
	}

	public static void sendMessageToAll( Color color, String message )
	{
		for( Player player : get() ) player.sendMessage( color, message );
	}
	
	public static void sendMessageToAll( Color color, String format, Object... args )
	{
		for( Player player : get() )
		{
			String message = String.format(format, args);
			player.sendMessage( color, message );
		}
	}

	public static void gameTextToAll( int time, int style, String text )
	{
		SampNativeFunction.gameTextForAll( text, time, style );
	}
	
	public static void gameTextToAll( int time, int style, String format, Object... args )
	{
		String text = String.format(format, args);
		SampNativeFunction.gameTextForAll( text, time, style );
	}
	
	
	EventDispatcher eventDispatcher = new EventDispatcher();
	
	int id = -1;
	String ip;
	String name;
	SpawnInfo spawnInfo = new SpawnInfo();
	Color color;
	
	boolean controllable = true;
	boolean isStuntBonusEnabled = false;
	boolean spectating = false;
	boolean isRecording = false;
	
	Player spectatingPlayer;
	Vehicle spectatingVehicle;

	int updateTick = -1;
	float health, armour;
	int money, score;
	int weather, cameraMode;

	PointAngle position = new PointAngle();
	Area worldBound = new Area(-20000.0f, -20000.0f, 20000.0f, 20000.0f);
	Velocity velocity = new Velocity();
	KeyState keyState = new KeyState();
	PlayerAttach playerAttach;
	PlayerSkill skill;
	Checkpoint checkpoint;
	RaceCheckpoint raceCheckpoint;
	
	Dialog dialog;
	

	public IEventDispatcher getEventDispatcher()		{ return eventDispatcher; }
	
	public int getId()									{ return id; }
	public int getPing()								{ return SampNativeFunction.getPlayerPing(id); }
	public int getTeam()								{ return SampNativeFunction.getPlayerTeam(id); }
	public int getSkin()								{ return SampNativeFunction.getPlayerSkin(id); }
	public int getWantedLevel()							{ return SampNativeFunction.getPlayerWantedLevel(id); }
	public int getCodepage()							{ return SampNativeFunction.getPlayerCodepage(id); };
	public String getIp()								{ return ip; }
	public String getName()								{ return name; }
	public SpawnInfo getSpawnInfo()						{ return spawnInfo.clone(); }
	public Color getColor()								{ return color; }

	public int getUpdateTick()							{ return updateTick; }
	public float getHealth()							{ return health; }
	public float getArmour()							{ return armour; }
	public int getWeapon()								{ return SampNativeFunction.getPlayerWeapon(id); }
	public int getAmmo()								{ return SampNativeFunction.getPlayerAmmo(id); }
	public int getMoney()								{ return money; }
	public int getScore()								{ return score; }
	public int getWeather()								{ return weather; }
	public int getCameraMode()							{ return cameraMode; }
	public int getFightingStyle()						{ return SampNativeFunction.getPlayerFightingStyle(id); }
	public Vehicle getVehicle()							{ return Vehicle.get(SampNativeFunction.getPlayerVehicleID(id)); }
	public int getVehicleSeat()							{ return SampNativeFunction.getPlayerVehicleSeat(id); }
	public int getSpecialAction()						{ return SampNativeFunction.getPlayerSpecialAction(id); }
	public Player getSpectatingPlayer()					{ return spectatingPlayer; }
	public Vehicle getSpectatingVehicle()				{ return spectatingVehicle; }
	
	public PointAngle getPosition()						{ return position.clone(); }
	public Area getWorldBound()							{ return worldBound.clone(); }
	public Velocity getVelocity()						{ return velocity.clone(); }
	public int getState()								{ return SampNativeFunction.getPlayerState(id); }
	public KeyState getKeyState()						{ return keyState.clone(); }
	public PlayerAttach getPlayerAttach()				{ return playerAttach; }
	public PlayerSkill getSkill()						{ return skill; }
	public Checkpoint getCheckpoint()					{ return checkpoint; }
	public RaceCheckpoint getRaceCheckpoint()			{ return raceCheckpoint; }
	
	public Dialog getDialog()							{ return dialog; }
	
	public boolean isStuntBonusEnabled()				{ return isStuntBonusEnabled; }
	public boolean isSpectating()						{ return spectating; }
	public boolean isRecording()						{ return isRecording; }
	public boolean isControllable()						{ return controllable; }
	
	
	protected Player()
	{
		SampObjectPool pool = (SampObjectPool) Shoebill.getInstance().getManagedObjectPool();
		
		id = pool.getCurrentPlayerId();
		ip = SampNativeFunction.getPlayerIp(id);
		name = SampNativeFunction.getPlayerName(id);
		color = new Color( SampNativeFunction.getPlayerColor(id) );
		
		health = SampNativeFunction.getPlayerHealth(id);
		armour = SampNativeFunction.getPlayerArmour(id);
		money = SampNativeFunction.getPlayerMoney(id);
		score = SampNativeFunction.getPlayerScore(id);
		
		SampNativeFunction.getPlayerPos(id, position);
		SampNativeFunction.getPlayerFacingAngle(id);
		
		position.interior = SampNativeFunction.getPlayerInterior(id);
		position.world = SampNativeFunction.getPlayerVirtualWorld(id);
		
		SampNativeFunction.getPlayerVelocity(id, velocity);
		
		SampNativeFunction.getPlayerKeys(id, keyState);
		
		playerAttach = new PlayerAttach(id);
		skill = new PlayerSkill(id);
		
		cameraMode = SampNativeFunction.getPlayerCameraMode(id);
	}

	
//---------------------------------------------------------
	
	void update()
	{
		health = SampNativeFunction.getPlayerHealth(id);
		armour = SampNativeFunction.getPlayerArmour(id);
		money = SampNativeFunction.getPlayerMoney(id);
		SampNativeFunction.getPlayerPos( id, position );
		position.angle = SampNativeFunction.getPlayerFacingAngle(id);
		position.world = SampNativeFunction.getPlayerVirtualWorld(id);
		SampNativeFunction.getPlayerVelocity( id, velocity );
		SampNativeFunction.getPlayerKeys(id, keyState);
		
		cameraMode = SampNativeFunction.getPlayerCameraMode(id);
		
		updateTick++;
		if( updateTick<0 ) updateTick = 0;
	}

	
//---------------------------------------------------------
	
	public void setCodepage( int codepage )
	{
		SampNativeFunction.setPlayerCodepage( id, codepage );
	}
	
	public void setName( String name ) throws IllegalArgumentException, IllegalLengthException, AlreadyExistException
	{
		if( name == null ) throw new IllegalArgumentException();
		if( name.length()<3 || name.length()>20 ) throw new IllegalLengthException();
		
		int ret = SampNativeFunction.setPlayerName(id, name);
		if( ret == 0 )	throw new AlreadyExistException();
		if( ret == -1 )	throw new IllegalArgumentException();
		
		this.name = name;
	}
	
	public void setSpawnInfo( float x, float y, float z, int interiorId, int worldId, float angle, int skin, int team, int weapon1, int ammo1, int weapon2, int ammo2, int weapon3, int ammo3 )
	{
		SpawnInfo info = new SpawnInfo(x, y, z, interiorId, worldId, angle, skin, team, weapon1, ammo1, weapon2, ammo2, weapon3, ammo3);
		setSpawnInfo( info );
	}
	
	public void setSpawnInfo( SpawnInfo info )
	{
		SampNativeFunction.setSpawnInfo( id, info.team, info.skin, info.position.x, info.position.y, info.position.z, info.position.angle, info.weapon1.id, info.weapon1.ammo, info.weapon2.id, info.weapon2.ammo, info.weapon3.id, info.weapon3.ammo );
		spawnInfo = info;
	}
	
	public void setColor( Color color )
	{
		this.color = color.clone();
		SampNativeFunction.setPlayerColor( id, color.getValue() );
	}

	public void setHealth( float health )
	{
		SampNativeFunction.setPlayerHealth( id, health );
		this.health = health;
	}
	
	public void setArmour( float armour)
	{
		SampNativeFunction.setPlayerArmour( id, armour );
		this.armour = armour;
	}
	
	public void setAmmo( int weaponslot, int ammo )
	{
		SampNativeFunction.setPlayerAmmo( id, weaponslot, ammo );
	}
	
	public void setMoney( int money )
	{
		SampNativeFunction.resetPlayerMoney( id );
		if( money != 0 ) SampNativeFunction.givePlayerMoney( id, money );
		
		this.money = money;
	}
	
	public void giveMoney( int money )
	{
		SampNativeFunction.givePlayerMoney( id, money );
		this.money = SampNativeFunction.getPlayerMoney(id);
	}
	
	public void setScore( int score )
	{
		SampNativeFunction.setPlayerScore( id, score );
		this.score = score;
	}
	
	public void setWeather( int weather )
	{
		SampNativeFunction.setPlayerWeather( id, weather );
		this.weather = weather;
	}
	
	public void setFightingStyle( int style )
	{
		SampNativeFunction.setPlayerFightingStyle( id, style );
	}

	public void setVehicle( Vehicle vehicle, int seat )
	{
		vehicle.putPlayer( this, seat );
	}
	
	public void setVehicle( Vehicle vehicle )
	{
		vehicle.putPlayer( this, 0 );
	}

	public void setPosition( float x, float y, float z )
	{
		SampNativeFunction.setPlayerPos( id, x, y, z );

		this.position.x = x;
		this.position.y = y;
		this.position.z = z;
	}
	
	public void setPositionFindZ( float x, float y, float z )
	{
		SampNativeFunction.setPlayerPosFindZ( id, x, y, z );

		this.position.x = x;
		this.position.y = y;
		this.position.z = z;
	}

	public void setPosition( Point position )
	{
		SampNativeFunction.setPlayerPos( id, position.x, position.y, position.z );

		if( position.interior != this.position.interior )
			SampNativeFunction.setPlayerInterior( id, position.interior );
		
		if( position.world != this.position.world )
			SampNativeFunction.setPlayerVirtualWorld( id, position.world );
		
		this.position.set( position );
	}
	
	public void setPositionFindZ( Point position )
	{
		SampNativeFunction.setPlayerPosFindZ( id, position.x, position.y, position.z );

		if( position.interior != this.position.interior )
			SampNativeFunction.setPlayerInterior( id, position.interior );
		
		if( position.world != this.position.world )
			SampNativeFunction.setPlayerVirtualWorld( id, position.world );
		
		this.position.set( position );
	}

	public void setPosition( PointAngle position )
	{
		SampNativeFunction.setPlayerPos( id, position.x, position.y, position.z );
		SampNativeFunction.setPlayerFacingAngle( id, position.angle );

		if( position.interior != this.position.interior )
			SampNativeFunction.setPlayerInterior( id, position.interior );
		
		if( position.world != this.position.world )
			SampNativeFunction.setPlayerVirtualWorld( id, position.world );
		
		this.position.set( position );
	}
	
	public void setPositionFindZ( PointAngle position )
	{
		SampNativeFunction.setPlayerPosFindZ( id, position.x, position.y, position.z );
		SampNativeFunction.setPlayerFacingAngle( id, position.angle );

		if( position.interior != this.position.interior )
			SampNativeFunction.setPlayerInterior( id, position.interior );
		
		if( position.world != this.position.world )
			SampNativeFunction.setPlayerVirtualWorld( id, position.world );
		
		this.position.set( position );
	}
	
	public void setAngle( float angle )
	{
		SampNativeFunction.setPlayerFacingAngle( id, angle );
		position.angle = angle;
	}
	
	public void setInterior( int interior )
	{
		SampNativeFunction.setPlayerInterior( id, interior );
		position.interior = interior;
	}
	
	public void setWorld( int world )
	{
		SampNativeFunction.setPlayerVirtualWorld( id, world );
		position.world = world;
	}
	
	public void setWorldBound( Area bound )
	{
		SampNativeFunction.setPlayerWorldBounds( id, bound.maxX, bound.minX, bound.maxY, bound.minY );
		worldBound.set( bound );
	}
	
	public void setSpeed( Velocity spd )
	{
		SampNativeFunction.setPlayerVelocity( id, spd.x, spd.y, spd.z );
		velocity.set( spd );
	}
	
	
//---------------------------------------------------------

	public void sendMessage( Color color, String message )
	{
		if( message == null ) throw new NullPointerException();
		SampNativeFunction.sendClientMessage( id, color.getValue(), message );
	}
	
	public void sendMessage( Color color, String format, Object... args )
	{
		String message = String.format(format, args);
		SampNativeFunction.sendClientMessage( id, color.getValue(), message );
	}
	
	public void sendChat( Player player, String message )
	{
		if( message == null ) throw new NullPointerException();
		SampNativeFunction.sendPlayerMessageToPlayer( player.id, id, message );
	}
	
	public void sendChatToAll( String message )
	{
		if( message == null ) throw new NullPointerException();
		for( Player player : get() ) sendChat( player, message );
	}

	public void sendDeathMessage( Player killer, int reason )
	{
		if( killer == null )
			SampNativeFunction.sendDeathMessage( INVALID_ID, id, reason );
		else
			SampNativeFunction.sendDeathMessage( killer.id, id, reason );
	}

	public void sendGameText( int time, int style, String text )
	{
		if( text == null ) throw new NullPointerException();
		SampNativeFunction.gameTextForPlayer( id, text, time, style );
	}
	
	public void sendGameText( int time, int style, String format, Object... args )
	{
		String text = String.format(format, args);
		SampNativeFunction.gameTextForPlayer( id, text, time, style );
	}

	public void spawn()
	{
		SampNativeFunction.spawnPlayer( id );
	}
	
	public void setDrunkLevel( int level )
	{
		SampNativeFunction.setPlayerDrunkLevel( id, level );
	}
	
	public int getDrunkLevel()
	{
		return SampNativeFunction.getPlayerDrunkLevel(id);
	}

	public void applyAnimation( String animlib, String animname, float delta, int loop, int lockX, int lockY, int freeze, int time, int forcesync )
	{
		if( animlib == null || animname == null ) throw new NullPointerException();
		SampNativeFunction.applyAnimation( id, animlib, animname, delta, loop, lockX, lockY, freeze, time, forcesync );
	}
	
	public void clearAnimations( int forcesync )
	{
		SampNativeFunction.clearAnimations( id, forcesync );
	}
	
	public int getAnimationIndex()
	{
		return SampNativeFunction.getPlayerAnimationIndex(id);
	}
	
	public void allowTeleport( boolean allow )
	{
		SampNativeFunction.allowPlayerTeleport(id, allow);
	}

	public void playSound( int sound, float x, float y, float z )
	{
		SampNativeFunction.playerPlaySound( id, sound, x, y, z );
	}
	
	public void playSound( int sound, Point point )
	{
		SampNativeFunction.playerPlaySound( id, sound, point.x, point.y, point.z );
	}
	
	public void markerForPlayer( Player player, Color color )
	{
		SampNativeFunction.setPlayerMarkerForPlayer( id, player.id, color.getValue() );
	}
	
	public void showNameTagForPlayer( Player player, boolean show )
	{
		SampNativeFunction.showPlayerNameTagForPlayer( id, player.id, show );
	}

	public void kick()
	{
		SampNativeFunction.kick( id );
	}
	
	public void ban()
	{
		SampNativeFunction.ban( id );
	}
	
	public void banEx( String reason )
	{
		if( reason == null ) throw new NullPointerException();
		SampNativeFunction.banEx( id, reason );
	}
	
	public Menu getMenu()
	{
		return Shoebill.getInstance().getManagedObjectPool().getMenu( SampNativeFunction.getPlayerMenu(id) );
	}

	public void setCameraPos( float x, float y, float z )
	{
		SampNativeFunction.setPlayerCameraPos( id, x, y, z );
	}
	
	public void setCameraPos( Point pos )
	{
		if( pos == null ) throw new NullPointerException();
		SampNativeFunction.setPlayerCameraPos( id, pos.x, pos.y, pos.z );
	}

	public void setCameraLookAt( float x, float y, float z )
	{
		SampNativeFunction.setPlayerCameraLookAt(id, x, y, z);
	}
	
	public void setCameraLookAt( Point lookat )
	{
		if( lookat == null ) throw new NullPointerException();
		SampNativeFunction.setPlayerCameraLookAt(id, lookat.x, lookat.y, lookat.z);
	}
	
	public void setCameraBehind()
	{
		SampNativeFunction.setCameraBehindPlayer(id);
	}
	
	public Point getCameraPos()
	{
		Point pos = new Point();
		SampNativeFunction.getPlayerCameraPos( id, pos );
		return pos;
	}
	
	public Point getCameraFrontVector()
	{
		Point lookat = new Point();
		SampNativeFunction.getPlayerCameraFrontVector( id, lookat );
		return lookat;
	}
	
	public boolean isInAnyVehicle()
	{
		return SampNativeFunction.isPlayerInAnyVehicle( id );
	}
	
	public boolean isInVehicle( Vehicle vehicle )
	{
		return SampNativeFunction.isPlayerInVehicle( id, vehicle.id );
	}
	
	public boolean isAdmin()
	{
		return SampNativeFunction.isPlayerAdmin(id);
	}
	
	public boolean isInRangeOfPoint(Point point, int range)
	{
		return SampNativeFunction.isPlayerInRangeOfPoint(id, range, point.x, point.y, point.z);
	}
	
	public boolean isStreamedIn( Player forPlayer )
	{
		return SampNativeFunction.isPlayerStreamedIn(id, forPlayer.id);
	}
	
	public void setCheckpoint( Checkpoint checkpoint )
	{
		checkpoint.set( this );
	}
	
	public void disableCheckpoint()
	{
		SampNativeFunction.disablePlayerCheckpoint( id );
		checkpoint = null;
	}
	
	public void setRaceCheckpoint( RaceCheckpoint checkpoint )
	{
		checkpoint.set( this );
	}
	
	public void disableRaceCheckpoint()
	{
		SampNativeFunction.disablePlayerRaceCheckpoint( id );
		raceCheckpoint = null;
	}
	
	public int getWeaponState()
	{
		return SampNativeFunction.getPlayerWeaponState( id );
	}
	
	public void setTeam( int team )
	{
		SampNativeFunction.setPlayerTeam( id, team );
	}
	
	public void setSkin( int skin )
	{
		SampNativeFunction.setPlayerSkin( id, skin );
	}
	
	public void giveWeapon( int weaponid, int ammo )
	{
		SampNativeFunction.givePlayerWeapon( id, weaponid, ammo );
	}
	
	public void resetWeapons()
	{
		SampNativeFunction.resetPlayerWeapons( id );
	}
	
	public void setTime( int hour, int minute )
	{
		SampNativeFunction.setPlayerTime( id, hour, minute );
	}
	
	public Time getTime()
	{
		Time time = new Time();
		SampNativeFunction.getPlayerTime( id, time );
		return time;
	}
	
	public void toggleClock( boolean toggle )
	{
		SampNativeFunction.togglePlayerClock( id, toggle );
	}
	
	public void forceClassSelection()
	{
		SampNativeFunction.forceClassSelection( id );
	}
	
	public void setWantedLevel( int level )
	{
		SampNativeFunction.setPlayerWantedLevel( id, level );
	}
	
	public void playCrimeReport( int suspectid, int crimeid )
	{
		SampNativeFunction.playCrimeReportForPlayer( id, suspectid, crimeid );
	}
	
	public void setShopName( String name )
	{
		if( name == null ) throw new NullPointerException();
		SampNativeFunction.setPlayerShopName(id, name);
	}
	
	public Vehicle getSurfingVehicle()
	{
		return Vehicle.get(Vehicle.class, SampNativeFunction.getPlayerSurfingVehicleID(id));
	}
	
	public void removeFromVehicle()
	{
		SampNativeFunction.removePlayerFromVehicle(id);
	}
	
	public void toggleControllable( boolean toggle )
	{
		SampNativeFunction.togglePlayerControllable( id, toggle );
		controllable = toggle;
	}
	
	public void setSpecialAction( int action )
	{
		SampNativeFunction.setPlayerSpecialAction( id, action );
	}
	
	public void setMapIcon( int iconid, Point point, int markertype, Color color, int style )
	{
		SampNativeFunction.setPlayerMapIcon( id, iconid, point.x, point.y, point.z, markertype, color.getValue(), style );
	}
	
	public void removeMapIcon( int iconid )
	{
		SampNativeFunction.removePlayerMapIcon( id, iconid );
	}
	
	public void enableStuntBonus( boolean enable )
	{
		if( enable )	SampNativeFunction.enableStuntBonusForPlayer( id, 1 );
		else			SampNativeFunction.enableStuntBonusForPlayer( id, 0 );
		
		isStuntBonusEnabled = enable;
	}
	
	public void toggleSpectating( boolean toggle )
	{
		SampNativeFunction.togglePlayerSpectating( id, toggle );
		spectating = toggle;
		
		if( toggle ) return;
		
		spectatingPlayer = null;
		spectatingVehicle = null;
	}
	
	public void spectatePlayer( Player player, int mode )
	{
		if( !spectating ) return;
		
		SampNativeFunction.playerSpectatePlayer(id, player.id, mode);
		spectatingPlayer = player;
		spectatingVehicle = null;
	}
	
	public void spectateVehicle( Vehicle vehicle, int mode )
	{
		if( !spectating ) return;

		SampNativeFunction.playerSpectateVehicle(id, vehicle.id, mode);
		spectatingPlayer = null;
		spectatingVehicle = vehicle;
	}
	
	public void startRecord( int type, String recordName )
	{
		SampNativeFunction.startRecordingPlayerData( id, type, recordName );
		isRecording = true;
	}
	
	public void stopRecord()
	{
		SampNativeFunction.stopRecordingPlayerData( id );
		isRecording = false;
	}
	
	public float distancToPoint( Point point )
	{
		return SampNativeFunction.getPlayerDistanceFromPoint(id, point.x, point.y, point.z);
	}
	
	public ObjectBase getSurfingObject()
	{
		int objectid = SampNativeFunction.getPlayerSurfingObjectID(id);
		if( objectid == ObjectBase.INVALID_ID ) return null;
		
		return Shoebill.getInstance().getManagedObjectPool().getObject( objectid );
	}
	
	public String getNetworkStats()
	{
		return SampNativeFunction.getPlayerNetworkStats(id);
	}
	
	public boolean isOnline()
	{
		return id == -1;
	}
}
