package com.kyfstore.mcversionrenamer.plugin.api.minecraft.client

import com.kyfstore.mcversionrenamer.MCVersionRenamer
import net.minecraft.client.Minecraft

class ClientAPI {
    var isInitialized: Boolean = false

    fun onEnable() {
        this.isInitialized = true
    }

    fun getMinecraftClientInstance(): Any? {
        return Minecraft.getInstance()
    }

    fun chain(): ReflectedObject {
        return ReflectedObject(getMinecraftClientInstance(), this)
    }

    fun getClientField(fieldName: String): Any? {
        val client = getMinecraftClientInstance() ?: return null
        return getFieldFromInstance(client, fieldName)
    }

    fun invokeClientMethod(methodName: String, vararg args: Any?): Any? {
        val client = getMinecraftClientInstance() ?: return null
        return invokeMethodOnInstance(client, methodName, *args)
    }

    internal fun getFieldFromInstance(instance: Any, fieldName: String): Any? {
        return try {
            val field = try {
                instance.javaClass.getDeclaredField(fieldName)
            } catch (e: NoSuchFieldException) {
                instance.javaClass.getField(fieldName)
            }
            field.isAccessible = true
            field.get(instance)
        } catch (e: Exception) {
            MCVersionRenamer.LOGGER.error("Failed to reflect field $fieldName on ${instance.javaClass.simpleName}", e)
            null
        }
    }

    internal fun invokeMethodOnInstance(instance: Any, methodName: String, vararg args: Any?): Any? {
        return try {
            val bestMethod = instance.javaClass.methods
                .filter { it.name == methodName && it.parameterCount == args.size }
                .map { method -> method to calculateMatchScore(method.parameterTypes, args) }
                .filter { it.second >= 0 }
                .maxByOrNull { it.second }
                ?.first ?: throw NoSuchMethodException("No matching method $methodName found on ${instance.javaClass.simpleName}.")

            bestMethod.isAccessible = true
            bestMethod.invoke(instance, *args)
        } catch (e: Exception) {
            MCVersionRenamer.LOGGER.error("Failed to reflect method $methodName on ${instance.javaClass.simpleName}", e)
            null
        }
    }

    private fun calculateMatchScore(paramTypes: Array<Class<*>>, args: Array<out Any?>): Int {
        var totalScore = 0
        for (i in paramTypes.indices) {
            val paramType = paramTypes[i]
            val arg = args[i]

            if (arg == null) {
                if (paramType.isPrimitive) return -1
                totalScore += 1
            } else {
                val argType = arg.javaClass
                when {
                    paramType == argType -> totalScore += 10
                    paramType.isPrimitive && isWrapperOf(paramType, argType) -> totalScore += 8
                    paramType.isAssignableFrom(argType) -> totalScore += 5
                    else -> return -1
                }
            }
        }
        return totalScore
    }

    private fun isWrapperOf(primitive: Class<*>, wrapper: Class<*>): Boolean {
        return when (primitive) {
            java.lang.Integer.TYPE -> wrapper == java.lang.Integer::class.java
            java.lang.Boolean.TYPE -> wrapper == java.lang.Boolean::class.java
            java.lang.Long.TYPE -> wrapper == java.lang.Long::class.java
            java.lang.Double.TYPE -> wrapper == java.lang.Double::class.java
            java.lang.Float.TYPE -> wrapper == java.lang.Float::class.java
            java.lang.Byte.TYPE -> wrapper == java.lang.Byte::class.java
            java.lang.Character.TYPE -> wrapper == java.lang.Character::class.java
            java.lang.Short.TYPE -> wrapper == java.lang.Short::class.java
            else -> false
        }
    }
}