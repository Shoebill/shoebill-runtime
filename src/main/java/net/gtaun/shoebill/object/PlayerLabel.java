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
import net.gtaun.shoebill.data.Color;
import net.gtaun.shoebill.data.Point;
import net.gtaun.shoebill.data.PointRange;
import net.gtaun.shoebill.samp.SampNativeFunction;

/**
 * @author MK124
 *
 */

public class PlayerLabel extends Label implements IDestroyable
{
	public static final int INVALID_ID =			0xFFFF;
	
	
	public static Collection<PlayerLabel> get( Player player )
	{
		return Shoebill.getInstance().getManagedObjectPool().getPlayerLabels( player );
	}
	
	public static <T extends PlayerLabel> Collection<T> get( Player player, Class<T> cls )
	{
		return Shoebill.getInstance().getManagedObjectPool().getPlayerLabels( player, cls );
	}
	
	
	Player player;
	
	public Player getPlayer()		{ return player; }
	
	
	public PlayerLabel( Player player, String text, Color color, Point point, float drawDistance, boolean testLOS )
	{
		if( text == null ) throw new NullPointerException();
		
		this.player = player;
		this.text = text;
		this.color = color.clone();
		this.position = new PointRange( point, drawDistance );
		this.testLOS = testLOS;
		
		init();
	}

	public PlayerLabel( Player player, String text, Color color, Point point, float drawDistance, boolean testLOS, Player attachedPlayer )
	{
		if( text == null ) throw new NullPointerException();
		
		this.player = player;
		this.text = text;
		this.color = color.clone();
		this.position = new PointRange( point, drawDistance );
		this.testLOS = testLOS;
		this.attachedPlayer = attachedPlayer;
		
		init();
	}
	
	public PlayerLabel( Player player, String text, Color color, Point point, float drawDistance, boolean testLOS, Vehicle attachedVehicle )
	{
		if( text == null ) throw new NullPointerException();
		
		this.player = player;
		this.text = text;
		this.color = color.clone();
		this.position = new PointRange( point, drawDistance );
		this.testLOS = testLOS;
		this.attachedVehicle = attachedVehicle;
		
		init();
	}

	public PlayerLabel( Player player, String text, Color color, PointRange point, boolean testLOS )
	{
		if( text == null ) throw new NullPointerException();
		
		this.player = player;
		this.text = text;
		this.color = color.clone();
		this.position = point.clone();
		this.testLOS = testLOS;
		
		init();
	}
	
	public PlayerLabel( Player player, String text, Color color, PointRange point, boolean testLOS, Player attachedPlayer )
	{
		if( text == null ) throw new NullPointerException();
		
		this.player = player;
		this.text = text;
		this.color = color.clone();
		this.position = point.clone();
		this.testLOS = testLOS;
		this.attachedPlayer = attachedPlayer;
		
		init();
	}
	
	public PlayerLabel( Player player, String text, Color color, PointRange point, boolean testLOS, Vehicle attachedVehicle )
	{
		if( text == null ) throw new NullPointerException();
		
		this.player = player;
		this.text = text;
		this.color = color.clone();
		this.position = point.clone();
		this.testLOS = testLOS;
		this.attachedVehicle = attachedVehicle;
		
		init();
	}
	
	private void init()
	{
		int playerId = Player.INVALID_ID, vehicleId = Vehicle.INVALID_ID;
		
		if( attachedPlayer != null )	playerId = attachedPlayer.id;
		if( attachedVehicle != null )	vehicleId = attachedVehicle.id;
		
		if( attachedPlayer != null || attachedVehicle != null )
		{
			offsetX = position.x;
			offsetY = position.y;
			offsetZ = position.z;
		}
		
		id = SampNativeFunction.createPlayer3DTextLabel( player.id, text, color.getValue(),
				position.x, position.y, position.z, position.distance, playerId, vehicleId, testLOS );
		
		SampObjectPool pool = (SampObjectPool) Shoebill.getInstance().getManagedObjectPool();
		pool.setPlayerLabel( player, id, this );
	}
	
//---------------------------------------------------------
	
	@Override
	public void destroy()
	{
		SampNativeFunction.deletePlayer3DTextLabel( player.id, id );

		SampObjectPool pool = (SampObjectPool) Shoebill.getInstance().getManagedObjectPool();
		pool.setPlayerLabel( player, id, null );
		
		id = -1;
	}
	
	@Override
	public boolean isDestroyed()
	{
		return id == -1;
	}

	@Override
	public void attach( Player player, float x, float y, float z )
	{
		SampNativeFunction.deletePlayer3DTextLabel( this.player.id, id );
		id = SampNativeFunction.createPlayer3DTextLabel( this.player.id, text, color.getValue(), x, y, z, position.distance, player.id, Vehicle.INVALID_ID, testLOS );
	}

	@Override
	public void attach( Vehicle vehicle, float x, float y, float z )
	{
		SampNativeFunction.deletePlayer3DTextLabel( this.player.id, id );
		id = SampNativeFunction.createPlayer3DTextLabel( this.player.id, text, color.getValue(), x, y, z, position.distance, Player.INVALID_ID, vehicle.id, testLOS );
	}
	
	@Override
	public void update( Color color, String text )
	{
		this.color = color.clone();
		this.text = text;
		
		SampNativeFunction.updatePlayer3DTextLabelText( player.id, id, color.getValue(), text );
	}
}
