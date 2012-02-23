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
import net.gtaun.shoebill.samp.SampNativeFunction;

/**
 * @author MK124, JoJLlmAn
 */

public class Pickup implements IPickup
{
	public static Collection<IPickup> get()
	{
		return Shoebill.getInstance().getManagedObjectPool().getPickups();
	}
	
	public static <T extends IPickup> Collection<T> get( Class<T> cls )
	{
		return Shoebill.getInstance().getManagedObjectPool().getPickups( cls );
	}
	
	
	private int id = -1;
	private int modelId, type;
	private Location location;

	
	@Override public int getModelId()							{ return modelId; }
	@Override public int getType()								{ return type; }
	@Override public Location getLocation()						{ return location.clone(); }
	
	
	public Pickup( int modelId, int type, float x, float y, float z, int worldId )
	{
		this.modelId = modelId;
		this.type = type;
		this.location = new Location( x, y, z, worldId );
		
		initialize();
	}
	
	public Pickup( int modelId, int type, float x, float y, float z )
	{
		this.modelId = modelId;
		this.type = type;
		this.location = new Location( x, y, z );
		
		initialize();
	}
	
	public Pickup( int modelId, int type, Location location )
	{
		this.modelId = modelId;
		this.type = type;
		this.location = location.clone();
		
		initialize();
	}
	
	private void initialize()
	{
		id = SampNativeFunction.createPickup( modelId, type, location.x, location.y, location.z, location.worldId );
		
		SampObjectPool pool = (SampObjectPool) Shoebill.getInstance().getManagedObjectPool();
		pool.setPickup( id, this );
	}
	
	
//---------------------------------------------------------
	
	@Override
	public void destroy()
	{
		SampNativeFunction.destroyPickup( id );

		SampObjectPool pool = (SampObjectPool) Shoebill.getInstance().getManagedObjectPool();
		pool.setPickup( id, null );
		
		id = -1;
	}
	
	@Override
	public boolean isDestroyed()
	{
		return id == -1;
	}
}
