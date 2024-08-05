package icyllis.modernui.widget;

import icyllis.modernui.animation.AnimationUtils;
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
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class NestedScrollView extends FrameLayout {

    static final int ANIMATED_SCROLL_GAP = 250;

    static final float MAX_SCROLL_FACTOR = 0.5F;

    private static final int DEFAULT_SMOOTH_SCROLL_DURATION = 250;

    private long mLastScroll;

    private final Rect mTempRect = new Rect();

    private final OverScroller mScroller;

    private final EdgeEffect mEdgeGlowTop;

    private final EdgeEffect mEdgeGlowBottom;

    private int mLastMotionY;

    private boolean mIsLayoutDirty = true;

    private boolean mIsLaidOut = false;

    private View mChildToScrollTo = null;

    private boolean mIsBeingDragged = false;

    private VelocityTracker mVelocityTracker;

    private boolean mFillViewport;

    private boolean mSmoothScrollingEnabled = true;

    private final int mTouchSlop;

    private final int mMinimumVelocity;

    private final int mMaximumVelocity;

    private final int[] mScrollOffset = new int[2];

    private final int[] mScrollConsumed = new int[2];

    private int mNestedYOffset;

    private int mLastScrollerY;

    private NestedScrollView.OnScrollChangeListener mOnScrollChangeListener;

    public NestedScrollView(Context context) {
        super(context);
        this.mEdgeGlowTop = new EdgeEffect();
        this.mEdgeGlowBottom = new EdgeEffect();
        this.mScroller = new OverScroller();
        this.setFocusable(true);
        this.setDescendantFocusability(262144);
        this.setWillNotDraw(false);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        this.mTouchSlop = configuration.getScaledTouchSlop();
        this.mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        this.setNestedScrollingEnabled(true);
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
    public void onNestedScroll(@Nonnull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @Nonnull int[] consumed) {
        int oldScrollY = this.getScrollY();
        this.scrollBy(0, dyUnconsumed);
        int myConsumed = this.getScrollY() - oldScrollY;
        if (consumed != null) {
            consumed[1] += myConsumed;
        }
        int myUnconsumed = dyUnconsumed - myConsumed;
        this.dispatchNestedScroll(0, myConsumed, 0, myUnconsumed, null, type, consumed);
    }

    @Override
    public boolean onStartNestedScroll(@Nonnull View child, @Nonnull View target, int axes, int type) {
        return (axes & 2) != 0;
    }

    @Override
    public void onNestedScrollAccepted(@Nonnull View child, @Nonnull View target, int axes, int type) {
        super.onNestedScrollAccepted(child, target, axes, type);
        this.startNestedScroll(2, type);
    }

    @Override
    public void onStopNestedScroll(@Nonnull View target, int type) {
        super.onStopNestedScroll(target, type);
        this.stopNestedScroll(type);
    }

    @Override
    public void onNestedPreScroll(@Nonnull View target, int dx, int dy, @Nonnull int[] consumed, int type) {
        this.dispatchNestedPreScroll(dx, dy, consumed, null, type);
    }

    @Override
    public boolean onNestedFling(@Nonnull View target, float velocityX, float velocityY, boolean consumed) {
        if (!consumed) {
            this.dispatchNestedFling(0.0F, velocityY, true);
            this.fling((int) velocityY);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onNestedPreFling(@Nonnull View target, float velocityX, float velocityY) {
        return this.dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return true;
    }

    public int getMaxScrollAmount() {
        return (int) (0.5F * (float) this.getHeight());
    }

    @Override
    public void addView(@Nonnull View child) {
        if (this.getChildCount() > 0) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        } else {
            super.addView(child);
        }
    }

    @Override
    public void addView(@Nonnull View child, int index) {
        if (this.getChildCount() > 0) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        } else {
            super.addView(child, index);
        }
    }

    @Override
    public void addView(@Nonnull View child, @Nonnull ViewGroup.LayoutParams params) {
        if (this.getChildCount() > 0) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        } else {
            super.addView(child, params);
        }
    }

    @Override
    public void addView(@Nonnull View child, int index, @Nonnull ViewGroup.LayoutParams params) {
        if (this.getChildCount() > 0) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        } else {
            super.addView(child, index, params);
        }
    }

    public void setOnScrollChangeListener(@Nullable NestedScrollView.OnScrollChangeListener l) {
        this.mOnScrollChangeListener = l;
    }

    private boolean canScroll() {
        if (this.getChildCount() > 0) {
            View child = this.getChildAt(0);
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) child.getLayoutParams();
            int childSize = child.getHeight() + lp.topMargin + lp.bottomMargin;
            int parentSpace = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
            return childSize > parentSpace;
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
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (this.mOnScrollChangeListener != null) {
            this.mOnScrollChangeListener.onScrollChange(this, l, t, oldl, oldt);
        }
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
                    int childSize = child.getMeasuredHeight();
                    int parentSpace = this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom() - lp.topMargin - lp.bottomMargin;
                    if (childSize < parentSpace) {
                        int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, this.getPaddingLeft() + this.getPaddingRight() + lp.leftMargin + lp.rightMargin, lp.width);
                        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(parentSpace, 1073741824);
                        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
                    }
                }
            }
        }
    }

    @Override
    public boolean dispatchKeyEvent(@Nonnull KeyEvent event) {
        return super.dispatchKeyEvent(event) || this.executeKeyEvent(event);
    }

    public boolean executeKeyEvent(@Nonnull KeyEvent event) {
        this.mTempRect.setEmpty();
        if (this.canScroll()) {
            boolean handled = false;
            if (event.getAction() == 0) {
                switch(event.getKeyCode()) {
                    case 32:
                        this.pageScroll(event.isShiftPressed() ? 33 : 130);
                        break;
                    case 264:
                        if (!event.isAltPressed()) {
                            handled = this.arrowScroll(130);
                        } else {
                            handled = this.fullScroll(130);
                        }
                        break;
                    case 265:
                        if (!event.isAltPressed()) {
                            handled = this.arrowScroll(33);
                        } else {
                            handled = this.fullScroll(33);
                        }
                }
            }
            return handled;
        } else if (this.isFocused() && event.getKeyCode() != 256) {
            View currentFocused = this.findFocus();
            if (currentFocused == this) {
                currentFocused = null;
            }
            View nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused, 130);
            return nextFocused != null && nextFocused != this && nextFocused.requestFocus(130);
        } else {
            return false;
        }
    }

    private boolean inChild(int x, int y) {
        if (this.getChildCount() <= 0) {
            return false;
        } else {
            int scrollY = this.getScrollY();
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
    public boolean onInterceptTouchEvent(@Nonnull MotionEvent ev) {
        int action = ev.getAction();
        if (action == 2 && this.mIsBeingDragged) {
            return true;
        } else {
            switch(action) {
                case 0:
                    int yx = (int) ev.getY();
                    if (!this.inChild((int) ev.getX(), yx)) {
                        this.mIsBeingDragged = this.stopGlowAnimations(ev) || !this.mScroller.isFinished();
                        this.recycleVelocityTracker();
                    } else {
                        this.mLastMotionY = yx;
                        this.initOrResetVelocityTracker();
                        this.mVelocityTracker.addMovement(ev);
                        this.mScroller.computeScrollOffset();
                        this.mIsBeingDragged = this.stopGlowAnimations(ev) || !this.mScroller.isFinished();
                        this.startNestedScroll(2, 0);
                    }
                    break;
                case 1:
                case 3:
                    this.mIsBeingDragged = false;
                    this.recycleVelocityTracker();
                    if (this.mScroller.springBack(this.getScrollX(), this.getScrollY(), 0, 0, 0, this.getScrollRange())) {
                        this.postInvalidateOnAnimation();
                    }
                    this.stopNestedScroll(0);
                    break;
                case 2:
                    int y = (int) ev.getY();
                    int yDiff = Math.abs(y - this.mLastMotionY);
                    if (yDiff > this.mTouchSlop && (this.getNestedScrollAxes() & 2) == 0) {
                        this.mIsBeingDragged = true;
                        this.mLastMotionY = y;
                        this.initVelocityTrackerIfNotExists();
                        this.mVelocityTracker.addMovement(ev);
                        this.mNestedYOffset = 0;
                        ViewParent parent = this.getParent();
                        if (parent != null) {
                            parent.requestDisallowInterceptTouchEvent(true);
                        }
                    }
            }
            return this.mIsBeingDragged;
        }
    }

    @Override
    public boolean onTouchEvent(@Nonnull MotionEvent ev) {
        this.initVelocityTrackerIfNotExists();
        int action = ev.getAction();
        if (action == 0) {
            this.mNestedYOffset = 0;
        }
        MotionEvent vtev = ev.copy();
        vtev.offsetLocation(0.0F, (float) this.mNestedYOffset);
        switch(action) {
            case 0:
                if (this.getChildCount() == 0) {
                    return false;
                }
                if (this.mIsBeingDragged) {
                    ViewParent parent = this.getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }
                if (!this.mScroller.isFinished()) {
                    this.abortAnimatedScroll();
                }
                this.mLastMotionY = (int) ev.getY();
                this.startNestedScroll(2, 0);
                break;
            case 1:
                VelocityTracker velocityTracker = this.mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                int initialVelocity = (int) velocityTracker.getYVelocity();
                if (Math.abs(initialVelocity) >= this.mMinimumVelocity) {
                    if (!this.edgeEffectFling(initialVelocity) && !this.dispatchNestedPreFling(0.0F, (float) (-initialVelocity))) {
                        this.dispatchNestedFling(0.0F, (float) (-initialVelocity), true);
                        this.fling(-initialVelocity);
                    }
                } else if (this.mScroller.springBack(this.getScrollX(), this.getScrollY(), 0, 0, 0, this.getScrollRange())) {
                    this.postInvalidateOnAnimation();
                }
                this.endDrag();
                break;
            case 2:
                int y = (int) ev.getY();
                int deltaY = this.mLastMotionY - y;
                deltaY -= this.releaseVerticalGlow(deltaY, ev.getX());
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
                    if (this.dispatchNestedPreScroll(0, deltaY, this.mScrollConsumed, this.mScrollOffset, 0)) {
                        deltaY -= this.mScrollConsumed[1];
                        this.mNestedYOffset = this.mNestedYOffset + this.mScrollOffset[1];
                    }
                    this.mLastMotionY = y - this.mScrollOffset[1];
                    int oldY = this.getScrollY();
                    int range = this.getScrollRange();
                    int overscrollMode = this.getOverScrollMode();
                    boolean canOverscroll = overscrollMode == 0 || overscrollMode == 1 && range > 0;
                    boolean clearVelocityTracker = this.overScrollByCompat(0, deltaY, 0, this.getScrollY(), 0, range, 0, ViewConfiguration.get(this.getContext()).getScaledOverscrollDistance(), true) && !this.hasNestedScrollingParent(0);
                    int scrolledDeltaY = this.getScrollY() - oldY;
                    int unconsumedY = deltaY - scrolledDeltaY;
                    this.mScrollConsumed[1] = 0;
                    this.dispatchNestedScroll(0, scrolledDeltaY, 0, unconsumedY, this.mScrollOffset, 0, this.mScrollConsumed);
                    this.mLastMotionY = this.mLastMotionY - this.mScrollOffset[1];
                    this.mNestedYOffset = this.mNestedYOffset + this.mScrollOffset[1];
                    if (canOverscroll) {
                        deltaY -= this.mScrollConsumed[1];
                        int pulledToY = oldY + deltaY;
                        if (pulledToY < 0) {
                            this.mEdgeGlowTop.onPullDistance((float) (-deltaY) / (float) this.getHeight(), ev.getX() / (float) this.getWidth());
                            if (!this.mEdgeGlowBottom.isFinished()) {
                                this.mEdgeGlowBottom.onRelease();
                            }
                        } else if (pulledToY > range) {
                            this.mEdgeGlowBottom.onPullDistance((float) deltaY / (float) this.getHeight(), 1.0F - ev.getX() / (float) this.getWidth());
                            if (!this.mEdgeGlowTop.isFinished()) {
                                this.mEdgeGlowTop.onRelease();
                            }
                        }
                        if (!this.mEdgeGlowTop.isFinished() || !this.mEdgeGlowBottom.isFinished()) {
                            this.postInvalidateOnAnimation();
                            clearVelocityTracker = false;
                        }
                    }
                    if (clearVelocityTracker) {
                        this.mVelocityTracker.clear();
                    }
                }
                break;
            case 3:
                if (this.mIsBeingDragged && this.getChildCount() > 0 && this.mScroller.springBack(this.getScrollX(), this.getScrollY(), 0, 0, 0, this.getScrollRange())) {
                    this.postInvalidateOnAnimation();
                }
                this.endDrag();
        }
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.addMovement(vtev);
        }
        vtev.recycle();
        return true;
    }

    private boolean edgeEffectFling(int velocityY) {
        boolean consumed = true;
        if (this.mEdgeGlowTop.getDistance() != 0.0F) {
            this.mEdgeGlowTop.onAbsorb(velocityY);
        } else if (this.mEdgeGlowBottom.getDistance() != 0.0F) {
            this.mEdgeGlowBottom.onAbsorb(-velocityY);
        } else {
            consumed = false;
        }
        return consumed;
    }

    private boolean stopGlowAnimations(MotionEvent e) {
        boolean stopped = false;
        if (this.mEdgeGlowTop.getDistance() != 0.0F) {
            this.mEdgeGlowTop.onPullDistance(0.0F, e.getY() / (float) this.getHeight());
            stopped = true;
        }
        if (this.mEdgeGlowBottom.getDistance() != 0.0F) {
            this.mEdgeGlowBottom.onPullDistance(0.0F, 1.0F - e.getY() / (float) this.getHeight());
            stopped = true;
        }
        if (stopped) {
        }
        return false;
    }

    @Override
    public boolean onGenericMotionEvent(@Nonnull MotionEvent event) {
        if (event.getAction() == 8 && !this.mIsBeingDragged) {
            float axisValue = event.getAxisValue(9);
            int delta = Math.round(axisValue * ViewConfiguration.get(this.getContext()).getScaledVerticalScrollFactor());
            if ((double) Math.abs(axisValue) > 0.9 && Math.abs(delta) * 6 > this.mMinimumVelocity) {
                int deltaY = MathUtil.clamp(delta * 6, -this.mMaximumVelocity, this.mMaximumVelocity);
                if (!this.edgeEffectFling(deltaY) && !this.dispatchNestedPreFling(0.0F, (float) (-deltaY))) {
                    this.dispatchNestedFling(0.0F, (float) (-deltaY), true);
                    this.fling(-deltaY);
                }
                return true;
            } else {
                this.smoothScrollBy(-delta, 0);
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.scrollTo(scrollX, scrollY);
    }

    boolean overScrollByCompat(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        int overScrollMode = this.getOverScrollMode();
        boolean canScrollHorizontal = this.computeHorizontalScrollRange() > this.computeHorizontalScrollExtent();
        boolean canScrollVertical = this.computeVerticalScrollRange() > this.computeVerticalScrollExtent();
        boolean overScrollHorizontal = overScrollMode == 0 || overScrollMode == 1 && canScrollHorizontal;
        boolean overScrollVertical = overScrollMode == 0 || overScrollMode == 1 && canScrollVertical;
        int newScrollX = scrollX + deltaX;
        if (!overScrollHorizontal) {
            maxOverScrollX = 0;
        }
        int newScrollY = scrollY + deltaY;
        if (!overScrollVertical) {
            maxOverScrollY = 0;
        }
        int left = -maxOverScrollX;
        int right = maxOverScrollX + scrollRangeX;
        int top = -maxOverScrollY;
        int bottom = maxOverScrollY + scrollRangeY;
        boolean clampedX = false;
        if (newScrollX > right) {
            newScrollX = right;
            clampedX = true;
        } else if (newScrollX < left) {
            newScrollX = left;
            clampedX = true;
        }
        boolean clampedY = false;
        if (newScrollY > bottom) {
            newScrollY = bottom;
            clampedY = true;
        } else if (newScrollY < top) {
            newScrollY = top;
            clampedY = true;
        }
        if (clampedY && !this.hasNestedScrollingParent(1)) {
            this.mScroller.springBack(newScrollX, newScrollY, 0, 0, 0, this.getScrollRange());
        }
        this.onOverScrolled(newScrollX, newScrollY, clampedX, clampedY);
        return clampedX || clampedY;
    }

    int getScrollRange() {
        int scrollRange = 0;
        if (this.getChildCount() > 0) {
            View child = this.getChildAt(0);
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) child.getLayoutParams();
            int childSize = child.getHeight() + lp.topMargin + lp.bottomMargin;
            int parentSpace = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
            scrollRange = Math.max(0, childSize - parentSpace);
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
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) view.getLayoutParams();
                int bottom = view.getBottom() + lp.bottomMargin + this.getPaddingBottom();
                if (this.mTempRect.top + height > bottom) {
                    this.mTempRect.top = bottom - height;
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
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) view.getLayoutParams();
                this.mTempRect.bottom = view.getBottom() + lp.bottomMargin + this.getPaddingBottom();
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
        int maxJump = this.getMaxScrollAmount();
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
                View child = this.getChildAt(0);
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) child.getLayoutParams();
                int daBottom = child.getBottom() + lp.bottomMargin;
                int screenBottom = this.getScrollY() + this.getHeight() - this.getPaddingBottom();
                scrollDelta = Math.min(daBottom - screenBottom, maxJump);
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

    private boolean isOffScreen(View descendant) {
        return !this.isWithinDeltaOfScreen(descendant, 0, this.getHeight());
    }

    private boolean isWithinDeltaOfScreen(View descendant, int delta, int height) {
        descendant.getDrawingRect(this.mTempRect);
        this.offsetDescendantRectToMyCoords(descendant, this.mTempRect);
        return this.mTempRect.bottom + delta >= this.getScrollY() && this.mTempRect.top - delta <= this.getScrollY() + height;
    }

    private void doScrollY(int delta) {
        if (delta != 0) {
            if (this.mSmoothScrollingEnabled) {
                this.smoothScrollBy(0, delta);
            } else {
                this.scrollBy(0, delta);
            }
        }
    }

    public final void smoothScrollBy(int dx, int dy) {
        this.smoothScrollBy(dx, dy, 250, false);
    }

    public final void smoothScrollBy(int dx, int dy, int scrollDurationMs) {
        this.smoothScrollBy(dx, dy, scrollDurationMs, false);
    }

    private void smoothScrollBy(int dx, int dy, int scrollDurationMs, boolean withNestedScrolling) {
        if (this.getChildCount() != 0) {
            long duration = AnimationUtils.currentAnimationTimeMillis() - this.mLastScroll;
            if (duration > 250L) {
                View child = this.getChildAt(0);
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) child.getLayoutParams();
                int childSize = child.getHeight() + lp.topMargin + lp.bottomMargin;
                int parentSpace = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
                int scrollY = this.getScrollY();
                int maxY = Math.max(0, childSize - parentSpace);
                dy = Math.max(0, Math.min(scrollY + dy, maxY)) - scrollY;
                this.mScroller.startScroll(this.getScrollX(), scrollY, 0, dy, scrollDurationMs);
                this.runAnimatedScroll(withNestedScrolling);
            } else {
                if (!this.mScroller.isFinished()) {
                    this.abortAnimatedScroll();
                }
                this.scrollBy(dx, dy);
            }
            this.mLastScroll = AnimationUtils.currentAnimationTimeMillis();
        }
    }

    public final void smoothScrollTo(int x, int y) {
        this.smoothScrollTo(x, y, 250, false);
    }

    public final void smoothScrollTo(int x, int y, int scrollDurationMs) {
        this.smoothScrollTo(x, y, scrollDurationMs, false);
    }

    void smoothScrollTo(int x, int y, boolean withNestedScrolling) {
        this.smoothScrollTo(x, y, 250, withNestedScrolling);
    }

    void smoothScrollTo(int x, int y, int scrollDurationMs, boolean withNestedScrolling) {
        this.smoothScrollBy(x - this.getScrollX(), y - this.getScrollY(), scrollDurationMs, withNestedScrolling);
    }

    @Override
    public int computeVerticalScrollRange() {
        int count = this.getChildCount();
        int parentSpace = this.getHeight() - this.getPaddingBottom() - this.getPaddingTop();
        if (count == 0) {
            return parentSpace;
        } else {
            View child = this.getChildAt(0);
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) child.getLayoutParams();
            int scrollRange = child.getBottom() + lp.bottomMargin;
            int scrollY = this.getScrollY();
            int overscrollBottom = Math.max(0, scrollRange - parentSpace);
            if (scrollY < 0) {
                scrollRange -= scrollY;
            } else if (scrollY > overscrollBottom) {
                scrollRange += scrollY - overscrollBottom;
            }
            return scrollRange;
        }
    }

    @Override
    public int computeVerticalScrollOffset() {
        return Math.max(0, super.computeVerticalScrollOffset());
    }

    @Override
    public int computeVerticalScrollExtent() {
        return super.computeVerticalScrollExtent();
    }

    @Override
    public int computeHorizontalScrollRange() {
        return super.computeHorizontalScrollRange();
    }

    @Override
    public int computeHorizontalScrollOffset() {
        return super.computeHorizontalScrollOffset();
    }

    @Override
    public int computeHorizontalScrollExtent() {
        return super.computeHorizontalScrollExtent();
    }

    @Override
    protected void measureChild(@Nonnull View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        ViewGroup.LayoutParams lp = child.getLayoutParams();
        int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec, this.getPaddingLeft() + this.getPaddingRight(), lp.width);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0, 0);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override
    protected void measureChildWithMargins(@Nonnull View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
        int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec, this.getPaddingLeft() + this.getPaddingRight() + lp.leftMargin + lp.rightMargin + widthUsed, lp.width);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(lp.topMargin + lp.bottomMargin, 0);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override
    public void computeScroll() {
        if (!this.mScroller.isFinished()) {
            this.mScroller.computeScrollOffset();
            int y = this.mScroller.getCurrY();
            int unconsumed = y - this.mLastScrollerY;
            this.mLastScrollerY = y;
            this.mScrollConsumed[1] = 0;
            this.dispatchNestedPreScroll(0, unconsumed, this.mScrollConsumed, null, 1);
            unconsumed -= this.mScrollConsumed[1];
            int range = this.getScrollRange();
            if (unconsumed != 0) {
                int oldScrollY = this.getScrollY();
                this.overScrollByCompat(0, unconsumed, this.getScrollX(), oldScrollY, 0, range, 0, 0, false);
                int scrolledByMe = this.getScrollY() - oldScrollY;
                unconsumed -= scrolledByMe;
                this.mScrollConsumed[1] = 0;
                this.dispatchNestedScroll(0, scrolledByMe, 0, unconsumed, this.mScrollOffset, 1, this.mScrollConsumed);
                unconsumed -= this.mScrollConsumed[1];
            }
            if (unconsumed != 0) {
                int mode = this.getOverScrollMode();
                boolean canOverscroll = mode == 0 || mode == 1 && range > 0;
                if (canOverscroll) {
                    if (unconsumed < 0) {
                        if (this.mEdgeGlowTop.isFinished()) {
                            this.mEdgeGlowTop.onAbsorb((int) this.mScroller.getCurrVelocity());
                        }
                    } else if (this.mEdgeGlowBottom.isFinished()) {
                        this.mEdgeGlowBottom.onAbsorb((int) this.mScroller.getCurrVelocity());
                    }
                }
                this.abortAnimatedScroll();
            }
            if (!this.mScroller.isFinished()) {
                this.postInvalidateOnAnimation();
            } else {
                this.stopNestedScroll(1);
            }
        }
    }

    private int releaseVerticalGlow(int deltaY, float x) {
        float consumed = 0.0F;
        float displacement = x / (float) this.getWidth();
        float pullDistance = (float) deltaY / (float) this.getHeight();
        if (this.mEdgeGlowTop.getDistance() != 0.0F) {
            consumed = -this.mEdgeGlowTop.onPullDistance(-pullDistance, displacement);
            if (this.mEdgeGlowTop.getDistance() == 0.0F) {
                this.mEdgeGlowTop.onRelease();
            }
        } else if (this.mEdgeGlowBottom.getDistance() != 0.0F) {
            consumed = this.mEdgeGlowBottom.onPullDistance(pullDistance, 1.0F - displacement);
            if (this.mEdgeGlowBottom.getDistance() == 0.0F) {
                this.mEdgeGlowBottom.onRelease();
            }
        }
        int pixelsConsumed = Math.round(consumed * (float) this.getHeight());
        if (pixelsConsumed != 0) {
            this.invalidate();
        }
        return pixelsConsumed;
    }

    private void runAnimatedScroll(boolean participateInNestedScrolling) {
        if (participateInNestedScrolling) {
            this.startNestedScroll(2, 1);
        } else {
            this.stopNestedScroll(1);
        }
        this.mLastScrollerY = this.getScrollY();
        this.postInvalidateOnAnimation();
    }

    private void abortAnimatedScroll() {
        this.mScroller.abortAnimation();
        this.stopNestedScroll(1);
    }

    private void scrollToChild(@Nonnull View child) {
        child.getDrawingRect(this.mTempRect);
        this.offsetDescendantRectToMyCoords(child, this.mTempRect);
        int scrollDelta = this.computeScrollDeltaToGetChildRectOnScreen(this.mTempRect);
        if (scrollDelta != 0) {
            this.scrollBy(0, scrollDelta);
        }
    }

    private boolean scrollToChildRect(Rect rect, boolean immediate) {
        int delta = this.computeScrollDeltaToGetChildRectOnScreen(rect);
        boolean scroll = delta != 0;
        if (scroll) {
            if (immediate) {
                this.scrollBy(0, delta);
            } else {
                this.smoothScrollBy(0, delta);
            }
        }
        return scroll;
    }

    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        if (this.getChildCount() == 0) {
            return 0;
        } else {
            int height = this.getHeight();
            int screenTop = this.getScrollY();
            int screenBottom = screenTop + height;
            int actualScreenBottom = screenBottom;
            int fadingEdge = 0;
            if (rect.top > 0) {
                screenTop += fadingEdge;
            }
            View child = this.getChildAt(0);
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) child.getLayoutParams();
            if (rect.bottom < child.getHeight() + lp.topMargin + lp.bottomMargin) {
                screenBottom -= fadingEdge;
            }
            int scrollYDelta = 0;
            if (rect.bottom > screenBottom && rect.top > screenTop) {
                if (rect.height() > height) {
                    scrollYDelta += rect.top - screenTop;
                } else {
                    scrollYDelta += rect.bottom - screenBottom;
                }
                int bottom = child.getBottom() + lp.bottomMargin;
                int distanceToBottom = bottom - actualScreenBottom;
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
        if (!this.mIsLayoutDirty) {
            this.scrollToChild(focused);
        } else {
            this.mChildToScrollTo = focused;
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
        super.onLayout(changed, l, t, r, b);
        this.mIsLayoutDirty = false;
        if (this.mChildToScrollTo != null && isViewDescendantOf(this.mChildToScrollTo, this)) {
            this.scrollToChild(this.mChildToScrollTo);
        }
        this.mChildToScrollTo = null;
        if (!this.mIsLaidOut) {
            int childSize = 0;
            if (this.getChildCount() > 0) {
                View child = this.getChildAt(0);
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) child.getLayoutParams();
                childSize = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            }
            int parentSpace = b - t - this.getPaddingTop() - this.getPaddingBottom();
            int currentScrollY = this.getScrollY();
            int newScrollY = clamp(currentScrollY, parentSpace, childSize);
            if (newScrollY != currentScrollY) {
                this.scrollTo(this.getScrollX(), newScrollY);
            }
        }
        this.scrollTo(this.getScrollX(), this.getScrollY());
        this.mIsLaidOut = true;
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mIsLaidOut = false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        View currentFocused = this.findFocus();
        if (null != currentFocused && this != currentFocused) {
            if (this.isWithinDeltaOfScreen(currentFocused, 0, oldh)) {
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
            this.mScroller.fling(this.getScrollX(), this.getScrollY(), 0, velocityY, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
            this.runAnimatedScroll(true);
        }
    }

    private void endDrag() {
        this.mIsBeingDragged = false;
        this.recycleVelocityTracker();
        this.stopNestedScroll(0);
        this.mEdgeGlowTop.onRelease();
        this.mEdgeGlowBottom.onRelease();
    }

    @Override
    public void scrollTo(int x, int y) {
        if (this.getChildCount() > 0) {
            View child = this.getChildAt(0);
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) child.getLayoutParams();
            int parentSpaceHorizontal = this.getWidth() - this.getPaddingLeft() - this.getPaddingRight();
            int childSizeHorizontal = child.getWidth() + lp.leftMargin + lp.rightMargin;
            int parentSpaceVertical = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
            int childSizeVertical = child.getHeight() + lp.topMargin + lp.bottomMargin;
            x = clamp(x, parentSpaceHorizontal, childSizeHorizontal);
            y = clamp(y, parentSpaceVertical, childSizeVertical);
            if (x != this.getScrollX() || y != this.getScrollY()) {
                super.scrollTo(x, y);
            }
        }
    }

    @Override
    public void onDrawForeground(@Nonnull Canvas canvas) {
        super.onDrawForeground(canvas);
        int scrollY = this.getScrollY();
        if (!this.mEdgeGlowTop.isFinished()) {
            int restoreCount = canvas.save();
            int width = this.getWidth();
            int height = this.getHeight();
            int xTranslation = 0;
            int yTranslation = Math.min(0, scrollY);
            if (this.getClipToPadding()) {
                width -= this.getPaddingLeft() + this.getPaddingRight();
                xTranslation += this.getPaddingLeft();
                height -= this.getPaddingTop() + this.getPaddingBottom();
                yTranslation += this.getPaddingTop();
            }
            canvas.translate((float) xTranslation, (float) yTranslation);
            this.mEdgeGlowTop.setSize(width, height);
            if (this.mEdgeGlowTop.draw(canvas)) {
                this.postInvalidateOnAnimation();
            }
            canvas.restoreToCount(restoreCount);
        }
        if (!this.mEdgeGlowBottom.isFinished()) {
            int restoreCountx = canvas.save();
            int widthx = this.getWidth();
            int heightx = this.getHeight();
            int xTranslationx = 0;
            int yTranslationx = Math.max(this.getScrollRange(), scrollY) + heightx;
            if (this.getClipToPadding()) {
                widthx -= this.getPaddingLeft() + this.getPaddingRight();
                xTranslationx += this.getPaddingLeft();
                heightx -= this.getPaddingTop() + this.getPaddingBottom();
                yTranslationx -= this.getPaddingBottom();
            }
            canvas.translate((float) (xTranslationx - widthx), (float) yTranslationx);
            canvas.rotate(180.0F, (float) widthx, 0.0F);
            this.mEdgeGlowBottom.setSize(widthx, heightx);
            if (this.mEdgeGlowBottom.draw(canvas)) {
                this.postInvalidateOnAnimation();
            }
            canvas.restoreToCount(restoreCountx);
        }
    }

    private static int clamp(int n, int my, int child) {
        if (my < child && n >= 0) {
            return my + n > child ? child - my : n;
        } else {
            return 0;
        }
    }

    @FunctionalInterface
    public interface OnScrollChangeListener {

        void onScrollChange(NestedScrollView var1, int var2, int var3, int var4, int var5);
    }
}