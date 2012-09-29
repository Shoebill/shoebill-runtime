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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Properties;

import net.gtaun.shoebill.exception.NoGamemodeAssignedException;
import net.gtaun.shoebill.object.impl.ServerImpl;
import net.gtaun.shoebill.object.impl.WorldImpl;
import net.gtaun.shoebill.resource.GamemodeManagerImpl;
import net.gtaun.shoebill.resource.PluginManagerImpl;
import net.gtaun.shoebill.samp.SampCallbackHandler;
import net.gtaun.shoebill.samp.SampCallbackManager;
import net.gtaun.shoebill.samp.AbstractSampCallbackHandler;
import net.gtaun.shoebill.samp.SampCallbackManagerImpl;
import net.gtaun.shoebill.samp.SampNativeFunction;
import net.gtaun.shoebill.util.event.EventManager;
import net.gtaun.shoebill.util.event.IEventManager;
import net.gtaun.shoebill.util.log.LogLevel;
import net.gtaun.shoebill.util.log.LoggerOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author MK124
 * 
 */

public class ShoebillImpl implements Shoebill, ShoebillLowLevel
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ShoebillImpl.class);
	
	
	private static ShoebillImpl instance;
	public static Shoebill getInstance()
	{
		return instance;
	}
	
	
	private ShoebillVersionImpl version;
	private ShoebillConfiguration configuration;
	private EventManager eventManager;
	
	private SampCallbackManagerImpl sampCallbackManager;
	private SampObjectPoolImpl managedObjectPool;
	private GamemodeManagerImpl gamemodeManager;
	private PluginManagerImpl pluginManager;
	
	private SampEventLogger sampEventLogger;
	private SampEventDispatcher sampEventDispatcher;
	
	private File pluginDir, gamemodeDir, dataDir;
	private File gamemodeFile;

	
	@Override public IEventManager getEventManager()				{ return eventManager; }
	@Override public SampObjectPool getManagedObjectPool()			{ return managedObjectPool; }
	@Override public GamemodeManager getGamemodeManager()			{ return gamemodeManager; }
	@Override public PluginManager getPluginManager()				{ return pluginManager; }
	@Override public SampCallbackManager getCallbackManager()		{ return sampCallbackManager; }
	@Override public ShoebillVersion getVersion()					{ return version; }
	
	
	ShoebillImpl() throws IOException
	{
		instance = this;
		initializeLoggerConfig();
		
		version = new ShoebillVersionImpl( this.getClass().getClassLoader().getResourceAsStream( "version.yml" ));
		
		String startupMessage = "Shoebill " + version.getVersion();
		
		if( version.getBuildNumber() != 0) startupMessage += " Build " + version.getBuildNumber();
		startupMessage += " (for " + version.getSupport() + ")";
		
		LOGGER.info( startupMessage );
		LOGGER.info( "Build date: " + version.getBuildDate() );
		LOGGER.info( "System environment: " + System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ", " + System.getProperty("os.version") + ")" );
		
		InputStream configFileIn = new FileInputStream("./shoebill/config.yml");
		configuration = new ShoebillConfiguration( configFileIn );
		
		File workdir = configuration.getWorkdir();
		pluginDir = new File(workdir, "plugins");
		gamemodeDir = new File(workdir, "gamemodes");
		dataDir = new File(workdir, "data");

		String gamemodeFilename = configuration.getGamemode();
		if( gamemodeFilename == null )
		{
			ShoebillImpl.LOGGER.error( "There's no gamemode assigned in config.yml." );
			throw new NoGamemodeAssignedException();
		}

		gamemodeFile = new File(gamemodeDir, gamemodeFilename);
	}

	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
	
	private void initializeLoggerConfig() throws IOException
	{
		File logConfigFile = new File("./shoebill/log4j.properties");
		if( logConfigFile.exists() )
		{
			PropertyConfigurator.configure( logConfigFile.toURI().toURL() );
		}
		else
		{
			InputStream in = this.getClass().getClassLoader().getResourceAsStream( "log4j.properties" );
			Properties properties = new Properties();
			properties.load(in);
			PropertyConfigurator.configure( properties );
		}

		System.setOut( new PrintStream(new LoggerOutputStream(LoggerFactory.getLogger("System.out"), LogLevel.INFO), true) );
		System.setErr( new PrintStream(new LoggerOutputStream(LoggerFactory.getLogger("System.err"), LogLevel.ERROR), true) );
		
		if( !logConfigFile.exists() ) LOGGER.info( "Not find " + logConfigFile.getPath() + " file, use the default configuration." );
		
	}

	private void registerRootCallbackHandler()
	{
		sampCallbackManager.registerCallbackHandler( new AbstractSampCallbackHandler()
		{
			@Override
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
			
			@Override
			public int onGameModeExit()
			{
				unloadPluginsAndGamemode();
				uninitialize();
				return 1;
			}
			
			@Override
			public int onRconCommand( String cmd )
			{
				String[] splits = cmd.split( " " );
				if( splits.length != 2 ) return 0;
				
				String op = splits[0].toLowerCase();
				switch( op )
				{
				case "changegamemode":
					{
						String gamemode = splits[1];
						File file = new File(gamemodeDir, gamemode);
						if( file.exists() == false || file.isFile() == false )
						{
							LOGGER.info( "'" + gamemode + "' can not be found." );
							return 0;
						}
						
						changeGamemode( file );
						return 1;
					}

				default:
					return 0;
				}
			}
		} );
	}
	
	private void initialize()
	{
		eventManager = new EventManager();
		
		ClassLoader classLoader = generateResourceClassLoader(pluginDir, gamemodeDir);
		gamemodeManager = new GamemodeManagerImpl(this, classLoader, gamemodeDir, dataDir);
		pluginManager = new PluginManagerImpl(this, classLoader, pluginDir, dataDir);

		managedObjectPool = new SampObjectPoolImpl( eventManager );
		managedObjectPool.setServer( new ServerImpl() );
		managedObjectPool.setWorld( new WorldImpl() );

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
		gamemodeManager = null;
		pluginManager = null;
		eventManager = null;
		
		System.gc();
	}
	
	private ClassLoader generateResourceClassLoader( File pluginDir, File gamemodeDir )
	{
		String[] exts = { "jar" };
		Collection<File> files = FileUtils.listFiles( pluginDir, exts, true );
		files.addAll( FileUtils.listFiles(gamemodeDir, exts, true) );
		
		URL[] urls = new URL[ files.size() ];
		int i = 0;
		
		for( File file : files ) try
		{
			urls[i] = file.toURI().toURL();
			i++;
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		
		return URLClassLoader.newInstance(urls, getClass().getClassLoader());
	}
	
	private void loadPluginsAndGamemode()
	{
		pluginManager.loadAllPlugin();
		gamemodeManager.constructGamemode( gamemodeFile );
	}
	
	private void unloadPluginsAndGamemode()
	{
		gamemodeManager.deconstructGamemode();
		pluginManager.unloadAllPlugin();
	}

	public SampCallbackHandler getCallbackHandler()
	{
		if( sampCallbackManager == null )
		{
			sampCallbackManager = new SampCallbackManagerImpl();
			registerRootCallbackHandler();
		}
		
		return sampCallbackManager.getMasterCallbackHandler();
	}
	
	@Override
	public void changeGamemode( File file )
	{
		gamemodeFile = file;
		reload();
	}
	
	@Override
	public void reload()
	{
		SampNativeFunction.sendRconCommand( "changemode Shoebill" );
	}
}
