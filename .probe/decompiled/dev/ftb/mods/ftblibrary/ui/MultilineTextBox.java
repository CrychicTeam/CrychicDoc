package dev.ftb.mods.ftblibrary.ui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.ftb.mods.ftblibrary.core.mixin.common.MultilineTextFieldAccess;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.ui.input.Key;
import dev.ftb.mods.ftblibrary.ui.input.KeyModifiers;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import java.util.function.Consumer;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.MultilineTextField;
import net.minecraft.client.gui.components.Whence;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class MultilineTextBox extends Widget implements IFocusableWidget {

    private final Font font;

    private boolean isFocused = false;

    private MultilineTextField textField;

    private Component placeHolder = Component.empty();

    private int frame;

    private Consumer<String> valueListener = str -> {
    };

    public MultilineTextBox(Panel panel) {
        super(panel);
        this.font = this.getGui().getTheme().getFont();
        this.createTextField("", 100);
    }

    @Override
    public void setWidth(int v) {
        super.setWidth(v);
        this.createTextField(this.textField.value(), this.width);
        this.recalculateHeight();
    }

    private void createTextField(String text, int width) {
        this.textField = new MultilineTextField(this.font, width);
        this.textField.setCursorListener(this::scrollToCursor);
        this.textField.setValue(text);
        this.textField.setValueListener(this.valueListener);
    }

    public void setValueListener(Consumer<String> valueListener) {
        this.valueListener = valueListener;
        this.textField.setValueListener(valueListener);
    }

    @Override
    public final boolean isFocused() {
        return this.isFocused;
    }

    @Override
    public final void setFocused(boolean focused) {
        this.isFocused = focused;
        if (focused) {
            this.getGui().setFocusedWidget(this);
        }
    }

    public String getText() {
        return this.textField.value();
    }

    public void setText(String text) {
        this.textField.setValue(text);
        this.recalculateHeight();
    }

    private void recalculateHeight() {
        this.height = this.innerPadding() * 2 + this.textField.getLineCount() * 9;
    }

    public void seekCursor(Whence whence, int pos) {
        this.textField.seekCursor(whence, pos);
    }

    public void setPlaceHolder(Component placeHolder) {
        this.placeHolder = placeHolder;
    }

    public void setSelecting(boolean selecting) {
        this.textField.setSelecting(selecting);
    }

    public boolean hasSelection() {
        return this.textField.hasSelection();
    }

    public String getSelectedText() {
        return this.textField.getSelectedText();
    }

    public void insertText(String toInsert) {
        this.textField.insertText(toInsert);
        this.recalculateHeight();
    }

    public int cursorPos() {
        return this.textField.cursor();
    }

    public void selectCurrentLine() {
        MultilineTextField.StringView view = this.textField.getLineView(this.textField.getLineAtCursor());
        this.textField.setSelecting(false);
        this.textField.seekCursor(Whence.ABSOLUTE, view.beginIndex());
        this.textField.setSelecting(true);
        this.textField.seekCursor(Whence.ABSOLUTE, view.endIndex());
    }

    public MultilineTextBox.StringExtents getLineView() {
        return MultilineTextBox.StringExtents.of(this.textField.getLineView(this.textField.getLineAtCursor()));
    }

    public MultilineTextBox.StringExtents getLineView(int line) {
        return MultilineTextBox.StringExtents.of(this.textField.getLineView(line));
    }

    public MultilineTextBox.StringExtents getSelected() {
        return MultilineTextBox.StringExtents.of(this.textField.getSelected());
    }

    @Override
    public void tick() {
        this.frame++;
    }

    @Override
    public boolean mousePressed(MouseButton button) {
        if (super.mousePressed(button)) {
            return true;
        } else if (this.isMouseOver()) {
            if (button.isLeft()) {
                this.setFocused(true);
                this.textField.setSelecting(Screen.hasShiftDown());
                this.setCursorPos(this.getMouseX(), (int) ((double) this.getMouseY() - this.parent.getScrollY()));
            }
            return true;
        } else {
            this.setFocused(false);
            return false;
        }
    }

    @Override
    public boolean mouseDoubleClicked(MouseButton button) {
        if (super.mouseDoubleClicked(button)) {
            return true;
        } else if (this.isMouseOver() && button.isLeft()) {
            this.setCursorPos(this.getMouseX(), (int) ((double) this.getMouseY() - this.parent.getScrollY()));
            MultilineTextField.StringView view = this.textField.getPreviousWord();
            this.textField.seekCursor(Whence.ABSOLUTE, view.beginIndex());
            ((MultilineTextFieldAccess) this.textField).setSelectCursor(view.endIndex());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean mouseDragged(int button, double dragX, double dragY) {
        if (super.mouseDragged(button, dragX, dragY)) {
            return true;
        } else if (this.isMouseOver()) {
            this.textField.setSelecting(true);
            this.setCursorPos(this.getMouseX(), (int) ((double) this.getMouseY() - this.parent.getScrollY()));
            this.textField.setSelecting(Screen.hasShiftDown());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean keyPressed(Key key) {
        boolean res = this.textField.keyPressed(key.keyCode);
        this.recalculateHeight();
        return res;
    }

    @Override
    public boolean charTyped(char c, KeyModifiers modifiers) {
        if (this.isFocused() && SharedConstants.isAllowedChatCharacter(c)) {
            this.textField.insertText(Character.toString(c));
            this.recalculateHeight();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        String s = this.getText();
        if (s.isEmpty() && !this.isFocused()) {
            theme.drawString(graphics, this.placeHolder, x + 4, y + 4, 1);
        } else {
            int cursorPos = this.textField.cursor();
            boolean drawCursor = this.isFocused() && this.frame / 6 % 2 == 0;
            boolean cursorInRange = cursorPos < s.length();
            int xPos = 0;
            int k = 0;
            int yPos = this.getY() + this.innerPadding();
            for (MultilineTextField.StringView stringview : this.textField.iterateLines()) {
                boolean shouldDrawLine = this.withinContentArea(yPos, yPos + 9);
                if (drawCursor && cursorInRange && cursorPos >= stringview.beginIndex() && cursorPos <= stringview.endIndex()) {
                    if (shouldDrawLine) {
                        xPos = theme.drawString(graphics, s.substring(stringview.beginIndex(), cursorPos), this.getX() + this.innerPadding(), yPos, Color4I.rgb(14737632), 0);
                        Color4I.rgb(10526880).draw(graphics, xPos - 1, yPos, 1, 9);
                        theme.drawString(graphics, s.substring(cursorPos, stringview.endIndex()), xPos, yPos, Color4I.rgb(14737632), 0);
                    }
                } else {
                    if (shouldDrawLine) {
                        xPos = theme.drawString(graphics, s.substring(stringview.beginIndex(), stringview.endIndex()), this.getX() + this.innerPadding(), yPos, Color4I.rgb(14737632), 0);
                    }
                    k = yPos;
                }
                yPos += 9;
            }
            if (drawCursor && !cursorInRange && this.withinContentArea(k, k + 9)) {
                theme.drawString(graphics, "_", xPos, k, Color4I.rgb(10526880), 0);
            }
            if (this.textField.hasSelection()) {
                MultilineTextField.StringView stringView = this.textField.getSelected();
                int xPos1 = this.getX() + this.innerPadding();
                yPos = this.getY() + this.innerPadding();
                for (MultilineTextField.StringView stringView1 : this.textField.iterateLines()) {
                    if (stringView.beginIndex() <= stringView1.endIndex()) {
                        if (stringView1.beginIndex() > stringView.endIndex()) {
                            break;
                        }
                        if (this.withinContentArea(yPos, yPos + 9)) {
                            int xOff1 = this.font.width(s.substring(stringView1.beginIndex(), Math.max(stringView.beginIndex(), stringView1.beginIndex())));
                            int xOff2 = stringView.endIndex() > stringView1.endIndex() ? this.width - this.innerPadding() : this.font.width(s.substring(stringView1.beginIndex(), stringView.endIndex()));
                            this.renderHighlight(graphics, xPos1 + xOff1, yPos, xPos1 + xOff2, yPos + 9);
                        }
                    }
                    yPos += 9;
                }
            }
        }
    }

    private void renderHighlight(GuiGraphics graphics, int x1, int y1, int x2, int y2) {
        RenderSystem.enableColorLogicOp();
        RenderSystem.logicOp(GlStateManager.LogicOp.OR_REVERSE);
        Color4I.rgb(255).draw(graphics, x1, y1, x2 - x1, y2 - y1);
        RenderSystem.disableColorLogicOp();
    }

    private boolean withinContentArea(int y1, int y2) {
        return (double) y1 - this.parent.getScrollY() >= (double) this.getY() && (double) y2 - this.parent.getScrollY() <= (double) (this.getY() + this.height);
    }

    private void setCursorPos(int mouseX, int mouseY) {
        double x = (double) mouseX - (double) this.getX() - (double) this.innerPadding();
        double y = (double) mouseY - (double) this.getY() - (double) this.innerPadding() + this.parent.getScrollY();
        this.seekCursorToPoint(x, y);
    }

    public void seekCursorToPoint(double x, double y) {
        int x1 = Mth.floor(x);
        int y1 = Mth.floor(y / 9.0);
        MultilineTextField.StringView stringView = this.textField.getLineView(Mth.clamp(y1, 0, this.textField.getLineCount() - 1));
        String lineSection = this.font.plainSubstrByWidth(this.textField.value().substring(stringView.beginIndex(), stringView.endIndex()), x1);
        int k = lineSection.length();
        this.textField.seekCursor(Whence.ABSOLUTE, stringView.beginIndex() + k);
        if (this.textField.cursor() < this.textField.value().length()) {
            int w1 = this.font.width(lineSection);
            if (x1 <= this.font.width(this.textField.value().substring(stringView.beginIndex(), stringView.endIndex()))) {
                String c = String.valueOf(this.textField.value().charAt(this.textField.cursor()));
                int w2 = this.font.width(c) / 2;
                if (x1 - w1 >= w2) {
                    this.textField.seekCursor(Whence.RELATIVE, 1);
                }
            }
        }
    }

    private void scrollToCursor() {
        int lh = 9;
        double d0 = this.parent.getScrollY();
        MultilineTextField.StringView stringView = this.textField.getLineView((int) (d0 / (double) lh));
        if (this.textField.cursor() <= stringView.beginIndex()) {
            d0 = (double) (this.textField.getLineAtCursor() * lh);
        } else {
            MultilineTextField.StringView stringView1 = this.textField.getLineView((int) ((d0 + (double) this.parent.height) / (double) lh) - 1);
            if (this.textField.cursor() > stringView1.endIndex()) {
                d0 = (double) (this.textField.getLineAtCursor() * lh - this.parent.height + lh + this.innerPadding() * 2);
            }
        }
        this.parent.setScrollY(d0);
    }

    private int innerPadding() {
        return 4;
    }

    public static record StringExtents(int start, int end) {

        public static MultilineTextBox.StringExtents of(MultilineTextField.StringView view) {
            return new MultilineTextBox.StringExtents(view.beginIndex(), view.endIndex());
        }
    }
}