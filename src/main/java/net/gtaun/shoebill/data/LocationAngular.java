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

import net.gtaun.shoebill.util.immutable.Immutably;
import net.gtaun.shoebill.util.immutable.ImmutablyException;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author MK124
 *
 */

public class LocationAngular extends Location implements Cloneable, Serializable
{
	private static final long serialVersionUID = -6964956260244629027L;
	
	
	private class ImmutablyLocationAngular extends LocationAngular implements Immutably
	{
		private static final long serialVersionUID = LocationAngular.serialVersionUID;
		
		private ImmutablyLocationAngular()
		{
			super( LocationAngular.this.getX(), LocationAngular.this.getY(), LocationAngular.this.getZ(), LocationAngular.this.getInteriorId(), LocationAngular.this.getWorldId(), LocationAngular.this.getAngle() );
		}
	}
	
	
	private float angle;
	

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
		super( location.getX(), location.getY(), location.getZ() );
		this.angle = angle;
	}
	
	public float getAngle()
	{
		return angle;
	}

	public void setAngle( float angle )
	{
		if( this instanceof Immutably ) throw new ImmutablyException();
		this.angle = angle;
	}

	public void set( LocationAngular location )
	{
		if( this instanceof Immutably ) throw new ImmutablyException();
		
		setX( location.getX() );
		setY( location.getY() );
		setZ( location.getZ() );
		setInteriorId( location.getInteriorId() );
		setWorldId( location.getWorldId() );
		setAngle( location.getAngle() );
	}
	
	@Override
	public int hashCode()
	{
		return HashCodeBuilder.reflectionHashCode(179424691, 198491317, this, false);
	}
	
	@Override
	public boolean equals( Object obj )
	{
		return EqualsBuilder.reflectionEquals(this, obj, false);
	}
	
	@Override
	public LocationAngular clone()
	{
		return (LocationAngular) super.clone();
	}
	
	@Override
	public LocationAngular immure()
	{
		return new ImmutablyLocationAngular();
	}
	
	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE, false);
	}
}
