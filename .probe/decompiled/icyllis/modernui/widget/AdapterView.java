package icyllis.modernui.widget;

import icyllis.modernui.core.Context;
import icyllis.modernui.core.Core;
import icyllis.modernui.util.DataSetObserver;
import icyllis.modernui.view.ContextMenu;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AdapterView<T extends Adapter> extends ViewGroup {

    public static final int ITEM_VIEW_TYPE_IGNORE = -1;

    public static final int ITEM_VIEW_TYPE_HEADER_OR_FOOTER = -2;

    int mFirstPosition = 0;

    int mSpecificTop;

    int mSyncPosition;

    long mSyncRowId = Long.MIN_VALUE;

    long mSyncHeight;

    boolean mNeedSync = false;

    int mSyncMode;

    private int mLayoutHeight;

    static final int SYNC_SELECTED_POSITION = 0;

    static final int SYNC_FIRST_POSITION = 1;

    static final int SYNC_MAX_DURATION_MILLIS = 100;

    boolean mInLayout = false;

    AdapterView.OnItemSelectedListener mOnItemSelectedListener;

    AdapterView.OnItemClickListener mOnItemClickListener;

    AdapterView.OnItemLongClickListener mOnItemLongClickListener;

    boolean mDataChanged;

    int mNextSelectedPosition = -1;

    long mNextSelectedRowId = Long.MIN_VALUE;

    int mSelectedPosition = -1;

    long mSelectedRowId = Long.MIN_VALUE;

    private View mEmptyView;

    int mItemCount;

    int mOldItemCount;

    public static final int INVALID_POSITION = -1;

    public static final long INVALID_ROW_ID = Long.MIN_VALUE;

    int mOldSelectedPosition = -1;

    long mOldSelectedRowId = Long.MIN_VALUE;

    private int mDesiredFocusableState;

    private boolean mDesiredFocusableInTouchModeState;

    private AdapterView<T>.SelectionNotifier mSelectionNotifier;

    private AdapterView<T>.SelectionNotifier mPendingSelectionNotifier;

    boolean mBlockLayoutRequests = false;

    public AdapterView(Context context) {
        super(context);
        this.mDesiredFocusableState = this.getFocusable();
        if (this.mDesiredFocusableState == 16) {
            super.setFocusable(0);
        }
    }

    public void setOnItemClickListener(@Nullable AdapterView.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Nullable
    public final AdapterView.OnItemClickListener getOnItemClickListener() {
        return this.mOnItemClickListener;
    }

    public boolean performItemClick(View view, int position, long id) {
        boolean result;
        if (this.mOnItemClickListener != null) {
            this.playSoundEffect(0);
            this.mOnItemClickListener.onItemClick(this, view, position, id);
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener listener) {
        if (!this.isLongClickable()) {
            this.setLongClickable(true);
        }
        this.mOnItemLongClickListener = listener;
    }

    public final AdapterView.OnItemLongClickListener getOnItemLongClickListener() {
        return this.mOnItemLongClickListener;
    }

    public void setOnItemSelectedListener(@Nullable AdapterView.OnItemSelectedListener listener) {
        this.mOnItemSelectedListener = listener;
    }

    @Nullable
    public final AdapterView.OnItemSelectedListener getOnItemSelectedListener() {
        return this.mOnItemSelectedListener;
    }

    public abstract T getAdapter();

    public abstract void setAdapter(T var1);

    @Override
    public void addView(@Nonnull View child) {
        throw new UnsupportedOperationException("addView(View) is not supported in AdapterView");
    }

    @Override
    public void addView(@Nonnull View child, int index) {
        throw new UnsupportedOperationException("addView(View, int) is not supported in AdapterView");
    }

    @Override
    public void addView(@Nonnull View child, @Nonnull ViewGroup.LayoutParams params) {
        throw new UnsupportedOperationException("addView(View, LayoutParams) is not supported in AdapterView");
    }

    @Override
    public void addView(@Nonnull View child, int index, @Nonnull ViewGroup.LayoutParams params) {
        throw new UnsupportedOperationException("addView(View, int, LayoutParams) is not supported in AdapterView");
    }

    @Override
    public void removeView(@Nonnull View child) {
        throw new UnsupportedOperationException("removeView(View) is not supported in AdapterView");
    }

    @Override
    public void removeViewAt(int index) {
        throw new UnsupportedOperationException("removeViewAt(int) is not supported in AdapterView");
    }

    @Override
    public void removeAllViews() {
        throw new UnsupportedOperationException("removeAllViews() is not supported in AdapterView");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        this.mLayoutHeight = this.getHeight();
    }

    public int getSelectedItemPosition() {
        return this.mNextSelectedPosition;
    }

    public long getSelectedItemId() {
        return this.mNextSelectedRowId;
    }

    @Nullable
    public abstract View getSelectedView();

    @Nullable
    public Object getSelectedItem() {
        T adapter = this.getAdapter();
        int selection = this.getSelectedItemPosition();
        return adapter != null && adapter.getCount() > 0 && selection >= 0 ? adapter.getItem(selection) : null;
    }

    public int getCount() {
        return this.mItemCount;
    }

    public int getPositionForView(View view) {
        View listItem = view;
        View v;
        try {
            while ((v = (View) listItem.getParent()) != null && !v.equals(this)) {
                listItem = v;
            }
        } catch (ClassCastException var5) {
            return -1;
        }
        int childCount = this.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (this.getChildAt(i).equals(listItem)) {
                return this.mFirstPosition + i;
            }
        }
        return -1;
    }

    public int getFirstVisiblePosition() {
        return this.mFirstPosition;
    }

    public int getLastVisiblePosition() {
        return this.mFirstPosition + this.getChildCount() - 1;
    }

    public abstract void setSelection(int var1);

    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
        T adapter = this.getAdapter();
        boolean empty = adapter == null || adapter.isEmpty();
        this.updateEmptyStatus(empty);
    }

    public View getEmptyView() {
        return this.mEmptyView;
    }

    boolean isInFilterMode() {
        return false;
    }

    @Override
    public void setFocusable(int focusable) {
        T adapter = this.getAdapter();
        boolean empty = adapter == null || adapter.getCount() == 0;
        this.mDesiredFocusableState = focusable;
        if ((focusable & 17) == 0) {
            this.mDesiredFocusableInTouchModeState = false;
        }
        super.setFocusable(empty && !this.isInFilterMode() ? 0 : focusable);
    }

    @Override
    public void setFocusableInTouchMode(boolean focusable) {
        T adapter = this.getAdapter();
        boolean empty = adapter == null || adapter.getCount() == 0;
        this.mDesiredFocusableInTouchModeState = focusable;
        if (focusable) {
            this.mDesiredFocusableState = 1;
        }
        super.setFocusableInTouchMode(focusable && (!empty || this.isInFilterMode()));
    }

    void checkFocus() {
        T adapter = this.getAdapter();
        boolean empty = adapter == null || adapter.getCount() == 0;
        boolean focusable = !empty || this.isInFilterMode();
        super.setFocusableInTouchMode(focusable && this.mDesiredFocusableInTouchModeState);
        super.setFocusable(focusable ? this.mDesiredFocusableState : 0);
        if (this.mEmptyView != null) {
            this.updateEmptyStatus(adapter == null || adapter.isEmpty());
        }
    }

    private void updateEmptyStatus(boolean empty) {
        if (this.isInFilterMode()) {
            empty = false;
        }
        if (empty) {
            if (this.mEmptyView != null) {
                this.mEmptyView.setVisibility(0);
                this.setVisibility(8);
            } else {
                this.setVisibility(0);
            }
            if (this.mDataChanged) {
                this.onLayout(false, this.getLeft(), this.getTop(), this.getRight(), this.getBottom());
            }
        } else {
            if (this.mEmptyView != null) {
                this.mEmptyView.setVisibility(8);
            }
            this.setVisibility(0);
        }
    }

    @Nullable
    public Object getItemAtPosition(int position) {
        T adapter = this.getAdapter();
        return adapter != null && position >= 0 ? adapter.getItem(position) : null;
    }

    public long getItemIdAtPosition(int position) {
        T adapter = this.getAdapter();
        return adapter != null && position >= 0 ? adapter.getItemId(position) : Long.MIN_VALUE;
    }

    @Override
    public void setOnClickListener(@Nullable View.OnClickListener l) {
        throw new RuntimeException("Don't call setOnClickListener for an AdapterView. You probably want setOnItemClickListener instead");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.removeCallbacks(this.mSelectionNotifier);
    }

    void selectionChanged() {
        this.mPendingSelectionNotifier = null;
        if (this.mOnItemSelectedListener != null) {
            if (!this.mInLayout && !this.mBlockLayoutRequests) {
                this.dispatchOnItemSelected();
            } else {
                if (this.mSelectionNotifier == null) {
                    this.mSelectionNotifier = new AdapterView.SelectionNotifier();
                } else {
                    this.removeCallbacks(this.mSelectionNotifier);
                }
                this.post(this.mSelectionNotifier);
            }
        }
    }

    private void dispatchOnItemSelected() {
        this.fireOnSelected();
        this.performAccessibilityActionsOnSelected();
    }

    private void fireOnSelected() {
        if (this.mOnItemSelectedListener != null) {
            int selection = this.getSelectedItemPosition();
            if (selection >= 0) {
                View v = this.getSelectedView();
                this.mOnItemSelectedListener.onItemSelected(this, v, selection, this.getAdapter().getItemId(selection));
            } else {
                this.mOnItemSelectedListener.onNothingSelected(this);
            }
        }
    }

    private void performAccessibilityActionsOnSelected() {
    }

    private boolean isScrollableForAccessibility() {
        T adapter = this.getAdapter();
        if (adapter == null) {
            return false;
        } else {
            int itemCount = adapter.getCount();
            return itemCount > 0 && (this.getFirstVisiblePosition() > 0 || this.getLastVisiblePosition() < itemCount - 1);
        }
    }

    void handleDataChanged() {
        int count = this.mItemCount;
        boolean found = false;
        if (count > 0) {
            if (this.mNeedSync) {
                this.mNeedSync = false;
                int newPos = this.findSyncPosition();
                if (newPos >= 0) {
                    int selectablePos = this.lookForSelectablePosition(newPos, true);
                    if (selectablePos == newPos) {
                        this.setNextSelectedPositionInt(newPos);
                        found = true;
                    }
                }
            }
            if (!found) {
                int newPos = this.getSelectedItemPosition();
                if (newPos >= count) {
                    newPos = count - 1;
                }
                if (newPos < 0) {
                    newPos = 0;
                }
                int selectablePos = this.lookForSelectablePosition(newPos, true);
                if (selectablePos < 0) {
                    selectablePos = this.lookForSelectablePosition(newPos, false);
                }
                if (selectablePos >= 0) {
                    this.setNextSelectedPositionInt(selectablePos);
                    this.checkSelectionChanged();
                    found = true;
                }
            }
        }
        if (!found) {
            this.mSelectedPosition = -1;
            this.mSelectedRowId = Long.MIN_VALUE;
            this.mNextSelectedPosition = -1;
            this.mNextSelectedRowId = Long.MIN_VALUE;
            this.mNeedSync = false;
            this.checkSelectionChanged();
        }
    }

    void checkSelectionChanged() {
        if (this.mSelectedPosition != this.mOldSelectedPosition || this.mSelectedRowId != this.mOldSelectedRowId) {
            this.selectionChanged();
            this.mOldSelectedPosition = this.mSelectedPosition;
            this.mOldSelectedRowId = this.mSelectedRowId;
        }
        if (this.mPendingSelectionNotifier != null) {
            this.mPendingSelectionNotifier.run();
        }
    }

    int findSyncPosition() {
        int count = this.mItemCount;
        if (count == 0) {
            return -1;
        } else {
            long idToMatch = this.mSyncRowId;
            int seed = this.mSyncPosition;
            if (idToMatch == Long.MIN_VALUE) {
                return -1;
            } else {
                seed = Math.max(0, seed);
                seed = Math.min(count - 1, seed);
                long endTime = Core.timeMillis() + 100L;
                int first = seed;
                int last = seed;
                boolean next = false;
                T adapter = this.getAdapter();
                if (adapter == null) {
                    return -1;
                } else {
                    while (Core.timeMillis() <= endTime) {
                        long rowId = adapter.getItemId(seed);
                        if (rowId == idToMatch) {
                            return seed;
                        }
                        boolean hitLast = last == count - 1;
                        boolean hitFirst = first == 0;
                        if (hitLast && hitFirst) {
                            break;
                        }
                        if (!hitFirst && (!next || hitLast)) {
                            seed = --first;
                            next = true;
                        } else {
                            seed = ++last;
                            next = false;
                        }
                    }
                    return -1;
                }
            }
        }
    }

    int lookForSelectablePosition(int position, boolean lookDown) {
        return position;
    }

    void setSelectedPositionInt(int position) {
        this.mSelectedPosition = position;
        this.mSelectedRowId = this.getItemIdAtPosition(position);
    }

    void setNextSelectedPositionInt(int position) {
        this.mNextSelectedPosition = position;
        this.mNextSelectedRowId = this.getItemIdAtPosition(position);
        if (this.mNeedSync && this.mSyncMode == 0 && position >= 0) {
            this.mSyncPosition = position;
            this.mSyncRowId = this.mNextSelectedRowId;
        }
    }

    void rememberSyncState() {
        if (this.getChildCount() > 0) {
            this.mNeedSync = true;
            this.mSyncHeight = (long) this.mLayoutHeight;
            if (this.mSelectedPosition >= 0) {
                View v = this.getChildAt(this.mSelectedPosition - this.mFirstPosition);
                this.mSyncRowId = this.mNextSelectedRowId;
                this.mSyncPosition = this.mNextSelectedPosition;
                if (v != null) {
                    this.mSpecificTop = v.getTop();
                }
                this.mSyncMode = 0;
            } else {
                View v = this.getChildAt(0);
                T adapter = this.getAdapter();
                if (this.mFirstPosition >= 0 && this.mFirstPosition < adapter.getCount()) {
                    this.mSyncRowId = adapter.getItemId(this.mFirstPosition);
                } else {
                    this.mSyncRowId = -1L;
                }
                this.mSyncPosition = this.mFirstPosition;
                if (v != null) {
                    this.mSpecificTop = v.getTop();
                }
                this.mSyncMode = 1;
            }
        }
    }

    public static class AdapterContextMenuInfo implements ContextMenu.ContextMenuInfo {

        public View targetView;

        public int position;

        public long id;

        public AdapterContextMenuInfo(View targetView, int position, long id) {
            this.targetView = targetView;
            this.position = position;
            this.id = id;
        }
    }

    class AdapterDataSetObserver implements DataSetObserver {

        @Override
        public void onChanged() {
            AdapterView.this.mDataChanged = true;
            AdapterView.this.mOldItemCount = AdapterView.this.mItemCount;
            AdapterView.this.mItemCount = AdapterView.this.getAdapter().getCount();
            AdapterView.this.rememberSyncState();
            AdapterView.this.checkFocus();
            AdapterView.this.requestLayout();
        }

        @Override
        public void onInvalidated() {
            AdapterView.this.mDataChanged = true;
            AdapterView.this.mOldItemCount = AdapterView.this.mItemCount;
            AdapterView.this.mItemCount = 0;
            AdapterView.this.mSelectedPosition = -1;
            AdapterView.this.mSelectedRowId = Long.MIN_VALUE;
            AdapterView.this.mNextSelectedPosition = -1;
            AdapterView.this.mNextSelectedRowId = Long.MIN_VALUE;
            AdapterView.this.mNeedSync = false;
            AdapterView.this.checkFocus();
            AdapterView.this.requestLayout();
        }

        public void clearSavedState() {
        }
    }

    @FunctionalInterface
    public interface OnItemClickListener {

        void onItemClick(@Nonnull AdapterView<?> var1, View var2, int var3, long var4);
    }

    @FunctionalInterface
    public interface OnItemLongClickListener {

        boolean onItemLongClick(AdapterView<?> var1, View var2, int var3, long var4);
    }

    public interface OnItemSelectedListener {

        void onItemSelected(AdapterView<?> var1, View var2, int var3, long var4);

        void onNothingSelected(AdapterView<?> var1);
    }

    private class SelectionNotifier implements Runnable {

        public void run() {
            AdapterView.this.mPendingSelectionNotifier = null;
            if (!AdapterView.this.mDataChanged || AdapterView.this.getViewRoot() == null || !AdapterView.this.getViewRoot().isLayoutRequested()) {
                AdapterView.this.dispatchOnItemSelected();
            } else if (AdapterView.this.getAdapter() != null) {
                AdapterView.this.mPendingSelectionNotifier = this;
            }
        }
    }
}