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
import net.gtaun.shoebill.data.Location;
import net.gtaun.shoebill.samp.SampNativeFunction;

/**
 * @author MK124
 *
 */

public class PlayerLabel implements IPlayerLabel
{
	public static final int INVALID_ID =			0xFFFF;
	
	
	public static Collection<IPlayerLabel> get( IPlayer player )
	{
		return Shoebill.getInstance().getManagedObjectPool().getPlayerLabels( player );
	}
	
	public static <T extends IPlayerLabel> Collection<T> get( IPlayer player, Class<T> cls )
	{
		return Shoebill.getInstance().getManagedObjectPool().getPlayerLabels( player, cls );
	}
	
	
	private int id = -1;
	private IPlayer player;
	private String text;
	private Color color;
	private float drawDistance;
	private Location location;
	private boolean testLOS;
	
	private float offsetX, offsetY, offsetZ;
	private IPlayer attachedPlayer;
	private IVehicle attachedVehicle;
	
	
	@Override public int getId()								{ return id; }
	@Override public IPlayer getPlayer()						{ return player; }
	
	@Override public String getText()							{ return text; }
	@Override public Color getColor()							{ return color.clone(); }
	@Override public float getDrawDistance()					{ return drawDistance; }
	@Override public IPlayer getAttachedPlayer()				{ return attachedPlayer; }
	@Override public IVehicle getAttachedVehicle()				{ return attachedVehicle; }
	
	
	public PlayerLabel( Player player, String text, Color color, Location location, float drawDistance, boolean testLOS )
	{
		if( text == null ) throw new NullPointerException();
		
		this.player = player;
		this.text = text;
		this.color = color.clone();
		this.drawDistance = drawDistance;
		this.location = location.clone();
		this.testLOS = testLOS;
		
		initialize();
	}

	public PlayerLabel( Player player, String text, Color color, Location location, float drawDistance, boolean testLOS, Player attachedPlayer )
	{
		if( text == null ) throw new NullPointerException();
		
		this.player = player;
		this.text = text;
		this.color = color.clone();
		this.drawDistance = drawDistance;
		this.location = location.clone();
		this.testLOS = testLOS;
		this.attachedPlayer = attachedPlayer;
		
		initialize();
	}
	
	public PlayerLabel( Player player, String text, Color color, Location location, float drawDistance, boolean testLOS, Vehicle attachedVehicle )
	{
		if( text == null ) throw new NullPointerException();
		
		this.player = player;
		this.text = text;
		this.color = color.clone();
		this.drawDistance = drawDistance;
		this.location = location.clone();
		this.testLOS = testLOS;
		this.attachedVehicle = attachedVehicle;
		
		initialize();
	}
	
	private void initialize()
	{
		int playerId = Player.INVALID_ID, vehicleId = Vehicle.INVALID_ID;
		
		if( attachedPlayer != null )	playerId = attachedPlayer.getId();
		if( attachedVehicle != null )	vehicleId = attachedVehicle.getId();
		
		if( attachedPlayer != null || attachedVehicle != null )
		{
			offsetX = location.x;
			offsetY = location.y;
			offsetZ = location.z;
		}
		
		id = SampNativeFunction.createPlayer3DTextLabel( player.getId(), text, color.getValue(),
				location.x, location.y, location.z, drawDistance, playerId, vehicleId, testLOS );
		
		SampObjectPool pool = (SampObjectPool) Shoebill.getInstance().getManagedObjectPool();
		pool.setPlayerLabel( player, id, this );
	}
	
	
	@Override
	public void destroy()
	{
		SampNativeFunction.deletePlayer3DTextLabel( player.getId(), id );

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
	public Location getLocation()
	{
		Location pos = null;
		
		if( attachedPlayer != null )	pos = attachedPlayer.getLocation();
		if( attachedVehicle != null )	pos = attachedVehicle.getLocation();
		
		if( pos != null )
		{
			location.x = pos.x + offsetX;
			location.y = pos.y + offsetY;
			location.z = pos.z + offsetZ;
			location.interiorId = pos.interiorId;
			location.worldId = pos.worldId;
		}
		
		return location.clone();
	}

	@Override
	public void attach( IPlayer player, float x, float y, float z )
	{
		int playerId = this.player.getId();
		
		SampNativeFunction.deletePlayer3DTextLabel( playerId, id );
		id = SampNativeFunction.createPlayer3DTextLabel( playerId, text, color.getValue(), x, y, z, drawDistance, player.getId(), Vehicle.INVALID_ID, testLOS );
	}

	@Override
	public void attach( IVehicle vehicle, float x, float y, float z )
	{
		int playerId = this.player.getId();
		
		SampNativeFunction.deletePlayer3DTextLabel( playerId, id );
		id = SampNativeFunction.createPlayer3DTextLabel( playerId, text, color.getValue(), x, y, z, drawDistance, Player.INVALID_ID, vehicle.getId(), testLOS );
	}
	
	@Override
	public void update( Color color, String text )
	{
		this.color = color.clone();
		this.text = text;
		
		SampNativeFunction.updatePlayer3DTextLabelText( player.getId(), id, color.getValue(), text );
	}
}
