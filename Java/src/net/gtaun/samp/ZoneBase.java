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
	public static <T> Vector<T> get( Class<T> cls )
	{
		return GameModeBase.getInstances( GameModeBase.instance.zonePool, cls );
	}
	
	private boolean[] showedPlayer = new boolean[500];
	private boolean[] flashedPlayer = new boolean[500];
	
	public int id;
	public int color;
	public int flashcolor;
	public Area area;
	
	public int id(){return this.id;}
	public int color(){return this.color;}
	public int flashcolor(){return this.flashcolor;}
	public Area area(){return this.area;}


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
		
		for(int i=0;i<500;i++){
			showedPlayer[i] = false;
			flashedPlayer[i] = false;
		}
	}
	
	//---------------------------------
	
	public void destroy()
	{
		NativeFunction.gangZoneDestroy( id );
		GameModeBase.instance.zonePool[ id ] = null;
	}
	
	public void show()
	{
		NativeFunction.gangZoneShowForAll(id, color);
		
		for(int i=0;i<500;i++)
			showedPlayer[i] = true;
	}
	
	public void show(PlayerBase player)
	{
		if(!showedPlayer[player.id]){
			NativeFunction.gangZoneShowForPlayer(player.id, id, color);
			showedPlayer[player.id] = true;
		}
	}
	
	public void hide()
	{
		NativeFunction.gangZoneHideForAll(id);
		
		for(int i=0;i<500;i++){
			showedPlayer[i] = false;
			flashedPlayer[i] = false;
		}
	}
	
	public void hide(PlayerBase player)
	{
		NativeFunction.gangZoneHideForPlayer(player.id, id);
		showedPlayer[player.id] = false;
		flashedPlayer[player.id] = false;
	}
	
	public void flash()
	{
		NativeFunction.gangZoneFlashForAll(id, flashcolor);
		for(int i=0;i<500;i++)
		{
			if(showedPlayer[i])
				flashedPlayer[i] = true;
		}
	}
	
	public void flash(PlayerBase player)
	{
		if(showedPlayer[player.id])
		{
			NativeFunction.gangZoneFlashForPlayer(player.id, id, flashcolor);
			flashedPlayer[player.id] = true;
		}
	}
	
	public void stopFlash()
	{
		NativeFunction.gangZoneStopFlashForAll(id);
		for(int i=0;i<500;i++)
			flashedPlayer[i] = false;
	}
	
	public void stopFlash(PlayerBase player)
	{
		NativeFunction.gangZoneStopFlashForPlayer(player.id, id);
		flashedPlayer[player.id] = false;
	}
	
	public void setColor(int color)
	{
		this.color = color;
		
		for(int i=0;i<500;i++)
		{
			if(showedPlayer[i])
			{
				NativeFunction.gangZoneHideForPlayer(i, id);
				NativeFunction.gangZoneShowForPlayer(i, id, color);
				if(flashedPlayer[i])
					NativeFunction.gangZoneFlashForPlayer(i, id, flashcolor);
			}
		}
	}
	
	public void setFlashColor(int flashcolor)
	{
		this.flashcolor = flashcolor;
		for(int i=0;i<500;i++)
		{
			if(flashedPlayer[i])
				NativeFunction.gangZoneFlashForPlayer(i, id, flashcolor);
		}
	}
}
