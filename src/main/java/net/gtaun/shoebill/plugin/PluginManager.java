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
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import net.gtaun.shoebill.IShoebill;
import net.gtaun.shoebill.Shoebill;
import net.gtaun.shoebill.event.plugin.PluginUnloadEvent;

/**
 * @author JoJLlmAn, MK124
 *
 */

public class PluginManager implements IPluginManager
{
	private Map<String, Plugin> plugins;
	
	private IShoebill shoebill;
	private File pluginFolder, dataFolder;
	
	
	public PluginManager( IShoebill shoebill, File pluginFolder, File dataFolder )
	{
		plugins = new HashMap<String, Plugin>();
		
		this.shoebill = shoebill;
		this.pluginFolder = pluginFolder;
		this.dataFolder = dataFolder;
		
		loadAllPlugin();
	}
	
	private void loadAllPlugin()
	{
		File[] files = pluginFolder.listFiles( new FilenameFilter()
    	{
    		public boolean accept( File dir, String name )
    		{
    			return name.substring( name.length()-4, name.length() ).equals(".jar");
    		}
    	} );
		
		for( File file : files ) loadPlugin( file );
	}
	
	public Plugin getPlugin( String name )
	{
		return plugins.get(name);
	}
	
	public Plugin loadPlugin( String filename )
	{
		File file = new File(pluginFolder, filename);
		return loadPlugin( file );
	}
	
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
			
			if(plugins.containsKey(desc.getName()))
			{
				System.out.println("There's a plugin which has the same name as \"" + desc.getName() + "\".");
				System.out.println("Abandon loading " + desc.getClassPath());
				return null;
			}
			
			ClassLoader loader = URLClassLoader.newInstance(new URL[]{file.toURI().toURL()}, getClass().getClassLoader());
			Class<? extends Plugin> clazz = Class.forName(desc.getClassPath(), true, loader).asSubclass(Plugin.class);
			Constructor<? extends Plugin> constructor = clazz.getConstructor();
			Plugin plugin = constructor.newInstance();
			
			File pluginDataFolder = new File(dataFolder, desc.getClassPath());
			if( ! pluginDataFolder.exists() ) pluginDataFolder.mkdirs();
			
			plugin.setContext( desc, shoebill, pluginDataFolder );
			plugin.enable();
			
			plugins.put( plugin.getDescription().getName(), plugin );
			return plugin;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public void unloadPlugin( String name )
	{
		if( plugins.containsKey(name) )
		{
			Shoebill.getInstance().getEventManager().dispatchEvent(new PluginUnloadEvent(plugins.get(name)));
			plugins.get(name).disable();
			plugins.remove(name);
		}
	}
}
