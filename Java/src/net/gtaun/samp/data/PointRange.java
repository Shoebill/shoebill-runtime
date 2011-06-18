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

public class PointRange extends Point
{
	public float distance;
	
	
	public PointRange()
	{
		
	}
	
	public PointRange( float x, float y, float z, float distance )
	{
		super( x, y, z );
		this.distance = distance;
	}
	
	public PointRange( float x, float y, float z, float distance, int interiorId, int worldId )
	{
		super( x, y, z, interiorId, worldId );
		this.distance = distance;
	}
	
	public PointRange( Point point, float distance )
	{
		super( point.x, point.y, point.z );
		this.distance = distance;
	}
	
	
	public void set( PointRange point )
	{
		x = point.x;
		y = point.y;
		z = point.z;
		interior = point.interior;
		world = point.world;
		distance = point.distance;
	}
	
	public boolean equals( Object obj )
	{
		if( obj == this )						return true;
		if( !(obj instanceof PointRange) )		return false;
		
		PointRange point = (PointRange) obj;
		if( point.x != x )						return false;
		if( point.y != y )						return false;
		if( point.z != z )						return false;
		if( point.interior != interior )	return false;
		if( point.world != world )			return false;
		if( point.distance != distance )		return false;
		
		return true;
	}
	
	public PointRange clone()
	{
		return new PointRange(x, y, z, distance, interior, world);
	}
}
