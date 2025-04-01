package com.kyfstore.mcversionrenamer;

import com.kyfstore.mcversionrenamer.customlibs.yacl.MCVersionRenamerConfig;
import com.kyfstore.mcversionrenamer.data.MCVersionRenamerPublicData;
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

    // Use string concatenation rather than String.format() for efficiency
    private static String versionName = "Minecraft* " + SharedConstants.getGameVersion();

    @Override
    public void onInitializeClient() {
        versionChecker.onEnable(this);

        ScreenEvents.AFTER_INIT.register((minecraftClient, screen, i, i1) -> {
            if (!MCVersionRenamerPublicData.fancyMenuIsLoaded) setClientWindowName(MCVersionRenamerConfig.titleText);
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Move these to the start of the tick to avoid redundant assignments
            if (!hasCheckedVersion && client.currentScreen instanceof TitleScreen) {
                hasCheckedVersion = true;
                versionChecker.checkVersion(client); // Check version only once
            }

            // Update MCVersionPublicData only if needed
            if (MCVersionRenamerPublicData.versionText != MCVersionRenamerConfig.versionText) {
                MCVersionRenamerPublicData.versionText = MCVersionRenamerConfig.versionText;
            }
            if (MCVersionRenamerPublicData.titleText != MCVersionRenamerConfig.titleText) {
                MCVersionRenamerPublicData.titleText = MCVersionRenamerConfig.titleText;
            }
            if (MCVersionRenamerPublicData.f3Text != MCVersionRenamerConfig.f3Text) {
                MCVersionRenamerPublicData.f3Text = MCVersionRenamerConfig.f3Text;
            }

            // Set the client window title once, instead of every tick
            if (client != null && client.getWindow() != null) {
                client.getWindow().setTitle(versionName);
            }

            // Toggle button visibility only if it changes
            if (MCVersionRenamerConfig.buttonEnabled != MCVersionRenamerPublicData.customButtonIsVisible) {
                setButtonVisibility(MCVersionRenamerConfig.buttonEnabled);
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

    public static void setButtonVisibility(boolean type) {
        MCVersionRenamerPublicData.customButtonIsVisible = type;
    }
}
