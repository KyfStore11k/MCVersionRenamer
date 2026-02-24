package com.kyfstore.mcversionrenamer.mixin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kyfstore.mcversionrenamer.MCVersionRenamer;
import com.kyfstore.mcversionrenamer.customlibs.yacl.MCVersionRenamerConfig;
import com.kyfstore.mcversionrenamer.util.MCVersionRenamerPublicData;
import com.kyfstore.mcversionrenamer.gui.MCVersionRenamerGui;
import com.kyfstore.mcversionrenamer.gui.MCVersionRenamerScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

@Mixin(TitleScreen.class)
@Environment(EnvType.CLIENT)
public abstract class TitleScreenMixin extends Screen {
    @Shadow @Nullable private SplashTextRenderer splashText;
    @Unique
    private ButtonWidget customButton;

    protected TitleScreenMixin() {
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

    @ModifyArg(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"),
            index = 1
    )
    private String modifyText(String originalString) {
        return "";
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void renderCustomVersion(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;

        String versionText = MCVersionRenamerPublicData.versionText;
        Text clickableVersion = Text.literal(versionText)
                .setStyle(MCVersionRenamerConfig.versionTextColor.getStyle().withBold(MCVersionRenamerConfig.versionTextIsBold).withItalic(MCVersionRenamerConfig.versionTextIsItalic).withObfuscated(MCVersionRenamerConfig.versionTextIsObfuscated));

        int x = 2;
        int y = client.getWindow().getScaledHeight() - 10;

        PressableTextWidget versionWidget = new PressableTextWidget(
                2, client.getWindow().getScaledHeight() - 10,
                client.textRenderer.getWidth(MCVersionRenamerPublicData.versionText), 10,
                clickableVersion,
                btn -> {
                    String uri = MCVersionRenamerConfig.versionTextURI;
                    if (!uri.isEmpty() && (uri.startsWith("http://") || uri.startsWith("https://"))) {
                        Util.getOperatingSystem().open(uri);
                    } else {
                        MCVersionRenamer.LOGGER.warn("Invalid URI, ignoring: " + uri);
                    }
                },
                client.textRenderer
        );

        this.addDrawableChild(versionWidget);

        int textWidth = textRenderer.getWidth(versionText);
        int textHeight = 10;

        if (mouseX >= x && mouseX <= x + textWidth && mouseY >= y && mouseY <= y + textHeight) {
            context.fill(x, y + textRenderer.fontHeight + 1, x + textWidth, y + textRenderer.fontHeight + 2, 0xFFAAAAAA);
        }
    }

    @Unique
    private ButtonWidget createButton(int x, int y, int width, int height, String text) {
        return ButtonWidget.builder(
                Text.literal(text),
                btn -> {
                    MinecraftClient client = MinecraftClient.getInstance();
                    client.setScreen(new MCVersionRenamerScreen(new MCVersionRenamerGui(client.currentScreen)));
                }
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

    @Inject(method = "init", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        this.splashText = new SplashTextRenderer(
                Text.literal(MCVersionRenamerConfig.splashText)
                        .setStyle(Style.EMPTY.withColor(0xFFFFFF55)));
    }
}
