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

import net.gtaun.shoebill.data.type.VehicleComponentSlot;
import net.gtaun.shoebill.samp.SampNativeFunction;


/**
 * @author MK124, JoJLlmAn
 *
 */

public class VehicleComponent implements IVehicleComponent
{	
	private IVehicle vehicle;
	private int[] components = new int[ VehicleComponentSlot.values().length ];
	
	
	VehicleComponent( IVehicle vehicle )
	{
		this.vehicle = vehicle;
		update();
	}
	
	
	@Override
	public IVehicle getVehicle()
	{
		return vehicle;
	}
	
	@Override
	public void add( int componentId )
	{
		SampNativeFunction.addVehicleComponent( vehicle.getId(), componentId );
		
		int slot = SampNativeFunction.getVehicleComponentType(componentId);
		components[slot] = SampNativeFunction.getVehicleComponentInSlot(vehicle.getId(), slot);
	}
	
	@Override
	public void remove( int componentId )
	{
		SampNativeFunction.removeVehicleComponent( vehicle.getId(), componentId );
		
		int slot = SampNativeFunction.getVehicleComponentType(componentId);
		components[slot] = SampNativeFunction.getVehicleComponentInSlot(vehicle.getId(), slot);
	}
	
	@Override
	public void remove( VehicleComponentSlot slot )
	{
		int componentId = components[ slot.getData() ];
		SampNativeFunction.removeVehicleComponent( vehicle.getId(), componentId );
	}
	
	@Override
	public int get( VehicleComponentSlot slot )
	{
		return SampNativeFunction.getVehicleComponentInSlot( vehicle.getId(), slot.getData() );
	}
	
	@Override
	public int[] toArray()
	{
		int[] data = new int[ components.length ];
		System.arraycopy( components, 0, data, 0, components.length );
		
		return data;
	}
	
		
	void update()
	{
		for( int i=0; i<components.length; i++ )
			components[i] = SampNativeFunction.getVehicleComponentInSlot(vehicle.getId(), i);
	}
}
