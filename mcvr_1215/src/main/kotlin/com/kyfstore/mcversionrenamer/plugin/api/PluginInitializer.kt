package com.kyfstore.mcversionrenamer.plugin.api

abstract class PluginInitializer {
    var main: PluginMain? = null

    // Abstract method that must be overridden by subclasses to initialize the plugin
    abstract fun onInitialize()

    // Set the PluginMain and initialize the plugin
    protected fun setPluginMain(pluginObject: PluginObject) {
        this.main = pluginObject.main
        pluginObject.main.onMainCall()  // Initialize the plugin by calling the main logic
    }
}
