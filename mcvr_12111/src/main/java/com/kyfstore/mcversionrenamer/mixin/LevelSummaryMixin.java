package com.kyfstore.mcversionrenamer.mixin;

import com.kyfstore.mcversionrenamer.util.MCVersionRenamerPublicData;
import net.minecraft.world.level.storage.LevelSummary;
import net.minecraft.world.level.storage.SaveVersionInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LevelSummary.class)
public class LevelSummaryMixin {
    @Redirect(method = "getVersion", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/SaveVersionInfo;getVersionName()Ljava/lang/String;"))
    private String redirectGetVersionName(SaveVersionInfo instance) {
        return MCVersionRenamerPublicData.versionText;
    }
}
