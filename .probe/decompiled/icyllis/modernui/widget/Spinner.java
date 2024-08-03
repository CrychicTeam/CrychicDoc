package icyllis.modernui.widget;

import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.transition.AutoTransition;
import icyllis.modernui.util.DataSetObserver;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.MeasureSpec;
import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.view.PointerIcon;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.view.menu.ShowableListMenu;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Spinner extends AbsSpinner {

    private static final int MAX_ITEMS_MEASURED = 15;

    private final Rect mTempRect = new Rect();

    private final Spinner.DropdownPopup mPopup;

    int mDropDownWidth;

    private final ForwardingListener mForwardingListener;

    private int mGravity;

    public Spinner(Context context) {
        super(context);
        this.mPopup = new Spinner.DropdownPopup(context);
        this.mPopup.setBackgroundDrawable(new Drawable() {

            private final int mRadius = Spinner.this.dp(2.0F);

            @Override
            public void draw(@Nonnull Canvas canvas) {
                Paint paint = Paint.obtain();
                paint.setColor(-534765536);
                Rect b = this.getBounds();
                canvas.drawRoundRect((float) b.left, (float) b.top, (float) b.right, (float) b.bottom, (float) this.mRadius, paint);
                paint.recycle();
            }

            @Override
            public boolean getPadding(@Nonnull Rect padding) {
                int r = (int) Math.ceil((double) ((float) this.mRadius / 2.0F));
                padding.set(r, r, r, r);
                return true;
            }
        });
        this.mDropDownWidth = -2;
        this.mForwardingListener = new ForwardingListener(this) {

            @Override
            public ShowableListMenu getPopup() {
                return Spinner.this.mPopup;
            }

            @Override
            public boolean onForwardingStarted() {
                if (!Spinner.this.mPopup.isShowing()) {
                    Spinner.this.mPopup.show(Spinner.this.getTextDirection(), Spinner.this.getTextAlignment());
                }
                return true;
            }
        };
        this.mGravity = 8388627;
        this.setClickable(true);
    }

    public void setPopupBackgroundDrawable(@Nullable Drawable background) {
        this.mPopup.setBackgroundDrawable(background);
    }

    @Nullable
    public Drawable getPopupBackground() {
        return this.mPopup.getBackground();
    }

    public void setDropDownVerticalOffset(int pixels) {
        this.mPopup.setVerticalOffset(pixels);
    }

    public int getDropDownVerticalOffset() {
        return this.mPopup.getVerticalOffset();
    }

    public void setDropDownHorizontalOffset(int pixels) {
        this.mPopup.setHorizontalOffset(pixels);
    }

    public int getDropDownHorizontalOffset() {
        return this.mPopup.getHorizontalOffset();
    }

    public void setDropDownWidth(int pixels) {
        this.mDropDownWidth = pixels;
    }

    public int getDropDownWidth() {
        return this.mDropDownWidth;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        int count = this.getChildCount();
        for (int i = 0; i < count; i++) {
            this.getChildAt(i).setEnabled(enabled);
        }
    }

    public void setGravity(int gravity) {
        if (this.mGravity != gravity) {
            if ((gravity & 7) == 0) {
                gravity |= 8388611;
            }
            this.mGravity = gravity;
            this.requestLayout();
        }
    }

    public int getGravity() {
        return this.mGravity;
    }

    @Override
    public void setAdapter(@Nullable SpinnerAdapter adapter) {
        super.setAdapter(adapter);
        this.mRecycler.clear();
        if (adapter != null && adapter.getViewTypeCount() != 1) {
            throw new IllegalArgumentException("Spinner adapter view type count must be 1");
        } else {
            this.mPopup.setAdapter(new Spinner.DropDownAdapter(adapter));
        }
    }

    @Override
    public int getBaseline() {
        View child = null;
        if (this.getChildCount() > 0) {
            child = this.getChildAt(0);
        } else if (this.mAdapter != null && this.mAdapter.getCount() > 0) {
            child = this.makeView(0, false);
            this.mRecycler.put(0, child);
        }
        if (child != null) {
            int childBaseline = child.getBaseline();
            return childBaseline >= 0 ? child.getTop() + childBaseline : -1;
        } else {
            return -1;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mPopup != null && this.mPopup.isShowing()) {
            this.mPopup.dismiss();
        }
    }

    @Override
    public void setOnItemClickListener(@Nullable AdapterView.OnItemClickListener l) {
        throw new UnsupportedOperationException("setOnItemClickListener cannot be used with a spinner.");
    }

    @Override
    public boolean onTouchEvent(@Nonnull MotionEvent event) {
        return this.mForwardingListener != null && this.mForwardingListener.onTouch(this, event) ? true : super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.mPopup != null && MeasureSpec.getMode(widthMeasureSpec) == Integer.MIN_VALUE) {
            int measuredWidth = this.getMeasuredWidth();
            this.setMeasuredDimension(Math.min(Math.max(measuredWidth, this.measureContentWidth(this.getAdapter(), this.getBackground())), MeasureSpec.getSize(widthMeasureSpec)), this.getMeasuredHeight());
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.mInLayout = true;
        this.positionViews(0, false);
        this.mInLayout = false;
    }

    @Override
    void positionViews(int delta, boolean animate) {
        int childrenLeft = this.mSpinnerPaddingLeft;
        int childrenWidth = this.getWidth() - this.mSpinnerPaddingLeft - this.mSpinnerPaddingRight;
        if (this.mDataChanged) {
            this.handleDataChanged();
        }
        if (this.mItemCount == 0) {
            this.resetList();
        } else {
            if (this.mNextSelectedPosition >= 0) {
                this.setSelectedPositionInt(this.mNextSelectedPosition);
            }
            this.recycleAllViews();
            this.removeAllViewsInLayout();
            this.mFirstPosition = this.mSelectedPosition;
            if (this.mAdapter != null) {
                View sel = this.makeView(this.mSelectedPosition, true);
                int width = sel.getMeasuredWidth();
                int selectedOffset = childrenLeft;
                int layoutDirection = this.getLayoutDirection();
                int absoluteGravity = Gravity.getAbsoluteGravity(this.mGravity, layoutDirection);
                switch(absoluteGravity & 7) {
                    case 1:
                        selectedOffset = childrenLeft + childrenWidth / 2 - width / 2;
                        break;
                    case 5:
                        selectedOffset = childrenLeft + childrenWidth - width;
                }
                sel.offsetLeftAndRight(selectedOffset);
            }
            this.mRecycler.clear();
            this.invalidate();
            this.checkSelectionChanged();
            this.mDataChanged = false;
            this.mNeedSync = false;
            this.setNextSelectedPositionInt(this.mSelectedPosition);
        }
    }

    private View makeView(int position, boolean addChild) {
        if (!this.mDataChanged) {
            View child = this.mRecycler.get(position);
            if (child != null) {
                this.setUpChild(child, addChild);
                return child;
            }
        }
        View child = this.mAdapter.getView(position, null, this);
        this.setUpChild(child, addChild);
        return child;
    }

    private void setUpChild(@Nonnull View child, boolean addChild) {
        ViewGroup.LayoutParams lp = child.getLayoutParams();
        if (lp == null) {
            lp = this.generateDefaultLayoutParams();
        }
        this.addViewInLayout(child, 0, lp);
        child.setSelected(this.hasFocus());
        child.setEnabled(this.isEnabled());
        int childHeightSpec = ViewGroup.getChildMeasureSpec(this.mHeightMeasureSpec, this.mSpinnerPaddingTop + this.mSpinnerPaddingBottom, lp.height);
        int childWidthSpec = ViewGroup.getChildMeasureSpec(this.mWidthMeasureSpec, this.mSpinnerPaddingLeft + this.mSpinnerPaddingRight, lp.width);
        child.measure(childWidthSpec, childHeightSpec);
        int childTop = this.mSpinnerPaddingTop + (this.getMeasuredHeight() - this.mSpinnerPaddingBottom - this.mSpinnerPaddingTop - child.getMeasuredHeight()) / 2;
        int childBottom = childTop + child.getMeasuredHeight();
        int width = child.getMeasuredWidth();
        int childLeft = 0;
        int childRight = childLeft + width;
        child.layout(childLeft, childTop, childRight, childBottom);
        if (!addChild) {
            this.removeViewInLayout(child);
        }
    }

    @Override
    public boolean performClick() {
        boolean handled = super.performClick();
        if (!handled && !this.mPopup.isShowing()) {
            this.mPopup.show(this.getTextDirection(), this.getTextAlignment());
        }
        return true;
    }

    int measureContentWidth(@Nullable SpinnerAdapter adapter, @Nullable Drawable background) {
        if (adapter == null) {
            return 0;
        } else {
            int width = 0;
            View itemView = null;
            int itemType = 0;
            int widthMeasureSpec = MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), 0);
            int heightMeasureSpec = MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 0);
            int start = Math.max(0, this.getSelectedItemPosition());
            int end = Math.min(adapter.getCount(), start + 15);
            int count = end - start;
            start = Math.max(0, start - (15 - count));
            for (int i = start; i < end; i++) {
                int positionType = adapter.getItemViewType(i);
                if (positionType != itemType) {
                    itemType = positionType;
                    itemView = null;
                }
                itemView = adapter.getView(i, itemView, this);
                if (itemView.getLayoutParams() == null) {
                    itemView.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
                }
                itemView.measure(widthMeasureSpec, heightMeasureSpec);
                width = Math.max(width, itemView.getMeasuredWidth());
            }
            if (background != null) {
                background.getPadding(this.mTempRect);
                width += this.mTempRect.left + this.mTempRect.right;
            }
            return width;
        }
    }

    @Override
    public PointerIcon onResolvePointerIcon(@Nonnull MotionEvent event) {
        return this.isClickable() && this.isEnabled() ? PointerIcon.getSystemIcon(1002) : super.onResolvePointerIcon(event);
    }

    private static class DropDownAdapter implements ListAdapter, SpinnerAdapter {

        private final SpinnerAdapter mAdapter;

        private final ListAdapter mListAdapter;

        public DropDownAdapter(@Nullable SpinnerAdapter adapter) {
            this.mAdapter = adapter;
            if (adapter instanceof ListAdapter) {
                this.mListAdapter = (ListAdapter) adapter;
            } else {
                this.mListAdapter = null;
            }
        }

        @Override
        public int getCount() {
            return this.mAdapter == null ? 0 : this.mAdapter.getCount();
        }

        @Override
        public Object getItem(int position) {
            return this.mAdapter == null ? null : this.mAdapter.getItem(position);
        }

        @Override
        public long getItemId(int position) {
            return this.mAdapter == null ? -1L : this.mAdapter.getItemId(position);
        }

        @Nullable
        @Override
        public View getView(int position, View convertView, @Nonnull ViewGroup parent) {
            return this.getDropDownView(position, convertView, parent);
        }

        @Nullable
        @Override
        public View getDropDownView(int position, View convertView, @Nonnull ViewGroup parent) {
            return this.mAdapter == null ? null : this.mAdapter.getDropDownView(position, convertView, parent);
        }

        @Override
        public boolean hasStableIds() {
            return this.mAdapter != null && this.mAdapter.hasStableIds();
        }

        @Override
        public void registerDataSetObserver(@Nonnull DataSetObserver observer) {
            if (this.mAdapter != null) {
                this.mAdapter.registerDataSetObserver(observer);
            }
        }

        @Override
        public void unregisterDataSetObserver(@Nonnull DataSetObserver observer) {
            if (this.mAdapter != null) {
                this.mAdapter.unregisterDataSetObserver(observer);
            }
        }

        @Override
        public boolean areAllItemsEnabled() {
            ListAdapter adapter = this.mListAdapter;
            return adapter != null ? adapter.areAllItemsEnabled() : true;
        }

        @Override
        public boolean isEnabled(int position) {
            ListAdapter adapter = this.mListAdapter;
            return adapter != null ? adapter.isEnabled(position) : true;
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return this.mAdapter == null || this.mAdapter.isEmpty();
        }
    }

    private class DropdownPopup extends ListPopupWindow {

        private ListAdapter mAdapter;

        public DropdownPopup(Context context) {
            super(context);
            this.setAnchorView(Spinner.this);
            this.setModal(true);
            this.setPromptPosition(0);
            this.setOnItemClickListener((parent, v, position, id) -> {
                Spinner.this.setSelection(position);
                if (Spinner.this.mOnItemClickListener != null) {
                    Spinner.this.performItemClick(v, position, this.mAdapter.getItemId(position));
                }
                this.dismiss();
            });
            this.mPopup.setEnterTransition(new AutoTransition());
            this.mPopup.setExitTransition(new AutoTransition());
        }

        @Override
        public void setAdapter(@Nullable ListAdapter adapter) {
            super.setAdapter(adapter);
            this.mAdapter = adapter;
        }

        void computeContentWidth() {
            Drawable background = this.getBackground();
            int hOffset = 0;
            if (background != null) {
                background.getPadding(Spinner.this.mTempRect);
                hOffset = Spinner.this.isLayoutRtl() ? Spinner.this.mTempRect.right : -Spinner.this.mTempRect.left;
            } else {
                Spinner.this.mTempRect.left = Spinner.this.mTempRect.right = 0;
            }
            int spinnerPaddingLeft = Spinner.this.getPaddingLeft();
            int spinnerPaddingRight = Spinner.this.getPaddingRight();
            int spinnerWidth = Spinner.this.getWidth();
            if (Spinner.this.mDropDownWidth == -2) {
                int contentWidth = Spinner.this.measureContentWidth((SpinnerAdapter) this.mAdapter, this.getBackground());
                int contentWidthLimit = Spinner.this.getRootView().getMeasuredWidth() - Spinner.this.mTempRect.left - Spinner.this.mTempRect.right;
                if (contentWidth > contentWidthLimit) {
                    contentWidth = contentWidthLimit;
                }
                this.setContentWidth(Math.max(contentWidth, spinnerWidth - spinnerPaddingLeft - spinnerPaddingRight));
            } else if (Spinner.this.mDropDownWidth == -1) {
                this.setContentWidth(spinnerWidth - spinnerPaddingLeft - spinnerPaddingRight);
            } else {
                this.setContentWidth(Spinner.this.mDropDownWidth);
            }
            if (Spinner.this.isLayoutRtl()) {
                hOffset += spinnerWidth - spinnerPaddingRight - this.getWidth();
            } else {
                hOffset += spinnerPaddingLeft;
            }
            this.setHorizontalOffset(hOffset);
        }

        public void show(int textDirection, int textAlignment) {
            this.computeContentWidth();
            super.show();
            ListView listView = this.getListView();
            assert listView != null;
            listView.setChoiceMode(1);
            listView.setTextDirection(textDirection);
            listView.setTextAlignment(textAlignment);
            this.setSelection(Spinner.this.getSelectedItemPosition());
        }
    }
}