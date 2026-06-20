package com.kyfstore.mcversionrenamer.plugin.api.minecraft.client

import com.kyfstore.mcversionrenamer.MCVersionRenamer
import net.minecraft.client.Minecraft
import org.slf4j.LoggerFactory

class ClientAPI {
    var isInitialized: Boolean = false

    fun onEnable() {
        this.isInitialized = true
    }

    fun getMinecraftClientInstance(): Minecraft? {
        return Minecraft.getInstance()
    }
}