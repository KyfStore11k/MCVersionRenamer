package com.kyfstore.mcversionrenamer.mixin;

import com.kyfstore.mcversionrenamer.util.MCVersionRenamerPublicData;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(DebugScreenOverlay.class)

public class DebugScreenMixin {
    @Inject(at = @At("RETURN"), method = "getGameInformation", cancellable = true)
    private void getLeftText(CallbackInfoReturnable<List<String>> info) {
        List<String> textList = info.getReturnValue();

        if (!textList.isEmpty()) {
            textList.set(0, MCVersionRenamerPublicData.f3Text);
        }

        info.setReturnValue(textList);
    }
}
