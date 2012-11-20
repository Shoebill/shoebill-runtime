/**
 * Copyright (C) 2011-2012 MK124
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
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

import net.gtaun.shoebill.exception.NoGamemodeAssignedException;
import net.gtaun.shoebill.proxy.ProxyableFactoryImpl;
import net.gtaun.shoebill.resource.GamemodeManagerImpl;
import net.gtaun.shoebill.resource.PluginManagerImpl;
import net.gtaun.shoebill.samp.AbstractSampCallbackHandler;
import net.gtaun.shoebill.samp.SampCallbackHandler;
import net.gtaun.shoebill.samp.SampCallbackManager;
import net.gtaun.shoebill.samp.SampCallbackManagerImpl;
import net.gtaun.shoebill.samp.SampNativeFunction;
import net.gtaun.shoebill.service.ServiceManagerImpl;
import net.gtaun.shoebill.util.log.LogLevel;
import net.gtaun.shoebill.util.log.LoggerOutputStream;
import net.gtaun.util.event.EventManager;
import net.gtaun.util.event.EventManagerImpl;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @author MK124
 */
public class ShoebillImpl implements Shoebill, ShoebillLowLevel
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ShoebillImpl.class);
	private static final FilenameFilter JAR_FILENAME_FILTER = new FilenameFilter()
	{
		@Override
		public boolean accept(File dir, String name)
		{
			return name.endsWith(".jar");
		}
	};
	
	
	private static Reference<ShoebillImpl> instance;
	
	
	public static ShoebillImpl getInstance()
	{
		return instance.get();
	}
	
	
	private ShoebillVersionImpl version;
	private ShoebillConfig configuration;
	private ResourceConfig resourceConfig;
	private ShoebillArtifactLocator artifactLocator;
	
	private EventManagerImpl eventManager;
	
	private SampCallbackManagerImpl sampCallbackManager;
	
	private SampObjectStoreImpl sampObjectStore;
	private SampObjectManager sampObjectManager;

	private GamemodeManagerImpl gamemodeManager;
	private PluginManagerImpl pluginManager;
	private ServiceManagerImpl serviceManager;
	
	private SampEventLogger sampEventLogger;
	private SampEventDispatcher sampEventDispatcher;
	
	private File pluginDir, gamemodeDir, dataDir;
	private File gamemodeFile;
	
	
	public ShoebillImpl() throws IOException, ClassNotFoundException
	{
		Class.forName(ProxyableFactoryImpl.class.getName());
		instance = new WeakReference<>(this);
		
		configuration = new ShoebillConfig(new FileInputStream("./shoebill/shoebill.yml"));
		initializeLoggerConfig(new File(configuration.getShoebillDir(), "log4j.properties"));
		
		version = new ShoebillVersionImpl(this.getClass().getClassLoader().getResourceAsStream("version.yml"));
		
		String startupMessage = "Shoebill " + version.getVersion();
		
		if (version.getBuildNumber() != 0) startupMessage += " Build " + version.getBuildNumber();
		startupMessage += " (for " + version.getSupport() + ")";
		
		LOGGER.info(startupMessage);
		LOGGER.info("Build date: " + version.getBuildDate());
		LOGGER.info("JVM: " + System.getProperty("java.vm.name") + " " + System.getProperty("java.vm.version"));
		LOGGER.info("Java: " + System.getProperty("java.specification.name") + " " + System.getProperty("java.specification.version"));
		LOGGER.info("System environment: " + System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ", " + System.getProperty("os.version") + ")");
		
		resourceConfig = new ResourceConfig(new FileInputStream(new File(configuration.getShoebillDir(), "resources.yml")));
		artifactLocator = new ShoebillArtifactLocator(configuration, resourceConfig);
		
		pluginDir = configuration.getPluginsDir();
		gamemodeDir = configuration.getGamemodesDir();
		dataDir = configuration.getDataDir();
		
		gamemodeFile = artifactLocator.getGamemodeFile();
		if (gamemodeFile == null)
		{
			LOGGER.error("There's no gamemode assigned in resource.yml or file not found.");
			throw new NoGamemodeAssignedException();
		}
	}
	
	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
	
	private void initializeLoggerConfig(File configFile) throws IOException
	{
		if (configFile.exists())
		{
			PropertyConfigurator.configure(configFile.toURI().toURL());
		}
		else
		{
			InputStream in = this.getClass().getClassLoader().getResourceAsStream("log4j.properties");
			Properties properties = new Properties();
			properties.load(in);
			PropertyConfigurator.configure(properties);
		}
		
		System.setOut(new PrintStream(new LoggerOutputStream(LoggerFactory.getLogger("System.out"), LogLevel.INFO), true));
		System.setErr(new PrintStream(new LoggerOutputStream(LoggerFactory.getLogger("System.err"), LogLevel.ERROR), true));
		
		if (!configFile.exists()) LOGGER.info("Not find " + configFile.getPath() + " file, use the default configuration.");
		
	}
	
	private void registerRootCallbackHandler()
	{
		sampCallbackManager.registerCallbackHandler(new AbstractSampCallbackHandler()
		{
			@Override
			public int onGameModeInit()
			{
				try
				{
					initialize();
					loadPluginsAndGamemode();
				}
				catch (Exception e)
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
			public int onRconCommand(String cmd)
			{
				String[] splits = cmd.split(" ");
				if (splits.length != 2) return 0;
				
				String op = splits[0].toLowerCase();
				switch (op)
				{
				case "changegamemode":
					{
						String gamemode = splits[1];
						File file = new File(gamemodeDir, gamemode);
						if (file.exists() == false || file.isFile() == false)
						{
							LOGGER.info("'" + gamemode + "' can not be found.");
							return 0;
						}
						
						changeGamemode(file);
						return 1;
					}
				
				default:
					return 0;
				}
			}
		});
	}
	
	private void initialize()
	{
		eventManager = new EventManagerImpl();
		
		ClassLoader classLoader = generateResourceClassLoader(pluginDir, gamemodeDir);
		
		gamemodeManager = new GamemodeManagerImpl(this, classLoader, gamemodeDir, dataDir);
		pluginManager = new PluginManagerImpl(this, classLoader, pluginDir, dataDir);
		serviceManager = new ServiceManagerImpl(eventManager);
		
		sampObjectStore = new SampObjectStoreImpl(eventManager);
		sampObjectManager = new SampObjectManager(eventManager, sampObjectStore);
		sampObjectManager.createWorld();
		sampObjectManager.createServer();
		
		sampEventLogger = new SampEventLogger(sampObjectStore);
		sampEventDispatcher = new SampEventDispatcher(sampObjectStore, eventManager);
		
		sampCallbackManager.registerCallbackHandler(sampObjectStore.getCallbackHandler());
		sampCallbackManager.registerCallbackHandler(sampEventDispatcher);
		sampCallbackManager.registerCallbackHandler(sampEventLogger);
	}
	
	private void uninitialize()
	{
		sampCallbackManager = null;
		sampEventLogger = null;
		sampEventDispatcher = null;
		sampObjectStore = null;
		sampObjectManager = null;
		
		gamemodeManager = null;
		pluginManager = null;
		serviceManager = null;
		
		eventManager = null;
		
		System.gc();
	}
	
	private ClassLoader generateResourceClassLoader(File pluginDir, File gamemodeDir)
	{
		File[] pluginFiles = pluginDir.listFiles(JAR_FILENAME_FILTER);
		File[] gamemodeFiles = gamemodeDir.listFiles(JAR_FILENAME_FILTER);
		
		URL[] urls = new URL[pluginFiles.length + gamemodeFiles.length];
		int idx = 0;

		File[][] fileArrays = {pluginFiles, gamemodeFiles};
		for (File[] files : fileArrays)
		{
			for (File file : files)
			{
				try
				{
					urls[idx] = file.toURI().toURL();
					idx++;
				}
				catch (MalformedURLException e)
				{
					e.printStackTrace();
				}
			}
		}
		
		return URLClassLoader.newInstance(urls, getClass().getClassLoader());
	}
	
	private void loadPluginsAndGamemode()
	{
		pluginManager.loadAllPlugin();
		gamemodeManager.constructGamemode(gamemodeFile);
	}
	
	private void unloadPluginsAndGamemode()
	{
		gamemodeManager.deconstructGamemode();
		pluginManager.unloadAllPlugin();
	}
	
	public SampCallbackHandler getCallbackHandler()
	{
		if (sampCallbackManager == null)
		{
			sampCallbackManager = new SampCallbackManagerImpl();
			registerRootCallbackHandler();
		}
		
		return sampCallbackManager.getMasterCallbackHandler();
	}
	
	@Override
	public SampObjectStore getSampObjectStore()
	{
		return sampObjectStore;
	}
	
	@Override
	public SampObjectManager getSampObjectFactory()
	{
		return sampObjectManager;
	}
	
	@Override
	public GamemodeManager getGamemodeManager()
	{
		return gamemodeManager;
	}
	
	@Override
	public PluginManager getPluginManager()
	{
		return pluginManager;
	}
	
	@Override
	public ServiceManagerImpl getServiceStore()
	{
		return serviceManager;
	}
	
	@Override
	public ShoebillVersion getVersion()
	{
		return version;
	}
	
	@Override
	public void changeGamemode(File file)
	{
		gamemodeFile = file;
		reload();
	}
	
	@Override
	public void reload()
	{
		SampNativeFunction.sendRconCommand("changemode Shoebill");
	}
	
	@Override
	public EventManager getEventManager()
	{
		return eventManager;
	}
	
	@Override
	public SampCallbackManager getCallbackManager()
	{
		return sampCallbackManager;
	}
}
