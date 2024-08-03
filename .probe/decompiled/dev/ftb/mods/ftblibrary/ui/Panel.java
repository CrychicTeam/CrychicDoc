package dev.ftb.mods.ftblibrary.ui;

import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.ui.input.Key;
import dev.ftb.mods.ftblibrary.ui.input.KeyModifiers;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import dev.ftb.mods.ftblibrary.util.client.PositionedIngredient;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

public abstract class Panel extends Widget {

    protected final List<Widget> widgets;

    private double scrollX = 0.0;

    private double scrollY = 0.0;

    private int offsetX = 0;

    private int offsetY = 0;

    private boolean onlyRenderWidgetsInside = true;

    private boolean onlyInteractWithWidgetsInside = true;

    private double scrollStep = 20.0;

    private int contentWidth = -1;

    private int contentHeight = -1;

    private int contentWidthExtra;

    private int contentHeightExtra;

    private PanelScrollBar attachedScrollbar = null;

    public Panel(Panel panel) {
        super(panel);
        this.widgets = new ArrayList();
    }

    public boolean getOnlyRenderWidgetsInside() {
        return this.onlyRenderWidgetsInside;
    }

    public void setOnlyRenderWidgetsInside(boolean value) {
        this.onlyRenderWidgetsInside = value;
    }

    public boolean getOnlyInteractWithWidgetsInside() {
        return this.onlyInteractWithWidgetsInside;
    }

    public void setOnlyInteractWithWidgetsInside(boolean value) {
        this.onlyInteractWithWidgetsInside = value;
    }

    public List<Widget> getWidgets() {
        return this.widgets;
    }

    public abstract void addWidgets();

    public abstract void alignWidgets();

    public void clearWidgets() {
        this.widgets.clear();
    }

    public void refreshWidgets() {
        this.contentWidth = this.contentHeight = -1;
        this.clearWidgets();
        try {
            this.addWidgets();
        } catch (Exception var4) {
            var4.printStackTrace();
        }
        this.widgets.sort(null);
        for (Widget widget : this.widgets) {
            if (widget instanceof Panel p) {
                p.refreshWidgets();
            }
        }
        this.alignWidgets();
    }

    public void add(Widget widget) {
        if (widget.parent != this) {
            throw new MismatchingParentPanelException(this, widget);
        } else {
            if (widget instanceof PanelScrollBar psb) {
                this.attachedScrollbar = psb;
            }
            this.widgets.add(widget);
            this.contentWidth = this.contentHeight = -1;
        }
    }

    public void addAll(Iterable<? extends Widget> list) {
        for (Widget w : list) {
            this.add(w);
        }
    }

    public final int align(WidgetLayout layout) {
        this.contentWidth = this.contentHeight = -1;
        int res = layout.align(this);
        this.contentHeightExtra = layout.getLayoutPadding().vertical();
        this.contentWidthExtra = layout.getLayoutPadding().horizontal();
        return res;
    }

    @Override
    public int getX() {
        return super.getX() + this.offsetX;
    }

    @Override
    public int getY() {
        return super.getY() + this.offsetY;
    }

    public int getContentWidth() {
        if (this.contentWidth == -1) {
            int minX = Integer.MAX_VALUE;
            int maxX = Integer.MIN_VALUE;
            for (Widget widget : this.widgets) {
                if (widget.posX < minX) {
                    minX = widget.posX;
                }
                if (widget.posX + widget.width > maxX) {
                    maxX = widget.posX + widget.width;
                }
            }
            this.contentWidth = maxX - minX + this.contentWidthExtra;
        }
        return this.contentWidth;
    }

    public int getContentHeight() {
        if (this.contentHeight == -1) {
            int minY = Integer.MAX_VALUE;
            int maxY = Integer.MIN_VALUE;
            for (Widget widget : this.widgets) {
                if (widget.posY < minY) {
                    minY = widget.posY;
                }
                if (widget.posY + widget.height > maxY) {
                    maxY = widget.posY + widget.height;
                }
            }
            this.contentHeight = maxY - minY + this.contentHeightExtra;
        }
        return this.contentHeight;
    }

    public void setOffset(boolean flag) {
        if (flag) {
            this.offsetX = (int) (-this.scrollX);
            this.offsetY = (int) (-this.scrollY);
        } else {
            this.offsetX = this.offsetY = 0;
        }
    }

    public boolean isOffset() {
        return this.offsetX != 0 || this.offsetY != 0;
    }

    public void setScrollX(double scroll) {
        this.scrollX = scroll;
    }

    public void setScrollY(double scroll) {
        this.scrollY = scroll;
    }

    public double getScrollX() {
        return this.scrollX;
    }

    public double getScrollY() {
        return this.scrollY;
    }

    @Override
    public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        boolean renderInside = this.getOnlyRenderWidgetsInside();
        this.drawBackground(graphics, theme, x, y, w, h);
        if (renderInside) {
            GuiHelper.pushScissor(this.getScreen(), x, y, w, h);
        }
        this.setOffset(true);
        this.widgets.stream().filter(widget -> widget.shouldRenderInLayer(Widget.DrawLayer.BACKGROUND, x, y, w, h)).forEach(widget -> this.drawWidget(graphics, theme, widget, x + this.offsetX, y + this.offsetY, w, h));
        this.drawOffsetBackground(graphics, theme, x + this.offsetX, y + this.offsetY, w, h);
        this.widgets.stream().filter(widget -> widget.shouldRenderInLayer(Widget.DrawLayer.FOREGROUND, x, y, w, h)).forEach(widget -> this.drawWidget(graphics, theme, widget, x + this.offsetX, y + this.offsetY, w, h));
        this.setOffset(false);
        if (renderInside) {
            GuiHelper.popScissor(this.getScreen());
        }
    }

    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
    }

    public void drawOffsetBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
    }

    public void drawWidget(GuiGraphics graphics, Theme theme, Widget widget, int x, int y, int w, int h) {
        int wx = widget.getX();
        int wy = widget.getY();
        int ww = widget.width;
        int wh = widget.height;
        widget.draw(graphics, theme, wx, wy, ww, wh);
        if (Theme.renderDebugBoxes) {
            Color4I col = Color4I.rgb(Color4I.HSBtoRGB((float) (widget.hashCode() & 0xFF) / 255.0F, 1.0F, 1.0F));
            GuiHelper.drawHollowRect(graphics, wx, wy, ww, wh, col.withAlpha(150), false);
            col.withAlpha(30).draw(graphics, wx + 1, wy + 1, ww - 2, wh - 2);
        }
    }

    @Override
    public void addMouseOverText(TooltipList list) {
        if (this.shouldAddMouseOverText() && (!this.getOnlyInteractWithWidgetsInside() || this.isMouseOver())) {
            this.setOffset(true);
            for (int i = this.widgets.size() - 1; i >= 0; i--) {
                Widget widget = (Widget) this.widgets.get(i);
                if (widget.shouldAddMouseOverText()) {
                    widget.addMouseOverText(list);
                    if (Theme.renderDebugBoxes) {
                        list.styledString(widget + "#" + (i + 1) + ": " + widget.width + "x" + widget.height, ChatFormatting.DARK_GRAY);
                    }
                }
            }
            this.setOffset(false);
        }
    }

    @Override
    public void updateMouseOver(int mouseX, int mouseY) {
        super.updateMouseOver(mouseX, mouseY);
        this.setOffset(true);
        for (Widget widget : this.widgets) {
            widget.updateMouseOver(mouseX, mouseY);
        }
        this.setOffset(false);
    }

    @Override
    public boolean mousePressed(MouseButton button) {
        if (this.getOnlyInteractWithWidgetsInside() && !this.isMouseOver()) {
            return false;
        } else {
            this.setOffset(true);
            for (int i = this.widgets.size() - 1; i >= 0; i--) {
                Widget widget = (Widget) this.widgets.get(i);
                if (widget.isEnabled() && widget.shouldDraw() && widget.mousePressed(button)) {
                    this.setOffset(false);
                    return true;
                }
            }
            this.setOffset(false);
            return false;
        }
    }

    @Override
    public boolean mouseDoubleClicked(MouseButton button) {
        if (this.getOnlyInteractWithWidgetsInside() && !this.isMouseOver()) {
            return false;
        } else {
            this.setOffset(true);
            for (int i = this.widgets.size() - 1; i >= 0; i--) {
                Widget widget = (Widget) this.widgets.get(i);
                if (widget.isEnabled() && widget.mouseDoubleClicked(button)) {
                    this.setOffset(false);
                    return true;
                }
            }
            this.setOffset(false);
            return false;
        }
    }

    @Override
    public void mouseReleased(MouseButton button) {
        this.setOffset(true);
        for (int i = this.widgets.size() - 1; i >= 0; i--) {
            Widget widget = (Widget) this.widgets.get(i);
            if (widget.isEnabled()) {
                widget.mouseReleased(button);
            }
        }
        this.setOffset(false);
    }

    @Override
    public boolean mouseScrolled(double scroll) {
        this.setOffset(true);
        for (int i = this.widgets.size() - 1; i >= 0; i--) {
            Widget widget = (Widget) this.widgets.get(i);
            if (widget.isEnabled() && widget.mouseScrolled(scroll)) {
                this.setOffset(false);
                return true;
            }
        }
        boolean scrollPanel = this.scrollPanel(scroll);
        this.setOffset(false);
        return scrollPanel;
    }

    @Override
    public boolean mouseDragged(int button, double dragX, double dragY) {
        this.setOffset(true);
        for (int i = this.widgets.size() - 1; i >= 0; i--) {
            Widget widget = (Widget) this.widgets.get(i);
            if (widget.isEnabled() && widget.mouseDragged(button, dragX, dragY)) {
                this.setOffset(false);
                return true;
            }
        }
        this.setOffset(false);
        return false;
    }

    public boolean scrollPanel(double scroll) {
        if (this.attachedScrollbar == null && this.isMouseOver()) {
            return this.isDefaultScrollVertical() != isShiftKeyDown() ? this.movePanelScroll(0.0, -this.getScrollStep() * scroll) : this.movePanelScroll(-this.getScrollStep() * scroll, 0.0);
        } else {
            return false;
        }
    }

    public boolean movePanelScroll(double dx, double dy) {
        if (dx == 0.0 && dy == 0.0) {
            return false;
        } else {
            double sx = this.getScrollX();
            double sy = this.getScrollY();
            if (dx != 0.0) {
                int w = this.getContentWidth();
                if (w > this.width) {
                    this.setScrollX(Mth.clamp(sx + dx, 0.0, (double) (w - this.width)));
                }
            }
            if (dy != 0.0) {
                int h = this.getContentHeight();
                if (h > this.height) {
                    this.setScrollY(Mth.clamp(sy + dy, 0.0, (double) (h - this.height)));
                }
            }
            return this.getScrollX() != sx || this.getScrollY() != sy;
        }
    }

    public boolean isDefaultScrollVertical() {
        return true;
    }

    public void setScrollStep(double s) {
        this.scrollStep = s;
    }

    public double getScrollStep() {
        return this.scrollStep;
    }

    @Override
    public boolean keyPressed(Key key) {
        if (super.keyPressed(key)) {
            return true;
        } else {
            this.setOffset(true);
            for (int i = this.widgets.size() - 1; i >= 0; i--) {
                Widget widget = (Widget) this.widgets.get(i);
                if (widget.isEnabled() && widget.keyPressed(key)) {
                    this.setOffset(false);
                    return true;
                }
            }
            this.setOffset(false);
            return false;
        }
    }

    @Override
    public void keyReleased(Key key) {
        this.setOffset(true);
        for (int i = this.widgets.size() - 1; i >= 0; i--) {
            Widget widget = (Widget) this.widgets.get(i);
            if (widget.isEnabled()) {
                widget.keyReleased(key);
            }
        }
        this.setOffset(false);
    }

    @Override
    public boolean charTyped(char c, KeyModifiers modifiers) {
        if (super.charTyped(c, modifiers)) {
            return true;
        } else {
            this.setOffset(true);
            for (int i = this.widgets.size() - 1; i >= 0; i--) {
                Widget widget = (Widget) this.widgets.get(i);
                if (widget.isEnabled() && widget.charTyped(c, modifiers)) {
                    this.setOffset(false);
                    return true;
                }
            }
            this.setOffset(false);
            return false;
        }
    }

    @Override
    public void onClosed() {
        for (Widget widget : this.widgets) {
            widget.onClosed();
        }
    }

    @Nullable
    public Widget getWidget(int index) {
        return index >= 0 && index < this.widgets.size() ? (Widget) this.widgets.get(index) : null;
    }

    @Override
    public Optional<PositionedIngredient> getIngredientUnderMouse() {
        this.setOffset(true);
        Optional<PositionedIngredient> result = Optional.empty();
        for (int i = this.widgets.size() - 1; i >= 0; i--) {
            Widget widget = (Widget) this.widgets.get(i);
            if (widget.isEnabled() && widget.isMouseOver()) {
                Optional<PositionedIngredient> ingredient = widget.getIngredientUnderMouse();
                if (ingredient.isPresent()) {
                    result = ingredient;
                    break;
                }
            }
        }
        this.setOffset(false);
        return result;
    }

    @Override
    public void tick() {
        this.setOffset(true);
        for (Widget widget : this.widgets) {
            if (widget.isEnabled()) {
                widget.tick();
            }
        }
        this.setOffset(false);
    }

    public boolean isMouseOverAnyWidget() {
        for (Widget widget : this.widgets) {
            if (widget.isMouseOver()) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public CursorType getCursor() {
        this.setOffset(true);
        for (int i = this.widgets.size() - 1; i >= 0; i--) {
            Widget widget = (Widget) this.widgets.get(i);
            if (widget.isEnabled() && widget.isMouseOver()) {
                CursorType cursor = widget.getCursor();
                if (cursor != null) {
                    this.setOffset(false);
                    return cursor;
                }
            }
        }
        this.setOffset(false);
        return null;
    }
}