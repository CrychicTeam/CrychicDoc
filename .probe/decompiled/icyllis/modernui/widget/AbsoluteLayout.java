package icyllis.modernui.widget;

import icyllis.modernui.core.Context;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AbsoluteLayout extends ViewGroup {

    public AbsoluteLayout(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = this.getChildCount();
        int maxHeight = 0;
        int maxWidth = 0;
        this.measureChildren(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < count; i++) {
            View child = this.getChildAt(i);
            if (child.getVisibility() != 8) {
                AbsoluteLayout.LayoutParams lp = (AbsoluteLayout.LayoutParams) child.getLayoutParams();
                int childRight = lp.x + child.getMeasuredWidth();
                int childBottom = lp.y + child.getMeasuredHeight();
                maxWidth = Math.max(maxWidth, childRight);
                maxHeight = Math.max(maxHeight, childBottom);
            }
        }
        maxWidth += this.mPaddingLeft + this.mPaddingRight;
        maxHeight += this.mPaddingTop + this.mPaddingBottom;
        maxHeight = Math.max(maxHeight, this.getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, this.getSuggestedMinimumWidth());
        this.setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, 0), resolveSizeAndState(maxHeight, heightMeasureSpec, 0));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int count = this.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = this.getChildAt(i);
            if (child.getVisibility() != 8) {
                AbsoluteLayout.LayoutParams lp = (AbsoluteLayout.LayoutParams) child.getLayoutParams();
                int childLeft = this.mPaddingLeft + lp.x;
                int childTop = this.mPaddingTop + lp.y;
                child.layout(childLeft, childTop, childLeft + child.getMeasuredWidth(), childTop + child.getMeasuredHeight());
            }
        }
    }

    @Nonnull
    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(@Nonnull ViewGroup.LayoutParams params) {
        return params instanceof AbsoluteLayout.LayoutParams ? new AbsoluteLayout.LayoutParams((AbsoluteLayout.LayoutParams) params) : new AbsoluteLayout.LayoutParams(params);
    }

    @Nonnull
    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new AbsoluteLayout.LayoutParams(-2, -2, 0, 0);
    }

    @Override
    protected boolean checkLayoutParams(@Nullable ViewGroup.LayoutParams params) {
        return params instanceof AbsoluteLayout.LayoutParams;
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {

        public int x;

        public int y;

        public LayoutParams(int width, int height, int x, int y) {
            super(width, height);
            this.x = x;
            this.y = y;
        }

        public LayoutParams(@Nonnull ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(@Nonnull AbsoluteLayout.LayoutParams source) {
            super(source);
            this.x = source.x;
            this.y = source.y;
        }
    }
}