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

package net.gtaun.shoebill;

import java.util.Vector;

import net.gtaun.lungfish.data.Point;
import net.gtaun.lungfish.data.PointRot;
import net.gtaun.lungfish.object.IObject;
import net.gtaun.lungfish.object.IPlayer;
import net.gtaun.lungfish.object.IVehicle;
import net.gtaun.lungfish.util.event.EventDispatcher;
import net.gtaun.lungfish.util.event.IEventDispatcher;

/**
 * @author MK124, JoJLlmAn
 *
 */

public class ObjectBase implements IObject
{
	public static Vector<ObjectBase> get()
	{
		return Gamemode.getInstances(Gamemode.instance.objectPool, ObjectBase.class);
	}
	
	public static <T> Vector<T> get( Class<T> cls )
	{
		return Gamemode.getInstances(Gamemode.instance.objectPool, cls);
	}
	
	
	EventDispatcher eventDispatcher = new EventDispatcher();
	
	int id, model;
	PointRot position;
	float speed = 0;
	Player attachedPlayer;
	Vehicle attachedVehicle;
	float drawDistance = 0;
	
	
	public IEventDispatcher getEventDispatcher()	{ return eventDispatcher; }
	
	public int getModel()							{ return model; }
	public float getSpeed()							{ return speed; }
	public Player getAttachedPlayer()				{ return attachedPlayer; }
	public Vehicle getAttachedVehicle()				{ return attachedVehicle; }
	public float getDrawDistance()					{ return drawDistance; }
	
	
	ObjectBase()
	{
		
	}
	
	public ObjectBase( int model, float x, float y, float z, float rx, float ry, float rz )
	{
		this.model = model;
		this.position = new PointRot( x, y, z, rx, ry, rz );
		
		init();
	}
	
	public ObjectBase( int model, float x, float y, float z, float rx, float ry, float rz, float drawDistance )
	{
		this.model = model;
		this.position = new PointRot( x, y, z, rx, ry, rz );
		this.drawDistance = drawDistance;
		
		init();
	}
	
	public ObjectBase( int model, Point point, float rx, float ry, float rz )
	{
		this.model = model;
		this.position = new PointRot( point, rx, ry, rz );
		
		init();
	}
	
	public ObjectBase( int model, Point point, float rx, float ry, float rz, float drawDistance)
	{
		this.model = model;
		this.position = new PointRot( point, rx, ry, rz );
		this.drawDistance = drawDistance;
		
		init();
	}
	
	public ObjectBase( int model, PointRot point )
	{
		this.model = model;
		this.position = point.clone();
		
		init();
	}
	
	public ObjectBase( int model, PointRot point, float drawDistance )
	{
		this.model = model;
		this.position = point.clone();
		this.drawDistance = drawDistance;
		
		init();
	}
	
	private void init()
	{
		id = NativeFunction.createObject( model, position.x, position.y, position.z, position.rx, position.ry, position.rz, drawDistance );
		Gamemode.instance.objectPool[id] = this;
	}
	

//---------------------------------------------------------

	public void destroy()
	{
		NativeFunction.destroyObject( id );
		Gamemode.instance.objectPool[ id ] = null;
	}
	
	public PointRot getPosition()
	{
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
		if(attachedPlayer == null && attachedVehicle == null) this.speed = speed;
		return NativeFunction.moveObject( id, x, y, z, speed );
	}
	
	public void stop()
	{
		speed = 0;
		NativeFunction.stopObject( id );
	}
	
	public void attach( IPlayer p, float x, float y, float z, float rx, float ry, float rz )
	{
		Player player = (Player)p;
		
		NativeFunction.attachObjectToPlayer( id, player.id, x, y, z, rx, ry, rz );
		attachedPlayer = player;
		speed = 0;
	}
	
	public void attach( IVehicle v, float x, float y, float z, float rx, float ry, float rz )
	{
		Vehicle vehicle = (Vehicle)v;
		
		NativeFunction.attachObjectToVehicle( id, vehicle.id, x, y, z, rx, ry, rz );
		attachedVehicle = vehicle;
		speed = 0;
	}
}
