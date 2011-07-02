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

import net.gtaun.event.EventDispatcher;
import net.gtaun.event.IEventDispatcher;
import net.gtaun.samp.event.MenuExitedEvent;
import net.gtaun.samp.event.MenuSelectedEvent;

/**
 * @author MK124, JoJLlmAn
 *
 */

public class MenuBase
{
	public static <T> Vector<T> getMenus( Class<T> cls )
	{
		return GameModeBase.getInstances(GameModeBase.instance.menuPool, cls);
	}
	

	int id;
	String title, columnHeader = "";
	int columns;
	float x, y;
	float col1Width, col2Width;
	
	public int id()					{ return id; }
	public String title()			{ return title; }
	public int columns()			{ return columns; }
	public float x()				{ return x; }
	public float y()				{ return y; }
	public float col1Width()		{ return col1Width; }
	public float col2Width()		{ return col2Width; }
	public String columnHeader()	{ return columnHeader; }
	
	EventDispatcher<MenuSelectedEvent> 	eventMenuSelected = new EventDispatcher<MenuSelectedEvent>();
	EventDispatcher<MenuExitedEvent> 	eventMenuExited = new EventDispatcher<MenuExitedEvent>();
	
	public IEventDispatcher<MenuSelectedEvent>	eventMenuSelected()	{ return eventMenuSelected; }
	public IEventDispatcher<MenuExitedEvent>	eventMenuExited()	{ return eventMenuExited; }
	
	
	public MenuBase( String title, int columns, float x, float y, float col1Width, float col2Width )
	{
		this.title = title;
		this.columns = columns;
		this.x = x;
		this.y = y;
		this.col1Width = col1Width;
		this.col2Width = col2Width;
		
		init();
	}
	
	private void init()
	{
		id = NativeFunction.createMenu( title, columns, x, y, col1Width, col1Width );
		GameModeBase.instance.menuPool[id] = this;
	}
	
//---------------------------------------------------------
	
	protected int onPlayerSelectedMenuRow( PlayerBase player, int row )
	{
		return 1;
	}
	
	protected int onPlayerExitedMenu( PlayerBase player )
	{
		return 1;
	}
	
	
//---------------------------------------------------------
	
	public void destroy()
	{
		NativeFunction.destroyMenu( id );
		GameModeBase.instance.menuPool[ id ] = null;
	}
	
	public void addItem( int column, String text )
	{
		if( text == null ) throw new NullPointerException();
		NativeFunction.addMenuItem( id, column, text );
	}
	
	public void setColumnHeader( int column, String text )
	{
		if( text == null ) throw new NullPointerException();
		
		NativeFunction.setMenuColumnHeader( id, column, text );
		columnHeader = text;
	}
	
	public void disable()
	{
		NativeFunction.disableMenu( id );
	}
	
	public void disableRow( int row )
	{
		NativeFunction.disableMenuRow( id, row );
	}
	
	public void show( PlayerBase player )
	{
		NativeFunction.showMenuForPlayer( id, player.id );
	}
	
	public void hide( PlayerBase player )
	{
		NativeFunction.hideMenuForPlayer( id, player.id );
	}
}
