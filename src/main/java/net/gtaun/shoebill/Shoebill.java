/**
 * Copyright (C) 2011 MK124
 *
 * Licensed under the Apache License, Version 2.0 (the "License"){}
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

package net.gtaun.shoebill;

import net.gtaun.lungfish.IObjectPool;
import net.gtaun.lungfish.IPluginManager;
import net.gtaun.lungfish.ISampCallbackManager;
import net.gtaun.lungfish.IShoebill;
import net.gtaun.lungfish.samp.ISampCallback;
import net.gtaun.lungfish.util.event.EventDispatcher;
import net.gtaun.lungfish.util.event.IEventDispatcher;


/**
 * @author MK124
 * 
 */

public class Shoebill implements IShoebill
{
	EventDispatcher globalEventDispatcher;
	
	SampCallbackManager sampCallbackManager;
	SampObjectPool managedObjectPool;
	PluginManager pluginManager;


	Shoebill()
	{
		globalEventDispatcher = new EventDispatcher();
		
		sampCallbackManager = new SampCallbackManager();
		managedObjectPool = new SampObjectPool();
		pluginManager = new PluginManager();
		
		sampCallbackManager.registerCallbackHandler( sampCallbackHandler );
	}

	
	@Override
	public IEventDispatcher getGlobalEventDispatcher()
	{
		return globalEventDispatcher;
	}
	
	
	@Override
	public ISampCallbackManager getCallbackManager()
	{
		return sampCallbackManager;
	}

	@Override
	public IObjectPool getManagedObjectPool()
	{
		return managedObjectPool;
	}
	
	@Override
	public IPluginManager getPluginManager()
	{
		return null;
	}
	
	
	ISampCallback sampCallbackHandler = new ISampCallback()
	{
		public int onVehicleStreamOut( int arg0, int arg1 )
		{
			
			return 0;
		}
		
		public int onVehicleStreamIn( int arg0, int arg1 )
		{

			return 0;
		}
		
		public int onVehicleSpawn( int arg0 )
		{

			return 0;
		}
		
		public int onVehicleRespray( int arg0, int arg1, int arg2, int arg3 )
		{
			
			return 0;
		}
		
		public int onVehiclePaintjob( int arg0, int arg1, int arg2 )
		{
			
			return 0;
		}
		
		public int onVehicleMod( int arg0, int arg1, int arg2 )
		{
			
			return 0;
		}
		
		public int onVehicleDeath( int arg0, int arg1 )
		{
			
			return 0;
		}
		
		public int onVehicleDamageStatusUpdate( int arg0, int arg1 )
		{
			
			return 0;
		}
		
		public int onUnoccupiedVehicleUpdate( int arg0, int arg1, int arg2 )
		{
			
			return 0;
		}
		
		public int onRconLoginAttempt( String arg0, String arg1, int arg2 )
		{
			
			return 0;
		}
		
		public int onRconCommand( String arg0 )
		{
			
			return 0;
		}
		
		public void onProcessTick()
		{
			
			
		}
		
		public int onPlayerUpdate( int arg0 )
		{
			
			return 0;
		}
		
		public int onPlayerText( int arg0, String arg1 )
		{
			
			return 0;
		}
		
		public int onPlayerStreamOut( int arg0, int arg1 )
		{
			
			return 0;
		}
		
		public int onPlayerStreamIn( int arg0, int arg1 )
		{
			
			return 0;
		}
		
		public int onPlayerStateChange( int arg0, int arg1, int arg2 )
		{
			
			return 0;
		}
		
		public int onPlayerSpawn( int arg0 )
		{
			
			return 0;
		}
		
		public int onPlayerSelectedMenuRow( int arg0, int arg1 )
		{
			
			return 0;
		}

		public int onPlayerRequestSpawn( int arg0 )
		{
			
			return 0;
		}
		
		public int onPlayerRequestClass( int arg0, int arg1 )
		{
			
			return 0;
		}
		
		public int onPlayerPickUpPickup( int arg0, int arg1 )
		{
			
			return 0;
		}
		
		public int onPlayerObjectMoved( int arg0, int arg1 )
		{
			
			return 0;
		}
		
		public int onPlayerLeaveRaceCheckpoint( int arg0 )
		{
			
			return 0;
		}
		
		public int onPlayerLeaveCheckpoint( int arg0 )
		{
			
			return 0;
		}
		
		public int onPlayerKeyStateChange( int arg0, int arg1, int arg2 )
		{
			
			return 0;
		}
		
		public int onPlayerInteriorChange( int arg0, int arg1, int arg2 )
		{
			
			return 0;
		}
		
		public int onPlayerExitedMenu( int arg0 )
		{
			
			return 0;
		}
		
		public int onPlayerExitVehicle( int arg0, int arg1 )
		{
			
			return 0;
		}
		
		public int onPlayerEnterVehicle( int arg0, int arg1, int arg2 )
		{
			
			return 0;
		}
		
		public int onPlayerEnterRaceCheckpoint( int arg0 )
		{
			
			return 0;
		}
		
		public int onPlayerEnterCheckpoint( int arg0 )
		{
			
			return 0;
		}
		
		public int onPlayerDisconnect( int arg0, int arg1 )
		{
			
			return 0;
		}
		
		public int onPlayerDeath( int arg0, int arg1, int arg2 )
		{
			
			return 0;
		}
		
		public int onPlayerConnect( int arg0 )
		{
			
			return 0;
		}
		
		public int onPlayerCommandText( int arg0, String arg1 )
		{
			
			return 0;
		}
		
		public int onPlayerClickPlayer( int arg0, int arg1, int arg2 )
		{
			
			return 0;
		}
		
		public int onObjectMoved( int arg0 )
		{
			
			return 0;
		}
		
		public int onGameModeInit()
		{
			
			return 0;
		}
		
		public int onGameModeExit()
		{
			
			return 0;
		}
		
		public int onEnterExitModShop( int arg0, int arg1, int arg2 )
		{
			
			return 0;
		}
		
		public int onDialogResponse( int arg0, int arg1, int arg2, int arg3, String arg4 )
		{
			
			return 0;
		}
	};
}
