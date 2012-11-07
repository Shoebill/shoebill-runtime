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

import net.gtaun.shoebill.ShoebillImpl;
import net.gtaun.shoebill.data.Location;
import net.gtaun.shoebill.data.Vector3D;
import net.gtaun.shoebill.events.ObjectEventHandler;
import net.gtaun.shoebill.events.object.PlayerObjectMovedEvent;
import net.gtaun.shoebill.exception.CreationFailedException;
import net.gtaun.shoebill.object.Player;
import net.gtaun.shoebill.object.PlayerObject;
import net.gtaun.shoebill.object.SampObject;
import net.gtaun.shoebill.object.Vehicle;
import net.gtaun.shoebill.proxy.ProxyManager;
import net.gtaun.shoebill.proxy.ProxyManagerImpl;
import net.gtaun.shoebill.samp.SampNativeFunction;
import net.gtaun.util.event.EventManager;
import net.gtaun.util.event.EventManager.Priority;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 * 
 * @author MK124
 */
public class PlayerObjectImpl implements PlayerObject
{
	private ProxyManager proxyManager;
	
	private int id = INVALID_ID;
	private Player player;
	
	private int modelId;
	private Location location;
	private float speed = 0.0F;
	private float drawDistance = 0;
	
	private Player attachedPlayer;
	
	private ObjectEventHandler eventHandler;
	
	
	public PlayerObjectImpl(Player player, int modelId, Location loc, Vector3D rot, float drawDistance) throws CreationFailedException
	{
		initialize(player, modelId, new Location(loc), new Vector3D(rot), drawDistance);
	}
	
	private void initialize(Player player, int modelId, Location loc, Vector3D rot, float drawDistance) throws CreationFailedException
	{
		if (player.isOnline() == false) throw new CreationFailedException();
		proxyManager = new ProxyManagerImpl();
		
		this.player = player;
		this.modelId = modelId;
		this.location = loc;
		this.drawDistance = drawDistance;
		
		id = SampNativeFunction.createPlayerObject(player.getId(), modelId, loc.getX(), loc.getY(), loc.getZ(), rot.getX(), rot.getY(), rot.getZ(), drawDistance);
		if (id == INVALID_ID) throw new CreationFailedException();
		
		eventHandler = new ObjectEventHandler()
		{
			@Override
			public void onPlayerObjectMoved(PlayerObjectMovedEvent event)
			{
				speed = 0.0F;
			}
		};
		
		EventManager eventManager = ShoebillImpl.getInstance().getEventManager();
		eventManager.addHandler(PlayerObjectMovedEvent.class, eventHandler, Priority.MONITOR);
	}
	
	@Override
	public ProxyManager getProxyManager()
	{
		return proxyManager;
	}
	
	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
	
	@Override
	public void destroy()
	{
		if (isDestroyed()) return;
		
		if (player.isOnline())
		{
			SampNativeFunction.destroyPlayerObject(player.getId(), id);
			
		}
		
		id = INVALID_ID;
	}
	
	@Override
	public boolean isDestroyed()
	{
		return id == INVALID_ID;
	}
	
	@Override
	public Player getPlayer()
	{
		return player;
	}
	
	@Override
	public int getId()
	{
		return id;
	}
	
	@Override
	public int getModelId()
	{
		return modelId;
	}
	
	@Override
	public float getSpeed()
	{
		if (isDestroyed()) return 0.0F;
		if (player.isOnline() == false) return 0.0F;
		
		if (attachedPlayer != null && attachedPlayer.isOnline()) return attachedPlayer.getVelocity().speed3d();
		
		return speed;
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
	public SampObject getAttachedObject()
	{
		return null;
	}
	
	@Override
	public Vehicle getAttachedVehicle()
	{
		return null;
	}
	
	@Override
	public Location getLocation()
	{
		if (isDestroyed()) return null;
		if (player.isOnline() == false) return null;
		
		SampNativeFunction.getPlayerObjectPos(player.getId(), id, location);
		return location.clone();
	}
	
	@Override
	public void setLocation(Vector3D pos)
	{
		if (isDestroyed()) return;
		if (player.isOnline() == false) return;
		
		location.set(pos);
		SampNativeFunction.setPlayerObjectPos(player.getId(), id, pos.getX(), pos.getY(), pos.getZ());
	}
	
	@Override
	public void setLocation(Location loc)
	{
		if (isDestroyed()) return;
		if (player.isOnline() == false) return;
		
		location.set(loc);
		SampNativeFunction.setPlayerObjectPos(player.getId(), id, loc.getX(), loc.getY(), loc.getZ());
	}
	
	@Override
	public Vector3D getRotate()
	{
		if (isDestroyed()) return null;
		if (player.isOnline() == false) return null;
		
		Vector3D rotate = new Vector3D();
		SampNativeFunction.getPlayerObjectRot(player.getId(), id, rotate);
		return rotate;
	}
	
	@Override
	public void setRotate(float rx, float ry, float rz)
	{
		if (isDestroyed()) return;
		if (player.isOnline() == false) return;
		
		SampNativeFunction.setPlayerObjectRot(player.getId(), id, rx, ry, rz);
	}
	
	@Override
	public void setRotate(Vector3D rot)
	{
		setRotate(rot.getX(), rot.getY(), rot.getZ());
	}
	
	@Override
	public boolean isMoving()
	{
		if (isDestroyed()) return false;
		if (player.isOnline() == false) return false;
		
		return SampNativeFunction.isPlayerObjectMoving(player.getId(), id);
	}
	
	@Override
	public int move(float x, float y, float z, float speed)
	{
		if (isDestroyed()) return 0;
		if (player.isOnline() == false) return 0;
		
		if (attachedPlayer == null) this.speed = speed;
		return SampNativeFunction.movePlayerObject(player.getId(), id, x, y, z, speed, -1000.0f, -1000.0f, -1000.0f);
	}
	
	@Override
	public int move(float x, float y, float z, float speed, float rotX, float rotY, float rotZ)
	{
		if (isDestroyed()) return 0;
		if (player.isOnline() == false) return 0;
		
		if (attachedPlayer == null) this.speed = speed;
		return SampNativeFunction.movePlayerObject(player.getId(), id, x, y, z, speed, rotX, rotY, rotZ);
	}
	
	@Override
	public int move(Vector3D pos, float speed)
	{
		return move(pos.getX(), pos.getY(), pos.getZ(), speed);
	}
	
	@Override
	public int move(Vector3D pos, float speed, Vector3D rot)
	{
		return move(pos.getX(), pos.getY(), pos.getZ(), speed, rot.getX(), rot.getY(), rot.getZ());
	}
	
	@Override
	public void stop()
	{
		if (isDestroyed()) return;
		if (player.isOnline() == false) return;
		
		speed = 0.0F;
		SampNativeFunction.stopPlayerObject(player.getId(), id);
	}
	
	@Override
	public void attach(Player target, float x, float y, float z, float rx, float ry, float rz)
	{
		if (isDestroyed()) return;
		if (player.isOnline() == false) return;
		if (target.isOnline() == false) return;
		
		SampNativeFunction.attachPlayerObjectToPlayer(player.getId(), id, target.getId(), x, y, z, rx, ry, rz);
		
		attachedPlayer = player;
		speed = 0.0F;
	}
	
	@Override
	public void attach(Player target, Vector3D pos, Vector3D rot)
	{
		attach(target, pos.getX(), pos.getY(), pos.getZ(), rot.getX(), rot.getY(), rot.getZ());
	}
	
	@Override
	public void attach(SampObject object, float x, float y, float z, float rx, float ry, float rz, boolean syncRotation)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void attach(SampObject object, Vector3D pos, Vector3D rot, boolean syncRotation)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void attach(Vehicle vehicle, float x, float y, float z, float rx, float ry, float rz)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void attach(Vehicle vehicle, Vector3D pos, Vector3D rot)
	{
		throw new UnsupportedOperationException();
	}
}
