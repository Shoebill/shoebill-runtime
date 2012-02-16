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
import net.gtaun.shoebill.plugin.PluginManager;
import net.gtaun.shoebill.samp.ISampCallbackHandler;
import net.gtaun.shoebill.samp.ISampCallbackManager;
import net.gtaun.shoebill.samp.SampCallbackHandler;
import net.gtaun.shoebill.samp.SampCallbackManager;
import net.gtaun.shoebill.util.event.EventManager;
import net.gtaun.shoebill.util.event.IEventManager;

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
	
	private File gamemodeFolder;

	
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

		System.setOut( new PrintStream(new LoggerOutputStream(Logger.getLogger("SysOut"), Level.INFO), true) );
		System.setErr( new PrintStream(new LoggerOutputStream(Logger.getLogger("SysErr"), Level.ERROR), true) );
		
		if( !logPropertyFile.exists() ) LOGGER.info( "Not find " + logPropertyFile.getPath() + " file, use the default configuration." );
		LOGGER.info( "System environment: " + System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ", " + System.getProperty("os.version") + ")" );
		
		FileInputStream configFileIn;
		configFileIn = new FileInputStream("./shoebill/config.yml");
		
		configuration = new ShoebillConfiguration( configFileIn );
		eventManager = new EventManager();
		
		sampCallbackManager = new SampCallbackManager();
		managedObjectPool = new SampObjectPool( eventManager );
		
		File workdir = configuration.getWorkdir();
		gamemodeFolder = new File(workdir, "gamemodes");
		
		pluginManager = new PluginManager(this, new File(workdir, "plugins"), gamemodeFolder, new File(workdir, "data"));
		pluginManager.loadAllPlugin();
		
		sampEventLogger = new SampEventLogger( managedObjectPool );
		sampEventDispatcher = new SampEventDispatcher( managedObjectPool, eventManager );
		
		initialize();
	}
	
	@Override
	protected void finalize() throws Throwable
	{
		instance = null;
	}
	
	private void initialize() throws IOException, ClassNotFoundException
	{
		managedObjectPool.setServer( new Server() );
		managedObjectPool.setWorld( new World() );
		
		sampCallbackManager.registerCallbackHandler( new SampCallbackHandler()
		{
			public int onGameModeInit()
			{
				try
				{
					List<String> gamemodes = configuration.getGamemodes();
					if( gamemodes == null || gamemodes.size() == 0 )
					{
						Shoebill.LOGGER.error( "There's no gamemode assigned in config.yml." );
						throw new NoGamemodeAssignedException();
					}
					
					File file = new File( gamemodeFolder, gamemodes.get(0) );
					Gamemode gamemode = pluginManager.constructGamemode( file );
					managedObjectPool.setGamemode( gamemode );
					
					eventManager.dispatchEvent( new GamemodeInitEvent(gamemode), gamemode );
				}
				catch( Exception e )
				{
					e.printStackTrace();
				}

				return 1;
			}
			
			public int onGameModeExit()
			{
				Gamemode gamemode = managedObjectPool.getGamemode();
				if( gamemode == null ) return 1;
				
				eventManager.dispatchEvent( new GamemodeExitEvent(gamemode), gamemode );
				managedObjectPool.setGamemode( null );
				return 1;
			}
		} );
		
		sampCallbackManager.registerCallbackHandler( managedObjectPool.getCallbackHandler() );
		sampCallbackManager.registerCallbackHandler( sampEventDispatcher );
		sampCallbackManager.registerCallbackHandler( sampEventLogger );
	}
	
	public ISampCallbackHandler getCallbackHandler()
	{
		return sampCallbackManager.getMasterCallbackHandler();
	}
}
