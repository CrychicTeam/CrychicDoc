package icyllis.modernui.widget;

import icyllis.modernui.ModernUI;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.MathUtil;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.view.FocusFinder;
import icyllis.modernui.view.KeyEvent;
import icyllis.modernui.view.MeasureSpec;
import icyllis.modernui.view.SoundEffectConstants;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.view.ViewParent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.ApiStatus.Internal;

public class ListView extends AbsListView {

    static final Marker MARKER = MarkerManager.getMarker("ListView");

    static final int NO_POSITION = -1;

    private static final float MAX_SCROLL_FACTOR = 0.33F;

    private static final int MIN_SCROLL_PREVIEW_PIXELS = 2;

    ArrayList<ListView.FixedViewInfo> mHeaderViewInfos = new ArrayList();

    ArrayList<ListView.FixedViewInfo> mFooterViewInfos = new ArrayList();

    Drawable mDivider;

    int mDividerHeight;

    Drawable mOverScrollHeader;

    Drawable mOverScrollFooter;

    private boolean mHeaderDividersEnabled;

    private boolean mFooterDividersEnabled;

    private boolean mAreAllItemsSelectable = true;

    private boolean mItemsCanFocus = false;

    private final Rect mTempRect = new Rect();

    private final ListView.ArrowScrollFocusResult mArrowScrollFocusResult = new ListView.ArrowScrollFocusResult();

    private ListView.FocusSelector mFocusSelector;

    public ListView(Context context) {
        super(context);
        this.mHeaderDividersEnabled = true;
        this.mFooterDividersEnabled = true;
    }

    public int getMaxScrollAmount() {
        return (int) (0.33F * (float) this.getHeight());
    }

    private void adjustViewsUpOrDown() {
        int childCount = this.getChildCount();
        if (childCount > 0) {
            int delta;
            if (!this.mStackFromBottom) {
                View child = this.getChildAt(0);
                delta = child.getTop() - this.mListPadding.top;
                if (this.mFirstPosition != 0) {
                    delta -= this.mDividerHeight;
                }
                if (delta < 0) {
                    delta = 0;
                }
            } else {
                View childx = this.getChildAt(childCount - 1);
                delta = childx.getBottom() - (this.getHeight() - this.mListPadding.bottom);
                if (this.mFirstPosition + childCount < this.mItemCount) {
                    delta += this.mDividerHeight;
                }
                if (delta > 0) {
                    delta = 0;
                }
            }
            if (delta != 0) {
                this.offsetChildrenTopAndBottom(-delta);
            }
        }
    }

    public void addHeaderView(@NonNull View v, Object data, boolean isSelectable) {
        if (v.getParent() != null && v.getParent() != this) {
            ModernUI.LOGGER.warn(MARKER, "The specified child already has a parent. You must call removeView() on the child's parent first.");
        }
        ListView.FixedViewInfo info = new ListView.FixedViewInfo();
        info.view = v;
        info.data = data;
        info.isSelectable = isSelectable;
        this.mHeaderViewInfos.add(info);
        this.mAreAllItemsSelectable &= isSelectable;
        if (this.mAdapter != null) {
            if (!(this.mAdapter instanceof HeaderViewListAdapter)) {
                this.wrapHeaderListAdapterInternal();
            }
            if (this.mDataSetObserver != null) {
                this.mDataSetObserver.onChanged();
            }
        }
    }

    public void addHeaderView(@NonNull View v) {
        this.addHeaderView(v, null, true);
    }

    @Override
    public int getHeaderViewsCount() {
        return this.mHeaderViewInfos.size();
    }

    public boolean removeHeaderView(@NonNull View v) {
        if (this.mHeaderViewInfos.size() > 0) {
            boolean result = false;
            if (this.mAdapter != null && ((HeaderViewListAdapter) this.mAdapter).removeHeader(v)) {
                if (this.mDataSetObserver != null) {
                    this.mDataSetObserver.onChanged();
                }
                result = true;
            }
            this.removeFixedViewInfo(v, this.mHeaderViewInfos);
            return result;
        } else {
            return false;
        }
    }

    private void removeFixedViewInfo(@NonNull View v, @NonNull ArrayList<ListView.FixedViewInfo> where) {
        int len = where.size();
        for (int i = 0; i < len; i++) {
            ListView.FixedViewInfo info = (ListView.FixedViewInfo) where.get(i);
            if (info.view == v) {
                where.remove(i);
                break;
            }
        }
    }

    public void addFooterView(@NonNull View v, Object data, boolean isSelectable) {
        if (v.getParent() != null && v.getParent() != this) {
            ModernUI.LOGGER.warn(MARKER, "The specified child already has a parent. You must call removeView() on the child's parent first.");
        }
        ListView.FixedViewInfo info = new ListView.FixedViewInfo();
        info.view = v;
        info.data = data;
        info.isSelectable = isSelectable;
        this.mFooterViewInfos.add(info);
        this.mAreAllItemsSelectable &= isSelectable;
        if (this.mAdapter != null) {
            if (!(this.mAdapter instanceof HeaderViewListAdapter)) {
                this.wrapHeaderListAdapterInternal();
            }
            if (this.mDataSetObserver != null) {
                this.mDataSetObserver.onChanged();
            }
        }
    }

    public void addFooterView(@NonNull View v) {
        this.addFooterView(v, null, true);
    }

    @Override
    public int getFooterViewsCount() {
        return this.mFooterViewInfos.size();
    }

    public boolean removeFooterView(@NonNull View v) {
        if (this.mFooterViewInfos.size() > 0) {
            boolean result = false;
            if (this.mAdapter != null && ((HeaderViewListAdapter) this.mAdapter).removeFooter(v)) {
                if (this.mDataSetObserver != null) {
                    this.mDataSetObserver.onChanged();
                }
                result = true;
            }
            this.removeFixedViewInfo(v, this.mFooterViewInfos);
            return result;
        } else {
            return false;
        }
    }

    public ListAdapter getAdapter() {
        return this.mAdapter;
    }

    @Override
    public void setAdapter(@Nullable ListAdapter adapter) {
        if (this.mAdapter != null && this.mDataSetObserver != null) {
            this.mAdapter.unregisterDataSetObserver(this.mDataSetObserver);
        }
        this.resetList();
        this.mRecycler.clear();
        if (this.mHeaderViewInfos.size() <= 0 && this.mFooterViewInfos.size() <= 0) {
            this.mAdapter = adapter;
        } else {
            this.mAdapter = this.wrapHeaderListAdapterInternal(this.mHeaderViewInfos, this.mFooterViewInfos, adapter);
        }
        this.mOldSelectedPosition = -1;
        this.mOldSelectedRowId = Long.MIN_VALUE;
        super.setAdapter(adapter);
        if (this.mAdapter != null) {
            this.mAreAllItemsSelectable = this.mAdapter.areAllItemsEnabled();
            this.mOldItemCount = this.mItemCount;
            this.mItemCount = this.mAdapter.getCount();
            this.checkFocus();
            this.mDataSetObserver = new AdapterView.AdapterDataSetObserver();
            this.mAdapter.registerDataSetObserver(this.mDataSetObserver);
            this.mRecycler.setViewTypeCount(this.mAdapter.getViewTypeCount());
            int position;
            if (this.mStackFromBottom) {
                position = this.lookForSelectablePosition(this.mItemCount - 1, false);
            } else {
                position = this.lookForSelectablePosition(0, true);
            }
            this.setSelectedPositionInt(position);
            this.setNextSelectedPositionInt(position);
            if (this.mItemCount == 0) {
                this.checkSelectionChanged();
            }
        } else {
            this.mAreAllItemsSelectable = true;
            this.checkFocus();
            this.checkSelectionChanged();
        }
        this.requestLayout();
    }

    @Override
    void resetList() {
        this.clearRecycledState(this.mHeaderViewInfos);
        this.clearRecycledState(this.mFooterViewInfos);
        super.resetList();
        this.mLayoutMode = 0;
    }

    private void clearRecycledState(@NonNull ArrayList<ListView.FixedViewInfo> infos) {
        for (ListView.FixedViewInfo info : infos) {
            View child = info.view;
            ViewGroup.LayoutParams params = child.getLayoutParams();
            if (this.checkLayoutParams(params)) {
                ((AbsListView.LayoutParams) params).recycledHeaderFooter = false;
            }
        }
    }

    private boolean showingTopFadingEdge() {
        int listTop = this.getScrollY() + this.mListPadding.top;
        return this.mFirstPosition > 0 || this.getChildAt(0).getTop() > listTop;
    }

    private boolean showingBottomFadingEdge() {
        int childCount = this.getChildCount();
        int bottomOfBottomChild = this.getChildAt(childCount - 1).getBottom();
        int lastVisiblePosition = this.mFirstPosition + childCount - 1;
        int listBottom = this.getScrollY() + this.getHeight() - this.mListPadding.bottom;
        return lastVisiblePosition < this.mItemCount - 1 || bottomOfBottomChild < listBottom;
    }

    @Override
    public boolean requestChildRectangleOnScreen(@NonNull View child, @NonNull Rect rect, boolean immediate) {
        int rectTopWithinChild = rect.top;
        rect.offset(child.getLeft(), child.getTop());
        rect.offset(-child.getScrollX(), -child.getScrollY());
        int height = this.getHeight();
        int listUnfadedTop = this.getScrollY();
        int listUnfadedBottom = listUnfadedTop + height;
        int fadingEdge = this.getVerticalFadingEdgeLength();
        if (this.showingTopFadingEdge() && (this.mSelectedPosition > 0 || rectTopWithinChild > fadingEdge)) {
            listUnfadedTop += fadingEdge;
        }
        int childCount = this.getChildCount();
        int bottomOfBottomChild = this.getChildAt(childCount - 1).getBottom();
        if (this.showingBottomFadingEdge() && (this.mSelectedPosition < this.mItemCount - 1 || rect.bottom < bottomOfBottomChild - fadingEdge)) {
            listUnfadedBottom -= fadingEdge;
        }
        int scrollYDelta = 0;
        if (rect.bottom > listUnfadedBottom && rect.top > listUnfadedTop) {
            if (rect.height() > height) {
                scrollYDelta += rect.top - listUnfadedTop;
            } else {
                scrollYDelta += rect.bottom - listUnfadedBottom;
            }
            int distanceToBottom = bottomOfBottomChild - listUnfadedBottom;
            scrollYDelta = Math.min(scrollYDelta, distanceToBottom);
        } else if (rect.top < listUnfadedTop && rect.bottom < listUnfadedBottom) {
            if (rect.height() > height) {
                scrollYDelta -= listUnfadedBottom - rect.bottom;
            } else {
                scrollYDelta -= listUnfadedTop - rect.top;
            }
            int top = this.getChildAt(0).getTop();
            int deltaToTop = top - listUnfadedTop;
            scrollYDelta = Math.max(scrollYDelta, deltaToTop);
        }
        boolean scroll = scrollYDelta != 0;
        if (scroll) {
            this.scrollListItemsBy(-scrollYDelta);
            this.positionSelector(-1, child);
            this.mSelectedTop = child.getTop();
            this.invalidate();
        }
        return scroll;
    }

    @Override
    void fillGap(boolean down) {
        int count = this.getChildCount();
        if (down) {
            int paddingTop = 0;
            if (this.hasBooleanFlag(34)) {
                paddingTop = this.getListPaddingTop();
            }
            int startOffset = count > 0 ? this.getChildAt(count - 1).getBottom() + this.mDividerHeight : paddingTop;
            this.fillDown(this.mFirstPosition + count, startOffset);
            this.correctTooHigh(this.getChildCount());
        } else {
            int paddingBottom = 0;
            if (this.hasBooleanFlag(34)) {
                paddingBottom = this.getListPaddingBottom();
            }
            int startOffset = count > 0 ? this.getChildAt(0).getTop() - this.mDividerHeight : this.getHeight() - paddingBottom;
            this.fillUp(this.mFirstPosition - 1, startOffset);
            this.correctTooLow(this.getChildCount());
        }
    }

    private View fillDown(int pos, int nextTop) {
        View selectedView = null;
        int end = this.getHeight();
        if (this.hasBooleanFlag(34)) {
            end -= this.mListPadding.bottom;
        }
        while (nextTop < end && pos < this.mItemCount) {
            boolean selected = pos == this.mSelectedPosition;
            View child = this.makeAndAddView(pos, nextTop, true, this.mListPadding.left, selected);
            nextTop = child.getBottom() + this.mDividerHeight;
            if (selected) {
                selectedView = child;
            }
            pos++;
        }
        return selectedView;
    }

    private View fillUp(int pos, int nextBottom) {
        View selectedView = null;
        int end = 0;
        if (this.hasBooleanFlag(34)) {
            end = this.mListPadding.top;
        }
        while (nextBottom > end && pos >= 0) {
            boolean selected = pos == this.mSelectedPosition;
            View child = this.makeAndAddView(pos, nextBottom, false, this.mListPadding.left, selected);
            nextBottom = child.getTop() - this.mDividerHeight;
            if (selected) {
                selectedView = child;
            }
            pos--;
        }
        this.mFirstPosition = pos + 1;
        return selectedView;
    }

    private View fillFromTop(int nextTop) {
        this.mFirstPosition = Math.min(this.mFirstPosition, this.mSelectedPosition);
        this.mFirstPosition = Math.min(this.mFirstPosition, this.mItemCount - 1);
        if (this.mFirstPosition < 0) {
            this.mFirstPosition = 0;
        }
        return this.fillDown(this.mFirstPosition, nextTop);
    }

    @NonNull
    private View fillFromMiddle(int childrenTop, int childrenBottom) {
        int height = childrenBottom - childrenTop;
        int position = this.reconcileSelectedPosition();
        View sel = this.makeAndAddView(position, childrenTop, true, this.mListPadding.left, true);
        this.mFirstPosition = position;
        int selHeight = sel.getMeasuredHeight();
        if (selHeight <= height) {
            sel.offsetTopAndBottom((height - selHeight) / 2);
        }
        this.fillAboveAndBelow(sel, position);
        if (!this.mStackFromBottom) {
            this.correctTooHigh(this.getChildCount());
        } else {
            this.correctTooLow(this.getChildCount());
        }
        return sel;
    }

    private void fillAboveAndBelow(View sel, int position) {
        int dividerHeight = this.mDividerHeight;
        if (!this.mStackFromBottom) {
            this.fillUp(position - 1, sel.getTop() - dividerHeight);
            this.adjustViewsUpOrDown();
            this.fillDown(position + 1, sel.getBottom() + dividerHeight);
        } else {
            this.fillDown(position + 1, sel.getBottom() + dividerHeight);
            this.adjustViewsUpOrDown();
            this.fillUp(position - 1, sel.getTop() - dividerHeight);
        }
    }

    @NonNull
    private View fillFromSelection(int selectedTop, int childrenTop, int childrenBottom) {
        int fadingEdgeLength = this.getVerticalFadingEdgeLength();
        int selectedPosition = this.mSelectedPosition;
        int topSelectionPixel = this.getTopSelectionPixel(childrenTop, fadingEdgeLength, selectedPosition);
        int bottomSelectionPixel = this.getBottomSelectionPixel(childrenBottom, fadingEdgeLength, selectedPosition);
        View sel = this.makeAndAddView(selectedPosition, selectedTop, true, this.mListPadding.left, true);
        if (sel.getBottom() > bottomSelectionPixel) {
            int spaceAbove = sel.getTop() - topSelectionPixel;
            int spaceBelow = sel.getBottom() - bottomSelectionPixel;
            int offset = Math.min(spaceAbove, spaceBelow);
            sel.offsetTopAndBottom(-offset);
        } else if (sel.getTop() < topSelectionPixel) {
            int spaceAbove = topSelectionPixel - sel.getTop();
            int spaceBelow = bottomSelectionPixel - sel.getBottom();
            int offset = Math.min(spaceAbove, spaceBelow);
            sel.offsetTopAndBottom(offset);
        }
        this.fillAboveAndBelow(sel, selectedPosition);
        if (!this.mStackFromBottom) {
            this.correctTooHigh(this.getChildCount());
        } else {
            this.correctTooLow(this.getChildCount());
        }
        return sel;
    }

    private int getBottomSelectionPixel(int childrenBottom, int fadingEdgeLength, int selectedPosition) {
        int bottomSelectionPixel = childrenBottom;
        if (selectedPosition != this.mItemCount - 1) {
            bottomSelectionPixel = childrenBottom - fadingEdgeLength;
        }
        return bottomSelectionPixel;
    }

    private int getTopSelectionPixel(int childrenTop, int fadingEdgeLength, int selectedPosition) {
        int topSelectionPixel = childrenTop;
        if (selectedPosition > 0) {
            topSelectionPixel = childrenTop + fadingEdgeLength;
        }
        return topSelectionPixel;
    }

    @Override
    public void smoothScrollToPosition(int position) {
        super.smoothScrollToPosition(position);
    }

    @Override
    public void smoothScrollByOffset(int offset) {
        super.smoothScrollByOffset(offset);
    }

    private View moveSelection(View oldSel, View newSel, int delta, int childrenTop, int childrenBottom) {
        int fadingEdgeLength = this.getVerticalFadingEdgeLength();
        int selectedPosition = this.mSelectedPosition;
        int topSelectionPixel = this.getTopSelectionPixel(childrenTop, fadingEdgeLength, selectedPosition);
        int bottomSelectionPixel = this.getBottomSelectionPixel(childrenTop, fadingEdgeLength, selectedPosition);
        View sel;
        if (delta > 0) {
            oldSel = this.makeAndAddView(selectedPosition - 1, oldSel.getTop(), true, this.mListPadding.left, false);
            int dividerHeight = this.mDividerHeight;
            sel = this.makeAndAddView(selectedPosition, oldSel.getBottom() + dividerHeight, true, this.mListPadding.left, true);
            if (sel.getBottom() > bottomSelectionPixel) {
                int spaceAbove = sel.getTop() - topSelectionPixel;
                int spaceBelow = sel.getBottom() - bottomSelectionPixel;
                int halfVerticalSpace = (childrenBottom - childrenTop) / 2;
                int offset = Math.min(spaceAbove, spaceBelow);
                offset = Math.min(offset, halfVerticalSpace);
                oldSel.offsetTopAndBottom(-offset);
                sel.offsetTopAndBottom(-offset);
            }
            if (!this.mStackFromBottom) {
                this.fillUp(this.mSelectedPosition - 2, sel.getTop() - dividerHeight);
                this.adjustViewsUpOrDown();
                this.fillDown(this.mSelectedPosition + 1, sel.getBottom() + dividerHeight);
            } else {
                this.fillDown(this.mSelectedPosition + 1, sel.getBottom() + dividerHeight);
                this.adjustViewsUpOrDown();
                this.fillUp(this.mSelectedPosition - 2, sel.getTop() - dividerHeight);
            }
        } else if (delta < 0) {
            if (newSel != null) {
                sel = this.makeAndAddView(selectedPosition, newSel.getTop(), true, this.mListPadding.left, true);
            } else {
                sel = this.makeAndAddView(selectedPosition, oldSel.getTop(), false, this.mListPadding.left, true);
            }
            if (sel.getTop() < topSelectionPixel) {
                int spaceAbove = topSelectionPixel - sel.getTop();
                int spaceBelow = bottomSelectionPixel - sel.getBottom();
                int halfVerticalSpace = (childrenBottom - childrenTop) / 2;
                int offset = Math.min(spaceAbove, spaceBelow);
                offset = Math.min(offset, halfVerticalSpace);
                sel.offsetTopAndBottom(offset);
            }
            this.fillAboveAndBelow(sel, selectedPosition);
        } else {
            int oldTop = oldSel.getTop();
            sel = this.makeAndAddView(selectedPosition, oldTop, true, this.mListPadding.left, true);
            if (oldTop < childrenTop) {
                int newBottom = sel.getBottom();
                if (newBottom < childrenTop + 20) {
                    sel.offsetTopAndBottom(childrenTop - sel.getTop());
                }
            }
            this.fillAboveAndBelow(sel, selectedPosition);
        }
        return sel;
    }

    @Override
    protected void onDetachedFromWindow() {
        if (this.mFocusSelector != null) {
            this.removeCallbacks(this.mFocusSelector);
            this.mFocusSelector = null;
        }
        super.onDetachedFromWindow();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (this.getChildCount() > 0) {
            View focusedChild = this.getFocusedChild();
            if (focusedChild != null) {
                int childPosition = this.mFirstPosition + this.indexOfChild(focusedChild);
                int childBottom = focusedChild.getBottom();
                int offset = Math.max(0, childBottom - (h - this.mPaddingTop));
                int top = focusedChild.getTop() - offset;
                if (this.mFocusSelector == null) {
                    this.mFocusSelector = new ListView.FocusSelector();
                }
                this.post(this.mFocusSelector.setupForSetSelection(childPosition, top));
            }
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int childWidth = 0;
        int childHeight = 0;
        int childState = 0;
        this.mItemCount = this.mAdapter == null ? 0 : this.mAdapter.getCount();
        if (this.mItemCount > 0 && (widthMode == 0 || heightMode == 0)) {
            View child = this.obtainView(0, this.mIsScrap);
            this.measureScrapChild(child, 0, widthMeasureSpec, heightSize);
            childWidth = child.getMeasuredWidth();
            childHeight = child.getMeasuredHeight();
            childState = combineMeasuredStates(childState, child.getMeasuredState());
            if (this.recycleOnMeasure() && this.mRecycler.shouldRecycleViewType(((AbsListView.LayoutParams) child.getLayoutParams()).viewType)) {
                this.mRecycler.addScrapView(child, 0);
            }
        }
        if (widthMode == 0) {
            widthSize = this.mListPadding.left + this.mListPadding.right + childWidth + this.getVerticalScrollbarWidth();
        } else {
            widthSize |= childState & 0xFF000000;
        }
        if (heightMode == 0) {
            heightSize = this.mListPadding.top + this.mListPadding.bottom + childHeight + this.getVerticalFadingEdgeLength() * 2;
        }
        if (heightMode == Integer.MIN_VALUE) {
            heightSize = this.measureHeightOfChildren(widthMeasureSpec, 0, -1, heightSize, -1);
        }
        this.setMeasuredDimension(widthSize, heightSize);
        this.mWidthMeasureSpec = widthMeasureSpec;
    }

    private void measureScrapChild(@NonNull View child, int position, int widthMeasureSpec, int heightHint) {
        AbsListView.LayoutParams p = (AbsListView.LayoutParams) child.getLayoutParams();
        if (p == null) {
            p = (AbsListView.LayoutParams) this.generateDefaultLayoutParams();
            child.setLayoutParams(p);
        }
        p.viewType = this.mAdapter.getItemViewType(position);
        p.isEnabled = this.mAdapter.isEnabled(position);
        p.forceAdd = true;
        int childWidthSpec = ViewGroup.getChildMeasureSpec(widthMeasureSpec, this.mListPadding.left + this.mListPadding.right, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, 1073741824);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(heightHint, 0);
        }
        child.measure(childWidthSpec, childHeightSpec);
        child.forceLayout();
    }

    @Internal
    protected boolean recycleOnMeasure() {
        return true;
    }

    final int measureHeightOfChildren(int widthMeasureSpec, int startPosition, int endPosition, int maxHeight, int disallowPartialChildPosition) {
        ListAdapter adapter = this.mAdapter;
        if (adapter == null) {
            return this.mListPadding.top + this.mListPadding.bottom;
        } else {
            int returnedHeight = this.mListPadding.top + this.mListPadding.bottom;
            int dividerHeight = this.mDividerHeight;
            int prevHeightWithoutPartialChild = 0;
            endPosition = endPosition == -1 ? adapter.getCount() - 1 : endPosition;
            AbsListView.RecycleBin recycleBin = this.mRecycler;
            boolean recyle = this.recycleOnMeasure();
            boolean[] isScrap = this.mIsScrap;
            for (int i = startPosition; i <= endPosition; i++) {
                View child = this.obtainView(i, isScrap);
                this.measureScrapChild(child, i, widthMeasureSpec, maxHeight);
                if (i > 0) {
                    returnedHeight += dividerHeight;
                }
                if (recyle && recycleBin.shouldRecycleViewType(((AbsListView.LayoutParams) child.getLayoutParams()).viewType)) {
                    recycleBin.addScrapView(child, -1);
                }
                returnedHeight += child.getMeasuredHeight();
                if (returnedHeight >= maxHeight) {
                    return disallowPartialChildPosition >= 0 && i > disallowPartialChildPosition && prevHeightWithoutPartialChild > 0 && returnedHeight != maxHeight ? prevHeightWithoutPartialChild : maxHeight;
                }
                if (disallowPartialChildPosition >= 0 && i >= disallowPartialChildPosition) {
                    prevHeightWithoutPartialChild = returnedHeight;
                }
            }
            return returnedHeight;
        }
    }

    @Override
    int findMotionRow(int y) {
        int childCount = this.getChildCount();
        if (childCount > 0) {
            if (!this.mStackFromBottom) {
                for (int i = 0; i < childCount; i++) {
                    View v = this.getChildAt(i);
                    if (y <= v.getBottom()) {
                        return this.mFirstPosition + i;
                    }
                }
            } else {
                for (int ix = childCount - 1; ix >= 0; ix--) {
                    View v = this.getChildAt(ix);
                    if (y >= v.getTop()) {
                        return this.mFirstPosition + ix;
                    }
                }
            }
        }
        return -1;
    }

    private View fillSpecific(int position, int top) {
        boolean tempIsSelected = position == this.mSelectedPosition;
        View temp = this.makeAndAddView(position, top, true, this.mListPadding.left, tempIsSelected);
        this.mFirstPosition = position;
        int dividerHeight = this.mDividerHeight;
        View above;
        View below;
        if (!this.mStackFromBottom) {
            above = this.fillUp(position - 1, temp.getTop() - dividerHeight);
            this.adjustViewsUpOrDown();
            below = this.fillDown(position + 1, temp.getBottom() + dividerHeight);
            int childCount = this.getChildCount();
            if (childCount > 0) {
                this.correctTooHigh(childCount);
            }
        } else {
            below = this.fillDown(position + 1, temp.getBottom() + dividerHeight);
            this.adjustViewsUpOrDown();
            above = this.fillUp(position - 1, temp.getTop() - dividerHeight);
            int childCount = this.getChildCount();
            if (childCount > 0) {
                this.correctTooLow(childCount);
            }
        }
        if (tempIsSelected) {
            return temp;
        } else {
            return above != null ? above : below;
        }
    }

    private void correctTooHigh(int childCount) {
        int lastPosition = this.mFirstPosition + childCount - 1;
        if (lastPosition == this.mItemCount - 1 && childCount > 0) {
            View lastChild = this.getChildAt(childCount - 1);
            int lastBottom = lastChild.getBottom();
            int end = this.getHeight() - this.mListPadding.bottom;
            int bottomOffset = end - lastBottom;
            View firstChild = this.getChildAt(0);
            int firstTop = firstChild.getTop();
            if (bottomOffset > 0 && (this.mFirstPosition > 0 || firstTop < this.mListPadding.top)) {
                if (this.mFirstPosition == 0) {
                    bottomOffset = Math.min(bottomOffset, this.mListPadding.top - firstTop);
                }
                this.offsetChildrenTopAndBottom(bottomOffset);
                if (this.mFirstPosition > 0) {
                    this.fillUp(this.mFirstPosition - 1, firstChild.getTop() - this.mDividerHeight);
                    this.adjustViewsUpOrDown();
                }
            }
        }
    }

    private void correctTooLow(int childCount) {
        if (this.mFirstPosition == 0 && childCount > 0) {
            View firstChild = this.getChildAt(0);
            int firstTop = firstChild.getTop();
            int start = this.mListPadding.top;
            int end = this.getHeight() - this.mListPadding.bottom;
            int topOffset = firstTop - start;
            View lastChild = this.getChildAt(childCount - 1);
            int lastBottom = lastChild.getBottom();
            int lastPosition = this.mFirstPosition + childCount - 1;
            if (topOffset > 0) {
                if (lastPosition >= this.mItemCount - 1 && lastBottom <= end) {
                    if (lastPosition == this.mItemCount - 1) {
                        this.adjustViewsUpOrDown();
                    }
                } else {
                    if (lastPosition == this.mItemCount - 1) {
                        topOffset = Math.min(topOffset, lastBottom - end);
                    }
                    this.offsetChildrenTopAndBottom(-topOffset);
                    if (lastPosition < this.mItemCount - 1) {
                        this.fillDown(lastPosition + 1, lastChild.getBottom() + this.mDividerHeight);
                        this.adjustViewsUpOrDown();
                    }
                }
            }
        }
    }

    @Override
    protected void layoutChildren() {
        boolean blockLayoutRequests = this.mBlockLayoutRequests;
        if (!blockLayoutRequests) {
            this.mBlockLayoutRequests = true;
            try {
                super.layoutChildren();
                this.invalidate();
                if (this.mAdapter == null) {
                    this.resetList();
                    this.invokeOnItemScrollListener();
                    return;
                }
                int childrenTop = this.mListPadding.top;
                int childrenBottom = this.getHeight() - this.mListPadding.bottom;
                int childCount = this.getChildCount();
                int delta = 0;
                View oldSel = null;
                View oldFirst = null;
                View newSel = null;
                newSel = switch(this.mLayoutMode) {
                    case 1, 3, 4, 5 ->
                        {
                        }
                    case 2 ->
                        {
                            int index = this.mNextSelectedPosition - this.mFirstPosition;
                            if (index >= 0 && index < childCount) {
                            }
                        }
                    default ->
                        {
                            label383: {
                            }
                        }
                };
                boolean dataChanged = this.mDataChanged;
                if (dataChanged) {
                    this.handleDataChanged();
                }
                if (this.mItemCount != 0) {
                    if (this.mItemCount != this.mAdapter.getCount()) {
                        throw new IllegalStateException("The content of the adapter has changed but ListView did not receive a notification. Make sure the content of your adapter is not modified from a background thread, but only from the UI thread. Make sure your adapter calls notifyDataSetChanged() when its content changes. [in ListView(" + this.getId() + ", " + this.getClass() + ") with Adapter(" + this.mAdapter.getClass() + ")]");
                    }
                    this.setSelectedPositionInt(this.mNextSelectedPosition);
                    View focusLayoutRestoreDirectChild = null;
                    View focusLayoutRestoreView = null;
                    View focusedChild = this.getFocusedChild();
                    if (focusedChild != null) {
                        if (!dataChanged || this.isDirectChildHeaderOrFooter(focusedChild) || focusedChild.hasTransientState() || this.mAdapterHasStableIds) {
                            focusLayoutRestoreDirectChild = focusedChild;
                            focusLayoutRestoreView = this.findFocus();
                            if (focusLayoutRestoreView != null) {
                                focusLayoutRestoreView.dispatchStartTemporaryDetach();
                            }
                        }
                        this.requestFocus();
                    }
                    int firstPosition = this.mFirstPosition;
                    AbsListView.RecycleBin recycleBin = this.mRecycler;
                    if (dataChanged) {
                        for (int i = 0; i < childCount; i++) {
                            recycleBin.addScrapView(this.getChildAt(i), firstPosition + i);
                        }
                    } else {
                        recycleBin.fillActiveViews(childCount, firstPosition);
                    }
                    this.detachAllViewsFromParent();
                    recycleBin.removeSkippedScrap();
                    View sel;
                    switch(this.mLayoutMode) {
                        case 1:
                            this.mFirstPosition = 0;
                            sel = this.fillFromTop(childrenTop);
                            this.adjustViewsUpOrDown();
                            break;
                        case 2:
                            if (newSel != null) {
                                sel = this.fillFromSelection(newSel.getTop(), childrenTop, childrenBottom);
                            } else {
                                sel = this.fillFromMiddle(childrenTop, childrenBottom);
                            }
                            break;
                        case 3:
                            sel = this.fillUp(this.mItemCount - 1, childrenBottom);
                            this.adjustViewsUpOrDown();
                            break;
                        case 4:
                            int selectedPosition = this.reconcileSelectedPosition();
                            sel = this.fillSpecific(selectedPosition, this.mSpecificTop);
                            if (sel == null && this.mFocusSelector != null) {
                                Runnable focusRunnable = this.mFocusSelector.setupFocusIfValid(selectedPosition);
                                if (focusRunnable != null) {
                                    this.post(focusRunnable);
                                }
                            }
                            break;
                        case 5:
                            sel = this.fillSpecific(this.mSyncPosition, this.mSpecificTop);
                            break;
                        case 6:
                            sel = this.moveSelection(oldSel, newSel, delta, childrenTop, childrenBottom);
                            break;
                        default:
                            if (childCount == 0) {
                                if (!this.mStackFromBottom) {
                                    int position = this.lookForSelectablePosition(0, true);
                                    this.setSelectedPositionInt(position);
                                    sel = this.fillFromTop(childrenTop);
                                } else {
                                    int position = this.lookForSelectablePosition(this.mItemCount - 1, false);
                                    this.setSelectedPositionInt(position);
                                    sel = this.fillUp(this.mItemCount - 1, childrenBottom);
                                }
                            } else if (this.mSelectedPosition >= 0 && this.mSelectedPosition < this.mItemCount) {
                                sel = this.fillSpecific(this.mSelectedPosition, oldSel == null ? childrenTop : oldSel.getTop());
                            } else if (this.mFirstPosition < this.mItemCount) {
                                sel = this.fillSpecific(this.mFirstPosition, oldFirst == null ? childrenTop : oldFirst.getTop());
                            } else {
                                sel = this.fillSpecific(0, childrenTop);
                            }
                    }
                    recycleBin.scrapActiveViews();
                    this.removeUnusedFixedViews(this.mHeaderViewInfos);
                    this.removeUnusedFixedViews(this.mFooterViewInfos);
                    if (sel != null) {
                        if (this.mItemsCanFocus && this.hasFocus() && !sel.hasFocus()) {
                            boolean focusWasTaken = sel == focusLayoutRestoreDirectChild && focusLayoutRestoreView != null && focusLayoutRestoreView.requestFocus() || sel.requestFocus();
                            if (!focusWasTaken) {
                                View focused = this.getFocusedChild();
                                if (focused != null) {
                                    focused.clearFocus();
                                }
                                this.positionSelector(-1, sel);
                            } else {
                                sel.setSelected(false);
                                this.mSelectorRect.setEmpty();
                            }
                        } else {
                            this.positionSelector(-1, sel);
                        }
                        this.mSelectedTop = sel.getTop();
                    } else {
                        boolean inTouchMode = this.mTouchMode == 1 || this.mTouchMode == 2;
                        if (inTouchMode) {
                            View child = this.getChildAt(this.mMotionPosition - this.mFirstPosition);
                            if (child != null) {
                                this.positionSelector(this.mMotionPosition, child);
                            }
                        } else if (this.mSelectorPosition != -1) {
                            View child = this.getChildAt(this.mSelectorPosition - this.mFirstPosition);
                            if (child != null) {
                                this.positionSelector(this.mSelectorPosition, child);
                            }
                        } else {
                            this.mSelectedTop = 0;
                            this.mSelectorRect.setEmpty();
                        }
                        if (this.hasFocus() && focusLayoutRestoreView != null) {
                            focusLayoutRestoreView.requestFocus();
                        }
                    }
                    if (focusLayoutRestoreView != null && focusLayoutRestoreView.isAttachedToWindow()) {
                        focusLayoutRestoreView.dispatchFinishTemporaryDetach();
                    }
                    this.mLayoutMode = 0;
                    this.mDataChanged = false;
                    if (this.mPositionScrollAfterLayout != null) {
                        this.post(this.mPositionScrollAfterLayout);
                        this.mPositionScrollAfterLayout = null;
                    }
                    this.mNeedSync = false;
                    this.setNextSelectedPositionInt(this.mSelectedPosition);
                    this.updateScrollIndicators();
                    if (this.mItemCount > 0) {
                        this.checkSelectionChanged();
                    }
                    this.invokeOnItemScrollListener();
                    return;
                }
                this.resetList();
                this.invokeOnItemScrollListener();
            } finally {
                if (this.mFocusSelector != null) {
                    this.mFocusSelector.onLayoutComplete();
                }
                this.mBlockLayoutRequests = false;
            }
        }
    }

    @Override
    boolean trackMotionScroll(int deltaY, int incrementalDeltaY) {
        boolean result = super.trackMotionScroll(deltaY, incrementalDeltaY);
        this.removeUnusedFixedViews(this.mHeaderViewInfos);
        this.removeUnusedFixedViews(this.mFooterViewInfos);
        return result;
    }

    private void removeUnusedFixedViews(@Nullable List<ListView.FixedViewInfo> infoList) {
        if (infoList != null) {
            for (int i = infoList.size() - 1; i >= 0; i--) {
                ListView.FixedViewInfo fixedViewInfo = (ListView.FixedViewInfo) infoList.get(i);
                View view = fixedViewInfo.view;
                AbsListView.LayoutParams lp = (AbsListView.LayoutParams) view.getLayoutParams();
                if (view.getParent() == null && lp != null && lp.recycledHeaderFooter) {
                    this.removeDetachedView(view, false);
                    lp.recycledHeaderFooter = false;
                }
            }
        }
    }

    private boolean isDirectChildHeaderOrFooter(View child) {
        ArrayList<ListView.FixedViewInfo> headers = this.mHeaderViewInfos;
        int numHeaders = headers.size();
        for (int i = 0; i < numHeaders; i++) {
            if (child == ((ListView.FixedViewInfo) headers.get(i)).view) {
                return true;
            }
        }
        ArrayList<ListView.FixedViewInfo> footers = this.mFooterViewInfos;
        int numFooters = footers.size();
        for (int ix = 0; ix < numFooters; ix++) {
            if (child == ((ListView.FixedViewInfo) footers.get(ix)).view) {
                return true;
            }
        }
        return false;
    }

    private View makeAndAddView(int position, int y, boolean flow, int childrenLeft, boolean selected) {
        if (!this.mDataChanged) {
            View activeView = this.mRecycler.getActiveView(position);
            if (activeView != null) {
                this.setupChild(activeView, position, y, flow, childrenLeft, selected, true);
                return activeView;
            }
        }
        View child = this.obtainView(position, this.mIsScrap);
        this.setupChild(child, position, y, flow, childrenLeft, selected, this.mIsScrap[0]);
        return child;
    }

    private void setupChild(@NonNull View child, int position, int y, boolean flowDown, int childrenLeft, boolean selected, boolean isAttachedToWindow) {
        boolean isSelected = selected && this.shouldShowSelector();
        boolean updateChildSelected = isSelected != child.isSelected();
        int mode = this.mTouchMode;
        boolean isPressed = mode > 0 && mode < 3 && this.mMotionPosition == position;
        boolean updateChildPressed = isPressed != child.isPressed();
        boolean needToMeasure = !isAttachedToWindow || updateChildSelected || child.isLayoutRequested();
        AbsListView.LayoutParams p = (AbsListView.LayoutParams) child.getLayoutParams();
        if (p == null) {
            p = (AbsListView.LayoutParams) this.generateDefaultLayoutParams();
        }
        p.viewType = this.mAdapter.getItemViewType(position);
        p.isEnabled = this.mAdapter.isEnabled(position);
        if (updateChildSelected) {
            child.setSelected(isSelected);
        }
        if (updateChildPressed) {
            child.setPressed(isPressed);
        }
        if (this.mChoiceMode != 0 && this.mCheckStates != null) {
            if (child instanceof Checkable) {
                ((Checkable) child).setChecked(this.mCheckStates.get(position));
            } else {
                child.setActivated(this.mCheckStates.get(position));
            }
        }
        if (isAttachedToWindow && !p.forceAdd || p.recycledHeaderFooter && p.viewType == -2) {
            this.attachViewToParent(child, flowDown ? -1 : 0, p);
            if (isAttachedToWindow && ((AbsListView.LayoutParams) child.getLayoutParams()).scrappedFromPosition != position) {
                child.jumpDrawablesToCurrentState();
            }
        } else {
            p.forceAdd = false;
            if (p.viewType == -2) {
                p.recycledHeaderFooter = true;
            }
            this.addViewInLayout(child, flowDown ? -1 : 0, p, true);
            child.resolveRtlPropertiesIfNeeded();
        }
        if (needToMeasure) {
            int childWidthSpec = ViewGroup.getChildMeasureSpec(this.mWidthMeasureSpec, this.mListPadding.left + this.mListPadding.right, p.width);
            int lpHeight = p.height;
            int childHeightSpec;
            if (lpHeight > 0) {
                childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, 1073741824);
            } else {
                childHeightSpec = MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 0);
            }
            child.measure(childWidthSpec, childHeightSpec);
        } else {
            this.cleanupLayoutState(child);
        }
        int w = child.getMeasuredWidth();
        int h = child.getMeasuredHeight();
        int childTop = flowDown ? y : y - h;
        if (needToMeasure) {
            int childRight = childrenLeft + w;
            int childBottom = childTop + h;
            child.layout(childrenLeft, childTop, childRight, childBottom);
        } else {
            child.offsetLeftAndRight(childrenLeft - child.getLeft());
            child.offsetTopAndBottom(childTop - child.getTop());
        }
    }

    @Override
    public void setSelection(int position) {
        this.setSelectionFromTop(position, 0);
    }

    @Override
    void setSelectionInt(int position) {
        this.setNextSelectedPositionInt(position);
        boolean awakeScrollbars = false;
        int selectedPosition = this.mSelectedPosition;
        if (selectedPosition >= 0) {
            if (position == selectedPosition - 1) {
                awakeScrollbars = true;
            } else if (position == selectedPosition + 1) {
                awakeScrollbars = true;
            }
        }
        if (this.mPositionScroller != null) {
            this.mPositionScroller.stop();
        }
        this.layoutChildren();
        if (awakeScrollbars) {
            this.awakenScrollBars();
        }
    }

    @Override
    int lookForSelectablePosition(int position, boolean lookDown) {
        ListAdapter adapter = this.mAdapter;
        if (adapter != null && !this.isInTouchMode()) {
            int count = adapter.getCount();
            if (!this.mAreAllItemsSelectable) {
                if (lookDown) {
                    position = Math.max(0, position);
                    while (position < count && !adapter.isEnabled(position)) {
                        position++;
                    }
                } else {
                    position = Math.min(position, count - 1);
                    while (position >= 0 && !adapter.isEnabled(position)) {
                        position--;
                    }
                }
            }
            return position >= 0 && position < count ? position : -1;
        } else {
            return -1;
        }
    }

    int lookForSelectablePositionAfter(int current, int position, boolean lookDown) {
        ListAdapter adapter = this.mAdapter;
        if (adapter != null && !this.isInTouchMode()) {
            int after = this.lookForSelectablePosition(position, lookDown);
            if (after != -1) {
                return after;
            } else {
                int count = adapter.getCount();
                current = MathUtil.clamp(current, -1, count - 1);
                if (lookDown) {
                    position = Math.min(position - 1, count - 1);
                    while (position > current && !adapter.isEnabled(position)) {
                        position--;
                    }
                    if (position <= current) {
                        return -1;
                    }
                } else {
                    position = Math.max(0, position + 1);
                    while (position < current && !adapter.isEnabled(position)) {
                        position++;
                    }
                    if (position >= current) {
                        return -1;
                    }
                }
                return position;
            }
        } else {
            return -1;
        }
    }

    public void setSelectionAfterHeaderView() {
        int count = this.getHeaderViewsCount();
        if (count > 0) {
            this.mNextSelectedPosition = 0;
        } else {
            if (this.mAdapter != null) {
                this.setSelection(count);
            } else {
                this.mNextSelectedPosition = count;
                this.mLayoutMode = 2;
            }
        }
    }

    @Override
    public boolean dispatchKeyEvent(@NonNull KeyEvent event) {
        boolean handled = super.dispatchKeyEvent(event);
        if (!handled) {
            View focused = this.getFocusedChild();
            if (focused != null && event.getAction() == 0) {
                handled = this.onKeyDown(event.getKeyCode(), event);
            }
        }
        return handled;
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        return this.commonKey(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, @NonNull KeyEvent event) {
        return this.commonKey(keyCode, event);
    }

    private boolean commonKey(int keyCode, KeyEvent event) {
        if (this.mAdapter != null && this.isAttachedToWindow()) {
            if (this.mDataChanged) {
                this.layoutChildren();
            }
            boolean handled = false;
            int action = event.getAction();
            if ((keyCode == 257 || keyCode == 335) && event.hasNoModifiers() && action != 1) {
                handled = this.resurrectSelectionIfNeeded();
                if (!handled && event.getRepeatCount() == 0 && this.getChildCount() > 0) {
                    this.keyPressed();
                    handled = true;
                }
            }
            if (!handled && action != 1) {
                switch(keyCode) {
                    case 258:
                        if (event.hasNoModifiers()) {
                            handled = this.resurrectSelectionIfNeeded() || this.arrowScroll(130);
                        } else if (event.hasModifiers(1)) {
                            handled = this.resurrectSelectionIfNeeded() || this.arrowScroll(33);
                        }
                    case 259:
                    case 260:
                    case 261:
                    default:
                        break;
                    case 262:
                        if (event.hasNoModifiers()) {
                            handled = this.handleHorizontalFocusWithinListItem(66);
                        }
                        break;
                    case 263:
                        if (event.hasNoModifiers()) {
                            handled = this.handleHorizontalFocusWithinListItem(17);
                        }
                        break;
                    case 264:
                        if (event.hasNoModifiers()) {
                            handled = this.resurrectSelectionIfNeeded();
                            if (!handled && this.arrowScroll(130)) {
                                handled = true;
                            }
                        } else if (event.hasModifiers(4)) {
                            handled = this.resurrectSelectionIfNeeded() || this.fullScroll(130);
                        }
                        break;
                    case 265:
                        if (event.hasNoModifiers()) {
                            handled = this.resurrectSelectionIfNeeded();
                            if (!handled && this.arrowScroll(33)) {
                                handled = true;
                            }
                        } else if (event.hasModifiers(4)) {
                            handled = this.resurrectSelectionIfNeeded() || this.fullScroll(33);
                        }
                        break;
                    case 266:
                        if (event.hasNoModifiers()) {
                            handled = this.resurrectSelectionIfNeeded() || this.pageScroll(33);
                        } else if (event.hasModifiers(4)) {
                            handled = this.resurrectSelectionIfNeeded() || this.fullScroll(33);
                        }
                        break;
                    case 267:
                        if (event.hasNoModifiers()) {
                            handled = this.resurrectSelectionIfNeeded() || this.pageScroll(130);
                        } else if (event.hasModifiers(4)) {
                            handled = this.resurrectSelectionIfNeeded() || this.fullScroll(130);
                        }
                        break;
                    case 268:
                        if (event.hasNoModifiers()) {
                            handled = this.resurrectSelectionIfNeeded() || this.fullScroll(33);
                        }
                        break;
                    case 269:
                        if (event.hasNoModifiers()) {
                            handled = this.resurrectSelectionIfNeeded() || this.fullScroll(130);
                        }
                }
            }
            if (handled) {
                return true;
            } else {
                switch(action) {
                    case 0:
                        return super.onKeyDown(keyCode, event);
                    case 1:
                        return super.onKeyUp(keyCode, event);
                    default:
                        return false;
                }
            }
        } else {
            return false;
        }
    }

    boolean pageScroll(int direction) {
        int nextPage;
        boolean down;
        if (direction == 33) {
            nextPage = Math.max(0, this.mSelectedPosition - this.getChildCount() - 1);
            down = false;
        } else {
            if (direction != 130) {
                return false;
            }
            nextPage = Math.min(this.mItemCount - 1, this.mSelectedPosition + this.getChildCount() - 1);
            down = true;
        }
        if (nextPage >= 0) {
            int position = this.lookForSelectablePositionAfter(this.mSelectedPosition, nextPage, down);
            if (position >= 0) {
                this.mLayoutMode = 4;
                this.mSpecificTop = this.mPaddingTop + this.getVerticalFadingEdgeLength();
                if (down && position > this.mItemCount - this.getChildCount()) {
                    this.mLayoutMode = 3;
                }
                if (!down && position < this.getChildCount()) {
                    this.mLayoutMode = 1;
                }
                this.setSelectionInt(position);
                this.invokeOnItemScrollListener();
                if (!this.awakenScrollBars()) {
                    this.invalidate();
                }
                return true;
            }
        }
        return false;
    }

    boolean fullScroll(int direction) {
        boolean moved = false;
        if (direction == 33) {
            if (this.mSelectedPosition != 0) {
                int position = this.lookForSelectablePositionAfter(this.mSelectedPosition, 0, true);
                if (position >= 0) {
                    this.mLayoutMode = 1;
                    this.setSelectionInt(position);
                    this.invokeOnItemScrollListener();
                }
                moved = true;
            }
        } else if (direction == 130) {
            int lastItem = this.mItemCount - 1;
            if (this.mSelectedPosition < lastItem) {
                int position = this.lookForSelectablePositionAfter(this.mSelectedPosition, lastItem, false);
                if (position >= 0) {
                    this.mLayoutMode = 3;
                    this.setSelectionInt(position);
                    this.invokeOnItemScrollListener();
                }
                moved = true;
            }
        }
        if (moved && !this.awakenScrollBars()) {
            this.awakenScrollBars();
            this.invalidate();
        }
        return moved;
    }

    private boolean handleHorizontalFocusWithinListItem(int direction) {
        if (direction != 17 && direction != 66) {
            throw new IllegalArgumentException("direction must be one of {View.FOCUS_LEFT, View.FOCUS_RIGHT}");
        } else {
            int numChildren = this.getChildCount();
            if (this.mItemsCanFocus && numChildren > 0 && this.mSelectedPosition != -1) {
                View selectedView = this.getSelectedView();
                if (selectedView != null && selectedView.hasFocus() && selectedView instanceof ViewGroup) {
                    View currentFocus = selectedView.findFocus();
                    View nextFocus = FocusFinder.getInstance().findNextFocus((ViewGroup) selectedView, currentFocus, direction);
                    if (nextFocus != null) {
                        Rect focusedRect = this.mTempRect;
                        if (currentFocus != null) {
                            currentFocus.getFocusedRect(focusedRect);
                            this.offsetDescendantRectToMyCoords(currentFocus, focusedRect);
                            this.offsetRectIntoDescendantCoords(nextFocus, focusedRect);
                        } else {
                            focusedRect = null;
                        }
                        if (nextFocus.requestFocus(direction, focusedRect)) {
                            return true;
                        }
                    }
                    View globalNextFocus = FocusFinder.getInstance().findNextFocus((ViewGroup) this.getRootView(), currentFocus, direction);
                    if (globalNextFocus != null) {
                        return this.isViewAncestorOf(globalNextFocus, this);
                    }
                }
            }
            return false;
        }
    }

    boolean arrowScroll(int direction) {
        boolean var3;
        try {
            this.mInLayout = true;
            boolean handled = this.arrowScrollImpl(direction);
            if (handled) {
                this.playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
            }
            var3 = handled;
        } finally {
            this.mInLayout = false;
        }
        return var3;
    }

    private int nextSelectedPositionForDirection(View selectedView, int selectedPos, int direction) {
        int nextSelected;
        if (direction == 130) {
            int listBottom = this.getHeight() - this.mListPadding.bottom;
            if (selectedView == null || selectedView.getBottom() > listBottom) {
                return -1;
            }
            nextSelected = selectedPos != -1 && selectedPos >= this.mFirstPosition ? selectedPos + 1 : this.mFirstPosition;
        } else {
            int listTop = this.mListPadding.top;
            if (selectedView == null || selectedView.getTop() < listTop) {
                return -1;
            }
            int lastPos = this.mFirstPosition + this.getChildCount() - 1;
            nextSelected = selectedPos != -1 && selectedPos <= lastPos ? selectedPos - 1 : lastPos;
        }
        return nextSelected >= 0 && nextSelected < this.mAdapter.getCount() ? this.lookForSelectablePosition(nextSelected, direction == 130) : -1;
    }

    private boolean arrowScrollImpl(int direction) {
        if (this.getChildCount() <= 0) {
            return false;
        } else {
            View selectedView = this.getSelectedView();
            int selectedPos = this.mSelectedPosition;
            int nextSelectedPosition = this.nextSelectedPositionForDirection(selectedView, selectedPos, direction);
            int amountToScroll = this.amountToScroll(direction, nextSelectedPosition);
            ListView.ArrowScrollFocusResult focusResult = this.mItemsCanFocus ? this.arrowScrollFocused(direction) : null;
            if (focusResult != null) {
                nextSelectedPosition = focusResult.getSelectedPosition();
                amountToScroll = focusResult.getAmountToScroll();
            }
            boolean needToRedraw = focusResult != null;
            if (nextSelectedPosition != -1) {
                this.handleNewSelectionChange(selectedView, direction, nextSelectedPosition, focusResult != null);
                this.setSelectedPositionInt(nextSelectedPosition);
                this.setNextSelectedPositionInt(nextSelectedPosition);
                selectedView = this.getSelectedView();
                selectedPos = nextSelectedPosition;
                if (this.mItemsCanFocus && focusResult == null) {
                    View focused = this.getFocusedChild();
                    if (focused != null) {
                        focused.clearFocus();
                    }
                }
                needToRedraw = true;
                this.checkSelectionChanged();
            }
            if (amountToScroll > 0) {
                this.scrollListItemsBy(direction == 33 ? amountToScroll : -amountToScroll);
                needToRedraw = true;
            }
            if (this.mItemsCanFocus && focusResult == null && selectedView != null && selectedView.hasFocus()) {
                View focused = selectedView.findFocus();
                if (focused != null && (!this.isViewAncestorOf(focused, this) || this.distanceToView(focused) > 0)) {
                    focused.clearFocus();
                }
            }
            if (nextSelectedPosition == -1 && selectedView != null && !this.isViewAncestorOf(selectedView, this)) {
                selectedView = null;
                this.hideSelector();
                this.mResurrectToPosition = -1;
            }
            if (needToRedraw) {
                if (selectedView != null) {
                    this.positionSelectorLikeFocus(selectedPos, selectedView);
                    this.mSelectedTop = selectedView.getTop();
                }
                if (!this.awakenScrollBars()) {
                    this.invalidate();
                }
                this.invokeOnItemScrollListener();
                return true;
            } else {
                return false;
            }
        }
    }

    private void handleNewSelectionChange(View selectedView, int direction, int newSelectedPosition, boolean newFocusAssigned) {
        if (newSelectedPosition == -1) {
            throw new IllegalArgumentException("newSelectedPosition needs to be valid");
        } else {
            boolean topSelected = false;
            int selectedIndex = this.mSelectedPosition - this.mFirstPosition;
            int nextSelectedIndex = newSelectedPosition - this.mFirstPosition;
            View topView;
            View bottomView;
            int topViewIndex;
            int bottomViewIndex;
            if (direction == 33) {
                topViewIndex = nextSelectedIndex;
                bottomViewIndex = selectedIndex;
                topView = this.getChildAt(nextSelectedIndex);
                bottomView = selectedView;
                topSelected = true;
            } else {
                topViewIndex = selectedIndex;
                bottomViewIndex = nextSelectedIndex;
                topView = selectedView;
                bottomView = this.getChildAt(nextSelectedIndex);
            }
            int numChildren = this.getChildCount();
            if (topView != null) {
                topView.setSelected(!newFocusAssigned && topSelected);
                this.measureAndAdjustDown(topView, topViewIndex, numChildren);
            }
            if (bottomView != null) {
                bottomView.setSelected(!newFocusAssigned && !topSelected);
                this.measureAndAdjustDown(bottomView, bottomViewIndex, numChildren);
            }
        }
    }

    private void measureAndAdjustDown(@NonNull View child, int childIndex, int numChildren) {
        int oldHeight = child.getHeight();
        this.measureItem(child);
        if (child.getMeasuredHeight() != oldHeight) {
            this.relayoutMeasuredItem(child);
            int heightDelta = child.getMeasuredHeight() - oldHeight;
            for (int i = childIndex + 1; i < numChildren; i++) {
                this.getChildAt(i).offsetTopAndBottom(heightDelta);
            }
        }
    }

    private void measureItem(@NonNull View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(-1, -2);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(this.mWidthMeasureSpec, this.mListPadding.left + this.mListPadding.right, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, 1073741824);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 0);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    private void relayoutMeasuredItem(View child) {
        int w = child.getMeasuredWidth();
        int h = child.getMeasuredHeight();
        int childLeft = this.mListPadding.left;
        int childRight = childLeft + w;
        int childTop = child.getTop();
        int childBottom = childTop + h;
        child.layout(childLeft, childTop, childRight, childBottom);
    }

    private int getArrowScrollPreviewLength() {
        return Math.max(2, this.getVerticalFadingEdgeLength());
    }

    private int amountToScroll(int direction, int nextSelectedPosition) {
        int listBottom = this.getHeight() - this.mListPadding.bottom;
        int listTop = this.mListPadding.top;
        int numChildren = this.getChildCount();
        if (direction == 130) {
            int indexToMakeVisible = numChildren - 1;
            if (nextSelectedPosition != -1) {
                indexToMakeVisible = nextSelectedPosition - this.mFirstPosition;
            }
            while (numChildren <= indexToMakeVisible) {
                this.addViewBelow(this.getChildAt(numChildren - 1), this.mFirstPosition + numChildren - 1);
                numChildren++;
            }
            int positionToMakeVisible = this.mFirstPosition + indexToMakeVisible;
            View viewToMakeVisible = this.getChildAt(indexToMakeVisible);
            int goalBottom = listBottom;
            if (positionToMakeVisible < this.mItemCount - 1) {
                goalBottom = listBottom - this.getArrowScrollPreviewLength();
            }
            if (viewToMakeVisible.getBottom() <= goalBottom) {
                return 0;
            } else if (nextSelectedPosition != -1 && goalBottom - viewToMakeVisible.getTop() >= this.getMaxScrollAmount()) {
                return 0;
            } else {
                int amountToScroll = viewToMakeVisible.getBottom() - goalBottom;
                if (this.mFirstPosition + numChildren == this.mItemCount) {
                    int max = this.getChildAt(numChildren - 1).getBottom() - listBottom;
                    amountToScroll = Math.min(amountToScroll, max);
                }
                return Math.min(amountToScroll, this.getMaxScrollAmount());
            }
        } else {
            int indexToMakeVisiblex = 0;
            if (nextSelectedPosition != -1) {
                indexToMakeVisiblex = nextSelectedPosition - this.mFirstPosition;
            }
            while (indexToMakeVisiblex < 0) {
                this.addViewAbove(this.getChildAt(0), this.mFirstPosition);
                this.mFirstPosition--;
                indexToMakeVisiblex = nextSelectedPosition - this.mFirstPosition;
            }
            int positionToMakeVisiblex = this.mFirstPosition + indexToMakeVisiblex;
            View viewToMakeVisiblex = this.getChildAt(indexToMakeVisiblex);
            int goalTop = listTop;
            if (positionToMakeVisiblex > 0) {
                goalTop = listTop + this.getArrowScrollPreviewLength();
            }
            if (viewToMakeVisiblex.getTop() >= goalTop) {
                return 0;
            } else if (nextSelectedPosition != -1 && viewToMakeVisiblex.getBottom() - goalTop >= this.getMaxScrollAmount()) {
                return 0;
            } else {
                int amountToScroll = goalTop - viewToMakeVisiblex.getTop();
                if (this.mFirstPosition == 0) {
                    int max = listTop - this.getChildAt(0).getTop();
                    amountToScroll = Math.min(amountToScroll, max);
                }
                return Math.min(amountToScroll, this.getMaxScrollAmount());
            }
        }
    }

    private int lookForSelectablePositionOnScreen(int direction) {
        int firstPosition = this.mFirstPosition;
        if (direction == 130) {
            int startPos = this.mSelectedPosition != -1 ? this.mSelectedPosition + 1 : firstPosition;
            if (startPos >= this.mAdapter.getCount()) {
                return -1;
            }
            if (startPos < firstPosition) {
                startPos = firstPosition;
            }
            int lastVisiblePos = this.getLastVisiblePosition();
            ListAdapter adapter = this.getAdapter();
            for (int pos = startPos; pos <= lastVisiblePos; pos++) {
                if (adapter.isEnabled(pos) && this.getChildAt(pos - firstPosition).getVisibility() == 0) {
                    return pos;
                }
            }
        } else {
            int last = firstPosition + this.getChildCount() - 1;
            int startPosx = this.mSelectedPosition != -1 ? this.mSelectedPosition - 1 : firstPosition + this.getChildCount() - 1;
            if (startPosx < 0 || startPosx >= this.mAdapter.getCount()) {
                return -1;
            }
            if (startPosx > last) {
                startPosx = last;
            }
            ListAdapter adapter = this.getAdapter();
            for (int posx = startPosx; posx >= firstPosition; posx--) {
                if (adapter.isEnabled(posx) && this.getChildAt(posx - firstPosition).getVisibility() == 0) {
                    return posx;
                }
            }
        }
        return -1;
    }

    @Nullable
    private ListView.ArrowScrollFocusResult arrowScrollFocused(int direction) {
        View selectedView = this.getSelectedView();
        View newFocus;
        if (selectedView != null && selectedView.hasFocus()) {
            View oldFocus = selectedView.findFocus();
            newFocus = FocusFinder.getInstance().findNextFocus(this, oldFocus, direction);
        } else {
            if (direction == 130) {
                boolean topFadingEdgeShowing = this.mFirstPosition > 0;
                int listTop = this.mListPadding.top + (topFadingEdgeShowing ? this.getArrowScrollPreviewLength() : 0);
                int ySearchPoint = selectedView != null && selectedView.getTop() > listTop ? selectedView.getTop() : listTop;
                this.mTempRect.set(0, ySearchPoint, 0, ySearchPoint);
            } else {
                boolean bottomFadingEdgeShowing = this.mFirstPosition + this.getChildCount() - 1 < this.mItemCount;
                int listBottom = this.getHeight() - this.mListPadding.bottom - (bottomFadingEdgeShowing ? this.getArrowScrollPreviewLength() : 0);
                int ySearchPoint = selectedView != null && selectedView.getBottom() < listBottom ? selectedView.getBottom() : listBottom;
                this.mTempRect.set(0, ySearchPoint, 0, ySearchPoint);
            }
            newFocus = FocusFinder.getInstance().findNextFocusFromRect(this, this.mTempRect, direction);
        }
        if (newFocus != null) {
            int positionOfNewFocus = this.positionOfNewFocus(newFocus);
            if (this.mSelectedPosition != -1 && positionOfNewFocus != this.mSelectedPosition) {
                int selectablePosition = this.lookForSelectablePositionOnScreen(direction);
                if (selectablePosition != -1 && (direction == 130 && selectablePosition < positionOfNewFocus || direction == 33 && selectablePosition > positionOfNewFocus)) {
                    return null;
                }
            }
            int focusScroll = this.amountToScrollToNewFocus(direction, newFocus, positionOfNewFocus);
            int maxScrollAmount = this.getMaxScrollAmount();
            if (focusScroll < maxScrollAmount) {
                newFocus.requestFocus(direction);
                this.mArrowScrollFocusResult.populate(positionOfNewFocus, focusScroll);
                return this.mArrowScrollFocusResult;
            }
            if (this.distanceToView(newFocus) < maxScrollAmount) {
                newFocus.requestFocus(direction);
                this.mArrowScrollFocusResult.populate(positionOfNewFocus, maxScrollAmount);
                return this.mArrowScrollFocusResult;
            }
        }
        return null;
    }

    private int positionOfNewFocus(View newFocus) {
        int numChildren = this.getChildCount();
        for (int i = 0; i < numChildren; i++) {
            View child = this.getChildAt(i);
            if (this.isViewAncestorOf(newFocus, child)) {
                return this.mFirstPosition + i;
            }
        }
        throw new IllegalArgumentException("newFocus is not a child of any of the children of the list!");
    }

    private boolean isViewAncestorOf(View child, View parent) {
        if (child == parent) {
            return true;
        } else {
            ViewParent theParent = child.getParent();
            return theParent instanceof ViewGroup && this.isViewAncestorOf((View) theParent, parent);
        }
    }

    private int amountToScrollToNewFocus(int direction, @NonNull View newFocus, int positionOfNewFocus) {
        int amountToScroll = 0;
        newFocus.getDrawingRect(this.mTempRect);
        this.offsetDescendantRectToMyCoords(newFocus, this.mTempRect);
        if (direction == 33) {
            if (this.mTempRect.top < this.mListPadding.top) {
                amountToScroll = this.mListPadding.top - this.mTempRect.top;
                if (positionOfNewFocus > 0) {
                    amountToScroll += this.getArrowScrollPreviewLength();
                }
            }
        } else {
            int listBottom = this.getHeight() - this.mListPadding.bottom;
            if (this.mTempRect.bottom > listBottom) {
                amountToScroll = this.mTempRect.bottom - listBottom;
                if (positionOfNewFocus < this.mItemCount - 1) {
                    amountToScroll += this.getArrowScrollPreviewLength();
                }
            }
        }
        return amountToScroll;
    }

    private int distanceToView(@NonNull View descendant) {
        int distance = 0;
        descendant.getDrawingRect(this.mTempRect);
        this.offsetDescendantRectToMyCoords(descendant, this.mTempRect);
        int listBottom = this.getHeight() - this.mListPadding.bottom;
        if (this.mTempRect.bottom < this.mListPadding.top) {
            distance = this.mListPadding.top - this.mTempRect.bottom;
        } else if (this.mTempRect.top > listBottom) {
            distance = this.mTempRect.top - listBottom;
        }
        return distance;
    }

    private void scrollListItemsBy(int amount) {
        int oldX = this.getScrollX();
        int oldY = this.getScrollY();
        this.offsetChildrenTopAndBottom(amount);
        int listBottom = this.getHeight() - this.mListPadding.bottom;
        int listTop = this.mListPadding.top;
        AbsListView.RecycleBin recycleBin = this.mRecycler;
        if (amount < 0) {
            int numChildren = this.getChildCount();
            View last;
            for (last = this.getChildAt(numChildren - 1); last.getBottom() < listBottom; numChildren++) {
                int lastVisiblePosition = this.mFirstPosition + numChildren - 1;
                if (lastVisiblePosition >= this.mItemCount - 1) {
                    break;
                }
                last = this.addViewBelow(last, lastVisiblePosition);
            }
            if (last.getBottom() < listBottom) {
                this.offsetChildrenTopAndBottom(listBottom - last.getBottom());
            }
            for (View first = this.getChildAt(0); first.getBottom() < listTop; this.mFirstPosition++) {
                AbsListView.LayoutParams layoutParams = (AbsListView.LayoutParams) first.getLayoutParams();
                if (recycleBin.shouldRecycleViewType(layoutParams.viewType)) {
                    recycleBin.addScrapView(first, this.mFirstPosition);
                }
                this.detachViewFromParent(first);
                first = this.getChildAt(0);
            }
        } else {
            View first;
            for (first = this.getChildAt(0); first.getTop() > listTop && this.mFirstPosition > 0; this.mFirstPosition--) {
                first = this.addViewAbove(first, this.mFirstPosition);
            }
            if (first.getTop() > listTop) {
                this.offsetChildrenTopAndBottom(listTop - first.getTop());
            }
            int lastIndex = this.getChildCount() - 1;
            for (View lastx = this.getChildAt(lastIndex); lastx.getTop() > listBottom; lastx = this.getChildAt(--lastIndex)) {
                AbsListView.LayoutParams layoutParams = (AbsListView.LayoutParams) lastx.getLayoutParams();
                if (recycleBin.shouldRecycleViewType(layoutParams.viewType)) {
                    recycleBin.addScrapView(lastx, this.mFirstPosition + lastIndex);
                }
                this.detachViewFromParent(lastx);
            }
        }
        recycleBin.fullyDetachScrapViews();
        this.removeUnusedFixedViews(this.mHeaderViewInfos);
        this.removeUnusedFixedViews(this.mFooterViewInfos);
        this.onScrollChanged(this.getScrollX(), this.getScrollY(), oldX, oldY);
    }

    private View addViewAbove(@NonNull View theView, int position) {
        int abovePosition = position - 1;
        View view = this.obtainView(abovePosition, this.mIsScrap);
        int edgeOfNewChild = theView.getTop() - this.mDividerHeight;
        this.setupChild(view, abovePosition, edgeOfNewChild, false, this.mListPadding.left, false, this.mIsScrap[0]);
        return view;
    }

    private View addViewBelow(@NonNull View theView, int position) {
        int belowPosition = position + 1;
        View view = this.obtainView(belowPosition, this.mIsScrap);
        int edgeOfNewChild = theView.getBottom() + this.mDividerHeight;
        this.setupChild(view, belowPosition, edgeOfNewChild, true, this.mListPadding.left, false, this.mIsScrap[0]);
        return view;
    }

    public void setItemsCanFocus(boolean itemsCanFocus) {
        this.mItemsCanFocus = itemsCanFocus;
        if (!itemsCanFocus) {
            this.setDescendantFocusability(393216);
        }
    }

    public boolean getItemsCanFocus() {
        return this.mItemsCanFocus;
    }

    void drawOverscrollHeader(@NonNull Canvas canvas, @NonNull Drawable drawable, Rect bounds) {
        int height = drawable.getMinimumHeight();
        canvas.save();
        canvas.clipRect(bounds);
        int span = bounds.bottom - bounds.top;
        if (span < height) {
            bounds.top = bounds.bottom - height;
        }
        drawable.setBounds(bounds);
        drawable.draw(canvas);
        canvas.restore();
    }

    void drawOverscrollFooter(@NonNull Canvas canvas, @NonNull Drawable drawable, Rect bounds) {
        int height = drawable.getMinimumHeight();
        canvas.save();
        canvas.clipRect(bounds);
        int span = bounds.bottom - bounds.top;
        if (span < height) {
            bounds.bottom = bounds.top + height;
        }
        drawable.setBounds(bounds);
        drawable.draw(canvas);
        canvas.restore();
    }

    @Override
    protected void dispatchDraw(@NonNull Canvas canvas) {
        int dividerHeight = this.mDividerHeight;
        Drawable overscrollHeader = this.mOverScrollHeader;
        Drawable overscrollFooter = this.mOverScrollFooter;
        boolean drawOverscrollHeader = overscrollHeader != null;
        boolean drawOverscrollFooter = overscrollFooter != null;
        boolean drawDividers = dividerHeight > 0 && this.mDivider != null;
        if (drawDividers || drawOverscrollHeader || drawOverscrollFooter) {
            Rect bounds = this.mTempRect;
            bounds.left = this.mPaddingLeft;
            bounds.right = this.getWidth() - this.mPaddingRight;
            int count = this.getChildCount();
            int headerCount = this.getHeaderViewsCount();
            int itemCount = this.mItemCount;
            int footerLimit = itemCount - this.mFooterViewInfos.size();
            boolean headerDividers = this.mHeaderDividersEnabled;
            boolean footerDividers = this.mFooterDividersEnabled;
            int first = this.mFirstPosition;
            ListAdapter adapter = this.mAdapter;
            int effectivePaddingTop = 0;
            int effectivePaddingBottom = 0;
            if (this.hasBooleanFlag(34)) {
                effectivePaddingTop = this.mListPadding.top;
                effectivePaddingBottom = this.mListPadding.bottom;
            }
            int listBottom = this.getHeight() - effectivePaddingBottom + this.getScrollY();
            if (!this.mStackFromBottom) {
                int bottom = 0;
                int scrollY = this.getScrollY();
                if (count > 0 && scrollY < 0) {
                    if (drawOverscrollHeader) {
                        bounds.bottom = 0;
                        bounds.top = scrollY;
                        this.drawOverscrollHeader(canvas, overscrollHeader, bounds);
                    } else if (drawDividers) {
                        bounds.bottom = 0;
                        bounds.top = -dividerHeight;
                        this.drawDivider(canvas, bounds, -1);
                    }
                }
                for (int i = 0; i < count; i++) {
                    int itemIndex = first + i;
                    boolean isHeader = itemIndex < headerCount;
                    boolean isFooter = itemIndex >= footerLimit;
                    if ((headerDividers || !isHeader) && (footerDividers || !isFooter)) {
                        View child = this.getChildAt(i);
                        bottom = child.getBottom();
                        boolean isLastItem = i == count - 1;
                        if (drawDividers && bottom < listBottom && (!drawOverscrollFooter || !isLastItem)) {
                            int nextIndex = itemIndex + 1;
                            if (adapter.isEnabled(itemIndex) && (headerDividers || !isHeader && nextIndex >= headerCount) && (isLastItem || adapter.isEnabled(nextIndex) && (footerDividers || !isFooter && nextIndex < footerLimit))) {
                                bounds.top = bottom;
                                bounds.bottom = bottom + dividerHeight;
                                this.drawDivider(canvas, bounds, i);
                            }
                        }
                    }
                }
                int overFooterBottom = this.getBottom() + this.getScrollY();
                if (drawOverscrollFooter && first + count == itemCount && overFooterBottom > bottom) {
                    bounds.top = bottom;
                    bounds.bottom = overFooterBottom;
                    this.drawOverscrollFooter(canvas, overscrollFooter, bounds);
                }
            } else {
                int scrollYx = this.getScrollY();
                if (count > 0 && drawOverscrollHeader) {
                    bounds.top = scrollYx;
                    bounds.bottom = this.getChildAt(0).getTop();
                    this.drawOverscrollHeader(canvas, overscrollHeader, bounds);
                }
                int start = drawOverscrollHeader ? 1 : 0;
                for (int ix = start; ix < count; ix++) {
                    int itemIndex = first + ix;
                    boolean isHeader = itemIndex < headerCount;
                    boolean isFooter = itemIndex >= footerLimit;
                    if ((headerDividers || !isHeader) && (footerDividers || !isFooter)) {
                        View child = this.getChildAt(ix);
                        int top = child.getTop();
                        if (drawDividers && top > effectivePaddingTop) {
                            boolean isFirstItem = ix == start;
                            int previousIndex = itemIndex - 1;
                            if (adapter.isEnabled(itemIndex) && (headerDividers || previousIndex >= headerCount) && (isFirstItem || adapter.isEnabled(previousIndex) && (footerDividers || previousIndex < footerLimit))) {
                                bounds.top = top - dividerHeight;
                                bounds.bottom = top;
                                this.drawDivider(canvas, bounds, ix - 1);
                            }
                        }
                    }
                }
                if (count > 0 && scrollYx > 0) {
                    if (drawOverscrollFooter) {
                        int absListBottom = this.getBottom();
                        bounds.top = absListBottom;
                        bounds.bottom = absListBottom + scrollYx;
                        this.drawOverscrollFooter(canvas, overscrollFooter, bounds);
                    } else if (drawDividers) {
                        bounds.top = listBottom;
                        bounds.bottom = listBottom + dividerHeight;
                        this.drawDivider(canvas, bounds, -1);
                    }
                }
            }
        }
        super.dispatchDraw(canvas);
    }

    void drawDivider(Canvas canvas, Rect bounds, int childIndex) {
        Drawable divider = this.mDivider;
        divider.setBounds(bounds);
        divider.draw(canvas);
    }

    @Nullable
    public Drawable getDivider() {
        return this.mDivider;
    }

    public void setDivider(@Nullable Drawable divider) {
        if (divider != null) {
            this.mDividerHeight = divider.getIntrinsicHeight();
        } else {
            this.mDividerHeight = 0;
        }
        this.mDivider = divider;
        this.requestLayout();
        this.invalidate();
    }

    public int getDividerHeight() {
        return this.mDividerHeight;
    }

    public void setDividerHeight(int height) {
        this.mDividerHeight = height;
        this.requestLayout();
        this.invalidate();
    }

    public void setHeaderDividersEnabled(boolean headerDividersEnabled) {
        this.mHeaderDividersEnabled = headerDividersEnabled;
        this.invalidate();
    }

    public boolean areHeaderDividersEnabled() {
        return this.mHeaderDividersEnabled;
    }

    public void setFooterDividersEnabled(boolean footerDividersEnabled) {
        this.mFooterDividersEnabled = footerDividersEnabled;
        this.invalidate();
    }

    public boolean areFooterDividersEnabled() {
        return this.mFooterDividersEnabled;
    }

    public void setOverscrollHeader(Drawable header) {
        this.mOverScrollHeader = header;
        if (this.getScrollY() < 0) {
            this.invalidate();
        }
    }

    public Drawable getOverscrollHeader() {
        return this.mOverScrollHeader;
    }

    public void setOverscrollFooter(Drawable footer) {
        this.mOverScrollFooter = footer;
        this.invalidate();
    }

    public Drawable getOverscrollFooter() {
        return this.mOverScrollFooter;
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        ListAdapter adapter = this.mAdapter;
        int closetChildIndex = -1;
        int closestChildTop = 0;
        if (adapter != null && gainFocus && previouslyFocusedRect != null) {
            previouslyFocusedRect.offset(this.getScrollX(), this.getScrollY());
            if (adapter.getCount() < this.getChildCount() + this.mFirstPosition) {
                this.mLayoutMode = 0;
                this.layoutChildren();
            }
            Rect otherRect = this.mTempRect;
            int minDistance = Integer.MAX_VALUE;
            int childCount = this.getChildCount();
            int firstPosition = this.mFirstPosition;
            for (int i = 0; i < childCount; i++) {
                if (adapter.isEnabled(firstPosition + i)) {
                    View other = this.getChildAt(i);
                    other.getDrawingRect(otherRect);
                    this.offsetDescendantRectToMyCoords(other, otherRect);
                    int distance = getDistance(previouslyFocusedRect, otherRect, direction);
                    if (distance < minDistance) {
                        minDistance = distance;
                        closetChildIndex = i;
                        closestChildTop = other.getTop();
                    }
                }
            }
        }
        if (closetChildIndex >= 0) {
            this.setSelectionFromTop(closetChildIndex + this.mFirstPosition, closestChildTop);
        } else {
            this.requestLayout();
        }
    }

    @Nullable
    @Override
    protected <T extends View> T findViewTraversal(int id) {
        View v = super.findViewTraversal(id);
        if (v == null) {
            v = this.findViewInHeadersOrFooters(this.mHeaderViewInfos, id);
            if (v != null) {
                return (T) v;
            }
            v = this.findViewInHeadersOrFooters(this.mFooterViewInfos, id);
            if (v != null) {
                return (T) v;
            }
        }
        return (T) v;
    }

    View findViewInHeadersOrFooters(ArrayList<ListView.FixedViewInfo> where, int id) {
        if (where != null) {
            int len = where.size();
            for (int i = 0; i < len; i++) {
                View v = ((ListView.FixedViewInfo) where.get(i)).view;
                if (!v.isRootNamespace()) {
                    v = v.findViewById(id);
                    if (v != null) {
                        return v;
                    }
                }
            }
        }
        return null;
    }

    @Override
    protected <T extends View> T findViewByPredicateTraversal(@NonNull Predicate<View> predicate, View childToSkip) {
        View v = super.findViewByPredicateTraversal(predicate, childToSkip);
        if (v == null) {
            v = this.findViewByPredicateInHeadersOrFooters(this.mHeaderViewInfos, predicate, childToSkip);
            if (v != null) {
                return (T) v;
            }
            v = this.findViewByPredicateInHeadersOrFooters(this.mFooterViewInfos, predicate, childToSkip);
            if (v != null) {
                return (T) v;
            }
        }
        return (T) v;
    }

    View findViewByPredicateInHeadersOrFooters(ArrayList<ListView.FixedViewInfo> where, Predicate<View> predicate, View childToSkip) {
        if (where != null) {
            int len = where.size();
            for (int i = 0; i < len; i++) {
                View v = ((ListView.FixedViewInfo) where.get(i)).view;
                if (v != childToSkip && !v.isRootNamespace()) {
                    v = v.findViewByPredicate(predicate);
                    if (v != null) {
                        return v;
                    }
                }
            }
        }
        return null;
    }

    @Override
    int getHeightForPosition(int position) {
        int height = super.getHeightForPosition(position);
        return this.shouldAdjustHeightForDivider(position) ? height + this.mDividerHeight : height;
    }

    private boolean shouldAdjustHeightForDivider(int itemIndex) {
        int dividerHeight = this.mDividerHeight;
        Drawable overscrollHeader = this.mOverScrollHeader;
        Drawable overscrollFooter = this.mOverScrollFooter;
        boolean drawOverscrollHeader = overscrollHeader != null;
        boolean drawOverscrollFooter = overscrollFooter != null;
        boolean drawDividers = dividerHeight > 0 && this.mDivider != null;
        if (drawDividers) {
            int itemCount = this.mItemCount;
            int headerCount = this.getHeaderViewsCount();
            int footerLimit = itemCount - this.mFooterViewInfos.size();
            boolean isHeader = itemIndex < headerCount;
            boolean isFooter = itemIndex >= footerLimit;
            boolean headerDividers = this.mHeaderDividersEnabled;
            boolean footerDividers = this.mFooterDividersEnabled;
            if ((headerDividers || !isHeader) && (footerDividers || !isFooter)) {
                ListAdapter adapter = this.mAdapter;
                if (!this.mStackFromBottom) {
                    boolean isLastItem = itemIndex == itemCount - 1;
                    if (!drawOverscrollFooter || !isLastItem) {
                        int nextIndex = itemIndex + 1;
                        return adapter.isEnabled(itemIndex) && (headerDividers || nextIndex >= headerCount) && (isLastItem || adapter.isEnabled(nextIndex) && (footerDividers || nextIndex < footerLimit));
                    }
                } else {
                    int start = drawOverscrollHeader ? 1 : 0;
                    boolean isFirstItem = itemIndex == start;
                    if (!isFirstItem) {
                        int previousIndex = itemIndex - 1;
                        return adapter.isEnabled(itemIndex) && (headerDividers || previousIndex >= headerCount) && adapter.isEnabled(previousIndex) && (footerDividers || previousIndex < footerLimit);
                    }
                }
            }
        }
        return false;
    }

    @Internal
    @NonNull
    protected HeaderViewListAdapter wrapHeaderListAdapterInternal(ArrayList<ListView.FixedViewInfo> headerViewInfos, ArrayList<ListView.FixedViewInfo> footerViewInfos, ListAdapter adapter) {
        return new HeaderViewListAdapter(headerViewInfos, footerViewInfos, adapter);
    }

    @Internal
    protected void wrapHeaderListAdapterInternal() {
        this.mAdapter = this.wrapHeaderListAdapterInternal(this.mHeaderViewInfos, this.mFooterViewInfos, this.mAdapter);
    }

    @Internal
    protected void dispatchDataSetObserverOnChangedInternal() {
        if (this.mDataSetObserver != null) {
            this.mDataSetObserver.onChanged();
        }
    }

    private static class ArrowScrollFocusResult {

        private int mSelectedPosition;

        private int mAmountToScroll;

        void populate(int selectedPosition, int amountToScroll) {
            this.mSelectedPosition = selectedPosition;
            this.mAmountToScroll = amountToScroll;
        }

        public int getSelectedPosition() {
            return this.mSelectedPosition;
        }

        public int getAmountToScroll() {
            return this.mAmountToScroll;
        }
    }

    public static class FixedViewInfo {

        public View view;

        public Object data;

        public boolean isSelectable;
    }

    private class FocusSelector implements Runnable {

        private static final int STATE_SET_SELECTION = 1;

        private static final int STATE_WAIT_FOR_LAYOUT = 2;

        private static final int STATE_REQUEST_FOCUS = 3;

        private int mAction;

        private int mPosition;

        private int mPositionTop;

        ListView.FocusSelector setupForSetSelection(int position, int top) {
            this.mPosition = position;
            this.mPositionTop = top;
            this.mAction = 1;
            return this;
        }

        public void run() {
            if (this.mAction == 1) {
                ListView.this.setSelectionFromTop(this.mPosition, this.mPositionTop);
                this.mAction = 2;
            } else if (this.mAction == 3) {
                int childIndex = this.mPosition - ListView.this.mFirstPosition;
                View child = ListView.this.getChildAt(childIndex);
                if (child != null) {
                    child.requestFocus();
                }
                this.mAction = -1;
            }
        }

        @Nullable
        Runnable setupFocusIfValid(int position) {
            if (this.mAction == 2 && position == this.mPosition) {
                this.mAction = 3;
                return this;
            } else {
                return null;
            }
        }

        void onLayoutComplete() {
            if (this.mAction == 2) {
                this.mAction = -1;
            }
        }
    }
}