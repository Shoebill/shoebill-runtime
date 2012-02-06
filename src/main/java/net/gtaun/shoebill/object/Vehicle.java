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
import net.gtaun.shoebill.data.LocationAngular;
import net.gtaun.shoebill.data.Quaternions;
import net.gtaun.shoebill.data.Velocity;
import net.gtaun.shoebill.event.vehicle.VehicleDestroyEvent;
import net.gtaun.shoebill.event.vehicle.VehicleSpawnEvent;
import net.gtaun.shoebill.samp.SampNativeFunction;

/**
 * @author MK124, JoJLlmAn
 *
 */

public class Vehicle implements IVehicle
{
	public static final int INVALID_ID	=				0xFFFF;
	
	
	public static Collection<IVehicle> get()
	{
		return Shoebill.getInstance().getManagedObjectPool().getVehicles();
	}
	
	public static <T extends IVehicle> Collection<T> get( Class<T> cls )
	{
		return Shoebill.getInstance().getManagedObjectPool().getVehicles( cls );
	}
	
	public static IVehicle get( int id )
	{
		return Shoebill.getInstance().getManagedObjectPool().getVehicle( id );
	}
	
	public static <T extends IVehicle> T get( Class<T> cls, int id )
	{
		return cls.cast( Shoebill.getInstance().getManagedObjectPool().getVehicle(id) );
	}
	
	
	public static void manualEngineAndLights()
	{
		SampNativeFunction.manualVehicleEngineAndLights();
	}
	

	void processVehicleMod()
	{
		//int type = SampNativeFunction.getVehicleComponentType(componentId);
		//component.components[type] = SampNativeFunction.getVehicleComponentInSlot(vehicleid, type);
	}
	
	
	private boolean isStatic = false;
	
	private int id = -1;
	private int modelId;
	private int interiorId;
	private int color1, color2;
	private int respawnDelay;

	private IVehicleParam param;
	private IVehicleComponent component;
	private IVehicleDamage damage;

	
	@Override public int getId()									{ return id; }
	
	@Override public boolean isStatic()								{ return isStatic; }
	
	@Override public int getModelId()								{ return modelId; }
	@Override public int getColor1()								{ return color1; }
	@Override public int getColor2()								{ return color2; }
	@Override public int getRespawnDelay()							{ return respawnDelay; }

	@Override public IVehicleParam getState()						{ return param; }
	@Override public IVehicleComponent getComponent()				{ return component; }
	@Override public IVehicleDamage getDamage()						{ return damage; }
	
	
	public Vehicle( int modelId, float x, float y, float z, int interiorId, int worldId, float angle, int color1, int color2, int respawnDelay )
	{
		this.color1 = color1;
		this.color2 = color2;
		this.respawnDelay = respawnDelay;
		
		initialize( x, y, z, interiorId, worldId, angle );
	}
	
	public Vehicle( int modelId, float x, float y, float z, float angle, int color1, int color2, int respawnDelay )
	{
		this.modelId = modelId;
		this.color1 = color1;
		this.color2 = color2;
		this.respawnDelay = respawnDelay;

		initialize( x, y, z, 0, 0, angle );
	}
	
	public Vehicle( int modelId, Location location, float angle, int color1, int color2, int respawnDelay )
	{
		this.modelId = modelId;
		this.color1 = color1;
		this.color2 = color2;
		this.respawnDelay = respawnDelay;

		initialize( location.x, location.y, location.z, location.interiorId, location.worldId, angle );
	}
	
	public Vehicle( int modelId, LocationAngular location, int color1, int color2, int respawnDelay )
	{
		this.modelId = modelId;
		this.color1 = color1;
		this.color2 = color2;
		this.respawnDelay = respawnDelay;

		initialize( location.x, location.y, location.z, location.interiorId, location.worldId, location.angle );
	}
	
	private void initialize( float x, float y, float z, int interiorId, int worldId, float angle )
	{
		switch( modelId )
		{
			case 537:
			case 538:
			case 569:
			case 570:
			case 590:
				id = SampNativeFunction.addStaticVehicle( modelId, x, y, z, angle, color1, color2 );
				isStatic = true;
				break;
							
			default:
				id = SampNativeFunction.createVehicle( modelId, x, y, z, angle, color1, color2, respawnDelay );
		}
		
		SampNativeFunction.linkVehicleToInterior( id, interiorId );
		SampNativeFunction.setVehicleVirtualWorld( id, worldId );

		param = new VehicleParam( id );
		component = new VehicleComponent( id );
		damage = new VehicleDamage( id );
		
		SampObjectPool pool = (SampObjectPool) Shoebill.getInstance().getManagedObjectPool();
		pool.setVehicle( id, this );
		
		VehicleSpawnEvent event = new VehicleSpawnEvent( this );
		Shoebill.getInstance().getEventManager().dispatchEvent( event, this );
	}
	
	
	@Override
	public void destroy()
	{
		if( isStatic ) return;
		
		SampNativeFunction.destroyVehicle( id );

		SampObjectPool pool = (SampObjectPool) Shoebill.getInstance().getManagedObjectPool();
		pool.setVehicle( id, null );

		VehicleDestroyEvent event = new VehicleDestroyEvent( this );
		Shoebill.getInstance().getEventManager().dispatchEvent( event, this );
		
		id = -1;
	}
	
	@Override
	public boolean isDestroyed()
	{
		return id == -1;
	}
	
	
	@Override
	public Vehicle getTrailer()
	{
		return get( Vehicle.class, SampNativeFunction.getVehicleTrailer(id) );
	}
	
	@Override
	public LocationAngular getLocation()
	{
		LocationAngular location = new LocationAngular();

		SampNativeFunction.getVehiclePos( id, location );
		location.angle = SampNativeFunction.getVehicleZAngle(id);
		location.interiorId = interiorId;
		location.worldId = SampNativeFunction.getVehicleVirtualWorld( id );
		
		return location;
	}
	
	@Override
	public float getAngle()
	{
		return SampNativeFunction.getVehicleZAngle(id);
	}
	
	@Override
	public int getInteriorId()
	{
		return interiorId;
	}
	
	@Override
	public int getWorldId()
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
	public void setLocation( float x, float y, float z )
	{
		SampNativeFunction.setVehiclePos( id, x, y, z );
	}
	
	@Override
	public void setLocation( Location location )
	{
		SampNativeFunction.setVehiclePos( id, location.x, location.y, location.z );
		SampNativeFunction.linkVehicleToInterior( id, location.interiorId );
		SampNativeFunction.setVehicleVirtualWorld( id, location.worldId );
	}
	
	@Override
	public void setLocation( LocationAngular location )
	{
		SampNativeFunction.setVehiclePos( id, location.x, location.y, location.z );
		SampNativeFunction.setVehicleZAngle( id, location.angle );
		SampNativeFunction.linkVehicleToInterior( id, location.interiorId );
		SampNativeFunction.setVehicleVirtualWorld( id, location.worldId );
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
	public void setInteriorId( int interiorId )
	{
		this.interiorId = interiorId;
		SampNativeFunction.linkVehicleToInterior( id, interiorId );
	}
	
	@Override
	public void setWorldId( int worldId )
	{
		SampNativeFunction.setVehicleVirtualWorld( id, worldId );
	}

	
	@Override
	public void putPlayer( IPlayer player, int seat )
	{
		SampNativeFunction.putPlayerInVehicle( player.getId(), id, seat );
	}
	
	@Override
	public boolean isPlayerIn( IPlayer player )
	{
		return SampNativeFunction.isPlayerInVehicle(player.getId(), id);
	}
	
	@Override
	public boolean isStreamedIn( IPlayer forPlayer )
	{
		return SampNativeFunction.isVehicleStreamedIn(id, forPlayer.getId());
	}
	
	@Override
	public void setParamsForPlayer( IPlayer player, boolean objective, boolean doorslocked )
	{
		SampNativeFunction.setVehicleParamsForPlayer( id, player.getId(), objective, doorslocked );
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
	public void setPaintjob( int paintjobId )
	{
		SampNativeFunction.changeVehiclePaintjob( id, paintjobId );
	}
	
	@Override
	public void attachTrailer( IVehicle trailer )
	{
		SampNativeFunction.attachTrailerToVehicle( trailer.getId(), id );
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
	
	//public static int getComponentType( int component );
}
