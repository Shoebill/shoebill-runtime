/**
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

import net.gtaun.shoebill.samp.SampNativeFunction;

/**
 * @author JoJLlmAn
 *
 */

public class VehicleParam implements IVehicleParam
{
	public static final int PARAM_UNSET =			-1;
	public static final int PARAM_OFF =				0;
	public static final int PARAM_ON =				1;
	
	
	private int vehicleId;
	private int engine, lights, alarm, doors, bonnet, boot, objective;
	
	public int getEngine()			{ return engine; }
	public int getLights()			{ return lights; }
	public int getAlarm()			{ return alarm; }
	public int getDoors()			{ return doors; }
	public int getBonnet()			{ return bonnet; }
	public int getBoot()			{ return boot; }
	public int getObjective()		{ return objective; }
	
	
	VehicleParam( int vehicleId )
	{
		this.vehicleId = vehicleId;
	}
	
	
	public void update()
	{
		SampNativeFunction.getVehicleParamsEx( vehicleId, this );
	}
	
	public void set( int engine, int lights, int alarm, int doors, int bonnet, int boot, int objective )
	{
		SampNativeFunction.setVehicleParamsEx( vehicleId, engine, lights, alarm, doors, bonnet, boot, objective );
		SampNativeFunction.getVehicleParamsEx( vehicleId, this);
	}
}
