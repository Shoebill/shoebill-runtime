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

import java.util.Vector;

import net.gtaun.shoebill.SampNativeFunction;
import net.gtaun.shoebill.data.Color;


/**
 * @author MK124, JoJLlmAn
 *
 */

public class Textdraw implements IDestroyable
{
	public static Vector<Textdraw> get()
	{
		return Gamemode.getInstances(Gamemode.instance.textdrawPool, Textdraw.class);
	}
	
	public static <T> Vector<T> get( Class<T> cls )
	{
		return Gamemode.getInstances(Gamemode.instance.textdrawPool, cls);
	}
	
	private boolean[] isPlayerShowed = new boolean[Gamemode.MAX_PLAYERS];
	
	
	int id = -1;
	float x, y;
	String text;
	
	
	public int getId()				{ return id; }
	public float getX()				{ return x; }
	public float getY()				{ return y; }
	public String getText()			{ return text; }
	
	
	public Textdraw( float x, float y, String text )
	{
		if( text == null ) throw new NullPointerException();
		
		this.x = x;
		this.y = y;
		this.text = text;
		
		init();
	}
	
	public Textdraw( float x, float y )
	{
		this.x = x;
		this.y = y;
		this.text = "";
		
		init();
	}
	
	private void init()
	{
		id = SampNativeFunction.textDrawCreate( x, y, text );
		for( int i=0; i<Gamemode.MAX_PLAYERS; i++ ) isPlayerShowed[i] = false;
		
		Gamemode.instance.textdrawPool[id] = this;
	}
	
	
//---------------------------------------------------------

	@Override
	public void destroy()
	{
		SampNativeFunction.textDrawDestroy( id );
		Gamemode.instance.textdrawPool[id] = null;
		
		id = -1;
	}

	@Override
	public boolean isDestroyed()
	{
		return id == -1;
	}

	public void setLetterSize( float x, float y )
	{
		SampNativeFunction.textDrawLetterSize( id, x, y );
	}

	public void setTextSize( float x, float y )
	{
		SampNativeFunction.textDrawTextSize( id, x, y );
	}

	public void setAlignment( int alignment )
	{
		SampNativeFunction.textDrawAlignment( id, alignment );
	}

	public void setColor( Color color )
	{
		SampNativeFunction.textDrawColor( id, color.getValue() );
	}

	public void setUseBox( boolean use )
	{
		SampNativeFunction.textDrawUseBox( id, use );
	}

	public void setBoxColor( Color color )
	{
		SampNativeFunction.textDrawBoxColor( id, color.getValue() );
	}

	public void setShadow( int size )
	{
		SampNativeFunction.textDrawSetShadow( id, size );
	}

	public void setOutline( int size )
	{
		SampNativeFunction.textDrawSetOutline( id, size );
	}

	public void setBackgroundColor( Color color )
	{
		SampNativeFunction.textDrawBackgroundColor( id, color.getValue() );
	}

	public void setFont( int font )
	{
		SampNativeFunction.textDrawFont( id, font );
	}

	public void setProportional( int set )
	{
		SampNativeFunction.textDrawSetProportional( id, set );
	}

	public void setText( String text )
	{
		if( text == null ) throw new NullPointerException();
		
		this.text = text;
		SampNativeFunction.textDrawSetString( id, text );
	}

	public void show( Player player )
	{
		SampNativeFunction.textDrawShowForPlayer( player.id, id );
		isPlayerShowed[player.id] = true;
	}

	public void hide( Player player )
	{
		SampNativeFunction.textDrawHideForPlayer( player.id, id );
		isPlayerShowed[player.id] = false;
	}

	public void showForAll()
	{
		SampNativeFunction.textDrawShowForAll( id );
		for( int i=0; i<Gamemode.MAX_PLAYERS; i++ ) isPlayerShowed[i] = true;
	}

	public void hideForAll()
	{
		SampNativeFunction.textDrawHideForAll( id );
		for( int i=0; i<Gamemode.MAX_PLAYERS; i++ ) isPlayerShowed[i] = false;
	}
}
