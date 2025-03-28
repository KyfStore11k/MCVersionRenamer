package com.kyfstore.mcversionrenamer;

import com.kyfstore.mcversionrenamer.customlibs.yacl.MCVersionRenamerConfig;
import com.kyfstore.mcversionrenamer.data.MCVersionPublicData;
import com.kyfstore.mcversionrenamer.event.KeyInputHandler;
import com.kyfstore.mcversionrenamer.version.VersionCheckerApi;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;

@Environment(EnvType.CLIENT)
public class MCVersionRenamerClient implements ClientModInitializer {

    private static final VersionCheckerApi versionChecker = new VersionCheckerApi();
    private static boolean hasCheckedVersion = false;

    private static String versionName = String.format("Minecraft* %s", SharedConstants.getGameVersion());

    @Override
    public void onInitializeClient() {
        KeyInputHandler.register();
        versionChecker.onEnable(this);

        ScreenEvents.AFTER_INIT.register((minecraftClient, screen, i, i1) -> {
            if (!MCVersionPublicData.fancyMenuIsLoaded) setClientWindowName(MCVersionRenamerConfig.titleText);
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            publicClient = client;
            MCVersionPublicData.versionText = MCVersionRenamerConfig.versionText;
            MCVersionPublicData.titleText = MCVersionRenamerConfig.titleText;
            MCVersionPublicData.f3Text = MCVersionRenamerConfig.f3Text;

            if (client != null && client.getWindow() != null) {
                client.getWindow().setTitle(versionName);
                if (client.currentScreen instanceof TitleScreen && !hasCheckedVersion) {
                    hasCheckedVersion = true;
                    versionChecker.checkVersion(client);
                }
            }
        });

        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
            MCVersionRenamerConfig.HANDLER.save();
            MCVersionRenamer.LOGGER.shutdown();
        });
    }

    public static MinecraftClient publicClient;

    public static void setClientWindowName(String newTitle) {
        versionName = newTitle;
    }

    public static void toggleButtonVisibility() {
        MCVersionPublicData.customButtonIsVisible = !MCVersionPublicData.customButtonIsVisible;
    }
}
