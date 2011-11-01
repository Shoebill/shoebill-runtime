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

package net.gtaun.shoebill.object;

import java.lang.ref.WeakReference;

import net.gtaun.lungfish.event.dialog.DialogCancelEvent;
import net.gtaun.lungfish.object.IDialog;
import net.gtaun.lungfish.object.IPlayer;
import net.gtaun.lungfish.util.event.EventDispatcher;
import net.gtaun.lungfish.util.event.IEventDispatcher;
import net.gtaun.shoebill.SampNativeFunction;

/**
 * @author MK124
 *
 */

public class Dialog implements IDialog
{
	public static final int STYLE_MSGBOX =		0;
	public static final int STYLE_INPUT =		1;
	public static final int STYLE_LIST =		2;

//----------------------------------------------------------
	
	private static int count = 0;
	
	
	EventDispatcher eventDispatcher = new EventDispatcher();
	
	int id, style;

	
	@Override public IEventDispatcher getEventDispatcher()		{ return getEventDispatcher(); }
	
	@Override public int getStyle()								{ return style; }
	

//---------------------------------------------------------
	
	public Dialog( int style )
	{
		this.style = style;
		init();
	}
	
	private void init()
	{
		id = count;
		count++;
		
		Gamemode.instance.dialogPool.put( id, new WeakReference<Dialog>(this) );
	}
	
	
//---------------------------------------------------------
	
	@Override
	public void show( IPlayer p, String caption, String text, String button1, String button2 )
	{
		Player player = (Player) p;
		
		if( caption == null || text == null || button1 == null || button2 == null ) throw new NullPointerException();
		cancel( player );
		
		player.dialog = this;
		SampNativeFunction.showPlayerDialog( player.id, id, style, caption, text, button1, button2 );
	}
	
	@Override
	public void cancel( IPlayer p )
	{
		Player player = (Player) p;
		
		if( player.dialog == null ) return;
		SampNativeFunction.showPlayerDialog( player.id, -1, 0, "", "", "", "" );
		
		player.dialog.eventDispatcher.dispatchEvent( new DialogCancelEvent(player.dialog, player) );
	}
}
