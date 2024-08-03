package com.github.einjerjar.mc.widgets;

import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import com.github.einjerjar.mc.widgets.utils.ColorGroup;
import com.github.einjerjar.mc.widgets.utils.ColorSet;
import com.github.einjerjar.mc.widgets.utils.ColorType;
import com.github.einjerjar.mc.widgets.utils.Point;
import com.github.einjerjar.mc.widgets.utils.Rect;
import com.github.einjerjar.mc.widgets.utils.Tooltipped;
import com.github.einjerjar.mc.widgets.utils.WidgetUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class EList<T extends EList.EListEntry<T>> extends EWidget {

    protected final Point<Double> lastClick = new Point<>(0.0);

    protected boolean dragging;

    protected int itemHeight;

    protected int scrollBarWidth = 6;

    protected int scrollSpeed = 8;

    protected List<T> items = new ArrayList();

    protected T itemHovered;

    protected T itemSelected;

    protected T lastItemSelected;

    protected Minecraft client;

    protected double scrollOffset = 0.0;

    protected double lastDrag;

    protected double lastScrollPos;

    protected boolean canDeselectItem = true;

    protected boolean lastClickWasInside = false;

    protected boolean didDrag = false;

    EWidget.SimpleWidgetAction<EList<T>> onItemSelected;

    protected EList(int itemHeight, int x, int y, int w, int h) {
        super(x, y, w, h);
        this._init(itemHeight);
    }

    protected EList(int itemHeight, Rect rect) {
        super(rect);
        this._init(itemHeight);
    }

    protected List<T> filteredItems() {
        return this.items;
    }

    public void setItemSelectedWithIndex(int i) {
        if (this.size() != 0) {
            if (this.size() <= i) {
                this.setItemSelected((T) this.items.get(0));
            } else {
                this.setItemSelected((T) this.items.get(i));
            }
        }
    }

    protected void setSelected(T i, boolean selected) {
        if (i != null) {
            i.selected(selected);
        }
    }

    protected void setItemSelected(T t) {
        this.setLastItemSelected(this.itemSelected);
        this.setSelected(this.itemSelected, false);
        this.itemSelected = t;
        this.setSelected(this.itemSelected, true);
    }

    protected void setLastItemSelected(T t) {
        this.setSelected(this.lastItemSelected, false);
        this.lastItemSelected = t;
        this.setSelected(this.lastItemSelected, true);
    }

    public void updateFilteredList() {
    }

    public void addItem(T item) {
        if (!this.items.contains(item)) {
            this.items.add(item);
        }
    }

    public void removeItem(T item) {
        this.items.remove(item);
    }

    public void clearItems() {
        this.items.clear();
        this.updateFilteredList();
    }

    public int size() {
        return this.filteredItems().size();
    }

    protected int scrollBarX() {
        return this.right() - this.scrollBarWidth;
    }

    protected T getHoveredItem(double mouseX, double mouseY) {
        int x = (int) mouseX;
        int y = (int) mouseY;
        if (x >= this.left() + this.padding.x() && x <= this.right() - this.padding.x()) {
            y = (int) ((double) y - ((double) (this.top() + this.padding.y()) - this.scrollOffset));
            int ix = y / this.itemHeight;
            return (T) (ix >= 0 && ix < this.size() ? this.filteredItems().get(ix) : null);
        } else {
            return null;
        }
    }

    protected int contentHeight() {
        return this.size() * this.itemHeight;
    }

    protected double maxScroll() {
        return (double) Math.max(0, this.contentHeight() - (this.rect.h() - this.padding.y() * 2));
    }

    protected boolean inScrollbar(double mouseX, double mouseY) {
        return mouseY >= (double) (this.top() + this.padding.y()) && mouseY <= (double) (this.bottom() - this.padding.y()) && mouseX >= (double) this.scrollBarX() && mouseX <= (double) (this.scrollBarWidth + this.scrollBarX());
    }

    public void setScrollPos(double pos) {
        this.scrollOffset = WidgetUtils.clamp(pos, 0.0, this.maxScroll());
    }

    public void relativeScrollPos(double pos) {
        this.scrollOffset = WidgetUtils.clamp(this.scrollOffset + pos, 0.0, this.maxScroll());
    }

    protected void _init(int itemHeight) {
        this.client = Minecraft.getInstance();
        this.itemHeight = itemHeight;
        this.allowRightClick = true;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (KeymapConfig.instance().debug()) {
            this.drawOutline(guiGraphics, -65536);
        }
        this.renderList(guiGraphics, mouseX, mouseY, partialTick);
        this.renderScrollBar();
    }

    protected void renderScrollBar() {
        Tesselator ts = Tesselator.getInstance();
        BufferBuilder bb = ts.getBuilder();
        int ch = this.contentHeight();
        int eh = this.rect.h() - this.padding.y() * 2;
        if (ch != 0) {
            RenderSystem.enableBlend();
            RenderSystem.setShader(GameRenderer::m_172811_);
            int colScrollBg = -2013265920;
            int colScrollFg = -1996488705;
            double scroll = (double) ((float) eh / (float) ch);
            if (!(scroll >= 1.0)) {
                bb.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
                int scrollTop = (int) (this.scrollOffset * scroll);
                int scrollHeight = (int) ((double) eh * scroll);
                int scrollLeft = this.right() - this.padding.x();
                int padTop = this.top() + this.padding.y();
                int actualScrollTop = padTop + scrollTop;
                int scrollBottom = actualScrollTop + scrollHeight;
                WidgetUtils.drawQuad(ts, bb, scrollLeft, this.right(), padTop, this.bottom() - this.padding.y(), colScrollBg, false);
                WidgetUtils.drawQuad(ts, bb, scrollLeft, this.right(), actualScrollTop, scrollBottom, colScrollFg, false);
                ts.end();
            }
        }
    }

    protected void renderList(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (this.itemHovered != null) {
            this.itemHovered.hovered(false);
        }
        this.itemHovered = this.getHoveredItem((double) mouseX, (double) mouseY);
        if (this.itemHovered != null) {
            this.itemHovered.hovered(true);
        }
        for (int i = 0; i < this.size(); i++) {
            T e = (T) this.filteredItems().get(i);
            Rect r = new Rect(this.left() + this.padding.x(), this.top() + i * this.itemHeight + this.padding.y() - (int) this.scrollOffset, this.rect.w() - this.padding.x() * 2, this.itemHeight);
            if (r.midY() <= this.rect.bottom() - this.padding.y() && r.midY() >= this.rect.top() + this.padding.y()) {
                e.render(guiGraphics, r, partialTick);
            }
        }
    }

    @Nullable
    @Override
    public List<Component> getTooltips() {
        return this.itemHovered != null ? this.itemHovered.getTooltips() : null;
    }

    protected void sort() {
    }

    @Override
    public boolean onMouseReleased(boolean inside, double mouseX, double mouseY, int button) {
        if (this.didDrag) {
            this.didDrag = false;
            return false;
        } else if (inside && button == 1) {
            this.setItemSelected(null);
            return true;
        } else {
            this.itemHovered = this.getHoveredItem(mouseX, mouseY);
            if (this.itemHovered == null && this.itemSelected != null) {
                if (this.canDeselectItem) {
                    this.setItemSelected(null);
                }
                return false;
            } else if (this.itemHovered == null) {
                return false;
            } else if (!inside) {
                return false;
            } else {
                for (T item : this.items) {
                    item.selected(false);
                    item.hovered(false);
                }
                this.setItemSelected(this.itemHovered);
                if (this.itemSelected != null) {
                    this.itemSelected.selected(true);
                    if (this.onItemSelected != null) {
                        this.onItemSelected.run(this);
                    }
                }
                return true;
            }
        }
    }

    @Override
    protected boolean onMouseScrolled(double mouseX, double mouseY, double delta) {
        this.relativeScrollPos(-delta * (double) this.scrollSpeed);
        return true;
    }

    @Override
    public EWidget focused(boolean focused) {
        if (!focused && this.itemSelected != null && this.canDeselectItem) {
            this.setItemSelected(null);
        }
        return super.focused(focused);
    }

    @Override
    public boolean onMouseClicked(boolean inside, double mouseX, double mouseY, int button) {
        this.lastClickWasInside = inside;
        this.lastClick.setXY(mouseX, mouseY);
        this.lastScrollPos = this.scrollOffset;
        if (!inside) {
            this.onMouseReleased(inside, mouseX, mouseY, button);
        }
        return this.getHoveredItem(mouseX, mouseY) != null;
    }

    @Override
    protected boolean onMouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (!this.lastClickWasInside) {
            return false;
        } else {
            this.didDrag = true;
            this.setScrollPos(this.lastScrollPos - (mouseY - this.lastClick.y()));
            return true;
        }
    }

    public boolean dragging() {
        return this.dragging;
    }

    public int itemHeight() {
        return this.itemHeight;
    }

    public int scrollBarWidth() {
        return this.scrollBarWidth;
    }

    public int scrollSpeed() {
        return this.scrollSpeed;
    }

    public List<T> items() {
        return this.items;
    }

    public T itemHovered() {
        return this.itemHovered;
    }

    public T itemSelected() {
        return this.itemSelected;
    }

    public T lastItemSelected() {
        return this.lastItemSelected;
    }

    public EList<T> onItemSelected(EWidget.SimpleWidgetAction<EList<T>> onItemSelected) {
        this.onItemSelected = onItemSelected;
        return this;
    }

    public abstract static class EListEntry<T extends EList.EListEntry<T>> implements Tooltipped {

        protected final Font font;

        protected final List<Component> tooltips = new ArrayList();

        protected boolean selected = false;

        protected boolean hovered = false;

        protected EList<T> container;

        protected ColorGroup color = new ColorGroup(new ColorSet(16777215, ColorType.NORMAL), new ColorSet(16724787, ColorType.HOVER), new ColorSet(65280, ColorType.ACTIVE), new ColorSet(16777215, ColorType.DISABLED));

        protected EListEntry(EList<T> container) {
            this.font = Minecraft.getInstance().font;
            this.container = container;
        }

        protected ColorSet getVariant() {
            if (this.selected) {
                return this.color.active();
            } else {
                return this.hovered ? this.color.hover() : this.color.normal();
            }
        }

        @Override
        public List<Component> getTooltips() {
            return this.tooltips;
        }

        public void render(@NotNull GuiGraphics guiGraphics, Rect r, float partialTick) {
            this.renderWidget(guiGraphics, r, partialTick);
        }

        public void updateTooltips() {
        }

        public void provideTooltips(List<Component> tips) {
            this.tooltips.clear();
            if (tips != null) {
                this.tooltips.addAll(tips);
            }
        }

        public abstract void renderWidget(@NotNull GuiGraphics var1, Rect var2, float var3);

        public boolean selected() {
            return this.selected;
        }

        public EList.EListEntry<T> selected(boolean selected) {
            this.selected = selected;
            return this;
        }

        public boolean hovered() {
            return this.hovered;
        }

        public EList.EListEntry<T> hovered(boolean hovered) {
            this.hovered = hovered;
            return this;
        }

        public EList<T> container() {
            return this.container;
        }
    }
}