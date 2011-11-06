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
		SampNativeFunction.manualVehicleEngineAndLights();
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

	
	@Override public IEventDispatcher getEventDispatcher()		{ return eventDispatcher; }
	
	@Override public boolean isStatic()							{ return isStatic; }
	@Override public boolean isDestroyed()						{ return isDestroyed; }
	
	@Override public int getModel()								{ return model; }
	@Override public int getColor1()							{ return color1; }
	@Override public int getColor2()							{ return color2; }
	@Override public int getRespawnDelay()						{ return respawnDelay; }

	@Override public IVehicleParam getState()					{ return param; }
	@Override public IVehicleComponent getComponent()			{ return component; }
	
	
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
		
		isDestroyed = true;
		Gamemode.instance.vehiclePool[ id ] = null;
		
		//onDestroy();
		eventDispatcher.dispatchEvent( new VehicleDestroyEvent(this) );
	}
	
	
	@Override
	public Vehicle getTrailer()
	{
		return get( Vehicle.class, SampNativeFunction.getVehicleTrailer(id) );
	}
	
	@Override
	public PointAngle getPosition()
	{
		PointAngle position = new PointAngle();

		SampNativeFunction.getVehiclePos( id, position );
		position.angle = SampNativeFunction.getVehicleZAngle(id);
		position.interior = interior;
		position.world = SampNativeFunction.getVehicleVirtualWorld( id );
		
		return position;
	}
	
	@Override
	public float getAngle()
	{
		return SampNativeFunction.getVehicleZAngle(id);
	}
	
	@Override
	public int getInterior()
	{
		return interior;
	}
	
	@Override
	public int getWorld()
	{
		return SampNativeFunction.getVehicleVirtualWorld(id);
	}
	
	@Override
	public float getHealth()
	{
		return SampNativeFunction.getVehicleHealth(id);
	}

	@Override
	public Velocity getVelocity()
	{
		Velocity velocity = new Velocity();
		SampNativeFunction.getVehicleVelocity( id, velocity );
		
		return velocity;
	}
	
	@Override
	public Quaternions getRotationQuat()
	{
		Quaternions quaternions = new Quaternions();
		SampNativeFunction.getVehicleRotationQuat( id, quaternions );
		
		return quaternions;
	}
	

	@Override
	public void setPosition( float x, float y, float z )
	{
		SampNativeFunction.setVehiclePos( id, x, y, z );
	}
	
	@Override
	public void setPosition( Point point )
	{
		SampNativeFunction.setVehiclePos( id, point.x, point.y, point.z );
		SampNativeFunction.linkVehicleToInterior( id, point.interior );
		SampNativeFunction.setVehicleVirtualWorld( id, point.world );
	}
	
	@Override
	public void setPosition( PointAngle point )
	{
		SampNativeFunction.setVehiclePos( id, point.x, point.y, point.z );
		SampNativeFunction.setVehicleZAngle( id, point.angle );
		SampNativeFunction.linkVehicleToInterior( id, point.interior );
		SampNativeFunction.setVehicleVirtualWorld( id, point.world );
	}
	
	@Override
	public void setHealth( float health )
	{
		SampNativeFunction.setVehicleHealth( id, health );
	}
	
	@Override
	public void setVelocity( Velocity velocity )
	{
		SampNativeFunction.setVehicleVelocity( id, velocity.x, velocity.y, velocity.z );
	}
	
	@Override
	public void setAngle( float angle )
	{
		SampNativeFunction.setVehicleZAngle( id, angle );
	}
	
	@Override
	public void setInterior( int interior )
	{
		this.interior = interior;
		SampNativeFunction.linkVehicleToInterior( id, interior );
	}
	
	@Override
	public void setWorld( int world )
	{
		SampNativeFunction.setVehicleVirtualWorld( id, world );
	}

	
	@Override
	public void putPlayer( IPlayer p, int seat )
	{
		Player player = (Player) p;
		SampNativeFunction.putPlayerInVehicle( player.id, id, seat );
	}
	
	@Override
	public boolean isPlayerIn( IPlayer p )
	{
		Player player = (Player) p;
		return SampNativeFunction.isPlayerInVehicle(player.id, id);
	}
	
	@Override
	public boolean isStreamedIn( IPlayer fp )
	{
		Player forplayer = (Player) fp;
		return SampNativeFunction.isVehicleStreamedIn(id, forplayer.id);
	}
	
	@Override
	public void setParamsForPlayer( IPlayer p, boolean objective, boolean doorslocked )
	{
		Player player = (Player) p;
		SampNativeFunction.setVehicleParamsForPlayer( id, player.id, objective, doorslocked );
	}
	
	@Override
	public void respawn()
	{
		SampNativeFunction.setVehicleToRespawn( id );
	}
	
	@Override
	public void setColor( int color1, int color2 )
	{
		SampNativeFunction.changeVehicleColor( id, color1, color2 );
	}
	
	@Override
	public void setPaintjob( int paintjobid )
	{
		SampNativeFunction.changeVehiclePaintjob( id, paintjobid );
	}
	
	@Override
	public void attachTrailer( IVehicle t )
	{
		Vehicle trailer = (Vehicle) t;
		SampNativeFunction.attachTrailerToVehicle( trailer.id, id );
	}
	
	@Override
	public void detachTrailer()
	{
		SampNativeFunction.detachTrailerFromVehicle( id );
	}
	
	@Override
	public boolean isTrailerAttached()
	{
		return SampNativeFunction.isTrailerAttachedToVehicle(id);
	}
	
	@Override
	public void setNumberPlate( String numberplate )
	{
		if( numberplate == null ) throw new NullPointerException();
		SampNativeFunction.setVehicleNumberPlate( id, numberplate );
	}
	
	@Override
	public void repair()
	{
		SampNativeFunction.repairVehicle( id );
	}
	
	@Override
	public void setAngularVelocity( Velocity velocity )
	{
		SampNativeFunction.setVehicleAngularVelocity( id, velocity.x, velocity.y, velocity.z );
	}
	
	@Override
	public float distanceToPoint( Point point )
	{
		return SampNativeFunction.getVehicleDistanceFromPoint(id, point.x, point.y, point.z);
	}
	
	//public static int getComponentType( int component );
}
