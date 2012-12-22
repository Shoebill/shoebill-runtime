/**
 * Copyright (C) 2011 JoJLlmAn
 * Copyright (C) 2011-2012 MK124
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

import java.util.ArrayList;
import java.util.Collection;

import net.gtaun.shoebill.SampObjectStore;
import net.gtaun.shoebill.constant.RaceCheckpointType;
import net.gtaun.shoebill.data.Radius;
import net.gtaun.shoebill.data.Vector3D;
import net.gtaun.shoebill.object.Player;
import net.gtaun.shoebill.object.RaceCheckpoint;
import net.gtaun.shoebill.samp.SampNativeFunction;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 * 
 * @author JoJLlmAn, MK124
 */
public abstract class RaceCheckpointImpl implements RaceCheckpoint
{
	private final SampObjectStore store;
	
	private Radius location;
	private RaceCheckpointType type;
	private RaceCheckpoint next;
	
	
	public RaceCheckpointImpl(SampObjectStore store, Radius loc, RaceCheckpointType type, RaceCheckpoint next)
	{
		this.store = store;
		this.location = new Radius(loc);
		this.type = type;
		this.next = next;
	}
	
	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
	
	@Override
	public Radius getLocation()
	{
		return location.clone();
	}
	
	@Override
	public void setLocation(float x, float y, float z)
	{
		location.set(x, y, z);
		update();
	}
	
	@Override
	public void setLocation(Vector3D pos)
	{
		location.set(pos);
		update();
	}
	
	@Override
	public void setLocation(Radius loc)
	{
		location.set(loc);
		update();
	}
	
	@Override
	public float getRadius()
	{
		return location.getRadius();
	}
	
	@Override
	public void setRadius(float size)
	{
		location.setRadius(size);
		update();
	}
	
	@Override
	public RaceCheckpointType getType()
	{
		return type;
	}
	
	@Override
	public RaceCheckpoint getNext()
	{
		return next;
	}
	
	@Override
	public void set(Player player)
	{
		player.setRaceCheckpoint(this);
	}
	
	@Override
	public void disable(Player player)
	{
		if (player.getRaceCheckpoint() != this) return;
		player.disableRaceCheckpoint();
	}
	
	@Override
	public boolean isInRange(Player player)
	{
		if (player.getRaceCheckpoint() != this) return false;
		
		return SampNativeFunction.isPlayerInRaceCheckpoint(player.getId());
	}
	
	@Override
	public boolean isInRange(Vector3D pos)
	{
		return location.isInRange(pos);
	}
	
	@Override
	public void update()
	{
		for (Player player : store.getPlayers())
		{
			if (player == null) continue;
			if (player.getRaceCheckpoint() == this) set(player);
		}
	}
	
	@Override
	public Collection<Player> getUsingPlayers()
	{
		Collection<Player> players = new ArrayList<>();
		for (Player player : store.getPlayers())
		{
			if (player.getRaceCheckpoint() == this) players.add(player);
		}
		
		return players;
	}
}
