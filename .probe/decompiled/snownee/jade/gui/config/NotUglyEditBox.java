package snownee.jade.gui.config;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

public class NotUglyEditBox extends AbstractWidget implements Renderable {

    private final Font font;

    public int paddingLeft;

    public int paddingRight;

    public int paddingTop;

    @Nullable
    public Consumer<String> responder;

    private String value = "";

    private int maxLength = 32;

    private int frame;

    private boolean canLoseFocus = true;

    private boolean isEditable = true;

    private boolean shiftPressed;

    private int displayPos;

    private int cursorPos;

    private int highlightPos;

    private int textColor = 14737632;

    private int textColorUneditable = 7368816;

    @Nullable
    private String suggestion;

    private Predicate<String> filter = Objects::nonNull;

    private BiFunction<String, Integer, FormattedCharSequence> formatter = (string, integer) -> FormattedCharSequence.forward(string, Style.EMPTY);

    @Nullable
    private Component hint;

    private boolean isMouseOverCross;

    public NotUglyEditBox(Font font, int i, int j, int k, int l, Component component) {
        this(font, i, j, k, l, null, component);
    }

    public NotUglyEditBox(Font font, int i, int j, int k, int l, @Nullable NotUglyEditBox editBox, Component component) {
        super(i, j, k, l, component);
        this.font = font;
        if (editBox != null) {
            this.setValue(editBox.getValue());
        }
    }

    public void setFormatter(BiFunction<String, Integer, FormattedCharSequence> biFunction) {
        this.formatter = biFunction;
    }

    public void tick() {
        this.frame++;
    }

    @Override
    protected MutableComponent createNarrationMessage() {
        Component component = this.m_6035_();
        String value = this.isMouseOverCross ? "" : this.value;
        return Component.translatable("gui.narrate.editBox", component, value);
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String string) {
        if (this.filter.test(string)) {
            this.value = string.length() > this.maxLength ? string.substring(0, this.maxLength) : string;
            this.moveCursorToEnd();
            this.setHighlightPos(this.cursorPos);
            this.onValueChange(string);
        }
    }

    public String getHighlighted() {
        int i = Math.min(this.cursorPos, this.highlightPos);
        int j = Math.max(this.cursorPos, this.highlightPos);
        return this.value.substring(i, j);
    }

    public void setFilter(Predicate<String> predicate) {
        this.filter = predicate;
    }

    public void insertText(String string) {
        int i = Math.min(this.cursorPos, this.highlightPos);
        int j = Math.max(this.cursorPos, this.highlightPos);
        int k = this.maxLength - this.value.length() - (i - j);
        String string2;
        int l;
        if (k < (l = (string2 = SharedConstants.filterText(string)).length())) {
            string2 = string2.substring(0, k);
            l = k;
        }
        String string3;
        if (this.filter.test(string3 = new StringBuilder(this.value).replace(i, j, string2).toString())) {
            this.value = string3;
            this.setCursorPosition(i + l);
            this.setHighlightPos(this.cursorPos);
            this.onValueChange(this.value);
        }
    }

    private void onValueChange(String string) {
        if (this.responder != null) {
            this.responder.accept(string);
        }
    }

    private void deleteText(int i) {
        if (Screen.hasControlDown()) {
            this.deleteWords(i);
        } else {
            this.deleteChars(i);
        }
    }

    public void deleteWords(int i) {
        if (!this.value.isEmpty()) {
            if (this.highlightPos != this.cursorPos) {
                this.insertText("");
            } else {
                this.deleteChars(this.getWordPosition(i) - this.cursorPos);
            }
        }
    }

    public void deleteChars(int i) {
        if (!this.value.isEmpty()) {
            if (this.highlightPos != this.cursorPos) {
                this.insertText("");
            } else {
                int j = this.getCursorPos(i);
                int k = Math.min(j, this.cursorPos);
                int l;
                if (k != (l = Math.max(j, this.cursorPos))) {
                    String string = new StringBuilder(this.value).delete(k, l).toString();
                    if (this.filter.test(string)) {
                        this.value = string;
                        this.moveCursorTo(k);
                    }
                }
            }
        }
    }

    public int getWordPosition(int i) {
        return this.getWordPosition(i, this.getCursorPosition());
    }

    private int getWordPosition(int i, int j) {
        return this.getWordPosition(i, j, true);
    }

    private int getWordPosition(int i, int j, boolean bl) {
        int k = j;
        boolean bl2 = i < 0;
        int l = Math.abs(i);
        for (int m = 0; m < l; m++) {
            if (!bl2) {
                int n = this.value.length();
                if ((k = this.value.indexOf(32, k)) == -1) {
                    k = n;
                } else {
                    while (bl && k < n && this.value.charAt(k) == ' ') {
                        k++;
                    }
                }
            } else {
                while (bl && k > 0 && this.value.charAt(k - 1) == ' ') {
                    k--;
                }
                while (k > 0 && this.value.charAt(k - 1) != ' ') {
                    k--;
                }
            }
        }
        return k;
    }

    public void moveCursor(int i) {
        this.moveCursorTo(this.getCursorPos(i));
    }

    private int getCursorPos(int i) {
        return Util.offsetByCodepoints(this.value, this.cursorPos, i);
    }

    public void moveCursorTo(int i) {
        this.setCursorPosition(i);
        if (!this.shiftPressed) {
            this.setHighlightPos(this.cursorPos);
        }
        this.onValueChange(this.value);
    }

    public void moveCursorToStart() {
        this.moveCursorTo(0);
    }

    public void moveCursorToEnd() {
        this.moveCursorTo(this.value.length());
    }

    @Override
    public boolean keyPressed(int i, int j, int k) {
        if (!this.canConsumeInput()) {
            return false;
        } else {
            this.shiftPressed = Screen.hasShiftDown();
            if (Screen.isSelectAll(i)) {
                this.moveCursorToEnd();
                this.setHighlightPos(0);
                return true;
            } else if (Screen.isCopy(i)) {
                Minecraft.getInstance().keyboardHandler.setClipboard(this.getHighlighted());
                return true;
            } else if (Screen.isPaste(i)) {
                if (this.isEditable) {
                    this.insertText(Minecraft.getInstance().keyboardHandler.getClipboard());
                }
                return true;
            } else if (Screen.isCut(i)) {
                Minecraft.getInstance().keyboardHandler.setClipboard(this.getHighlighted());
                if (this.isEditable) {
                    this.insertText("");
                }
                return true;
            } else {
                switch(i) {
                    case 259:
                        if (this.isEditable) {
                            this.shiftPressed = false;
                            this.deleteText(-1);
                            this.shiftPressed = Screen.hasShiftDown();
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
                        if (this.isEditable) {
                            this.shiftPressed = false;
                            this.deleteText(1);
                            this.shiftPressed = Screen.hasShiftDown();
                        }
                        return true;
                    case 262:
                        if (Screen.hasControlDown()) {
                            this.moveCursorTo(this.getWordPosition(1));
                        } else {
                            this.moveCursor(1);
                        }
                        return true;
                    case 263:
                        if (Screen.hasControlDown()) {
                            this.moveCursorTo(this.getWordPosition(-1));
                        } else {
                            this.moveCursor(-1);
                        }
                        return true;
                    case 268:
                        this.moveCursorToStart();
                        return true;
                    case 269:
                        this.moveCursorToEnd();
                        return true;
                }
            }
        }
    }

    public boolean canConsumeInput() {
        return this.isVisible() && this.m_93696_() && this.isEditable();
    }

    @Override
    public boolean charTyped(char c, int i) {
        if (!this.canConsumeInput()) {
            return false;
        } else if (SharedConstants.isAllowedChatCharacter(c)) {
            if (this.isEditable) {
                this.insertText(Character.toString(c));
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onClick(double x, double y) {
        if (this.isMouseOverCross) {
            this.setValue("");
            super.playDownSound(Minecraft.getInstance().getSoundManager());
        } else {
            int i = Mth.floor(x) - this.m_252754_() - this.paddingLeft;
            String string = this.font.plainSubstrByWidth(this.value.substring(this.displayPos), this.getInnerWidth());
            this.moveCursorTo(this.font.plainSubstrByWidth(string, i).length() + this.displayPos);
        }
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
    }

    protected void renderBackground(GuiGraphics guiGraphics, int i, int j, float f) {
        guiGraphics.fill(this.m_252754_(), this.m_252907_(), this.m_252754_() + this.f_93618_, this.m_252907_() + this.f_93619_, -16777216);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        this.isMouseOverCross = false;
        if (this.isVisible()) {
            this.renderBackground(guiGraphics, i, j, f);
            if (this.isEditable && !this.value.isEmpty()) {
                this.isMouseOverCross = this.f_93622_ && i > this.f_93618_ - this.paddingRight + 4;
                int c = this.isMouseOverCross ? this.textColor : this.textColorUneditable;
                guiGraphics.drawString(this.font, "×", this.m_252754_() + this.f_93618_ - 10, this.m_252907_() + this.paddingTop + 1, c);
            }
            int k = this.isEditable ? this.textColor : this.textColorUneditable;
            int l = this.cursorPos - this.displayPos;
            int m = this.highlightPos - this.displayPos;
            String string = this.font.plainSubstrByWidth(this.value.substring(this.displayPos), this.getInnerWidth());
            boolean bl = l >= 0 && l <= string.length();
            boolean bl2 = this.m_93696_() && this.frame / 6 % 2 == 0 && bl;
            int n = this.m_252754_() + this.paddingLeft;
            int o = this.m_252907_() + this.paddingTop;
            int p = n;
            if (m > string.length()) {
                m = string.length();
            }
            if (!string.isEmpty()) {
                String string2 = bl ? string.substring(0, l) : string;
                p = guiGraphics.drawString(this.font, (FormattedCharSequence) this.formatter.apply(string2, this.displayPos), n, o, k);
            }
            boolean bl3 = this.cursorPos < this.value.length() || this.value.length() >= this.getMaxLength();
            int q = p;
            if (!bl) {
                q = l > 0 ? n + this.f_93618_ : n;
            } else if (bl3) {
                q = p - 1;
                p--;
            }
            if (!string.isEmpty() && bl && l < string.length()) {
                guiGraphics.drawString(this.font, (FormattedCharSequence) this.formatter.apply(string.substring(l), this.cursorPos), p, o, k);
            }
            if (this.hint != null && string.isEmpty() && !this.m_93696_()) {
                guiGraphics.drawString(this.font, this.hint, p, o, 8421504);
            }
            if (!bl3 && this.suggestion != null) {
                guiGraphics.drawString(this.font, this.suggestion, q - 1, o, -8355712);
            }
            if (bl2) {
                guiGraphics.fill(RenderType.guiOverlay(), q, o - 1, q + 1, o + 1 + 9, -3092272);
            }
            if (m != l) {
                int r = n + this.font.width(string.substring(0, m));
                this.renderHighlight(guiGraphics, q, o - 1, r - 1, o + 1 + 9);
            }
        }
    }

    private void renderHighlight(GuiGraphics guiGraphics, int i, int j, int k, int l) {
        if (i < k) {
            int m = i;
            i = k;
            k = m;
        }
        if (j < l) {
            int m = j;
            j = l;
            l = m;
        }
        if (k > this.m_252754_() + this.f_93618_) {
            k = this.m_252754_() + this.f_93618_;
        }
        if (i > this.m_252754_() + this.f_93618_) {
            i = this.m_252754_() + this.f_93618_;
        }
        guiGraphics.fill(RenderType.guiTextHighlight(), i, j, k, l, -16776961);
    }

    private int getMaxLength() {
        return this.maxLength;
    }

    public void setMaxLength(int i) {
        this.maxLength = i;
        if (this.value.length() > i) {
            this.value = this.value.substring(0, i);
            this.onValueChange(this.value);
        }
    }

    public int getCursorPosition() {
        return this.cursorPos;
    }

    public void setCursorPosition(int i) {
        this.cursorPos = Mth.clamp(i, 0, this.value.length());
    }

    public void setTextColor(int i) {
        this.textColor = i;
    }

    public void setTextColorUneditable(int i) {
        this.textColorUneditable = i;
    }

    @Nullable
    @Override
    public ComponentPath nextFocusPath(FocusNavigationEvent focusNavigationEvent) {
        return this.f_93624_ && this.isEditable ? super.nextFocusPath(focusNavigationEvent) : null;
    }

    @Override
    public boolean isMouseOver(double d, double e) {
        return this.f_93624_ && d >= (double) this.m_252754_() && d < (double) (this.m_252754_() + this.f_93618_) && e >= (double) this.m_252907_() && e < (double) (this.m_252907_() + this.f_93619_);
    }

    @Override
    public void setFocused(boolean bl) {
        if (this.canLoseFocus || bl) {
            super.setFocused(bl);
            if (bl) {
                this.frame = 0;
            }
        }
    }

    private boolean isEditable() {
        return this.isEditable;
    }

    public void setEditable(boolean bl) {
        this.isEditable = bl;
    }

    public int getInnerWidth() {
        return this.f_93618_ - this.paddingLeft - this.paddingRight;
    }

    public void setHighlightPos(int i) {
        int j = this.value.length();
        this.highlightPos = Mth.clamp(i, 0, j);
        if (this.font != null) {
            if (this.displayPos > j) {
                this.displayPos = j;
            }
            int k = this.getInnerWidth();
            String string = this.font.plainSubstrByWidth(this.value.substring(this.displayPos), k);
            int l = string.length() + this.displayPos;
            if (this.highlightPos == this.displayPos) {
                this.displayPos = this.displayPos - this.font.plainSubstrByWidth(this.value, k, true).length();
            }
            if (this.highlightPos > l) {
                this.displayPos = this.displayPos + (this.highlightPos - l);
            } else if (this.highlightPos <= this.displayPos) {
                this.displayPos = this.displayPos - (this.displayPos - this.highlightPos);
            }
            this.displayPos = Mth.clamp(this.displayPos, 0, j);
        }
    }

    public void setCanLoseFocus(boolean bl) {
        this.canLoseFocus = bl;
    }

    public boolean isVisible() {
        return this.f_93624_;
    }

    public void setVisible(boolean bl) {
        this.f_93624_ = bl;
    }

    public void setSuggestion(@Nullable String string) {
        this.suggestion = string;
    }

    public int getScreenX(int i) {
        return i > this.value.length() ? this.m_252754_() : this.m_252754_() + this.font.width(this.value.substring(0, i));
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.TITLE, this.createNarrationMessage());
        if (this.isMouseOverCross) {
            narrationElementOutput.add(NarratedElementType.USAGE, Component.translatable("narration.jade.clear_content.usage"));
        }
    }

    public void setHint(Component component) {
        this.hint = component;
    }
}