package net.gtaun.shoebill.amx

import net.gtaun.shoebill.SampNativeFunction
import net.gtaun.shoebill.amx.types.ReturnType

/**
 * @author Marvin Haschker
 */
internal class AmxCallableImpl(private val amxInstance: AmxInstance, private val id: Int,
                               override val name: String, private val type: AmxCallableType,
                               override val returnType: ReturnType) : AmxCallable() {

    override fun call(vararg args: Any): Any? {
        val arguments: Array<Any> = arrayOf(*args)
        args.filter { it is Boolean }.forEachIndexed { index, any ->
            arguments[index] = if((any as Boolean) == true) 1 else 0
        }

        try {
            if (type == AmxCallableType.NATIVE)
                return SampNativeFunction.callFunction(amxInstance.handle, id, returnType.value, *arguments)
            else if (type == AmxCallableType.PUBLIC)
                return SampNativeFunction.callPublic(amxInstance.handle, id, returnType.value, *arguments)
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        return null
    }
}
