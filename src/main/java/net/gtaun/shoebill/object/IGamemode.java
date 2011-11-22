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

package net.gtaun.shoebill.object;

import net.gtaun.shoebill.data.Point;
import net.gtaun.shoebill.data.SpawnInfo;
import net.gtaun.shoebill.util.event.IEventDispatcher;

/**
 * @author MK124
 *
 */

public interface IGamemode
{
	IEventDispatcher getEventDispatcher();
	
	int getDeathDropAmount();
	float getNameTagDrawDistance();
	float getChatRadius();
	float getPlayerMarkerRadius();
	
	int getWeather();
	float getGravity();
	int getServerCodepage();
	void setServerCodepage( int codepage );
	void setGameModeText( String string );
	void setTeamCount( int count );
	int addPlayerClass( int model, float x, float y, float z, float angle, int weapon1, int ammo1, int weapon2, int ammo2, int weapon3, int ammo3 );
	int addPlayerClass( int model, SpawnInfo spawninfo );
	int addPlayerClassEx( int team, int model, SpawnInfo spawninfo );
	void showNameTags( boolean enabled );
	void showPlayerMarkers( int mode );
	void setWorldTime( int hour );
	void enableTirePopping( boolean enabled );
	void allowInteriorWeapons( boolean allow );
	void setWeather( int weatherid );
	void setGravity( float gravity );
	void setDeathDropAmount( int amount );
	void createExplosion( Point point, int type, float radius );
	void enableZoneNames( boolean enabled );
	void usePlayerPedAnims();
	void disableInteriorEnterExits();
	void setNameTagDrawDistance( float distance );
	void disableNameTagLOS();
	void sendRconCommand( String command );
	String getServerVarAsString( String varname );
	int getServerVarAsInt( String varname );
	boolean getServerVarAsBool( String varname );
	void connectNPC( String name, String script );
	void exit();
	String getNetworkStats();
}
