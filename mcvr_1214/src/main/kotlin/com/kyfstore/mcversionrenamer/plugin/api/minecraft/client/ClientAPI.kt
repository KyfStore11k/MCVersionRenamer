package com.kyfstore.mcversionrenamer.plugin.api.minecraft.client

import com.kyfstore.mcversionrenamer.MCVersionRenamer
import net.minecraft.client.MinecraftClient
import org.slf4j.LoggerFactory

class ClientAPI {
    var isInitialized: Boolean = false

    fun onEnable() {
        this.isInitialized = true
    }

    fun getMinecraftClientInstance(): MinecraftClient {
        return MinecraftClient.getInstance()
    }
}