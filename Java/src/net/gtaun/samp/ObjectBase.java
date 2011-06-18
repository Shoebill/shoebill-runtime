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

/**
 * @author MK124
 *
 */

public class ObjectBase
{
	public static <T> Vector<T> get( Class<T> cls )
	{
		return GameModeBase.getInstances( GameModeBase.instance.objectPool, cls );
	}
	
	
	int id;
	int model;
	PointRot position;
	float speed;
	

	protected ObjectBase()
	{
		
	}
	
	public ObjectBase( int model, float x, float y, float z, float rx, float ry, float rz )
	{
		this.model = model;
		this.position = new PointRot( x, y, z, rx, ry, rz );
		
		init();
	}
	
	public ObjectBase( int model, Point point, float rx, float ry, float rz )
	{
		this.model = model;
		this.position = new PointRot( point, rx, ry, rz );
		
		init();
	}
	
	public ObjectBase( int model, PointRot point )
	{
		this.model = model;
		this.position = point.clone();
		
		init();
	}
	
	private void init()
	{
		id = NativeFunction.createObject( model, position.x, position.y, position.z, position.rx, position.ry, position.rz );
	}
	

//---------------------------------------------------------
	
	public int onMoved()
	{
		return 1;
	}
	

//---------------------------------------------------------
	
	public void move( float x, float y, float z, float speed )
	{
		NativeFunction.moveObject( id, x, y, z, speed );
	}
	
	public void stop()
	{
		NativeFunction.stopObject( id );
	}
	
	public void attach( PlayerBase player, float x, float y, float z, float rx, float ry, float rz )
	{
		NativeFunction.attachObjectToPlayer( id, player.id, x, y, z, rx, ry, rz );
	}

	public void destroy()
	{
		NativeFunction.destroyObject( id );
		GameModeBase.instance.objectPool[ id ] = null;
	}
}
