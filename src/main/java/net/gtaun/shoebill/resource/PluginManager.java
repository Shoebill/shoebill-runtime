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

package net.gtaun.shoebill.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import net.gtaun.shoebill.IPluginManager;
import net.gtaun.shoebill.IShoebill;
import net.gtaun.shoebill.IShoebillLowLevel;
import net.gtaun.shoebill.Shoebill;
import net.gtaun.shoebill.event.plugin.PluginLoadEvent;
import net.gtaun.shoebill.event.plugin.PluginUnloadEvent;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/**
 * @author JoJLlmAn, MK124
 *
 */

public class PluginManager implements IPluginManager
{
	private static final Logger LOGGER = Logger.getLogger(PluginManager.class);
	

	private ClassLoader classLoader;
	private IShoebill shoebill;
	private File pluginFolder, dataFolder;

	private Map<File, PluginDescription> descriptions;
	private Map<Class<? extends Plugin>, Plugin> plugins;
	
	
	public PluginManager( IShoebill shoebill, ClassLoader classLoader, File pluginFolder, File dataFolder )
	{
		plugins = new HashMap<Class<? extends Plugin>, Plugin>();
		
		this.shoebill = shoebill;
		this.classLoader = classLoader;
		this.pluginFolder = pluginFolder;
		this.dataFolder = dataFolder;
		
		this.descriptions = generateDescriptions(pluginFolder);
	}
	
	private PluginDescription generateDescription( File file ) throws IOException, ClassNotFoundException
	{
		JarFile jarFile = new JarFile( file );
		JarEntry entry = jarFile.getJarEntry( "plugin.yml" );
		InputStream in = jarFile.getInputStream( entry );
		
		PluginDescription desc = new PluginDescription(in, classLoader);
		return desc;
	}
	
	private Map<File, PluginDescription> generateDescriptions( File dir )
	{
		Map<File, PluginDescription> descriptions = new HashMap<>();
		Collection<File> files = FileUtils.listFiles(dir, new String[]{ ".jar" }, true );
		
		for( File file : files )
		{
			try
			{
				PluginDescription desc = generateDescription( file );
				descriptions.put( file, desc );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
		}
		
		return descriptions;
	}
	
	public void loadAllPlugin()
	{
		for( PluginDescription desc : descriptions.values() ) loadPlugin( desc );
	}

	@Override
	public Plugin loadPlugin( String filename )
	{
		File file = new File(pluginFolder, filename);
		return loadPlugin( file );
	}

	@Override
	public Plugin loadPlugin( File file )
	{
		if( file.canRead() == false ) return null;
		
		LOGGER.info("Load plugin: " + file.getName() );
		
		try
		{
			JarFile jarFile = new JarFile( file );
			JarEntry entry = jarFile.getJarEntry( "plugin.yml" );
			InputStream in = jarFile.getInputStream( entry );
			
			PluginDescription desc = new PluginDescription(in, classLoader);
			return loadPlugin(desc);
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public Plugin loadPlugin( PluginDescription desc )
	{
		try
		{
			LOGGER.info("Load plugin: " + desc.getName() );
			Class<? extends Plugin> clazz = desc.getClazz();
			if( plugins.containsKey(clazz) )
			{
				LOGGER.warn("There's a plugin which has the same class as \"" + desc.getClazz().getName() + "\".");
				LOGGER.warn("Abandon loading " + desc.getClazz().getName());
				return null;
			}
			
			Constructor<? extends Plugin> constructor = clazz.getConstructor();
			Plugin plugin = constructor.newInstance();
			
			File pluginDataFolder = new File(dataFolder, desc.getClazz().getName());
			if( ! pluginDataFolder.exists() ) pluginDataFolder.mkdirs();
			
			plugin.setContext( desc, shoebill, pluginDataFolder );
			plugin.enable();
			
			plugins.put( clazz, plugin );
			
			PluginLoadEvent event = new PluginLoadEvent(plugin);
			IShoebillLowLevel shoebillLowLevel = (IShoebillLowLevel) Shoebill.getInstance();
			shoebillLowLevel.getEventManager().dispatchEvent( event, this );
			
			return plugin;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void unloadPlugin( Plugin plugin )
	{
		for( Entry<Class<? extends Plugin>, Plugin> entry : plugins.entrySet() )
		{
			if( entry.getValue() != plugin ) continue;
			LOGGER.info("Unload plugin: " + plugin.getDescription().getClazz().getName() );

			PluginUnloadEvent event = new PluginUnloadEvent(plugin);
			IShoebillLowLevel shoebillLowLevel = (IShoebillLowLevel) Shoebill.getInstance();
			shoebillLowLevel.getEventManager().dispatchEvent( event, this );
			
			try
			{
				plugin.disable();
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			plugins.remove( entry.getKey() );
			return;
		}
	}

	@Override
	public <T extends Plugin> T getPlugin( Class<T> clz )
	{
		T plugin = clz.cast( plugins.get(clz) );
		if( plugin != null ) return plugin;
		
		for( Plugin p : plugins.values() )
		{
			if( clz.isInstance(p) ) return clz.cast( p );
		}
		
		return null;
	}

	@Override
	public Collection<Plugin> getPlugins()
	{
		return plugins.values();
	}
}
