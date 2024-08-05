package icyllis.modernui.widget;

import icyllis.modernui.annotation.ColorInt;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.MathUtil;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.drawable.ShapeDrawable;
import icyllis.modernui.resources.SystemTheme;
import icyllis.modernui.view.FocusFinder;
import icyllis.modernui.view.KeyEvent;
import icyllis.modernui.view.MeasureSpec;
import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.view.VelocityTracker;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewConfiguration;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.view.ViewParent;
import java.util.List;

public class HorizontalScrollView extends FrameLayout {

    private static final float FLING_DESTRETCH_FACTOR = 4.0F;

    private final Rect mTempRect = new Rect();

    private final OverScroller mScroller;

    private final EdgeEffect mEdgeGlowLeft;

    private final EdgeEffect mEdgeGlowRight;

    private int mLastMotionX;

    private boolean mIsLayoutDirty = true;

    private View mChildToScrollTo = null;

    private boolean mIsBeingDragged = false;

    private VelocityTracker mVelocityTracker;

    private boolean mFillViewport;

    private boolean mSmoothScrollingEnabled = true;

    private final int mTouchSlop;

    private final int mMinimumVelocity;

    private final int mMaximumVelocity;

    private final int mOverscrollDistance;

    private final int mOverflingDistance;

    private final float mHorizontalScrollFactor;

    private int mActivePointerId = -1;

    public HorizontalScrollView(Context context) {
        super(context);
        this.mScroller = new OverScroller();
        this.mEdgeGlowLeft = new EdgeEffect();
        this.mEdgeGlowRight = new EdgeEffect();
        this.setFocusable(true);
        this.setDescendantFocusability(262144);
        this.setWillNotDraw(false);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        this.mTouchSlop = configuration.getScaledTouchSlop();
        this.mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        this.mOverscrollDistance = configuration.getScaledOverscrollDistance();
        this.mOverflingDistance = configuration.getScaledOverflingDistance();
        this.mHorizontalScrollFactor = configuration.getScaledHorizontalScrollFactor();
        this.setHorizontalScrollBarEnabled(true);
        ShapeDrawable thumb = new ShapeDrawable();
        thumb.setShape(3);
        thumb.setStroke(this.dp(4.0F), SystemTheme.modulateColor(-1, 0.25F));
        thumb.setCornerRadius(1.0F);
        this.setHorizontalScrollbarThumbDrawable(thumb);
        ShapeDrawable track = new ShapeDrawable();
        track.setShape(3);
        track.setStroke(this.dp(4.0F), 1082163328);
        track.setSize(this.dp(4.0F), -1);
        track.setCornerRadius(1.0F);
        this.setHorizontalScrollbarTrackDrawable(track);
    }

    public void setEdgeEffectColor(@ColorInt int color) {
        this.setLeftEdgeEffectColor(color);
        this.setRightEdgeEffectColor(color);
    }

    public void setRightEdgeEffectColor(@ColorInt int color) {
        this.mEdgeGlowRight.setColor(color);
    }

    public void setLeftEdgeEffectColor(@ColorInt int color) {
        this.mEdgeGlowLeft.setColor(color);
    }

    @ColorInt
    public int getLeftEdgeEffectColor() {
        return this.mEdgeGlowLeft.getColor();
    }

    @ColorInt
    public int getRightEdgeEffectColor() {
        return this.mEdgeGlowRight.getColor();
    }

    @Override
    public void addView(@NonNull View child) {
        if (this.getChildCount() > 0) {
            throw new IllegalStateException("HorizontalScrollView can host only one direct child");
        } else {
            super.addView(child);
        }
    }

    @Override
    public void addView(@NonNull View child, int index) {
        if (this.getChildCount() > 0) {
            throw new IllegalStateException("HorizontalScrollView can host only one direct child");
        } else {
            super.addView(child, index);
        }
    }

    @Override
    public void addView(@NonNull View child, @NonNull ViewGroup.LayoutParams params) {
        if (this.getChildCount() > 0) {
            throw new IllegalStateException("HorizontalScrollView can host only one direct child");
        } else {
            super.addView(child, params);
        }
    }

    @Override
    public void addView(@NonNull View child, int index, @NonNull ViewGroup.LayoutParams params) {
        if (this.getChildCount() > 0) {
            throw new IllegalStateException("HorizontalScrollView can host only one direct child");
        } else {
            super.addView(child, index, params);
        }
    }

    private boolean canScroll() {
        View child = this.getChildAt(0);
        if (child != null) {
            int childWidth = child.getWidth();
            return this.getWidth() < childWidth + this.mPaddingLeft + this.mPaddingRight;
        } else {
            return false;
        }
    }

    public boolean isFillViewport() {
        return this.mFillViewport;
    }

    public void setFillViewport(boolean fillViewport) {
        if (fillViewport != this.mFillViewport) {
            this.mFillViewport = fillViewport;
            this.requestLayout();
        }
    }

    public boolean isSmoothScrollingEnabled() {
        return this.mSmoothScrollingEnabled;
    }

    public void setSmoothScrollingEnabled(boolean smoothScrollingEnabled) {
        this.mSmoothScrollingEnabled = smoothScrollingEnabled;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.mFillViewport) {
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            if (widthMode != 0) {
                if (this.getChildCount() > 0) {
                    View child = this.getChildAt(0);
                    FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) child.getLayoutParams();
                    int widthPadding = this.mPaddingLeft + this.mPaddingRight + lp.leftMargin + lp.rightMargin;
                    int heightPadding = this.mPaddingTop + this.mPaddingBottom + lp.topMargin + lp.bottomMargin;
                    int desiredWidth = this.getMeasuredWidth() - widthPadding;
                    if (child.getMeasuredWidth() < desiredWidth) {
                        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(desiredWidth, 1073741824);
                        int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, heightPadding, lp.height);
                        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
                    }
                }
            }
        }
    }

    @Override
    public boolean dispatchKeyEvent(@NonNull KeyEvent event) {
        return super.dispatchKeyEvent(event) || this.executeKeyEvent(event);
    }

    public boolean executeKeyEvent(KeyEvent event) {
        this.mTempRect.setEmpty();
        if (this.canScroll()) {
            boolean handled = false;
            if (event.getAction() == 0) {
                switch(event.getKeyCode()) {
                    case 32:
                        handled = this.pageScroll(event.isShiftPressed() ? 17 : 66);
                        break;
                    case 262:
                        if (!event.isAltPressed()) {
                            handled = this.arrowScroll(66);
                        } else {
                            handled = this.fullScroll(66);
                        }
                        break;
                    case 263:
                        if (!event.isAltPressed()) {
                            handled = this.arrowScroll(17);
                        } else {
                            handled = this.fullScroll(17);
                        }
                }
            }
            return handled;
        } else if (this.isFocused()) {
            View currentFocused = this.findFocus();
            if (currentFocused == this) {
                currentFocused = null;
            }
            View nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused, 66);
            return nextFocused != null && nextFocused != this && nextFocused.requestFocus(66);
        } else {
            return false;
        }
    }

    private boolean inChild(int x, int y) {
        if (this.getChildCount() <= 0) {
            return false;
        } else {
            int scrollX = this.mScrollX;
            View child = this.getChildAt(0);
            return y >= child.getTop() && y < child.getBottom() && x >= child.getLeft() - scrollX && x < child.getRight() - scrollX;
        }
    }

    private void initOrResetVelocityTracker() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        } else {
            this.mVelocityTracker.clear();
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        if (disallowIntercept) {
            this.recycleVelocityTracker();
        }
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == 2 && this.mIsBeingDragged) {
            return true;
        } else if (super.onInterceptTouchEvent(ev)) {
            return true;
        } else {
            switch(action) {
                case 0:
                    int x = (int) ev.getX();
                    if (!this.inChild(x, (int) ev.getY())) {
                        this.mIsBeingDragged = false;
                        this.recycleVelocityTracker();
                    } else {
                        this.mLastMotionX = x;
                        this.mActivePointerId = ev.getPointerId(0);
                        this.initOrResetVelocityTracker();
                        this.mVelocityTracker.addMovement(ev);
                        this.mIsBeingDragged = !this.mScroller.isFinished();
                        if (!this.mEdgeGlowLeft.isFinished()) {
                            this.mEdgeGlowLeft.onPullDistance(0.0F, 1.0F - ev.getY() / (float) this.getHeight());
                        }
                        if (!this.mEdgeGlowRight.isFinished()) {
                            this.mEdgeGlowRight.onPullDistance(0.0F, ev.getY() / (float) this.getHeight());
                        }
                    }
                    break;
                case 1:
                case 3:
                    this.mIsBeingDragged = false;
                    this.mActivePointerId = -1;
                    if (this.mScroller.springBack(this.mScrollX, this.mScrollY, 0, this.getScrollRange(), 0, 0)) {
                        this.postInvalidateOnAnimation();
                    }
                    break;
                case 2:
                    int activePointerId = this.mActivePointerId;
                    if (activePointerId != -1) {
                        int xx = (int) ev.getX();
                        int xDiff = Math.abs(xx - this.mLastMotionX);
                        if (xDiff > this.mTouchSlop) {
                            this.mIsBeingDragged = true;
                            this.mLastMotionX = xx;
                            this.initVelocityTrackerIfNotExists();
                            this.mVelocityTracker.addMovement(ev);
                            ViewParent parent = this.getParent();
                            if (parent != null) {
                                parent.requestDisallowInterceptTouchEvent(true);
                            }
                        }
                    }
            }
            return this.mIsBeingDragged;
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {
        this.initVelocityTrackerIfNotExists();
        this.mVelocityTracker.addMovement(ev);
        int action = ev.getAction();
        switch(action & 0xFF) {
            case 0:
                if (this.getChildCount() == 0) {
                    return false;
                }
                if (!this.mScroller.isFinished()) {
                    ViewParent parent = this.getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }
                if (!this.mScroller.isFinished()) {
                    this.mScroller.abortAnimation();
                }
                this.mLastMotionX = (int) ev.getX();
                this.mActivePointerId = ev.getPointerId(0);
                break;
            case 1:
                if (this.mIsBeingDragged) {
                    VelocityTracker velocityTracker = this.mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                    int initialVelocity = (int) velocityTracker.getXVelocity();
                    if (this.getChildCount() > 0) {
                        if (Math.abs(initialVelocity) > this.mMinimumVelocity) {
                            this.fling(-initialVelocity);
                        } else if (this.mScroller.springBack(this.mScrollX, this.mScrollY, 0, this.getScrollRange(), 0, 0)) {
                            this.postInvalidateOnAnimation();
                        }
                    }
                    this.mActivePointerId = -1;
                    this.mIsBeingDragged = false;
                    this.recycleVelocityTracker();
                    if (this.shouldDisplayEdgeEffects()) {
                        this.mEdgeGlowLeft.onRelease();
                        this.mEdgeGlowRight.onRelease();
                    }
                }
                break;
            case 2:
                if (this.mActivePointerId != -1) {
                    int x = (int) ev.getX();
                    int deltaX = this.mLastMotionX - x;
                    if (!this.mIsBeingDragged && Math.abs(deltaX) > this.mTouchSlop) {
                        ViewParent parent = this.getParent();
                        if (parent != null) {
                            parent.requestDisallowInterceptTouchEvent(true);
                        }
                        this.mIsBeingDragged = true;
                        if (deltaX > 0) {
                            deltaX -= this.mTouchSlop;
                        } else {
                            deltaX += this.mTouchSlop;
                        }
                    }
                    if (this.mIsBeingDragged) {
                        this.mLastMotionX = x;
                        int oldX = this.mScrollX;
                        int range = this.getScrollRange();
                        int overscrollMode = this.getOverScrollMode();
                        boolean canOverscroll = overscrollMode == 0 || overscrollMode == 1 && range > 0;
                        float displacement = ev.getY() / (float) this.getHeight();
                        if (canOverscroll) {
                            int consumed = 0;
                            if (deltaX < 0 && this.mEdgeGlowRight.getDistance() != 0.0F) {
                                consumed = Math.round((float) this.getWidth() * this.mEdgeGlowRight.onPullDistance((float) deltaX / (float) this.getWidth(), displacement));
                            } else if (deltaX > 0 && this.mEdgeGlowLeft.getDistance() != 0.0F) {
                                consumed = Math.round((float) (-this.getWidth()) * this.mEdgeGlowLeft.onPullDistance((float) (-deltaX) / (float) this.getWidth(), 1.0F - displacement));
                            }
                            deltaX -= consumed;
                        }
                        this.overScrollBy(deltaX, 0, this.mScrollX, 0, range, 0, this.mOverscrollDistance, 0, true);
                        if (canOverscroll && (float) deltaX != 0.0F) {
                            int pulledToX = oldX + deltaX;
                            if (pulledToX < 0) {
                                this.mEdgeGlowLeft.onPullDistance((float) (-deltaX) / (float) this.getWidth(), 1.0F - displacement);
                                if (!this.mEdgeGlowRight.isFinished()) {
                                    this.mEdgeGlowRight.onRelease();
                                }
                            } else if (pulledToX > range) {
                                this.mEdgeGlowRight.onPullDistance((float) deltaX / (float) this.getWidth(), displacement);
                                if (!this.mEdgeGlowLeft.isFinished()) {
                                    this.mEdgeGlowLeft.onRelease();
                                }
                            }
                            if (this.shouldDisplayEdgeEffects() && (!this.mEdgeGlowLeft.isFinished() || !this.mEdgeGlowRight.isFinished())) {
                                this.postInvalidateOnAnimation();
                            }
                        }
                    }
                }
                break;
            case 3:
                if (this.mIsBeingDragged && this.getChildCount() > 0) {
                    if (this.mScroller.springBack(this.mScrollX, this.mScrollY, 0, this.getScrollRange(), 0, 0)) {
                        this.postInvalidateOnAnimation();
                    }
                    this.mActivePointerId = -1;
                    this.mIsBeingDragged = false;
                    this.recycleVelocityTracker();
                    if (this.shouldDisplayEdgeEffects()) {
                        this.mEdgeGlowLeft.onRelease();
                        this.mEdgeGlowRight.onRelease();
                    }
                }
        }
        return true;
    }

    @Override
    public boolean onGenericMotionEvent(@NonNull MotionEvent event) {
        if (event.getAction() == 8 && !this.mIsBeingDragged) {
            float axisValue;
            if (event.isShiftPressed()) {
                axisValue = event.getAxisValue(9);
            } else {
                axisValue = event.getAxisValue(10);
            }
            int delta = Math.round(axisValue * this.mHorizontalScrollFactor);
            if ((double) Math.abs(axisValue) > 0.9 && Math.abs(delta) * 6 > this.mMinimumVelocity) {
                int deltaX = MathUtil.clamp(delta * 6, -this.mMaximumVelocity, this.mMaximumVelocity);
                this.fling(-deltaX);
                return true;
            }
            if (this.smoothScrollBy(-delta)) {
                return true;
            }
        }
        return super.onGenericMotionEvent(event);
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return true;
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        if (!this.mScroller.isFinished()) {
            int oldX = this.mScrollX;
            int oldY = this.mScrollY;
            this.mScrollX = scrollX;
            this.mScrollY = scrollY;
            this.onScrollChanged(this.mScrollX, this.mScrollY, oldX, oldY);
            if (clampedX) {
                this.mScroller.springBack(this.mScrollX, this.mScrollY, 0, this.getScrollRange(), 0, 0);
            }
        } else {
            super.scrollTo(scrollX, scrollY);
        }
        this.awakenScrollBars();
    }

    private int getScrollRange() {
        int scrollRange = 0;
        if (this.getChildCount() > 0) {
            View child = this.getChildAt(0);
            scrollRange = Math.max(0, child.getWidth() - (this.getWidth() - this.mPaddingLeft - this.mPaddingRight));
        }
        return scrollRange;
    }

    private View findFocusableViewInMyBounds(boolean leftFocus, int left, View preferredFocusable) {
        int fadingEdgeLength = 0;
        int leftWithoutFadingEdge = left + 0;
        int rightWithoutFadingEdge = left + this.getWidth() - 0;
        return preferredFocusable != null && preferredFocusable.getLeft() < rightWithoutFadingEdge && preferredFocusable.getRight() > leftWithoutFadingEdge ? preferredFocusable : this.findFocusableViewInBounds(leftFocus, leftWithoutFadingEdge, rightWithoutFadingEdge);
    }

    private View findFocusableViewInBounds(boolean leftFocus, int left, int right) {
        List<View> focusables = this.getFocusables(2);
        View focusCandidate = null;
        boolean foundFullyContainedFocusable = false;
        for (View view : focusables) {
            int viewLeft = view.getLeft();
            int viewRight = view.getRight();
            if (left < viewRight && viewLeft < right) {
                boolean viewIsFullyContained = left < viewLeft && viewRight < right;
                if (focusCandidate == null) {
                    focusCandidate = view;
                    foundFullyContainedFocusable = viewIsFullyContained;
                } else {
                    boolean viewIsCloserToBoundary = leftFocus && viewLeft < focusCandidate.getLeft() || !leftFocus && viewRight > focusCandidate.getRight();
                    if (foundFullyContainedFocusable) {
                        if (viewIsFullyContained && viewIsCloserToBoundary) {
                            focusCandidate = view;
                        }
                    } else if (viewIsFullyContained) {
                        focusCandidate = view;
                        foundFullyContainedFocusable = true;
                    } else if (viewIsCloserToBoundary) {
                        focusCandidate = view;
                    }
                }
            }
        }
        return focusCandidate;
    }

    public boolean pageScroll(int direction) {
        boolean right = direction == 66;
        int width = this.getWidth();
        if (right) {
            this.mTempRect.left = this.getScrollX() + width;
            int count = this.getChildCount();
            if (count > 0) {
                View view = this.getChildAt(0);
                if (this.mTempRect.left + width > view.getRight()) {
                    this.mTempRect.left = view.getRight() - width;
                }
            }
        } else {
            this.mTempRect.left = this.getScrollX() - width;
            if (this.mTempRect.left < 0) {
                this.mTempRect.left = 0;
            }
        }
        this.mTempRect.right = this.mTempRect.left + width;
        return this.scrollAndFocus(direction, this.mTempRect.left, this.mTempRect.right);
    }

    public boolean fullScroll(int direction) {
        boolean right = direction == 66;
        int width = this.getWidth();
        this.mTempRect.left = 0;
        this.mTempRect.right = width;
        if (right) {
            int count = this.getChildCount();
            if (count > 0) {
                View view = this.getChildAt(0);
                this.mTempRect.right = view.getRight();
                this.mTempRect.left = this.mTempRect.right - width;
            }
        }
        return this.scrollAndFocus(direction, this.mTempRect.left, this.mTempRect.right);
    }

    private boolean scrollAndFocus(int direction, int left, int right) {
        boolean handled = true;
        int width = this.getWidth();
        int containerLeft = this.getScrollX();
        int containerRight = containerLeft + width;
        boolean goLeft = direction == 17;
        View newFocused = this.findFocusableViewInBounds(goLeft, left, right);
        if (newFocused == null) {
            newFocused = this;
        }
        if (left >= containerLeft && right <= containerRight) {
            handled = false;
        } else {
            int delta = goLeft ? left - containerLeft : right - containerRight;
            this.doScrollX(delta);
        }
        if (newFocused != this.findFocus()) {
            newFocused.requestFocus(direction);
        }
        return handled;
    }

    public boolean arrowScroll(int direction) {
        View currentFocused = this.findFocus();
        if (currentFocused == this) {
            currentFocused = null;
        }
        View nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused, direction);
        int maxJump = (int) ((float) this.getWidth() * 0.5F);
        if (nextFocused != null && this.isWithinDeltaOfScreen(nextFocused, maxJump)) {
            nextFocused.getDrawingRect(this.mTempRect);
            this.offsetDescendantRectToMyCoords(nextFocused, this.mTempRect);
            int scrollDelta = this.computeScrollDeltaToGetChildRectOnScreen(this.mTempRect);
            this.doScrollX(scrollDelta);
            nextFocused.requestFocus(direction);
        } else {
            int scrollDelta = maxJump;
            if (direction == 17 && this.getScrollX() < maxJump) {
                scrollDelta = this.getScrollX();
            } else if (direction == 66 && this.getChildCount() > 0) {
                int daRight = this.getChildAt(0).getRight();
                int screenRight = this.getScrollX() + this.getWidth();
                if (daRight - screenRight < maxJump) {
                    scrollDelta = daRight - screenRight;
                }
            }
            if (scrollDelta == 0) {
                return false;
            }
            this.doScrollX(direction == 66 ? scrollDelta : -scrollDelta);
        }
        if (currentFocused != null && currentFocused.isFocused() && this.isOffScreen(currentFocused)) {
            int descendantFocusability = this.getDescendantFocusability();
            this.setDescendantFocusability(131072);
            this.requestFocus();
            this.setDescendantFocusability(descendantFocusability);
        }
        return true;
    }

    private boolean isOffScreen(View descendant) {
        return !this.isWithinDeltaOfScreen(descendant, 0);
    }

    private boolean isWithinDeltaOfScreen(View descendant, int delta) {
        descendant.getDrawingRect(this.mTempRect);
        this.offsetDescendantRectToMyCoords(descendant, this.mTempRect);
        return this.mTempRect.right + delta >= this.getScrollX() && this.mTempRect.left - delta <= this.getScrollX() + this.getWidth();
    }

    private void doScrollX(int delta) {
        if (delta != 0) {
            if (this.mSmoothScrollingEnabled) {
                this.smoothScrollBy(delta);
            } else {
                this.scrollBy(delta, 0);
            }
        }
    }

    public final boolean smoothScrollBy(int delta) {
        if (this.getChildCount() == 0) {
            return false;
        } else {
            int scrollX = this.mScrollX;
            delta = Math.max(0, Math.min(scrollX + delta, this.getScrollRange())) - scrollX;
            if (delta != 0) {
                this.mScroller.startScroll(scrollX, this.mScrollY, delta, 0);
                this.postInvalidateOnAnimation();
                return true;
            } else {
                return false;
            }
        }
    }

    public final void smoothScrollTo(int x) {
        this.smoothScrollBy(x - this.mScrollX);
    }

    @Override
    protected int computeHorizontalScrollRange() {
        int count = this.getChildCount();
        int contentWidth = this.getWidth() - this.mPaddingLeft - this.mPaddingRight;
        if (count == 0) {
            return contentWidth;
        } else {
            int scrollRange = this.getChildAt(0).getRight();
            int scrollX = this.mScrollX;
            int overscrollRight = Math.max(0, scrollRange - contentWidth);
            if (scrollX < 0) {
                scrollRange -= scrollX;
            } else if (scrollX > overscrollRight) {
                scrollRange += scrollX - overscrollRight;
            }
            return scrollRange;
        }
    }

    @Override
    protected int computeHorizontalScrollOffset() {
        return Math.max(0, super.computeHorizontalScrollOffset());
    }

    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        ViewGroup.LayoutParams lp = child.getLayoutParams();
        int horizontalPadding = this.mPaddingLeft + this.mPaddingRight;
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(Math.max(0, MeasureSpec.getSize(parentWidthMeasureSpec) - horizontalPadding), 0);
        int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec, this.mPaddingTop + this.mPaddingBottom, lp.height);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override
    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
        int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec, this.mPaddingTop + this.mPaddingBottom + lp.topMargin + lp.bottomMargin + heightUsed, lp.height);
        int usedTotal = this.mPaddingLeft + this.mPaddingRight + lp.leftMargin + lp.rightMargin + widthUsed;
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(Math.max(0, MeasureSpec.getSize(parentWidthMeasureSpec) - usedTotal), 0);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override
    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            int oldX = this.mScrollX;
            int oldY = this.mScrollY;
            int x = this.mScroller.getCurrX();
            int y = this.mScroller.getCurrY();
            int deltaX = this.consumeFlingInStretch(x - oldX);
            if (deltaX != 0 || oldY != y) {
                int range = this.getScrollRange();
                int overscrollMode = this.getOverScrollMode();
                boolean canOverscroll = overscrollMode == 0 || overscrollMode == 1 && range > 0;
                this.overScrollBy(deltaX, y - oldY, oldX, oldY, range, 0, this.mOverflingDistance, 0, false);
                this.onScrollChanged(this.mScrollX, this.mScrollY, oldX, oldY);
                if (canOverscroll && deltaX != 0) {
                    if (x < 0 && oldX >= 0) {
                        this.mEdgeGlowLeft.onAbsorb((int) this.mScroller.getCurrVelocity());
                    } else if (x > range && oldX <= range) {
                        this.mEdgeGlowRight.onAbsorb((int) this.mScroller.getCurrVelocity());
                    }
                }
            }
            if (!this.awakenScrollBars()) {
                this.postInvalidateOnAnimation();
            }
        }
    }

    private int consumeFlingInStretch(int unconsumed) {
        if (unconsumed > 0 && this.mEdgeGlowLeft != null && this.mEdgeGlowLeft.getDistance() != 0.0F) {
            int size = this.getWidth();
            float deltaDistance = (float) (-unconsumed) * 4.0F / (float) size;
            int consumed = Math.round((float) (-size) / 4.0F * this.mEdgeGlowLeft.onPullDistance(deltaDistance, 0.5F));
            if (consumed != unconsumed) {
                this.mEdgeGlowLeft.finish();
            }
            return unconsumed - consumed;
        } else if (unconsumed < 0 && this.mEdgeGlowRight != null && this.mEdgeGlowRight.getDistance() != 0.0F) {
            int size = this.getWidth();
            float deltaDistance = (float) unconsumed * 4.0F / (float) size;
            int consumed = Math.round((float) size / 4.0F * this.mEdgeGlowRight.onPullDistance(deltaDistance, 0.5F));
            if (consumed != unconsumed) {
                this.mEdgeGlowRight.finish();
            }
            return unconsumed - consumed;
        } else {
            return unconsumed;
        }
    }

    private void scrollToChild(View child) {
        child.getDrawingRect(this.mTempRect);
        this.offsetDescendantRectToMyCoords(child, this.mTempRect);
        int scrollDelta = this.computeScrollDeltaToGetChildRectOnScreen(this.mTempRect);
        if (scrollDelta != 0) {
            this.scrollBy(scrollDelta, 0);
        }
    }

    private boolean scrollToChildRect(Rect rect, boolean immediate) {
        int delta = this.computeScrollDeltaToGetChildRectOnScreen(rect);
        boolean scroll = delta != 0;
        if (scroll) {
            if (immediate) {
                this.scrollBy(delta, 0);
            } else {
                this.smoothScrollBy(delta);
            }
        }
        return scroll;
    }

    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        if (this.getChildCount() == 0) {
            return 0;
        } else {
            int width = this.getWidth();
            int screenLeft = this.getScrollX();
            int screenRight = screenLeft + width;
            int scrollXDelta = 0;
            if (rect.right > screenRight && rect.left > screenLeft) {
                if (rect.width() > width) {
                    scrollXDelta += rect.left - screenLeft;
                } else {
                    scrollXDelta += rect.right - screenRight;
                }
                int right = this.getChildAt(0).getRight();
                int distanceToRight = right - screenRight;
                scrollXDelta = Math.min(scrollXDelta, distanceToRight);
            } else if (rect.left < screenLeft && rect.right < screenRight) {
                if (rect.width() > width) {
                    scrollXDelta -= screenRight - rect.right;
                } else {
                    scrollXDelta -= screenLeft - rect.left;
                }
                scrollXDelta = Math.max(scrollXDelta, -this.getScrollX());
            }
            return scrollXDelta;
        }
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        if (focused != null && focused.getRevealOnFocusHint()) {
            if (!this.mIsLayoutDirty) {
                this.scrollToChild(focused);
            } else {
                this.mChildToScrollTo = focused;
            }
        }
        super.requestChildFocus(child, focused);
    }

    @Override
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        if (direction == 2) {
            direction = 66;
        } else if (direction == 1) {
            direction = 17;
        }
        View nextFocus = previouslyFocusedRect == null ? FocusFinder.getInstance().findNextFocus(this, null, direction) : FocusFinder.getInstance().findNextFocusFromRect(this, previouslyFocusedRect, direction);
        if (nextFocus == null) {
            return false;
        } else {
            return this.isOffScreen(nextFocus) ? false : nextFocus.requestFocus(direction, previouslyFocusedRect);
        }
    }

    @Override
    public boolean requestChildRectangleOnScreen(View child, Rect rectangle, boolean immediate) {
        rectangle.offset(child.getLeft() - child.getScrollX(), child.getTop() - child.getScrollY());
        return this.scrollToChildRect(rectangle, immediate);
    }

    @Override
    public void requestLayout() {
        this.mIsLayoutDirty = true;
        super.requestLayout();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childWidth = 0;
        int childMargins = 0;
        if (this.getChildCount() > 0) {
            childWidth = this.getChildAt(0).getMeasuredWidth();
            FrameLayout.LayoutParams childParams = (FrameLayout.LayoutParams) this.getChildAt(0).getLayoutParams();
            childMargins = childParams.leftMargin + childParams.rightMargin;
        }
        int available = r - l - this.getPaddingLeftWithForeground() - this.getPaddingRightWithForeground() - childMargins;
        boolean forceLeftGravity = childWidth > available;
        this.layoutChildren(l, t, r, b, forceLeftGravity);
        this.mIsLayoutDirty = false;
        if (this.mChildToScrollTo != null && isViewDescendantOf(this.mChildToScrollTo, this)) {
            this.scrollToChild(this.mChildToScrollTo);
        }
        this.mChildToScrollTo = null;
        if (!this.isLaidOut()) {
            int scrollRange = Math.max(0, childWidth - (r - l - this.mPaddingLeft - this.mPaddingRight));
            if (this.isLayoutRtl()) {
                this.mScrollX = scrollRange - this.mScrollX;
            }
            if (this.mScrollX > scrollRange) {
                this.mScrollX = scrollRange;
            } else if (this.mScrollX < 0) {
                this.mScrollX = 0;
            }
        }
        this.scrollTo(this.mScrollX, this.mScrollY);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        View currentFocused = this.findFocus();
        if (null != currentFocused && this != currentFocused) {
            int maxJump = this.getWidth();
            if (this.isWithinDeltaOfScreen(currentFocused, maxJump)) {
                currentFocused.getDrawingRect(this.mTempRect);
                this.offsetDescendantRectToMyCoords(currentFocused, this.mTempRect);
                int scrollDelta = this.computeScrollDeltaToGetChildRectOnScreen(this.mTempRect);
                this.doScrollX(scrollDelta);
            }
        }
    }

    private static boolean isViewDescendantOf(View child, View parent) {
        if (child == parent) {
            return true;
        } else {
            ViewParent theParent = child.getParent();
            return theParent instanceof ViewGroup && isViewDescendantOf((View) theParent, parent);
        }
    }

    public void fling(int velocityX) {
        if (this.getChildCount() > 0) {
            int width = this.getWidth() - this.mPaddingRight - this.mPaddingLeft;
            int right = this.getChildAt(0).getRight() - this.mPaddingLeft;
            int maxScroll = Math.max(0, right - width);
            boolean shouldFling = false;
            if (this.mScrollX == 0 && !this.mEdgeGlowLeft.isFinished()) {
                if (this.shouldAbsorb(this.mEdgeGlowLeft, -velocityX)) {
                    this.mEdgeGlowLeft.onAbsorb(-velocityX);
                } else {
                    shouldFling = true;
                }
            } else if (this.mScrollX != maxScroll || this.mEdgeGlowRight.isFinished()) {
                shouldFling = true;
            } else if (this.shouldAbsorb(this.mEdgeGlowRight, velocityX)) {
                this.mEdgeGlowRight.onAbsorb(velocityX);
            } else {
                shouldFling = true;
            }
            if (shouldFling) {
                this.mScroller.fling(this.mScrollX, this.mScrollY, velocityX, 0, 0, maxScroll, 0, 0, width / 2, 0);
                boolean movingRight = velocityX > 0;
                View currentFocused = this.findFocus();
                View newFocused = this.findFocusableViewInMyBounds(movingRight, this.mScroller.getFinalX(), currentFocused);
                if (newFocused == null) {
                    newFocused = this;
                }
                if (newFocused != currentFocused) {
                    newFocused.requestFocus(movingRight ? 66 : 17);
                }
            }
            this.postInvalidateOnAnimation();
        }
    }

    private boolean shouldAbsorb(EdgeEffect edgeEffect, int velocity) {
        if (velocity > 0) {
            return true;
        } else {
            float distance = edgeEffect.getDistance() * (float) this.getWidth();
            float flingDistance = (float) this.mScroller.getSplineFlingDistance(-velocity);
            return flingDistance < distance;
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        if (this.getChildCount() > 0) {
            View child = this.getChildAt(0);
            x = clamp(x, this.getWidth() - this.mPaddingRight - this.mPaddingLeft, child.getWidth());
            y = clamp(y, this.getHeight() - this.mPaddingBottom - this.mPaddingTop, child.getHeight());
            if (x != this.mScrollX || y != this.mScrollY) {
                super.scrollTo(x, y);
            }
        }
    }

    private boolean shouldDisplayEdgeEffects() {
        return this.getOverScrollMode() != 2;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        super.draw(canvas);
        if (this.shouldDisplayEdgeEffects()) {
            int scrollX = this.mScrollX;
            if (!this.mEdgeGlowLeft.isFinished()) {
                int restoreCount = canvas.save();
                int height = this.getHeight() - this.mPaddingTop - this.mPaddingBottom;
                canvas.rotate(270.0F);
                canvas.translate((float) (-height + this.mPaddingTop), (float) Math.min(0, scrollX));
                this.mEdgeGlowLeft.setSize(height, this.getWidth());
                if (this.mEdgeGlowLeft.draw(canvas)) {
                    this.postInvalidateOnAnimation();
                }
                canvas.restoreToCount(restoreCount);
            }
            if (!this.mEdgeGlowRight.isFinished()) {
                int restoreCount = canvas.save();
                int width = this.getWidth();
                int height = this.getHeight() - this.mPaddingTop - this.mPaddingBottom;
                canvas.rotate(90.0F);
                canvas.translate((float) (-this.mPaddingTop), (float) (-(Math.max(this.getScrollRange(), scrollX) + width)));
                this.mEdgeGlowRight.setSize(height, width);
                if (this.mEdgeGlowRight.draw(canvas)) {
                    this.postInvalidateOnAnimation();
                }
                canvas.restoreToCount(restoreCount);
            }
        }
    }

    private static int clamp(int n, int my, int child) {
        if (my < child && n >= 0) {
            return my + n > child ? child - my : n;
        } else {
            return 0;
        }
    }
}