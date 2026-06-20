package com.kyfstore.mcversionrenamer.customlibs.yacl;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

public enum FormattingColorEnum {
    RED(TextColor.fromLegacyFormat(ChatFormatting.RED)),
    ORANGE(TextColor.fromRgb(0xFFA500)),
    YELLOW(TextColor.fromLegacyFormat(ChatFormatting.YELLOW)),
    GREEN(TextColor.fromLegacyFormat(ChatFormatting.GREEN)),
    AQUA(TextColor.fromLegacyFormat(ChatFormatting.AQUA)),
    BLUE(TextColor.fromLegacyFormat(ChatFormatting.BLUE)),
    PURPLE(TextColor.fromLegacyFormat(ChatFormatting.LIGHT_PURPLE)),
    WHITE(TextColor.fromLegacyFormat(ChatFormatting.WHITE)),
    GRAY(TextColor.fromLegacyFormat(ChatFormatting.GRAY)),
    BLACK(TextColor.fromLegacyFormat(ChatFormatting.BLACK));

    private final Object formatting;

    FormattingColorEnum(TextColor formatting) {
        this.formatting = formatting;
    }

    public Object getFormatting() {
        return this.formatting;
    }

    public Component getDisplayName() {
        return Component.translatable("mcversionrenamer.color." + name().toLowerCase());
    }

    public Style getStyle() {
        if (this.formatting instanceof ChatFormatting) {
            return Style.EMPTY.applyFormat((ChatFormatting) this.formatting);
        } else if (this.formatting instanceof TextColor) {
            return Style.EMPTY.withColor((TextColor) this.formatting);
        }
        return Style.EMPTY;
    }
}