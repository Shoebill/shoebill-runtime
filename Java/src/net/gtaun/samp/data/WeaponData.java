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

package net.gtaun.samp.data;

/**
 * @author MK124
 *
 */

public class WeaponData
{
	public static final int WEAPON_BRASSKNUCKLE =				1;
	public static final int WEAPON_GOLFCLUB =					2;
	public static final int WEAPON_NITESTICK =					3;
	public static final int WEAPON_KNIFE =						4;
	public static final int WEAPON_BAT =						5;
	public static final int WEAPON_SHOVEL =						6;
	public static final int WEAPON_POOLSTICK =					7;
	public static final int WEAPON_KATANA =						8;
	public static final int WEAPON_CHAINSAW =					9;
	public static final int WEAPON_DILDO =						10;
	public static final int WEAPON_DILDO2 =						11;
	public static final int WEAPON_VIBRATOR =					12;
	public static final int WEAPON_VIBRATOR2 =					13;
	public static final int WEAPON_FLOWER =						14;
	public static final int WEAPON_CANE =						15;
	public static final int WEAPON_GRENADE =					16;
	public static final int WEAPON_TEARGAS =					17;
	public static final int WEAPON_MOLTOV =						18;
	public static final int WEAPON_COLT45 =						22;
	public static final int WEAPON_SILENCED =					23;
	public static final int WEAPON_DEAGLE =						24;
	public static final int WEAPON_SHOTGUN =					25;
	public static final int WEAPON_SAWEDOFF =					26;
	public static final int WEAPON_SHOTGSPA =					27;
	public static final int WEAPON_UZI =						28;
	public static final int WEAPON_MP5 =						29;
	public static final int WEAPON_AK47 =						30;
	public static final int WEAPON_M4 =							31;
	public static final int WEAPON_TEC9 =						32;
	public static final int WEAPON_RIFLE =						33;
	public static final int WEAPON_SNIPER =						34;
	public static final int WEAPON_ROCKETLAUNCHER =				35;
	public static final int WEAPON_HEATSEEKER =					36;
	public static final int WEAPON_FLAMETHROWER =				37;
	public static final int WEAPON_MINIGUN =					38;
	public static final int WEAPON_SATCHEL =					39;
	public static final int WEAPON_BOMB =						40;
	public static final int WEAPON_SPRAYCAN =					41;
	public static final int WEAPON_FIREEXTINGUISHER =			42;
	public static final int WEAPON_CAMERA =						43;
	public static final int WEAPON_PARACHUTE =					46;
	public static final int WEAPON_VEHICLE =					49;
	public static final int WEAPON_DROWN =						53;
	public static final int WEAPON_COLLISION =					54;
	
	
	public int id, ammo;
	

	public WeaponData()
	{
		
	}
	
	public WeaponData( int id, int ammo )
	{
		this.id = id;
		this.ammo = ammo;
	}
	
	
	public boolean equals( Object obj )
	{
		if( obj == this )					return true;
		if( !(obj instanceof WeaponData) )	return false;
		
		WeaponData data = (WeaponData) obj;
		if( data.id != id )					return false;
		if( data.ammo != ammo )				return false;
		
		return true;
	}
	
	public WeaponData clone()
	{
		return new WeaponData(id, ammo);
	}
}
