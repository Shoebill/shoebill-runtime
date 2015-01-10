/**
 * Copyright (C) 2011-2014 MK124
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

import net.gtaun.shoebill.constant.DialogStyle;
import net.gtaun.shoebill.event.destroyable.DestroyEvent;
import net.gtaun.shoebill.object.DialogId;
import net.gtaun.shoebill.object.Player;
import net.gtaun.util.event.EventManager;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *
 *
 * @author MK124
 */
public class DialogIdImpl implements DialogId
{
	static final int INVALID_ID = -1;


	private final EventManager rootEventManager;
	private int id;

	private OnResponseHandler onResponseHandler;
	private OnShowHandler onShowHandler;
	private OnCloseHandler onCloseHandler;


	public DialogIdImpl(EventManager eventManager, int id, OnResponseHandler onResponse, OnShowHandler onShow, OnCloseHandler onClose)
	{
		this.rootEventManager = eventManager;
		this.id = id;
		this.onResponseHandler = onResponse;
		this.onShowHandler = onShow;
		this.onCloseHandler = onClose;
	}

	@Override
	protected void finalize() throws Throwable
	{
		destroy();
	}

	@Override
	public void destroy()
	{
		if (id == INVALID_ID) return;

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
	public String toString()
	{
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("id", id).toString();
	}

	@Override
	public int getId()
	{
		return id;
	}

	public OnResponseHandler getOnResponseHandler()
	{
		return onResponseHandler;
	}

	public OnShowHandler getOnShowHandler()
	{
		return onShowHandler;
	}

	public OnCloseHandler getOnCloseHandler()
	{
		return onCloseHandler;
	}

	@Override
	public void show(Player player, DialogStyle style, String caption, String text, String button1, String button2)
	{
		player.showDialog(this, style, caption, text, button1, button2);
	}

	@Override
	public void cancel(Player player)
	{
		if (player.getDialog() != this) return;
		player.cancelDialog();
	}
}
