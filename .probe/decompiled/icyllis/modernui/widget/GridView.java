package icyllis.modernui.widget;

import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.KeyEvent;
import icyllis.modernui.view.MeasureSpec;
import icyllis.modernui.view.SoundEffectConstants;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.jetbrains.annotations.ApiStatus.Internal;

public class GridView extends AbsListView {

    public static final int NO_STRETCH = 0;

    public static final int STRETCH_SPACING = 1;

    public static final int STRETCH_COLUMN_WIDTH = 2;

    public static final int STRETCH_SPACING_UNIFORM = 3;

    public static final int AUTO_FIT = -1;

    private int mNumColumns = -1;

    private int mHorizontalSpacing = 0;

    private int mRequestedHorizontalSpacing;

    private int mVerticalSpacing = 0;

    private int mStretchMode = 2;

    private int mColumnWidth;

    private int mRequestedColumnWidth;

    private int mRequestedNumColumns;

    private View mReferenceView = null;

    private View mReferenceViewInSelectedRow = null;

    private int mGravity = 8388611;

    private final Rect mTempRect = new Rect();

    public GridView(Context context) {
        super(context);
    }

    public ListAdapter getAdapter() {
        return this.mAdapter;
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        if (this.mAdapter != null && this.mDataSetObserver != null) {
            this.mAdapter.unregisterDataSetObserver(this.mDataSetObserver);
        }
        this.resetList();
        this.mRecycler.clear();
        this.mAdapter = adapter;
        this.mOldSelectedPosition = -1;
        this.mOldSelectedRowId = Long.MIN_VALUE;
        super.setAdapter(adapter);
        if (this.mAdapter != null) {
            this.mOldItemCount = this.mItemCount;
            this.mItemCount = this.mAdapter.getCount();
            this.mDataChanged = true;
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
            this.checkSelectionChanged();
        } else {
            this.checkFocus();
            this.checkSelectionChanged();
        }
        this.requestLayout();
    }

    @Override
    int lookForSelectablePosition(int position, boolean lookDown) {
        ListAdapter adapter = this.mAdapter;
        if (adapter == null || this.isInTouchMode()) {
            return -1;
        } else {
            return position >= 0 && position < this.mItemCount ? position : -1;
        }
    }

    @Override
    void fillGap(boolean down) {
        int numColumns = this.mNumColumns;
        int verticalSpacing = this.mVerticalSpacing;
        int count = this.getChildCount();
        if (down) {
            int paddingTop = 0;
            if (this.hasBooleanFlag(34)) {
                paddingTop = this.getListPaddingTop();
            }
            int startOffset = count > 0 ? this.getChildAt(count - 1).getBottom() + verticalSpacing : paddingTop;
            int position = this.mFirstPosition + count;
            if (this.mStackFromBottom) {
                position += numColumns - 1;
            }
            this.fillDown(position, startOffset);
            this.correctTooHigh(numColumns, verticalSpacing, this.getChildCount());
        } else {
            int paddingBottom = 0;
            if (this.hasBooleanFlag(34)) {
                paddingBottom = this.getListPaddingBottom();
            }
            int startOffset = count > 0 ? this.getChildAt(0).getTop() - verticalSpacing : this.getHeight() - paddingBottom;
            int position = this.mFirstPosition;
            if (!this.mStackFromBottom) {
                position -= numColumns;
            } else {
                position--;
            }
            this.fillUp(position, startOffset);
            this.correctTooLow(numColumns, verticalSpacing, this.getChildCount());
        }
    }

    private View fillDown(int pos, int nextTop) {
        View selectedView = null;
        int end = this.getHeight();
        if (this.hasBooleanFlag(34)) {
            end -= this.mListPadding.bottom;
        }
        while (nextTop < end && pos < this.mItemCount) {
            View temp = this.makeRow(pos, nextTop, true);
            if (temp != null) {
                selectedView = temp;
            }
            nextTop = this.mReferenceView.getBottom() + this.mVerticalSpacing;
            pos += this.mNumColumns;
        }
        return selectedView;
    }

    private View makeRow(int startPos, int y, boolean flow) {
        int columnWidth = this.mColumnWidth;
        int horizontalSpacing = this.mHorizontalSpacing;
        boolean isLayoutRtl = this.isLayoutRtl();
        int nextLeft;
        if (isLayoutRtl) {
            nextLeft = this.getWidth() - this.mListPadding.right - columnWidth - (this.mStretchMode == 3 ? horizontalSpacing : 0);
        } else {
            nextLeft = this.mListPadding.left + (this.mStretchMode == 3 ? horizontalSpacing : 0);
        }
        int last;
        if (!this.mStackFromBottom) {
            last = Math.min(startPos + this.mNumColumns, this.mItemCount);
        } else {
            last = startPos + 1;
            startPos = Math.max(0, startPos - this.mNumColumns + 1);
            if (last - startPos < this.mNumColumns) {
                int deltaLeft = (this.mNumColumns - (last - startPos)) * (columnWidth + horizontalSpacing);
                nextLeft += (isLayoutRtl ? -1 : 1) * deltaLeft;
            }
        }
        View selectedView = null;
        boolean hasFocus = this.shouldShowSelector();
        boolean inClick = this.touchModeDrawsInPressedState();
        int selectedPosition = this.mSelectedPosition;
        View child = null;
        int nextChildDir = isLayoutRtl ? -1 : 1;
        for (int pos = startPos; pos < last; pos++) {
            boolean selected = pos == selectedPosition;
            int where = flow ? -1 : pos - startPos;
            child = this.makeAndAddView(pos, y, flow, nextLeft, selected, where);
            nextLeft += nextChildDir * columnWidth;
            if (pos < last - 1) {
                nextLeft += nextChildDir * horizontalSpacing;
            }
            if (selected && (hasFocus || inClick)) {
                selectedView = child;
            }
        }
        this.mReferenceView = child;
        if (selectedView != null) {
            this.mReferenceViewInSelectedRow = this.mReferenceView;
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
            View temp = this.makeRow(pos, nextBottom, false);
            if (temp != null) {
                selectedView = temp;
            }
            nextBottom = this.mReferenceView.getTop() - this.mVerticalSpacing;
            this.mFirstPosition = pos;
            pos -= this.mNumColumns;
        }
        if (this.mStackFromBottom) {
            this.mFirstPosition = Math.max(0, pos + 1);
        }
        return selectedView;
    }

    private View fillFromTop(int nextTop) {
        this.mFirstPosition = Math.min(this.mFirstPosition, this.mSelectedPosition);
        this.mFirstPosition = Math.min(this.mFirstPosition, this.mItemCount - 1);
        if (this.mFirstPosition < 0) {
            this.mFirstPosition = 0;
        }
        this.mFirstPosition = this.mFirstPosition - this.mFirstPosition % this.mNumColumns;
        return this.fillDown(this.mFirstPosition, nextTop);
    }

    private View fillFromBottom(int lastPosition, int nextBottom) {
        lastPosition = Math.max(lastPosition, this.mSelectedPosition);
        lastPosition = Math.min(lastPosition, this.mItemCount - 1);
        int invertedPosition = this.mItemCount - 1 - lastPosition;
        lastPosition = this.mItemCount - 1 - (invertedPosition - invertedPosition % this.mNumColumns);
        return this.fillUp(lastPosition, nextBottom);
    }

    private View fillSelection(int childrenTop, int childrenBottom) {
        int selectedPosition = this.reconcileSelectedPosition();
        int numColumns = this.mNumColumns;
        int verticalSpacing = this.mVerticalSpacing;
        int rowEnd = -1;
        int rowStart;
        if (!this.mStackFromBottom) {
            rowStart = selectedPosition - selectedPosition % numColumns;
        } else {
            int invertedSelection = this.mItemCount - 1 - selectedPosition;
            rowEnd = this.mItemCount - 1 - (invertedSelection - invertedSelection % numColumns);
            rowStart = Math.max(0, rowEnd - numColumns + 1);
        }
        int fadingEdgeLength = this.getVerticalFadingEdgeLength();
        int topSelectionPixel = this.getTopSelectionPixel(childrenTop, fadingEdgeLength, rowStart);
        View sel = this.makeRow(this.mStackFromBottom ? rowEnd : rowStart, topSelectionPixel, true);
        this.mFirstPosition = rowStart;
        View referenceView = this.mReferenceView;
        if (!this.mStackFromBottom) {
            this.fillDown(rowStart + numColumns, referenceView.getBottom() + verticalSpacing);
            this.pinToBottom(childrenBottom);
            this.fillUp(rowStart - numColumns, referenceView.getTop() - verticalSpacing);
            this.adjustViewsUpOrDown();
        } else {
            int bottomSelectionPixel = this.getBottomSelectionPixel(childrenBottom, fadingEdgeLength, numColumns, rowStart);
            int offset = bottomSelectionPixel - referenceView.getBottom();
            this.offsetChildrenTopAndBottom(offset);
            this.fillUp(rowStart - 1, referenceView.getTop() - verticalSpacing);
            this.pinToTop(childrenTop);
            this.fillDown(rowEnd + numColumns, referenceView.getBottom() + verticalSpacing);
            this.adjustViewsUpOrDown();
        }
        return sel;
    }

    private void pinToTop(int childrenTop) {
        if (this.mFirstPosition == 0) {
            int top = this.getChildAt(0).getTop();
            int offset = childrenTop - top;
            if (offset < 0) {
                this.offsetChildrenTopAndBottom(offset);
            }
        }
    }

    private void pinToBottom(int childrenBottom) {
        int count = this.getChildCount();
        if (this.mFirstPosition + count == this.mItemCount) {
            int bottom = this.getChildAt(count - 1).getBottom();
            int offset = childrenBottom - bottom;
            if (offset > 0) {
                this.offsetChildrenTopAndBottom(offset);
            }
        }
    }

    @Override
    int findMotionRow(int y) {
        int childCount = this.getChildCount();
        if (childCount > 0) {
            int numColumns = this.mNumColumns;
            if (!this.mStackFromBottom) {
                for (int i = 0; i < childCount; i += numColumns) {
                    if (y <= this.getChildAt(i).getBottom()) {
                        return this.mFirstPosition + i;
                    }
                }
            } else {
                for (int ix = childCount - 1; ix >= 0; ix -= numColumns) {
                    if (y >= this.getChildAt(ix).getTop()) {
                        return this.mFirstPosition + ix;
                    }
                }
            }
        }
        return -1;
    }

    private View fillSpecific(int position, int top) {
        int numColumns = this.mNumColumns;
        int motionRowEnd = -1;
        int motionRowStart;
        if (!this.mStackFromBottom) {
            motionRowStart = position - position % numColumns;
        } else {
            int invertedSelection = this.mItemCount - 1 - position;
            motionRowEnd = this.mItemCount - 1 - (invertedSelection - invertedSelection % numColumns);
            motionRowStart = Math.max(0, motionRowEnd - numColumns + 1);
        }
        View temp = this.makeRow(this.mStackFromBottom ? motionRowEnd : motionRowStart, top, true);
        this.mFirstPosition = motionRowStart;
        View referenceView = this.mReferenceView;
        if (referenceView == null) {
            return null;
        } else {
            int verticalSpacing = this.mVerticalSpacing;
            View above;
            View below;
            if (!this.mStackFromBottom) {
                above = this.fillUp(motionRowStart - numColumns, referenceView.getTop() - verticalSpacing);
                this.adjustViewsUpOrDown();
                below = this.fillDown(motionRowStart + numColumns, referenceView.getBottom() + verticalSpacing);
                int childCount = this.getChildCount();
                if (childCount > 0) {
                    this.correctTooHigh(numColumns, verticalSpacing, childCount);
                }
            } else {
                below = this.fillDown(motionRowEnd + numColumns, referenceView.getBottom() + verticalSpacing);
                this.adjustViewsUpOrDown();
                above = this.fillUp(motionRowStart - 1, referenceView.getTop() - verticalSpacing);
                int childCount = this.getChildCount();
                if (childCount > 0) {
                    this.correctTooLow(numColumns, verticalSpacing, childCount);
                }
            }
            if (temp != null) {
                return temp;
            } else {
                return above != null ? above : below;
            }
        }
    }

    private void correctTooHigh(int numColumns, int verticalSpacing, int childCount) {
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
                    this.fillUp(this.mFirstPosition - (this.mStackFromBottom ? 1 : numColumns), firstChild.getTop() - verticalSpacing);
                    this.adjustViewsUpOrDown();
                }
            }
        }
    }

    private void correctTooLow(int numColumns, int verticalSpacing, int childCount) {
        if (this.mFirstPosition == 0 && childCount > 0) {
            View firstChild = this.getChildAt(0);
            int firstTop = firstChild.getTop();
            int start = this.mListPadding.top;
            int end = this.getHeight() - this.mListPadding.bottom;
            int topOffset = firstTop - start;
            View lastChild = this.getChildAt(childCount - 1);
            int lastBottom = lastChild.getBottom();
            int lastPosition = this.mFirstPosition + childCount - 1;
            if (topOffset > 0 && (lastPosition < this.mItemCount - 1 || lastBottom > end)) {
                if (lastPosition == this.mItemCount - 1) {
                    topOffset = Math.min(topOffset, lastBottom - end);
                }
                this.offsetChildrenTopAndBottom(-topOffset);
                if (lastPosition < this.mItemCount - 1) {
                    this.fillDown(lastPosition + (!this.mStackFromBottom ? 1 : numColumns), lastChild.getBottom() + verticalSpacing);
                    this.adjustViewsUpOrDown();
                }
            }
        }
    }

    private View fillFromSelection(int selectedTop, int childrenTop, int childrenBottom) {
        int fadingEdgeLength = this.getVerticalFadingEdgeLength();
        int selectedPosition = this.mSelectedPosition;
        int numColumns = this.mNumColumns;
        int verticalSpacing = this.mVerticalSpacing;
        int rowEnd = -1;
        int rowStart;
        if (!this.mStackFromBottom) {
            rowStart = selectedPosition - selectedPosition % numColumns;
        } else {
            int invertedSelection = this.mItemCount - 1 - selectedPosition;
            rowEnd = this.mItemCount - 1 - (invertedSelection - invertedSelection % numColumns);
            rowStart = Math.max(0, rowEnd - numColumns + 1);
        }
        int topSelectionPixel = this.getTopSelectionPixel(childrenTop, fadingEdgeLength, rowStart);
        int bottomSelectionPixel = this.getBottomSelectionPixel(childrenBottom, fadingEdgeLength, numColumns, rowStart);
        View sel = this.makeRow(this.mStackFromBottom ? rowEnd : rowStart, selectedTop, true);
        this.mFirstPosition = rowStart;
        View referenceView = this.mReferenceView;
        this.adjustForTopFadingEdge(referenceView, topSelectionPixel, bottomSelectionPixel);
        this.adjustForBottomFadingEdge(referenceView, topSelectionPixel, bottomSelectionPixel);
        if (!this.mStackFromBottom) {
            this.fillUp(rowStart - numColumns, referenceView.getTop() - verticalSpacing);
            this.adjustViewsUpOrDown();
            this.fillDown(rowStart + numColumns, referenceView.getBottom() + verticalSpacing);
        } else {
            this.fillDown(rowEnd + numColumns, referenceView.getBottom() + verticalSpacing);
            this.adjustViewsUpOrDown();
            this.fillUp(rowStart - 1, referenceView.getTop() - verticalSpacing);
        }
        return sel;
    }

    private int getBottomSelectionPixel(int childrenBottom, int fadingEdgeLength, int numColumns, int rowStart) {
        int bottomSelectionPixel = childrenBottom;
        if (rowStart + numColumns - 1 < this.mItemCount - 1) {
            bottomSelectionPixel = childrenBottom - fadingEdgeLength;
        }
        return bottomSelectionPixel;
    }

    private int getTopSelectionPixel(int childrenTop, int fadingEdgeLength, int rowStart) {
        int topSelectionPixel = childrenTop;
        if (rowStart > 0) {
            topSelectionPixel = childrenTop + fadingEdgeLength;
        }
        return topSelectionPixel;
    }

    private void adjustForBottomFadingEdge(View childInSelectedRow, int topSelectionPixel, int bottomSelectionPixel) {
        if (childInSelectedRow.getBottom() > bottomSelectionPixel) {
            int spaceAbove = childInSelectedRow.getTop() - topSelectionPixel;
            int spaceBelow = childInSelectedRow.getBottom() - bottomSelectionPixel;
            int offset = Math.min(spaceAbove, spaceBelow);
            this.offsetChildrenTopAndBottom(-offset);
        }
    }

    private void adjustForTopFadingEdge(View childInSelectedRow, int topSelectionPixel, int bottomSelectionPixel) {
        if (childInSelectedRow.getTop() < topSelectionPixel) {
            int spaceAbove = topSelectionPixel - childInSelectedRow.getTop();
            int spaceBelow = bottomSelectionPixel - childInSelectedRow.getBottom();
            int offset = Math.min(spaceAbove, spaceBelow);
            this.offsetChildrenTopAndBottom(offset);
        }
    }

    @Override
    public void smoothScrollToPosition(int position) {
        super.smoothScrollToPosition(position);
    }

    @Override
    public void smoothScrollByOffset(int offset) {
        super.smoothScrollByOffset(offset);
    }

    private View moveSelection(int delta, int childrenTop, int childrenBottom) {
        int fadingEdgeLength = this.getVerticalFadingEdgeLength();
        int selectedPosition = this.mSelectedPosition;
        int numColumns = this.mNumColumns;
        int verticalSpacing = this.mVerticalSpacing;
        int rowEnd = -1;
        int oldRowStart;
        int rowStart;
        if (!this.mStackFromBottom) {
            oldRowStart = selectedPosition - delta - (selectedPosition - delta) % numColumns;
            rowStart = selectedPosition - selectedPosition % numColumns;
        } else {
            int invertedSelection = this.mItemCount - 1 - selectedPosition;
            rowEnd = this.mItemCount - 1 - (invertedSelection - invertedSelection % numColumns);
            rowStart = Math.max(0, rowEnd - numColumns + 1);
            invertedSelection = this.mItemCount - 1 - (selectedPosition - delta);
            oldRowStart = this.mItemCount - 1 - (invertedSelection - invertedSelection % numColumns);
            oldRowStart = Math.max(0, oldRowStart - numColumns + 1);
        }
        int rowDelta = rowStart - oldRowStart;
        int topSelectionPixel = this.getTopSelectionPixel(childrenTop, fadingEdgeLength, rowStart);
        int bottomSelectionPixel = this.getBottomSelectionPixel(childrenBottom, fadingEdgeLength, numColumns, rowStart);
        this.mFirstPosition = rowStart;
        View sel;
        View referenceView;
        if (rowDelta > 0) {
            int oldBottom = this.mReferenceViewInSelectedRow == null ? 0 : this.mReferenceViewInSelectedRow.getBottom();
            sel = this.makeRow(this.mStackFromBottom ? rowEnd : rowStart, oldBottom + verticalSpacing, true);
            referenceView = this.mReferenceView;
            this.adjustForBottomFadingEdge(referenceView, topSelectionPixel, bottomSelectionPixel);
        } else if (rowDelta < 0) {
            int oldTop = this.mReferenceViewInSelectedRow == null ? 0 : this.mReferenceViewInSelectedRow.getTop();
            sel = this.makeRow(this.mStackFromBottom ? rowEnd : rowStart, oldTop - verticalSpacing, false);
            referenceView = this.mReferenceView;
            this.adjustForTopFadingEdge(referenceView, topSelectionPixel, bottomSelectionPixel);
        } else {
            int oldTop = this.mReferenceViewInSelectedRow == null ? 0 : this.mReferenceViewInSelectedRow.getTop();
            sel = this.makeRow(this.mStackFromBottom ? rowEnd : rowStart, oldTop, true);
            referenceView = this.mReferenceView;
        }
        if (!this.mStackFromBottom) {
            this.fillUp(rowStart - numColumns, referenceView.getTop() - verticalSpacing);
            this.adjustViewsUpOrDown();
            this.fillDown(rowStart + numColumns, referenceView.getBottom() + verticalSpacing);
        } else {
            this.fillDown(rowEnd + numColumns, referenceView.getBottom() + verticalSpacing);
            this.adjustViewsUpOrDown();
            this.fillUp(rowStart - 1, referenceView.getTop() - verticalSpacing);
        }
        return sel;
    }

    private boolean determineColumns(int availableSpace) {
        int requestedHorizontalSpacing = this.mRequestedHorizontalSpacing;
        int stretchMode = this.mStretchMode;
        int requestedColumnWidth = this.mRequestedColumnWidth;
        boolean didNotInitiallyFit = false;
        if (this.mRequestedNumColumns == -1) {
            if (requestedColumnWidth > 0) {
                this.mNumColumns = (availableSpace + requestedHorizontalSpacing) / (requestedColumnWidth + requestedHorizontalSpacing);
            } else {
                this.mNumColumns = 2;
            }
        } else {
            this.mNumColumns = this.mRequestedNumColumns;
        }
        if (this.mNumColumns <= 0) {
            this.mNumColumns = 1;
        }
        switch(stretchMode) {
            case 0:
                this.mColumnWidth = requestedColumnWidth;
                this.mHorizontalSpacing = requestedHorizontalSpacing;
                break;
            default:
                int spaceLeftOver = availableSpace - this.mNumColumns * requestedColumnWidth - (this.mNumColumns - 1) * requestedHorizontalSpacing;
                if (spaceLeftOver < 0) {
                    didNotInitiallyFit = true;
                }
                switch(stretchMode) {
                    case 1:
                        this.mColumnWidth = requestedColumnWidth;
                        if (this.mNumColumns > 1) {
                            this.mHorizontalSpacing = requestedHorizontalSpacing + spaceLeftOver / (this.mNumColumns - 1);
                        } else {
                            this.mHorizontalSpacing = requestedHorizontalSpacing + spaceLeftOver;
                        }
                        break;
                    case 2:
                        this.mColumnWidth = requestedColumnWidth + spaceLeftOver / this.mNumColumns;
                        this.mHorizontalSpacing = requestedHorizontalSpacing;
                        break;
                    case 3:
                        this.mColumnWidth = requestedColumnWidth;
                        if (this.mNumColumns > 1) {
                            this.mHorizontalSpacing = requestedHorizontalSpacing + spaceLeftOver / (this.mNumColumns + 1);
                        } else {
                            this.mHorizontalSpacing = requestedHorizontalSpacing + spaceLeftOver;
                        }
                }
        }
        return didNotInitiallyFit;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == 0) {
            if (this.mColumnWidth > 0) {
                widthSize = this.mColumnWidth + this.mListPadding.left + this.mListPadding.right;
            } else {
                widthSize = this.mListPadding.left + this.mListPadding.right;
            }
            widthSize += this.getVerticalScrollbarWidth();
        }
        int childWidth = widthSize - this.mListPadding.left - this.mListPadding.right;
        boolean didNotInitiallyFit = this.determineColumns(childWidth);
        int childHeight = 0;
        int childState = 0;
        this.mItemCount = this.mAdapter == null ? 0 : this.mAdapter.getCount();
        int count = this.mItemCount;
        if (count > 0) {
            View child = this.obtainView(0, this.mIsScrap);
            AbsListView.LayoutParams p = (AbsListView.LayoutParams) child.getLayoutParams();
            if (p == null) {
                p = (AbsListView.LayoutParams) this.generateDefaultLayoutParams();
                child.setLayoutParams(p);
            }
            p.viewType = this.mAdapter.getItemViewType(0);
            p.isEnabled = this.mAdapter.isEnabled(0);
            p.forceAdd = true;
            int childHeightSpec = getChildMeasureSpec(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), 0), 0, p.height);
            int childWidthSpec = getChildMeasureSpec(MeasureSpec.makeMeasureSpec(this.mColumnWidth, 1073741824), 0, p.width);
            child.measure(childWidthSpec, childHeightSpec);
            childHeight = child.getMeasuredHeight();
            childState = combineMeasuredStates(childState, child.getMeasuredState());
            if (this.mRecycler.shouldRecycleViewType(p.viewType)) {
                this.mRecycler.addScrapView(child, -1);
            }
        }
        if (heightMode == 0) {
            heightSize = this.mListPadding.top + this.mListPadding.bottom + childHeight + this.getVerticalFadingEdgeLength() * 2;
        }
        if (heightMode == Integer.MIN_VALUE) {
            int ourSize = this.mListPadding.top + this.mListPadding.bottom;
            int numColumns = this.mNumColumns;
            for (int i = 0; i < count; i += numColumns) {
                ourSize += childHeight;
                if (i + numColumns < count) {
                    ourSize += this.mVerticalSpacing;
                }
                if (ourSize >= heightSize) {
                    ourSize = heightSize;
                    break;
                }
            }
            heightSize = ourSize;
        }
        if (widthMode == Integer.MIN_VALUE && this.mRequestedNumColumns != -1) {
            int ourSize = this.mRequestedNumColumns * this.mColumnWidth + (this.mRequestedNumColumns - 1) * this.mHorizontalSpacing + this.mListPadding.left + this.mListPadding.right;
            if (ourSize > widthSize || didNotInitiallyFit) {
                widthSize |= 16777216;
            }
        }
        this.setMeasuredDimension(widthSize, heightSize);
        this.mWidthMeasureSpec = widthMeasureSpec;
    }

    @Override
    protected void layoutChildren() {
        boolean blockLayoutRequests = this.mBlockLayoutRequests;
        if (!blockLayoutRequests) {
            this.mBlockLayoutRequests = true;
        }
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
            switch(this.mLayoutMode) {
                case 1:
                case 3:
                case 4:
                case 5:
                    break;
                case 2:
                    int index = this.mNextSelectedPosition - this.mFirstPosition;
                    if (index >= 0 && index < childCount) {
                        newSel = this.getChildAt(index);
                    }
                    break;
                case 6:
                    if (this.mNextSelectedPosition >= 0) {
                        delta = this.mNextSelectedPosition - this.mSelectedPosition;
                    }
                    break;
                default:
                    int index = this.mSelectedPosition - this.mFirstPosition;
                    if (index >= 0 && index < childCount) {
                        oldSel = this.getChildAt(index);
                    }
                    oldFirst = this.getChildAt(0);
            }
            boolean dataChanged = this.mDataChanged;
            if (dataChanged) {
                this.handleDataChanged();
            }
            if (this.mItemCount != 0) {
                this.setSelectedPositionInt(this.mNextSelectedPosition);
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
                            sel = this.fillSelection(childrenTop, childrenBottom);
                        }
                        break;
                    case 3:
                        sel = this.fillUp(this.mItemCount - 1, childrenBottom);
                        this.adjustViewsUpOrDown();
                        break;
                    case 4:
                        sel = this.fillSpecific(this.mSelectedPosition, this.mSpecificTop);
                        break;
                    case 5:
                        sel = this.fillSpecific(this.mSyncPosition, this.mSpecificTop);
                        break;
                    case 6:
                        sel = this.moveSelection(delta, childrenTop, childrenBottom);
                        break;
                    default:
                        if (childCount != 0) {
                            if (this.mSelectedPosition >= 0 && this.mSelectedPosition < this.mItemCount) {
                                sel = this.fillSpecific(this.mSelectedPosition, oldSel == null ? childrenTop : oldSel.getTop());
                            } else if (this.mFirstPosition < this.mItemCount) {
                                sel = this.fillSpecific(this.mFirstPosition, oldFirst == null ? childrenTop : oldFirst.getTop());
                            } else {
                                sel = this.fillSpecific(0, childrenTop);
                            }
                        } else if (!this.mStackFromBottom) {
                            this.setSelectedPositionInt(this.mAdapter != null && !this.isInTouchMode() ? 0 : -1);
                            sel = this.fillFromTop(childrenTop);
                        } else {
                            int last = this.mItemCount - 1;
                            this.setSelectedPositionInt(this.mAdapter != null && !this.isInTouchMode() ? last : -1);
                            sel = this.fillFromBottom(last, childrenBottom);
                        }
                }
                recycleBin.scrapActiveViews();
                if (sel != null) {
                    this.positionSelector(-1, sel);
                    this.mSelectedTop = sel.getTop();
                } else {
                    boolean inTouchMode = this.mTouchMode > 0 && this.mTouchMode < 3;
                    if (inTouchMode) {
                        View child = this.getChildAt(this.mMotionPosition - this.mFirstPosition);
                        if (child != null) {
                            this.positionSelector(this.mMotionPosition, child);
                        }
                    } else if (this.mSelectedPosition != -1) {
                        View child = this.getChildAt(this.mSelectorPosition - this.mFirstPosition);
                        if (child != null) {
                            this.positionSelector(this.mSelectorPosition, child);
                        }
                    } else {
                        this.mSelectedTop = 0;
                        this.mSelectorRect.setEmpty();
                    }
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
            if (!blockLayoutRequests) {
                this.mBlockLayoutRequests = false;
            }
        }
    }

    private View makeAndAddView(int position, int y, boolean flow, int childrenLeft, boolean selected, int where) {
        if (!this.mDataChanged) {
            View activeView = this.mRecycler.getActiveView(position);
            if (activeView != null) {
                this.setupChild(activeView, position, y, flow, childrenLeft, selected, true, where);
                return activeView;
            }
        }
        View child = this.obtainView(position, this.mIsScrap);
        this.setupChild(child, position, y, flow, childrenLeft, selected, this.mIsScrap[0], where);
        return child;
    }

    private void setupChild(View child, int position, int y, boolean flowDown, int childrenLeft, boolean selected, boolean isAttachedToWindow, int where) {
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
            if (isSelected) {
                this.requestFocus();
            }
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
        if (isAttachedToWindow && !p.forceAdd) {
            this.attachViewToParent(child, where, p);
            if (!isAttachedToWindow || ((AbsListView.LayoutParams) child.getLayoutParams()).scrappedFromPosition != position) {
                child.jumpDrawablesToCurrentState();
            }
        } else {
            p.forceAdd = false;
            this.addViewInLayout(child, where, p, true);
        }
        if (needToMeasure) {
            int childHeightSpec = ViewGroup.getChildMeasureSpec(MeasureSpec.makeMeasureSpec(0, 0), 0, p.height);
            int childWidthSpec = ViewGroup.getChildMeasureSpec(MeasureSpec.makeMeasureSpec(this.mColumnWidth, 1073741824), 0, p.width);
            child.measure(childWidthSpec, childHeightSpec);
        } else {
            this.cleanupLayoutState(child);
        }
        int w = child.getMeasuredWidth();
        int h = child.getMeasuredHeight();
        int childTop = flowDown ? y : y - h;
        int layoutDirection = this.getLayoutDirection();
        int absoluteGravity = Gravity.getAbsoluteGravity(this.mGravity, layoutDirection);
        int childLeft = switch(absoluteGravity & 7) {
            case 1 ->
                childrenLeft + (this.mColumnWidth - w) / 2;
            default ->
                childrenLeft;
            case 3 ->
                childrenLeft;
            case 5 ->
                childrenLeft + this.mColumnWidth - w;
        };
        if (needToMeasure) {
            int childRight = childLeft + w;
            int childBottom = childTop + h;
            child.layout(childLeft, childTop, childRight, childBottom);
        } else {
            child.offsetLeftAndRight(childLeft - child.getLeft());
            child.offsetTopAndBottom(childTop - child.getTop());
        }
    }

    @Override
    public void setSelection(int position) {
        if (!this.isInTouchMode()) {
            this.setNextSelectedPositionInt(position);
        } else {
            this.mResurrectToPosition = position;
        }
        this.mLayoutMode = 2;
        if (this.mPositionScroller != null) {
            this.mPositionScroller.stop();
        }
        this.requestLayout();
    }

    @Override
    void setSelectionInt(int position) {
        int previousSelectedPosition = this.mNextSelectedPosition;
        if (this.mPositionScroller != null) {
            this.mPositionScroller.stop();
        }
        this.setNextSelectedPositionInt(position);
        this.layoutChildren();
        int next = this.mStackFromBottom ? this.mItemCount - 1 - this.mNextSelectedPosition : this.mNextSelectedPosition;
        int previous = this.mStackFromBottom ? this.mItemCount - 1 - previousSelectedPosition : previousSelectedPosition;
        int nextRow = next / this.mNumColumns;
        int previousRow = previous / this.mNumColumns;
        if (nextRow != previousRow) {
            this.awakenScrollBars();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return this.commonKey(keyCode, 1, event);
    }

    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        return this.commonKey(keyCode, repeatCount, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return this.commonKey(keyCode, 1, event);
    }

    private boolean commonKey(int keyCode, int count, KeyEvent event) {
        if (this.mAdapter == null) {
            return false;
        } else {
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
                            handled = this.resurrectSelectionIfNeeded() || this.sequenceScroll(2);
                        } else if (event.hasModifiers(1)) {
                            handled = this.resurrectSelectionIfNeeded() || this.sequenceScroll(1);
                        }
                    case 259:
                    case 260:
                    case 261:
                    default:
                        break;
                    case 262:
                        if (event.hasNoModifiers()) {
                            handled = this.resurrectSelectionIfNeeded() || this.arrowScroll(66);
                        }
                        break;
                    case 263:
                        if (event.hasNoModifiers()) {
                            handled = this.resurrectSelectionIfNeeded() || this.arrowScroll(17);
                        }
                        break;
                    case 264:
                        if (event.hasNoModifiers()) {
                            handled = this.resurrectSelectionIfNeeded() || this.arrowScroll(130);
                        } else if (event.hasModifiers(4)) {
                            handled = this.resurrectSelectionIfNeeded() || this.fullScroll(130);
                        }
                        break;
                    case 265:
                        if (event.hasNoModifiers()) {
                            handled = this.resurrectSelectionIfNeeded() || this.arrowScroll(33);
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
        }
    }

    boolean pageScroll(int direction) {
        int nextPage = -1;
        if (direction == 33) {
            nextPage = Math.max(0, this.mSelectedPosition - this.getChildCount());
        } else if (direction == 130) {
            nextPage = Math.min(this.mItemCount - 1, this.mSelectedPosition + this.getChildCount());
        }
        if (nextPage >= 0) {
            this.setSelectionInt(nextPage);
            this.invokeOnItemScrollListener();
            this.awakenScrollBars();
            return true;
        } else {
            return false;
        }
    }

    boolean fullScroll(int direction) {
        boolean moved = false;
        if (direction == 33) {
            this.mLayoutMode = 2;
            this.setSelectionInt(0);
            this.invokeOnItemScrollListener();
            moved = true;
        } else if (direction == 130) {
            this.mLayoutMode = 2;
            this.setSelectionInt(this.mItemCount - 1);
            this.invokeOnItemScrollListener();
            moved = true;
        }
        if (moved) {
            this.awakenScrollBars();
        }
        return moved;
    }

    boolean arrowScroll(int direction) {
        int selectedPosition = this.mSelectedPosition;
        int numColumns = this.mNumColumns;
        boolean moved = false;
        int startOfRowPos;
        int endOfRowPos;
        if (!this.mStackFromBottom) {
            startOfRowPos = selectedPosition / numColumns * numColumns;
            endOfRowPos = Math.min(startOfRowPos + numColumns - 1, this.mItemCount - 1);
        } else {
            int invertedSelection = this.mItemCount - 1 - selectedPosition;
            endOfRowPos = this.mItemCount - 1 - invertedSelection / numColumns * numColumns;
            startOfRowPos = Math.max(0, endOfRowPos - numColumns + 1);
        }
        switch(direction) {
            case 33:
                if (startOfRowPos > 0) {
                    this.mLayoutMode = 6;
                    this.setSelectionInt(Math.max(0, selectedPosition - numColumns));
                    moved = true;
                }
                break;
            case 130:
                if (endOfRowPos < this.mItemCount - 1) {
                    this.mLayoutMode = 6;
                    this.setSelectionInt(Math.min(selectedPosition + numColumns, this.mItemCount - 1));
                    moved = true;
                }
        }
        boolean isLayoutRtl = this.isLayoutRtl();
        if (selectedPosition <= startOfRowPos || (direction != 17 || isLayoutRtl) && (direction != 66 || !isLayoutRtl)) {
            if (selectedPosition < endOfRowPos && (direction == 17 && isLayoutRtl || direction == 66 && !isLayoutRtl)) {
                this.mLayoutMode = 6;
                this.setSelectionInt(Math.min(selectedPosition + 1, this.mItemCount - 1));
                moved = true;
            }
        } else {
            this.mLayoutMode = 6;
            this.setSelectionInt(Math.max(0, selectedPosition - 1));
            moved = true;
        }
        if (moved) {
            this.playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
            this.invokeOnItemScrollListener();
        }
        if (moved) {
            this.awakenScrollBars();
        }
        return moved;
    }

    boolean sequenceScroll(int direction) {
        int selectedPosition = this.mSelectedPosition;
        int numColumns = this.mNumColumns;
        int count = this.mItemCount;
        int startOfRow;
        int endOfRow;
        if (!this.mStackFromBottom) {
            startOfRow = selectedPosition / numColumns * numColumns;
            endOfRow = Math.min(startOfRow + numColumns - 1, count - 1);
        } else {
            int invertedSelection = count - 1 - selectedPosition;
            endOfRow = count - 1 - invertedSelection / numColumns * numColumns;
            startOfRow = Math.max(0, endOfRow - numColumns + 1);
        }
        boolean moved = false;
        boolean showScroll = false;
        switch(direction) {
            case 1:
                if (selectedPosition > 0) {
                    this.mLayoutMode = 6;
                    this.setSelectionInt(selectedPosition - 1);
                    moved = true;
                    showScroll = selectedPosition == startOfRow;
                }
                break;
            case 2:
                if (selectedPosition < count - 1) {
                    this.mLayoutMode = 6;
                    this.setSelectionInt(selectedPosition + 1);
                    moved = true;
                    showScroll = selectedPosition == endOfRow;
                }
        }
        if (moved) {
            this.playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
            this.invokeOnItemScrollListener();
        }
        if (showScroll) {
            this.awakenScrollBars();
        }
        return moved;
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        int closestChildIndex = -1;
        if (gainFocus && previouslyFocusedRect != null) {
            previouslyFocusedRect.offset(this.mScrollX, this.mScrollY);
            Rect otherRect = this.mTempRect;
            int minDistance = Integer.MAX_VALUE;
            int childCount = this.getChildCount();
            for (int i = 0; i < childCount; i++) {
                if (this.isCandidateSelection(i, direction)) {
                    View other = this.getChildAt(i);
                    other.getDrawingRect(otherRect);
                    this.offsetDescendantRectToMyCoords(other, otherRect);
                    int distance = getDistance(previouslyFocusedRect, otherRect, direction);
                    if (distance < minDistance) {
                        minDistance = distance;
                        closestChildIndex = i;
                    }
                }
            }
        }
        if (closestChildIndex >= 0) {
            this.setSelection(closestChildIndex + this.mFirstPosition);
        } else {
            this.requestLayout();
        }
    }

    private boolean isCandidateSelection(int childIndex, int direction) {
        int count = this.getChildCount();
        int invertedIndex = count - 1 - childIndex;
        int rowStart;
        int rowEnd;
        if (!this.mStackFromBottom) {
            rowStart = childIndex - childIndex % this.mNumColumns;
            rowEnd = Math.min(rowStart + this.mNumColumns - 1, count);
        } else {
            rowEnd = count - 1 - (invertedIndex - invertedIndex % this.mNumColumns);
            rowStart = Math.max(0, rowEnd - this.mNumColumns + 1);
        }
        switch(direction) {
            case 1:
                return childIndex == rowEnd && rowEnd == count - 1;
            case 2:
                return childIndex == rowStart && rowStart == 0;
            case 17:
                return childIndex == rowEnd;
            case 33:
                return rowEnd == count - 1;
            case 66:
                return childIndex == rowStart;
            case 130:
                return rowStart == 0;
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT, FOCUS_FORWARD, FOCUS_BACKWARD}.");
        }
    }

    public void setGravity(int gravity) {
        if (this.mGravity != gravity) {
            this.mGravity = gravity;
            this.requestLayoutIfNecessary();
        }
    }

    public int getGravity() {
        return this.mGravity;
    }

    public void setHorizontalSpacing(int horizontalSpacing) {
        if (horizontalSpacing != this.mRequestedHorizontalSpacing) {
            this.mRequestedHorizontalSpacing = horizontalSpacing;
            this.requestLayoutIfNecessary();
        }
    }

    public int getHorizontalSpacing() {
        return this.mHorizontalSpacing;
    }

    public int getRequestedHorizontalSpacing() {
        return this.mRequestedHorizontalSpacing;
    }

    public void setVerticalSpacing(int verticalSpacing) {
        if (verticalSpacing != this.mVerticalSpacing) {
            this.mVerticalSpacing = verticalSpacing;
            this.requestLayoutIfNecessary();
        }
    }

    public int getVerticalSpacing() {
        return this.mVerticalSpacing;
    }

    public void setStretchMode(int stretchMode) {
        if (stretchMode != this.mStretchMode) {
            this.mStretchMode = stretchMode;
            this.requestLayoutIfNecessary();
        }
    }

    public int getStretchMode() {
        return this.mStretchMode;
    }

    public void setColumnWidth(int columnWidth) {
        if (columnWidth != this.mRequestedColumnWidth) {
            this.mRequestedColumnWidth = columnWidth;
            this.requestLayoutIfNecessary();
        }
    }

    public int getColumnWidth() {
        return this.mColumnWidth;
    }

    public int getRequestedColumnWidth() {
        return this.mRequestedColumnWidth;
    }

    public void setNumColumns(int numColumns) {
        if (numColumns != this.mRequestedNumColumns) {
            this.mRequestedNumColumns = numColumns;
            this.requestLayoutIfNecessary();
        }
    }

    public int getNumColumns() {
        return this.mNumColumns;
    }

    private void adjustViewsUpOrDown() {
        int childCount = this.getChildCount();
        if (childCount > 0) {
            int delta;
            if (!this.mStackFromBottom) {
                View child = this.getChildAt(0);
                delta = child.getTop() - this.mListPadding.top;
                if (this.mFirstPosition != 0) {
                    delta -= this.mVerticalSpacing;
                }
                if (delta < 0) {
                    delta = 0;
                }
            } else {
                View childx = this.getChildAt(childCount - 1);
                delta = childx.getBottom() - (this.getHeight() - this.mListPadding.bottom);
                if (this.mFirstPosition + childCount < this.mItemCount) {
                    delta += this.mVerticalSpacing;
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

    @Override
    protected int computeVerticalScrollExtent() {
        int count = this.getChildCount();
        if (count > 0) {
            int numColumns = this.mNumColumns;
            int rowCount = (count + numColumns - 1) / numColumns;
            int extent = rowCount * 100;
            View view = this.getChildAt(0);
            int top = view.getTop();
            int height = view.getHeight();
            if (height > 0) {
                extent += top * 100 / height;
            }
            view = this.getChildAt(count - 1);
            int bottom = view.getBottom();
            height = view.getHeight();
            if (height > 0) {
                extent -= (bottom - this.getHeight()) * 100 / height;
            }
            return extent;
        } else {
            return 0;
        }
    }

    @Override
    protected int computeVerticalScrollOffset() {
        if (this.mFirstPosition >= 0 && this.getChildCount() > 0) {
            View view = this.getChildAt(0);
            int top = view.getTop();
            int height = view.getHeight();
            if (height > 0) {
                int numColumns = this.mNumColumns;
                int rowCount = (this.mItemCount + numColumns - 1) / numColumns;
                int oddItemsOnFirstRow = this.isStackFromBottom() ? rowCount * numColumns - this.mItemCount : 0;
                int whichRow = (this.mFirstPosition + oddItemsOnFirstRow) / numColumns;
                return Math.max(whichRow * 100 - top * 100 / height + (int) ((float) this.mScrollY / (float) this.getHeight() * (float) rowCount * 100.0F), 0);
            }
        }
        return 0;
    }

    @Override
    protected int computeVerticalScrollRange() {
        int numColumns = this.mNumColumns;
        int rowCount = (this.mItemCount + numColumns - 1) / numColumns;
        int result = Math.max(rowCount * 100, 0);
        if (this.mScrollY != 0) {
            result += Math.abs((int) ((float) this.mScrollY / (float) this.getHeight() * (float) rowCount * 100.0F));
        }
        return result;
    }

    @Retention(RetentionPolicy.SOURCE)
    @Internal
    public @interface StretchMode {
    }
}