package net.gtaun.shoebill.amx

import net.gtaun.shoebill.SampNativeFunction
import net.gtaun.shoebill.amx.types.ReturnType
import java.util.function.Function

/**
 * @author Marvin Haschker
 */
internal class AmxInstanceImpl(override val handle: Int) : AmxInstance() {

    private val registeredFunctions: MutableMap<String, (Array<Any>) -> Int> = mutableMapOf()

    override fun getPublic(name: String, returnType: ReturnType): AmxCallable? {
        val funcHandle = SampNativeFunction.getPublic(handle, name)
        if (funcHandle == AmxCallable.INVALID_CALLABLE)
            return null
        return AmxCallableImpl(this, funcHandle, name, AmxCallableType.PUBLIC, returnType)
    }

    override fun getNative(name: String, returnType: ReturnType): AmxCallable? {
        val id = SampNativeFunction.getNative(name)
        return if (id != 0)
            AmxCallableImpl(this, id, name, AmxCallableType.NATIVE, returnType)
        else
            null
    }

    override fun unregisterFunction(name: String): Boolean {
        if (!registeredFunctions.containsKey(name))
            return false
        val result = SampNativeFunction.unregisterFunction(handle, name)
        if (result) registeredFunctions.remove(name)
        return result
    }

    override fun callRegisteredFunction(name: String, parameters: Array<Any>): Int {
        return registeredFunctions[name]?.invoke(parameters) ?: -1
    }

    override fun isFunctionRegistered(name: String): Boolean = registeredFunctions.containsKey(name)

    override fun registerFunction(name: String, callback: (Array<Any>) -> Int,
                                  vararg types: String): Boolean {
        if (registeredFunctions.containsKey(name)) return false
        val result = SampNativeFunction.registerFunction(handle, name, *types)
        if (result) registeredFunctions[name] = callback
        return result
    }
}
