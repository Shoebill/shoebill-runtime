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

package net.gtaun.shoebill.data;

import java.io.Serializable;

/**
 * @author MK124
 *
 */

public class Color implements Cloneable, Serializable
{
	private static final long serialVersionUID = -6538397318569967446L;
	
	
	public static final Color WHITE =			new Color( 0xFFFFFFFF );
    public static final Color LIGHT_GRAY =		new Color( 0xC0C0C0FF );
    public static final Color GRAY =			new Color( 0x808080FF );
    public static final Color DARK_GRAY =		new Color( 0x404040FF );
    public static final Color BLACK =			new Color( 0x000000FF );
    public static final Color RED =				new Color( 0xFF0000FF );
    public static final Color PINK =			new Color( 0xFFAFAFFF );
    public static final Color ORANGE =			new Color( 0xFFC800FF );
    public static final Color YELLOW =			new Color( 0xFFFF00FF );
    public static final Color GREEN =			new Color( 0x00FF00FF );
    public static final Color MAGENTA =			new Color( 0xFF00FFFF );
    public static final Color CYAN =			new Color( 0x00FFFFFF );
    public static final Color BLUE =			new Color( 0x0000FFFF );
    
    
    int value;
    
    
    public Color( int value )
    {
    	this.value = value;
    }
    
    public Color( int r, int g, int b )
    {
		value = r&0xFF << 24 | g&0xFF << 16 | b&0xFF << 8 | 0xFF;
    }
    
    public Color( int r, int g, int b, int a )
    {
    	value = r&0xFF << 24 | g&0xFF << 16 | b&0xFF << 8 | a&0xFF;
    }
    
    
    public int getValue()
    {
    	return value;
    }
    
    public int getR()
    {
    	return (value&0xFF00000) >> 24;
    }
    
    public int getG()
    {
    	return (value&0x00FF0000) >> 16;
    }

    public int getB()
    {
    	return (value&0x0000FF00) >> 8;
    }

    public int getA()
    {
    	return (value&0x000000FF);
    }
    
    
    public int getY()
    {
    	return (int) (0.299f*getR() + 0.587f*getG() + 0.114f*getB() + 0.5f);
    }
    
    
    @Override
    public Color clone()
    {
    	return new Color( value );
    }
}
