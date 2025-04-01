package com.kyfstore.mcversionrenamer.mixin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kyfstore.mcversionrenamer.MCVersionRenamer;
import com.kyfstore.mcversionrenamer.customlibs.yacl.MCVersionRenamerConfig;
import com.kyfstore.mcversionrenamer.data.MCVersionRenamerPublicData;
import com.kyfstore.mcversionrenamer.gui.MCVersionRenamerGui;
import com.kyfstore.mcversionrenamer.gui.MCVersionRenamerScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

@Mixin(TitleScreen.class)
@Environment(EnvType.CLIENT)
public abstract class MCVersionRenamerScreenMixin extends Screen {
    // @Shadow @Final private static Logger LOGGER;
    @Unique
    private ButtonWidget customButton;

    protected MCVersionRenamerScreenMixin() {
        super(Text.literal("MCVersionRenamer"));
    }

    @Inject(method = "addNormalWidgets", at = @At("RETURN"))
    public void addCustomButton(int y, int spacingY, CallbackInfoReturnable<Integer> cir) {
        int buttonX, buttonY, buttonWidth = 50, buttonHeight = 20;

        if (!MCVersionRenamerConfig.useLegacyButton) {
            buttonX = this.width / 2 - 100 + 205;
            buttonY = y - (MCVersionRenamerPublicData.modMenuIsLoaded && "classic".equals(getModsButtonStyle()) ? 72 : 48);
            customButton = createButton(buttonX, buttonY, buttonWidth, buttonHeight, "MCVR");
        } else {
            customButton = createButton(5, 5, 150, 20, "Change MC Version");
        }

        customButton.visible = MCVersionRenamerPublicData.customButtonIsVisible;
        this.addDrawableChild(customButton);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (customButton != null) {
            customButton.visible = MCVersionRenamerPublicData.customButtonIsVisible;
        }
    }

    @ModifyVariable(method = "render", at = @At(value = "STORE"), ordinal = 0)
    private String render(String value) {
        return MCVersionRenamerPublicData.versionText;
    }

    @Unique
    private ButtonWidget createButton(int x, int y, int width, int height, String text) {
        return ButtonWidget.builder(
                Text.literal(text),
                btn -> MinecraftClient.getInstance().setScreen(new MCVersionRenamerScreen(new MCVersionRenamerGui()))
        ).dimensions(x, y, width, height).build();
    }

    @Unique
    private static String getModsButtonStyle() {
        Path configPath = MinecraftClient.getInstance().runDirectory.toPath().resolve("config/modmenu.json");

        if (!Files.exists(configPath)) return "classic";

        try (Reader reader = Files.newBufferedReader(configPath, StandardCharsets.UTF_8)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            return json.has("mods_button_style") ? json.get("mods_button_style").getAsString() : "classic";
        } catch (IOException e) {
            MCVersionRenamer.LOGGER.error("Error reading ModMenu config: ", e);
            return "classic";
        }
    }
}
