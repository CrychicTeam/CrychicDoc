package org.embeddedt.embeddium.gui.frame.components;

import java.util.List;
import java.util.function.BiFunction;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.widgets.AbstractWidget;
import me.jellysquid.mods.sodium.client.util.Dim2i;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

public class SearchTextFieldComponent extends AbstractWidget {

    protected final Dim2i dim;

    protected final List<OptionPage> pages;

    private final Font textRenderer = Minecraft.getInstance().font;

    private final BiFunction<String, Integer, FormattedCharSequence> renderTextProvider = (string, firstCharacterIndex) -> FormattedCharSequence.forward(string, Style.EMPTY);

    private final SearchTextFieldModel model;

    public SearchTextFieldComponent(Dim2i dim, List<OptionPage> pages, SearchTextFieldModel model) {
        this.dim = dim;
        this.pages = pages;
        this.model = model;
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        this.model.innerWidth = this.getInnerWidth();
        if (this.isVisible()) {
            if (this.model.text.isEmpty()) {
                this.drawString(context, Component.translatable("embeddium.search_bar_empty"), this.dim.x() + 6, this.dim.y() + 6, -5592406);
            }
            this.drawRect(context, this.dim.x(), this.dim.y(), this.dim.getLimitX(), this.dim.getLimitY(), this.m_93696_() ? -536870912 : -1879048192);
            int j = this.model.selectionStart - this.model.firstCharacterIndex;
            int k = this.model.selectionEnd - this.model.firstCharacterIndex;
            String string = this.textRenderer.plainSubstrByWidth(this.model.text.substring(this.model.firstCharacterIndex), this.getInnerWidth());
            boolean bl = j >= 0 && j <= string.length();
            int l = this.dim.x() + 6;
            int m = this.dim.y() + 6;
            int n = l;
            if (k > string.length()) {
                k = string.length();
            }
            if (!string.isEmpty()) {
                String string2 = bl ? string.substring(0, j) : string;
                n = context.drawString(this.textRenderer, (FormattedCharSequence) this.renderTextProvider.apply(string2, this.model.firstCharacterIndex), l, m, -1);
            }
            boolean bl3 = this.model.selectionStart < this.model.text.length() || this.model.text.length() >= this.model.getMaxLength();
            int o = n;
            if (!bl) {
                o = j > 0 ? l + this.dim.width() - 12 : l;
            } else if (bl3) {
                o = n - 1;
                n--;
            }
            if (!string.isEmpty() && bl && j < string.length()) {
                context.drawString(this.textRenderer, (FormattedCharSequence) this.renderTextProvider.apply(string.substring(j), this.model.selectionStart), n, m, -1);
            }
            if (this.m_93696_()) {
                context.fill(RenderType.guiOverlay(), o, m - 1, o + 1, m + 1 + 9, -3092272);
            }
            if (k != j) {
                int p = l + this.textRenderer.width(string.substring(0, k));
                this.drawSelectionHighlight(context, o, m - 1, p - 1, m + 1 + 9);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int i = Mth.floor(mouseX) - this.dim.x() - 6;
        String string = this.textRenderer.plainSubstrByWidth(this.model.text.substring(this.model.firstCharacterIndex), this.getInnerWidth());
        this.model.setCursor(this.textRenderer.plainSubstrByWidth(string, i).length() + this.model.firstCharacterIndex);
        this.setFocused(this.dim.containsCursor(mouseX, mouseY));
        return this.m_93696_();
    }

    @Override
    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    private void drawSelectionHighlight(GuiGraphics context, int x1, int y1, int x2, int y2) {
        if (x1 < x2) {
            int i = x1;
            x1 = x2;
            x2 = i;
        }
        if (y1 < y2) {
            int i = y1;
            y1 = y2;
            y2 = i;
        }
        if (x2 > this.dim.x() + this.dim.width()) {
            x2 = this.dim.x() + this.dim.width();
        }
        if (x1 > this.dim.x() + this.dim.width()) {
            x1 = this.dim.x() + this.dim.width();
        }
        context.fill(RenderType.guiTextHighlight(), x1, y1, x2, y2, -16776961);
    }

    @Override
    public boolean isActive() {
        return this.isVisible() && this.m_93696_() && this.isEditable();
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (!this.isActive()) {
            return false;
        } else if (SharedConstants.isAllowedChatCharacter(chr)) {
            if (this.model.editable) {
                this.model.write(Character.toString(chr));
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!this.isActive()) {
            return false;
        } else {
            this.model.selecting = Screen.hasShiftDown();
            if (Screen.isSelectAll(keyCode)) {
                this.model.setCursorToEnd();
                this.model.setSelectionEnd(0);
                return true;
            } else if (Screen.isCopy(keyCode)) {
                Minecraft.getInstance().keyboardHandler.setClipboard(this.model.getSelectedText());
                return true;
            } else if (Screen.isPaste(keyCode)) {
                if (this.model.editable) {
                    this.model.write(Minecraft.getInstance().keyboardHandler.getClipboard());
                }
                return true;
            } else if (Screen.isCut(keyCode)) {
                Minecraft.getInstance().keyboardHandler.setClipboard(this.model.getSelectedText());
                if (this.model.editable) {
                    this.model.write("");
                }
                return true;
            } else {
                switch(keyCode) {
                    case 259:
                        if (this.model.editable) {
                            this.model.selecting = false;
                            this.model.erase(-1);
                            this.model.selecting = Screen.hasShiftDown();
                        }
                        return true;
                    case 260:
                    case 264:
                    case 265:
                    case 266:
                    case 267:
                    default:
                        return false;
                    case 261:
                        if (this.model.editable) {
                            this.model.selecting = false;
                            this.model.erase(1);
                            this.model.selecting = Screen.hasShiftDown();
                        }
                        return true;
                    case 262:
                        if (Screen.hasControlDown()) {
                            this.model.setCursor(this.model.getWordSkipPosition(1));
                        } else {
                            this.model.moveCursor(1);
                        }
                        boolean state = this.model.getCursor() != this.model.lastCursorPosition && this.model.getCursor() != this.model.text.length() + 1;
                        this.model.lastCursorPosition = this.model.getCursor();
                        return state;
                    case 263:
                        if (Screen.hasControlDown()) {
                            this.model.setCursor(this.model.getWordSkipPosition(-1));
                        } else {
                            this.model.moveCursor(-1);
                        }
                        boolean state = this.model.getCursor() != this.model.lastCursorPosition && this.model.getCursor() != 0;
                        this.model.lastCursorPosition = this.model.getCursor();
                        return state;
                    case 268:
                        this.model.setCursorToStart();
                        return true;
                    case 269:
                        this.model.setCursorToEnd();
                        return true;
                }
            }
        }
    }

    public boolean isVisible() {
        return this.model.visible;
    }

    public boolean isEditable() {
        return this.model.editable;
    }

    public int getInnerWidth() {
        return this.dim.width() - 12;
    }

    @Nullable
    @Override
    public ComponentPath nextFocusPath(FocusNavigationEvent navigation) {
        return !this.model.visible ? null : super.nextFocusPath(navigation);
    }

    @Override
    public ScreenRectangle getRectangle() {
        return new ScreenRectangle(this.dim.x(), this.dim.y(), this.dim.width(), this.dim.height());
    }
}