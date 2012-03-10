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

public class LocationRotational extends Location implements Cloneable, Serializable
{
	private static final long serialVersionUID = 3951785121331456948L;
	
	
	private class ImmutablyLocationRotational extends LocationRotational implements Immutably
	{
		private static final long serialVersionUID = LocationRotational.serialVersionUID;
		
		private ImmutablyLocationRotational()
		{
			super( LocationRotational.this.getX(), LocationRotational.this.getY(), LocationRotational.this.getZ(), LocationRotational.this.getInteriorId(), LocationRotational.this.getWorldId(), LocationRotational.this.getRx(), LocationRotational.this.getRy(), LocationRotational.this.getRz() );
		}
	}
	
	
	private float rx, ry, rz;
	
	
	public LocationRotational()
	{
		
	}

	public LocationRotational( float x, float y, float z, int interiorId, int worldId, float rx, float ry, float rz )
	{
		super( x, y, z, interiorId, worldId );
		
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
	}
	
	public LocationRotational( float x, float y, float z, int worldId, float rx, float ry, float rz )
	{
		super( x, y, z, worldId );
		
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
	}
	
	public LocationRotational( float x, float y, float z, float rx, float ry, float rz )
	{
		super( x, y, z );
		
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
	}

	public LocationRotational( Location location, float rx, float ry, float rz )
	{
		super( location.getX(), location.getY(), location.getZ() );
		
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
	}
	
	public float getRx()
	{
		return rx;
	}

	public void setRx( float rx )
	{
		if( this instanceof Immutably ) throw new ImmutablyException();
		this.rx = rx;
	}

	public float getRy()
	{
		return ry;
	}

	public void setRy( float ry )
	{
		if( this instanceof Immutably ) throw new ImmutablyException();
		this.ry = ry;
	}

	public float getRz()
	{
		return rz;
	}

	public void setRz( float rz )
	{
		if( this instanceof Immutably ) throw new ImmutablyException();
		this.rz = rz;
	}

	public void set( float x, float y, float z, float rx, float ry, float rz )
	{
		if( this instanceof Immutably ) throw new ImmutablyException();
		
		setX( x );
		setY( y );
		setZ( z );
		setRx( rx );
		setRx( ry );
		setRx( rz );
	}
	
	public void set( float x, float y, float z, int worldId, float rx, float ry, float rz )
	{
		if( this instanceof Immutably ) throw new ImmutablyException();
		
		setX( x );
		setY( y );
		setZ( z );
		setWorldId( worldId );
		setRx( rx );
		setRx( ry );
		setRx( rz );
	}
	
	public void set( float x, float y, float z, int interiorId, int worldId, float rx, float ry, float rz )
	{
		if( this instanceof Immutably ) throw new ImmutablyException();
		
		setX( x );
		setY( y );
		setZ( z );
		setInteriorId( interiorId );
		setWorldId( worldId );
		setRx( rx );
		setRx( ry );
		setRx( rz );
	}
	
	public void set( LocationRotational location )
	{
		if( this instanceof Immutably ) throw new ImmutablyException();
		
		super.set( location );

		setX( location.getX() );
		setY( location.getY() );
		setZ( location.getZ() );
		setInteriorId( location.getInteriorId() );
		setWorldId( location.getWorldId() );
		setRx( location.getRx() );
		setRy( location.getRy() );
		setRz( location.getRz() );
	}
	
	@Override
	public int hashCode()
	{
		return HashCodeBuilder.reflectionHashCode(573259433, 613651369, this, false);
	}
	
	@Override
	public boolean equals( Object obj )
	{
		return EqualsBuilder.reflectionEquals(this, obj, false);
	}
	
	@Override
	public LocationRotational clone()
	{
		return (LocationRotational) super.clone();
	}
	
	@Override
	public LocationRotational immure()
	{
		return new ImmutablyLocationRotational();
	}
	
	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE, false);
	}
}
