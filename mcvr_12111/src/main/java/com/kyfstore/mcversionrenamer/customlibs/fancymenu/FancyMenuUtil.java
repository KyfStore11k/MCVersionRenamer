package com.kyfstore.mcversionrenamer.customlibs.fancymenu;

import de.keksuccino.fancymenu.util.ScreenTitleUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class FancyMenuUtil {
    public static void setClientWindowTitleName(MinecraftClient client, Text title) {
        ScreenTitleUtils.setScreenTitle(client.currentScreen, title);
    }
}
