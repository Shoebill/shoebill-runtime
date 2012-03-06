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
import net.gtaun.shoebill.data.LocationRotational;
import net.gtaun.shoebill.exception.CreationFailedException;
import net.gtaun.shoebill.samp.SampNativeFunction;

/**
 * @author MK124, JoJLlmAn
 *
 */

public class ObjectBase implements IObject
{
	static final int INVALID_ID =				0xFFFF;
	
	
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
		speed = 0;
		SampNativeFunction.getObjectPos( id, location );
		SampNativeFunction.getObjectRot( id, location );
	};
	
	
	private int id = INVALID_ID;
	private int modelId;
	private LocationRotational location;
	private float speed = 0;
	private IPlayer attachedPlayer;
	private IObject attachedObject;
	private IVehicle attachedVehicle;
	private float drawDistance = 0;
	
	
	@Override public int getId()									{ return id; }
	@Override public int getModelId()								{ return modelId; }
	@Override public float getSpeed()								{ return speed; }
	@Override public float getDrawDistance()						{ return drawDistance; }
	@Override public IPlayer getAttachedPlayer()					{ return attachedPlayer; }
	@Override public IObject getAttachedObject()					{ return attachedObject; }
	@Override public IVehicle getAttachedVehicle()					{ return attachedVehicle; }
	
	
	public ObjectBase( int modelId, float x, float y, float z, float rx, float ry, float rz ) throws CreationFailedException
	{
		this.modelId = modelId;
		this.location = new LocationRotational( x, y, z, rx, ry, rz );
		
		initialize();
	}
	
	public ObjectBase( int modelId, float x, float y, float z, float rx, float ry, float rz, float drawDistance ) throws CreationFailedException
	{
		this.modelId = modelId;
		this.location = new LocationRotational( x, y, z, rx, ry, rz );
		this.drawDistance = drawDistance;
		
		initialize();
	}
	
	public ObjectBase( int modelId, Location location, float rx, float ry, float rz ) throws CreationFailedException
	{
		this.modelId = modelId;
		this.location = new LocationRotational( location, rx, ry, rz );
		
		initialize();
	}
	
	public ObjectBase( int modelId, Location location, float rx, float ry, float rz, float drawDistance) throws CreationFailedException
	{
		this.modelId = modelId;
		this.location = new LocationRotational( location, rx, ry, rz );
		this.drawDistance = drawDistance;
		
		initialize();
	}
	
	public ObjectBase( int modelId, LocationRotational location ) throws CreationFailedException
	{
		this.modelId = modelId;
		this.location = location.clone();
		
		initialize();
	}
	
	public ObjectBase( int modelId, LocationRotational location, float drawDistance ) throws CreationFailedException
	{
		this.modelId = modelId;
		this.location = location.clone();
		this.drawDistance = drawDistance;
		
		initialize();
	}
	
	private void initialize() throws CreationFailedException
	{
		id = SampNativeFunction.createObject( modelId, location.getX(), location.getY(), location.getZ(), location.getRx(), location.getRy(), location.getRz(), drawDistance );
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
	public LocationRotational getLocation()
	{
		if( isDestroyed() ) return null;
		
		if( attachedPlayer != null )			location.set( attachedPlayer.getLocation() );
		else if( attachedVehicle != null )		location.set( attachedVehicle.getLocation() );
		else SampNativeFunction.getObjectPos( id, location );
		
		SampNativeFunction.getObjectRot( id, location );
		return location.clone();
	}
	
	@Override
	public void setLocation( Location location )
	{
		if( isDestroyed() ) return;
		
		this.location.set( location );
		SampNativeFunction.setObjectPos( id, location.getX(), location.getY(), location.getZ() );
	}
	
	@Override
	public void setLocation( LocationRotational location )
	{
		if( isDestroyed() ) return;
		
		this.location = location.clone();
		SampNativeFunction.setObjectPos( id, location.getX(), location.getY(), location.getZ() );
		SampNativeFunction.setObjectRot( id, location.getRx(), location.getRy(), location.getRz() );
	}
	
	@Override
	public void setRotate( float rx, float ry, float rz )
	{
		if( isDestroyed() ) return;
		
		location.setRx( rx );
		location.setRy( ry );
		location.setRz( rz );
		
		SampNativeFunction.setObjectRot( id, rx, ry, rz );
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
	public void stop()
	{
		if( isDestroyed() ) return;
		
		speed = 0;
		SampNativeFunction.stopObject( id );
	}
	
	@Override
	public void attach( IPlayer player, float x, float y, float z, float rx, float ry, float rz )
	{
		if( isDestroyed() ) return;
		if( player.isOnline() == false ) return;
		
		SampNativeFunction.attachObjectToPlayer( id, player.getId(), x, y, z, rx, ry, rz );
		attachedPlayer = player;
		speed = 0;
	}
	
	@Override
	public void attach( IObject object, float x, float y, float z, float rx, float ry, float rz, boolean syncRotation )
	{
		if( isDestroyed() ) return;
		if( object.isDestroyed() ) return;
		
		if( object instanceof PlayerObject ) throw new UnsupportedOperationException();
		SampNativeFunction.attachObjectToObject( id, object.getId(), x, y, z, rz, ry, rz, syncRotation?1:0 );
		attachedObject = object;
		speed = 0;
	}
	
	@Override
	public void attach( IVehicle vehicle, float x, float y, float z, float rx, float ry, float rz )
	{
		if( isDestroyed() ) return;
		if( vehicle.isDestroyed() ) return;
		
		SampNativeFunction.attachObjectToVehicle( id, vehicle.getId(), x, y, z, rx, ry, rz );
		attachedVehicle = vehicle;
		speed = 0;
	}
}
