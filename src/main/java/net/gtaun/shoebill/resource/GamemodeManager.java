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

package net.gtaun.shoebill.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import net.gtaun.shoebill.IGamemodeManager;
import net.gtaun.shoebill.IShoebill;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/**
 * @author MK124
 *
 */

public class GamemodeManager implements IGamemodeManager
{
	private static final Logger LOGGER = Logger.getLogger(PluginManager.class);
	
	
	private IShoebill shoebill;
	private ClassLoader classLoader;
	private File gamemodeDir, dataFolder;
	
	private Map<File, GamemodeDescription> descriptions;
	private Gamemode gamemode;
	
	
	public GamemodeManager( IShoebill shoebill, ClassLoader classLoader, File gamemodeDir, File dataFolder )
	{
		this.shoebill = shoebill;
		this.classLoader = classLoader;
		this.gamemodeDir = gamemodeDir;
		this.dataFolder = dataFolder;
		
		this.descriptions = generateDescriptions( gamemodeDir );
	}
	
	private GamemodeDescription generateDescription( File file ) throws IOException, ClassNotFoundException
	{
		JarFile jarFile = new JarFile( file );
		JarEntry entry = jarFile.getJarEntry( "plugin.yml" );
		InputStream in = jarFile.getInputStream( entry );
		
		GamemodeDescription desc = new GamemodeDescription(in, classLoader);
		return desc;
	}
	
	private Map<File, GamemodeDescription> generateDescriptions( File dir )
	{
		Map<File, GamemodeDescription> descriptions = new HashMap<>();
		Collection<File> files = FileUtils.listFiles(dir, new String[]{ ".jar" }, true );
		
		for( File file : files )
		{
			try
			{
				GamemodeDescription desc = generateDescription( file );
				descriptions.put( file, desc );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
		}
		
		return descriptions;
	}
	
	private Gamemode constructGamemode( File file ) throws IOException
	{
		JarFile jarFile = new JarFile( file );
		JarEntry entry = jarFile.getJarEntry( "gamemode.yml" );
		InputStream in = jarFile.getInputStream( entry );
		
		GamemodeDescription desc;
		Gamemode gamemode = null;
		
		LOGGER.info("Load gamemode: " + file.getName() );
		
		try
		{
			desc = new GamemodeDescription(in, classLoader);
			gamemode = desc.getClazz().newInstance();
			gamemode.setContext( desc, shoebill, new File(dataFolder, desc.getClazz().getName()) );
			gamemode.enable();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		
		return gamemode;
	}
	
	private void deconstructGamemode( Gamemode gamemode )
	{
		try
		{
			gamemode.disable();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

	@Override
	public void changeMode( String filename )
	{
		return;
	}

	@Override
	public void changeMode( File file )
	{
		return;
	}

	@Override
	public Gamemode getGamemode()
	{
		return gamemode;
	}

	@Override
	public <T extends Gamemode> T getGamemode( Class<T> cls )
	{
		return cls.cast( gamemode );
	}

	@Override
	public Collection<GamemodeDescription> getGamemodeDescriptions()
	{
		return new ArrayList<>( descriptions.values() );
	}
}
