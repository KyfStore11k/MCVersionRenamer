package com.kyfstore.mcversionrenamer.libapi.core.plugin.api

import com.kyfstore.mcversionrenamer.MCVersionRenamer
import com.kyfstore.mcversionrenamer.libapi.core.plugin.core.main.data.PublicPluginRegistry

data class PluginObject(val id: String, val main: PluginMain, val jarFilePath: String, var isEnabled: Boolean = true) {
    fun onEnable() {
        if (isEnabled) {
            PublicPluginRegistry.plugins.getOrPut(id) { mutableMapOf() }[main] = this
            main.onMainCall()
        }
    }

    fun onDisable() {
        MCVersionRenamer.LOGGER.info("Plugin $id has been disabled!")
    }

    fun disablePlugin() {
        isEnabled = false
    }

    fun enablePlugin() {
        isEnabled = true
    }
}