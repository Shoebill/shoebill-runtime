/**
 * Copyright (C) 2011-2014 MK124
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

import net.gtaun.shoebill.amx.AmxInstanceManagerImpl;
import net.gtaun.shoebill.object.Server;
import net.gtaun.shoebill.resource.ResourceManager;
import net.gtaun.shoebill.resource.ResourceManagerImpl;
import net.gtaun.shoebill.samp.SampCallbackHandler;
import net.gtaun.shoebill.samp.SampCallbackManagerImpl;
import net.gtaun.shoebill.service.ServiceManagerImpl;
import net.gtaun.shoebill.util.log.LogLevel;
import net.gtaun.shoebill.util.log.LoggerOutputStream;
import net.gtaun.util.event.EventManager;
import net.gtaun.util.event.EventManagerRoot;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.ref.WeakReference;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author MK124
 */
public class ShoebillImpl implements Shoebill {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShoebillImpl.class);

    private static final String VERSION_FILENAME = "version.yml";

    private static final String SHOEBILL_CONFIG_PATH = "./shoebill/shoebill.yml";
    private static final String LOG4J_CONFIG_FILENAME = "log4j.xml";
    private static final String RESOURCES_CONFIG_FILENAME = "resources.yml";
    private boolean initialized, restart = false;
    private ShoebillVersionImpl version;
    private ShoebillConfig config;
    private ResourceConfig resourceConfig;
    private ShoebillArtifactLocator artifactLocator;
    private EventManagerRoot eventManager;
    private SampCallbackManagerImpl sampCallbackManager;
    private AmxInstanceManagerImpl amxInstanceManager;
    private SampObjectManagerImpl sampObjectManager;
    private ResourceManagerImpl pluginManager;
    private ServiceManagerImpl serviceManager;
    //private SampEventLogger sampEventLogger;
    private SampEventDispatcher sampEventDispatcher;
    private PrintStream originOutPrintStream;
    private PrintStream originErrPrintStream;
    private Queue<Runnable> asyncExecQueue;

    public ShoebillImpl(int[] amxHandles) throws IOException {
        Shoebill.Instance.reference = new WeakReference<>(this);

        config = new ShoebillConfig(new FileInputStream(SHOEBILL_CONFIG_PATH));
        initLogger(new File(config.getShoebillDir(), LOG4J_CONFIG_FILENAME));

        version = new ShoebillVersionImpl(this.getClass().getClassLoader().getResourceAsStream(VERSION_FILENAME));

        String startupMessage = version.getName() + " " + version.getVersion();

        if (version.getBuildNumber() != 0) startupMessage += " Build " + version.getBuildNumber();
        startupMessage += " (for " + version.getSupportedVersion() + ")";

        LOGGER.info(startupMessage);
        LOGGER.info("Build date: " + version.getBuildDate());
        LOGGER.info("System environment: " + System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ", " + System.getProperty("os.version") + ")");
        LOGGER.info("JVM: " + System.getProperty("java.vm.name") + " " + System.getProperty("java.vm.version"));
        LOGGER.info("Java: " + System.getProperty("java.specification.name") + " " + System.getProperty("java.specification.version"));

        asyncExecQueue = new ConcurrentLinkedQueue<>();

        eventManager = new EventManagerRoot();
        amxInstanceManager = new AmxInstanceManagerImpl(eventManager, amxHandles);

        resourceConfig = new ResourceConfig(new FileInputStream(new File(config.getShoebillDir(), RESOURCES_CONFIG_FILENAME)));
        artifactLocator = new ShoebillArtifactLocator(config, resourceConfig);

        if (artifactLocator.getGamemodeFile() == null) {
            LOGGER.info("There's no gamemode assigned in " + RESOURCES_CONFIG_FILENAME + " or file not found.");
            LOGGER.info("Shoebill will only use plugins!");
            //throw new NoGamemodeAssignedException();
        }


		/*File configFile = new File("server.cfg");
        if(configFile.exists())
		{
			try(BufferedReader reader = new BufferedReader(new FileReader(configFile)))
			{
				String line;
				while((line = reader.readLine()) != null)
				{
					if(line.startsWith("gamemode"))
					{
						String[] splits = line.split("[ ]");
						if(splits.length > 1) {
							gamemodeName = splits[1];
							return;
						}
					}
				}
			}
		}*/
    }
    //private String gamemodeName;

    public static ShoebillImpl getInstance() {
        return (ShoebillImpl) Shoebill.get();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("version", version).toString();
    }

    private void initLogger(File configFile) {
        if (configFile.exists()) {
            DOMConfigurator.configureAndWatch(configFile.getPath());
        } else {
            DOMConfigurator.configure(this.getClass().getClassLoader().getResource(LOG4J_CONFIG_FILENAME));
        }

        originOutPrintStream = System.out;
        originErrPrintStream = System.err;
        System.setOut(new PrintStream(new LoggerOutputStream(LoggerFactory.getLogger("System.out"), LogLevel.INFO), true));
        System.setErr(new PrintStream(new LoggerOutputStream(LoggerFactory.getLogger("System.err"), LogLevel.ERROR), true));

        if (!configFile.exists())
            LOGGER.info("Could not find " + configFile.getPath() + " file. The default configuration will be used.");

    }

    private void registerRootCallbackHandler() {
        sampCallbackManager.registerCallbackHandler(new SampCallbackHandler() {

            @Override
            public boolean onGameModeInit() {
                if (!initialized) {
                    try {
                        loadPluginsAndGamemode();
                        initialized = true;
                        return true;
                    } catch (Throwable e) {
                        e.printStackTrace();
                        return false;
                    }
                }
                return true;
            }

            @Override
            public boolean onGameModeExit() {
                if (initialized) {
                    unloadPluginsAndGamemode();
                    uninitialize();
                    initialized = false;
                    if (restart) {
                        SampNativeFunction.restartShoebill();
                        restart = false;
                    }
                }
                return true;
            }

            @Override
            public int onRconCommand(String cmd) {
                String[] splits = cmd.split(" ");
                String op = splits[0].toLowerCase();

                switch (op) {
                    case "reload":
                        reload();
                        return 1;

                    case "onlineonce":
                        try {
                            new File(config.getShoebillDir(), "ONLINE_MODE_ONCE").createNewFile();
                        } catch (IOException e) {
                            LOGGER.info("Failed to create flag file.");
                        }
                        return 1;

                    case "gc":
                        System.gc();
                        return 1;

                    case "mem":
                        Runtime runtime = Runtime.getRuntime();
                        LOGGER.info("FreeMem: " + runtime.freeMemory() + " bytes / TotalMem: " + runtime.totalMemory() + " bytes.");
                        return 1;

                    default:
                        return 1;
                }
            }

            @Override
            public void onProcessTick() {
                while (!asyncExecQueue.isEmpty()) asyncExecQueue.poll().run();
            }

            @Override
            public boolean isActive() {
                return true;
            }

            @Override
            public void onAmxLoad(int handle) {
                amxInstanceManager.onAmxLoad(handle);
                if (!initialized) {
                    try {
                        loadPluginsAndGamemode();
                        initialized = true;
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onAmxUnload(int handle) {
                amxInstanceManager.onAmxUnload(handle);
            }

            @Override
            public void onShoebillUnload() {
                if (initialized) {
                    uninitialize();
                    unloadPluginsAndGamemode();
                    initialized = false;
                }
            }

            @Override
            public void onShoebillLoad() {
                try {
                    if (!initialized)
                        initialize();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initialize() {
        pluginManager = new ResourceManagerImpl(this, eventManager, artifactLocator, config.getDataDir());
        serviceManager = new ServiceManagerImpl(eventManager);

        sampObjectManager = new SampObjectManagerImpl(eventManager);

        Server.get().setServerCodepage(config.getServerCodepage());

        //sampEventLogger = new SampEventLogger(); // annoying messages
        sampEventDispatcher = new SampEventDispatcher(sampObjectManager, eventManager);

        sampCallbackManager.registerCallbackHandler(sampObjectManager.getCallbackHandler());
        sampCallbackManager.registerCallbackHandler(sampEventDispatcher);
        //sampCallbackManager.registerCallbackHandler(sampEventLogger);
    }

    private void uninitialize() {
        System.setOut(originOutPrintStream);
        System.setErr(originErrPrintStream);
    }

    private void loadPluginsAndGamemode() {
        pluginManager.loadAllResource();
    }

    private void unloadPluginsAndGamemode() {
        pluginManager.unloadAllResource();
    }

    public SampCallbackHandler getCallbackHandler() {
        if (sampCallbackManager == null) {
            sampCallbackManager = new SampCallbackManagerImpl();
            registerRootCallbackHandler();
        }

        return sampCallbackManager.getMasterCallbackHandler();
    }

    public ShoebillConfig getConfig() {
        return config;
    }

    public ResourceConfig getResourceConfig() {
        return resourceConfig;
    }

    public ShoebillArtifactLocator getArtifactLocator() {
        return artifactLocator;
    }

    public EventManager getRootEventManager() {
        return eventManager;
    }

    @Override
    public SampObjectManager getSampObjectManager() {
        return sampObjectManager;
    }

    @Override
    public AmxInstanceManagerImpl getAmxInstanceManager() {
        return amxInstanceManager;
    }

    @Override
    public ResourceManager getResourceManager() {
        return pluginManager;
    }

    @Override
    public ServiceManagerImpl getServiceStore() {
        return serviceManager;
    }

    @Override
    public ShoebillVersion getVersion() {
        return version;
    }

    @Override
    public void reload() {
        restart = true;
        SampNativeFunction.sendRconCommand("gmx");
    }

    public SampEventDispatcher getSampEventDispatcher() {
        return sampEventDispatcher;
    }

    @Override
    public void runOnSampThread(Runnable runnable) {
        asyncExecQueue.offer(runnable);
    }
}
