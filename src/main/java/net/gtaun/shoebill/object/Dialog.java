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

import net.gtaun.shoebill.SampObjectPool;
import net.gtaun.shoebill.Shoebill;
import net.gtaun.shoebill.util.event.EventDispatcher;
import net.gtaun.shoebill.util.event.IEventDispatcher;

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
	
	
	private EventDispatcher eventDispatcher = new EventDispatcher();
	
	private int id, style;

	
	@Override public IEventDispatcher getEventDispatcher()			{ return eventDispatcher; }

	@Override public int getId()									{ return id; }
	@Override public int getStyle()									{ return style; }
	

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
		
		SampObjectPool pool = (SampObjectPool) Shoebill.getInstance().getManagedObjectPool();
		pool.putDialog( id, this );
	}
	
	
	@Override
	public void show( IPlayer player, String caption, String text, String button1, String button2 )
	{
		player.showDialog( this, caption, text, button1, button2 );
	}
	
	@Override
	public void cancel( IPlayer player )
	{
		if( player.getDialog() != this ) return;
		player.cancelDialog();
	}
}
