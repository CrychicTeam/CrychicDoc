package icyllis.modernui.view;

import icyllis.modernui.animation.LayoutTransition;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.Rect;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public final class WindowGroup extends ViewGroup implements WindowManager {

    public WindowGroup(@NonNull Context context) {
        super(context);
        this.setDescendantFocusability(262144);
        this.setLayoutTransition(new LayoutTransition());
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
        if (this.mFocused != null) {
            WindowManager.LayoutParams attrs = (WindowManager.LayoutParams) this.mFocused.getLayoutParams();
            if (attrs.isModal()) {
                return this.dispatchTransformedTouchEvent(ev, this.mFocused, false);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected boolean dispatchHoverEvent(@NonNull MotionEvent event) {
        if (this.mFocused != null) {
            WindowManager.LayoutParams attrs = (WindowManager.LayoutParams) this.mFocused.getLayoutParams();
            if (attrs.isModal()) {
                return this.dispatchTransformedGenericPointerEvent(event, this.mFocused);
            }
        }
        return super.dispatchHoverEvent(event);
    }

    @Override
    protected boolean dispatchGenericPointerEvent(@NonNull MotionEvent event) {
        if (this.mFocused != null) {
            WindowManager.LayoutParams attrs = (WindowManager.LayoutParams) this.mFocused.getLayoutParams();
            if (attrs.isModal()) {
                return this.dispatchTransformedGenericPointerEvent(event, this.mFocused);
            }
        }
        return super.dispatchGenericPointerEvent(event);
    }

    @Override
    boolean dispatchTooltipHoverEvent(@NonNull MotionEvent event) {
        if (this.mFocused != null) {
            WindowManager.LayoutParams attrs = (WindowManager.LayoutParams) this.mFocused.getLayoutParams();
            if (attrs.isModal()) {
                return this.dispatchTransformedTooltipHoverEvent(event, this.mFocused);
            }
        }
        return super.dispatchTooltipHoverEvent(event);
    }

    @Override
    public PointerIcon onResolvePointerIcon(@NonNull MotionEvent event) {
        if (this.mFocused != null) {
            WindowManager.LayoutParams attrs = (WindowManager.LayoutParams) this.mFocused.getLayoutParams();
            if (attrs.isModal()) {
                return this.dispatchResolvePointerIcon(event, this.mFocused);
            }
        }
        return super.onResolvePointerIcon(event);
    }

    @Override
    public void addView(@NonNull View child, int index, @NonNull ViewGroup.LayoutParams params) {
        if (!(params instanceof WindowManager.LayoutParams lhs)) {
            throw new IllegalArgumentException("Params must be WindowManager.LayoutParams");
        } else {
            int count = this.getChildCount();
            for (int i = 0; i < count; i++) {
                WindowManager.LayoutParams rhs = (WindowManager.LayoutParams) this.getChildAt(i).getLayoutParams();
                if (lhs.type < rhs.type) {
                    index = i;
                    break;
                }
            }
            super.addView(child, index, params);
        }
    }

    @Override
    protected void onViewAdded(View child) {
        super.onViewAdded(child);
        WindowManager.LayoutParams attrs = (WindowManager.LayoutParams) child.getLayoutParams();
        if ((attrs.flags & 8) == 0) {
            this.requestChildFocus(child, child);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = this.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = this.getChildAt(i);
            if (child.getVisibility() != 8) {
                WindowManager.LayoutParams attrs = (WindowManager.LayoutParams) child.getLayoutParams();
                int childWidthSpec = getChildMeasureSpec(widthMeasureSpec, 0, attrs.width);
                int childHeightSpec = getChildMeasureSpec(heightMeasureSpec, 0, attrs.height);
                child.measure(childWidthSpec, childHeightSpec);
            }
        }
        int windowWidth = MeasureSpec.getSize(widthMeasureSpec);
        int windowHeight = MeasureSpec.getSize(heightMeasureSpec);
        this.setMeasuredDimension(windowWidth, windowHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int count = this.getChildCount();
        Rect outParentFrame = new Rect(left, top, right, bottom);
        Rect outFrame = new Rect();
        for (int i = 0; i < count; i++) {
            View child = this.getChildAt(i);
            if (child.getVisibility() != 8) {
                WindowManager.LayoutParams attrs = (WindowManager.LayoutParams) child.getLayoutParams();
                int pw = outParentFrame.width();
                int ph = outParentFrame.height();
                int w = child.getMeasuredWidth();
                int h = child.getMeasuredHeight();
                Gravity.apply(attrs.gravity, w, h, outParentFrame, (int) ((float) attrs.x + attrs.horizontalMargin * (float) pw), (int) ((float) attrs.y + attrs.verticalMargin * (float) ph), outFrame);
                Gravity.applyDisplay(attrs.gravity, outParentFrame, outFrame);
                child.layout(outFrame.left, outFrame.top, outFrame.right, outFrame.bottom);
            }
        }
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        if (this.getDescendantFocusability() != 393216) {
            this.mFocused = null;
            super.unFocus(focused);
            this.mFocused = child;
            if (this.mParent != null) {
                this.mParent.requestChildFocus(this, focused);
            }
        }
    }

    @Override
    public void clearChildFocus(View child) {
        super.clearChildFocus(child);
        int count = this.getChildCount();
        for (int i = count - 1; i >= 0; i--) {
            View c = this.getChildAt(i);
            if (c.hasFocus()) {
                this.mFocused = c;
                break;
            }
        }
    }

    @Override
    public void clearFocus() {
    }
}