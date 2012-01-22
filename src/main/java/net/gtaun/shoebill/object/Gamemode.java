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
import net.gtaun.shoebill.data.SpawnInfo;
import net.gtaun.shoebill.samp.SampNativeFunction;

/**
 * @author MK124, JoJLlmAn
 *
 */

public abstract class Gamemode extends Plugin implements IGamemode
{
	protected Gamemode()
	{
		
	}

	
	@Override
	public void exit()
	{
		SampNativeFunction.gameModeExit();
	}
	
	@Override
	public void setGamemodeText( String string )
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
	public int addPlayerClass( int modelId, float x, float y, float z, float angle, int weapon1, int ammo1, int weapon2, int ammo2, int weapon3, int ammo3 )
	{
		return SampNativeFunction.addPlayerClass( modelId, x, y, z, angle, weapon1, ammo1, weapon2, ammo2, weapon3, ammo3 );
	}

	@Override
	public int addPlayerClass( int modelId, SpawnInfo spawnInfo )
	{
		return SampNativeFunction.addPlayerClass( modelId, 
			spawnInfo.location.x, spawnInfo.location.y, spawnInfo.location.z, spawnInfo.location.angle,
			spawnInfo.weapon1.type.getId(), spawnInfo.weapon1.ammo, spawnInfo.weapon2.type.getId(), spawnInfo.weapon2.ammo,
			spawnInfo.weapon3.type.getId(), spawnInfo.weapon3.ammo );
	}
	
	@Override
	public int addPlayerClassEx( int teamId, int modelId, SpawnInfo spawnInfo )
	{
		return SampNativeFunction.addPlayerClassEx( teamId, modelId, 
			spawnInfo.location.x, spawnInfo.location.y, spawnInfo.location.z, spawnInfo.location.angle,
			spawnInfo.weapon1.type.getId(), spawnInfo.weapon1.ammo, spawnInfo.weapon2.type.getId(), spawnInfo.weapon2.ammo,
			spawnInfo.weapon3.type.getId(), spawnInfo.weapon3.ammo );
	}
}
