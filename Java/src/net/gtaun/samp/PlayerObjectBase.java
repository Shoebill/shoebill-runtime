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

import net.gtaun.samp.data.Point;
import net.gtaun.samp.data.PointRot;

/**
 * @author MK124
 *
 */

public class PlayerObjectBase extends ObjectBase
{
	PlayerBase player;
	
	
	public PlayerObjectBase( PlayerBase player, int model, float x, float y, float z, float rx, float ry, float rz )
	{
		this.player = player;
		this.model = model;
		this.position = new PointRot( x, y, z, rx, ry, rz );
		
		init();
	}
	
	public PlayerObjectBase( PlayerBase player, int model, Point point, float rx, float ry, float rz )
	{
		this.player = player;
		this.model = model;
		this.position = new PointRot( point, rx, ry, rz );
		
		init();
	}
	
	public PlayerObjectBase( PlayerBase player, int model, PointRot point )
	{
		this.player = player;
		this.model = model;
		this.position = point.clone();
		
		init();
	}
	
	private void init()
	{
		id = NativeFunction.createPlayerObject( player.id, model, position.x, position.y, position.z, position.rx, position.ry, position.rz );
	}
	

//---------------------------------------------------------
	
	public int onMoved()
	{
		return 1;
	}
	

//---------------------------------------------------------

	public void destroy()
	{
		NativeFunction.destroyObject( id );
		GameModeBase.instance.objectPool[ id ] = null;
	}
	
	public PointRot position()
	{
		if( speed == 0 ) return position.clone();
		
		NativeFunction.getPlayerObjectPos( player.id, id, position );
		NativeFunction.getPlayerObjectRot( player.id, id, position );
		return position.clone();
	}
	
	public void setPosition( Point position )
	{
		this.position.set( position );
		NativeFunction.setPlayerObjectPos( player.id, id, position.x, position.y, position.z );
	}
	
	public void setPosition( PointRot position )
	{
		this.position = position.clone();
		NativeFunction.setPlayerObjectPos( player.id, id, position.x, position.y, position.z );
		NativeFunction.setPlayerObjectRot( player.id, id, position.rx, position.ry, position.rz );
	}
	
	public void setRotate( float rx, float ry, float rz )
	{
		position.rx = rx;
		position.ry = ry;
		position.rz = rz;
		
		NativeFunction.setPlayerObjectRot( player.id, id, rx, ry, rz );
	}
	
	public void move( float x, float y, float z, float speed )
	{
		NativeFunction.movePlayerObject( player.id, id, x, y, z, speed );
	}
	
	public void stop()
	{
		NativeFunction.stopPlayerObject( player.id, id );
	}
	
	public void attach( PlayerBase player, float x, float y, float z, float rx, float ry, float rz )
	{
		NativeFunction.attachPlayerObjectToPlayer( player.id, id, player.id, x, y, z, rx, ry, rz );
	}
}
