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
import net.gtaun.lungfish.data.PointAngle;
import net.gtaun.lungfish.data.Quaternions;
import net.gtaun.lungfish.data.Velocity;
import net.gtaun.lungfish.event.vehicle.VehicleDestroyEvent;
import net.gtaun.lungfish.object.IVehicle;
import net.gtaun.lungfish.util.event.EventDispatcher;
import net.gtaun.lungfish.util.event.IEventDispatcher;

/**
 * @author MK124, JoJLlmAn
 *
 */

public class Vehicle implements IVehicle
{
	public static Vector<Vehicle> get()
	{
		return Gamemode.getInstances(Gamemode.instance.vehiclePool, Vehicle.class);
	}
	
	public static <T> Vector<T> get( Class<T> cls )
	{
		return Gamemode.getInstances(Gamemode.instance.vehiclePool, cls);
	}
	
	public static Vehicle get( int id )
	{
		return get( Vehicle.class, id );
	}
	
	public static <T> T get( Class<T> cls, int id )
	{
		return Gamemode.getInstance(Gamemode.instance.vehiclePool, cls, id);
	}
	
	
	public static void manualEngineAndLights()
	{
		NativeFunction.manualVehicleEngineAndLights();
	}
	

	EventDispatcher eventDispatcher = new EventDispatcher();
	
	boolean isStatic = false, isDestroyed = false;
	
	int id, model;
	int interior;
	int color1, color2;
	int respawnDelay;

	VehicleParam param;
	VehicleComponent component;
	VehicleDamage damage;

	
	public IEventDispatcher getEventDispatcher()	{ return eventDispatcher; }
	
	public boolean isStatic()						{ return isStatic; }
	public boolean isDestroyed()					{ return isDestroyed; }
	
	public int getModel()							{ return model; }
	public int getColor1()							{ return color1; }
	public int getColor2()							{ return color2; }
	public int getRespawnDelay()					{ return respawnDelay; }

	public VehicleParam getState()					{ return param; }
	public VehicleComponent getComponent()			{ return component; }
	
	
	public Vehicle( int model, float x, float y, float z, int interior, int world, float angle, int color1, int color2, int respawnDelay )
	{
		this.color1 = color1;
		this.color2 = color2;
		this.respawnDelay = respawnDelay;
		
		init( x, y, z, interior, world, angle );
	}
	
	public Vehicle( int model, float x, float y, float z, float angle, int color1, int color2, int respawnDelay )
	{
		this.model = model;
		this.color1 = color1;
		this.color2 = color2;
		this.respawnDelay = respawnDelay;

		init( x, y, z, 0, 0, angle );
	}
	
	public Vehicle( int model, Point point, float angle, int color1, int color2, int respawnDelay )
	{
		this.model = model;
		this.color1 = color1;
		this.color2 = color2;
		this.respawnDelay = respawnDelay;

		init( point.x, point.y, point.z, point.interior, point.world, angle );
	}
	
	public Vehicle( int model, PointAngle point, int color1, int color2, int respawnDelay )
	{
		this.model = model;
		this.color1 = color1;
		this.color2 = color2;
		this.respawnDelay = respawnDelay;

		init( point.x, point.y, point.z, point.interior, point.world, point.angle );
	}
	
	private void init( float x, float y, float z, int interior, int world, float angle )
	{
		switch( model )
		{
			case 537:
			case 538:
			case 569:
			case 570:
			case 590:
				id = NativeFunction.addStaticVehicle( model, x, y, z, angle, color1, color2 );
				isStatic = true;
				break;
							
			default:
				id = NativeFunction.createVehicle( model, x, y, z, angle, color1, color2, respawnDelay );
		}
		
		NativeFunction.linkVehicleToInterior( id, interior );
		NativeFunction.setVehicleVirtualWorld( id, world );

		param = new VehicleParam( id );
		component = new VehicleComponent( id );
		damage = new VehicleDamage( id );
		
		Gamemode.instance.vehiclePool[id] = this;
		
		//this.onSpawn();
	}
	
	
//---------------------------------------------------------
	
	public void destroy()
	{
		if( isStatic ) return;
		
		NativeFunction.destroyVehicle( id );
		
		isDestroyed = true;
		Gamemode.instance.vehiclePool[ id ] = null;
		
		//onDestroy();
		eventDispatcher.dispatchEvent( new VehicleDestroyEvent(this) );
	}
	
	
	public Vehicle trailer()
	{
		return get( Vehicle.class, NativeFunction.getVehicleTrailer(id) );
	}
	
	public PointAngle position()
	{
		PointAngle position = new PointAngle();

		NativeFunction.getVehiclePos( id, position );
		position.angle = NativeFunction.getVehicleZAngle(id);
		position.interior = interior;
		position.world = NativeFunction.getVehicleVirtualWorld( id );
		
		return position;
	}
	
	public float angle()
	{
		return NativeFunction.getVehicleZAngle(id);
	}
	
	public int interior()
	{
		return interior;
	}
	
	public int world()
	{
		return NativeFunction.getVehicleVirtualWorld(id);
	}
	
	public float health()
	{
		return NativeFunction.getVehicleHealth(id);
	}

	public Velocity velocity()
	{
		Velocity velocity = new Velocity();
		NativeFunction.getVehicleVelocity( id, velocity );
		
		return velocity;
	}
	
	public Quaternions rotationQuat()
	{
		Quaternions quaternions = new Quaternions();
		NativeFunction.getVehicleRotationQuat( id, quaternions );
		
		return quaternions;
	}
	

	public void setPosition( float x, float y, float z )
	{
		NativeFunction.setVehiclePos( id, x, y, z );
	}
	
	public void setPosition( Point point )
	{
		NativeFunction.setVehiclePos( id, point.x, point.y, point.z );
		NativeFunction.linkVehicleToInterior( id, point.interior );
		NativeFunction.setVehicleVirtualWorld( id, point.world );
	}
	
	public void setPosition( PointAngle point )
	{
		NativeFunction.setVehiclePos( id, point.x, point.y, point.z );
		NativeFunction.setVehicleZAngle( id, point.angle );
		NativeFunction.linkVehicleToInterior( id, point.interior );
		NativeFunction.setVehicleVirtualWorld( id, point.world );
	}
	
	public void setHealth( float health )
	{
		NativeFunction.setVehicleHealth( id, health );
	}
	
	public void setVelocity( Velocity velocity )
	{
		NativeFunction.setVehicleVelocity( id, velocity.x, velocity.y, velocity.z );
	}
	
	public void setAngle( float angle )
	{
		NativeFunction.setVehicleZAngle( id, angle );
	}
	
	public void setInterior( int interior )
	{
		this.interior = interior;
		NativeFunction.linkVehicleToInterior( id, interior );
	}
	
	public void setWorld( int world )
	{
		NativeFunction.setVehicleVirtualWorld( id, world );
	}

	
	public void putPlayer( Player player, int seat )
	{
		NativeFunction.putPlayerInVehicle( player.id, id, seat );
	}
	
	public boolean isPlayerIn( Player player )
	{
		return NativeFunction.isPlayerInVehicle(player.id, id);
	}
	
	public boolean isStreamedIn( Player forplayer )
	{
		return NativeFunction.isVehicleStreamedIn(id, forplayer.id);
	}
	
	public void setParamsForPlayer( Player player, boolean objective, boolean doorslocked )
	{
		NativeFunction.setVehicleParamsForPlayer( id, player.id, objective, doorslocked );
	}
	
	public void respawn()
	{
		NativeFunction.setVehicleToRespawn( id );
	}
	
	public void setColor( int color1, int color2 )
	{
		NativeFunction.changeVehicleColor( id, color1, color2 );
	}
	
	public void setPaintjob( int paintjobid )
	{
		NativeFunction.changeVehiclePaintjob( id, paintjobid );
	}
	
	public void attachTrailer( Vehicle trailer )
	{
		NativeFunction.attachTrailerToVehicle( trailer.id, id );
	}
	
	public void detachTrailer()
	{
		NativeFunction.detachTrailerFromVehicle( id );
	}
	
	public boolean isTrailerAttached()
	{
		return NativeFunction.isTrailerAttachedToVehicle(id);
	}
	
	public void setNumberPlate( String numberplate )
	{
		if( numberplate == null ) throw new NullPointerException();
		NativeFunction.setVehicleNumberPlate( id, numberplate );
	}
	
	public void repair()
	{
		NativeFunction.repairVehicle( id );
	}
	
	public void setAngularVelocity( Velocity velocity )
	{
		NativeFunction.setVehicleAngularVelocity( id, velocity.x, velocity.y, velocity.z );
	}
	
	public float distanceToPoint( Point point )
	{
		return NativeFunction.getVehicleDistanceFromPoint(id, point.x, point.y, point.z);
	}
	
	//public static int getComponentType( int component );
}
