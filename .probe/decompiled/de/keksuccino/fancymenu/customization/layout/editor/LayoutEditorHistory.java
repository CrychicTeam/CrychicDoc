package de.keksuccino.fancymenu.customization.layout.editor;

import de.keksuccino.fancymenu.customization.layout.Layout;
import de.keksuccino.fancymenu.customization.layout.editor.widget.AbstractLayoutEditorWidget;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LayoutEditorHistory {

    private static final Logger LOGGER = LogManager.getLogger();

    protected LayoutEditorScreen editor;

    private List<LayoutEditorHistory.Snapshot> history = new ArrayList();

    private int current = -1;

    private boolean preventSnapshotSaving = false;

    public LayoutEditorHistory(LayoutEditorScreen editor) {
        this.editor = editor;
    }

    public void saveSnapshot() {
        this.saveSnapshot(this.createSnapshot());
    }

    public void saveSnapshot(LayoutEditorHistory.Snapshot snap) {
        if (!this.preventSnapshotSaving) {
            if (this.current < 0) {
                this.history.clear();
                this.history.add(snap);
                this.current = 0;
            } else if (this.current <= this.history.size() - 1) {
                List<LayoutEditorHistory.Snapshot> l = new ArrayList();
                for (int i = 0; i <= this.current; i++) {
                    l.add((LayoutEditorHistory.Snapshot) this.history.get(i));
                }
                l.add(snap);
                this.history = l;
                this.current = this.history.size() - 1;
            } else {
                this.current = this.history.size() - 1;
                this.saveSnapshot(snap);
            }
        }
    }

    public LayoutEditorHistory.Snapshot createSnapshot() {
        return new LayoutEditorHistory.Snapshot(this.editor);
    }

    public void setPreventSnapshotSaving(boolean preventSaving) {
        this.preventSnapshotSaving = preventSaving;
    }

    public void stepBack() {
        if (this.current > -1 && this.current <= this.history.size() - 1) {
            LayoutEditorHistory.Snapshot snap = (LayoutEditorHistory.Snapshot) this.history.get(this.current);
            this.current--;
            snap.preSnapshotState = this.createSnapshot();
            this.editor.layout = snap.snapshot;
            this.editor.isMouseSelection = false;
            this.editor.preDragElementSnapshot = null;
            this.editor.rightClickMenu.closeMenu();
            if (this.editor.activeElementContextMenu != null) {
                this.editor.activeElementContextMenu.closeMenu();
                this.editor.activeElementContextMenu = null;
            }
            this.editor.constructElementInstances();
            for (AbstractLayoutEditorWidget w : this.editor.layoutEditorWidgets) {
                w.refresh();
            }
        }
    }

    public void stepForward() {
        if (this.current >= -1 && this.current < this.history.size() - 1) {
            this.current++;
            LayoutEditorHistory.Snapshot snap = ((LayoutEditorHistory.Snapshot) this.history.get(this.current)).preSnapshotState;
            if (snap != null) {
                this.editor.layout = snap.snapshot;
                this.editor.isMouseSelection = false;
                this.editor.preDragElementSnapshot = null;
                this.editor.rightClickMenu.closeMenu();
                if (this.editor.activeElementContextMenu != null) {
                    this.editor.activeElementContextMenu.closeMenu();
                    this.editor.activeElementContextMenu = null;
                }
                this.editor.constructElementInstances();
                for (AbstractLayoutEditorWidget w : this.editor.layoutEditorWidgets) {
                    w.refresh();
                }
            }
        }
    }

    public static class Snapshot {

        public Layout snapshot;

        public LayoutEditorHistory.Snapshot preSnapshotState = null;

        public Snapshot(LayoutEditorScreen editor) {
            editor.serializeElementInstancesToLayoutInstance();
            this.snapshot = editor.layout.copy();
        }
    }
}