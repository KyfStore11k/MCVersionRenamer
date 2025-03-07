package com.kyfstore.mcversionrenamer.libapi.version;

import com.google.gson.JsonParser;
import com.kyfstore.mcversionrenamer.MCVersionRenamer;
import com.kyfstore.mcversionrenamer.MCVersionRenamerClient;
import com.kyfstore.mcversionrenamer.gui.versionModal.VersionCheckerGui;
import com.kyfstore.mcversionrenamer.gui.versionModal.VersionCheckerScreen;
import net.minecraft.client.MinecraftClient;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public class VersionCheckerApi {

    private MCVersionRenamerClient instance;
    private final String REPO_URL = "https://api.github.com/repos/KyfStore11k/MCVersionRenamer/releases/latest";

    private Properties versionProperties = new Properties();

    private Logger logger;

    public void onEnable(MCVersionRenamerClient instance) {
        this.instance = instance;
        logger = LoggerFactory.getLogger(this.instance.getClass());
    }

    public VersionCheckerApi() {
        try (InputStream inputStream = getClass().getResourceAsStream("/version.properties")) {
            if (inputStream != null) {
                versionProperties.load(inputStream);
            }
        } catch (Exception e) {
            logger.error(e.toString());
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

                Boolean result = VersionComparator.compareVersions(VersionComparator.removePrefix(currentVersion, "v"), VersionComparator.removePrefix(latestVersion, "v"));

                if (result) {
                    if (MCVersionRenamer.CONFIG.shouldPopenVersionModal())
                        minecraftClient.setScreenAndRender(new VersionCheckerScreen(new VersionCheckerGui(VersionCheckerGui.VersionPopupModalType.NEW_VERSION, latestVersion)));
                } else if (!result) {
                    if (MCVersionRenamer.CONFIG.shouldPopenVersionModal())
                        minecraftClient.setScreenAndRender(new VersionCheckerScreen(new VersionCheckerGui(VersionCheckerGui.VersionPopupModalType.OLD_VERSION, latestVersion)));
                } else if (result == null) {
                    logger.info("MCVersionRenamer is Up to Date!");
                }
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    private String getVersion() {
        return versionProperties.getProperty("version", "UNKNOWN VERSION");
    }

}
