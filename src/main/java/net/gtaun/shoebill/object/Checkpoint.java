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

import java.util.Collection;
import java.util.Vector;

import net.gtaun.shoebill.Shoebill;
import net.gtaun.shoebill.data.Vector3D;
import net.gtaun.shoebill.samp.SampNativeFunction;
import net.gtaun.shoebill.util.event.EventDispatcher;
import net.gtaun.shoebill.util.event.IEventDispatcher;

/**
 * @author JoJLlmAn, MK124
 *
 */

public class Checkpoint implements ICheckpoint
{
	private EventDispatcher eventDispatcher = new EventDispatcher();
	
	private Vector3D location;
	private float size;
	
	
	@Override public IEventDispatcher getEventDispatcher()			{ return eventDispatcher; }

	@Override public Vector3D getLocation()							{ return location.clone(); }
	@Override public float getSize()								{ return size; }

	
	public Checkpoint( float x, float y, float z, float size )
	{
		this.location = new Vector3D( x, y, z );
		this.size = size;
	}
	
	public Checkpoint( Vector3D location, float size )
	{
		this.location = location.clone();
		this.size = size;
	}
	

	@Override
	public void setLocation( Vector3D location )
	{
		this.location = location.clone();
		update();
	}
	
	@Override
	public void set( IPlayer player )
	{
		player.setCheckpoint( this );
	}
	
	@Override
	public void disable( IPlayer player )
	{
		if( player.getCheckpoint() != this ) return;
		player.setCheckpoint( null );
	}
	
	@Override
	public boolean isInCheckpoint( IPlayer player )
	{
		if( player.getCheckpoint() != this ) return false;
		return SampNativeFunction.isPlayerInCheckpoint( player.getId() );
	}
	
	@Override
	public void update()
	{
		Collection<IPlayer> players = Shoebill.getInstance().getManagedObjectPool().getPlayers();
		for( IPlayer player : players )
		{
			if( player == null ) continue;
			if( player.getCheckpoint() == this ) set( player );
		}
	}
	
	@Override
	public Collection<IPlayer> getUsingPlayers()
	{
		Collection<IPlayer> players = new Vector<IPlayer>();
		for( IPlayer player : Player.get() )
		{
			if( player.getCheckpoint() == this ) players.add( player );
		}
		
		return players;
	}
}
