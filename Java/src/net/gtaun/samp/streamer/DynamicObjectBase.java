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

package net.gtaun.samp.streamer;

import java.io.StringBufferInputStream;
import java.util.Vector;

import net.gtaun.samp.GameModeBase;
import net.gtaun.samp.ObjectBase;
import net.gtaun.samp.PlayerBase;
import net.gtaun.samp.data.Point;

/**
 * @author MK124
 *
 */

public class DynamicObjectBase extends ObjectBase implements IStreamObject
{
	static Streamer<DynamicObjectBase> streamer;
	
	public static void initialize( GameModeBase gamemode, int range )
	{
		if( streamer == null )
			streamer = new Streamer<DynamicObjectBase>(gamemode, range);
	}
	
	@Deprecated
	public static <T> Vector<T> get( Class<T> cls )
	{
		return null;
	}
	
	
	int id[] = new int[500];
	
	
	public DynamicObjectBase()
	{
		for( int i = 0; i < id.length; i++ ) id[i] = -1;
	}


	public Point getPosition()
	{
		return position;
	}
	
	public void streamIn( PlayerBase player )
	{
		if( id[player.id()] == -1 );
	}

	public void streamOut( PlayerBase player )
	{
		if( id[player.id()] != -1 );
	}
}
