/**
 * Copyright (C) 2011 MK124
 * Copyright (C) 2011 JoJLlmAn
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

package net.gtaun.shoebill;

import java.util.Vector;

import net.gtaun.lungfish.data.Area;

/**
 * @author MK124, JoJLlmAn
 *
 */

public class Zone
{
	public static Vector<Zone> get()
	{
		return Gamemode.getInstances(Gamemode.instance.zonePool, Zone.class);
	}
	
	public static <T> Vector<T> get( Class<T> cls )
	{
		return Gamemode.getInstances(Gamemode.instance.zonePool, cls);
	}
	
	
	private boolean[] isPlayerShowed = new boolean[Gamemode.MAX_PLAYERS];
	private boolean[] isPlayerFlashing = new boolean[Gamemode.MAX_PLAYERS];
	
	
	int id;
	Area area;
	
	public int id()				{return this.id;}
	public Area area()			{return this.area;}


	public Zone( float minx, float miny, float maxx, float maxy )
	{
		this.area = new Area( minx, miny, maxx, maxy );
		init();
	}
	
	public Zone( Area area )
	{
		this.area = area;
		init();
	}
	
	private void init()
	{
		id = NativeFunction.gangZoneCreate( area.minX, area.minY, area.maxX, area.maxY );
		
		for(int i=0; i<Gamemode.MAX_PLAYERS; i++)
		{
			isPlayerShowed[i] = false;
			isPlayerFlashing[i] = false;
		}
		
		Gamemode.instance.zonePool[id] = this;
	}


//---------------------------------------------------------
	
	public void destroy()
	{
		NativeFunction.gangZoneDestroy( id );
		Gamemode.instance.zonePool[id] = null;
	}
	
	
	public void show( Player player, int color )
	{
		NativeFunction.gangZoneShowForPlayer( player.id, id, color );
		isPlayerShowed[player.id] = true;
		isPlayerFlashing[player.id] = false;
	}
	
	public void hide( Player player )
	{
		NativeFunction.gangZoneHideForPlayer( player.id, id );
		
		isPlayerShowed[player.id] = false;
		isPlayerFlashing[player.id] = false;
	}
	
	public void flash( Player player, int color )
	{
		if( isPlayerShowed[player.id] ){
			NativeFunction.gangZoneFlashForPlayer( player.id, id, color );
			isPlayerFlashing[player.id] = true;
		}
	}
	
	public void stopFlash( Player player )
	{
		NativeFunction.gangZoneStopFlashForPlayer( player.id, id );
		isPlayerFlashing[player.id] = false;
	}
	

	public void showForAll( int color )
	{
		NativeFunction.gangZoneShowForAll( id, color );
		for( int i=0; i<Gamemode.MAX_PLAYERS; i++ ) isPlayerShowed[i] = true;
	}
	
	public void hideForAll()
	{
		NativeFunction.gangZoneHideForAll( id );
		
		for( int i=0; i<Gamemode.MAX_PLAYERS; i++ )
		{
			isPlayerShowed[i] = false;
			isPlayerFlashing[i] = false;
		}
	}
	
	public void flashForAll( int color )
	{
		NativeFunction.gangZoneFlashForAll( id, color );
		for( int i=0; i<Gamemode.MAX_PLAYERS; i++ ) if( isPlayerShowed[i] ) isPlayerFlashing[i] = true;
	}

	public void stopFlashForAll()
	{
		NativeFunction.gangZoneStopFlashForAll( id );
		for( int i=0; i<Gamemode.MAX_PLAYERS; i++ ) isPlayerFlashing[i] = false;
	}
}
