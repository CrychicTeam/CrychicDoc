package icyllis.modernui.widget;

import icyllis.modernui.core.Context;
import icyllis.modernui.core.Core;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.MathUtil;
import icyllis.modernui.graphics.Matrix;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.RectF;
import icyllis.modernui.util.Pools;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.view.ViewParent;
import icyllis.modernui.view.ViewTreeObserver;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import org.jetbrains.annotations.VisibleForTesting;

public class CoordinatorLayout extends ViewGroup {

    private static final ThreadLocal<Matrix> sMatrix = ThreadLocal.withInitial(Matrix::new);

    private static final ThreadLocal<RectF> sRectF = ThreadLocal.withInitial(RectF::new);

    private static final int TYPE_ON_INTERCEPT = 0;

    private static final int TYPE_ON_TOUCH = 1;

    static final int EVENT_PRE_DRAW = 0;

    static final int EVENT_NESTED_SCROLL = 1;

    static final int EVENT_VIEW_REMOVED = 2;

    static final Comparator<View> TOP_SORTED_CHILDREN_COMPARATOR = (lhs, rhs) -> Float.compare(rhs.getZ(), lhs.getZ());

    private static final Pools.Pool<Rect> sRectPool = Pools.newSynchronizedPool(12);

    private final List<View> mDependencySortedChildren = new ArrayList();

    private final DirectedAcyclicGraph<View> mChildDag = new DirectedAcyclicGraph<>();

    private final List<View> mTempList1 = new ArrayList();

    private final int[] mBehaviorConsumed = new int[2];

    private boolean mDisallowInterceptReset;

    private View mBehaviorTouchView;

    private View mNestedScrollingTarget;

    private CoordinatorLayout.OnPreDrawListener mOnPreDrawListener;

    private boolean mNeedsPreDrawListener;

    @Nonnull
    private static Rect acquireTempRect() {
        Rect rect = sRectPool.acquire();
        if (rect == null) {
            rect = new Rect();
        }
        return rect;
    }

    private static void releaseTempRect(@Nonnull Rect rect) {
        rect.setEmpty();
        sRectPool.release(rect);
    }

    public CoordinatorLayout(Context context) {
        super(context);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.resetTouchBehaviors();
        if (this.mNeedsPreDrawListener) {
            if (this.mOnPreDrawListener == null) {
                this.mOnPreDrawListener = new CoordinatorLayout.OnPreDrawListener();
            }
            ViewTreeObserver vto = this.getViewTreeObserver();
            vto.addOnPreDrawListener(this.mOnPreDrawListener);
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.resetTouchBehaviors();
        if (this.mNeedsPreDrawListener && this.mOnPreDrawListener != null) {
            ViewTreeObserver vto = this.getViewTreeObserver();
            vto.removeOnPreDrawListener(this.mOnPreDrawListener);
        }
        if (this.mNestedScrollingTarget != null) {
            this.onStopNestedScroll(this.mNestedScrollingTarget, 0);
        }
    }

    private void cancelInterceptBehaviors() {
        MotionEvent cancelEvent = null;
        int childCount = this.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = this.getChildAt(i);
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            CoordinatorLayout.Behavior<View> b = lp.getBehavior();
            if (b != null) {
                if (cancelEvent == null) {
                    long now = Core.timeNanos();
                    cancelEvent = MotionEvent.obtain(now, 3, 0.0F, 0.0F, 0);
                }
                b.onInterceptTouchEvent(this, child, cancelEvent);
            }
        }
        if (cancelEvent != null) {
            cancelEvent.recycle();
        }
    }

    private void resetTouchBehaviors() {
        if (this.mBehaviorTouchView != null) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) this.mBehaviorTouchView.getLayoutParams();
            CoordinatorLayout.Behavior<View> b = lp.getBehavior();
            if (b != null) {
                long now = Core.timeNanos();
                MotionEvent cancelEvent = MotionEvent.obtain(now, 3, 0.0F, 0.0F, 0);
                b.onTouchEvent(this, this.mBehaviorTouchView, cancelEvent);
                cancelEvent.recycle();
            }
            this.mBehaviorTouchView = null;
        }
        int childCount = this.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = this.getChildAt(i);
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            lp.resetTouchBehaviorTracking();
        }
        this.mDisallowInterceptReset = false;
    }

    private void getTopSortedChildren(@Nonnull List<View> out) {
        out.clear();
        boolean useCustomOrder = this.isChildrenDrawingOrderEnabled();
        int childCount = this.getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            int childIndex = useCustomOrder ? this.getChildDrawingOrder(childCount, i) : i;
            View child = this.getChildAt(childIndex);
            out.add(child);
        }
        out.sort(TOP_SORTED_CHILDREN_COMPARATOR);
    }

    private boolean performIntercept(@Nonnull MotionEvent ev, int type) {
        boolean intercepted = false;
        boolean newBlock = false;
        int action = ev.getAction();
        MotionEvent cancelEvent = null;
        List<View> topmostChildList = this.mTempList1;
        this.getTopSortedChildren(topmostChildList);
        int childCount = topmostChildList.size();
        for (int i = 0; i < childCount; i++) {
            View child = (View) topmostChildList.get(i);
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            CoordinatorLayout.Behavior<View> b = lp.getBehavior();
            if ((intercepted || newBlock) && action != 0) {
                if (b != null) {
                    if (cancelEvent == null) {
                        cancelEvent = this.obtainCancelEvent(ev);
                    }
                    this.performEvent(b, child, cancelEvent, type);
                }
            } else {
                if (!newBlock && !intercepted && b != null) {
                    intercepted = this.performEvent(b, child, ev, type);
                    if (intercepted) {
                        this.mBehaviorTouchView = child;
                        if (action != 3 && action != 1) {
                            for (int j = 0; j < i; j++) {
                                View priorChild = (View) topmostChildList.get(j);
                                CoordinatorLayout.Behavior<View> priorBehavior = ((CoordinatorLayout.LayoutParams) priorChild.getLayoutParams()).getBehavior();
                                if (priorBehavior != null) {
                                    if (cancelEvent == null) {
                                        cancelEvent = this.obtainCancelEvent(ev);
                                    }
                                    this.performEvent(priorBehavior, priorChild, cancelEvent, type);
                                }
                            }
                        }
                    }
                }
                boolean wasBlocking = lp.didBlockInteraction();
                boolean isBlocking = lp.isBlockingInteractionBelow(this, child);
                newBlock = isBlocking && !wasBlocking;
                if (isBlocking && !newBlock) {
                    break;
                }
            }
        }
        topmostChildList.clear();
        if (cancelEvent != null) {
            cancelEvent.recycle();
        }
        return intercepted;
    }

    private boolean performEvent(CoordinatorLayout.Behavior<View> behavior, View child, MotionEvent ev, int type) {
        switch(type) {
            case 0:
                return behavior.onInterceptTouchEvent(this, child, ev);
            case 1:
                return behavior.onTouchEvent(this, child, ev);
            default:
                throw new IllegalArgumentException();
        }
    }

    @Nonnull
    private MotionEvent obtainCancelEvent(@Nonnull MotionEvent other) {
        MotionEvent event = other.copy();
        event.setAction(3);
        return event;
    }

    @Override
    public boolean onInterceptTouchEvent(@Nonnull MotionEvent ev) {
        int action = ev.getAction();
        if (action == 0) {
            this.resetTouchBehaviors();
        }
        boolean intercepted = this.performIntercept(ev, 0);
        if (action == 1 || action == 3) {
            this.mBehaviorTouchView = null;
            this.resetTouchBehaviors();
        }
        return intercepted;
    }

    @Override
    public boolean onTouchEvent(@Nonnull MotionEvent ev) {
        boolean handled = false;
        boolean cancelSuper = false;
        int action = ev.getAction();
        if (this.mBehaviorTouchView != null) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) this.mBehaviorTouchView.getLayoutParams();
            CoordinatorLayout.Behavior<View> b = lp.getBehavior();
            if (b != null) {
                handled = b.onTouchEvent(this, this.mBehaviorTouchView, ev);
            }
        } else {
            handled = this.performIntercept(ev, 1);
            cancelSuper = action != 0 && handled;
        }
        if (this.mBehaviorTouchView == null || action == 3) {
            handled |= super.onTouchEvent(ev);
        } else if (cancelSuper) {
            MotionEvent cancelEvent = this.obtainCancelEvent(ev);
            super.onTouchEvent(cancelEvent);
            cancelEvent.recycle();
        }
        if (action == 1 || action == 3) {
            this.mBehaviorTouchView = null;
            this.resetTouchBehaviors();
        }
        return handled;
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
        if (disallowIntercept && !this.mDisallowInterceptReset) {
            if (this.mBehaviorTouchView == null) {
                this.cancelInterceptBehaviors();
            }
            this.resetTouchBehaviors();
            this.mDisallowInterceptReset = true;
        }
    }

    CoordinatorLayout.LayoutParams getResolvedLayoutParams(@Nonnull View child) {
        CoordinatorLayout.LayoutParams result = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        if (!result.mBehaviorResolved && child instanceof CoordinatorLayout.AttachedBehavior) {
            CoordinatorLayout.Behavior<?> attachedBehavior = ((CoordinatorLayout.AttachedBehavior) child).getBehavior();
            result.setBehavior(attachedBehavior);
            result.mBehaviorResolved = true;
        }
        return result;
    }

    private void prepareChildren() {
        this.mDependencySortedChildren.clear();
        this.mChildDag.clear();
        int i = 0;
        for (int count = this.getChildCount(); i < count; i++) {
            View view = this.getChildAt(i);
            CoordinatorLayout.LayoutParams lp = this.getResolvedLayoutParams(view);
            lp.findAnchorView(this, view);
            this.mChildDag.addNode(view);
            for (int j = 0; j < count; j++) {
                if (j != i) {
                    View other = this.getChildAt(j);
                    if (lp.dependsOn(this, view, other)) {
                        if (!this.mChildDag.contains(other)) {
                            this.mChildDag.addNode(other);
                        }
                        this.mChildDag.addEdge(other, view);
                    }
                }
            }
        }
        this.mDependencySortedChildren.addAll(this.mChildDag.getSortedList());
        Collections.reverse(this.mDependencySortedChildren);
    }

    void getDescendantRect(@Nonnull View descendant, @Nonnull Rect out) {
        out.set(0, 0, descendant.getWidth(), descendant.getHeight());
        this.offsetDescendantRect(descendant, out);
    }

    void offsetDescendantRect(View descendant, Rect rect) {
        Matrix m = (Matrix) sMatrix.get();
        m.setIdentity();
        this.offsetDescendantMatrix(descendant, m);
        RectF rectF = (RectF) sRectF.get();
        rectF.set(rect);
        m.mapRect(rectF);
        rectF.round(rect);
    }

    private void offsetDescendantMatrix(@Nonnull View view, Matrix m) {
        ViewParent parent = view.getParent();
        if (parent instanceof View vp && parent != this) {
            this.offsetDescendantMatrix(vp, m);
            m.preTranslate((float) (-vp.getScrollX()), (float) (-vp.getScrollY()));
        }
        m.preTranslate((float) view.getLeft(), (float) view.getTop());
        if (!view.getMatrix().isIdentity()) {
            m.preConcat(view.getMatrix());
        }
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return Math.max(super.getSuggestedMinimumWidth(), this.getPaddingLeft() + this.getPaddingRight());
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return Math.max(super.getSuggestedMinimumHeight(), this.getPaddingTop() + this.getPaddingBottom());
    }

    public void onMeasureChild(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        this.measureChildWithMargins(child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.prepareChildren();
        this.ensurePreDrawListener();
        int paddingLeft = this.getPaddingLeft();
        int paddingTop = this.getPaddingTop();
        int paddingRight = this.getPaddingRight();
        int paddingBottom = this.getPaddingBottom();
        int widthPadding = paddingLeft + paddingRight;
        int heightPadding = paddingTop + paddingBottom;
        int widthUsed = this.getSuggestedMinimumWidth();
        int heightUsed = this.getSuggestedMinimumHeight();
        int childState = 0;
        for (View child : this.mDependencySortedChildren) {
            if (child.getVisibility() != 8) {
                CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
                CoordinatorLayout.Behavior<View> b = lp.getBehavior();
                if (b == null || !b.onMeasureChild(this, child, widthMeasureSpec, 0, heightMeasureSpec, 0)) {
                    this.onMeasureChild(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                }
                widthUsed = Math.max(widthUsed, widthPadding + child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                heightUsed = Math.max(heightUsed, heightPadding + child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
                childState = View.combineMeasuredStates(childState, child.getMeasuredState());
            }
        }
        int width = View.resolveSizeAndState(widthUsed, widthMeasureSpec, childState & 0xFF000000);
        int height = View.resolveSizeAndState(heightUsed, heightMeasureSpec, childState << 16);
        this.setMeasuredDimension(width, height);
    }

    public void onLayoutChild(@Nonnull View child, int layoutDirection) {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        if (lp.checkAnchorChanged()) {
            throw new IllegalStateException("An anchor may not be changed after CoordinatorLayout measurement begins before layout is complete.");
        } else {
            if (lp.mAnchorView != null) {
                this.layoutChildWithAnchor(child, lp.mAnchorView, layoutDirection);
            } else {
                this.layoutChild(child, layoutDirection);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int layoutDirection = this.getLayoutDirection();
        for (View child : this.mDependencySortedChildren) {
            if (child.getVisibility() != 8) {
                CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
                CoordinatorLayout.Behavior<View> behavior = lp.getBehavior();
                if (behavior == null || !behavior.onLayoutChild(this, child, layoutDirection)) {
                    this.onLayoutChild(child, layoutDirection);
                }
            }
        }
    }

    void recordLastChildRect(@Nonnull View child, Rect r) {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        lp.setLastChildRect(r);
    }

    void getLastChildRect(@Nonnull View child, @Nonnull Rect out) {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        out.set(lp.getLastChildRect());
    }

    void getChildRect(@Nonnull View child, boolean transform, Rect out) {
        if (!child.isLayoutRequested() && child.getVisibility() != 8) {
            if (transform) {
                this.getDescendantRect(child, out);
            } else {
                out.set(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
            }
        } else {
            out.setEmpty();
        }
    }

    private void getDesiredAnchoredChildRectWithoutConstraints(int layoutDirection, @Nonnull Rect anchorRect, Rect out, @Nonnull CoordinatorLayout.LayoutParams lp, int childWidth, int childHeight) {
        int absGravity = Gravity.getAbsoluteGravity(resolveAnchoredChildGravity(lp.gravity), layoutDirection);
        int absAnchorGravity = Gravity.getAbsoluteGravity(resolveGravity(lp.anchorGravity), layoutDirection);
        int hgrav = absGravity & 7;
        int vgrav = absGravity & 112;
        int anchorHgrav = absAnchorGravity & 7;
        int anchorVgrav = absAnchorGravity & 112;
        int left = switch(anchorHgrav) {
            case 1 ->
                anchorRect.left + anchorRect.width() / 2;
            case 5 ->
                anchorRect.right;
            default ->
                anchorRect.left;
        };
        int top = switch(anchorVgrav) {
            case 16 ->
                anchorRect.top + anchorRect.height() / 2;
            case 80 ->
                anchorRect.bottom;
            default ->
                anchorRect.top;
        };
        switch(hgrav) {
            case 1:
                left -= childWidth / 2;
                break;
            case 2:
            case 3:
            case 4:
            default:
                left -= childWidth;
            case 5:
        }
        switch(vgrav) {
            case 16:
                top -= childHeight / 2;
                break;
            case 48:
            default:
                top -= childHeight;
            case 80:
        }
        out.set(left, top, left + childWidth, top + childHeight);
    }

    private void constrainChildRect(@Nonnull CoordinatorLayout.LayoutParams lp, Rect out, int childWidth, int childHeight) {
        int width = this.getWidth();
        int height = this.getHeight();
        int left = Math.max(this.getPaddingLeft() + lp.leftMargin, Math.min(out.left, width - this.getPaddingRight() - childWidth - lp.rightMargin));
        int top = Math.max(this.getPaddingTop() + lp.topMargin, Math.min(out.top, height - this.getPaddingBottom() - childHeight - lp.bottomMargin));
        out.set(left, top, left + childWidth, top + childHeight);
    }

    void getDesiredAnchoredChildRect(@Nonnull View child, int layoutDirection, Rect anchorRect, Rect out) {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        int childWidth = child.getMeasuredWidth();
        int childHeight = child.getMeasuredHeight();
        this.getDesiredAnchoredChildRectWithoutConstraints(layoutDirection, anchorRect, out, lp, childWidth, childHeight);
        this.constrainChildRect(lp, out, childWidth, childHeight);
    }

    private void layoutChildWithAnchor(View child, View anchor, int layoutDirection) {
        Rect anchorRect = acquireTempRect();
        Rect childRect = acquireTempRect();
        try {
            this.getDescendantRect(anchor, anchorRect);
            this.getDesiredAnchoredChildRect(child, layoutDirection, anchorRect, childRect);
            child.layout(childRect.left, childRect.top, childRect.right, childRect.bottom);
        } finally {
            releaseTempRect(anchorRect);
            releaseTempRect(childRect);
        }
    }

    private void layoutChild(@Nonnull View child, int layoutDirection) {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        Rect parent = acquireTempRect();
        parent.set(this.getPaddingLeft() + lp.leftMargin, this.getPaddingTop() + lp.topMargin, this.getWidth() - this.getPaddingRight() - lp.rightMargin, this.getHeight() - this.getPaddingBottom() - lp.bottomMargin);
        Rect out = acquireTempRect();
        Gravity.apply(resolveGravity(lp.gravity), child.getMeasuredWidth(), child.getMeasuredHeight(), parent, out, layoutDirection);
        child.layout(out.left, out.top, out.right, out.bottom);
        releaseTempRect(parent);
        releaseTempRect(out);
    }

    private static int resolveGravity(int gravity) {
        if ((gravity & 7) == 0) {
            gravity |= 8388611;
        }
        if ((gravity & 112) == 0) {
            gravity |= 48;
        }
        return gravity;
    }

    private static int resolveAnchoredChildGravity(int gravity) {
        return gravity == 0 ? 17 : gravity;
    }

    @Override
    protected void drawChild(@Nonnull Canvas canvas, @Nonnull View child, long drawingTime) {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        if (lp.mBehavior != null) {
            float scrimAlpha = lp.mBehavior.getScrimOpacity(this, child);
            if (scrimAlpha > 0.0F) {
                Paint paint = Paint.obtain();
                paint.setColor(lp.mBehavior.getScrimColor(this, child));
                paint.setAlpha(MathUtil.clamp(Math.round(255.0F * scrimAlpha), 0, 255));
                canvas.drawRect((float) this.getPaddingLeft(), (float) this.getPaddingTop(), (float) (this.getWidth() - this.getPaddingRight()), (float) (this.getHeight() - this.getPaddingBottom()), paint);
                paint.recycle();
            }
        }
        super.drawChild(canvas, child, drawingTime);
    }

    final void onChildViewsChanged(int type) {
        int layoutDirection = this.getLayoutDirection();
        int childCount = this.mDependencySortedChildren.size();
        Rect inset = acquireTempRect();
        Rect drawRect = acquireTempRect();
        Rect lastDrawRect = acquireTempRect();
        for (int i = 0; i < childCount; i++) {
            View child = (View) this.mDependencySortedChildren.get(i);
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            if (type != 0 || child.getVisibility() != 8) {
                for (int j = 0; j < i; j++) {
                    View checkChild = (View) this.mDependencySortedChildren.get(j);
                    if (lp.mAnchorDirectChild == checkChild) {
                        this.offsetChildToAnchor(child, layoutDirection);
                    }
                }
                this.getChildRect(child, true, drawRect);
                if (lp.insetEdge != 0 && !drawRect.isEmpty()) {
                    int absInsetEdge = Gravity.getAbsoluteGravity(lp.insetEdge, layoutDirection);
                    switch(absInsetEdge & 112) {
                        case 48:
                            inset.top = Math.max(inset.top, drawRect.bottom);
                            break;
                        case 80:
                            inset.bottom = Math.max(inset.bottom, this.getHeight() - drawRect.top);
                    }
                    switch(absInsetEdge & 7) {
                        case 3:
                            inset.left = Math.max(inset.left, drawRect.right);
                            break;
                        case 5:
                            inset.right = Math.max(inset.right, this.getWidth() - drawRect.left);
                    }
                }
                if (lp.dodgeInsetEdges != 0 && child.getVisibility() == 0) {
                    this.offsetChildByInset(child, inset, layoutDirection);
                }
                if (type != 2) {
                    this.getLastChildRect(child, lastDrawRect);
                    if (lastDrawRect.equals(drawRect)) {
                        continue;
                    }
                    this.recordLastChildRect(child, drawRect);
                }
                for (int jx = i + 1; jx < childCount; jx++) {
                    View checkChild = (View) this.mDependencySortedChildren.get(jx);
                    CoordinatorLayout.LayoutParams checkLp = (CoordinatorLayout.LayoutParams) checkChild.getLayoutParams();
                    CoordinatorLayout.Behavior<View> b = checkLp.getBehavior();
                    if (b != null && b.layoutDependsOn(this, checkChild, child)) {
                        if (type == 0 && checkLp.getChangedAfterNestedScroll()) {
                            checkLp.resetChangedAfterNestedScroll();
                        } else {
                            boolean handled;
                            if (type == 2) {
                                b.onDependentViewRemoved(this, checkChild, child);
                                handled = true;
                            } else {
                                handled = b.onDependentViewChanged(this, checkChild, child);
                            }
                            if (type == 1) {
                                checkLp.setChangedAfterNestedScroll(handled);
                            }
                        }
                    }
                }
            }
        }
        releaseTempRect(inset);
        releaseTempRect(drawRect);
        releaseTempRect(lastDrawRect);
    }

    private void offsetChildByInset(@Nonnull View child, Rect inset, int layoutDirection) {
        if (child.isLaidOut()) {
            if (child.getWidth() > 0 && child.getHeight() > 0) {
                CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
                CoordinatorLayout.Behavior<View> behavior = lp.getBehavior();
                Rect dodgeRect = acquireTempRect();
                Rect bounds = acquireTempRect();
                bounds.set(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
                if (behavior != null && behavior.getInsetDodgeRect(this, child, dodgeRect)) {
                    if (!bounds.contains(dodgeRect)) {
                        throw new IllegalArgumentException("Rect should be within the child's bounds. Rect:" + dodgeRect.toShortString() + " | Bounds:" + bounds.toShortString());
                    }
                } else {
                    dodgeRect.set(bounds);
                }
                releaseTempRect(bounds);
                if (dodgeRect.isEmpty()) {
                    releaseTempRect(dodgeRect);
                } else {
                    int absDodgeInsetEdges = Gravity.getAbsoluteGravity(lp.dodgeInsetEdges, layoutDirection);
                    boolean offsetY = false;
                    if ((absDodgeInsetEdges & 48) == 48) {
                        int distance = dodgeRect.top - lp.topMargin - lp.mInsetOffsetY;
                        if (distance < inset.top) {
                            this.setInsetOffsetY(child, inset.top - distance);
                            offsetY = true;
                        }
                    }
                    if ((absDodgeInsetEdges & 80) == 80) {
                        int distance = this.getHeight() - dodgeRect.bottom - lp.bottomMargin + lp.mInsetOffsetY;
                        if (distance < inset.bottom) {
                            this.setInsetOffsetY(child, distance - inset.bottom);
                            offsetY = true;
                        }
                    }
                    if (!offsetY) {
                        this.setInsetOffsetY(child, 0);
                    }
                    boolean offsetX = false;
                    if ((absDodgeInsetEdges & 3) == 3) {
                        int distance = dodgeRect.left - lp.leftMargin - lp.mInsetOffsetX;
                        if (distance < inset.left) {
                            this.setInsetOffsetX(child, inset.left - distance);
                            offsetX = true;
                        }
                    }
                    if ((absDodgeInsetEdges & 5) == 5) {
                        int distance = this.getWidth() - dodgeRect.right - lp.rightMargin + lp.mInsetOffsetX;
                        if (distance < inset.right) {
                            this.setInsetOffsetX(child, distance - inset.right);
                            offsetX = true;
                        }
                    }
                    if (!offsetX) {
                        this.setInsetOffsetX(child, 0);
                    }
                    releaseTempRect(dodgeRect);
                }
            }
        }
    }

    private void setInsetOffsetX(@Nonnull View child, int offsetX) {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        if (lp.mInsetOffsetX != offsetX) {
            int dx = offsetX - lp.mInsetOffsetX;
            child.offsetLeftAndRight(dx);
            lp.mInsetOffsetX = offsetX;
        }
    }

    private void setInsetOffsetY(@Nonnull View child, int offsetY) {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        if (lp.mInsetOffsetY != offsetY) {
            int dy = offsetY - lp.mInsetOffsetY;
            child.offsetTopAndBottom(dy);
            lp.mInsetOffsetY = offsetY;
        }
    }

    public void dispatchDependentViewsChanged(@Nonnull View view) {
        List<View> dependents = this.mChildDag.getIncomingEdgesInternal(view);
        if (dependents != null && !dependents.isEmpty()) {
            for (View child : dependents) {
                CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
                CoordinatorLayout.Behavior<View> b = lp.getBehavior();
                if (b != null) {
                    b.onDependentViewChanged(this, child, view);
                }
            }
        }
    }

    @Nonnull
    public List<View> getDependencies(@Nonnull View child) {
        List<View> result = this.mChildDag.getOutgoingEdges(child);
        return result == null ? Collections.emptyList() : result;
    }

    @Nonnull
    public List<View> getDependents(@Nonnull View child) {
        List<View> result = this.mChildDag.getIncomingEdges(child);
        return result == null ? Collections.emptyList() : result;
    }

    @Nonnull
    @VisibleForTesting
    @UnmodifiableView
    public final List<View> getDependencySortedChildren() {
        this.prepareChildren();
        return Collections.unmodifiableList(this.mDependencySortedChildren);
    }

    void ensurePreDrawListener() {
        boolean hasDependencies = false;
        int childCount = this.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = this.getChildAt(i);
            if (this.hasDependencies(child)) {
                hasDependencies = true;
                break;
            }
        }
        if (hasDependencies != this.mNeedsPreDrawListener) {
            if (hasDependencies) {
                this.addPreDrawListener();
            } else {
                this.removePreDrawListener();
            }
        }
    }

    private boolean hasDependencies(View child) {
        return this.mChildDag.hasOutgoingEdges(child);
    }

    void addPreDrawListener() {
        if (this.isAttachedToWindow()) {
            if (this.mOnPreDrawListener == null) {
                this.mOnPreDrawListener = new CoordinatorLayout.OnPreDrawListener();
            }
            ViewTreeObserver vto = this.getViewTreeObserver();
            vto.addOnPreDrawListener(this.mOnPreDrawListener);
        }
        this.mNeedsPreDrawListener = true;
    }

    void removePreDrawListener() {
        if (this.isAttachedToWindow() && this.mOnPreDrawListener != null) {
            ViewTreeObserver vto = this.getViewTreeObserver();
            vto.removeOnPreDrawListener(this.mOnPreDrawListener);
        }
        this.mNeedsPreDrawListener = false;
    }

    void offsetChildToAnchor(@Nonnull View child, int layoutDirection) {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        if (lp.mAnchorView != null) {
            Rect anchorRect = acquireTempRect();
            Rect childRect = acquireTempRect();
            Rect desiredChildRect = acquireTempRect();
            this.getDescendantRect(lp.mAnchorView, anchorRect);
            this.getChildRect(child, false, childRect);
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            this.getDesiredAnchoredChildRectWithoutConstraints(layoutDirection, anchorRect, desiredChildRect, lp, childWidth, childHeight);
            boolean changed = desiredChildRect.left != childRect.left || desiredChildRect.top != childRect.top;
            this.constrainChildRect(lp, desiredChildRect, childWidth, childHeight);
            int dx = desiredChildRect.left - childRect.left;
            int dy = desiredChildRect.top - childRect.top;
            if (dx != 0) {
                child.offsetLeftAndRight(dx);
            }
            if (dy != 0) {
                child.offsetTopAndBottom(dy);
            }
            if (changed) {
                CoordinatorLayout.Behavior<View> b = lp.getBehavior();
                if (b != null) {
                    b.onDependentViewChanged(this, child, lp.mAnchorView);
                }
            }
            releaseTempRect(anchorRect);
            releaseTempRect(childRect);
            releaseTempRect(desiredChildRect);
        }
    }

    public boolean isPointInChildBounds(@Nonnull View child, int x, int y) {
        Rect r = acquireTempRect();
        this.offsetDescendantRectToMyCoords(child, r);
        boolean var5;
        try {
            var5 = r.contains(x, y);
        } finally {
            releaseTempRect(r);
        }
        return var5;
    }

    public boolean doViewsOverlap(@Nonnull View first, @Nonnull View second) {
        if (first.getVisibility() == 0 && second.getVisibility() == 0) {
            Rect firstRect = acquireTempRect();
            this.getChildRect(first, first.getParent() != this, firstRect);
            Rect secondRect = acquireTempRect();
            this.getChildRect(second, second.getParent() != this, secondRect);
            boolean var5;
            try {
                var5 = firstRect.left <= secondRect.right && firstRect.top <= secondRect.bottom && firstRect.right >= secondRect.left && firstRect.bottom >= secondRect.top;
            } finally {
                releaseTempRect(firstRect);
                releaseTempRect(secondRect);
            }
            return var5;
        } else {
            return false;
        }
    }

    @Override
    protected void onViewRemoved(View child) {
        super.onViewRemoved(child);
        this.onChildViewsChanged(2);
    }

    @Override
    public boolean requestChildRectangleOnScreen(@Nonnull View child, Rect rectangle, boolean immediate) {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        CoordinatorLayout.Behavior<View> behavior = lp.getBehavior();
        return behavior != null && behavior.onRequestChildRectangleOnScreen(this, child, rectangle, immediate) ? true : super.requestChildRectangleOnScreen(child, rectangle, immediate);
    }

    @Nonnull
    protected CoordinatorLayout.LayoutParams generateLayoutParams(@Nonnull ViewGroup.LayoutParams p) {
        if (p instanceof CoordinatorLayout.LayoutParams) {
            return new CoordinatorLayout.LayoutParams((ViewGroup.MarginLayoutParams) ((CoordinatorLayout.LayoutParams) p));
        } else if (p instanceof FrameLayout.LayoutParams vp) {
            CoordinatorLayout.LayoutParams lp = new CoordinatorLayout.LayoutParams((ViewGroup.MarginLayoutParams) vp);
            if (vp.gravity != -1) {
                lp.gravity = vp.gravity;
            }
            return lp;
        } else {
            return p instanceof ViewGroup.MarginLayoutParams ? new CoordinatorLayout.LayoutParams((ViewGroup.MarginLayoutParams) p) : new CoordinatorLayout.LayoutParams(p);
        }
    }

    @Nonnull
    protected CoordinatorLayout.LayoutParams generateDefaultLayoutParams() {
        return new CoordinatorLayout.LayoutParams(-2, -2);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof CoordinatorLayout.LayoutParams;
    }

    @Override
    public boolean onStartNestedScroll(@Nonnull View child, @Nonnull View target, int axes, int type) {
        boolean handled = false;
        int childCount = this.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = this.getChildAt(i);
            if (view.getVisibility() != 8) {
                CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
                CoordinatorLayout.Behavior<View> viewBehavior = lp.getBehavior();
                if (viewBehavior != null) {
                    boolean accepted = viewBehavior.onStartNestedScroll(this, view, child, target, axes, type);
                    handled |= accepted;
                    lp.setNestedScrollAccepted(type, accepted);
                } else {
                    lp.setNestedScrollAccepted(type, false);
                }
            }
        }
        return handled;
    }

    @Override
    public void onNestedScrollAccepted(@Nonnull View child, @Nonnull View target, int axes, int type) {
        super.onNestedScrollAccepted(child, target, axes, type);
        this.mNestedScrollingTarget = target;
        int childCount = this.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = this.getChildAt(i);
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
            if (!lp.isNestedScrollDenied(type)) {
                CoordinatorLayout.Behavior<View> viewBehavior = lp.getBehavior();
                if (viewBehavior != null) {
                    viewBehavior.onNestedScrollAccepted(this, view, child, target, axes, type);
                }
            }
        }
    }

    @Override
    public void onStopNestedScroll(@Nonnull View target, int type) {
        super.onStopNestedScroll(target, type);
        int childCount = this.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = this.getChildAt(i);
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
            if (!lp.isNestedScrollDenied(type)) {
                CoordinatorLayout.Behavior<View> viewBehavior = lp.getBehavior();
                if (viewBehavior != null) {
                    viewBehavior.onStopNestedScroll(this, view, target, type);
                }
                lp.resetNestedScroll(type);
                lp.resetChangedAfterNestedScroll();
            }
        }
        this.mNestedScrollingTarget = null;
    }

    @Override
    public void onNestedScroll(@Nonnull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @Nonnull int[] consumed) {
        int childCount = this.getChildCount();
        boolean accepted = false;
        int xConsumed = 0;
        int yConsumed = 0;
        for (int i = 0; i < childCount; i++) {
            View view = this.getChildAt(i);
            if (view.getVisibility() != 8) {
                CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
                if (!lp.isNestedScrollDenied(type)) {
                    CoordinatorLayout.Behavior<View> viewBehavior = lp.getBehavior();
                    if (viewBehavior != null) {
                        this.mBehaviorConsumed[0] = 0;
                        this.mBehaviorConsumed[1] = 0;
                        viewBehavior.onNestedScroll(this, view, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, this.mBehaviorConsumed);
                        xConsumed = dxUnconsumed > 0 ? Math.max(xConsumed, this.mBehaviorConsumed[0]) : Math.min(xConsumed, this.mBehaviorConsumed[0]);
                        yConsumed = dyUnconsumed > 0 ? Math.max(yConsumed, this.mBehaviorConsumed[1]) : Math.min(yConsumed, this.mBehaviorConsumed[1]);
                        accepted = true;
                    }
                }
            }
        }
        consumed[0] += xConsumed;
        consumed[1] += yConsumed;
        if (accepted) {
            this.onChildViewsChanged(1);
        }
    }

    @Override
    public void onNestedPreScroll(@Nonnull View target, int dx, int dy, @Nonnull int[] consumed, int type) {
        int xConsumed = 0;
        int yConsumed = 0;
        boolean accepted = false;
        int childCount = this.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = this.getChildAt(i);
            if (view.getVisibility() != 8) {
                CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
                if (!lp.isNestedScrollDenied(type)) {
                    CoordinatorLayout.Behavior<View> viewBehavior = lp.getBehavior();
                    if (viewBehavior != null) {
                        this.mBehaviorConsumed[0] = 0;
                        this.mBehaviorConsumed[1] = 0;
                        viewBehavior.onNestedPreScroll(this, view, target, dx, dy, this.mBehaviorConsumed, type);
                        xConsumed = dx > 0 ? Math.max(xConsumed, this.mBehaviorConsumed[0]) : Math.min(xConsumed, this.mBehaviorConsumed[0]);
                        yConsumed = dy > 0 ? Math.max(yConsumed, this.mBehaviorConsumed[1]) : Math.min(yConsumed, this.mBehaviorConsumed[1]);
                        accepted = true;
                    }
                }
            }
        }
        consumed[0] = xConsumed;
        consumed[1] = yConsumed;
        if (accepted) {
            this.onChildViewsChanged(1);
        }
    }

    @Override
    public boolean onNestedFling(@Nonnull View target, float velocityX, float velocityY, boolean consumed) {
        boolean handled = false;
        int childCount = this.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = this.getChildAt(i);
            if (view.getVisibility() != 8) {
                CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
                if (!lp.isNestedScrollDenied(0)) {
                    CoordinatorLayout.Behavior<View> viewBehavior = lp.getBehavior();
                    if (viewBehavior != null) {
                        handled |= viewBehavior.onNestedFling(this, view, target, velocityX, velocityY, consumed);
                    }
                }
            }
        }
        if (handled) {
            this.onChildViewsChanged(1);
        }
        return handled;
    }

    @Override
    public boolean onNestedPreFling(@Nonnull View target, float velocityX, float velocityY) {
        boolean handled = false;
        int childCount = this.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = this.getChildAt(i);
            if (view.getVisibility() != 8) {
                CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
                if (!lp.isNestedScrollDenied(0)) {
                    CoordinatorLayout.Behavior<View> viewBehavior = lp.getBehavior();
                    if (viewBehavior != null) {
                        handled |= viewBehavior.onNestedPreFling(this, view, target, velocityX, velocityY);
                    }
                }
            }
        }
        return handled;
    }

    public interface AttachedBehavior {

        @Nonnull
        CoordinatorLayout.Behavior<?> getBehavior();
    }

    public abstract static class Behavior<V extends View> {

        protected Behavior() {
        }

        public void onAttachedToLayoutParams(@Nonnull CoordinatorLayout.LayoutParams params) {
        }

        public void onDetachedFromLayoutParams() {
        }

        public boolean onInterceptTouchEvent(@Nonnull CoordinatorLayout parent, @Nonnull V child, @Nonnull MotionEvent ev) {
            return false;
        }

        public boolean onTouchEvent(@Nonnull CoordinatorLayout parent, @Nonnull V child, @Nonnull MotionEvent ev) {
            return false;
        }

        public int getScrimColor(@Nonnull CoordinatorLayout parent, @Nonnull V child) {
            return 0;
        }

        public float getScrimOpacity(@Nonnull CoordinatorLayout parent, @Nonnull V child) {
            return 0.0F;
        }

        public boolean blocksInteractionBelow(@Nonnull CoordinatorLayout parent, @Nonnull V child) {
            return this.getScrimOpacity(parent, child) > 0.0F;
        }

        public boolean layoutDependsOn(@Nonnull CoordinatorLayout parent, @Nonnull V child, @Nonnull View dependency) {
            return false;
        }

        public boolean onDependentViewChanged(@Nonnull CoordinatorLayout parent, @Nonnull V child, @Nonnull View dependency) {
            return false;
        }

        public void onDependentViewRemoved(@Nonnull CoordinatorLayout parent, @Nonnull V child, @Nonnull View dependency) {
        }

        public boolean onMeasureChild(@Nonnull CoordinatorLayout parent, @Nonnull V child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
            return false;
        }

        public boolean onLayoutChild(@Nonnull CoordinatorLayout parent, @Nonnull V child, int layoutDirection) {
            return false;
        }

        public static void setTag(@Nonnull View child, @Nullable Object tag) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            lp.mBehaviorTag = tag;
        }

        @Nullable
        public static Object getTag(@Nonnull View child) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            return lp.mBehaviorTag;
        }

        public boolean onStartNestedScroll(@Nonnull CoordinatorLayout coordinatorLayout, @Nonnull V child, @Nonnull View directTargetChild, @Nonnull View target, int axes, int type) {
            return false;
        }

        public void onNestedScrollAccepted(@Nonnull CoordinatorLayout coordinatorLayout, @Nonnull V child, @Nonnull View directTargetChild, @Nonnull View target, int axes, int type) {
        }

        public void onStopNestedScroll(@Nonnull CoordinatorLayout coordinatorLayout, @Nonnull V child, @Nonnull View target, int type) {
        }

        public void onNestedScroll(@Nonnull CoordinatorLayout coordinatorLayout, @Nonnull V child, @Nonnull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @Nonnull int[] consumed) {
        }

        public void onNestedPreScroll(@Nonnull CoordinatorLayout coordinatorLayout, @Nonnull V child, @Nonnull View target, int dx, int dy, @Nonnull int[] consumed, int type) {
        }

        public boolean onNestedFling(@Nonnull CoordinatorLayout coordinatorLayout, @Nonnull V child, @Nonnull View target, float velocityX, float velocityY, boolean consumed) {
            return false;
        }

        public boolean onNestedPreFling(@Nonnull CoordinatorLayout coordinatorLayout, @Nonnull V child, @Nonnull View target, float velocityX, float velocityY) {
            return false;
        }

        public boolean onRequestChildRectangleOnScreen(@Nonnull CoordinatorLayout coordinatorLayout, @Nonnull V child, @Nonnull Rect rectangle, boolean immediate) {
            return false;
        }

        public boolean getInsetDodgeRect(@Nonnull CoordinatorLayout parent, @Nonnull V child, @Nonnull Rect rect) {
            return false;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DispatchChangeEvent {
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {

        CoordinatorLayout.Behavior mBehavior;

        boolean mBehaviorResolved = false;

        public int gravity = 0;

        public int anchorGravity = 0;

        int mAnchorId = -1;

        public int insetEdge = 0;

        public int dodgeInsetEdges = 0;

        int mInsetOffsetX;

        int mInsetOffsetY;

        View mAnchorView;

        View mAnchorDirectChild;

        private boolean mDidBlockInteraction;

        private boolean mDidAcceptNestedScrollTouch;

        private boolean mDidAcceptNestedScrollNonTouch;

        private boolean mDidChangeAfterNestedScroll;

        final Rect mLastChildRect = new Rect();

        Object mBehaviorTag;

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams p) {
            super(p);
        }

        public LayoutParams(ViewGroup.LayoutParams p) {
            super(p);
        }

        public int getAnchorId() {
            return this.mAnchorId;
        }

        public void setAnchorId(int id) {
            this.invalidateAnchor();
            this.mAnchorId = id;
        }

        @Nullable
        public <V extends View> CoordinatorLayout.Behavior<V> getBehavior() {
            return this.mBehavior;
        }

        public void setBehavior(@Nullable CoordinatorLayout.Behavior<?> behavior) {
            if (this.mBehavior != behavior) {
                if (this.mBehavior != null) {
                    this.mBehavior.onDetachedFromLayoutParams();
                }
                this.mBehavior = behavior;
                this.mBehaviorTag = null;
                this.mBehaviorResolved = true;
                if (behavior != null) {
                    behavior.onAttachedToLayoutParams(this);
                }
            }
        }

        void setLastChildRect(Rect r) {
            this.mLastChildRect.set(r);
        }

        Rect getLastChildRect() {
            return this.mLastChildRect;
        }

        boolean checkAnchorChanged() {
            return this.mAnchorView == null && this.mAnchorId != -1;
        }

        boolean didBlockInteraction() {
            if (this.mBehavior == null) {
                this.mDidBlockInteraction = false;
            }
            return this.mDidBlockInteraction;
        }

        boolean isBlockingInteractionBelow(CoordinatorLayout parent, View child) {
            return this.mDidBlockInteraction ? true : (this.mDidBlockInteraction = this.mBehavior != null && this.mBehavior.blocksInteractionBelow(parent, child));
        }

        void resetTouchBehaviorTracking() {
            this.mDidBlockInteraction = false;
        }

        void resetNestedScroll(int type) {
            this.setNestedScrollAccepted(type, false);
        }

        void setNestedScrollAccepted(int type, boolean accept) {
            switch(type) {
                case 0:
                    this.mDidAcceptNestedScrollTouch = accept;
                    break;
                case 1:
                    this.mDidAcceptNestedScrollNonTouch = accept;
            }
        }

        boolean isNestedScrollDenied(int type) {
            return switch(type) {
                case 0 ->
                    !this.mDidAcceptNestedScrollTouch;
                case 1 ->
                    !this.mDidAcceptNestedScrollNonTouch;
                default ->
                    true;
            };
        }

        boolean getChangedAfterNestedScroll() {
            return this.mDidChangeAfterNestedScroll;
        }

        void setChangedAfterNestedScroll(boolean changed) {
            this.mDidChangeAfterNestedScroll = changed;
        }

        void resetChangedAfterNestedScroll() {
            this.mDidChangeAfterNestedScroll = false;
        }

        boolean dependsOn(CoordinatorLayout parent, View child, View dependency) {
            return dependency == this.mAnchorDirectChild || this.shouldDodge(dependency, parent.getLayoutDirection()) || this.mBehavior != null && this.mBehavior.layoutDependsOn(parent, child, dependency);
        }

        void invalidateAnchor() {
            this.mAnchorView = this.mAnchorDirectChild = null;
        }

        View findAnchorView(CoordinatorLayout parent, View forChild) {
            if (this.mAnchorId == -1) {
                this.mAnchorView = this.mAnchorDirectChild = null;
                return null;
            } else {
                if (this.mAnchorView == null || !this.verifyAnchorView(forChild, parent)) {
                    this.resolveAnchorView(forChild, parent);
                }
                return this.mAnchorView;
            }
        }

        private void resolveAnchorView(View forChild, @Nonnull CoordinatorLayout parent) {
            this.mAnchorView = parent.findViewById(this.mAnchorId);
            if (this.mAnchorView == null) {
                throw new IllegalStateException("Could not find CoordinatorLayout descendant view with id " + this.mAnchorId + " to anchor view " + forChild);
            } else if (this.mAnchorView == parent) {
                throw new IllegalStateException("View can not be anchored to the the parent CoordinatorLayout");
            } else {
                directChild = this.mAnchorView;
                for (ViewParent p = this.mAnchorView.getParent(); p != parent && p != null; p = p.getParent()) {
                    if (p == forChild) {
                        throw new IllegalStateException("Anchor must not be a descendant of the anchored view");
                    }
                    if (p instanceof View directChild) {
                        ;
                    }
                }
                this.mAnchorDirectChild = directChild;
            }
        }

        private boolean verifyAnchorView(View forChild, CoordinatorLayout parent) {
            if (this.mAnchorView.getId() != this.mAnchorId) {
                return false;
            } else {
                directChild = this.mAnchorView;
                for (ViewParent p = this.mAnchorView.getParent(); p != parent; p = p.getParent()) {
                    if (p == null || p == forChild) {
                        this.mAnchorView = this.mAnchorDirectChild = null;
                        return false;
                    }
                    if (p instanceof View directChild) {
                        ;
                    }
                }
                this.mAnchorDirectChild = directChild;
                return true;
            }
        }

        private boolean shouldDodge(@Nonnull View other, int layoutDirection) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) other.getLayoutParams();
            int absInset = Gravity.getAbsoluteGravity(lp.insetEdge, layoutDirection);
            return absInset != 0 && (absInset & Gravity.getAbsoluteGravity(this.dodgeInsetEdges, layoutDirection)) == absInset;
        }
    }

    class OnPreDrawListener implements ViewTreeObserver.OnPreDrawListener {

        @Override
        public boolean onPreDraw() {
            CoordinatorLayout.this.onChildViewsChanged(0);
            return true;
        }
    }
}