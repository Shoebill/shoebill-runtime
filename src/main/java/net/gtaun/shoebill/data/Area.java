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

public class Area implements Cloneable, Serializable
{
	private static final long serialVersionUID = 4319892622317856825L;
	
	
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
	
	@Override
	public int hashCode()
	{
		return HashCodeBuilder.reflectionHashCode(275604547, 295075147, this, false);
	}
	
	@Override
	public boolean equals( Object obj )
	{
		return EqualsBuilder.reflectionEquals(this, obj, false);
	}

	@Override
	public Area clone()
	{
		try
		{
			return (Area) super.clone();
		}
		catch( CloneNotSupportedException e )
		{
			throw new InternalError();
		}
	}
	
	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE, false);
	}
}
