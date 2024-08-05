package de.keksuccino.fancymenu.util.rendering.ui.screen.texteditor.formattingrules.brackets;

import de.keksuccino.fancymenu.util.rendering.ui.screen.texteditor.TextEditorFormattingRule;
import de.keksuccino.fancymenu.util.rendering.ui.screen.texteditor.TextEditorLine;
import de.keksuccino.fancymenu.util.rendering.ui.screen.texteditor.TextEditorScreen;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.Nullable;

public abstract class HighlightBracketsFormattingRuleBase extends TextEditorFormattingRule {

    protected TextEditorLine openBracketLine = null;

    protected int openBracketInLineIndex = -1;

    protected TextEditorLine closeBracketLine = null;

    protected int closeBracketInLineIndex = -1;

    protected abstract String getOpenBracketChar();

    protected abstract String getCloseBracketChar();

    protected abstract Style getHighlightStyle();

    @Override
    public void resetRule(TextEditorScreen editor) {
        this.openBracketLine = null;
        this.openBracketInLineIndex = -1;
        this.closeBracketLine = null;
        this.closeBracketInLineIndex = -1;
        TextEditorLine focusedLine = editor.getFocusedLine();
        if (focusedLine != null && focusedLine.m_94155_().length() > 0) {
            String textBeforeCursor = focusedLine.m_94155_().substring(0, focusedLine.m_94207_());
            String textAfterCursor = focusedLine.m_94155_().substring(focusedLine.m_94207_());
            int focusedLineIndex = editor.getFocusedLineIndex();
            if (textAfterCursor.startsWith(this.getOpenBracketChar())) {
                this.openBracketLine = focusedLine;
                this.openBracketInLineIndex = textBeforeCursor.length();
                List<TextEditorLine> lines = new ArrayList();
                if (focusedLineIndex == editor.getLineCount() - 1) {
                    lines.add(focusedLine);
                } else {
                    lines.addAll(editor.getLines().subList(focusedLineIndex, editor.getLineCount()));
                }
                int depth = 1;
                for (TextEditorLine line : lines) {
                    if (line.m_94155_().contains(this.getOpenBracketChar()) || line.m_94155_().contains(this.getCloseBracketChar())) {
                        int inLineIndex = 0;
                        String lineValue = line.m_94155_();
                        if (line == focusedLine) {
                            lineValue = textAfterCursor.substring(1);
                            inLineIndex = textBeforeCursor.length() + 1;
                        }
                        for (char c : lineValue.toCharArray()) {
                            String s = String.valueOf(c);
                            if (s.equals(this.getOpenBracketChar())) {
                                depth++;
                            }
                            if (s.equals(this.getCloseBracketChar())) {
                                if (--depth == 0) {
                                    this.closeBracketLine = line;
                                    this.closeBracketInLineIndex = inLineIndex;
                                    return;
                                }
                            }
                            inLineIndex++;
                        }
                    }
                }
            }
            if (textBeforeCursor.endsWith(this.getCloseBracketChar())) {
                this.closeBracketLine = focusedLine;
                this.closeBracketInLineIndex = textBeforeCursor.length() - 1;
                List<TextEditorLine> lines = new ArrayList();
                if (focusedLineIndex == 0) {
                    lines.add(focusedLine);
                } else {
                    lines.addAll(editor.getLines().subList(0, focusedLineIndex + 1));
                }
                Collections.reverse(lines);
                int depth = 1;
                for (TextEditorLine linex : lines) {
                    if (linex.m_94155_().contains(this.getOpenBracketChar()) || linex.m_94155_().contains(this.getCloseBracketChar())) {
                        int inLineIndex = linex.m_94155_().length() - 1;
                        String lineValue = new StringBuilder(linex.m_94155_()).reverse().toString();
                        if (linex == focusedLine) {
                            lineValue = new StringBuilder(textBeforeCursor).reverse().substring(1);
                            inLineIndex = lineValue.length() - 1;
                        }
                        for (char c : lineValue.toCharArray()) {
                            String sx = String.valueOf(c);
                            if (sx.equals(this.getCloseBracketChar())) {
                                depth++;
                            }
                            if (sx.equals(this.getOpenBracketChar())) {
                                if (--depth == 0) {
                                    this.openBracketLine = linex;
                                    this.openBracketInLineIndex = inLineIndex;
                                    return;
                                }
                            }
                            inLineIndex--;
                        }
                    }
                }
            }
        }
    }

    @Nullable
    @Override
    public Style getStyle(char atCharacterInLine, int atCharacterIndexInLine, int cursorPosInLine, TextEditorLine inLine, int atCharacterIndexTotal, TextEditorScreen editor) {
        if (this.openBracketLine != null && this.closeBracketLine != null) {
            String s = String.valueOf(atCharacterInLine);
            if (s.equals(this.getOpenBracketChar()) && inLine == this.openBracketLine && atCharacterIndexInLine == this.openBracketInLineIndex) {
                return this.getHighlightStyle();
            }
            if (s.equals(this.getCloseBracketChar()) && inLine == this.closeBracketLine && atCharacterIndexInLine == this.closeBracketInLineIndex) {
                return this.getHighlightStyle();
            }
        }
        return null;
    }
}