package com.kyfstore.mcversionrenamer.mixin

import com.kyfstore.mcversionrenamer.MCVersionRenamerClient
import com.kyfstore.mcversionrenamer.event.KeyInputHandler
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.TitleScreen
import net.minecraft.client.util.InputUtil
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(TitleScreen::class)
class TitleScreenMixin {
    private var wasPressed: Boolean = false

    @Inject(method = ["init"], at = [At("HEAD")])
    private fun onInit(ci: CallbackInfo) {
    }

    @Inject(method = ["tick"], at = [At("HEAD")])
    private fun onTick(ci: CallbackInfo) {
        val client = MinecraftClient.getInstance()
        val windowHandle: Long = client.window.handle
        val keyCode: Int = InputUtil.fromTranslationKey(KeyInputHandler.toggleButtonKey.boundKeyTranslationKey).code
        val isPressed: Boolean = InputUtil.isKeyPressed(windowHandle, keyCode)

        if (isPressed && !wasPressed) {
            wasPressed = true
            MCVersionRenamerClient.toggleButtonVisibility()
        } else if (!isPressed) {
            wasPressed = false
        }
    }
}
