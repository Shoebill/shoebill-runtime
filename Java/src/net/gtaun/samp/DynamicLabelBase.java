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
import net.gtaun.samp.data.PointAngle;
import net.gtaun.samp.data.PointRange;
import net.gtaun.samp.streamer.IStreamObject;
import net.gtaun.samp.streamer.Streamer;

/**
 * @author MK124
 *
 */

public class DynamicLabelBase extends LabelBase implements IStreamObject
{
	static final int DEFAULT_RANGE =		300;
	
	
	static Streamer<DynamicLabelBase> streamer;

	public static void initialize( GameModeBase gamemode )
	{
		if( streamer == null )
			streamer = new Streamer<DynamicLabelBase>(gamemode, DEFAULT_RANGE);
	}
	public static void initialize( GameModeBase gamemode, int range )
	{
		if( streamer == null )
			streamer = new Streamer<DynamicLabelBase>(gamemode, range);
	}
	
	
	int id[] = new int[ GameModeBase.MAX_PLAYERS ];
	
	
	public DynamicLabelBase( String text, int color, Point point, boolean testLOS )
	{
		this.text = text;
		this.color = color;
		this.position = new PointRange( point, streamer.range() );
		this.testLOS = testLOS;
		
		init();
	}
	
	public DynamicLabelBase( String text, int color, PointRange point, boolean testLOS )
	{
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
	
	public void attach( PlayerBase player, float x, float y, float z )
	{
		offsetX = x;
		offsetY = y;
		offsetZ = z;
		
		for( int i=0; i<GameModeBase.MAX_PLAYERS; i++ )
		{
			if( id[i] < 0 ) continue;
			NativeFunction.deletePlayer3DTextLabel( i, id[i] );
			id[i] = NativeFunction.createPlayer3DTextLabel( i, text, color, x, y, z, position.distance,
					player.id, GameModeBase.INVALID_VEHICLE_ID, testLOS );
		}
	}
	
	public void attach( VehicleBase vehicle, float x, float y, float z )
	{
		offsetX = x;
		offsetY = y;
		offsetZ = z;
		
		for( int i=0; i<GameModeBase.MAX_PLAYERS; i++ )
		{
			if( id[i] < 0 ) continue;
			NativeFunction.deletePlayer3DTextLabel( i, id[i] );
			id[i] = NativeFunction.createPlayer3DTextLabel( i, text, color, x, y, z, position.distance,
					GameModeBase.INVALID_PLAYER_ID, vehicle.id, testLOS );
		}
	}
	
	public void update( int color, String text )
	{
		this.color = color;
		this.text = text;
		
		for( int i=0; i<GameModeBase.MAX_PLAYERS; i++ )
		{
			if( id[i] < 0 ) continue;
			NativeFunction.updatePlayer3DTextLabelText( i, id[i], color, text );
		}
	}
	
	
//---------------------------------------------------------

	public void streamIn( PlayerBase player )
	{
		if( id[player.id] == -1 )
		{
			id[player.id] = NativeFunction.createPlayer3DTextLabel( player.id, text, color,
					position.x, position.y, position.z, position.distance,
					GameModeBase.INVALID_PLAYER_ID, GameModeBase.INVALID_VEHICLE_ID, testLOS );
		}
	}

	public void streamOut( PlayerBase player )
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
