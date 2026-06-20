package com.kyfstore.mcversionrenamer.plugin.api

abstract class PluginInitializer {
    var main: PluginMain? = null

    abstract fun onInitialize()

    protected fun setPluginMain(pluginObject: PluginObject) {
        this.main = pluginObject.main
        pluginObject.main.onMainCall()
    }
}
