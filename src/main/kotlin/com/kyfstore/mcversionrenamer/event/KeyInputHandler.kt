package com.kyfstore.mcversionrenamer.event

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW

object KeyInputHandler {
    const val KEY_CATEGORY_MCVERSIONRENAMER: String = "key.category.mcversionrenamer"
    const val KEY_TOGGLE_BUTTON: String = "key.mcversionrenamer.togglebutton"

    var toggleButtonKey: KeyBinding? = null

    fun registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick { client: MinecraftClient? ->
            if (toggleButtonKey!!.wasPressed()) {
            }
        })
    }

    fun register() {
        toggleButtonKey = KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                KEY_TOGGLE_BUTTON,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_M,
                KEY_CATEGORY_MCVERSIONRENAMER
            )
        )

        registerKeyInputs()
    }
}
