package com.kyfstore.mcversionrenamer.plugin.main.control

import com.kyfstore.mcversionrenamer.MCVersionRenamer
import com.kyfstore.mcversionrenamer.plugin.api.PluginObject
import com.kyfstore.mcversionrenamer.plugin.main.data.PublicPluginRegistry

class PluginManager {

    companion object {
        @JvmStatic
        fun loadPlugins() {
            defaultInit()
        }

        private fun defaultInit() {
            PluginFileManager.checkPluginDirectoryExists()
            PluginFileManager.listAndSavePlugins();
        }

        @JvmStatic
        fun getPlugins(): List<PluginObject> {
            return PublicPluginRegistry.plugins.flatMap { it.value.values }.toList()
        }

        @JvmStatic
        fun getPluginById(pluginId: String): PluginObject? {
            return PublicPluginRegistry.plugins[pluginId]?.values?.firstOrNull()
        }

        @JvmStatic
        fun getPluginByIndex(index: Int): PluginObject? {
            return getPlugins().getOrNull(index)
        }

        @JvmStatic
        fun disablePluginById(pluginId: String) {
            val plugin = PublicPluginRegistry.plugins[pluginId]?.values?.firstOrNull()
            plugin?.onDisable()
            MCVersionRenamer.LOGGER.info("Plugin $pluginId disabled.")
        }

        @JvmStatic
        fun disablePluginByIndex(index: Int) {
            val plugin = getPluginByIndex(index)
            plugin?.onDisable()
            MCVersionRenamer.LOGGER.info("Plugin at index $index disabled.")
        }

        @JvmStatic
        fun removePluginById(pluginId: String) {
            val removed = PublicPluginRegistry.plugins.remove(pluginId)
            if (removed != null) {
                MCVersionRenamer.LOGGER.info("Plugin $pluginId removed.")
            } else {
                MCVersionRenamer.LOGGER.error("Plugin $pluginId not found.")
            }
        }

        @JvmStatic
        fun removePluginByIndex(index: Int) {
            val pluginList = getPlugins()
            if (index in pluginList.indices) {
                val pluginToRemove = pluginList[index]
                PublicPluginRegistry.plugins.entries.removeIf { entry ->
                    entry.value.containsValue(pluginToRemove)
                }
                MCVersionRenamer.LOGGER.info("Plugin at index $index removed.")
            } else {
                MCVersionRenamer.LOGGER.error("Plugin at index $index not found.")
            }
        }
    }
}