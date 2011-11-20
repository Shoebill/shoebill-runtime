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

import net.gtaun.shoebill.SampNativeFunction;
import net.gtaun.shoebill.data.Point;
import net.gtaun.shoebill.data.PointAngle;
import net.gtaun.shoebill.data.Quaternions;
import net.gtaun.shoebill.data.Velocity;
import net.gtaun.shoebill.event.vehicle.VehicleDestroyEvent;
import net.gtaun.shoebill.util.event.EventDispatcher;
import net.gtaun.shoebill.util.event.IEventDispatcher;

/**
 * @author MK124, JoJLlmAn
 *
 */

public class Vehicle implements IDestroyable
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
		SampNativeFunction.manualVehicleEngineAndLights();
	}
	

	EventDispatcher eventDispatcher = new EventDispatcher();
	
	boolean isStatic = false;
	
	int id = -1;
	int model;
	int interior;
	int color1, color2;
	int respawnDelay;

	VehicleParam param;
	VehicleComponent component;
	VehicleDamage damage;

	
	public IEventDispatcher getEventDispatcher()		{ return eventDispatcher; }
	
	public boolean isStatic()							{ return isStatic; }
	
	public int getModel()								{ return model; }
	public int getColor1()								{ return color1; }
	public int getColor2()								{ return color2; }
	public int getRespawnDelay()						{ return respawnDelay; }

	public VehicleParam getState()						{ return param; }
	public VehicleComponent getComponent()				{ return component; }
	
	
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
				id = SampNativeFunction.addStaticVehicle( model, x, y, z, angle, color1, color2 );
				isStatic = true;
				break;
							
			default:
				id = SampNativeFunction.createVehicle( model, x, y, z, angle, color1, color2, respawnDelay );
		}
		
		SampNativeFunction.linkVehicleToInterior( id, interior );
		SampNativeFunction.setVehicleVirtualWorld( id, world );

		param = new VehicleParam( id );
		component = new VehicleComponent( id );
		damage = new VehicleDamage( id );
		
		Gamemode.instance.vehiclePool[id] = this;
		
		//this.onSpawn();
	}
	
	
//---------------------------------------------------------
	
	@Override
	public void destroy()
	{
		if( isStatic ) return;
		
		SampNativeFunction.destroyVehicle( id );
		Gamemode.instance.vehiclePool[ id ] = null;
		
		id = -1;
		
		eventDispatcher.dispatchEvent( new VehicleDestroyEvent(this) );
	}
	
	@Override
	public boolean isDestroyed()
	{
		return id == -1;
	}
	
	
	public Vehicle getTrailer()
	{
		return get( Vehicle.class, SampNativeFunction.getVehicleTrailer(id) );
	}
	
	public PointAngle getPosition()
	{
		PointAngle position = new PointAngle();

		SampNativeFunction.getVehiclePos( id, position );
		position.angle = SampNativeFunction.getVehicleZAngle(id);
		position.interior = interior;
		position.world = SampNativeFunction.getVehicleVirtualWorld( id );
		
		return position;
	}
	
	public float getAngle()
	{
		return SampNativeFunction.getVehicleZAngle(id);
	}
	
	public int getInterior()
	{
		return interior;
	}
	
	public int getWorld()
	{
		return SampNativeFunction.getVehicleVirtualWorld(id);
	}
	
	public float getHealth()
	{
		return SampNativeFunction.getVehicleHealth(id);
	}

	public Velocity getVelocity()
	{
		Velocity velocity = new Velocity();
		SampNativeFunction.getVehicleVelocity( id, velocity );
		
		return velocity;
	}
	
	public Quaternions getRotationQuat()
	{
		Quaternions quaternions = new Quaternions();
		SampNativeFunction.getVehicleRotationQuat( id, quaternions );
		
		return quaternions;
	}
	

	public void setPosition( float x, float y, float z )
	{
		SampNativeFunction.setVehiclePos( id, x, y, z );
	}
	
	public void setPosition( Point point )
	{
		SampNativeFunction.setVehiclePos( id, point.x, point.y, point.z );
		SampNativeFunction.linkVehicleToInterior( id, point.interior );
		SampNativeFunction.setVehicleVirtualWorld( id, point.world );
	}
	
	public void setPosition( PointAngle point )
	{
		SampNativeFunction.setVehiclePos( id, point.x, point.y, point.z );
		SampNativeFunction.setVehicleZAngle( id, point.angle );
		SampNativeFunction.linkVehicleToInterior( id, point.interior );
		SampNativeFunction.setVehicleVirtualWorld( id, point.world );
	}
	
	public void setHealth( float health )
	{
		SampNativeFunction.setVehicleHealth( id, health );
	}
	
	public void setVelocity( Velocity velocity )
	{
		SampNativeFunction.setVehicleVelocity( id, velocity.x, velocity.y, velocity.z );
	}
	
	public void setAngle( float angle )
	{
		SampNativeFunction.setVehicleZAngle( id, angle );
	}
	
	public void setInterior( int interior )
	{
		this.interior = interior;
		SampNativeFunction.linkVehicleToInterior( id, interior );
	}
	
	public void setWorld( int world )
	{
		SampNativeFunction.setVehicleVirtualWorld( id, world );
	}

	
	public void putPlayer( Player player, int seat )
	{
		SampNativeFunction.putPlayerInVehicle( player.id, id, seat );
	}
	
	public boolean isPlayerIn( Player player )
	{
		return SampNativeFunction.isPlayerInVehicle(player.id, id);
	}
	
	public boolean isStreamedIn( Player forPlayer )
	{
		return SampNativeFunction.isVehicleStreamedIn(id, forPlayer.id);
	}
	
	public void setParamsForPlayer( Player player, boolean objective, boolean doorslocked )
	{
		SampNativeFunction.setVehicleParamsForPlayer( id, player.id, objective, doorslocked );
	}
	
	public void respawn()
	{
		SampNativeFunction.setVehicleToRespawn( id );
	}
	
	public void setColor( int color1, int color2 )
	{
		SampNativeFunction.changeVehicleColor( id, color1, color2 );
	}
	
	public void setPaintjob( int paintjobid )
	{
		SampNativeFunction.changeVehiclePaintjob( id, paintjobid );
	}
	
	public void attachTrailer( Vehicle trailer )
	{
		SampNativeFunction.attachTrailerToVehicle( trailer.id, id );
	}
	
	public void detachTrailer()
	{
		SampNativeFunction.detachTrailerFromVehicle( id );
	}
	
	public boolean isTrailerAttached()
	{
		return SampNativeFunction.isTrailerAttachedToVehicle(id);
	}
	
	public void setNumberPlate( String numberplate )
	{
		if( numberplate == null ) throw new NullPointerException();
		SampNativeFunction.setVehicleNumberPlate( id, numberplate );
	}
	
	public void repair()
	{
		SampNativeFunction.repairVehicle( id );
	}
	
	public void setAngularVelocity( Velocity velocity )
	{
		SampNativeFunction.setVehicleAngularVelocity( id, velocity.x, velocity.y, velocity.z );
	}
	
	public float distanceToPoint( Point point )
	{
		return SampNativeFunction.getVehicleDistanceFromPoint(id, point.x, point.y, point.z);
	}
	
	//public static int getComponentType( int component );
}
