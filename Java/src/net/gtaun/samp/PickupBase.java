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

package net.gtaun.samp;

import java.util.Vector;

import net.gtaun.event.EventDispatcher;
import net.gtaun.event.IEventDispatcher;
import net.gtaun.samp.data.Point;
import net.gtaun.samp.event.PlayerPickupEvent;

/**
 * @author MK124, JoJLlmAn
 */

public class PickupBase
{
	public static <T> Vector<T> getPickups( Class<T> cls )
	{
		return GameModeBase.getInstances(GameModeBase.instance.pickupPool, cls);
	}
	
	
	int id;
	int model, type;
	int world = -1;
	Point position;
	
	public int model()			{ return this.model; }
	public int type()			{ return this.type; }
	public int world()			{ return this.world; }
	public Point position()		{ return this.position; }
	
	
	EventDispatcher<PlayerPickupEvent>		eventPickup = new EventDispatcher<PlayerPickupEvent>();
	
	public IEventDispatcher<PlayerPickupEvent>		eventPickup()		{ return eventPickup; }
	
	
	public PickupBase( int model, int type, float x, float y, float z, int virtualWorld )
	{
		this.model = model;
		this.type = type;
		this.position = new Point( x, y, z );
		this.world = virtualWorld;
		
		init();
	}
	
	public PickupBase( int model, int type, float x, float y, float z)
	{
		this.model = model;
		this.type = type;
		this.position = new Point( x, y, z );
		
		init();
	}
	
	public PickupBase( int model, int type, Point point )
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
		
		GameModeBase.instance.pickupPool[id] = this;
	}
	
	
//---------------------------------------------------------
	
	protected int onPickup( PlayerBase player )
	{
		return 1;
	}
	
	
//---------------------------------------------------------
	
	public void destroy()
	{
		NativeFunction.destroyPickup( id );
		GameModeBase.instance.pickupPool[ id ] = null;
	}
}
