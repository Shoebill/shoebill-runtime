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

import java.lang.ref.WeakReference;

import net.gtaun.event.EventDispatcher;
import net.gtaun.event.IEventDispatcher;
import net.gtaun.samp.event.DialogCancelEvent;
import net.gtaun.samp.event.DialogResponseEvent;

/**
 * @author MK124
 *
 */

public class DialogBase
{
	public static final int STYLE_MSGBOX =		0;
	public static final int STYLE_INPUT =		1;
	public static final int STYLE_LIST =		2;

//----------------------------------------------------------
	
	private static int count = 0;
	
	
	int id, style;
	public String caption, info, button1, button2;
	
	public int style()		{ return style; }
	

	EventDispatcher<DialogResponseEvent>	eventResponse = new EventDispatcher<DialogResponseEvent>();
	EventDispatcher<DialogCancelEvent>		eventCancel = new EventDispatcher<DialogCancelEvent>();

	public IEventDispatcher<DialogResponseEvent>	eventResponse()		{ return eventResponse; }
	public IEventDispatcher<DialogCancelEvent>		eventCancel()		{ return eventCancel; }
	
	
	public DialogBase( int style )
	{
		this.style = style;
		init();
	}
	
	public DialogBase( int style, String caption, String info, String button1, String button2 )
	{
		this.style = style;
		this.caption = caption;
		this.info = info;
		this.button1 = button1;
		this.button2 = button2;
		
		init();
	}
	
	private void init()
	{
		id = count;
		count++;
		
		GameModeBase.instance.dialogPool.put( id, new WeakReference<DialogBase>(this) );
	}
	
	
//---------------------------------------------------------
	
	protected int onResponse( PlayerBase player, int response, int listitem, String inputtext )
	{
		return 1;
	}
	
	protected int onCancel( PlayerBase player )
	{
		return 1;
	}
	
	
//---------------------------------------------------------
	
	private void cancelPlayerDialog( PlayerBase player )
	{
		if( player.dialog == null ) return;

		player.dialog.onCancel( player );
		player.dialog.eventCancel.dispatchEvent( new DialogCancelEvent(player.dialog, player) );
	}

	public void show( PlayerBase player )
	{
		cancelPlayerDialog( player );
		
		player.dialog = this;
		NativeFunction.showPlayerDialog( player.id, id, style, caption, info, button1, button2 );
	}
	
	public void show( PlayerBase player, String info )
	{
		cancelPlayerDialog( player );
		
		player.dialog = this;
		NativeFunction.showPlayerDialog( player.id, id, style, caption, info, button1, button2 );
	}
	
	public void show( PlayerBase player, String caption, String info )
	{
		cancelPlayerDialog( player );
		
		player.dialog = this;
		NativeFunction.showPlayerDialog( player.id, id, style, caption, info, button1, button2 );
	}
	
	public void show( PlayerBase player, String caption, String info, String button1, String button2 )
	{
		cancelPlayerDialog( player );
		
		player.dialog = this;
		NativeFunction.showPlayerDialog( player.id, id, style, caption, info, button1, button2 );
	}
}
