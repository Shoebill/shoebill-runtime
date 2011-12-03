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
	
	public LocationRadius( Location point, float distance )
	{
		super( point.x, point.y, point.z );
		this.distance = distance;
	}
	
	
	public void set( LocationRadius point )
	{
		x = point.x;
		y = point.y;
		z = point.z;
		interior = point.interior;
		world = point.world;
		distance = point.distance;
	}
	
	@Override
	public boolean equals( Object obj )
	{
		if( obj == this )						return true;
		if( !(obj instanceof LocationRadius) )		return false;
		
		LocationRadius point = (LocationRadius) obj;
		if( point.x != x )						return false;
		if( point.y != y )						return false;
		if( point.z != z )						return false;
		if( point.interior != interior )	return false;
		if( point.world != world )			return false;
		if( point.distance != distance )		return false;
		
		return true;
	}
	
	@Override
	public LocationRadius clone()
	{
		return new LocationRadius(x, y, z, distance, interior, world);
	}
}
