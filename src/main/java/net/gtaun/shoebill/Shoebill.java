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
import net.gtaun.lungfish.samp.ISampFunction;
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
	ISampFunction sampFunction;
	SampObjectPool managedObjectPool;
	PluginManager pluginManager;


	Shoebill()
	{
		globalEventDispatcher = new EventDispatcher();
		sampCallbackManager = new SampCallbackManager();
		managedObjectPool = new SampObjectPool();
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
	public ISampFunction getSampFunction()
	{
		return sampFunction;
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
}
