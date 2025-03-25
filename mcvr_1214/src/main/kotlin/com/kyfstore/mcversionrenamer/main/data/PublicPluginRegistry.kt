package com.kyfstore.mcversionrenamer.libapi.core.plugin.core.main.data

import com.kyfstore.mcversionrenamer.libapi.core.plugin.api.PluginMain
import com.kyfstore.mcversionrenamer.libapi.core.plugin.api.PluginObject
import net.fabricmc.loader.api.FabricLoader
import java.io.File


class PublicPluginRegistry {
    companion object {
        @JvmStatic
        var plugins: MutableMap<String, MutableMap<PluginMain, PluginObject>> = HashMap()

        @JvmStatic
        val pluginDirectory: File = FabricLoader.getInstance().gameDir.resolve("mods/mcvrplugins").toFile()
    }
}