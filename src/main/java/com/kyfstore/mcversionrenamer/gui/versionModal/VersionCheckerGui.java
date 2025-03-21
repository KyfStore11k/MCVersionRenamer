package com.kyfstore.mcversionrenamer.gui.versionModal;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon;

public class VersionCheckerGui extends LightweightGuiDescription {
    public enum VersionPopupModalType {
        OLD_VERSION,
        NEW_VERSION
    }

    public VersionCheckerGui(VersionPopupModalType type, String latestVersion) {
        int labelHeight = 4;
        int labelWidth = 4;
        int labelX = 1;
        int labelY = 1;

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(300, 80);
        root.setInsets(Insets.ROOT_PANEL);

        WButton closeButton = new WButton(Text.literal("Close"));
        // closeButton.setIcon(new ItemIcon(new ItemStack(Items.RED_WOOL))); // This may error in some scenarios!
        closeButton.setOnClick(() -> MinecraftClient.getInstance().setScreen(new TitleScreen()));

        WLabel oldVersionText1 = new WLabel(Text.literal("There is a newer version of MCVersionRenamer"));
        WLabel oldVersionText2 = new WLabel(Text.literal(String.format("available (%s)! If you would like to update", latestVersion)));
        WLabel oldVersionText3 = new WLabel(Text.literal("please visit the Modrinth/GitHub/CurseForge page."));
        WLabel oldVersionTextLink1 = new WLabel(Text.literal("https://www.modrinth.com/mod/mcversionrenamer").setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.modrinth.com/mod/mcversionrenamer"))));
        WLabel oldVersionTextLink2 = new WLabel(Text.literal("https://www.github.com/KyfStore11k/MCVersionRenamer").setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.github.com/KyfStore11k/MCVersionRenamer"))));
        WLabel oldVersionTextLink3 = new WLabel(Text.literal("https://www.curseforge.com/minecraft/mc-mods/mcversionrenamer").setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.curseforge.com/minecraft/mc-mods/mcversionrenamer"))));

        oldVersionTextLink1.setColor(0x96ff2e).setDarkmodeColor(0x96ff2e);
        oldVersionTextLink2.setColor(0x1c1c1c).setDarkmodeColor(0x1c1c1c);
        oldVersionTextLink3.setColor(0xffab2e).setDarkmodeColor(0xffab2e);

        WLabel newVersionText1 = new WLabel(Text.literal("It appears you have a version of MCVersionRenamer"));
        WLabel newVersionText2 = new WLabel(Text.literal(String.format("that is newer than the latest version (%s).", latestVersion)));
        WLabel newVersionText3 = new WLabel(Text.literal("I will assume this is an early dev version and"));
        WLabel newVersionText4 = new WLabel(Text.literal("ignore this warning. To dismiss this message,"));
        WLabel newVersionText5 = new WLabel(Text.literal("please press the 'Close' button below."));

        root.add(closeButton, 12, 5, 5, 1);

        if (type == VersionPopupModalType.OLD_VERSION) {
            root.add(oldVersionText1, labelX, labelY, labelWidth, labelHeight);
            root.add(oldVersionText2, labelX, labelY + 1, labelWidth, labelHeight);
            root.add(oldVersionText3, labelX, labelY + 2, labelWidth, labelHeight);
            root.add(oldVersionTextLink1, labelX, labelY + 6, labelWidth, labelHeight);
            root.add(oldVersionTextLink2, labelX, labelY + 7, labelWidth, labelHeight);
            root.add(oldVersionTextLink3, labelX, labelY + 8, labelWidth, labelHeight);

        }
        else if (type == VersionPopupModalType.NEW_VERSION) {
            root.add(newVersionText1, labelX, labelY, labelWidth, labelHeight);
            root.add(newVersionText2, labelX, labelY + 1, labelWidth, labelHeight);
            root.add(newVersionText3, labelX, labelY + 2, labelWidth, labelHeight);
            root.add(newVersionText4, labelX, labelY + 3, labelWidth, labelHeight);
            root.add(newVersionText5, labelX, labelY + 4, labelWidth, labelHeight);
        }

        root.validate(this);
    }

}
