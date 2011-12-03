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

package net.gtaun.shoebill.data;

import java.io.Serializable;

import net.gtaun.shoebill.data.type.WeaponType;


/**
 * @author MK124
 *
 */

public class WeaponData implements Cloneable, Serializable
{
	private static final long serialVersionUID = 8584508544432627380L;
	
	
	public WeaponType type;
	public int ammo;
	

	public WeaponData()
	{
		
	}
	
	public WeaponData( WeaponType type, int ammo )
	{
		this.type = type;
		this.ammo = ammo;
	}
	
	
	@Override
	public boolean equals( Object obj )
	{
		if( obj == this )					return true;
		if( !(obj instanceof WeaponData) )	return false;
		
		WeaponData data = (WeaponData) obj;
		if( data.type != type )				return false;
		if( data.ammo != ammo )				return false;
		
		return true;
	}
	
	@Override
	public WeaponData clone()
	{
		return new WeaponData(type, ammo);
	}
}
