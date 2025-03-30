package com.kyfstore.mcversionrenamer.version;

import com.google.gson.JsonParser;
import com.kyfstore.mcversionrenamer.MCVersionRenamer;
import com.kyfstore.mcversionrenamer.MCVersionRenamerClient;
import com.kyfstore.mcversionrenamer.async.logger.AsyncLogger;
import com.kyfstore.mcversionrenamer.customlibs.yacl.MCVersionRenamerConfig;
import com.kyfstore.mcversionrenamer.gui.versionModal.VersionCheckerGui;
import com.kyfstore.mcversionrenamer.gui.versionModal.VersionCheckerScreen;
import net.minecraft.client.MinecraftClient;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.InputStream;
import java.util.Properties;

public class VersionCheckerApi {

    private MCVersionRenamerClient instance;
    private final String REPO_URL = "https://api.github.com/repos/KyfStore11k/MCVersionRenamer/releases/latest";

    private Properties versionProperties = new Properties();

    private AsyncLogger logger;

    public void onEnable(MCVersionRenamerClient instance) {
        this.instance = instance;
        logger = MCVersionRenamer.LOGGER;

        getVersionProperties();
    }

    public void getVersionProperties() {
        try (InputStream inputStream = getClass().getResourceAsStream("/mcversionrenamer.version.properties")) {
            if (inputStream != null) {
                logger.info("Found mcversionrenamer.version.properties file. Loading contents...");
                versionProperties.load(inputStream);
                String loadedVersion = getVersion();
                logger.info("Loaded version from properties file: " + loadedVersion);

                if (!loadedVersion.matches("\\d+\\.\\d+\\.\\d+(-[A-Za-z0-9]+)?")) {
                    logger.warn("Unexpected version format detected: " + loadedVersion);
                }
            } else {
                logger.error("mcversionrenamer.version.properties file not found in resources.");
            }
        } catch (Exception e) {
            logger.error("Error loading mcversionrenamer.version.properties: " + e.getMessage(), e);
        }
    }

    public void checkVersion(MinecraftClient minecraftClient) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(REPO_URL).build();
            okhttp3.Response response = client.newCall(request).execute();

            if (response.isSuccessful() && response.body() != null) {
                String json = response.body().string();
                String latestVersion = JsonParser.parseString(json).getAsJsonObject().get("tag_name").getAsString();

                String currentVersion = String.format("v%s", getVersion());

                Boolean result = VersionComparator.compareVersions(
                        VersionComparator.removePrefix(currentVersion, "v"),
                        VersionComparator.removePrefix(latestVersion, "v")
                );

                if (result != null) {
                    if (result) {
                        if (MCVersionRenamerConfig.shouldPopenVersionModal)
                            minecraftClient.setScreenAndRender(new VersionCheckerScreen(
                                    new VersionCheckerGui(VersionCheckerGui.VersionPopupModalType.NEW_VERSION, latestVersion)));
                    } else {
                        if (MCVersionRenamerConfig.shouldPopenVersionModal)
                            minecraftClient.setScreenAndRender(new VersionCheckerScreen(
                                    new VersionCheckerGui(VersionCheckerGui.VersionPopupModalType.OLD_VERSION, latestVersion)));
                    }
                } else {
                    logger.info("MCVersionRenamer is Up to Date! (No version comparison result available)");
                }
            }
        } catch (Exception e) {
            logger.error("Error checking version: " + e.getMessage(), e);
        }
    }

    private String getVersion() {
        return versionProperties.getProperty("version", "1.0.0");
    }
}
