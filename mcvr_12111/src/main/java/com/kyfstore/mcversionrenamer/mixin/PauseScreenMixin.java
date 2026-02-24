package com.kyfstore.mcversionrenamer.mixin;

import com.kyfstore.mcversionrenamer.gui.MCVersionRenamerGui;
import com.kyfstore.mcversionrenamer.gui.MCVersionRenamerScreen;
import com.kyfstore.mcversionrenamer.util.MCVersionRenamerPublicData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
@Environment(EnvType.CLIENT)
public class PauseScreenMixin extends Screen {
    protected PauseScreenMixin() {
        super(Text.literal("MCVersionRenamer"));
    }

    @Inject(method = "initWidgets", at = @At("RETURN"))
    private void addCustomButton(CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();

        ButtonWidget backButton = ((GameMenuScreen)(Object)this).children().stream()
                .filter(b -> b instanceof ButtonWidget)
                .map(b -> (ButtonWidget) b)
                .filter(b -> Text.translatable("menu.returnToGame").equals(b.getMessage()))
                .findFirst()
                .orElse(null);

        if (backButton != null) {
            int buttonWidth = 50;
            int buttonHeight = 20;

            int x = backButton.getX() + backButton.getWidth() + 5;
            int y = backButton.getY();

            ButtonWidget button = ButtonWidget.builder(Text.literal("MCVR"), btn -> {
                client.setScreen(new MCVersionRenamerScreen(new MCVersionRenamerGui(client.currentScreen)));
            }).dimensions(x, y, buttonWidth, buttonHeight).build();

            button.visible = MCVersionRenamerPublicData.customButtonIsVisible;
            this.addDrawableChild(button);
        }
    }
}
