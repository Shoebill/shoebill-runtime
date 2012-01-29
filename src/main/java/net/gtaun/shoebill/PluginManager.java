/**
 * Copyright (C) 2011 JoJLlmAn
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

package net.gtaun.shoebill;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

import net.gtaun.shoebill.event.plugin.PluginUnloadEvent;

/**
 * @author MK124, JoJLlmAn
 *
 */

public class PluginManager implements IPluginManager
{
	Map<String, Plugin> plugins;
	
	
	PluginManager()
	{
		plugins = new HashMap<String, Plugin>();
	}
	
	public Plugin getPlugin( String name )
	{
		return plugins.get(name);
	}
	
	public Plugin loadPlugin( String name )
	{
		try {
			ClassLoader loader = URLClassLoader.newInstance(new URL[]{new URL("File://" + System.getProperty("user.dir") + "/Plugins/" + name)}, getClass().getClassLoader());
			Class<? extends Plugin> clazz = Class.forName("net.gtaun.shoebill.plugin.Test", true, loader).asSubclass(Plugin.class);
			Constructor<? extends Plugin> constructor = clazz.getConstructor();
			Plugin plugin = constructor.newInstance();
			plugins.put("Test", plugin);
			return plugin;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void unloadPlugin( String name )
	{
		if(plugins.containsKey(name))
		{
			Shoebill.getInstance().getEventManager().dispatchEvent(new PluginUnloadEvent(plugins.get(name)));
			plugins.remove(name);
		}
	}
}
