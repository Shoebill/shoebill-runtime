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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.WeakHashMap;

import net.gtaun.shoebill.object.Dialog;
import net.gtaun.shoebill.object.Label;
import net.gtaun.shoebill.object.Menu;
import net.gtaun.shoebill.object.ObjectBase;
import net.gtaun.shoebill.object.Pickup;
import net.gtaun.shoebill.object.Player;
import net.gtaun.shoebill.object.PlayerLabel;
import net.gtaun.shoebill.object.PlayerObject;
import net.gtaun.shoebill.object.Textdraw;
import net.gtaun.shoebill.object.Timer;
import net.gtaun.shoebill.object.Vehicle;
import net.gtaun.shoebill.object.Zone;

/**
 * @author MK124
 *
 */

public class SampObjectPool implements ISampObjectPool
{
	public static final int MAX_PLAYERS =				500;
	public static final int MAX_VEHICLES =				2000;
	public static final int MAX_OBJECTS =				400;
	public static final int MAX_ZONES =					1024;
	public static final int MAX_TEXT_DRAWS =			2048;
	public static final int MAX_MENUS =					128;
	public static final int MAX_LABELS_GLOBAL =			1024;
	public static final int MAX_LABELS_PLAYER =			1024;
	public static final int MAX_PICKUPS =				2048;
	
	
	static <T> Collection<T> getInstances( Object[] items, Class<T> cls )
	{
		Collection<T> list = new Vector<T>();
		for( Object item : items ) if( cls.isInstance(item) ) list.add( cls.cast(item) );
		return list;		
	}
	
	
	Player[] players									= new Player[MAX_PLAYERS];
	Vehicle[] vehicles									= new Vehicle[MAX_VEHICLES];
	ObjectBase[] objects								= new ObjectBase[MAX_OBJECTS];
	Map<Player, PlayerObject[]> playerObjects			= new WeakHashMap<Player, PlayerObject[]>();
	Pickup[] pickups									= new Pickup[MAX_PICKUPS];
	Label[] labels										= new Label[MAX_LABELS_GLOBAL];
	Map<Player, PlayerLabel[]> playerLabels				= new WeakHashMap<Player, PlayerLabel[]>();
	Textdraw[] textdraws								= new Textdraw[MAX_TEXT_DRAWS];
	Zone[] zones										= new Zone[MAX_ZONES];
	Menu[] menus										= new Menu[MAX_MENUS];
	
	List<Reference<Timer>> timers						= new Vector<Reference<Timer>>();
	Map<Integer, Reference<Dialog>> dialogs				= new HashMap<Integer, Reference<Dialog>>();
	
	int currentPlayerId = -1;
	
	
	SampObjectPool()
	{
		
	}
	
	
	@Override
	public Player getPlayer( int id )
	{
		return players[id];
	}
	
	@Override
	public Vehicle getVehicle( int id )
	{
		return vehicles[id];
	}
	
	@Override
	public ObjectBase getObject( int id )
	{
		return objects[id];
	}
	
	@Override
	public PlayerObject getPlayerObject( Player player, int id )
	{
		return playerObjects.get( player ) [id];
	}
	
	@Override
	public Pickup getPickup( int id )
	{
		return pickups[id];
	}
	
	@Override
	public Label getLabel( int id )
	{
		return labels[id];
	}
	
	@Override
	public PlayerLabel getPlayerLabel( Player player, int id )
	{
		return playerLabels.get( player ) [id];
	}
	
	@Override
	public Textdraw getTextdraw( int id )
	{
		return textdraws[id];
	}
	
	@Override
	public Zone getZone( int id )
	{
		return zones[id];
	}
	
	@Override
	public Menu getMenu( int id )
	{
		return menus[id];
	}
	
	@Override
	public Dialog getDialog( int id )
	{
		for( Dialog dialog : getDialogs() ) if( dialog.getId() == id ) return dialog;
		return null;
	}
	
	
	@Override
	public Collection<Player> getPlayers()
	{
		return getInstances( players, Player.class );
	}
	
	@Override
	public Collection<Vehicle> getVehicles()
	{
		return getInstances( players, Vehicle.class );
	}
	
	@Override
	public Collection<ObjectBase> getObjects()
	{
		return getInstances( objects, ObjectBase.class );
	}
	
	@Override
	public Collection<PlayerObject> getPlayerObjects( Player player )
	{
		return getInstances( playerObjects.get(player), PlayerObject.class );
	}
	
	@Override
	public Collection<Pickup> getPickups()
	{
		return getInstances( pickups, Pickup.class );
	}
	
	@Override
	public Collection<Label> getLabels()
	{
		return getInstances( labels, Label.class );
	}
	
	@Override
	public Collection<PlayerLabel> getPlayerLabels( Player player )
	{
		return getInstances( playerLabels.get(player), PlayerLabel.class );
	}
	
	@Override
	public Collection<Textdraw> getTextdraws()
	{
		return getInstances( textdraws, Textdraw.class );
	}
	
	@Override
	public Collection<Zone> getZones()
	{
		return getInstances( zones, Zone.class );
	}
	
	@Override
	public Collection<Menu> getMenus()
	{
		return getInstances( menus, Menu.class );
	}
	
	@Override
	public Collection<Dialog> getDialogs()
	{
		return getDialogs( Dialog.class );
	}
	
	@Override
	public Collection<Timer> getTimers()
	{
		return getTimers( Timer.class );
	}
	
	
	@Override
	public <T extends Player> Collection<T> getPlayers( Class<T> cls )
	{
		return getInstances( players, cls );
	}
	
	@Override
	public <T extends Vehicle> Collection<T> getVehicles( Class<T> cls )
	{
		return getInstances( vehicles, cls );
	}
	
	@Override
	public <T extends ObjectBase> Collection<T> getObjects( Class<T> cls )
	{
		return getInstances( objects, cls );
	}
	
	@Override
	public <T extends PlayerObject> Collection<T> getPlayerObjects( Player player, Class<T> cls )
	{
		return getInstances( playerObjects.get(player), cls );
	}
	
	@Override
	public <T extends Pickup> Collection<T> getPickups( Class<T> cls )
	{
		return getInstances( pickups, cls );
	}
	
	@Override
	public <T extends Label> Collection<T> getLabels( Class<T> cls )
	{
		return getInstances( labels, cls );
	}
	
	@Override
	public <T extends PlayerLabel> Collection<T> getPlayerLabels( Player player, Class<T> cls )
	{
		return getInstances( playerLabels.get(player), cls );
	}
	
	@Override
	public <T extends Textdraw> Collection<T> getTextdraws( Class<T> cls )
	{
		return getInstances( textdraws, cls );
	}
	
	@Override
	public <T extends Zone> Collection<T> getZones( Class<T> cls )
	{
		return getInstances( zones, cls );
	}
	
	@Override
	public <T extends Menu> Collection<T> getMenus( Class<T> cls )
	{
		return getInstances( menus, cls );
	}
	
	@Override
	public <T extends Dialog> Collection<T> getDialogs( Class<T> cls )
	{
		Collection<T> items = new Vector<T>();
		for( Reference<Dialog> reference : dialogs.values() )
		{
			Dialog dialog = reference.get();
			if( ! cls.isInstance(dialog) ) continue;
			
			items.add( cls.cast(dialog) );
		}
		
		return items;
	}
	
	@Override
	public <T extends Timer> Collection<T> getTimers( Class<T> cls )
	{
		Collection<T> items = new Vector<T>();
		for( Reference<Timer> reference : timers )
		{
			Timer timer = reference.get();
			if( ! cls.isInstance(timer) ) continue;
			
			items.add( cls.cast(timer) );
		}
		
		return items;
	}
	
	
	public int getCurrentPlayerId()
	{
		return currentPlayerId;
	}
	
	public void setPlayer( int id, Player player )
	{
		players[ id ] = player;
	}
	
	public void setVehicle( int id, Vehicle vehicle )
	{
		vehicles[ id ] = vehicle;
	}
	
	public void setObject( int id, ObjectBase object )
	{
		objects[ id ] = object;
	}
	
	public void setPlayerObject( Player player, int id, PlayerObject object )
	{
		playerObjects.get( player ) [ id ] = object;
	}
	
	public void setPickup( int id, Pickup pickup )
	{
		pickups[ id ] = pickup;
	}
	
	public void setLabel( int id, Label label )
	{
		labels[ id ] = label;
	}
	
	public void setPlayerLabel( Player player, int id, PlayerLabel label )
	{
		playerLabels.get( player ) [ id ] = label;
	}
	
	public void setTextdraw( int id, Textdraw textdraw )
	{
		textdraws[ id ] = textdraw;
	}
	
	public void setZone( int id, Zone zone )
	{
		zones[ id ] = zone;
	}

	public void setMenu( int id, Menu menu )
	{
		menus[ id ] = menu;
	}
	
	public void putTimer( Timer timer )
	{
		timers.add( new WeakReference<Timer>( timer ) );
	}
	
	public void putDialog( int id, Dialog dialog )
	{
		dialogs.put( id, new WeakReference<Dialog>( dialog ) );
	}
}
