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

package net.gtaun.shoebill.object.impl;

import net.gtaun.shoebill.data.Location;
import net.gtaun.shoebill.data.LocationAngle;
import net.gtaun.shoebill.data.SpawnInfo;
import net.gtaun.shoebill.data.WeaponData;
import net.gtaun.shoebill.data.type.PlayerMarkerMode;
import net.gtaun.shoebill.object.IWorld;
import net.gtaun.shoebill.samp.SampNativeFunction;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author MK124
 *
 */

public class WorldImpl implements IWorld
{
	private float nameTagDrawDistance = 70;
	private float chatRadius = -1;
	private float playerMarkerRadius = -1;
	
	
	public WorldImpl()
	{
		
	}

	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
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
		LocationAngle loc = spawnInfo.getLocation();
		WeaponData weapon1 = spawnInfo.getWeapon1();
		WeaponData weapon2 = spawnInfo.getWeapon2();
		WeaponData weapon3 = spawnInfo.getWeapon3();
		
		return SampNativeFunction.addPlayerClass( modelId, loc.getX(), loc.getY(), loc.getZ(), loc.getAngle(), weapon1.getType().getId(), weapon1.getAmmo(), weapon2.getType().getId(), weapon2.getAmmo(), weapon3.getType().getId(), weapon3.getAmmo() );
	}
	
	@Override
	public int addPlayerClassEx( int teamId, int modelId, SpawnInfo spawnInfo )
	{
		LocationAngle loc = spawnInfo.getLocation();
		WeaponData weapon1 = spawnInfo.getWeapon1();
		WeaponData weapon2 = spawnInfo.getWeapon2();
		WeaponData weapon3 = spawnInfo.getWeapon3();
		
		return SampNativeFunction.addPlayerClassEx( teamId, modelId, loc.getX(), loc.getY(), loc.getZ(), loc.getAngle(), weapon1.getType().getId(), weapon1.getAmmo(), weapon2.getType().getId(), weapon2.getAmmo(), weapon3.getType().getId(), weapon3.getAmmo() );
	}

	@Override
	public float getChatRadius()
	{
		return chatRadius;
	}
	
	@Override
	public void setChatRadius( float radius )
	{
		SampNativeFunction.limitGlobalChatRadius( radius );
	}

	@Override
	public float getPlayerMarkerRadius()
	{
		return playerMarkerRadius;
	}
	
	@Override
	public void setPlayerMarkerRadius( float radius )
	{
		SampNativeFunction.limitPlayerMarkerRadius( radius );
	}
	
	@Override public int getWeatherId()
	{
		return SampNativeFunction.getServerVarAsInt("weather");
	}

	@Override
	public void setWeatherId( int weatherid )
	{
		SampNativeFunction.setWeather( weatherid );
	}
	
	@Override public float getGravity()
	{
		return Float.parseFloat(SampNativeFunction.getServerVarAsString("gravity"));
	}
	
	@Override
	public void setGravity( float gravity )
	{
		SampNativeFunction.setGravity( gravity );
	}
	
	@Override
	public void setWorldTime( int hour )
	{
		SampNativeFunction.setWorldTime( hour );
	}

	@Override
	public float getNameTagDrawDistance()
	{
		return nameTagDrawDistance;
	}

	@Override
	public void setNameTagDrawDistance( float distance )
	{
		nameTagDrawDistance = distance;
		SampNativeFunction.setNameTagDrawDistance( distance );
	}
	
	@Override
	public void showNameTags( boolean enabled )
	{
		SampNativeFunction.showNameTags( enabled );
	}
	
	@Override
	public void showPlayerMarkers( PlayerMarkerMode mode )
	{
		SampNativeFunction.showPlayerMarkers( mode.getData() );
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
	public void createExplosion( Location location, int type, float radius )
	{
		SampNativeFunction.createExplosion( location.getX(), location.getY(), location.getZ(), type, radius );
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
	public void disableNameTagLOS()
	{
		SampNativeFunction.disableNameTagLOS();
	}
}
