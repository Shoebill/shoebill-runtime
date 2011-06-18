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

public class PointRot extends Point
{
	public float rx, ry, rz;
	
	
	public PointRot()
	{
		
	}

	public PointRot( float x, float y, float z, int interiorId, int worldId, float rx, float ry, float rz )
	{
		super( x, y, z, interiorId, worldId );
		
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
	}
	
	public PointRot( float x, float y, float z, int worldId, float rx, float ry, float rz )
	{
		super( x, y, z, worldId );
		
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
	}
	
	public PointRot( float x, float y, float z, float rx, float ry, float rz )
	{
		super( x, y, z );
		
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
	}

	public PointRot( Point point, float rx, float ry, float rz )
	{
		super( point );
		
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
	}
	
	public PointRot( PointRot point )
	{
		super( point );
		
		this.rx = point.rx;
		this.ry = point.ry;
		this.rz = point.rz;
	}
	
	
	public void set( PointRot point )
	{
		x = point.x;
		y = point.y;
		z = point.z;
		interiorId = point.interiorId;
		worldId = point.worldId;
		rx = point.rx;
		ry = point.ry;
		rz = point.rz;
	}
	
	public boolean equals( Object obj )
	{
		if( obj == this )						return true;
		if( !(obj instanceof PointRot) )		return false;
		
		PointRot point = (PointRot) obj;
		if( point.x != x )						return false;
		if( point.y != y )						return false;
		if( point.z != z )						return false;
		if( point.interiorId != interiorId )	return false;
		if( point.worldId != worldId )			return false;
		if( point.rx != rx )					return false;
		if( point.ry != ry )					return false;
		if( point.rz != rz )					return false;
		
		return true;
	}
	
	public PointRot clone()
	{
		return new PointRot(this);
	}
}
