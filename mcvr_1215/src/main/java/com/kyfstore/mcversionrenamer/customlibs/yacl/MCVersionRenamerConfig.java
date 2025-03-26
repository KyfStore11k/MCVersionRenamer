package com.kyfstore.mcversionrenamer.customlibs.yacl;

import com.kyfstore.mcversionrenamer.MCVersionRenamer;
import com.kyfstore.mcversionrenamer.data.MCVersionPublicData;
import com.nimbusds.jose.shaded.gson.GsonBuilder;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class MCVersionRenamerConfig implements ModMenuApi {
    public static ConfigClassHandler<MCVersionRenamerConfig> HANDLER = ConfigClassHandler.createBuilder(MCVersionRenamerConfig.class)
            .id(Identifier.of(MCVersionRenamer.MOD_ID, "mcversionrenamer_config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("mcversionrenamer.json5"))
                    .setJson5(true)
                    .build())
            .build();

    @SerialEntry public static String versionText = MCVersionPublicData.versionText;
    @SerialEntry public static String f3Text = MCVersionPublicData.f3Text;
    @SerialEntry public static String titleText = MCVersionPublicData.titleText;
    @SerialEntry public static Boolean shouldPopenVersionModal = true;
    @SerialEntry public static Boolean useLegacyButton = false;

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parentScreen -> YetAnotherConfigLib.createBuilder()
                .title(Text.literal("MCVersionRenamer Config Screen"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("General Settings"))
                        .tooltip(Text.literal("The basic general settings for MCVersionRenamer"))
                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Version Text Settings"))
                                .description(OptionDescription.of(Text.literal("The settings that control the different version texts; Title Text, F3 Text, Version Text, etc.,;")))
                                .option(Option.<String>createBuilder()
                                        .name(Text.literal("Version Text"))
                                        .description(OptionDescription.of(Text.literal("The Version Text Located at the Bottom Right of the Title Screen (which also appears elsewhere)")))
                                        .binding(MCVersionPublicData.defaultVersionText, () -> this.versionText, newVal -> this.versionText = newVal)
                                        .controller(StringControllerBuilder::create)
                                        .build())
                                .option(Option.<String>createBuilder()
                                        .name(Text.literal("F3 Text"))
                                        .description(OptionDescription.of(Text.literal("The F3 text found in the default F3 menu (and BetterF3)")))
                                        .binding(MCVersionPublicData.defaultF3Text, () -> this.f3Text, newVal -> this.f3Text = newVal)
                                        .controller(StringControllerBuilder::create)
                                        .build())
                                .option(Option.<String>createBuilder()
                                        .name(Text.literal("Title Text"))
                                        .description(OptionDescription.of(Text.literal("The title text of the current open Minecraft window")))
                                        .binding(MCVersionPublicData.defaultTitleText, () -> this.titleText, newVal -> this.titleText = newVal)
                                        .controller(StringControllerBuilder::create)
                                        .build())
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Other Settings"))
                                .description(OptionDescription.of(Text.literal("Other settings that don't fit in any category")))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Should Open Version Checking Modal?"))
                                        .description(OptionDescription.of(Text.literal("If you should have the startup MCVersionRenamer version modal open?")))
                                        .binding(true, () -> this.shouldPopenVersionModal, newVal -> this.shouldPopenVersionModal = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Use Legacy Button?"))
                                        .description(OptionDescription.of(Text.literal("If you should use the old legacy button location/size?")))
                                        .binding(false, () -> this.useLegacyButton, newVal -> this.useLegacyButton = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())
                        .build())
                .build()
                .generateScreen(parentScreen);
    }
}
