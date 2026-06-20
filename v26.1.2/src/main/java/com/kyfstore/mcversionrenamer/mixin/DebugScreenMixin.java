package com.kyfstore.mcversionrenamer.mixin;

import com.kyfstore.mcversionrenamer.util.MCVersionRenamerPublicData;
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//import net.minecraft.client.gui.hud.debug.DebugHudEntry;
//import net.minecraft.client.gui.hud.debug.DebugHudLines;
//import net.minecraft.client.gui.hud.debug.DebugHudEntries;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.client.gui.components.debug.DebugScreenEntries;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;

import java.util.Map;

@Mixin(DebugScreenEntries.class)
public class DebugScreenMixin {
    @Mutable
    @Shadow @Final
    private static Map<Identifier, DebugScreenEntry> ENTRIES_BY_ID;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void replaceGameVersionEntry(CallbackInfo ci) {
        DebugScreenEntry customEntry = new DebugScreenEntry() {
            @Override
            public void display(DebugScreenDisplayer lines, @Nullable Level level, @Nullable LevelChunk levelChunk, @Nullable LevelChunk levelChunk2) {
                lines.addPriorityLine(MCVersionRenamerPublicData.f3Text);
            }

            @Override
            public boolean isAllowed(boolean bl) {
                return true;
            }
            //@Override
            //public void render(DebugHudLines lines, @Nullable Level world,
            //                   @Nullable LevelChunk clientChunk, @Nullable LevelChunk chunk) {
            //    lines.addPriorityLine(MCVersionRenamerPublicData.f3Text);
            //}
            //@Override
            //public boolean canShow(boolean reducedDebugInfo) {
            //    return true;
            //}
        };
        ENTRIES_BY_ID.put(DebugScreenEntries.GAME_VERSION, customEntry);
    }
}