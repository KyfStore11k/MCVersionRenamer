package com.kyfstore.mcversionrenamer;

import com.kyfstore.mcversionrenamer.customlibs.owolib.MCVersionRenamerConfig;
import com.kyfstore.mcversionrenamer.data.MCVersionPublicData;
import com.kyfstore.mcversionrenamer.libapi.core.plugin.core.main.control.PluginManager;
import com.kyfstore.mcversionrenamer.async.logger.AsyncLogger;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class MCVersionRenamer implements ModInitializer {
    public static final String MOD_ID = "mcversionrenamer";
    public static final AsyncLogger LOGGER = new AsyncLogger(MOD_ID);

    public static final MCVersionRenamerConfig CONFIG = MCVersionRenamerConfig.createAndLoad();

    @Override
    public void onInitialize() {
        setupDefaultInit();
        setupModHooks();
        setupPlugins();
    }

    private void setupDefaultInit() {
    }

    private void setupModHooks() {
        if (FabricLoader.getInstance().isModLoaded("betterf3")) {
            LOGGER.info("BetterF3 loaded! Initiating BetterF3 hooks for MCVersionRenamer...");
        } else {
            LOGGER.info("Can't find mod; BetterF3; skipped BetterF3 related hooks...");
        }
        if (FabricLoader.getInstance().isModLoaded("fancymenu")) {
            MCVersionPublicData.fancyMenuIsLoaded = true;
        } else {
            LOGGER.info("Can't find mod; FancyMenu; skipped FancyMenu related hooks...");
        }
        if (FabricLoader.getInstance().isModLoaded("modmenu")) {
            MCVersionPublicData.modMenuIsLoaded = true;
        } else {
            LOGGER.info("Can't find mod; ModMenu; skipped ModMenu related hooks...");
        }
    }

    private void setupPlugins() {
        PluginManager.loadPlugins();
    }
}
