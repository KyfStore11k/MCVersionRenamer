package com.kyfstore.mcversionrenamer.mixin;

import com.kyfstore.mcversionrenamer.gui.MCVersionRenamerGui;
import com.kyfstore.mcversionrenamer.gui.MCVersionRenamerScreen;
import com.kyfstore.mcversionrenamer.util.MCVersionRenamerPublicData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PauseScreen.class)
@Environment(EnvType.CLIENT)
public class PauseScreenMixin extends Screen {
    protected PauseScreenMixin() {
        super(Component.literal("MCVersionRenamer"));
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void addCustomButton(CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();

        Button backButton = ((PauseScreen)(Object)this).children().stream()
                .filter(b -> b instanceof Button)
                .map(b -> (Button) b)
                .filter(b -> Component.translatable("menu.returnToGame").equals(b.getMessage()))
                .findFirst()
                .orElse(null);

        if (backButton != null) {
            int buttonWidth = 50;
            int buttonHeight = 20;

            int x = backButton.getX() + backButton.getWidth() + 5;
            int y = backButton.getY();

            Button button = Button.builder(Component.literal("MCVR"), btn -> {
                client.setScreen(new MCVersionRenamerScreen(new MCVersionRenamerGui(client.screen)));
            }).bounds(x, y, buttonWidth, buttonHeight).build();

            button.visible = MCVersionRenamerPublicData.customButtonIsVisible;
            this.addRenderableWidget(button);
        }
    }
}
