package net.minecraft.client.gui.components;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;

public class MultilineTextField {

    public static final int NO_CHARACTER_LIMIT = Integer.MAX_VALUE;

    private static final int LINE_SEEK_PIXEL_BIAS = 2;

    private final Font font;

    private final List<MultilineTextField.StringView> displayLines = Lists.newArrayList();

    private String value;

    private int cursor;

    private int selectCursor;

    private boolean selecting;

    private int characterLimit = Integer.MAX_VALUE;

    private final int width;

    private Consumer<String> valueListener = p_239235_ -> {
    };

    private Runnable cursorListener = () -> {
    };

    public MultilineTextField(Font font0, int int1) {
        this.font = font0;
        this.width = int1;
        this.setValue("");
    }

    public int characterLimit() {
        return this.characterLimit;
    }

    public void setCharacterLimit(int int0) {
        if (int0 < 0) {
            throw new IllegalArgumentException("Character limit cannot be negative");
        } else {
            this.characterLimit = int0;
        }
    }

    public boolean hasCharacterLimit() {
        return this.characterLimit != Integer.MAX_VALUE;
    }

    public void setValueListener(Consumer<String> consumerString0) {
        this.valueListener = consumerString0;
    }

    public void setCursorListener(Runnable runnable0) {
        this.cursorListener = runnable0;
    }

    public void setValue(String string0) {
        this.value = this.truncateFullText(string0);
        this.cursor = this.value.length();
        this.selectCursor = this.cursor;
        this.onValueChange();
    }

    public String value() {
        return this.value;
    }

    public void insertText(String string0) {
        if (!string0.isEmpty() || this.hasSelection()) {
            String $$1 = this.truncateInsertionText(SharedConstants.filterText(string0, true));
            MultilineTextField.StringView $$2 = this.getSelected();
            this.value = new StringBuilder(this.value).replace($$2.beginIndex, $$2.endIndex, $$1).toString();
            this.cursor = $$2.beginIndex + $$1.length();
            this.selectCursor = this.cursor;
            this.onValueChange();
        }
    }

    public void deleteText(int int0) {
        if (!this.hasSelection()) {
            this.selectCursor = Mth.clamp(this.cursor + int0, 0, this.value.length());
        }
        this.insertText("");
    }

    public int cursor() {
        return this.cursor;
    }

    public void setSelecting(boolean boolean0) {
        this.selecting = boolean0;
    }

    public MultilineTextField.StringView getSelected() {
        return new MultilineTextField.StringView(Math.min(this.selectCursor, this.cursor), Math.max(this.selectCursor, this.cursor));
    }

    public int getLineCount() {
        return this.displayLines.size();
    }

    public int getLineAtCursor() {
        for (int $$0 = 0; $$0 < this.displayLines.size(); $$0++) {
            MultilineTextField.StringView $$1 = (MultilineTextField.StringView) this.displayLines.get($$0);
            if (this.cursor >= $$1.beginIndex && this.cursor <= $$1.endIndex) {
                return $$0;
            }
        }
        return -1;
    }

    public MultilineTextField.StringView getLineView(int int0) {
        return (MultilineTextField.StringView) this.displayLines.get(Mth.clamp(int0, 0, this.displayLines.size() - 1));
    }

    public void seekCursor(Whence whence0, int int1) {
        switch(whence0) {
            case ABSOLUTE:
                this.cursor = int1;
                break;
            case RELATIVE:
                this.cursor += int1;
                break;
            case END:
                this.cursor = this.value.length() + int1;
        }
        this.cursor = Mth.clamp(this.cursor, 0, this.value.length());
        this.cursorListener.run();
        if (!this.selecting) {
            this.selectCursor = this.cursor;
        }
    }

    public void seekCursorLine(int int0) {
        if (int0 != 0) {
            int $$1 = this.font.width(this.value.substring(this.getCursorLineView().beginIndex, this.cursor)) + 2;
            MultilineTextField.StringView $$2 = this.getCursorLineView(int0);
            int $$3 = this.font.plainSubstrByWidth(this.value.substring($$2.beginIndex, $$2.endIndex), $$1).length();
            this.seekCursor(Whence.ABSOLUTE, $$2.beginIndex + $$3);
        }
    }

    public void seekCursorToPoint(double double0, double double1) {
        int $$2 = Mth.floor(double0);
        int $$3 = Mth.floor(double1 / 9.0);
        MultilineTextField.StringView $$4 = (MultilineTextField.StringView) this.displayLines.get(Mth.clamp($$3, 0, this.displayLines.size() - 1));
        int $$5 = this.font.plainSubstrByWidth(this.value.substring($$4.beginIndex, $$4.endIndex), $$2).length();
        this.seekCursor(Whence.ABSOLUTE, $$4.beginIndex + $$5);
    }

    public boolean keyPressed(int int0) {
        this.selecting = Screen.hasShiftDown();
        if (Screen.isSelectAll(int0)) {
            this.cursor = this.value.length();
            this.selectCursor = 0;
            return true;
        } else if (Screen.isCopy(int0)) {
            Minecraft.getInstance().keyboardHandler.setClipboard(this.getSelectedText());
            return true;
        } else if (Screen.isPaste(int0)) {
            this.insertText(Minecraft.getInstance().keyboardHandler.getClipboard());
            return true;
        } else if (Screen.isCut(int0)) {
            Minecraft.getInstance().keyboardHandler.setClipboard(this.getSelectedText());
            this.insertText("");
            return true;
        } else {
            switch(int0) {
                case 257:
                case 335:
                    this.insertText("\n");
                    return true;
                case 259:
                    if (Screen.hasControlDown()) {
                        MultilineTextField.StringView $$3 = this.getPreviousWord();
                        this.deleteText($$3.beginIndex - this.cursor);
                    } else {
                        this.deleteText(-1);
                    }
                    return true;
                case 261:
                    if (Screen.hasControlDown()) {
                        MultilineTextField.StringView $$4 = this.getNextWord();
                        this.deleteText($$4.beginIndex - this.cursor);
                    } else {
                        this.deleteText(1);
                    }
                    return true;
                case 262:
                    if (Screen.hasControlDown()) {
                        MultilineTextField.StringView $$2 = this.getNextWord();
                        this.seekCursor(Whence.ABSOLUTE, $$2.beginIndex);
                    } else {
                        this.seekCursor(Whence.RELATIVE, 1);
                    }
                    return true;
                case 263:
                    if (Screen.hasControlDown()) {
                        MultilineTextField.StringView $$1 = this.getPreviousWord();
                        this.seekCursor(Whence.ABSOLUTE, $$1.beginIndex);
                    } else {
                        this.seekCursor(Whence.RELATIVE, -1);
                    }
                    return true;
                case 264:
                    if (!Screen.hasControlDown()) {
                        this.seekCursorLine(1);
                    }
                    return true;
                case 265:
                    if (!Screen.hasControlDown()) {
                        this.seekCursorLine(-1);
                    }
                    return true;
                case 266:
                    this.seekCursor(Whence.ABSOLUTE, 0);
                    return true;
                case 267:
                    this.seekCursor(Whence.END, 0);
                    return true;
                case 268:
                    if (Screen.hasControlDown()) {
                        this.seekCursor(Whence.ABSOLUTE, 0);
                    } else {
                        this.seekCursor(Whence.ABSOLUTE, this.getCursorLineView().beginIndex);
                    }
                    return true;
                case 269:
                    if (Screen.hasControlDown()) {
                        this.seekCursor(Whence.END, 0);
                    } else {
                        this.seekCursor(Whence.ABSOLUTE, this.getCursorLineView().endIndex);
                    }
                    return true;
                default:
                    return false;
            }
        }
    }

    public Iterable<MultilineTextField.StringView> iterateLines() {
        return this.displayLines;
    }

    public boolean hasSelection() {
        return this.selectCursor != this.cursor;
    }

    @VisibleForTesting
    public String getSelectedText() {
        MultilineTextField.StringView $$0 = this.getSelected();
        return this.value.substring($$0.beginIndex, $$0.endIndex);
    }

    private MultilineTextField.StringView getCursorLineView() {
        return this.getCursorLineView(0);
    }

    private MultilineTextField.StringView getCursorLineView(int int0) {
        int $$1 = this.getLineAtCursor();
        if ($$1 < 0) {
            throw new IllegalStateException("Cursor is not within text (cursor = " + this.cursor + ", length = " + this.value.length() + ")");
        } else {
            return (MultilineTextField.StringView) this.displayLines.get(Mth.clamp($$1 + int0, 0, this.displayLines.size() - 1));
        }
    }

    @VisibleForTesting
    public MultilineTextField.StringView getPreviousWord() {
        if (this.value.isEmpty()) {
            return MultilineTextField.StringView.EMPTY;
        } else {
            int $$0 = Mth.clamp(this.cursor, 0, this.value.length() - 1);
            while ($$0 > 0 && Character.isWhitespace(this.value.charAt($$0 - 1))) {
                $$0--;
            }
            while ($$0 > 0 && !Character.isWhitespace(this.value.charAt($$0 - 1))) {
                $$0--;
            }
            return new MultilineTextField.StringView($$0, this.getWordEndPosition($$0));
        }
    }

    @VisibleForTesting
    public MultilineTextField.StringView getNextWord() {
        if (this.value.isEmpty()) {
            return MultilineTextField.StringView.EMPTY;
        } else {
            int $$0 = Mth.clamp(this.cursor, 0, this.value.length() - 1);
            while ($$0 < this.value.length() && !Character.isWhitespace(this.value.charAt($$0))) {
                $$0++;
            }
            while ($$0 < this.value.length() && Character.isWhitespace(this.value.charAt($$0))) {
                $$0++;
            }
            return new MultilineTextField.StringView($$0, this.getWordEndPosition($$0));
        }
    }

    private int getWordEndPosition(int int0) {
        int $$1 = int0;
        while ($$1 < this.value.length() && !Character.isWhitespace(this.value.charAt($$1))) {
            $$1++;
        }
        return $$1;
    }

    private void onValueChange() {
        this.reflowDisplayLines();
        this.valueListener.accept(this.value);
        this.cursorListener.run();
    }

    private void reflowDisplayLines() {
        this.displayLines.clear();
        if (this.value.isEmpty()) {
            this.displayLines.add(MultilineTextField.StringView.EMPTY);
        } else {
            this.font.getSplitter().splitLines(this.value, this.width, Style.EMPTY, false, (p_239846_, p_239847_, p_239848_) -> this.displayLines.add(new MultilineTextField.StringView(p_239847_, p_239848_)));
            if (this.value.charAt(this.value.length() - 1) == '\n') {
                this.displayLines.add(new MultilineTextField.StringView(this.value.length(), this.value.length()));
            }
        }
    }

    private String truncateFullText(String string0) {
        return this.hasCharacterLimit() ? StringUtil.truncateStringIfNecessary(string0, this.characterLimit, false) : string0;
    }

    private String truncateInsertionText(String string0) {
        if (this.hasCharacterLimit()) {
            int $$1 = this.characterLimit - this.value.length();
            return StringUtil.truncateStringIfNecessary(string0, $$1, false);
        } else {
            return string0;
        }
    }

    protected static record StringView(int f_238590_, int f_238654_) {

        private final int beginIndex;

        private final int endIndex;

        static final MultilineTextField.StringView EMPTY = new MultilineTextField.StringView(0, 0);

        protected StringView(int f_238590_, int f_238654_) {
            this.beginIndex = f_238590_;
            this.endIndex = f_238654_;
        }
    }
}