package com.kyfstore.mcversionrenamer.plugin.api.minecraft.client

class ReflectedObject(val instance: Any?, private val api: ClientAPI) {

    fun isNull(): Boolean = instance == null

    fun getField(fieldName: String): ReflectedObject {
        if (instance == null) return this
        val value = api.getFieldFromInstance(instance, fieldName)
        return ReflectedObject(value, api)
    }

    fun invokeMethod(methodName: String, vararg args: Any?): ReflectedObject {
        if (instance == null) return this
        val value = api.invokeMethodOnInstance(instance, methodName, *args)
        return ReflectedObject(value, api)
    }

    fun unwrap(): Any? = instance
}