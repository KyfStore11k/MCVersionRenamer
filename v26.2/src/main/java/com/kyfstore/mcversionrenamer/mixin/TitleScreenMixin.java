package com.kyfstore.mcversionrenamer.mixin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kyfstore.mcversionrenamer.MCVersionRenamer;
import com.kyfstore.mcversionrenamer.customlibs.yacl.MCVersionRenamerConfig;
import com.kyfstore.mcversionrenamer.gui.MCVersionRenamerScreen;
import com.kyfstore.mcversionrenamer.util.MCVersionRenamerPublicData;
import com.kyfstore.mcversionrenamer.gui.MCVersionRenamerGui;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.client.gui.screens.Screen;
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
    @Shadow
    @Nullable private SplashRenderer splash;
    @Unique
    private Button customButton;

    protected TitleScreenMixin() {
        super(Component.literal("MCVersionRenamer"));
    }

    @Inject(method = "createNormalMenuOptions", at = @At("RETURN"))
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
        this.addRenderableWidget(customButton);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (customButton != null) {
            customButton.visible = MCVersionRenamerPublicData.customButtonIsVisible;
        }
    }

    //@ModifyArg(
    //        method = "render",
    //        at = @At(
    //                value = "INVOKE",
    //                //target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)V"),
    //                target = ""
    //                //index = 1
    //)
    @ModifyArg(
            method = "extractRenderState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;text(Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)V"
            )
    )
    private String modifyText(String originalString) {
        return "";
    }

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void renderCustomVersion(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        Font textRenderer = client.font;

        // --- FIXED FOR MOJANG MAPPINGS BYPASSING ENUM TYPE ISSUES ---
        TextColor configColor = TextColor.fromRgb(ChatFormatting.WHITE.ordinal());
        if (MCVersionRenamerConfig.versionTextColor.getFormatting() instanceof TextColor) {
            configColor = (TextColor) MCVersionRenamerConfig.versionTextColor.getFormatting();
        }

        Style mojangStyle = Style.EMPTY
                .withColor(configColor)
                .withBold(MCVersionRenamerConfig.versionTextIsBold)
                .withItalic(MCVersionRenamerConfig.versionTextIsItalic)
                .withObfuscated(MCVersionRenamerConfig.versionTextIsObfuscated);
        // ------------------------------------------------------------

        String versionText = MCVersionRenamerPublicData.versionText;
        Component clickableVersion = Component.literal(versionText).setStyle(mojangStyle);

        int x = 2;
        int y = client.getWindow().getGuiScaledHeight() - 10;

        PlainTextButton versionWidget = new PlainTextButton(
                2, client.getWindow().getGuiScaledHeight() - 10,
                client.font.width(MCVersionRenamerPublicData.versionText), 10,
                clickableVersion,
                btn -> {
                    String uri = MCVersionRenamerConfig.versionTextURI;
                    if (!uri.isEmpty() && (uri.startsWith("http://") || uri.startsWith("https://"))) {
                        Util.getPlatform().openUri(uri);
                    } else {
                        MCVersionRenamer.LOGGER.warn("Invalid URI, ignoring: " + uri);
                    }
                },
                client.font
        );

        this.addRenderableWidget(versionWidget);

        int textWidth = textRenderer.width(versionText);
        int textHeight = 10;

        if (mouseX >= x && mouseX <= x + textWidth && mouseY >= y && mouseY <= y + textHeight) {
            context.fill(x, y + textRenderer.lineHeight + 1, x + textWidth, y + textRenderer.lineHeight + 2, 0xFFAAAAAA);
        }
    }

    @Unique
    private Button createButton(int x, int y, int width, int height, String text) {
        return Button.builder(
                Component.literal(text),
                btn -> {
                    Minecraft client = Minecraft.getInstance();
                    client.setScreenAndShow(new MCVersionRenamerScreen(new MCVersionRenamerGui(client.gui.screen())));
                }
        ).bounds(x, y, width, height).build();
    }

    @Unique
    private static String getModsButtonStyle() {
        Path configPath = Minecraft.getInstance().gameDirectory.toPath().resolve("config/modmenu.json");

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
        this.splash = new SplashRenderer(
                Component.literal(MCVersionRenamerConfig.splashText)
                        .setStyle(Style.EMPTY.withColor(0xFFFFFF55)));
    }
}