package me.shedaniel.clothconfig2.gui.entries;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import me.shedaniel.clothconfig2.ClothConfigInitializer;
import me.shedaniel.clothconfig2.api.ScissorsHandler;
import me.shedaniel.clothconfig2.api.ScrollingContainer;
import me.shedaniel.math.Rectangle;
import me.shedaniel.math.impl.PointHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

@OnlyIn(Dist.CLIENT)
public class DropdownBoxEntry<T> extends TooltipListEntry<T> {

    protected Button resetButton;

    protected DropdownBoxEntry.SelectionElement<T> selectionElement;

    @NotNull
    private final Supplier<T> defaultValue;

    private boolean suggestionMode = true;

    @Deprecated
    @Internal
    public DropdownBoxEntry(Component fieldName, @NotNull Component resetButtonKey, @Nullable Supplier<Optional<Component[]>> tooltipSupplier, boolean requiresRestart, @Nullable Supplier<T> defaultValue, @Nullable Consumer<T> saveConsumer, @Nullable Iterable<T> selections, @NotNull DropdownBoxEntry.SelectionTopCellElement<T> topRenderer, @NotNull DropdownBoxEntry.SelectionCellCreator<T> cellCreator) {
        super(fieldName, tooltipSupplier, requiresRestart);
        this.defaultValue = defaultValue;
        this.saveCallback = saveConsumer;
        this.resetButton = Button.builder(resetButtonKey, widget -> this.selectionElement.topRenderer.setValue((T) defaultValue.get())).bounds(0, 0, Minecraft.getInstance().font.width(resetButtonKey) + 6, 20).build();
        this.selectionElement = new DropdownBoxEntry.SelectionElement<>(this, new Rectangle(0, 0, 150, 20), new DropdownBoxEntry.DefaultDropdownMenuElement<>(selections == null ? ImmutableList.of() : ImmutableList.copyOf(selections)), topRenderer, cellCreator);
    }

    @Override
    public void render(GuiGraphics graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta) {
        super.render(graphics, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);
        Window window = Minecraft.getInstance().getWindow();
        this.resetButton.f_93623_ = this.isEditable() && this.getDefaultValue().isPresent() && (!this.defaultValue.get().equals(this.getValue()) || this.getConfigError().isPresent());
        this.resetButton.m_253211_(y);
        this.selectionElement.active = this.isEditable();
        this.selectionElement.bounds.y = y;
        Component displayedFieldName = this.getDisplayedFieldName();
        if (Minecraft.getInstance().font.isBidirectional()) {
            graphics.drawString(Minecraft.getInstance().font, displayedFieldName.getVisualOrderText(), window.getGuiScaledWidth() - x - Minecraft.getInstance().font.width(displayedFieldName), y + 6, this.getPreferredTextColor());
            this.resetButton.m_252865_(x);
            this.selectionElement.bounds.x = x + this.resetButton.m_5711_() + 1;
        } else {
            graphics.drawString(Minecraft.getInstance().font, displayedFieldName.getVisualOrderText(), x, y + 6, this.getPreferredTextColor());
            this.resetButton.m_252865_(x + entryWidth - this.resetButton.m_5711_());
            this.selectionElement.bounds.x = x + entryWidth - 150 + 1;
        }
        this.selectionElement.bounds.width = 150 - this.resetButton.m_5711_() - 4;
        this.resetButton.m_88315_(graphics, mouseX, mouseY, delta);
        this.selectionElement.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    public boolean isEdited() {
        return this.selectionElement.topRenderer.isEdited();
    }

    public boolean isSuggestionMode() {
        return this.suggestionMode;
    }

    public void setSuggestionMode(boolean suggestionMode) {
        this.suggestionMode = suggestionMode;
    }

    @Override
    public void updateSelected(boolean isSelected) {
        this.selectionElement.topRenderer.isSelected = isSelected;
        this.selectionElement.menu.isSelected = isSelected;
    }

    @NotNull
    public ImmutableList<T> getSelections() {
        return this.selectionElement.menu.getSelections();
    }

    @Override
    public T getValue() {
        return this.selectionElement.getValue();
    }

    @Deprecated
    public DropdownBoxEntry.SelectionElement<T> getSelectionElement() {
        return this.selectionElement;
    }

    @Override
    public Optional<T> getDefaultValue() {
        return this.defaultValue == null ? Optional.empty() : Optional.ofNullable(this.defaultValue.get());
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return Lists.newArrayList(new GuiEventListener[] { this.selectionElement, this.resetButton });
    }

    @Override
    public List<? extends NarratableEntry> narratables() {
        return Collections.singletonList(this.resetButton);
    }

    @Override
    public Optional<Component> getError() {
        return this.selectionElement.topRenderer.getError();
    }

    @Override
    public void lateRender(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.selectionElement.lateRender(graphics, mouseX, mouseY, delta);
    }

    @Override
    public int getMorePossibleHeight() {
        return this.selectionElement.getMorePossibleHeight();
    }

    @Override
    public boolean mouseScrolled(double double_1, double double_2, double double_3) {
        return this.selectionElement.mouseScrolled(double_1, double_2, double_3);
    }

    public static class DefaultDropdownMenuElement<R> extends DropdownBoxEntry.DropdownMenuElement<R> {

        @NotNull
        protected ImmutableList<R> selections;

        @NotNull
        protected List<DropdownBoxEntry.SelectionCellElement<R>> cells;

        @NotNull
        protected List<DropdownBoxEntry.SelectionCellElement<R>> currentElements;

        protected Component lastSearchKeyword = Component.empty();

        protected Rectangle lastRectangle;

        protected boolean scrolling;

        protected double scroll;

        protected double target;

        protected long start;

        protected long duration;

        public DefaultDropdownMenuElement(@NotNull ImmutableList<R> selections) {
            this.selections = selections;
            this.cells = Lists.newArrayList();
            this.currentElements = Lists.newArrayList();
        }

        public double getMaxScroll() {
            return (double) (this.getCellCreator().getCellHeight() * this.currentElements.size());
        }

        protected double getMaxScrollPosition() {
            return Math.max(0.0, this.getMaxScroll() - (double) this.getHeight());
        }

        @NotNull
        @Override
        public ImmutableList<R> getSelections() {
            return this.selections;
        }

        @Override
        public void initCells() {
            UnmodifiableIterator var1 = this.getSelections().iterator();
            while (var1.hasNext()) {
                R selection = (R) var1.next();
                this.cells.add(this.getCellCreator().create(selection));
            }
            for (DropdownBoxEntry.SelectionCellElement<R> cell : this.cells) {
                cell.entry = this.getEntry();
            }
            this.search();
        }

        public void search() {
            if (this.isSuggestionMode()) {
                this.currentElements.clear();
                String keyword = this.lastSearchKeyword.getString().toLowerCase();
                for (DropdownBoxEntry.SelectionCellElement<R> cell : this.cells) {
                    Component key = cell.getSearchKey();
                    if (key == null || key.getString().toLowerCase().contains(keyword)) {
                        this.currentElements.add(cell);
                    }
                }
                if (!keyword.isEmpty()) {
                    Comparator<DropdownBoxEntry.SelectionCellElement<?>> c = Comparator.comparingDouble(i -> i.getSearchKey() == null ? Double.MAX_VALUE : this.similarity(i.getSearchKey().getString(), keyword));
                    this.currentElements.sort(c.reversed());
                }
                this.scrollTo(0.0, false);
            } else {
                this.currentElements.clear();
                this.currentElements.addAll(this.cells);
            }
        }

        protected int editDistance(String s1, String s2) {
            s1 = s1.toLowerCase();
            s2 = s2.toLowerCase();
            int[] costs = new int[s2.length() + 1];
            for (int i = 0; i <= s1.length(); i++) {
                int lastValue = i;
                for (int j = 0; j <= s2.length(); j++) {
                    if (i == 0) {
                        costs[j] = j;
                    } else if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
                            newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
                        }
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
                if (i > 0) {
                    costs[s2.length()] = lastValue;
                }
            }
            return costs[s2.length()];
        }

        protected double similarity(String s1, String s2) {
            String longer = s1;
            String shorter = s2;
            if (s1.length() < s2.length()) {
                longer = s2;
                shorter = s1;
            }
            int longerLength = longer.length();
            return longerLength == 0 ? 1.0 : (double) (longerLength - this.editDistance(longer, shorter)) / (double) longerLength;
        }

        @Override
        public void render(GuiGraphics graphics, int mouseX, int mouseY, Rectangle rectangle, float delta) {
            if (!this.getEntry().selectionElement.topRenderer.getSearchTerm().equals(this.lastSearchKeyword)) {
                this.lastSearchKeyword = this.getEntry().selectionElement.topRenderer.getSearchTerm();
                this.search();
            }
            this.updatePosition(delta);
            this.lastRectangle = rectangle.clone();
            this.lastRectangle.translate(0, -1);
        }

        private void updatePosition(float delta) {
            double[] target = new double[] { this.target };
            this.scroll = ScrollingContainer.handleScrollingPosition(target, this.scroll, this.getMaxScrollPosition(), delta, (double) this.start, (double) this.duration);
            this.target = target[0];
        }

        @Override
        public void lateRender(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
            int last10Height = this.getHeight();
            int cWidth = this.getCellCreator().getCellWidth();
            graphics.fill(this.lastRectangle.x, this.lastRectangle.y + this.lastRectangle.height, this.lastRectangle.x + cWidth, this.lastRectangle.y + this.lastRectangle.height + last10Height + 1, this.isExpanded() ? -1 : -6250336);
            graphics.fill(this.lastRectangle.x + 1, this.lastRectangle.y + this.lastRectangle.height + 1, this.lastRectangle.x + cWidth - 1, this.lastRectangle.y + this.lastRectangle.height + last10Height, -16777216);
            graphics.pose().pushPose();
            graphics.pose().translate(0.0F, 0.0F, 300.0F);
            ScissorsHandler.INSTANCE.scissor(new Rectangle(this.lastRectangle.x, this.lastRectangle.y + this.lastRectangle.height + 1, cWidth - 6, last10Height - 1));
            double yy = (double) (this.lastRectangle.y + this.lastRectangle.height) - this.scroll;
            for (DropdownBoxEntry.SelectionCellElement<R> cell : this.currentElements) {
                if (yy + (double) this.getCellCreator().getCellHeight() >= (double) (this.lastRectangle.y + this.lastRectangle.height) && yy <= (double) (this.lastRectangle.y + this.lastRectangle.height + last10Height + 1)) {
                    cell.render(graphics, mouseX, mouseY, this.lastRectangle.x, (int) yy, this.getMaxScrollPosition() > 6.0 ? this.getCellCreator().getCellWidth() - 6 : this.getCellCreator().getCellWidth(), this.getCellCreator().getCellHeight(), delta);
                } else {
                    cell.dontRender(graphics, delta);
                }
                yy += (double) this.getCellCreator().getCellHeight();
            }
            ScissorsHandler.INSTANCE.removeLastScissor();
            if (this.currentElements.isEmpty()) {
                Font textRenderer = Minecraft.getInstance().font;
                Component text = Component.translatable("text.cloth-config.dropdown.value.unknown");
                graphics.drawString(textRenderer, text.getVisualOrderText(), (int) ((float) this.lastRectangle.x + (float) this.getCellCreator().getCellWidth() / 2.0F - (float) textRenderer.width(text) / 2.0F), this.lastRectangle.y + this.lastRectangle.height + 3, -1);
            }
            if (this.getMaxScrollPosition() > 6.0) {
                RenderSystem.setShader(GameRenderer::m_172820_);
                int scrollbarPositionMinX = this.lastRectangle.x + this.getCellCreator().getCellWidth() - 6;
                int scrollbarPositionMaxX = scrollbarPositionMinX + 6;
                int height = (int) ((double) (last10Height * last10Height) / this.getMaxScrollPosition());
                height = Mth.clamp(height, 32, last10Height - 8);
                height = (int) ((double) height - Math.min(this.scroll < 0.0 ? (double) ((int) (-this.scroll)) : (this.scroll > this.getMaxScrollPosition() ? (double) ((int) this.scroll) - this.getMaxScrollPosition() : 0.0), (double) height * 0.95));
                height = Math.max(10, height);
                int minY = (int) Math.min(Math.max((double) ((int) this.scroll * (last10Height - height)) / this.getMaxScrollPosition() + (double) (this.lastRectangle.y + this.lastRectangle.height + 1), (double) (this.lastRectangle.y + this.lastRectangle.height + 1)), (double) (this.lastRectangle.y + this.lastRectangle.height + 1 + last10Height - height));
                int bottomc = new Rectangle(scrollbarPositionMinX, minY, scrollbarPositionMaxX - scrollbarPositionMinX, height).contains(PointHelper.ofMouse()) ? 168 : 128;
                int topc = new Rectangle(scrollbarPositionMinX, minY, scrollbarPositionMaxX - scrollbarPositionMinX, height).contains(PointHelper.ofMouse()) ? 222 : 172;
                Tesselator tesselator = Tesselator.getInstance();
                BufferBuilder buffer = tesselator.getBuilder();
                buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
                buffer.m_5483_((double) scrollbarPositionMinX, (double) (minY + height), 0.0).color(bottomc, bottomc, bottomc, 255).endVertex();
                buffer.m_5483_((double) scrollbarPositionMaxX, (double) (minY + height), 0.0).color(bottomc, bottomc, bottomc, 255).endVertex();
                buffer.m_5483_((double) scrollbarPositionMaxX, (double) minY, 0.0).color(bottomc, bottomc, bottomc, 255).endVertex();
                buffer.m_5483_((double) scrollbarPositionMinX, (double) minY, 0.0).color(bottomc, bottomc, bottomc, 255).endVertex();
                buffer.m_5483_((double) scrollbarPositionMinX, (double) (minY + height - 1), 0.0).color(topc, topc, topc, 255).endVertex();
                buffer.m_5483_((double) (scrollbarPositionMaxX - 1), (double) (minY + height - 1), 0.0).color(topc, topc, topc, 255).endVertex();
                buffer.m_5483_((double) (scrollbarPositionMaxX - 1), (double) minY, 0.0).color(topc, topc, topc, 255).endVertex();
                buffer.m_5483_((double) scrollbarPositionMinX, (double) minY, 0.0).color(topc, topc, topc, 255).endVertex();
                tesselator.end();
            }
            graphics.pose().popPose();
        }

        @Override
        public int getHeight() {
            return Math.max(Math.min(this.getCellCreator().getDropBoxMaxHeight(), (int) this.getMaxScroll()), 14);
        }

        @Override
        public boolean isMouseOver(double mouseX, double mouseY) {
            return this.isExpanded() && mouseX >= (double) this.lastRectangle.x && mouseX <= (double) (this.lastRectangle.x + this.getCellCreator().getCellWidth()) && mouseY >= (double) (this.lastRectangle.y + this.lastRectangle.height) && mouseY <= (double) (this.lastRectangle.y + this.lastRectangle.height + this.getHeight() + 1);
        }

        @Override
        public boolean mouseDragged(double double_1, double double_2, int int_1, double double_3, double double_4) {
            if (!this.isExpanded()) {
                return false;
            } else if (int_1 == 0 && this.scrolling) {
                if (double_2 < (double) this.lastRectangle.y + (double) this.lastRectangle.height) {
                    this.scrollTo(0.0, false);
                } else if (double_2 > (double) this.lastRectangle.y + (double) this.lastRectangle.height + (double) this.getHeight()) {
                    this.scrollTo(this.getMaxScrollPosition(), false);
                } else {
                    double double_5 = Math.max(1.0, this.getMaxScrollPosition());
                    int int_2 = this.getHeight();
                    int int_3 = Mth.clamp((int) ((float) (int_2 * int_2) / (float) this.getMaxScrollPosition()), 32, int_2 - 8);
                    double double_6 = Math.max(1.0, double_5 / (double) (int_2 - int_3));
                    this.offset(double_4 * double_6, false);
                }
                this.target = Mth.clamp(this.target, 0.0, this.getMaxScrollPosition());
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean mouseScrolled(double mouseX, double mouseY, double double_3) {
            if (this.isMouseOver(mouseX, mouseY)) {
                this.offset(ClothConfigInitializer.getScrollStep() * -double_3, true);
                return true;
            } else {
                return false;
            }
        }

        protected void updateScrollingState(double double_1, double double_2, int int_1) {
            this.scrolling = this.isExpanded() && this.lastRectangle != null && int_1 == 0 && double_1 >= (double) this.lastRectangle.x + (double) this.getCellCreator().getCellWidth() - 6.0 && double_1 < (double) (this.lastRectangle.x + this.getCellCreator().getCellWidth());
        }

        @Override
        public boolean mouseClicked(double double_1, double double_2, int int_1) {
            if (!this.isExpanded()) {
                return false;
            } else {
                this.updateScrollingState(double_1, double_2, int_1);
                return super.m_6375_(double_1, double_2, int_1) || this.scrolling;
            }
        }

        public void offset(double value, boolean animated) {
            this.scrollTo(this.target + value, animated);
        }

        public void scrollTo(double value, boolean animated) {
            this.scrollTo(value, animated, ClothConfigInitializer.getScrollDuration());
        }

        public void scrollTo(double value, boolean animated, long duration) {
            this.target = ScrollingContainer.clampExtension(value, this.getMaxScrollPosition());
            if (animated) {
                this.start = System.currentTimeMillis();
                this.duration = duration;
            } else {
                this.scroll = this.target;
            }
        }

        @Override
        public List<DropdownBoxEntry.SelectionCellElement<R>> children() {
            return this.currentElements;
        }
    }

    public static class DefaultSelectionCellCreator<R> extends DropdownBoxEntry.SelectionCellCreator<R> {

        protected Function<R, Component> toTextFunction;

        public DefaultSelectionCellCreator(Function<R, Component> toTextFunction) {
            this.toTextFunction = toTextFunction;
        }

        public DefaultSelectionCellCreator() {
            this(r -> Component.literal(r.toString()));
        }

        @Override
        public DropdownBoxEntry.SelectionCellElement<R> create(R selection) {
            return new DropdownBoxEntry.DefaultSelectionCellElement<>(selection, this.toTextFunction);
        }

        @Override
        public int getCellHeight() {
            return 14;
        }

        @Override
        public int getDropBoxMaxHeight() {
            return this.getCellHeight() * 7;
        }
    }

    public static class DefaultSelectionCellElement<R> extends DropdownBoxEntry.SelectionCellElement<R> {

        protected R r;

        protected int x;

        protected int y;

        protected int width;

        protected int height;

        protected boolean rendering;

        protected Function<R, Component> toTextFunction;

        public DefaultSelectionCellElement(R r, Function<R, Component> toTextFunction) {
            this.r = r;
            this.toTextFunction = toTextFunction;
        }

        @Override
        public void render(GuiGraphics graphics, int mouseX, int mouseY, int x, int y, int width, int height, float delta) {
            this.rendering = true;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            boolean b = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
            if (b) {
                graphics.fill(x + 1, y + 1, x + width - 1, y + height - 1, -15132391);
            }
            graphics.drawString(Minecraft.getInstance().font, ((Component) this.toTextFunction.apply(this.r)).getVisualOrderText(), x + 6, y + 3, b ? 16777215 : 8947848);
        }

        @Override
        public void dontRender(GuiGraphics graphics, float delta) {
            this.rendering = false;
        }

        @Nullable
        @Override
        public Component getSearchKey() {
            return (Component) this.toTextFunction.apply(this.r);
        }

        @Nullable
        @Override
        public R getSelection() {
            return this.r;
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return Collections.emptyList();
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int int_1) {
            boolean b = this.rendering && mouseX >= (double) this.x && mouseX <= (double) (this.x + this.width) && mouseY >= (double) this.y && mouseY <= (double) (this.y + this.height);
            if (b) {
                this.getEntry().selectionElement.topRenderer.setValue(this.r);
                this.getEntry().selectionElement.m_7522_(null);
                this.getEntry().selectionElement.dontReFocus = true;
                return true;
            } else {
                return false;
            }
        }
    }

    public static class DefaultSelectionTopCellElement<R> extends DropdownBoxEntry.SelectionTopCellElement<R> {

        protected EditBox textFieldWidget;

        protected Function<String, R> toObjectFunction;

        protected Function<R, Component> toTextFunction;

        protected final R original;

        protected R value;

        public DefaultSelectionTopCellElement(R value, Function<String, R> toObjectFunction, Function<R, Component> toTextFunction) {
            this.original = (R) Objects.requireNonNull(value);
            this.value = (R) Objects.requireNonNull(value);
            this.toObjectFunction = (Function<String, R>) Objects.requireNonNull(toObjectFunction);
            this.toTextFunction = (Function<R, Component>) Objects.requireNonNull(toTextFunction);
            this.textFieldWidget = new EditBox(Minecraft.getInstance().font, 0, 0, 148, 18, Component.empty()) {

                @Override
                public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
                    this.m_93692_(DefaultSelectionTopCellElement.this.isSuggestionMode() && DefaultSelectionTopCellElement.this.isSelected && DefaultSelectionTopCellElement.this.getParent().m_7222_() == DefaultSelectionTopCellElement.this.getParent().selectionElement && DefaultSelectionTopCellElement.this.getParent().selectionElement.m_7222_() == DefaultSelectionTopCellElement.this && DefaultSelectionTopCellElement.this.m_7222_() == this);
                    super.m_88315_(graphics, mouseX, mouseY, delta);
                }

                @Override
                public boolean keyPressed(int int_1, int int_2, int int_3) {
                    if (int_1 != 257 && int_1 != 335) {
                        return DefaultSelectionTopCellElement.this.isSuggestionMode() && super.keyPressed(int_1, int_2, int_3);
                    } else {
                        DefaultSelectionTopCellElement.this.selectFirstRecommendation();
                        return true;
                    }
                }

                @Override
                public boolean charTyped(char chr, int keyCode) {
                    return DefaultSelectionTopCellElement.this.isSuggestionMode() && super.charTyped(chr, keyCode);
                }
            };
            this.textFieldWidget.setBordered(false);
            this.textFieldWidget.setMaxLength(999999);
            this.textFieldWidget.setValue(((Component) toTextFunction.apply(value)).getString());
        }

        @Override
        public boolean isEdited() {
            return super.isEdited() || !this.getValue().equals(this.original);
        }

        @Override
        public void render(GuiGraphics graphics, int mouseX, int mouseY, int x, int y, int width, int height, float delta) {
            this.textFieldWidget.m_252865_(x + 4);
            this.textFieldWidget.m_253211_(y + 6);
            this.textFieldWidget.m_93674_(width - 8);
            this.textFieldWidget.setEditable(this.getParent().isEditable());
            this.textFieldWidget.setTextColor(this.getPreferredTextColor());
            this.textFieldWidget.m_88315_(graphics, mouseX, mouseY, delta);
        }

        @Override
        public R getValue() {
            return (R) (this.hasError() ? this.value : this.toObjectFunction.apply(this.textFieldWidget.getValue()));
        }

        @Override
        public void setValue(R value) {
            this.textFieldWidget.setValue(((Component) this.toTextFunction.apply(value)).getString());
            this.textFieldWidget.moveCursorTo(0);
        }

        @Override
        public Component getSearchTerm() {
            return Component.literal(this.textFieldWidget.getValue());
        }

        @Override
        public Optional<Component> getError() {
            return this.toObjectFunction.apply(this.textFieldWidget.getValue()) != null ? Optional.empty() : Optional.of(Component.literal("Invalid Value!"));
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return Collections.singletonList(this.textFieldWidget);
        }
    }

    public abstract static class DropdownMenuElement<R> extends AbstractContainerEventHandler {

        @Deprecated
        @NotNull
        private DropdownBoxEntry.SelectionCellCreator<R> cellCreator;

        @Deprecated
        @NotNull
        private DropdownBoxEntry<R> entry;

        private boolean isSelected;

        @NotNull
        public DropdownBoxEntry.SelectionCellCreator<R> getCellCreator() {
            return this.cellCreator;
        }

        @NotNull
        public final DropdownBoxEntry<R> getEntry() {
            return this.entry;
        }

        @Nullable
        @Override
        public ComponentPath nextFocusPath(FocusNavigationEvent focusNavigationEvent) {
            return null;
        }

        @NotNull
        public abstract ImmutableList<R> getSelections();

        public abstract void initCells();

        public abstract void render(GuiGraphics var1, int var2, int var3, Rectangle var4, float var5);

        public abstract void lateRender(GuiGraphics var1, int var2, int var3, float var4);

        public abstract int getHeight();

        public final boolean isExpanded() {
            return this.isSelected && this.getEntry().m_7222_() == this.getEntry().selectionElement;
        }

        public final boolean isSuggestionMode() {
            return this.entry.isSuggestionMode();
        }

        @Override
        public abstract List<DropdownBoxEntry.SelectionCellElement<R>> children();
    }

    public abstract static class SelectionCellCreator<R> {

        public abstract DropdownBoxEntry.SelectionCellElement<R> create(R var1);

        public abstract int getCellHeight();

        public abstract int getDropBoxMaxHeight();

        public int getCellWidth() {
            return 132;
        }
    }

    public abstract static class SelectionCellElement<R> extends AbstractContainerEventHandler {

        @Deprecated
        @NotNull
        private DropdownBoxEntry<R> entry;

        @NotNull
        public final DropdownBoxEntry<R> getEntry() {
            return this.entry;
        }

        public abstract void render(GuiGraphics var1, int var2, int var3, int var4, int var5, int var6, int var7, float var8);

        public abstract void dontRender(GuiGraphics var1, float var2);

        @Nullable
        public abstract Component getSearchKey();

        @Nullable
        public abstract R getSelection();
    }

    public static class SelectionElement<R> extends AbstractContainerEventHandler implements Renderable {

        protected Rectangle bounds;

        protected boolean active;

        protected DropdownBoxEntry.SelectionTopCellElement<R> topRenderer;

        protected DropdownBoxEntry<R> entry;

        protected DropdownBoxEntry.DropdownMenuElement<R> menu;

        protected boolean dontReFocus = false;

        public SelectionElement(DropdownBoxEntry<R> entry, Rectangle bounds, DropdownBoxEntry.DropdownMenuElement<R> menu, DropdownBoxEntry.SelectionTopCellElement<R> topRenderer, DropdownBoxEntry.SelectionCellCreator<R> cellCreator) {
            this.bounds = bounds;
            this.entry = entry;
            this.menu = (DropdownBoxEntry.DropdownMenuElement<R>) Objects.requireNonNull(menu);
            this.menu.entry = entry;
            this.menu.cellCreator = (DropdownBoxEntry.SelectionCellCreator<R>) Objects.requireNonNull(cellCreator);
            this.menu.initCells();
            this.topRenderer = (DropdownBoxEntry.SelectionTopCellElement<R>) Objects.requireNonNull(topRenderer);
            this.topRenderer.entry = entry;
        }

        @Override
        public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
            graphics.fill(this.bounds.x, this.bounds.y, this.bounds.x + this.bounds.width, this.bounds.y + this.bounds.height, this.topRenderer.isSelected ? -1 : -6250336);
            graphics.fill(this.bounds.x + 1, this.bounds.y + 1, this.bounds.x + this.bounds.width - 1, this.bounds.y + this.bounds.height - 1, -16777216);
            this.topRenderer.render(graphics, mouseX, mouseY, this.bounds.x, this.bounds.y, this.bounds.width, this.bounds.height, delta);
            if (this.menu.isExpanded()) {
                this.menu.render(graphics, mouseX, mouseY, this.bounds, delta);
            }
        }

        @Deprecated
        public DropdownBoxEntry.SelectionTopCellElement<R> getTopRenderer() {
            return this.topRenderer;
        }

        @Override
        public boolean mouseScrolled(double double_1, double double_2, double double_3) {
            return this.menu.isExpanded() ? this.menu.m_6050_(double_1, double_2, double_3) : false;
        }

        public void lateRender(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
            if (this.menu.isExpanded()) {
                this.menu.lateRender(graphics, mouseX, mouseY, delta);
            }
        }

        public int getMorePossibleHeight() {
            return this.menu.isExpanded() ? this.menu.getHeight() : -1;
        }

        public R getValue() {
            return this.topRenderer.getValue();
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return Lists.newArrayList(new AbstractContainerEventHandler[] { this.topRenderer, this.menu });
        }

        @Override
        public boolean mouseClicked(double double_1, double double_2, int int_1) {
            this.dontReFocus = false;
            boolean b = super.m_6375_(double_1, double_2, int_1);
            if (this.dontReFocus) {
                this.m_7522_(null);
                this.dontReFocus = false;
            }
            return b;
        }
    }

    public abstract static class SelectionTopCellElement<R> extends AbstractContainerEventHandler {

        @Deprecated
        private DropdownBoxEntry<R> entry;

        protected boolean isSelected = false;

        public abstract R getValue();

        public abstract void setValue(R var1);

        public abstract Component getSearchTerm();

        public boolean isEdited() {
            return this.getConfigError().isPresent();
        }

        public abstract Optional<Component> getError();

        public final Optional<Component> getConfigError() {
            return this.entry.getConfigError();
        }

        public DropdownBoxEntry<R> getParent() {
            return this.entry;
        }

        public final boolean hasConfigError() {
            return this.getConfigError().isPresent();
        }

        public final boolean hasError() {
            return this.getError().isPresent();
        }

        public final int getPreferredTextColor() {
            return this.getConfigError().isPresent() ? 16733525 : 16777215;
        }

        public final boolean isSuggestionMode() {
            return this.getParent().isSuggestionMode();
        }

        public void selectFirstRecommendation() {
            for (DropdownBoxEntry.SelectionCellElement<R> child : this.getParent().selectionElement.menu.children()) {
                if (child.getSelection() != null) {
                    this.setValue(child.getSelection());
                    this.getParent().selectionElement.m_7522_(null);
                    break;
                }
            }
        }

        public abstract void render(GuiGraphics var1, int var2, int var3, int var4, int var5, int var6, int var7, float var8);
    }
}