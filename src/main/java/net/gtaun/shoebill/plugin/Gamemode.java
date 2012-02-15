/**
 * Copyright (C) 2012 MK124
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

package net.gtaun.shoebill.plugin;

import net.gtaun.shoebill.SampObjectPool;
import net.gtaun.shoebill.object.IPlayer;
import net.gtaun.shoebill.samp.SampNativeFunction;

/**
 * @author MK124
 *
 */

public abstract class Gamemode extends Plugin
{
	protected Gamemode()
	{
		
	}
	
	protected Gamemode( Class<? extends IPlayer> cls )
	{
		SampObjectPool pool = (SampObjectPool)(getShoebill().getManagedObjectPool());
		pool.setPlayerClass( cls );
	}
	
	
	public void exit()
	{
		try
		{
			disable();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		
		SampNativeFunction.gameModeExit();
	}
	
	public void setGamemodeText( String string )
	{
		if( string == null ) throw new NullPointerException();
		SampNativeFunction.setGameModeText( string );
	}
}
