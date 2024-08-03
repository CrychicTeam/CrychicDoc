package icyllis.modernui.widget;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.core.Core;
import icyllis.modernui.core.UndoManager;
import icyllis.modernui.core.UndoOperation;
import icyllis.modernui.core.UndoOwner;
import icyllis.modernui.text.Editable;
import icyllis.modernui.text.InputFilter;
import icyllis.modernui.text.Selection;
import icyllis.modernui.text.Spannable;
import icyllis.modernui.text.SpannableStringBuilder;
import icyllis.modernui.text.Spanned;
import icyllis.modernui.text.TextUtils;
import icyllis.modernui.text.method.MovementMethod;
import icyllis.modernui.text.method.WordIterator;
import icyllis.modernui.util.Parcel;
import icyllis.modernui.util.Parcelable;
import icyllis.modernui.view.ContextMenu;
import icyllis.modernui.view.MenuItem;
import icyllis.modernui.view.MotionEvent;
import javax.annotation.Nonnull;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class Editor {

    private static final Marker MARKER = MarkerManager.getMarker("Editor");

    static final int BLINK = 500;

    private static final String UNDO_OWNER_TAG = "Editor";

    private static final int MENU_ITEM_ORDER_ASSIST = 0;

    private static final int MENU_ITEM_ORDER_UNDO = 2;

    private static final int MENU_ITEM_ORDER_REDO = 3;

    private static final int MENU_ITEM_ORDER_CUT = 4;

    private static final int MENU_ITEM_ORDER_COPY = 5;

    private static final int MENU_ITEM_ORDER_PASTE = 6;

    private static final int MENU_ITEM_ORDER_SHARE = 7;

    private static final int MENU_ITEM_ORDER_SELECT_ALL = 8;

    private static final int MENU_ITEM_ORDER_REPLACE = 9;

    private static final int MENU_ITEM_ORDER_AUTOFILL = 10;

    private static final int MENU_ITEM_ORDER_PASTE_AS_PLAIN_TEXT = 11;

    private static final int MENU_ITEM_ORDER_SECONDARY_ASSIST_ACTIONS_START = 50;

    private static final int MENU_ITEM_ORDER_PROCESS_TEXT_INTENT_ACTIONS_START = 100;

    private final UndoManager mUndoManager = new UndoManager();

    private UndoOwner mUndoOwner = this.mUndoManager.getOwner("Editor", this);

    final Editor.UndoInputFilter mUndoInputFilter = new Editor.UndoInputFilter(this);

    boolean mAllowUndo = true;

    private final TextView mTextView;

    boolean mSelectionMoved;

    boolean mTouchFocusSelected;

    boolean mDiscardNextActionUp;

    boolean mIgnoreActionUpEvent;

    private long mShowCursor;

    private Editor.Blink mBlink;

    boolean mCursorVisible = true;

    boolean mSelectAllOnFocus;

    boolean mTextIsSelectable;

    boolean mIsBeingLongClicked;

    private float mContextMenuAnchorX;

    private float mContextMenuAnchorY;

    private int mLastButtonState;

    private WordIterator mWordIterator;

    private final MenuItem.OnMenuItemClickListener mOnContextMenuItemClickListener;

    private int mLastTapPosition = -1;

    Editor(TextView textView) {
        this.mTextView = textView;
        this.mOnContextMenuItemClickListener = item -> this.mTextView.onTextContextMenuItem(item.getItemId());
    }

    void forgetUndoRedo() {
        UndoOwner[] owners = new UndoOwner[] { this.mUndoOwner };
        this.mUndoManager.forgetUndos(owners, -1);
        this.mUndoManager.forgetRedos(owners, -1);
    }

    boolean canUndo() {
        UndoOwner[] owners = new UndoOwner[] { this.mUndoOwner };
        return this.mAllowUndo && this.mUndoManager.countUndos(owners) > 0;
    }

    boolean canRedo() {
        UndoOwner[] owners = new UndoOwner[] { this.mUndoOwner };
        return this.mAllowUndo && this.mUndoManager.countRedos(owners) > 0;
    }

    void undo() {
        if (this.mAllowUndo) {
            UndoOwner[] owners = new UndoOwner[] { this.mUndoOwner };
            this.mUndoManager.undo(owners, 1);
        }
    }

    void redo() {
        if (this.mAllowUndo) {
            UndoOwner[] owners = new UndoOwner[] { this.mUndoOwner };
            this.mUndoManager.redo(owners, 1);
        }
    }

    void onAttachedToWindow() {
        this.resumeBlink();
    }

    void onDetachedFromWindow() {
        this.suspendBlink();
    }

    private boolean isCursorVisible() {
        return this.mCursorVisible && this.mTextView.isTextEditable();
    }

    boolean shouldRenderCursor() {
        if (this.isCursorVisible()) {
            long showCursorDelta = Core.timeMillis() - this.mShowCursor;
            return showCursorDelta % 1000L < 500L;
        } else {
            return false;
        }
    }

    private void suspendBlink() {
        if (this.mBlink != null) {
            this.mBlink.cancel();
        }
    }

    private void resumeBlink() {
        if (this.mBlink != null) {
            this.mBlink.reset();
            this.makeBlink();
        }
    }

    WordIterator getWordIterator() {
        if (this.mWordIterator == null) {
            this.mWordIterator = new WordIterator(this.mTextView.getTextLocale());
        }
        return this.mWordIterator;
    }

    void onFocusChanged(boolean focused, int direction) {
        this.mShowCursor = Core.timeMillis();
        if (focused) {
            int selStart = this.mTextView.getSelectionStart();
            int selEnd = this.mTextView.getSelectionEnd();
            if (this.mSelectAllOnFocus && selStart == 0 && selEnd == this.mTextView.getText().length()) {
                boolean var7 = true;
            } else {
                boolean var10000 = false;
            }
            if (this.mLastTapPosition >= 0) {
                Selection.setSelection((Spannable) this.mTextView.getText(), this.mLastTapPosition);
            } else {
                MovementMethod movement = this.mTextView.getMovementMethod();
                if (movement != null) {
                    movement.onTakeFocus(this.mTextView, (Spannable) this.mTextView.getText(), direction);
                }
                if (this.mSelectionMoved && selStart >= 0 && selEnd >= 0) {
                    Selection.setSelection((Spannable) this.mTextView.getText(), selStart, selEnd);
                }
            }
            if (this.mSelectAllOnFocus) {
                this.mTextView.selectAllText();
            }
            this.makeBlink();
        } else {
            this.mLastTapPosition = -1;
        }
    }

    void onTouchEvent(@Nonnull MotionEvent event) {
        int action = event.getAction();
        boolean filterOutEvent;
        if ((action == 0 || action == 1) && ((this.mLastButtonState ^ event.getButtonState()) & 1) == 0) {
            filterOutEvent = true;
        } else {
            filterOutEvent = action == 2 && !event.isButtonPressed(1);
        }
        this.mLastButtonState = event.getButtonState();
        if (filterOutEvent) {
            if (event.getAction() == 1) {
                this.mDiscardNextActionUp = true;
            }
        } else {
            if (event.getAction() == 0) {
                this.mLastTapPosition = this.mTextView.getOffsetForPosition(event.getX(), event.getY());
                this.mTouchFocusSelected = false;
                this.mIgnoreActionUpEvent = false;
            }
        }
    }

    void onTouchUpEvent(@Nonnull MotionEvent event) {
        boolean selectAllGotFocus = this.mSelectAllOnFocus && this.mTextView.didTouchFocusSelect();
        CharSequence text = this.mTextView.getText();
        if (!selectAllGotFocus) {
            int offset = this.mTextView.getOffsetForPosition(event.getX(), event.getY());
            Selection.setSelection((Spannable) text, offset);
        }
    }

    void sendOnTextChanged(int start, int before, int after) {
    }

    void addSpanWatchers(Spannable sp) {
    }

    private boolean shouldBlink() {
        if (this.isCursorVisible() && this.mTextView.isFocused()) {
            int start = this.mTextView.getSelectionStart();
            if (start < 0) {
                return false;
            } else {
                int end = this.mTextView.getSelectionEnd();
                return end < 0 ? false : start == end;
            }
        } else {
            return false;
        }
    }

    void makeBlink() {
        if (this.shouldBlink()) {
            this.mShowCursor = Core.timeMillis();
            if (this.mBlink == null) {
                this.mBlink = new Editor.Blink();
            }
            this.mTextView.removeCallbacks(this.mBlink);
            this.mTextView.postDelayed(this.mBlink, 500L);
        } else if (this.mBlink != null) {
            this.mTextView.removeCallbacks(this.mBlink);
        }
    }

    void setContextMenuAnchor(float x, float y) {
        this.mContextMenuAnchorX = x;
        this.mContextMenuAnchorY = y;
    }

    void onCreateContextMenu(ContextMenu menu) {
        if (!this.mIsBeingLongClicked && !Float.isNaN(this.mContextMenuAnchorX) && !Float.isNaN(this.mContextMenuAnchorY)) {
            int offset = this.mTextView.getOffsetForPosition(this.mContextMenuAnchorX, this.mContextMenuAnchorY);
            if (offset != -1) {
                int min = Math.min(this.mTextView.getSelectionStart(), this.mTextView.getSelectionEnd());
                int max = Math.max(this.mTextView.getSelectionStart(), this.mTextView.getSelectionEnd());
                boolean isOnSelection = this.mTextView.hasSelection() && offset >= min && offset <= max;
                if (!isOnSelection) {
                    Selection.setSelection((Spannable) this.mTextView.getText(), offset);
                }
                menu.add(0, 16908338, 2, "Undo").setAlphabeticShortcut('z').setOnMenuItemClickListener(this.mOnContextMenuItemClickListener).setEnabled(this.mTextView.canUndo());
                menu.add(0, 16908339, 3, "Redo").setAlphabeticShortcut('y').setOnMenuItemClickListener(this.mOnContextMenuItemClickListener).setEnabled(this.mTextView.canRedo());
                menu.add(0, 16908320, 4, "Cut").setAlphabeticShortcut('x').setOnMenuItemClickListener(this.mOnContextMenuItemClickListener).setEnabled(this.mTextView.canCut());
                menu.add(0, 16908321, 5, "Copy").setAlphabeticShortcut('c').setOnMenuItemClickListener(this.mOnContextMenuItemClickListener).setEnabled(this.mTextView.canCopy());
                menu.add(0, 16908322, 6, "Paste").setAlphabeticShortcut('v').setEnabled(this.mTextView.canPaste()).setOnMenuItemClickListener(this.mOnContextMenuItemClickListener);
                menu.add(0, 16908319, 8, "Select all").setAlphabeticShortcut('a').setEnabled(this.mTextView.canSelectAllText()).setOnMenuItemClickListener(this.mOnContextMenuItemClickListener);
                menu.setQwertyMode(true);
            }
        }
    }

    private static boolean isValidRange(CharSequence text, int start, int end) {
        return 0 <= start && start <= end && end <= text.length();
    }

    private class Blink implements Runnable {

        private boolean mCancelled;

        public void run() {
            if (!this.mCancelled) {
                if (Editor.this.shouldBlink()) {
                    if (Editor.this.mTextView.getLayout() != null) {
                        Editor.this.mTextView.invalidateCursorPath();
                    }
                    Editor.this.mTextView.postDelayed(this, 500L);
                }
            }
        }

        void cancel() {
            if (!this.mCancelled) {
                Editor.this.mTextView.removeCallbacks(this);
                this.mCancelled = true;
            }
        }

        void reset() {
            this.mCancelled = false;
        }
    }

    private interface CursorController {

        void show();

        void hide();

        void onDetached();

        boolean isCursorBeingModified();

        boolean isActive();
    }

    public static class EditOperation extends UndoOperation<Editor> {

        public static final Parcelable.ClassLoaderCreator<Editor.EditOperation> CREATOR = Editor.EditOperation::new;

        private static final int TYPE_INSERT = 0;

        private static final int TYPE_DELETE = 1;

        private static final int TYPE_REPLACE = 2;

        private int mType;

        private String mOldText;

        private String mNewText;

        private int mStart;

        private int mOldCursorPos;

        private int mNewCursorPos;

        private boolean mFrozen;

        private boolean mIsComposition;

        public EditOperation(Editor editor, String oldText, int dstart, String newText) {
            super(editor.mUndoOwner);
            this.mOldText = oldText;
            this.mNewText = newText;
            if (!this.mNewText.isEmpty() && this.mOldText.isEmpty()) {
                this.mType = 0;
            } else if (this.mNewText.isEmpty() && !this.mOldText.isEmpty()) {
                this.mType = 1;
            } else {
                this.mType = 2;
            }
            this.mStart = dstart;
            this.mOldCursorPos = editor.mTextView.getSelectionStart();
            this.mNewCursorPos = dstart + this.mNewText.length();
        }

        public EditOperation(Parcel src, ClassLoader loader) {
            super(src, loader);
            this.mType = src.readInt();
            this.mOldText = src.readString();
            this.mNewText = src.readString();
            this.mStart = src.readInt();
            this.mOldCursorPos = src.readInt();
            this.mNewCursorPos = src.readInt();
            this.mFrozen = src.readInt() == 1;
            this.mIsComposition = src.readInt() == 1;
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
            dest.writeInt(this.mType);
            dest.writeString(this.mOldText);
            dest.writeString(this.mNewText);
            dest.writeInt(this.mStart);
            dest.writeInt(this.mOldCursorPos);
            dest.writeInt(this.mNewCursorPos);
            dest.writeInt(this.mFrozen ? 1 : 0);
            dest.writeInt(this.mIsComposition ? 1 : 0);
        }

        private int getNewTextEnd() {
            return this.mStart + this.mNewText.length();
        }

        private int getOldTextEnd() {
            return this.mStart + this.mOldText.length();
        }

        @Override
        public void commit() {
        }

        @Override
        public void undo() {
            Editor editor = this.getOwnerData();
            Editable text = (Editable) editor.mTextView.getText();
            modifyText(text, this.mStart, this.getNewTextEnd(), this.mOldText, this.mStart, this.mOldCursorPos);
        }

        @Override
        public void redo() {
            Editor editor = this.getOwnerData();
            Editable text = (Editable) editor.mTextView.getText();
            modifyText(text, this.mStart, this.getOldTextEnd(), this.mNewText, this.mStart, this.mNewCursorPos);
        }

        private boolean mergeWith(Editor.EditOperation edit) {
            if (this.mFrozen) {
                return false;
            } else {
                switch(this.mType) {
                    case 0:
                        return this.mergeInsertWith(edit);
                    case 1:
                        return this.mergeDeleteWith(edit);
                    case 2:
                        return this.mergeReplaceWith(edit);
                    default:
                        return false;
                }
            }
        }

        private boolean mergeInsertWith(Editor.EditOperation edit) {
            if (edit.mType == 0) {
                if (this.getNewTextEnd() != edit.mStart) {
                    return false;
                } else {
                    this.mNewText = this.mNewText + edit.mNewText;
                    this.mNewCursorPos = edit.mNewCursorPos;
                    this.mFrozen = edit.mFrozen;
                    this.mIsComposition = edit.mIsComposition;
                    return true;
                }
            } else if (this.mIsComposition && edit.mType == 2 && this.mStart <= edit.mStart && this.getNewTextEnd() >= edit.getOldTextEnd()) {
                this.mNewText = this.mNewText.substring(0, edit.mStart - this.mStart) + edit.mNewText + this.mNewText.substring(edit.getOldTextEnd() - this.mStart);
                this.mNewCursorPos = edit.mNewCursorPos;
                this.mIsComposition = edit.mIsComposition;
                return true;
            } else {
                return false;
            }
        }

        private boolean mergeDeleteWith(Editor.EditOperation edit) {
            if (edit.mType != 1) {
                return false;
            } else if (this.mStart != edit.getOldTextEnd()) {
                return false;
            } else {
                this.mStart = edit.mStart;
                this.mOldText = edit.mOldText + this.mOldText;
                this.mNewCursorPos = edit.mNewCursorPos;
                this.mIsComposition = edit.mIsComposition;
                return true;
            }
        }

        private boolean mergeReplaceWith(Editor.EditOperation edit) {
            if (edit.mType == 0 && this.getNewTextEnd() == edit.mStart) {
                this.mNewText = this.mNewText + edit.mNewText;
                this.mNewCursorPos = edit.mNewCursorPos;
                return true;
            } else if (!this.mIsComposition) {
                return false;
            } else if (edit.mType == 1 && this.mStart <= edit.mStart && this.getNewTextEnd() >= edit.getOldTextEnd()) {
                this.mNewText = this.mNewText.substring(0, edit.mStart - this.mStart) + this.mNewText.substring(edit.getOldTextEnd() - this.mStart);
                if (this.mNewText.isEmpty()) {
                    this.mType = 1;
                }
                this.mNewCursorPos = edit.mNewCursorPos;
                this.mIsComposition = edit.mIsComposition;
                return true;
            } else if (edit.mType == 2 && this.mStart == edit.mStart && TextUtils.contentEquals(this.mNewText, edit.mOldText)) {
                this.mNewText = edit.mNewText;
                this.mNewCursorPos = edit.mNewCursorPos;
                this.mIsComposition = edit.mIsComposition;
                return true;
            } else {
                return false;
            }
        }

        public void forceMergeWith(Editor.EditOperation edit) {
            if (!this.mergeWith(edit)) {
                Editor editor = this.getOwnerData();
                Editable editable = (Editable) editor.mTextView.getText();
                Editable originalText = new SpannableStringBuilder(editable.toString());
                modifyText(originalText, this.mStart, this.getNewTextEnd(), this.mOldText, this.mStart, this.mOldCursorPos);
                Editable finalText = new SpannableStringBuilder(editable.toString());
                modifyText(finalText, edit.mStart, edit.getOldTextEnd(), edit.mNewText, edit.mStart, edit.mNewCursorPos);
                this.mType = 2;
                this.mNewText = finalText.toString();
                this.mOldText = originalText.toString();
                this.mStart = 0;
                this.mNewCursorPos = edit.mNewCursorPos;
                this.mIsComposition = edit.mIsComposition;
            }
        }

        private static void modifyText(Editable text, int deleteFrom, int deleteTo, CharSequence newText, int newTextInsertAt, int newCursorPos) {
            if (Editor.isValidRange(text, deleteFrom, deleteTo) && newTextInsertAt <= text.length() - (deleteTo - deleteFrom)) {
                if (deleteFrom != deleteTo) {
                    text.delete(deleteFrom, deleteTo);
                }
                if (!newText.isEmpty()) {
                    text.insert(newTextInsertAt, newText);
                }
            }
            if (0 <= newCursorPos && newCursorPos <= text.length()) {
                Selection.setSelection(text, newCursorPos);
            }
        }

        private String getTypeString() {
            switch(this.mType) {
                case 0:
                    return "insert";
                case 1:
                    return "delete";
                case 2:
                    return "replace";
                default:
                    return "";
            }
        }

        public String toString() {
            return "[mType=" + this.getTypeString() + ", mOldText=" + this.mOldText + ", mNewText=" + this.mNewText + ", mStart=" + this.mStart + ", mOldCursorPos=" + this.mOldCursorPos + ", mNewCursorPos=" + this.mNewCursorPos + ", mFrozen=" + this.mFrozen + ", mIsComposition=" + this.mIsComposition + "]";
        }
    }

    public static class UndoInputFilter implements InputFilter {

        private final Editor mEditor;

        private boolean mIsUserEdit;

        private boolean mPreviousOperationWasInSameBatchEdit;

        private static final int MERGE_EDIT_MODE_FORCE_MERGE = 0;

        private static final int MERGE_EDIT_MODE_NORMAL = 2;

        public UndoInputFilter(Editor editor) {
            this.mEditor = editor;
        }

        public void beginBatchEdit() {
            this.mIsUserEdit = true;
        }

        public void endBatchEdit() {
            this.mIsUserEdit = false;
            this.mPreviousOperationWasInSameBatchEdit = false;
        }

        @Override
        public CharSequence filter(@NonNull CharSequence source, int start, int end, @NonNull Spanned dest, int dstart, int dend) {
            if (!this.canUndoEdit(source, start, end, dest, dstart, dend)) {
                return null;
            } else {
                this.handleEdit(source, start, end, dest, dstart, dend);
                return null;
            }
        }

        void freezeLastEdit() {
            this.mEditor.mUndoManager.beginUpdate("Edit text");
            Editor.EditOperation lastEdit = this.getLastEdit();
            if (lastEdit != null) {
                lastEdit.mFrozen = true;
            }
            this.mEditor.mUndoManager.endUpdate();
        }

        private void handleEdit(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            int mergeMode;
            if (!this.isInTextWatcher() && !this.mPreviousOperationWasInSameBatchEdit) {
                mergeMode = 2;
            } else {
                mergeMode = 0;
            }
            String newText = TextUtils.substring(source, start, end);
            String oldText = TextUtils.substring(dest, dstart, dend);
            Editor.EditOperation edit = new Editor.EditOperation(this.mEditor, oldText, dstart, newText);
            this.recordEdit(edit, mergeMode);
        }

        private Editor.EditOperation getLastEdit() {
            return this.mEditor.mUndoManager.getLastOperation(Editor.EditOperation.class, this.mEditor.mUndoOwner, 1);
        }

        private void recordEdit(Editor.EditOperation edit, int mergeMode) {
            UndoManager um = this.mEditor.mUndoManager;
            um.beginUpdate("Edit text");
            Editor.EditOperation lastEdit = this.getLastEdit();
            if (lastEdit == null) {
                um.addOperation(edit, 0);
            } else if (mergeMode == 0) {
                lastEdit.forceMergeWith(edit);
            } else if (!this.mIsUserEdit) {
                um.commitState(this.mEditor.mUndoOwner);
                um.addOperation(edit, 0);
            } else if (mergeMode != 2 || !lastEdit.mergeWith(edit)) {
                um.commitState(this.mEditor.mUndoOwner);
                um.addOperation(edit, 0);
            }
            this.mPreviousOperationWasInSameBatchEdit = this.mIsUserEdit;
            um.endUpdate();
        }

        private boolean canUndoEdit(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (!this.mEditor.mAllowUndo) {
                return false;
            } else if (this.mEditor.mUndoManager.isInUndo()) {
                return false;
            } else {
                return !Editor.isValidRange(source, start, end) || !Editor.isValidRange(dest, dstart, dend) ? false : start != end || dstart != dend;
            }
        }

        private boolean isInTextWatcher() {
            CharSequence text = this.mEditor.mTextView.getText();
            return text instanceof SpannableStringBuilder && ((SpannableStringBuilder) text).getTextWatcherDepth() > 0;
        }
    }
}