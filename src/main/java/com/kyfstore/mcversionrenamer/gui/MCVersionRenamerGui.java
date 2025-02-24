package com.kyfstore.mcversionrenamer.gui;

import com.kyfstore.mcversionrenamer.MCVersionRenamerClient;
import com.kyfstore.mcversionrenamer.data.MCVersionPublicData;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

public class MCVersionRenamerGui extends LightweightGuiDescription {

    public MCVersionRenamerGui() {
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(300, 80);
        root.setInsets(Insets.ROOT_PANEL);

        WTextField textField = new WTextField();
        textField.setMaxLength(50);

        WButton button = new WButton(Text.literal("Set Text"));
        button.setIcon(new ItemIcon(new ItemStack(Items.LIME_WOOL)));
        button.setOnClick(() -> {
            MCVersionRenamerClient.setClientWindowName(textField.getText());
            MCVersionPublicData.versionText = textField.getText();
            MinecraftClient.getInstance().reloadResources();
            MinecraftClient.getInstance().setScreen(new TitleScreen());
        });

        WButton closeButton = new WButton(Text.literal("Close"));
        closeButton.setIcon(new ItemIcon(new ItemStack(Items.RED_WOOL)));
        closeButton.setOnClick(() -> MinecraftClient.getInstance().setScreen(new TitleScreen()));

        WButton defaultButton = new WButton(Text.literal("Default"));
        defaultButton.setIcon(new ItemIcon(new ItemStack(Items.WHITE_WOOL)));
        defaultButton.setOnClick(() -> {
            MCVersionRenamerClient.setClientWindowName("Minecraft* 1.21.4");
            MCVersionPublicData.versionText = "Minecraft* 1.21.4/Fabric (Modded)";
            MinecraftClient.getInstance().reloadResources();
            MinecraftClient.getInstance().setScreen(new TitleScreen());
        });

        root.add(textField, 3, 2, 11, 3);
        root.add(button, 0, 5, 5, 1);
        root.add(closeButton, 12, 5, 5, 1);
        root.add(defaultButton, 6, 5, 5, 1);

        root.validate(this);
    }
}
