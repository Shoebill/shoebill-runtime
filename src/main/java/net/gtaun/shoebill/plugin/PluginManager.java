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

package net.gtaun.shoebill.plugin;

import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import net.gtaun.shoebill.IShoebill;
import net.gtaun.shoebill.event.plugin.PluginLoadEvent;
import net.gtaun.shoebill.event.plugin.PluginUnloadEvent;

/**
 * @author JoJLlmAn, MK124
 *
 */

public class PluginManager implements IPluginManager
{
	final FilenameFilter jarFileFliter = new FilenameFilter()
	{
		public boolean accept( File dir, String name )
		{
			final String extname = ".jar";
			return name.length()>extname.length() && name.substring( name.length()-4, name.length() ).equals(extname);
		}
	};
	
	
	private Map<Class<? extends Plugin>, Plugin> plugins;

	private ClassLoader classLoader;
	private IShoebill shoebill;
	private File pluginFolder, dataFolder;
	
	
	public PluginManager( IShoebill shoebill, File pluginFolder, File dataFolder )
	{
		plugins = new HashMap<Class<? extends Plugin>, Plugin>();
		
		File[] files = pluginFolder.listFiles( jarFileFliter );
		URL[] urls = new URL[ files.length ];
		for( int i=0; i<files.length; i++ ) try
		{
			urls[i] = files[i].toURI().toURL();
		}
		catch( MalformedURLException e )
		{
			e.printStackTrace();
		}
		
		this.classLoader = URLClassLoader.newInstance(urls, getClass().getClassLoader());
		this.shoebill = shoebill;
		this.pluginFolder = pluginFolder;
		this.dataFolder = dataFolder;
	}
	
	public void loadAllPlugin()
	{
		File[] files = pluginFolder.listFiles( jarFileFliter );
		for( File file : files ) loadPlugin( file );
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
		
		System.out.println("Load plugin: " + file.getPath() );
		
		try
		{
			JarFile jarFile = new JarFile( file );
			JarEntry entry = jarFile.getJarEntry( "plugin.yml" );
			InputStream in = jarFile.getInputStream( entry );
			
			PluginDescription desc = new PluginDescription(in);
			Class<? extends Plugin> clazz = Class.forName(desc.getClassName(), true, classLoader).asSubclass(Plugin.class);
			if( plugins.containsKey(clazz) )
			{
				System.out.println("There's a plugin which has the same class as \"" + desc.getClassName() + "\".");
				System.out.println("Abandon loading " + desc.getClassName());
				return null;
			}
			
			Constructor<? extends Plugin> constructor = clazz.getConstructor();
			Plugin plugin = constructor.newInstance();
			
			File pluginDataFolder = new File(dataFolder, desc.getClassName());
			if( ! pluginDataFolder.exists() ) pluginDataFolder.mkdirs();
			
			plugin.setContext( desc, shoebill, pluginDataFolder );
			plugin.enable();
			
			plugins.put( clazz, plugin );
			shoebill.getEventManager().dispatchEvent( new PluginLoadEvent(plugin), this );
			return plugin;
		}
		catch( Exception e )
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

			shoebill.getEventManager().dispatchEvent( new PluginUnloadEvent(plugin), this );
			plugin.disable();
			
			plugins.remove( entry.getKey() );
			return;
		}
	}
	
	@Override
	public <T extends Plugin> T getPlugin( Class<T> cls )
	{
		for( Plugin plugin : plugins.values() )
		{
			if( cls.isInstance(plugin) ) return cls.cast( plugin );
		}
		
		return null;
	}

	@Override
	public Collection<Plugin> getPlugins()
	{
		return plugins.values();
	}
}
