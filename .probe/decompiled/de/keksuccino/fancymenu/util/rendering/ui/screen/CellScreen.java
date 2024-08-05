package de.keksuccino.fancymenu.util.rendering.ui.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.Window;
import de.keksuccino.fancymenu.util.ConsumingSupplier;
import de.keksuccino.fancymenu.util.cycle.ILocalizedValueCycle;
import de.keksuccino.fancymenu.util.input.CharacterFilter;
import de.keksuccino.fancymenu.util.rendering.DrawableColor;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.fancymenu.util.rendering.ui.UIBase;
import de.keksuccino.fancymenu.util.rendering.ui.screen.texteditor.TextEditorScreen;
import de.keksuccino.fancymenu.util.rendering.ui.scroll.v2.scrollarea.ScrollArea;
import de.keksuccino.fancymenu.util.rendering.ui.scroll.v2.scrollarea.entry.ScrollAreaEntry;
import de.keksuccino.fancymenu.util.rendering.ui.widget.button.CycleButton;
import de.keksuccino.fancymenu.util.rendering.ui.widget.button.ExtendedButton;
import de.keksuccino.fancymenu.util.rendering.ui.widget.editbox.ExtendedEditBox;
import de.keksuccino.konkrete.input.MouseInput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.navigation.ScreenDirection;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class CellScreen extends Screen {

    private static final Logger LOGGER = LogManager.getLogger();

    public ScrollArea scrollArea;

    @Nullable
    protected CellScreen.RenderCell selectedCell;

    protected final List<AbstractWidget> rightSideWidgets = new ArrayList();

    @Nullable
    protected ExtendedButton doneButton;

    @Nullable
    protected ExtendedButton cancelButton;

    protected int lastWidth = 0;

    protected int lastHeight = 0;

    protected CellScreen(@NotNull Component title) {
        super(title);
    }

    protected void initCells() {
    }

    protected void initRightSideWidgets() {
    }

    public void rebuild() {
        this.m_6574_(Minecraft.getInstance(), this.f_96543_, this.f_96544_);
    }

    @Override
    protected void init() {
        this.rightSideWidgets.clear();
        this.selectedCell = null;
        float oldScrollX = 0.0F;
        float oldScrollY = 0.0F;
        if (this.scrollArea != null) {
            oldScrollX = this.scrollArea.horizontalScrollBar.getScroll();
            oldScrollY = this.scrollArea.verticalScrollBar.getScroll();
        }
        this.scrollArea = new ScrollArea(20.0F, 65.0F, (float) (this.f_96543_ - 40 - this.getRightSideWidgetWidth() - 20), (float) (this.f_96544_ - 85));
        this.initCells();
        this.m_7787_(this.scrollArea);
        this.scrollArea.horizontalScrollBar.setScroll(oldScrollX);
        this.scrollArea.verticalScrollBar.setScroll(oldScrollY);
        for (ScrollAreaEntry e : this.scrollArea.getEntries()) {
            if (e instanceof CellScreen.CellScrollEntry ce) {
                ce.cell.updateSize(ce);
                ce.setHeight((float) ce.cell.getHeight());
            }
        }
        this.initRightSideWidgets();
        this.addRightSideDefaultSpacer();
        this.cancelButton = this.addRightSideButton(20, Component.translatable("fancymenu.guicomponents.cancel"), button -> this.onCancel());
        this.doneButton = this.addRightSideButton(20, Component.translatable("fancymenu.guicomponents.done"), button -> {
            if (this.allowDone()) {
                this.onDone();
            }
        }).setIsActiveSupplier(consumes -> this.allowDone());
        int widgetWidth = this.getRightSideWidgetWidth();
        int widgetX = this.f_96543_ - 20 - widgetWidth;
        int widgetY = this.f_96544_ - 20;
        AbstractWidget topRightSideWidget = null;
        for (AbstractWidget w : Lists.reverse(this.rightSideWidgets)) {
            if (!(w instanceof CellScreen.RightSideSpacer)) {
                UIBase.applyDefaultWidgetSkinTo(w);
                w.setX(widgetX);
                w.setY(widgetY - w.getHeight());
                w.setWidth(widgetWidth);
                this.m_142416_(w);
                topRightSideWidget = w;
            }
            widgetY -= w.getHeight() + this.getRightSideDefaultSpaceBetweenWidgets();
        }
        Window window = Minecraft.getInstance().getWindow();
        boolean resized = window.getScreenWidth() != this.lastWidth || window.getScreenHeight() != this.lastHeight;
        this.lastWidth = window.getScreenWidth();
        this.lastHeight = window.getScreenHeight();
        if (topRightSideWidget.getY() < 20 && window.getGuiScale() > 1.0) {
            double newScale = window.getGuiScale();
            if (--newScale < 1.0) {
                newScale = 1.0;
            }
            window.setGuiScale(newScale);
            this.m_6574_(Minecraft.getInstance(), window.getGuiScaledWidth(), window.getGuiScaledHeight());
        } else if (topRightSideWidget.getY() >= 20 && resized) {
            RenderingUtils.resetGuiScale();
            this.m_6574_(Minecraft.getInstance(), window.getGuiScaledWidth(), window.getGuiScaledHeight());
        }
    }

    @Override
    protected void setInitialFocus(GuiEventListener $$0) {
    }

    protected abstract void onCancel();

    protected abstract void onDone();

    @Override
    public void onClose() {
        this.onCancel();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        this.updateSelectedCell();
        graphics.fill(0, 0, this.f_96543_, this.f_96544_, UIBase.getUIColorTheme().screen_background_color.getColorInt());
        Component titleComp = this.f_96539_.copy().withStyle(Style.EMPTY.withBold(true));
        graphics.drawString(this.f_96547_, titleComp, 20, 20, UIBase.getUIColorTheme().generic_text_base_color.getColorInt(), false);
        this.scrollArea.render(graphics, mouseX, mouseY, partial);
        super.render(graphics, mouseX, mouseY, partial);
    }

    @Override
    public void tick() {
        for (ScrollAreaEntry e : this.scrollArea.getEntries()) {
            if (e instanceof CellScreen.CellScrollEntry c) {
                c.cell.tick();
            }
        }
    }

    public int getRightSideWidgetWidth() {
        return 150;
    }

    public int getRightSideDefaultSpaceBetweenWidgets() {
        return 5;
    }

    public boolean allowDone() {
        return true;
    }

    protected void addRightSideDefaultSpacer() {
        this.addRightSideSpacer(5);
    }

    protected void addRightSideSpacer(int height) {
        this.rightSideWidgets.add(new CellScreen.RightSideSpacer(height));
    }

    protected <T> CycleButton<T> addRightSideCycleButton(int height, @NotNull ILocalizedValueCycle<T> cycle, @NotNull CycleButton.CycleButtonClickFeedback<T> clickFeedback) {
        return this.addRightSideWidget(new CycleButton<>(0, 0, 0, height, cycle, clickFeedback));
    }

    protected ExtendedButton addRightSideButton(int height, @NotNull Component label, @NotNull Consumer<ExtendedButton> onClick) {
        return this.addRightSideWidget(new ExtendedButton(0, 0, 0, height, label, var1x -> onClick.accept((ExtendedButton) var1x)));
    }

    protected <T extends AbstractWidget> T addRightSideWidget(@NotNull T widget) {
        this.rightSideWidgets.add(widget);
        return widget;
    }

    @NotNull
    protected CellScreen.TextInputCell addTextInputCell(@Nullable CharacterFilter characterFilter, boolean allowEditor, boolean allowEditorPlaceholders) {
        return this.addCell(new CellScreen.TextInputCell(characterFilter, allowEditor, allowEditorPlaceholders));
    }

    @NotNull
    protected CellScreen.LabelCell addLabelCell(@NotNull Component text) {
        return this.addCell(new CellScreen.LabelCell(text));
    }

    protected void addDescriptionEndSeparatorCell() {
        this.addSpacerCell(5);
        this.addSeparatorCell();
        this.addSpacerCell(5);
    }

    @NotNull
    protected CellScreen.SeparatorCell addSeparatorCell(int height) {
        return this.addCell(new CellScreen.SeparatorCell(height));
    }

    @NotNull
    protected CellScreen.SeparatorCell addSeparatorCell() {
        return this.addCell(new CellScreen.SeparatorCell());
    }

    @NotNull
    protected CellScreen.SpacerCell addCellGroupEndSpacerCell() {
        return this.addSpacerCell(7);
    }

    @NotNull
    protected CellScreen.SpacerCell addStartEndSpacerCell() {
        return this.addSpacerCell(20);
    }

    @NotNull
    protected CellScreen.SpacerCell addSpacerCell(int height) {
        return this.addCell(new CellScreen.SpacerCell(height));
    }

    @NotNull
    protected <T> CellScreen.WidgetCell addCycleButtonCell(@NotNull ILocalizedValueCycle<T> cycle, boolean applyDefaultButtonSkin, CycleButton.CycleButtonClickFeedback<T> clickFeedback) {
        return this.addWidgetCell(new CycleButton<>(0, 0, 20, 20, cycle, clickFeedback), applyDefaultButtonSkin);
    }

    @NotNull
    protected CellScreen.WidgetCell addWidgetCell(@NotNull AbstractWidget widget, boolean applyDefaultButtonSkin) {
        return this.addCell(new CellScreen.WidgetCell(widget, applyDefaultButtonSkin));
    }

    @NotNull
    protected <T extends CellScreen.RenderCell> T addCell(@NotNull T cell) {
        this.scrollArea.addEntry(new CellScreen.CellScrollEntry(this.scrollArea, cell));
        return (T) this.m_7787_(cell);
    }

    protected void updateSelectedCell() {
        for (ScrollAreaEntry e : this.scrollArea.getEntries()) {
            if (e instanceof CellScreen.CellScrollEntry c && c.cell.selectable && c.cell.selected) {
                this.selectedCell = c.cell;
                return;
            }
        }
        this.selectedCell = null;
    }

    @Nullable
    protected CellScreen.RenderCell getSelectedCell() {
        return this.selectedCell;
    }

    @Override
    public boolean keyPressed(int keycode, int scancode, int modifiers) {
        if (keycode == 257) {
            if (this.allowDone()) {
                this.onDone();
            }
            return true;
        } else {
            return super.keyPressed(keycode, scancode, modifiers);
        }
    }

    @Override
    public FocusNavigationEvent.ArrowNavigation createArrowEvent(ScreenDirection $$0) {
        return null;
    }

    @Override
    public FocusNavigationEvent.TabNavigation createTabEvent() {
        return null;
    }

    protected class CellScrollEntry extends ScrollAreaEntry {

        public final CellScreen.RenderCell cell;

        public CellScrollEntry(@NotNull ScrollArea parent, @NotNull CellScreen.RenderCell cell) {
            super(parent, 10.0F, 10.0F);
            this.clickable = false;
            this.selectable = false;
            this.selectOnClick = false;
            this.playClickSound = false;
            this.setBackgroundColorHover(this.getBackgroundColorNormal());
            this.cell = cell;
        }

        @Override
        public void renderEntry(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
            this.cell.updateSize(this);
            this.setWidth((float) (this.cell.getWidth() + 40));
            if (this.getWidth() < this.parent.getInnerWidth()) {
                this.setWidth(this.parent.getInnerWidth());
            }
            this.setHeight((float) this.cell.getHeight());
            this.cell.updatePosition(this);
            this.cell.hovered = UIBase.isXYInArea((double) mouseX, (double) mouseY, (double) this.getX(), (double) this.getY(), (double) this.parent.getInnerWidth(), (double) this.getHeight());
            if (this.cell.isSelectable() && this.cell.isHovered() || this.cell == CellScreen.this.selectedCell) {
                RenderingUtils.resetShaderColor(graphics);
                graphics.fill((int) this.getX(), (int) this.getY(), (int) (this.getX() + this.parent.getInnerWidth()), (int) (this.getY() + this.getHeight()), ((DrawableColor) this.cell.hoverColorSupplier.get()).getColorInt());
                RenderingUtils.resetShaderColor(graphics);
            }
            this.cell.render(graphics, mouseX, mouseY, partial);
        }

        @Override
        public void onClick(ScrollAreaEntry entry, double mouseX, double mouseY, int button) {
        }
    }

    public class LabelCell extends CellScreen.RenderCell {

        @NotNull
        protected Component text;

        public LabelCell(@NotNull Component label) {
            this.text = label;
        }

        @Override
        public void renderCell(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
            RenderingUtils.resetShaderColor(graphics);
            UIBase.drawElementLabel(graphics, Minecraft.getInstance().font, this.text, this.getX(), this.getY());
            RenderingUtils.resetShaderColor(graphics);
        }

        @Override
        protected void updateSize(@NotNull CellScreen.CellScrollEntry scrollEntry) {
            this.setWidth(Minecraft.getInstance().font.width(this.text));
            this.setHeight(9);
        }

        @NotNull
        public Component getText() {
            return this.text;
        }

        public CellScreen.LabelCell setText(@NotNull Component text) {
            this.text = text;
            return this;
        }
    }

    public abstract class RenderCell extends AbstractContainerEventHandler implements Renderable, NarratableEntry {

        protected int x;

        protected int y;

        protected int width;

        protected int height;

        protected boolean selectable = false;

        protected boolean selected = false;

        protected boolean hovered = false;

        protected Supplier<DrawableColor> hoverColorSupplier = () -> UIBase.getUIColorTheme().list_entry_color_selected_hovered;

        protected final List<GuiEventListener> children = new ArrayList();

        protected final Map<String, String> memory = new HashMap();

        public abstract void renderCell(@NotNull GuiGraphics var1, int var2, int var3, float var4);

        @Override
        public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
            if (!this.selectable) {
                this.selected = false;
            }
            this.renderCell(graphics, mouseX, mouseY, partial);
            for (GuiEventListener l : this.children) {
                if (l instanceof Renderable r) {
                    r.render(graphics, mouseX, mouseY, partial);
                }
            }
        }

        public void tick() {
        }

        protected void updateSize(@NotNull CellScreen.CellScrollEntry scrollEntry) {
            this.setWidth((int) (CellScreen.this.scrollArea.getInnerWidth() - 40.0F));
            this.setHeight(20);
        }

        protected void updatePosition(@NotNull CellScreen.CellScrollEntry scrollEntry) {
            this.setX((int) (scrollEntry.getX() + 20.0F));
            this.setY((int) scrollEntry.getY());
        }

        public int getTopBottomSpace() {
            return 3;
        }

        public int getX() {
            return this.x;
        }

        public CellScreen.RenderCell setX(int x) {
            this.x = x;
            return this;
        }

        public int getY() {
            return this.y + this.getTopBottomSpace();
        }

        public CellScreen.RenderCell setY(int y) {
            this.y = y;
            return this;
        }

        public int getWidth() {
            return this.width;
        }

        public CellScreen.RenderCell setWidth(int width) {
            this.width = width;
            return this;
        }

        public int getHeight() {
            return this.height + this.getTopBottomSpace() * 2;
        }

        public CellScreen.RenderCell setHeight(int height) {
            this.height = height;
            return this;
        }

        public boolean isHovered() {
            return this.hovered;
        }

        public CellScreen.RenderCell setSelected(boolean selected) {
            this.selected = selected;
            return this;
        }

        public boolean isSelected() {
            return this.selected;
        }

        public boolean isSelectable() {
            return this.selectable;
        }

        public CellScreen.RenderCell setSelectable(boolean selectable) {
            this.selectable = selectable;
            if (!this.selectable) {
                this.selected = false;
            }
            return this;
        }

        public CellScreen.RenderCell setHoverColorSupplier(@NotNull Supplier<DrawableColor> hoverColorSupplier) {
            this.hoverColorSupplier = hoverColorSupplier;
            return this;
        }

        public CellScreen.RenderCell putMemoryValue(@NotNull String key, @NotNull String value) {
            this.memory.put(key, value);
            return this;
        }

        @Nullable
        public String getMemoryValue(@NotNull String key) {
            return (String) this.memory.get(key);
        }

        @NotNull
        @Override
        public List<GuiEventListener> children() {
            return this.children;
        }

        @NotNull
        @Override
        public NarratableEntry.NarrationPriority narrationPriority() {
            return NarratableEntry.NarrationPriority.NONE;
        }

        @Override
        public void updateNarration(@NotNull NarrationElementOutput var1) {
        }

        @Override
        public boolean mouseClicked(double $$0, double $$1, int $$2) {
            if (CellScreen.this.scrollArea.isMouseInteractingWithGrabbers()) {
                return false;
            } else if (!CellScreen.this.scrollArea.isInnerAreaHovered()) {
                return false;
            } else {
                if (this.hovered && this.selectable) {
                    this.selected = true;
                } else {
                    this.selected = false;
                }
                return super.m_6375_($$0, $$1, $$2);
            }
        }

        @Override
        public boolean mouseDragged(double $$0, double $$1, int $$2, double $$3, double $$4) {
            return CellScreen.this.scrollArea.isMouseInteractingWithGrabbers() ? false : super.m_7979_($$0, $$1, $$2, $$3, $$4);
        }

        @Override
        public boolean mouseReleased(double $$0, double $$1, int $$2) {
            return CellScreen.this.scrollArea.isMouseInteractingWithGrabbers() ? false : super.m_6348_($$0, $$1, $$2);
        }
    }

    protected class RightSideSpacer extends AbstractWidget {

        protected RightSideSpacer(int height) {
            super(0, 0, 0, height, Component.empty());
        }

        @Override
        public void setFocused(boolean var1) {
        }

        @Override
        public void renderWidget(GuiGraphics graphics, int var2, int var3, float var4) {
        }

        @Override
        public boolean isFocused() {
            return false;
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput var1) {
        }
    }

    public class SeparatorCell extends CellScreen.RenderCell {

        protected Supplier<DrawableColor> separatorColorSupplier = () -> UIBase.getUIColorTheme().element_border_color_normal;

        protected int separatorThickness = 1;

        public SeparatorCell() {
            this.setHeight(10);
        }

        public SeparatorCell(int height) {
            this.setHeight(height);
        }

        @Override
        public int getTopBottomSpace() {
            return 0;
        }

        @Override
        public void renderCell(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
            int centerY = this.getY() + this.getHeight() / 2;
            int halfThickness = Math.max(1, this.separatorThickness / 2);
            graphics.fill(this.getX(), centerY - (halfThickness > 1 ? halfThickness : 0), this.getX() + this.getWidth(), centerY + halfThickness, ((DrawableColor) this.separatorColorSupplier.get()).getColorInt());
            RenderingUtils.resetShaderColor(graphics);
        }

        @Override
        protected void updateSize(@NotNull CellScreen.CellScrollEntry scrollEntry) {
            this.setWidth((int) (CellScreen.this.scrollArea.getInnerWidth() - 40.0F));
        }

        @NotNull
        public Supplier<DrawableColor> getSeparatorColorSupplier() {
            return this.separatorColorSupplier;
        }

        public CellScreen.SeparatorCell setSeparatorColorSupplier(@NotNull Supplier<DrawableColor> separatorColorSupplier) {
            this.separatorColorSupplier = separatorColorSupplier;
            return this;
        }

        public int getSeparatorThickness() {
            return this.separatorThickness;
        }

        public CellScreen.SeparatorCell setSeparatorThickness(int separatorThickness) {
            this.separatorThickness = separatorThickness;
            return this;
        }
    }

    public class SpacerCell extends CellScreen.RenderCell {

        public SpacerCell(int height) {
            this.setHeight(height);
            this.setWidth(10);
        }

        @Override
        public int getTopBottomSpace() {
            return 0;
        }

        @Override
        public boolean isSelectable() {
            return false;
        }

        @Override
        public CellScreen.RenderCell setSelectable(boolean selectable) {
            throw new RuntimeException("You can't make SpacerCells selectable.");
        }

        @Override
        public void renderCell(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        }

        @Override
        protected void updateSize(@NotNull CellScreen.CellScrollEntry scrollEntry) {
        }
    }

    public class TextInputCell extends CellScreen.RenderCell {

        public ExtendedEditBox editBox;

        public ExtendedButton openEditorButton;

        public final boolean allowEditor;

        protected boolean widgetSizesSet = false;

        protected BiConsumer<String, CellScreen.TextInputCell> editorCallback = (s, cell) -> cell.editBox.setValue(s);

        protected ConsumingSupplier<CellScreen.TextInputCell, String> editorSetTextSupplier = consumes -> consumes.editBox.m_94155_();

        public TextInputCell(@Nullable CharacterFilter characterFilter, boolean allowEditor, boolean allowEditorPlaceholders) {
            this.allowEditor = allowEditor;
            this.editBox = new ExtendedEditBox(Minecraft.getInstance().font, 0, 0, 20, 18, Component.empty());
            this.editBox.m_94199_(100000);
            this.editBox.setCharacterFilter(characterFilter);
            UIBase.applyDefaultWidgetSkinTo(this.editBox);
            this.m_6702_().add(this.editBox);
            if (this.allowEditor) {
                this.openEditorButton = new ExtendedButton(0, 0, 20, 20, Component.translatable("fancymenu.ui.screens.string_builder_screen.edit_in_editor"), button -> {
                    if (allowEditor) {
                        TextEditorScreen s = new TextEditorScreen(characterFilter != null ? characterFilter.convertToLegacyFilter() : null, callback -> {
                            if (callback != null) {
                                this.editorCallback.accept(callback, this);
                            }
                            Minecraft.getInstance().setScreen(CellScreen.this);
                        });
                        s.setMultilineMode(false);
                        s.setPlaceholdersAllowed(allowEditorPlaceholders);
                        s.setText(this.editorSetTextSupplier.get(this));
                        Minecraft.getInstance().setScreen(s);
                    }
                });
                UIBase.applyDefaultWidgetSkinTo(this.openEditorButton);
                this.m_6702_().add(this.openEditorButton);
            }
        }

        @Override
        public void renderCell(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
            if (!this.widgetSizesSet) {
                this.setWidgetSizes();
                this.widgetSizesSet = true;
            }
            this.editBox.m_252865_(this.getX() + 1);
            this.editBox.m_253211_(this.getY() + 1);
            if (this.allowEditor) {
                this.openEditorButton.m_252865_(this.getX() + this.getWidth() - this.openEditorButton.m_5711_());
                this.openEditorButton.m_253211_(this.getY());
            }
            if (MouseInput.isLeftMouseDown() && !this.editBox.m_274382_()) {
                this.editBox.setFocused(false);
            }
        }

        protected void setWidgetSizes() {
            int editorButtonWidth = (this.allowEditor ? Minecraft.getInstance().font.width(this.openEditorButton.getLabelSupplier().get(this.openEditorButton)) : 0) + 6;
            this.editBox.m_93674_(this.allowEditor ? this.getWidth() - editorButtonWidth - 5 : this.getWidth());
            if (this.allowEditor) {
                this.openEditorButton.m_93674_(editorButtonWidth);
            }
        }

        @Override
        public void tick() {
            this.editBox.m_94120_();
        }

        public CellScreen.TextInputCell setEditorPresetTextSupplier(@NotNull ConsumingSupplier<CellScreen.TextInputCell, String> supplier) {
            this.editorSetTextSupplier = (ConsumingSupplier<CellScreen.TextInputCell, String>) Objects.requireNonNull(supplier);
            return this;
        }

        public CellScreen.TextInputCell setEditorCallback(@NotNull BiConsumer<String, CellScreen.TextInputCell> callback) {
            this.editorCallback = (BiConsumer<String, CellScreen.TextInputCell>) Objects.requireNonNull(callback);
            return this;
        }

        public CellScreen.TextInputCell setEditListener(@Nullable Consumer<String> listener) {
            this.editBox.m_94151_(listener);
            return this;
        }

        @NotNull
        public String getText() {
            return this.editBox.m_94155_();
        }

        public CellScreen.TextInputCell setText(@Nullable String text) {
            if (text == null) {
                text = "";
            }
            this.editBox.setValue(text);
            this.editBox.m_94196_(0);
            this.editBox.m_94208_(0);
            this.editBox.setDisplayPosition(0);
            return this;
        }
    }

    public class WidgetCell extends CellScreen.RenderCell {

        public final AbstractWidget widget;

        public WidgetCell(@NotNull AbstractWidget widget, boolean applyDefaultSkin) {
            this.widget = widget;
            if (applyDefaultSkin) {
                UIBase.applyDefaultWidgetSkinTo(this.widget);
            }
            this.m_6702_().add(this.widget);
        }

        @Override
        public void renderCell(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
            this.widget.setX(this.getX());
            this.widget.setY(this.getY());
            this.widget.setWidth(this.getWidth());
        }

        @Override
        protected void updateSize(@NotNull CellScreen.CellScrollEntry scrollEntry) {
            this.setWidth((int) (CellScreen.this.scrollArea.getInnerWidth() - 40.0F));
            this.setHeight(this.widget.getHeight());
        }
    }
}