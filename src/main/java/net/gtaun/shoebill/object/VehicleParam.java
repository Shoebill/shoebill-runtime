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

import net.gtaun.lungfish.object.IVehicleParam;
import net.gtaun.shoebill.NativeFunction;

/**
 * @author JoJLlmAn
 *
 */

public class VehicleParam implements IVehicleParam
{
	public static final int PARAM_UNSET =			-1;
	public static final int PARAM_OFF =				0;
	public static final int PARAM_ON =				1;
	
	
	int vehicleId;
	int engine, lights, alarm, doors, bonnet, boot, objective;
	
	public int engine()			{ return engine; }
	public int lights()			{ return lights; }
	public int alarm()			{ return alarm; }
	public int doors()			{ return doors; }
	public int bonnet()			{ return bonnet; }
	public int boot()			{ return boot; }
	public int objective()		{ return objective; }
	
	
	VehicleParam( int vehicleId )
	{
		this.vehicleId = vehicleId;
	}
	
	public void update()
	{
		NativeFunction.getVehicleParamsEx( vehicleId, this );
	}
	
	public void set( int engine, int lights, int alarm, int doors, int bonnet, int boot, int objective )
	{
		NativeFunction.setVehicleParamsEx( vehicleId, engine, lights, alarm, doors, bonnet, boot, objective );
		NativeFunction.getVehicleParamsEx( vehicleId, this);
	}
}
