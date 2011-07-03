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
import net.gtaun.samp.data.Point;
import net.gtaun.samp.data.Vector3D;
import net.gtaun.samp.event.RaceCheckpointEnterEvent;
import net.gtaun.samp.event.RaceCheckpointLeaveEvent;

/**
 * @author JoJLlmAn, MK124
 *
 */

public class RaceCheckpointBase extends Vector3D
{
	public static final int TYPE_NORMAL =				0;
	public static final int TYPE_AIR =					3;
	public static final int TYPE_NOTHING =				2;

	private static final int TYPE_NORMAL_FINISH =		1;
	private static final int TYPE_AIR_FINISH =			4;

	
//---------------------------------------------------------
	
	public float size;
	public int type;
	public RaceCheckpointBase next;
	
	
	EventDispatcher<RaceCheckpointEnterEvent>	eventEnter = new EventDispatcher<RaceCheckpointEnterEvent>();
	EventDispatcher<RaceCheckpointLeaveEvent>	eventLeave = new EventDispatcher<RaceCheckpointLeaveEvent>();

	public IEventDispatcher<RaceCheckpointEnterEvent>	eventEnter()	{ return eventEnter; }
	public IEventDispatcher<RaceCheckpointLeaveEvent>	eventLeave()	{ return eventLeave; }
	

	public RaceCheckpointBase( float x, float y, float z, float size, int type, RaceCheckpointBase next )
	{
		super( x, y, z );
		
		this.size = size;
		this.type = type;
	}

	public RaceCheckpointBase( Point position, float size, int type, RaceCheckpointBase next )
	{
		super( position.x, position.y, position.z );
		
		this.size = size;
		this.type = type;
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
		if( next != null )
			NativeFunction.setPlayerRaceCheckpoint( player.id, type, x, y, z, next.x, next.y, next.z, size );
		else
		{
			int type = this.type;
			
			if( type == TYPE_NORMAL )		type = TYPE_NORMAL_FINISH;
			else if( type == TYPE_AIR )		type = TYPE_AIR_FINISH;
			
			NativeFunction.setPlayerRaceCheckpoint( player.id, type, x, y, z, x, y, z, size );
		}
		
		player.raceCheckpoint = this;
	}
	
	public void disable( PlayerBase player )
	{
		if(player.raceCheckpoint != this) return;

		NativeFunction.disablePlayerRaceCheckpoint( player.id );
		player.raceCheckpoint = null;
	}
	
	public boolean inCheckpoint( PlayerBase player )
	{
		if( player.raceCheckpoint != this ) return false;
		return NativeFunction.isPlayerInRaceCheckpoint( player.id );
	}
	
	public void update()
	{
		for (int i = 0; i < GameModeBase.instance.playerPool.length; i++)
		{
			PlayerBase player = GameModeBase.instance.playerPool[i];
			if( player == null ) continue;
			
			if( player.raceCheckpoint == this ) set( player );
		}
	}
	
	public <T extends PlayerBase> Vector<T> usingPlayers( Class<T> cls )
	{
		Vector<T> players = new Vector<T>();
		
		Iterator<T> iterator = PlayerBase.get(cls).iterator();
		while( iterator.hasNext() )
		{
			T player = iterator.next();
			if( player.raceCheckpoint == this ) players.add( player );
		}
		
		return players;
	}
}
