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

package net.gtaun.shoebill;

import java.util.Vector;

import net.gtaun.shoebill.data.Point;
import net.gtaun.shoebill.data.PointRot;

/**
 * @author MK124
 *
 */

public class PlayerObjectBase extends ObjectBase
{
	public static Vector<PlayerObjectBase> get( int playerid )
	{
		Vector<PlayerObjectBase> list = new Vector<PlayerObjectBase>();
		
		int baseIndex = playerid*GameModeBase.MAX_OBJECTS;
		for( int i = baseIndex; i < baseIndex+GameModeBase.MAX_OBJECTS ; i++ )
		{
			list.add( GameModeBase.instance.playerObjectPool[i] );
		}
		
		return list;
	}
	
	public static <T> Vector<T> get( Class<T> cls, int playerid )
	{
		Vector<T> list = new Vector<T>();
		
		int baseIndex = playerid*GameModeBase.MAX_OBJECTS;
		for( int i = baseIndex; i < baseIndex+GameModeBase.MAX_OBJECTS ; i++ )
		{
			PlayerObjectBase obj = GameModeBase.instance.playerObjectPool[i];
			if( cls.isInstance(obj) ) list.add( cls.cast(obj) );
		}
		
		return list;
	}
	
	
	PlayerBase player;
	
	public PlayerBase player()		{ return player; }
	
	public PlayerObjectBase( PlayerBase player, int model, float x, float y, float z, float rx, float ry, float rz )
	{
		this.player = player;
		this.model = model;
		this.position = new PointRot( x, y, z, rx, ry, rz );
		
		init();
	}
	
	public PlayerObjectBase( PlayerBase player, int model, float x, float y, float z, float rx, float ry, float rz, float drawDistance )
	{
		this.player = player;
		this.model = model;
		this.position = new PointRot( x, y, z, rx, ry, rz );
		this.drawDistance = drawDistance;
		
		init();
	}
	
	public PlayerObjectBase( PlayerBase player, int model, Point point, float rx, float ry, float rz )
	{
		this.player = player;
		this.model = model;
		this.position = new PointRot( point, rx, ry, rz );
		
		init();
	}
	
	public PlayerObjectBase( PlayerBase player, int model, Point point, float rx, float ry, float rz, float drawDistance )
	{
		this.player = player;
		this.model = model;
		this.position = new PointRot( point, rx, ry, rz );
		this.drawDistance = drawDistance;
		
		init();
	}
	
	public PlayerObjectBase( PlayerBase player, int model, PointRot point )
	{
		this.player = player;
		this.model = model;
		this.position = point.clone();
		
		init();
	}
	
	public PlayerObjectBase( PlayerBase player, int model, PointRot point, float drawDistance )
	{
		this.player = player;
		this.model = model;
		this.position = point.clone();
		this.drawDistance = drawDistance;
		
		init();
	}
	
	private void init()
	{
		id = NativeFunction.createPlayerObject( player.id, model, position.x, position.y, position.z, position.rx, position.ry, position.rz, drawDistance );
		
		GameModeBase.instance.playerObjectPool[id + player.id*GameModeBase.MAX_OBJECTS] = this;
	}
	

//---------------------------------------------------------
	
	protected int onMoved()
	{
		return 1;
	}
	

//---------------------------------------------------------

	public void destroy()
	{
		NativeFunction.destroyObject( id );
		GameModeBase.instance.playerObjectPool[ id + player.id*GameModeBase.MAX_OBJECTS ] = null;
	}
	
	public PointRot position()
	{	
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
	
	public int move( float x, float y, float z, float speed )
	{
		NativeFunction.movePlayerObject( player.id, id, x, y, z, speed );
		if(attachedPlayer == null) this.speed = speed;
		return 0;
	}
	
	public void stop()
	{
		speed = 0;
		NativeFunction.stopPlayerObject( player.id, id );
	}
	
	public void attach( PlayerBase player, float x, float y, float z, float rx, float ry, float rz )
	{
		NativeFunction.attachPlayerObjectToPlayer( this.player.id, id, player.id, x, y, z, rx, ry, rz );
		this.attachedPlayer = player;
		speed = 0;
	}
	
	public void attach( VehicleBase vehicle, float x, float y, float z, float rx, float ry, float rz )
	{
		return;
	}
}
