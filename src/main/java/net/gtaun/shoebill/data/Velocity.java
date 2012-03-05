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

public class Velocity extends Vector3D implements Cloneable, Serializable
{
	private static final long serialVersionUID = 6111643976368753336L;
	
	
	//public float x, y, z;
	

	public Velocity()
	{
		
	}
	
	public Velocity( float x, float y, float z )
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	
	public float speed2d()		{ return (float) Math.sqrt( x*x + y*y ); }
	public float speed3d()		{ return (float) Math.sqrt( x*x + y*y + z*z ); }
	public float angle2d()		{ return (float) Math.acos( x/Math.abs(speed2d()) ); }
	public float angleZ()		{ return (float) Math.acos( z/Math.abs(speed3d()) ); }
	
	

	public void set( Velocity spd )
	{
		x = spd.x;
		y = spd.y;
		z = spd.z;
	}
	
	@Override
	public int hashCode()
	{
		return HashCodeBuilder.reflectionHashCode(413158523, 941083981, this, false);
	}
	
	@Override
	public boolean equals( Object obj )
	{
		return EqualsBuilder.reflectionEquals(this, obj, false);
	}
	
	@Override
	public Velocity clone()
	{
		return (Velocity) super.clone();
	}
	
	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE, false);
	}
}
