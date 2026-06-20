package com.kyfstore.mcversionrenamer.customlibs.yacl;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screens.Screen;

public class MCVersionRenamerIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> (Screen) ModPlatform.INSTANCE.getCreateConfigScreen().invoke(parent);
    }
}
