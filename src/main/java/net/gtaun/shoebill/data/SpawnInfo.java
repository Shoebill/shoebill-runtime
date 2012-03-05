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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import net.gtaun.shoebill.data.type.WeaponType;

/**
 * @author MK124
 *
 */

public class SpawnInfo implements Cloneable, Serializable
{
	private static final long serialVersionUID = -1494282877268559489L;
	
	
	public LocationAngular location;
	public int skinId, teamId;
	public WeaponData weapon1, weapon2, weapon3;
	
	
	public SpawnInfo( float x, float y, float z, int interiorId, int worldId, float angle, int skin, int team, WeaponType weapon1, int ammo1, WeaponType weapon2, int ammo2, WeaponType weapon3, int ammo3 )
	{
		location = new LocationAngular(x, y, z, interiorId, worldId, angle);
		
		this.skinId = skin;
		this.teamId = team;
		
		this.weapon1 = new WeaponData(weapon1, ammo1);
		this.weapon2 = new WeaponData(weapon2, ammo2);
		this.weapon3 = new WeaponData(weapon3, ammo3);
	}
	
	public SpawnInfo( LocationAngular location, int skin, int team, WeaponData weapon1, WeaponData weapon2, WeaponData weapon3 )
	{
		this.location = location.clone();
		this.skinId = skin;
		this.teamId = team;
		this.weapon1 = weapon1.clone();
		this.weapon2 = weapon2.clone();
		this.weapon3 = weapon3.clone();
	}
	
	@Override
	public int hashCode()
	{
		return HashCodeBuilder.reflectionHashCode(492876847, 715225739, this, false);
	}

	@Override
	public boolean equals( Object obj )
	{
		return EqualsBuilder.reflectionEquals(this, obj, false);
	}
	
	@Override
	public SpawnInfo clone()
	{
		try
		{
			return (SpawnInfo) super.clone();
		}
		catch( CloneNotSupportedException e )
		{
			throw new InternalError();
		}
	}
	
	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE, false);
	}
}
