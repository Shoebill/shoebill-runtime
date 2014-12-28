/**
 * Copyright (C) 2011-2014 MK124
 * Copyright (C) 2011 JoJLlmAn
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
import net.gtaun.shoebill.constant.ObjectMaterialSize;
import net.gtaun.shoebill.constant.ObjectMaterialTextAlign;
import net.gtaun.shoebill.data.Color;
import net.gtaun.shoebill.data.Location;
import net.gtaun.shoebill.data.Vector3D;
import net.gtaun.shoebill.event.destroyable.DestroyEvent;
import net.gtaun.shoebill.exception.CreationFailedException;
import net.gtaun.shoebill.object.Player;
import net.gtaun.shoebill.object.SampObject;
import net.gtaun.shoebill.object.Vehicle;
import net.gtaun.util.event.EventManager;
import net.gtaun.util.event.EventManagerNode;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 * 
 * @author MK124, JoJLlmAn & 123marvin123
 */
public class SampObjectImpl implements SampObject
{
	private int id = INVALID_ID;
	private int modelId;
	private Location location;
	private float speed = 0.0F;
	private float drawDistance = 0.0F;
	
	@SuppressWarnings("unused")
	private Vector3D attachedOffset;
	@SuppressWarnings("unused")
	private Vector3D attachedRotate;
	
	private Player attachedPlayer;
	private SampObject attachedObject;
	private Vehicle attachedVehicle;
	
	private EventManagerNode eventManagerNode;
	
	public SampObjectImpl(EventManager eventManager, SampObjectStoreImpl store, int modelId, Location loc, Vector3D rot, float drawDistance) {
		this(eventManager, store, modelId, loc, rot, drawDistance, true, -1);
	}

	public SampObjectImpl(EventManager eventManager, SampObjectStoreImpl store, int modelId, Location loc, Vector3D rot, float drawDistance, boolean doInit, int id) throws CreationFailedException
	{
		this.modelId = modelId;
		this.location = new Location(loc);
		this.drawDistance = drawDistance;

		if(doInit || id < 0) {
			SampEventDispatcher.getInstance().executeWithoutEvent(() -> this.id = SampNativeFunction.createObject(modelId, loc.getX(), loc.getY(), loc.getZ(), rot.getX(), rot.getY(), rot.getZ(), drawDistance));
			System.out.println("Created new obj with id " + this.id);
		} else {
			this.id = id;
			System.out.println("Created obj with existing id: " + this.id);
		}
		if (this.id == INVALID_ID) throw new CreationFailedException();
		store.setObject(this.id, this);
		eventManagerNode = eventManager.createChildNode();
	}

	public void onObjectMoved()
	{
		speed = 0.0F;
		SampNativeFunction.getObjectPos(id, location);
	}
	
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
			.append("id", id).append("modelId", modelId).toString();
	}
	
	@Override
	public void destroy()
	{
		if (isDestroyed()) return;
		SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.destroyObject(id));
		destroyWithoutExec();
	}

	public void destroyWithoutExec()
	{
		if (isDestroyed()) return;

		DestroyEvent destroyEvent = new DestroyEvent(this);
		eventManagerNode.dispatchEvent(destroyEvent, this);

		eventManagerNode.destroy();
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
	public int getModelId()
	{
		return modelId;
	}
	
	@Override
	public float getSpeed()
	{
		if (isDestroyed()) return 0.0F;
		
		if (attachedPlayer != null && attachedPlayer.isOnline()) return attachedPlayer.getVelocity().speed3d();
		if (attachedObject != null && attachedObject.isDestroyed() == false) return attachedObject.getSpeed();
		if (attachedVehicle != null && attachedVehicle.isDestroyed() == false) return attachedVehicle.getVelocity().speed3d();
		
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
		return attachedObject;
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
		
		if (attachedPlayer != null) location.set(attachedPlayer.getLocation());
		else if (attachedVehicle != null) location.set(attachedVehicle.getLocation());
		else SampNativeFunction.getObjectPos(id, location);
		
		return location.clone();
	}
	
	@Override
	public void setLocation(Vector3D pos)
	{
		if (isDestroyed()) return;
		
		speed = 0.0F;
		location.set(pos);
		SampNativeFunction.setObjectPos(id, pos.getX(), pos.getY(), pos.getZ());
	}
	
	@Override
	public void setLocation(Location loc)
	{
		if (isDestroyed()) return;
		
		speed = 0.0F;
		location.set(loc);
		SampNativeFunction.setObjectPos(id, loc.getX(), loc.getY(), loc.getZ());
	}
	
	@Override
	public Vector3D getRotation()
	{
		if (isDestroyed()) return null;
		
		Vector3D rotate = new Vector3D();
		SampNativeFunction.getObjectRot(id, rotate);
		
		return rotate;
	}
	
	@Override
	public void setRotation(float rx, float ry, float rz)
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.setObjectRot(id, rx, ry, rz);
	}
	
	@Override
	public void setRotation(Vector3D rot)
	{
		setRotation(rot.getX(), rot.getY(), rot.getZ());
	}
	
	@Override
	public boolean isMoving()
	{
		if (isDestroyed()) return false;
		return SampNativeFunction.isObjectMoving(id);
	}
	
	@Override
	public int move(float x, float y, float z, float speed)
	{
		if (isDestroyed()) return 0;
		
		if (attachedPlayer == null && attachedVehicle == null) this.speed = speed;
		return SampNativeFunction.moveObject(id, x, y, z, speed, -1000.0f, -1000.0f, -1000.0f);
	}
	
	@Override
	public int move(float x, float y, float z, float speed, float rotX, float rotY, float rotZ)
	{
		if (isDestroyed()) return 0;
		
		if (attachedPlayer == null && attachedVehicle == null) this.speed = speed;
		return SampNativeFunction.moveObject(id, x, y, z, speed, rotX, rotY, rotZ);
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
		
		speed = 0.0F;
		SampNativeFunction.stopObject(id);
	}
	
	@Override
	public void attach(Player player, float x, float y, float z, float rx, float ry, float rz)
	{
		if (isDestroyed()) return;
		if (player.isOnline() == false) return;
		
		SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.attachObjectToPlayer(id, player.getId(), x, y, z, rx, ry, rz));
		
		attachWithoutExecution(player, new Vector3D(x, y, z), new Vector3D(rx, ry, rz));
	}
	
	@Override
	public void attach(Player player, Vector3D pos, Vector3D rot)
	{
		attach(player, pos.getX(), pos.getY(), pos.getZ(), rot.getX(), rot.getY(), rot.getZ());
	}

	@Override
	public void attachCamera(Player player) {
		if (isDestroyed()) return;
		SampNativeFunction.attachCameraToObject(id, player.getId());
	}

	@Override
	public void attach(SampObject object, float x, float y, float z, float rx, float ry, float rz, boolean syncRotation)
	{
		if (isDestroyed()) return;
		if (object.isDestroyed()) return;
		
		if (object instanceof PlayerObjectImpl) throw new UnsupportedOperationException();
		SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.attachObjectToObject(id, object.getId(), x, y, z, rz, ry, rz, syncRotation ? 1 : 0));
		
		attachWithoutExecution(object, new Vector3D(x, y, z), new Vector3D(rx, ry, rz));
	}
	
	@Override
	public void attach(SampObject object, Vector3D pos, Vector3D rot, boolean syncRotation)
	{
		attach(object, pos.getX(), pos.getY(), pos.getZ(), rot.getX(), rot.getY(), rot.getZ(), syncRotation);
	}
	
	@Override
	public void attach(Vehicle vehicle, float x, float y, float z, float rx, float ry, float rz)
	{
		if (isDestroyed()) return;
		if (vehicle.isDestroyed()) return;
		
		SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.attachObjectToVehicle(id, vehicle.getId(), x, y, z, rx, ry, rz));
		
		attachWithoutExecution(vehicle, new Vector3D(x, y, z), new Vector3D(rx, ry, rz));
	}
	
	@Override
	public void attach(Vehicle vehicle, Vector3D pos, Vector3D rot)
	{
		attach(vehicle, pos.getX(), pos.getY(), pos.getZ(), rot.getX(), rot.getY(), rot.getZ());
	}

	public void attachWithoutExecution(Vehicle vehicle, Vector3D pos, Vector3D rot)
	{
		if (isDestroyed()) return;
		if (vehicle.isDestroyed()) return;

		speed = 0.0F;
		attachedOffset = new Vector3D(pos.x, pos.y, pos.z);
		attachedRotate = new Vector3D(rot.x, rot.y, rot.z);

		attachedPlayer = null;
		attachedObject = null;
		attachedVehicle = vehicle;
	}

	public void attachWithoutExecution(Player player, Vector3D pos, Vector3D rot)
	{
		if (isDestroyed()) return;
		if (!player.isOnline()) return;

		speed = 0.0F;
		attachedOffset = new Vector3D(pos.x, pos.y, pos.z);
		attachedRotate = new Vector3D(rot.x, rot.y, rot.z);

		attachedPlayer = player;
		attachedObject = null;
		attachedVehicle = null;
	}

	public void attachWithoutExecution(SampObject object, Vector3D pos, Vector3D rot)
	{
		if (isDestroyed()) return;
		if (object.isDestroyed()) return;

		speed = 0.0F;
		attachedOffset = new Vector3D(pos.x, pos.y, pos.z);
		attachedRotate = new Vector3D(rot.x, rot.y, rot.z);

		attachedPlayer = null;
		attachedObject = object;
		attachedVehicle = null;
	}
	
	@Override
	public void setMaterial(int materialIndex, int modelId, String txdName, String textureName, Color materialColor)
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.setObjectMaterial(id, materialIndex, modelId, txdName, textureName, materialColor.toArgbValue());
	}
	
	@Override
	public void setMaterial(int materialIndex, int modelId, String txdName, String textureName)
	{
		setMaterial(materialIndex, modelId, txdName, textureName, Color.TRANSPARENT);
	}
	
	@Override
	public void setMaterialText(String text, int materialIndex, ObjectMaterialSize materialSize, String fontFace, int fontSize, boolean isBold, Color fontColor, Color backColor, ObjectMaterialTextAlign textAlignment)
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.setObjectMaterialText(id, text, materialIndex, materialSize.getValue(), fontFace, fontSize, isBold ? 1 : 0, fontColor.toArgbValue(), backColor.toArgbValue(), textAlignment.getValue());
	}
	
	@Override
	public void setMaterialText(String text)
	{
		setMaterialText(text, 0, ObjectMaterialSize.SIZE_256x128, "Arial", 24, true, Color.WHITE, Color.TRANSPARENT, ObjectMaterialTextAlign.LEFT);
	}
}
