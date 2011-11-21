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

import net.gtaun.shoebill.samp.ISampCallbackManager;
import net.gtaun.shoebill.samp.SampCallbackManager;
import net.gtaun.shoebill.util.event.EventDispatcher;
import net.gtaun.shoebill.util.event.IEventDispatcher;


/**
 * @author MK124
 * 
 */

public class Shoebill implements IShoebill
{
	static Shoebill instance;
	public static IShoebill getInstance()		{ return instance; }
	
	
	EventDispatcher globalEventDispatcher;
	
	SampCallbackManager sampCallbackManager;
	SampObjectPool managedObjectPool;
	PluginManager pluginManager;
	
	SampEventLogger sampEventLogger;
	SampEventDispatcher sampEventDispatcher;


	Shoebill()
	{
		globalEventDispatcher = new EventDispatcher();
		
		sampCallbackManager = new SampCallbackManager();
		managedObjectPool = new SampObjectPool();
		pluginManager = new PluginManager();
		
		sampEventLogger = new SampEventLogger( managedObjectPool );
		sampEventDispatcher = new SampEventDispatcher( managedObjectPool, globalEventDispatcher );

		sampCallbackManager.registerCallbackHandler( sampEventLogger );
		sampCallbackManager.registerCallbackHandler( sampEventDispatcher );
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
	public ISampObjectPool getManagedObjectPool()
	{
		return managedObjectPool;
	}
	
	@Override
	public PluginManager getPluginManager()
	{
		return null;
	}
}
