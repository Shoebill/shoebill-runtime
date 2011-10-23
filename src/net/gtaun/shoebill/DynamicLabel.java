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

import net.gtaun.lungfish.data.Point;
import net.gtaun.lungfish.data.PointAngle;
import net.gtaun.lungfish.data.PointRange;
import net.gtaun.shoebill.streamer.IStreamObject;
import net.gtaun.shoebill.streamer.Streamer;

/**
 * @author MK124
 *
 */

public class DynamicLabel extends Label implements IStreamObject
{
	static final int DEFAULT_RANGE =		300;
	
	
	static Streamer<DynamicLabel> streamer;

	public static void initialize( Gamemode gamemode )
	{
		if( streamer == null )
			streamer = new Streamer<DynamicLabel>(gamemode, DEFAULT_RANGE);
	}
	public static void initialize( Gamemode gamemode, int range )
	{
		if( streamer == null )
			streamer = new Streamer<DynamicLabel>(gamemode, range);
	}
	
	
	int id[] = new int[ Gamemode.MAX_PLAYERS ];
	
	
	public DynamicLabel( String text, int color, Point point, boolean testLOS )
	{
		if( text == null ) throw new NullPointerException();
		
		this.text = text;
		this.color = color;
		this.position = new PointRange( point, streamer.range() );
		this.testLOS = testLOS;
		
		init();
	}
	
	public DynamicLabel( String text, int color, PointRange point, boolean testLOS )
	{
		if( text == null ) throw new NullPointerException();
		
		this.text = text;
		this.color = color;
		this.position = point.clone();
		this.testLOS = testLOS;
		
		init();
	}
	
	private void init()
	{
		for( int i = 0; i < id.length; i++ ) id[i] = -1;
		streamer.add( this );
	}
	
	
//---------------------------------------------------------
	
	public void destroy()
	{
		streamer.remove( this );
		
		for( int i=0; i<500; i++ )
			NativeFunction.deletePlayer3DTextLabel( i, id[i] );
	}

	public PointRange position()
	{
		PointAngle pos = null;
		
		if( attachedPlayer != null )	pos = attachedPlayer.position;
		if( attachedVehicle != null )	pos = attachedVehicle.position();
		
		if( pos != null )
		{
			position.x = pos.x + offsetX;
			position.y = pos.y + offsetY;
			position.z = pos.z + offsetZ;
			position.interior = pos.interior;
			position.world = pos.world;
		}
		
		return position.clone();
	}
	
	public void attach( Player player, float x, float y, float z )
	{
		offsetX = x;
		offsetY = y;
		offsetZ = z;
		
		for( int i=0; i<Gamemode.MAX_PLAYERS; i++ )
		{
			if( id[i] < 0 ) continue;
			NativeFunction.deletePlayer3DTextLabel( i, id[i] );
			id[i] = NativeFunction.createPlayer3DTextLabel( i, text, color, x, y, z, position.distance,
					player.id, Gamemode.INVALID_VEHICLE_ID, testLOS );
		}
	}
	
	public void attach( Vehicle vehicle, float x, float y, float z )
	{
		offsetX = x;
		offsetY = y;
		offsetZ = z;
		
		for( int i=0; i<Gamemode.MAX_PLAYERS; i++ )
		{
			if( id[i] < 0 ) continue;
			NativeFunction.deletePlayer3DTextLabel( i, id[i] );
			id[i] = NativeFunction.createPlayer3DTextLabel( i, text, color, x, y, z, position.distance,
					Gamemode.INVALID_PLAYER_ID, vehicle.id, testLOS );
		}
	}
	
	public void update( int color, String text )
	{
		if( text == null ) throw new NullPointerException();
		
		this.color = color;
		this.text = text;
		
		for( int i=0; i<Gamemode.MAX_PLAYERS; i++ )
		{
			if( id[i] < 0 ) continue;
			NativeFunction.updatePlayer3DTextLabelText( i, id[i], color, text );
		}
	}
	
	
//---------------------------------------------------------

	public void streamIn( Player player )
	{
		if( id[player.id] == -1 )
		{
			id[player.id] = NativeFunction.createPlayer3DTextLabel( player.id, text, color,
					position.x, position.y, position.z, position.distance,
					Gamemode.INVALID_PLAYER_ID, Gamemode.INVALID_VEHICLE_ID, testLOS );
		}
	}

	public void streamOut( Player player )
	{
		if( id[player.id] != -1 )
		{
			NativeFunction.deletePlayer3DTextLabel( player.id, id[player.id] );
			id[player.id] = -1;
		}
	}
	
	public Point getPosition()
	{
		return position;
	}
}
