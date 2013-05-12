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
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.gtaun.shoebill.amx.AmxInstanceManagerImpl;
import net.gtaun.shoebill.exception.NoGamemodeAssignedException;
import net.gtaun.shoebill.object.Server;
import net.gtaun.shoebill.proxy.GlobalProxyManager;
import net.gtaun.shoebill.proxy.GlobalProxyManagerImpl;
import net.gtaun.shoebill.proxy.ProxyableFactoryImpl;
import net.gtaun.shoebill.resource.ResourceManager;
import net.gtaun.shoebill.resource.ResourceManagerImpl;
import net.gtaun.shoebill.samp.AbstractSampCallbackHandler;
import net.gtaun.shoebill.samp.SampCallbackHandler;
import net.gtaun.shoebill.samp.SampCallbackManagerImpl;
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
public class ShoebillImpl implements Shoebill
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ShoebillImpl.class);
	
	private static final String VERSION_FILENAME = "version.yml";
	
	private static final String SHOEBILL_CONFIG_PATH = "./shoebill/shoebill.yml";
	private static final String LOG4J_CONFIG_FILENAME = "log4j.properties";
	private static final String RESOURCES_CONFIG_FILENAME = "resources.yml";
	
	
	public static ShoebillImpl getInstancea()
	{
		return (ShoebillImpl) Shoebill.Instance.get();
	}
	
	
	private ShoebillVersionImpl version;
	private ShoebillConfig config;
	private ResourceConfig resourceConfig;
	private ShoebillArtifactLocator artifactLocator;
	
	private EventManagerImpl eventManager;
	
	private SampCallbackManagerImpl sampCallbackManager;
	private AmxInstanceManagerImpl amxInstanceManager;
	
	private SampObjectStoreImpl sampObjectStore;
	private SampObjectManager sampObjectManager;

	private ResourceManagerImpl pluginManager;
	private ServiceManagerImpl serviceManager;
	
	private SampEventLogger sampEventLogger;
	private SampEventDispatcher sampEventDispatcher;
	
	private GlobalProxyManager globalProxyManager;

	private PrintStream originOutPrintStream;
	private PrintStream originErrPrintStream;
	
	private Queue<Runnable> asyncExecQueue;
	
	
	public ShoebillImpl(int[] amxHandles) throws IOException, ClassNotFoundException
	{
		Class.forName(ProxyableFactoryImpl.class.getName());
		Shoebill.Instance.shoebillReference = new WeakReference<>(this);
		
		config = new ShoebillConfig(new FileInputStream(SHOEBILL_CONFIG_PATH));
		initializeLoggerConfig(new File(config.getShoebillDir(), LOG4J_CONFIG_FILENAME));
		
		version = new ShoebillVersionImpl(this.getClass().getClassLoader().getResourceAsStream(VERSION_FILENAME));
		
		String startupMessage = version.getName() + " " + version.getVersion();
		
		if (version.getBuildNumber() != 0) startupMessage += " Build " + version.getBuildNumber();
		startupMessage += " (for " + version.getSupport() + ")";
		
		LOGGER.info(startupMessage);
		LOGGER.info("Build date: " + version.getBuildDate());
		LOGGER.info("System environment: " + System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ", " + System.getProperty("os.version") + ")");
		LOGGER.info("JVM: " + System.getProperty("java.vm.name") + " " + System.getProperty("java.vm.version"));
		LOGGER.info("Java: " + System.getProperty("java.specification.name") + " " + System.getProperty("java.specification.version"));
		
		asyncExecQueue = new ConcurrentLinkedQueue<Runnable>();

		eventManager = new EventManagerImpl();
		amxInstanceManager = new AmxInstanceManagerImpl(eventManager, amxHandles);
		
		resourceConfig = new ResourceConfig(new FileInputStream(new File(config.getShoebillDir(), RESOURCES_CONFIG_FILENAME)));
		artifactLocator = new ShoebillArtifactLocator(config, resourceConfig);
		
		if (artifactLocator.getGamemodeFile() == null)
		{
			LOGGER.error("There's no gamemode assigned in " + RESOURCES_CONFIG_FILENAME + " or file not found.");
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
			InputStream in = this.getClass().getClassLoader().getResourceAsStream(LOG4J_CONFIG_FILENAME);
			Properties properties = new Properties();
			properties.load(in);
			PropertyConfigurator.configure(properties);
		}
		
		originOutPrintStream = System.out;
		originErrPrintStream = System.err;
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
				String op = splits[0].toLowerCase();
				
				switch (op)
				{
				case "reload":
					reload();
					return 1;
					
				case "gc":
					System.gc();
					return 1;
					
				case "memstatus":
					Runtime runtime = Runtime.getRuntime();
					LOGGER.info("FreeMem: " + runtime.freeMemory() + " bytes / TotalMem: " + runtime.totalMemory() + " bytes.");
					return 1;
					
				default:
					return 0;
				}
			}
			
			@Override
			public void onProcessTick()
			{
				for (Iterator<Runnable> it = asyncExecQueue.iterator(); it.hasNext();)
				{
					Runnable runnable = it.next();
					runnable.run();
					it.remove();
				}
			}
			
			@Override
			public void onAmxLoad(int handle)
			{
				amxInstanceManager.onAmxLoad(handle);
			}
			
			@Override
			public void onAmxUnload(int handle)
			{
				amxInstanceManager.onAmxUnload(handle);
			}
		});
	}
	
	private void initialize()
	{
		globalProxyManager = new GlobalProxyManagerImpl();
		
		pluginManager = new ResourceManagerImpl(this, eventManager, artifactLocator, config.getDataDir());
		serviceManager = new ServiceManagerImpl(eventManager);
		
		sampObjectStore = new SampObjectStoreImpl();
		sampObjectManager = new SampObjectManager(eventManager, globalProxyManager, sampObjectStore);
		sampObjectStore.setFactory(sampObjectManager);
		
		sampObjectManager.createWorld();
		
		Server server = sampObjectManager.createServer();
		server.setServerCodepage(config.getServerCodepage());
		
		sampEventLogger = new SampEventLogger(sampObjectStore);
		sampEventDispatcher = new SampEventDispatcher(sampObjectStore, eventManager);
		
		sampCallbackManager.registerCallbackHandler(sampObjectStore.getCallbackHandler());
		sampCallbackManager.registerCallbackHandler(sampEventDispatcher);
		sampCallbackManager.registerCallbackHandler(sampEventLogger);
	}
	
	private void uninitialize()
	{
		System.setOut(originOutPrintStream);
		System.setErr(originErrPrintStream);
	}
	
	private void loadPluginsAndGamemode()
	{
		pluginManager.loadAllResource();
	}
	
	private void unloadPluginsAndGamemode()
	{
		pluginManager.unloadAllResource();
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
	
	public ShoebillConfig getConfig()
	{
		return config;
	}
	
	public ResourceConfig getResourceConfig()
	{
		return resourceConfig;
	}
	
	public ShoebillArtifactLocator getArtifactLocator()
	{
		return artifactLocator;
	}
	
	public EventManager getRootEventManager()
	{
		return eventManager;
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
	public AmxInstanceManagerImpl getAmxInstanceManager()
	{
		return amxInstanceManager;
	}
	
	@Override
	public ResourceManager getResourceManager()
	{
		return pluginManager;
	}
	
	@Override
	public ServiceManagerImpl getServiceStore()
	{
		return serviceManager;
	}
	
	@Override
	public GlobalProxyManager getGlobalProxyManager()
	{
		return globalProxyManager;
	}
	
	@Override
	public ShoebillVersion getVersion()
	{
		return version;
	}
	
	@Override
	public void reload()
	{
		SampNativeFunction.sendRconCommand("changemode Shoebill");
	}
	
	@Override
	public void asyncExec(Runnable runnable)
	{
		asyncExecQueue.offer(runnable);
	}
}
