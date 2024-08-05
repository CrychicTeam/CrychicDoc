package com.github.einjerjar.mc.keymap.client.gui.widgets;

import com.github.einjerjar.mc.keymap.Keymap;
import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import com.github.einjerjar.mc.keymap.keys.KeyType;
import com.github.einjerjar.mc.keymap.keys.extrakeybind.KeyComboData;
import com.github.einjerjar.mc.keymap.keys.extrakeybind.KeymapRegistry;
import com.github.einjerjar.mc.keymap.keys.sources.KeymappingNotifier;
import com.github.einjerjar.mc.keymap.keys.wrappers.keys.KeyHolder;
import com.github.einjerjar.mc.keymap.keys.wrappers.keys.VanillaKeymap;
import com.github.einjerjar.mc.widgets.EList;
import com.github.einjerjar.mc.widgets.utils.Rect;
import com.github.einjerjar.mc.widgets.utils.Styles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class KeymapListWidget extends EList<KeymapListWidget.KeymapListEntry> {

    protected final List<KeymapListWidget.KeymapListEntry> filteredItemList = new ArrayList();

    protected String filterString = "";

    public KeymapListWidget(int itemHeight, int x, int y, int w, int h) {
        super(itemHeight, x, y, w, h);
        this.updateFilteredList();
    }

    @Override
    protected List<KeymapListWidget.KeymapListEntry> filteredItems() {
        return this.filteredItemList;
    }

    public KeymapListWidget filterString(String s) {
        this.filterString = s.trim().toLowerCase();
        this.updateFilteredList();
        return this;
    }

    @Override
    public void updateFilteredList() {
        this.filteredItemList.clear();
        if (this.filterString.trim().isBlank()) {
            this.filteredItemList.addAll(this.items);
        } else {
            for (KeymapListWidget.KeymapListEntry item : this.items) {
                List<String> segments = Arrays.stream(this.filterString.split(" ")).filter(s -> !s.isBlank()).toList();
                long matches = (long) segments.stream().filter(s -> item.map.getSearchString().contains(s)).toList().size();
                if (matches == (long) segments.size()) {
                    this.filteredItemList.add(item);
                }
            }
        }
        this.setScrollPos(0.0);
    }

    protected void setSelected(KeymapListWidget.KeymapListEntry i, boolean selected) {
        if (i != null) {
            i.selected(selected);
            KeymappingNotifier.notifySubscriber(i.map.getSingleCode(), selected);
        }
    }

    public void setItemSelected(KeymapListWidget.KeymapListEntry t) {
        this.setLastItemSelected(this.itemSelected);
        this.setSelected(this.itemSelected, false);
        this.itemSelected = t;
        this.setSelected(this.itemSelected, true);
    }

    public void setLastItemSelected(KeymapListWidget.KeymapListEntry t) {
        this.setSelected(this.lastItemSelected, false);
        this.lastItemSelected = t;
        this.setSelected(this.lastItemSelected, true);
    }

    public void resetKey() {
        KeymapListWidget.KeymapListEntry ix = this.itemSelected != null ? this.itemSelected : this.lastItemSelected;
        if (ix != null) {
            if (ix.map instanceof VanillaKeymap vk) {
                this.setKey(new KeyComboData(vk.map().getDefaultKey()));
            }
        }
    }

    public void resetAllKeys() {
        KeymapRegistry.resetAll();
        for (KeymapListWidget.KeymapListEntry item : this.items) {
            item.resetKey();
        }
        KeyMapping.resetMapping();
        KeymappingNotifier.loadKeys();
        KeymappingNotifier.notifyAllSubscriber();
        this.updateAllEntryTooltips();
        this.setItemSelected(null);
        this.setItemSelected(null);
    }

    public boolean setKeyForItem(KeyComboData kd) {
        return this.setKeyForItem(kd, false);
    }

    public boolean setKeyForItem(KeyComboData kd, boolean removeKeybindReg) {
        KeymapListWidget.KeymapListEntry item = this.itemSelected != null ? this.itemSelected : this.lastItemSelected;
        if (item == null) {
            return false;
        } else if (item.map instanceof VanillaKeymap vk) {
            int code = kd.keyCode();
            if (code == 256) {
                code = -1;
            }
            Keymap.logger().warn(code);
            KeymappingNotifier.updateKey((Integer) vk.getCode().get(0), code, vk);
            vk.setKey(List.of(code), kd.keyType() == KeyType.MOUSE);
            item.updateTooltips();
            item.selected(false);
            if (removeKeybindReg) {
                KeymapRegistry.remove(vk.map());
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean setKey(KeyComboData kd) {
        KeymapListWidget.KeymapListEntry item = this.itemSelected != null ? this.itemSelected : this.lastItemSelected;
        Keymap.logger().warn(kd);
        if (item != null && item.map instanceof VanillaKeymap vk) {
            int var9 = vk.getSingleCode();
            int newCode = kd.keyCode() == 256 ? -1 : kd.keyCode();
            int ko = KeymappingNotifier.keyOf(vk);
            int kk = ko;
            KeyComboData kl = (KeyComboData) KeymapRegistry.bindMap().get(vk.map());
            if (kl != null) {
                kk = kl.keyCode();
            }
            if (kd.onlyKey()) {
                KeymapRegistry.remove(vk.map());
                KeymappingNotifier.updateKey(var9, newCode, vk);
                vk.setKey(List.of(newCode), kd.keyType() == KeyType.MOUSE);
            } else {
                this.setComplexKey(kd, vk, var9, ko);
            }
            KeymappingNotifier.notifySubscriber(ko, false);
            KeymappingNotifier.notifySubscriber(kk, false);
            KeymappingNotifier.notifySubscriber(newCode, false);
            item.updateTooltips();
            item.selected(false);
            this.setItemSelected(null);
            this.setItemSelected(null);
            return true;
        } else {
            return false;
        }
    }

    private void setComplexKey(KeyComboData kd, VanillaKeymap vk, int lastCode, int ko) {
        Keymap.logger().error(2);
        KeyMapping vItem = null;
        if (KeymapRegistry.bindMap().inverse().containsKey(kd)) {
            vItem = (KeyMapping) KeymapRegistry.bindMap().inverse().get(kd);
        }
        KeymapRegistry.put(vk.map(), kd);
        KeymappingNotifier.updateKey(ko == -99 ? lastCode : ko, -1, vk);
        vk.setKey(List.of(-1), false);
        if (vItem != null) {
            for (KeymapListWidget.KeymapListEntry entry : this.items) {
                if (entry.map instanceof VanillaKeymap vvk && vvk.map() == vItem) {
                    entry.updateTooltips();
                    break;
                }
            }
        }
    }

    protected void updateAllEntryTooltips() {
        for (KeymapListWidget.KeymapListEntry item : this.items) {
            item.updateTooltips();
        }
    }

    @Override
    public void sort() {
        this.items().sort(Comparator.comparing(o -> o.map.getTranslatedName().getString()));
        this.filteredItemList.sort(Comparator.comparing(o -> o.map.getTranslatedName().getString()));
    }

    public String filterString() {
        return this.filterString;
    }

    public static class KeymapListEntry extends EList.EListEntry<KeymapListWidget.KeymapListEntry> {

        protected static final String CHAR_ASSIGNED = "⬛ ";

        protected static final String CHAR_UNASSIGNED = "⬜ ";

        protected static Integer CHAR_ASSIGN_W = null;

        protected KeyHolder map;

        protected Component keyString;

        public KeymapListEntry(KeyHolder map, KeymapListWidget container) {
            super(container);
            this.map = map;
            this.keyString = map.getTranslatedName();
            this.updateTooltips();
        }

        protected static int charW() {
            if (CHAR_ASSIGN_W == null) {
                CHAR_ASSIGN_W = Minecraft.getInstance().font.width("⬛ ");
            }
            return CHAR_ASSIGN_W;
        }

        protected void updateDebugTooltips() {
            if (KeymapConfig.instance().debug()) {
                this.tooltips.add(Component.literal("--------------------").withStyle(Styles.muted()));
                this.tooltips.add(Component.literal(String.format("Search: %s", this.map.getSearchString())).withStyle(Styles.yellow()));
            }
        }

        @Override
        public void updateTooltips() {
            this.tooltips.clear();
            this.tooltips.add(Component.literal(this.keyString.getString()).withStyle(Styles.headerBold()));
            this.tooltips.add(Component.literal(String.format("[@%s #%s]", Language.getInstance().getOrDefault(this.map().getCategory()), Language.getInstance().getOrDefault(this.map().getModName()))).withStyle(Styles.muted2()));
            this.tooltips.add(this.map.isAssigned() ? this.map.getTranslatedKey() : Component.translatable("key.keyboard.unknown"));
            this.updateDebugTooltips();
        }

        public String toString() {
            return "KeymapListEntryWidget{keyString=" + this.keyString.getString() + "}";
        }

        public void resetKey() {
            this.map.resetKey();
        }

        @Override
        public void renderWidget(@NotNull GuiGraphics guiGraphics, Rect r, float partialTick) {
            String trimmed = this.font.substrByWidth(this.keyString, r.w() - charW()).getString();
            guiGraphics.drawString(this.font, this.map.isAssigned() ? "⬛ " : "⬜ ", r.x(), r.y(), this.map.isAssigned() ? this.color.active().text() : this.color.normal().text());
            guiGraphics.drawString(this.font, trimmed, r.x() + charW(), r.y(), this.getVariant().text());
        }

        public KeyHolder map() {
            return this.map;
        }

        public Component keyString() {
            return this.keyString;
        }
    }
}