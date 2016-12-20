package net.gtaun.shoebill.amx

import net.gtaun.shoebill.SampNativeFunction
import net.gtaun.shoebill.amx.types.ReturnType
import org.slf4j.LoggerFactory

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

        arguments.forEachIndexed { index, any ->
            arguments[index] = AmxCallableClasses.primitiveToComposite(any)
        }

        val types: MutableList<String> = mutableListOf()
        val VALID_CLASSES = AmxCallableClasses.VALID_CLASSES

        arguments.forEach {
            if (!VALID_CLASSES.containsKey(it.javaClass)) {
                LOGGER.error("Cannot convert ${it.javaClass.simpleName} to a basic type. Please make sure that you " +
                        "use a valid class for the usage of the native interface.")
                return null
            } else {
                types.add(VALID_CLASSES[it.javaClass]!!)
            }
        }

        try {
            if (type == AmxCallableType.NATIVE)
                return SampNativeFunction.callFunction(amxInstance.handle, id, returnType.value, arguments, *types.toTypedArray())
            else if (type == AmxCallableType.PUBLIC)
                return SampNativeFunction.callPublic(amxInstance.handle, id, returnType.value, arguments, *types.toTypedArray())
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return null
    }

    companion object {

        val LOGGER = LoggerFactory.getLogger("Amx Callable")

    }
}
