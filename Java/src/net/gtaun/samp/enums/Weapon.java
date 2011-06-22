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

package net.gtaun.samp.enums;

/**
 * @author MK124
 *
 */

public enum Weapon
{
	UNKNOWN,
	BRASSKNUCKLE,
	GOLFCLUB,
	NITESTICK,
	KNIFE,
	BAT,
	SHOVEL,
	POOLSTICK,
	KATANA,
	CHAINSAW,
	DILDO,
	DILDO2,
	VIBRATOR,
	VIBRATOR2,
	FLOWER,
	CANE,
	GRENADE,
	TEARGAS,
	MOLTOV,
	COLT45,
	SILENCED,
	DEAGLE,
	SHOTGUN,
	SAWEDOFF,
	SHOTGSPA,
	UZI,
	MP5,
	AK47,
	M4,
	TEC9,
	RIFLE,
	SNIPER,
	ROCKETLAUNCHER,
	HEATSEEKER,
	FLAMETHROWER,
	MINIGUN,
	SATCHEL,
	BOMB,
	SPRAYCAN,
	FIREEXTINGUISHER,
	CAMERA,
	PARACHUTE,
	VEHICLE,
	DROWN,
	COLLISION;
	
	
	static final int weaponid[] = {
		0,	1,	2,	3,	4,	5,	6,	7,	8,	9,
		10,	11,	12,	13,	14,	15,	16,	17,	18,	22,
		23,	24,	25,	26,	27,	28,	29,	30,	31,	32,
		33,	34,	35,	36,	37,	38,	39,	40,	41,	42,
		43,	46,	49,	53,	54		};
	
	public static Weapon get( int id )
	{
		for( int i=0; i<weaponid.length; i++ ) if( weaponid[i] == id ) return values()[i];
		return UNKNOWN;
	}
	
	
	public int id()
	{
		return weaponid[ this.ordinal() ];
	}
}
