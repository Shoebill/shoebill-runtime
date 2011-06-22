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


/**
 * @author MK124, JoJLlmAn
 *
 */

public class VehicleComponent
{
	public static final int TYPE_SPOILER =			0;
	public static final int TYPE_HOOD =				1;
	public static final int TYPE_ROOF =				2;
	public static final int TYPE_SIDESKIRT =		3;
	public static final int TYPE_LAMPS =			4;
	public static final int TYPE_NITRO =			5;
	public static final int TYPE_EXHAUST =			6;
	public static final int TYPE_WHEELS =			7;
	public static final int TYPE_STEREO =			8;
	public static final int TYPE_HYDRAULICS =		9;
	public static final int TYPE_FRONT_BUMPER =		10;
	public static final int TYPE_REAR_BUMPER =		11;
	public static final int TYPE_VENT_RIGHT =		12;
	public static final int TYPE_VENT_LEFT =		13;
	public static final int TYPES =					14;

	
	public static int getComponentType( int componentid )
	{
		return NativeFunction.getVehicleComponentType(componentid);
	}
	
	
	int vehicleId;
	
	int[] components = new int[TYPES];
	
	
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
	
	public int getFromSlot( int type )
	{
		return NativeFunction.getVehicleComponentInSlot( vehicleId, type );
	}
	
	public int[] toArray()
	{
		int[] data = new int[TYPES];
		System.arraycopy( components, 0, data, 0, TYPES );
		
		return data;
	}
	
	
//---------------------------------------------------------
		
	void update()
	{
		for( int i=0; i<TYPES; i++ )
			components[i] = NativeFunction.getVehicleComponentInSlot(vehicleId, i);
	}
}
