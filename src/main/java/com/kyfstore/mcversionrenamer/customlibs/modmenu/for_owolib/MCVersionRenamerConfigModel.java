package com.kyfstore.mcversionrenamer.customlibs.modmenu.for_owolib;

import com.kyfstore.mcversionrenamer.MCVersionRenamer;
import com.kyfstore.mcversionrenamer.data.MCVersionPublicData;
import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.Nest;
import io.wispforest.owo.config.annotation.SectionHeader;

@Modmenu(modId = MCVersionRenamer.MOD_ID)
@Config(name = "mcversionrenamer-config", wrapperName = "MCVersionRenamerConfig")
public class MCVersionRenamerConfigModel {

    @SectionHeader("general-settings")
    @Nest
    public VersionTextSettings versionTextSettings = new VersionTextSettings();

    public static class VersionTextSettings {
        public String versionText = MCVersionPublicData.versionText;
        public String titleText = MCVersionPublicData.titleText;
        public String f3Text = MCVersionPublicData.f3Text;
    }
}
