/**
 * Copyright (C) 2011-2012 MK124
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
import net.gtaun.shoebill.SampObjectStoreImpl;
import net.gtaun.shoebill.constant.TextDrawAlign;
import net.gtaun.shoebill.constant.TextDrawFont;
import net.gtaun.shoebill.data.Color;
import net.gtaun.shoebill.data.Vector2D;
import net.gtaun.shoebill.event.destroyable.DestroyEvent;
import net.gtaun.shoebill.exception.CreationFailedException;
import net.gtaun.shoebill.object.Player;
import net.gtaun.shoebill.object.Textdraw;
import net.gtaun.util.event.EventManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 * 
 * @author MK124, JoJLlmAn
 */
public abstract class TextdrawImpl implements Textdraw
{
	private final EventManager rootEventManager;
	
	private int id = INVALID_ID;
	private Vector2D position;
	private String text;
	
	private boolean[] isPlayerShowed = new boolean[SampObjectStoreImpl.MAX_PLAYERS];
	
	
	public TextdrawImpl(EventManager eventManager, float x, float y, String text) throws CreationFailedException
	{
		this.rootEventManager = eventManager;
		
		position = new Vector2D(x, y);
		if (StringUtils.isEmpty(text)) text = " ";
		
		id = SampNativeFunction.textDrawCreate(x, y, text);
		if (id == INVALID_ID) throw new CreationFailedException();
		
		for (int i = 0; i < isPlayerShowed.length; i++)
			isPlayerShowed[i] = false;
	}
	
	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
	
	@Override
	public void destroy()
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.textDrawDestroy(id);
		
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
	public Vector2D getPosition()
	{
		return position.clone();
	}
	
	@Override
	public String getText()
	{
		return text;
	}
	
	@Override
	public void setLetterSize(float x, float y)
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.textDrawLetterSize(id, x, y);
	}
	
	@Override
	public void setLetterSize(Vector2D vec)
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.textDrawLetterSize(id, vec.getX(), vec.getY());
	}
	
	@Override
	public void setTextSize(float x, float y)
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.textDrawTextSize(id, x, y);
	}
	
	@Override
	public void setTextSize(Vector2D vec)
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.textDrawTextSize(id, vec.getX(), vec.getY());
	}
	
	@Override
	public void setAlignment(TextDrawAlign alignment)
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.textDrawAlignment(id, alignment.getValue());
	}
	
	@Override
	public void setColor(Color color)
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.textDrawColor(id, color.getValue());
	}
	
	@Override
	public void setUseBox(boolean use)
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.textDrawUseBox(id, use);
	}
	
	@Override
	public void setBoxColor(Color color)
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.textDrawBoxColor(id, color.getValue());
	}
	
	@Override
	public void setShadowSize(int size)
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.textDrawSetShadow(id, size);
	}
	
	@Override
	public void setOutlineSize(int size)
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.textDrawSetOutline(id, size);
	}
	
	@Override
	public void setBackgroundColor(Color color)
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.textDrawBackgroundColor(id, color.getValue());
	}
	
	@Override
	public void setFont(TextDrawFont font)
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.textDrawFont(id, font.getValue());
	}
	
	@Override
	public void setProportional(boolean set)
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.textDrawSetProportional(id, set ? 1 : 0);
	}
	
	@Override
	public void setSelectable(boolean set)
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.textDrawSetSelectable(id, set ? 1 : 0);
	}
	
	@Override
	public void setText(String text)
	{
		if (isDestroyed()) return;
		
		if (text == null) throw new NullPointerException();
		
		this.text = text;
		SampNativeFunction.textDrawSetString(id, text);
	}
	
	@Override
	public void show(Player player)
	{
		if (isDestroyed()) return;
		if (player.isOnline() == false) return;
		
		int playerId = player.getId();
		
		SampNativeFunction.textDrawShowForPlayer(playerId, id);
		isPlayerShowed[playerId] = true;
	}
	
	@Override
	public void hide(Player player)
	{
		if (isDestroyed()) return;
		if (player.isOnline() == false) return;
		
		int playerId = player.getId();
		
		SampNativeFunction.textDrawHideForPlayer(playerId, id);
		isPlayerShowed[playerId] = false;
	}
	
	@Override
	public void showForAll()
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.textDrawShowForAll(id);
		for (int i = 0; i < isPlayerShowed.length; i++)
			isPlayerShowed[i] = true;
	}
	
	@Override
	public void hideForAll()
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.textDrawHideForAll(id);
		for (int i = 0; i < isPlayerShowed.length; i++)
			isPlayerShowed[i] = false;
	}
	
	@Override
	public boolean isShowed(Player player)
	{
		if (isDestroyed()) return false;
		
		return isPlayerShowed[player.getId()];
	}
}
