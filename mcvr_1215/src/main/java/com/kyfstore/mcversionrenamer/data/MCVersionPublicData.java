package com.kyfstore.mcversionrenamer.data;

import net.minecraft.SharedConstants;

public class MCVersionPublicData {

    public static String defaultVersionText = String.format("Minecraft* %s/Fabric (Modded)", SharedConstants.VERSION_NAME);
    public static String defaultTitleText = String.format("Minecraft* %s", SharedConstants.VERSION_NAME);
    public static String defaultF3Text = defaultVersionText;


    public static String versionText = defaultVersionText;
    public static String titleText = defaultTitleText;
    public static String f3Text = defaultF3Text;
    public static boolean customButtonIsVisible = true;

    public static boolean modMenuIsLoaded = false;
    public static boolean fancyMenuIsLoaded = false;
}