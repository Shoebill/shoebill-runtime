/**
 * Copyright (C) 2012 MK124
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

import net.gtaun.shoebill.data.Color;
import net.gtaun.shoebill.data.Location;
import net.gtaun.shoebill.data.LocationAngle;
import net.gtaun.shoebill.data.Vector3D;
import net.gtaun.shoebill.event.DestroyEventHandler;
import net.gtaun.shoebill.event.destroyable.DestroyEvent;
import net.gtaun.shoebill.exception.CreationFailedException;
import net.gtaun.shoebill.object.Destroyable;
import net.gtaun.shoebill.object.Dialog;
import net.gtaun.shoebill.object.Label;
import net.gtaun.shoebill.object.Menu;
import net.gtaun.shoebill.object.Pickup;
import net.gtaun.shoebill.object.Player;
import net.gtaun.shoebill.object.PlayerLabel;
import net.gtaun.shoebill.object.PlayerObject;
import net.gtaun.shoebill.object.PlayerTextdraw;
import net.gtaun.shoebill.object.SampObject;
import net.gtaun.shoebill.object.Server;
import net.gtaun.shoebill.object.Textdraw;
import net.gtaun.shoebill.object.Timer;
import net.gtaun.shoebill.object.Vehicle;
import net.gtaun.shoebill.object.World;
import net.gtaun.shoebill.object.Zone;
import net.gtaun.shoebill.object.impl.DialogImpl;
import net.gtaun.shoebill.object.impl.LabelImpl;
import net.gtaun.shoebill.object.impl.MenuImpl;
import net.gtaun.shoebill.object.impl.PickupImpl;
import net.gtaun.shoebill.object.impl.PlayerImpl;
import net.gtaun.shoebill.object.impl.PlayerLabelImpl;
import net.gtaun.shoebill.object.impl.PlayerObjectImpl;
import net.gtaun.shoebill.object.impl.SampObjectImpl;
import net.gtaun.shoebill.object.impl.ServerImpl;
import net.gtaun.shoebill.object.impl.TextdrawImpl;
import net.gtaun.shoebill.object.impl.TimerImpl;
import net.gtaun.shoebill.object.impl.VehicleImpl;
import net.gtaun.shoebill.object.impl.WorldImpl;
import net.gtaun.shoebill.object.impl.ZoneImpl;
import net.gtaun.shoebill.proxy.ProxyableFactory;
import net.gtaun.util.event.EventHandler;
import net.gtaun.util.event.EventManager;
import net.gtaun.util.event.EventManager.HandlerEntry;
import net.gtaun.util.event.EventManager.HandlerPriority;

/**
 * 
 * 
 * @author MK124
 */
public class SampObjectManager extends AbstractSampObjectFactory
{
	private static final Class<?>[] PLAYER_CONSTRUCTOR_ARGUMENT_TYPES = { int.class };
	private static final Class<?>[] VEHICLE_CONSTRUCTOR_ARGUMENT_TYPES = { int.class, LocationAngle.class, int.class, int.class, int.class };
	private static final Class<?>[] OBJECT_CONSTRUCTOR_ARGUMENT_TYPES = { int.class, Location.class, Vector3D.class, float.class };
	private static final Class<?>[] PLAYER_OBJECT_CONSTRUCTOR_ARGUMENT_TYPES = { Player.class, int.class, Location.class, Vector3D.class, float.class };
	private static final Class<?>[] PLAYER_OBJECT_CONSTRUCTOR_ATTACHED_PLAYER_ARGUMENT_TYPES = { Player.class, int.class, Location.class, Vector3D.class, float.class, Player.class };
	private static final Class<?>[] PLAYER_OBJECT_CONSTRUCTOR_ATTACHED_VEHICLE_ARGUMENT_TYPES = { Player.class, int.class, Location.class, Vector3D.class, float.class, Vehicle.class };
	private static final Class<?>[] PICKUP_CONSTRUCTOR_ARGUMENT_TYPES = { int.class, int.class, Location.class };
	private static final Class<?>[] LABEL_CONSTRUCTOR_ARGUMENT_TYPES = { String.class, Color.class, Location.class, float.class, boolean.class };
	private static final Class<?>[] PLAYER_LABEL_CONSTRUCTOR_ARGUMENT_TYPES = { Player.class, String.class, Color.class, Location.class, float.class, boolean.class };
	private static final Class<?>[] TEXTDRAW_CONSTRUCTOR_ARGUMENT_TYPES = { float.class, float.class, String.class };
	private static final Class<?>[] PLAYER_TEXTDRAW_CONSTRUCTOR_ARGUMENT_TYPES = { Player.class, float.class, float.class, String.class };
	private static final Class<?>[] ZONE_CONSTRUCTOR_ARGUMENT_TYPES = { float.class, float.class, float.class, float.class };
	private static final Class<?>[] MENU_CONSTRUCTOR_ARGUMENT_TYPES = { String.class, int.class, float.class, float.class, float.class, float.class };
	private static final Class<?>[] TIMER_CONSTRUCTOR_ARGUMENT_TYPES = { int.class, int.class };
	
	
	private final EventManager eventManager;
	private final SampObjectStoreImpl store;
	
	private ProxyableFactory<WorldImpl> worldFactory;
	private ProxyableFactory<ServerImpl> serverFactory;
	private ProxyableFactory<PlayerImpl> playerFactory;
	private ProxyableFactory<VehicleImpl> vehicleFactory;
	private ProxyableFactory<SampObjectImpl> objectFactory;
	private ProxyableFactory<PlayerObjectImpl> playerObjectFactory;
	private ProxyableFactory<PickupImpl> pickupFactory;
	private ProxyableFactory<LabelImpl> labelFactory;
	private ProxyableFactory<PlayerLabelImpl> playerLabelFactory;
	private ProxyableFactory<TextdrawImpl> textdrawFactory;
	private ProxyableFactory<PlayerTextdraw> playerTextdrawFactory;
	private ProxyableFactory<ZoneImpl> zoneFactory;
	private ProxyableFactory<MenuImpl> menuFactory;
	private ProxyableFactory<DialogImpl> dialogFactory;
	private ProxyableFactory<TimerImpl> timerFactory;
	
	private HandlerEntry destroyEventHandlerEntry;
	
	
	public SampObjectManager(EventManager eventManager, SampObjectStoreImpl store)
	{
		this.eventManager = eventManager;
		this.store = store;
		initialize();
	}
	
	@Override
	protected void finalize() throws Throwable
	{
		super.finalize();
		destroyEventHandlerEntry.cancel();
	}
	
	private void initialize()
	{
		worldFactory = ProxyableFactory.Impl.createProxyableFactory(WorldImpl.class);
		serverFactory = ProxyableFactory.Impl.createProxyableFactory(ServerImpl.class);
		playerFactory = ProxyableFactory.Impl.createProxyableFactory(PlayerImpl.class);
		vehicleFactory = ProxyableFactory.Impl.createProxyableFactory(VehicleImpl.class);
		objectFactory = ProxyableFactory.Impl.createProxyableFactory(SampObjectImpl.class);
		playerObjectFactory = ProxyableFactory.Impl.createProxyableFactory(PlayerObjectImpl.class);
		pickupFactory = ProxyableFactory.Impl.createProxyableFactory(PickupImpl.class);
		labelFactory = ProxyableFactory.Impl.createProxyableFactory(LabelImpl.class);
		playerLabelFactory = ProxyableFactory.Impl.createProxyableFactory(PlayerLabelImpl.class);
		textdrawFactory = ProxyableFactory.Impl.createProxyableFactory(TextdrawImpl.class);
		playerTextdrawFactory = ProxyableFactory.Impl.createProxyableFactory(PlayerTextdraw.class);
		zoneFactory = ProxyableFactory.Impl.createProxyableFactory(ZoneImpl.class);
		menuFactory = ProxyableFactory.Impl.createProxyableFactory(MenuImpl.class);
		dialogFactory = ProxyableFactory.Impl.createProxyableFactory(DialogImpl.class);
		timerFactory = ProxyableFactory.Impl.createProxyableFactory(TimerImpl.class);
		
		EventHandler eventHandler = new DestroyEventHandler()
		{
			@Override
			public void onDestroy(DestroyEvent event)
			{
				Destroyable obj = event.getDestroyable();
				
				if(obj instanceof VehicleImpl)
				{
					Vehicle vehicle = (Vehicle) obj;
					store.setVehicle(vehicle.getId(), null);
				}
				else if(obj instanceof PlayerObject)
				{
					PlayerObject object = (PlayerObject) obj;
					store.setPlayerObject(object.getPlayer(), object.getId(), null);
				}
				else if(obj instanceof SampObject)
				{
					SampObject object = (SampObject) obj;
					store.setObject(object.getId(), null);
				}
				else if (obj instanceof Pickup)
				{
					Pickup pickup = (Pickup) obj;
					store.setPickup(pickup.getId(), null);
				}
				else if (obj instanceof PlayerLabel)
				{
					PlayerLabel label = (PlayerLabel) obj;
					store.setPlayerLabel(label.getPlayer(), label.getId(), null);
				}
				else if (obj instanceof Label)
				{
					Label label = (Label) obj;
					store.setLabel(label.getId(), null);
				}
				else if (obj instanceof Textdraw)
				{
					Textdraw textdraw = (Textdraw) obj;
					store.setTextdraw(textdraw.getId(), null);
				}
				else if (obj instanceof PlayerTextdraw)
				{
					PlayerTextdraw textdraw = (PlayerTextdraw) obj;
					store.setPlayerTextdraw(textdraw.getPlayer(), textdraw.getId(), null);
				}
				else if (obj instanceof Zone)
				{
					Zone zone = (Zone) obj;
					store.setZone(zone.getId(), null);
				}
				else if (obj instanceof Menu)
				{
					Menu menu = (Menu) obj;
					store.setMenu(menu.getId(), null);
				}
			}
		};
		destroyEventHandlerEntry = eventManager.addHandler(DestroyEvent.class, eventHandler, HandlerPriority.BOTTOM);
	}
	
	public World createWorld()
	{
		World world = (World) worldFactory.create();
		store.setWorld(world);
		return world;
	}
	
	public Server createServer()
	{
		Server server = (Server) serverFactory.create();
		store.setServer(server);
		return server;
	}
	
	public Player createPlayer(int playerId)
	{
		final Object[] args = { playerId };
		Player player = (Player) playerFactory.create(PLAYER_CONSTRUCTOR_ARGUMENT_TYPES, args);
		store.setPlayer(playerId, player);
		return player;
	}
	
	@Override
	public Vehicle createVehicle(int modelId, LocationAngle loc, int color1, int color2, int respawnDelay) throws CreationFailedException
	{
		final Object[] args = { modelId, loc, color1, color2, respawnDelay };
		Vehicle vehicle = (Vehicle) vehicleFactory.create(VEHICLE_CONSTRUCTOR_ARGUMENT_TYPES, args);
		store.setVehicle(vehicle.getId(), vehicle);
		return vehicle;
	}
	
	@Override
	public SampObject createObject(int modelId, Location loc, Vector3D rot, float drawDistance) throws CreationFailedException
	{
		final Object[] args = { modelId, loc, rot, drawDistance };
		SampObject object = (SampObject) objectFactory.create(OBJECT_CONSTRUCTOR_ARGUMENT_TYPES, args);
		store.setObject(object.getId(), object);
		return object;
	}
	
	@Override
	public PlayerObject createPlayerObject(Player player, int modelId, Location loc, Vector3D rot, float drawDistance) throws CreationFailedException
	{
		final Object[] args = { player, modelId, loc, rot, drawDistance };
		PlayerObject object = (PlayerObject) playerObjectFactory.create(PLAYER_OBJECT_CONSTRUCTOR_ARGUMENT_TYPES, args);
		store.setPlayerObject(player, object.getId(), object);
		return object;
	}
	
	@Override
	public Pickup createPickup(int modelId, int type, Location loc) throws CreationFailedException
	{
		final Object[] args = { modelId, type, loc };
		Pickup pickup = (Pickup) pickupFactory.create(PICKUP_CONSTRUCTOR_ARGUMENT_TYPES, args);
		store.setPickup(pickup.getId(), pickup);
		return pickup;
	}
	
	@Override
	public Label createLabel(String text, Color color, Location loc, float drawDistance, boolean testLOS) throws CreationFailedException
	{
		final Object[] args = { text, color, loc, drawDistance, testLOS };
		Label label = (Label) labelFactory.create(LABEL_CONSTRUCTOR_ARGUMENT_TYPES, args);
		store.setLabel(label.getId(), label);
		return label;
	}
	
	@Override
	public PlayerLabel createPlayerLabel(Player player, String text, Color color, Location loc, float drawDistance, boolean testLOS)
	{
		final Object[] args = { player, text, color, loc, drawDistance, testLOS };
		PlayerLabel label = (PlayerLabel) playerLabelFactory.create(PLAYER_LABEL_CONSTRUCTOR_ARGUMENT_TYPES, args);
		store.setLabel(label.getId(), label);
		return label;
	}
	
	@Override
	public PlayerLabel createPlayerLabel(Player player, String text, Color color, Location loc, float drawDistance, boolean testLOS, Player attachedPlayer)
	{
		final Object[] args = { player, text, color, loc, drawDistance, testLOS, attachedPlayer };
		PlayerLabel label = (PlayerLabel) playerLabelFactory.create(PLAYER_OBJECT_CONSTRUCTOR_ATTACHED_PLAYER_ARGUMENT_TYPES, args);
		store.setLabel(label.getId(), label);
		return label;
	}
	
	@Override
	public PlayerLabel createPlayerLabel(Player player, String text, Color color, Location loc, float drawDistance, boolean testLOS, Vehicle attachedVehicle)
	{
		final Object[] args = { player, text, color, loc, drawDistance, testLOS, attachedVehicle };
		PlayerLabel label = (PlayerLabel) playerLabelFactory.create(PLAYER_OBJECT_CONSTRUCTOR_ATTACHED_VEHICLE_ARGUMENT_TYPES, args);
		store.setLabel(label.getId(), label);
		return label;
	}
	
	@Override
	public Textdraw createTextdraw(float x, float y, String text)
	{
		final Object[] args = { x, y, text };
		Textdraw textdraw = (Textdraw) textdrawFactory.create(TEXTDRAW_CONSTRUCTOR_ARGUMENT_TYPES, args);
		store.setTextdraw(textdraw.getId(), textdraw);
		return textdraw;
	}
	
	@Override
	public PlayerTextdraw createPlayerTextdraw(Player player, float x, float y, String text)
	{
		final Object[] args = { player, x, y, text };
		PlayerTextdraw textdraw = (PlayerTextdraw) playerTextdrawFactory.create(PLAYER_TEXTDRAW_CONSTRUCTOR_ARGUMENT_TYPES, args);
		store.setPlayerTextdraw(player, textdraw.getId(), textdraw);
		return textdraw;
	}
	
	@Override
	public Zone createZone(float minX, float minY, float maxX, float maxY)
	{
		final Object[] args = { minX, minY, maxX, maxY };
		Zone zone = (Zone) zoneFactory.create(ZONE_CONSTRUCTOR_ARGUMENT_TYPES, args);
		store.setZone(zone.getId(), zone);
		return zone;
	}
	
	@Override
	public Menu createMenu(String title, int columns, float x, float y, float col1Width, float col2Width)
	{
		final Object[] args = { title, columns, x, y, col1Width, col2Width };
		Menu menu = (Menu) menuFactory.create(MENU_CONSTRUCTOR_ARGUMENT_TYPES, args);
		store.setMenu(menu.getId(), menu);
		return menu;
	}
	
	@Override
	public Dialog createDialog()
	{
		Dialog dialog = (Dialog) dialogFactory.create();
		store.putDialog(dialog.getId(), dialog);
		return dialog;
	}
	
	@Override
	public Timer createTimer(int interval, int count)
	{
		final Object[] args = { interval, count };
		Timer timer = (Timer) timerFactory.create(TIMER_CONSTRUCTOR_ARGUMENT_TYPES, args);
		store.putTimer(timer);
		return timer;
	}
}
