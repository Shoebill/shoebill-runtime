package net.gtaun.shoebill.amx

import net.gtaun.shoebill.SampNativeFunction
import net.gtaun.shoebill.event.amx.AmxCallEvent
import net.gtaun.shoebill.event.amx.AmxLoadEvent
import net.gtaun.shoebill.event.amx.AmxUnloadEvent
import net.gtaun.util.event.EventManager
import java.util.*
import java.util.function.Consumer

/**
 * @author Marvin Haschker
 */
class AmxInstanceManagerImpl(private val eventManager: EventManager, existingHandles: IntArray) : AmxInstanceManager() {

    private val instances: MutableSet<AmxInstance> = HashSet()
    private val hooks: MutableList<AmxHook> = ArrayList()

    init {
        existingHandles.forEach { instances.add(AmxInstanceImpl(it)) }
    }

    fun onAmxLoad(handle: Int) {
        val instance = AmxInstanceImpl(handle)
        instances.add(instance)
        eventManager.dispatchEvent(AmxLoadEvent(instance), this)
    }

    fun onAmxUnload(handle: Int) {
        val instance = AmxInstanceImpl(handle)
        instances.remove(instance)
        eventManager.dispatchEvent(AmxUnloadEvent(instance), this)
    }

    override val amxInstances: Set<AmxInstance>
        get() = Collections.unmodifiableSet(instances)

    override val amxHooks: Set<AmxHook>
        get() = Collections.unmodifiableSet(HashSet(hooks))

    override fun hookCallback(callbackName: String, hook: Consumer<AmxCallEvent>, types: String): Boolean {
        if (SampNativeFunction.hookCallback(callbackName, types)) {
            hooks.add(AmxHook(callbackName, hook, types))
            return true
        }
        return false
    }

    override fun unhookCallback(callbackName: String): Boolean {
        val hook = hooks.filter({ it.name === callbackName }).firstOrNull()
        if (SampNativeFunction.unhookCallback(callbackName) && hook != null) {
            hooks.remove(hook)
            return true
        }
        return false
    }
}
