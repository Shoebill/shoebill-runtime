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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.gtaun.shoebill.object.impl.PlayerImpl;
import net.gtaun.shoebill.object.primitive.DialogPrim;
import net.gtaun.shoebill.object.primitive.LabelPrim;
import net.gtaun.shoebill.object.primitive.MenuPrim;
import net.gtaun.shoebill.object.primitive.ObjectPrim;
import net.gtaun.shoebill.object.primitive.PickupPrim;
import net.gtaun.shoebill.object.primitive.PlayerPrim;
import net.gtaun.shoebill.object.primitive.PlayerLabelPrim;
import net.gtaun.shoebill.object.primitive.PlayerObjectPrim;
import net.gtaun.shoebill.object.primitive.ServerPrim;
import net.gtaun.shoebill.object.primitive.TextdrawPrim;
import net.gtaun.shoebill.object.primitive.TimerPrim;
import net.gtaun.shoebill.object.primitive.VehiclePrim;
import net.gtaun.shoebill.object.primitive.WorldPrim;
import net.gtaun.shoebill.object.primitive.ZonePrim;
import net.gtaun.shoebill.samp.SampCallbackHandler;
import net.gtaun.shoebill.samp.AbstractSampCallbackHandler;
import net.gtaun.shoebill.util.event.EventManager;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author MK124
 *
 */

public class SampObjectPoolImpl implements SampObjectPool
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
	
	
	private SampCallbackHandler callbackHandler;
	
	private ServerPrim server;
	private WorldPrim world;
	
	private PlayerPrim[] players									= new PlayerPrim[MAX_PLAYERS];
	private VehiclePrim[] vehicles									= new VehiclePrim[MAX_VEHICLES];
	private ObjectPrim[] objects									= new ObjectPrim[MAX_OBJECTS];
	private PlayerObjectPrim[][] playerObjectsArrays				= new PlayerObjectPrim[MAX_PLAYERS][];
	private PickupPrim[] pickups									= new PickupPrim[MAX_PICKUPS];
	private LabelPrim[] labels										= new LabelPrim[MAX_GLOBAL_LABELS];
	private PlayerLabelPrim[][] playerLabelsArrays					= new PlayerLabelPrim[MAX_PLAYERS][];
	private TextdrawPrim[] textdraws								= new TextdrawPrim[MAX_TEXT_DRAWS];
	private ZonePrim[] zones										= new ZonePrim[MAX_ZONES];
	private MenuPrim[] menus										= new MenuPrim[MAX_MENUS];
	
	private Collection<Reference<TimerPrim>> timers				= new ConcurrentLinkedQueue<>();
	private Map<Integer, Reference<DialogPrim>> dialogs			= new ConcurrentHashMap<>();
	
	
	SampObjectPoolImpl( final EventManager eventManager )
	{
		callbackHandler = new AbstractSampCallbackHandler()
		{
			@Override
			public int onPlayerConnect( int playerid )
			{
				try
				{
					playerObjectsArrays[playerid] = new PlayerObjectPrim[MAX_OBJECTS];
					playerObjectsArrays[playerid] = new PlayerObjectPrim[MAX_OBJECTS];
					playerLabelsArrays[playerid] = new PlayerLabelPrim[MAX_PLAYER_LABELS];
					
					PlayerPrim player = new PlayerImpl(playerid);
					setPlayer( playerid, player );
				}
				catch( Exception e )
				{
					e.printStackTrace();
				}
				
				return 1;
			}
			
			@Override
			public int onPlayerDisconnect( int playerid, int reason )
			{
				return 1;
			}
		};
	}

	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
	
	SampCallbackHandler getCallbackHandler()
	{
		return callbackHandler;
	}
	
	
	@Override
	public ServerPrim getServer()
	{
		return server;
	}
	
	@Override
	public WorldPrim getWorld()
	{
		return world;
	}
	
	@Override
	public PlayerPrim getPlayer( int id )
	{
		if( id < 0 || id >= MAX_PLAYERS ) return null;
		return players[id];
	}
	
	@Override
	public VehiclePrim getVehicle( int id )
	{
		if( id < 0 || id >= MAX_VEHICLES ) return null;
		return vehicles[id];
	}
	
	@Override
	public ObjectPrim getObject( int id )
	{
		if( id < 0 || id >= MAX_OBJECTS ) return null;
		return objects[id];
	}
	
	@Override
	public PlayerObjectPrim getPlayerObject( PlayerPrim player, int id )
	{
		if( player == null ) return null;
		
		int playerId = player.getId();
		if( playerId < 0 || playerId >= MAX_PLAYERS ) return null;
		
		if( id < 0 || id >= MAX_OBJECTS ) return null;
		
		PlayerObjectPrim[] playerObjects = playerObjectsArrays[playerId];
		if( playerObjects == null ) return null;
		
		return playerObjects[id];
	}
	
	@Override
	public PickupPrim getPickup( int id )
	{
		if( id < 0 || id >= MAX_PICKUPS ) return null;
		return pickups[id];
	}
	
	@Override
	public LabelPrim getLabel( int id )
	{
		if( id < 0 || id >= MAX_GLOBAL_LABELS ) return null;
		return labels[id];
	}
	
	@Override
	public PlayerLabelPrim getPlayerLabel( PlayerPrim player, int id )
	{
		if( player == null ) return null;
		
		int playerId = player.getId();
		if( playerId < 0 || playerId >= MAX_PLAYERS ) return null;
		
		if( id < 0 || id >= MAX_PLAYER_LABELS ) return null;
		
		PlayerLabelPrim[] playerLabels = playerLabelsArrays[playerId];
		if( playerLabels == null ) return null;
		
		return playerLabels[id];
	}
	
	@Override
	public TextdrawPrim getTextdraw( int id )
	{
		if( id < 0 || id >= MAX_TEXT_DRAWS ) return null;
		return textdraws[id];
	}
	
	@Override
	public ZonePrim getZone( int id )
	{
		if( id < 0 || id >= MAX_ZONES ) return null;
		return zones[id];
	}
	
	@Override
	public MenuPrim getMenu( int id )
	{
		if( id < 0 || id >= MAX_MENUS ) return null;
		return menus[id];
	}
	
	@Override
	public DialogPrim getDialog( int id )
	{
		return dialogs.get(id).get();
	}
	
	
	@Override
	public Collection<PlayerPrim> getPlayers()
	{
		return getInstances( players, PlayerPrim.class );
	}
	
	@Override
	public Collection<VehiclePrim> getVehicles()
	{
		return getInstances( players, VehiclePrim.class );
	}
	
	@Override
	public Collection<ObjectPrim> getObjects()
	{
		return getInstances( objects, ObjectPrim.class );
	}
	
	@Override
	public Collection<PlayerObjectPrim> getPlayerObjects( PlayerPrim player )
	{
		return getPlayerObjects( player, PlayerObjectPrim.class );
	}
	
	@Override
	public Collection<PickupPrim> getPickups()
	{
		return getInstances( pickups, PickupPrim.class );
	}
	
	@Override
	public Collection<LabelPrim> getLabels()
	{
		return getInstances( labels, LabelPrim.class );
	}
	
	@Override
	public Collection<PlayerLabelPrim> getPlayerLabels( PlayerPrim player )
	{
		return getPlayerLabels( player, PlayerLabelPrim.class );
	}
	
	@Override
	public Collection<TextdrawPrim> getTextdraws()
	{
		return getInstances( textdraws, TextdrawPrim.class );
	}
	
	@Override
	public Collection<ZonePrim> getZones()
	{
		return getInstances( zones, ZonePrim.class );
	}
	
	@Override
	public Collection<MenuPrim> getMenus()
	{
		return getInstances( menus, MenuPrim.class );
	}
	
	@Override
	public Collection<DialogPrim> getDialogs()
	{
		return getDialogs( DialogPrim.class );
	}
	
	@Override
	public Collection<TimerPrim> getTimers()
	{
		return getTimers( TimerPrim.class );
	}
	
	
	@Override
	public <T extends PlayerPrim> Collection<T> getPlayers( Class<T> cls )
	{
		return getInstances( players, cls );
	}
	
	@Override
	public <T extends VehiclePrim> Collection<T> getVehicles( Class<T> cls )
	{
		return getInstances( vehicles, cls );
	}
	
	@Override
	public <T extends ObjectPrim> Collection<T> getObjects( Class<T> cls )
	{
		return getInstances( objects, cls );
	}
	
	@Override
	public <T extends PlayerObjectPrim> Collection<T> getPlayerObjects( PlayerPrim player, Class<T> cls )
	{
		if( player == null ) return null;
		
		int playerId = player.getId();
		if( playerId < 0 || playerId >= MAX_PLAYERS ) return new ArrayList<T>(0);
		
		PlayerObjectPrim[] playerObjects = playerObjectsArrays[playerId];
		return getInstances( playerObjects, cls );
	}
	
	@Override
	public <T extends PickupPrim> Collection<T> getPickups( Class<T> cls )
	{
		return getInstances( pickups, cls );
	}
	
	@Override
	public <T extends LabelPrim> Collection<T> getLabels( Class<T> cls )
	{
		return getInstances( labels, cls );
	}
	
	@Override
	public <T extends PlayerLabelPrim> Collection<T> getPlayerLabels( PlayerPrim player, Class<T> cls )
	{
		if( player == null ) return null;
		
		int playerId = player.getId();
		if( playerId < 0 || playerId >= MAX_PLAYERS ) return new ArrayList<T>(0);
		
		PlayerLabelPrim[] playerLabels = playerLabelsArrays[playerId];
		return getInstances( playerLabels, cls );
	}
	
	@Override
	public <T extends TextdrawPrim> Collection<T> getTextdraws( Class<T> cls )
	{
		return getInstances( textdraws, cls );
	}
	
	@Override
	public <T extends ZonePrim> Collection<T> getZones( Class<T> cls )
	{
		return getInstances( zones, cls );
	}
	
	@Override
	public <T extends MenuPrim> Collection<T> getMenus( Class<T> cls )
	{
		return getInstances( menus, cls );
	}
	
	@Override
	public <T extends DialogPrim> Collection<T> getDialogs( Class<T> cls )
	{
		Collection<T> items = new ArrayList<T>();
		for( Reference<DialogPrim> reference : dialogs.values() )
		{
			DialogPrim dialog = reference.get();
			if( ! cls.isInstance(dialog) ) continue;
			
			items.add( cls.cast(dialog) );
		}
		
		return items;
	}
	
	@Override
	public <T extends TimerPrim> Collection<T> getTimers( Class<T> cls )
	{
		Collection<T> items = new ArrayList<T>();
		for( Reference<TimerPrim> reference : timers )
		{
			TimerPrim timer = reference.get();
			if( ! cls.isInstance(timer) ) continue;
			
			items.add( cls.cast(timer) );
		}
		
		return items;
	}
	
	
	public void setServer( ServerPrim server )
	{
		this.server = server;
	}
	
	public void setWorld( WorldPrim world )
	{
		this.world = world;
	}
	
	public void setPlayer( int id, PlayerPrim player )
	{
		players[ id ] = player;
	}
	
	public void setVehicle( int id, VehiclePrim vehicle )
	{
		vehicles[ id ] = vehicle;
	}
	
	public void setObject( int id, ObjectPrim object )
	{
		objects[ id ] = object;
	}
	
	public void setPlayerObject( PlayerPrim player, int id, PlayerObjectPrim object )
	{
		PlayerObjectPrim[] playerObjects = playerObjectsArrays[player.getId()];
		playerObjects[ id ] = object;
	}
	
	public void setPickup( int id, PickupPrim pickup )
	{
		pickups[ id ] = pickup;
	}
	
	public void setLabel( int id, LabelPrim label )
	{
		labels[ id ] = label;
	}
	
	public void setPlayerLabel( PlayerPrim player, int id, PlayerLabelPrim label )
	{
		PlayerLabelPrim[] playerLabels = playerLabelsArrays[player.getId()];
		playerLabels[ id ] = label;
	}
	
	public void setTextdraw( int id, TextdrawPrim textdraw )
	{
		textdraws[ id ] = textdraw;
	}
	
	public void setZone( int id, ZonePrim zone )
	{
		zones[ id ] = zone;
	}

	public void setMenu( int id, MenuPrim menu )
	{
		menus[ id ] = menu;
	}
	
	public void putTimer( TimerPrim timer )
	{
		for( Reference<TimerPrim> ref : timers )
		{
			if( ref.get() == null ) timers.remove( ref );
		}
		
		timers.add( new WeakReference<TimerPrim>( timer ) );
	}
	
	public void putDialog( int id, DialogPrim dialog )
	{
		for( Entry<Integer, Reference<DialogPrim>> entry : dialogs.entrySet() )
		{
			if( entry.getValue().get() == null ) dialogs.remove( entry.getKey() );
		}
		
		dialogs.put( id, new WeakReference<DialogPrim>( dialog ) );
	}
	
	public void removePlayer( PlayerPrim player )
	{
		for( PlayerLabelPrim playerLabel : getPlayerLabels(player) ) playerLabel.destroy();
		for( PlayerObjectPrim playerObject : getPlayerObjects(player) ) playerObject.destroy();
		setPlayer( player.getId(), null );
	}
}
