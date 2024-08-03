package net.minecraft.client.gui.components;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
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

public class EditBox extends AbstractWidget implements Renderable {

    public static final int BACKWARDS = -1;

    public static final int FORWARDS = 1;

    private static final int CURSOR_INSERT_WIDTH = 1;

    private static final int CURSOR_INSERT_COLOR = -3092272;

    private static final String CURSOR_APPEND_CHARACTER = "_";

    public static final int DEFAULT_TEXT_COLOR = 14737632;

    private static final int BORDER_COLOR_FOCUSED = -1;

    private static final int BORDER_COLOR = -6250336;

    private static final int BACKGROUND_COLOR = -16777216;

    private final Font font;

    private String value = "";

    private int maxLength = 32;

    private int frame;

    private boolean bordered = true;

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

    @Nullable
    private Consumer<String> responder;

    private Predicate<String> filter = Objects::nonNull;

    private BiFunction<String, Integer, FormattedCharSequence> formatter = (p_94147_, p_94148_) -> FormattedCharSequence.forward(p_94147_, Style.EMPTY);

    @Nullable
    private Component hint;

    public EditBox(Font font0, int int1, int int2, int int3, int int4, Component component5) {
        this(font0, int1, int2, int3, int4, null, component5);
    }

    public EditBox(Font font0, int int1, int int2, int int3, int int4, @Nullable EditBox editBox5, Component component6) {
        super(int1, int2, int3, int4, component6);
        this.font = font0;
        if (editBox5 != null) {
            this.setValue(editBox5.getValue());
        }
    }

    public void setResponder(Consumer<String> consumerString0) {
        this.responder = consumerString0;
    }

    public void setFormatter(BiFunction<String, Integer, FormattedCharSequence> biFunctionStringIntegerFormattedCharSequence0) {
        this.formatter = biFunctionStringIntegerFormattedCharSequence0;
    }

    public void tick() {
        this.frame++;
    }

    @Override
    protected MutableComponent createNarrationMessage() {
        Component $$0 = this.m_6035_();
        return Component.translatable("gui.narrate.editBox", $$0, this.value);
    }

    public void setValue(String string0) {
        if (this.filter.test(string0)) {
            if (string0.length() > this.maxLength) {
                this.value = string0.substring(0, this.maxLength);
            } else {
                this.value = string0;
            }
            this.moveCursorToEnd();
            this.setHighlightPos(this.cursorPos);
            this.onValueChange(string0);
        }
    }

    public String getValue() {
        return this.value;
    }

    public String getHighlighted() {
        int $$0 = Math.min(this.cursorPos, this.highlightPos);
        int $$1 = Math.max(this.cursorPos, this.highlightPos);
        return this.value.substring($$0, $$1);
    }

    public void setFilter(Predicate<String> predicateString0) {
        this.filter = predicateString0;
    }

    public void insertText(String string0) {
        int $$1 = Math.min(this.cursorPos, this.highlightPos);
        int $$2 = Math.max(this.cursorPos, this.highlightPos);
        int $$3 = this.maxLength - this.value.length() - ($$1 - $$2);
        String $$4 = SharedConstants.filterText(string0);
        int $$5 = $$4.length();
        if ($$3 < $$5) {
            $$4 = $$4.substring(0, $$3);
            $$5 = $$3;
        }
        String $$6 = new StringBuilder(this.value).replace($$1, $$2, $$4).toString();
        if (this.filter.test($$6)) {
            this.value = $$6;
            this.setCursorPosition($$1 + $$5);
            this.setHighlightPos(this.cursorPos);
            this.onValueChange(this.value);
        }
    }

    private void onValueChange(String string0) {
        if (this.responder != null) {
            this.responder.accept(string0);
        }
    }

    private void deleteText(int int0) {
        if (Screen.hasControlDown()) {
            this.deleteWords(int0);
        } else {
            this.deleteChars(int0);
        }
    }

    public void deleteWords(int int0) {
        if (!this.value.isEmpty()) {
            if (this.highlightPos != this.cursorPos) {
                this.insertText("");
            } else {
                this.deleteChars(this.getWordPosition(int0) - this.cursorPos);
            }
        }
    }

    public void deleteChars(int int0) {
        if (!this.value.isEmpty()) {
            if (this.highlightPos != this.cursorPos) {
                this.insertText("");
            } else {
                int $$1 = this.getCursorPos(int0);
                int $$2 = Math.min($$1, this.cursorPos);
                int $$3 = Math.max($$1, this.cursorPos);
                if ($$2 != $$3) {
                    String $$4 = new StringBuilder(this.value).delete($$2, $$3).toString();
                    if (this.filter.test($$4)) {
                        this.value = $$4;
                        this.moveCursorTo($$2);
                    }
                }
            }
        }
    }

    public int getWordPosition(int int0) {
        return this.getWordPosition(int0, this.getCursorPosition());
    }

    private int getWordPosition(int int0, int int1) {
        return this.getWordPosition(int0, int1, true);
    }

    private int getWordPosition(int int0, int int1, boolean boolean2) {
        int $$3 = int1;
        boolean $$4 = int0 < 0;
        int $$5 = Math.abs(int0);
        for (int $$6 = 0; $$6 < $$5; $$6++) {
            if (!$$4) {
                int $$7 = this.value.length();
                $$3 = this.value.indexOf(32, $$3);
                if ($$3 == -1) {
                    $$3 = $$7;
                } else {
                    while (boolean2 && $$3 < $$7 && this.value.charAt($$3) == ' ') {
                        $$3++;
                    }
                }
            } else {
                while (boolean2 && $$3 > 0 && this.value.charAt($$3 - 1) == ' ') {
                    $$3--;
                }
                while ($$3 > 0 && this.value.charAt($$3 - 1) != ' ') {
                    $$3--;
                }
            }
        }
        return $$3;
    }

    public void moveCursor(int int0) {
        this.moveCursorTo(this.getCursorPos(int0));
    }

    private int getCursorPos(int int0) {
        return Util.offsetByCodepoints(this.value, this.cursorPos, int0);
    }

    public void moveCursorTo(int int0) {
        this.setCursorPosition(int0);
        if (!this.shiftPressed) {
            this.setHighlightPos(this.cursorPos);
        }
        this.onValueChange(this.value);
    }

    public void setCursorPosition(int int0) {
        this.cursorPos = Mth.clamp(int0, 0, this.value.length());
    }

    public void moveCursorToStart() {
        this.moveCursorTo(0);
    }

    public void moveCursorToEnd() {
        this.moveCursorTo(this.value.length());
    }

    @Override
    public boolean keyPressed(int int0, int int1, int int2) {
        if (!this.canConsumeInput()) {
            return false;
        } else {
            this.shiftPressed = Screen.hasShiftDown();
            if (Screen.isSelectAll(int0)) {
                this.moveCursorToEnd();
                this.setHighlightPos(0);
                return true;
            } else if (Screen.isCopy(int0)) {
                Minecraft.getInstance().keyboardHandler.setClipboard(this.getHighlighted());
                return true;
            } else if (Screen.isPaste(int0)) {
                if (this.isEditable) {
                    this.insertText(Minecraft.getInstance().keyboardHandler.getClipboard());
                }
                return true;
            } else if (Screen.isCut(int0)) {
                Minecraft.getInstance().keyboardHandler.setClipboard(this.getHighlighted());
                if (this.isEditable) {
                    this.insertText("");
                }
                return true;
            } else {
                switch(int0) {
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
    public boolean charTyped(char char0, int int1) {
        if (!this.canConsumeInput()) {
            return false;
        } else if (SharedConstants.isAllowedChatCharacter(char0)) {
            if (this.isEditable) {
                this.insertText(Character.toString(char0));
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onClick(double double0, double double1) {
        int $$2 = Mth.floor(double0) - this.m_252754_();
        if (this.bordered) {
            $$2 -= 4;
        }
        String $$3 = this.font.plainSubstrByWidth(this.value.substring(this.displayPos), this.getInnerWidth());
        this.moveCursorTo(this.font.plainSubstrByWidth($$3, $$2).length() + this.displayPos);
    }

    @Override
    public void playDownSound(SoundManager soundManager0) {
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        if (this.isVisible()) {
            if (this.isBordered()) {
                int $$4 = this.m_93696_() ? -1 : -6250336;
                guiGraphics0.fill(this.m_252754_() - 1, this.m_252907_() - 1, this.m_252754_() + this.f_93618_ + 1, this.m_252907_() + this.f_93619_ + 1, $$4);
                guiGraphics0.fill(this.m_252754_(), this.m_252907_(), this.m_252754_() + this.f_93618_, this.m_252907_() + this.f_93619_, -16777216);
            }
            int $$5 = this.isEditable ? this.textColor : this.textColorUneditable;
            int $$6 = this.cursorPos - this.displayPos;
            int $$7 = this.highlightPos - this.displayPos;
            String $$8 = this.font.plainSubstrByWidth(this.value.substring(this.displayPos), this.getInnerWidth());
            boolean $$9 = $$6 >= 0 && $$6 <= $$8.length();
            boolean $$10 = this.m_93696_() && this.frame / 6 % 2 == 0 && $$9;
            int $$11 = this.bordered ? this.m_252754_() + 4 : this.m_252754_();
            int $$12 = this.bordered ? this.m_252907_() + (this.f_93619_ - 8) / 2 : this.m_252907_();
            int $$13 = $$11;
            if ($$7 > $$8.length()) {
                $$7 = $$8.length();
            }
            if (!$$8.isEmpty()) {
                String $$14 = $$9 ? $$8.substring(0, $$6) : $$8;
                $$13 = guiGraphics0.drawString(this.font, (FormattedCharSequence) this.formatter.apply($$14, this.displayPos), $$11, $$12, $$5);
            }
            boolean $$15 = this.cursorPos < this.value.length() || this.value.length() >= this.getMaxLength();
            int $$16 = $$13;
            if (!$$9) {
                $$16 = $$6 > 0 ? $$11 + this.f_93618_ : $$11;
            } else if ($$15) {
                $$16 = $$13 - 1;
                $$13--;
            }
            if (!$$8.isEmpty() && $$9 && $$6 < $$8.length()) {
                guiGraphics0.drawString(this.font, (FormattedCharSequence) this.formatter.apply($$8.substring($$6), this.cursorPos), $$13, $$12, $$5);
            }
            if (this.hint != null && $$8.isEmpty() && !this.m_93696_()) {
                guiGraphics0.drawString(this.font, this.hint, $$13, $$12, $$5);
            }
            if (!$$15 && this.suggestion != null) {
                guiGraphics0.drawString(this.font, this.suggestion, $$16 - 1, $$12, -8355712);
            }
            if ($$10) {
                if ($$15) {
                    guiGraphics0.fill(RenderType.guiOverlay(), $$16, $$12 - 1, $$16 + 1, $$12 + 1 + 9, -3092272);
                } else {
                    guiGraphics0.drawString(this.font, "_", $$16, $$12, $$5);
                }
            }
            if ($$7 != $$6) {
                int $$17 = $$11 + this.font.width($$8.substring(0, $$7));
                this.renderHighlight(guiGraphics0, $$16, $$12 - 1, $$17 - 1, $$12 + 1 + 9);
            }
        }
    }

    private void renderHighlight(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4) {
        if (int1 < int3) {
            int $$5 = int1;
            int1 = int3;
            int3 = $$5;
        }
        if (int2 < int4) {
            int $$6 = int2;
            int2 = int4;
            int4 = $$6;
        }
        if (int3 > this.m_252754_() + this.f_93618_) {
            int3 = this.m_252754_() + this.f_93618_;
        }
        if (int1 > this.m_252754_() + this.f_93618_) {
            int1 = this.m_252754_() + this.f_93618_;
        }
        guiGraphics0.fill(RenderType.guiTextHighlight(), int1, int2, int3, int4, -16776961);
    }

    public void setMaxLength(int int0) {
        this.maxLength = int0;
        if (this.value.length() > int0) {
            this.value = this.value.substring(0, int0);
            this.onValueChange(this.value);
        }
    }

    private int getMaxLength() {
        return this.maxLength;
    }

    public int getCursorPosition() {
        return this.cursorPos;
    }

    private boolean isBordered() {
        return this.bordered;
    }

    public void setBordered(boolean boolean0) {
        this.bordered = boolean0;
    }

    public void setTextColor(int int0) {
        this.textColor = int0;
    }

    public void setTextColorUneditable(int int0) {
        this.textColorUneditable = int0;
    }

    @Nullable
    @Override
    public ComponentPath nextFocusPath(FocusNavigationEvent focusNavigationEvent0) {
        return this.f_93624_ && this.isEditable ? super.nextFocusPath(focusNavigationEvent0) : null;
    }

    @Override
    public boolean isMouseOver(double double0, double double1) {
        return this.f_93624_ && double0 >= (double) this.m_252754_() && double0 < (double) (this.m_252754_() + this.f_93618_) && double1 >= (double) this.m_252907_() && double1 < (double) (this.m_252907_() + this.f_93619_);
    }

    @Override
    public void setFocused(boolean boolean0) {
        if (this.canLoseFocus || boolean0) {
            super.setFocused(boolean0);
            if (boolean0) {
                this.frame = 0;
            }
        }
    }

    private boolean isEditable() {
        return this.isEditable;
    }

    public void setEditable(boolean boolean0) {
        this.isEditable = boolean0;
    }

    public int getInnerWidth() {
        return this.isBordered() ? this.f_93618_ - 8 : this.f_93618_;
    }

    public void setHighlightPos(int int0) {
        int $$1 = this.value.length();
        this.highlightPos = Mth.clamp(int0, 0, $$1);
        if (this.font != null) {
            if (this.displayPos > $$1) {
                this.displayPos = $$1;
            }
            int $$2 = this.getInnerWidth();
            String $$3 = this.font.plainSubstrByWidth(this.value.substring(this.displayPos), $$2);
            int $$4 = $$3.length() + this.displayPos;
            if (this.highlightPos == this.displayPos) {
                this.displayPos = this.displayPos - this.font.plainSubstrByWidth(this.value, $$2, true).length();
            }
            if (this.highlightPos > $$4) {
                this.displayPos = this.displayPos + (this.highlightPos - $$4);
            } else if (this.highlightPos <= this.displayPos) {
                this.displayPos = this.displayPos - (this.displayPos - this.highlightPos);
            }
            this.displayPos = Mth.clamp(this.displayPos, 0, $$1);
        }
    }

    public void setCanLoseFocus(boolean boolean0) {
        this.canLoseFocus = boolean0;
    }

    public boolean isVisible() {
        return this.f_93624_;
    }

    public void setVisible(boolean boolean0) {
        this.f_93624_ = boolean0;
    }

    public void setSuggestion(@Nullable String string0) {
        this.suggestion = string0;
    }

    public int getScreenX(int int0) {
        return int0 > this.value.length() ? this.m_252754_() : this.m_252754_() + this.font.width(this.value.substring(0, int0));
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput narrationElementOutput0) {
        narrationElementOutput0.add(NarratedElementType.TITLE, this.createNarrationMessage());
    }

    public void setHint(Component component0) {
        this.hint = component0;
    }
}