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
import net.gtaun.shoebill.exception.CreationFailedException;
import net.gtaun.shoebill.samp.SampNativeFunction;

/**
 * @author MK124, JoJLlmAn
 *
 */

public class Menu implements IMenu
{
	static final int INVALID_ID =			0xFF;
	
	
	public static Collection<IMenu> get()
	{
		return Shoebill.getInstance().getManagedObjectPool().getMenus();
	}
	
	public static <T extends IMenu> Collection<T> get( Class<T> cls )
	{
		return Shoebill.getInstance().getManagedObjectPool().getMenus( cls );
	}
	

	private int id = INVALID_ID;
	private String title, columnHeader = "";
	private int columns;
	private float x, y;
	private float col1Width, col2Width;
	
	
	@Override public int getId()								{ return id; }
	@Override public String getTitle()							{ return title; }
	@Override public int getColumns()							{ return columns; }
	@Override public float getX()								{ return x; }
	@Override public float getY()								{ return y; }
	@Override public float getCol1Width()						{ return col1Width; }
	@Override public float getCol2Width()						{ return col2Width; }
	@Override public String getColumnHeader()					{ return columnHeader; }
	
	
	public Menu( String title, int columns, float x, float y, float col1Width, float col2Width ) throws CreationFailedException
	{
		if( title == null ) throw new NullPointerException();
		if( title.length() == 0 ) title = " ";
		
		this.title = title;
		this.columns = columns;
		this.x = x;
		this.y = y;
		this.col1Width = col1Width;
		this.col2Width = col2Width;
		
		initialize();
	}
	
	private void initialize() throws CreationFailedException
	{
		id = SampNativeFunction.createMenu( title, columns, x, y, col1Width, col1Width );
		if( id == -1 ) throw new CreationFailedException();
		
		SampObjectPool pool = (SampObjectPool) Shoebill.getInstance().getManagedObjectPool();
		pool.setMenu( id, this );
	}
	
	
	@Override
	public void destroy()
	{
		SampNativeFunction.destroyMenu( id );
		
		SampObjectPool pool = (SampObjectPool) Shoebill.getInstance().getManagedObjectPool();
		pool.setMenu( id, null );
		
		id = INVALID_ID;
	}
	
	@Override
	public boolean isDestroyed()
	{
		return id == INVALID_ID;
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
