/**
 * Copyright (C) 2011 MK124
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

import net.gtaun.shoebill.data.Location;
import net.gtaun.shoebill.data.LocationAngular;
import net.gtaun.shoebill.data.Quaternions;
import net.gtaun.shoebill.data.Velocity;
import net.gtaun.shoebill.util.event.IEventDispatcher;

/**
 * @author MK124
 *
 */

public interface IVehicle extends IDestroyable
{
	IEventDispatcher getEventDispatcher();
	
	boolean isStatic();
	
	int getId();
	int getModel();
	int getColor1();
	int getColor2();
	int getRespawnDelay();

	IVehicleParam getState();
	IVehicleComponent getComponent();
	public IVehicleDamage getDamage();
	
	IVehicle getTrailer();
	LocationAngular getLocation();
	float getAngle();
	int getInteriorId();
	int getWorldId();
	float getHealth();
	Velocity getVelocity();
	Quaternions getRotationQuat();
	void setLocation( float x, float y, float z );
	void setLocation( Location location );
	void setLocation( LocationAngular location );
	void setHealth( float health );
	void setVelocity( Velocity velocity );
	void setAngle( float angle );
	void setInteriorId( int interiorId );
	void setWorldId( int worldId );
	void putPlayer( IPlayer player, int seat );
	boolean isPlayerIn( IPlayer player );
	boolean isStreamedIn( IPlayer forplayer );
	void setParamsForPlayer( IPlayer player, boolean objective, boolean doorslocked );
	void respawn();
	void setColor( int color1, int color2 );
	void setPaintjob( int paintjobid );
	void attachTrailer( IVehicle trailer );
	void detachTrailer();
	boolean isTrailerAttached();
	void setNumberPlate( String numberplate );
	void repair();
	void setAngularVelocity( Velocity velocity );
}
