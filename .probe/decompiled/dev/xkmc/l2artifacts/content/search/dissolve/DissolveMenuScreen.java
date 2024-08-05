package dev.xkmc.l2artifacts.content.search.dissolve;

import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.search.common.AbstractScrollerScreen;
import dev.xkmc.l2artifacts.content.search.tabs.FilterTabManager;
import dev.xkmc.l2artifacts.content.upgrades.StatContainerItem;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2library.base.menu.base.MenuLayoutConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class DissolveMenuScreen extends AbstractScrollerScreen<DissolveMenu> {

    public DissolveMenuScreen(DissolveMenu cont, Inventory plInv, Component title) {
        super(cont, plInv, LangData.TAB_DISSOLVE.get().withStyle(ChatFormatting.GRAY), FilterTabManager.DISSOLVE);
    }

    @Override
    protected void renderBgExtra(GuiGraphics g, MenuLayoutConfig.ScreenRenderer sr, int mx, int my) {
        ItemStack stack = ((DissolveMenu) this.f_97732_).container.getItem(0);
        int rank = stack.isEmpty() ? 0 : ((StatContainerItem) stack.getItem()).rank;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (((DissolveMenu) this.f_97732_).select_index.get() == i * 6 + j) {
                    sr.draw(g, "grid", "toggle_slot_1", j * 18 - 1, i * 18 - 1);
                } else {
                    ItemStack art = ((DissolveMenu) this.f_97732_).container.getItem(i * 6 + j + 2);
                    int r = art.isEmpty() ? 0 : ((BaseArtifact) art.getItem()).rank;
                    if (rank > 0 && r > 0 && rank != r) {
                        sr.draw(g, "grid", "toggle_slot_2", j * 18 - 1, i * 18 - 1);
                    }
                }
            }
        }
    }

    @Override
    public Component getTitle() {
        return super.m_96636_().copy().append(": " + ((DissolveMenu) this.f_97732_).current_count.get() + "/" + ((DissolveMenu) this.f_97732_).total_count.get());
    }
}