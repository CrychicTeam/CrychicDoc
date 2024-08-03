package dev.xkmc.l2artifacts.content.client.tab;

import dev.xkmc.l2tabs.tabs.core.BaseTab;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class TabSetEffects extends BaseTab<TabSetEffects> {

    public TabSetEffects(TabToken<TabSetEffects> token, TabManager manager, ItemStack stack, Component title) {
        super(token, manager, stack, title);
    }

    public void onTabClicked() {
        Minecraft.getInstance().setScreen(new SetEffectScreen(this.m_6035_()));
    }
}