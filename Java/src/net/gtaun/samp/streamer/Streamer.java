/**
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

package net.gtaun.samp.streamer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import net.gtaun.event.IEventListener;
import net.gtaun.samp.GameModeBase;
import net.gtaun.samp.PlayerBase;
import net.gtaun.samp.data.Point;
import net.gtaun.samp.event.PlayerConnectEvent;
import net.gtaun.samp.event.PlayerDisconnectEvent;
import net.gtaun.samp.event.PlayerUpdateEvent;

/**
 * @author MK124
 *
 */

class BlockArea
{
	short x, y, z;
	int interiorId, worldId;
	
	
	BlockArea( Point point, int size )
	{
		x = point.x >= 0 ? (short)(point.x/size) : (short)(point.x/size-1);
		y = point.y >= 0 ? (short)(point.y/size) : (short)(point.y/size-1);
		z = point.z >= 0 ? (short)(point.z/size) : (short)(point.z/size-1);
		interiorId = point.interior;
		worldId = point.world;
	}
	
	public boolean equals( Object object )
	{
		if( object == this )					return true;
		if( !(object instanceof BlockArea) )	return false;
		
		BlockArea area = (BlockArea) object;
		if( x != area.x )						return false;
		if( y != area.y )						return false;
		if( z != area.z )						return false;
		if( interiorId != area.interiorId )		return false;
		if( worldId != area.worldId )			return false;
		
		return true;
	}
	
	public int hashCode()
	{
		return ((((x+124)^(y-258)^z-124)^23346)<<16) ^ (((x+258)^(y-124)^z+258)^(worldId+12421)^interiorId);
	}
}


public class Streamer<O extends IStreamObject>
{
	protected Map<BlockArea, Vector<O>> blocks = new HashMap<BlockArea, Vector<O>>();
	
	GameModeBase gamemode;
	int range, blockSize, updateFrameTick = 100;
	
	public int range()		{ return range; }
	
	
	public Streamer( GameModeBase gamemode, int range )
	{
		gamemode.eventConnect().addListener( onPlayerConnect );
		gamemode.eventDisconnect().addListener( onPlayerDisconnect );
		
		this.range = range;
		blockSize = range/3*2;
	}
	
	IEventListener<PlayerConnectEvent> onPlayerConnect = new IEventListener<PlayerConnectEvent>()
	{
		public void handleEvent( PlayerConnectEvent event )
		{
			event.player.eventUpdate().addListener( onPlayerUpdate );
		}
	};
	
	IEventListener<PlayerDisconnectEvent> onPlayerDisconnect = new IEventListener<PlayerDisconnectEvent>()
	{
		public void handleEvent( PlayerDisconnectEvent event )
		{
			event.player.eventUpdate().removeListener( onPlayerUpdate );
		}
	};
	
	IEventListener<PlayerUpdateEvent> onPlayerUpdate = new IEventListener<PlayerUpdateEvent>()
	{
		public void handleEvent( PlayerUpdateEvent event )
		{
			if( event.player.frame() % updateFrameTick == event.player.id() % updateFrameTick )
				update( event.player );
		}
	};
	
	public void add( O object )
	{
		BlockArea area = new BlockArea(object.getPosition(), blockSize);
		Vector<O> vector = blocks.get(area);
		
		if( vector == null )
		{
			vector = new Vector<O>();
			blocks.put( area, vector );
		}
		
		vector.add( object );
	}
	
	public void remove( O object )
	{
		BlockArea area = new BlockArea(object.getPosition(), blockSize);
		Vector<O> vector = blocks.get( area );
		
		if( vector == null ) return;
		int idx = vector.indexOf(object), dst = vector.size()-1;
		if( dst >= 0 )
		{
			vector.set( idx, vector.get(vector.size()-1) );
			vector.remove(idx);
		}
		else blocks.remove( area );
	}
	
	public void update( PlayerBase player )
	{
		
	}
	
	@SuppressWarnings("unused")
	private void updateBlock( Vector<O> block )
	{
		
	}
	
	@Deprecated
	public <T extends O> Vector<T> get ( Class<T> cls )
	{
		Vector<T> list = new Vector<T>();
		
		Iterator<Vector<O>> iterBlocks =  blocks.values().iterator();
		while( iterBlocks.hasNext() )
		{
			Iterator<O> iterObject = iterBlocks.next().iterator();
			while( iterObject.hasNext() )
			{
				O object = iterObject.next();
				if( cls.isInstance(object) ) list.add( cls.cast(object) );
			}
		}
		
		return list;		
	}
}
