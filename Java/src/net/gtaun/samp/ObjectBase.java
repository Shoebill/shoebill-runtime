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
import net.gtaun.samp.data.PointRot;
import net.gtaun.samp.event.ObjectMovedEvent;

/**
 * @author MK124, JoJLlmAn
 *
 */

public class ObjectBase
{
	public static <T> Vector<T> get( Class<T> cls )
	{
		return GameModeBase.getInstances(GameModeBase.instance.objectPool, cls);
	}
	
	
	int id, model;
	PointRot position;
	float speed = 0;
	PlayerBase attachedPlayer;
	
	public int model()					{ return model; }
	public float speed()				{ return speed; }
	
	
	EventDispatcher<ObjectMovedEvent> eventMoved = new EventDispatcher<ObjectMovedEvent>();
	
	public IEventDispatcher<ObjectMovedEvent> 	eventMoved()	{ return eventMoved;}

	
	ObjectBase()
	{
		
	}
	
	public ObjectBase( int model, float x, float y, float z, float rx, float ry, float rz )
	{
		this.model = model;
		this.position = new PointRot( x, y, z, rx, ry, rz );
		
		init();
	}
	
	public ObjectBase( int model, Point point, float rx, float ry, float rz )
	{
		this.model = model;
		this.position = new PointRot( point, rx, ry, rz );
		
		init();
	}
	
	public ObjectBase( int model, PointRot point )
	{
		this.model = model;
		this.position = point.clone();
		
		init();
	}
	
	private void init()
	{
		id = NativeFunction.createObject( model, position.x, position.y, position.z, position.rx, position.ry, position.rz );
		GameModeBase.instance.objectPool[id] = this;
	}
	

//---------------------------------------------------------
	
	public int onMoved()
	{
		return 1;
	}
	

//---------------------------------------------------------

	public void destroy()
	{
		NativeFunction.destroyObject( id );
		GameModeBase.instance.objectPool[ id ] = null;
	}
	
	public PointRot position()
	{
		if( attachedPlayer != null) return new PointRot(
				position.x + attachedPlayer.position.x,
				position.y + attachedPlayer.position.y, 
				position.z + attachedPlayer.position.z, 
				position.rx, 
				position.ry, 
				position.rz);
		
		if( speed == 0 ) return position.clone();
		
		NativeFunction.getObjectPos( id, position );
		NativeFunction.getObjectRot( id, position );
		return position.clone();
	}
	
	public void setPosition( Point position )
	{
		this.position.set( position );
		NativeFunction.setObjectPos( id, position.x, position.y, position.z );
	}
	
	public void setPosition( PointRot position )
	{
		this.position = position.clone();
		NativeFunction.setObjectPos( id, position.x, position.y, position.z );
		NativeFunction.setObjectRot( id, position.rx, position.ry, position.rz );
	}
	
	public void setRotate( float rx, float ry, float rz )
	{
		position.rx = rx;
		position.ry = ry;
		position.rz = rz;
		
		NativeFunction.setObjectRot( id, rx, ry, rz );
	}
	
	public int move( float x, float y, float z, float speed )
	{
		this.speed = speed;
		return NativeFunction.moveObject( id, x, y, z, speed );
	}
	
	public void stop()
	{
		speed = 0;
		NativeFunction.stopObject( id );
	}
	
	public void attach( PlayerBase player, float x, float y, float z, float rx, float ry, float rz )
	{
		NativeFunction.attachObjectToPlayer( id, player.id, x, y, z, rx, ry, rz );
		attachedPlayer = player;
		
		position = new PointRot(x, y, z, rx, ry, rz);
	}
}
