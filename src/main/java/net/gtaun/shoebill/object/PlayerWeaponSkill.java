/**
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

import net.gtaun.shoebill.data.type.WeaponSkillType;
import net.gtaun.shoebill.samp.SampNativeFunction;

/**
 * @author JoJLlmAn
 *
 */

public class PlayerWeaponSkill implements IPlayerWeaponSkill
{
	private int[] skills = new int[ WeaponSkillType.values().length ];
	private int playerId;
	
	
	PlayerWeaponSkill( int playerid )
	{
		for( int i=0; i<WeaponSkillType.values().length; i++ )	skills[i] = 999;
		this.playerId = playerid;
	}
	
	
	public void set( WeaponSkillType type, int level )
	{
		if(level > 999)		level = 999;
		else if(level < 0)	level = 0;
		
		SampNativeFunction.setPlayerSkillLevel( playerId, type.getData(), level );
		skills[ type.getData() ] = level;
	}
	
	public int get( WeaponSkillType type )
	{
		return skills[ type.getData() ];
	}
}
