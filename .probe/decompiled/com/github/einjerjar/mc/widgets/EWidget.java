package com.github.einjerjar.mc.widgets;

import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import com.github.einjerjar.mc.widgets.utils.ColorGroup;
import com.github.einjerjar.mc.widgets.utils.ColorGroups;
import com.github.einjerjar.mc.widgets.utils.ColorSet;
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
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.NotNull;

public abstract class EWidget implements Renderable, GuiEventListener, NarratableEntry, Tooltipped {

    protected Font font;

    protected ColorGroup color;

    protected Rect rect;

    protected boolean visible;

    protected boolean enabled;

    protected boolean focused;

    protected boolean active;

    protected boolean hovered;

    protected boolean allowRightClick;

    protected List<Component> tooltips;

    protected Point<Integer> padding;

    protected EWidget(int x, int y, int w, int h) {
        this.font = Minecraft.getInstance().font;
        this.color = ColorGroups.WHITE;
        this.visible = true;
        this.enabled = true;
        this.focused = false;
        this.active = false;
        this.hovered = false;
        this.allowRightClick = false;
        this.padding = new Point<>(4);
        this.rect = new Rect(x, y, w, h);
    }

    protected EWidget(Rect rect) {
        this.font = Minecraft.getInstance().font;
        this.color = ColorGroups.WHITE;
        this.visible = true;
        this.enabled = true;
        this.focused = false;
        this.active = false;
        this.hovered = false;
        this.allowRightClick = false;
        this.padding = new Point<>(4);
        this.rect = rect;
    }

    @Override
    public List<Component> getTooltips() {
        return this.tooltips;
    }

    protected void setTooltip(Component tip) {
        if (this.tooltips == null) {
            this.tooltips = new ArrayList();
        }
        this.tooltips.clear();
        this.tooltips.add(tip);
    }

    protected boolean onCharTyped(char codePoint, int modifiers) {
        return false;
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        return this.enabled() && this.visible() && this.focused() ? this.onCharTyped(codePoint, modifiers) : false;
    }

    protected void init() {
    }

    protected ColorSet colorVariant() {
        return this.color.getVariant(this.hovered, this.active, !this.enabled);
    }

    @Override
    public void setFocused(boolean focused) {
        this.focused(focused);
    }

    @Override
    public boolean isFocused() {
        return this.focused();
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (this.visible) {
            this.hovered = this.isMouseOver((double) mouseX, (double) mouseY);
            if (KeymapConfig.instance().debug()) {
                this.drawOutline(guiGraphics, 1157562368);
            }
            this.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
        }
    }

    public void updateTooltips() {
    }

    public boolean onMouseClicked(boolean inside, double mouseX, double mouseY, int button) {
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!this.hovered) {
            this.active = false;
            this.onMouseClicked(false, mouseX, mouseY, button);
            return false;
        } else if (!this.allowRightClick && button != 0) {
            return false;
        } else {
            this.playSound(SoundEvents.UI_BUTTON_CLICK.value());
            this.active = true;
            return this.onMouseClicked(true, mouseX, mouseY, button);
        }
    }

    public boolean onMouseReleased(boolean inside, double mouseX, double mouseY, int button) {
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.active = false;
        if (!this.hovered) {
            this.onMouseReleased(false, mouseX, mouseY, button);
            return false;
        } else {
            return !this.allowRightClick && button != 0 ? false : this.onMouseReleased(true, mouseX, mouseY, button);
        }
    }

    protected boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return !this.focused ? false : this.onKeyPressed(keyCode, scanCode, modifiers);
    }

    protected boolean onMouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return !this.enabled ? false : this.onMouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    protected boolean onMouseScrolled(double mouseX, double mouseY, double delta) {
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        return !this.enabled ? false : this.onMouseScrolled(mouseX, mouseY, delta);
    }

    protected abstract void renderWidget(@NotNull GuiGraphics var1, int var2, int var3, float var4);

    public int top() {
        return this.rect.top();
    }

    public int bottom() {
        return this.rect.bottom();
    }

    public int left() {
        return this.rect.left();
    }

    public int right() {
        return this.rect.right();
    }

    public int midX() {
        return this.rect.midX();
    }

    public int midY() {
        return this.rect.midY();
    }

    public void drawOutline(@NotNull GuiGraphics guiGraphics, int left, int top, int right, int bottom, int color) {
        guiGraphics.hLine(left, right, top, color);
        guiGraphics.hLine(left, right, bottom, color);
        guiGraphics.vLine(left, top, bottom, color);
        guiGraphics.vLine(right, top, bottom, color);
    }

    public void drawOutline(@NotNull GuiGraphics guiGraphics, int color) {
        this.drawOutline(guiGraphics, this.left(), this.top(), this.right(), this.bottom(), color);
    }

    public void drawOutline(@NotNull GuiGraphics guiGraphics) {
        this.drawOutline(guiGraphics, this.colorVariant().border());
    }

    public void drawBg(@NotNull GuiGraphics guiGraphics, int left, int top, int right, int bottom, int color) {
        guiGraphics.fill(left, top, right, bottom, color);
    }

    public void drawBg(@NotNull GuiGraphics guiGraphics, int color) {
        this.drawBg(guiGraphics, this.left(), this.top(), this.right(), this.bottom(), color);
    }

    public void drawBg(@NotNull GuiGraphics guiGraphics) {
        this.drawBg(guiGraphics, this.colorVariant().bg());
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.rect.contains(mouseX, mouseY);
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
    public NarratableEntry.NarrationPriority narrationPriority() {
        if (this.focused) {
            return NarratableEntry.NarrationPriority.FOCUSED;
        } else {
            return this.hovered ? NarratableEntry.NarrationPriority.HOVERED : NarratableEntry.NarrationPriority.NONE;
        }
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput narrationElementOutput) {
    }

    public ColorGroup color() {
        return this.color;
    }

    public EWidget color(ColorGroup color) {
        this.color = color;
        return this;
    }

    public Rect rect() {
        return this.rect;
    }

    public EWidget rect(Rect rect) {
        this.rect = rect;
        return this;
    }

    public boolean visible() {
        return this.visible;
    }

    public EWidget visible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public boolean enabled() {
        return this.enabled;
    }

    public EWidget enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public boolean focused() {
        return this.focused;
    }

    public EWidget focused(boolean focused) {
        this.focused = focused;
        return this;
    }

    public boolean active() {
        return this.active;
    }

    public boolean hovered() {
        return this.hovered;
    }

    public boolean allowRightClick() {
        return this.allowRightClick;
    }

    public List<Component> tooltips() {
        return this.tooltips;
    }

    public EWidget tooltips(List<Component> tooltips) {
        this.tooltips = tooltips;
        return this;
    }

    public Point<Integer> padding() {
        return this.padding;
    }

    public EWidget padding(Point<Integer> padding) {
        this.padding = padding;
        return this;
    }

    public interface SimpleWidgetAction<T> {

        void run(T var1);
    }
}