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

import java.util.Collection;

import net.gtaun.shoebill.SampObjectPool;
import net.gtaun.shoebill.Shoebill;
import net.gtaun.shoebill.data.Point;
import net.gtaun.shoebill.data.PointRot;
import net.gtaun.shoebill.samp.SampNativeFunction;
import net.gtaun.shoebill.util.event.EventDispatcher;
import net.gtaun.shoebill.util.event.IEventDispatcher;

/**
 * @author MK124
 *
 */

public class PlayerObject implements IPlayerObject
{
	public static Collection<IPlayerObject> get( IPlayer player )
	{
		return Shoebill.getInstance().getManagedObjectPool().getPlayerObjects( player );
	}
	
	public static <T extends IPlayerObject> Collection<T> get( IPlayer player, Class<T> cls )
	{
		return Shoebill.getInstance().getManagedObjectPool().getPlayerObjects( player, cls );
	}
	

	private EventDispatcher eventDispatcher = new EventDispatcher();
	
	private int id = -1;
	private Player player;
	
	private int model;
	private PointRot position;
	private float speed = 0;
	private IPlayer attachedPlayer;
	private IVehicle attachedVehicle;
	private float drawDistance = 0;
	
	
	@Override public Player getPlayer()								{ return player; }
	@Override public IEventDispatcher getEventDispatcher()			{ return eventDispatcher; }
	
	@Override public int getModel()									{ return model; }
	@Override public float getSpeed()								{ return speed; }
	@Override public IPlayer getAttachedPlayer()					{ return attachedPlayer; }
	@Override public IVehicle getAttachedVehicle()					{ return attachedVehicle; }
	@Override public float getDrawDistance()						{ return drawDistance; }
	
	
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
		id = SampNativeFunction.createPlayerObject( player.getId(), model, position.x, position.y, position.z, position.rx, position.ry, position.rz, drawDistance );
		
		SampObjectPool pool = (SampObjectPool) Shoebill.getInstance().getManagedObjectPool();
		pool.setPlayerObject( player, id, this );
	}
	

//---------------------------------------------------------

	@Override
	public void destroy()
	{
		SampNativeFunction.destroyObject( id );

		SampObjectPool pool = (SampObjectPool) Shoebill.getInstance().getManagedObjectPool();
		pool.setPlayerObject( player, id, null );
		
		id = -1;
	}

	@Override
	public boolean isDestroyed()
	{
		return id == -1;
	}
	
	@Override
	public PointRot getPosition()
	{	
		SampNativeFunction.getPlayerObjectPos( player.getId(), id, position );
		SampNativeFunction.getPlayerObjectRot( player.getId(), id, position );
		return position.clone();
	}
	
	@Override
	public void setPosition( Point position )
	{
		this.position.set( position );
		SampNativeFunction.setPlayerObjectPos( player.getId(), id, position.x, position.y, position.z );
	}
	
	@Override
	public void setPosition( PointRot position )
	{
		this.position = position.clone();
		SampNativeFunction.setPlayerObjectPos( player.getId(), id, position.x, position.y, position.z );
		SampNativeFunction.setPlayerObjectRot( player.getId(), id, position.rx, position.ry, position.rz );
	}
	
	@Override
	public void setRotate( float rx, float ry, float rz )
	{
		position.rx = rx;
		position.ry = ry;
		position.rz = rz;
		
		SampNativeFunction.setPlayerObjectRot( player.getId(), id, rx, ry, rz );
	}
	
	@Override
	public int move( float x, float y, float z, float speed )
	{
		SampNativeFunction.movePlayerObject( player.getId(), id, x, y, z, speed );
		if(attachedPlayer == null) this.speed = speed;
		return 0;
	}
	
	@Override
	public void stop()
	{
		speed = 0;
		SampNativeFunction.stopPlayerObject( player.getId(), id );
	}
	
	@Override
	public void attach( IPlayer player, float x, float y, float z, float rx, float ry, float rz )
	{
		SampNativeFunction.attachPlayerObjectToPlayer( this.player.getId(), id, player.getId(), x, y, z, rx, ry, rz );
		this.attachedPlayer = player;
		speed = 0;
	}
	
	@Override
	public void attach( IVehicle vehicle, float x, float y, float z, float rx, float ry, float rz )
	{
		return;
	}
}
