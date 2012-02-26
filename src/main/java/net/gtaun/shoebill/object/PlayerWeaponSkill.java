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

import net.gtaun.shoebill.data.type.WeaponSkill;
import net.gtaun.shoebill.samp.SampNativeFunction;

/**
 * @author JoJLlmAn
 *
 */

public class PlayerWeaponSkill implements IPlayerWeaponSkill
{
	private IPlayer player;
	private int[] skills = new int[ WeaponSkill.values().length ];
	
	
	PlayerWeaponSkill( IPlayer player )
	{
		this.player = player;
		for( int i=0; i<WeaponSkill.values().length; i++ )	skills[i] = 999;
	}
	
	
	@Override
	public IPlayer getPlayer()
	{
		return player;
	}
	
	@Override
	public void set( WeaponSkill type, int level )
	{
		if( player.isOnline() == false ) return;
		
		if(level > 999)		level = 999;
		else if(level < 0)	level = 0;
		
		int typeData = type.getData();
		SampNativeFunction.setPlayerSkillLevel( player.getId(), typeData, level );
		skills[typeData] = level;
	}
	
	@Override
	public int get( WeaponSkill type )
	{
		if( player.isOnline() == false ) return 0;
		
		return skills[ type.getData() ];
	}
}
