package com.kyfstore.mcversionrenamer;

import com.kyfstore.mcversionrenamer.async.logger.AsyncLogger;
import com.kyfstore.mcversionrenamer.customlibs.yacl.MCVersionRenamerConfig;
import com.kyfstore.mcversionrenamer.data.MCVersionPublicData;
import com.kyfstore.mcversionrenamer.plugin.main.control.PluginManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class MCVersionRenamer implements ModInitializer {
    public static final String MOD_ID = "mcversionrenamer";
    public static final AsyncLogger LOGGER = new AsyncLogger(MOD_ID);

    @Override
    public void onInitialize() {
        setupDefaultInit();
        setupModHooks();
        setupPlugins();
    }

    private void setupDefaultInit() {
        MCVersionRenamerConfig.HANDLER.save();
        MCVersionRenamerConfig.HANDLER.load();
    }

    private void setupModHooks() {
        if (FabricLoader.getInstance().isModLoaded("betterf3")) {
            LOGGER.info("BetterF3 loaded! Initiating BetterF3 hooks for MCVersionRenamer...");
        } else {
            LOGGER.info("BetterF3 not found, skipping BetterF3 related hooks...");
        }
        if (FabricLoader.getInstance().isModLoaded("fancymenu")) {
            MCVersionPublicData.fancyMenuIsLoaded = true;
        } else {
            LOGGER.info("FancyMenu not found, skipping FancyMenu related hooks...");
        }
        if (FabricLoader.getInstance().isModLoaded("modmenu")) {
            MCVersionPublicData.modMenuIsLoaded = true;
        } else {
            LOGGER.info("ModMenu not found, skipping ModMenu related hooks...");
        }
    }

    private void setupPlugins() {
        PluginManager.loadPlugins();
    }
}
