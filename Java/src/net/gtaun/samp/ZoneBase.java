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

package net.gtaun.samp;

import java.util.Vector;

import net.gtaun.samp.data.Area;

/**
 * @author MK124, JoJLlmAn
 *
 */

public class ZoneBase
{
	public static <T> Vector<T> getZones( Class<T> cls )
	{
		return GameModeBase.getInstances(GameModeBase.instance.zonePool, cls);
	}
	
	
	private boolean[] isPlayerShowed = new boolean[GameModeBase.MAX_PLAYERS];
	private boolean[] isPlayerFlashing = new boolean[GameModeBase.MAX_PLAYERS];
	
	
	int id;
	Area area;
	
	public int id()				{return this.id;}
	public Area area()			{return this.area;}


	public ZoneBase( float minx, float miny, float maxx, float maxy )
	{
		this.area = new Area( minx, miny, maxx, maxy );
		init();
	}
	
	public ZoneBase( Area area )
	{
		this.area = area;
		init();
	}
	
	private void init()
	{
		id = NativeFunction.gangZoneCreate( area.minX, area.minY, area.maxX, area.maxY );
		
		for(int i=0; i<GameModeBase.MAX_PLAYERS; i++)
		{
			isPlayerShowed[i] = false;
			isPlayerFlashing[i] = false;
		}
		
		GameModeBase.instance.zonePool[id] = this;
	}


//---------------------------------------------------------
	
	public void destroy()
	{
		NativeFunction.gangZoneDestroy( id );
		GameModeBase.instance.zonePool[id] = null;
	}
	
	
	public void show( PlayerBase player, int color )
	{
		NativeFunction.gangZoneShowForPlayer( player.id, id, color );
		isPlayerShowed[player.id] = true;
		isPlayerFlashing[player.id] = false;
	}
	
	public void hide( PlayerBase player )
	{
		NativeFunction.gangZoneHideForPlayer( player.id, id );
		
		isPlayerShowed[player.id] = false;
		isPlayerFlashing[player.id] = false;
	}
	
	public void flash( PlayerBase player, int color )
	{
		if( isPlayerShowed[player.id] ){
			NativeFunction.gangZoneFlashForPlayer( player.id, id, color );
			isPlayerFlashing[player.id] = true;
		}
	}
	
	public void stopFlash( PlayerBase player )
	{
		NativeFunction.gangZoneStopFlashForPlayer( player.id, id );
		isPlayerFlashing[player.id] = false;
	}
	

	public void showForAll( int color )
	{
		NativeFunction.gangZoneShowForAll( id, color );
		for( int i=0; i<GameModeBase.MAX_PLAYERS; i++ ) isPlayerShowed[i] = true;
	}
	
	public void hideForAll()
	{
		NativeFunction.gangZoneHideForAll( id );
		
		for( int i=0; i<GameModeBase.MAX_PLAYERS; i++ )
		{
			isPlayerShowed[i] = false;
			isPlayerFlashing[i] = false;
		}
	}
	
	public void flashForAll( int color )
	{
		NativeFunction.gangZoneFlashForAll( id, color );
		for( int i=0; i<GameModeBase.MAX_PLAYERS; i++ ) if( isPlayerShowed[i] ) isPlayerFlashing[i] = true;
	}

	public void stopFlashForAll()
	{
		NativeFunction.gangZoneStopFlashForAll( id );
		for( int i=0; i<GameModeBase.MAX_PLAYERS; i++ ) isPlayerFlashing[i] = false;
	}
}
