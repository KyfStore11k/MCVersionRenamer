package com.kyfstore.mcversionrenamer;

import com.kyfstore.mcversionrenamer.customlibs.yacl.MCVersionRenamerConfig;
import kotlin.jvm.functions.Function1;
import net.fabricmc.api.ModInitializer;
import com.kyfstore.mcversionrenamer.customlibs.yacl.ModPlatform;
import net.minecraft.client.gui.screens.Screen;

public class MCVR262 implements ModInitializer {
    @Override
    public void onInitialize() {
        ModPlatform.INSTANCE.setLoadConfig(() -> {
            MCVersionRenamerConfig.HANDLER.load();
            return null;
        });

        ModPlatform.INSTANCE.setSaveConfig(() -> {
            MCVersionRenamerConfig.HANDLER.save();
            return null;
        });
        ModPlatform.INSTANCE.setPluginsEnabled(() -> MCVersionRenamerConfig.pluginsEnabled);

        ModPlatform.INSTANCE.setCreateConfigScreen((Function1<Object, ?>) (parent) -> {
            Screen parentScreen = (Screen) parent;
            return MCVersionRenamerConfig.createConfigScreen(parentScreen);
        });

        MCVersionRenamer.initCommon();
    }
}
