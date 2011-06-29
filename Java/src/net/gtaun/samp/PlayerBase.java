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

package net.gtaun.samp;

import java.util.Iterator;
import java.util.Vector;

import net.gtaun.event.EventDispatcher;
import net.gtaun.event.IEventDispatcher;
import net.gtaun.samp.data.Area;
import net.gtaun.samp.data.KeyState;
import net.gtaun.samp.data.Point;
import net.gtaun.samp.data.PointAngle;
import net.gtaun.samp.data.SpawnInfo;
import net.gtaun.samp.data.Time;
import net.gtaun.samp.data.Velocity;
import net.gtaun.samp.event.DialogResponseEvent;
import net.gtaun.samp.event.MenuExitedEvent;
import net.gtaun.samp.event.MenuSelectedEvent;
import net.gtaun.samp.event.PlayerClickPlayerEvent;
import net.gtaun.samp.event.PlayerCommandEvent;
import net.gtaun.samp.event.PlayerDeathEvent;
import net.gtaun.samp.event.PlayerDisconnectEvent;
import net.gtaun.samp.event.CheckpointEnterEvent;
import net.gtaun.samp.event.PlayerEnterExitModShopEvent;
import net.gtaun.samp.event.RaceCheckpointEnterEvent;
import net.gtaun.samp.event.PlayerInteriorChangeEvent;
import net.gtaun.samp.event.PlayerKeyStateChangeEvent;
import net.gtaun.samp.event.PlayerKillEvent;
import net.gtaun.samp.event.CheckpointLeaveEvent;
import net.gtaun.samp.event.RaceCheckpointLeaveEvent;
import net.gtaun.samp.event.PlayerObjectMovedEvent;
import net.gtaun.samp.event.PlayerPickupEvent;
import net.gtaun.samp.event.PlayerRequestClassEvent;
import net.gtaun.samp.event.PlayerRequestSpawnEvent;
import net.gtaun.samp.event.PlayerSpawnEvent;
import net.gtaun.samp.event.PlayerStateChangeEvent;
import net.gtaun.samp.event.PlayerStreamInEvent;
import net.gtaun.samp.event.PlayerStreamOutEvent;
import net.gtaun.samp.event.PlayerTextEvent;
import net.gtaun.samp.event.PlayerUpdateEvent;
import net.gtaun.samp.event.VehicleEnterEvent;
import net.gtaun.samp.event.VehicleExitEvent;
import net.gtaun.samp.event.VehicleModEvent;
import net.gtaun.samp.event.VehiclePaintjobEvent;
import net.gtaun.samp.event.VehicleResprayEvent;
import net.gtaun.samp.event.VehicleStreamInEvent;
import net.gtaun.samp.event.VehicleStreamOutEvent;
import net.gtaun.samp.exception.AlreadyExistException;
import net.gtaun.samp.exception.IllegalLengthException;

/**
 * @author MK124, JoJLlmAn
 *
 */

public class PlayerBase
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
	
	public static <T> Vector<T> get( Class<T> cls )
	{
		return GameModeBase.getInstances(GameModeBase.instance.playerPool, cls);
	}
	
	public static <T> T get( Class<T> cls, int id )
	{
		return GameModeBase.getInstance(GameModeBase.instance.playerPool, cls, id);
	}
	
	public static int getMaxPlayers()
	{
		return NativeFunction.getMaxPlayers();
	}
	
	public static void enableStuntBonusForAll( boolean enabled )
	{
		NativeFunction.enableStuntBonusForAll( enabled );
		
		Vector<PlayerBase> players = PlayerBase.get(PlayerBase.class);
		Iterator<PlayerBase> iterator = players.iterator();
		while( iterator.hasNext() )
		{
			PlayerBase player = iterator.next();
			player.stuntBonus = enabled;
		}
	}
	
	public static void sendMessageToAll( int color, String message )
	{
		Vector<PlayerBase> players = get(PlayerBase.class);
		Iterator<PlayerBase> iterator = players.iterator();
		while( iterator.hasNext() )
		{
			PlayerBase player = iterator.next();
			player.sendMessage( color, message );
		}
	}
	
	public static void gameTextToAll( String text, int time, int style )
	{
		NativeFunction.gameTextForAll( text, time, style );
	}
	
	
	int id = -1, ping, team, skin, wantedLevel;
	String ip;
	String name;
	SpawnInfo spawnInfo = new SpawnInfo();
	int color;
	
	boolean controllable = true;
	boolean stuntBonus = false;
	boolean spectating = false;
	boolean recording = false;
	
	PlayerBase spectatingPlayer;
	VehicleBase spectatingVehicle;

	int frame = -1;
	float health, armour;
	int money, score;
	int weather;
//	VehicleBase vehicle;

	PointAngle position = new PointAngle();
	Area worldBound = new Area(-20000.0f, -20000.0f, 20000.0f, 20000.0f);
	Velocity velocity = new Velocity();
	int state = STATE_NONE;
	KeyState keyState = new KeyState();
	PlayerAttach playerAttach;
	PlayerSkill skill;
	CheckpointBase checkpoint;
	RaceCheckpointBase raceCheckpoint;
	
	DialogBase dialog;
	

	public int id()							{ return id; }
	public int ping()						{ return ping; }
	public int team()						{ return team; }
	public int skin()						{ return skin; }
	public int wantedLevel()				{ return wantedLevel; }
	public int codepage()					{ return NativeFunction.getPlayerCodepage(id); };
	public String ip()						{ return ip; }
	public String name()					{ return name; }
	public SpawnInfo spawnInfo()			{ return spawnInfo.clone(); }
	public int color()						{ return color; }

	public int frame()						{ return frame; }
	public float health()					{ return health; }
	public float armour()					{ return armour; }
	public int weapon()						{ return NativeFunction.getPlayerWeapon(id); }
	public int ammo()						{ return NativeFunction.getPlayerAmmo(id); }
	public int money()						{ return money; }
	public int score()						{ return score; }
	public int weather()					{ return weather; }
	public int fightingStyle()				{ return NativeFunction.getPlayerFightingStyle(id); }
	public VehicleBase vehicle()			{ return VehicleBase.get(VehicleBase.class, NativeFunction.getPlayerVehicleID(id)); }
	public int seat()						{ return NativeFunction.getPlayerVehicleSeat(id); }
	public boolean controllable()			{ return controllable; }
	public int specialAction()				{ return NativeFunction.getPlayerSpecialAction(id); }
	public boolean stuntBonus()				{ return stuntBonus; }
	public boolean spectating()				{ return spectating; }
	public PlayerBase spectatingPlayer()	{ return spectatingPlayer; }
	public VehicleBase spectatingVehicle()	{ return spectatingVehicle; }
	public boolean recording()				{ return recording; }
	
	public PointAngle position()			{ return position.clone(); }
	public float angle()					{ return position.angle; }
	public float interior()					{ return position.interior; }
	public float world()					{ return position.world; }
	public Area worldBound()				{ return worldBound.clone(); }
	public Velocity velocity()				{ return velocity.clone(); }
	public int state()						{ return state; }
	public KeyState keyState()				{ return keyState.clone(); }
	public PlayerAttach playerAttach()		{ return playerAttach; }
	public PlayerSkill skill()				{ return skill; }
	public CheckpointBase checkpoint()			{ return checkpoint; }
	public RaceCheckpointBase raceCheckpoint()	{ return raceCheckpoint; }
	
	public DialogBase dialog()				{ return dialog; }
	
	
	EventDispatcher<PlayerDisconnectEvent>			eventDisconnect = new EventDispatcher<PlayerDisconnectEvent>();
	EventDispatcher<PlayerRequestSpawnEvent>		eventRequestSpawn = new EventDispatcher<PlayerRequestSpawnEvent>();
	EventDispatcher<PlayerSpawnEvent>				eventSpawn = new EventDispatcher<PlayerSpawnEvent>();
	EventDispatcher<PlayerKillEvent>				eventKill = new EventDispatcher<PlayerKillEvent>();
	EventDispatcher<PlayerDeathEvent>				eventDeath = new EventDispatcher<PlayerDeathEvent>();
	EventDispatcher<PlayerTextEvent>				eventText = new EventDispatcher<PlayerTextEvent>();
	EventDispatcher<PlayerCommandEvent>				eventCommand = new EventDispatcher<PlayerCommandEvent>();
	EventDispatcher<PlayerRequestClassEvent>		eventRequestClass = new EventDispatcher<PlayerRequestClassEvent>();
	EventDispatcher<PlayerUpdateEvent>				eventUpdate = new EventDispatcher<PlayerUpdateEvent>();
	EventDispatcher<PlayerStateChangeEvent>			eventStateChange = new EventDispatcher<PlayerStateChangeEvent>();
	EventDispatcher<CheckpointEnterEvent>			eventEnterCheckpoint = new EventDispatcher<CheckpointEnterEvent>();
	EventDispatcher<CheckpointLeaveEvent>			eventLeaveCheckpoint = new EventDispatcher<CheckpointLeaveEvent>();
	EventDispatcher<RaceCheckpointEnterEvent>		eventEnterRaceCheckpoint = new EventDispatcher<RaceCheckpointEnterEvent>();
	EventDispatcher<RaceCheckpointLeaveEvent>		eventLeaveRaceCheckpoint = new EventDispatcher<RaceCheckpointLeaveEvent>();
	EventDispatcher<PlayerObjectMovedEvent>			eventObjectMoved = new EventDispatcher<PlayerObjectMovedEvent>();
	EventDispatcher<PlayerPickupEvent>				eventPickup = new EventDispatcher<PlayerPickupEvent>();
	EventDispatcher<PlayerEnterExitModShopEvent>	eventEnterExitModShop = new EventDispatcher<PlayerEnterExitModShopEvent>();
	EventDispatcher<PlayerInteriorChangeEvent>		eventInteriorChange = new EventDispatcher<PlayerInteriorChangeEvent>();
	EventDispatcher<PlayerKeyStateChangeEvent>		eventKeyStateChange = new EventDispatcher<PlayerKeyStateChangeEvent>();
	EventDispatcher<PlayerStreamInEvent>			eventPlayerStreamIn = new EventDispatcher<PlayerStreamInEvent>();
	EventDispatcher<PlayerStreamOutEvent>			eventPlayerStreamOut = new EventDispatcher<PlayerStreamOutEvent>();
	EventDispatcher<PlayerClickPlayerEvent>			eventClickPlayer = new EventDispatcher<PlayerClickPlayerEvent>();
	EventDispatcher<PlayerClickPlayerEvent>			eventOthersClick = new EventDispatcher<PlayerClickPlayerEvent>();
	EventDispatcher<VehicleEnterEvent>				eventEnterVehicle = new EventDispatcher<VehicleEnterEvent>();
	EventDispatcher<VehicleExitEvent>				eventExitVehicle = new EventDispatcher<VehicleExitEvent>();
	EventDispatcher<VehicleModEvent>				eventVehicleMod = new EventDispatcher<VehicleModEvent>();
	EventDispatcher<VehiclePaintjobEvent>			eventVehiclePaintjob = new EventDispatcher<VehiclePaintjobEvent>();
	EventDispatcher<VehicleResprayEvent>			eventVehicleRespray = new EventDispatcher<VehicleResprayEvent>();
	EventDispatcher<VehicleStreamInEvent>			eventVehicleStreamIn = new EventDispatcher<VehicleStreamInEvent>();
	EventDispatcher<VehicleStreamOutEvent>			eventVehicleStreamOut = new EventDispatcher<VehicleStreamOutEvent>();
	EventDispatcher<DialogResponseEvent>			eventDialogResponse = new EventDispatcher<DialogResponseEvent>();
	EventDispatcher<MenuSelectedEvent>				eventMenuSelected = new EventDispatcher<MenuSelectedEvent>();
	EventDispatcher<MenuExitedEvent>				eventMenuExited = new EventDispatcher<MenuExitedEvent>();

	public IEventDispatcher<PlayerDisconnectEvent>			eventDisconnect() 			{ return eventDisconnect; }
	public IEventDispatcher<PlayerRequestSpawnEvent>		eventRequestSpawn() 		{ return eventRequestSpawn; }
	public IEventDispatcher<PlayerSpawnEvent>				eventSpawn() 				{ return eventSpawn; }		
	public IEventDispatcher<PlayerKillEvent>				eventKill() 				{ return eventKill; }
	public IEventDispatcher<PlayerDeathEvent>				eventDeath() 				{ return eventDeath; }
	public IEventDispatcher<PlayerTextEvent>				eventText() 				{ return eventText; }
	public IEventDispatcher<PlayerCommandEvent>				eventCommand() 				{ return eventCommand; }
	public IEventDispatcher<PlayerRequestClassEvent>		eventRequestClass() 		{ return eventRequestClass; }
	public IEventDispatcher<PlayerUpdateEvent>				eventUpdate() 				{ return eventUpdate; }
	public IEventDispatcher<PlayerStateChangeEvent>			eventStateChange() 			{ return eventStateChange; }
	public IEventDispatcher<CheckpointEnterEvent>		eventEnterCheckpoint() 		{ return eventEnterCheckpoint; }
	public IEventDispatcher<CheckpointLeaveEvent>		eventLeaveCheckpoint() 		{ return eventLeaveCheckpoint; }
	public IEventDispatcher<RaceCheckpointEnterEvent>	eventEnterRaceCheckpoint() 	{ return eventEnterRaceCheckpoint; }
	public IEventDispatcher<RaceCheckpointLeaveEvent>	eventLeaveRaceCheckpoint() 	{ return eventLeaveRaceCheckpoint; }
	public IEventDispatcher<PlayerObjectMovedEvent>			eventObjectMoved() 			{ return eventObjectMoved; }
	public IEventDispatcher<PlayerPickupEvent>				eventPickup() 				{ return eventPickup; }
	public IEventDispatcher<PlayerEnterExitModShopEvent>	eventEnterExitModShop() 	{ return eventEnterExitModShop; }
	public IEventDispatcher<PlayerInteriorChangeEvent>		eventInteriorChange() 		{ return eventInteriorChange; }
	public IEventDispatcher<PlayerKeyStateChangeEvent>		eventKeyStateChange() 		{ return eventKeyStateChange; }
	public IEventDispatcher<PlayerStreamInEvent>			eventStreamInEvent() 		{ return eventPlayerStreamIn; }
	public IEventDispatcher<PlayerStreamOutEvent>			eventStreamOut() 			{ return eventPlayerStreamOut; }
	public IEventDispatcher<PlayerClickPlayerEvent>			eventClickPlayer() 			{ return eventClickPlayer; }
	public IEventDispatcher<PlayerClickPlayerEvent>			eventOthersClick() 				{ return eventOthersClick; }
	public IEventDispatcher<VehicleEnterEvent>				eventEnterVehicle() 		{ return eventEnterVehicle; }
	public IEventDispatcher<VehicleExitEvent>				eventExitVehicle() 			{ return eventExitVehicle; }
	public IEventDispatcher<VehicleModEvent>				eventVehicleMod() 			{ return eventVehicleMod; }
	public IEventDispatcher<VehiclePaintjobEvent>			eventVehiclePaintjob() 		{ return eventVehiclePaintjob; }
	public IEventDispatcher<VehicleResprayEvent>			eventVehicleRespray() 		{ return eventVehicleRespray; }
	public IEventDispatcher<VehicleStreamInEvent>			eventVehicleStreamIn() 		{ return eventVehicleStreamIn; }
	public IEventDispatcher<VehicleStreamOutEvent>			eventVehicleStreamOut() 	{ return eventVehicleStreamOut; }
	public IEventDispatcher<DialogResponseEvent>			eventDialogResponse() 		{ return eventDialogResponse; }
	public IEventDispatcher<MenuSelectedEvent>				eventMenuSelected() 		{ return eventMenuSelected; }
	public IEventDispatcher<MenuExitedEvent>				eventMenuExited() 			{ return eventMenuExited; }

	
	protected PlayerBase()
	{
		id = GameModeBase.instance.currentPlayerId;
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
	}


//---------------------------------------------------------
	
	protected int onDisconnect( int reason )
	{
		return 1;
	}

	protected int onRequestSpawn()
	{
		return 1;
	}
	
	protected int onSpawn()
	{
		return 1;
	}

	protected int onKill( PlayerBase victim, int reason )
	{
		return 1;
	}
	
	protected int onDeath( PlayerBase killer, int reason )
	{
		return 1;
	}
	
	protected int onText( String text )
	{
		return 1;
	}

	protected int onCommand( String command )
	{
		return 0;
	}

	protected int onRequestClass( int classid )
	{
		return 1;
	}

	protected int onUpdate()
	{
		return 1;
	}

	protected int onStateChange( int oldstate )
	{
		
		return 1;
	}

	protected int onEnterCheckpoint()
	{
		return 1;
	}

	protected int onLeaveCheckpoint()
	{
		return 1;
	}

	protected int onEnterRaceCheckpoint()
	{
		return 1;
	}

	protected int onLeaveRaceCheckpoint()
	{
		return 1;
	}
	
	protected int onObjectMoved( ObjectBase object )
	{
		return 1;
	}

	protected int onPickup( PickupBase pickup )
	{
		return 1;
	}
	
	protected int onEnterExitModShop( boolean enterexit, int interiorid )
	{
		return 1;
	}
	
	protected int onInteriorChange( int oldinteriorid )
	{
		return 1;
	}

	protected int onKeyStateChange( int oldkeys )
	{
		return 1;
	}
	
	protected int onPlayerStreamIn( PlayerBase forPlayer )
	{
		return 1;
	}

	protected int onPlayerStreamOut( PlayerBase forPlayer )
	{
		return 1;
	}

	protected int onClickPlayer( PlayerBase clickedplayer, int source )
	{
		return 1;
	}
	
	protected int onOthersClick( PlayerBase player, int source )
	{
		
		return 1;
	}

	protected int onEnterVehicle( VehicleBase vehicle, boolean ispassenger )
	{
		return 1;
	}

	protected int onExitVehicle( VehicleBase vehicle )
	{
		return 1;
	}

	protected int onVehicleMod( int componentid )
	{
		return 1;
	}

	protected int onVehiclePaintjob( int paintjobid )
	{
		return 1;
	}

	protected int onVehicleRespray( int color1, int color2 )
	{
		return 1;
	}

	protected int onVehicleStreamIn( VehicleBase vehicle )
	{
		return 1;
	}

	protected int onVehicleStreamOut( VehicleBase vehicle )
	{
		return 1;
	}

	protected int onDialogResponse( DialogBase dialog, int response, int listitem, String inputtext )
	{
		return 1;
	}

	protected int onMenuSelected( MenuBase menu, int row )
	{
		return 1;
	}

	protected int onMenuExited( MenuBase menu )
	{
		return 1;
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
		
		frame++;
		if( frame<0 ) frame = 0;
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
		NativeFunction.givePlayerMoney( id, money );
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

	public void setVehicle( VehicleBase vehicle, int seat )
	{
		vehicle.putPlayer( this, seat );
	}
	
	public void setVehicle( VehicleBase vehicle )
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
		NativeFunction.sendClientMessage( id, color, message );
	}
	
	public void sendChat( PlayerBase player, String message )
	{
		NativeFunction.sendPlayerMessageToPlayer( player.id, id, message );
	}
	
	public void sendChatToAll( String message )
	{
		Vector<PlayerBase> players = get(PlayerBase.class);
		Iterator<PlayerBase> iterator = players.iterator();
		while( iterator.hasNext() )
		{
			PlayerBase player = iterator.next();
			sendChat( player, message );
		}
	}

	public void sendDeathMessage( PlayerBase killer, int reason )
	{
		if(killer == null)
			NativeFunction.sendDeathMessage(GameModeBase.INVALID_PLAYER_ID, id, reason);
		else
			NativeFunction.sendDeathMessage( killer.id, id, reason );
	}
	
	public void gameText( String text, int time, int style )
	{
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
	
	public void getAnimationName(int index, String lib, String name)
	{
		NativeFunction.getAnimationName(index, lib, lib.length(), name, name.length());
	}
	
	public void allowRightclickTeleport( boolean allow )
	{
		NativeFunction.allowAdminTeleport( allow );
	}

	public void playSound( int sound, float x, float y, float z )
	{
		NativeFunction.playerPlaySound( id, sound, x, y, z );
	}
	
	public void playSound( int sound, Point point )
	{
		NativeFunction.playerPlaySound( id, sound, point.x, point.y, point.z );
	}
	
	public void markerForPlayer( PlayerBase player, int color )
	{
		NativeFunction.setPlayerMarkerForPlayer( id, player.id, color );
	}
	
	public void nameTagForPlayer( PlayerBase player, boolean show )
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
		NativeFunction.banEx( id, reason );
	}
	
	public MenuBase getMenu()
	{
		return GameModeBase.instance.menuPool[ NativeFunction.getPlayerMenu(id) ];
	}
	
	public void setCameraPos( Point pos )
	{
		NativeFunction.setPlayerCameraPos( id, pos.x, pos.y, pos.z );
	}
	
	public void setCamerLookAt( Point lookat )
	{
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
	
//--------------------------------------------------------- Is Functions
	
	public boolean isInVehicle()
	{
		return NativeFunction.isPlayerInAnyVehicle( id );
	}
	
	public boolean isInVehicle(VehicleBase vehicle)
	{
		return NativeFunction.isPlayerInVehicle( id, vehicle.id );
	}
	
	public boolean isAdmin()
	{
		return NativeFunction.isPlayerAdmin(id);
	}
	
	public boolean isInRangeOfPoint(Point point, int range)
	{
		return NativeFunction.isPlayerInRangeOfPoint(id, range, point.x, point.y, point.z);
	}
	
	public boolean isStreamedIn(PlayerBase forplayer)
	{
		return NativeFunction.isPlayerStreamedIn(id, forplayer.id);
	}
	
	public void setCheckpoint( CheckpointBase checkpoint )
	{
		checkpoint.set( this );
	}
	
	public void disableCheckpoint()
	{
		NativeFunction.disablePlayerCheckpoint( id );
		checkpoint = null;
	}
	
	public void setRaceCheckpoint( RaceCheckpointBase checkpoint )
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
		return NativeFunction.getPlayerWeaponState(id);
	}
	
	public void setTeam(int team)
	{
		NativeFunction.setPlayerTeam(id, team);
		this.team = team;
	}
	
	public void setSkin(int skin)
	{
		NativeFunction.setPlayerSkin(id, skin);
		this.skin = skin;
	}
	
	public void giveWeapon(int weaponid, int ammo)
	{
		NativeFunction.givePlayerWeapon(id, weaponid, ammo);
	}
	
	public void resetWeapons()
	{
		NativeFunction.resetPlayerWeapons(id);
	}
	
	public void setTime(int hour, int minute)
	{
		NativeFunction.setPlayerTime(id, hour, minute);
	}
	
	public Time getTime()
	{
		Time time = new Time();
		NativeFunction.getPlayerTime(id, time);
		return time;
	}
	
	public void toggleClock(boolean toggle)
	{
		NativeFunction.togglePlayerClock(id, toggle);
	}
	
	public void forceClassSelection()
	{
		NativeFunction.forceClassSelection(id);
	}
	
	public void setWantedLevel(int level)
	{
		NativeFunction.setPlayerWantedLevel(id, level);
		wantedLevel = level;
	}
	
	public void playCrimeReport(int suspectid, int crimeid)
	{
		NativeFunction.playCrimeReportForPlayer(id, suspectid, crimeid);
	}
	
	public void setShopName(String shop)
	{
		NativeFunction.setPlayerShopName(id, shop);
	}
	
	public VehicleBase getSurfingVehicle()
	{
		return VehicleBase.get(VehicleBase.class, NativeFunction.getPlayerSurfingVehicleID(id));
	}
	
	public void removeFromVehicle()
	{
		NativeFunction.removePlayerFromVehicle(id);
	}
	
	public void toggleControllable(boolean toggle)
	{
		NativeFunction.togglePlayerControllable(id, toggle);
		controllable = toggle;
	}
	
	public void setSpecialAction(int action)
	{
		NativeFunction.setPlayerSpecialAction(id, action);
	}
	
	public void setMapIcon(int iconid, Point point, int markertype, int color, int style)
	{
		NativeFunction.setPlayerMapIcon(id, iconid, point.x, point.y, point.z, markertype, color, style);
	}
	
	public void removeMapIcon(int iconid)
	{
		NativeFunction.removePlayerMapIcon(id, iconid);
	}
	
	public void enableStuntBonus(boolean enable)
	{
		if(enable)
			NativeFunction.enableStuntBonusForPlayer(id, 1);
		else
			NativeFunction.enableStuntBonusForPlayer(id, 0);
		stuntBonus = enable;
	}
	
	public void toggleSpectating(boolean toggle)
	{
		NativeFunction.togglePlayerSpectating(id, toggle);
		spectating = toggle;
		
		if(!toggle)
		{
			spectatingPlayer = null;
			spectatingVehicle = null;
		}
	}
	
	public void spectatePlayer(PlayerBase player, int mode)
	{
		if(spectating){
			NativeFunction.playerSpectatePlayer(id, player.id, mode);
			spectatingPlayer = player;
			spectatingVehicle = null;
		}
	}
	
	public void spectateVehicle(VehicleBase vehicle, int mode)
	{
		if(spectating){
			NativeFunction.playerSpectateVehicle(id, vehicle.id, mode);
			spectatingPlayer = null;
			spectatingVehicle = vehicle;
		}
	}
	
	public void startRecord(int type, String recordName)
	{
		NativeFunction.startRecordingPlayerData(id, type, recordName);
		recording = true;
	}
	
	public void stopRecord()
	{
		NativeFunction.stopRecordingPlayerData(id);
		recording = false;
	}
}
