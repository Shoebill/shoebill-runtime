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

public class LocationAngular extends Location implements Cloneable, Serializable
{
	private static final long serialVersionUID = -6964956260244629027L;
	
	
	public float angle;
	

	public LocationAngular()
	{
		
	}
	
	public LocationAngular( float x, float y, float z, int interiorId, int worldId, float angle )
	{
		super( x, y, z, interiorId, worldId );
		this.angle = angle;
	}
	
	public LocationAngular( float x, float y, float z, float angle )
	{
		super( x, y, z );
		this.angle = angle;
	}

	public LocationAngular( Location location, float angle )
	{
		super( location.x, location.y, location.z );
		this.angle = angle;
	}
	
	
	public void set( LocationAngular location )
	{
		x = location.x;
		y = location.y;
		z = location.z;
		interiorId = location.interiorId;
		worldId = location.worldId;
		angle = location.angle;
	}
	
	@Override
	public boolean equals( Object obj )
	{
		if( obj == this )							return true;
		if( !(obj instanceof LocationAngular) )		return false;
		LocationAngular location = (LocationAngular) obj;

		if( location.x != x )						return false;
		if( location.y != y )						return false;
		if( location.z != z )						return false;
		if( location.interiorId != interiorId )		return false;
		if( location.worldId != worldId )			return false;
		if( location.angle != angle )				return false;
		
		return true;
	}
	
	@Override
	public LocationAngular clone()
	{
		return new LocationAngular(x, y, z, interiorId, worldId, angle);
	}
}
