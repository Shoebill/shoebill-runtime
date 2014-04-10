/**
 * Copyright (C) 2011-2014 MK124
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

import net.gtaun.shoebill.SampNativeFunction;
import net.gtaun.shoebill.SampObjectStoreImpl;
import net.gtaun.shoebill.data.Area;
import net.gtaun.shoebill.data.Color;
import net.gtaun.shoebill.data.Vector3D;
import net.gtaun.shoebill.event.destroyable.DestroyEvent;
import net.gtaun.shoebill.exception.CreationFailedException;
import net.gtaun.shoebill.object.Player;
import net.gtaun.shoebill.object.Zone;
import net.gtaun.util.event.EventManager;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 * 
 * @author MK124, JoJLlmAn
 */
public class ZoneImpl implements Zone
{
	private final EventManager rootEventManager;
	
	private int id = INVALID_ID;
	private Area area;
	
	private boolean[] isPlayerShowed = new boolean[SampObjectStoreImpl.MAX_PLAYERS];
	private boolean[] isPlayerFlashing = new boolean[SampObjectStoreImpl.MAX_PLAYERS];
	
	
	public ZoneImpl(EventManager eventManager, float minX, float minY, float maxX, float maxY) throws CreationFailedException
	{
		this.rootEventManager = eventManager;
		initialize(minX, minY, maxX, maxY);
	}
	
	private void initialize(float minX, float minY, float maxX, float maxY) throws CreationFailedException
	{
		area = new Area(minX, minY, maxX, maxY);
		
		id = SampNativeFunction.gangZoneCreate(minX, minY, maxX, maxY);
		if (id == INVALID_ID) throw new CreationFailedException();
		
		for (int i = 0; i < isPlayerShowed.length; i++)
		{
			isPlayerShowed[i] = false;
			isPlayerFlashing[i] = false;
		}
	}
	
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("id", id).toString();
	}
	
	@Override
	public void destroy()
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.gangZoneDestroy(id);
		
		DestroyEvent destroyEvent = new DestroyEvent(this);
		rootEventManager.dispatchEvent(destroyEvent, this);
		
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
	public Area getArea()
	{
		return area.clone();
	}
	
	@Override
	public void show(Player player, Color color)
	{
		if (isDestroyed()) return;
		if (player.isOnline() == false) return;
		
		int playerId = player.getId();
		
		SampNativeFunction.gangZoneShowForPlayer(playerId, id, color.getValue());
		isPlayerShowed[playerId] = true;
		isPlayerFlashing[playerId] = false;
	}
	
	@Override
	public void hide(Player player)
	{
		if (isDestroyed()) return;
		if (player.isOnline() == false) return;
		
		int playerId = player.getId();
		SampNativeFunction.gangZoneHideForPlayer(playerId, id);
		
		isPlayerShowed[playerId] = false;
		isPlayerFlashing[playerId] = false;
	}
	
	@Override
	public void flash(Player player, Color color)
	{
		if (isDestroyed()) return;
		if (player.isOnline() == false) return;
		
		int playerId = player.getId();
		
		if (isPlayerShowed[playerId])
		{
			SampNativeFunction.gangZoneFlashForPlayer(playerId, id, color.getValue());
			isPlayerFlashing[playerId] = true;
		}
	}
	
	@Override
	public void stopFlash(Player player)
	{
		if (isDestroyed()) return;
		if (player.isOnline() == false) return;
		
		int playerId = player.getId();
		
		SampNativeFunction.gangZoneStopFlashForPlayer(playerId, id);
		isPlayerFlashing[playerId] = false;
	}
	
	@Override
	public void showForAll(Color color)
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.gangZoneShowForAll(id, color.getValue());
		for (int i = 0; i < isPlayerShowed.length; i++)
			isPlayerShowed[i] = true;
	}
	
	@Override
	public void hideForAll()
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.gangZoneHideForAll(id);
		
		for (int i = 0; i < isPlayerShowed.length; i++)
		{
			isPlayerShowed[i] = false;
			isPlayerFlashing[i] = false;
		}
	}
	
	@Override
	public void flashForAll(Color color)
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.gangZoneFlashForAll(id, color.getValue());
		for (int i = 0; i < isPlayerShowed.length; i++)
			if (isPlayerShowed[i]) isPlayerFlashing[i] = true;
	}
	
	@Override
	public void stopFlashForAll()
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.gangZoneStopFlashForAll(id);
		for (int i = 0; i < isPlayerFlashing.length; i++)
			isPlayerFlashing[i] = false;
	}
	
	@Override
	public boolean isInRange(Vector3D pos)
	{
		return area.isInRange(pos);
	}
}
