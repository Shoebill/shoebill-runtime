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

import java.util.Iterator;
import java.util.Vector;

import net.gtaun.lungfish.data.Area;
import net.gtaun.lungfish.data.Color;
import net.gtaun.lungfish.data.KeyState;
import net.gtaun.lungfish.data.Point;
import net.gtaun.lungfish.data.PointAngle;
import net.gtaun.lungfish.data.SpawnInfo;
import net.gtaun.lungfish.data.Time;
import net.gtaun.lungfish.data.Velocity;
import net.gtaun.lungfish.exception.AlreadyExistException;
import net.gtaun.lungfish.exception.IllegalLengthException;
import net.gtaun.lungfish.object.ICheckpoint;
import net.gtaun.lungfish.object.IPlayer;
import net.gtaun.lungfish.object.IPlayerAttach;
import net.gtaun.lungfish.object.IPlayerSkill;
import net.gtaun.lungfish.object.IRaceCheckpoint;
import net.gtaun.lungfish.object.IVehicle;
import net.gtaun.lungfish.util.event.EventDispatcher;
import net.gtaun.lungfish.util.event.IEventDispatcher;
import net.gtaun.shoebill.SampNativeFunction;

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
		return SampNativeFunction.getMaxPlayers();
	}
	
	public static void enableStuntBonusForAll( boolean enabled )
	{
		SampNativeFunction.enableStuntBonusForAll( enabled );
		
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
		SampNativeFunction.allowAdminTeleport(allow);
	}

	public static void sendMessageToAll( Color color, String message )
	{
		Vector<Player> players = get(Player.class);
		Iterator<Player> iterator = players.iterator();
		while( iterator.hasNext() )
		{
			Player player = iterator.next();
			player.sendMessage( color, message );
		}
	}
	
	public static void sendMessageToAll( Color color, String format, Object... args )
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
		SampNativeFunction.gameTextForAll( text, time, style );
	}
	
	public static void gameTextToAll( int time, int style, String format, Object... args )
	{
		String text = String.format(format, args);
		SampNativeFunction.gameTextForAll( text, time, style );
	}
	
	
	EventDispatcher eventDispatcher = new EventDispatcher();
	
	int id = -1, ping, team, skin, wantedLevel;
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
	

	@Override public IEventDispatcher getEventDispatcher()		{ return eventDispatcher; }
	
	@Override public int getId()								{ return id; }
	@Override public int getPing()								{ return ping; }
	@Override public int getTeam()								{ return team; }
	@Override public int getSkin()								{ return skin; }
	@Override public int getWantedLevel()						{ return wantedLevel; }
	@Override public int getCodepage()							{ return SampNativeFunction.getPlayerCodepage(id); };
	@Override public String getIp()								{ return ip; }
	@Override public String getName()							{ return name; }
	@Override public SpawnInfo getSpawnInfo()					{ return spawnInfo.clone(); }
	@Override public Color getColor()							{ return color; }

	@Override public int getUpdateTick()						{ return updateTick; }
	@Override public float getHealth()							{ return health; }
	@Override public float getArmour()							{ return armour; }
	@Override public int getWeapon()							{ return SampNativeFunction.getPlayerWeapon(id); }
	@Override public int getAmmo()								{ return SampNativeFunction.getPlayerAmmo(id); }
	@Override public int getMoney()								{ return money; }
	@Override public int getScore()								{ return score; }
	@Override public int getWeather()							{ return weather; }
	@Override public int getCameraMode()						{ return cameraMode; }
	@Override public int getFightingStyle()						{ return SampNativeFunction.getPlayerFightingStyle(id); }
	@Override public IVehicle getVehicle()						{ return Vehicle.get(SampNativeFunction.getPlayerVehicleID(id)); }
	@Override public int getVehicleSeat()						{ return SampNativeFunction.getPlayerVehicleSeat(id); }
	@Override public int getSpecialAction()						{ return SampNativeFunction.getPlayerSpecialAction(id); }
	@Override public Player getSpectatingPlayer()				{ return spectatingPlayer; }
	@Override public IVehicle getSpectatingVehicle()			{ return spectatingVehicle; }
	
	@Override public PointAngle getPosition()					{ return position.clone(); }
	@Override public Area getWorldBound()						{ return worldBound.clone(); }
	@Override public Velocity getVelocity()						{ return velocity.clone(); }
	@Override public int getState()								{ return state; }
	@Override public KeyState getKeyState()						{ return keyState.clone(); }
	@Override public IPlayerAttach getPlayerAttach()			{ return playerAttach; }
	@Override public IPlayerSkill getSkill()					{ return skill; }
	@Override public Checkpoint getCheckpoint()					{ return checkpoint; }
	@Override public RaceCheckpoint getRaceCheckpoint()			{ return raceCheckpoint; }
	
	@Override public Dialog getDialog()							{ return dialog; }
	
	@Override public boolean isStuntBonusEnabled()				{ return isStuntBonusEnabled; }
	@Override public boolean isSpectating()						{ return spectating; }
	@Override public boolean isRecording()						{ return isRecording; }
	@Override public boolean isControllable()					{ return controllable; }
	
	
	protected Player()
	{
		id = Gamemode.instance.currentPlayerId;
		ip = SampNativeFunction.getPlayerIp(id);
		team = SampNativeFunction.getPlayerTeam(id);
		skin = SampNativeFunction.getPlayerSkin(id);
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
		
		state = SampNativeFunction.getPlayerState(id);
		SampNativeFunction.getPlayerKeys(id, keyState);
		
		playerAttach = new PlayerAttach(id);
		skill = new PlayerSkill(id);
		
		cameraMode = SampNativeFunction.getPlayerCameraMode(id);
	}

	
//---------------------------------------------------------
	
	void update()
	{
		ping = SampNativeFunction.getPlayerPing( id );
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
	
	@Override
	public void setCodepage( int codepage )
	{
		SampNativeFunction.setPlayerCodepage( id, codepage );
	}
	
	@Override
	public void setName( String name ) throws IllegalArgumentException, IllegalLengthException, AlreadyExistException
	{
		if( name == null ) throw new IllegalArgumentException();
		if( name.length()<3 || name.length()>20 ) throw new IllegalLengthException();
		
		int ret = SampNativeFunction.setPlayerName(id, name);
		if( ret == 0 )	throw new AlreadyExistException();
		if( ret == -1 )	throw new IllegalArgumentException();
		
		this.name = name;
	}
	
	@Override
	public void setSpawnInfo( float x, float y, float z, int interiorId, int worldId, float angle, int skin, int team, int weapon1, int ammo1, int weapon2, int ammo2, int weapon3, int ammo3 )
	{
		SpawnInfo info = new SpawnInfo(x, y, z, interiorId, worldId, angle, skin, team, weapon1, ammo1, weapon2, ammo2, weapon3, ammo3);
		setSpawnInfo( info );
	}
	
	@Override
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

	@Override
	public void setHealth( float health )
	{
		SampNativeFunction.setPlayerHealth( id, health );
		this.health = health;
	}
	
	@Override
	public void setArmour( float armour)
	{
		SampNativeFunction.setPlayerArmour( id, armour );
		this.armour = armour;
	}
	
	@Override
	public void setAmmo( int weaponslot, int ammo )
	{
		SampNativeFunction.setPlayerAmmo( id, weaponslot, ammo );
	}
	
	@Override
	public void setMoney( int money )
	{
		SampNativeFunction.resetPlayerMoney( id );
		if( money != 0 ) SampNativeFunction.givePlayerMoney( id, money );
		
		this.money = money;
	}
	
	@Override
	public void giveMoney( int money )
	{
		SampNativeFunction.givePlayerMoney( id, money );
		this.money = SampNativeFunction.getPlayerMoney(id);
	}
	
	@Override
	public void setScore( int score )
	{
		SampNativeFunction.setPlayerScore( id, score );
		this.score = score;
	}
	
	@Override
	public void setWeather( int weather )
	{
		SampNativeFunction.setPlayerWeather( id, weather );
		this.weather = weather;
	}
	
	@Override
	public void setFightingStyle( int style )
	{
		SampNativeFunction.setPlayerFightingStyle( id, style );
	}

	@Override
	public void setVehicle( IVehicle vehicle, int seat )
	{
		vehicle.putPlayer( this, seat );
	}
	
	@Override
	public void setVehicle( IVehicle vehicle )
	{
		vehicle.putPlayer( this, 0 );
	}

	@Override
	public void setPosition( float x, float y, float z )
	{
		SampNativeFunction.setPlayerPos( id, x, y, z );

		this.position.x = x;
		this.position.y = y;
		this.position.z = z;
	}
	
	@Override
	public void setPositionFindZ( float x, float y, float z )
	{
		SampNativeFunction.setPlayerPosFindZ( id, x, y, z );

		this.position.x = x;
		this.position.y = y;
		this.position.z = z;
	}

	@Override
	public void setPosition( Point position )
	{
		SampNativeFunction.setPlayerPos( id, position.x, position.y, position.z );

		if( position.interior != this.position.interior )
			SampNativeFunction.setPlayerInterior( id, position.interior );
		
		if( position.world != this.position.world )
			SampNativeFunction.setPlayerVirtualWorld( id, position.world );
		
		this.position.set( position );
	}
	
	@Override
	public void setPositionFindZ( Point position )
	{
		SampNativeFunction.setPlayerPosFindZ( id, position.x, position.y, position.z );

		if( position.interior != this.position.interior )
			SampNativeFunction.setPlayerInterior( id, position.interior );
		
		if( position.world != this.position.world )
			SampNativeFunction.setPlayerVirtualWorld( id, position.world );
		
		this.position.set( position );
	}

	@Override
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
	
	@Override
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
	
	@Override
	public void setAngle( float angle )
	{
		SampNativeFunction.setPlayerFacingAngle( id, angle );
		position.angle = angle;
	}
	
	@Override
	public void setInterior( int interior )
	{
		SampNativeFunction.setPlayerInterior( id, interior );
		position.interior = interior;
	}
	
	@Override
	public void setWorld( int world )
	{
		SampNativeFunction.setPlayerVirtualWorld( id, world );
		position.world = world;
	}
	
	@Override
	public void setWorldBound( Area bound )
	{
		SampNativeFunction.setPlayerWorldBounds( id, bound.maxX, bound.minX, bound.maxY, bound.minY );
		worldBound.set( bound );
	}
	
	@Override
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
	
	@Override
	public void sendChat( IPlayer p, String message )
	{
		Player player = (Player) p;
		
		if( message == null ) throw new NullPointerException();
		SampNativeFunction.sendPlayerMessageToPlayer( player.id, id, message );
	}
	
	@Override
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

	@Override
	public void sendDeathMessage( IPlayer k, int reason )
	{
		Player killer = (Player) k;
		
		if(killer == null)
			SampNativeFunction.sendDeathMessage(Gamemode.INVALID_PLAYER_ID, id, reason);
		else
			SampNativeFunction.sendDeathMessage( killer.id, id, reason );
	}

	@Override
	public void sendGameText( int time, int style, String text )
	{
		if( text == null ) throw new NullPointerException();
		SampNativeFunction.gameTextForPlayer( id, text, time, style );
	}
	
	@Override
	public void sendGameText( int time, int style, String format, Object... args )
	{
		String text = String.format(format, args);
		SampNativeFunction.gameTextForPlayer( id, text, time, style );
	}

	@Override
	public void spawn()
	{
		SampNativeFunction.spawnPlayer( id );
	}
	
	@Override
	public void setDrunkLevel( int level )
	{
		SampNativeFunction.setPlayerDrunkLevel( id, level );
	}
	
	@Override
	public int getDrunkLevel()
	{
		return SampNativeFunction.getPlayerDrunkLevel(id);
	}

	@Override
	public void applyAnimation( String animlib, String animname, float delta, int loop, int lockX, int lockY, int freeze, int time, int forcesync )
	{
		if( animlib == null || animname == null ) throw new NullPointerException();
		SampNativeFunction.applyAnimation( id, animlib, animname, delta, loop, lockX, lockY, freeze, time, forcesync );
	}
	
	@Override
	public void clearAnimations( int forcesync )
	{
		SampNativeFunction.clearAnimations( id, forcesync );
	}
	
	@Override
	public int getAnimationIndex()
	{
		return SampNativeFunction.getPlayerAnimationIndex(id);
	}
	
	@Override
	public void allowTeleport( boolean allow )
	{
		SampNativeFunction.allowPlayerTeleport(id, allow);
	}

	@Override
	public void playSound( int sound, float x, float y, float z )
	{
		SampNativeFunction.playerPlaySound( id, sound, x, y, z );
	}
	
	@Override
	public void playSound( int sound, Point point )
	{
		SampNativeFunction.playerPlaySound( id, sound, point.x, point.y, point.z );
	}
	
	public void markerForPlayer( IPlayer p, Color color )
	{
		Player player = (Player) p;
		SampNativeFunction.setPlayerMarkerForPlayer( id, player.id, color.getValue() );
	}
	
	@Override
	public void showNameTagForPlayer( IPlayer p, boolean show )
	{
		Player player = (Player) p;
		SampNativeFunction.showPlayerNameTagForPlayer( id, player.id, show );
	}

	@Override
	public void kick()
	{
		SampNativeFunction.kick( id );
	}
	
	@Override
	public void ban()
	{
		SampNativeFunction.ban( id );
	}
	
	@Override
	public void banEx( String reason )
	{
		if( reason == null ) throw new NullPointerException();
		SampNativeFunction.banEx( id, reason );
	}
	
	@Override
	public Menu getMenu()
	{
		return Gamemode.instance.menuPool[ SampNativeFunction.getPlayerMenu(id) ];
	}

	@Override
	public void setCameraPos( float x, float y, float z )
	{
		SampNativeFunction.setPlayerCameraPos( id, x, y, z );
	}
	
	@Override
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
	
	public boolean isInVehicle( IVehicle v )
	{
		Vehicle vehicle = (Vehicle) v;
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
	
	public boolean isStreamedIn(IPlayer fp)
	{
		Player forplayer = (Player) fp;
		return SampNativeFunction.isPlayerStreamedIn(id, forplayer.id);
	}
	
	public void setCheckpoint( ICheckpoint checkpoint )
	{
		checkpoint.set( this );
	}
	
	public void disableCheckpoint()
	{
		SampNativeFunction.disablePlayerCheckpoint( id );
		checkpoint = null;
	}
	
	public void setRaceCheckpoint( IRaceCheckpoint checkpoint )
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
		this.team = team;
	}
	
	public void setSkin( int skin )
	{
		SampNativeFunction.setPlayerSkin( id, skin );
		this.skin = skin;
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
		wantedLevel = level;
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
	
	public IVehicle getSurfingVehicle()
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
	
	public void spectatePlayer(IPlayer p, int mode)
	{
		Player player = (Player) p;
		if( !spectating ) return;
		
		SampNativeFunction.playerSpectatePlayer(id, player.id, mode);
		spectatingPlayer = player;
		spectatingVehicle = null;
	}
	
	public void spectateVehicle(IVehicle v, int mode)
	{
		Vehicle vehicle = (Vehicle) v;
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
		
		if(objectid != 65535)
			return Gamemode.instance.objectPool[objectid];
		
		return null;
	}
	
	public String getNetworkStats()
	{
		return SampNativeFunction.getPlayerNetworkStats(id);
	}
}
