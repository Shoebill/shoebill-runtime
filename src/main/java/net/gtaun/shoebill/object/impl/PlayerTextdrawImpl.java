/**
 * Copyright (C) 2012 MK124
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

import net.gtaun.shoebill.ShoebillImpl;
import net.gtaun.shoebill.constant.TextDrawAlign;
import net.gtaun.shoebill.constant.TextDrawFont;
import net.gtaun.shoebill.data.Color;
import net.gtaun.shoebill.data.Vector2D;
import net.gtaun.shoebill.event.destroyable.DestroyEvent;
import net.gtaun.shoebill.exception.CreationFailedException;
import net.gtaun.shoebill.object.Player;
import net.gtaun.shoebill.object.PlayerTextdraw;
import net.gtaun.shoebill.samp.SampNativeFunction;
import net.gtaun.util.event.EventManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 * 
 * @author MK124
 */
public abstract class PlayerTextdrawImpl implements PlayerTextdraw
{
	private Player player;
	private int id = INVALID_ID;
	private Vector2D position;
	private String text;
	
	private boolean isShowed = false;
	
	
	public PlayerTextdrawImpl(Player player, float x, float y, String text) throws CreationFailedException
	{
		this.player = player;
		
		position = new Vector2D(x, y);
		if (StringUtils.isEmpty(text)) text = " ";
		
		id = SampNativeFunction.createPlayerTextDraw(player.getId(), x, y, text);
		if (id == INVALID_ID) throw new CreationFailedException();
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
		
		SampNativeFunction.playerTextDrawDestroy(player.getId(), id);
		
		EventManager eventManager = ShoebillImpl.getInstance().getEventManager();
		DestroyEvent destroyEvent = new DestroyEvent(this);
		eventManager.dispatchEvent(destroyEvent, this);
		
		id = INVALID_ID;
	}
	
	@Override
	public boolean isDestroyed()
	{
		if( player.isOnline() == false ) id = INVALID_ID;
		return id == INVALID_ID;
	}
	
	@Override
	public Player getPlayer()
	{
		return player;
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
		
		SampNativeFunction.playerTextDrawLetterSize(player.getId(), id, x, y);
	}
	
	@Override
	public void setLetterSize(Vector2D vec)
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.playerTextDrawLetterSize(player.getId(), id, vec.getX(), vec.getY());
	}
	
	@Override
	public void setTextSize(float x, float y)
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.playerTextDrawTextSize(player.getId(), id, x, y);
	}
	
	@Override
	public void setTextSize(Vector2D vec)
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.playerTextDrawTextSize(player.getId(), id, vec.getX(), vec.getY());
	}
	
	@Override
	public void setAlignment(TextDrawAlign alignment)
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.playerTextDrawAlignment(player.getId(), id, alignment.getValue());
	}
	
	@Override
	public void setColor(Color color)
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.playerTextDrawColor(player.getId(), id, color.getValue());
	}
	
	@Override
	public void setUseBox(boolean use)
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.playerTextDrawUseBox(player.getId(), id, use ? 1 : 0);
	}
	
	@Override
	public void setBoxColor(Color color)
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.playerTextDrawBoxColor(player.getId(), id, color.getValue());
	}
	
	@Override
	public void setShadowSize(int size)
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.playerTextDrawSetShadow(player.getId(), id, size);
	}
	
	@Override
	public void setOutlineSize(int size)
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.playerTextDrawSetOutline(player.getId(), id, size);
	}
	
	@Override
	public void setBackgroundColor(Color color)
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.playerTextDrawBackgroundColor(player.getId(), id, color.getValue());
	}
	
	@Override
	public void setFont(TextDrawFont font)
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.playerTextDrawFont(player.getId(), id, font.getValue());
	}
	
	@Override
	public void setProportional(boolean set)
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.playerTextDrawSetProportional(player.getId(), id, set ? 1 : 0);
	}
	
	@Override
	public void setSelectable(boolean set)
	{
		if (isDestroyed()) return;
		
		SampNativeFunction.playerTextDrawSetSelectable(player.getId(), id, set ? 1 : 0);
	}
	
	@Override
	public void setText(String text)
	{
		if (isDestroyed()) return;
		
		if (text == null) throw new NullPointerException();
		
		this.text = text;
		SampNativeFunction.playerTextDrawSetString(player.getId(), id, text);
	}
	
	@Override
	public void show()
	{
		if (isDestroyed()) return;
		
		int playerId = player.getId();
		
		SampNativeFunction.playerTextDrawShow(playerId, id);
		isShowed = true;
	}
	
	@Override
	public void hide()
	{
		if (isDestroyed()) return;
		
		int playerId = player.getId();
		
		SampNativeFunction.playerTextDrawHide(playerId, id);
		isShowed = false;
	}
	
	@Override
	public boolean isShowed()
	{
		return isShowed;
	}
}
