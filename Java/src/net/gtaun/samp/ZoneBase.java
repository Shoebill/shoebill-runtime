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

package net.gtaun.samp;

import java.util.Vector;

import net.gtaun.samp.data.Area;

/**
 * @author MK124
 *
 */

public class ZoneBase
{
	public static <T> Vector<T> get( Class<T> cls )
	{
		return GameModeBase.getInstances( GameModeBase.instance.zonePool, cls );
	}
	
	
	public int id;
	public Area area;


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
	}
	
	public void destroy()
	{
		NativeFunction.gangZoneDestroy( id );
		GameModeBase.instance.zonePool[ id ] = null;
	}
}
