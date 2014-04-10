/**
 * Copyright (C) 2011-2014 MK124
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

package net.gtaun.shoebill.object.impl;

import net.gtaun.shoebill.SampNativeFunction;
import net.gtaun.shoebill.data.Vector2D;
import net.gtaun.shoebill.event.destroyable.DestroyEvent;
import net.gtaun.shoebill.exception.CreationFailedException;
import net.gtaun.shoebill.object.Menu;
import net.gtaun.shoebill.object.Player;
import net.gtaun.util.event.EventManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 * 
 * @author MK124, JoJLlmAn
 */
public class MenuImpl implements Menu
{
	private final EventManager rootEventManager;
	
	private int id = INVALID_ID;
	private String title, columnHeader = "";
	private int columns;
	private Vector2D position;
	private float col1Width, col2Width;
	
	
	public MenuImpl(EventManager eventManager, String title, int columns, float x, float y, float col1Width, float col2Width) throws CreationFailedException
	{
		if (StringUtils.isEmpty(title)) title = " ";
		
		this.rootEventManager = eventManager;
		
		this.title = title;
		this.columns = columns;
		this.position = new Vector2D(x, y);
		this.col1Width = col1Width;
		this.col2Width = col2Width;
		
		id = SampNativeFunction.createMenu(title, columns, position.getX(), position.getY(), col1Width, col1Width);
		if (id == -1) throw new CreationFailedException();
	}
	
	@Override
	public String toString()
	{
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("id", id).toString();
	}
	
	@Override
	public void destroy()
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.destroyMenu(id);
		
		DestroyEvent destroyEvent = new DestroyEvent(this);
		rootEventManager.dispatchEvent(destroyEvent, this);
		
		id = INVALID_ID;
	}
	
	@Override
	public boolean isDestroyed()
	{
		return id == INVALID_ID;
	}
	
	@Override
	public int getId()
	{
		return id;
	}
	
	@Override
	public String getTitle()
	{
		return title;
	}
	
	@Override
	public int getColumns()
	{
		return columns;
	}
	
	@Override
	public Vector2D getPosition()
	{
		return position.clone();
	}
	
	@Override
	public float getColumn1Width()
	{
		return col1Width;
	}
	
	@Override
	public float getColumn2Width()
	{
		return col2Width;
	}
	
	@Override
	public String getColumnHeader()
	{
		return columnHeader;
	}
	
	@Override
	public void addItem(int column, String text)
	{
		if (isDestroyed()) return;
		
		if (text == null) throw new NullPointerException();
		SampNativeFunction.addMenuItem(id, column, text);
	}
	
	@Override
	public void setColumnHeader(int column, String text)
	{
		if (isDestroyed()) return;
		if (text == null) throw new NullPointerException();
		
		SampNativeFunction.setMenuColumnHeader(id, column, text);
		columnHeader = text;
	}
	
	@Override
	public void disable()
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.disableMenu(id);
	}
	
	@Override
	public void disableRow(int row)
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.disableMenuRow(id, row);
	}
	
	@Override
	public void show(Player player)
	{
		if (isDestroyed()) return;
		if (player.isOnline() == false) return;
		
		SampNativeFunction.showMenuForPlayer(id, player.getId());
	}
	
	@Override
	public void hide(Player player)
	{
		if (isDestroyed()) return;
		if (player.isOnline() == false) return;
		
		SampNativeFunction.hideMenuForPlayer(id, player.getId());
	}
}
