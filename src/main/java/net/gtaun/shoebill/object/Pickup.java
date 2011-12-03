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
import net.gtaun.shoebill.util.event.EventDispatcher;
import net.gtaun.shoebill.util.event.IEventDispatcher;

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
	
	
	private EventDispatcher eventDispatcher = new EventDispatcher();
	
	private int id = -1;
	private int model, type;
	private int world = -1;
	private Location position;

	
	@Override public IEventDispatcher getEventDispatcher()		{ return eventDispatcher; }
	
	@Override public int getModel()								{ return model; }
	@Override public int getType()								{ return type; }
	@Override public int getWorld()								{ return world; }
	@Override public Location getPosition()						{ return position.clone(); }
	
	
	public Pickup( int model, int type, float x, float y, float z, int virtualWorld )
	{
		this.model = model;
		this.type = type;
		this.position = new Location( x, y, z );
		this.world = virtualWorld;
		
		initialize();
	}
	
	public Pickup( int model, int type, float x, float y, float z)
	{
		this.model = model;
		this.type = type;
		this.position = new Location( x, y, z );
		
		initialize();
	}
	
	public Pickup( int model, int type, Location point )
	{
		this.model = model;
		this.type = type;
		this.position = point.clone();
		this.world = point.world;
		
		initialize();
	}
	
	private void initialize()
	{
		id = SampNativeFunction.createPickup( model, type, position.x, position.y, position.z, world );
		
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
