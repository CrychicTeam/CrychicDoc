package dev.xkmc.l2artifacts.content.search.fitered;

import dev.xkmc.l2artifacts.content.search.common.AbstractScrollerScreen;
import dev.xkmc.l2artifacts.content.search.tabs.FilterTabManager;
import dev.xkmc.l2artifacts.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class FilteredMenuScreen extends AbstractScrollerScreen<FilteredMenu> {

    public FilteredMenuScreen(FilteredMenu cont, Inventory plInv, Component title) {
        super(cont, plInv, LangData.TAB_FILTERED.get(), FilterTabManager.FILTERED);
    }

    @Override
    public Component getTitle() {
        return super.m_96636_().copy().append(": " + ((FilteredMenu) this.f_97732_).current_count.get() + "/" + ((FilteredMenu) this.f_97732_).total_count.get()).withStyle(ChatFormatting.GRAY);
    }
}