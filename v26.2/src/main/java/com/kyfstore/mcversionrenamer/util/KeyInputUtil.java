package com.kyfstore.mcversionrenamer.util;

import com.kyfstore.mcversionrenamer.gui.MCVersionRenamerScreen;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.InputConstants;

public class KeyInputUtil {

    private final String keyTranslationKey;
    private boolean wasPressed = false;
    private boolean wasJustToggled = false;

    public KeyInputUtil(String keyTranslationKey) {
        this.keyTranslationKey = keyTranslationKey;
    }

    public void handleKeyPress(Runnable toggleAction) {
        InputConstants.Key key = InputConstants.getKey(keyTranslationKey);
        int keyCode = key.getValue();
        boolean isPressed = InputConstants.isKeyDown(Minecraft.getInstance().getWindow(), keyCode);

        wasJustToggled = false;

        if (isPressed && !wasPressed) {
            wasPressed = true;
            if (!(Minecraft.getInstance().gui.screen() instanceof MCVersionRenamerScreen)) {
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