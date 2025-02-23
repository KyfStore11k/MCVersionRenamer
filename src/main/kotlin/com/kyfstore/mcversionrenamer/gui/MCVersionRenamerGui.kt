package com.kyfstore.mcversionrenamer.gui

import com.kyfstore.mcversionrenamer.MCVersionRenamerClient.Companion.setClientWindowName
import com.kyfstore.mcversionrenamer.data.MCVersionPublicData
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription
import io.github.cottonmc.cotton.gui.widget.WButton
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WTextField
import io.github.cottonmc.cotton.gui.widget.data.Insets
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.TitleScreen
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.text.Text

class MCVersionRenamerGui : LightweightGuiDescription() {
    init {
        val root = WGridPanel()
        setRootPanel(root)
        root.setSize(300, 80)
        root.setInsets(Insets.ROOT_PANEL)

        val textField = WTextField()
        textField.setMaxLength(50)

        val button = WButton(Text.literal("Set Text"))
        button.setIcon(ItemIcon(ItemStack(Items.LIME_WOOL)))
        button.setOnClick {
            setClientWindowName(textField.text)
            MCVersionPublicData.versionText = textField.text
            MinecraftClient.getInstance().reloadResources()
            MinecraftClient.getInstance().setScreen(TitleScreen())
        }

        val closeButton = WButton(Text.literal("Close"))
        closeButton.setIcon(ItemIcon(ItemStack(Items.RED_WOOL)))
        closeButton.setOnClick {
            MinecraftClient.getInstance().setScreen(TitleScreen())
        }

        val defaultButton = WButton(Text.literal("Default"))
        defaultButton.setIcon(ItemIcon(ItemStack(Items.WHITE_WOOL)))
        defaultButton.setOnClick {
            setClientWindowName("Mineraft* 1.21.4")
            MCVersionPublicData.versionText = "Minecraft* 1.21.4/Fabric (Modded)"
            MinecraftClient.getInstance().reloadResources()
            MinecraftClient.getInstance().setScreen(TitleScreen())
        }

        root.add(textField, 3, 2, 11, 3)
        root.add(button, 0, 5, 5, 1)
        root.add(closeButton, 12, 5, 5, 1)
        root.add(defaultButton, 6, 5, 5, 1)

        root.validate(this)
    }
}
