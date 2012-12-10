/**
 * Copyright (C) 2011-2012 MK124
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

package net.gtaun.shoebill.object.impl;

import net.gtaun.shoebill.SampObjectStoreImpl;
import net.gtaun.shoebill.ShoebillImpl;
import net.gtaun.shoebill.data.Color;
import net.gtaun.shoebill.data.Location;
import net.gtaun.shoebill.data.Vector3D;
import net.gtaun.shoebill.event.destroyable.DestroyEvent;
import net.gtaun.shoebill.exception.CreationFailedException;
import net.gtaun.shoebill.object.Player;
import net.gtaun.shoebill.object.PlayerLabel;
import net.gtaun.shoebill.object.Vehicle;
import net.gtaun.shoebill.samp.SampNativeFunction;
import net.gtaun.util.event.EventManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 * 
 * @author MK124
 */
public abstract class PlayerLabelImpl implements PlayerLabel
{
	private int id = INVALID_ID;
	private Player player;
	private String text;
	private Color color;
	private float drawDistance;
	private Location location;
	private boolean testLOS;
	
	private Vector3D offset;
	private Player attachedPlayer;
	private Vehicle attachedVehicle;
	
	
	public PlayerLabelImpl(Player player, String text, Color color, Location loc, float drawDistance, boolean testLOS) throws CreationFailedException
	{
		initialize(player, text, color, new Location(loc), drawDistance, testLOS, null, null);
	}
	
	public PlayerLabelImpl(Player player, String text, Color color, Location loc, float drawDistance, boolean testLOS, Player attachedPlayer) throws CreationFailedException
	{
		initialize(player, text, color, new Location(loc), drawDistance, testLOS, attachedPlayer, null);
	}
	
	public PlayerLabelImpl(Player player, String text, Color color, Location loc, float drawDistance, boolean testLOS, Vehicle attachedVehicle) throws CreationFailedException
	{
		initialize(player, text, color, new Location(loc), drawDistance, testLOS, null, attachedVehicle);
	}
	
	private void initialize(Player player, String text, Color color, Location loc, float drawDistance, boolean testLOS, Player attachedPlayer, Vehicle attachedVehicle) throws CreationFailedException
	{
		if (StringUtils.isEmpty(text)) text = " ";
		
		this.player = player;
		this.text = text;
		this.color = new Color(color);
		this.drawDistance = drawDistance;
		this.location = new Location(loc);
		this.testLOS = testLOS;
		
		int playerId = Player.INVALID_ID, vehicleId = Vehicle.INVALID_ID;
		
		if (attachedPlayer != null) playerId = attachedPlayer.getId();
		if (attachedVehicle != null) vehicleId = attachedVehicle.getId();
		
		if (playerId == Player.INVALID_ID) attachedPlayer = null;
		if (vehicleId == Vehicle.INVALID_ID) attachedVehicle = null;
		
		if (attachedPlayer != null || attachedVehicle != null)
		{
			offset = new Vector3D(location.getX(), location.getY(), location.getZ());
		}
		
		if (player.isOnline() == false) throw new CreationFailedException();
		
		id = SampNativeFunction.createPlayer3DTextLabel(player.getId(), text, color.getValue(), location.getX(), location.getY(), location.getZ(), drawDistance, playerId, vehicleId, testLOS);
		if (id == INVALID_ID) throw new CreationFailedException();
	}
	
	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
	
	@Override
	public int getId()
	{
		return id;
	}
	
	@Override
	public Player getPlayer()
	{
		return player;
	}
	
	@Override
	public void destroy()
	{
		if (isDestroyed()) return;
		
		if (player.isOnline())
		{
			SampNativeFunction.deletePlayer3DTextLabel(player.getId(), id);
		}
		
		EventManager eventManager = ShoebillImpl.getInstance().getRootEventManager();
		DestroyEvent destroyEvent = new DestroyEvent(this);
		eventManager.dispatchEvent(destroyEvent, this);
		
		id = INVALID_ID;
	}
	
	@Override
	public boolean isDestroyed()
	{
		if (player.isOnline() == false) id = INVALID_ID;
		return id == INVALID_ID;
	}
	
	@Override
	public String getText()
	{
		return text;
	}
	
	@Override
	public Color getColor()
	{
		return color.clone();
	}
	
	@Override
	public float getDrawDistance()
	{
		return drawDistance;
	}
	
	@Override
	public Player getAttachedPlayer()
	{
		return attachedPlayer;
	}
	
	@Override
	public Vehicle getAttachedVehicle()
	{
		return attachedVehicle;
	}
	
	@Override
	public Location getLocation()
	{
		if (isDestroyed()) return null;
		
		Location pos = null;
		
		if (attachedPlayer != null) pos = attachedPlayer.getLocation();
		if (attachedVehicle != null) pos = attachedVehicle.getLocation();
		
		if (pos != null)
		{
			location.set(pos.getX() + offset.getX(), pos.getY(), pos.getZ() + offset.getZ(), pos.getInteriorId(), pos.getWorldId());
		}
		
		return location.clone();
	}
	
	@Override
	public void attach(Player target, float x, float y, float z)
	{
		if (isDestroyed()) return;
		if (target.isOnline() == false) return;
		
		int playerId = player.getId();
		
		SampObjectStoreImpl store = (SampObjectStoreImpl) ShoebillImpl.getInstance().getSampObjectStore();

		store.setPlayerLabel(player, id, null);
		SampNativeFunction.deletePlayer3DTextLabel(playerId, id);
		
		id = SampNativeFunction.createPlayer3DTextLabel(playerId, text, color.getValue(), x, y, z, drawDistance, target.getId(), Vehicle.INVALID_ID, testLOS);
		store.setPlayerLabel(player, id, this);
		
		attachedPlayer = target;
		attachedVehicle = null;
	}
	
	@Override
	public void attach(Player target, Vector3D offset)
	{
		attach(target, offset.getX(), offset.getY(), offset.getZ());
	}
	
	@Override
	public void attach(Vehicle vehicle, float x, float y, float z)
	{
		if (isDestroyed()) return;
		if (vehicle.isDestroyed()) return;
		
		int playerId = player.getId();
		
		SampObjectStoreImpl store = (SampObjectStoreImpl) ShoebillImpl.getInstance().getSampObjectStore();
		
		store.setPlayerLabel(player, id, null);
		SampNativeFunction.deletePlayer3DTextLabel(playerId, id);
		
		id = SampNativeFunction.createPlayer3DTextLabel(playerId, text, color.getValue(), x, y, z, drawDistance, Player.INVALID_ID, vehicle.getId(), testLOS);
		store.setPlayerLabel(player, id, this);
		
		attachedPlayer = null;
		attachedVehicle = vehicle;
	}
	
	@Override
	public void attach(Vehicle vehicle, Vector3D offset)
	{
		attach(vehicle, offset.getX(), offset.getY(), offset.getZ());
	}
	
	@Override
	public void update(Color color, String text)
	{
		if (isDestroyed()) return;
		
		this.color.set(color);
		this.text = text;
		
		SampNativeFunction.updatePlayer3DTextLabelText(player.getId(), id, color.getValue(), text);
	}
}
