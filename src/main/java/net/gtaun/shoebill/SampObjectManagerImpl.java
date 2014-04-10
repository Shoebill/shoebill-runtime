/**
 * Copyright (C) 2012-2014 MK124
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

import java.util.LinkedList;
import java.util.Queue;

import net.gtaun.shoebill.data.AngledLocation;
import net.gtaun.shoebill.data.Color;
import net.gtaun.shoebill.data.Location;
import net.gtaun.shoebill.data.Vector3D;
import net.gtaun.shoebill.event.destroyable.DestroyEvent;
import net.gtaun.shoebill.exception.CreationFailedException;
import net.gtaun.shoebill.object.Destroyable;
import net.gtaun.shoebill.object.DialogId;
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
import net.gtaun.shoebill.object.Timer.TimerCallback;
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
import net.gtaun.shoebill.object.impl.PlayerTextdrawImpl;
import net.gtaun.shoebill.object.impl.SampObjectImpl;
import net.gtaun.shoebill.object.impl.ServerImpl;
import net.gtaun.shoebill.object.impl.TextdrawImpl;
import net.gtaun.shoebill.object.impl.TimerImpl;
import net.gtaun.shoebill.object.impl.VehicleImpl;
import net.gtaun.shoebill.object.impl.WorldImpl;
import net.gtaun.shoebill.object.impl.ZoneImpl;
import net.gtaun.util.event.EventManager;
import net.gtaun.util.event.HandlerPriority;

/**
 * 
 * 
 * @author MK124
 */
public class SampObjectManagerImpl extends SampObjectStoreImpl implements SampObjectManager
{
	private static final int MAX_DIALOG_ID = 32767;
	
	
	private int allocatedDialogId = 0;
	private Queue<Integer> recycledDialogIds;
	
	
	public SampObjectManagerImpl(EventManager eventManagerNode)
	{
		super(eventManagerNode);
		initialize();
	}
	
	@Override
	protected void finalize() throws Throwable
	{
		super.finalize();
	}
	
	private void initialize()
	{
		recycledDialogIds = new LinkedList<>();
		
		eventManagerNode.registerHandler(DestroyEvent.class, HandlerPriority.BOTTOM, (DestroyEvent e) ->
		{
			Destroyable obj = e.getDestroyable();
			
			if(obj instanceof VehicleImpl)
			{
				Vehicle vehicle = (Vehicle) obj;
				super.setVehicle(vehicle.getId(), null);
			}
			else if(obj instanceof PlayerObject)
			{
				PlayerObject object = (PlayerObject) obj;
				super.setPlayerObject(object.getPlayer(), object.getId(), null);
			}
			else if(obj instanceof SampObject)
			{
				SampObject object = (SampObject) obj;
				super.setObject(object.getId(), null);
			}
			else if (obj instanceof Pickup)
			{
				Pickup pickup = (Pickup) obj;
				super.setPickup(pickup.getId(), null);
			}
			else if (obj instanceof PlayerLabel)
			{
				PlayerLabel label = (PlayerLabel) obj;
				super.setPlayerLabel(label.getPlayer(), label.getId(), null);
			}
			else if (obj instanceof Label)
			{
				Label label = (Label) obj;
				super.setLabel(label.getId(), null);
			}
			else if (obj instanceof Textdraw)
			{
				Textdraw textdraw = (Textdraw) obj;
				super.setTextdraw(textdraw.getId(), null);
			}
			else if (obj instanceof PlayerTextdraw)
			{
				PlayerTextdraw textdraw = (PlayerTextdraw) obj;
				super.setPlayerTextdraw(textdraw.getPlayer(), textdraw.getId(), null);
			}
			else if (obj instanceof Zone)
			{
				Zone zone = (Zone) obj;
				super.setZone(zone.getId(), null);
			}
			else if (obj instanceof Menu)
			{
				Menu menu = (Menu) obj;
				super.setMenu(menu.getId(), null);
			}
			else if (obj instanceof DialogId)
			{
				DialogId dialog = (DialogId) obj;
				super.removeDialog(dialog);
				int dialogId = dialog.getId();
				recycleDialogId(dialogId);
			}
			else if (obj instanceof Timer)
			{
				Timer timer = (Timer) obj;
				super.removeTimer(timer);
			}
		});
	}
	
	public World createWorld()
	{
		try
		{
			World world = new WorldImpl(this);
			super.setWorld(world);
			return world;
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		catch (Throwable e)
		{
			throw new CreationFailedException(e);
		}
	}
	
	public Server createServer()
	{
		try
		{
			Server server = new ServerImpl(this);
			super.setServer(server);
			return server;
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		catch (Throwable e)
		{
			throw new CreationFailedException(e);
		}
	}
	
	public Player createPlayer(int playerId) throws UnsupportedOperationException
	{
		try
		{
			Player player = new PlayerImpl(eventManagerNode, this, playerId);
			super.setPlayer(playerId, player);
			return player;
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		catch (Throwable e)
		{
			throw new CreationFailedException(e);
		}
	}
	
	@Override
	public Vehicle createVehicle(int modelId, AngledLocation loc, int color1, int color2, int respawnDelay) throws CreationFailedException
	{
		try
		{
			Vehicle vehicle = new VehicleImpl(eventManagerNode, this, modelId, loc, color1, color2, respawnDelay);
			super.setVehicle(vehicle.getId(), vehicle);
			return vehicle;
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		catch (Throwable e)
		{
			throw new CreationFailedException(e);
		}
	}
	
	@Override
	public SampObject createObject(int modelId, Location loc, Vector3D rot, float drawDistance) throws CreationFailedException
	{
		try
		{
			SampObject object = new SampObjectImpl(eventManagerNode, modelId, loc, rot, drawDistance);
			super.setObject(object.getId(), object);
			return object;
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		catch (Throwable e)
		{
			throw new CreationFailedException(e);
		}
	}
	
	@Override
	public PlayerObject createPlayerObject(Player player, int modelId, Location loc, Vector3D rot, float drawDistance) throws CreationFailedException
	{
		try
		{
			if (!player.isOnline()) throw new CreationFailedException();
			
			PlayerObject object = new PlayerObjectImpl(eventManagerNode, player, modelId, loc, rot, drawDistance);
			super.setPlayerObject(player, object.getId(), object);
			return object;
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		catch (Throwable e)
		{
			throw new CreationFailedException(e);
		}
	}
	
	@Override
	public Pickup createPickup(int modelId, int type, Location loc) throws CreationFailedException
	{
		try
		{
			Pickup pickup = new PickupImpl(eventManagerNode, modelId, type, loc);
			super.setPickup(pickup.getId(), pickup);
			return pickup;
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		catch (Throwable e)
		{
			throw new CreationFailedException(e);
		}
	}
	
	@Override
	public Label createLabel(String text, Color color, Location loc, float drawDistance, boolean testLOS) throws CreationFailedException
	{
		try
		{
			Label label = new LabelImpl(eventManagerNode, text, color, loc, drawDistance, testLOS);
			super.setLabel(label.getId(), label);
			return label;
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		catch (Throwable e)
		{
			throw new CreationFailedException(e);
		}
	}
	
	@Override
	public PlayerLabel createPlayerLabel(Player player, String text, Color color, Location loc, float drawDistance, boolean testLOS) throws CreationFailedException
	{
		try
		{
			if (!player.isOnline()) return null;
			
			PlayerLabel label = new PlayerLabelImpl(eventManagerNode, this, player, text, color, loc, drawDistance, testLOS);
			super.setLabel(label.getId(), label);
			return label;
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		catch (Throwable e)
		{
			throw new CreationFailedException(e);
		}
	}
	
	@Override
	public PlayerLabel createPlayerLabel(Player player, String text, Color color, Location loc, float drawDistance, boolean testLOS, Player attachedPlayer) throws CreationFailedException
	{
		try
		{
			if (!player.isOnline()) throw new CreationFailedException();
			
			PlayerLabel label = new PlayerLabelImpl(eventManagerNode, this, player, text, color, loc, drawDistance, testLOS, attachedPlayer);
			super.setLabel(label.getId(), label);
			return label;
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		catch (Throwable e)
		{
			throw new CreationFailedException(e);
		}
	}
	
	@Override
	public PlayerLabel createPlayerLabel(Player player, String text, Color color, Location loc, float drawDistance, boolean testLOS, Vehicle attachedVehicle) throws CreationFailedException
	{
		try
		{
			if (!player.isOnline()) throw new CreationFailedException();
			
			PlayerLabel label = new PlayerLabelImpl(eventManagerNode, this, player, text, color, loc, drawDistance, testLOS, attachedVehicle);
			super.setLabel(label.getId(), label);
			return label;
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		catch (Throwable e)
		{
			throw new CreationFailedException(e);
		}
	}
	
	@Override
	public Textdraw createTextdraw(float x, float y, String text) throws CreationFailedException
	{
		try
		{
			Textdraw textdraw = new TextdrawImpl(eventManagerNode, x, y, text);
			super.setTextdraw(textdraw.getId(), textdraw);
			return textdraw;
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public PlayerTextdraw createPlayerTextdraw(Player player, float x, float y, String text) throws CreationFailedException
	{
		try
		{
			if (!player.isOnline()) throw new CreationFailedException();
			
			PlayerTextdraw textdraw = new PlayerTextdrawImpl(eventManagerNode, player, x, y, text);
			super.setPlayerTextdraw(player, textdraw.getId(), textdraw);
			return textdraw;
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		catch (Throwable e)
		{
			throw new CreationFailedException(e);
		}
	}
	
	@Override
	public Zone createZone(float minX, float minY, float maxX, float maxY) throws CreationFailedException
	{
		try
		{
			Zone zone = new ZoneImpl(eventManagerNode, minX, minY, maxX, maxY);
			super.setZone(zone.getId(), zone);
			return zone;
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		catch (Throwable e)
		{
			throw new CreationFailedException(e);
		}
	}
	
	@Override
	public Menu createMenu(String title, int columns, float x, float y, float col1Width, float col2Width) throws CreationFailedException
	{
		try
		{
			Menu menu = new MenuImpl(eventManagerNode, title, columns, x, y, col1Width, col2Width);
			super.setMenu(menu.getId(), menu);
			return menu;
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		catch (Throwable e)
		{
			throw new CreationFailedException(e);
		}
	}
	
	private int allocateDialogId()
	{
		Integer dialogId = recycledDialogIds.poll();
		if (dialogId == null)
		{
			if (allocatedDialogId > MAX_DIALOG_ID) throw new CreationFailedException();
			dialogId = allocatedDialogId;
			allocatedDialogId++;
		}
		
		return dialogId;
	}
	
	private void recycleDialogId(int dialogId)
	{
		recycledDialogIds.offer(dialogId);
	}
	
	@Override
	public DialogId createDialogId() throws CreationFailedException
	{
		try
		{
			Integer dialogId = allocateDialogId();
			DialogId dialog = new DialogImpl(eventManagerNode, dialogId);
			super.putDialog(dialog.getId(), dialog);
			return dialog;
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		catch (Throwable e)
		{
			throw new CreationFailedException(e);
		}
	}
	
	@Override
	public Timer createTimer(int interval, int count, TimerCallback callback)
	{
		try
		{
			TimerImpl timer = new TimerImpl(eventManagerNode, interval, count, callback);
			super.putTimer(timer);
			return timer;
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		catch (Throwable e)
		{
			throw new CreationFailedException(e);
		}
	}
}
