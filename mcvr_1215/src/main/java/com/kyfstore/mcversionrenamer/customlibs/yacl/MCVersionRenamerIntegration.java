package com.kyfstore.mcversionrenamer.customlibs.yacl;

/*? if ModMenu {*/

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class MCVersionRenamerIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return MCVersionRenamerConfig::createConfigScreen;
    }
}

/*?}*/
