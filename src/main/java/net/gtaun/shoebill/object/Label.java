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
import net.gtaun.shoebill.util.event.EventDispatcher;
import net.gtaun.shoebill.util.event.IEventDispatcher;

/**
 * @author MK124
 *
 */

public class Label implements ILabel
{
	public static Collection<ILabel> get()
	{
		return Shoebill.getInstance().getManagedObjectPool().getLabels();
	}
	
	public static <T extends ILabel> Collection<T> get( Class<T> cls )
	{
		return Shoebill.getInstance().getManagedObjectPool().getLabels( cls );
	}
	
	
	private EventDispatcher eventDispatcher = new EventDispatcher();
	
	private int id = -1;
	private String text;
	private Color color;
	private Location location;
	private float drawDistance;
	private boolean testLOS;
	
	private float offsetX, offsetY, offsetZ;
	private IPlayer attachedPlayer;
	private IVehicle attachedVehicle;

	@Override public IEventDispatcher getEventDispatcher()		{ return eventDispatcher; }
	
	@Override public int getId()								{ return id; }
	@Override public String getText()							{ return text; }
	@Override public Color getColor()							{ return color.clone(); }
	@Override public float getDrawDistance()					{ return drawDistance; }
	@Override public IPlayer getAttachedPlayer()				{ return attachedPlayer; }
	@Override public IVehicle getAttachedVehicle()				{ return attachedVehicle; }
	
	
	public Label( String text, Color color, Location location, float drawDistance, boolean testLOS )
	{
		if( text == null ) throw new NullPointerException();
		
		this.text = text;
		this.color = color.clone();
		this.location = location.clone();
		this.drawDistance = drawDistance;
		this.testLOS = testLOS;
		
		initialize();
	}
	
	private void initialize()
	{
		id = SampNativeFunction.create3DTextLabel( text, color.getValue(),
				location.x, location.y, location.z, drawDistance, location.worldId, testLOS );
		
		SampObjectPool pool = (SampObjectPool) Shoebill.getInstance().getManagedObjectPool();
		pool.setLabel( id, this );
	}
	
	
	@Override
	public void destroy()
	{
		SampNativeFunction.delete3DTextLabel( id );
		
		SampObjectPool pool = (SampObjectPool) Shoebill.getInstance().getManagedObjectPool();
		pool.setLabel( id, null );

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
		offsetX = x;
		offsetY = y;
		offsetZ = z;
		
		SampNativeFunction.attach3DTextLabelToPlayer( id, player.getId(), x, y, z );
		attachedPlayer = player;
		attachedVehicle = null;
	}

	@Override
	public void attach( IVehicle vehicle, float x, float y, float z )
	{
		offsetX = x;
		offsetY = y;
		offsetZ = z;
		
		SampNativeFunction.attach3DTextLabelToVehicle( id, vehicle.getId(), x, y, z );
		attachedPlayer = null;
		attachedVehicle = vehicle;
	}

	@Override
	public void update( Color color, String text )
	{
		if( text == null ) throw new NullPointerException();
		
		this.color = color.clone();
		this.text = text;
		
		SampNativeFunction.update3DTextLabelText( id, color.getValue(), text );
	}
}
