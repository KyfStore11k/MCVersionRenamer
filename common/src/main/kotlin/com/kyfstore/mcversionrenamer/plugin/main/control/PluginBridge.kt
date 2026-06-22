package com.kyfstore.mcversionrenamer.plugin.main.control

object PluginBridge : PluginAPI {

    private val services = mutableMapOf<String, Any>()

    private val eventSubscribers =
        mutableMapOf<Class<*>, MutableList<(Any) -> Unit>>()

    override fun registerService(name: String, service: Any) {
        services[name] = service
    }

    override fun <T> getService(name: String): T? {
        return services[name] as? T
    }

    override fun sendEvent(event: Any) {
        eventSubscribers[event::class.java]?.forEach {
            it(event)
        }
    }

    override fun subscribe(eventType: Class<*>, handler: (Any) -> Unit) {
        eventSubscribers
            .computeIfAbsent(eventType) { mutableListOf() }
            .add(handler)
    }
}