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

import java.util.Iterator;
import java.util.Vector;

import net.gtaun.lungfish.data.Point;
import net.gtaun.lungfish.data.Vector3D;
import net.gtaun.lungfish.object.IPlayer;
import net.gtaun.lungfish.object.IRaceCheckpoint;
import net.gtaun.lungfish.util.event.EventDispatcher;
import net.gtaun.lungfish.util.event.IEventDispatcher;
import net.gtaun.shoebill.NativeFunction;

/**
 * @author JoJLlmAn, MK124
 *
 */

public class RaceCheckpoint extends Vector3D implements IRaceCheckpoint
{
	private static final long serialVersionUID = 1L;
	
	public static final int TYPE_NORMAL =				0;
	public static final int TYPE_AIR =					3;
	public static final int TYPE_NOTHING =				2;

	private static final int TYPE_NORMAL_FINISH =		1;
	private static final int TYPE_AIR_FINISH =			4;

	
//---------------------------------------------------------
	
	EventDispatcher eventDispatcher = new EventDispatcher();
	
	float size;
	int type;
	RaceCheckpoint next;
	

	@Override public IEventDispatcher	getEventDispatcher()	{ return eventDispatcher; }
	
	@Override public float getSize()							{ return size; }
	@Override public int getType()							{ return type; }
	
	
	public RaceCheckpoint( float x, float y, float z, float size, int type, RaceCheckpoint next )
	{
		super( x, y, z );
		
		this.size = size;
		this.type = type;
	}

	public RaceCheckpoint( Point position, float size, int type, RaceCheckpoint next )
	{
		super( position.x, position.y, position.z );
		
		this.size = size;
		this.type = type;
	}
	
	
//---------------------------------------------------------
	
	@Override
	public void set( IPlayer p )
	{
		Player player = (Player) p;
		
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
	
	@Override
	public void disable( IPlayer p )
	{
		Player player = (Player) p;
		if(player.raceCheckpoint != this) return;

		NativeFunction.disablePlayerRaceCheckpoint( player.id );
		player.raceCheckpoint = null;
	}
	
	@Override
	public boolean isInCheckpoint( IPlayer p )
	{
		Player player = (Player) p;
		if( player.raceCheckpoint != this ) return false;
		
		return NativeFunction.isPlayerInRaceCheckpoint( player.id );
	}
	
	@Override
	public void update()
	{
		for( Player player : Gamemode.instance.playerPool )
		{
			if( player == null ) continue;
			
			if( player.raceCheckpoint == this ) set( player );
		}
	}
	
	@Override
	public Vector<IPlayer> getUsingPlayers()
	{
		Vector<IPlayer> players = new Vector<IPlayer>();
		
		Iterator<IPlayer> iterator = Player.get().iterator();
		while( iterator.hasNext() )
		{
			Player player = (Player) iterator.next();
			if( player.raceCheckpoint == this ) players.add( player );
		}
		
		return players;
	}
}
