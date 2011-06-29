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
import net.gtaun.samp.data.PointAngle;
import net.gtaun.samp.data.Quaternions;
import net.gtaun.samp.data.Velocity;
import net.gtaun.samp.event.VehicleDeathEvent;
import net.gtaun.samp.event.VehicleDestroyEvent;
import net.gtaun.samp.event.VehicleEnterEvent;
import net.gtaun.samp.event.VehicleExitEvent;
import net.gtaun.samp.event.VehicleModEvent;
import net.gtaun.samp.event.VehiclePaintjobEvent;
import net.gtaun.samp.event.VehicleResprayEvent;
import net.gtaun.samp.event.VehicleSpawnEvent;
import net.gtaun.samp.event.VehicleStreamInEvent;
import net.gtaun.samp.event.VehicleStreamOutEvent;
import net.gtaun.samp.event.VehicleUpdateDamageEvent;
import net.gtaun.samp.event.VehicleUpdateEvent;

/**
 * @author MK124, JoJLlmAn
 *
 */

public class VehicleBase
{
	public static <T> Vector<T> get( Class<T> cls )
	{
		return GameModeBase.getInstances(GameModeBase.instance.vehiclePool, cls);
	}
	
	static <T> T get( Class<T> cls, int id )
	{
		return GameModeBase.getInstance(GameModeBase.instance.vehiclePool, cls, id);
	}
	
	
	public static void manualEngineAndLights()
	{
		NativeFunction.manualVehicleEngineAndLights();
	}
	
	
	boolean isStatic = false, isDestroyed = false;
	
	int id, model;
	int interior;
	int color1, color2;
	int respawnDelay;

	VehicleParam param;
	VehicleComponent component;
	VehicleDamage damage;

	public boolean isStatic()					{ return isStatic; }
	public boolean isDestroyed()				{ return isDestroyed; }
	
	public int model()							{ return model; }
	public int color1()							{ return color1; }
	public int color2()							{ return color2; }
	public int respawnDelay()					{ return respawnDelay; }

	public VehicleParam state()					{ return param; }
	public VehicleComponent component()			{ return component; }
	
	
	EventDispatcher<VehicleDestroyEvent>		eventDestroy = new EventDispatcher<VehicleDestroyEvent>();
	EventDispatcher<VehicleSpawnEvent>			eventSpawn = new EventDispatcher<VehicleSpawnEvent>();
	EventDispatcher<VehicleDeathEvent>			eventDeath = new EventDispatcher<VehicleDeathEvent>();
	EventDispatcher<VehicleUpdateEvent>			eventUpdate = new EventDispatcher<VehicleUpdateEvent>();
	EventDispatcher<VehicleEnterEvent>			eventEnter = new EventDispatcher<VehicleEnterEvent>();
	EventDispatcher<VehicleExitEvent>			eventExit = new EventDispatcher<VehicleExitEvent>();
	EventDispatcher<VehicleModEvent>			eventMod = new EventDispatcher<VehicleModEvent>();
	EventDispatcher<VehiclePaintjobEvent>		eventPaintjob = new EventDispatcher<VehiclePaintjobEvent>();
	EventDispatcher<VehicleResprayEvent>		eventRespray = new EventDispatcher<VehicleResprayEvent>();
	EventDispatcher<VehicleUpdateDamageEvent>	eventUpdateDamage = new EventDispatcher<VehicleUpdateDamageEvent>();
	EventDispatcher<VehicleStreamInEvent>		eventStreamIn = new EventDispatcher<VehicleStreamInEvent>();
	EventDispatcher<VehicleStreamOutEvent>		eventStreamOut = new EventDispatcher<VehicleStreamOutEvent>();

	public IEventDispatcher<VehicleDestroyEvent>		eventDestroy()			{ return eventDestroy; }
	public IEventDispatcher<VehicleSpawnEvent>			eventSpawn()			{ return eventSpawn; }
	public IEventDispatcher<VehicleDeathEvent>			eventDeath()			{ return eventDeath; }
	public IEventDispatcher<VehicleUpdateEvent>			eventUpdate()			{ return eventUpdate; }
	public IEventDispatcher<VehicleEnterEvent>			eventEnter()			{ return eventEnter; }
	public IEventDispatcher<VehicleExitEvent>			eventExit()				{ return eventExit; }
	public IEventDispatcher<VehicleModEvent>			eventMod()				{ return eventMod; }
	public IEventDispatcher<VehiclePaintjobEvent>		eventPaintjob()			{ return eventPaintjob; }
	public IEventDispatcher<VehicleResprayEvent>		eventRespray()			{ return eventRespray; }
	public IEventDispatcher<VehicleUpdateDamageEvent>	eventUpdateDamage()		{ return eventUpdateDamage; }
	public IEventDispatcher<VehicleStreamInEvent>		eventStreamIn()			{ return eventStreamIn; }
	public IEventDispatcher<VehicleStreamOutEvent>		eventStreamOut()		{ return eventStreamOut; }
	

	public VehicleBase( int model, float x, float y, float z, int interior, int world, float angle, int color1, int color2, int respawnDelay )
	{
		this.color1 = color1;
		this.color2 = color2;
		this.respawnDelay = respawnDelay;
		
		init( x, y, z, interior, world, angle );
	}
	
	public VehicleBase( int model, float x, float y, float z, float angle, int color1, int color2, int respawnDelay )
	{
		this.model = model;
		this.color1 = color1;
		this.color2 = color2;
		this.respawnDelay = respawnDelay;

		init( x, y, z, 0, 0, angle );
	}
	
	public VehicleBase( int model, Point point, float angle, int color1, int color2, int respawnDelay )
	{
		this.model = model;
		this.color1 = color1;
		this.color2 = color2;
		this.respawnDelay = respawnDelay;

		init( point.x, point.y, point.z, point.interior, point.world, angle );
	}
	
	public VehicleBase( int model, PointAngle point, int color1, int color2, int respawnDelay )
	{
		this.model = model;
		this.color1 = color1;
		this.color2 = color2;
		this.respawnDelay = respawnDelay;

		init( point.x, point.y, point.z, point.interior, point.world, point.angle );
	}
	
	private void init( float x, float y, float z, int interior, int world, float angle )
	{
		id = NativeFunction.createVehicle( model, x, y, z, angle, color1, color2, respawnDelay );
		NativeFunction.linkVehicleToInterior( id, interior );
		NativeFunction.setVehicleVirtualWorld( id, world );

		param = new VehicleParam( id );
		component = new VehicleComponent( id );
		damage = new VehicleDamage( id );
		
		GameModeBase.instance.vehiclePool[id] = this;
		
		this.onSpawn();
	}
	
	
//---------------------------------------------------------
	
	protected int onDestroy()
	{
		return 1;
	}
	
	protected int onSpawn()
	{
		return 1;
	}

	protected int onDeath( PlayerBase killer )
	{
		return 1;
	}
	
	protected int onUpdate()
	{
		return 1;
	}
	
	protected int onEnter( PlayerBase player, boolean ispassenger )
	{
		return 1;
	}

	protected int onExit( PlayerBase player )
	{
		return 1;
	}
	
	protected int onMod( int componentid )
	{
		return 1;
	}

	protected int onPaintjob( int paintjobid )
	{
		return 1;
	}

	protected int onRespray( int color1, int color2 )
	{
		return 1;
	}

	protected int onUpdateDamage( PlayerBase player )
	{
		return 1;
	}

	protected int onStreamIn( PlayerBase player )
	{
		return 1;
	}

	protected int onStreamOut( PlayerBase player )
	{
		return 1;
	}
	
	
//---------------------------------------------------------
		
	void update()
	{
		
	}
	
	
//---------------------------------------------------------
	
	public void destroy()
	{
		if( isStatic ) return;
		
		NativeFunction.destroyVehicle( id );
		
		isDestroyed = true;
		GameModeBase.instance.vehiclePool[ id ] = null;
		
		onDestroy();
		eventDestroy.dispatchEvent( new VehicleDestroyEvent(this) );
	}
	
	
	public VehicleBase trailer()
	{
		return get( VehicleBase.class, NativeFunction.getVehicleTrailer(id) );
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

	
	public void putPlayer( PlayerBase player, int seat )
	{
		NativeFunction.putPlayerInVehicle( player.id, id, seat );
	}
	
	public boolean isPlayerIn( PlayerBase player )
	{
		return NativeFunction.isPlayerInVehicle(player.id, id);
	}
	
	public boolean isStreamedIn( PlayerBase forplayer )
	{
		return NativeFunction.isVehicleStreamedIn(id, forplayer.id);
	}
	
	public void setParamsForPlayer( PlayerBase player, boolean objective, boolean doorslocked )
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
	
	public void attachTrailer( VehicleBase trailer )
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
	
	//public static int getComponentType( int component );
}
