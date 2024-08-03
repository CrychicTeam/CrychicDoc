package de.keksuccino.fancymenu.util.rendering.ui.scroll.v2.scrollarea;

import de.keksuccino.fancymenu.util.rendering.DrawableColor;
import de.keksuccino.fancymenu.util.rendering.ui.UIBase;
import de.keksuccino.fancymenu.util.rendering.ui.scroll.v2.scrollarea.entry.ScrollAreaEntry;
import de.keksuccino.fancymenu.util.rendering.ui.scroll.v2.scrollbar.ScrollBar;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ScrollArea extends UIBase implements GuiEventListener, Renderable, NarratableEntry {

    private static final Logger LOGGER = LogManager.getLogger();

    public ScrollBar verticalScrollBar;

    public ScrollBar horizontalScrollBar;

    protected float x;

    protected float y;

    protected float width;

    protected float height;

    public Supplier<DrawableColor> backgroundColor = () -> getUIColorTheme().area_background_color;

    public Supplier<DrawableColor> borderColor = () -> getUIColorTheme().element_border_color_normal;

    protected float borderThickness = 1.0F;

    public boolean makeEntriesWidthOfArea = false;

    public boolean minimumEntryWidthIsAreaWidth = true;

    public boolean makeAllEntriesWidthOfWidestEntry = true;

    protected List<ScrollAreaEntry> entries = new ArrayList();

    public float overriddenTotalScrollWidth = -1.0F;

    public float overriddenTotalScrollHeight = -1.0F;

    public boolean correctYOnAddingRemovingEntries = true;

    protected boolean applyScissor = true;

    @Nullable
    public Float renderScale = null;

    protected boolean hovered = false;

    protected boolean innerAreaHovered = false;

    public ScrollArea(float x, float y, float width, float height) {
        this.setX(x, true);
        this.setY(y, true);
        this.setWidth(width, true);
        this.setHeight(height, true);
        this.verticalScrollBar = new ScrollBar(ScrollBar.ScrollBarDirection.VERTICAL, 5.0F, 40.0F, 0.0F, 0.0F, 0.0F, 0.0F, () -> getUIColorTheme().scroll_grabber_color_normal, () -> getUIColorTheme().scroll_grabber_color_hover);
        this.verticalScrollBar.setScrollWheelAllowed(true);
        this.horizontalScrollBar = new ScrollBar(ScrollBar.ScrollBarDirection.HORIZONTAL, 40.0F, 5.0F, 0.0F, 0.0F, 0.0F, 0.0F, () -> getUIColorTheme().scroll_grabber_color_normal, () -> getUIColorTheme().scroll_grabber_color_hover);
        this.updateScrollArea();
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        this.hovered = this.isMouseOver((double) mouseX, (double) mouseY);
        this.innerAreaHovered = this.isMouseOverInnerArea((double) mouseX, (double) mouseY);
        this.updateScrollArea();
        this.updateWheelScrollSpeed();
        this.resetScrollOnFit();
        this.renderBackground(graphics, mouseX, mouseY, partial);
        this.renderEntries(graphics, mouseX, mouseY, partial);
        this.renderBorder(graphics, mouseX, mouseY, partial);
        if (this.verticalScrollBar.active) {
            this.verticalScrollBar.render(graphics, mouseX, mouseY, partial);
        }
        if (this.horizontalScrollBar.active) {
            this.horizontalScrollBar.render(graphics, mouseX, mouseY, partial);
        }
    }

    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        fillF(graphics, this.getInnerX(), this.getInnerY(), this.getInnerX() + this.getInnerWidth(), this.getInnerY() + this.getInnerHeight(), ((DrawableColor) this.backgroundColor.get()).getColorInt());
    }

    public void renderBorder(GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        renderBorder(graphics, this.getXWithBorder(), this.getYWithBorder(), this.getXWithBorder() + this.getWidthWithBorder(), this.getYWithBorder() + this.getHeightWithBorder(), this.getBorderThickness(), ((DrawableColor) this.borderColor.get()).getColorInt(), true, true, true, true);
    }

    public void renderEntries(GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        if (this.isApplyScissor()) {
            double scale = this.renderScale != null ? (double) this.renderScale.floatValue() : 1.0;
            int xMin = (int) ((double) this.getInnerX() * scale);
            int yMin = (int) ((double) this.getInnerY() * scale);
            int xMax = xMin + (int) this.getInnerWidth();
            int yMax = yMin + (int) this.getInnerHeight();
            graphics.enableScissor(xMin, yMin, xMax, yMax);
        }
        float totalWidth = this.makeAllEntriesWidthOfWidestEntry ? this.getTotalEntryWidth() : 0.0F;
        this.updateEntries(entry -> {
            if (this.makeAllEntriesWidthOfWidestEntry) {
                entry.setWidth(totalWidth);
            }
            if (this.minimumEntryWidthIsAreaWidth && entry.getWidth() < this.getInnerWidth()) {
                entry.setWidth(this.getInnerWidth());
            }
            entry.render(graphics, mouseX, mouseY, partial);
        });
        if (this.isApplyScissor()) {
            graphics.disableScissor();
        }
    }

    public float getEntryRenderOffsetX() {
        return this.getEntryRenderOffsetX(this.getTotalScrollWidth());
    }

    public float getEntryRenderOffsetY() {
        return this.getEntryRenderOffsetY(this.getTotalScrollHeight());
    }

    public float getEntryRenderOffsetX(float totalScrollWidth) {
        return -(totalScrollWidth / 100.0F * this.horizontalScrollBar.getScroll() * 100.0F);
    }

    public float getEntryRenderOffsetY(float totalScrollHeight) {
        return -(totalScrollHeight / 100.0F * this.verticalScrollBar.getScroll() * 100.0F);
    }

    public float getTotalScrollWidth() {
        return this.overriddenTotalScrollWidth != -1.0F ? this.overriddenTotalScrollWidth : Math.max(0.0F, this.getTotalEntryWidth() - this.getInnerWidth());
    }

    public float getTotalScrollHeight() {
        return this.overriddenTotalScrollHeight != -1.0F ? this.overriddenTotalScrollHeight : Math.max(0.0F, this.getTotalEntryHeight() - this.getInnerHeight());
    }

    public void updateEntries(@Nullable Consumer<ScrollAreaEntry> doAfterEachEntryUpdate) {
        try {
            int index = 0;
            float y = this.getInnerY();
            for (ScrollAreaEntry e : new ArrayList(this.entries)) {
                e.index = index;
                e.setX(this.getInnerX() + this.getEntryRenderOffsetX());
                e.setY(y + this.getEntryRenderOffsetY());
                if (this.makeEntriesWidthOfArea) {
                    e.setWidth(this.getInnerWidth());
                }
                if (doAfterEachEntryUpdate != null) {
                    doAfterEachEntryUpdate.accept(e);
                }
                index++;
                y += e.getHeight();
            }
        } catch (Exception var7) {
            var7.printStackTrace();
        }
    }

    public void updateScrollArea() {
        this.verticalScrollBar.scrollAreaStartX = this.getInnerX() + 1.0F;
        this.verticalScrollBar.scrollAreaStartY = this.getInnerY() + 1.0F;
        this.verticalScrollBar.scrollAreaEndX = this.getInnerX() + this.getInnerWidth() - 1.0F;
        this.verticalScrollBar.scrollAreaEndY = this.getInnerY() + this.getInnerHeight() - this.horizontalScrollBar.grabberHeight - 2.0F;
        this.horizontalScrollBar.scrollAreaStartX = this.getInnerX() + 1.0F;
        this.horizontalScrollBar.scrollAreaStartY = this.getInnerY() + 1.0F;
        this.horizontalScrollBar.scrollAreaEndX = this.getInnerX() + this.getInnerWidth() - this.verticalScrollBar.grabberWidth - 2.0F;
        this.horizontalScrollBar.scrollAreaEndY = this.getInnerY() + this.getInnerHeight() - 1.0F;
    }

    public void updateWheelScrollSpeed() {
        this.verticalScrollBar.setWheelScrollSpeed(1.0F / (this.getTotalScrollHeight() / 500.0F));
    }

    public void resetScrollOnFit() {
        if (this.getTotalEntryWidth() <= this.getInnerWidth()) {
            this.horizontalScrollBar.setScroll(0.0F);
        }
        if (this.getTotalEntryHeight() <= this.getInnerHeight()) {
            this.verticalScrollBar.setScroll(0.0F);
        }
    }

    public void correctYScrollAfterAddingOrRemovingEntries(boolean removed, ScrollAreaEntry... addedOrRemovedEntries) {
        if (addedOrRemovedEntries != null && addedOrRemovedEntries.length > 0) {
            int totalHeightRemovedAdded = 0;
            for (ScrollAreaEntry e : addedOrRemovedEntries) {
                totalHeightRemovedAdded = (int) ((float) totalHeightRemovedAdded + e.getHeight());
            }
            float oldTotalScrollHeight;
            if (!removed) {
                oldTotalScrollHeight = this.getTotalScrollHeight() - (float) totalHeightRemovedAdded;
            } else {
                oldTotalScrollHeight = this.getTotalScrollHeight() + (float) totalHeightRemovedAdded;
            }
            float yOld = this.getEntryRenderOffsetY(oldTotalScrollHeight);
            float yNew = this.getEntryRenderOffsetY();
            float yDiff = Math.max(yOld, yNew) - Math.min(yOld, yNew);
            if (this.getTotalScrollHeight() <= 0.0F) {
                return;
            }
            float scrollDiff = Math.max(0.0F, Math.min(1.0F, yDiff / this.getTotalScrollHeight()));
            if (!removed) {
                scrollDiff = -scrollDiff;
            }
            this.verticalScrollBar.setScroll(this.verticalScrollBar.getScroll() + scrollDiff);
        }
    }

    public boolean isMouseInteractingWithGrabbers() {
        return this.verticalScrollBar.isGrabberGrabbed() || this.verticalScrollBar.isGrabberHovered() || this.horizontalScrollBar.isGrabberGrabbed() || this.horizontalScrollBar.isGrabberHovered();
    }

    public void setX(float x, boolean respectBorder) {
        this.x = x;
        if (respectBorder) {
            this.x = this.x + this.borderThickness;
        }
    }

    public void setX(float x) {
        this.setX(x, true);
    }

    public float getInnerX() {
        return this.x;
    }

    public float getXWithBorder() {
        return this.x - this.borderThickness;
    }

    public void setY(float y, boolean respectBorder) {
        this.y = y;
        if (respectBorder) {
            this.y = this.y + this.borderThickness;
        }
    }

    public void setY(float y) {
        this.setY(y, true);
    }

    public float getInnerY() {
        return this.y;
    }

    public float getYWithBorder() {
        return this.y - this.borderThickness;
    }

    public void setWidth(float width, boolean respectBorder) {
        this.width = width;
        if (respectBorder) {
            this.width = this.width - this.borderThickness * 2.0F;
        }
    }

    public void setWidth(float width) {
        this.setWidth(width, true);
    }

    public float getInnerWidth() {
        return this.width;
    }

    public float getWidthWithBorder() {
        return this.width + this.borderThickness * 2.0F;
    }

    public void setHeight(float height, boolean respectBorder) {
        this.height = height;
        if (respectBorder) {
            this.height = this.height - this.borderThickness * 2.0F;
        }
    }

    public void setHeight(float height) {
        this.setHeight(height, true);
    }

    public float getInnerHeight() {
        return this.height;
    }

    public float getHeightWithBorder() {
        return this.height + this.borderThickness * 2.0F;
    }

    public void setBorderThickness(float borderThickness) {
        this.borderThickness = borderThickness;
    }

    public float getBorderThickness() {
        return this.borderThickness;
    }

    public boolean isInnerAreaHovered() {
        return this.innerAreaHovered;
    }

    public boolean isHovered() {
        return this.hovered;
    }

    public boolean isMouseOverInnerArea(double mouseX, double mouseY) {
        return isXYInArea(mouseX, mouseY, (double) this.getInnerX(), (double) this.getInnerY(), (double) this.getInnerWidth(), (double) this.getInnerHeight());
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.isMouseInteractingWithGrabbers() ? true : isXYInArea(mouseX, mouseY, (double) this.getXWithBorder(), (double) this.getYWithBorder(), (double) this.getWidthWithBorder(), (double) this.getHeightWithBorder());
    }

    public int getEntryCount() {
        return this.entries.size();
    }

    public float getTotalEntryWidth() {
        float i = this.width;
        for (ScrollAreaEntry e : this.entries) {
            if (e.getWidth() > i) {
                i = e.getWidth();
            }
        }
        return i;
    }

    public float getTotalEntryHeight() {
        float i = 0.0F;
        for (ScrollAreaEntry e : this.entries) {
            i += e.getHeight();
        }
        return i;
    }

    @Nullable
    public ScrollAreaEntry getFocusedEntry() {
        for (ScrollAreaEntry e : this.entries) {
            if (e.isSelected()) {
                return e;
            }
        }
        return null;
    }

    public int getFocusedEntryIndex() {
        ScrollAreaEntry e = this.getFocusedEntry();
        return e != null ? this.getIndexOfEntry(e) : -1;
    }

    public List<ScrollAreaEntry> getEntries() {
        return new ArrayList(this.entries);
    }

    @Nullable
    public ScrollAreaEntry getEntry(int index) {
        return index <= this.entries.size() - 1 ? (ScrollAreaEntry) this.entries.get(index) : null;
    }

    public void addEntry(ScrollAreaEntry entry) {
        if (!this.entries.contains(entry)) {
            this.entries.add(entry);
            if (this.correctYOnAddingRemovingEntries) {
                this.correctYScrollAfterAddingOrRemovingEntries(false, entry);
            }
        }
        this.makeCurrentEntriesSameWidth();
    }

    public void addEntryAtIndex(ScrollAreaEntry entry, int index) {
        if (index > this.getEntryCount()) {
            index = this.getEntryCount();
        }
        this.entries.add(index, entry);
        if (this.correctYOnAddingRemovingEntries) {
            this.correctYScrollAfterAddingOrRemovingEntries(false, entry);
        }
        this.makeCurrentEntriesSameWidth();
    }

    public void removeEntry(ScrollAreaEntry entry) {
        this.entries.remove(entry);
        if (this.correctYOnAddingRemovingEntries) {
            this.correctYScrollAfterAddingOrRemovingEntries(true, entry);
        }
        this.makeCurrentEntriesSameWidth();
    }

    public void removeEntryAtIndex(int index) {
        if (index <= this.getEntryCount() - 1) {
            ScrollAreaEntry entry = (ScrollAreaEntry) this.entries.remove(index);
            if (entry != null && this.correctYOnAddingRemovingEntries) {
                this.correctYScrollAfterAddingOrRemovingEntries(true, entry);
            }
        }
        this.makeCurrentEntriesSameWidth();
    }

    public void clearEntries() {
        this.entries.clear();
        this.verticalScrollBar.setScroll(0.0F);
        this.horizontalScrollBar.setScroll(0.0F);
    }

    public int getIndexOfEntry(ScrollAreaEntry entry) {
        return this.entries.indexOf(entry);
    }

    public void makeCurrentEntriesSameWidth() {
        float totalWidth = this.getTotalEntryWidth();
        for (ScrollAreaEntry e : this.getEntries()) {
            e.setWidth(totalWidth);
        }
    }

    public boolean isApplyScissor() {
        return this.applyScissor;
    }

    public void setApplyScissor(boolean apply) {
        this.applyScissor = apply;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.verticalScrollBar.mouseClicked(mouseX, mouseY, button)) {
            return true;
        } else if (this.horizontalScrollBar.mouseClicked(mouseX, mouseY, button)) {
            return true;
        } else {
            for (ScrollAreaEntry entry : this.entries) {
                if (entry.mouseClicked(mouseX, mouseY, button)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return this.verticalScrollBar.mouseReleased(mouseX, mouseY, button) ? true : this.horizontalScrollBar.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double $$3, double $$4) {
        return this.verticalScrollBar.mouseDragged(mouseX, mouseY, button, $$3, $$4) ? true : this.horizontalScrollBar.mouseDragged(mouseX, mouseY, button, $$3, $$4);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollDelta) {
        return this.verticalScrollBar.mouseScrolled(mouseX, mouseY, scrollDelta) ? true : this.horizontalScrollBar.mouseScrolled(mouseX, mouseY, scrollDelta);
    }

    @Override
    public void setFocused(boolean var1) {
    }

    @Override
    public boolean isFocused() {
        return false;
    }

    @NotNull
    @Override
    public NarratableEntry.NarrationPriority narrationPriority() {
        return NarratableEntry.NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput var1) {
    }
}