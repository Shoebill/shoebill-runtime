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

import net.gtaun.lungfish.data.Color;
import net.gtaun.lungfish.object.IPlayer;
import net.gtaun.lungfish.object.ITextdraw;
import net.gtaun.shoebill.SampNativeFunction;


/**
 * @author MK124, JoJLlmAn
 *
 */

public class Textdraw implements ITextdraw
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
	
	
	int id;
	float x, y;
	String text;
	
	
	@Override public int getId()			{ return id; }
	@Override public float getX()			{ return x; }
	@Override public float getY()			{ return y; }
	@Override public String getText()		{ return text; }
	
	
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
	}

	@Override
	public void setLetterSize( float x, float y )
	{
		SampNativeFunction.textDrawLetterSize( id, x, y );
	}

	@Override
	public void setTextSize( float x, float y )
	{
		SampNativeFunction.textDrawTextSize( id, x, y );
	}

	@Override
	public void setAlignment( int alignment )
	{
		SampNativeFunction.textDrawAlignment( id, alignment );
	}

	@Override
	public void setColor( Color color )
	{
		SampNativeFunction.textDrawColor( id, color.getValue() );
	}

	@Override
	public void setUseBox( boolean use )
	{
		SampNativeFunction.textDrawUseBox( id, use );
	}

	@Override
	public void setBoxColor( Color color )
	{
		SampNativeFunction.textDrawBoxColor( id, color.getValue() );
	}

	@Override
	public void setShadow( int size )
	{
		SampNativeFunction.textDrawSetShadow( id, size );
	}

	@Override
	public void setOutline( int size )
	{
		SampNativeFunction.textDrawSetOutline( id, size );
	}

	@Override
	public void setBackgroundColor( Color color )
	{
		SampNativeFunction.textDrawBackgroundColor( id, color.getValue() );
	}

	@Override
	public void setFont( int font )
	{
		SampNativeFunction.textDrawFont( id, font );
	}

	@Override
	public void setProportional( int set )
	{
		SampNativeFunction.textDrawSetProportional( id, set );
	}

	@Override
	public void setText( String text )
	{
		if( text == null ) throw new NullPointerException();
		
		this.text = text;
		SampNativeFunction.textDrawSetString( id, text );
	}

	@Override
	public void show( IPlayer p )
	{
		Player player = (Player) p;
		SampNativeFunction.textDrawShowForPlayer( player.id, id );
		isPlayerShowed[player.id] = true;
	}

	@Override
	public void hide( IPlayer p )
	{
		Player player = (Player) p;
		SampNativeFunction.textDrawHideForPlayer( player.id, id );
		isPlayerShowed[player.id] = false;
	}

	@Override
	public void showForAll()
	{
		SampNativeFunction.textDrawShowForAll( id );
		for( int i=0; i<Gamemode.MAX_PLAYERS; i++ ) isPlayerShowed[i] = true;
	}

	@Override
	public void hideForAll()
	{
		SampNativeFunction.textDrawHideForAll( id );
		for( int i=0; i<Gamemode.MAX_PLAYERS; i++ ) isPlayerShowed[i] = false;
	}
}
