package com.kyfstore.mcversionrenamer.plugin.main.control

interface PluginAPI {

    fun registerService(name: String, service: Any)

    fun <T> getService(name: String): T?

    fun sendEvent(event: Any)

    fun subscribe(eventType: Class<*>, handler: (Any) -> Unit)
}