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

import java.io.Serializable;

/**
 * @author MK124
 *
 */

public class Area implements Serializable
{
	
	private static final long serialVersionUID = 1L;
	
	public float minX, minY, maxX, maxY;
	
	
	public Area()
	{
		
	}

	public Area( float minX, float minY, float maxX, float maxY )
	{
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
	}
	
	
	public float area()
	{
		return (maxX-minX) * (maxY-minY);
	}


	public void set( Area bound )
	{
		minX = bound.minX;
		maxX = bound.maxX;
		minY = bound.minY;
		maxX = bound.maxY;
	}
	
	public boolean equals( Object obj )
	{
		if( obj == this )				return true;
		if( !(obj instanceof Area) )	return false;
		
		Area area = (Area) obj;
		if( area.minX != area.minX )	return false;
		if( area.minY != area.minY )	return false;
		if( area.maxX != area.maxX )	return false;
		if( area.maxY != area.maxY )	return false;
		
		return true;
	}
	
	public Area clone()
	{
		return new Area(minX, minY, maxX, maxY);
	}
}
