package com.kyfstore.mcversionrenamer.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;

public class KeyInputUtil {

    private final String keyTranslationKey;
    private boolean wasPressed = false;
    private boolean wasJustToggled = false;

    public KeyInputUtil(String keyTranslationKey) {
        this.keyTranslationKey = keyTranslationKey;
    }

    public boolean handleKeyPress(Runnable toggleAction) {
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

        return wasJustToggled;
    }

    public boolean wasJustToggled() {
        return wasJustToggled;
    }

    public boolean wasPressed() {
        return wasPressed;
    }
}