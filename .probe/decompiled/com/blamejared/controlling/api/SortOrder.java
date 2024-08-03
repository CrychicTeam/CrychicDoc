package com.blamejared.controlling.api;

import com.blamejared.controlling.ControllingConstants;
import com.blamejared.controlling.client.NewKeyBindsList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.network.chat.Component;

public enum SortOrder {

    NONE("options.sortNone", entries -> {
    }), AZ("options.sortAZ", entries -> entries.sort(Comparator.comparing(o -> ((NewKeyBindsList.KeyEntry) o).getKeyDesc().getString()))), ZA("options.sortZA", entries -> entries.sort(Comparator.comparing(o -> ((NewKeyBindsList.KeyEntry) o).getKeyDesc().getString()).reversed()));

    private final ISort sorter;

    private final Component display;

    private SortOrder(String key, ISort sorter) {
        this.sorter = sorter;
        this.display = ControllingConstants.COMPONENT_OPTIONS_SORT.m_6881_().append(": ").append(Component.translatable(key));
    }

    public SortOrder cycle() {
        return values()[(this.ordinal() + 1) % values().length];
    }

    public void sort(List<KeyBindsList.Entry> list) {
        list.removeIf(entry -> !(entry instanceof NewKeyBindsList.KeyEntry));
        this.sorter.sort(list);
    }

    public Component getDisplay() {
        return this.display;
    }
}