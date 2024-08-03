package icyllis.modernui.mc;

import icyllis.modernui.core.UndoOperation;
import icyllis.modernui.core.UndoOwner;
import icyllis.modernui.util.Parcel;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.components.EditBox;

public class EditBoxEditAction extends UndoOperation<EditBox> {

    private final boolean mIsInsert;

    private final String mOldText;

    private String mNewText;

    private final int mStart;

    private final int mOldCursorPos;

    private int mNewCursorPos;

    public EditBoxEditAction(UndoOwner owner, int cursor, String oldText, int start, String newText) {
        super(owner);
        this.mOldText = oldText;
        this.mNewText = newText;
        this.mIsInsert = !this.mNewText.isEmpty() && this.mOldText.isEmpty();
        this.mStart = start;
        this.mOldCursorPos = cursor;
        this.mNewCursorPos = start + this.mNewText.length();
    }

    @Override
    public void commit() {
    }

    @Override
    public void undo() {
        EditBox target = this.getOwnerData();
        applyUndoOrRedo(target, this.mStart, this.mStart + this.mNewText.length(), this.mOldText, this.mStart, this.mOldCursorPos);
    }

    @Override
    public void redo() {
        EditBox target = this.getOwnerData();
        applyUndoOrRedo(target, this.mStart, this.mStart + this.mOldText.length(), this.mNewText, this.mStart, this.mNewCursorPos);
    }

    private static void applyUndoOrRedo(EditBox target, int deleteFrom, int deleteTo, CharSequence newText, int newTextInsertAt, int newCursorPos) {
        StringBuilder text = new StringBuilder(target.getValue());
        if (0 <= deleteFrom && deleteFrom <= deleteTo && deleteTo <= text.length() && newTextInsertAt <= text.length() - (deleteTo - deleteFrom)) {
            if (deleteFrom != deleteTo) {
                text.delete(deleteFrom, deleteTo);
            }
            if (!newText.isEmpty()) {
                text.insert(newTextInsertAt, newText);
            }
            target.setValue(text.toString());
        }
        if (0 <= newCursorPos && newCursorPos <= text.length()) {
            target.setCursorPosition(newCursorPos);
            target.setHighlightPos(newCursorPos);
        }
    }

    @Override
    public void writeToParcel(@Nonnull Parcel dest, int flags) {
    }

    public boolean mergeInsertWith(EditBoxEditAction edit) {
        if (this.mIsInsert && edit.mIsInsert) {
            if (this.mStart + this.mNewText.length() != edit.mStart) {
                return false;
            } else {
                this.mNewText = this.mNewText + edit.mNewText;
                this.mNewCursorPos = edit.mNewCursorPos;
                return true;
            }
        } else {
            return false;
        }
    }

    public String toString() {
        return "EditBoxEditAction{mOldText=" + this.mOldText + ", mNewText=" + this.mNewText + ", mStart=" + this.mStart + ", mOldCursorPos=" + this.mOldCursorPos + ", mNewCursorPos=" + this.mNewCursorPos + "}";
    }
}