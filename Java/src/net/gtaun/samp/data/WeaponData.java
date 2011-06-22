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

import net.gtaun.samp.enums.Weapon;

/**
 * @author MK124
 *
 */

public class WeaponData
{
	public Weapon weapon;
	public int ammo;
	

	public WeaponData()
	{
		
	}
	
	public WeaponData( int weapon, int ammo )
	{
		this.weapon = Weapon.get(weapon);
		this.ammo = ammo;
	}
	
	public WeaponData( Weapon weapon, int ammo )
	{
		this.weapon = weapon;
		this.ammo = ammo;
	}
	
	public int id()
	{
		return weapon.id();
	}
	
	
	public boolean equals( Object obj )
	{
		if( obj == this )					return true;
		if( !(obj instanceof WeaponData) )	return false;
		
		WeaponData data = (WeaponData) obj;
		if( data.weapon != weapon )					return false;
		if( data.ammo != ammo )				return false;
		
		return true;
	}
	
	public WeaponData clone()
	{
		return new WeaponData(weapon, ammo);
	}
}
