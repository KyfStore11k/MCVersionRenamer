package com.kyfstore.mcversionrenamer.mixin;

import com.kyfstore.mcversionrenamer.data.MCVersionPublicData;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(DebugHud.class)
public class DebugScreenMixin {

    @Inject(method = "render", at = @At("HEAD"))
    private void modifyDebugText(DrawContext context, CallbackInfo ci) {
    }

    @Inject(method = "getLeftText", at = @At("RETURN"), cancellable = true)
    private void modifyLeftText(CallbackInfoReturnable<List<String>> info) {
        List<String> originalText = info.getReturnValue();

        List<String> newText = new ArrayList<>();

        newText.add(MCVersionPublicData.versionText);

        if (originalText.size() > 1) {
            newText.addAll(originalText.subList(1, originalText.size()));
        }

        info.setReturnValue(newText);
    }
}
