package com.kyfstore.mcversionrenamer.mixin;

import com.kyfstore.mcversionrenamer.util.MCVersionRenamerPublicData;
import net.minecraft.world.level.storage.LevelSummary;
import net.minecraft.world.level.storage.LevelVersion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LevelSummary.class)
public class LevelSummaryMixin {
    //@Redirect(method = "getWorldVersionName", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/LevelVersion;getVersionName()Ljava/lang/String;"))
    //private String redirectGetVersionName(LevelVersion instance) {
    //    return MCVersionRenamerPublicData.versionText;
    //}
    @Redirect(
            method = "getWorldVersionName",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/storage/LevelVersion;minecraftVersionName()Ljava/lang/String;"
            )
    )
    private String redirectGetVersionName(LevelVersion instance) {
        return MCVersionRenamerPublicData.versionText;
    }
}
