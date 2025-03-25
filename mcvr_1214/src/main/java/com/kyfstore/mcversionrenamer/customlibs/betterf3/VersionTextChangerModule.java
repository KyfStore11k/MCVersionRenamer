package com.kyfstore.mcversionrenamer.customlibs.betterf3;

import com.kyfstore.mcversionrenamer.data.MCVersionPublicData;
import me.cominixo.betterf3.modules.MinecraftModule;
import me.cominixo.betterf3.utils.DebugLine;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

public class VersionTextChangerModule extends MinecraftModule {
    public VersionTextChangerModule() {
        this.defaultNameColor = TextColor.fromFormatting(Formatting.GOLD);
        this.defaultValueColor = TextColor.fromFormatting(Formatting.DARK_GREEN);

        this.nameColor = defaultNameColor;
        this.valueColor = defaultValueColor;

        lines.removeAll(lines());

        lines.add(new DebugLine("minecraft", "format.betterf3.default_no_colon", false));

        for (final DebugLine line : lines) {
            line.inReducedDebug = true;
        }
    }

    @Override
    public void update(MinecraftClient client) {
        lines.get(0).value(I18n.translate(MCVersionPublicData.f3Text));
    }
}
