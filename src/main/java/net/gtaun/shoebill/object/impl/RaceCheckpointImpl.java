/**
 * Copyright (C) 2011 JoJLlmAn
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

package net.gtaun.shoebill.object.impl;

import java.util.ArrayList;
import java.util.Collection;

import net.gtaun.shoebill.Shoebill;
import net.gtaun.shoebill.data.Location;
import net.gtaun.shoebill.data.LocationRadius;
import net.gtaun.shoebill.data.Point3D;
import net.gtaun.shoebill.data.constant.RaceCheckpointType;
import net.gtaun.shoebill.object.Player;
import net.gtaun.shoebill.object.RaceCheckpoint;
import net.gtaun.shoebill.samp.SampNativeFunction;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author JoJLlmAn, MK124
 *
 */

public class RaceCheckpointImpl implements RaceCheckpoint
{
	private LocationRadius location;
	private RaceCheckpointType type;
	private RaceCheckpointImpl next;
	
	
	public RaceCheckpointImpl( float x, float y, float z, float size, RaceCheckpointType type, RaceCheckpointImpl next )
	{
		initialize( new LocationRadius(x, y, z, size), type, next );
	}

	public RaceCheckpointImpl( Point3D pos, float size, RaceCheckpointType type, RaceCheckpointImpl next )
	{
		initialize( new LocationRadius(pos, size), type, next );
	}
	
	public RaceCheckpointImpl( Location loc, float size, RaceCheckpointType type, RaceCheckpointImpl next )
	{
		initialize( new LocationRadius(loc, size), type, next );
	}
	
	public RaceCheckpointImpl( LocationRadius loc, RaceCheckpointType type, RaceCheckpointImpl next )
	{
		initialize( new LocationRadius(loc), type, next );
	}
	
	private void initialize( LocationRadius loc, RaceCheckpointType type, RaceCheckpointImpl next )
	{
		this.location = loc;
		this.type = type;
		this.next = next;
	}

	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
	
	@Override
	public LocationRadius getLocation()
	{
		return location.clone();
	}

	@Override
	public void setLocation( float x, float y, float z )
	{
		location.set( x, y, z );
		update();
	}
	
	@Override
	public void setLocation( Point3D pos )
	{
		location.set( pos );
		update();
	}
	
	@Override
	public void setLocation( LocationRadius loc )
	{
		location.set( loc );
		update();
	}
	
	@Override
	public float getSize()
	{
		return location.getRadius();
	}
	
	@Override
	public void setSize( float size )
	{
		location.setRadius( size );
		update();
	}
	
	@Override
	public RaceCheckpointType getType()
	{
		return type;
	}
	
	@Override
	public RaceCheckpointImpl getNext()
	{
		return next;
	}
	
	@Override
	public void set( Player player )
	{
		player.setRaceCheckpoint( this );
	}
	
	@Override
	public void disable( Player player )
	{
		if( player.getRaceCheckpoint() != this ) return;
		player.disableRaceCheckpoint();
	}
	
	@Override
	public boolean isInCheckpoint( Player player )
	{
		if( player.getRaceCheckpoint() != this ) return false;
		
		return SampNativeFunction.isPlayerInRaceCheckpoint( player.getId() );
	}
	
	@Override
	public void update()
	{
		for( Player player : Shoebill.getInstance().getManagedObjectPool().getPlayers() )
		{
			if( player == null ) continue;
			if( player.getRaceCheckpoint() == this ) set( player );
		}
	}
	
	@Override
	public Collection<Player> getUsingPlayers()
	{
		Collection<Player> players = new ArrayList<>();
		for( Player player : Shoebill.getInstance().getManagedObjectPool().getPlayers() )
		{
			if( player.getRaceCheckpoint() == this ) players.add( player );
		}
		
		return players;
	}
}
