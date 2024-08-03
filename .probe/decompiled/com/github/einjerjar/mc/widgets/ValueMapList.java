package com.github.einjerjar.mc.widgets;

import com.github.einjerjar.mc.widgets.utils.Rect;
import java.util.Objects;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;

public class ValueMapList extends EList<ValueMapList.ValueMapEntry<?>> {

    public ValueMapList(int itemHeight, int x, int y, int w, int h, boolean canDeselectItem) {
        super(itemHeight, x, y, w, h);
        this.canDeselectItem = canDeselectItem;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
        this.drawOutline(guiGraphics, -1);
    }

    public void setItemSelectedWithKey(String key) {
        for (ValueMapList.ValueMapEntry<?> filteredItem : this.filteredItems()) {
            if (Objects.equals(filteredItem.key, key)) {
                this.setItemSelected(filteredItem);
                return;
            }
        }
    }

    public <T> void setItemSelectedWithValue(T value) {
        for (ValueMapList.ValueMapEntry<?> filteredItem : this.filteredItems()) {
            if (Objects.equals(filteredItem.value, value)) {
                this.setItemSelected(filteredItem);
                return;
            }
        }
    }

    public static class ValueMapEntry<V> extends EList.EListEntry<ValueMapList.ValueMapEntry<?>> {

        protected String key;

        protected V value;

        public ValueMapEntry(String key, V value, ValueMapList container) {
            super(container);
            this.key = key;
            this.value = value;
        }

        @Override
        public void renderWidget(@NotNull GuiGraphics guiGraphics, Rect r, float partialTick) {
            String trimmed = this.font.plainSubstrByWidth(this.key, r.w());
            guiGraphics.drawString(this.font, trimmed, r.x(), r.y(), this.getVariant().text());
        }

        public String key() {
            return this.key;
        }

        public V value() {
            return this.value;
        }
    }
}