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

package net.gtaun.shoebill.object;

import java.util.ArrayList;
import java.util.Collection;

import net.gtaun.shoebill.Shoebill;
import net.gtaun.shoebill.data.Location;
import net.gtaun.shoebill.data.LocationRadius;
import net.gtaun.shoebill.data.Vector3D;
import net.gtaun.shoebill.data.type.RaceCheckpointType;
import net.gtaun.shoebill.samp.SampNativeFunction;

/**
 * @author JoJLlmAn, MK124
 *
 */

public class RaceCheckpoint implements IRaceCheckpoint
{
	private LocationRadius location;
	private RaceCheckpointType type;
	private RaceCheckpoint next;
	
	
	public RaceCheckpoint( float x, float y, float z, float size, RaceCheckpointType type, RaceCheckpoint next )
	{
		initialize( new LocationRadius(x, y, z, size), type, next );
	}

	public RaceCheckpoint( Vector3D pos, float size, RaceCheckpointType type, RaceCheckpoint next )
	{
		initialize( new LocationRadius(pos, size), type, next );
	}
	
	public RaceCheckpoint( Location loc, float size, RaceCheckpointType type, RaceCheckpoint next )
	{
		initialize( new LocationRadius(loc, size), type, next );
	}
	
	public RaceCheckpoint( LocationRadius loc, RaceCheckpointType type, RaceCheckpoint next )
	{
		initialize( new LocationRadius(loc), type, next );
	}
	
	private void initialize( LocationRadius loc, RaceCheckpointType type, RaceCheckpoint next )
	{
		this.location = loc;
		this.type = type;
		this.next = next;
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
	public void setLocation( Vector3D pos )
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
	public RaceCheckpoint getNext()
	{
		return next;
	}
	
	@Override
	public void set( IPlayer player )
	{
		player.setRaceCheckpoint( this );
	}
	
	@Override
	public void disable( IPlayer player )
	{
		if( player.getRaceCheckpoint() != this ) return;
		player.disableRaceCheckpoint();
	}
	
	@Override
	public boolean isInCheckpoint( IPlayer player )
	{
		if( player.getRaceCheckpoint() != this ) return false;
		
		return SampNativeFunction.isPlayerInRaceCheckpoint( player.getId() );
	}
	
	@Override
	public void update()
	{
		for( IPlayer player : Shoebill.getInstance().getManagedObjectPool().getPlayers() )
		{
			if( player == null ) continue;
			if( player.getRaceCheckpoint() == this ) set( player );
		}
	}
	
	@Override
	public Collection<IPlayer> getUsingPlayers()
	{
		Collection<IPlayer> players = new ArrayList<>();
		for( IPlayer player : Shoebill.getInstance().getManagedObjectPool().getPlayers() )
		{
			if( player.getRaceCheckpoint() == this ) players.add( player );
		}
		
		return players;
	}
}
