package net.minecraftforge.client.gui;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.common.CreativeModeTabRegistry;
import net.minecraftforge.common.util.ConcatenatedListView;

public final class CreativeTabsScreenPage {

    private final List<CreativeModeTab> tabs;

    private final List<CreativeModeTab> topTabs;

    private final List<CreativeModeTab> bottomTabs;

    private final List<CreativeModeTab> visibleTabs;

    public CreativeTabsScreenPage(List<CreativeModeTab> tabs) {
        this.tabs = tabs;
        this.topTabs = new ArrayList();
        this.bottomTabs = new ArrayList();
        this.visibleTabs = ConcatenatedListView.of(tabs, CreativeModeTabRegistry.getDefaultTabs());
        int maxLength = 10;
        int topLength = maxLength / 2;
        int length = tabs.size();
        for (int i = 0; i < length; i++) {
            CreativeModeTab tab = (CreativeModeTab) tabs.get(i);
            (i < topLength ? this.topTabs : this.bottomTabs).add(tab);
        }
    }

    public List<CreativeModeTab> getVisibleTabs() {
        return this.visibleTabs.stream().filter(CreativeModeTab::m_257497_).toList();
    }

    public boolean isTop(CreativeModeTab tab) {
        return !this.tabs.contains(tab) ? CreativeModeTabRegistry.getDefaultTabs().indexOf(tab) < CreativeModeTabRegistry.getDefaultTabs().size() / 2 : this.topTabs.contains(tab);
    }

    public int getColumn(CreativeModeTab tab) {
        if (!this.tabs.contains(tab)) {
            return CreativeModeTabRegistry.getDefaultTabs().indexOf(tab) % (CreativeModeTabRegistry.getDefaultTabs().size() / 2) + 5;
        } else {
            return this.topTabs.contains(tab) ? this.topTabs.indexOf(tab) : this.bottomTabs.indexOf(tab);
        }
    }

    public CreativeModeTab getDefaultTab() {
        return (CreativeModeTab) this.tabs.get(0);
    }
}