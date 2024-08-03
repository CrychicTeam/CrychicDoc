package de.keksuccino.fancymenu.util.rendering.ui.screen.texteditor;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class TextEditorHistory {

    private static final Logger LOGGER = LogManager.getLogger();

    protected TextEditorScreen parent;

    protected List<TextEditorHistory.Snapshot> snapshots = new ArrayList();

    protected int index = 0;

    protected TextEditorHistory(@NotNull TextEditorScreen parent) {
        this.parent = parent;
    }

    public void saveSnapshot() {
        try {
            if (this.index > 0 && ((TextEditorHistory.Snapshot) this.snapshots.get(this.index - 1)).text.equals(this.parent.getText())) {
                return;
            }
            if (this.index < this.snapshots.size()) {
                if (this.index == 0) {
                    this.snapshots.clear();
                } else {
                    this.snapshots = this.snapshots.subList(0, this.index);
                }
            }
            TextEditorLine focusedLine = this.parent.getFocusedLine();
            int cursorPos = focusedLine != null ? focusedLine.m_94207_() : 0;
            this.snapshots.add(new TextEditorHistory.Snapshot(this.parent.getText(), this.parent.getFocusedLineIndex(), cursorPos, this.parent.verticalScrollBar.getScroll(), this.parent.horizontalScrollBar.getScroll()));
            this.index = this.snapshots.size();
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    public void stepBack() {
        try {
            if (this.snapshots.isEmpty()) {
                return;
            }
            if (this.index > 0) {
                if (this.index == this.snapshots.size()) {
                    if (!((TextEditorHistory.Snapshot) this.snapshots.get(this.index - 1)).text.equals(this.parent.getText())) {
                        this.saveSnapshot();
                        this.index--;
                    } else if (this.index > 1) {
                        this.index--;
                    }
                }
                this.index--;
                this.restoreFrom((TextEditorHistory.Snapshot) this.snapshots.get(this.index));
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }
    }

    public void stepForward() {
        try {
            if (this.snapshots.isEmpty()) {
                return;
            }
            this.index++;
            if (this.index >= this.snapshots.size()) {
                this.index = this.snapshots.size() - 1;
            }
            this.restoreFrom((TextEditorHistory.Snapshot) this.snapshots.get(this.index));
            if (this.index == this.snapshots.size() - 1) {
                this.index = this.snapshots.size();
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }
    }

    protected void restoreFrom(@NotNull TextEditorHistory.Snapshot snap) {
        this.parent.setText(snap.text);
        if (snap.focusedLineIndex != -1) {
            this.parent.setFocusedLine(snap.focusedLineIndex);
            TextEditorLine focused = this.parent.getFocusedLine();
            if (focused != null) {
                focused.setCursorPosition(snap.cursorPos);
                focused.m_94208_(snap.cursorPos);
            }
        }
        this.parent.verticalScrollBar.setScroll(snap.verticalScroll, false);
        this.parent.horizontalScrollBar.setScroll(snap.horizontalScroll, false);
    }

    public static record Snapshot(@NotNull String text, int focusedLineIndex, int cursorPos, float verticalScroll, float horizontalScroll) {
    }
}