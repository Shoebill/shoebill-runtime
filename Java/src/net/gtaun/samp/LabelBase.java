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

import java.util.Vector;

import net.gtaun.samp.data.Point;
import net.gtaun.samp.data.PointAngle;
import net.gtaun.samp.data.PointRange;

/**
 * @author MK124
 *
 */

public class LabelBase
{
	public static Vector<LabelBase> get()
	{
		return GameModeBase.getInstances(GameModeBase.instance.labelPool, LabelBase.class);
	}
	
	public static <T> Vector<T> get( Class<T> cls )
	{
		return GameModeBase.getInstances(GameModeBase.instance.labelPool, cls);
	}
	
	
	int id;
	String text;
	int color;
	PointRange position;
	boolean testLOS;
	
	float offsetX, offsetY, offsetZ;
	PlayerBase attachedPlayer;
	VehicleBase attachedVehicle;

	public int id()					{ return id; }
	public String text()			{ return text; }
	public int color()				{ return color; }
	
	public PlayerBase attachedPlayer()		{ return attachedPlayer; }
	public VehicleBase attachedVehicle()	{ return attachedVehicle; }
	

	LabelBase()
	{
		
	}
	
	public LabelBase( String text, int color, Point point, float drawDistance, boolean testLOS )
	{
		if( text == null ) throw new NullPointerException();
		
		this.text = text;
		this.color = color;
		this.position = new PointRange( point, drawDistance );
		this.testLOS = testLOS;
		
		init();
	}
	
	public LabelBase( String text, int color, PointRange point, boolean testLOS )
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
		id = NativeFunction.create3DTextLabel( text, color,
				position.x, position.y, position.z, position.distance, position.world, testLOS );
		
		GameModeBase.instance.labelPool[id] = this;
	}
	
//---------------------------------------------------------
	
	public void destroy()
	{
		NativeFunction.delete3DTextLabel( id );
		GameModeBase.instance.labelPool[ id ] = null;
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
		
		NativeFunction.attach3DTextLabelToPlayer( id, player.id, x, y, z );
		attachedPlayer = player;
		attachedVehicle = null;
	}
	
	public void attach( VehicleBase vehicle, float x, float y, float z )
	{
		offsetX = x;
		offsetY = y;
		offsetZ = z;
		
		NativeFunction.attach3DTextLabelToVehicle( id, vehicle.id, x, y, z );
		attachedPlayer = null;
		attachedVehicle = vehicle;
	}
	
	public void update( int color, String text )
	{
		if( text == null ) throw new NullPointerException();
		
		this.color = color;
		this.text = text;
		
		NativeFunction.update3DTextLabelText( id, color, text );
	}
}
