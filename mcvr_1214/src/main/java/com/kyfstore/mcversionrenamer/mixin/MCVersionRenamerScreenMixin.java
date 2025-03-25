package com.kyfstore.mcversionrenamer.mixin;

import com.kyfstore.mcversionrenamer.MCVersionRenamer;
import com.kyfstore.mcversionrenamer.data.MCVersionPublicData;
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

@Mixin(TitleScreen.class)
@Environment(EnvType.CLIENT)
public abstract class MCVersionRenamerScreenMixin extends Screen {
    @Unique
    private ButtonWidget customButton;

    protected MCVersionRenamerScreenMixin() {
        super(Text.literal("MCVersionRenamer"));
    }

    @Inject(method = "addNormalWidgets", at = @At("RETURN"))
    public void addCustomButton(int y, int spacingY, CallbackInfoReturnable<Integer> cir) {

        if (!MCVersionRenamer.CONFIG.useLegacyButton()) {
            int buttonWidth = 50;
            int buttonHeight = 20;
            int buttonX = this.width / 2 - 100 + 205;

            customButton = ButtonWidget.builder(
                    Text.literal("MCVR"),
                    btn -> {
                        MinecraftClient.getInstance().setScreen(new MCVersionRenamerScreen(new MCVersionRenamerGui()));
                    }
            ).dimensions(buttonX, y, buttonWidth, buttonHeight).build();

            if (MCVersionPublicData.modMenuIsLoaded) customButton.setPosition(buttonX, y - 72);
            else if (!MCVersionPublicData.modMenuIsLoaded) customButton.setPosition(buttonX, y - 48);

            customButton.visible = MCVersionPublicData.customButtonIsVisible;

            this.addDrawableChild(customButton);
        } else {
            int buttonx = 5;
            int buttony = 5;
            int width = 150;
            int height = 20;

            customButton = ButtonWidget.builder(
                    Text.literal("Change MC Version"),
                    btn -> {
                        MinecraftClient.getInstance().setScreen(new MCVersionRenamerScreen(new MCVersionRenamerGui()));
                    }
            ).dimensions(buttonx, buttony, width, height).build();

            customButton.visible = MCVersionPublicData.customButtonIsVisible;

            this.addDrawableChild(customButton);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        customButton.visible = MCVersionPublicData.customButtonIsVisible;
    }

    @ModifyVariable(method = "render", at = @At(value = "STORE"), ordinal = 0)
    private String render(String value) {
        return MCVersionPublicData.versionText;
    }
}