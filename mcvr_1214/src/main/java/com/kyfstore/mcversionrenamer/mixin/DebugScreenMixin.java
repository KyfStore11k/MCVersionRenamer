package com.kyfstore.mcversionrenamer.mixin;

import com.kyfstore.mcversionrenamer.util.MCVersionRenamerPublicData;
import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(DebugHud.class)
public class DebugScreenMixin {

    @Inject(method = "getLeftText", at = @At("RETURN"), cancellable = true)
    private void modifyLeftText(CallbackInfoReturnable<List<String>> info) {
        List<String> textList = info.getReturnValue();
        textList.set(0, MCVersionRenamerPublicData.f3Text);
        info.setReturnValue(textList);
    }
}
