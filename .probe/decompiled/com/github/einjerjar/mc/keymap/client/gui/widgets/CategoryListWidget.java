package com.github.einjerjar.mc.keymap.client.gui.widgets;

import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import com.github.einjerjar.mc.keymap.keys.wrappers.categories.CategoryHolder;
import com.github.einjerjar.mc.widgets.EList;
import com.github.einjerjar.mc.widgets.utils.Rect;
import com.github.einjerjar.mc.widgets.utils.Styles;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class CategoryListWidget extends EList<CategoryListWidget.CategoryListEntry> {

    public CategoryListWidget(int itemHeight, int x, int y, int w, int h) {
        super(itemHeight, x, y, w, h);
    }

    protected CategoryListWidget(int itemHeight, Rect rect) {
        super(itemHeight, rect);
    }

    @Override
    public boolean onMouseReleased(boolean inside, double mouseX, double mouseY, int button) {
        boolean ret = super.onMouseReleased(inside, mouseX, mouseY, button);
        this.setItemSelected(null);
        this.setLastItemSelected(null);
        return ret;
    }

    public static class CategoryListEntry extends EList.EListEntry<CategoryListWidget.CategoryListEntry> {

        protected CategoryHolder category;

        protected Component keyString;

        public CategoryListEntry(CategoryHolder category, CategoryListWidget container) {
            super(container);
            this.category = category;
            this.keyString = this.category.getTranslatedName();
            this.updateTooltips();
        }

        @Override
        public void updateTooltips() {
            this.tooltips.clear();
            this.tooltips.add(Component.literal(this.keyString.getString()).withStyle(Styles.headerBold()));
            this.tooltips.add(Component.literal(this.category.getModName()).withStyle(Styles.muted2()));
            if (KeymapConfig.instance().debug()) {
                this.tooltips.add(Component.literal("--------------------").withStyle(Styles.muted()));
                this.tooltips.add(Component.literal(String.format("Search: %s", this.category.getFilterSlug())).withStyle(Styles.yellow()));
            }
        }

        @Override
        public void renderWidget(@NotNull GuiGraphics guiGraphics, Rect r, float partialTick) {
            String trimmed = this.font.substrByWidth(this.keyString, r.w()).getString();
            guiGraphics.drawString(this.font, trimmed, r.x(), r.y(), this.getVariant().text());
        }

        public CategoryHolder category() {
            return this.category;
        }

        public Component keyString() {
            return this.keyString;
        }
    }
}