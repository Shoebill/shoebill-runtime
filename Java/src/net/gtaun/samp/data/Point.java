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

public class Point
{
	public float x, y, z;
	public int interiorId, worldId;
	

	public Point()
	{

	}
	
	public Point( float x, float y, float z )
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Point( float x, float y, float z, int worldId )
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.worldId = worldId;
	}
	
	public Point( float x, float y, float z, int interiorId, int worldId )
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.interiorId = interiorId;
		this.worldId = worldId;
	}

	public Point( Point point )
	{
		this.x = point.x;
    	this.y = point.y;
    	this.z = point.z;
    	this.interiorId = point.interiorId;
    	this.worldId = point.worldId;
	}
	
	
	public void set( Point point )
	{
		x = point.x;
		y = point.y;
		z = point.z;
		interiorId = point.interiorId;
		worldId = point.worldId;
	}
	
	public boolean equals( Object obj )
	{
		if( obj == this )						return true;
		if( !(obj instanceof Point) )			return false;
		
		Point point = (Point) obj;
		if( point.x != x )						return false;
		if( point.y != y )						return false;
		if( point.z != z )						return false;
		if( point.interiorId != interiorId )	return false;
		if( point.worldId != worldId )			return false;
		
		return true;
	}
	
	public Point clone()
	{
		return new Point(this);
	}
}
