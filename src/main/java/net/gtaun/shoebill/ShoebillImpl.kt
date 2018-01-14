/**
 * Copyright (C) 2011-2016 MK124

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
import org.apache.log4j.xml.DOMConfigurator
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileInputStream
import java.io.PrintStream
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * @author MK124
 * @author Marvin Haschker
 */
class ShoebillImpl constructor(amxHandles: IntArray) : Shoebill() {
    override val version: ShoebillVersionImpl =
            ShoebillVersionImpl(this.javaClass.classLoader.getResourceAsStream(VERSION_FILENAME))
    val config: ShoebillConfig = ShoebillConfig(FileInputStream(SHOEBILL_CONFIG_PATH))
    val resourceConfig: ResourceConfig = ResourceConfig(FileInputStream(File(config.shoebillDir, RESOURCES_CONFIG_FILENAME)))
    val artifactLocator: ShoebillArtifactLocator = ShoebillArtifactLocator(config, resourceConfig)
    override val eventManager: EventManagerRoot = EventManagerRoot()
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
        startupMessage += " (for ${version.supportedVersion} and target API ${version.targetApi})"

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

    private fun registerRootCallbackHandler() {
        sampCallbackManager.registerCallbackHandler(object : SampCallbackHandler {

            override fun onGameModeInit(): Boolean {
                initialize()
                loadPluginsAndGamemode()
                return true
            }

            override fun onGameModeExit(): Boolean {
                unloadPluginsAndGamemode()
                uninitialize()
                return true
            }

            override fun onRconCommand(cmd: String): Int {
                val splits = cmd.split(" ".toRegex()).dropLastWhile(String::isEmpty).toTypedArray()
                val op = splits[0].toLowerCase()

                when (op) {
                    "reload" -> {
                        SampNativeFunction.sendRconCommand("gmx")
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

            override fun onAmxLoad(handle: Int) = amxInstanceManager.onAmxLoad(handle)
            override fun onAmxUnload(handle: Int) = amxInstanceManager.onAmxUnload(handle)
        })
    }

    private fun initLogger(configFile: File) {
        if (configFile.exists())
            DOMConfigurator.configureAndWatch(configFile.path)
        else
            DOMConfigurator.configure(this.javaClass.classLoader.getResource(LOG4J_CONFIG_FILENAME))


        originOutPrintStream = System.out
        originErrPrintStream = System.err
        System.setOut(PrintStream(LoggerOutputStream(LoggerFactory.getLogger("System.out"), LogLevel.INFO), true))
        System.setErr(PrintStream(LoggerOutputStream(LoggerFactory.getLogger("System.err"), LogLevel.ERROR), true))

        if (!configFile.exists())
            LOGGER.info("Could not find " + configFile.path + " file. The default configuration will be used.")

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

    @Suppress("unused")
    val callbackHandler: SampCallbackHandler
        get() = sampCallbackManager.masterCallbackHandler

    val rootEventManager: EventManager
        get() = eventManager

    override val resourceManager: ResourceManager
        get() = pluginManager

    override fun runOnMainThread(runnable: Runnable) = asyncExecQueue.offer(runnable)

    companion object {
        private val LOGGER = LoggerFactory.getLogger("Shoebill")
        private val LOG4J_CONFIG_FILENAME = "log4j.xml"
        private val VERSION_FILENAME = "version.yml"

        private val SHOEBILL_CONFIG_PATH = "./shoebill/shoebill.yml"
        private val RESOURCES_CONFIG_FILENAME = "resources.yml"

        val instance: ShoebillImpl
            get() = Shoebill.get() as ShoebillImpl
    }
}