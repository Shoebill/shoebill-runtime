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

package net.gtaun.samp;

/**
 * @author JoJLlmAn
 *
 */

public class PlayerSkill {
	
	public static final int WEAPONSKILL_PISTOL = 					0;
	public static final int WEAPONSKILL_PISTOL_SILENCED =			1;
	public static final int WEAPONSKILL_DESERT_EAGLE =				2;
	public static final int WEAPONSKILL_SHOTGUN =					3;
	public static final int WEAPONSKILL_SAWNOFF_SHOTGUN =			4;
	public static final int WEAPONSKILL_SPAS12_SHOTGUN =			5;
	public static final int WEAPONSKILL_MICRO_UZI =					6;
	public static final int WEAPONSKILL_MP5 =						7;
	public static final int WEAPONSKILL_AK47 =						8;
	public static final int WEAPONSKILL_M4 =						9;
	public static final int WEAPONSKILL_SNIPERRIFLE =				10;
	
	int[] skills = new int[11];
	int playerid;
	
	public PlayerSkill(int playerid)
	{
		for(int i=0;i<11;i++)
			skills[i] = 999;
		
		this.playerid = playerid;
	}
	
	public void set(int type, int level)
	{
		if(level > 999)
			level = 999;
		else if(level < 0)
			level = 0;
		
		NativeFunction.setPlayerSkillLevel(playerid, type, level);
		skills[type] = level;
	}
	
	public int get(int type)
	{
		return skills[type];
	}
}
