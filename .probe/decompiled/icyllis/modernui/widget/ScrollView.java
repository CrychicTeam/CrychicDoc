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

public class ScrollView extends FrameLayout {

    private static final float FLING_DESTRETCH_FACTOR = 4.0F;

    private final Rect mTempRect = new Rect();

    private final OverScroller mScroller;

    private final EdgeEffect mEdgeGlowTop;

    private final EdgeEffect mEdgeGlowBottom;

    private int mLastMotionY;

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

    private final float mVerticalScrollFactor;

    private int mActivePointerId = -1;

    private final int[] mScrollOffset = new int[2];

    private final int[] mScrollConsumed = new int[2];

    private int mNestedYOffset;

    public ScrollView(Context context) {
        super(context);
        this.mScroller = new OverScroller();
        this.mEdgeGlowTop = new EdgeEffect();
        this.mEdgeGlowBottom = new EdgeEffect();
        this.setFocusable(true);
        this.setDescendantFocusability(262144);
        this.setWillNotDraw(false);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        this.mTouchSlop = configuration.getScaledTouchSlop();
        this.mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        this.mOverscrollDistance = configuration.getScaledOverscrollDistance();
        this.mOverflingDistance = configuration.getScaledOverflingDistance();
        this.mVerticalScrollFactor = configuration.getScaledVerticalScrollFactor();
        this.setVerticalScrollBarEnabled(true);
        ShapeDrawable thumb = new ShapeDrawable();
        thumb.setShape(4);
        thumb.setStroke(this.dp(4.0F), SystemTheme.modulateColor(-1, 0.25F));
        thumb.setCornerRadius(1.0F);
        this.setVerticalScrollbarThumbDrawable(thumb);
        ShapeDrawable track = new ShapeDrawable();
        track.setShape(4);
        track.setStroke(this.dp(4.0F), 1082163328);
        track.setSize(this.dp(4.0F), -1);
        track.setCornerRadius(1.0F);
        this.setVerticalScrollbarTrackDrawable(track);
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return true;
    }

    public void setEdgeEffectColor(@ColorInt int color) {
        this.setTopEdgeEffectColor(color);
        this.setBottomEdgeEffectColor(color);
    }

    public void setBottomEdgeEffectColor(@ColorInt int color) {
        this.mEdgeGlowBottom.setColor(color);
    }

    public void setTopEdgeEffectColor(@ColorInt int color) {
        this.mEdgeGlowTop.setColor(color);
    }

    @ColorInt
    public int getTopEdgeEffectColor() {
        return this.mEdgeGlowTop.getColor();
    }

    @ColorInt
    public int getBottomEdgeEffectColor() {
        return this.mEdgeGlowBottom.getColor();
    }

    @Override
    public void addView(@NonNull View child) {
        if (this.getChildCount() > 0) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        } else {
            super.addView(child);
        }
    }

    @Override
    public void addView(@NonNull View child, int index) {
        if (this.getChildCount() > 0) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        } else {
            super.addView(child, index);
        }
    }

    @Override
    public void addView(@NonNull View child, @NonNull ViewGroup.LayoutParams params) {
        if (this.getChildCount() > 0) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        } else {
            super.addView(child, params);
        }
    }

    @Override
    public void addView(@NonNull View child, int index, @NonNull ViewGroup.LayoutParams params) {
        if (this.getChildCount() > 0) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        } else {
            super.addView(child, index, params);
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
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            if (heightMode != 0) {
                if (this.getChildCount() > 0) {
                    View child = this.getChildAt(0);
                    FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) child.getLayoutParams();
                    int widthPadding = this.mPaddingLeft + this.mPaddingRight + lp.leftMargin + lp.rightMargin;
                    int heightPadding = this.mPaddingTop + this.mPaddingBottom + lp.topMargin + lp.bottomMargin;
                    int desiredHeight = this.getMeasuredHeight() - heightPadding;
                    if (child.getMeasuredHeight() < desiredHeight) {
                        int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, widthPadding, lp.width);
                        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(desiredHeight, 1073741824);
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

    public boolean executeKeyEvent(@NonNull KeyEvent event) {
        this.mTempRect.setEmpty();
        boolean handled = false;
        if (event.getAction() == 0) {
            switch(event.getKeyCode()) {
                case 32:
                    handled = this.pageScroll(event.isShiftPressed() ? 33 : 130);
                    break;
                case 264:
                    handled = this.arrowScroll(130);
                    break;
                case 265:
                    handled = this.arrowScroll(33);
                    break;
                case 266:
                    handled = this.pageScroll(33);
                    break;
                case 267:
                    handled = this.pageScroll(130);
                    break;
                case 268:
                    handled = this.fullScroll(33);
                    break;
                case 269:
                    handled = this.fullScroll(130);
            }
        }
        return handled;
    }

    private boolean inChild(int x, int y) {
        if (this.getChildCount() <= 0) {
            return false;
        } else {
            int scrollY = this.mScrollY;
            View child = this.getChildAt(0);
            return y >= child.getTop() - scrollY && y < child.getBottom() - scrollY && x >= child.getLeft() && x < child.getRight();
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
    public boolean onInterceptTouchEvent(@NonNull MotionEvent ev) {
        int action = ev.getAction();
        if (action == 2 && this.mIsBeingDragged) {
            return true;
        } else if (super.onInterceptTouchEvent(ev)) {
            return true;
        } else if (this.getScrollY() == 0 && !this.canScrollVertically(1)) {
            return false;
        } else {
            switch(action) {
                case 0:
                    int y = (int) ev.getY();
                    if (!this.inChild((int) ev.getX(), y)) {
                        this.mIsBeingDragged = false;
                        this.recycleVelocityTracker();
                    } else {
                        this.mLastMotionY = y;
                        this.mActivePointerId = ev.getPointerId(0);
                        this.initOrResetVelocityTracker();
                        this.mVelocityTracker.addMovement(ev);
                        this.mScroller.computeScrollOffset();
                        this.mIsBeingDragged = !this.mScroller.isFinished();
                        if (!this.mEdgeGlowTop.isFinished()) {
                            this.mEdgeGlowTop.onPullDistance(0.0F, ev.getX() / (float) this.getWidth());
                        }
                        if (!this.mEdgeGlowBottom.isFinished()) {
                            this.mEdgeGlowBottom.onPullDistance(0.0F, 1.0F - ev.getX() / (float) this.getWidth());
                        }
                        this.startNestedScroll(2, 0);
                    }
                    break;
                case 1:
                case 3:
                    this.mIsBeingDragged = false;
                    this.mActivePointerId = -1;
                    this.recycleVelocityTracker();
                    if (this.mScroller.springBack(this.mScrollX, this.mScrollY, 0, 0, 0, this.getScrollRange())) {
                        this.postInvalidateOnAnimation();
                    }
                    this.stopNestedScroll(0);
                    break;
                case 2:
                    int activePointerId = this.mActivePointerId;
                    if (activePointerId != -1) {
                        int yx = (int) ev.getY();
                        int yDiff = Math.abs(yx - this.mLastMotionY);
                        if (yDiff > this.mTouchSlop && (this.getNestedScrollAxes() & 2) == 0) {
                            this.mIsBeingDragged = true;
                            this.mLastMotionY = yx;
                            this.initVelocityTrackerIfNotExists();
                            this.mVelocityTracker.addMovement(ev);
                            this.mNestedYOffset = 0;
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

    private boolean shouldDisplayEdgeEffects() {
        return this.getOverScrollMode() != 2;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {
        this.initVelocityTrackerIfNotExists();
        MotionEvent vtev = ev.copy();
        int action = ev.getAction();
        if (action == 0) {
            this.mNestedYOffset = 0;
        }
        vtev.offsetLocation(0.0F, (float) this.mNestedYOffset);
        switch(action) {
            case 0:
                if (this.getChildCount() == 0) {
                    vtev.recycle();
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
                this.mLastMotionY = (int) ev.getY();
                this.mActivePointerId = ev.getPointerId(0);
                this.startNestedScroll(2, 0);
                break;
            case 1:
                if (this.mIsBeingDragged) {
                    VelocityTracker velocityTracker = this.mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                    int initialVelocity = (int) velocityTracker.getYVelocity();
                    if (Math.abs(initialVelocity) > this.mMinimumVelocity) {
                        this.flingWithNestedDispatch(-initialVelocity);
                    } else if (this.mScroller.springBack(this.mScrollX, this.mScrollY, 0, 0, 0, this.getScrollRange())) {
                        this.postInvalidateOnAnimation();
                    }
                    this.mActivePointerId = -1;
                    this.endDrag();
                }
                break;
            case 2:
                if (this.mActivePointerId != -1) {
                    int y = (int) ev.getY();
                    int deltaY = this.mLastMotionY - y;
                    if (this.dispatchNestedPreScroll(0, deltaY, this.mScrollConsumed, this.mScrollOffset, 0)) {
                        deltaY -= this.mScrollConsumed[1];
                        vtev.offsetLocation(0.0F, (float) this.mScrollOffset[1]);
                        this.mNestedYOffset = this.mNestedYOffset + this.mScrollOffset[1];
                    }
                    if (!this.mIsBeingDragged && Math.abs(deltaY) > this.mTouchSlop) {
                        ViewParent parent = this.getParent();
                        if (parent != null) {
                            parent.requestDisallowInterceptTouchEvent(true);
                        }
                        this.mIsBeingDragged = true;
                        if (deltaY > 0) {
                            deltaY -= this.mTouchSlop;
                        } else {
                            deltaY += this.mTouchSlop;
                        }
                    }
                    if (this.mIsBeingDragged) {
                        this.mLastMotionY = y - this.mScrollOffset[1];
                        int oldY = this.mScrollY;
                        int range = this.getScrollRange();
                        int overscrollMode = this.getOverScrollMode();
                        boolean canOverscroll = overscrollMode == 0 || overscrollMode == 1 && range > 0;
                        float displacement = ev.getX() / (float) this.getWidth();
                        if (canOverscroll) {
                            int consumed = 0;
                            if (deltaY < 0 && this.mEdgeGlowBottom.getDistance() != 0.0F) {
                                consumed = Math.round((float) this.getHeight() * this.mEdgeGlowBottom.onPullDistance((float) deltaY / (float) this.getHeight(), 1.0F - displacement));
                            } else if (deltaY > 0 && this.mEdgeGlowTop.getDistance() != 0.0F) {
                                consumed = Math.round((float) (-this.getHeight()) * this.mEdgeGlowTop.onPullDistance((float) (-deltaY) / (float) this.getHeight(), displacement));
                            }
                            deltaY -= consumed;
                        }
                        if (this.overScrollBy(0, deltaY, 0, this.mScrollY, 0, range, 0, this.mOverscrollDistance, true) && !this.hasNestedScrollingParent(0)) {
                            this.mVelocityTracker.clear();
                        }
                        int scrolledDeltaY = this.mScrollY - oldY;
                        int unconsumedY = deltaY - scrolledDeltaY;
                        if (this.dispatchNestedScroll(0, scrolledDeltaY, 0, unconsumedY, this.mScrollOffset, 0, this.mScrollConsumed)) {
                            this.mLastMotionY = this.mLastMotionY - this.mScrollOffset[1];
                            vtev.offsetLocation(0.0F, (float) this.mScrollOffset[1]);
                            this.mNestedYOffset = this.mNestedYOffset + this.mScrollOffset[1];
                        } else if (canOverscroll && (float) deltaY != 0.0F) {
                            int pulledToY = oldY + deltaY;
                            if (pulledToY < 0) {
                                this.mEdgeGlowTop.onPullDistance((float) (-deltaY) / (float) this.getHeight(), displacement);
                                if (!this.mEdgeGlowBottom.isFinished()) {
                                    this.mEdgeGlowBottom.onRelease();
                                }
                            } else if (pulledToY > range) {
                                this.mEdgeGlowBottom.onPullDistance((float) deltaY / (float) this.getHeight(), 1.0F - displacement);
                                if (!this.mEdgeGlowTop.isFinished()) {
                                    this.mEdgeGlowTop.onRelease();
                                }
                            }
                            if (this.shouldDisplayEdgeEffects() && (!this.mEdgeGlowTop.isFinished() || !this.mEdgeGlowBottom.isFinished())) {
                                this.postInvalidateOnAnimation();
                            }
                        }
                    }
                }
                break;
            case 3:
                if (this.mIsBeingDragged && this.getChildCount() > 0) {
                    if (this.mScroller.springBack(this.mScrollX, this.mScrollY, 0, 0, 0, this.getScrollRange())) {
                        this.postInvalidateOnAnimation();
                    }
                    this.mActivePointerId = -1;
                    this.endDrag();
                }
        }
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.addMovement(vtev);
        }
        vtev.recycle();
        return true;
    }

    @Override
    public boolean onGenericMotionEvent(@NonNull MotionEvent event) {
        if (event.getAction() == 8) {
            float axisValue = event.getAxisValue(9);
            int delta = Math.round(axisValue * this.mVerticalScrollFactor);
            if ((double) Math.abs(axisValue) > 0.9 && Math.abs(delta) * 6 > this.mMinimumVelocity) {
                int deltaY = MathUtil.clamp(delta * 6, -this.mMaximumVelocity, this.mMaximumVelocity);
                this.flingWithNestedDispatch(-deltaY);
                return true;
            }
            if (this.smoothScrollBy(-delta)) {
                return true;
            }
        }
        return super.onGenericMotionEvent(event);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        if (!this.mScroller.isFinished()) {
            int oldX = this.mScrollX;
            int oldY = this.mScrollY;
            this.mScrollX = scrollX;
            this.mScrollY = scrollY;
            this.onScrollChanged(this.mScrollX, this.mScrollY, oldX, oldY);
            if (clampedY) {
                this.mScroller.springBack(this.mScrollX, this.mScrollY, 0, 0, 0, this.getScrollRange());
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
            scrollRange = Math.max(0, child.getHeight() - (this.getHeight() - this.mPaddingBottom - this.mPaddingTop));
        }
        return scrollRange;
    }

    private View findFocusableViewInBounds(boolean topFocus, int top, int bottom) {
        List<View> focusables = this.getFocusables(2);
        View focusCandidate = null;
        boolean foundFullyContainedFocusable = false;
        for (View view : focusables) {
            int viewTop = view.getTop();
            int viewBottom = view.getBottom();
            if (top < viewBottom && viewTop < bottom) {
                boolean viewIsFullyContained = top < viewTop && viewBottom < bottom;
                if (focusCandidate == null) {
                    focusCandidate = view;
                    foundFullyContainedFocusable = viewIsFullyContained;
                } else {
                    boolean viewIsCloserToBoundary = topFocus && viewTop < focusCandidate.getTop() || !topFocus && viewBottom > focusCandidate.getBottom();
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
        boolean down = direction == 130;
        int height = this.getHeight();
        if (down) {
            this.mTempRect.top = this.getScrollY() + height;
            int count = this.getChildCount();
            if (count > 0) {
                View view = this.getChildAt(count - 1);
                if (this.mTempRect.top + height > view.getBottom()) {
                    this.mTempRect.top = view.getBottom() - height;
                }
            }
        } else {
            this.mTempRect.top = this.getScrollY() - height;
            if (this.mTempRect.top < 0) {
                this.mTempRect.top = 0;
            }
        }
        this.mTempRect.bottom = this.mTempRect.top + height;
        return this.scrollAndFocus(direction, this.mTempRect.top, this.mTempRect.bottom);
    }

    public boolean fullScroll(int direction) {
        boolean down = direction == 130;
        int height = this.getHeight();
        this.mTempRect.top = 0;
        this.mTempRect.bottom = height;
        if (down) {
            int count = this.getChildCount();
            if (count > 0) {
                View view = this.getChildAt(count - 1);
                this.mTempRect.bottom = view.getBottom() + this.mPaddingBottom;
                this.mTempRect.top = this.mTempRect.bottom - height;
            }
        }
        return this.scrollAndFocus(direction, this.mTempRect.top, this.mTempRect.bottom);
    }

    private boolean scrollAndFocus(int direction, int top, int bottom) {
        boolean handled = true;
        int height = this.getHeight();
        int containerTop = this.getScrollY();
        int containerBottom = containerTop + height;
        boolean up = direction == 33;
        View newFocused = this.findFocusableViewInBounds(up, top, bottom);
        if (newFocused == null) {
            newFocused = this;
        }
        if (top >= containerTop && bottom <= containerBottom) {
            handled = false;
        } else {
            int delta = up ? top - containerTop : bottom - containerBottom;
            this.doScrollY(delta);
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
        int maxJump = (int) ((float) this.getHeight() * 0.5F);
        if (nextFocused != null && this.isWithinDeltaOfScreen(nextFocused, maxJump, this.getHeight())) {
            nextFocused.getDrawingRect(this.mTempRect);
            this.offsetDescendantRectToMyCoords(nextFocused, this.mTempRect);
            int scrollDelta = this.computeScrollDeltaToGetChildRectOnScreen(this.mTempRect);
            this.doScrollY(scrollDelta);
            nextFocused.requestFocus(direction);
        } else {
            int scrollDelta = maxJump;
            if (direction == 33 && this.getScrollY() < maxJump) {
                scrollDelta = this.getScrollY();
            } else if (direction == 130 && this.getChildCount() > 0) {
                int daBottom = this.getChildAt(0).getBottom();
                int screenBottom = this.getScrollY() + this.getHeight() - this.mPaddingBottom;
                if (daBottom - screenBottom < maxJump) {
                    scrollDelta = daBottom - screenBottom;
                }
            }
            if (scrollDelta == 0) {
                return false;
            }
            this.doScrollY(direction == 130 ? scrollDelta : -scrollDelta);
        }
        if (currentFocused != null && currentFocused.isFocused() && this.isOffScreen(currentFocused)) {
            int descendantFocusability = this.getDescendantFocusability();
            this.setDescendantFocusability(131072);
            this.requestFocus();
            this.setDescendantFocusability(descendantFocusability);
        }
        return true;
    }

    private boolean isOffScreen(@NonNull View descendant) {
        return !this.isWithinDeltaOfScreen(descendant, 0, this.getHeight());
    }

    private boolean isWithinDeltaOfScreen(@NonNull View descendant, int delta, int height) {
        descendant.getDrawingRect(this.mTempRect);
        this.offsetDescendantRectToMyCoords(descendant, this.mTempRect);
        return this.mTempRect.bottom + delta >= this.getScrollY() && this.mTempRect.top - delta <= this.getScrollY() + height;
    }

    private void doScrollY(int delta) {
        if (delta != 0) {
            if (this.mSmoothScrollingEnabled) {
                this.smoothScrollBy(delta);
            } else {
                this.scrollBy(0, delta);
            }
        }
    }

    public final boolean smoothScrollBy(int delta) {
        if (this.getChildCount() == 0) {
            return false;
        } else {
            delta = Math.max(0, Math.min(this.mScroller.getFinalY() + delta, this.getScrollRange())) - this.mScrollY;
            if (delta != 0) {
                this.mScroller.startScroll(this.mScrollX, this.mScrollY, 0, delta);
                this.postInvalidateOnAnimation();
                return true;
            } else {
                return false;
            }
        }
    }

    public final void smoothScrollTo(int y) {
        this.smoothScrollBy(y - this.mScrollY);
    }

    @Override
    protected int computeVerticalScrollRange() {
        int count = this.getChildCount();
        int contentHeight = this.getHeight() - this.mPaddingBottom - this.mPaddingTop;
        if (count == 0) {
            return contentHeight;
        } else {
            int scrollRange = this.getChildAt(0).getBottom();
            int scrollY = this.mScrollY;
            int overscrollBottom = Math.max(0, scrollRange - contentHeight);
            if (scrollY < 0) {
                scrollRange -= scrollY;
            } else if (scrollY > overscrollBottom) {
                scrollRange += scrollY - overscrollBottom;
            }
            return scrollRange;
        }
    }

    @Override
    protected int computeVerticalScrollOffset() {
        return Math.max(0, super.computeVerticalScrollOffset());
    }

    @Override
    protected void measureChild(@NonNull View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        ViewGroup.LayoutParams lp = child.getLayoutParams();
        int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec, this.mPaddingLeft + this.mPaddingRight, lp.width);
        int verticalPadding = this.mPaddingTop + this.mPaddingBottom;
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(Math.max(0, MeasureSpec.getSize(parentHeightMeasureSpec) - verticalPadding), 0);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override
    protected void measureChildWithMargins(@NonNull View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
        int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec, this.mPaddingLeft + this.mPaddingRight + lp.leftMargin + lp.rightMargin + widthUsed, lp.width);
        int usedTotal = this.mPaddingTop + this.mPaddingBottom + lp.topMargin + lp.bottomMargin + heightUsed;
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(Math.max(0, MeasureSpec.getSize(parentHeightMeasureSpec) - usedTotal), 0);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override
    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            int oldX = this.mScrollX;
            int oldY = this.mScrollY;
            int x = this.mScroller.getCurrX();
            int y = this.mScroller.getCurrY();
            if (oldX != x || oldY != y) {
                int range = this.getScrollRange();
                int overscrollMode = this.getOverScrollMode();
                boolean canOverscroll = overscrollMode == 0 || overscrollMode == 1 && range > 0;
                this.overScrollBy(x - oldX, y - oldY, oldX, oldY, 0, range, 0, this.mOverflingDistance, false);
                this.onScrollChanged(this.mScrollX, this.mScrollY, oldX, oldY);
                if (canOverscroll) {
                    if (y < 0 && oldY >= 0) {
                        this.mEdgeGlowTop.onAbsorb((int) this.mScroller.getCurrVelocity());
                    } else if (y > range && oldY <= range) {
                        this.mEdgeGlowBottom.onAbsorb((int) this.mScroller.getCurrVelocity());
                    }
                }
            }
            if (!this.awakenScrollBars()) {
                this.postInvalidateOnAnimation();
            }
        }
    }

    private int consumeFlingInStretch(int unconsumed) {
        if (unconsumed > 0 && this.mEdgeGlowTop != null && this.mEdgeGlowTop.getDistance() != 0.0F) {
            int size = this.getHeight();
            float deltaDistance = (float) (-unconsumed) * 4.0F / (float) size;
            int consumed = Math.round((float) (-size) / 4.0F * this.mEdgeGlowTop.onPullDistance(deltaDistance, 0.5F));
            if (consumed != unconsumed) {
                this.mEdgeGlowTop.finish();
            }
            return unconsumed - consumed;
        } else if (unconsumed < 0 && this.mEdgeGlowBottom != null && this.mEdgeGlowBottom.getDistance() != 0.0F) {
            int size = this.getHeight();
            float deltaDistance = (float) unconsumed * 4.0F / (float) size;
            int consumed = Math.round((float) size / 4.0F * this.mEdgeGlowBottom.onPullDistance(deltaDistance, 0.5F));
            if (consumed != unconsumed) {
                this.mEdgeGlowBottom.finish();
            }
            return unconsumed - consumed;
        } else {
            return unconsumed;
        }
    }

    public void scrollToDescendant(@NonNull View child) {
        if (!this.mIsLayoutDirty) {
            child.getDrawingRect(this.mTempRect);
            this.offsetDescendantRectToMyCoords(child, this.mTempRect);
            int scrollDelta = this.computeScrollDeltaToGetChildRectOnScreen(this.mTempRect);
            if (scrollDelta != 0) {
                this.scrollBy(0, scrollDelta);
            }
        } else {
            this.mChildToScrollTo = child;
        }
    }

    private boolean scrollToChildRect(Rect rect, boolean immediate) {
        int delta = this.computeScrollDeltaToGetChildRectOnScreen(rect);
        if (delta != 0) {
            if (immediate) {
                this.scrollBy(0, delta);
            } else {
                this.smoothScrollBy(delta);
            }
            return true;
        } else {
            return false;
        }
    }

    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        if (this.getChildCount() == 0) {
            return 0;
        } else {
            int height = this.getHeight();
            int screenTop = this.getScrollY();
            int screenBottom = screenTop + height;
            int scrollYDelta = 0;
            if (rect.bottom > screenBottom && rect.top > screenTop) {
                if (rect.height() > height) {
                    scrollYDelta += rect.top - screenTop;
                } else {
                    scrollYDelta += rect.bottom - screenBottom;
                }
                int bottom = this.getChildAt(0).getBottom();
                int distanceToBottom = bottom - screenBottom;
                scrollYDelta = Math.min(scrollYDelta, distanceToBottom);
            } else if (rect.top < screenTop && rect.bottom < screenBottom) {
                if (rect.height() > height) {
                    scrollYDelta -= screenBottom - rect.bottom;
                } else {
                    scrollYDelta -= screenTop - rect.top;
                }
                scrollYDelta = Math.max(scrollYDelta, -this.getScrollY());
            }
            return scrollYDelta;
        }
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        if (focused != null && focused.getRevealOnFocusHint()) {
            if (!this.mIsLayoutDirty) {
                this.scrollToDescendant(focused);
            } else {
                this.mChildToScrollTo = focused;
            }
        }
        super.requestChildFocus(child, focused);
    }

    @Override
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        if (direction == 2) {
            direction = 130;
        } else if (direction == 1) {
            direction = 33;
        }
        View nextFocus = previouslyFocusedRect == null ? FocusFinder.getInstance().findNextFocus(this, null, direction) : FocusFinder.getInstance().findNextFocusFromRect(this, previouslyFocusedRect, direction);
        if (nextFocus == null) {
            return false;
        } else {
            return this.isOffScreen(nextFocus) ? false : nextFocus.requestFocus(direction, previouslyFocusedRect);
        }
    }

    @Override
    public boolean requestChildRectangleOnScreen(@NonNull View child, @NonNull Rect rectangle, boolean immediate) {
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
        super.onLayout(changed, l, t, r, b);
        this.mIsLayoutDirty = false;
        if (this.mChildToScrollTo != null && isViewDescendantOf(this.mChildToScrollTo, this)) {
            this.scrollToDescendant(this.mChildToScrollTo);
        }
        this.mChildToScrollTo = null;
        if (!this.isLaidOut()) {
            int childHeight = this.getChildCount() > 0 ? this.getChildAt(0).getMeasuredHeight() : 0;
            int scrollRange = Math.max(0, childHeight - (b - t - this.mPaddingBottom - this.mPaddingTop));
            if (this.mScrollY > scrollRange) {
                this.mScrollY = scrollRange;
            } else if (this.mScrollY < 0) {
                this.mScrollY = 0;
            }
        }
        this.scrollTo(this.mScrollX, this.mScrollY);
    }

    @Override
    protected void onSizeChanged(int w, int h, int prevWidth, int prevHeight) {
        super.onSizeChanged(w, h, prevWidth, prevHeight);
        View currentFocused = this.findFocus();
        if (currentFocused != null && currentFocused != this) {
            if (this.isWithinDeltaOfScreen(currentFocused, 0, prevHeight)) {
                currentFocused.getDrawingRect(this.mTempRect);
                this.offsetDescendantRectToMyCoords(currentFocused, this.mTempRect);
                int scrollDelta = this.computeScrollDeltaToGetChildRectOnScreen(this.mTempRect);
                this.doScrollY(scrollDelta);
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

    public void fling(int velocityY) {
        if (this.getChildCount() > 0) {
            int height = this.getHeight() - this.mPaddingBottom - this.mPaddingTop;
            int bottom = this.getChildAt(0).getHeight();
            this.mScroller.fling(this.mScrollX, this.mScrollY, 0, velocityY, 0, 0, 0, Math.max(0, bottom - height), 0, height / 2);
            this.postInvalidateOnAnimation();
        }
    }

    private void flingWithNestedDispatch(int velocityY) {
        boolean canFling = (this.mScrollY > 0 || velocityY > 0) && (this.mScrollY < this.getScrollRange() || velocityY < 0);
        if (!this.dispatchNestedPreFling(0.0F, (float) velocityY)) {
            boolean consumed = this.dispatchNestedFling(0.0F, (float) velocityY, canFling);
            if (canFling) {
                this.fling(velocityY);
            } else if (!consumed) {
                if (!this.mEdgeGlowTop.isFinished()) {
                    if (this.shouldAbsorb(this.mEdgeGlowTop, -velocityY)) {
                        this.mEdgeGlowTop.onAbsorb(-velocityY);
                    } else {
                        this.fling(velocityY);
                    }
                } else if (!this.mEdgeGlowBottom.isFinished()) {
                    if (this.shouldAbsorb(this.mEdgeGlowBottom, velocityY)) {
                        this.mEdgeGlowBottom.onAbsorb(velocityY);
                    } else {
                        this.fling(velocityY);
                    }
                }
            }
        }
    }

    private boolean shouldAbsorb(EdgeEffect edgeEffect, int velocity) {
        if (velocity > 0) {
            return true;
        } else {
            float distance = edgeEffect.getDistance() * (float) this.getHeight();
            float flingDistance = (float) this.mScroller.getSplineFlingDistance(-velocity);
            return flingDistance < distance;
        }
    }

    private void endDrag() {
        this.mIsBeingDragged = false;
        this.recycleVelocityTracker();
        if (this.shouldDisplayEdgeEffects()) {
            this.mEdgeGlowTop.onRelease();
            this.mEdgeGlowBottom.onRelease();
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

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        return (axes & 2) != 0;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        super.onNestedScrollAccepted(child, target, axes, type);
        this.startNestedScroll(2, type);
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        int oldScrollY = this.mScrollY;
        this.scrollBy(0, dyUnconsumed);
        int myConsumed = this.mScrollY - oldScrollY;
        int myUnconsumed = dyUnconsumed - myConsumed;
        consumed[1] += myConsumed;
        this.dispatchNestedScroll(0, myConsumed, 0, myUnconsumed, null, type, consumed);
    }

    @Override
    public boolean onNestedFling(@NonNull View target, float velocityX, float velocityY, boolean consumed) {
        if (!consumed) {
            this.flingWithNestedDispatch((int) velocityY);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onDrawForeground(@NonNull Canvas canvas) {
        super.onDrawForeground(canvas);
        if (this.shouldDisplayEdgeEffects()) {
            int scrollY = this.mScrollY;
            boolean clipToPadding = this.getClipToPadding();
            if (!this.mEdgeGlowTop.isFinished()) {
                canvas.save();
                int width;
                int height;
                float translateX;
                float translateY;
                if (clipToPadding) {
                    width = this.getWidth() - this.mPaddingLeft - this.mPaddingRight;
                    height = this.getHeight() - this.mPaddingTop - this.mPaddingBottom;
                    translateX = (float) this.mPaddingLeft;
                    translateY = (float) this.mPaddingTop;
                } else {
                    width = this.getWidth();
                    height = this.getHeight();
                    translateX = 0.0F;
                    translateY = 0.0F;
                }
                canvas.translate(translateX, (float) Math.min(0, scrollY) + translateY);
                this.mEdgeGlowTop.setSize(width, height);
                if (this.mEdgeGlowTop.draw(canvas)) {
                    this.postInvalidateOnAnimation();
                }
                canvas.restore();
            }
            if (!this.mEdgeGlowBottom.isFinished()) {
                canvas.save();
                int widthx;
                int heightx;
                float translateXx;
                float translateYx;
                if (clipToPadding) {
                    widthx = this.getWidth() - this.mPaddingLeft - this.mPaddingRight;
                    heightx = this.getHeight() - this.mPaddingTop - this.mPaddingBottom;
                    translateXx = (float) this.mPaddingLeft;
                    translateYx = (float) this.mPaddingTop;
                } else {
                    widthx = this.getWidth();
                    heightx = this.getHeight();
                    translateXx = 0.0F;
                    translateYx = 0.0F;
                }
                canvas.translate((float) (-widthx) + translateXx, (float) (Math.max(this.getScrollRange(), scrollY) + heightx) + translateYx);
                canvas.rotate(180.0F, (float) widthx, 0.0F);
                this.mEdgeGlowBottom.setSize(widthx, heightx);
                if (this.mEdgeGlowBottom.draw(canvas)) {
                    this.postInvalidateOnAnimation();
                }
                canvas.restore();
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