package com.github.einjerjar.mc.widgets2;

import com.github.einjerjar.mc.widgets.utils.Point;
import com.github.einjerjar.mc.widgets.utils.Rect;
import com.github.einjerjar.mc.widgets.utils.Tooltipped;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;

public abstract class EWidget2Utils implements Renderable, GuiEventListener, NarratableEntry, Tooltipped {

    protected final Font font;

    protected Rect rect;

    protected boolean visible;

    protected boolean enabled;

    protected boolean focused;

    protected boolean active;

    protected boolean hovered;

    protected boolean focusable;

    protected int color;

    protected int transparency;

    protected List<Component> tooltips;

    protected Point<Integer> padding;

    protected EWidget2Utils(int x, int y, int w, int h) {
        this.font = Minecraft.getInstance().font;
        this.visible = true;
        this.enabled = true;
        this.focused = false;
        this.active = false;
        this.hovered = false;
        this.focusable = false;
        this.color = 16777215;
        this.transparency = -16777216;
        this.tooltips = new ArrayList();
        this.padding = new Point<>(0);
        this.rect = new Rect(x, y, w, h);
    }

    protected EWidget2Utils(@NotNull Rect rect) {
        this.font = Minecraft.getInstance().font;
        this.visible = true;
        this.enabled = true;
        this.focused = false;
        this.active = false;
        this.hovered = false;
        this.focusable = false;
        this.color = 16777215;
        this.transparency = -16777216;
        this.tooltips = new ArrayList();
        this.padding = new Point<>(0);
        this.rect = rect;
    }

    protected int tColor(int t) {
        return t | this.color;
    }

    protected int tColor() {
        return this.tColor(this.transparency);
    }

    protected void drawOutline(@NotNull GuiGraphics guiGraphics, int l, int t, int r, int b, int c) {
        U.outline(guiGraphics, l, t, r, b, c);
    }

    protected void drawOutline(@NotNull GuiGraphics guiGraphics, Rect r, int c) {
        this.drawOutline(guiGraphics, r.left(), r.top(), r.right(), r.bottom(), c);
    }

    protected void drawOutline(@NotNull GuiGraphics guiGraphics, int c) {
        this.drawOutline(guiGraphics, this.rect, c);
    }

    protected Point<Integer> center() {
        return new Point<>((this.rect.left() + this.rect.right()) / 2, (this.rect.top() + this.rect.bottom()) / 2);
    }

    @Override
    public void setFocused(boolean focused) {
        this.focused(focused);
    }

    @Override
    public boolean isFocused() {
        return this.focused();
    }

    public WidgetState state() {
        if (!this.enabled()) {
            return WidgetState.DISABLED;
        } else if (this.active()) {
            return WidgetState.ACTIVE;
        } else if (this.hovered()) {
            return WidgetState.HOVER;
        } else {
            return this.focused() ? WidgetState.FOCUS : WidgetState.BASE;
        }
    }

    protected void playSound(SoundEvent sound, float pitch, float volume) {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(sound, pitch, volume));
    }

    protected void playSound(SoundEvent sound, float pitch) {
        this.playSound(sound, pitch, 0.25F);
    }

    protected void playSound(SoundEvent sound) {
        this.playSound(sound, 1.0F);
    }

    @Override
    public List<Component> getTooltips() {
        return this.tooltips;
    }

    public void setTooltip(@NotNull Component tip) {
        this.tooltips.clear();
        this.tooltips.add(tip);
    }

    public void setTooltips(@NotNull List<Component> tips) {
        this.tooltips = tips;
    }

    @NotNull
    @Override
    public NarratableEntry.NarrationPriority narrationPriority() {
        return NarratableEntry.NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput narrationElementOutput) {
    }

    public Rect rect() {
        return this.rect;
    }

    public EWidget2Utils rect(Rect rect) {
        this.rect = rect;
        return this;
    }

    public boolean visible() {
        return this.visible;
    }

    public EWidget2Utils visible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public boolean enabled() {
        return this.enabled;
    }

    public EWidget2Utils enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public boolean focused() {
        return this.focused;
    }

    public EWidget2Utils focused(boolean focused) {
        this.focused = focused;
        return this;
    }

    public boolean active() {
        return this.active;
    }

    public EWidget2Utils active(boolean active) {
        this.active = active;
        return this;
    }

    public boolean hovered() {
        return this.hovered;
    }

    public EWidget2Utils hovered(boolean hovered) {
        this.hovered = hovered;
        return this;
    }

    public boolean focusable() {
        return this.focusable;
    }

    public EWidget2Utils focusable(boolean focusable) {
        this.focusable = focusable;
        return this;
    }

    public int color() {
        return this.color;
    }

    public EWidget2Utils color(int color) {
        this.color = color;
        return this;
    }

    public int transparency() {
        return this.transparency;
    }

    public EWidget2Utils transparency(int transparency) {
        this.transparency = transparency;
        return this;
    }

    public List<Component> tooltips() {
        return this.tooltips;
    }

    public EWidget2Utils tooltips(List<Component> tooltips) {
        this.tooltips = tooltips;
        return this;
    }

    public Point<Integer> padding() {
        return this.padding;
    }

    public EWidget2Utils padding(Point<Integer> padding) {
        this.padding = padding;
        return this;
    }
}