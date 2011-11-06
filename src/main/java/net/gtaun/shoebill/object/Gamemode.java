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

import java.io.PrintStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import net.gtaun.shoebill.LogPrintStream;
import net.gtaun.shoebill.SampNativeFunction;
import net.gtaun.shoebill.data.Color;
import net.gtaun.shoebill.data.Point;
import net.gtaun.shoebill.data.SpawnInfo;
import net.gtaun.shoebill.event.checkpoint.CheckpointEnterEvent;
import net.gtaun.shoebill.event.checkpoint.CheckpointLeaveEvent;
import net.gtaun.shoebill.event.dialog.DialogResponseEvent;
import net.gtaun.shoebill.event.gamemode.GamemodeExitEvent;
import net.gtaun.shoebill.event.menu.MenuExitedEvent;
import net.gtaun.shoebill.event.menu.MenuSelectedEvent;
import net.gtaun.shoebill.event.object.ObjectMovedEvent;
import net.gtaun.shoebill.event.object.PlayerObjectMovedEvent;
import net.gtaun.shoebill.event.player.PlayerClickPlayerEvent;
import net.gtaun.shoebill.event.player.PlayerCommandEvent;
import net.gtaun.shoebill.event.player.PlayerConnectEvent;
import net.gtaun.shoebill.event.player.PlayerDeathEvent;
import net.gtaun.shoebill.event.player.PlayerDisconnectEvent;
import net.gtaun.shoebill.event.player.PlayerEnterExitModShopEvent;
import net.gtaun.shoebill.event.player.PlayerInteriorChangeEvent;
import net.gtaun.shoebill.event.player.PlayerKeyStateChangeEvent;
import net.gtaun.shoebill.event.player.PlayerKillEvent;
import net.gtaun.shoebill.event.player.PlayerPickupEvent;
import net.gtaun.shoebill.event.player.PlayerRequestClassEvent;
import net.gtaun.shoebill.event.player.PlayerRequestSpawnEvent;
import net.gtaun.shoebill.event.player.PlayerSpawnEvent;
import net.gtaun.shoebill.event.player.PlayerStateChangeEvent;
import net.gtaun.shoebill.event.player.PlayerStreamInEvent;
import net.gtaun.shoebill.event.player.PlayerStreamOutEvent;
import net.gtaun.shoebill.event.player.PlayerTextEvent;
import net.gtaun.shoebill.event.player.PlayerUpdateEvent;
import net.gtaun.shoebill.event.racecheckpoint.RaceCheckpointEnterEvent;
import net.gtaun.shoebill.event.racecheckpoint.RaceCheckpointLeaveEvent;
import net.gtaun.shoebill.event.rcon.RconCommandEvent;
import net.gtaun.shoebill.event.rcon.RconLoginEvent;
import net.gtaun.shoebill.event.vehicle.VehicleDeathEvent;
import net.gtaun.shoebill.event.vehicle.VehicleEnterEvent;
import net.gtaun.shoebill.event.vehicle.VehicleExitEvent;
import net.gtaun.shoebill.event.vehicle.VehicleModEvent;
import net.gtaun.shoebill.event.vehicle.VehiclePaintjobEvent;
import net.gtaun.shoebill.event.vehicle.VehicleResprayEvent;
import net.gtaun.shoebill.event.vehicle.VehicleSpawnEvent;
import net.gtaun.shoebill.event.vehicle.VehicleStreamInEvent;
import net.gtaun.shoebill.event.vehicle.VehicleStreamOutEvent;
import net.gtaun.shoebill.event.vehicle.VehicleUnoccupiedUpdateEvent;
import net.gtaun.shoebill.event.vehicle.VehicleUpdateDamageEvent;
import net.gtaun.shoebill.util.event.EventDispatcher;
import net.gtaun.shoebill.util.event.IEventDispatcher;

/**
 * @author MK124, JoJLlmAn
 *
 */

public abstract class Gamemode implements IGamemode
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
	
	static Gamemode instance = null;

	static PrintStream consoleStream = System.out;
	static PrintStream errorStream = System.err;
	static LogPrintStream logStream;
	

	static <T> Vector<T> getInstances(Object[] items, Class<T> cls)
	{
		Vector<T> list = new Vector<T>();
		
		for( Object item : items )
		{
			if( item != null && cls.isInstance(item) )
				list.add( cls.cast(item) );
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
	

	private Class<?> playerCls = Player.class;
	
	EventDispatcher eventDispatcher = new EventDispatcher();
	
	Player[] playerPool						= new Player[MAX_PLAYERS];
	Vehicle[] vehiclePool					= new Vehicle[MAX_VEHICLES];
	ObjectBase[] objectPool					= new ObjectBase[MAX_OBJECTS];
	PlayerObject[] playerObjectPool			= new PlayerObject[MAX_OBJECTS*MAX_PLAYERS];
	Pickup[] pickupPool						= new Pickup[MAX_PICKUPS];
	Label[] labelPool						= new Label[MAX_LABELS_GLOBAL];
	PlayerLabel[] playerLabelPool			= new PlayerLabel[MAX_LABELS_PLAYER*MAX_PLAYERS];
	Textdraw[] textdrawPool					= new Textdraw[MAX_TEXT_DRAWS];
	Zone[] zonePool							= new Zone[MAX_ZONES];
	Menu[] menuPool							= new Menu[MAX_MENUS];
	
	Vector<Reference<Timer>> timerPool				= new Vector<Reference<Timer>>();
	Map<Integer, WeakReference<Dialog>> dialogPool	= new HashMap<Integer, WeakReference<Dialog>>();
	
	
	int currentPlayerId;
	
	int deathDropAmount = 0;
	float nameTagDrawDistance = 70;
	float chatRadius = -1;
	float playerMarkerRadius = -1;
	
	
	@Override public IEventDispatcher getEventDispatcher()		{ return eventDispatcher; }
	
	@Override public int getDeathDropAmount()					{ return deathDropAmount; }
	@Override public float getNameTagDrawDistance()				{ return nameTagDrawDistance; }
	@Override public float getChatRadius()						{ return chatRadius; }
	@Override public float getPlayerMarkerRadius()				{ return playerMarkerRadius;}
	

	protected Gamemode()
	{
		init();
	}

	protected Gamemode( Class<?> playercls )
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
			
			instance = this;
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

	
//--------------------------------------------------------- 
	
	public int weather()
	{
		return SampNativeFunction.getServerVarAsInt("weather");

	}
	
	public float gravity()
	{
		return Float.parseFloat(SampNativeFunction.getServerVarAsString("gravity"));
	}
	
	public int serverCodepage()
	{
		return SampNativeFunction.getServerCodepage();
	}
	
	@Override
	public void setServerCodepage( int codepage )
	{
		SampNativeFunction.setServerCodepage( codepage );
	}
	
	@Override
	public void setGameModeText( String string )
	{
		if( string == null ) throw new NullPointerException();
		SampNativeFunction.setGameModeText( string );
	}
	
	@Override
	public void setTeamCount( int count )
	{
		SampNativeFunction.setTeamCount( count );
	}

	@Override
	public int addPlayerClass( int model, float x, float y, float z, float angle, int weapon1, int ammo1, int weapon2, int ammo2, int weapon3, int ammo3 )
	{
		return SampNativeFunction.addPlayerClass( model, x, y, z, angle, weapon1, ammo1, weapon2, ammo2, weapon3, ammo3 );
	}

	@Override
	public int addPlayerClass( int model, SpawnInfo spawninfo )
	{
		return SampNativeFunction.addPlayerClass( model, 
			spawninfo.position.x, spawninfo.position.y, spawninfo.position.z, spawninfo.position.angle,
			spawninfo.weapon1.id, spawninfo.weapon1.ammo, spawninfo.weapon2.id, spawninfo.weapon2.ammo,
			spawninfo.weapon3.id, spawninfo.weapon3.ammo );
	}
	
	@Override
	public int addPlayerClassEx( int team, int model, SpawnInfo spawninfo )
	{
		return SampNativeFunction.addPlayerClassEx( team, model, 
				spawninfo.position.x, spawninfo.position.y, spawninfo.position.z, spawninfo.position.angle,
				spawninfo.weapon1.id, spawninfo.weapon1.ammo, spawninfo.weapon2.id, spawninfo.weapon2.ammo,
				spawninfo.weapon3.id, spawninfo.weapon3.ammo );
	}

	@Override
	public void showNameTags( boolean enabled )
	{
		SampNativeFunction.showNameTags( enabled );
	}
	
	@Override
	public void showPlayerMarkers( int mode )
	{
		SampNativeFunction.showPlayerMarkers( mode );
	}
	
	@Override
	public void setWorldTime( int hour )
	{
		SampNativeFunction.setWorldTime( hour );
	}
	
	@Override
	public void enableTirePopping( boolean enabled )
	{
		SampNativeFunction.enableTirePopping( enabled );
	}
	
	@Override
	public void allowInteriorWeapons( boolean allow )
	{
		SampNativeFunction.allowInteriorWeapons( allow );
	}
	
	@Override
	public void setWeather( int weatherid )
	{
		SampNativeFunction.setWeather( weatherid );
	}
	
	@Override
	public void setGravity( float gravity )
	{
		SampNativeFunction.setGravity( gravity );
	}
	
	@Override
	public void setDeathDropAmount( int amount )
	{
		SampNativeFunction.setDeathDropAmount( amount );
		deathDropAmount = amount;
	}
	
	@Override
	public void createExplosion( Point point, int type, float radius )
	{
		SampNativeFunction.createExplosion( point.x, point.y, point.z, type, radius );
	}
	
	@Override
	public void enableZoneNames( boolean enabled )
	{
		SampNativeFunction.enableZoneNames( enabled );
	}
	
	@Override
	public void usePlayerPedAnims()
	{
		SampNativeFunction.usePlayerPedAnims();
	}
	
	@Override
	public void disableInteriorEnterExits()
	{
		SampNativeFunction.disableInteriorEnterExits();
	}
	
	@Override
	public void setNameTagDrawDistance( float distance )
	{
		nameTagDrawDistance = distance;
		SampNativeFunction.setNameTagDrawDistance( distance );
	}
	
	@Override
	public void disableNameTagLOS()
	{
		SampNativeFunction.disableNameTagLOS();
	}

	@Override
	public void sendRconCommand( String command )
	{
		if( command == null ) throw new NullPointerException();
		SampNativeFunction.sendRconCommand( command );
	}
	
	@Override
	public String getServerVarAsString( String varname )
	{
		if( varname == null ) throw new NullPointerException();
		return SampNativeFunction.getServerVarAsString( varname );
	}
	
	@Override
	public int getServerVarAsInt( String varname )
	{
		if( varname == null ) throw new NullPointerException();
		return SampNativeFunction.getServerVarAsInt( varname );
	}
	
	@Override
	public boolean getServerVarAsBool( String varname )
	{
		if( varname == null ) throw new NullPointerException();
		return SampNativeFunction.getServerVarAsBool( varname );
	}
	
	@Override
	public void connectNPC( String name, String script )
	{
		if( name == null || script == null ) throw new NullPointerException();
		SampNativeFunction.connectNPC( name, script );
	}
	
	@Override
	public void exit()
	{
		SampNativeFunction.gameModeExit();
	}
	
	@Override
	public String getNetworkStats()
	{
		return SampNativeFunction.getNetworkStats();
	}
	
	
//---------------------------------------------------------

	long lastTick = System.nanoTime();
	void onProcessTick()
	{	
		try
		{
			long nowTick = System.nanoTime();
			int interval = (int) ((nowTick - lastTick) / 1000 / 1000);
			lastTick = nowTick;
			
			Iterator<Reference<Timer>> iterator = timerPool.iterator();
			while( iterator.hasNext() )
			{
				Timer timer = iterator.next().get();
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
			eventDispatcher.dispatchEvent( new GamemodeExitEvent(this) );
			
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
			Player player;
			currentPlayerId = playerid;
			
			try
			{
				player = (Player) playerCls.newInstance();
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
			
			eventDispatcher.dispatchEvent( new PlayerConnectEvent(player) );
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
			Player player = playerPool[playerid];
			logStream.log( "[part] " + player.name + " has left the server (" + playerid + ":" + reason + ")" );
			
			PlayerDisconnectEvent event = new PlayerDisconnectEvent(player, reason);
			
			eventDispatcher.dispatchEvent( event );
			
			player.eventDispatcher.dispatchEvent( event );
			
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
			Player player = playerPool[playerid];
			System.out.println( "[spawn] " + player.name + " has spawned (" + playerid + ")" );
			
			player.team = SampNativeFunction.getPlayerTeam(player.id);
			player.skin = SampNativeFunction.getPlayerSkin(player.id);
			
			player.playerAttach = new PlayerAttach(playerid);
			player.eventDispatcher.dispatchEvent( new PlayerSpawnEvent(player) );
			
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
			Player player = playerPool[playerid];
			Player killer = null;
			
			if( killerid != 0xFFFF )
			{
				killer = playerPool[killerid];
				logStream.log( "[kill] " + killer.name + " killed " + player.name +
						" (" + SampNativeFunction.getWeaponName(reason) + ")" );
				
				killer.eventDispatcher.dispatchEvent( new PlayerKillEvent(killer, player, reason) );
			}
			else logStream.log( "[death] " + player.name + " died (" + playerid + ":" + reason + ")" );
			
			player.playerAttach = new PlayerAttach(playerid);
			player.eventDispatcher.dispatchEvent( new PlayerDeathEvent(player, killer, reason) );
			
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
			Player player = playerPool[playerid];
			logStream.log( "[chat] [" + player.name + "]: " + text );
			
			PlayerTextEvent event = new PlayerTextEvent(player, text, 1);
			player.eventDispatcher.dispatchEvent( event );
			
			if( event.getResult() != 0 )
				Player.sendMessageToAll( Color.WHITE, "{FE8B13}" + player.getName() + ": {FFFFFF}" + text );
			
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
			Player player = playerPool[playerid];
			System.out.println( "[cmd] [" + player.name + "] " + cmdtext );
			
			PlayerCommandEvent event = new PlayerCommandEvent(player, cmdtext, 0);
			player.eventDispatcher.dispatchEvent( event );
			
			return event.getResult();
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
			Player player = playerPool[playerid];
			
			PlayerRequestClassEvent event = new PlayerRequestClassEvent(player, classid, 1);
			player.eventDispatcher.dispatchEvent( event );
			
			return event.getResult();
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
			Player player = playerPool[playerid];
			
			player.state = newstate;
			player.eventDispatcher.dispatchEvent( new PlayerStateChangeEvent(player, oldstate) );
			
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
			Player player = playerPool[playerid];
			
			CheckpointEnterEvent event = new CheckpointEnterEvent( player );
			
			player.checkpoint.eventDispatcher.dispatchEvent( event );
			player.eventDispatcher.dispatchEvent( event );
			
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
			Player player = playerPool[playerid];
			CheckpointLeaveEvent event = new CheckpointLeaveEvent(player);

			player.checkpoint.eventDispatcher.dispatchEvent( event );
			player.eventDispatcher.dispatchEvent( event );
			
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
			Player player = playerPool[playerid];
			RaceCheckpointEnterEvent event = new RaceCheckpointEnterEvent(player);
			
			player.raceCheckpoint.eventDispatcher.dispatchEvent( event );
			player.eventDispatcher.dispatchEvent( event );
			
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
			Player player = playerPool[playerid];
			RaceCheckpointLeaveEvent event = new RaceCheckpointLeaveEvent(player);
			
			player.raceCheckpoint.eventDispatcher.dispatchEvent( event );
			player.eventDispatcher.dispatchEvent( event );
			
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
			Player player = playerPool[playerid];
			
			PlayerRequestSpawnEvent event = new PlayerRequestSpawnEvent( player, 1 );
			player.eventDispatcher.dispatchEvent( event );
			
			return event.getResult();
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
			SampNativeFunction.getObjectPos(objectid, object.position);
			SampNativeFunction.getObjectRot(objectid, object.position);
			
			object.eventDispatcher.dispatchEvent( new ObjectMovedEvent(object) );
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
			Player player = playerPool[playerid];
			ObjectBase object = playerObjectPool[objectid + playerid*MAX_OBJECTS];
			
			object.speed = 0;  		
			
			object.eventDispatcher.dispatchEvent(new ObjectMovedEvent(object));
			player.eventDispatcher.dispatchEvent( new PlayerObjectMovedEvent(player, object) );
			
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
			Player player = playerPool[playerid];
			Pickup pickup = pickupPool[pickupid];

			System.out.println( "[pickup] " + player.name + " pickup " + pickup.model + " (" + pickup.type + ")" );
			
			PlayerPickupEvent event = new PlayerPickupEvent(player, pickup);
			
			pickup.eventDispatcher.dispatchEvent( event );
			player.eventDispatcher.dispatchEvent( event );
			
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
			
			Player player = playerPool[playerid];
			Menu menu = menuPool[SampNativeFunction.getPlayerMenu(playerid)];
			
			MenuSelectedEvent event = new MenuSelectedEvent( menu, player, row );
			
			player.eventDispatcher.dispatchEvent( event );
			menu.eventDispatcher.dispatchEvent( event );
			
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
			
			Player player = playerPool[playerid];
			Menu menu = menuPool[SampNativeFunction.getPlayerMenu(playerid)];
			
			MenuExitedEvent event = new MenuExitedEvent( menu, player );
			
			player.eventDispatcher.dispatchEvent( event );
			menu.eventDispatcher.dispatchEvent( event );
				
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
			Player player = playerPool[playerid];
			System.out.println( "[interior] " + player.name + " interior has changed to " + newinteriorid );
			
			player.position.interior = newinteriorid;
			player.eventDispatcher.dispatchEvent( new PlayerInteriorChangeEvent(player, oldinteriorid) );
			
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
			Player player = playerPool[playerid];
			
			player.keyState.keys = newkeys;
			//update keystate
			
			player.eventDispatcher.dispatchEvent( new PlayerKeyStateChangeEvent(player, oldkeys) );
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
			Player player = playerPool[playerid];
			player.update();
			
			player.eventDispatcher.dispatchEvent( new PlayerUpdateEvent(player) );
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
			Player player = playerPool[playerid];
			Player forplayer = playerPool[forplayerid];
			
			player.eventDispatcher.dispatchEvent( new PlayerStreamInEvent(player, forplayer) );
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
			Player player = playerPool[playerid];
			Player forplayer = playerPool[forplayerid];
			
			player.eventDispatcher.dispatchEvent( new PlayerStreamOutEvent(player, forplayer) );
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
			Player player = playerPool[playerid];
			Player clickedPlayer = playerPool[clickedplayerid];

			System.out.println( "[click] " + player.name + " has clicked " + clickedPlayer.name );
			
			
			PlayerClickPlayerEvent event = new PlayerClickPlayerEvent( player, clickedPlayer, source );
			
			player.eventDispatcher.dispatchEvent( event );
			clickedPlayer.eventDispatcher.dispatchEvent( event );
			
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
			Player player = playerPool[playerid];
			player.eventDispatcher.dispatchEvent( new PlayerEnterExitModShopEvent(player, enterexit, interiorid) );
			
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
			Player player = playerPool[playerid];
			Dialog dialog = dialogPool.get(dialogid).get();
			
			if( dialog == null )
			{
				dialogPool.remove( dialogid );
				return 0;
			}
			
			DialogResponseEvent event = new DialogResponseEvent(dialog, player, response, listitem, inputtext);
			
			player.eventDispatcher.dispatchEvent( event );
			dialog.eventDispatcher.dispatchEvent( event );
			
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
			Player player = playerPool[playerid];
			Vehicle vehicle = vehiclePool[vehicleid];
			
			System.out.println( "[vehicle] " + player.name + " enter a vehicle (" + vehicle.model + ")" );
			
			
			VehicleEnterEvent event = new VehicleEnterEvent(vehicle, player, ispassenger != 0);
			
			vehicle.eventDispatcher.dispatchEvent( event );
			player.eventDispatcher.dispatchEvent( event );
			
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
			Player player = playerPool[playerid];
			Vehicle vehicle = vehiclePool[vehicleid];

			System.out.println( "[vehicle] " + player.name + " leave a vehicle (" + vehicle.model + ")" );
			
			
			VehicleExitEvent event = new VehicleExitEvent(vehicle, player);
			
			vehicle.eventDispatcher.dispatchEvent( event );
			player.eventDispatcher.dispatchEvent( event );
			
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
			Vehicle vehicle = vehiclePool[vehicleid];
			
			vehicle.eventDispatcher.dispatchEvent( new VehicleSpawnEvent(vehicle) );
			
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
			Vehicle vehicle = vehiclePool[vehicleid];
			Player killer = playerPool[killerid];
			
			vehicle.eventDispatcher.dispatchEvent( new VehicleDeathEvent(vehicle, killer) );
			
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
			Player player = playerPool[playerid];
			Vehicle vehicle = vehiclePool[vehicleid];
			
			VehicleModEvent event = new VehicleModEvent(vehicle, componentid);
			
			int type = SampNativeFunction.getVehicleComponentType(componentid);
			vehicle.component.components[type] = SampNativeFunction.getVehicleComponentInSlot(vehicleid, type);
	
			vehicle.eventDispatcher.dispatchEvent( event );
			player.eventDispatcher.dispatchEvent( event );
			
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
			Player player = playerPool[playerid];
			Vehicle vehicle = vehiclePool[vehicleid];
			
			VehiclePaintjobEvent event = new VehiclePaintjobEvent(vehicle, paintjobid);
	
			vehicle.eventDispatcher.dispatchEvent( event );
			player.eventDispatcher.dispatchEvent( event );
			
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
			Player player = playerPool[playerid];
			Vehicle vehicle = vehiclePool[vehicleid];
			
			VehicleResprayEvent event = new VehicleResprayEvent( vehicle, color1, color2 );
	
			vehicle.eventDispatcher.dispatchEvent( event );
			player.eventDispatcher.dispatchEvent( event );
			
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
			Vehicle vehicle = vehiclePool[vehicleid];
			Player player = playerPool[playerid];
			
			SampNativeFunction.getVehicleDamageStatus(vehicleid, vehicle.damage);
			
			VehicleUpdateDamageEvent event = new VehicleUpdateDamageEvent(vehicle, player);
	
			vehicle.eventDispatcher.dispatchEvent( event );
			
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
			Player player = playerPool[forplayerid];
			Vehicle vehicle = vehiclePool[vehicleid];
			
			VehicleStreamInEvent event = new VehicleStreamInEvent(vehicle, player);
			
			vehicle.eventDispatcher.dispatchEvent( event );
			player.eventDispatcher.dispatchEvent( event );
	
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
			Player player = playerPool[forplayerid];
			Vehicle vehicle = vehiclePool[vehicleid];
			
			VehicleStreamOutEvent event = new VehicleStreamOutEvent(vehicle, player);
	
			player.eventDispatcher.dispatchEvent( event );
			vehicle.eventDispatcher.dispatchEvent( event );
			
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
			
			
			RconCommandEvent event = new RconCommandEvent(cmd, 0);
			eventDispatcher.dispatchEvent( event );
			
			return event.getResult();
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
			
			eventDispatcher.dispatchEvent( new RconLoginEvent(ip, password, success!=0) );
			
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
			Player player = playerPool[playerid];
			Vehicle vehicle = vehiclePool[vehicleid];
			
			VehicleUnoccupiedUpdateEvent event = new VehicleUnoccupiedUpdateEvent(vehicle, player, passenger_seat);
	
			player.eventDispatcher.dispatchEvent( event );
			
			vehicle.eventDispatcher.dispatchEvent( event );
			
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
