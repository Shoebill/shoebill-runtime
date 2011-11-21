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

public class Checkpoint extends Vector3D
{
	private static final long serialVersionUID = 8248982970415175584L;

	
	EventDispatcher eventDispatcher = new EventDispatcher();
	
	float size;
	
	
	public IEventDispatcher getEventDispatcher()		{ return eventDispatcher; }
	
	public float getSize()								{ return size; }

	
	public Checkpoint( float x, float y, float z, float size )
	{
		super( x, y, z );
		this.size = size;
	}
	
	public Checkpoint( Vector3D point, float size )
	{
		super( point.x, point.y, point.z );
		this.size = size;
	}
	

//---------------------------------------------------------
	
	public void set( Player player )
	{
		SampNativeFunction.setPlayerCheckpoint( player.id, x, y, z, size );
		player.checkpoint = this;
	}
	
	public void disable( Player player )
	{
		if( player.checkpoint != this ) return;

		SampNativeFunction.disablePlayerCheckpoint( player.id );
		player.checkpoint = null;
	}
	
	public boolean isInCheckpoint( Player player )
	{
		if( player.checkpoint != this ) return false;
		return SampNativeFunction.isPlayerInCheckpoint(player.id);
	}
	
	public void update()
	{
		Collection<Player> players = Shoebill.getInstance().getManagedObjectPool().getPlayers();
		for( Player player : players )
		{
			if( player == null ) continue;
			if( player.checkpoint == this ) set( player );
		}
	}
	
	public Collection<Player> getUsingPlayers()
	{
		Collection<Player> players = new Vector<Player>();
		for( Player player : Player.get() )
		{
			if( player.checkpoint == this ) players.add( player );
		}
		
		return players;
	}
}
