package com.github.einjerjar.mc.widgets;

import com.github.einjerjar.mc.keymap.utils.Utils;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.locale.Language;
import org.jetbrains.annotations.NotNull;

public class EInput extends EWidget {

    protected final StringBuilder text = new StringBuilder();

    protected final String placeholder = Language.getInstance().getOrDefault("keymap.inpSearchPlaceholder");

    protected String display = "";

    int cursor = 0;

    EInput.EInputChangedAction onChanged;

    public EInput(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    public String text() {
        return this.text.toString();
    }

    public void setCursor(int c) {
        this.cursor = Utils.clamp(c, 0, this.text.length());
        this.updateDisplay();
    }

    public void moveCursor(int offset) {
        this.setCursor(this.cursor + offset);
    }

    protected void updateDisplay() {
        this.display = this.text.toString();
        int maxW = this.rect.w() - this.padding.x() * 2;
        int tWidth = this.font.width(this.display);
        if (tWidth > maxW) {
            StringBuilder temp = new StringBuilder(this.text);
            this.display = new StringBuilder(this.font.plainSubstrByWidth(temp.reverse().toString(), maxW)).reverse().toString();
            temp.reverse();
            int delta = this.text.length() - this.display.length();
            int cDelta = this.cursor - delta;
            if (cDelta <= 0) {
                this.display = this.font.plainSubstrByWidth(temp.substring(Math.max(0, this.cursor - 1), temp.length()), maxW);
            }
        }
        if (this.onChanged != null) {
            this.onChanged.run(this, this.text.toString());
        }
    }

    public void text(String t) {
        this.text.setLength(0);
        this.text.append(t);
        this.moveCursor(this.text.length());
        this.updateDisplay();
    }

    public void textAppend(String t) {
        this.text.append(t);
        this.updateDisplay();
        this.moveCursor(this.text.length());
    }

    public void textDelete(int count) {
        if (this.cursor <= this.text.length() && this.cursor >= 0 && count != 0) {
            int sub = Math.max(0, this.cursor - count);
            int min = Math.min(sub, this.cursor);
            int max = Math.max(sub, this.cursor);
            if (sub != this.cursor) {
                this.text.delete(min, max);
                this.updateDisplay();
                this.moveCursor(0);
            }
        }
    }

    protected void write(String s) {
        if (this.cursor == this.text.length()) {
            this.text.append(s);
        } else {
            this.text.insert(this.cursor, s);
        }
        this.updateDisplay();
        this.moveCursor(s.length());
    }

    protected void write(char s) {
        if (this.cursor == this.text.length()) {
            this.text.append(s);
        } else {
            this.text.insert(this.cursor, s);
        }
        this.updateDisplay();
        this.moveCursor(1);
    }

    @Override
    protected boolean onCharTyped(char codePoint, int modifiers) {
        if (SharedConstants.isAllowedChatCharacter(codePoint)) {
            this.write(codePoint);
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 259) {
            boolean isLast = this.cursor == this.text.length();
            this.textDelete(1);
            if (!isLast) {
                this.moveCursor(-1);
            }
            return true;
        } else if (keyCode == 261) {
            this.textDelete(-1);
            this.moveCursor(0);
            return true;
        } else if (keyCode == 263) {
            this.moveCursor(-1);
            return true;
        } else if (keyCode == 262) {
            this.moveCursor(1);
            return true;
        } else if (keyCode == 269) {
            this.setCursor(this.text.length());
            return true;
        } else if (keyCode == 268) {
            this.setCursor(0);
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.drawBg(guiGraphics);
        this.drawOutline(guiGraphics);
        if (!this.display.isEmpty()) {
            guiGraphics.drawString(this.font, this.display, this.left() + this.padding.x(), this.top() + this.padding.y(), this.colorVariant().text());
        } else {
            guiGraphics.drawString(this.font, this.placeholder, this.left() + this.padding.x(), this.top() + this.padding.y(), this.color.disabled().text());
        }
        int cPosX = 0;
        if (this.cursor > 0) {
            int cDelta = this.cursor - (this.text.length() - this.display.length());
            cPosX = this.font.width(this.display.substring(0, Math.min(cDelta <= 0 ? 1 : cDelta, this.display.length())));
        }
        guiGraphics.hLine(this.left() + this.padding.x() + cPosX, this.left() + this.padding.x() + cPosX + 4, this.bottom() - this.padding.y(), this.focused() ? -65536 : -1);
    }

    public int cursor() {
        return this.cursor;
    }

    public EInput onChanged(EInput.EInputChangedAction onChanged) {
        this.onChanged = onChanged;
        return this;
    }

    public interface EInputChangedAction {

        void run(EInput var1, String var2);
    }
}