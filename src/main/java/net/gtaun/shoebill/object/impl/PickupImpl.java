/**
 * Copyright (C) 2011-2012 MK124
 * Copyright (C) 2011 JoJLlmAn
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

package net.gtaun.shoebill.object.impl;

import net.gtaun.shoebill.SampObjectPoolImpl;
import net.gtaun.shoebill.ShoebillImpl;
import net.gtaun.shoebill.data.Location;
import net.gtaun.shoebill.exception.CreationFailedException;
import net.gtaun.shoebill.object.Pickup;
import net.gtaun.shoebill.proxy.ProxyManager;
import net.gtaun.shoebill.samp.SampNativeFunction;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 * 
 * @author MK124, JoJLlmAn
 */
public class PickupImpl implements Pickup
{
	private ProxyManager proxyManager;
	
	private int id = INVALID_ID;
	private int modelId, type;
	private Location location;
	
	
	public PickupImpl( int modelId, int type, Location loc ) throws CreationFailedException
	{
		initialize( modelId, type, new Location(loc) );
	}
	
	private void initialize( int modelId, int type, Location loc ) throws CreationFailedException
	{
		this.modelId = modelId;
		this.type = type;
		this.location = loc;
		
		id = SampNativeFunction.createPickup( modelId, type, loc.getX(), loc.getY(), loc.getZ(), loc.getWorldId() );
		if( id == INVALID_ID ) throw new CreationFailedException();
	}
	
	@Override
	public ProxyManager getProxyManager()
	{
		return proxyManager;
	}

	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
	
	@Override
	public void destroy()
	{
		if( isDestroyed() ) return;
		
		SampNativeFunction.destroyPickup( id );

		SampObjectPoolImpl pool = (SampObjectPoolImpl) ShoebillImpl.getInstance().getSampObjectPool();
		pool.setPickup( id, null );
		
		id = INVALID_ID;
	}
	
	@Override
	public boolean isDestroyed()
	{
		return id == INVALID_ID;
	}
	
	@Override
	public int getId()
	{
		return id;
	}
	
	@Override
	public int getModelId()
	{
		return modelId;
	}
	
	@Override
	public int getType()
	{
		return type;
	}
	
	@Override
	public Location getLocation()
	{
		return location.clone();
	}
}
