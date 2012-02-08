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

import java.io.File;

import net.gtaun.shoebill.object.SampEventDispatcher;
import net.gtaun.shoebill.object.Server;
import net.gtaun.shoebill.object.World;
import net.gtaun.shoebill.plugin.PluginManager;
import net.gtaun.shoebill.samp.ISampCallbackHandler;
import net.gtaun.shoebill.samp.ISampCallbackManager;
import net.gtaun.shoebill.samp.SampCallbackManager;
import net.gtaun.shoebill.util.event.EventManager;
import net.gtaun.shoebill.util.event.IEventManager;

/**
 * @author MK124
 * 
 */

public class Shoebill implements IShoebill
{
	private static Shoebill instance;
	public static IShoebill getInstance()		{ return instance; }
	
	
	private EventManager globalEventDispatcher;
	
	private SampCallbackManager sampCallbackManager;
	private SampObjectPool managedObjectPool;
	private PluginManager pluginManager;
	
	private SampEventLogger sampEventLogger;
	private SampEventDispatcher sampEventDispatcher;

	
	@Override public IEventManager getEventManager()				{ return globalEventDispatcher; }
	@Override public ISampCallbackManager getCallbackManager()		{ return sampCallbackManager; }
	@Override public ISampObjectPool getManagedObjectPool()			{ return managedObjectPool; }
	@Override public PluginManager getPluginManager()				{ return pluginManager; }
	
	
	Shoebill()
	{
		instance = this;
		
		globalEventDispatcher = new EventManager();
		sampCallbackManager = new SampCallbackManager();
		managedObjectPool = new SampObjectPool();
		pluginManager = new PluginManager(this, new File("shoebill/plugins/"), new File("shoebill/data/"));
		
		sampEventLogger = new SampEventLogger( managedObjectPool );
		sampEventDispatcher = new SampEventDispatcher( managedObjectPool, globalEventDispatcher );
		
		initialize();
	}
	
	@Override
	protected void finalize() throws Throwable
	{
		instance = null;
	}
	
	private void initialize()
	{
		managedObjectPool.setServer( new Server() );
		managedObjectPool.setWorld( new World() );
		
		sampCallbackManager.registerCallbackHandler( managedObjectPool.getCallbackHandler() );
		sampCallbackManager.registerCallbackHandler( sampEventDispatcher );
		sampCallbackManager.registerCallbackHandler( sampEventLogger );
	}
	
	public ISampCallbackHandler getCallbackHandler()
	{
		return sampCallbackManager.getMasterCallbackHandler();
	}
}
