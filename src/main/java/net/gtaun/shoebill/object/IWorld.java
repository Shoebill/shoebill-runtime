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

import net.gtaun.shoebill.data.Location;
import net.gtaun.shoebill.data.type.PlayerMarkerMode;
import net.gtaun.shoebill.util.event.IEventDispatcher;

/**
 * @author MK124
 *
 */

public interface IWorld
{
	IEventDispatcher getEventDispatcher();
	
	float getChatRadius();
	void setChatRadius( float radius );
	
	float getPlayerMarkerRadius();
	void setPlayerMarkerRadius( float radius );

	int getWeather();
	void setWeather( int weatherid );

	float getGravity();
	void setGravity( float gravity );

	void setWorldTime( int hour );
	
	float getNameTagDrawDistance();
	void setNameTagDrawDistance( float distance );
	
	void showNameTags( boolean enabled );
	void showPlayerMarkers( PlayerMarkerMode mode );
	void enableTirePopping( boolean enabled );
	void allowInteriorWeapons( boolean allow );
	
	void createExplosion( Location location, int type, float radius );
	void enableZoneNames( boolean enabled );
	void usePlayerPedAnims();

	void disableInteriorEnterExits();
	void disableNameTagLOS();
}
