package net.gtaun.shoebill.amx

import net.gtaun.shoebill.SampNativeFunction
import net.gtaun.shoebill.amx.types.ReturnType
import java.util.function.Function

/**
 * @author Marvin Haschker
 */
internal class AmxInstanceImpl(override val handle: Int) : AmxInstance {

    private val registeredFunctions: MutableMap<String, Function<Array<Any>, Int>> = mutableMapOf()

    override fun getPublic(name: String, returnType: ReturnType): AmxCallable? {
        val funcHandle = SampNativeFunction.getPublic(handle, name)
        if (funcHandle == AmxCallable.INVALID_CALLABLE)
            return null
        return AmxCallableImpl(this, funcHandle, name, AmxCallableType.PUBLIC, returnType)
    }

    override fun getNative(name: String, returnType: ReturnType): AmxCallable? {
        val id = SampNativeFunction.getNative(name)
        if (id != 0)
            return AmxCallableImpl(this, id, name, AmxCallableType.NATIVE, returnType)
        else
            return null
    }

    override fun unregisterFunction(name: String): Boolean {
        if (!registeredFunctions.containsKey(name))
            return false
        val result = SampNativeFunction.unregisterFunction(handle, name)
        if (result) registeredFunctions.remove(name)
        return result
    }

    override fun callRegisteredFunction(name: String, vararg parameters: Any): Int {
        return registeredFunctions[name]?.apply(arrayOf(*parameters)) ?: -1
    }

    override fun isFunctionRegistered(name: String): Boolean = registeredFunctions.containsKey(name)

    @SafeVarargs
    override fun registerFunction(name: String, callback: Function<Array<Any>, Int>,
                                  vararg parameterTypes: Class<Any>): Boolean {
        if (registeredFunctions.containsKey(name)) return false
        val result = SampNativeFunction.registerFunction(handle, name, *parameterTypes)
        if (result) registeredFunctions[name] = callback
        return result
    }

    override fun getPublic(name: String): AmxCallable? = getPublic(name, ReturnType.INTEGER)
    override fun getNative(name: String): AmxCallable? = getNative(name, ReturnType.INTEGER)
}
