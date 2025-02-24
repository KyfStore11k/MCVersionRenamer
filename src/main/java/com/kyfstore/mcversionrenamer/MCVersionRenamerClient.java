package com.kyfstore.mcversionrenamer;

import com.kyfstore.mcversionrenamer.data.MCVersionPublicData;
import com.kyfstore.mcversionrenamer.event.KeyInputHandler;
import com.kyfstore.mcversionrenamer.mixin.MCVersionRenamerScreenMixin;
import com.kyfstore.mcversionrenamer.rewrites.MCVersionRenamerMinecraftVersionClass;
import com.kyfstore.mcversionrenamer.rewrites.MCVersionRenamerMinecraftGameVersion;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

public class MCVersionRenamerClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        MCVersionRenamerMinecraftGameVersion versionClass = MCVersionRenamerMinecraftVersionClass.create();

        publicVersionClass = versionClass;

        KeyInputHandler.register();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            publicClient = client;
            if (client != null && client.getWindow() != null) {
                client.getWindow().setTitle(versionClass.getName());
            }
        });
    }

    public static MinecraftClient publicClient;
    public static MCVersionRenamerMinecraftGameVersion publicVersionClass;

    public static void setClientWindowName(String newTitle) {
        if (publicVersionClass != null) {
            publicVersionClass.setName(newTitle);
        }
    }

    public static void toggleButtonVisibility() {
        MCVersionPublicData.customButtonIsVisible = !MCVersionPublicData.customButtonIsVisible;
    }
}
