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

package net.gtaun.shoebill;

import java.util.Iterator;
import java.util.Vector;

import net.gtaun.lungfish.data.Area;
import net.gtaun.lungfish.data.KeyState;
import net.gtaun.lungfish.data.Point;
import net.gtaun.lungfish.data.PointAngle;
import net.gtaun.lungfish.data.SpawnInfo;
import net.gtaun.lungfish.data.Time;
import net.gtaun.lungfish.data.Velocity;
import net.gtaun.lungfish.object.IPlayer;
import net.gtaun.lungfish.object.IPlayerAttach;
import net.gtaun.lungfish.object.IPlayerSkill;
import net.gtaun.lungfish.object.IVehicle;
import net.gtaun.lungfish.util.event.EventDispatcher;
import net.gtaun.lungfish.util.event.IEventDispatcher;
import net.gtaun.shoebill.exception.AlreadyExistException;
import net.gtaun.shoebill.exception.IllegalLengthException;

/**
 * @author MK124, JoJLlmAn
 *
 */

public class Player implements IPlayer
{
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
	
	public static Vector<IPlayer> get()
	{
		return Gamemode.getInstances(Gamemode.instance.playerPool, IPlayer.class);
	}
	
	public static <T> Vector<T> get( Class<T> cls )
	{
		return Gamemode.getInstances(Gamemode.instance.playerPool, cls);
	}
	
	public static <T> T get( Class<T> cls, int id )
	{
		return Gamemode.getInstance(Gamemode.instance.playerPool, cls, id);
	}
	
	public static int getMaxPlayers()
	{
		return NativeFunction.getMaxPlayers();
	}
	
	public static void enableStuntBonusForAll( boolean enabled )
	{
		NativeFunction.enableStuntBonusForAll( enabled );
		
		Vector<Player> players = Player.get(Player.class);
		Iterator<Player> iterator = players.iterator();
		while( iterator.hasNext() )
		{
			Player player = iterator.next();
			player.isStuntBonusEnabled = enabled;
		}
	}
	
	public static void allowAdminTeleport( boolean allow )
	{
		NativeFunction.allowAdminTeleport(allow);
	}

	public static void sendMessageToAll( int color, String message )
	{
		Vector<Player> players = get(Player.class);
		Iterator<Player> iterator = players.iterator();
		while( iterator.hasNext() )
		{
			Player player = iterator.next();
			player.sendMessage( color, message );
		}
	}
	
	public static void sendMessageToAll( int color, String format, Object... args )
	{
		Vector<Player> players = get(Player.class);
		Iterator<Player> iterator = players.iterator();
		while( iterator.hasNext() )
		{
			Player player = iterator.next();
			String message = String.format(format, args);
			player.sendMessage( color, message );
		}
	}

	public static void gameTextToAll( int time, int style, String text )
	{
		NativeFunction.gameTextForAll( text, time, style );
	}
	
	public static void gameTextToAll( int time, int style, String format, Object... args )
	{
		String text = String.format(format, args);
		NativeFunction.gameTextForAll( text, time, style );
	}
	
	
	EventDispatcher eventDispatcher = new EventDispatcher();
	
	int id = -1, ping, team, skin, wantedLevel;
	String ip;
	String name;
	SpawnInfo spawnInfo = new SpawnInfo();
	int color;
	
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
//	VehicleBase vehicle;

	PointAngle position = new PointAngle();
	Area worldBound = new Area(-20000.0f, -20000.0f, 20000.0f, 20000.0f);
	Velocity velocity = new Velocity();
	int state = STATE_NONE;
	KeyState keyState = new KeyState();
	PlayerAttach playerAttach;
	PlayerSkill skill;
	Checkpoint checkpoint;
	RaceCheckpoint raceCheckpoint;
	
	Dialog dialog;
	

	public IEventDispatcher getEventDispatcher()	{ return eventDispatcher; }
	
	public int getId()								{ return id; }
	public int getPing()							{ return ping; }
	public int getTeam()							{ return team; }
	public int getSkin()							{ return skin; }
	public int getWantedLevel()						{ return wantedLevel; }
	public int getCodepage()						{ return NativeFunction.getPlayerCodepage(id); };
	public String getIp()							{ return ip; }
	public String getName()							{ return name; }
	public SpawnInfo getSpawnInfo()					{ return spawnInfo.clone(); }
	public int getColor()							{ return color; }

	public int getUpdateTick()						{ return updateTick; }
	public float getHealth()						{ return health; }
	public float getArmour()						{ return armour; }
	public int getWeapon()							{ return NativeFunction.getPlayerWeapon(id); }
	public int getAmmo()							{ return NativeFunction.getPlayerAmmo(id); }
	public int getMoney()							{ return money; }
	public int getScore()							{ return score; }
	public int getWeather()							{ return weather; }
	public int getCameraMode()						{ return cameraMode; }
	public int getFightingStyle()					{ return NativeFunction.getPlayerFightingStyle(id); }
	public IVehicle getVehicle()					{ return Vehicle.get(NativeFunction.getPlayerVehicleID(id)); }
	public int getVehicleSeat()						{ return NativeFunction.getPlayerVehicleSeat(id); }
	public int getSpecialAction()					{ return NativeFunction.getPlayerSpecialAction(id); }
	public Player getSpectatingPlayer()				{ return spectatingPlayer; }
	public IVehicle getSpectatingVehicle()			{ return spectatingVehicle; }
	
	public PointAngle getPosition()					{ return position.clone(); }
	public Area getWorldBound()						{ return worldBound.clone(); }
	public Velocity getVelocity()					{ return velocity.clone(); }
	public int getState()							{ return state; }
	public KeyState getKeyState()					{ return keyState.clone(); }
	public IPlayerAttach getPlayerAttach()			{ return playerAttach; }
	public IPlayerSkill getSkill()					{ return skill; }
	public Checkpoint getCheckpoint()			{ return checkpoint; }
	public RaceCheckpoint getRaceCheckpoint()	{ return raceCheckpoint; }
	
	public Dialog getDialog()					{ return dialog; }

	public boolean isStuntBonusEnabled()			{ return isStuntBonusEnabled; }
	public boolean isSpectating()					{ return spectating; }
	public boolean isRecording()					{ return isRecording; }
	public boolean isControllable()					{ return controllable; }
	
	
	
	protected Player()
	{
		id = Gamemode.instance.currentPlayerId;
		ip = NativeFunction.getPlayerIp(id);
		team = NativeFunction.getPlayerTeam(id);
		skin = NativeFunction.getPlayerSkin(id);
		name = NativeFunction.getPlayerName(id);
		color = NativeFunction.getPlayerColor(id);
		
		health = NativeFunction.getPlayerHealth(id);
		armour = NativeFunction.getPlayerArmour(id);
		money = NativeFunction.getPlayerMoney(id);
		score = NativeFunction.getPlayerScore(id);
		
		NativeFunction.getPlayerPos(id, position);
		NativeFunction.getPlayerFacingAngle(id);
		
		position.interior = NativeFunction.getPlayerInterior(id);
		position.world = NativeFunction.getPlayerVirtualWorld(id);
		
		NativeFunction.getPlayerVelocity(id, velocity);
		
		state = NativeFunction.getPlayerState(id);
		NativeFunction.getPlayerKeys(id, keyState);
		
		playerAttach = new PlayerAttach(id);
		skill = new PlayerSkill(id);
		
		cameraMode = NativeFunction.getPlayerCameraMode(id);
	}

	
//---------------------------------------------------------
	
	void update()
	{
		ping = NativeFunction.getPlayerPing( id );
		health = NativeFunction.getPlayerHealth(id);
		armour = NativeFunction.getPlayerArmour(id);
		money = NativeFunction.getPlayerMoney(id);
		NativeFunction.getPlayerPos( id, position );
		position.angle = NativeFunction.getPlayerFacingAngle(id);
		position.world = NativeFunction.getPlayerVirtualWorld(id);
		NativeFunction.getPlayerVelocity( id, velocity );
		NativeFunction.getPlayerKeys(id, keyState);
		
		cameraMode = NativeFunction.getPlayerCameraMode(id);
		
		updateTick++;
		if( updateTick<0 ) updateTick = 0;
	}

	
//---------------------------------------------------------
	
	public void setCodepage( int codepage )
	{
		NativeFunction.setPlayerCodepage( id, codepage );
	}
	
	public void setName( String name ) throws IllegalArgumentException, IllegalLengthException, AlreadyExistException
	{
		if( name == null ) throw new IllegalArgumentException();
		if( name.length()<3 || name.length()>20 ) throw new IllegalLengthException();
		
		int ret = NativeFunction.setPlayerName(id, name);
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
		NativeFunction.setSpawnInfo( id, info.team, info.skin, info.position.x, info.position.y, info.position.z, info.position.angle, info.weapon1.id, info.weapon1.ammo, info.weapon2.id, info.weapon2.ammo, info.weapon3.id, info.weapon3.ammo );
		spawnInfo = info;
	}
	
	public void setColor( int color )
	{
		NativeFunction.setPlayerColor( id, color );
		this.color = color;
	}

	public void setHealth( float health )
	{
		NativeFunction.setPlayerHealth( id, health );
		this.health = health;
	}
	
	public void setArmour( float armour)
	{
		NativeFunction.setPlayerArmour( id, armour );
		this.armour = armour;
	}
	
	public void setAmmo( int weaponslot, int ammo )
	{
		NativeFunction.setPlayerAmmo( id, weaponslot, ammo );
	}
	
	public void setMoney( int money )
	{
		NativeFunction.resetPlayerMoney( id );
		if( money != 0 ) NativeFunction.givePlayerMoney( id, money );
		
		this.money = money;
	}
	
	public void giveMoney( int money )
	{
		NativeFunction.givePlayerMoney( id, money );
		this.money = NativeFunction.getPlayerMoney(id);
	}
	
	public void setScore( int score )
	{
		NativeFunction.setPlayerScore( id, score );
		this.score = score;
	}
	
	public void setWeather( int weather )
	{
		NativeFunction.setPlayerWeather( id, weather );
		this.weather = weather;
	}
	
	public void setFightingStyle( int style )
	{
		NativeFunction.setPlayerFightingStyle( id, style );
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
		NativeFunction.setPlayerPos( id, x, y, z );

		this.position.x = x;
		this.position.y = y;
		this.position.z = z;
	}
	
	public void setPositionFindZ( float x, float y, float z )
	{
		NativeFunction.setPlayerPosFindZ( id, x, y, z );

		this.position.x = x;
		this.position.y = y;
		this.position.z = z;
	}

	public void setPosition( Point position )
	{
		NativeFunction.setPlayerPos( id, position.x, position.y, position.z );

		if( position.interior != this.position.interior )
			NativeFunction.setPlayerInterior( id, position.interior );
		
		if( position.world != this.position.world )
			NativeFunction.setPlayerVirtualWorld( id, position.world );
		
		this.position.set( position );
	}
	
	public void setPositionFindZ( Point position )
	{
		NativeFunction.setPlayerPosFindZ( id, position.x, position.y, position.z );

		if( position.interior != this.position.interior )
			NativeFunction.setPlayerInterior( id, position.interior );
		
		if( position.world != this.position.world )
			NativeFunction.setPlayerVirtualWorld( id, position.world );
		
		this.position.set( position );
	}

	public void setPosition( PointAngle position )
	{
		NativeFunction.setPlayerPos( id, position.x, position.y, position.z );
		NativeFunction.setPlayerFacingAngle( id, position.angle );

		if( position.interior != this.position.interior )
			NativeFunction.setPlayerInterior( id, position.interior );
		
		if( position.world != this.position.world )
			NativeFunction.setPlayerVirtualWorld( id, position.world );
		
		this.position.set( position );
	}
	
	public void setPositionFindZ( PointAngle position )
	{
		NativeFunction.setPlayerPosFindZ( id, position.x, position.y, position.z );
		NativeFunction.setPlayerFacingAngle( id, position.angle );

		if( position.interior != this.position.interior )
			NativeFunction.setPlayerInterior( id, position.interior );
		
		if( position.world != this.position.world )
			NativeFunction.setPlayerVirtualWorld( id, position.world );
		
		this.position.set( position );
	}
	
	public void setAngle( float angle )
	{
		NativeFunction.setPlayerFacingAngle( id, angle );
		position.angle = angle;
	}
	
	public void setInterior( int interior )
	{
		NativeFunction.setPlayerInterior( id, interior );
		position.interior = interior;
	}
	
	public void setWorld( int world )
	{
		NativeFunction.setPlayerVirtualWorld( id, world );
		position.world = world;
	}
	
	public void setWorldBound( Area bound )
	{
		NativeFunction.setPlayerWorldBounds( id, bound.maxX, bound.minX, bound.maxY, bound.minY );
		worldBound.set( bound );
	}
	
	public void setSpeed( Velocity spd )
	{
		NativeFunction.setPlayerVelocity( id, spd.x, spd.y, spd.z );
		velocity.set( spd );
	}
	
	
//---------------------------------------------------------

	public void sendMessage( int color, String message )
	{
		if( message == null ) throw new NullPointerException();
		NativeFunction.sendClientMessage( id, color, message );
	}
	
	public void sendMessage( int color, String format, Object... args )
	{
		String message = String.format(format, args);
		NativeFunction.sendClientMessage( id, color, message );
	}
	
	public void sendChat( Player player, String message )
	{
		if( message == null ) throw new NullPointerException();
		NativeFunction.sendPlayerMessageToPlayer( player.id, id, message );
	}
	
	public void sendChatToAll( String message )
	{
		if( message == null ) throw new NullPointerException();
		
		Vector<Player> players = get(Player.class);
		Iterator<Player> iterator = players.iterator();
		while( iterator.hasNext() )
		{
			Player player = iterator.next();
			sendChat( player, message );
		}
	}

	public void sendDeathMessage( Player killer, int reason )
	{
		if(killer == null)
			NativeFunction.sendDeathMessage(Gamemode.INVALID_PLAYER_ID, id, reason);
		else
			NativeFunction.sendDeathMessage( killer.id, id, reason );
	}

	public void gameText( int time, int style, String text )
	{
		if( text == null ) throw new NullPointerException();
		NativeFunction.gameTextForPlayer( id, text, time, style );
	}
	
	public void gameText( int time, int style, String format, Object... args )
	{
		String text = String.format(format, args);
		NativeFunction.gameTextForPlayer( id, text, time, style );
	}

	public void spawn()
	{
		NativeFunction.spawnPlayer( id );
	}
	
	public void setDrunkLevel( int level )
	{
		NativeFunction.setPlayerDrunkLevel( id, level );
	}
	
	public int getDrunkLevel()
	{
		return NativeFunction.getPlayerDrunkLevel(id);
	}

	public void applyAnimation( String animlib, String animname, float delta, int loop, int lockX, int lockY, int freeze, int time, int forcesync )
	{
		if( animlib == null || animname == null ) throw new NullPointerException();
		NativeFunction.applyAnimation( id, animlib, animname, delta, loop, lockX, lockY, freeze, time, forcesync );
	}
	
	public void clearAnimations( int forcesync )
	{
		NativeFunction.clearAnimations( id, forcesync );
	}
	
	public int getAnimationIndex()
	{
		return NativeFunction.getPlayerAnimationIndex(id);
	}
	
	public void allowTeleport( boolean allow )
	{
		NativeFunction.allowPlayerTeleport(id, allow);
	}

	public void playSound( int sound, float x, float y, float z )
	{
		NativeFunction.playerPlaySound( id, sound, x, y, z );
	}
	
	public void playSound( int sound, Point point )
	{
		NativeFunction.playerPlaySound( id, sound, point.x, point.y, point.z );
	}
	
	public void markerForPlayer( Player player, int color )
	{
		NativeFunction.setPlayerMarkerForPlayer( id, player.id, color );
	}
	
	public void nameTagForPlayer( Player player, boolean show )
	{
		NativeFunction.showPlayerNameTagForPlayer( id, player.id, show );
	}

	public void kick()
	{
		NativeFunction.kick( id );
	}
	
	public void ban()
	{
		NativeFunction.ban( id );
	}
	
	public void banEx( String reason )
	{
		if( reason == null ) throw new NullPointerException();
		NativeFunction.banEx( id, reason );
	}
	
	public Menu getMenu()
	{
		return Gamemode.instance.menuPool[ NativeFunction.getPlayerMenu(id) ];
	}

	public void setCameraPos( float x, float y, float z )
	{
		NativeFunction.setPlayerCameraPos( id, x, y, z );
	}
	
	public void setCameraPos( Point pos )
	{
		if( pos == null ) throw new NullPointerException();
		NativeFunction.setPlayerCameraPos( id, pos.x, pos.y, pos.z );
	}

	public void setCameraLookAt( float x, float y, float z )
	{
		NativeFunction.setPlayerCameraLookAt(id, x, y, z);
	}
	
	public void setCameraLookAt( Point lookat )
	{
		if( lookat == null ) throw new NullPointerException();
		NativeFunction.setPlayerCameraLookAt(id, lookat.x, lookat.y, lookat.z);
	}
	
	public void setCameraBehind()
	{
		NativeFunction.setCameraBehindPlayer(id);
	}
	
	public Point getCameraPos()
	{
		Point pos = new Point();
		NativeFunction.getPlayerCameraPos( id, pos );
		return pos;
	}
	
	public Point getCameraFrontVector()
	{
		Point lookat = new Point();
		NativeFunction.getPlayerCameraFrontVector( id, lookat );
		return lookat;
	}
	
	public boolean inAnyVehicle()
	{
		return NativeFunction.isPlayerInAnyVehicle( id );
	}
	
	public boolean inVehicle( Vehicle vehicle )
	{
		return NativeFunction.isPlayerInVehicle( id, vehicle.id );
	}
	
	public boolean isAdmin()
	{
		return NativeFunction.isPlayerAdmin(id);
	}
	
	public boolean inRangeOfPoint(Point point, int range)
	{
		return NativeFunction.isPlayerInRangeOfPoint(id, range, point.x, point.y, point.z);
	}
	
	public boolean isStreamedIn(Player forplayer)
	{
		return NativeFunction.isPlayerStreamedIn(id, forplayer.id);
	}
	
	public void setCheckpoint( Checkpoint checkpoint )
	{
		checkpoint.set( this );
	}
	
	public void disableCheckpoint()
	{
		NativeFunction.disablePlayerCheckpoint( id );
		checkpoint = null;
	}
	
	public void setRaceCheckpoint( RaceCheckpoint checkpoint )
	{
		checkpoint.set( this );
	}
	
	public void disableRaceCheckpoint()
	{
		NativeFunction.disablePlayerRaceCheckpoint( id );
		raceCheckpoint = null;
	}
	
	public int getWeaponState()
	{
		return NativeFunction.getPlayerWeaponState( id );
	}
	
	public void setTeam( int team )
	{
		NativeFunction.setPlayerTeam( id, team );
		this.team = team;
	}
	
	public void setSkin( int skin )
	{
		NativeFunction.setPlayerSkin( id, skin );
		this.skin = skin;
	}
	
	public void giveWeapon( int weaponid, int ammo )
	{
		NativeFunction.givePlayerWeapon( id, weaponid, ammo );
	}
	
	public void resetWeapons()
	{
		NativeFunction.resetPlayerWeapons( id );
	}
	
	public void setTime( int hour, int minute )
	{
		NativeFunction.setPlayerTime( id, hour, minute );
	}
	
	public Time getTime()
	{
		Time time = new Time();
		NativeFunction.getPlayerTime( id, time );
		return time;
	}
	
	public void toggleClock( boolean toggle )
	{
		NativeFunction.togglePlayerClock( id, toggle );
	}
	
	public void forceClassSelection()
	{
		NativeFunction.forceClassSelection( id );
	}
	
	public void setWantedLevel( int level )
	{
		NativeFunction.setPlayerWantedLevel( id, level );
		wantedLevel = level;
	}
	
	public void playCrimeReport( int suspectid, int crimeid )
	{
		NativeFunction.playCrimeReportForPlayer( id, suspectid, crimeid );
	}
	
	public void setShopName( String name )
	{
		if( name == null ) throw new NullPointerException();
		NativeFunction.setPlayerShopName(id, name);
	}
	
	public Vehicle getSurfingVehicle()
	{
		return Vehicle.get(Vehicle.class, NativeFunction.getPlayerSurfingVehicleID(id));
	}
	
	public void removeFromVehicle()
	{
		NativeFunction.removePlayerFromVehicle(id);
	}
	
	public void toggleControllable( boolean toggle )
	{
		NativeFunction.togglePlayerControllable( id, toggle );
		controllable = toggle;
	}
	
	public void setSpecialAction( int action )
	{
		NativeFunction.setPlayerSpecialAction( id, action );
	}
	
	public void setMapIcon( int iconid, Point point, int markertype, int color, int style )
	{
		NativeFunction.setPlayerMapIcon( id, iconid, point.x, point.y, point.z, markertype, color, style );
	}
	
	public void removeMapIcon( int iconid )
	{
		NativeFunction.removePlayerMapIcon( id, iconid );
	}
	
	public void enableStuntBonus( boolean enable )
	{
		if( enable )	NativeFunction.enableStuntBonusForPlayer( id, 1 );
		else			NativeFunction.enableStuntBonusForPlayer( id, 0 );
		
		isStuntBonusEnabled = enable;
	}
	
	public void toggleSpectating( boolean toggle )
	{
		NativeFunction.togglePlayerSpectating( id, toggle );
		spectating = toggle;
		
		if( toggle ) return;
		
		spectatingPlayer = null;
		spectatingVehicle = null;
	}
	
	public void spectatePlayer(Player player, int mode)
	{
		if( !spectating ) return;
		
		NativeFunction.playerSpectatePlayer(id, player.id, mode);
		spectatingPlayer = player;
		spectatingVehicle = null;
	}
	
	public void spectateVehicle(Vehicle vehicle, int mode)
	{
		if( !spectating ) return;

		NativeFunction.playerSpectateVehicle(id, vehicle.id, mode);
		spectatingPlayer = null;
		spectatingVehicle = vehicle;
	}
	
	public void startRecord( int type, String recordName )
	{
		NativeFunction.startRecordingPlayerData( id, type, recordName );
		isRecording = true;
	}
	
	public void stopRecord()
	{
		NativeFunction.stopRecordingPlayerData( id );
		isRecording = false;
	}
	
	public float distancToPoint( Point point )
	{
		return NativeFunction.getPlayerDistanceFromPoint(id, point.x, point.y, point.z);
	}
	
	public ObjectBase getSurfingObject()
	{
		int objectid = NativeFunction.getPlayerSurfingObjectID(id);
		
		if(objectid != 65535)
			return Gamemode.instance.objectPool[objectid];
		
		return null;
	}
	
	public String getNetworkStats()
	{
		return NativeFunction.getPlayerNetworkStats(id);
	}
}
