package net.gtaun.shoebill;

import java.lang.ref.Reference;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.WeakHashMap;

import net.gtaun.lungfish.IObjectPool;
import net.gtaun.lungfish.object.IDialog;
import net.gtaun.lungfish.object.ILabel;
import net.gtaun.lungfish.object.IMenu;
import net.gtaun.lungfish.object.IObject;
import net.gtaun.lungfish.object.IPickup;
import net.gtaun.lungfish.object.IPlayer;
import net.gtaun.lungfish.object.IPlayerLabel;
import net.gtaun.lungfish.object.IPlayerObject;
import net.gtaun.lungfish.object.ITextdraw;
import net.gtaun.lungfish.object.ITimer;
import net.gtaun.lungfish.object.IVehicle;
import net.gtaun.lungfish.object.IZone;
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

public class ObjectPool implements IObjectPool
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
	
	
	public IPlayer getPlayer( int id )
	{
		return playerPool[id];
	}
	
	public IVehicle getVehicle( int id )
	{
		return vehiclePool[id];
	}
	
	public IObject getObject( int id )
	{
		return objectPool[id];
	}
	
	public IPlayerObject getPlayerObject( IPlayer player, int id )
	{
		return playerObjectPool.get( player ) [id];
	}
	
	public IPickup getPickup( int id )
	{
		return pickupPool[id];
	}
	
	public ILabel getLabel( int id )
	{
		return labelPool[id];
	}
	
	public IPlayerLabel getPlayerLabel( IPlayer player, int id )
	{
		return playerLabelPool.get( player ) [id];
	}
	
	public ITextdraw getTextdraw( int id )
	{
		return textdrawPool[id];
	}
	
	public IZone getZone( int id )
	{
		return zonePool[id];
	}
	
	public IMenu getMenu( int id )
	{
		return menuPool[id];
	}
	
	
	public Collection<IPlayer> getPlayers()
	{
		Collection<IPlayer> players = new Vector<IPlayer>();
		for( IPlayer player : playerPool ) players.add( player );
		
		return players;
	}
	
	public Collection<IVehicle> getVehicles()
	{
		Collection<IVehicle> vehicles = new Vector<IVehicle>();
		for( IVehicle vehicle : vehiclePool ) vehicles.add( vehicle );
		
		return vehicles;
	}
	
	public Collection<IObject> getObjects()
	{
		Collection<IObject> objects  = new Vector<IObject>();
		for( IObject object : objectPool ) objects.add( object );
		
		return objects;
	}
	
	public Collection<IPlayerObject> getPlayerObjects( IPlayer player )
	{
		Collection<IPlayerObject> objects  = new Vector<IPlayerObject>();
		for( IPlayerObject object : playerObjectPool.get(player) ) objects.add( object );
		
		return objects;
	}
	
	public Collection<IPickup> getPickups()
	{
		Collection<IPickup> pickups = new Vector<IPickup>();
		for( IPickup pickup : pickupPool ) pickups.add( pickup );
		
		return pickups;
	}
	
	public Collection<ILabel> getLabels()
	{
		Collection<ILabel> labels = new Vector<ILabel>();
		for( ILabel label : labelPool ) labels.add( label );
		
		return labels;
	}
	
	public Collection<IPlayerLabel> getpPlayerLabels( IPlayer player )
	{
		Collection<IPlayerLabel> objects  = new Vector<IPlayerLabel>();
		for( IPlayerLabel object : playerLabelPool.get(player) ) objects.add( object );
		
		return objects;
	}
	
	public Collection<ITextdraw> getTextdraws()
	{
		Collection<ITextdraw> textdraws = new Vector<ITextdraw>();
		for( ITextdraw textdraw : textdrawPool ) textdraws.add( textdraw );
		
		return textdraws;
	}
	
	public Collection<IZone> getZones()
	{
		Collection<IZone> zones = new Vector<IZone>();
		for( IZone zone : zonePool ) zones.add( zone );
		
		return zones;
	}
	
	public Collection<IMenu> getMenus()
	{
		List<IMenu> menus = new Vector<IMenu>();
		for( IMenu menu : menuPool ) menus.add( menu );
		
		return menus;
	}
	
	
	public Collection<ITimer> getTimers()
	{
		Collection<ITimer> timers = new Vector<ITimer>();
		for( Reference<Timer> reference : timerPool )
		{
			ITimer timer = reference.get();
			if( timer == null ) continue;
			
			timers.add( timer );
		}
		
		return timers;
	}
	
	public Collection<IDialog> getDialogs()
	{
		Collection<IDialog> dialogs = new Vector<IDialog>();
		for( Reference<Dialog> reference : dialogPool.values() )
		{
			IDialog dialog = reference.get();
			if( dialog == null ) continue;
			
			dialogs.add( dialog );
		}
		
		return dialogs;
	}
}
