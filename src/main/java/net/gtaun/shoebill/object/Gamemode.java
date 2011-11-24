/**
 * Copyright (C) 2011 MK124
 * Copyright (C) 2011 JoJLlmAn
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

import net.gtaun.shoebill.Plugin;
import net.gtaun.shoebill.data.Point;
import net.gtaun.shoebill.data.SpawnInfo;
import net.gtaun.shoebill.samp.SampNativeFunction;
import net.gtaun.shoebill.util.event.EventDispatcher;
import net.gtaun.shoebill.util.event.IEventDispatcher;

/**
 * @author MK124, JoJLlmAn
 *
 */

public abstract class Gamemode extends Plugin implements IGamemode
{
	private EventDispatcher eventDispatcher = new EventDispatcher();
	
	private int deathDropAmount = 0;
	private float nameTagDrawDistance = 70;
	private float chatRadius = -1;
	private float playerMarkerRadius = -1;
	
	
	@Override public IEventDispatcher getEventDispatcher()		{ return eventDispatcher; }
	
	@Override public int getDeathDropAmount()					{ return deathDropAmount; }
	@Override public float getNameTagDrawDistance()				{ return nameTagDrawDistance; }
	@Override public float getChatRadius()						{ return chatRadius; }
	@Override public float getPlayerMarkerRadius()				{ return playerMarkerRadius;}

	@Override public int getWeather()							{ return SampNativeFunction.getServerVarAsInt("weather"); }
	@Override public float getGravity()							{ return Float.parseFloat(SampNativeFunction.getServerVarAsString("gravity")); }
	
	@Override public int getServerCodepage()					{ return SampNativeFunction.getServerCodepage(); }
	

	protected Gamemode()
	{
		
	}

	
	@Override
	public void setGameModeText( String string )
	{
		if( string == null ) throw new NullPointerException();
		SampNativeFunction.setGameModeText( string );
	}
	
	@Override
	public void setTeamCount( int count )
	{
		SampNativeFunction.setTeamCount( count );
	}

	@Override
	public int addPlayerClass( int model, float x, float y, float z, float angle, int weapon1, int ammo1, int weapon2, int ammo2, int weapon3, int ammo3 )
	{
		return SampNativeFunction.addPlayerClass( model, x, y, z, angle, weapon1, ammo1, weapon2, ammo2, weapon3, ammo3 );
	}

	@Override
	public int addPlayerClass( int model, SpawnInfo spawninfo )
	{
		return SampNativeFunction.addPlayerClass( model, 
			spawninfo.position.x, spawninfo.position.y, spawninfo.position.z, spawninfo.position.angle,
			spawninfo.weapon1.id, spawninfo.weapon1.ammo, spawninfo.weapon2.id, spawninfo.weapon2.ammo,
			spawninfo.weapon3.id, spawninfo.weapon3.ammo );
	}
	
	@Override
	public int addPlayerClassEx( int team, int model, SpawnInfo spawninfo )
	{
		return SampNativeFunction.addPlayerClassEx( team, model, 
			spawninfo.position.x, spawninfo.position.y, spawninfo.position.z, spawninfo.position.angle,
			spawninfo.weapon1.id, spawninfo.weapon1.ammo, spawninfo.weapon2.id, spawninfo.weapon2.ammo,
			spawninfo.weapon3.id, spawninfo.weapon3.ammo );
	}

	@Override
	public void showNameTags( boolean enabled )
	{
		SampNativeFunction.showNameTags( enabled );
	}
	
	@Override
	public void showPlayerMarkers( int mode )
	{
		SampNativeFunction.showPlayerMarkers( mode );
	}
	
	@Override
	public void setWorldTime( int hour )
	{
		SampNativeFunction.setWorldTime( hour );
	}
	
	@Override
	public void enableTirePopping( boolean enabled )
	{
		SampNativeFunction.enableTirePopping( enabled );
	}
	
	@Override
	public void allowInteriorWeapons( boolean allow )
	{
		SampNativeFunction.allowInteriorWeapons( allow );
	}
	
	@Override
	public void setWeather( int weatherid )
	{
		SampNativeFunction.setWeather( weatherid );
	}
	
	@Override
	public void setGravity( float gravity )
	{
		SampNativeFunction.setGravity( gravity );
	}
	
	@Override
	public void setDeathDropAmount( int amount )
	{
		SampNativeFunction.setDeathDropAmount( amount );
		deathDropAmount = amount;
	}
	
	@Override
	public void createExplosion( Point point, int type, float radius )
	{
		SampNativeFunction.createExplosion( point.x, point.y, point.z, type, radius );
	}
	
	@Override
	public void enableZoneNames( boolean enabled )
	{
		SampNativeFunction.enableZoneNames( enabled );
	}
	
	@Override
	public void usePlayerPedAnims()
	{
		SampNativeFunction.usePlayerPedAnims();
	}
	
	@Override
	public void disableInteriorEnterExits()
	{
		SampNativeFunction.disableInteriorEnterExits();
	}
	
	@Override
	public void setNameTagDrawDistance( float distance )
	{
		nameTagDrawDistance = distance;
		SampNativeFunction.setNameTagDrawDistance( distance );
	}
	
	@Override
	public void disableNameTagLOS()
	{
		SampNativeFunction.disableNameTagLOS();
	}

	@Override
	public void sendRconCommand( String command )
	{
		if( command == null ) throw new NullPointerException();
		SampNativeFunction.sendRconCommand( command );
	}
	
	@Override
	public String getServerVarAsString( String varname )
	{
		if( varname == null ) throw new NullPointerException();
		return SampNativeFunction.getServerVarAsString( varname );
	}
	
	@Override
	public int getServerVarAsInt( String varname )
	{
		if( varname == null ) throw new NullPointerException();
		return SampNativeFunction.getServerVarAsInt( varname );
	}
	
	@Override
	public boolean getServerVarAsBool( String varname )
	{
		if( varname == null ) throw new NullPointerException();
		return SampNativeFunction.getServerVarAsBool( varname );
	}
	
	@Override
	public void connectNPC( String name, String script )
	{
		if( name == null || script == null ) throw new NullPointerException();
		SampNativeFunction.connectNPC( name, script );
	}
	
	@Override
	public void exit()
	{
		SampNativeFunction.gameModeExit();
	}
	
	@Override
	public String getNetworkStats()
	{
		return SampNativeFunction.getNetworkStats();
	}
}
