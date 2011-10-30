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

import java.util.Vector;

import net.gtaun.lungfish.data.Point;
import net.gtaun.lungfish.object.IPickup;
import net.gtaun.lungfish.util.event.EventDispatcher;
import net.gtaun.lungfish.util.event.IEventDispatcher;
import net.gtaun.shoebill.NativeFunction;

/**
 * @author MK124, JoJLlmAn
 */

public class Pickup implements IPickup
{
	public static Vector<Pickup> get()
	{
		return Gamemode.getInstances(Gamemode.instance.pickupPool, Pickup.class);
	}
	
	public static <T> Vector<T> get( Class<T> cls )
	{
		return Gamemode.getInstances(Gamemode.instance.pickupPool, cls);
	}
	
	
	EventDispatcher eventDispatcher = new EventDispatcher();
	
	int id;
	int model, type;
	int world = -1;
	Point position;

	
	@Override public IEventDispatcher getEventDispatcher()		{ return eventDispatcher; }
	
	@Override public int getModel()								{ return model; }
	@Override public int getType()								{ return type; }
	@Override public int getWorld()								{ return world; }
	@Override public Point getPosition()							{ return position.clone(); }
	
	
	
	public Pickup( int model, int type, float x, float y, float z, int virtualWorld )
	{
		this.model = model;
		this.type = type;
		this.position = new Point( x, y, z );
		this.world = virtualWorld;
		
		init();
	}
	
	public Pickup( int model, int type, float x, float y, float z)
	{
		this.model = model;
		this.type = type;
		this.position = new Point( x, y, z );
		
		init();
	}
	
	public Pickup( int model, int type, Point point )
	{
		this.model = model;
		this.type = type;
		this.position = point.clone();
		this.world = point.world;
		
		init();
	}
	
	private void init()
	{
		id = NativeFunction.createPickup( model, type, position.x, position.y, position.z, world );
		
		Gamemode.instance.pickupPool[id] = this;
	}
	
	
//---------------------------------------------------------
	
	@Override
	public void destroy()
	{
		NativeFunction.destroyPickup( id );
		Gamemode.instance.pickupPool[ id ] = null;
	}
}
