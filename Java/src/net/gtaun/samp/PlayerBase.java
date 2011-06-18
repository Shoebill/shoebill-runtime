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

import java.util.Iterator;
import java.util.Vector;

import net.gtaun.event.EventDispatcher;
import net.gtaun.event.IEventDispatcher;
import net.gtaun.samp.data.Area;
import net.gtaun.samp.data.KeyState;
import net.gtaun.samp.data.PlayerState;
import net.gtaun.samp.data.Point;
import net.gtaun.samp.data.PointAngle;
import net.gtaun.samp.data.SpawnInfo;
import net.gtaun.samp.data.Speed;
import net.gtaun.samp.event.DialogResponseEvent;
import net.gtaun.samp.event.MenuExitedEvent;
import net.gtaun.samp.event.MenuSelectedEvent;
import net.gtaun.samp.event.PlayerClickPlayerEvent;
import net.gtaun.samp.event.PlayerCommandEvent;
import net.gtaun.samp.event.PlayerDeathEvent;
import net.gtaun.samp.event.PlayerDisconnectEvent;
import net.gtaun.samp.event.PlayerEnterCheckpointEvent;
import net.gtaun.samp.event.PlayerEnterExitModShopEvent;
import net.gtaun.samp.event.PlayerEnterRaceCheckpointEvent;
import net.gtaun.samp.event.PlayerInteriorChangeEvent;
import net.gtaun.samp.event.PlayerKeyStateChangeEvent;
import net.gtaun.samp.event.PlayerKillEvent;
import net.gtaun.samp.event.PlayerLeaveCheckpointEvent;
import net.gtaun.samp.event.PlayerLeaveRaceCheckpointEvent;
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
 * @author MK124
 *
 */

public class PlayerBase
{
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

	public static final int WEAPONSKILL_PISTOL = 					0;
	public static final int WEAPONSKILL_PISTOL_SILENCED =			1;
	public static final int WEAPONSKILL_DESERT_EAGLE =				2;
	public static final int WEAPONSKILL_SHOTGUN =					3;
	public static final int WEAPONSKILL_SAWNOFF_SHOTGUN =			4;
	public static final int WEAPONSKILL_SPAS12_SHOTGUN =			5;
	public static final int WEAPONSKILL_MICRO_UZI =					6;
	public static final int WEAPONSKILL_MP5 =						7;
	public static final int WEAPONSKILL_AK47 =						8;
	public static final int WEAPONSKILL_M4 =						9;
	public static final int WEAPONSKILL_SNIPERRIFLE =				10;

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
	
	
	int id = -1, ping;
	String ip;
	String playerName;
	SpawnInfo spawnInfo = new SpawnInfo();
	int color;

	int frame = -1;
	float health, armour;
	int money, score;
	int weather, fightingStyle;
//	VehicleBase vehicle;

	PointAngle position = new PointAngle();
	Area worldBound = new Area(-20000.0f, -20000.0f, 20000.0f, 20000.0f);
	Speed speed = new Speed();
	PlayerState state = new PlayerState();
	KeyState keyState = new KeyState();
	
	DialogBase dialog;
	

	public int id()					{ return id; }
	public int ping()				{ return ping; }
	public int codepage()			{ return NativeFunction.getPlayerCodepage(id); };
	public String ip()				{ return ip; }
	public String name()			{ return playerName; }
	public SpawnInfo spawnInfo()	{ return spawnInfo.clone(); }
	public int color()				{ return color; }

	public int frame()				{ return frame; }
	public float health()			{ return health; }
	public float armour()			{ return armour; }
	public int money()				{ return money; }
	public int score()				{ return score; }
	public int weather()			{ return weather; }
	public int fightingStyle()		{ return fightingStyle; }
	public VehicleBase vehicle()	{ return VehicleBase.get(VehicleBase.class, NativeFunction.getPlayerVehicleID(id)); }
	
	public PointAngle position()	{ return position.clone(); }
	public Area worldBound()		{ return worldBound.clone(); }
	public Speed speed()			{ return speed.clone(); }
	public PlayerState state()		{ return state.clone(); }
	public KeyState keyState()		{ return keyState.clone(); }
	
	public DialogBase dialog()		{ return dialog; }
	
	
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
	EventDispatcher<PlayerEnterCheckpointEvent>		eventEnterCheckpoint = new EventDispatcher<PlayerEnterCheckpointEvent>();
	EventDispatcher<PlayerLeaveCheckpointEvent>		eventLeaveCheckpoint = new EventDispatcher<PlayerLeaveCheckpointEvent>();
	EventDispatcher<PlayerEnterRaceCheckpointEvent>	eventEnterRaceCheckpoint = new EventDispatcher<PlayerEnterRaceCheckpointEvent>();
	EventDispatcher<PlayerLeaveRaceCheckpointEvent>	eventLeaveRaceCheckpoint = new EventDispatcher<PlayerLeaveRaceCheckpointEvent>();
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
	public IEventDispatcher<PlayerEnterCheckpointEvent>		eventEnterCheckpoint() 		{ return eventEnterCheckpoint; }
	public IEventDispatcher<PlayerLeaveCheckpointEvent>		eventLeaveCheckpoint() 		{ return eventLeaveCheckpoint; }
	public IEventDispatcher<PlayerEnterRaceCheckpointEvent>	eventEnterRaceCheckpoint() 	{ return eventEnterRaceCheckpoint; }
	public IEventDispatcher<PlayerLeaveRaceCheckpointEvent>	eventLeaveRaceCheckpoint() 	{ return eventLeaveRaceCheckpoint; }
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
		playerName = NativeFunction.getPlayerName(id);
		color = NativeFunction.getPlayerColor(id);
		
		health = NativeFunction.getPlayerHealth(id);
		armour = NativeFunction.getPlayerArmour(id);
		money = NativeFunction.getPlayerMoney(id);
		score = NativeFunction.getPlayerScore(id);
		fightingStyle = NativeFunction.getPlayerFightingStyle(id);
		
		NativeFunction.getPlayerPos(id, position);
		NativeFunction.getPlayerFacingAngle(id);
		
		position.interior = NativeFunction.getPlayerInterior(id);
		position.world = NativeFunction.getPlayerVirtualWorld(id);
		
		NativeFunction.getPlayerVelocity(id, speed);
		
		state.state = NativeFunction.getPlayerState(id);
		NativeFunction.getPlayerKeys(id, keyState );
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
		NativeFunction.getPlayerVelocity( id, speed );
		
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
		
		playerName = name;
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
		fightingStyle = style;
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
	
	public void setInteriorId( int interiorId )
	{
		NativeFunction.setPlayerInterior( id, interiorId );
		position.interior = interiorId;
	}
	
	public void setWorldId( int worldId )
	{
		NativeFunction.setPlayerVirtualWorld( id, worldId );
		position.world = worldId;
	}
	
	public void setWorldBound( Area bound )
	{
		NativeFunction.setPlayerWorldBounds( id, bound.maxX, bound.minX, bound.maxY, bound.minY );
		worldBound.set( bound );
	}
	
	public void setSpeed( Speed spd )
	{
		NativeFunction.setPlayerVelocity( id, spd.x, spd.y, spd.z );
		speed.set( spd );
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

	public void applyAnimation( String animlib, String animname, float delta, int loop, int lockX, int lockY, int freeze, int time, int forcesync )
	{
		NativeFunction.applyAnimation( id, animlib, animname, delta, loop, lockX, lockY, freeze, time, forcesync );
	}
	
	public void clearAnimations( int forcesync )
	{
		NativeFunction.clearAnimations( id, forcesync );
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

	public void setCamera( Point pos, Point lookat )
	{
		NativeFunction.setPlayerCameraPos( id, pos.x, pos.y, pos.z );
		NativeFunction.setPlayerCameraLookAt( id, lookat.x, lookat.y, lookat.z );
	}
}














