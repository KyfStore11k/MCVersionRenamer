package com.kyfstore.mcversionrenamer.data;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.Version;

public class MCVersionPublicData {

    public static String getMinecraftVersion() {
        Version version = FabricLoader.getInstance().getModContainer("minecraft")
                .orElseThrow()
                .getMetadata()
                .getVersion();
        return version.getFriendlyString();
    }

    public static String defaultVersionText = String.format("Minecraft* %s/Fabric (Modded)", getMinecraftVersion());
    public static String defaultTitleText = String.format("Minecraft* %s", getMinecraftVersion());
    public static String defaultF3Text = defaultVersionText;


    public static String versionText = defaultVersionText;
    public static String titleText = defaultTitleText;
    public static String f3Text = defaultF3Text;
    public static boolean customButtonIsVisible = true;

    public static boolean modMenuIsLoaded = false;
    public static boolean fancyMenuIsLoaded = false;
}