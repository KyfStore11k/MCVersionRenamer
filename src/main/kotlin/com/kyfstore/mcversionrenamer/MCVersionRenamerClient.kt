package com.kyfstore.mcversionrenamer

import com.kyfstore.mcversionrenamer.event.KeyInputHandler
import com.kyfstore.mcversionrenamer.rewrites.MCVersionRenamerMinecraftVersionClass
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import com.kyfstore.mcversionrenamer.rewrites.MCVersionRenamerMinecraftGameVersion
import net.minecraft.client.MinecraftClient

class MCVersionRenamerClient : ClientModInitializer {

    override fun onInitializeClient() {
        val versionClass: MCVersionRenamerMinecraftGameVersion = MCVersionRenamerMinecraftVersionClass.create()

        publicVersionClass = versionClass

        KeyInputHandler.register()

        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick { client: MinecraftClient? ->
            publicClient = client
            if (client != null && client.window != null) {
                client.window.setTitle(versionClass.name)
            }
        })
    }

    companion object {
        @JvmStatic
        private var publicClient: MinecraftClient? = null

        @JvmStatic
        private var publicVersionClass: MCVersionRenamerMinecraftGameVersion? = null

        @JvmStatic
        fun setClientWindowName(newTitle: String) {
            publicVersionClass?.name = newTitle
        }
    }
}
