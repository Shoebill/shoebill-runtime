/**
 * Copyright (C) 2011 MK124
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

package net.gtaun.shoebill.object;

import java.util.Collection;

import net.gtaun.shoebill.SampObjectPool;
import net.gtaun.shoebill.Shoebill;
import net.gtaun.shoebill.data.Location;
import net.gtaun.shoebill.data.Vector3D;
import net.gtaun.shoebill.exception.CreationFailedException;
import net.gtaun.shoebill.samp.SampNativeFunction;

/**
 * @author MK124, JoJLlmAn
 *
 */

public class ObjectBase implements IObject
{	
	public static Collection<IObject> get()
	{
		return Shoebill.getInstance().getManagedObjectPool().getObjects();
	}
	
	public static <T extends IObject> Collection<T> get( Class<T> cls )
	{
		return Shoebill.getInstance().getManagedObjectPool().getObjects( cls );
	}
	
	
	void processObjectMoved()
	{
		speed = 0.0F;
		SampNativeFunction.getObjectPos( id, location );
	};
	
	
	private int id = INVALID_ID;
	private int modelId;
	private Location location;
	private float speed = 0.0F;
	private float drawDistance = 0.0F;

	@SuppressWarnings("unused")
	private Vector3D attachedOffset;
	@SuppressWarnings("unused")
	private Vector3D attachedRotate;
	
	private IPlayer attachedPlayer;
	private IObject attachedObject;
	private IVehicle attachedVehicle;
	
	
	public ObjectBase( int modelId, float x, float y, float z, float rx, float ry, float rz ) throws CreationFailedException
	{
		initialize( modelId, new Location(x, y, z), new Vector3D(rx, ry, rz), drawDistance );
	}
	
	public ObjectBase( int modelId, float x, float y, float z, float rx, float ry, float rz, float drawDistance ) throws CreationFailedException
	{
		initialize( modelId, new Location(x, y, z), new Vector3D(rx, ry, rz), drawDistance );
	}
	
	public ObjectBase( int modelId, Location loc, float rx, float ry, float rz ) throws CreationFailedException
	{
		initialize( modelId, new Location(loc), new Vector3D(rx, ry, rz), drawDistance );
	}
	
	public ObjectBase( int modelId, Location loc, float rx, float ry, float rz, float drawDistance) throws CreationFailedException
	{
		initialize( modelId, new Location(loc), new Vector3D(rx, ry, rz), drawDistance );
	}
	
	public ObjectBase( int modelId, Location loc, Vector3D rot ) throws CreationFailedException
	{
		initialize( modelId, new Location(loc), new Vector3D(rot), drawDistance );
	}
	
	public ObjectBase( int modelId, Location loc, Vector3D rot, float drawDistance ) throws CreationFailedException
	{
		initialize( modelId, new Location(loc), new Vector3D(rot), drawDistance );
	}
	
	private void initialize( int modelId, Location loc, Vector3D rot, float drawDistance ) throws CreationFailedException
	{
		this.modelId = modelId;
		this.location = loc;
		this.drawDistance = drawDistance;
		
		id = SampNativeFunction.createObject( modelId, loc.getX(), loc.getY(), loc.getZ(), rot.getX(), rot.getY(), rot.getZ(), drawDistance );
		if( id == INVALID_ID ) throw new CreationFailedException();
		
		SampObjectPool pool = (SampObjectPool) Shoebill.getInstance().getManagedObjectPool();
		pool.setObject( id, this );
	}
	
	@Override
	public void destroy()
	{
		if( isDestroyed() ) return;
		
		SampNativeFunction.destroyObject( id );

		SampObjectPool pool = (SampObjectPool) Shoebill.getInstance().getManagedObjectPool();
		pool.setObject( id, null );
		
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
		if( isDestroyed() ) return 0.0F;
		
		if( attachedPlayer != null && attachedPlayer.isOnline() )				return attachedPlayer.getVelocity().speed3d();
		if( attachedObject != null && attachedObject.isDestroyed() == false )	return attachedObject.getSpeed();
		if( attachedVehicle != null && attachedVehicle.isDestroyed() == false )	return attachedVehicle.getVelocity().speed3d();
		
		return speed;
	}
	
	@Override
	public float getDrawDistance()
	{
		return drawDistance;
	}
	
	@Override
	public IPlayer getAttachedPlayer()
	{
		return attachedPlayer;
	}
	
	@Override
	public IObject getAttachedObject()
	{
		return attachedObject;
	}
	
	@Override
	public IVehicle getAttachedVehicle()
	{
		return attachedVehicle;
	}
	
	@Override
	public Location getLocation()
	{
		if( isDestroyed() ) return null;
		
		if( attachedPlayer != null )			location.set( attachedPlayer.getLocation() );
		else if( attachedVehicle != null )		location.set( attachedVehicle.getLocation() );
		else SampNativeFunction.getObjectPos( id, location );
		
		return location.clone();
	}
	
	@Override
	public void setLocation( Vector3D pos )
	{
		if( isDestroyed() ) return;
		
		speed = 0.0F;
		location.set( pos );
		SampNativeFunction.setObjectPos( id, pos.getX(), pos.getY(), pos.getZ() );
	}
	
	@Override
	public void setLocation( Location loc )
	{
		if( isDestroyed() ) return;

		speed = 0.0F;
		location.set( loc );
		SampNativeFunction.setObjectPos( id, loc.getX(), loc.getY(), loc.getZ() );
	}
	
	@Override
	public Vector3D getRotate()
	{
		if( isDestroyed() ) return null;
	
		Vector3D rotate = new Vector3D();
		SampNativeFunction.getObjectRot( id, rotate );
		
		return rotate;
	}
	
	@Override
	public void setRotate( float rx, float ry, float rz )
	{
		if( isDestroyed() ) return;
		
		SampNativeFunction.setObjectRot( id, rx, ry, rz );
	}
	
	@Override
	public void setRotate( Vector3D rot )
	{
		setRotate( rot.getX(), rot.getY(), rot.getZ() );
	}
	
	@Override
	public boolean isMoving()
	{
		if( isDestroyed() ) return false;
		return SampNativeFunction.isObjectMoving( id );
	}

	@Override
	public int move( float x, float y, float z, float speed )
	{
		if( isDestroyed() ) return 0;
		
		if(attachedPlayer == null && attachedVehicle == null) this.speed = speed;
		return SampNativeFunction.moveObject( id, x, y, z, speed, -1000.0f, -1000.0f, -1000.0f );
	}
	
	@Override
	public int move( float x, float y, float z, float speed, float rotX, float rotY, float rotZ )
	{
		if( isDestroyed() ) return 0;
		
		if(attachedPlayer == null && attachedVehicle == null) this.speed = speed;
		return SampNativeFunction.moveObject( id, x, y, z, speed, rotX, rotY, rotZ );
	}
	
	@Override
	public int move( Vector3D pos, float speed )
	{
		return move( pos.getX(), pos.getY(), pos.getZ(), speed );
	}
	
	@Override
	public int move( Vector3D pos, float speed, Vector3D rot )
	{
		return move( pos.getX(), pos.getY(), pos.getZ(), speed, rot.getX(), rot.getY(), rot.getZ() );
	}
	
	@Override
	public void stop()
	{
		if( isDestroyed() ) return;
		
		speed = 0.0F;
		SampNativeFunction.stopObject( id );
	}

	@Override
	public void attach( IPlayer player, float x, float y, float z, float rx, float ry, float rz )
	{
		if( isDestroyed() ) return;
		if( player.isOnline() == false ) return;
		
		SampNativeFunction.attachObjectToPlayer( id, player.getId(), x, y, z, rx, ry, rz );

		speed = 0.0F;
		attachedOffset = new Vector3D( x, y, z );
		attachedRotate = new Vector3D( rx, ry, rz );
		
		attachedPlayer = player;
		attachedObject = null;
		attachedVehicle = null;
	}

	@Override
	public void attach( IPlayer player, Vector3D pos, Vector3D rot )
	{
		attach( player, pos.getX(), pos.getY(), pos.getZ(), rot.getX(), rot.getY(), rot.getZ() );
	}

	@Override
	public void attach( IObject object, float x, float y, float z, float rx, float ry, float rz, boolean syncRotation )
	{
		if( isDestroyed() ) return;
		if( object.isDestroyed() ) return;
		
		if( object instanceof PlayerObject ) throw new UnsupportedOperationException();
		SampNativeFunction.attachObjectToObject( id, object.getId(), x, y, z, rz, ry, rz, syncRotation?1:0 );

		speed = 0.0F;
		attachedOffset = new Vector3D( x, y, z );
		attachedRotate = new Vector3D( rx, ry, rz );
		
		attachedPlayer = null;
		attachedObject = object;
		attachedVehicle = null;
	}
	

	@Override
	public void attach( IObject object, Vector3D pos, Vector3D rot, boolean syncRotation )
	{
		attach( object, pos.getX(), pos.getY(), pos.getZ(), rot.getX(), rot.getY(), rot.getZ(), syncRotation );
	}
	
	@Override
	public void attach( IVehicle vehicle, float x, float y, float z, float rx, float ry, float rz )
	{
		if( isDestroyed() ) return;
		if( vehicle.isDestroyed() ) return;
		
		SampNativeFunction.attachObjectToVehicle( id, vehicle.getId(), x, y, z, rx, ry, rz );

		speed = 0.0F;
		attachedOffset = new Vector3D( x, y, z );
		attachedRotate = new Vector3D( rx, ry, rz );
		
		attachedPlayer = null;
		attachedObject = null;
		attachedVehicle = vehicle;
	}
	
	@Override
	public void attach( IVehicle vehicle, Vector3D pos, Vector3D rot )
	{
		attach( vehicle, pos.getX(), pos.getY(), pos.getZ(), rot.getX(), rot.getY(), rot.getZ() );
	}
}
