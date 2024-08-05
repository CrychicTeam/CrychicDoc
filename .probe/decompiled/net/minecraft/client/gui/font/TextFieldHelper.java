package net.minecraft.client.gui.font;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.Mth;

public class TextFieldHelper {

    private final Supplier<String> getMessageFn;

    private final Consumer<String> setMessageFn;

    private final Supplier<String> getClipboardFn;

    private final Consumer<String> setClipboardFn;

    private final Predicate<String> stringValidator;

    private int cursorPos;

    private int selectionPos;

    public TextFieldHelper(Supplier<String> supplierString0, Consumer<String> consumerString1, Supplier<String> supplierString2, Consumer<String> consumerString3, Predicate<String> predicateString4) {
        this.getMessageFn = supplierString0;
        this.setMessageFn = consumerString1;
        this.getClipboardFn = supplierString2;
        this.setClipboardFn = consumerString3;
        this.stringValidator = predicateString4;
        this.setCursorToEnd();
    }

    public static Supplier<String> createClipboardGetter(Minecraft minecraft0) {
        return () -> getClipboardContents(minecraft0);
    }

    public static String getClipboardContents(Minecraft minecraft0) {
        return ChatFormatting.stripFormatting(minecraft0.keyboardHandler.getClipboard().replaceAll("\\r", ""));
    }

    public static Consumer<String> createClipboardSetter(Minecraft minecraft0) {
        return p_95173_ -> setClipboardContents(minecraft0, p_95173_);
    }

    public static void setClipboardContents(Minecraft minecraft0, String string1) {
        minecraft0.keyboardHandler.setClipboard(string1);
    }

    public boolean charTyped(char char0) {
        if (SharedConstants.isAllowedChatCharacter(char0)) {
            this.insertText((String) this.getMessageFn.get(), Character.toString(char0));
        }
        return true;
    }

    public boolean keyPressed(int int0) {
        if (Screen.isSelectAll(int0)) {
            this.selectAll();
            return true;
        } else if (Screen.isCopy(int0)) {
            this.copy();
            return true;
        } else if (Screen.isPaste(int0)) {
            this.paste();
            return true;
        } else if (Screen.isCut(int0)) {
            this.cut();
            return true;
        } else {
            TextFieldHelper.CursorStep $$1 = Screen.hasControlDown() ? TextFieldHelper.CursorStep.WORD : TextFieldHelper.CursorStep.CHARACTER;
            if (int0 == 259) {
                this.removeFromCursor(-1, $$1);
                return true;
            } else {
                if (int0 == 261) {
                    this.removeFromCursor(1, $$1);
                } else {
                    if (int0 == 263) {
                        this.moveBy(-1, Screen.hasShiftDown(), $$1);
                        return true;
                    }
                    if (int0 == 262) {
                        this.moveBy(1, Screen.hasShiftDown(), $$1);
                        return true;
                    }
                    if (int0 == 268) {
                        this.setCursorToStart(Screen.hasShiftDown());
                        return true;
                    }
                    if (int0 == 269) {
                        this.setCursorToEnd(Screen.hasShiftDown());
                        return true;
                    }
                }
                return false;
            }
        }
    }

    private int clampToMsgLength(int int0) {
        return Mth.clamp(int0, 0, ((String) this.getMessageFn.get()).length());
    }

    private void insertText(String string0, String string1) {
        if (this.selectionPos != this.cursorPos) {
            string0 = this.deleteSelection(string0);
        }
        this.cursorPos = Mth.clamp(this.cursorPos, 0, string0.length());
        String $$2 = new StringBuilder(string0).insert(this.cursorPos, string1).toString();
        if (this.stringValidator.test($$2)) {
            this.setMessageFn.accept($$2);
            this.selectionPos = this.cursorPos = Math.min($$2.length(), this.cursorPos + string1.length());
        }
    }

    public void insertText(String string0) {
        this.insertText((String) this.getMessageFn.get(), string0);
    }

    private void resetSelectionIfNeeded(boolean boolean0) {
        if (!boolean0) {
            this.selectionPos = this.cursorPos;
        }
    }

    public void moveBy(int int0, boolean boolean1, TextFieldHelper.CursorStep textFieldHelperCursorStep2) {
        switch(textFieldHelperCursorStep2) {
            case CHARACTER:
                this.moveByChars(int0, boolean1);
                break;
            case WORD:
                this.moveByWords(int0, boolean1);
        }
    }

    public void moveByChars(int int0) {
        this.moveByChars(int0, false);
    }

    public void moveByChars(int int0, boolean boolean1) {
        this.cursorPos = Util.offsetByCodepoints((String) this.getMessageFn.get(), this.cursorPos, int0);
        this.resetSelectionIfNeeded(boolean1);
    }

    public void moveByWords(int int0) {
        this.moveByWords(int0, false);
    }

    public void moveByWords(int int0, boolean boolean1) {
        this.cursorPos = StringSplitter.getWordPosition((String) this.getMessageFn.get(), int0, this.cursorPos, true);
        this.resetSelectionIfNeeded(boolean1);
    }

    public void removeFromCursor(int int0, TextFieldHelper.CursorStep textFieldHelperCursorStep1) {
        switch(textFieldHelperCursorStep1) {
            case CHARACTER:
                this.removeCharsFromCursor(int0);
                break;
            case WORD:
                this.removeWordsFromCursor(int0);
        }
    }

    public void removeWordsFromCursor(int int0) {
        int $$1 = StringSplitter.getWordPosition((String) this.getMessageFn.get(), int0, this.cursorPos, true);
        this.removeCharsFromCursor($$1 - this.cursorPos);
    }

    public void removeCharsFromCursor(int int0) {
        String $$1 = (String) this.getMessageFn.get();
        if (!$$1.isEmpty()) {
            String $$2;
            if (this.selectionPos != this.cursorPos) {
                $$2 = this.deleteSelection($$1);
            } else {
                int $$3 = Util.offsetByCodepoints($$1, this.cursorPos, int0);
                int $$4 = Math.min($$3, this.cursorPos);
                int $$5 = Math.max($$3, this.cursorPos);
                $$2 = new StringBuilder($$1).delete($$4, $$5).toString();
                if (int0 < 0) {
                    this.selectionPos = this.cursorPos = $$4;
                }
            }
            this.setMessageFn.accept($$2);
        }
    }

    public void cut() {
        String $$0 = (String) this.getMessageFn.get();
        this.setClipboardFn.accept(this.getSelected($$0));
        this.setMessageFn.accept(this.deleteSelection($$0));
    }

    public void paste() {
        this.insertText((String) this.getMessageFn.get(), (String) this.getClipboardFn.get());
        this.selectionPos = this.cursorPos;
    }

    public void copy() {
        this.setClipboardFn.accept(this.getSelected((String) this.getMessageFn.get()));
    }

    public void selectAll() {
        this.selectionPos = 0;
        this.cursorPos = ((String) this.getMessageFn.get()).length();
    }

    private String getSelected(String string0) {
        int $$1 = Math.min(this.cursorPos, this.selectionPos);
        int $$2 = Math.max(this.cursorPos, this.selectionPos);
        return string0.substring($$1, $$2);
    }

    private String deleteSelection(String string0) {
        if (this.selectionPos == this.cursorPos) {
            return string0;
        } else {
            int $$1 = Math.min(this.cursorPos, this.selectionPos);
            int $$2 = Math.max(this.cursorPos, this.selectionPos);
            String $$3 = string0.substring(0, $$1) + string0.substring($$2);
            this.selectionPos = this.cursorPos = $$1;
            return $$3;
        }
    }

    public void setCursorToStart() {
        this.setCursorToStart(false);
    }

    public void setCursorToStart(boolean boolean0) {
        this.cursorPos = 0;
        this.resetSelectionIfNeeded(boolean0);
    }

    public void setCursorToEnd() {
        this.setCursorToEnd(false);
    }

    public void setCursorToEnd(boolean boolean0) {
        this.cursorPos = ((String) this.getMessageFn.get()).length();
        this.resetSelectionIfNeeded(boolean0);
    }

    public int getCursorPos() {
        return this.cursorPos;
    }

    public void setCursorPos(int int0) {
        this.setCursorPos(int0, true);
    }

    public void setCursorPos(int int0, boolean boolean1) {
        this.cursorPos = this.clampToMsgLength(int0);
        this.resetSelectionIfNeeded(boolean1);
    }

    public int getSelectionPos() {
        return this.selectionPos;
    }

    public void setSelectionPos(int int0) {
        this.selectionPos = this.clampToMsgLength(int0);
    }

    public void setSelectionRange(int int0, int int1) {
        int $$2 = ((String) this.getMessageFn.get()).length();
        this.cursorPos = Mth.clamp(int0, 0, $$2);
        this.selectionPos = Mth.clamp(int1, 0, $$2);
    }

    public boolean isSelecting() {
        return this.cursorPos != this.selectionPos;
    }

    public static enum CursorStep {

        CHARACTER, WORD
    }
}