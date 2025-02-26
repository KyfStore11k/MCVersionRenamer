package com.kyfstore.mcversionrenamer;

import com.kyfstore.mcversionrenamer.customlibs.betterf3.VersionTextChangerModule;
import com.kyfstore.mcversionrenamer.customlibs.modmenu.for_owolib.MCVersionRenamerConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MCVersionRenamer implements ModInitializer {
    public static final String MOD_ID = "mcversionrenamer";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final MCVersionRenamerConfig CONFIG = MCVersionRenamerConfig.createAndLoad();

    @Override
    public void onInitialize() {
        if (FabricLoader.getInstance().isModLoaded("betterf3")) {
            LOGGER.info("BetterF3 loaded! Initiating BetterF3 hooks for MCVersionRenamer...");

            new VersionTextChangerModule().init();
        } else {
            LOGGER.info("Can't find mod; BetterF3; skipped BetterF3 related hooks...");
        }
    }
}
