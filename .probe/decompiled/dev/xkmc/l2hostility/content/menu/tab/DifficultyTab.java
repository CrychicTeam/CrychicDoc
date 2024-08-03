package dev.xkmc.l2hostility.content.menu.tab;

import dev.xkmc.l2tabs.tabs.core.BaseTab;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class DifficultyTab extends BaseTab<DifficultyTab> {

    public DifficultyTab(TabToken<DifficultyTab> token, TabManager manager, ItemStack stack, Component title) {
        super(token, manager, stack, title);
    }

    public void onTabClicked() {
        Minecraft.getInstance().setScreen(new DifficultyScreen(this.m_6035_()));
    }
}