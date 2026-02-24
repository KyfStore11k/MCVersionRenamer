package com.kyfstore.mcversionrenamer.mixin;

import com.kyfstore.mcversionrenamer.util.MCVersionRenamerPublicData;
import net.minecraft.client.gui.hud.debug.DebugHudEntry;
import net.minecraft.client.gui.hud.debug.DebugHudLines;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import net.minecraft.client.gui.hud.debug.DebugHudEntries;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(DebugHudEntries.class)
public class DebugScreenMixin {
    @Mutable
    @Shadow @Final
    private static Map<Identifier, DebugHudEntry> ENTRIES;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void replaceGameVersionEntry(CallbackInfo ci) {
        DebugHudEntry customEntry = new DebugHudEntry() {
            @Override
            public void render(DebugHudLines lines, @Nullable World world,
                               @Nullable WorldChunk clientChunk, @Nullable WorldChunk chunk) {
                lines.addPriorityLine(MCVersionRenamerPublicData.f3Text);
            }

            @Override
            public boolean canShow(boolean reducedDebugInfo) {
                return true;
            }
        };
        ENTRIES.put(DebugHudEntries.GAME_VERSION, customEntry);
    }
}

