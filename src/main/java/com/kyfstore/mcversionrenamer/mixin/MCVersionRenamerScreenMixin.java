package com.kyfstore.mcversionrenamer.mixin;

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

@Mixin(TitleScreen.class)
@Environment(EnvType.CLIENT)
public class MCVersionRenamerScreenMixin extends Screen {
    @Unique
    private ButtonWidget customButton;

    protected MCVersionRenamerScreenMixin() {
        super(Text.of("MCVersionRenamer"));
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void addCustomButton(CallbackInfo ci) {
        Screen screen = (Screen) (Object) this;

        int x = 5;
        int y = 5;
        int width = 150;
        int height = 20;

        customButton = ButtonWidget.builder(
                Text.literal("Change MC Version"),
                btn -> {
                    MinecraftClient.getInstance().setScreen(new MCVersionRenamerScreen(new MCVersionRenamerGui()));
                }
        ).dimensions(x, y, width, height).build();

        customButton.visible = true;
        this.addDrawableChild(customButton);
    }

    @ModifyVariable(method = "render", at = @At(value = "STORE"), ordinal = 0)
    private String render(String value) {
        return MCVersionPublicData.versionText;
    }

    public String getVersionText() {
        return MCVersionPublicData.versionText;
    }

    public void setVersionText(String newText) {
        MCVersionPublicData.versionText = newText;
    }
}