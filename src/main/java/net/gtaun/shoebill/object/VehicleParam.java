/**
 * Copyright (C) 2011 JoJLlmAn
 * Copyright (C) 2012 MK124
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

import net.gtaun.shoebill.samp.SampNativeFunction;

/**
 * @author JoJLlmAn, MK124
 *
 */

public class VehicleParam implements IVehicleParam
{
	public static final int PARAM_UNSET =			-1;
	public static final int PARAM_OFF =				0;
	public static final int PARAM_ON =				1;
	
	
	private int vehicleId;
	private int engine, lights, alarm, doors, bonnet, boot, objective;
	
	
	VehicleParam( int vehicleId )
	{
		this.vehicleId = vehicleId;
	}
	

	@Override
	public int getEngine()
	{
		SampNativeFunction.getVehicleParamsEx( vehicleId, this );
		return engine;
	}
	
	@Override
	public int getLights()
	{
		SampNativeFunction.getVehicleParamsEx( vehicleId, this );
		return lights;
	}
	
	@Override
	public int getAlarm()
	{
		SampNativeFunction.getVehicleParamsEx( vehicleId, this );
		return alarm;
	}
	
	@Override
	public int getDoors()
	{
		SampNativeFunction.getVehicleParamsEx( vehicleId, this );
		return doors;
	}
	
	@Override
	public int getBonnet()
	{
		SampNativeFunction.getVehicleParamsEx( vehicleId, this );
		return bonnet;
	}
	
	@Override
	public int getBoot()
	{
		SampNativeFunction.getVehicleParamsEx( vehicleId, this );
		return boot;
	}
	
	@Override
	public int getObjective()
	{
		SampNativeFunction.getVehicleParamsEx( vehicleId, this );
		return objective;
	}
	

	@Override
	public void setEngine( int engine )
	{
		SampNativeFunction.getVehicleParamsEx( vehicleId, this );
		this.engine = engine;
		
		set( engine, lights, alarm, doors, bonnet, boot, objective );
	}
	
	@Override
	public void setLights( int lights )
	{
		SampNativeFunction.getVehicleParamsEx( vehicleId, this );
		set( engine, lights, alarm, doors, bonnet, boot, objective );
	}
	
	@Override
	public void setAlarm( int alarm )
	{
		SampNativeFunction.getVehicleParamsEx( vehicleId, this );
		set( engine, lights, alarm, doors, bonnet, boot, objective );
	}
	
	@Override
	public void setDoors( int doors )
	{
		SampNativeFunction.getVehicleParamsEx( vehicleId, this );
		set( engine, lights, alarm, doors, bonnet, boot, objective );
	}
	
	@Override
	public void setBonnet( int bonnet )
	{
		SampNativeFunction.getVehicleParamsEx( vehicleId, this );
		set( engine, lights, alarm, doors, bonnet, boot, objective );
	}
	
	@Override
	public void setBoot( int boot )
	{
		SampNativeFunction.getVehicleParamsEx( vehicleId, this );
		set( engine, lights, alarm, doors, bonnet, boot, objective );
	}
	
	@Override
	public void setObjective( int objective )
	{
		SampNativeFunction.getVehicleParamsEx( vehicleId, this );
		set( engine, lights, alarm, doors, bonnet, boot, objective );
	}
	
	
	@Override
	public void set( int engine, int lights, int alarm, int doors, int bonnet, int boot, int objective )
	{
		SampNativeFunction.setVehicleParamsEx( vehicleId, engine, lights, alarm, doors, bonnet, boot, objective );
		SampNativeFunction.getVehicleParamsEx( vehicleId, this );
	}
}
