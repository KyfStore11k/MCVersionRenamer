package com.kyfstore.mcversionrenamer.plugin.api

import com.kyfstore.mcversionrenamer.plugin.main.control.PluginAPI

abstract class PluginInitializer {
    var main: PluginMain? = null

    lateinit var api: PluginAPI

    abstract fun onInitialize()

    protected fun setPluginMain(pluginObject: PluginObject) {
        this.main = pluginObject.main
        pluginObject.main.onMainCall()
    }
}
