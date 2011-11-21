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
import net.gtaun.shoebill.samp.SampNativeFunction;
import net.gtaun.shoebill.util.event.EventDispatcher;
import net.gtaun.shoebill.util.event.IEventDispatcher;

/**
 * @author MK124, JoJLlmAn
 *
 */

public class Menu implements IDestroyable
{
	public static final int INVALID_ID =			0xFF;
	
	
	public static Collection<Menu> get()
	{
		return Shoebill.getInstance().getManagedObjectPool().getMenus();
	}
	
	public static <T extends Menu> Collection<T> get( Class<T> cls )
	{
		return Shoebill.getInstance().getManagedObjectPool().getMenus( cls );
	}
	

	EventDispatcher eventDispatcher = new EventDispatcher();
	
	int id = -1;
	String title, columnHeader = "";
	int columns;
	float x, y;
	float col1Width, col2Width;
	

	public IEventDispatcher getEventDispatcher()		{ return eventDispatcher; }
	
	public int getId()								{ return id; }
	public String getTitle()							{ return title; }
	public int getColumns()							{ return columns; }
	public float getX()								{ return x; }
	public float getY()								{ return y; }
	public float getCol1Width()						{ return col1Width; }
	public float getCol2Width()						{ return col2Width; }
	public String getColumnHeader()					{ return columnHeader; }
	
	
	public Menu( String title, int columns, float x, float y, float col1Width, float col2Width )
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
		id = SampNativeFunction.createMenu( title, columns, x, y, col1Width, col1Width );
		
		SampObjectPool pool = (SampObjectPool) Shoebill.getInstance().getManagedObjectPool();
		pool.setMenu( id, this );
	}
	
	
//---------------------------------------------------------
	
	@Override
	public void destroy()
	{
		SampNativeFunction.destroyMenu( id );
		
		SampObjectPool pool = (SampObjectPool) Shoebill.getInstance().getManagedObjectPool();
		pool.setMenu( id, null );
		
		id = -1;
	}
	
	@Override
	public boolean isDestroyed()
	{
		return id == -1;
	}
	
	public void addItem( int column, String text )
	{
		if( text == null ) throw new NullPointerException();
		SampNativeFunction.addMenuItem( id, column, text );
	}
	
	public void setColumnHeader( int column, String text )
	{
		if( text == null ) throw new NullPointerException();
		
		SampNativeFunction.setMenuColumnHeader( id, column, text );
		columnHeader = text;
	}
	
	public void disable()
	{
		SampNativeFunction.disableMenu( id );
	}
	
	public void disableRow( int row )
	{
		SampNativeFunction.disableMenuRow( id, row );
	}
	
	public void show( Player player )
	{
		SampNativeFunction.showMenuForPlayer( id, player.id );
	}
	
	public void hide( Player player )
	{
		SampNativeFunction.hideMenuForPlayer( id, player.id );
	}
}
