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

import java.io.PrintStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import net.gtaun.lungfish.util.event.EventDispatcher;
import net.gtaun.lungfish.event.CheckpointEnterEvent;
import net.gtaun.lungfish.event.CheckpointLeaveEvent;
import net.gtaun.lungfish.event.DialogResponseEvent;
import net.gtaun.lungfish.event.GameModeExitEvent;
import net.gtaun.lungfish.event.MenuExitedEvent;
import net.gtaun.lungfish.event.MenuSelectedEvent;
import net.gtaun.lungfish.event.ObjectMovedEvent;
import net.gtaun.lungfish.event.PlayerClickPlayerEvent;
import net.gtaun.lungfish.event.PlayerCommandEvent;
import net.gtaun.lungfish.event.PlayerConnectEvent;
import net.gtaun.lungfish.event.PlayerDeathEvent;
import net.gtaun.lungfish.event.PlayerDisconnectEvent;
import net.gtaun.lungfish.event.PlayerEnterExitModShopEvent;
import net.gtaun.lungfish.event.PlayerInteriorChangeEvent;
import net.gtaun.lungfish.event.PlayerKeyStateChangeEvent;
import net.gtaun.lungfish.event.PlayerKillEvent;
import net.gtaun.lungfish.event.PlayerObjectMovedEvent;
import net.gtaun.lungfish.event.PlayerPickupEvent;
import net.gtaun.lungfish.event.PlayerRequestClassEvent;
import net.gtaun.lungfish.event.PlayerRequestSpawnEvent;
import net.gtaun.lungfish.event.PlayerSpawnEvent;
import net.gtaun.lungfish.event.PlayerStateChangeEvent;
import net.gtaun.lungfish.event.PlayerStreamInEvent;
import net.gtaun.lungfish.event.PlayerStreamOutEvent;
import net.gtaun.lungfish.event.PlayerTextEvent;
import net.gtaun.lungfish.event.PlayerUpdateEvent;
import net.gtaun.lungfish.event.RaceCheckpointEnterEvent;
import net.gtaun.lungfish.event.RaceCheckpointLeaveEvent;
import net.gtaun.lungfish.event.RconCommandEvent;
import net.gtaun.lungfish.event.RconLoginEvent;
import net.gtaun.lungfish.event.TimerTickEvent;
import net.gtaun.lungfish.event.VehicleDeathEvent;
import net.gtaun.lungfish.event.VehicleEnterEvent;
import net.gtaun.lungfish.event.VehicleExitEvent;
import net.gtaun.lungfish.event.VehicleModEvent;
import net.gtaun.lungfish.event.VehiclePaintjobEvent;
import net.gtaun.lungfish.event.VehicleResprayEvent;
import net.gtaun.lungfish.event.VehicleSpawnEvent;
import net.gtaun.lungfish.event.VehicleStreamInEvent;
import net.gtaun.lungfish.event.VehicleStreamOutEvent;
import net.gtaun.lungfish.event.VehicleUnoccupiedUpdate;
import net.gtaun.lungfish.event.VehicleUpdateDamageEvent;
import net.gtaun.shoebill.data.Color;
import net.gtaun.shoebill.data.Point;
import net.gtaun.shoebill.data.SpawnInfo;

/**
 * @author MK124, JoJLlmAn
 *
 */

public abstract class GameModeBase
{
	public static final int MAX_PLAYER_NAME =			24;
	public static final int MAX_PLAYERS =				500;
    public static final int MAX_VEHICLES =				2000;
    public static final int MAX_OBJECTS =				400;
    public static final int MAX_ZONES =					1024;
    public static final int MAX_TEXT_DRAWS =			2048;
    public static final int MAX_MENUS =					128;
    public static final int MAX_LABELS_GLOBAL =			1024;
    public static final int MAX_LABELS_PLAYER =			1024;
    public static final int MAX_PICKUPS =				2048;
    
    static final int INVALID_PLAYER_ID =				0xFFFF;
    static final int INVALID_VEHICLE_ID	=				0xFFFF;
    static final int INVALID_OBJECT_ID =				0xFFFF;
    static final int INVALID_MENU =						0xFF;
    static final int INVALID_TEXT_DRAW =				0xFFFF;
    static final int INVALID_GANG_ZONE =				-1;
    static final int INVALID_3DTEXT_ID =				0xFFFF;
    static final int PLAYER_NO_TEAM =					255;
	
//----------------------------------------------------------
    
	static GameModeBase instance = null;

	static PrintStream consoleStream = System.out;
	static PrintStream errorStream = System.err;
	static LogPrintStream logStream;
	

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
	
	static <T> Vector<T> getInstances(Vector<?> items, Class<T> cls)
	{
		Vector<T> list = new Vector<T>();
		
		Iterator<?> iterator = items.iterator();
		while (iterator.hasNext())
		{
			Object object = iterator.next();
			if( cls.isInstance(object) ) list.add( cls.cast(object) );
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
	
	Vector<Reference<TimerBase>> timerPool				= new Vector<Reference<TimerBase>>();
	Map<Integer, WeakReference<DialogBase>> dialogPool	= new HashMap<Integer, WeakReference<DialogBase>>();
	// 撘勗���
	
	int currentPlayerId;
	// PlayerBase() ����典
	
	int deathDropAmount = 0;
	float nameTagDrawDistance = 70;
	float chatRadius = -1;
	float playerMarkerRadius = -1;
	
	public int deathDropAmount()		{ return deathDropAmount; }
	public float nameTagDrawDistance()	{ return nameTagDrawDistance; }
	public float chatRadius()			{ return chatRadius; }
	public float playerMarkerRadius()	{ return playerMarkerRadius;}
	
	EventDispatcher<GameModeExitEvent>		eventExit = new EventDispatcher<GameModeExitEvent>();
	EventDispatcher<PlayerConnectEvent>		eventConnect = new EventDispatcher<PlayerConnectEvent>();
	EventDispatcher<PlayerDisconnectEvent>	eventDisconnect = new EventDispatcher<PlayerDisconnectEvent>();
	EventDispatcher<RconCommandEvent>		eventRconCommand = new EventDispatcher<RconCommandEvent>();
	EventDispatcher<RconLoginEvent>			eventRconLogin = new EventDispatcher<RconLoginEvent>();
	EventDispatcher<TimerTickEvent>			eventTick = new EventDispatcher<TimerTickEvent>();
	
	public EventDispatcher<GameModeExitEvent>		eventExit()				{ return eventExit; }
	public EventDispatcher<PlayerConnectEvent>		eventConnect()			{ return eventConnect; }
	public EventDispatcher<PlayerDisconnectEvent>	eventDisconnect()		{ return eventDisconnect; }
	public EventDispatcher<RconCommandEvent>		eventRconCommand()		{ return eventRconCommand; }
	public EventDispatcher<RconLoginEvent>			eventRconLogin()		{ return eventRconLogin; }
	public EventDispatcher<TimerTickEvent>			eventTick()				{ return eventTick; }
	

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
		try
		{
			logStream = new LogPrintStream(consoleStream);
			System.setOut( logStream );
			System.setErr( logStream );
			
			NativeFunction.loadLibrary();
			instance = this;
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	
	
//--------------------------------------------------------- ��蝏扳���亙���隞�

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

	
//--------------------------------------------------------- ����梁��賣隞�
	
	public int weather()
	{
		return NativeFunction.getServerVarAsInt("weather");

	}
	
	public float gravity()
	{
		return Float.parseFloat(NativeFunction.getServerVarAsString("gravity"));
	}
	
	public int serverCodepage()
	{
		return NativeFunction.getServerCodepage();
	}
	
	public void setServerCodepage( int codepage )
	{
		NativeFunction.setServerCodepage( codepage );
	}
	
	public void setGameModeText( String string )
	{
		if( string == null ) throw new NullPointerException();
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
	
	public void setDeathDropAmount( int amount )
	{
		NativeFunction.setDeathDropAmount( amount );
		deathDropAmount = amount;
	}
	
	public void createExplosion( Point point, int type, float radius )
	{
		NativeFunction.createExplosion( point.x, point.y, point.z, type, radius );
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
		nameTagDrawDistance = distance;
		NativeFunction.setNameTagDrawDistance( distance );
	}
	
	public void disableNameTagLOS()
	{
		NativeFunction.disableNameTagLOS();
	}

	public void sendRconCommand( String command )
	{
		if( command == null ) throw new NullPointerException();
		NativeFunction.sendRconCommand( command );
	}
	
	public String getServerVarAsString( String varname )
	{
		if( varname == null ) throw new NullPointerException();
		return NativeFunction.getServerVarAsString( varname );
	}
	
	public int getServerVarAsInt( String varname )
	{
		if( varname == null ) throw new NullPointerException();
		return NativeFunction.getServerVarAsInt( varname );
	}
	
	public boolean getServerVarAsBool( String varname )
	{
		if( varname == null ) throw new NullPointerException();
		return NativeFunction.getServerVarAsBool( varname );
	}
	
	public void connectNPC( String name, String script )
	{
		if( name == null || script == null ) throw new NullPointerException();
		NativeFunction.connectNPC( name, script );
	}
	
	public void exit()
	{
		NativeFunction.gameModeExit();
	}
	
	public String getNetworkStats()
	{
		return NativeFunction.getNetworkStats();
	}
	
	
//--------------------------------------------------------- 鋡侯NI�澆��

	long lastTick = System.nanoTime();
	void onProcessTick()
	{	
		try
		{
			long nowTick = System.nanoTime();
			int interval = (int) ((nowTick - lastTick) / 1000 / 1000);
			lastTick = nowTick;
			
			Iterator<Reference<TimerBase>> iterator = timerPool.iterator();
			while( iterator.hasNext() )
			{
				TimerBase timer = iterator.next().get();
				timer.tick( interval );
			}
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
			
			logStream.log( "--- Server Shutting Down." );
			
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
    		logStream.log( "[join] " + player.name + " has joined the server (" + playerid + ":" + player.ip + ")" );
    		
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
    		logStream.log( "[part] " + player.name + " has left the server (" + playerid + ":" + reason + ")" );
    		
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
    		System.out.println( "[spawn] " + player.name + " has spawned (" + playerid + ")" );
    		
    		player.team = NativeFunction.getPlayerTeam(player.id);
    		player.skin = NativeFunction.getPlayerSkin(player.id);
    		
    		player.playerAttach = new PlayerAttach(playerid);
    			
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
    			logStream.log( "[kill] " + killer.name + " killed " + player.name +
    					" (" + NativeFunction.getWeaponName(reason) + ")" );
    			
    			killer.onKill( player, reason );
    			killer.eventKill.dispatchEvent( new PlayerKillEvent(killer, player, reason) );
    		}
    		else logStream.log( "[death] " + player.name + " died (" + playerid + ":" + reason + ")" );
    		
    		player.playerAttach = new PlayerAttach(playerid);
    		
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
    		logStream.log( "[chat] [" + player.name + "]: " + text );
    		
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
    		System.out.println( "[cmd] [" + player.name + "] " + cmdtext );
    		
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
    		
    		player.state = newstate;
    		
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
    		
    		CheckpointEnterEvent event = new CheckpointEnterEvent( player );
    		
    		player.checkpoint.onEnter( player );
    		player.checkpoint.eventEnter.dispatchEvent( event );
    		
    		player.onEnterCheckpoint();
    		player.eventEnterCheckpoint.dispatchEvent( event );
    		
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
    		
    		CheckpointLeaveEvent event = new CheckpointLeaveEvent(player);

    		player.checkpoint.onLeave( player );
    		player.checkpoint.eventLeave.dispatchEvent( event );
    		
    		player.onLeaveCheckpoint();
    		player.eventLeaveCheckpoint.dispatchEvent( event );
    		
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
    		
    		RaceCheckpointEnterEvent event = new RaceCheckpointEnterEvent(player);
    		
    		player.raceCheckpoint.onEnter( player );
    		player.raceCheckpoint.eventEnter.dispatchEvent( event );
    		
    		player.onEnterRaceCheckpoint();
    		player.eventEnterRaceCheckpoint.dispatchEvent( event );
    		
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
    		
    		RaceCheckpointLeaveEvent event = new RaceCheckpointLeaveEvent(player);
    		
    		player.raceCheckpoint.onLeave( player );
    		player.raceCheckpoint.eventLeave.dispatchEvent( event );
    		
    		player.onLeaveRaceCheckpoint();
    		player.eventLeaveRaceCheckpoint.dispatchEvent( event );
    		
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
			ObjectBase object = objectPool[objectid];
			
			object.speed = 0;
			NativeFunction.getObjectPos(objectid, object.position);
			NativeFunction.getObjectRot(objectid, object.position);
			
			object.onMoved();
			object.eventMoved.dispatchEvent( new ObjectMovedEvent(object) );
			
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
    		ObjectBase object = playerObjectPool[objectid + playerid*MAX_OBJECTS];
    		
    		object.speed = 0;  		
    		
    		object.onMoved();
    		object.eventMoved.dispatchEvent(new ObjectMovedEvent(object));
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

    		System.out.println( "[pickup] " + player.name + " pickup " + pickup.model + " (" + pickup.type + ")" );
    		
    		PlayerPickupEvent event = new PlayerPickupEvent(player, pickup);
    		
    		pickup.onPickup( player );
    		pickup.eventPickup.dispatchEvent( event );
    		player.onPickup( pickup );
    		player.eventPickup.dispatchEvent( event );
    		
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
			
			PlayerBase player = playerPool[playerid];
			MenuBase menu = menuPool[NativeFunction.getPlayerMenu(playerid)];
			
			MenuSelectedEvent event = new MenuSelectedEvent( menu, player, row );
			
			player.onMenuSelected( menu, row );
			menu.onPlayerSelectedMenuRow( player, row );
			
			player.eventMenuSelected.dispatchEvent( event );
    		menu.eventMenuSelected.dispatchEvent( event );
			
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
			
			PlayerBase player = playerPool[playerid];
			MenuBase menu = menuPool[NativeFunction.getPlayerMenu(playerid)];
			
			MenuExitedEvent event = new MenuExitedEvent( menu, player );
			
			player.onMenuExited( menu );
			menu.onPlayerExitedMenu( player );
			
			player.eventMenuExited.dispatchEvent( event );
    		menu.eventMenuExited.dispatchEvent( event );
    			
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
    		System.out.println( "[interior] " + player.name + " interior has changed to " + newinteriorid );
    		
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

    		System.out.println( "[click] " + player.name + " has clicked " + clickedPlayer.name );
    		
    		
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
    		DialogBase dialog = dialogPool.get(dialogid).get();
    		
    		if( dialog == null )
    		{
    			dialogPool.remove( dialogid );
    			return 0;
    		}
    		
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
    		
    		System.out.println( "[vehicle] " + player.name + " enter a vehicle (" + vehicle.model + ")" );
    		
    		
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

    		System.out.println( "[vehicle] " + player.name + " leave a vehicle (" + vehicle.model + ")" );
    		
    		
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
    		
    		int type = NativeFunction.getVehicleComponentType(componentid);
    		vehicle.component.components[type] = NativeFunction.getVehicleComponentInSlot(vehicleid, type);
    
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
    		
    		NativeFunction.getVehicleDamageStatus(vehicleid, vehicle.damage);
    		
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
			logStream.log( "[rcon] " + " command: " + cmd );
			
    		
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
			if( success == 0 )
				logStream.log( "[rcon] " + " bad rcon attempy by: " + ip + " (" + password + ")" );
			else logStream.log( "[rcon] " + ip + " has logged." );
    		
    		
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
	
	int onUnoccupiedVehicleUpdate( int vehicleid, int playerid, int passenger_seat )
	{
		try 
		{
			PlayerBase player = playerPool[playerid];
    		VehicleBase vehicle = vehiclePool[vehicleid];
    		
    		VehicleUnoccupiedUpdate event = new VehicleUnoccupiedUpdate(vehicle, player, passenger_seat);
    
    		player.onUpdateUnoccupiedVehicle( vehicle );
    		player.eventVehicleUnoccupiedUpdate.dispatchEvent( event );
    		
    		vehicle.onPlayerUnoccupiedUpdate( player );
    		vehicle.eventVehicleUnoccupiedUpdate.dispatchEvent( event );
    		
    		return 1;
		}
		catch (Exception e) 
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
