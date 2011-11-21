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

import net.gtaun.shoebill.data.Point;
import net.gtaun.shoebill.data.SpawnInfo;
import net.gtaun.shoebill.samp.SampNativeFunction;
import net.gtaun.shoebill.util.event.EventDispatcher;
import net.gtaun.shoebill.util.event.IEventDispatcher;

/**
 * @author MK124, JoJLlmAn
 *
 */

public abstract class Gamemode
{
	EventDispatcher eventDispatcher = new EventDispatcher();
	
	int deathDropAmount = 0;
	float nameTagDrawDistance = 70;
	float chatRadius = -1;
	float playerMarkerRadius = -1;
	
	
	public IEventDispatcher getEventDispatcher()		{ return eventDispatcher; }
	
	public int getDeathDropAmount()						{ return deathDropAmount; }
	public float getNameTagDrawDistance()				{ return nameTagDrawDistance; }
	public float getChatRadius()						{ return chatRadius; }
	public float getPlayerMarkerRadius()				{ return playerMarkerRadius;}
	

	protected Gamemode()
	{
		
	}

	
//--------------------------------------------------------- 
	
	public int weather()
	{
		return SampNativeFunction.getServerVarAsInt("weather");

	}
	
	public float gravity()
	{
		return Float.parseFloat(SampNativeFunction.getServerVarAsString("gravity"));
	}
	
	public int serverCodepage()
	{
		return SampNativeFunction.getServerCodepage();
	}
	
	public void setServerCodepage( int codepage )
	{
		SampNativeFunction.setServerCodepage( codepage );
	}
	
	public void setGameModeText( String string )
	{
		if( string == null ) throw new NullPointerException();
		SampNativeFunction.setGameModeText( string );
	}
	
	public void setTeamCount( int count )
	{
		SampNativeFunction.setTeamCount( count );
	}

	public int addPlayerClass( int model, float x, float y, float z, float angle, int weapon1, int ammo1, int weapon2, int ammo2, int weapon3, int ammo3 )
	{
		return SampNativeFunction.addPlayerClass( model, x, y, z, angle, weapon1, ammo1, weapon2, ammo2, weapon3, ammo3 );
	}

	public int addPlayerClass( int model, SpawnInfo spawninfo )
	{
		return SampNativeFunction.addPlayerClass( model, 
			spawninfo.position.x, spawninfo.position.y, spawninfo.position.z, spawninfo.position.angle,
			spawninfo.weapon1.id, spawninfo.weapon1.ammo, spawninfo.weapon2.id, spawninfo.weapon2.ammo,
			spawninfo.weapon3.id, spawninfo.weapon3.ammo );
	}
	
	public int addPlayerClassEx( int team, int model, SpawnInfo spawninfo )
	{
		return SampNativeFunction.addPlayerClassEx( team, model, 
				spawninfo.position.x, spawninfo.position.y, spawninfo.position.z, spawninfo.position.angle,
				spawninfo.weapon1.id, spawninfo.weapon1.ammo, spawninfo.weapon2.id, spawninfo.weapon2.ammo,
				spawninfo.weapon3.id, spawninfo.weapon3.ammo );
	}

	public void showNameTags( boolean enabled )
	{
		SampNativeFunction.showNameTags( enabled );
	}
	
	public void showPlayerMarkers( int mode )
	{
		SampNativeFunction.showPlayerMarkers( mode );
	}
	
	public void setWorldTime( int hour )
	{
		SampNativeFunction.setWorldTime( hour );
	}
	
	public void enableTirePopping( boolean enabled )
	{
		SampNativeFunction.enableTirePopping( enabled );
	}
	
	public void allowInteriorWeapons( boolean allow )
	{
		SampNativeFunction.allowInteriorWeapons( allow );
	}
	
	public void setWeather( int weatherid )
	{
		SampNativeFunction.setWeather( weatherid );
	}
	
	public void setGravity( float gravity )
	{
		SampNativeFunction.setGravity( gravity );
	}
	
	public void setDeathDropAmount( int amount )
	{
		SampNativeFunction.setDeathDropAmount( amount );
		deathDropAmount = amount;
	}
	
	public void createExplosion( Point point, int type, float radius )
	{
		SampNativeFunction.createExplosion( point.x, point.y, point.z, type, radius );
	}
	
	public void enableZoneNames( boolean enabled )
	{
		SampNativeFunction.enableZoneNames( enabled );
	}
	
	public void usePlayerPedAnims()
	{
		SampNativeFunction.usePlayerPedAnims();
	}
	
	public void disableInteriorEnterExits()
	{
		SampNativeFunction.disableInteriorEnterExits();
	}
	
	public void setNameTagDrawDistance( float distance )
	{
		nameTagDrawDistance = distance;
		SampNativeFunction.setNameTagDrawDistance( distance );
	}
	
	public void disableNameTagLOS()
	{
		SampNativeFunction.disableNameTagLOS();
	}

	public void sendRconCommand( String command )
	{
		if( command == null ) throw new NullPointerException();
		SampNativeFunction.sendRconCommand( command );
	}
	
	public String getServerVarAsString( String varname )
	{
		if( varname == null ) throw new NullPointerException();
		return SampNativeFunction.getServerVarAsString( varname );
	}
	
	public int getServerVarAsInt( String varname )
	{
		if( varname == null ) throw new NullPointerException();
		return SampNativeFunction.getServerVarAsInt( varname );
	}
	
	public boolean getServerVarAsBool( String varname )
	{
		if( varname == null ) throw new NullPointerException();
		return SampNativeFunction.getServerVarAsBool( varname );
	}
	
	public void connectNPC( String name, String script )
	{
		if( name == null || script == null ) throw new NullPointerException();
		SampNativeFunction.connectNPC( name, script );
	}
	
	public void exit()
	{
		SampNativeFunction.gameModeExit();
	}
	
	public String getNetworkStats()
	{
		return SampNativeFunction.getNetworkStats();
	}
}
