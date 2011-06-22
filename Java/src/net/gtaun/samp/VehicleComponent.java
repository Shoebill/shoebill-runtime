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

import net.gtaun.samp.enums.VehicleComponentType;


/**
 * @author MK124, JoJLlmAn
 *
 */

public class VehicleComponent
{
	public static int getComponentType( int componentid )
	{
		return NativeFunction.getVehicleComponentType(componentid);
	}
	
	
	int vehicleId;
	
	int[] components = new int[ VehicleComponentType.values().length ];
	
	
	VehicleComponent( int vehicleId )
	{
		this.vehicleId = vehicleId;
		update();
	}
	
//---------------------------------------------------------
	
	public void add( int componentid )
	{
		NativeFunction.addVehicleComponent( vehicleId, componentid );
		
		int type = NativeFunction.getVehicleComponentType(componentid);
		components[type] = NativeFunction.getVehicleComponentInSlot(vehicleId, type);
	}
	
	public void remove( int componentid )
	{
		NativeFunction.removeVehicleComponent( vehicleId, componentid );
		
		int type = NativeFunction.getVehicleComponentType(componentid);
		components[type] = NativeFunction.getVehicleComponentInSlot(vehicleId, type);
	}
	
	public int get( VehicleComponentType type )
	{
		return components[ type.ordinal() ];
	}
	
	public int[] toArray()
	{
		int[] data = new int[components.length];
		System.arraycopy( components, 0, data, 0, components.length );
		
		return data;
	}
	
	
//---------------------------------------------------------
		
	void update()
	{
		for( int i=0; i<components.length; i++ )
			components[i] = NativeFunction.getVehicleComponentInSlot(vehicleId, i);
	}
}
