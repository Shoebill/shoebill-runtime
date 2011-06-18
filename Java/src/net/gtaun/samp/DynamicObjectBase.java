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

import java.util.Vector;

import net.gtaun.samp.data.Point;
import net.gtaun.samp.data.PointRot;
import net.gtaun.samp.streamer.IStreamObject;
import net.gtaun.samp.streamer.Streamer;

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
	
	
	public DynamicObjectBase( int model, float x, float y, float z, float rx, float ry, float rz )
	{
		this.model = model;
		this.position = new PointRot( x, y, z, rx, ry, rz );
		
		init();
	}
	
	public DynamicObjectBase( int model, Point point, float rx, float ry, float rz )
	{
		this.model = model;
		this.position = new PointRot( point, rx, ry, rz );
		
		init();
	}
	
	public DynamicObjectBase( int model, PointRot point )
	{
		this.model = model;
		this.position = point.clone();
		
		init();
	}
	
	private void init()
	{
		for( int i = 0; i < id.length; i++ ) id[i] = -1;
	}


//---------------------------------------------------------
	
	public int onMoved()
	{
		return 1;
	}
	

//---------------------------------------------------------
	
	public void move( float x, float y, float z, float speed )
	{
		
	}
	
	public void stop()
	{
		
	}
	
	public void attach( PlayerBase player, float x, float y, float z, float rx, float ry, float rz )
	{
		
	}

	public void destroy()
	{
		streamer.remove( this );
		
		for( int i=0; i<500; i++ )
			NativeFunction.destroyPlayerObject( i, id[i] );
	}

	
//---------------------------------------------------------

	public Point getPosition()
	{
		return position;
	}
	
	public void streamIn( PlayerBase player )
	{
		if( id[player.id] == -1 )
		{
			id[player.id] = NativeFunction.createPlayerObject( player.id, model,
					position.x, position.y, position.z, position.rx, position.ry, position.rz );
		}
	}

	public void streamOut( PlayerBase player )
	{
		if( id[player.id] != -1 )
		{
			NativeFunction.destroyPlayerObject( player.id, id[player.id] );
			id[player.id] = -1;
		}
	}
}
