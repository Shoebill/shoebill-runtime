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

import net.gtaun.shoebill.data.Point;
import net.gtaun.shoebill.samp.SampNativeFunction;

/**
 * @author JoJLlmAn, MK124
 *
 */

public class RaceCheckpoint extends Checkpoint
{
	private static final long serialVersionUID = 1L;
	
	public static final int TYPE_NORMAL =				0;
	public static final int TYPE_AIR =					3;
	public static final int TYPE_NOTHING =				2;

	private static final int TYPE_NORMAL_FINISH =		1;
	private static final int TYPE_AIR_FINISH =			4;

	
//---------------------------------------------------------
	
	int type;
	RaceCheckpoint next;
	
	public int getType()				{ return type; }
	public RaceCheckpoint getNext()			{ return next; }
	
	
	public RaceCheckpoint( float x, float y, float z, float size, int type, RaceCheckpoint next )
	{
		super( x, y, z, size );
		
		this.size = size;
		this.type = type;
	}

	public RaceCheckpoint( Point position, float size, int type, RaceCheckpoint next )
	{
		super( position.x, position.y, position.z, size );
		
		this.size = size;
		this.type = type;
	}
	
	
//---------------------------------------------------------
	
	public void set( Player player )
	{
		if( next != null )
			SampNativeFunction.setPlayerRaceCheckpoint( player.id, type, x, y, z, next.x, next.y, next.z, size );
		else
		{
			int type = this.type;
			
			if( type == TYPE_NORMAL )		type = TYPE_NORMAL_FINISH;
			else if( type == TYPE_AIR )		type = TYPE_AIR_FINISH;
			
			SampNativeFunction.setPlayerRaceCheckpoint( player.id, type, x, y, z, x, y, z, size );
		}
		
		player.raceCheckpoint = this;
	}
	
	public void disable( Player player )
	{
		if(player.raceCheckpoint != this) return;

		SampNativeFunction.disablePlayerRaceCheckpoint( player.id );
		player.raceCheckpoint = null;
	}
	
	public boolean isInCheckpoint( Player player )
	{
		if( player.raceCheckpoint != this ) return false;
		
		return SampNativeFunction.isPlayerInRaceCheckpoint( player.id );
	}
	
	public void update()
	{
		for( Player player : Player.get() )
		{
			if( player == null ) continue;			
			if( player.raceCheckpoint == this ) set( player );
		}
	}
	
	public Collection<Player> getUsingPlayers()
	{
		Collection<Player> players = new Vector<Player>();
		for( Player player : Player.get() )
		{
			if( player.raceCheckpoint == this ) players.add( player );
		}
		
		return players;
	}
}
