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
import net.gtaun.lungfish.data.PointRange;
import net.gtaun.shoebill.NativeFunction;

/**
 * @author MK124
 *
 */

public class PlayerLabel extends Label
{
	public static Vector<PlayerLabel> get( int playerid )
	{
		Vector<PlayerLabel> list = new Vector<PlayerLabel>();
		
		int baseIndex = playerid*Gamemode.MAX_LABELS_PLAYER;
		for( int i = baseIndex; i < baseIndex+Gamemode.MAX_LABELS_PLAYER ; i++ )
		{
			list.add( Gamemode.instance.playerLabelPool[i] );
		}
		
		return list;
	}
	
	public static <T> Vector<T> get( Class<T> cls, int playerid )
	{
		Vector<T> list = new Vector<T>();
		
		int baseIndex = playerid*Gamemode.MAX_LABELS_PLAYER;
		for( int i = baseIndex; i < baseIndex+Gamemode.MAX_LABELS_PLAYER ; i++ )
		{
			PlayerLabel obj = Gamemode.instance.playerLabelPool[i];
			if( cls.isInstance(obj) ) list.add( cls.cast(obj) );
		}
		
		return list;
	}
	
	Player player;
	
	
	public PlayerLabel( Player player, String text, int color, Point point, float drawDistance, boolean testLOS )
	{
		if( text == null ) throw new NullPointerException();
		
		this.player = player;
		this.text = text;
		this.color = color;
		this.position = new PointRange( point, drawDistance );
		this.testLOS = testLOS;
		
		init();
	}

	public PlayerLabel( Player player, String text, int color, Point point, float drawDistance, boolean testLOS, Player attachedPlayer )
	{
		if( text == null ) throw new NullPointerException();
		
		this.player = player;
		this.text = text;
		this.color = color;
		this.position = new PointRange( point, drawDistance );
		this.testLOS = testLOS;
		this.attachedPlayer = attachedPlayer;
		
		init();
	}
	
	public PlayerLabel( Player player, String text, int color, Point point, float drawDistance, boolean testLOS, Vehicle attachedVehicle )
	{
		if( text == null ) throw new NullPointerException();
		
		this.player = player;
		this.text = text;
		this.color = color;
		this.position = new PointRange( point, drawDistance );
		this.testLOS = testLOS;
		this.attachedVehicle = attachedVehicle;
		
		init();
	}

	public PlayerLabel( Player player, String text, int color, PointRange point, boolean testLOS )
	{
		if( text == null ) throw new NullPointerException();
		
		this.player = player;
		this.text = text;
		this.color = color;
		this.position = point.clone();
		this.testLOS = testLOS;
		
		init();
	}
	
	public PlayerLabel( Player player, String text, int color, PointRange point, boolean testLOS, Player attachedPlayer )
	{
		if( text == null ) throw new NullPointerException();
		
		this.player = player;
		this.text = text;
		this.color = color;
		this.position = point.clone();
		this.testLOS = testLOS;
		this.attachedPlayer = attachedPlayer;
		
		init();
	}
	
	public PlayerLabel( Player player, String text, int color, PointRange point, boolean testLOS, Vehicle attachedVehicle )
	{
		if( text == null ) throw new NullPointerException();
		
		this.player = player;
		this.text = text;
		this.color = color;
		this.position = point.clone();
		this.testLOS = testLOS;
		this.attachedVehicle = attachedVehicle;
		
		init();
	}
	
	private void init()
	{
		int playerId = Gamemode.INVALID_PLAYER_ID, vehicleId = Gamemode.INVALID_VEHICLE_ID;
		
		if( attachedPlayer != null )	playerId = attachedPlayer.id;
		if( attachedVehicle != null )	vehicleId = attachedVehicle.id;
		
		if( attachedPlayer != null || attachedVehicle != null )
		{
			offsetX = position.x;
			offsetY = position.y;
			offsetZ = position.z;
		}
		
		id = NativeFunction.createPlayer3DTextLabel( player.id, text, color,
				position.x, position.y, position.z, position.distance, playerId, vehicleId, testLOS );
		
		Gamemode.instance.playerLabelPool[id+player.id*Gamemode.MAX_LABELS_PLAYER] = this;
	}
	
//---------------------------------------------------------
	
	public void destroy()
	{
		NativeFunction.deletePlayer3DTextLabel( player.id, id );
		Gamemode.instance.playerLabelPool[id+player.id*Gamemode.MAX_LABELS_PLAYER] = null;
	}

	public void attach( Player player, float x, float y, float z )
	{
		NativeFunction.deletePlayer3DTextLabel( this.player.id, id );
		id = NativeFunction.createPlayer3DTextLabel( this.player.id, text, color, x, y, z, position.distance,
				player.id, Gamemode.INVALID_VEHICLE_ID, testLOS );
	}

	public void attach( Vehicle vehicle, float x, float y, float z )
	{
		NativeFunction.deletePlayer3DTextLabel( this.player.id, id );
		id = NativeFunction.createPlayer3DTextLabel( this.player.id, text, color, x, y, z, position.distance,
				Gamemode.INVALID_PLAYER_ID, vehicle.id, testLOS );
	}
	
	public void update( int color, String text )
	{
		this.color = color;
		this.text = text;
		
		NativeFunction.updatePlayer3DTextLabelText( player.id, id, color, text );
	}
}
