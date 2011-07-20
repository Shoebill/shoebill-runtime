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

package net.gtaun.samp;

import java.util.Iterator;
import java.util.Vector;

import net.gtaun.event.EventDispatcher;
import net.gtaun.event.IEventDispatcher;
import net.gtaun.samp.data.Vector3D;
import net.gtaun.samp.event.CheckpointEnterEvent;
import net.gtaun.samp.event.CheckpointLeaveEvent;

/**
 * @author JoJLlmAn, MK124
 *
 */

public class CheckpointBase extends Vector3D
{
	private static final long serialVersionUID = 1L;

	public float size;
	
	
	EventDispatcher<CheckpointEnterEvent>	eventEnter = new EventDispatcher<CheckpointEnterEvent>();
	EventDispatcher<CheckpointLeaveEvent>	eventLeave = new EventDispatcher<CheckpointLeaveEvent>();

	public IEventDispatcher<CheckpointEnterEvent>	eventEnter()	{ return eventEnter; }
	public IEventDispatcher<CheckpointLeaveEvent>	eventLeave()	{ return eventLeave; }

	
	public CheckpointBase( float x, float y, float z, float size )
	{
		super( x, y, z );
		this.size = size;
	}
	
	public CheckpointBase( Vector3D point, float size )
	{
		super( point.x, point.y, point.z );
		this.size = size;
	}
	

//---------------------------------------------------------

	protected int onEnter( PlayerBase player )
	{
		return 0;
	}
	
	protected int onLeave( PlayerBase player )
	{
		return 0;
	}
	
	
//---------------------------------------------------------
	
	public void set( PlayerBase player )
	{
		NativeFunction.setPlayerCheckpoint( player.id, x, y, z, size );
		player.checkpoint = this;
	}
	
	public void disable( PlayerBase player )
	{
		if( player.checkpoint != this ) return;

		NativeFunction.disablePlayerCheckpoint( player.id );
		player.checkpoint = null;
	}
	
	public boolean inCheckpoint( PlayerBase player )
	{
		if( player.checkpoint != this ) return false;
		return NativeFunction.isPlayerInCheckpoint(player.id);
	}
	
	public void update()
	{
		for (int i = 0; i < GameModeBase.instance.playerPool.length; i++)
		{
			PlayerBase player = GameModeBase.instance.playerPool[i];
			if( player == null ) continue;
			
			if( player.checkpoint == this ) set( player );
		}
	}
	
	public <T extends PlayerBase> Vector<T> usingPlayers( Class<T> cls )
	{
		Vector<T> players = new Vector<T>();
		
		Iterator<T> iterator = PlayerBase.get(cls).iterator();
		while( iterator.hasNext() )
		{
			T player = iterator.next();
			if( player.checkpoint == this ) players.add( player );
		}
		
		return players;
	}
}
