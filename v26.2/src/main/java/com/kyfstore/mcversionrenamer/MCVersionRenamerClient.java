package com.kyfstore.mcversionrenamer;

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
//import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.SharedConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class MCVersionRenamerClient implements ClientModInitializer {

    private static final VersionCheckerApi versionChecker = new VersionCheckerApi();
    private static boolean hasCheckedVersion = false;

    private static String versionName = "Minecraft* " + SharedConstants.getCurrentVersion().name();
    public static KeyMapping guiToggleKeyBinding;
    public static KeyInputUtil guiToggleKeyInput;

    @Override
    public void onInitializeClient() {
        versionChecker.onEnable(this);

        guiToggleKeyBinding = KeyMappingHelper.registerKeyMapping(
                new KeyMapping(
                        "key.mcversionrenamer.guiToggle",
                        InputConstants.Type.KEYSYM,
                        GLFW.GLFW_KEY_LEFT_BRACKET,
                        KeyMapping.Category.register(Identifier.fromNamespaceAndPath("minecraft", "category.mcversionrenamer.default.name"))
                )
        );

        ScreenEvents.AFTER_INIT.register((minecraftClient, screen, i, i1) -> {
            setClientWindowName(MCVersionRenamerConfig.titleText);

            guiToggleKeyInput = new KeyInputUtil(guiToggleKeyBinding.saveString());
            //guiToggleKeyInput = new KeyInputUtil(guiToggleKeyBinding.getName());
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            guiToggleKeyInput.handleKeyPress(() -> {
                client.setScreenAndShow(new MCVersionRenamerScreen(new MCVersionRenamerGui(client.gui.screen())));
            });

            if (!hasCheckedVersion && client.gui.screen() instanceof TitleScreen) {
                hasCheckedVersion = true;
                versionChecker.checkVersion(client);
            }

            if (MCVersionRenamerPublicData.versionText != MCVersionRenamerConfig.versionText) {
                MCVersionRenamerPublicData.versionText = MCVersionRenamerConfig.versionText;
            }
            if (MCVersionRenamerPublicData.titleText != MCVersionRenamerConfig.titleText) {
                MCVersionRenamerPublicData.titleText = MCVersionRenamerConfig.titleText;
            }
            if (MCVersionRenamerPublicData.f3Text != MCVersionRenamerConfig.f3Text) {
                MCVersionRenamerPublicData.f3Text = MCVersionRenamerConfig.f3Text;
            }

            if (client != null && client.getWindow() != null) {
                client.getWindow().setTitle(versionName);
            }

            if (MCVersionRenamerConfig.buttonEnabled != MCVersionRenamerPublicData.customButtonIsVisible) {
                setButtonVisibility(MCVersionRenamerConfig.buttonEnabled);
            }
        });

        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
            MCVersionRenamerConfig.HANDLER.save();
            MCVersionRenamer.LOGGER.shutdown();
        });
    }

    public static Minecraft publicClient;

    public static void setClientWindowName(String newTitle) {
        versionName = newTitle;
    }

    public static void setButtonVisibility(boolean type) {
        MCVersionRenamerPublicData.customButtonIsVisible = type;
    }
}
