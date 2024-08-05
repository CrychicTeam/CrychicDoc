package icyllis.modernui.widget;

import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.MeasureSpec;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public class LinearLayout extends ViewGroup {

    public static final int HORIZONTAL = 0;

    public static final int VERTICAL = 1;

    public static final int SHOW_DIVIDER_NONE = 0;

    public static final int SHOW_DIVIDER_BEGINNING = 1;

    public static final int SHOW_DIVIDER_MIDDLE = 2;

    public static final int SHOW_DIVIDER_END = 4;

    private boolean mBaselineAligned = true;

    private int mBaselineAlignedChildIndex = -1;

    private int mBaselineChildTop = 0;

    private int mOrientation;

    private int mGravity = 8388659;

    private int mTotalLength;

    private float mWeightSum;

    private boolean mUseLargestChild;

    private int[] mMaxAscent;

    private int[] mMaxDescent;

    private static final int VERTICAL_GRAVITY_COUNT = 4;

    private static final int INDEX_CENTER_VERTICAL = 0;

    private static final int INDEX_TOP = 1;

    private static final int INDEX_BOTTOM = 2;

    private static final int INDEX_FILL = 3;

    private Drawable mDivider;

    private int mDividerWidth;

    private int mDividerHeight;

    private int mShowDividers;

    private int mDividerPadding;

    private int mLayoutDirection = -1;

    public LinearLayout(Context context) {
        super(context);
    }

    private boolean isShowingDividers() {
        return this.mShowDividers != 0 && this.mDivider != null;
    }

    public void setShowDividers(int showDividers) {
        if (showDividers != this.mShowDividers) {
            this.mShowDividers = showDividers;
            this.setWillNotDraw(!this.isShowingDividers());
            this.requestLayout();
        }
    }

    public int getShowDividers() {
        return this.mShowDividers;
    }

    @Nullable
    public Drawable getDividerDrawable() {
        return this.mDivider;
    }

    public void setDividerDrawable(@Nullable Drawable divider) {
        if (divider != this.mDivider) {
            this.mDivider = divider;
            if (divider != null) {
                this.mDividerWidth = divider.getIntrinsicWidth();
                this.mDividerHeight = divider.getIntrinsicHeight();
            } else {
                this.mDividerWidth = 0;
                this.mDividerHeight = 0;
            }
            this.setWillNotDraw(!this.isShowingDividers());
            this.requestLayout();
        }
    }

    public void setDividerPadding(int padding) {
        if (padding != this.mDividerPadding) {
            this.mDividerPadding = padding;
            if (this.isShowingDividers()) {
                this.requestLayout();
                this.invalidate();
            }
        }
    }

    public int getDividerPadding() {
        return this.mDividerPadding;
    }

    public int getDividerWidth() {
        return this.mDividerWidth;
    }

    public boolean isBaselineAligned() {
        return this.mBaselineAligned;
    }

    public void setBaselineAligned(boolean baselineAligned) {
        this.mBaselineAligned = baselineAligned;
    }

    public boolean isMeasureWithLargestChildEnabled() {
        return this.mUseLargestChild;
    }

    public void setMeasureWithLargestChildEnabled(boolean enabled) {
        this.mUseLargestChild = enabled;
    }

    @Override
    public int getBaseline() {
        if (this.mBaselineAlignedChildIndex < 0) {
            return super.getBaseline();
        } else if (this.getChildCount() <= this.mBaselineAlignedChildIndex) {
            throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout set to an index that is out of bounds.");
        } else {
            View child = this.getChildAt(this.mBaselineAlignedChildIndex);
            int childBaseline = child.getBaseline();
            if (childBaseline == -1) {
                if (this.mBaselineAlignedChildIndex == 0) {
                    return -1;
                } else {
                    throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout points to a View that doesn't know how to get its baseline.");
                }
            } else {
                int childTop = this.mBaselineChildTop;
                if (this.mOrientation == 1) {
                    int majorGravity = this.mGravity & 112;
                    if (majorGravity != 48) {
                        switch(majorGravity) {
                            case 16:
                                childTop += (this.getHeight() - this.mPaddingTop - this.mPaddingBottom - this.mTotalLength) / 2;
                                break;
                            case 80:
                                childTop = this.getHeight() - this.mPaddingBottom - this.mTotalLength;
                        }
                    }
                }
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) child.getLayoutParams();
                return childTop + lp.topMargin + childBaseline;
            }
        }
    }

    public int getBaselineAlignedChildIndex() {
        return this.mBaselineAlignedChildIndex;
    }

    public void setBaselineAlignedChildIndex(int i) {
        if (i >= 0 && i < this.getChildCount()) {
            this.mBaselineAlignedChildIndex = i;
        } else {
            throw new IllegalArgumentException("base aligned child index out of range (0, " + this.getChildCount() + ")");
        }
    }

    @Nullable
    View getVirtualChildAt(int index) {
        return this.getChildAt(index);
    }

    int getVirtualChildCount() {
        return this.getChildCount();
    }

    public float getWeightSum() {
        return this.mWeightSum;
    }

    public void setWeightSum(float weightSum) {
        this.mWeightSum = Math.max(0.0F, weightSum);
    }

    @Override
    protected void onDraw(@Nonnull Canvas canvas) {
        if (this.mDivider != null) {
            if (this.mOrientation == 1) {
                this.drawDividersVertical(canvas);
            } else {
                this.drawDividersHorizontal(canvas);
            }
        }
    }

    void drawDividersVertical(Canvas canvas) {
        int count = this.getVirtualChildCount();
        for (int i = 0; i < count; i++) {
            View child = this.getVirtualChildAt(i);
            if (child != null && child.getVisibility() != 8 && this.hasDividerBeforeChildAt(i)) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) child.getLayoutParams();
                int top = child.getTop() - lp.topMargin - this.mDividerHeight;
                this.drawHorizontalDivider(canvas, top);
            }
        }
        if (this.hasDividerBeforeChildAt(count)) {
            View child = this.getLastNonGoneChild();
            int bottom;
            if (child == null) {
                bottom = this.getHeight() - this.getPaddingBottom() - this.mDividerHeight;
            } else {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) child.getLayoutParams();
                bottom = child.getBottom() + lp.bottomMargin;
            }
            this.drawHorizontalDivider(canvas, bottom);
        }
    }

    @Nullable
    private View getLastNonGoneChild() {
        for (int i = this.getVirtualChildCount() - 1; i >= 0; i--) {
            View child = this.getVirtualChildAt(i);
            if (child != null && child.getVisibility() != 8) {
                return child;
            }
        }
        return null;
    }

    void drawDividersHorizontal(Canvas canvas) {
        int count = this.getVirtualChildCount();
        boolean isLayoutRtl = this.isLayoutRtl();
        for (int i = 0; i < count; i++) {
            View child = this.getVirtualChildAt(i);
            if (child != null && child.getVisibility() != 8 && this.hasDividerBeforeChildAt(i)) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) child.getLayoutParams();
                int position;
                if (isLayoutRtl) {
                    position = child.getRight() + lp.rightMargin;
                } else {
                    position = child.getLeft() - lp.leftMargin - this.mDividerWidth;
                }
                this.drawVerticalDivider(canvas, position);
            }
        }
        if (this.hasDividerBeforeChildAt(count)) {
            View child = this.getLastNonGoneChild();
            int position;
            if (child == null) {
                if (isLayoutRtl) {
                    position = this.getPaddingLeft();
                } else {
                    position = this.getWidth() - this.getPaddingRight() - this.mDividerWidth;
                }
            } else {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) child.getLayoutParams();
                if (isLayoutRtl) {
                    position = child.getLeft() - lp.leftMargin - this.mDividerWidth;
                } else {
                    position = child.getRight() + lp.rightMargin;
                }
            }
            this.drawVerticalDivider(canvas, position);
        }
    }

    void drawHorizontalDivider(Canvas canvas, int top) {
        this.mDivider.setBounds(this.getPaddingLeft() + this.mDividerPadding, top, this.getWidth() - this.getPaddingRight() - this.mDividerPadding, top + this.mDividerHeight);
        this.mDivider.draw(canvas);
    }

    void drawVerticalDivider(Canvas canvas, int left) {
        this.mDivider.setBounds(left, this.getPaddingTop() + this.mDividerPadding, left + this.mDividerWidth, this.getHeight() - this.getPaddingBottom() - this.mDividerPadding);
        this.mDivider.draw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mOrientation == 1) {
            this.measureVertical(widthMeasureSpec, heightMeasureSpec);
        } else {
            this.measureHorizontal(widthMeasureSpec, heightMeasureSpec);
        }
    }

    protected boolean hasDividerBeforeChildAt(int childIndex) {
        if (childIndex == this.getVirtualChildCount()) {
            return (this.mShowDividers & 4) != 0;
        } else {
            boolean allViewsAreGoneBefore = this.allViewsAreGoneBefore(childIndex);
            return allViewsAreGoneBefore ? (this.mShowDividers & 1) != 0 : (this.mShowDividers & 2) != 0;
        }
    }

    private boolean allViewsAreGoneBefore(int childIndex) {
        for (int i = childIndex - 1; i >= 0; i--) {
            View child = this.getVirtualChildAt(i);
            if (child != null && child.getVisibility() != 8) {
                return false;
            }
        }
        return true;
    }

    void measureVertical(int widthMeasureSpec, int heightMeasureSpec) {
        this.mTotalLength = 0;
        int maxWidth = 0;
        int childState = 0;
        int alternativeMaxWidth = 0;
        int weightedMaxWidth = 0;
        boolean allFillParent = true;
        float totalWeight = 0.0F;
        int count = this.getVirtualChildCount();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        boolean matchWidth = false;
        boolean skippedMeasure = false;
        int baselineChildIndex = this.mBaselineAlignedChildIndex;
        boolean useLargestChild = this.mUseLargestChild;
        int largestChildHeight = Integer.MIN_VALUE;
        int consumedExcessSpace = 0;
        int nonSkippedChildCount = 0;
        for (int i = 0; i < count; i++) {
            View child = this.getVirtualChildAt(i);
            if (child == null) {
                this.mTotalLength = this.mTotalLength + this.measureNullChild(i);
            } else if (child.getVisibility() == 8) {
                i += this.getChildrenSkipCount(child, i);
            } else {
                nonSkippedChildCount++;
                if (this.hasDividerBeforeChildAt(i)) {
                    this.mTotalLength = this.mTotalLength + this.mDividerHeight;
                }
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) child.getLayoutParams();
                totalWeight += lp.weight;
                boolean useExcessSpace = lp.height == 0 && lp.weight > 0.0F;
                if (heightMode == 1073741824 && useExcessSpace) {
                    int totalLength = this.mTotalLength;
                    this.mTotalLength = Math.max(totalLength, totalLength + lp.topMargin + lp.bottomMargin);
                    skippedMeasure = true;
                } else {
                    if (useExcessSpace) {
                        lp.height = -2;
                    }
                    int usedHeight = totalWeight == 0.0F ? this.mTotalLength : 0;
                    this.measureChildBeforeLayout(child, i, widthMeasureSpec, 0, heightMeasureSpec, usedHeight);
                    int childHeight = child.getMeasuredHeight();
                    if (useExcessSpace) {
                        lp.height = 0;
                        consumedExcessSpace += childHeight;
                    }
                    int totalLength = this.mTotalLength;
                    this.mTotalLength = Math.max(totalLength, totalLength + childHeight + lp.topMargin + lp.bottomMargin + this.getNextLocationOffset(child));
                    if (useLargestChild) {
                        largestChildHeight = Math.max(childHeight, largestChildHeight);
                    }
                }
                if (baselineChildIndex >= 0 && baselineChildIndex == i + 1) {
                    this.mBaselineChildTop = this.mTotalLength;
                }
                if (i < baselineChildIndex && lp.weight > 0.0F) {
                    throw new RuntimeException("A child of LinearLayout with index less than mBaselineAlignedChildIndex has weight > 0, which won't work.  Either remove the weight, or don't set mBaselineAlignedChildIndex.");
                }
                boolean matchWidthLocally = false;
                if (widthMode != 1073741824 && lp.width == -1) {
                    matchWidth = true;
                    matchWidthLocally = true;
                }
                int margin = lp.leftMargin + lp.rightMargin;
                int measuredWidth = child.getMeasuredWidth() + margin;
                maxWidth = Math.max(maxWidth, measuredWidth);
                childState = combineMeasuredStates(childState, child.getMeasuredState());
                allFillParent = allFillParent && lp.width == -1;
                if (lp.weight > 0.0F) {
                    weightedMaxWidth = Math.max(weightedMaxWidth, matchWidthLocally ? margin : measuredWidth);
                } else {
                    alternativeMaxWidth = Math.max(alternativeMaxWidth, matchWidthLocally ? margin : measuredWidth);
                }
                i += this.getChildrenSkipCount(child, i);
            }
        }
        if (nonSkippedChildCount > 0 && this.hasDividerBeforeChildAt(count)) {
            this.mTotalLength = this.mTotalLength + this.mDividerHeight;
        }
        if (useLargestChild && (heightMode == Integer.MIN_VALUE || heightMode == 0)) {
            this.mTotalLength = 0;
            for (int ix = 0; ix < count; ix++) {
                View child = this.getVirtualChildAt(ix);
                if (child == null) {
                    this.mTotalLength = this.mTotalLength + this.measureNullChild(ix);
                } else if (child.getVisibility() == 8) {
                    ix += this.getChildrenSkipCount(child, ix);
                } else {
                    LinearLayout.LayoutParams lpx = (LinearLayout.LayoutParams) child.getLayoutParams();
                    int totalLength = this.mTotalLength;
                    this.mTotalLength = Math.max(totalLength, totalLength + largestChildHeight + lpx.topMargin + lpx.bottomMargin + this.getNextLocationOffset(child));
                }
            }
        }
        this.mTotalLength = this.mTotalLength + this.mPaddingTop + this.mPaddingBottom;
        int heightSize = this.mTotalLength;
        heightSize = Math.max(heightSize, this.getSuggestedMinimumHeight());
        int heightSizeAndState = resolveSizeAndState(heightSize, heightMeasureSpec, 0);
        heightSize = heightSizeAndState & 16777215;
        int remainingExcess = heightSize - this.mTotalLength + consumedExcessSpace;
        if (!skippedMeasure && !(totalWeight > 0.0F)) {
            alternativeMaxWidth = Math.max(alternativeMaxWidth, weightedMaxWidth);
            if (useLargestChild && heightMode != 1073741824) {
                for (int ixx = 0; ixx < count; ixx++) {
                    View child = this.getVirtualChildAt(ixx);
                    if (child != null && child.getVisibility() != 8) {
                        LinearLayout.LayoutParams lpx = (LinearLayout.LayoutParams) child.getLayoutParams();
                        float childExtra = lpx.weight;
                        if (childExtra > 0.0F) {
                            child.measure(MeasureSpec.makeMeasureSpec(child.getMeasuredWidth(), 1073741824), MeasureSpec.makeMeasureSpec(largestChildHeight, 1073741824));
                        }
                    }
                }
            }
        } else {
            float remainingWeightSum = this.mWeightSum > 0.0F ? this.mWeightSum : totalWeight;
            this.mTotalLength = 0;
            for (int ixxx = 0; ixxx < count; ixxx++) {
                View child = this.getVirtualChildAt(ixxx);
                if (child != null && child.getVisibility() != 8) {
                    LinearLayout.LayoutParams lpx = (LinearLayout.LayoutParams) child.getLayoutParams();
                    float childWeight = lpx.weight;
                    if (childWeight > 0.0F) {
                        int share = (int) (childWeight * (float) remainingExcess / remainingWeightSum);
                        remainingExcess -= share;
                        remainingWeightSum -= childWeight;
                        int childHeightx;
                        if (this.mUseLargestChild && heightMode != 1073741824) {
                            childHeightx = largestChildHeight;
                        } else if (lpx.height == 0) {
                            childHeightx = share;
                        } else {
                            childHeightx = child.getMeasuredHeight() + share;
                        }
                        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(Math.max(0, childHeightx), 1073741824);
                        int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, this.mPaddingLeft + this.mPaddingRight + lpx.leftMargin + lpx.rightMargin, lpx.width);
                        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
                        childState = combineMeasuredStates(childState, child.getMeasuredState() & -256);
                    }
                    int margin = lpx.leftMargin + lpx.rightMargin;
                    int measuredWidth = child.getMeasuredWidth() + margin;
                    maxWidth = Math.max(maxWidth, measuredWidth);
                    boolean matchWidthLocallyx = widthMode != 1073741824 && lpx.width == -1;
                    alternativeMaxWidth = Math.max(alternativeMaxWidth, matchWidthLocallyx ? margin : measuredWidth);
                    allFillParent = allFillParent && lpx.width == -1;
                    int totalLength = this.mTotalLength;
                    this.mTotalLength = Math.max(totalLength, totalLength + child.getMeasuredHeight() + lpx.topMargin + lpx.bottomMargin + this.getNextLocationOffset(child));
                }
            }
            this.mTotalLength = this.mTotalLength + this.mPaddingTop + this.mPaddingBottom;
        }
        if (!allFillParent && widthMode != 1073741824) {
            maxWidth = alternativeMaxWidth;
        }
        maxWidth += this.mPaddingLeft + this.mPaddingRight;
        maxWidth = Math.max(maxWidth, this.getSuggestedMinimumWidth());
        this.setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState), heightSizeAndState);
        if (matchWidth) {
            this.forceUniformWidth(count, heightMeasureSpec);
        }
    }

    private void forceUniformWidth(int count, int heightMeasureSpec) {
        int uniformMeasureSpec = MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), 1073741824);
        for (int i = 0; i < count; i++) {
            View child = this.getVirtualChildAt(i);
            if (child != null && child.getVisibility() != 8) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) child.getLayoutParams();
                if (lp.width == -1) {
                    int oldHeight = lp.height;
                    lp.height = child.getMeasuredHeight();
                    this.measureChildWithMargins(child, uniformMeasureSpec, 0, heightMeasureSpec, 0);
                    lp.height = oldHeight;
                }
            }
        }
    }

    void measureHorizontal(int widthMeasureSpec, int heightMeasureSpec) {
        this.mTotalLength = 0;
        int maxHeight = 0;
        int childState = 0;
        int alternativeMaxHeight = 0;
        int weightedMaxHeight = 0;
        boolean allFillParent = true;
        float totalWeight = 0.0F;
        int count = this.getVirtualChildCount();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        boolean matchHeight = false;
        boolean skippedMeasure = false;
        if (this.mMaxAscent == null || this.mMaxDescent == null) {
            this.mMaxAscent = new int[4];
            this.mMaxDescent = new int[4];
        }
        int[] maxAscent = this.mMaxAscent;
        int[] maxDescent = this.mMaxDescent;
        maxAscent[0] = maxAscent[1] = maxAscent[2] = maxAscent[3] = -1;
        maxDescent[0] = maxDescent[1] = maxDescent[2] = maxDescent[3] = -1;
        boolean baselineAligned = this.mBaselineAligned;
        boolean useLargestChild = this.mUseLargestChild;
        boolean isExactly = widthMode == 1073741824;
        int largestChildWidth = Integer.MIN_VALUE;
        int usedExcessSpace = 0;
        int nonSkippedChildCount = 0;
        for (int i = 0; i < count; i++) {
            View child = this.getVirtualChildAt(i);
            if (child == null) {
                this.mTotalLength = this.mTotalLength + this.measureNullChild(i);
            } else if (child.getVisibility() == 8) {
                i += this.getChildrenSkipCount(child, i);
            } else {
                nonSkippedChildCount++;
                if (this.hasDividerBeforeChildAt(i)) {
                    this.mTotalLength = this.mTotalLength + this.mDividerWidth;
                }
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) child.getLayoutParams();
                totalWeight += lp.weight;
                boolean useExcessSpace = lp.width == 0 && lp.weight > 0.0F;
                if (widthMode == 1073741824 && useExcessSpace) {
                    if (isExactly) {
                        this.mTotalLength = this.mTotalLength + lp.leftMargin + lp.rightMargin;
                    } else {
                        int totalLength = this.mTotalLength;
                        this.mTotalLength = Math.max(totalLength, totalLength + lp.leftMargin + lp.rightMargin);
                    }
                    if (baselineAligned) {
                        int freeWidthSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), 0);
                        int freeHeightSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), 0);
                        child.measure(freeWidthSpec, freeHeightSpec);
                    } else {
                        skippedMeasure = true;
                    }
                } else {
                    if (useExcessSpace) {
                        lp.width = -2;
                    }
                    int usedWidth = totalWeight == 0.0F ? this.mTotalLength : 0;
                    this.measureChildBeforeLayout(child, i, widthMeasureSpec, usedWidth, heightMeasureSpec, 0);
                    int childWidth = child.getMeasuredWidth();
                    if (useExcessSpace) {
                        lp.width = 0;
                        usedExcessSpace += childWidth;
                    }
                    if (isExactly) {
                        this.mTotalLength = this.mTotalLength + childWidth + lp.leftMargin + lp.rightMargin + this.getNextLocationOffset(child);
                    } else {
                        int totalLength = this.mTotalLength;
                        this.mTotalLength = Math.max(totalLength, totalLength + childWidth + lp.leftMargin + lp.rightMargin + this.getNextLocationOffset(child));
                    }
                    if (useLargestChild) {
                        largestChildWidth = Math.max(childWidth, largestChildWidth);
                    }
                }
                boolean matchHeightLocally = false;
                if (heightMode != 1073741824 && lp.height == -1) {
                    matchHeight = true;
                    matchHeightLocally = true;
                }
                int margin = lp.topMargin + lp.bottomMargin;
                int childHeight = child.getMeasuredHeight() + margin;
                childState = combineMeasuredStates(childState, child.getMeasuredState());
                if (baselineAligned) {
                    int childBaseline = child.getBaseline();
                    if (childBaseline != -1) {
                        int gravity = (lp.gravity < 0 ? this.mGravity : lp.gravity) & 112;
                        int index = (gravity >> 4 & -2) >> 1;
                        maxAscent[index] = Math.max(maxAscent[index], childBaseline);
                        maxDescent[index] = Math.max(maxDescent[index], childHeight - childBaseline);
                    }
                }
                maxHeight = Math.max(maxHeight, childHeight);
                allFillParent = allFillParent && lp.height == -1;
                if (lp.weight > 0.0F) {
                    weightedMaxHeight = Math.max(weightedMaxHeight, matchHeightLocally ? margin : childHeight);
                } else {
                    alternativeMaxHeight = Math.max(alternativeMaxHeight, matchHeightLocally ? margin : childHeight);
                }
                i += this.getChildrenSkipCount(child, i);
            }
        }
        if (nonSkippedChildCount > 0 && this.hasDividerBeforeChildAt(count)) {
            this.mTotalLength = this.mTotalLength + this.mDividerWidth;
        }
        if (maxAscent[1] != -1 || maxAscent[0] != -1 || maxAscent[2] != -1 || maxAscent[3] != -1) {
            int ascent = Math.max(maxAscent[3], Math.max(maxAscent[0], Math.max(maxAscent[1], maxAscent[2])));
            int descent = Math.max(maxDescent[3], Math.max(maxDescent[0], Math.max(maxDescent[1], maxDescent[2])));
            maxHeight = Math.max(maxHeight, ascent + descent);
        }
        if (useLargestChild && (widthMode == Integer.MIN_VALUE || widthMode == 0)) {
            this.mTotalLength = 0;
            for (int ix = 0; ix < count; ix++) {
                View child = this.getVirtualChildAt(ix);
                if (child == null) {
                    this.mTotalLength = this.mTotalLength + this.measureNullChild(ix);
                } else if (child.getVisibility() == 8) {
                    ix += this.getChildrenSkipCount(child, ix);
                } else {
                    LinearLayout.LayoutParams lpx = (LinearLayout.LayoutParams) child.getLayoutParams();
                    if (isExactly) {
                        this.mTotalLength = this.mTotalLength + largestChildWidth + lpx.leftMargin + lpx.rightMargin + this.getNextLocationOffset(child);
                    } else {
                        int totalLength = this.mTotalLength;
                        this.mTotalLength = Math.max(totalLength, totalLength + largestChildWidth + lpx.leftMargin + lpx.rightMargin + this.getNextLocationOffset(child));
                    }
                }
            }
        }
        this.mTotalLength = this.mTotalLength + this.mPaddingLeft + this.mPaddingRight;
        int widthSize = this.mTotalLength;
        widthSize = Math.max(widthSize, this.getSuggestedMinimumWidth());
        int widthSizeAndState = resolveSizeAndState(widthSize, widthMeasureSpec, 0);
        widthSize = widthSizeAndState & 16777215;
        int remainingExcess = widthSize - this.mTotalLength + usedExcessSpace;
        if (!skippedMeasure && !(totalWeight > 0.0F)) {
            alternativeMaxHeight = Math.max(alternativeMaxHeight, weightedMaxHeight);
            if (useLargestChild && widthMode != 1073741824) {
                for (int ixx = 0; ixx < count; ixx++) {
                    View child = this.getVirtualChildAt(ixx);
                    if (child != null && child.getVisibility() != 8) {
                        LinearLayout.LayoutParams lpx = (LinearLayout.LayoutParams) child.getLayoutParams();
                        float childExtra = lpx.weight;
                        if (childExtra > 0.0F) {
                            child.measure(MeasureSpec.makeMeasureSpec(largestChildWidth, 1073741824), MeasureSpec.makeMeasureSpec(child.getMeasuredHeight(), 1073741824));
                        }
                    }
                }
            }
        } else {
            float remainingWeightSum = this.mWeightSum > 0.0F ? this.mWeightSum : totalWeight;
            maxAscent[0] = maxAscent[1] = maxAscent[2] = maxAscent[3] = -1;
            maxDescent[0] = maxDescent[1] = maxDescent[2] = maxDescent[3] = -1;
            maxHeight = -1;
            this.mTotalLength = 0;
            for (int ixxx = 0; ixxx < count; ixxx++) {
                View child = this.getVirtualChildAt(ixxx);
                if (child != null && child.getVisibility() != 8) {
                    LinearLayout.LayoutParams lpx = (LinearLayout.LayoutParams) child.getLayoutParams();
                    float childWeight = lpx.weight;
                    if (childWeight > 0.0F) {
                        int share = (int) (childWeight * (float) remainingExcess / remainingWeightSum);
                        remainingExcess -= share;
                        remainingWeightSum -= childWeight;
                        int childWidthx;
                        if (this.mUseLargestChild && widthMode != 1073741824) {
                            childWidthx = largestChildWidth;
                        } else if (lpx.width == 0) {
                            childWidthx = share;
                        } else {
                            childWidthx = child.getMeasuredWidth() + share;
                        }
                        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(Math.max(0, childWidthx), 1073741824);
                        int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, this.mPaddingTop + this.mPaddingBottom + lpx.topMargin + lpx.bottomMargin, lpx.height);
                        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
                        childState = combineMeasuredStates(childState, child.getMeasuredState() & 0xFF000000);
                    }
                    if (isExactly) {
                        this.mTotalLength = this.mTotalLength + child.getMeasuredWidth() + lpx.leftMargin + lpx.rightMargin + this.getNextLocationOffset(child);
                    } else {
                        int totalLength = this.mTotalLength;
                        this.mTotalLength = Math.max(totalLength, totalLength + child.getMeasuredWidth() + lpx.leftMargin + lpx.rightMargin + this.getNextLocationOffset(child));
                    }
                    boolean matchHeightLocallyx = heightMode != 1073741824 && lpx.height == -1;
                    int marginx = lpx.topMargin + lpx.bottomMargin;
                    int childHeightx = child.getMeasuredHeight() + marginx;
                    maxHeight = Math.max(maxHeight, childHeightx);
                    alternativeMaxHeight = Math.max(alternativeMaxHeight, matchHeightLocallyx ? marginx : childHeightx);
                    allFillParent = allFillParent && lpx.height == -1;
                    if (baselineAligned) {
                        int childBaseline = child.getBaseline();
                        if (childBaseline != -1) {
                            int gravity = (lpx.gravity < 0 ? this.mGravity : lpx.gravity) & 112;
                            int index = (gravity >> 4 & -2) >> 1;
                            maxAscent[index] = Math.max(maxAscent[index], childBaseline);
                            maxDescent[index] = Math.max(maxDescent[index], childHeightx - childBaseline);
                        }
                    }
                }
            }
            this.mTotalLength = this.mTotalLength + this.mPaddingLeft + this.mPaddingRight;
            if (maxAscent[1] != -1 || maxAscent[0] != -1 || maxAscent[2] != -1 || maxAscent[3] != -1) {
                int ascent = Math.max(maxAscent[3], Math.max(maxAscent[0], Math.max(maxAscent[1], maxAscent[2])));
                int descent = Math.max(maxDescent[3], Math.max(maxDescent[0], Math.max(maxDescent[1], maxDescent[2])));
                maxHeight = Math.max(maxHeight, ascent + descent);
            }
        }
        if (!allFillParent && heightMode != 1073741824) {
            maxHeight = alternativeMaxHeight;
        }
        maxHeight += this.mPaddingTop + this.mPaddingBottom;
        maxHeight = Math.max(maxHeight, this.getSuggestedMinimumHeight());
        this.setMeasuredDimension(widthSizeAndState | childState & 0xFF000000, resolveSizeAndState(maxHeight, heightMeasureSpec, childState << 16));
        if (matchHeight) {
            this.forceUniformHeight(count, widthMeasureSpec);
        }
    }

    private void forceUniformHeight(int count, int widthMeasureSpec) {
        int uniformMeasureSpec = MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 1073741824);
        for (int i = 0; i < count; i++) {
            View child = this.getVirtualChildAt(i);
            if (child != null && child.getVisibility() != 8) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) child.getLayoutParams();
                if (lp.height == -1) {
                    int oldWidth = lp.width;
                    lp.width = child.getMeasuredWidth();
                    this.measureChildWithMargins(child, widthMeasureSpec, 0, uniformMeasureSpec, 0);
                    lp.width = oldWidth;
                }
            }
        }
    }

    int getChildrenSkipCount(View child, int index) {
        return 0;
    }

    int measureNullChild(int childIndex) {
        return 0;
    }

    void measureChildBeforeLayout(View child, int childIndex, int widthMeasureSpec, int totalWidth, int heightMeasureSpec, int totalHeight) {
        this.measureChildWithMargins(child, widthMeasureSpec, totalWidth, heightMeasureSpec, totalHeight);
    }

    int getLocationOffset(View child) {
        return 0;
    }

    int getNextLocationOffset(View child) {
        return 0;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (this.mOrientation == 1) {
            this.layoutVertical(left, top, right, bottom);
        } else {
            this.layoutHorizontal(left, top, right, bottom);
        }
    }

    void layoutVertical(int left, int top, int right, int bottom) {
        int paddingLeft = this.mPaddingLeft;
        int width = right - left;
        int childRight = width - this.mPaddingRight;
        int childSpace = width - paddingLeft - this.mPaddingRight;
        int count = this.getVirtualChildCount();
        int majorGravity = this.mGravity & 112;
        int minorGravity = this.mGravity & 8388615;
        int childTop = switch(majorGravity) {
            case 16 ->
                this.mPaddingTop + (bottom - top - this.mTotalLength) / 2;
            case 80 ->
                this.mPaddingTop + bottom - top - this.mTotalLength;
            default ->
                this.mPaddingTop;
        };
        for (int i = 0; i < count; i++) {
            View child = this.getVirtualChildAt(i);
            if (child == null) {
                childTop += this.measureNullChild(i);
            } else if (child.getVisibility() != 8) {
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) child.getLayoutParams();
                int gravity = lp.gravity;
                if (gravity < 0) {
                    gravity = minorGravity;
                }
                int layoutDirection = this.getLayoutDirection();
                int absoluteGravity = Gravity.getAbsoluteGravity(gravity, layoutDirection);
                int childLeft = switch(absoluteGravity & 7) {
                    case 1 ->
                        paddingLeft + (childSpace - childWidth) / 2 + lp.leftMargin - lp.rightMargin;
                    case 5 ->
                        childRight - childWidth - lp.rightMargin;
                    default ->
                        paddingLeft + lp.leftMargin;
                };
                if (this.hasDividerBeforeChildAt(i)) {
                    childTop += this.mDividerHeight;
                }
                childTop += lp.topMargin;
                this.setChildFrame(child, childLeft, childTop + this.getLocationOffset(child), childWidth, childHeight);
                childTop += childHeight + lp.bottomMargin + this.getNextLocationOffset(child);
                i += this.getChildrenSkipCount(child, i);
            }
        }
    }

    @Override
    public void onRtlPropertiesChanged(int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);
        if (layoutDirection != this.mLayoutDirection) {
            this.mLayoutDirection = layoutDirection;
            if (this.mOrientation == 0) {
                this.requestLayout();
            }
        }
    }

    void layoutHorizontal(int left, int top, int right, int bottom) {
        boolean isLayoutRtl = this.isLayoutRtl();
        int paddingTop = this.mPaddingTop;
        int height = bottom - top;
        int childBottom = height - this.mPaddingBottom;
        int childSpace = height - paddingTop - this.mPaddingBottom;
        int count = this.getVirtualChildCount();
        int majorGravity = this.mGravity & 8388615;
        int minorGravity = this.mGravity & 112;
        boolean baselineAligned = this.mBaselineAligned;
        int[] maxAscent = this.mMaxAscent;
        int[] maxDescent = this.mMaxDescent;
        int layoutDirection = this.getLayoutDirection();
        int childLeft = switch(Gravity.getAbsoluteGravity(majorGravity, layoutDirection)) {
            case 1 ->
                this.mPaddingLeft + (right - left - this.mTotalLength) / 2;
            case 5 ->
                this.mPaddingLeft + right - left - this.mTotalLength;
            default ->
                this.mPaddingLeft;
        };
        int start = 0;
        int dir = 1;
        if (isLayoutRtl) {
            start = count - 1;
            dir = -1;
        }
        for (int i = 0; i < count; i++) {
            int childIndex = start + dir * i;
            View child = this.getVirtualChildAt(childIndex);
            if (child == null) {
                childLeft += this.measureNullChild(childIndex);
            } else if (child.getVisibility() != 8) {
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                int childBaseline = -1;
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) child.getLayoutParams();
                if (baselineAligned && lp.height != -1) {
                    childBaseline = child.getBaseline();
                }
                int gravity = lp.gravity;
                if (gravity < 0) {
                    gravity = minorGravity;
                }
                int childTop = switch(gravity & 112) {
                    case 16 ->
                        paddingTop + (childSpace - childHeight) / 2 + lp.topMargin - lp.bottomMargin;
                    case 48 ->
                        {
                            ???;
                            if (childBaseline != -1) {
                                ???;
                            }
                        }
                    case 80 ->
                        {
                            ???;
                            if (childBaseline != -1) {
                                int descent = child.getMeasuredHeight() - childBaseline;
                                ???;
                            }
                        }
                    default ->
                        paddingTop;
                };
                if (this.hasDividerBeforeChildAt(childIndex)) {
                    childLeft += this.mDividerWidth;
                }
                childLeft += lp.leftMargin;
                this.setChildFrame(child, childLeft + this.getLocationOffset(child), childTop, childWidth, childHeight);
                childLeft += childWidth + lp.rightMargin + this.getNextLocationOffset(child);
                i += this.getChildrenSkipCount(child, childIndex);
            }
        }
    }

    private void setChildFrame(@Nonnull View child, int left, int top, int width, int height) {
        child.layout(left, top, left + width, top + height);
    }

    public void setOrientation(int orientation) {
        if (this.mOrientation != orientation) {
            this.mOrientation = orientation;
            this.requestLayout();
        }
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public void setGravity(int gravity) {
        if (this.mGravity != gravity) {
            if ((gravity & 8388615) == 0) {
                gravity |= 8388611;
            }
            if ((gravity & 112) == 0) {
                gravity |= 48;
            }
            this.mGravity = gravity;
            this.requestLayout();
        }
    }

    public int getGravity() {
        return this.mGravity;
    }

    public void setHorizontalGravity(int horizontalGravity) {
        int gravity = horizontalGravity & 8388615;
        if ((this.mGravity & 8388615) != gravity) {
            this.mGravity = this.mGravity & -8388616 | gravity;
            this.requestLayout();
        }
    }

    public void setVerticalGravity(int verticalGravity) {
        int gravity = verticalGravity & 112;
        if ((this.mGravity & 112) != gravity) {
            this.mGravity = this.mGravity & -113 | gravity;
            this.requestLayout();
        }
    }

    @Nonnull
    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(@Nonnull ViewGroup.LayoutParams params) {
        if (params instanceof LinearLayout.LayoutParams) {
            return new LinearLayout.LayoutParams((LinearLayout.LayoutParams) params);
        } else {
            return params instanceof ViewGroup.MarginLayoutParams ? new LinearLayout.LayoutParams((ViewGroup.MarginLayoutParams) params) : new LinearLayout.LayoutParams(params);
        }
    }

    @Nonnull
    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        if (this.mOrientation == 0) {
            return new LinearLayout.LayoutParams(-2, -2);
        } else if (this.mOrientation == 1) {
            return new LinearLayout.LayoutParams(-1, -2);
        } else {
            throw new IllegalStateException("Unexpected orientation: " + this.mOrientation);
        }
    }

    @Override
    protected boolean checkLayoutParams(@Nullable ViewGroup.LayoutParams params) {
        return params instanceof LinearLayout.LayoutParams;
    }

    @Retention(RetentionPolicy.SOURCE)
    @Internal
    public @interface DividerMode {
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {

        public float weight;

        public int gravity = -1;

        public LayoutParams(int width, int height) {
            super(width, height);
            this.weight = 0.0F;
        }

        public LayoutParams(int width, int height, float weight) {
            super(width, height);
            this.weight = weight;
        }

        public LayoutParams(@Nonnull ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(@Nonnull ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(@Nonnull LinearLayout.LayoutParams source) {
            super((ViewGroup.MarginLayoutParams) source);
            this.weight = source.weight;
            this.gravity = source.gravity;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @Internal
    public @interface OrientationMode {
    }
}