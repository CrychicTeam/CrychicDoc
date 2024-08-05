package net.mehvahdjukaar.amendments.client.gui;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.font.TextFieldHelper;
import org.jetbrains.annotations.Nullable;

public class StyledTextFieldHelper extends TextFieldHelper {

    private static final char TOKEN = '§';

    private final Supplier<String> getMessageFn;

    public StyledTextFieldHelper(Supplier<String> supplier, Consumer<String> consumer, Supplier<String> supplier2, Consumer<String> consumer2, Predicate<String> predicate) {
        super(supplier, consumer, supplier2, consumer2, predicate);
        this.getMessageFn = supplier;
    }

    @Override
    public void removeFromCursor(int i, TextFieldHelper.CursorStep cursorStep) {
        String msg = (String) this.getMessageFn.get();
        int cursorPos = this.m_95194_();
        boolean hasTokenAtCursor = cursorPos < msg.length() && msg.charAt(cursorPos) == 167;
        if (i < 0) {
            int k = getIndexBeforeToken(i, msg, cursorPos);
            if (cursorPos == msg.length() || hasTokenAtCursor) {
                i = k;
            }
            super.removeFromCursor(i, cursorStep);
            if (k != i) {
                this.moveBy(k + 1, false, cursorStep);
            }
        } else if (hasTokenAtCursor) {
            this.moveBy(i, false, TextFieldHelper.CursorStep.CHARACTER);
            this.removeFromCursor(-1, cursorStep);
        } else {
            super.removeFromCursor(i, cursorStep);
        }
    }

    private static int getIndexBeforeToken(int i, String msg, int cursorPos) {
        int p = cursorPos - 3;
        if (p >= 0 && msg.charAt(p) == 167) {
            i = -3;
            int p1 = cursorPos - 5;
            if (p1 >= 0 && msg.charAt(p1) == 167) {
                i = -5;
            }
        }
        return i;
    }

    @Override
    public void moveBy(int i, boolean keepSelection, TextFieldHelper.CursorStep cursorStep) {
        String msg = (String) this.getMessageFn.get();
        int cursorPos = this.m_95194_();
        if (i < 0) {
            i = getIndexBeforeToken(i, msg, cursorPos);
            super.moveBy(i, keepSelection, cursorStep);
        } else {
            if (cursorPos < msg.length() && msg.charAt(cursorPos) == 167) {
                i = 3;
                int p = cursorPos + 2;
                if (p < msg.length() && msg.charAt(p) == 167) {
                    i = 5;
                }
            }
            super.moveBy(i, keepSelection, TextFieldHelper.CursorStep.CHARACTER);
        }
    }

    public void insertStyledText(String text, ChatFormatting color, ChatFormatting style) {
        String currentMod = this.getModifier(color, style);
        String lastMod = this.getPreviousModifier();
        if (!Objects.equals(currentMod, lastMod)) {
            String s = currentMod + text;
            this.m_95158_(s);
            int j = this.m_95194_();
            if (this.m_95194_() != ((String) this.getMessageFn.get()).length() && lastMod != null) {
                this.m_95158_(lastMod);
                super.setCursorPos(j, false);
            }
        } else {
            this.m_95158_(text);
        }
    }

    @Override
    public void setCursorPos(int textIndex, boolean keepSelection) {
        String text = (String) this.getMessageFn.get();
        super.setCursorPos(textIndex, keepSelection);
    }

    private String getModifier(ChatFormatting color, ChatFormatting style) {
        String s = color.toString();
        if (style != ChatFormatting.RESET) {
            s = s + style.toString();
        }
        return s.replace('§', '§');
    }

    @Nullable
    private String getPreviousModifier() {
        String text = (String) this.getMessageFn.get();
        int cursorPos = this.m_95194_() - 1;
        for (int i = cursorPos; i >= 0 && i < text.length(); i--) {
            if (text.charAt(i) == 167) {
                int start = i;
                int end = i + 2;
                if (i >= 2 && text.charAt(i - 2) == 167) {
                    start = i - 2;
                }
                if (end <= text.length()) {
                    return text.substring(start, end);
                }
            }
        }
        return null;
    }

    public void formatSelected(@Nullable ChatFormatting ink, @Nullable ChatFormatting quill) {
    }
}