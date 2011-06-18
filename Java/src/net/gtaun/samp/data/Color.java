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

package net.gtaun.samp.data;


/**
 * @author MK124
 *
 */

public final class Color
{
	public static final int WHITE = 0xFFFFFFFF;
    public static final int LIGHT_GRAY = 0xC0C0C0FF;
    public static final int GRAY = 0x808080FF;
    public static final int DARK_GRAY = 0x404040FF;
    public static final int BLACK = 0x000000FF;
    public static final int RED = 0xFF0000FF;
    public static final int PINK = 0xFFAFAFFF;
    public static final int ORANGE = 0xFFC800FF;
    public static final int YELLOW = 0xFFFF00FF;
    public static final int GREEN = 0x00FF00FF;
    public static final int MAGENTA = 0xFF00FFFF;
    public static final int CYAN = 0x00FFFFFF;
    public static final int BLUE = 0x0000FFFF;
	

	public static int rgb( int i, int j, int k )
	{
		return i&0xFF << 24 | j&0xFF << 16 | k&0xFF << 8 | 0xFF;
	}
	
	public static int rgba( int r, int g, int b, int a )
	{
		return r&0xFF << 24 | g&0xFF << 16 | b&0xFF << 8 | a&0xFF;
	}
}
