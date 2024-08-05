package org.violetmoon.zeta.client.config.widget;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.NotNull;

public class ScrollableWidgetList<S extends Screen, E extends ScrollableWidgetList.Entry<E>> extends ObjectSelectionList<E> {

    public final S parent;

    public ScrollableWidgetList(S parent) {
        super(Minecraft.getInstance(), parent.width, parent.height, 40, parent.height - 40, 30);
        this.parent = parent;
    }

    public int addEntry(E entry) {
        return super.m_7085_(entry);
    }

    @Override
    public void replaceEntries(Collection<E> newEntries) {
        super.m_5988_(newEntries);
    }

    public void scroll2(int amt) {
        this.m_93410_(this.m_93517_() + (double) amt);
    }

    public void ensureVisible2(int index) {
        int i = this.m_7610_(index);
        int j = i - this.f_93390_ - 4 - this.f_93387_;
        if (j < 0) {
            this.scroll2(j);
        }
        int k = this.f_93391_ - i - this.f_93387_ - this.f_93387_;
        if (k < 0) {
            this.scroll2(-k);
        }
    }

    @Override
    protected int getScrollbarPosition() {
        return super.m_5756_() + 20;
    }

    @Override
    public int getRowWidth() {
        return super.m_5759_() + 50;
    }

    public void forEachWidgetWrapper(Consumer<ScrollableWidgetList.WidgetWrapper> action) {
        this.m_6702_().forEach(e -> e.children.forEach(action));
    }

    public void addChildWidgets(Consumer<AbstractWidget> addRenderableWidget, Consumer<AbstractWidget> addWidget) {
        this.forEachWidgetWrapper(w -> {
            if (w.widget instanceof Button) {
                addRenderableWidget.accept(w.widget);
            } else {
                addWidget.accept(w.widget);
            }
        });
    }

    public void removeChildWidgets(Consumer<AbstractWidget> removeWidget) {
        this.forEachWidgetWrapper(w -> removeWidget.accept(w.widget));
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.forEachWidgetWrapper(w -> {
            w.widget.visible = false;
            w.wasOnScreen = false;
        });
        super.m_88315_(guiGraphics, mouseX, mouseY, partialTicks);
    }

    public void reenableVisibleWidgets() {
        this.forEachWidgetWrapper(w -> {
            if (w.wasOnScreen) {
                w.widget.visible = true;
            }
        });
    }

    public abstract static class Entry<E extends ScrollableWidgetList.Entry<E>> extends ObjectSelectionList.Entry<E> {

        public List<ScrollableWidgetList.WidgetWrapper> children = new ArrayList();

        public void addScrollingWidget(AbstractWidget e) {
            this.children.add(new ScrollableWidgetList.WidgetWrapper(e));
        }

        @Override
        public void render(@NotNull GuiGraphics guiGraphics, int index, int rowTop, int rowLeft, int rowWidth, int rowHeight, int mouseX, int mouseY, boolean hovered, float partialTicks) {
            this.children.forEach(c -> {
                c.updatePosition(rowLeft, rowTop);
                c.wasOnScreen = true;
                c.widget.visible = true;
                c.widget.render(guiGraphics, mouseX, mouseY, partialTicks);
                c.widget.visible = false;
            });
        }

        public void drawBackground(GuiGraphics guiGraphics, int index, int rowTop, int rowLeft, int rowWidth, int rowHeight, int mouseX, int mouseY, boolean hovered) {
            if (index % 2 == 0) {
                guiGraphics.fill(rowLeft, rowTop, rowLeft + rowWidth, rowTop + rowHeight, 1711276032);
            }
            if (hovered) {
                guiGraphics.fill(rowLeft, rowTop, rowLeft + 1, rowTop + rowHeight, -1);
                guiGraphics.fill(rowLeft + rowWidth - 1, rowTop, rowLeft + rowWidth, rowTop + rowHeight, -1);
                guiGraphics.fill(rowLeft, rowTop, rowLeft + rowWidth, rowTop + 1, -1);
                guiGraphics.fill(rowLeft, rowTop + rowHeight - 1, rowLeft + rowWidth, rowTop + rowHeight, -1);
            }
        }
    }

    public static class WidgetWrapper {

        public final AbstractWidget widget;

        public final int relativeX;

        public final int relativeY;

        public boolean wasOnScreen = false;

        public WidgetWrapper(AbstractWidget widget) {
            this.widget = widget;
            this.relativeX = widget.getX();
            this.relativeY = widget.getY();
        }

        public void updatePosition(int currX, int currY) {
            this.widget.setX(currX + this.relativeX);
            this.widget.setY(currY + this.relativeY);
            this.widget.visible = true;
        }
    }
}