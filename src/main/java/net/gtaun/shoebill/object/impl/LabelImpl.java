/**
 * Copyright (C) 2011-2014 MK124
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

import net.gtaun.shoebill.SampEventDispatcher;
import net.gtaun.shoebill.SampNativeFunction;
import net.gtaun.shoebill.SampObjectStoreImpl;
import net.gtaun.shoebill.ShoebillImpl;
import net.gtaun.shoebill.data.Color;
import net.gtaun.shoebill.data.Location;
import net.gtaun.shoebill.data.Vector3D;
import net.gtaun.shoebill.event.destroyable.DestroyEvent;
import net.gtaun.shoebill.exception.CreationFailedException;
import net.gtaun.shoebill.object.Label;
import net.gtaun.shoebill.object.Player;
import net.gtaun.shoebill.object.Vehicle;
import net.gtaun.util.event.EventManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 * 
 * @author MK124
 */
public class LabelImpl implements Label
{
	private final EventManager rootEventManager;

	private int id = INVALID_ID;
	private String text;
	private Color color;
	private Location location;
	private float drawDistance;
	
	private Vector3D offset;
	private Player attachedPlayer;
	private Vehicle attachedVehicle;

	public LabelImpl(EventManager eventManager, SampObjectStoreImpl store, String text, Color color, Location loc, float drawDistance, boolean testLOS)
	{
		this(eventManager, store, text, color, loc, drawDistance, testLOS, true, -1);
	}

	public LabelImpl(EventManager eventManager, SampObjectStoreImpl store, String text, Color color, Location loc, float drawDistance, boolean testLOS, boolean doInit, int id) throws CreationFailedException
	{
		if (StringUtils.isEmpty(text)) text = " ";
		
		this.rootEventManager = eventManager;

		this.text = text;
		this.color = new Color(color);
		this.location = new Location(loc);
		this.drawDistance = drawDistance;

		if(doInit || id < 0) {
			final String finalText = text;
			SampEventDispatcher.getInstance().executeWithoutEvent(() -> this.id = SampNativeFunction.create3DTextLabel(finalText, color.getValue(), location.getX(), location.getY(), location.getZ(), drawDistance, location.getWorldId(), testLOS));
		} else this.id = id;
		if (this.id == INVALID_ID) throw new CreationFailedException();
		store.setLabel(this.id, this);
	}
	
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("id", id).toString();
	}
	
	@Override
	public void destroy()
	{
		if (isDestroyed()) return;
		
		SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.delete3DTextLabel(id));
		destroyWithoutExec();
	}

	public void destroyWithoutExec()
	{
		if (isDestroyed()) return;

		DestroyEvent destroyEvent = new DestroyEvent(this);
		rootEventManager.dispatchEvent(destroyEvent, this);

		id = INVALID_ID;
	}

	@Override
	public boolean isDestroyed()
	{
		return id == INVALID_ID;
	}
	
	@Override
	public int getId()
	{
		return id;
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
		
		Location loc = null;
		
		if (attachedPlayer != null) loc = attachedPlayer.getLocation();
		if (attachedVehicle != null) loc = attachedVehicle.getLocation();
		
		if (loc != null)
		{
			location.set(loc.getX() + offset.getX(), loc.getY() + offset.getY(), loc.getZ() + offset.getZ(), loc.getInteriorId(), loc.getWorldId());
		}
		
		return location.clone();
	}
	
	@Override
	public void attach(Player player, float x, float y, float z)
	{
		SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.attach3DTextLabelToPlayer(id, player.getId(), x, y, z));
		attachWithoutExec(player, x, y, z);
	}
	
	@Override
	public void attach(Player player, Vector3D offset)
	{
		attach(player, offset.getX(), offset.getY(), offset.getZ());
	}
	
	@Override
	public void attach(Vehicle vehicle, float x, float y, float z)
	{
		SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.attach3DTextLabelToVehicle(id, vehicle.getId(), x, y, z));
		attachWithoutExec(vehicle, x, y, z);
	}
	
	@Override
	public void attach(Vehicle vehicle, Vector3D offset)
	{
		attach(vehicle, offset.getX(), offset.getY(), offset.getZ());
	}

	public void attachWithoutExec(Vehicle vehicle, float x, float y, float z)
	{
		if (isDestroyed()) return;
		if (vehicle.isDestroyed()) return;
		offset = new Vector3D(x, y, z);
		attachedPlayer = null;
		attachedVehicle = vehicle;
	}

	public void attachWithoutExec(Player player, float x, float y, float z)
	{
		if (isDestroyed()) return;
		if (player.isOnline() == false) return;
		offset = new Vector3D(x, y, z);
		attachedPlayer = player;
		attachedVehicle = null;
	}

	@Override
	public void update(Color color, String text)
	{
		updateWithoutExec(color, text);
		SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.update3DTextLabelText(id, color.getValue(), text));
	}

	public void updateWithoutExec(Color color, String text) {
		if (isDestroyed()) return;
		if (text == null) throw new NullPointerException();

		this.color.set(color);
		this.text = text;
	}
}
