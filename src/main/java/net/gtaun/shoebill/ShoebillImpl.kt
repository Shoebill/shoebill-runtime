/**
 * Copyright (C) 2011-2014 MK124

 * Licensed under the Apache License, Version 2.0 (the "License"){}
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.gtaun.shoebill

import net.gtaun.shoebill.amx.AmxInstanceManagerImpl
import net.gtaun.shoebill.entities.Server
import net.gtaun.shoebill.resource.ResourceManager
import net.gtaun.shoebill.resource.ResourceManagerImpl
import net.gtaun.shoebill.samp.SampCallbackHandler
import net.gtaun.shoebill.samp.SampCallbackManagerImpl
import net.gtaun.shoebill.service.ServiceManagerImpl
import net.gtaun.shoebill.util.log.LogLevel
import net.gtaun.shoebill.util.log.LoggerOutputStream
import net.gtaun.util.event.EventManager
import net.gtaun.util.event.EventManagerRoot
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import org.apache.log4j.xml.DOMConfigurator
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.PrintStream
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * @author MK124
 */
class ShoebillImpl @Throws(IOException::class) constructor(amxHandles: IntArray) : Shoebill() {

    private var isInitialized: Boolean = false
    private var isRestart: Boolean = false
    override val version: ShoebillVersionImpl =
            ShoebillVersionImpl(this.javaClass.classLoader.getResourceAsStream(VERSION_FILENAME))
    val config: ShoebillConfig = ShoebillConfig(FileInputStream(SHOEBILL_CONFIG_PATH))
    val resourceConfig: ResourceConfig = ResourceConfig(FileInputStream(File(config.shoebillDir, RESOURCES_CONFIG_FILENAME)))
    val artifactLocator: ShoebillArtifactLocator = ShoebillArtifactLocator(config, resourceConfig)
    private val eventManager: EventManagerRoot = EventManagerRoot()
    private var sampCallbackManager: SampCallbackManagerImpl = SampCallbackManagerImpl()
    override val amxInstanceManager: AmxInstanceManagerImpl = AmxInstanceManagerImpl(eventManager, amxHandles)
    lateinit override var sampObjectManager: SampObjectManagerImpl
    lateinit private var pluginManager: ResourceManagerImpl
    lateinit override var serviceStore: ServiceManagerImpl
        private set

    lateinit var sampEventDispatcher: SampEventDispatcher
        private set
    private var originOutPrintStream: PrintStream = System.out
    private var originErrPrintStream: PrintStream = System.err
    private val asyncExecQueue: Queue<Runnable> = ConcurrentLinkedQueue<Runnable>()

    init {
        initLogger(File(config.shoebillDir, LOG4J_CONFIG_FILENAME))

        var startupMessage = "${version.name} ${version.version}"

        if (version.buildNumber != 0) startupMessage += " Build ${version.buildNumber}"
        startupMessage += " (for ${version.supportedVersion})"

        LOGGER.info(startupMessage)
        LOGGER.info("Build date: ${version.buildDate}")
        LOGGER.info("System environment: ${System.getProperty("os.name")} (${System.getProperty("os.arch")}, ${System.getProperty("os.version")})")
        LOGGER.info("JVM: ${System.getProperty("java.vm.name")} ${System.getProperty("java.vm.version")}")
        LOGGER.info("Java: ${System.getProperty("java.specification.name")} ${System.getProperty("java.specification.version")}")

        if (artifactLocator.gamemodeFile == null) {
            LOGGER.info("There's no gamemode assigned in $RESOURCES_CONFIG_FILENAME or file not found.")
            LOGGER.info("Shoebill will only use plugins!")
        }

        registerRootCallbackHandler()
    }

    override fun toString(): String = ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
            .append("version", version)
            .toString()

    private fun initLogger(configFile: File) {
        if (configFile.exists())
            DOMConfigurator.configureAndWatch(configFile.path)
        else
            DOMConfigurator.configure(this.javaClass.classLoader.getResource(LOG4J_CONFIG_FILENAME))

        System.setOut(PrintStream(LoggerOutputStream(LoggerFactory.getLogger("System.out"), LogLevel.INFO), true))
        System.setErr(PrintStream(LoggerOutputStream(LoggerFactory.getLogger("System.err"), LogLevel.ERROR), true))

        if (!configFile.exists())
            LOGGER.info("Could not find " + configFile.path + " file. The default configuration will be used.")

    }

    private fun registerRootCallbackHandler() {
        sampCallbackManager.registerCallbackHandler(object : SampCallbackHandler {

            override fun onGameModeInit(): Boolean {
                if (!isInitialized) {
                    try {
                        loadPluginsAndGamemode()
                        isInitialized = true
                        return true
                    } catch (e: Throwable) {
                        e.printStackTrace()
                        return false
                    }

                }
                return true
            }

            override fun onGameModeExit(): Boolean {
                if (isInitialized) {
                    unloadPluginsAndGamemode()
                    uninitialize()
                    isInitialized = false
                    if (isRestart) {
                        SampNativeFunction.restartShoebill()
                        isRestart = false
                    }
                }
                return true
            }

            override fun onRconCommand(cmd: String): Int {
                val splits = cmd.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val op = splits[0].toLowerCase()

                when (op) {
                    "reload" -> {
                        reload()
                        return 1
                    }

                    "onlineonce" -> {
                        File(config.shoebillDir, "ONLINE_MODE_ONCE").createNewFile()
                        return 1
                    }

                    "gc" -> {
                        System.gc()
                        return 1
                    }

                    "mem" -> {
                        val runtime = Runtime.getRuntime()
                        LOGGER.info("FreeMem: " + runtime.freeMemory() + " bytes / TotalMem: " + runtime.totalMemory() + " bytes.")
                        return 1
                    }

                    else -> return 1
                }
            }

            override fun onProcessTick() {
                while (!asyncExecQueue.isEmpty()) asyncExecQueue.poll().run()
            }

            override fun onAmxLoad(handle: Int) {
                amxInstanceManager.onAmxLoad(handle)
                if (!isInitialized) {
                    try {
                        loadPluginsAndGamemode()
                        isInitialized = true
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }

                }
            }

            override fun onAmxUnload(handle: Int) {
                amxInstanceManager.onAmxUnload(handle)
            }

            override fun onShoebillUnload() {
                if (isInitialized) {
                    uninitialize()
                    unloadPluginsAndGamemode()
                    isInitialized = false
                }
            }

            override fun onShoebillLoad() {
                try {
                    if (!isInitialized)
                        initialize()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }

            }
        })
    }

    private fun initialize() {
        pluginManager = ResourceManagerImpl(this, eventManager, artifactLocator, config.dataDir)
        serviceStore = ServiceManagerImpl(eventManager)

        sampObjectManager = SampObjectManagerImpl(eventManager)

        Server.get().serverCodepage = config.serverCodepage

        sampEventDispatcher = SampEventDispatcher(sampObjectManager, eventManager)

        sampCallbackManager.registerCallbackHandler(sampObjectManager.callbackHandler)
        sampCallbackManager.registerCallbackHandler(sampEventDispatcher)
    }

    private fun uninitialize() {
        System.setOut(originOutPrintStream)
        System.setErr(originErrPrintStream)
    }

    private fun loadPluginsAndGamemode() = pluginManager.loadAllResource()
    private fun unloadPluginsAndGamemode() = pluginManager.unloadAllResource()

    val callbackHandler: SampCallbackHandler
        get() = sampCallbackManager.masterCallbackHandler

    val rootEventManager: EventManager
        get() = eventManager

    override val resourceManager: ResourceManager
        get() = pluginManager


    override fun reload() {
        isRestart = true
        SampNativeFunction.sendRconCommand("gmx")
    }

    override fun runOnSampThread(runnable: Runnable) = asyncExecQueue.offer(runnable)

    companion object {
        private val LOGGER = LoggerFactory.getLogger(ShoebillImpl::class.java)

        private val VERSION_FILENAME = "version.yml"

        private val SHOEBILL_CONFIG_PATH = "./shoebill/shoebill.yml"
        private val LOG4J_CONFIG_FILENAME = "log4j.xml"
        private val RESOURCES_CONFIG_FILENAME = "resources.yml"

        val instance: ShoebillImpl
            get() = Shoebill.get() as ShoebillImpl
    }
}
