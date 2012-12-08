/**
 * Copyright (C) 2011 JoJLlmAn
 * Copyright (C) 2011-2012 MK124
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

package net.gtaun.shoebill.resource;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.gtaun.shoebill.Shoebill;
import net.gtaun.shoebill.ShoebillArtifactLocator;
import net.gtaun.shoebill.event.resource.ResourceLoadEvent;
import net.gtaun.shoebill.event.resource.ResourceUnloadEvent;
import net.gtaun.util.event.EventManager;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @author JoJLlmAn, MK124
 */
public class ResourceManagerImpl implements ResourceManager
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ResourceManagerImpl.class);
	
	
	private Shoebill shoebill;
	private EventManager eventManager;
	private ShoebillArtifactLocator artifactLocator;
	private File dataDir;
	
	private Map<Class<? extends Plugin>, Plugin> plugins;
	private Gamemode gamemode;
	
	
	public ResourceManagerImpl(Shoebill shoebill, EventManager eventManager, ShoebillArtifactLocator locator, File dataDir)
	{
		this.shoebill = shoebill;
		this.eventManager = eventManager;
		this.artifactLocator = locator;
		this.dataDir = dataDir;
		
		plugins = new HashMap<Class<? extends Plugin>, Plugin>();
	}
	
	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
	
	public void loadAllResource()
	{
		for (File file : artifactLocator.getPluginFiles())
		{
			loadPlugin(file);
		}
		
		loadGamemode();
	}
	
	public void unloadAllResource()
	{
		unloadGamemode();
		
		for (Plugin plugin : plugins.values())
		{
			unloadPlugin(plugin);
		}
	}
	
	@Override
	public Plugin loadPlugin(String coord)
	{
		File file = artifactLocator.getPluginFile(coord);
		return loadPlugin(file);
	}
	
	@Override
	public Plugin loadPlugin(File file)
	{
		if (file.canRead() == false) return null;
		
		ResourceDescription desc = null;
		try
		{
			desc = new ResourceDescription(ResourceType.PLUGIN, file, getClass().getClassLoader());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
				
		if (desc == null) return null;
		return loadPlugin(desc);
	}
	
	private Plugin loadPlugin(ResourceDescription desc)
	{
		try
		{
			LOGGER.info("Load plugin: " + desc.getName());
			Class<? extends Plugin> clazz = desc.getClazz().asSubclass(Plugin.class);
			
			if (plugins.containsKey(clazz))
			{
				LOGGER.warn("There's a plugin which has the same class as \"" + desc.getClazz().getName() + "\".");
				LOGGER.warn("Abandon loading " + desc.getClazz().getName());
				return null;
			}

			Plugin plugin = constructResource(desc, clazz);
			plugins.put(clazz, plugin);
			
			ResourceLoadEvent event = new ResourceLoadEvent(plugin);
			eventManager.dispatchEvent(event, this);
			
			plugin.enable();
			return plugin;
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	private <T extends Resource> T constructResource(ResourceDescription desc, Class<T> clazz) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		Constructor<T> constructor = clazz.getConstructor();
		T resource = constructor.newInstance();
		
		File resDataDir = new File(dataDir, desc.getClazz().getName());
		if (!resDataDir.exists()) resDataDir.mkdirs();
		
		resource.setContext(desc, shoebill, resDataDir);
		return resource;
	}
	
	@Override
	public void unloadPlugin(Plugin plugin)
	{
		Class<? extends Plugin> clazz = plugin.getClass();
		if (plugins.containsKey(clazz)) return;
		
		LOGGER.info("Unload plugin: " + clazz.getName());
		
		try
		{
			plugin.disable();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
		
		ResourceUnloadEvent event = new ResourceUnloadEvent(plugin);
		eventManager.dispatchEvent(event, this);
		
		plugins.remove(clazz);
		
		System.gc();
		return;
	}
	
	@Override
	public <T extends Plugin> T getPlugin(Class<T> clz)
	{
		T plugin = clz.cast(plugins.get(clz));
		if (plugin != null) return plugin;
		
		for (Plugin p : plugins.values())
		{
			if (clz.isInstance(p)) return clz.cast(p);
		}
		
		return null;
	}
	
	@Override
	public Collection<Plugin> getPlugins()
	{
		return plugins.values();
	}
	
	private void loadGamemode()
	{
		ResourceDescription desc = null;
		try
		{
			desc = new ResourceDescription(ResourceType.GAMEMODE, artifactLocator.getGamemodeFile(), getClass().getClassLoader());
	
			LOGGER.info("Load gamemode: " + desc.getName());
			Class<? extends Gamemode> clazz = desc.getClazz().asSubclass(Gamemode.class);
			
			gamemode = constructResource(desc, clazz);
			
			ResourceLoadEvent event = new ResourceLoadEvent(gamemode);
			eventManager.dispatchEvent(event, this);
			
			gamemode.enable();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
	}
	
	private void unloadGamemode()
	{
		try
		{
			ResourceUnloadEvent event = new ResourceUnloadEvent(gamemode);
			eventManager.dispatchEvent(event, this);
			
			gamemode.disable();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
		
		gamemode = null;
	}

	@Override
	public Gamemode getGamemode()
	{
		return gamemode;
	}
	
	@Override
	public <T extends Gamemode> T getGamemode(Class<T> cls)
	{
		return cls.cast(gamemode);
	}
}
