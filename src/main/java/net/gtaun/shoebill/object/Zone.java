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

package net.gtaun.shoebill.object;

import java.util.Collection;

import net.gtaun.shoebill.SampObjectPool;
import net.gtaun.shoebill.Shoebill;
import net.gtaun.shoebill.data.Area;
import net.gtaun.shoebill.data.Color;
import net.gtaun.shoebill.samp.SampNativeFunction;

/**
 * @author MK124, JoJLlmAn
 *
 */

public class Zone implements IDestroyable
{
	public static final int INVALID_ID =		-1;
	
	
	public static Collection<Zone> get()
	{
		return Shoebill.getInstance().getManagedObjectPool().getZones();
	}
	
	public static <T extends Zone> Collection<T> get( Class<T> cls )
	{
		return Shoebill.getInstance().getManagedObjectPool().getZones( cls );
	}
	
	
	private boolean[] isPlayerShowed = new boolean[SampObjectPool.MAX_PLAYERS];
	private boolean[] isPlayerFlashing = new boolean[SampObjectPool.MAX_PLAYERS];
	
	
	int id = -1;
	Area area;
	
	
	public int getId()				{ return id; }
	public Area getArea()			{ return area.clone(); }


	public Zone( float minX, float minY, float maxX, float maxY )
	{
		this.area = new Area( minX, minY, maxX, maxY );
		init();
	}
	
	public Zone( Area area )
	{
		this.area = area.clone();
		init();
	}
	
	private void init()
	{
		id = SampNativeFunction.gangZoneCreate( area.minX, area.minY, area.maxX, area.maxY );
		
		for( int i=0; i<isPlayerShowed.length; i++ )
		{
			isPlayerShowed[i] = false;
			isPlayerFlashing[i] = false;
		}
		
		SampObjectPool pool = (SampObjectPool) Shoebill.getInstance().getManagedObjectPool();
		pool.setZone( id, this );
	}


//---------------------------------------------------------
	
	@Override
	public void destroy()
	{
		SampNativeFunction.gangZoneDestroy( id );

		SampObjectPool pool = (SampObjectPool) Shoebill.getInstance().getManagedObjectPool();
		pool.setZone( id, null );
		
		id = -1;
	}
	
	@Override
	public boolean isDestroyed()
	{
		return id == -1;
	}
	

	public void show( Player player, int color )
	{
		SampNativeFunction.gangZoneShowForPlayer( player.id, id, color );
		isPlayerShowed[player.id] = true;
		isPlayerFlashing[player.id] = false;
	}

	public void hide( Player player )
	{
		SampNativeFunction.gangZoneHideForPlayer( player.id, id );
		
		isPlayerShowed[player.id] = false;
		isPlayerFlashing[player.id] = false;
	}

	public void flash( Player player, int color )
	{
		if( isPlayerShowed[player.id] ){
			SampNativeFunction.gangZoneFlashForPlayer( player.id, id, color );
			isPlayerFlashing[player.id] = true;
		}
	}

	public void stopFlash( Player player )
	{
		SampNativeFunction.gangZoneStopFlashForPlayer( player.id, id );
		isPlayerFlashing[player.id] = false;
	}
	

	public void showForAll( Color color )
	{
		SampNativeFunction.gangZoneShowForAll( id, color.getValue() );
		for( int i=0; i<isPlayerShowed.length; i++ ) isPlayerShowed[i] = true;
	}

	public void hideForAll()
	{
		SampNativeFunction.gangZoneHideForAll( id );
		
		for( int i=0; i<isPlayerShowed.length; i++ )
		{
			isPlayerShowed[i] = false;
			isPlayerFlashing[i] = false;
		}
	}

	public void flashForAll( Color color )
	{
		SampNativeFunction.gangZoneFlashForAll( id, color.getValue() );
		for( int i=0; i<isPlayerShowed.length; i++ ) if( isPlayerShowed[i] ) isPlayerFlashing[i] = true;
	}
	
	public void stopFlashForAll()
	{
		SampNativeFunction.gangZoneStopFlashForAll( id );
		for( int i=0; i<isPlayerFlashing.length; i++ ) isPlayerFlashing[i] = false;
	}
}
