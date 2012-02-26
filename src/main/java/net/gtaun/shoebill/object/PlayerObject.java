/**
 * Copyright (C) 2011 MK124
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
 * @author MK124
 *
 */

public class PlayerObject implements IPlayerObject
{
	static final int INVALID_ID =				0xFFFF;
	
	
	public static Collection<IPlayerObject> get( IPlayer player )
	{
		return Shoebill.getInstance().getManagedObjectPool().getPlayerObjects( player );
	}
	
	public static <T extends IPlayerObject> Collection<T> get( IPlayer player, Class<T> cls )
	{
		return Shoebill.getInstance().getManagedObjectPool().getPlayerObjects( player, cls );
	}
	

	void processPlayerObjectMoved()
	{
		speed = 0;
	}
	
	private int id = INVALID_ID;
	private IPlayer player;
	
	private int modelId;
	private LocationRotational location;
	private float speed = 0;
	private IPlayer attachedPlayer;
	private float drawDistance = 0;
	
	
	@Override public IPlayer getPlayer()							{ return player; }

	@Override public int getId()									{ return id; }
	@Override public int getModelId()								{ return modelId; }
	@Override public float getSpeed()								{ return speed; }
	@Override public float getDrawDistance()						{ return drawDistance; }
	@Override public IPlayer getAttachedPlayer()					{ return attachedPlayer; }
	@Override public IObject getAttachedObject()					{ return null; }
	@Override public IVehicle getAttachedVehicle()					{ return null; }
	
	
	public PlayerObject( IPlayer player, int modelId, float x, float y, float z, float rx, float ry, float rz ) throws CreationFailedException
	{
		this.player = player;
		this.modelId = modelId;
		this.location = new LocationRotational( x, y, z, rx, ry, rz );
		
		initialize();
	}
	
	public PlayerObject( IPlayer player, int modelId, float x, float y, float z, float rx, float ry, float rz, float drawDistance ) throws CreationFailedException
	{
		this.player = player;
		this.modelId = modelId;
		this.location = new LocationRotational( x, y, z, rx, ry, rz );
		this.drawDistance = drawDistance;
		
		initialize();
	}
	
	public PlayerObject( IPlayer player, int modelId, Location location, float rx, float ry, float rz ) throws CreationFailedException
	{
		this.player = player;
		this.modelId = modelId;
		this.location = new LocationRotational( location, rx, ry, rz );
		
		initialize();
	}
	
	public PlayerObject( IPlayer player, int modelId, Location location, float rx, float ry, float rz, float drawDistance ) throws CreationFailedException
	{
		this.player = player;
		this.modelId = modelId;
		this.location = new LocationRotational( location, rx, ry, rz );
		this.drawDistance = drawDistance;
		
		initialize();
	}
	
	public PlayerObject( IPlayer player, int modelId, LocationRotational location ) throws CreationFailedException
	{
		this.player = player;
		this.modelId = modelId;
		this.location = location.clone();
		
		initialize();
	}
	
	public PlayerObject( IPlayer player, int modelId, LocationRotational location, float drawDistance ) throws CreationFailedException
	{
		this.player = player;
		this.modelId = modelId;
		this.location = location.clone();
		this.drawDistance = drawDistance;
		
		initialize();
	}
	
	private void initialize() throws CreationFailedException
	{
		if( player.isOnline() == false ) throw new CreationFailedException();
		
		id = SampNativeFunction.createPlayerObject( player.getId(), modelId, location.x, location.y, location.z, location.rx, location.ry, location.rz, drawDistance );
		if( id == INVALID_ID ) throw new CreationFailedException();
		
		SampObjectPool pool = (SampObjectPool) Shoebill.getInstance().getManagedObjectPool();
		pool.setPlayerObject( player, id, this );
	}
	

	@Override
	public void destroy()
	{
		if( isDestroyed() ) return;
		
		if( player.isOnline() )
		{
			SampNativeFunction.destroyPlayerObject( player.getId(), id );

			SampObjectPool pool = (SampObjectPool) Shoebill.getInstance().getManagedObjectPool();
			pool.setPlayerObject( player, id, null );
		}

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
		if( player.isOnline() == false ) return null;
		
		SampNativeFunction.getPlayerObjectPos( player.getId(), id, location );
		SampNativeFunction.getPlayerObjectRot( player.getId(), id, location );
		return location.clone();
	}
	
	@Override
	public void setLocation( Location location )
	{
		if( isDestroyed() ) return;
		if( player.isOnline() == false ) return;
		
		this.location.set( location );
		SampNativeFunction.setPlayerObjectPos( player.getId(), id, location.x, location.y, location.z );
	}
	
	@Override
	public void setLocation( LocationRotational location )
	{
		if( isDestroyed() ) return;
		if( player.isOnline() == false ) return;
		
		this.location = location.clone();
		SampNativeFunction.setPlayerObjectPos( player.getId(), id, location.x, location.y, location.z );
		SampNativeFunction.setPlayerObjectRot( player.getId(), id, location.rx, location.ry, location.rz );
	}
	
	@Override
	public void setRotate( float rx, float ry, float rz )
	{
		if( isDestroyed() ) return;
		if( player.isOnline() == false ) return;
		
		location.rx = rx;
		location.ry = ry;
		location.rz = rz;
		
		SampNativeFunction.setPlayerObjectRot( player.getId(), id, rx, ry, rz );
	}
	
	@Override
	public boolean isMoving()
	{
		if( isDestroyed() ) return false;
		if( player.isOnline() == false ) return false;
		
		return SampNativeFunction.isPlayerObjectMoving(player.getId(), id );
	}
	
	@Override
	public int move( float x, float y, float z, float speed )
	{
		if( isDestroyed() ) return 0;
		if( player.isOnline() == false ) return 0;
		
		if( attachedPlayer == null ) this.speed = speed;	
		return SampNativeFunction.movePlayerObject( player.getId(), id, x, y, z, speed, -1000.0f, -1000.0f, -1000.0f );
	}
	
	@Override
	public int move( float x, float y, float z, float speed, float rotX, float rotY, float rotZ )
	{
		if( isDestroyed() ) return 0;
		if( player.isOnline() == false ) return 0;
		
		if( attachedPlayer == null ) this.speed = speed;	
		return SampNativeFunction.movePlayerObject( player.getId(), id, x, y, z, speed, rotX, rotY, rotZ );
	}
	
	@Override
	public void stop()
	{
		if( isDestroyed() ) return;
		if( player.isOnline() == false ) return;
		
		speed = 0;
		SampNativeFunction.stopPlayerObject( player.getId(), id );
	}
	
	@Override
	public void attach( IPlayer target, float x, float y, float z, float rx, float ry, float rz )
	{
		if( isDestroyed() ) return;
		if( player.isOnline() == false ) return;
		if( target.isOnline() == false ) return;
		
		SampNativeFunction.attachPlayerObjectToPlayer( player.getId(), id, target.getId(), x, y, z, rx, ry, rz );
		
		attachedPlayer = player;
		speed = 0;
	}
	
	@Override
	public void attach( IObject object, float x, float y, float z, float rx, float ry, float rz, boolean syncRotation )
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void attach( IVehicle vehicle, float x, float y, float z, float rx, float ry, float rz )
	{
		throw new UnsupportedOperationException();
	}
}
