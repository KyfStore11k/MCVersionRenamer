package com.kyfstore.mcversionrenamer.customlibs.yacl;

import dev.isxander.yacl3.api.NameableEnum;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

public enum FormattingColorEnum implements NameableEnum {
    RED(Formatting.RED),
    ORANGE(TextColor.fromRgb(0xFFA500)),
    YELLOW(Formatting.YELLOW),
    GREEN(Formatting.GREEN),
    AQUA(Formatting.AQUA),
    BLUE(Formatting.BLUE),
    PURPLE(Formatting.LIGHT_PURPLE),
    WHITE(Formatting.WHITE),
    GRAY(Formatting.GRAY),
    BLACK(Formatting.BLACK);


    private final Object formatting;

    FormattingColorEnum(Formatting formatting) {
        this.formatting = formatting;
    }

    FormattingColorEnum(TextColor formatting) {
        this.formatting = formatting;
    }

    public Object getFormatting() {
        return this.formatting;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("mcversionrenamer.color." + name().toLowerCase());
    }

    public Style getStyle() {
        if (this.formatting instanceof Formatting) {
            return Style.EMPTY.withFormatting((Formatting) this.formatting);
        } else if (this.formatting instanceof TextColor) {
            return Style.EMPTY.withColor((TextColor) this.formatting);
        }
        return Style.EMPTY;
    }
}