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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import net.gtaun.shoebill.event.gamemode.GamemodeExitEvent;
import net.gtaun.shoebill.event.gamemode.GamemodeInitEvent;
import net.gtaun.shoebill.exception.NoGamemodeAssignedException;
import net.gtaun.shoebill.object.SampEventDispatcher;
import net.gtaun.shoebill.object.Server;
import net.gtaun.shoebill.object.World;
import net.gtaun.shoebill.plugin.Gamemode;
import net.gtaun.shoebill.plugin.Plugin;
import net.gtaun.shoebill.plugin.PluginManager;
import net.gtaun.shoebill.samp.ISampCallbackHandler;
import net.gtaun.shoebill.samp.ISampCallbackManager;
import net.gtaun.shoebill.samp.SampCallbackHandler;
import net.gtaun.shoebill.samp.SampCallbackManager;
import net.gtaun.shoebill.util.event.EventManager;
import net.gtaun.shoebill.util.event.IEventManager;
import net.gtaun.shoebill.util.log.LoggerOutputStream;

/**
 * @author MK124
 * 
 */

public class Shoebill implements IShoebill, IShoebillLowLevel
{
	public static final Logger LOGGER = Logger.getLogger(Shoebill.class);
	
	
	private static Shoebill instance;
	public static IShoebill getInstance()		{ return instance; }
	
	
	private ShoebillConfiguration configuration;
	private EventManager eventManager;
	
	private SampCallbackManager sampCallbackManager;
	private SampObjectPool managedObjectPool;
	private PluginManager pluginManager;
	
	private SampEventLogger sampEventLogger;
	private SampEventDispatcher sampEventDispatcher;
	
	private File pluginFolder, gamemodeFolder, dataFolder;

	
	@Override public IEventManager getEventManager()				{ return eventManager; }
	@Override public ISampObjectPool getManagedObjectPool()			{ return managedObjectPool; }
	@Override public PluginManager getPluginManager()				{ return pluginManager; }
	@Override public ISampCallbackManager getCallbackManager()		{ return sampCallbackManager; }
	
	
	Shoebill() throws IOException, ClassNotFoundException
	{
		instance = this;
		
		File logPropertyFile = new File("./shoebill/log4j.properties");
		if( logPropertyFile.exists() )
		{
			PropertyConfigurator.configure( logPropertyFile.toURI().toURL() );
		}
		else
		{
			InputStream in = this.getClass().getClassLoader().getResourceAsStream( "log4j.properties" );
			Properties properties = new Properties();
			properties.load(in);
			PropertyConfigurator.configure( properties );
		}

		System.setOut( new PrintStream(new LoggerOutputStream(Logger.getLogger("System.out"), Level.INFO), true) );
		System.setErr( new PrintStream(new LoggerOutputStream(Logger.getLogger("System.err"), Level.ERROR), true) );
		
		if( !logPropertyFile.exists() ) LOGGER.info( "Not find " + logPropertyFile.getPath() + " file, use the default configuration." );
		LOGGER.info( "System environment: " + System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ", " + System.getProperty("os.version") + ")" );
		
		FileInputStream configFileIn;
		configFileIn = new FileInputStream("./shoebill/config.yml");
		
		configuration = new ShoebillConfiguration( configFileIn );
		
		File workdir = configuration.getWorkdir();
		pluginFolder = new File(workdir, "plugins");
		gamemodeFolder = new File(workdir, "gamemodes");
		dataFolder = new File(workdir, "data");
	}

	private void registerRootCallbackHandler()
	{
		sampCallbackManager.registerCallbackHandler( new SampCallbackHandler()
		{
			public int onGameModeInit()
			{
				try
				{
					initialize();
					loadPluginsAndGamemode();
				}
				catch( Exception e )
				{
					e.printStackTrace();
				}
				
				return 1;
			}
			
			public int onGameModeExit()
			{
				unloadPluginsAndGamemode();
				uninitialize();
				return 1;
			}
		} );
	}
	
	private void initialize()
	{
		eventManager = new EventManager();
		pluginManager = new PluginManager(this, pluginFolder, gamemodeFolder, dataFolder);

		managedObjectPool = new SampObjectPool( eventManager );
		managedObjectPool.setServer( new Server() );
		managedObjectPool.setWorld( new World() );

		sampEventLogger = new SampEventLogger( managedObjectPool );
		sampEventDispatcher = new SampEventDispatcher( managedObjectPool, eventManager );
		
		sampCallbackManager.registerCallbackHandler( managedObjectPool.getCallbackHandler() );
		sampCallbackManager.registerCallbackHandler( sampEventDispatcher );
		sampCallbackManager.registerCallbackHandler( sampEventLogger );
	}
	
	private void uninitialize()
	{
		sampCallbackManager = null;
		sampEventLogger = null;
		sampEventDispatcher = null;
		managedObjectPool = null;
		pluginManager = null;
		eventManager = null;
		
		System.gc();
	}
	
	private void loadPluginsAndGamemode() throws IOException
	{
		pluginManager.loadAllPlugin();
		
		List<String> gamemodes = configuration.getGamemodes();
		if( gamemodes == null || gamemodes.size() == 0 )
		{
			Shoebill.LOGGER.error( "There's no gamemode assigned in config.yml." );
			throw new NoGamemodeAssignedException();
		}
		
		File file = new File( gamemodeFolder, gamemodes.get(0) );
		Gamemode gamemode = pluginManager.constructGamemode( file );
		if( gamemode != null )
		{
			managedObjectPool.setGamemode( gamemode );
			GamemodeInitEvent event = new GamemodeInitEvent(gamemode);
			eventManager.dispatchEvent( event, gamemode );
		}
	}
	
	private void unloadPluginsAndGamemode()
	{
		Gamemode gamemode = managedObjectPool.getGamemode();
		if( gamemode != null )
		{
			GamemodeExitEvent event = new GamemodeExitEvent(gamemode);
			eventManager.dispatchEvent( event, gamemode );
			pluginManager.deconstructGamemode( gamemode );
		}
		
		Collection<Plugin> plugins = pluginManager.getPlugins();
		for( Plugin plugin : plugins ) pluginManager.unloadPlugin( plugin );
		
		managedObjectPool.setGamemode( null );
	}

	public ISampCallbackHandler getCallbackHandler()
	{
		if( sampCallbackManager == null )
		{
			sampCallbackManager = new SampCallbackManager();
			registerRootCallbackHandler();
		}
		
		return sampCallbackManager.getMasterCallbackHandler();
	}
}
