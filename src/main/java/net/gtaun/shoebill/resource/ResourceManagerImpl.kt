/**
 * Copyright (C) 2011 JoJLlmAn
 * Copyright (C) 2011-2012 MK124

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.gtaun.shoebill.resource

import net.gtaun.shoebill.Shoebill
import net.gtaun.shoebill.ShoebillArtifactLocator
import net.gtaun.shoebill.event.resource.ResourceLoadEvent
import net.gtaun.shoebill.event.resource.ResourceUnloadEvent
import net.gtaun.util.event.EventManager
import org.slf4j.LoggerFactory
import java.io.File
import java.lang.reflect.InvocationTargetException

/**
 * @author JoJLlmAn
 * @author MK124
 * @author Marvin Haschker
 */
class ResourceManagerImpl(private val shoebill: Shoebill, private val rootEventManager: EventManager,
                          private val artifactLocator: ShoebillArtifactLocator,
                          private val dataDir: File) : ResourceManager() {

    override val plugins: Collection<Plugin>
        get() = pluginMap.values

    private val pluginMap: MutableMap<Class<out Plugin>, Plugin> = mutableMapOf()
    override var gamemode: Gamemode? = null
        private set

    override fun <T : Plugin> getPlugin(pluginClass: Class<out T>): T? {
        val plugin = pluginMap[pluginClass] ?: return null

        val castedPlugin = pluginClass.cast(plugin)
        if(castedPlugin != null) return castedPlugin

        return pluginClass.cast(plugins.find { pluginClass.isInstance(it) })
    }

    fun loadAllResource() {
        artifactLocator.pluginFiles.forEach { loadPlugin(it) }
        loadGamemode()
    }

    fun unloadAllResource() {
        unloadGamemode()
        plugins.forEach { this.unloadPlugin(it) }
    }

    override fun loadPlugin(filename: String): Plugin? = loadPlugin(artifactLocator.getPluginFile(filename))

    override fun loadPlugin(file: File): Plugin? {
        if (!file.canRead()) return null

        var desc: ResourceDescription? = null
        try {
            desc = ResourceDescription(ResourceType.PLUGIN, file, javaClass.classLoader)
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        if (desc == null) return null
        return loadPlugin(desc)
    }

    private fun loadPlugin(desc: ResourceDescription): Plugin? {
        try {
            LOGGER.info("Loading plugin ${desc.name!!}...")
            val clazz = desc.clazz!!.asSubclass(Plugin::class.java)

            if (pluginMap.containsKey(clazz)) {
                LOGGER.warn("There is a plugin which has the same class as \"${desc.clazz!!.name}\".")
                LOGGER.warn("Abandon loading ${desc.clazz!!.name}")
                return null
            }

            val plugin = constructResource(desc, clazz)
            pluginMap[clazz] = plugin

            val event = ResourceLoadEvent(plugin)
            rootEventManager.dispatchEvent(event, this)

            plugin.enable()
            return plugin
        } catch (e: Throwable) {
            e.printStackTrace()
            return null
        }

    }

    @Throws(NoSuchMethodException::class, SecurityException::class, InstantiationException::class,
            IllegalAccessException::class, IllegalArgumentException::class, InvocationTargetException::class)
    private fun <T : Resource> constructResource(desc: ResourceDescription, clazz: Class<out T>): T {
        val constructor = clazz.getConstructor()
        val resource = constructor.newInstance()

        val resDataDir = File(dataDir, desc.clazz!!.name)
        if (!resDataDir.exists()) resDataDir.mkdirs()

        resource.setContext(desc, shoebill, rootEventManager, resDataDir)
        return resource
    }

    override fun unloadPlugin(plugin: Plugin) {
        val clazz = plugin.javaClass
        if (!pluginMap.containsKey(clazz)) return

        LOGGER.info("Unloading plugin ${clazz.name}...")

        try {
            plugin.disable()
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        val event = ResourceUnloadEvent(plugin)
        rootEventManager.dispatchEvent(event, this)

        pluginMap.remove(clazz)
    }

    private fun loadGamemode() {
        if (artifactLocator.gamemodeFile == null)
            return
        val desc: ResourceDescription?
        try {
            desc = ResourceDescription(ResourceType.GAMEMODE, artifactLocator.gamemodeFile, javaClass.classLoader)

            LOGGER.info("Loading gamemode ${desc.name!!}...")
            val clazz: Class<out Gamemode> = desc.clazz!!.asSubclass(Gamemode::class.java)

            gamemode = constructResource(desc, clazz)

            val event = ResourceLoadEvent(gamemode!!)
            rootEventManager.dispatchEvent(event, this)

            gamemode?.enable()
        } catch (e: Throwable) {
            e.printStackTrace()
        }

    }

    private fun unloadGamemode() {
        if (gamemode == null) return

        try {
            val event = ResourceUnloadEvent(gamemode!!)
            rootEventManager.dispatchEvent(event, this)

            gamemode!!.disable()
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        gamemode = null
    }

    override fun <T : Gamemode> getGamemode(gamemodeClass: Class<T>): T? = gamemodeClass.cast(gamemode)

    companion object {
        private val LOGGER = LoggerFactory.getLogger("Resource Manager")
    }
}
