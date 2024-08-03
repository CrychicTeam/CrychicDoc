package icyllis.modernui.widget;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.MeasureSpec;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import java.util.ArrayList;

public class FrameLayout extends ViewGroup {

    private static final int DEFAULT_CHILD_GRAVITY = 8388659;

    boolean mMeasureAllChildren = false;

    private int mForegroundPaddingLeft = 0;

    private int mForegroundPaddingTop = 0;

    private int mForegroundPaddingRight = 0;

    private int mForegroundPaddingBottom = 0;

    private final ArrayList<View> mMatchParentChildren = new ArrayList(1);

    public FrameLayout(@NonNull Context context) {
        super(context);
    }

    @Override
    public void setForegroundGravity(int foregroundGravity) {
        if (this.getForegroundGravity() != foregroundGravity) {
            super.setForegroundGravity(foregroundGravity);
            Drawable foreground = this.getForeground();
            if (this.getForegroundGravity() == 119 && foreground != null) {
                Rect padding = new Rect();
                if (foreground.getPadding(padding)) {
                    this.mForegroundPaddingLeft = padding.left;
                    this.mForegroundPaddingTop = padding.top;
                    this.mForegroundPaddingRight = padding.right;
                    this.mForegroundPaddingBottom = padding.bottom;
                }
            } else {
                this.mForegroundPaddingLeft = 0;
                this.mForegroundPaddingTop = 0;
                this.mForegroundPaddingRight = 0;
                this.mForegroundPaddingBottom = 0;
            }
            this.requestLayout();
        }
    }

    int getPaddingLeftWithForeground() {
        return this.isForegroundInsidePadding() ? Math.max(this.mPaddingLeft, this.mForegroundPaddingLeft) : this.mPaddingLeft + this.mForegroundPaddingLeft;
    }

    int getPaddingRightWithForeground() {
        return this.isForegroundInsidePadding() ? Math.max(this.mPaddingRight, this.mForegroundPaddingRight) : this.mPaddingRight + this.mForegroundPaddingRight;
    }

    private int getPaddingTopWithForeground() {
        return this.isForegroundInsidePadding() ? Math.max(this.mPaddingTop, this.mForegroundPaddingTop) : this.mPaddingTop + this.mForegroundPaddingTop;
    }

    private int getPaddingBottomWithForeground() {
        return this.isForegroundInsidePadding() ? Math.max(this.mPaddingBottom, this.mForegroundPaddingBottom) : this.mPaddingBottom + this.mForegroundPaddingBottom;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = this.getChildCount();
        boolean measureMatchParentChildren = MeasureSpec.getMode(widthMeasureSpec) != 1073741824 || MeasureSpec.getMode(heightMeasureSpec) != 1073741824;
        this.mMatchParentChildren.clear();
        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;
        for (int i = 0; i < count; i++) {
            View child = this.getChildAt(i);
            if (this.mMeasureAllChildren || child.getVisibility() != 8) {
                this.measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) child.getLayoutParams();
                maxWidth = Math.max(maxWidth, child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
                childState = combineMeasuredStates(childState, child.getMeasuredState());
                if (measureMatchParentChildren && (lp.width == -1 || lp.height == -1)) {
                    this.mMatchParentChildren.add(child);
                }
            }
        }
        maxWidth += this.getPaddingLeftWithForeground() + this.getPaddingRightWithForeground();
        maxHeight += this.getPaddingTopWithForeground() + this.getPaddingBottomWithForeground();
        maxHeight = Math.max(maxHeight, this.getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, this.getSuggestedMinimumWidth());
        Drawable drawable = this.getForeground();
        if (drawable != null) {
            maxHeight = Math.max(maxHeight, drawable.getMinimumHeight());
            maxWidth = Math.max(maxWidth, drawable.getMinimumWidth());
        }
        this.setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState), resolveSizeAndState(maxHeight, heightMeasureSpec, childState << 16));
        count = this.mMatchParentChildren.size();
        if (count > 1) {
            for (int ix = 0; ix < count; ix++) {
                View child = (View) this.mMatchParentChildren.get(ix);
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                int childWidthMeasureSpec;
                if (lp.width == -1) {
                    int width = Math.max(0, this.getMeasuredWidth() - this.getPaddingLeftWithForeground() - this.getPaddingRightWithForeground() - lp.leftMargin - lp.rightMargin);
                    childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width, 1073741824);
                } else {
                    childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, this.getPaddingLeftWithForeground() + this.getPaddingRightWithForeground() + lp.leftMargin + lp.rightMargin, lp.width);
                }
                int childHeightMeasureSpec;
                if (lp.height == -1) {
                    int height = Math.max(0, this.getMeasuredHeight() - this.getPaddingTopWithForeground() - this.getPaddingBottomWithForeground() - lp.topMargin - lp.bottomMargin);
                    childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, 1073741824);
                } else {
                    childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, this.getPaddingTopWithForeground() + this.getPaddingBottomWithForeground() + lp.topMargin + lp.bottomMargin, lp.height);
                }
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        this.layoutChildren(left, top, right, bottom, false);
    }

    void layoutChildren(int left, int top, int right, int bottom, boolean forceLeftGravity) {
        int count = this.getChildCount();
        int parentLeft = this.getPaddingLeftWithForeground();
        int parentRight = right - left - this.getPaddingRightWithForeground();
        int parentTop = this.getPaddingTopWithForeground();
        int parentBottom = bottom - top - this.getPaddingBottomWithForeground();
        for (int i = 0; i < count; i++) {
            View child = this.getChildAt(i);
            if (child.getVisibility() != 8) {
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) child.getLayoutParams();
                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();
                int gravity = lp.gravity;
                if (gravity == -1) {
                    gravity = 8388659;
                }
                int layoutDirection = this.getLayoutDirection();
                int absoluteGravity = Gravity.getAbsoluteGravity(gravity, layoutDirection);
                int verticalGravity = gravity & 112;
                int childLeft;
                switch(absoluteGravity & 7) {
                    case 1:
                        childLeft = parentLeft + (parentRight - parentLeft - width) / 2 + lp.leftMargin - lp.rightMargin;
                        break;
                    case 5:
                        if (!forceLeftGravity) {
                            childLeft = parentRight - width - lp.rightMargin;
                            break;
                        }
                    case 2:
                    case 3:
                    case 4:
                    default:
                        childLeft = parentLeft + lp.leftMargin;
                }
                int childTop = switch(verticalGravity) {
                    case 16 ->
                        parentTop + (parentBottom - parentTop - height) / 2 + lp.topMargin - lp.bottomMargin;
                    case 80 ->
                        parentBottom - height - lp.bottomMargin;
                    default ->
                        parentTop + lp.topMargin;
                };
                child.layout(childLeft, childTop, childLeft + width, childTop + height);
            }
        }
    }

    public void setMeasureAllChildren(boolean measureAll) {
        this.mMeasureAllChildren = measureAll;
    }

    public boolean getMeasureAllChildren() {
        return this.mMeasureAllChildren;
    }

    @NonNull
    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(@NonNull ViewGroup.LayoutParams params) {
        if (params instanceof FrameLayout.LayoutParams) {
            return new FrameLayout.LayoutParams((FrameLayout.LayoutParams) params);
        } else {
            return params instanceof ViewGroup.MarginLayoutParams ? new FrameLayout.LayoutParams((ViewGroup.MarginLayoutParams) params) : new FrameLayout.LayoutParams(params);
        }
    }

    @NonNull
    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new FrameLayout.LayoutParams(-1, -1);
    }

    @Override
    protected boolean checkLayoutParams(@Nullable ViewGroup.LayoutParams params) {
        return params instanceof FrameLayout.LayoutParams;
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {

        public static final int UNSPECIFIED_GRAVITY = -1;

        public int gravity = -1;

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(int width, int height, int gravity) {
            super(width, height);
            this.gravity = gravity;
        }

        public LayoutParams(@NonNull ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(@NonNull ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(@NonNull FrameLayout.LayoutParams source) {
            super((ViewGroup.MarginLayoutParams) source);
            this.gravity = source.gravity;
        }
    }
}