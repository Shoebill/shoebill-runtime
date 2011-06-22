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

public class Area3D extends Area
{
	public float minZ, maxZ;
	
	
	public Area3D( float minX, float minY, float minZ, float maxX, float maxY, float maxZ )
	{
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}
	
	public Area3D( Area area, float minZ, float maxZ )
	{
		this.minX = area.minX;
		this.maxX = area.maxX;
		this.minY = area.minY;
		this.maxY = area.maxY;
		this.minZ = minZ;
		this.maxZ = maxZ;
	}
	
	
	public boolean equals( Object obj )
	{
		if( obj == this )				return true;
		if( !(obj instanceof Area3D) )	return false;
		
		Area3D area = (Area3D) obj;
		if( area.minX != minX )			return false;
		if( area.minY != minY )			return false;
		if( area.minZ != minZ )			return false;
		if( area.maxX != maxX )			return false;
		if( area.maxY != maxY )			return false;
		if( area.maxZ != maxZ )			return false;
		
		return true;
	}
	
	public Area3D clone()
	{
		return new Area3D(minX, minY, minZ, maxX, maxY, maxZ);
	}
}
