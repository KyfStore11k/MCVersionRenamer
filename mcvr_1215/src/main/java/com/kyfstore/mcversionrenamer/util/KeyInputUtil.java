package com.kyfstore.mcversionrenamer.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;

/**
 * Utility class for handling key press toggle behavior.
 */
public class KeyInputUtil {

    private final String keyTranslationKey;
    private boolean wasPressed = false;
    private boolean wasJustToggled = false;

    public KeyInputUtil(String keyTranslationKey) {
        this.keyTranslationKey = keyTranslationKey;
    }

    /**
     * Checks if the associated key was just pressed and executes the given action if so.
     *
     * @param toggleAction A Runnable action to execute when the key is pressed.
     */
    public void handleKeyPress(Runnable toggleAction) {
        MinecraftClient client = MinecraftClient.getInstance();
        long windowHandle = client.getWindow().getHandle();
        int keyCode = InputUtil.fromTranslationKey(keyTranslationKey).getCode();
        boolean isPressed = InputUtil.isKeyPressed(windowHandle, keyCode);

        wasJustToggled = false;

        if (isPressed && !wasPressed) {
            wasPressed = true;
            toggleAction.run();
            wasJustToggled = true;
        } else if (!isPressed) {
            wasPressed = false;
        }
    }

    public boolean wasJustToggled() {
        return wasJustToggled;
    }

    public boolean isPressed() {
        return wasPressed;
    }
}