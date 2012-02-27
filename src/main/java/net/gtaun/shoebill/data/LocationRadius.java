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

public class LocationRadius extends Location implements Cloneable, Serializable
{
	private static final long serialVersionUID = -4375366678586498863L;
	
	
	public float distance;
	
	
	public LocationRadius()
	{
		
	}
	
	public LocationRadius( float x, float y, float z, float distance )
	{
		super( x, y, z );
		this.distance = distance;
	}
	
	public LocationRadius( float x, float y, float z, float distance, int interiorId, int worldId )
	{
		super( x, y, z, interiorId, worldId );
		this.distance = distance;
	}
	
	public LocationRadius( Location location, float distance )
	{
		super( location.x, location.y, location.z );
		this.distance = distance;
	}
	
	
	public void set( LocationRadius location )
	{
		x = location.x;
		y = location.y;
		z = location.z;
		interiorId = location.interiorId;
		worldId = location.worldId;
		distance = location.distance;
	}
	
	@Override
	public boolean equals( Object obj )
	{
		if( obj == this )								return true;
		if( obj instanceof LocationRadius == false )	return false;
		
		LocationRadius location = (LocationRadius) obj;
		
		if( location.x != x )						return false;
		if( location.y != y )						return false;
		if( location.z != z )						return false;
		if( location.interiorId != interiorId )		return false;
		if( location.worldId != worldId )			return false;
		if( location.distance != distance )			return false;
		
		return true;
	}
	
	@Override
	public LocationRadius clone()
	{
		return new LocationRadius(x, y, z, distance, interiorId, worldId);
	}
}
