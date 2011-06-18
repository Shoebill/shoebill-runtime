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

import java.util.HashMap;
import java.util.Vector;

import net.gtaun.event.EventDispatcher;
import net.gtaun.samp.data.Color;
import net.gtaun.samp.data.SpawnInfo;
import net.gtaun.samp.event.DialogResponseEvent;
import net.gtaun.samp.event.GameModeExitEvent;
import net.gtaun.samp.event.PlayerClickPlayerEvent;
import net.gtaun.samp.event.PlayerCommandEvent;
import net.gtaun.samp.event.PlayerConnectEvent;
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
import net.gtaun.samp.event.RconCommandEvent;
import net.gtaun.samp.event.RconLoginEvent;
import net.gtaun.samp.event.TickEvent;
import net.gtaun.samp.event.VehicleDeathEvent;
import net.gtaun.samp.event.VehicleEnterEvent;
import net.gtaun.samp.event.VehicleExitEvent;
import net.gtaun.samp.event.VehicleModEvent;
import net.gtaun.samp.event.VehiclePaintjobEvent;
import net.gtaun.samp.event.VehicleResprayEvent;
import net.gtaun.samp.event.VehicleSpawnEvent;
import net.gtaun.samp.event.VehicleStreamInEvent;
import net.gtaun.samp.event.VehicleStreamOutEvent;
import net.gtaun.samp.event.VehicleUpdateDamageEvent;

/**
 * @author MK124
 *
 */

public abstract class GameModeBase
{
	public static final int MAX_PLAYER_NAME =						24;
	public static final int MAX_PLAYERS =							500;
    public static final int MAX_VEHICLES =							2000;
    public static final int MAX_OBJECTS =							400;
    public static final int MAX_ZONES =								1024;
    public static final int MAX_TEXT_DRAWS =						2048;
    public static final int MAX_MENUS =								128;
    public static final int MAX_LABELS_GLOBAL =						1024;
    public static final int MAX_LABELS_PLAYER =						1024;
    public static final int MAX_PICKUPS =							2048;
	
//----------------------------------------------------------
    
	static GameModeBase instance = null;

	static <T> Vector<T> getInstances(Object[] items, Class<T> cls)
	{
		Vector<T> list = new Vector<T>();
		
		for( int i=0; i<items.length; i++ )
		{
			if( items[i] != null && cls.isInstance(items[i]) )
				list.add( cls.cast(items[i]) );
		}
		
		return list;		
	}

	static <T> T getInstance( Object[] items, Class<T> cls, int id )
	{
		if( items[id] != null && cls.isInstance(items[id]) ) return cls.cast(items[id]);
		return null;		
	}
	

	private Class<?> playerCls = PlayerBase.class;
	
	PlayerBase[] playerPool						= new PlayerBase[MAX_PLAYERS];
	VehicleBase[] vehiclePool					= new VehicleBase[MAX_VEHICLES];
	ObjectBase[] objectPool						= new ObjectBase[MAX_OBJECTS];
	PlayerObjectBase[] playerObjectPool			= new PlayerObjectBase[MAX_OBJECTS*MAX_PLAYERS];
	PickupBase[] pickupPool						= new PickupBase[MAX_PICKUPS];
	LabelBase[] labelPool						= new LabelBase[MAX_LABELS_GLOBAL];
	PlayerLabelBase[] playerLabelPool			= new PlayerLabelBase[MAX_LABELS_PLAYER*MAX_PLAYERS];
	TextdrawBase[] textdrawPool					= new TextdrawBase[MAX_TEXT_DRAWS];
	ZoneBase[] zonePool							= new ZoneBase[MAX_ZONES];
	MenuBase[] menuPool							= new MenuBase[MAX_MENUS];
	TimerBase[] timerPool						= new TimerBase[10000];
	
	HashMap<Integer, DialogBase> dialogPool		= new HashMap<Integer, DialogBase> ();
	// 需要弱引用
	
	int currentPlayerId;
	// PlayerBase() 需要用到
	
	
	EventDispatcher<GameModeExitEvent>		eventExit = new EventDispatcher<GameModeExitEvent>();
	EventDispatcher<PlayerConnectEvent>		eventConnect = new EventDispatcher<PlayerConnectEvent>();
	EventDispatcher<PlayerDisconnectEvent>	eventDisconnect = new EventDispatcher<PlayerDisconnectEvent>();
	EventDispatcher<RconCommandEvent>		eventRconCommand = new EventDispatcher<RconCommandEvent>();
	EventDispatcher<RconLoginEvent>			eventRconLogin = new EventDispatcher<RconLoginEvent>();
	EventDispatcher<TickEvent>				eventTick = new EventDispatcher<TickEvent>();
	
	public EventDispatcher<GameModeExitEvent>		eventExit()				{ return eventExit; }
	public EventDispatcher<PlayerConnectEvent>		eventConnect()			{ return eventConnect; }
	public EventDispatcher<PlayerDisconnectEvent>	eventDisconnect()		{ return eventDisconnect; }
	public EventDispatcher<RconCommandEvent>		eventRconCommand()		{ return eventRconCommand; }
	public EventDispatcher<RconLoginEvent>			eventRconLogin()		{ return eventRconLogin; }
	public EventDispatcher<TickEvent>				eventTick()				{ return eventTick; }
	

	protected GameModeBase()
	{
		init();
	}

	protected GameModeBase( Class<?> playercls )
	{
		playerCls = playercls;
		init();
	}
	
	private void init()
	{
		NativeFunction.loadLibrary();
		instance = this;
		
		System.out.println( "Java: GameModeBase Inited." );
	}
	
	
//--------------------------------------------------------- 通过继承重写来处理事件

	protected int onExit()
	{
		return 1;
	}
	
	protected int onConnect( PlayerBase player )
	{
		return 1;
	}
	
	protected int onDisconnect( PlayerBase player, int reason )
	{
		return 1;
	}
	
	protected int onRconCommand( String command )
	{
		return 0;
	}

	protected int onRconLogin( String ip, String password, boolean success )
	{
		return 1;
	}

	protected void onTick()
	{
		
	}

	
//--------------------------------------------------------- 提供的可爱的函数们
	
	public int getServerCodepage()
	{
		return NativeFunction.getServerCodepage();
	}
	
	public void setServerCodepage( int codepage )
	{
		NativeFunction.setServerCodepage( codepage );
	}
	
	public void setGameModeText( String string )
	{
		NativeFunction.setGameModeText( string );
	}
	
	public void setTeamCount( int count )
	{
		NativeFunction.setTeamCount( count );
	}

	public int addPlayerClass( int model, float x, float y, float z, float angle, int weapon1, int ammo1, int weapon2, int ammo2, int weapon3, int ammo3 )
	{
		return NativeFunction.addPlayerClass( model, x, y, z, angle, weapon1, ammo1, weapon2, ammo2, weapon3, ammo3 );
	}

	public int addPlayerClass( int model, SpawnInfo spawninfo )
	{
		return NativeFunction.addPlayerClass( model, 
			spawninfo.position.x, spawninfo.position.y, spawninfo.position.z, spawninfo.position.angle,
			spawninfo.weapon1.id, spawninfo.weapon1.ammo, spawninfo.weapon2.id, spawninfo.weapon2.ammo,
			spawninfo.weapon3.id, spawninfo.weapon3.ammo );
	}
	
	public int addPlayerClassEx( int team, int model, SpawnInfo spawninfo )
	{
		return NativeFunction.addPlayerClassEx( team, model, 
				spawninfo.position.x, spawninfo.position.y, spawninfo.position.z, spawninfo.position.angle,
				spawninfo.weapon1.id, spawninfo.weapon1.ammo, spawninfo.weapon2.id, spawninfo.weapon2.ammo,
				spawninfo.weapon3.id, spawninfo.weapon3.ammo );
	}

	public void showNameTags( boolean enabled )
	{
		NativeFunction.showNameTags( enabled );
	}
	
	public void showPlayerMarkers( int mode )
	{
		NativeFunction.showPlayerMarkers( mode );
	}
	
	public void setWorldTime( int hour )
	{
		NativeFunction.setWorldTime( hour );
	}
	
	public String getWeaponName( int weaponid )
	{
		return NativeFunction.getWeaponName( weaponid );
	}
	
	public void enableTirePopping( boolean enabled )
	{
		NativeFunction.enableTirePopping( enabled );
	}
	
	public void allowInteriorWeapons( boolean allow )
	{
		NativeFunction.allowInteriorWeapons( allow );
	}
	
	public void setWeather( int weatherid )
	{
		NativeFunction.setWeather( weatherid );
	}
	
	public void setGravity( float gravity )
	{
		NativeFunction.setGravity( gravity );
	}
	
	public void allowAdminTeleport( boolean allow )
	{
		NativeFunction.allowAdminTeleport( allow );
	}
	
	public void setDeathDropAmount( int amount )
	{
		NativeFunction.setDeathDropAmount( amount );
	}
	
	public void createExplosion( float x, float y, float z, int type, float radius )
	{
		NativeFunction.createExplosion( x, y, z, type, radius );
	}
	
	public void enableStuntBonus( boolean enabled )
	{
		NativeFunction.enableStuntBonusForAll( enabled );
	}
	
	public void enableZoneNames( boolean enabled )
	{
		NativeFunction.enableZoneNames( enabled );
	}
	
	public void usePlayerPedAnims()
	{
		NativeFunction.usePlayerPedAnims();
	}
	
	public void disableInteriorEnterExits()
	{
		NativeFunction.disableInteriorEnterExits();
	}
	
	public void setNameTagDrawDistance( float distance )
	{
		NativeFunction.setNameTagDrawDistance( distance );
	}
	
	public void disableNameTagLOS()
	{
		NativeFunction.disableNameTagLOS();
	}
	
	public void limitGlobalChatRadius( float radius )
	{
		NativeFunction.limitGlobalChatRadius( radius );
	}
	
	public void limitPlayerMarkerRadius( float radius )
	{
		NativeFunction.limitPlayerMarkerRadius( radius );
	}

	public void sendRconCommand( String command )
	{
		NativeFunction.sendRconCommand( command );
	}
	
	public String getServerVarAsString( String varname )
	{
		return NativeFunction.getServerVarAsString( varname );
	}
	
	public int getServerVarAsInt( String varname )
	{
		return NativeFunction.getServerVarAsInt( varname );
	}
	
	public boolean getServerVarAsBool( String varname )
	{
		return NativeFunction.getServerVarAsBool( varname );
	}
	
	public void connectNPC( String name, String script )
	{
		NativeFunction.connectNPC( name, script );
	}
	
	
//--------------------------------------------------------- 被JNI呼叫的玩意儿

	void onProcessTick()
	{
		try
		{
			onTick();
			eventTick.dispatchEvent( new TickEvent() );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	
	int onGameModeExit()
	{
		try
		{
			onExit();
			eventExit.dispatchEvent( new GameModeExitEvent(this) );
			
			instance = null;
			return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	int onPlayerConnect( int playerid )
	{
		try
		{
    		PlayerBase player;
    		currentPlayerId = playerid;
    		
    		try
    		{
    			player = (PlayerBase) playerCls.newInstance();
    		}
    		catch (InstantiationException e)
    		{
    			e.printStackTrace();
    			return 0;
    		}
    		catch (IllegalAccessException e)
    		{
    			e.printStackTrace();
    			return 0;
    		}
    		
    		playerPool[playerid] = player;
    		
    		onConnect( player );
    		eventConnect.dispatchEvent( new PlayerConnectEvent(player) );
    		return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}

	int onPlayerDisconnect( int playerid, int reason )
	{
		try
		{
    		PlayerBase player = playerPool[playerid];
    		
    		PlayerDisconnectEvent event = new PlayerDisconnectEvent(player, reason);
    		
    		onDisconnect( player, reason );
    		eventDisconnect.dispatchEvent( event );
    		
    		player.onDisconnect( reason );
    		player.eventDisconnect.dispatchEvent( event );
    		
    		playerPool[playerid] = null;
    		return 1;
    	}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}

	int onPlayerSpawn( int playerid )
	{
		try
		{
    		PlayerBase player = playerPool[playerid];
    			
    		player.onSpawn();
    		player.eventSpawn.dispatchEvent( new PlayerSpawnEvent(player) );
    
    		return 1;
    	}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}

	int onPlayerDeath( int playerid, int killerid, int reason )
	{
		try
		{
    		PlayerBase player = playerPool[playerid];
    		PlayerBase killer = null;
    		
    		if( killerid != 0xFFFF )
    		{
    			killer = playerPool[killerid];
    			killer.onKill( player, reason );
    			killer.eventKill.dispatchEvent( new PlayerKillEvent(killer, player, reason) );
    		}
    		
    		player.onDeath( killer, reason );
    		player.eventDeath.dispatchEvent( new PlayerDeathEvent(player, killer, reason) );
    		
    		return 1;
    	}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}

	int onPlayerText( int playerid, String text )
	{
		try
		{
    		PlayerBase player = playerPool[playerid];
    		
    		PlayerTextEvent event = new PlayerTextEvent(player, text, player.onText(text));
    		player.eventText.dispatchEvent( event );
    		
    		if( event.result() != 0 )
    			PlayerBase.sendMessageToAll( Color.WHITE, "{FE8B13}" + player.name() + ": {FFFFFF}" + text );
    		
    		return 0;
    	}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}

	int onPlayerCommandText( int playerid, String cmdtext )
	{
		try
		{
    		PlayerBase player = playerPool[playerid];
    		
    		PlayerCommandEvent event = new PlayerCommandEvent(player, cmdtext, player.onCommand(cmdtext));
    		player.eventCommand.dispatchEvent( event );
    		
    		return event.result();
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}

	int onPlayerRequestClass( int playerid, int classid )
	{
		try
		{
    		PlayerBase player = playerPool[playerid];
    		
    		PlayerRequestClassEvent event = new PlayerRequestClassEvent(player, classid, player.onRequestClass(classid));
    		player.eventRequestClass.dispatchEvent( event );
    		
    		return event.result();
    	}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}

	int onPlayerStateChange( int playerid, int newstate, int oldstate )
	{
		try
		{
    		PlayerBase player = playerPool[playerid];
    		
    		player.state.state = newstate;
    		
    		player.onStateChange( oldstate );
    		player.eventStateChange.dispatchEvent( new PlayerStateChangeEvent(player, oldstate) );
    		
    		return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}

	int onPlayerEnterCheckpoint( int playerid )
	{
		try
		{
    		PlayerBase player = playerPool[playerid];
    		
    		player.onEnterCheckpoint();
    		player.eventEnterCheckpoint.dispatchEvent( new PlayerEnterCheckpointEvent(player) );
    		
    		return 1;
    	}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}

	int onPlayerLeaveCheckpoint( int playerid )
	{
		try
		{
    		PlayerBase player = playerPool[playerid];
    		
    		player.onLeaveCheckpoint();
    		player.eventLeaveCheckpoint.dispatchEvent( new PlayerLeaveCheckpointEvent(player) );
    		
    		return 1;
    	}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}

	int onPlayerEnterRaceCheckpoint( int playerid )
	{
		try
		{
    		PlayerBase player = playerPool[playerid];
    		
    		player.onEnterRaceCheckpoint();
    		player.eventEnterRaceCheckpoint.dispatchEvent( new PlayerEnterRaceCheckpointEvent(player) );
    		
    		return 1;
    	}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}

	int onPlayerLeaveRaceCheckpoint( int playerid )
	{
		try
		{
    		PlayerBase player = playerPool[playerid];
    		
    		player.onLeaveRaceCheckpoint();
    		player.eventLeaveRaceCheckpoint.dispatchEvent( new PlayerLeaveRaceCheckpointEvent(player) );
    		
    		return 1;
    	}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}

	int onPlayerRequestSpawn( int playerid )
	{
		try
		{
    		PlayerBase player = playerPool[playerid];
    		
    		PlayerRequestSpawnEvent event = new PlayerRequestSpawnEvent( player, player.onRequestSpawn() );
    		player.eventRequestSpawn.dispatchEvent( event );
    		
    		return event.result();
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}

	int onObjectMoved( int objectid )
	{
		try
		{
			return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}

	int onPlayerObjectMoved( int playerid, int objectid )
	{
		try
		{
    		PlayerBase player = playerPool[playerid];
    		ObjectBase object = playerObjectPool[objectid];
    		
    		player.onObjectMoved( object );
    		player.eventObjectMoved.dispatchEvent( new PlayerObjectMovedEvent(player, object) );
    		
    		return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}

	int onPlayerPickUpPickup( int playerid, int pickupid )
	{
		try
		{
    		PlayerBase player = playerPool[playerid];
    		PickupBase pickup = pickupPool[pickupid];
    		
    		player.onPickup( pickup );
    		player.eventPickup.dispatchEvent( new PlayerPickupEvent(player, pickup) );
    		
    		return 1;
    	}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}

	int onPlayerSelectedMenuRow( int playerid, int row )
	{
		try
		{
    		//PlayerBase player = playerPool[playerid];
    		//MenuBase menu = menuPool[];
    		
    		//player.on();
    		//player.event.dispatchEvent( new Player(player) );
    		
    		return 1;
    	}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}

	int onPlayerExitedMenu( int playerid )
	{
		try
		{
    		//PlayerBase player = playerPool[playerid];
    		//MenuBase menu = menuPool[];
    		
    		//player.on();
    		//player.event.dispatchEvent( new Player(player) );
    			
    		return 1;
    	}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}

	int onPlayerInteriorChange( int playerid, int newinteriorid, int oldinteriorid )
	{
		try
		{
    		PlayerBase player = playerPool[playerid];
    		
    		player.position.interior = newinteriorid;
    		
    		player.onInteriorChange( oldinteriorid );
    		player.eventInteriorChange.dispatchEvent( new PlayerInteriorChangeEvent(player, oldinteriorid) );
    		
    		return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}

	int onPlayerKeyStateChange( int playerid, int newkeys, int oldkeys )
	{
		try
		{
    		PlayerBase player = playerPool[playerid];
    		
    		player.keyState.keys = newkeys;
    		//update keystate
    		
    		player.onKeyStateChange( oldkeys );
    		player.eventKeyStateChange.dispatchEvent( new PlayerKeyStateChangeEvent(player, oldkeys) );
    		
    		return 1;
    	}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}

	int onPlayerUpdate( int playerid )
	{
		try
		{
    		PlayerBase player = playerPool[playerid];
    		player.update();
    		
    		player.onUpdate();
    		player.eventUpdate.dispatchEvent( new PlayerUpdateEvent(player) );
    		
    		return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}

	int onPlayerStreamIn( int playerid, int forplayerid )
	{
		try
		{
    		PlayerBase player = playerPool[playerid];
    		PlayerBase forplayer = playerPool[forplayerid];
    		
    		player.onPlayerStreamIn( forplayer );
    		player.eventPlayerStreamIn.dispatchEvent( new PlayerStreamInEvent(player, forplayer) );
    		
    		return 1;
    	}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}

	int onPlayerStreamOut( int playerid, int forplayerid )
	{
		try
		{
    		PlayerBase player = playerPool[playerid];
    		PlayerBase forplayer = playerPool[forplayerid];
    		
    		player.onPlayerStreamOut( forplayer );
    		player.eventPlayerStreamOut.dispatchEvent( new PlayerStreamOutEvent(player, forplayer) );
    		
    		return 1;
    	}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}

	int onPlayerClickPlayer( int playerid, int clickedplayerid, int source )
	{
		try
		{
    		PlayerBase player = playerPool[playerid];
    		PlayerBase clickedPlayer = playerPool[clickedplayerid];
    		
    		PlayerClickPlayerEvent event = new PlayerClickPlayerEvent( player, clickedPlayer, source );
    		
    		player.onClickPlayer( clickedPlayer, source );
    		player.eventClickPlayer.dispatchEvent( event );
    		
    		clickedPlayer.onOthersClick( clickedPlayer, source );
    		clickedPlayer.eventOthersClick.dispatchEvent( event );
    		
    		return 1;
    	}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	int onEnterExitModShop( int playerid, int enterexit, int interiorid )
	{
		try
		{
    		PlayerBase player = playerPool[playerid];
    		
    		player.onEnterExitModShop( enterexit != 0, interiorid );
    		player.eventEnterExitModShop.dispatchEvent( new PlayerEnterExitModShopEvent(player, enterexit != 0, interiorid) );
    		
    		return 1;
    	}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}

	int onDialogResponse( int playerid, int dialogid, int response, int listitem, String inputtext )
	{
		try
		{
    		PlayerBase player = playerPool[playerid];
    		DialogBase dialog = dialogPool.get(dialogid);
    		
    		DialogResponseEvent event = new DialogResponseEvent(dialog, player, response, listitem, inputtext);
    		
    		player.onDialogResponse( dialog, response, listitem, inputtext );
    		player.eventDialogResponse.dispatchEvent( event );
    		
    		dialog.onResponse( player, response, listitem, inputtext );
    		dialog.eventResponse.dispatchEvent( event );
    		
    		player.dialog = null;
    		return 1;
    	}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}

	int onPlayerEnterVehicle( int playerid, int vehicleid, int ispassenger )
	{
		try
		{
    		PlayerBase player = playerPool[playerid];
    		VehicleBase vehicle = vehiclePool[vehicleid];
    		
    		VehicleEnterEvent event = new VehicleEnterEvent(vehicle, player, ispassenger != 0);
    		
    		vehicle.onEnter( player, ispassenger != 0 );
    		vehicle.eventEnter.dispatchEvent( event );
    		
    		player.onEnterVehicle( vehicle, ispassenger != 0 );
    		player.eventEnterVehicle.dispatchEvent( event );
    		
    		return 1;
    	}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}

	int onPlayerExitVehicle( int playerid, int vehicleid )
	{
		try
		{
    		PlayerBase player = playerPool[playerid];
    		VehicleBase vehicle = vehiclePool[vehicleid];
    		
    		VehicleExitEvent event = new VehicleExitEvent(vehicle, player);
    		
    		vehicle.onExit( player );
    		vehicle.eventExit.dispatchEvent( event );
    		
    		player.onExitVehicle( vehicle );
    		player.eventExitVehicle.dispatchEvent( event );
    		
    		return 1;
    	}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}

	int onVehicleSpawn( int vehicleid )
	{
		try
		{
    		VehicleBase vehicle = vehiclePool[vehicleid];
    		
    		vehicle.onSpawn();
    		vehicle.eventSpawn.dispatchEvent( new VehicleSpawnEvent(vehicle) );
    		
    		return 1;
    	}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}

	int onVehicleDeath( int vehicleid, int killerid )
	{
		try
		{
    		VehicleBase vehicle = vehiclePool[vehicleid];
    		PlayerBase killer = playerPool[killerid];
    		
    		vehicle.onDeath( killer );
    		vehicle.eventDeath.dispatchEvent( new VehicleDeathEvent(vehicle, killer) );
    		
    		return 1;
    	}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}

	int onVehicleMod( int playerid, int vehicleid, int componentid )
	{
		try
		{
    		PlayerBase player = playerPool[playerid];
    		VehicleBase vehicle = vehiclePool[vehicleid];
    		
    		VehicleModEvent event = new VehicleModEvent(vehicle, componentid);
    
    		vehicle.onMod( componentid );
    		vehicle.eventMod.dispatchEvent( event );
    		
    		player.onVehicleMod( componentid );
    		player.eventVehicleMod.dispatchEvent( event );
    		
    		return 1;	
    	}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}

	int onVehiclePaintjob( int playerid, int vehicleid, int paintjobid )
	{
		try
		{
    		PlayerBase player = playerPool[playerid];
    		VehicleBase vehicle = vehiclePool[vehicleid];
    		
    		VehiclePaintjobEvent event = new VehiclePaintjobEvent(vehicle, paintjobid);
    
    		vehicle.onPaintjob( paintjobid );
    		vehicle.eventPaintjob.dispatchEvent( event );
    		
    		player.onVehiclePaintjob( paintjobid );
    		player.eventVehiclePaintjob.dispatchEvent( event );
    		
    		return 1;
    	}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}

	int onVehicleRespray( int playerid, int vehicleid, int color1, int color2 )
	{
		try
		{
    		PlayerBase player = playerPool[playerid];
    		VehicleBase vehicle = vehiclePool[vehicleid];
    		
    		VehicleResprayEvent event = new VehicleResprayEvent( vehicle, color1, color2 );
    
    		vehicle.onRespray( color1, color2 );
    		vehicle.eventRespray.dispatchEvent( event );
    		
    		player.onVehicleRespray( color1, color2 );
    		player.eventVehicleRespray.dispatchEvent( event );
    		
    		return 1;
    	}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
    }

	int onVehicleDamageStatusUpdate( int vehicleid, int playerid )
	{
		try
		{
    		VehicleBase vehicle = vehiclePool[vehicleid];
    		PlayerBase player = playerPool[playerid];
    		
    		VehicleUpdateDamageEvent event = new VehicleUpdateDamageEvent(vehicle, player);
    
    		vehicle.onUpdateDamage( player );
    		vehicle.eventUpdateDamage.dispatchEvent( event );
    		
    		//player.on();
    		//player.event.dispatchEvent( new Player(player) );
    		
    		return 1;
    	}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}

	int onVehicleStreamIn( int vehicleid, int forplayerid )
	{
		try
		{
    		PlayerBase player = playerPool[forplayerid];
    		VehicleBase vehicle = vehiclePool[vehicleid];
    		
    		VehicleStreamInEvent event = new VehicleStreamInEvent(vehicle, player);
    		
    		vehicle.onStreamIn( player );
    		vehicle.eventStreamIn.dispatchEvent( event );
    		
    		player.onVehicleStreamIn( vehicle );
    		player.eventVehicleStreamIn.dispatchEvent( event );
    
    		return 1;
    	}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}

	int onVehicleStreamOut( int vehicleid, int forplayerid )
	{
		try
		{
    		PlayerBase player = playerPool[forplayerid];
    		VehicleBase vehicle = vehiclePool[vehicleid];
    		
    		VehicleStreamOutEvent event = new VehicleStreamOutEvent(vehicle, player);
    
    		player.onVehicleStreamOut( vehicle );
    		player.eventVehicleStreamOut.dispatchEvent( event );
    		
    		vehicle.onStreamOut( player );
    		vehicle.eventStreamOut.dispatchEvent( event );
    		
    		return 1;
    	}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}

	int onServerRconCommand( String cmd )
	{
		try
		{
    		RconCommandEvent event = new RconCommandEvent(cmd, onRconCommand(cmd));
    		eventRconCommand.dispatchEvent( event );
    		
    		return event.result();
    	}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}

	int onServerRconLoginAttempt( String ip, String password, int success )
	{
		try
		{
    		onRconLogin( ip, password, success != 0 );
    		eventRconLogin.dispatchEvent( new RconLoginEvent(ip, password, success!=0) );
    		
    		return 1;
    	}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	int onTimer( int timerIndex )
	{
		try
		{
			return 1;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
}
