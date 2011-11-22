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

public class Menu implements IMenu
{
	public static final int INVALID_ID =			0xFF;
	
	
	public static Collection<IMenu> get()
	{
		return Shoebill.getInstance().getManagedObjectPool().getMenus();
	}
	
	public static <T extends IMenu> Collection<T> get( Class<T> cls )
	{
		return Shoebill.getInstance().getManagedObjectPool().getMenus( cls );
	}
	

	EventDispatcher eventDispatcher = new EventDispatcher();
	
	int id = -1;
	String title, columnHeader = "";
	int columns;
	float x, y;
	float col1Width, col2Width;
	

	@Override public IEventDispatcher getEventDispatcher()		{ return eventDispatcher; }
	
	@Override public int getId()								{ return id; }
	@Override public String getTitle()							{ return title; }
	@Override public int getColumns()							{ return columns; }
	@Override public float getX()								{ return x; }
	@Override public float getY()								{ return y; }
	@Override public float getCol1Width()						{ return col1Width; }
	@Override public float getCol2Width()						{ return col2Width; }
	@Override public String getColumnHeader()					{ return columnHeader; }
	
	
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
	
	@Override
	public void addItem( int column, String text )
	{
		if( text == null ) throw new NullPointerException();
		SampNativeFunction.addMenuItem( id, column, text );
	}
	
	@Override
	public void setColumnHeader( int column, String text )
	{
		if( text == null ) throw new NullPointerException();
		
		SampNativeFunction.setMenuColumnHeader( id, column, text );
		columnHeader = text;
	}
	
	@Override
	public void disable()
	{
		SampNativeFunction.disableMenu( id );
	}
	
	@Override
	public void disableRow( int row )
	{
		SampNativeFunction.disableMenuRow( id, row );
	}
	
	@Override
	public void show( IPlayer player )
	{
		SampNativeFunction.showMenuForPlayer( id, player.getId() );
	}
	
	@Override
	public void hide( IPlayer player )
	{
		SampNativeFunction.hideMenuForPlayer( id, player.getId() );
	}
}
