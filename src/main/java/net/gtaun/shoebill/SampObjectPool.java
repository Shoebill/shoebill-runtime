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

package net.gtaun.shoebill;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.gtaun.shoebill.object.IDialog;
import net.gtaun.shoebill.object.ILabel;
import net.gtaun.shoebill.object.IMenu;
import net.gtaun.shoebill.object.IObject;
import net.gtaun.shoebill.object.IPickup;
import net.gtaun.shoebill.object.IPlayer;
import net.gtaun.shoebill.object.IPlayerLabel;
import net.gtaun.shoebill.object.IPlayerObject;
import net.gtaun.shoebill.object.IServer;
import net.gtaun.shoebill.object.ITextdraw;
import net.gtaun.shoebill.object.ITimer;
import net.gtaun.shoebill.object.IVehicle;
import net.gtaun.shoebill.object.IWorld;
import net.gtaun.shoebill.object.IZone;
import net.gtaun.shoebill.object.Player;
import net.gtaun.shoebill.plugin.Gamemode;
import net.gtaun.shoebill.samp.ISampCallbackHandler;
import net.gtaun.shoebill.samp.SampCallbackHandler;
import net.gtaun.shoebill.util.event.EventManager;

/**
 * @author MK124
 *
 */

public class SampObjectPool implements ISampObjectPool
{
	public static final int MAX_PLAYERS =				800;
	public static final int MAX_VEHICLES =				2000;
	public static final int MAX_OBJECTS =				1000;
	public static final int MAX_ZONES =					1024;
	public static final int MAX_TEXT_DRAWS =			2048;
	public static final int MAX_MENUS =					128;
	public static final int MAX_GLOBAL_LABELS =			1024;
	public static final int MAX_PLAYER_LABELS =			1024;
	public static final int MAX_PICKUPS =				4096;
	
	
	private static <T> Collection<T> getInstances( Object[] items, Class<T> cls )
	{
		Collection<T> list = new ArrayList<>();
		if( items != null ) for( Object item : items ) if( cls.isInstance(item) ) list.add( cls.cast(item) );
		return list;		
	}
	
	
	ISampCallbackHandler callbackHandler;
	
	IServer server;
	IWorld world;
	Gamemode gamemode;
	
	IPlayer[] players									= new IPlayer[MAX_PLAYERS];
	IVehicle[] vehicles									= new IVehicle[MAX_VEHICLES];
	IObject[] objects									= new IObject[MAX_OBJECTS];
	IPlayerObject[][] playerObjectsArrays				= new IPlayerObject[MAX_PLAYERS][];
	IPickup[] pickups									= new IPickup[MAX_PICKUPS];
	ILabel[] labels										= new ILabel[MAX_GLOBAL_LABELS];
	IPlayerLabel[][] playerLabelsArrays					= new IPlayerLabel[MAX_PLAYERS][];
	ITextdraw[] textdraws								= new ITextdraw[MAX_TEXT_DRAWS];
	IZone[] zones										= new IZone[MAX_ZONES];
	IMenu[] menus										= new IMenu[MAX_MENUS];
	
	Collection<Reference<ITimer>> timers				= new ConcurrentLinkedQueue<>();
	Map<Integer, Reference<IDialog>> dialogs			= new ConcurrentHashMap<>();
	
	Class<? extends IPlayer> playerClass = Player.class;
	
	
	SampObjectPool( final EventManager eventManager )
	{
		callbackHandler = new SampCallbackHandler()
		{
			public int onPlayerConnect( int playerid )
			{
				try
				{
					playerObjectsArrays[playerid] = new IPlayerObject[MAX_OBJECTS];
					playerLabelsArrays[playerid] = new IPlayerLabel[MAX_PLAYER_LABELS];
					
					Constructor<? extends IPlayer> constructor = playerClass.getConstructor( int.class );
					IPlayer player = constructor.newInstance( playerid );
					
					setPlayer( playerid, player );
				}
				catch( Exception e )
				{
					e.printStackTrace();
				}
				
				return 1;
			}
			
			public int onPlayerDisconnect( int playerid, int reason )
			{
				return 1;
			}
		};
	}
	
	
	ISampCallbackHandler getCallbackHandler()
	{
		return callbackHandler;
	}
	
	
	@Override
	public IServer getServer()
	{
		return server;
	}
	
	@Override
	public IWorld getWorld()
	{
		return world;
	}
	
	@Override
	public Gamemode getGamemode()
	{
		return gamemode;
	}
	
	@Override
	public IPlayer getPlayer( int id )
	{
		if( id < 0 || id >= MAX_PLAYERS ) return null;
		return players[id];
	}
	
	@Override
	public IVehicle getVehicle( int id )
	{
		if( id < 0 || id >= MAX_VEHICLES ) return null;
		return vehicles[id];
	}
	
	@Override
	public IObject getObject( int id )
	{
		if( id < 0 || id >= MAX_OBJECTS ) return null;
		return objects[id];
	}
	
	@Override
	public IPlayerObject getPlayerObject( IPlayer player, int id )
	{
		if( player == null ) return null;
		
		int playerId = player.getId();
		if( playerId < 0 || playerId >= MAX_PLAYERS ) return null;
		
		if( id < 0 || id >= MAX_OBJECTS ) return null;
		
		IPlayerObject[] playerObjects = playerObjectsArrays[playerId];
		if( playerObjects == null ) return null;
		
		return playerObjects[id];
	}
	
	@Override
	public IPickup getPickup( int id )
	{
		if( id < 0 || id >= MAX_PICKUPS ) return null;
		return pickups[id];
	}
	
	@Override
	public ILabel getLabel( int id )
	{
		if( id < 0 || id >= MAX_GLOBAL_LABELS ) return null;
		return labels[id];
	}
	
	@Override
	public IPlayerLabel getPlayerLabel( IPlayer player, int id )
	{
		if( player == null ) return null;
		
		int playerId = player.getId();
		if( playerId < 0 || playerId >= MAX_PLAYERS ) return null;
		
		if( id < 0 || id >= MAX_PLAYER_LABELS ) return null;
		
		IPlayerLabel[] playerLabels = playerLabelsArrays[playerId];
		if( playerLabels == null ) return null;
		
		return playerLabels[id];
	}
	
	@Override
	public ITextdraw getTextdraw( int id )
	{
		if( id < 0 || id >= MAX_TEXT_DRAWS ) return null;
		return textdraws[id];
	}
	
	@Override
	public IZone getZone( int id )
	{
		if( id < 0 || id >= MAX_ZONES ) return null;
		return zones[id];
	}
	
	@Override
	public IMenu getMenu( int id )
	{
		if( id < 0 || id >= MAX_MENUS ) return null;
		return menus[id];
	}
	
	@Override
	public IDialog getDialog( int id )
	{
		return dialogs.get(id).get();
	}
	
	
	@Override
	public Collection<IPlayer> getPlayers()
	{
		return getInstances( players, IPlayer.class );
	}
	
	@Override
	public Collection<IVehicle> getVehicles()
	{
		return getInstances( players, IVehicle.class );
	}
	
	@Override
	public Collection<IObject> getObjects()
	{
		return getInstances( objects, IObject.class );
	}
	
	@Override
	public Collection<IPlayerObject> getPlayerObjects( IPlayer player )
	{
		return getPlayerObjects( player, IPlayerObject.class );
	}
	
	@Override
	public Collection<IPickup> getPickups()
	{
		return getInstances( pickups, IPickup.class );
	}
	
	@Override
	public Collection<ILabel> getLabels()
	{
		return getInstances( labels, ILabel.class );
	}
	
	@Override
	public Collection<IPlayerLabel> getPlayerLabels( IPlayer player )
	{
		return getPlayerLabels( player, IPlayerLabel.class );
	}
	
	@Override
	public Collection<ITextdraw> getTextdraws()
	{
		return getInstances( textdraws, ITextdraw.class );
	}
	
	@Override
	public Collection<IZone> getZones()
	{
		return getInstances( zones, IZone.class );
	}
	
	@Override
	public Collection<IMenu> getMenus()
	{
		return getInstances( menus, IMenu.class );
	}
	
	@Override
	public Collection<IDialog> getDialogs()
	{
		return getDialogs( IDialog.class );
	}
	
	@Override
	public Collection<ITimer> getTimers()
	{
		return getTimers( ITimer.class );
	}
	
	
	@Override
	public <T extends IPlayer> Collection<T> getPlayers( Class<T> cls )
	{
		return getInstances( players, cls );
	}
	
	@Override
	public <T extends IVehicle> Collection<T> getVehicles( Class<T> cls )
	{
		return getInstances( vehicles, cls );
	}
	
	@Override
	public <T extends IObject> Collection<T> getObjects( Class<T> cls )
	{
		return getInstances( objects, cls );
	}
	
	@Override
	public <T extends IPlayerObject> Collection<T> getPlayerObjects( IPlayer player, Class<T> cls )
	{
		if( player == null ) return null;
		
		int playerId = player.getId();
		if( playerId < 0 || playerId >= MAX_PLAYERS ) return new ArrayList<T>(0);
		
		IPlayerObject[] playerObjects = playerObjectsArrays[playerId];
		return getInstances( playerObjects, cls );
	}
	
	@Override
	public <T extends IPickup> Collection<T> getPickups( Class<T> cls )
	{
		return getInstances( pickups, cls );
	}
	
	@Override
	public <T extends ILabel> Collection<T> getLabels( Class<T> cls )
	{
		return getInstances( labels, cls );
	}
	
	@Override
	public <T extends IPlayerLabel> Collection<T> getPlayerLabels( IPlayer player, Class<T> cls )
	{
		if( player == null ) return null;
		
		int playerId = player.getId();
		if( playerId < 0 || playerId >= MAX_PLAYERS ) return new ArrayList<T>(0);
		
		IPlayerLabel[] playerLabels = playerLabelsArrays[playerId];
		return getInstances( playerLabels, cls );
	}
	
	@Override
	public <T extends ITextdraw> Collection<T> getTextdraws( Class<T> cls )
	{
		return getInstances( textdraws, cls );
	}
	
	@Override
	public <T extends IZone> Collection<T> getZones( Class<T> cls )
	{
		return getInstances( zones, cls );
	}
	
	@Override
	public <T extends IMenu> Collection<T> getMenus( Class<T> cls )
	{
		return getInstances( menus, cls );
	}
	
	@Override
	public <T extends IDialog> Collection<T> getDialogs( Class<T> cls )
	{
		Collection<T> items = new ArrayList<T>();
		for( Reference<IDialog> reference : dialogs.values() )
		{
			IDialog dialog = reference.get();
			if( ! cls.isInstance(dialog) ) continue;
			
			items.add( cls.cast(dialog) );
		}
		
		return items;
	}
	
	@Override
	public <T extends ITimer> Collection<T> getTimers( Class<T> cls )
	{
		Collection<T> items = new ArrayList<T>();
		for( Reference<ITimer> reference : timers )
		{
			ITimer timer = reference.get();
			if( ! cls.isInstance(timer) ) continue;
			
			items.add( cls.cast(timer) );
		}
		
		return items;
	}
	
	
	public void setServer( IServer server )
	{
		this.server = server;
	}
	
	public void setWorld( IWorld world )
	{
		this.world = world;
	}
	
	public void setGamemode( Gamemode gamemode )
	{
		this.gamemode = gamemode;
	}
	
	public void setPlayer( int id, IPlayer player )
	{
		players[ id ] = player;
	}
	
	public void setVehicle( int id, IVehicle vehicle )
	{
		vehicles[ id ] = vehicle;
	}
	
	public void setObject( int id, IObject object )
	{
		objects[ id ] = object;
	}
	
	public void setPlayerObject( IPlayer player, int id, IPlayerObject object )
	{
		IPlayerObject[] playerObjects = playerObjectsArrays[player.getId()];
		playerObjects[ id ] = object;
	}
	
	public void setPickup( int id, IPickup pickup )
	{
		pickups[ id ] = pickup;
	}
	
	public void setLabel( int id, ILabel label )
	{
		labels[ id ] = label;
	}
	
	public void setPlayerLabel( IPlayer player, int id, IPlayerLabel label )
	{
		IPlayerLabel[] playerLabels = playerLabelsArrays[player.getId()];
		playerLabels[ id ] = label;
	}
	
	public void setTextdraw( int id, ITextdraw textdraw )
	{
		textdraws[ id ] = textdraw;
	}
	
	public void setZone( int id, IZone zone )
	{
		zones[ id ] = zone;
	}

	public void setMenu( int id, IMenu menu )
	{
		menus[ id ] = menu;
	}
	
	public void putTimer( ITimer timer )
	{
		for( Reference<ITimer> ref : timers )
		{
			if( ref.get() == null ) timers.remove( ref );
		}
		
		timers.add( new WeakReference<ITimer>( timer ) );
	}
	
	public void putDialog( int id, IDialog dialog )
	{
		for( Entry<Integer, Reference<IDialog>> entry : dialogs.entrySet() )
		{
			if( entry.getValue().get() == null ) dialogs.remove( entry.getKey() );
		}
		
		dialogs.put( id, new WeakReference<IDialog>( dialog ) );
	}
	
	public void removePlayer( IPlayer player )
	{
		for( IPlayerLabel playerLabel : getPlayerLabels(player) ) playerLabel.destroy();
		for( IPlayerObject playerObject : getPlayerObjects(player) ) playerObject.destroy();
		setPlayer( player.getId(), null );
	}
	
	public <T extends IPlayer> void setPlayerClass( Class<T> cls )
	{
		playerClass = cls;
	}
}
