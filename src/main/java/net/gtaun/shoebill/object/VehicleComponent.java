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

import net.gtaun.shoebill.SampNativeFunction;


/**
 * @author MK124, JoJLlmAn
 *
 */

public class VehicleComponent
{
	public static final int SLOT_SPOILER =			0;
	public static final int SLOT_HOOD =				1;
	public static final int SLOT_ROOF =				2;
	public static final int SLOT_SIDESKIRT =		3;
	public static final int SLOT_LAMPS =			4;
	public static final int SLOT_NITRO =			5;
	public static final int SLOT_EXHAUST =			6;
	public static final int SLOT_WHEELS =			7;
	public static final int SLOT_STEREO =			8;
	public static final int SLOT_HYDRAULICS =		9;
	public static final int SLOT_FRONT_BUMPER =		10;
	public static final int SLOT_REAR_BUMPER =		11;
	public static final int SLOT_VENT_RIGHT =		12;
	public static final int SLOT_VENT_LEFT =		13;
	public static final int SLOTS =					14;

	
	public static int getComponentSlot( int componentid )
	{
		return SampNativeFunction.getVehicleComponentType(componentid);
	}
	
	
	int vehicleId;
	
	int[] components = new int[SLOTS];
	
	
	VehicleComponent( int vehicleId )
	{
		this.vehicleId = vehicleId;
		update();
	}
	
//---------------------------------------------------------
	
	public void add( int componentid )
	{
		SampNativeFunction.addVehicleComponent( vehicleId, componentid );
		
		int slot = SampNativeFunction.getVehicleComponentType(componentid);
		components[slot] = SampNativeFunction.getVehicleComponentInSlot(vehicleId, slot);
	}
	
	public void remove( int componentid )
	{
		SampNativeFunction.removeVehicleComponent( vehicleId, componentid );
		
		int slot = SampNativeFunction.getVehicleComponentType(componentid);
		components[slot] = SampNativeFunction.getVehicleComponentInSlot(vehicleId, slot);
	}
	
	public int get( int slot )
	{
		return SampNativeFunction.getVehicleComponentInSlot( vehicleId, slot );
	}
	
	public int[] toArray()
	{
		int[] data = new int[SLOTS];
		System.arraycopy( components, 0, data, 0, SLOTS );
		
		return data;
	}
	
	
//---------------------------------------------------------
		
	void update()
	{
		for( int i=0; i<SLOTS; i++ )
			components[i] = SampNativeFunction.getVehicleComponentInSlot(vehicleId, i);
	}
}
