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

package net.gtaun.shoebill.object;

import java.util.Vector;

import net.gtaun.lungfish.data.Point;
import net.gtaun.lungfish.data.PointRot;
import net.gtaun.lungfish.object.IPlayer;
import net.gtaun.lungfish.object.IPlayerObject;
import net.gtaun.shoebill.SampNativeFunction;

/**
 * @author MK124
 *
 */

public class PlayerObject extends ObjectBase implements IPlayerObject
{
	public static Vector<PlayerObject> get( int playerid )
	{
		Vector<PlayerObject> list = new Vector<PlayerObject>();
		
		int baseIndex = playerid*Gamemode.MAX_OBJECTS;
		for( int i = baseIndex; i < baseIndex+Gamemode.MAX_OBJECTS ; i++ )
		{
			list.add( Gamemode.instance.playerObjectPool[i] );
		}
		
		return list;
	}
	
	public static <T> Vector<T> get( Class<T> cls, int playerid )
	{
		Vector<T> list = new Vector<T>();
		
		int baseIndex = playerid*Gamemode.MAX_OBJECTS;
		for( int i = baseIndex; i < baseIndex+Gamemode.MAX_OBJECTS ; i++ )
		{
			PlayerObject obj = Gamemode.instance.playerObjectPool[i];
			if( cls.isInstance(obj) ) list.add( cls.cast(obj) );
		}
		
		return list;
	}
	
	
	Player player;
	
	@Override public IPlayer getPlayer()		{ return player; }
	
	
	public PlayerObject( Player player, int model, float x, float y, float z, float rx, float ry, float rz )
	{
		this.player = player;
		this.model = model;
		this.position = new PointRot( x, y, z, rx, ry, rz );
		
		init();
	}
	
	public PlayerObject( Player player, int model, float x, float y, float z, float rx, float ry, float rz, float drawDistance )
	{
		this.player = player;
		this.model = model;
		this.position = new PointRot( x, y, z, rx, ry, rz );
		this.drawDistance = drawDistance;
		
		init();
	}
	
	public PlayerObject( Player player, int model, Point point, float rx, float ry, float rz )
	{
		this.player = player;
		this.model = model;
		this.position = new PointRot( point, rx, ry, rz );
		
		init();
	}
	
	public PlayerObject( Player player, int model, Point point, float rx, float ry, float rz, float drawDistance )
	{
		this.player = player;
		this.model = model;
		this.position = new PointRot( point, rx, ry, rz );
		this.drawDistance = drawDistance;
		
		init();
	}
	
	public PlayerObject( Player player, int model, PointRot point )
	{
		this.player = player;
		this.model = model;
		this.position = point.clone();
		
		init();
	}
	
	public PlayerObject( Player player, int model, PointRot point, float drawDistance )
	{
		this.player = player;
		this.model = model;
		this.position = point.clone();
		this.drawDistance = drawDistance;
		
		init();
	}
	
	private void init()
	{
		id = SampNativeFunction.createPlayerObject( player.id, model, position.x, position.y, position.z, position.rx, position.ry, position.rz, drawDistance );
		
		Gamemode.instance.playerObjectPool[id + player.id*Gamemode.MAX_OBJECTS] = this;
	}
	

//---------------------------------------------------------

	@Override
	public void destroy()
	{
		SampNativeFunction.destroyObject( id );
		Gamemode.instance.playerObjectPool[ id + player.id*Gamemode.MAX_OBJECTS ] = null;
	}
	
	@Override
	public PointRot getPosition()
	{	
		SampNativeFunction.getPlayerObjectPos( player.id, id, position );
		SampNativeFunction.getPlayerObjectRot( player.id, id, position );
		return position.clone();
	}
	
	@Override
	public void setPosition( Point position )
	{
		this.position.set( position );
		SampNativeFunction.setPlayerObjectPos( player.id, id, position.x, position.y, position.z );
	}
	
	@Override
	public void setPosition( PointRot position )
	{
		this.position = position.clone();
		SampNativeFunction.setPlayerObjectPos( player.id, id, position.x, position.y, position.z );
		SampNativeFunction.setPlayerObjectRot( player.id, id, position.rx, position.ry, position.rz );
	}
	
	@Override
	public void setRotate( float rx, float ry, float rz )
	{
		position.rx = rx;
		position.ry = ry;
		position.rz = rz;
		
		SampNativeFunction.setPlayerObjectRot( player.id, id, rx, ry, rz );
	}
	
	@Override
	public int move( float x, float y, float z, float speed )
	{
		SampNativeFunction.movePlayerObject( player.id, id, x, y, z, speed );
		if(attachedPlayer == null) this.speed = speed;
		return 0;
	}
	
	@Override
	public void stop()
	{
		speed = 0;
		SampNativeFunction.stopPlayerObject( player.id, id );
	}
	
	public void attach( Player player, float x, float y, float z, float rx, float ry, float rz )
	{
		SampNativeFunction.attachPlayerObjectToPlayer( this.player.id, id, player.id, x, y, z, rx, ry, rz );
		this.attachedPlayer = player;
		speed = 0;
	}
	
	public void attach( Vehicle vehicle, float x, float y, float z, float rx, float ry, float rz )
	{
		return;
	}
}
