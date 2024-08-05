package icyllis.modernui.widget;

import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.util.DataSetObserver;
import icyllis.modernui.util.SparseArray;
import icyllis.modernui.view.MeasureSpec;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbsSpinner extends AdapterView<SpinnerAdapter> {

    SpinnerAdapter mAdapter;

    int mWidthMeasureSpec;

    int mHeightMeasureSpec;

    int mSelectionPaddingLeft;

    int mSelectionPaddingTop;

    int mSelectionPaddingRight;

    int mSelectionPaddingBottom;

    int mSpinnerPaddingLeft;

    int mSpinnerPaddingTop;

    int mSpinnerPaddingRight;

    int mSpinnerPaddingBottom;

    final AbsSpinner.RecycleBin mRecycler = new AbsSpinner.RecycleBin();

    private DataSetObserver mDataSetObserver;

    private Rect mTouchFrame;

    AbsSpinner(Context context) {
        super(context);
        this.setFocusable(true);
        this.setWillNotDraw(true);
    }

    public void setAdapter(@Nullable SpinnerAdapter adapter) {
        if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(this.mDataSetObserver);
            this.resetList();
        }
        this.mAdapter = adapter;
        this.mOldSelectedPosition = -1;
        this.mOldSelectedRowId = Long.MIN_VALUE;
        if (this.mAdapter != null) {
            this.mOldItemCount = this.mItemCount;
            this.mItemCount = this.mAdapter.getCount();
            this.checkFocus();
            this.mDataSetObserver = new AdapterView.AdapterDataSetObserver();
            this.mAdapter.registerDataSetObserver(this.mDataSetObserver);
            int position = this.mItemCount > 0 ? 0 : -1;
            this.setSelectedPositionInt(position);
            this.setNextSelectedPositionInt(position);
            if (this.mItemCount == 0) {
                this.checkSelectionChanged();
            }
        } else {
            this.checkFocus();
            this.resetList();
            this.checkSelectionChanged();
        }
        this.requestLayout();
    }

    void resetList() {
        this.mDataChanged = false;
        this.mNeedSync = false;
        this.removeAllViewsInLayout();
        this.mOldSelectedPosition = -1;
        this.mOldSelectedRowId = Long.MIN_VALUE;
        this.setSelectedPositionInt(-1);
        this.setNextSelectedPositionInt(-1);
        this.invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.mWidthMeasureSpec = widthMeasureSpec;
        this.mHeightMeasureSpec = heightMeasureSpec;
        this.mSpinnerPaddingLeft = Math.max(this.mPaddingLeft, this.mSelectionPaddingLeft);
        this.mSpinnerPaddingTop = Math.max(this.mPaddingTop, this.mSelectionPaddingTop);
        this.mSpinnerPaddingRight = Math.max(this.mPaddingRight, this.mSelectionPaddingRight);
        this.mSpinnerPaddingBottom = Math.max(this.mPaddingBottom, this.mSelectionPaddingBottom);
        if (this.mDataChanged) {
            this.handleDataChanged();
        }
        int preferredWidth = 0;
        int preferredHeight = 0;
        boolean needsMeasuring = true;
        int selectedPosition = this.getSelectedItemPosition();
        if (selectedPosition >= 0 && this.mAdapter != null && selectedPosition < this.mAdapter.getCount()) {
            View view = this.mRecycler.get(selectedPosition);
            if (view == null) {
                view = this.mAdapter.getView(selectedPosition, null, this);
            }
            this.mRecycler.put(selectedPosition, view);
            if (view.getLayoutParams() == null) {
                this.mBlockLayoutRequests = true;
                view.setLayoutParams(this.generateDefaultLayoutParams());
                this.mBlockLayoutRequests = false;
            }
            this.measureChild(view, widthMeasureSpec, heightMeasureSpec);
            preferredWidth = view.getMeasuredWidth() + this.mSpinnerPaddingLeft + this.mSpinnerPaddingRight;
            preferredHeight = view.getMeasuredHeight() + this.mSpinnerPaddingTop + this.mSpinnerPaddingBottom;
            needsMeasuring = false;
        }
        if (needsMeasuring) {
            if (MeasureSpec.getMode(widthMeasureSpec) == 0) {
                preferredWidth = this.mSpinnerPaddingLeft + this.mSpinnerPaddingRight;
            }
            preferredHeight = this.mSpinnerPaddingTop + this.mSpinnerPaddingBottom;
        }
        preferredWidth = Math.max(preferredWidth, this.getSuggestedMinimumWidth());
        preferredHeight = Math.max(preferredHeight, this.getSuggestedMinimumHeight());
        int widthSize = resolveSizeAndState(preferredWidth, widthMeasureSpec, 0);
        int heightSize = resolveSizeAndState(preferredHeight, heightMeasureSpec, 0);
        this.setMeasuredDimension(widthSize, heightSize);
    }

    @Nonnull
    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new ViewGroup.LayoutParams(-1, -2);
    }

    void recycleAllViews() {
        int childCount = this.getChildCount();
        int position = this.mFirstPosition;
        for (int i = 0; i < childCount; i++) {
            View v = this.getChildAt(i);
            int index = position + i;
            this.mRecycler.put(index, v);
        }
    }

    public void setSelection(int position, boolean animate) {
        boolean shouldAnimate = animate && this.mFirstPosition <= position && position <= this.mFirstPosition + this.getChildCount() - 1;
        this.setSelectionInt(position, shouldAnimate);
    }

    @Override
    public void setSelection(int position) {
        this.setNextSelectedPositionInt(position);
        this.requestLayout();
        this.invalidate();
    }

    void setSelectionInt(int position, boolean animate) {
        if (position != this.mOldSelectedPosition) {
            this.mBlockLayoutRequests = true;
            int delta = position - this.mSelectedPosition;
            this.setNextSelectedPositionInt(position);
            this.positionViews(delta, animate);
            this.mBlockLayoutRequests = false;
        }
    }

    abstract void positionViews(int var1, boolean var2);

    @Override
    public View getSelectedView() {
        return this.mItemCount > 0 && this.mSelectedPosition >= 0 ? this.getChildAt(this.mSelectedPosition - this.mFirstPosition) : null;
    }

    @Override
    public void requestLayout() {
        if (!this.mBlockLayoutRequests) {
            super.requestLayout();
        }
    }

    public SpinnerAdapter getAdapter() {
        return this.mAdapter;
    }

    @Override
    public int getCount() {
        return this.mItemCount;
    }

    public int pointToPosition(int x, int y) {
        Rect frame = this.mTouchFrame;
        if (frame == null) {
            this.mTouchFrame = new Rect();
            frame = this.mTouchFrame;
        }
        int count = this.getChildCount();
        for (int i = count - 1; i >= 0; i--) {
            View child = this.getChildAt(i);
            if (child.getVisibility() == 0) {
                child.getHitRect(frame);
                if (frame.contains(x, y)) {
                    return this.mFirstPosition + i;
                }
            }
        }
        return -1;
    }

    class RecycleBin {

        private final SparseArray<View> mScrapHeap = new SparseArray<>();

        void put(int position, @Nonnull View v) {
            this.mScrapHeap.put(position, v);
        }

        @Nullable
        View get(int position) {
            return this.mScrapHeap.remove(position);
        }

        void clear() {
            SparseArray<View> heap = this.mScrapHeap;
            int i = 0;
            for (int e = heap.size(); i < e; i++) {
                View v = heap.valueAt(i);
                AbsSpinner.this.removeDetachedView(v, true);
            }
            heap.clear();
        }
    }
}