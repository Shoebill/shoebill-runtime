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

import net.gtaun.shoebill.SampNativeFunction;
import net.gtaun.shoebill.SampObjectStore;
import net.gtaun.shoebill.data.Radius;
import net.gtaun.shoebill.data.Vector3D;
import net.gtaun.shoebill.object.Checkpoint;
import net.gtaun.shoebill.object.Player;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 * 
 * @author JoJLlmAn, MK124
 */
public abstract class CheckpointImpl implements Checkpoint
{
	private final SampObjectStore store;
	private Radius location;
	
	
	public CheckpointImpl(SampObjectStore store, Radius loc)
	{
		this.store = store;
		location = new Radius(loc);
	}
	
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
			.append("location", location).toString();
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
	public void setRadius(float radius)
	{
		location.setRadius(radius);
		update();
	}
	
	@Override
	public void set(Player player)
	{
		player.setCheckpoint(this);
	}
	
	@Override
	public void disable(Player player)
	{
		if (player.getCheckpoint() != this) return;
		player.setCheckpoint(null);
	}
	
	@Override
	public boolean isInRange(Player player)
	{
		if (player.getCheckpoint() != this) return false;
		return SampNativeFunction.isPlayerInCheckpoint(player.getId());
	}
	
	@Override
	public boolean isInRange(Vector3D pos)
	{
		return location.isInRange(pos);
	}
	
	@Override
	public void update()
	{
		Collection<? extends Player> players = store.getPlayers();
		for (Player player : players)
		{
			if (player == null) continue;
			if (player.getCheckpoint() == this) set(player);
		}
	}
	
	@Override
	public Collection<Player> getUsingPlayers()
	{
		Collection<Player> usingPlayers = new ArrayList<>();
		Collection<Player> players = store.getPlayers();
		for (Player player : players)
		{
			if (player.getCheckpoint() == this) usingPlayers.add(player);
		}
		
		return usingPlayers;
	}
}
