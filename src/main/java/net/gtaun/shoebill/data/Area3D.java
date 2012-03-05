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

/**
 * @author MK124
 *
 */

public class Area3D extends Area implements Cloneable, Serializable
{
	private static final long serialVersionUID = 7421659231232420433L;
	
	
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
	
	
	public float getVolume()
	{
		return (maxX-minX) * (maxY-minY) * (maxZ-minZ);
	}
	
	@Override
	public int hashCode()
	{
		return HashCodeBuilder.reflectionHashCode(256203221, 275604541, this, false);
	}
	
	@Override
	public boolean equals( Object obj )
	{
		return EqualsBuilder.reflectionEquals(this, obj, false);
	}

	@Override
	public Area3D clone()
	{
		return (Area3D) super.clone();
	}
	
	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE, false);
	}
}
