package com.github.einjerjar.mc.widgets2;

import com.github.einjerjar.mc.widgets.utils.Point;
import com.github.einjerjar.mc.widgets.utils.Rect;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class EScreen2Utils extends Screen {

    protected final Point<Integer> margin = new Point<>(6);

    protected final Point<Integer> padding = new Point<>(6);

    protected int targetScreenWidth = -1;

    protected int minScreenWidth = 10;

    protected List<EWidget2> children = new ArrayList();

    private EWidget2 focusWidget;

    private EWidget2 hoverWidget;

    private EWidget2 activeWidget;

    protected Rect scr;

    protected Screen parent;

    protected boolean clickState = false;

    @Nullable
    protected EWidget2 focusWidget() {
        if (this.focusWidget != null) {
            if (this.focusWidget.focused) {
                return this.focusWidget;
            }
            this.focusWidget(null);
        }
        return null;
    }

    @Nullable
    protected EWidget2 hoverWidget() {
        if (this.hoverWidget != null) {
            if (this.hoverWidget.hovered) {
                return this.hoverWidget;
            }
            this.hoverWidget(null);
        }
        return null;
    }

    @Nullable
    protected EWidget2 activeWidget() {
        if (this.activeWidget != null) {
            if (this.activeWidget.active) {
                return this.activeWidget;
            }
            this.activeWidget(null);
        }
        return null;
    }

    protected void focusWidget(EWidget2 w) {
        if (this.focusWidget != w && this.focusWidget != null) {
            this.focusWidget.focused(false);
        }
        this.focusWidget = w;
        if (w != null) {
            w.focused(true);
        }
    }

    protected void hoverWidget(EWidget2 w) {
        if (this.hoverWidget != w && this.hoverWidget != null) {
            this.hoverWidget.hovered(false);
        }
        this.hoverWidget = w;
        if (this.hoverWidget != null) {
            this.hoverWidget.hovered(true);
        }
    }

    protected void activeWidget(EWidget2 w) {
        if (this.activeWidget != w && this.activeWidget != null) {
            this.activeWidget.active(false);
        }
        this.activeWidget = w;
        if (w != null) {
            w.active(true);
        }
    }

    protected EScreen2Utils(Component component) {
        super(component);
    }

    protected void drawOutline(@NotNull GuiGraphics guiGraphics, int l, int t, int r, int b, int c) {
        U.outline(guiGraphics, l, t, r, b, c);
    }

    protected void drawOutline(@NotNull GuiGraphics guiGraphics, Rect r, int c) {
        this.drawOutline(guiGraphics, r.left(), r.top(), r.right(), r.bottom(), c);
    }

    protected void drawOutline(@NotNull GuiGraphics guiGraphics, int c) {
        this.drawOutline(guiGraphics, this.scr, c);
    }

    protected void drawOutsideOutline(@NotNull GuiGraphics guiGraphics, Rect r, int c) {
        this.drawOutline(guiGraphics, r.left() - 1, r.top() - 1, r.right() - 1, r.bottom() - 1, c);
    }

    protected void drawOutsideOutline(@NotNull GuiGraphics guiGraphics, int c) {
        this.drawOutsideOutline(guiGraphics, this.scr, c);
    }

    protected Rect scrFromWidth(int w) {
        if (w == -1) {
            w = this.f_96543_;
        }
        w = Math.max(Math.min(w, this.f_96543_ - this.margin.x() * 2), this.minScreenWidth);
        return new Rect(Math.max((this.f_96543_ - w) / 2, 0) + this.margin.x(), this.margin.y(), w - this.margin.x() * 2, this.f_96544_ - this.margin.y() * 2);
    }

    protected Point<Integer> center() {
        return new Point<>((this.scr.left() + this.scr.right()) / 2, (this.scr.top() + this.scr.bottom()) / 2);
    }

    public Point<Integer> margin() {
        return this.margin;
    }

    public Point<Integer> padding() {
        return this.padding;
    }

    public int targetScreenWidth() {
        return this.targetScreenWidth;
    }

    public int minScreenWidth() {
        return this.minScreenWidth;
    }

    @Override
    public List<EWidget2> children() {
        return this.children;
    }

    public Rect scr() {
        return this.scr;
    }

    public Screen parent() {
        return this.parent;
    }
}