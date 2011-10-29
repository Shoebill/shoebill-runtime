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
import net.gtaun.shoebill.NativeFunction;


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
	
	
	public int getId()			{ return id; }
	public float getX()			{ return x; }
	public float getY()			{ return y; }
	public String getText()		{ return text; }
	
	
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
		id = NativeFunction.textDrawCreate( x, y, text );
		for( int i=0; i<Gamemode.MAX_PLAYERS; i++ ) isPlayerShowed[i] = false;
		
		Gamemode.instance.textdrawPool[id] = this;
	}
	
	
//---------------------------------------------------------
	
	public void destroy()
	{
		NativeFunction.textDrawDestroy( id );
		Gamemode.instance.textdrawPool[id] = null;
	}
	
	public void setLetterSize( float x, float y )
	{
		NativeFunction.textDrawLetterSize( id, x, y );
	}
	
	public void setTextSize( float x, float y )
	{
		NativeFunction.textDrawTextSize( id, x, y );
	}
	
	public void setAlignment( int alignment )
	{
		NativeFunction.textDrawAlignment( id, alignment );
	}
	
	public void setColor( Color color )
	{
		NativeFunction.textDrawColor( id, color.getValue() );
	}
	
	public void setUseBox( boolean use )
	{
		NativeFunction.textDrawUseBox( id, use );
	}
	
	public void setBoxColor( Color color )
	{
		NativeFunction.textDrawBoxColor( id, color.getValue() );
	}
	
	public void setShadow( int size )
	{
		NativeFunction.textDrawSetShadow( id, size );
	}
	
	public void setOutline( int size )
	{
		NativeFunction.textDrawSetOutline( id, size );
	}
	
	public void setBackgroundColor( Color color )
	{
		NativeFunction.textDrawBackgroundColor( id, color.getValue() );
	}
	
	public void setFont( int font )
	{
		NativeFunction.textDrawFont( id, font );
	}
	
	public void setProportional( int set )
	{
		NativeFunction.textDrawSetProportional( id, set );
	}
	
	public void setText( String text )
	{
		if( text == null ) throw new NullPointerException();
		
		this.text = text;
		NativeFunction.textDrawSetString( id, text );
	}
	
	public void show( IPlayer p )
	{
		Player player = (Player) p;
		NativeFunction.textDrawShowForPlayer( player.id, id );
		isPlayerShowed[player.id] = true;
	}
	
	public void hide( IPlayer p )
	{
		Player player = (Player) p;
		NativeFunction.textDrawHideForPlayer( player.id, id );
		isPlayerShowed[player.id] = false;
	}
	
	public void showForAll()
	{
		NativeFunction.textDrawShowForAll( id );
		for( int i=0; i<Gamemode.MAX_PLAYERS; i++ ) isPlayerShowed[i] = true;
	}
	
	public void hideForAll()
	{
		NativeFunction.textDrawHideForAll( id );
		for( int i=0; i<Gamemode.MAX_PLAYERS; i++ ) isPlayerShowed[i] = false;
	}
}
