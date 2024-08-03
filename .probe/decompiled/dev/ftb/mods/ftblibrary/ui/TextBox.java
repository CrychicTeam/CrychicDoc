package dev.ftb.mods.ftblibrary.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.input.Key;
import dev.ftb.mods.ftblibrary.ui.input.KeyModifiers;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import java.util.Objects;
import java.util.function.Predicate;
import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class TextBox extends Widget implements IFocusableWidget {

    private boolean isFocused = false;

    public int charLimit = 2000;

    public Color4I textColor = Icon.empty();

    public String ghostText = "";

    private String text = "";

    private int displayPos;

    private int cursorPos;

    private int highlightPos;

    private boolean validText = true;

    private int maxLength = 1024;

    private Predicate<String> filter = Objects::nonNull;

    public TextBox(Panel panel) {
        super(panel);
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public void setWidth(int v) {
        super.setWidth(v);
        this.scrollTo(this.getCursorPos());
    }

    @Override
    public final boolean isFocused() {
        return this.isFocused;
    }

    @Override
    public final void setFocused(boolean focused) {
        if (this.isFocused != focused) {
            this.isFocused = focused;
            if (focused) {
                this.getGui().setFocusedWidget(this);
            }
        }
    }

    public void setFilter(Predicate<String> filter) {
        this.filter = filter;
    }

    public final String getText() {
        return this.text;
    }

    public String getSelectedText() {
        return this.text.substring(Math.min(this.cursorPos, this.highlightPos), Math.max(this.cursorPos, this.highlightPos));
    }

    public void setText(String string, boolean triggerChange) {
        if (this.filter.test(string)) {
            if (string.length() > this.maxLength) {
                this.text = string.substring(0, this.maxLength);
            } else {
                this.text = string;
            }
            this.validText = this.isValid(this.text);
            this.moveCursorToEnd(false);
            this.setSelectionPos(this.cursorPos);
            if (triggerChange) {
                this.onTextChanged();
            }
        }
    }

    public final void setText(String s) {
        this.setText(s, true);
    }

    public void moveCursor(int pos, boolean extendSelection) {
        this.moveCursorTo(this.getCursorPos(pos), extendSelection);
    }

    private int getCursorPos(int pos) {
        return Util.offsetByCodepoints(this.text, this.cursorPos, pos);
    }

    public void setCursorPosition(int pos) {
        this.cursorPos = Mth.clamp(pos, 0, this.text.length());
        this.scrollTo(this.cursorPos);
    }

    public void moveCursorTo(int pos, boolean extendSelection) {
        this.setCursorPosition(pos);
        if (!extendSelection) {
            this.setSelectionPos(this.cursorPos);
        }
        this.onTextChanged();
    }

    public void moveCursorToStart(boolean extendSelection) {
        this.moveCursorTo(0, extendSelection);
    }

    public void moveCursorToEnd(boolean extendSelection) {
        this.moveCursorTo(this.text.length(), extendSelection);
    }

    public void setCursorPos(int pos) {
        this.cursorPos = Mth.clamp(pos, 0, this.text.length());
        this.scrollTo(this.cursorPos);
    }

    public void setSelectionPos(int i) {
        this.highlightPos = Mth.clamp(i, 0, this.text.length());
        this.scrollTo(this.highlightPos);
    }

    public int getCursorPos() {
        return this.cursorPos;
    }

    public void insertText(String string) {
        int selStart = Math.min(this.cursorPos, this.highlightPos);
        int selEnd = Math.max(this.cursorPos, this.highlightPos);
        int space = this.maxLength - this.text.length() - (selStart - selEnd);
        if (space > 0) {
            String filtered = SharedConstants.filterText(string);
            int nToInsert = filtered.length();
            if (space < nToInsert) {
                if (Character.isHighSurrogate(filtered.charAt(space - 1))) {
                    space--;
                }
                filtered = filtered.substring(0, space);
                nToInsert = space;
            }
            String newText = new StringBuilder(this.text).replace(selStart, selEnd, filtered).toString();
            this.validText = this.isValid(newText);
            if (this.validText) {
                this.text = newText;
                this.setCursorPosition(selStart + nToInsert);
                this.setSelectionPos(this.cursorPos);
                this.onTextChanged();
            }
        }
    }

    private void scrollTo(int pos) {
        Font font = this.getGui().getTheme().getFont();
        if (font != null) {
            this.displayPos = Math.min(this.displayPos, this.text.length());
            String string = font.plainSubstrByWidth(this.text.substring(this.displayPos), this.width);
            int k = string.length() + this.displayPos;
            if (pos == this.displayPos) {
                this.displayPos = this.displayPos - font.plainSubstrByWidth(this.text, this.width, true).length();
            }
            if (pos > k) {
                this.displayPos += pos - k;
            } else if (pos <= this.displayPos) {
                this.displayPos = this.displayPos - (this.displayPos - pos);
            }
            this.displayPos = Mth.clamp(this.displayPos, 0, this.text.length());
        }
    }

    public int getWordPosition(int count) {
        return this.getWordPosition(count, this.getCursorPos());
    }

    private int getWordPosition(int count, int fromPos) {
        int res = fromPos;
        boolean backwards = count < 0;
        int absCount = Math.abs(count);
        for (int m = 0; m < absCount; m++) {
            if (!backwards) {
                int n = this.text.length();
                res = this.text.indexOf(32, res);
                if (res == -1) {
                    res = n;
                } else {
                    while (res < n && this.text.charAt(res) == ' ') {
                        res++;
                    }
                }
            } else {
                while (res > 0 && this.text.charAt(res - 1) == ' ') {
                    res--;
                }
                while (res > 0 && this.text.charAt(res - 1) != ' ') {
                    res--;
                }
            }
        }
        return res;
    }

    public boolean allowInput() {
        return true;
    }

    private void deleteText(int count) {
        if (Screen.hasControlDown()) {
            this.deleteWords(count);
        } else {
            this.deleteChars(count);
        }
    }

    public void deleteWords(int count) {
        if (!this.text.isEmpty()) {
            if (this.highlightPos != this.cursorPos) {
                this.insertText("");
            } else {
                this.deleteCharsToPos(this.getWordPosition(count));
            }
        }
    }

    public void deleteChars(int count) {
        this.deleteCharsToPos(this.getCursorPos(count));
    }

    public void deleteCharsToPos(int pos) {
        if (!this.text.isEmpty()) {
            if (this.highlightPos != this.cursorPos) {
                this.insertText("");
            } else {
                int from = Math.min(pos, this.cursorPos);
                int to = Math.max(pos, this.cursorPos);
                if (from != to) {
                    String newText = new StringBuilder(this.text).delete(from, to).toString();
                    if (this.filter.test(newText)) {
                        this.text = newText;
                        this.moveCursorTo(from, false);
                    }
                }
            }
        }
    }

    @Override
    public boolean mousePressed(MouseButton button) {
        if (this.isMouseOver()) {
            this.setFocused(true);
            if (button.isLeft()) {
                if (this.isFocused) {
                    int i = this.getMouseX() - this.getX();
                    Theme theme = this.getGui().getTheme();
                    String s = theme.trimStringToWidth(this.text.substring(this.displayPos), this.width);
                    if (isShiftKeyDown()) {
                        this.setSelectionPos(theme.trimStringToWidth(s, i).length() + this.displayPos);
                    } else {
                        this.setCursorPos(theme.trimStringToWidth(s, i).length() + this.displayPos);
                        this.setSelectionPos(this.getCursorPos());
                    }
                }
            } else if (button.isRight() && !this.getText().isEmpty() && this.allowInput()) {
                this.setText("");
            }
            return true;
        } else {
            this.setFocused(false);
            return false;
        }
    }

    @Override
    public boolean keyPressed(Key key) {
        if (!this.isFocused()) {
            return false;
        } else if (key.selectAll()) {
            this.setCursorPos(this.text.length());
            this.setSelectionPos(0);
            return true;
        } else if (key.copy()) {
            setClipboardString(this.getSelectedText());
            return true;
        } else if (key.paste()) {
            this.insertText(getClipboardString());
            return true;
        } else if (key.cut()) {
            setClipboardString(this.getSelectedText());
            this.insertText("");
            return true;
        } else {
            switch(key.keyCode) {
                case 256:
                    this.setFocused(false);
                    return true;
                case 257:
                case 335:
                    if (this.validText) {
                        this.setFocused(false);
                        this.onEnterPressed();
                    }
                    return true;
                case 258:
                    if (this.validText) {
                        this.setFocused(false);
                        this.onTabPressed();
                    }
                    return true;
                case 259:
                    this.deleteText(-1);
                    return true;
                case 261:
                    this.deleteText(1);
                    return true;
                case 262:
                    if (Screen.hasControlDown()) {
                        this.moveCursorTo(this.getWordPosition(1), Screen.hasShiftDown());
                    } else {
                        this.moveCursor(1, Screen.hasShiftDown());
                    }
                    return true;
                case 263:
                    if (Screen.hasControlDown()) {
                        this.moveCursorTo(this.getWordPosition(-1), Screen.hasShiftDown());
                    } else {
                        this.moveCursor(-1, Screen.hasShiftDown());
                    }
                    return true;
                case 268:
                    this.moveCursorToStart(Screen.hasShiftDown());
                    return true;
                case 269:
                    this.moveCursorToEnd(Screen.hasShiftDown());
                    return true;
                default:
                    return true;
            }
        }
    }

    @Override
    public boolean charTyped(char c, KeyModifiers modifiers) {
        if (this.isFocused()) {
            if (SharedConstants.isAllowedChatCharacter(c)) {
                this.insertText(Character.toString(c));
            }
            return true;
        } else {
            return false;
        }
    }

    public void onTextChanged() {
    }

    public void onTabPressed() {
    }

    public void onEnterPressed() {
    }

    public String getFormattedText() {
        return !this.isFocused() && this.text.isEmpty() && !this.ghostText.isEmpty() ? ChatFormatting.ITALIC + this.ghostText : this.text;
    }

    @Override
    public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        this.drawTextBox(graphics, theme, x, y, w, h);
        boolean drawGhostText = !this.isFocused() && this.text.isEmpty() && !this.ghostText.isEmpty();
        String textToDraw = this.getFormattedText();
        GuiHelper.pushScissor(this.getScreen(), x, y, w, h);
        Color4I col = this.validText ? (this.textColor.isEmpty() ? theme.getContentColor(WidgetType.NORMAL) : this.textColor).withAlpha(drawGhostText ? 120 : 255) : Color4I.RED;
        int j = this.cursorPos - this.displayPos;
        String s = theme.trimStringToWidth(textToDraw.substring(this.displayPos), w);
        int textX = x + 4;
        int textY = y + (h - 8) / 2;
        int textX1 = textX;
        if (!s.isEmpty()) {
            String s1 = j > 0 && j <= s.length() ? s.substring(0, j) : s;
            textX1 = theme.drawString(graphics, Component.literal(s1), textX, textY, col, 0);
        }
        boolean drawCursor = this.cursorPos < textToDraw.length() || textToDraw.length() >= this.charLimit;
        int cursorX = textX1;
        if (j > 0 && j <= s.length()) {
            if (drawCursor) {
                cursorX = textX1 - 1;
            }
        } else {
            cursorX = j > 0 ? textX + w : textX;
        }
        if (j > 0 && j < s.length()) {
            theme.drawString(graphics, Component.literal(s.substring(j)), textX1, textY, col, 0);
        }
        if (j >= 0 && j <= s.length() && this.isFocused() && System.currentTimeMillis() % 1000L > 500L) {
            if (drawCursor) {
                col.draw(graphics, cursorX, textY - 1, 1, theme.getFontHeight() + 2);
            } else {
                col.draw(graphics, cursorX, textY + theme.getFontHeight() - 2, 5, 1);
            }
        }
        int k = Mth.clamp(this.highlightPos - this.displayPos, 0, s.length());
        if (k != j) {
            int xMax = textX + theme.getStringWidth(Component.literal(s.substring(0, k)));
            int startX = Math.min(cursorX, xMax - 1);
            int endX = Math.max(cursorX, xMax - 1);
            int startY = textY - 1;
            int endY = textY + theme.getFontHeight();
            endX = Math.min(endX, x + w);
            startX = Math.min(startX, x + w);
            graphics.fill(RenderType.guiTextHighlight(), startX, startY, endX, endY, -2147483520);
        }
        GuiHelper.popScissor(this.getScreen());
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void drawTextBox(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        theme.drawTextBox(graphics, x, y, w, h);
    }

    public boolean isValid(String txt) {
        return this.filter.test(txt);
    }

    public final boolean isTextValid() {
        return this.validText;
    }

    @Override
    public CursorType getCursor() {
        return CursorType.IBEAM;
    }
}