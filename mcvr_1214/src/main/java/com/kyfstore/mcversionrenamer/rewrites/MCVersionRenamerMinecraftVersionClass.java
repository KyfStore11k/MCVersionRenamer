package com.kyfstore.mcversionrenamer.rewrites;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

import net.minecraft.SaveVersion;
import net.minecraft.SharedConstants;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.JsonHelper;
import org.slf4j.Logger;

public class MCVersionRenamerMinecraftVersionClass implements MCVersionRenamerMinecraftGameVersion {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final MCVersionRenamerMinecraftGameVersion CURRENT = new MCVersionRenamerMinecraftVersionClass();
    public final String id;
    public String name;
    private final boolean stable;
    private final SaveVersion saveVersion;
    private final int protocolVersion;
    private final int resourcePackVersion;
    private final int dataPackVersion;
    private final Date buildTime;
    public MCVersionRenamerMinecraftVersionClass() {
        this.id = UUID.randomUUID().toString().replaceAll("-", "");
        this.name = "1.21.4";
        this.stable = true;
        this.saveVersion = new SaveVersion(3700, "main");
        this.protocolVersion = SharedConstants.getProtocolVersion();
        this.resourcePackVersion = 22;
        this.dataPackVersion = 26;
        this.buildTime = new Date();
    }
    public MCVersionRenamerMinecraftVersionClass(JsonObject var1) {
        this.id = JsonHelper.getString(var1, "id");
        this.name = JsonHelper.getString(var1, "name");
        this.stable = JsonHelper.getBoolean(var1, "stable");
        this.saveVersion = new SaveVersion(JsonHelper.getInt(var1, "world_version"), JsonHelper.getString(var1, "series_id", SaveVersion.MAIN_SERIES));
        this.protocolVersion = JsonHelper.getInt(var1, "protocol_version");
        JsonObject var2 = JsonHelper.getObject(var1, "pack_version");
        this.resourcePackVersion = JsonHelper.getInt(var2, "resource");
        this.dataPackVersion = JsonHelper.getInt(var2, "data");
        this.buildTime = Date.from(ZonedDateTime.parse(JsonHelper.getString(var1, "build_time")).toInstant());
    }

    public static MCVersionRenamerMinecraftGameVersion create() {
        try {
            InputStream var0 = MCVersionRenamerMinecraftVersionClass.class.getResourceAsStream("/version.json");
            MCVersionRenamerMinecraftGameVersion var9;
            label63: {
                MCVersionRenamerMinecraftVersionClass var2;
                try {
                    if (var0 == null) {
                        LOGGER.warn("Missing version information!");
                        var9 = CURRENT;
                        break label63;
                    }
                    InputStreamReader var1 = new InputStreamReader(var0);
                    try {
                        var2 = new MCVersionRenamerMinecraftVersionClass(JsonHelper.deserialize(var1));
                    } catch (Throwable var6) {
                        try {
                            var1.close();
                        } catch (Throwable var5) {
                            var6.addSuppressed(var5);
                        }
                        throw var6;
                    }
                    var1.close();
                } catch (Throwable var7) {
                    if (var0 != null) {
                        try {
                            var0.close();
                        } catch (Throwable var4) {
                            var7.addSuppressed(var4);
                        }
                    }
                    throw var7;
                }
                if (var0 != null) {
                    var0.close();
                }
                return var2;
            }
            if (var0 != null) {
                var0.close();
            }
            return var9;
        } catch (JsonParseException | IOException var8) {
            throw new IllegalStateException("Game version information is corrupt", var8);
        }
    }
    @Override
    public String getId() {
        return this.id;
    }
    @Override
    public String getName() {
        return this.name;
    }
    @Override
    public SaveVersion getSaveVersion() {
        return this.saveVersion;
    }
    @Override
    public int getProtocolVersion() {
        return this.protocolVersion;
    }
    @Override
    public int getResourceVersion(ResourceType type) {
        return type == ResourceType.SERVER_DATA ? this.dataPackVersion : this.resourcePackVersion;
    }
    @Override
    public Date getBuildTime() {
        return this.buildTime;
    }
    @Override
    public boolean isStable() {
        return this.stable;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
