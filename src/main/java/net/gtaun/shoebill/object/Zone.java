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

public class Zone implements IZone
{
	public static final int INVALID_ID =		-1;
	
	
	public static Collection<IZone> get()
	{
		return Shoebill.getInstance().getManagedObjectPool().getZones();
	}
	
	public static <T extends IZone> Collection<T> get( Class<T> cls )
	{
		return Shoebill.getInstance().getManagedObjectPool().getZones( cls );
	}
	
	
	private boolean[] isPlayerShowed = new boolean[SampObjectPool.MAX_PLAYERS];
	private boolean[] isPlayerFlashing = new boolean[SampObjectPool.MAX_PLAYERS];
	
	
	int id = -1;
	Area area;
	
	
	@Override public int getId()				{ return id; }
	@Override public Area getArea()				{ return area.clone(); }


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
	

	@Override
	public void show( IPlayer player, int color )
	{
		int playerId = player.getId();
		
		SampNativeFunction.gangZoneShowForPlayer( playerId, id, color );
		isPlayerShowed[playerId] = true;
		isPlayerFlashing[playerId] = false;
	}

	@Override
	public void hide( IPlayer player )
	{
		int playerId = player.getId();
		SampNativeFunction.gangZoneHideForPlayer( playerId, id );
		
		isPlayerShowed[playerId] = false;
		isPlayerFlashing[playerId] = false;
	}

	@Override
	public void flash( IPlayer player, int color )
	{
		int playerId = player.getId();
		
		if( isPlayerShowed[playerId] )
		{
			SampNativeFunction.gangZoneFlashForPlayer( playerId, id, color );
			isPlayerFlashing[playerId] = true;
		}
	}

	@Override
	public void stopFlash( IPlayer player )
	{
		int playerId = player.getId();
		
		SampNativeFunction.gangZoneStopFlashForPlayer( playerId, id );
		isPlayerFlashing[playerId] = false;
	}
	

	@Override
	public void showForAll( Color color )
	{
		SampNativeFunction.gangZoneShowForAll( id, color.getValue() );
		for( int i=0; i<isPlayerShowed.length; i++ ) isPlayerShowed[i] = true;
	}

	@Override
	public void hideForAll()
	{
		SampNativeFunction.gangZoneHideForAll( id );
		
		for( int i=0; i<isPlayerShowed.length; i++ )
		{
			isPlayerShowed[i] = false;
			isPlayerFlashing[i] = false;
		}
	}

	@Override
	public void flashForAll( Color color )
	{
		SampNativeFunction.gangZoneFlashForAll( id, color.getValue() );
		for( int i=0; i<isPlayerShowed.length; i++ ) if( isPlayerShowed[i] ) isPlayerFlashing[i] = true;
	}
	
	@Override
	public void stopFlashForAll()
	{
		SampNativeFunction.gangZoneStopFlashForAll( id );
		for( int i=0; i<isPlayerFlashing.length; i++ ) isPlayerFlashing[i] = false;
	}
}
