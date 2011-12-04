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

/**
 * @author MK124
 *
 */

public class Location extends Vector3D implements Cloneable, Serializable
{
	private static final long serialVersionUID = 8895946392500802993L;
	
	
	//public float x, y, z;
	public int interiorId, worldId;
	

	public Location()
	{

	}
	
	public Location( float x, float y, float z )
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Location( float x, float y, float z, int worldId )
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.worldId = worldId;
	}
	
	public Location( float x, float y, float z, int interiorId, int worldId )
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.interiorId = interiorId;
		this.worldId = worldId;
	}
	
	
	public void set( Location location )
	{
		x = location.x;
		y = location.y;
		z = location.z;
		interiorId = location.interiorId;
		worldId = location.worldId;
	}
	
	@Override
	public boolean equals( Object obj )
	{
		if( obj == this )							return true;
		if( !(obj instanceof Location) )			return false;
		
		Location location = (Location) obj;
		if( location.x != x )						return false;
		if( location.y != y )						return false;
		if( location.z != z )						return false;
		if( location.interiorId != interiorId )		return false;
		if( location.worldId != worldId )			return false;
		
		return true;
	}
	
	@Override
	public Location clone()
	{
		return new Location(x, y, z, interiorId, worldId);
	}
}
