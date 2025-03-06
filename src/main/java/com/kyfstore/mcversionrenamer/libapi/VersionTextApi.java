package com.kyfstore.mcversionrenamer.libapi;

import com.kyfstore.mcversionrenamer.data.MCVersionPublicData;

public class VersionTextApi {
    public static void setVersionText(String versionText) {
        MCVersionPublicData.versionText = versionText;
    }

    public static String getVersionText() {
        return MCVersionPublicData.versionText;
    }
}
