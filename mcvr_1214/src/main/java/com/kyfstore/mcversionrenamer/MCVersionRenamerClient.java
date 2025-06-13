package com.kyfstore.mcversionrenamer;

import com.kyfstore.mcversionrenamer.customlibs.fancymenu.FancyMenuUtil;
import com.kyfstore.mcversionrenamer.customlibs.yacl.MCVersionRenamerConfig;
import com.kyfstore.mcversionrenamer.gui.MCVersionRenamerGui;
import com.kyfstore.mcversionrenamer.gui.MCVersionRenamerScreen;
import com.kyfstore.mcversionrenamer.util.KeyInputUtil;
import com.kyfstore.mcversionrenamer.util.MCVersionRenamerPublicData;
import com.kyfstore.mcversionrenamer.version.VersionCheckerApi;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class MCVersionRenamerClient implements ClientModInitializer {

    private static final VersionCheckerApi versionChecker = new VersionCheckerApi();
    private static boolean hasCheckedVersion = false;

    private static String versionName = "Minecraft* " + SharedConstants.getGameVersion();

    public static KeyBinding guiToggleKeyBinding;
    public static KeyInputUtil guiToggleKeyInput;

    @Override
    public void onInitializeClient() {
        versionChecker.onEnable(this);

        guiToggleKeyBinding = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.mcversionrenamer.guiToggle",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_LEFT_BRACKET,
                        "category.mcversionrenamer.default.name"
                )
        );

        ScreenEvents.AFTER_INIT.register((minecraftClient, screen, i, i1) -> {
            if (MCVersionRenamerPublicData.fancyMenuIsLoaded) FancyMenuUtil.setClientWindowTitleName(minecraftClient, Text.literal(MCVersionRenamerPublicData.titleText));
            setClientWindowName(MCVersionRenamerConfig.titleText);

            guiToggleKeyInput = new KeyInputUtil(guiToggleKeyBinding.getBoundKeyTranslationKey());
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            guiToggleKeyInput.handleKeyPress(() -> {
                client.setScreen(new MCVersionRenamerScreen(new MCVersionRenamerGui(client.currentScreen)));
            });

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
