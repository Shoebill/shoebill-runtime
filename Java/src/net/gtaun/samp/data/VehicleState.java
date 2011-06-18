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

package net.gtaun.samp.data;

/**
 * @author MK124
 *
 */

public class VehicleState
{
	public boolean engine, lights, alarm, doors, bonnet, boot, objective;
	
	
	public VehicleState()
	{
		
	}
	
	
	public boolean equals( Object obj )
	{
		if( obj == this )						return true;
		if( !(obj instanceof VehicleState) )	return false;
		
		VehicleState state = (VehicleState) obj;
		if( state.engine != engine )			return false;
		if( state.lights != lights )			return false;
		if( state.alarm != alarm )				return false;
		if( state.doors != doors )				return false;
		if( state.bonnet != bonnet )			return false;
		if( state.boot != boot )				return false;
		if( state.objective != objective )		return false;
		
		return true;
	}
	
	public VehicleState clone()
	{
		VehicleState state = new VehicleState();
		
		state.engine = engine;
		state.lights = lights;
		state.alarm = alarm;
		state.doors = doors;
		state.bonnet = bonnet;
		state.boot = boot;
		state.objective = objective;
		
		return state;
	}
}
