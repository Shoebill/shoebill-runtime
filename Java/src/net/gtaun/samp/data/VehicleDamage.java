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

public class VehicleDamage
{
	public int panels, doors, lights, tires;
	
	
	public VehicleDamage()
	{
		
	}
	
	
	public boolean equals( Object obj )
	{
		if( obj == this )						return true;
		if( !(obj instanceof VehicleDamage) )	return false;
		
		VehicleDamage damage = (VehicleDamage) obj;
		if( damage.panels != panels )			return false;
		if( damage.doors != doors )				return false;
		if( damage.lights != lights )			return false;
		if( damage.tires != tires )				return false;
		
		return true;
	}
	
	public VehicleDamage clone()
	{
		VehicleDamage damage = new VehicleDamage();
		
		damage.panels = panels;
		damage.doors = doors;
		damage.lights = lights;
		damage.tires = tires;
		
		return damage;
	}
}
