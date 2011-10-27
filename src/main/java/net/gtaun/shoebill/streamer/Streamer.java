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

package net.gtaun.shoebill.streamer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import net.gtaun.lungfish.data.Point;
import net.gtaun.lungfish.event.player.PlayerConnectEvent;
import net.gtaun.lungfish.event.player.PlayerDisconnectEvent;
import net.gtaun.lungfish.event.player.PlayerUpdateEvent;
import net.gtaun.lungfish.object.IPlayer;
import net.gtaun.lungfish.util.event.Event;
import net.gtaun.lungfish.util.event.IEventListener;
import net.gtaun.shoebill.object.Gamemode;

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
	
	Gamemode gamemode;
	int range, blockSize, updateTick = 100;
	
	public int range()		{ return range; }
	
	
	public Streamer( Gamemode gamemode, int range )
	{
		gamemode.getEventDispatcher().addListener( PlayerConnectEvent.class, onPlayerConnect );
		gamemode.getEventDispatcher().addListener( PlayerDisconnectEvent.class, onPlayerDisconnect );
		
		this.range = range;
		blockSize = range/3*2;
	}
	
	IEventListener onPlayerConnect = new IEventListener()
	{
		public void handleEvent( Event e )
		{
			PlayerConnectEvent event = (PlayerConnectEvent) e;
			event.getPlayer().getEventDispatcher().addListener( PlayerUpdateEvent.class, onPlayerUpdate );
		}
	};
	
	IEventListener onPlayerDisconnect = new IEventListener()
	{
		public void handleEvent( Event e )
		{
			PlayerDisconnectEvent event = (PlayerDisconnectEvent) e;
			event.getPlayer().getEventDispatcher().removeListener( PlayerUpdateEvent.class, onPlayerUpdate );
		}
	};
	
	IEventListener onPlayerUpdate = new IEventListener()
	{
		public void handleEvent( Event e )
		{
			PlayerUpdateEvent event = (PlayerUpdateEvent) e;
			if( event.getPlayer().getUpdateTick() % updateTick == event.getPlayer().getId() % updateTick )
				update( event.getPlayer() );
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
	
	public void update( IPlayer player )
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
