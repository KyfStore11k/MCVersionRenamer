package com.kyfstore.mcversionrenamer.mixin;

import com.kyfstore.mcversionrenamer.data.MCVersionPublicData;
import me.cominixo.betterf3.modules.MinecraftModule;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftModule.class)
public class MinecraftModuleMixin {

    @Inject(method = "update", at = @At("HEAD"), cancellable = true, remap = false)
    public void modifyUpdate(MinecraftClient client, CallbackInfo ci) {
        ((MinecraftModule) (Object) this).lines().get(0).value(MCVersionPublicData.versionText);

        ci.cancel();
    }
}