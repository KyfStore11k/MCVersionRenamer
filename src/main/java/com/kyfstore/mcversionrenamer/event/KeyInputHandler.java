package com.kyfstore.mcversionrenamer.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    public static final String KEY_CATEGORY_MCVERSIONRENAMER = "key.category.mcversionrenamer";
    public static final String KEY_TOGGLE_BUTTON = "key.mcversionrenamer.togglebutton";

    public static KeyBinding toggleButtonKey;

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (toggleButtonKey.wasPressed()) {
                // Handle the key press event here if needed
            }
        });
    }

    public static void register() {
        toggleButtonKey = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        KEY_TOGGLE_BUTTON,
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_M,
                        KEY_CATEGORY_MCVERSIONRENAMER
                )
        );

        registerKeyInputs();
    }
}
