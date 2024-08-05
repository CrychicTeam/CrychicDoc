package fr.frinn.custommachinery.client.screen.widget;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.navigation.ScreenAxis;
import net.minecraft.client.gui.navigation.ScreenDirection;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ListWidget<E extends ListWidget.Entry> extends AbstractWidget implements ContainerEventHandler {

    private final int itemHeight;

    private final List<E> entries = new ArrayList();

    private final Minecraft mc = Minecraft.getInstance();

    private final Font font;

    @Nullable
    private E selected;

    private boolean renderSelection;

    private double scrollAmount;

    private boolean scrolling;

    private boolean dragging;

    public ListWidget(int x, int y, int width, int height, int itemHeight, Component message) {
        super(x, y, width, height, message);
        this.font = this.mc.font;
        this.renderSelection = false;
        this.itemHeight = itemHeight;
    }

    public List<E> getEntries() {
        return ImmutableList.copyOf(this.entries);
    }

    public void addEntry(E entry) {
        this.entries.add(entry);
    }

    public void clear() {
        this.entries.clear();
        this.selected = null;
    }

    @Nullable
    public E getSelected() {
        return this.selected;
    }

    public void setSelected(@Nullable E selected) {
        this.selected = selected;
    }

    @Nullable
    public E getEntryAtPosition(double mouseX, double mouseY) {
        int index = Mth.clamp(Mth.floor(mouseY - (double) this.m_252907_() + this.getScrollAmount() - 4.0), 0, this.getMaxPosition()) / this.itemHeight;
        return (E) (mouseX >= (double) this.m_252754_() && mouseX <= (double) (this.m_252754_() + this.f_93618_) && index >= 0 && index < this.entries.size() ? this.entries.get(index) : null);
    }

    @NotNull
    @Override
    public List<E> children() {
        return this.getEntries();
    }

    public int getMaxPosition() {
        return this.entries.size() * this.itemHeight;
    }

    private void scroll(int scroll) {
        this.setScrollAmount(this.getScrollAmount() + (double) scroll);
    }

    public double getScrollAmount() {
        return this.scrollAmount;
    }

    public void setScrollAmount(double scroll) {
        this.scrollAmount = Mth.clamp(scroll, 0.0, (double) this.getMaxScroll());
    }

    public int getMaxScroll() {
        return Math.max(0, this.getMaxPosition() - this.f_93619_ + 4);
    }

    public void updateScrollingState(double mouseX, double mouseY, int button) {
        this.scrolling = button == 0 && mouseX >= (double) this.getScrollbarPosition() && mouseX < (double) (this.getScrollbarPosition() + 6);
    }

    protected int getScrollbarPosition() {
        return this.m_252754_() + this.f_93618_ - 6;
    }

    public void ensureVisible(E entry) {
        int entryTop = this.m_252907_() + 4 - (int) this.getScrollAmount() + this.entries.indexOf(entry) * this.itemHeight;
        int j = entryTop - this.m_252907_() - 4 - this.itemHeight;
        if (j < 0) {
            this.scroll(j);
        }
        int k = this.m_252907_() + this.f_93619_ - entryTop - this.itemHeight * 2;
        if (k < 0) {
            this.scroll(-k);
        }
    }

    protected void setRenderSelection() {
        this.renderSelection = true;
    }

    protected void renderList(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        for (int index = 0; index < this.entries.size(); index++) {
            int entryTop = this.m_252907_() + 4 - (int) this.getScrollAmount() + index * this.itemHeight;
            int entryBottom = entryTop + this.itemHeight;
            if (entryBottom >= this.m_252907_() && entryTop <= this.m_252907_() + this.f_93619_) {
                this.renderItem(graphics, mouseX, mouseY, partialTick, index, this.m_252754_(), entryTop, this.f_93618_, this.itemHeight - 4);
            }
        }
    }

    protected void renderItem(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, int index, int left, int top, int width, int height) {
        E entry = (E) this.entries.get(index);
        entry.renderBackground(graphics, index, left, top, width, height, mouseX, mouseY, partialTick);
        if (this.renderSelection && this.selected == entry) {
            this.renderSelection(graphics, top, width - 8, height, FastColor.ARGB32.color(255, 0, 0, 0), FastColor.ARGB32.color(255, 198, 198, 198));
        }
        entry.render(graphics, index, left, top, width, height, mouseX, mouseY, partialTick);
        for (Object children : entry.m_6702_()) {
            if (children instanceof Renderable renderable) {
                renderable.render(graphics, mouseX, mouseY, partialTick);
            }
        }
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int barLeft = this.getScrollbarPosition();
        int barRight = barLeft + 6;
        graphics.enableScissor(this.m_252754_(), this.m_252907_(), this.m_252754_() + this.f_93618_, this.m_252907_() + this.f_93619_);
        this.renderList(graphics, mouseX, mouseY, partialTick);
        graphics.disableScissor();
        if (this.getMaxScroll() > 0) {
            int n = (int) ((float) (this.f_93619_ * this.f_93619_) / (float) this.getMaxPosition());
            n = Mth.clamp(n, 32, this.f_93619_ - 8);
            int o = (int) this.getScrollAmount() * (this.f_93619_ - n) / this.getMaxScroll() + this.m_252907_();
            if (o < this.m_252907_()) {
                o = this.m_252907_();
            }
            graphics.fill(barLeft, this.m_252907_(), barRight, this.m_252907_() + this.f_93619_, -16777216);
            graphics.fill(barLeft, o, barRight, o + n, -8355712);
            graphics.fill(barLeft, o, barRight - 1, o + n - 1, -4144960);
        }
        RenderSystem.disableBlend();
    }

    protected void renderSelection(GuiGraphics guiGraphics, int top, int width, int height, int outerColor, int innerColor) {
        guiGraphics.fill(this.m_252754_(), top - 2, this.m_252754_() + width, top + height + 2, outerColor);
        guiGraphics.fill(this.m_252754_() + 1, top - 1, this.m_252754_() + width - 1, top + height + 1, innerColor);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.updateScrollingState(mouseX, mouseY, button);
        if (!this.m_5953_(mouseX, mouseY)) {
            return false;
        } else {
            E entry = this.getEntryAtPosition(mouseX, mouseY);
            if (entry != null && entry.m_6375_(mouseX, mouseY, button)) {
                GuiEventListener focused = this.getFocused();
                if (focused != entry && focused instanceof ContainerEventHandler container) {
                    container.setFocused(null);
                }
                this.setFocused(entry);
                this.setDragging(true);
                return true;
            } else {
                return this.scrolling;
            }
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (this.getFocused() != null) {
            this.getFocused().mouseReleased(mouseX, mouseY, button);
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        if (this.getFocused() != null) {
            this.getFocused().mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }
        if (button == 0 && this.scrolling) {
            if (mouseY < (double) this.m_252907_()) {
                this.setScrollAmount(0.0);
            } else if (mouseY > (double) (this.m_252907_() + this.f_93619_)) {
                this.setScrollAmount((double) this.getMaxScroll());
            } else {
                double d = (double) Math.max(1, this.getMaxScroll());
                int i = this.f_93619_;
                int j = Mth.clamp((int) ((float) (i * i) / (float) this.getMaxPosition()), 32, i - 8);
                double e = Math.max(1.0, d / (double) (i - j));
                this.setScrollAmount(this.getScrollAmount() + dragY * e);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        this.setScrollAmount(this.getScrollAmount() - delta * (double) this.itemHeight / 2.0);
        return true;
    }

    @Nullable
    @Override
    public ComponentPath nextFocusPath(FocusNavigationEvent event) {
        if (this.entries.isEmpty()) {
            return null;
        } else if (event instanceof FocusNavigationEvent.ArrowNavigation arrowNavigation) {
            E entry = this.selected;
            if (arrowNavigation.direction().getAxis() == ScreenAxis.HORIZONTAL && entry != null) {
                return ComponentPath.path(this, entry.nextFocusPath(event));
            } else {
                int i = -1;
                ScreenDirection screenDirection = arrowNavigation.direction();
                if (entry != null) {
                    i = entry.m_6702_().indexOf(entry.getFocused());
                }
                if (i == -1) {
                    switch(screenDirection) {
                        case LEFT:
                            i = Integer.MAX_VALUE;
                            screenDirection = ScreenDirection.DOWN;
                            break;
                        case RIGHT:
                            i = 0;
                            screenDirection = ScreenDirection.DOWN;
                            break;
                        default:
                            i = 0;
                    }
                }
                E entry2 = entry;
                while ((entry2 = this.nextEntry(screenDirection, arg -> !arg.m_6702_().isEmpty(), entry2)) != null) {
                    ComponentPath componentPath;
                    if ((componentPath = entry2.focusPathAtIndex(arrowNavigation, i)) != null) {
                        return ComponentPath.path(this, componentPath);
                    }
                }
                return null;
            }
        } else {
            return super.nextFocusPath(event);
        }
    }

    @Nullable
    protected E nextEntry(ScreenDirection direction, Predicate<E> predicate, @Nullable E selected) {
        byte b0 = switch(direction) {
            case LEFT, RIGHT ->
                0;
            case UP ->
                -1;
            case DOWN ->
                1;
        };
        if (!this.children().isEmpty() && b0 != 0) {
            int j;
            if (selected == null) {
                j = b0 > 0 ? 0 : this.children().size() - 1;
            } else {
                j = this.children().indexOf(selected) + b0;
            }
            for (int k = j; k >= 0 && k < this.entries.size(); k += b0) {
                E e = (E) this.children().get(k);
                if (predicate.test(e)) {
                    return e;
                }
            }
        }
        return null;
    }

    @Override
    public void setFocused(@Nullable GuiEventListener focused) {
        int i = this.entries.indexOf(focused);
        if (i >= 0) {
            E entry = (E) this.entries.get(i);
            this.setSelected(entry);
            if (this.mc.getLastInputType().isKeyboard()) {
                this.ensureVisible(entry);
            }
            entry.m_93692_(true);
        } else {
            this.setSelected(null);
        }
    }

    @Nullable
    @Override
    public GuiEventListener getFocused() {
        return this.selected;
    }

    @Override
    public void setDragging(boolean isDragging) {
        this.dragging = isDragging;
    }

    @Override
    public boolean isDragging() {
        return this.dragging;
    }

    public abstract static class Entry implements ContainerEventHandler {

        @Nullable
        private GuiEventListener focused;

        private boolean dragging;

        protected void renderBackground(GuiGraphics graphics, int index, int x, int y, int width, int height, int mouseX, int mouseY, float partialTicks) {
        }

        protected abstract void render(GuiGraphics var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, float var9);

        @Override
        public boolean isDragging() {
            return this.dragging;
        }

        @Override
        public void setDragging(boolean isDragging) {
            this.dragging = isDragging;
        }

        @Nullable
        @Override
        public GuiEventListener getFocused() {
            return this.focused;
        }

        @Override
        public void setFocused(@Nullable GuiEventListener focused) {
            if (this.focused != null) {
                this.focused.setFocused(false);
            }
            if (focused != null) {
                focused.setFocused(true);
            }
            this.focused = focused;
        }

        @Nullable
        public ComponentPath focusPathAtIndex(FocusNavigationEvent event, int index) {
            if (this.m_6702_().isEmpty()) {
                return null;
            } else {
                ComponentPath componentPath = ((GuiEventListener) this.m_6702_().get(Math.min(index, this.m_6702_().size() - 1))).nextFocusPath(event);
                return ComponentPath.path(this, componentPath);
            }
        }

        @Nullable
        @Override
        public ComponentPath nextFocusPath(FocusNavigationEvent event) {
            if (event instanceof FocusNavigationEvent.ArrowNavigation arrowNavigation) {
                int i = arrowNavigation.direction() == ScreenDirection.RIGHT ? 1 : 0;
                if (i == 0) {
                    return null;
                }
                for (int k = Mth.clamp(i + this.m_6702_().indexOf(this.getFocused()), 0, this.m_6702_().size() - 1); k >= 0 && k < this.m_6702_().size(); k += i) {
                    GuiEventListener guiEventListener = (GuiEventListener) this.m_6702_().get(k);
                    ComponentPath componentPath = guiEventListener.nextFocusPath(event);
                    if (componentPath != null) {
                        return ComponentPath.path(this, componentPath);
                    }
                }
            }
            return ContainerEventHandler.super.nextFocusPath(event);
        }
    }
}