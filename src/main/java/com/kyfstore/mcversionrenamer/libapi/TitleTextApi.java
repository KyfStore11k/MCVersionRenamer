package com.kyfstore.mcversionrenamer.libapi;

import net.minecraft.client.MinecraftClient;

public class TitleTextApi {
    public static void setTitleText(MinecraftClient client, String titleText) {
        client.getWindow().setTitle(titleText);
    }
}
