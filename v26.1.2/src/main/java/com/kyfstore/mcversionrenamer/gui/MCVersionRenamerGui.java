package com.kyfstore.mcversionrenamer.gui;

import com.kyfstore.mcversionrenamer.MCVersionRenamerClient;
import com.kyfstore.mcversionrenamer.customlibs.yacl.MCVersionRenamerConfig;
import com.kyfstore.mcversionrenamer.util.MCVersionRenamerPublicData;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Field;
import java.util.IdentityHashMap;
import java.util.Map;

public class MCVersionRenamerGui extends LightweightGuiDescription {

    public MCVersionRenamerGui(Screen lastScreen) {
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(300, 80);
        root.setInsets(Insets.ROOT_PANEL);

        WTextField textField = new WTextField();
        textField.setMaxLength(50);

        WButton button = new WButton(Component.literal("Set Text"));
        try {
            button.setIcon(new ItemIcon(new ItemStack(Items.LIME_WOOL)));
        } catch (Exception e) {
            button.setIcon(new TextureIcon(Identifier.fromNamespaceAndPath("minecraft", "textures/block/lime_wool.png")));
        }
        button.setOnClick(() -> {
            MCVersionRenamerConfig.titleText = textField.getText();
            MCVersionRenamerConfig.versionText = textField.getText();
            MCVersionRenamerConfig.f3Text = textField.getText();
            MCVersionRenamerClient.setClientWindowName(MCVersionRenamerConfig.titleText);
            MCVersionRenamerPublicData.versionText = MCVersionRenamerConfig.versionText;
            MCVersionRenamerPublicData.f3Text = MCVersionRenamerConfig.f3Text;
            Minecraft.getInstance().setScreen(lastScreen);
        });

        WButton closeButton = new WButton(Component.literal("Close"));
        try {
            closeButton.setIcon(new ItemIcon(new ItemStack(Items.RED_WOOL)));
        } catch (Exception e) {
            closeButton.setIcon(new TextureIcon(Identifier.fromNamespaceAndPath("minecraft", "textures/block/red_wool.png")));
        }
        closeButton.setOnClick(() -> Minecraft.getInstance().setScreen(lastScreen));

        WButton defaultButton = new WButton(Component.literal("Default"));
        try {
            defaultButton.setIcon(new ItemIcon(new ItemStack(Items.WHITE_WOOL)));
        } catch (Exception e) {
            defaultButton.setIcon(new TextureIcon(Identifier.fromNamespaceAndPath("minecraft", "textures/block/white_wool.png")));
        }
        defaultButton.setOnClick(() -> {
            MCVersionRenamerConfig.versionText = MCVersionRenamerPublicData.defaultVersionText;
            MCVersionRenamerConfig.titleText = MCVersionRenamerPublicData.defaultTitleText;
            MCVersionRenamerConfig.f3Text = MCVersionRenamerPublicData.defaultF3Text;
            MCVersionRenamerClient.setClientWindowName(MCVersionRenamerPublicData.defaultTitleText);
            MCVersionRenamerPublicData.titleText = MCVersionRenamerPublicData.defaultTitleText;
            MCVersionRenamerPublicData.versionText = MCVersionRenamerPublicData.defaultVersionText;
            MCVersionRenamerPublicData.f3Text = MCVersionRenamerPublicData.defaultF3Text;
            Minecraft.getInstance().setScreen(lastScreen);
        });

        WButton openConfigButton = new WButton(Component.literal("Open Config"));
        openConfigButton.setOnClick(() -> {
           Minecraft.getInstance().setScreen(MCVersionRenamerConfig.createConfigScreen(Minecraft.getInstance().screen));
        });

        root.add(textField, 3, 2, 11, 3);
        root.add(button, 0, 5, 5, 1);
        root.add(closeButton, 12, 5, 5, 1);
        root.add(defaultButton, 6, 5, 5, 1);

        root.add(openConfigButton, 0, 0, 5, 1);

        root.validate(this);
    }
}
