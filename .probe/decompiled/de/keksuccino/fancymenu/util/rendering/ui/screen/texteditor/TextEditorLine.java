package de.keksuccino.fancymenu.util.rendering.ui.screen.texteditor;

import de.keksuccino.fancymenu.mixin.mixins.common.client.IMixinEditBox;
import de.keksuccino.konkrete.gui.content.AdvancedTextField;
import de.keksuccino.konkrete.input.CharacterFilter;
import de.keksuccino.konkrete.input.MouseInput;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TextEditorLine extends AdvancedTextField {

    private static final Logger LOGGER = LogManager.getLogger();

    public TextEditorScreen parent;

    protected String lastTickValue = "";

    public boolean isInMouseHighlightingMode = false;

    protected final Font font2;

    protected final boolean handleSelf2;

    public int textWidth = 0;

    public int lineIndex = 0;

    protected int currentHighlightPosXStart = 0;

    protected int currentHighlightPosXEnd = 0;

    protected int currentCharacterRenderIndex = 0;

    protected static boolean leftRightArrowWasDown = false;

    public TextEditorLine(Font font, int x, int y, int width, int height, boolean handleSelf, @Nullable CharacterFilter characterFilter, TextEditorScreen parent) {
        super(font, x, y, width, height, handleSelf, characterFilter);
        this.parent = parent;
        this.font2 = font;
        this.handleSelf2 = handleSelf;
        this.m_94182_(false);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        if (this.isInEditorArea()) {
            super.m_88315_(graphics, mouseX, mouseY, partial);
        }
        this.lastTickValue = this.m_94155_();
    }

    protected MutableComponent getFormattedText(String text) {
        List<Component> chars = new ArrayList();
        for (char c : text.toCharArray()) {
            Style style = Style.EMPTY;
            for (TextEditorFormattingRule r : this.parent.formattingRules) {
                Style rs = r.getStyle(c, this.currentCharacterRenderIndex, this.m_94207_(), this, this.parent.currentRenderCharacterIndexTotal, this.parent);
                if (rs != null && rs != Style.EMPTY) {
                    style = rs.applyTo(style);
                }
            }
            chars.add(Component.literal(String.valueOf(c)).withStyle(style));
            this.currentCharacterRenderIndex++;
            this.parent.currentRenderCharacterIndexTotal++;
        }
        MutableComponent comp = Component.literal("");
        for (Component c : chars) {
            comp.append(c);
        }
        return comp;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        this.currentCharacterRenderIndex = 0;
        this.m_94202_(this.parent.textColor.getRGB());
        this.m_94205_(this.parent.textColor.getRGB());
        if (this.m_94213_()) {
            if (this.m_93696_()) {
                graphics.fill(0, this.m_252907_(), this.parent.f_96543_, this.m_252907_() + this.f_93619_, this.parent.focusedLineColor.getRGB());
            }
            int textColorInt = this.m_94222_() ? this.getAsAccessor().getTextColorFancyMenu() : this.getAsAccessor().getTextColorUneditableFancyMenu();
            int cursorPos = this.m_94207_() - this.getAsAccessor().getDisplayPosFancyMenu();
            int highlightPos = this.getAsAccessor().getHighlightPosFancyMenu() - this.getAsAccessor().getDisplayPosFancyMenu();
            String text = this.m_94155_();
            boolean isCursorNotAtStartOrEnd = cursorPos >= 0 && cursorPos <= text.length();
            boolean renderCursor = this.m_93696_() && this.getAsAccessor().getFrameFancyMenu() / 6 % 2 == 0 && isCursorNotAtStartOrEnd;
            int textX = this.getAsAccessor().getBorderedFancyMenu() ? this.m_252754_() + 4 : this.m_252754_() + 1;
            int textY = this.getAsAccessor().getBorderedFancyMenu() ? this.m_252907_() + (this.f_93619_ - 8) / 2 : this.m_252907_() + Math.max(0, this.m_93694_() / 2) - 9 / 2;
            int textXRender = textX;
            if (highlightPos > text.length()) {
                highlightPos = text.length();
            }
            if (!text.isEmpty()) {
                String textBeforeCursor = isCursorNotAtStartOrEnd ? text.substring(0, cursorPos) : text;
                textXRender = graphics.drawString(this.font2, this.getFormattedText(textBeforeCursor), textX, textY, textColorInt, false);
            }
            boolean isCursorNotAtEndOfLine = this.m_94207_() < this.m_94155_().length() || this.m_94155_().length() >= this.getAsAccessor().getMaxLengthFancyMenu();
            int cursorPosRender = textXRender;
            if (!isCursorNotAtStartOrEnd) {
                cursorPosRender = cursorPos > 0 ? textX + this.f_93618_ : textX;
            } else if (isCursorNotAtEndOfLine) {
                cursorPosRender = textXRender - 1;
            }
            if (!text.isEmpty() && isCursorNotAtStartOrEnd && cursorPos < text.length()) {
                graphics.drawString(this.font2, this.getFormattedText(text.substring(cursorPos)), textXRender, textY, textColorInt, false);
            }
            if (this.getAsAccessor().getHintFancyMenu() != null && text.isEmpty() && !this.m_93696_()) {
                graphics.drawString(this.font2, this.getAsAccessor().getHintFancyMenu(), textXRender, textY, textColorInt, false);
            }
            if (!isCursorNotAtEndOfLine && this.getAsAccessor().getSuggestionFancyMenu() != null) {
                graphics.drawString(this.font2, this.getAsAccessor().getSuggestionFancyMenu(), cursorPosRender - 1, textY, -8355712, false);
            }
            if (renderCursor) {
                if (isCursorNotAtEndOfLine) {
                    graphics.fill(cursorPosRender, textY - 1, cursorPosRender + 1, textY + 1 + 9, textColorInt);
                } else {
                    graphics.drawString(this.font2, "_", cursorPosRender, textY, textColorInt, false);
                }
            }
            if (highlightPos != cursorPos) {
                this.currentHighlightPosXStart = cursorPosRender;
                this.currentHighlightPosXEnd = textX + this.font2.width(text.substring(0, highlightPos)) - 1;
                this.getAsAccessor().invokeRenderHighlightFancyMenu(graphics, this.currentHighlightPosXStart, textY - 1, this.currentHighlightPosXEnd, textY + 1 + 9);
            } else {
                this.currentHighlightPosXStart = 0;
                this.currentHighlightPosXEnd = 0;
            }
        }
    }

    public boolean isInEditorArea() {
        return this.m_252907_() + this.m_93694_() >= this.parent.getEditorAreaY() && this.m_252907_() <= this.parent.getEditorAreaY() + this.parent.getEditorAreaHeight();
    }

    public boolean isHighlightedHovered() {
        if (this.isInEditorArea() && this.currentHighlightPosXStart != this.currentHighlightPosXEnd && this.m_274382_()) {
            int mouseX = MouseInput.getMouseX();
            return mouseX >= Math.min(this.currentHighlightPosXStart, this.currentHighlightPosXEnd) && mouseX <= Math.max(this.currentHighlightPosXStart, this.currentHighlightPosXEnd);
        } else {
            return false;
        }
    }

    public int getActualHeight() {
        int h = this.f_93619_;
        if (this.getAsAccessor().getBorderedFancyMenu()) {
            h += 2;
        }
        return h;
    }

    public IMixinEditBox getAsAccessor() {
        return (IMixinEditBox) this;
    }

    @Override
    public void setCursorPosition(int newPos) {
        this.textWidth = this.font2.width(this.m_94155_());
        super.m_94196_(newPos);
        if (newPos != this.parent.lastCursorPosSetByUser && this.m_93696_()) {
            this.parent.lastCursorPosSetByUser = this.m_94207_();
        }
        this.parent.correctXScroll(this);
    }

    @Override
    public void tick() {
        if (!MouseInput.isLeftMouseDown() && this.isInMouseHighlightingMode) {
            this.isInMouseHighlightingMode = false;
        }
        super.m_94120_();
        leftRightArrowWasDown = false;
    }

    @Override
    public boolean keyPressed(int keycode, int i1, int i2) {
        if (Screen.isCopy(keycode) || Screen.isPaste(keycode) || Screen.isSelectAll(keycode) || Screen.isCut(keycode)) {
            return false;
        } else if (keycode == 259) {
            return false;
        } else if ((keycode == 262 || keycode == 263) && this.parent.isInMouseHighlightingMode()) {
            return false;
        } else {
            if (keycode == 263) {
                if (leftRightArrowWasDown) {
                    return true;
                }
                if (this.parent.isLineFocused() && this.parent.getFocusedLine() == this && this.m_94207_() <= 0 && this.parent.getLineIndex(this) > 0) {
                    leftRightArrowWasDown = true;
                    this.parent.goUpLine();
                    this.parent.getFocusedLine().m_94192_(this.parent.getFocusedLine().m_94155_().length());
                    this.parent.correctYScroll(0);
                    return true;
                }
            }
            if (keycode == 262) {
                if (leftRightArrowWasDown) {
                    return true;
                }
                if (this.parent.isLineFocused() && this.parent.getFocusedLine() == this && this.m_94207_() >= this.m_94155_().length() && this.parent.getLineIndex(this) < this.parent.getLineCount() - 1) {
                    leftRightArrowWasDown = true;
                    this.parent.goDownLine(false);
                    this.parent.getFocusedLine().m_94192_(0);
                    this.parent.correctYScroll(0);
                    return true;
                }
            }
            return super.m_7933_(keycode, i1, i2);
        }
    }

    @Override
    public void deleteChars(int i) {
        if (!this.parent.justSwitchedLineByWordDeletion) {
            if (this.m_94207_() == 0 && this.parent.getFocusedLineIndex() > 0) {
                int lastLineIndex = this.parent.getFocusedLineIndex();
                this.parent.justSwitchedLineByWordDeletion = true;
                this.parent.goUpLine();
                this.parent.getFocusedLine().m_94201_();
                this.parent.getFocusedLine().insertText(this.m_94155_());
                this.parent.getFocusedLine().setCursorPosition(this.parent.getFocusedLine().m_94207_() - this.m_94155_().length());
                this.parent.getFocusedLine().m_94208_(this.parent.getFocusedLine().m_94207_());
                if (lastLineIndex > 0) {
                    this.parent.removeLineAtIndex(this.parent.getFocusedLineIndex() + 1);
                    this.parent.correctYScroll(-1);
                }
            } else {
                super.m_94180_(i);
            }
        }
        this.textWidth = this.font2.width(this.m_94155_());
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (this.parent.isMouseInsideEditorArea() && !this.parent.rightClickContextMenu.isOpen()) {
            if (mouseButton == 0 && this.m_274382_() && !this.isInMouseHighlightingMode && this.m_94213_()) {
                if (!this.parent.isAtLeastOneLineInHighlightMode()) {
                    this.parent.startHighlightLine = this;
                }
                this.isInMouseHighlightingMode = true;
                this.parent.setFocusedLine(Math.max(0, this.parent.getLineIndex(this)));
                super.m_6375_(mouseX, mouseY, mouseButton);
                this.getAsAccessor().setShiftPressedFancyMenu(false);
                this.m_94208_(this.m_94207_());
            } else if (mouseButton == 0 && !this.m_274382_()) {
                this.m_94208_(this.m_94207_());
            }
            return !this.isInMouseHighlightingMode && mouseButton == 0 ? super.m_6375_(mouseX, mouseY, mouseButton) : true;
        } else {
            return false;
        }
    }

    @Override
    public void setValue(String string0) {
        super.m_94144_(string0);
        this.textWidth = this.font2.width(this.m_94155_());
    }

    @Override
    public void insertText(String textToWrite) {
        super.m_94164_(textToWrite);
        this.textWidth = this.font2.width(this.m_94155_());
    }

    @Override
    public void setMaxLength(int int0) {
        super.m_94199_(int0);
        this.textWidth = this.font2.width(this.m_94155_());
    }
}