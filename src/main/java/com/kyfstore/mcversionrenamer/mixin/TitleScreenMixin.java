package com.kyfstore.mcversionrenamer.mixin;

import com.kyfstore.mcversionrenamer.MCVersionRenamerClient;
import com.kyfstore.mcversionrenamer.event.KeyInputHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
    @Unique
    private boolean wasPressed = false;

    @Inject(method = "init", at = @At("HEAD"))
    private void onInit(CallbackInfo ci) {
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        long windowHandle = client.getWindow().getHandle();
        int keyCode = InputUtil.fromTranslationKey(KeyInputHandler.toggleButtonKey.getBoundKeyTranslationKey()).getCode();
        boolean isPressed = InputUtil.isKeyPressed(windowHandle, keyCode);

        if (isPressed && !wasPressed) {
            wasPressed = true;
            MCVersionRenamerClient.toggleButtonVisibility();
        } else if (!isPressed) {
            wasPressed = false;
        }
    }
}
