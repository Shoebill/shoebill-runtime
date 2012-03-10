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

public class LocationRadius extends Location implements Cloneable, Serializable
{
	private static final long serialVersionUID = -4375366678586498863L;
	
	
	private class ImmutablyLocationRadius extends LocationRadius implements Immutably
	{
		private static final long serialVersionUID = LocationRadius.serialVersionUID;
		
		private ImmutablyLocationRadius()
		{
			super( LocationRadius.this.getX(), LocationRadius.this.getY(), LocationRadius.this.getZ(), LocationRadius.this.getInteriorId(), LocationRadius.this.getWorldId(), LocationRadius.this.getDistance() );
		}
	}
	
	
	private float distance;
	
	
	public LocationRadius()
	{
		
	}
	
	public LocationRadius( float x, float y, float z, float distance )
	{
		super( x, y, z );
		this.distance = distance;
	}
	
	public LocationRadius( float x, float y, float z, int interiorId, int worldId, float distance )
	{
		super( x, y, z, interiorId, worldId );
		this.distance = distance;
	}
	
	public LocationRadius( Location location, float distance )
	{
		super( location.getX(), location.getY(), location.getZ(), location.getInteriorId(), location.getWorldId() );
		this.distance = distance;
	}
	
	public float getDistance()
	{
		return distance;
	}

	public void setDistance( float distance )
	{
		if( this instanceof Immutably ) throw new ImmutablyException();
		this.distance = distance;
	}
	

	public void set( float x, float y, float z, float distance )
	{
		if( this instanceof Immutably ) throw new ImmutablyException();
		
		setX( x );
		setY( y );
		setZ( z );
		setDistance( distance );
	}
	
	public void set( float x, float y, float z, int worldId, float distance )
	{
		if( this instanceof Immutably ) throw new ImmutablyException();
		
		setX( x );
		setY( y );
		setZ( z );
		setWorldId( worldId );
		setDistance( distance );
	}
	
	public void set( float x, float y, float z, int interiorId, int worldId, float distance )
	{
		if( this instanceof Immutably ) throw new ImmutablyException();
		
		setX( x );
		setY( y );
		setZ( z );
		setInteriorId( interiorId );
		setWorldId( worldId );
		setDistance( distance );
	}

	public void set( LocationRadius location )
	{
		if( this instanceof Immutably ) throw new ImmutablyException();

		setX( location.getX() );
		setY( location.getY() );
		setZ( location.getZ() );
		setInteriorId( location.getInteriorId() );
		setWorldId( location.getWorldId() );
		setDistance( location.getDistance() );
	}
	
	@Override
	public int hashCode()
	{
		return HashCodeBuilder.reflectionHashCode(160481219, 179424673, this, false);
	}
	
	@Override
	public boolean equals( Object obj )
	{
		return EqualsBuilder.reflectionEquals(this, obj, false);
	}
	
	@Override
	public LocationRadius clone()
	{
		return (LocationRadius) super.clone();
	}
	
	@Override
	public LocationRadius immure()
	{
		return new ImmutablyLocationRadius();
	}
	
	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE, false);
	}
}
