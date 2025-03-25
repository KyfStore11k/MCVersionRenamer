package com.kyfstore.mcversionrenamer.mixin;

import com.kyfstore.mcversionrenamer.data.MCVersionPublicData;
import de.keksuccino.fancymenu.FancyMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FancyMenu.class)
abstract class FancyMenuMixin {

    @Inject(method = "getMinecraftVersion()Ljava/lang/String;", at = @At("RETURN"), cancellable = true, remap = false)
    private static void setCustomVersionTextReturnValue(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue(MCVersionPublicData.versionText);
    }
}
