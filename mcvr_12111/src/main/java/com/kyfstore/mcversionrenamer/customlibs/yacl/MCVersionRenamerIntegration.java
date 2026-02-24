package com.kyfstore.mcversionrenamer.customlibs.yacl;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class MCVersionRenamerIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return MCVersionRenamerConfig::createConfigScreen;
    }
}
