package com.kyfstore.mcversionrenamer.util;

import com.kyfstore.mcversionrenamer.gui.MCVersionRenamerScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;

public class KeyInputUtil {

    private final String keyTranslationKey;
    private boolean wasPressed = false;
    private boolean wasJustToggled = false;

    public KeyInputUtil(String keyTranslationKey) {
        this.keyTranslationKey = keyTranslationKey;
    }

    public void handleKeyPress(Runnable toggleAction) {
        int keyCode = InputUtil.fromTranslationKey(keyTranslationKey).getCode();
        boolean isPressed = InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow(), keyCode);

        wasJustToggled = false;

        if (isPressed && !wasPressed) {
            wasPressed = true;
            if (!(MinecraftClient.getInstance().currentScreen instanceof MCVersionRenamerScreen)) {
                toggleAction.run();
                wasJustToggled = true;
            }
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