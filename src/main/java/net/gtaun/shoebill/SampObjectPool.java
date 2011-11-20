package net.gtaun.shoebill;

import java.lang.ref.Reference;
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

public class SampObjectPool
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
	
	
	Player[] playerPool									= new Player[MAX_PLAYERS];
	Vehicle[] vehiclePool								= new Vehicle[MAX_VEHICLES];
	ObjectBase[] objectPool								= new ObjectBase[MAX_OBJECTS];
	Map<Player, PlayerObject[]> playerObjectPool		= new WeakHashMap<Player, PlayerObject[]>();
	Pickup[] pickupPool									= new Pickup[MAX_PICKUPS];
	Label[] labelPool									= new Label[MAX_LABELS_GLOBAL];
	Map<Player, PlayerLabel[]> playerLabelPool			= new WeakHashMap<Player, PlayerLabel[]>();
	Textdraw[] textdrawPool								= new Textdraw[MAX_TEXT_DRAWS];
	Zone[] zonePool										= new Zone[MAX_ZONES];
	Menu[] menuPool										= new Menu[MAX_MENUS];
	
	Vector<Reference<Timer>> timerPool					= new Vector<Reference<Timer>>();
	Map<Integer, Reference<Dialog>> dialogPool			= new HashMap<Integer, Reference<Dialog>>();
	
	
	public Player getPlayer( int id )
	{
		return playerPool[id];
	}
	
	public Vehicle getVehicle( int id )
	{
		return vehiclePool[id];
	}
	
	public Object getObject( int id )
	{
		return objectPool[id];
	}
	
	public PlayerObject getPlayerObject( Player player, int id )
	{
		return playerObjectPool.get( player ) [id];
	}
	
	public Pickup getPickup( int id )
	{
		return pickupPool[id];
	}
	
	public Label getLabel( int id )
	{
		return labelPool[id];
	}
	
	public PlayerLabel getPlayerLabel( Player player, int id )
	{
		return playerLabelPool.get( player ) [id];
	}
	
	public Textdraw getTextdraw( int id )
	{
		return textdrawPool[id];
	}
	
	public Zone getZone( int id )
	{
		return zonePool[id];
	}
	
	public Menu getMenu( int id )
	{
		return menuPool[id];
	}
	
	
	public Collection<Player> getPlayers()
	{
		Collection<Player> players = new Vector<Player>();
		for( Player player : playerPool ) players.add( player );
		
		return players;
	}
	
	public Collection<Vehicle> getVehicles()
	{
		Collection<Vehicle> vehicles = new Vector<Vehicle>();
		for( Vehicle vehicle : vehiclePool ) vehicles.add( vehicle );
		
		return vehicles;
	}
	
	public Collection<Object> getObjects()
	{
		Collection<Object> objects  = new Vector<Object>();
		for( Object object : objectPool ) objects.add( object );
		
		return objects;
	}
	
	public Collection<PlayerObject> getPlayerObjects( Player player )
	{
		Collection<PlayerObject> objects  = new Vector<PlayerObject>();
		for( PlayerObject object : playerObjectPool.get(player) ) objects.add( object );
		
		return objects;
	}
	
	public Collection<Pickup> getPickups()
	{
		Collection<Pickup> pickups = new Vector<Pickup>();
		for( Pickup pickup : pickupPool ) pickups.add( pickup );
		
		return pickups;
	}
	
	public Collection<Label> getLabels()
	{
		Collection<Label> labels = new Vector<Label>();
		for( Label label : labelPool ) labels.add( label );
		
		return labels;
	}
	
	public Collection<PlayerLabel> getpPlayerLabels( Player player )
	{
		Collection<PlayerLabel> objects  = new Vector<PlayerLabel>();
		for( PlayerLabel object : playerLabelPool.get(player) ) objects.add( object );
		
		return objects;
	}
	
	public Collection<Textdraw> getTextdraws()
	{
		Collection<Textdraw> textdraws = new Vector<Textdraw>();
		for( Textdraw textdraw : textdrawPool ) textdraws.add( textdraw );
		
		return textdraws;
	}
	
	public Collection<Zone> getZones()
	{
		Collection<Zone> zones = new Vector<Zone>();
		for( Zone zone : zonePool ) zones.add( zone );
		
		return zones;
	}
	
	public Collection<Menu> getMenus()
	{
		List<Menu> menus = new Vector<Menu>();
		for( Menu menu : menuPool ) menus.add( menu );
		
		return menus;
	}
	
	
	public Collection<Timer> getTimers()
	{
		Collection<Timer> timers = new Vector<Timer>();
		for( Reference<Timer> reference : timerPool )
		{
			Timer timer = reference.get();
			if( timer == null ) continue;
			
			timers.add( timer );
		}
		
		return timers;
	}
	
	public Collection<Dialog> getDialogs()
	{
		Collection<Dialog> dialogs = new Vector<Dialog>();
		for( Reference<Dialog> reference : dialogPool.values() )
		{
			Dialog dialog = reference.get();
			if( dialog == null ) continue;
			
			dialogs.add( dialog );
		}
		
		return dialogs;
	}
}
