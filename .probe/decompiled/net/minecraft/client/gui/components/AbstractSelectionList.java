package net.minecraft.client.gui.components;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.ScreenDirection;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public abstract class AbstractSelectionList<E extends AbstractSelectionList.Entry<E>> extends AbstractContainerEventHandler implements Renderable, NarratableEntry {

    protected final Minecraft minecraft;

    protected final int itemHeight;

    private final List<E> children = new AbstractSelectionList.TrackedList();

    protected int width;

    protected int height;

    protected int y0;

    protected int y1;

    protected int x1;

    protected int x0;

    protected boolean centerListVertically = true;

    private double scrollAmount;

    private boolean renderSelection = true;

    private boolean renderHeader;

    protected int headerHeight;

    private boolean scrolling;

    @Nullable
    private E selected;

    private boolean renderBackground = true;

    private boolean renderTopAndBottom = true;

    @Nullable
    private E hovered;

    public AbstractSelectionList(Minecraft minecraft0, int int1, int int2, int int3, int int4, int int5) {
        this.minecraft = minecraft0;
        this.width = int1;
        this.height = int2;
        this.y0 = int3;
        this.y1 = int4;
        this.itemHeight = int5;
        this.x0 = 0;
        this.x1 = int1;
    }

    public void setRenderSelection(boolean boolean0) {
        this.renderSelection = boolean0;
    }

    protected void setRenderHeader(boolean boolean0, int int1) {
        this.renderHeader = boolean0;
        this.headerHeight = int1;
        if (!boolean0) {
            this.headerHeight = 0;
        }
    }

    public int getRowWidth() {
        return 220;
    }

    @Nullable
    public E getSelected() {
        return this.selected;
    }

    public void setSelected(@Nullable E e0) {
        this.selected = e0;
    }

    public E getFirstElement() {
        return (E) this.children.get(0);
    }

    public void setRenderBackground(boolean boolean0) {
        this.renderBackground = boolean0;
    }

    public void setRenderTopAndBottom(boolean boolean0) {
        this.renderTopAndBottom = boolean0;
    }

    @Nullable
    public E getFocused() {
        return (E) super.getFocused();
    }

    @Override
    public final List<E> children() {
        return this.children;
    }

    protected void clearEntries() {
        this.children.clear();
        this.selected = null;
    }

    protected void replaceEntries(Collection<E> collectionE0) {
        this.clearEntries();
        this.children.addAll(collectionE0);
    }

    protected E getEntry(int int0) {
        return (E) this.children().get(int0);
    }

    protected int addEntry(E e0) {
        this.children.add(e0);
        return this.children.size() - 1;
    }

    protected void addEntryToTop(E e0) {
        double $$1 = (double) this.getMaxScroll() - this.getScrollAmount();
        this.children.add(0, e0);
        this.setScrollAmount((double) this.getMaxScroll() - $$1);
    }

    protected boolean removeEntryFromTop(E e0) {
        double $$1 = (double) this.getMaxScroll() - this.getScrollAmount();
        boolean $$2 = this.removeEntry(e0);
        this.setScrollAmount((double) this.getMaxScroll() - $$1);
        return $$2;
    }

    protected int getItemCount() {
        return this.children().size();
    }

    protected boolean isSelectedItem(int int0) {
        return Objects.equals(this.getSelected(), this.children().get(int0));
    }

    @Nullable
    protected final E getEntryAtPosition(double double0, double double1) {
        int $$2 = this.getRowWidth() / 2;
        int $$3 = this.x0 + this.width / 2;
        int $$4 = $$3 - $$2;
        int $$5 = $$3 + $$2;
        int $$6 = Mth.floor(double1 - (double) this.y0) - this.headerHeight + (int) this.getScrollAmount() - 4;
        int $$7 = $$6 / this.itemHeight;
        return (E) (double0 < (double) this.getScrollbarPosition() && double0 >= (double) $$4 && double0 <= (double) $$5 && $$7 >= 0 && $$6 >= 0 && $$7 < this.getItemCount() ? this.children().get($$7) : null);
    }

    public void updateSize(int int0, int int1, int int2, int int3) {
        this.width = int0;
        this.height = int1;
        this.y0 = int2;
        this.y1 = int3;
        this.x0 = 0;
        this.x1 = int0;
    }

    public void setLeftPos(int int0) {
        this.x0 = int0;
        this.x1 = int0 + this.width;
    }

    protected int getMaxPosition() {
        return this.getItemCount() * this.itemHeight + this.headerHeight;
    }

    protected void clickedHeader(int int0, int int1) {
    }

    protected void renderHeader(GuiGraphics guiGraphics0, int int1, int int2) {
    }

    protected void renderBackground(GuiGraphics guiGraphics0) {
    }

    protected void renderDecorations(GuiGraphics guiGraphics0, int int1, int int2) {
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.renderBackground(guiGraphics0);
        int $$4 = this.getScrollbarPosition();
        int $$5 = $$4 + 6;
        this.hovered = this.isMouseOver((double) int1, (double) int2) ? this.getEntryAtPosition((double) int1, (double) int2) : null;
        if (this.renderBackground) {
            guiGraphics0.setColor(0.125F, 0.125F, 0.125F, 1.0F);
            int $$6 = 32;
            guiGraphics0.blit(Screen.BACKGROUND_LOCATION, this.x0, this.y0, (float) this.x1, (float) (this.y1 + (int) this.getScrollAmount()), this.x1 - this.x0, this.y1 - this.y0, 32, 32);
            guiGraphics0.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
        int $$7 = this.getRowLeft();
        int $$8 = this.y0 + 4 - (int) this.getScrollAmount();
        this.enableScissor(guiGraphics0);
        if (this.renderHeader) {
            this.renderHeader(guiGraphics0, $$7, $$8);
        }
        this.renderList(guiGraphics0, int1, int2, float3);
        guiGraphics0.disableScissor();
        if (this.renderTopAndBottom) {
            int $$9 = 32;
            guiGraphics0.setColor(0.25F, 0.25F, 0.25F, 1.0F);
            guiGraphics0.blit(Screen.BACKGROUND_LOCATION, this.x0, 0, 0.0F, 0.0F, this.width, this.y0, 32, 32);
            guiGraphics0.blit(Screen.BACKGROUND_LOCATION, this.x0, this.y1, 0.0F, (float) this.y1, this.width, this.height - this.y1, 32, 32);
            guiGraphics0.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            int $$10 = 4;
            guiGraphics0.fillGradient(RenderType.guiOverlay(), this.x0, this.y0, this.x1, this.y0 + 4, -16777216, 0, 0);
            guiGraphics0.fillGradient(RenderType.guiOverlay(), this.x0, this.y1 - 4, this.x1, this.y1, 0, -16777216, 0);
        }
        int $$11 = this.getMaxScroll();
        if ($$11 > 0) {
            int $$12 = (int) ((float) ((this.y1 - this.y0) * (this.y1 - this.y0)) / (float) this.getMaxPosition());
            $$12 = Mth.clamp($$12, 32, this.y1 - this.y0 - 8);
            int $$13 = (int) this.getScrollAmount() * (this.y1 - this.y0 - $$12) / $$11 + this.y0;
            if ($$13 < this.y0) {
                $$13 = this.y0;
            }
            guiGraphics0.fill($$4, this.y0, $$5, this.y1, -16777216);
            guiGraphics0.fill($$4, $$13, $$5, $$13 + $$12, -8355712);
            guiGraphics0.fill($$4, $$13, $$5 - 1, $$13 + $$12 - 1, -4144960);
        }
        this.renderDecorations(guiGraphics0, int1, int2);
        RenderSystem.disableBlend();
    }

    protected void enableScissor(GuiGraphics guiGraphics0) {
        guiGraphics0.enableScissor(this.x0, this.y0, this.x1, this.y1);
    }

    protected void centerScrollOn(E e0) {
        this.setScrollAmount((double) (this.children().indexOf(e0) * this.itemHeight + this.itemHeight / 2 - (this.y1 - this.y0) / 2));
    }

    protected void ensureVisible(E e0) {
        int $$1 = this.getRowTop(this.children().indexOf(e0));
        int $$2 = $$1 - this.y0 - 4 - this.itemHeight;
        if ($$2 < 0) {
            this.scroll($$2);
        }
        int $$3 = this.y1 - $$1 - this.itemHeight - this.itemHeight;
        if ($$3 < 0) {
            this.scroll(-$$3);
        }
    }

    private void scroll(int int0) {
        this.setScrollAmount(this.getScrollAmount() + (double) int0);
    }

    public double getScrollAmount() {
        return this.scrollAmount;
    }

    public void setScrollAmount(double double0) {
        this.scrollAmount = Mth.clamp(double0, 0.0, (double) this.getMaxScroll());
    }

    public int getMaxScroll() {
        return Math.max(0, this.getMaxPosition() - (this.y1 - this.y0 - 4));
    }

    public int getScrollBottom() {
        return (int) this.getScrollAmount() - this.height - this.headerHeight;
    }

    protected void updateScrollingState(double double0, double double1, int int2) {
        this.scrolling = int2 == 0 && double0 >= (double) this.getScrollbarPosition() && double0 < (double) (this.getScrollbarPosition() + 6);
    }

    protected int getScrollbarPosition() {
        return this.width / 2 + 124;
    }

    @Override
    public boolean mouseClicked(double double0, double double1, int int2) {
        this.updateScrollingState(double0, double1, int2);
        if (!this.isMouseOver(double0, double1)) {
            return false;
        } else {
            E $$3 = this.getEntryAtPosition(double0, double1);
            if ($$3 != null) {
                if ($$3.m_6375_(double0, double1, int2)) {
                    E $$4 = this.getFocused();
                    if ($$4 != $$3 && $$4 instanceof ContainerEventHandler $$5) {
                        $$5.setFocused(null);
                    }
                    this.setFocused($$3);
                    this.m_7897_(true);
                    return true;
                }
            } else if (int2 == 0) {
                this.clickedHeader((int) (double0 - (double) (this.x0 + this.width / 2 - this.getRowWidth() / 2)), (int) (double1 - (double) this.y0) + (int) this.getScrollAmount() - 4);
                return true;
            }
            return this.scrolling;
        }
    }

    @Override
    public boolean mouseReleased(double double0, double double1, int int2) {
        if (this.getFocused() != null) {
            this.getFocused().m_6348_(double0, double1, int2);
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double double0, double double1, int int2, double double3, double double4) {
        if (super.m_7979_(double0, double1, int2, double3, double4)) {
            return true;
        } else if (int2 == 0 && this.scrolling) {
            if (double1 < (double) this.y0) {
                this.setScrollAmount(0.0);
            } else if (double1 > (double) this.y1) {
                this.setScrollAmount((double) this.getMaxScroll());
            } else {
                double $$5 = (double) Math.max(1, this.getMaxScroll());
                int $$6 = this.y1 - this.y0;
                int $$7 = Mth.clamp((int) ((float) ($$6 * $$6) / (float) this.getMaxPosition()), 32, $$6 - 8);
                double $$8 = Math.max(1.0, $$5 / (double) ($$6 - $$7));
                this.setScrollAmount(this.getScrollAmount() + double4 * $$8);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean mouseScrolled(double double0, double double1, double double2) {
        this.setScrollAmount(this.getScrollAmount() - double2 * (double) this.itemHeight / 2.0);
        return true;
    }

    @Override
    public void setFocused(@Nullable GuiEventListener guiEventListener0) {
        super.setFocused(guiEventListener0);
        int $$1 = this.children.indexOf(guiEventListener0);
        if ($$1 >= 0) {
            E $$2 = (E) this.children.get($$1);
            this.setSelected($$2);
            if (this.minecraft.getLastInputType().isKeyboard()) {
                this.ensureVisible($$2);
            }
        }
    }

    @Nullable
    protected E nextEntry(ScreenDirection screenDirection0) {
        return this.nextEntry(screenDirection0, p_93510_ -> true);
    }

    @Nullable
    protected E nextEntry(ScreenDirection screenDirection0, Predicate<E> predicateE1) {
        return this.nextEntry(screenDirection0, predicateE1, this.getSelected());
    }

    @Nullable
    protected E nextEntry(ScreenDirection screenDirection0, Predicate<E> predicateE1, @Nullable E e2) {
        int $$3 = switch(screenDirection0) {
            case RIGHT, LEFT ->
                0;
            case UP ->
                -1;
            case DOWN ->
                1;
        };
        if (!this.children().isEmpty() && $$3 != 0) {
            int $$4;
            if (e2 == null) {
                $$4 = $$3 > 0 ? 0 : this.children().size() - 1;
            } else {
                $$4 = this.children().indexOf(e2) + $$3;
            }
            for (int $$6 = $$4; $$6 >= 0 && $$6 < this.children.size(); $$6 += $$3) {
                E $$7 = (E) this.children().get($$6);
                if (predicateE1.test($$7)) {
                    return $$7;
                }
            }
        }
        return null;
    }

    @Override
    public boolean isMouseOver(double double0, double double1) {
        return double1 >= (double) this.y0 && double1 <= (double) this.y1 && double0 >= (double) this.x0 && double0 <= (double) this.x1;
    }

    protected void renderList(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        int $$4 = this.getRowLeft();
        int $$5 = this.getRowWidth();
        int $$6 = this.itemHeight - 4;
        int $$7 = this.getItemCount();
        for (int $$8 = 0; $$8 < $$7; $$8++) {
            int $$9 = this.getRowTop($$8);
            int $$10 = this.getRowBottom($$8);
            if ($$10 >= this.y0 && $$9 <= this.y1) {
                this.renderItem(guiGraphics0, int1, int2, float3, $$8, $$4, $$9, $$5, $$6);
            }
        }
    }

    protected void renderItem(GuiGraphics guiGraphics0, int int1, int int2, float float3, int int4, int int5, int int6, int int7, int int8) {
        E $$9 = this.getEntry(int4);
        $$9.renderBack(guiGraphics0, int4, int6, int5, int7, int8, int1, int2, Objects.equals(this.hovered, $$9), float3);
        if (this.renderSelection && this.isSelectedItem(int4)) {
            int $$10 = this.m_93696_() ? -1 : -8355712;
            this.renderSelection(guiGraphics0, int6, int7, int8, $$10, -16777216);
        }
        $$9.render(guiGraphics0, int4, int6, int5, int7, int8, int1, int2, Objects.equals(this.hovered, $$9), float3);
    }

    protected void renderSelection(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, int int5) {
        int $$6 = this.x0 + (this.width - int2) / 2;
        int $$7 = this.x0 + (this.width + int2) / 2;
        guiGraphics0.fill($$6, int1 - 2, $$7, int1 + int3 + 2, int4);
        guiGraphics0.fill($$6 + 1, int1 - 1, $$7 - 1, int1 + int3 + 1, int5);
    }

    public int getRowLeft() {
        return this.x0 + this.width / 2 - this.getRowWidth() / 2 + 2;
    }

    public int getRowRight() {
        return this.getRowLeft() + this.getRowWidth();
    }

    protected int getRowTop(int int0) {
        return this.y0 + 4 - (int) this.getScrollAmount() + int0 * this.itemHeight + this.headerHeight;
    }

    protected int getRowBottom(int int0) {
        return this.getRowTop(int0) + this.itemHeight;
    }

    @Override
    public NarratableEntry.NarrationPriority narrationPriority() {
        if (this.m_93696_()) {
            return NarratableEntry.NarrationPriority.FOCUSED;
        } else {
            return this.hovered != null ? NarratableEntry.NarrationPriority.HOVERED : NarratableEntry.NarrationPriority.NONE;
        }
    }

    @Nullable
    protected E remove(int int0) {
        E $$1 = (E) this.children.get(int0);
        return this.removeEntry((E) this.children.get(int0)) ? $$1 : null;
    }

    protected boolean removeEntry(E e0) {
        boolean $$1 = this.children.remove(e0);
        if ($$1 && e0 == this.getSelected()) {
            this.setSelected(null);
        }
        return $$1;
    }

    @Nullable
    protected E getHovered() {
        return this.hovered;
    }

    void bindEntryToSelf(AbstractSelectionList.Entry<E> abstractSelectionListEntryE0) {
        abstractSelectionListEntryE0.list = this;
    }

    protected void narrateListElementPosition(NarrationElementOutput narrationElementOutput0, E e1) {
        List<E> $$2 = this.children();
        if ($$2.size() > 1) {
            int $$3 = $$2.indexOf(e1);
            if ($$3 != -1) {
                narrationElementOutput0.add(NarratedElementType.POSITION, Component.translatable("narrator.position.list", $$3 + 1, $$2.size()));
            }
        }
    }

    @Override
    public ScreenRectangle getRectangle() {
        return new ScreenRectangle(this.x0, this.y0, this.x1 - this.x0, this.y1 - this.y0);
    }

    protected abstract static class Entry<E extends AbstractSelectionList.Entry<E>> implements GuiEventListener {

        @Deprecated
        AbstractSelectionList<E> list;

        @Override
        public void setFocused(boolean boolean0) {
        }

        @Override
        public boolean isFocused() {
            return this.list.getFocused() == this;
        }

        public abstract void render(GuiGraphics var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, boolean var9, float var10);

        public void renderBack(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, boolean boolean8, float float9) {
        }

        @Override
        public boolean isMouseOver(double double0, double double1) {
            return Objects.equals(this.list.getEntryAtPosition(double0, double1), this);
        }
    }

    class TrackedList extends AbstractList<E> {

        private final List<E> delegate = Lists.newArrayList();

        public E get(int int0) {
            return (E) this.delegate.get(int0);
        }

        public int size() {
            return this.delegate.size();
        }

        public E set(int int0, E e1) {
            E $$2 = (E) this.delegate.set(int0, e1);
            AbstractSelectionList.this.bindEntryToSelf(e1);
            return $$2;
        }

        public void add(int int0, E e1) {
            this.delegate.add(int0, e1);
            AbstractSelectionList.this.bindEntryToSelf(e1);
        }

        public E remove(int int0) {
            return (E) this.delegate.remove(int0);
        }
    }
}