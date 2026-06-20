package com.kyfstore.mcversionrenamer.mixin;

import com.kyfstore.mcversionrenamer.customlibs.yacl.MCVersionRenamerConfig;
import net.minecraft.client.ClientBrandRetriever;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ClientBrandRetriever.class)
public class ClientBrandRetrieverMixin {
    @Inject(method = "getClientModName", at = @At("RETURN"), cancellable = true, remap = false)
    private static void modifyClientBrand(CallbackInfoReturnable<String> info) {
        info.setReturnValue(MCVersionRenamerConfig.clientBrand);
    }
}