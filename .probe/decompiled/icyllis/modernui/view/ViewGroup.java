package icyllis.modernui.view;

import icyllis.modernui.animation.LayoutTransition;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.annotation.UiThread;
import icyllis.modernui.core.Context;
import icyllis.modernui.core.Core;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.Point;
import icyllis.modernui.graphics.PointF;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.RectF;
import icyllis.modernui.util.Pools;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import org.jetbrains.annotations.ApiStatus.Internal;

@UiThread
public abstract class ViewGroup extends View implements ViewParent, ViewManager {

    private static final int ARRAY_CAPACITY_INCREMENT = 12;

    static final int FLAG_CLIP_CHILDREN = 1;

    private static final int FLAG_CLIP_TO_PADDING = 2;

    private static final int FLAG_PADDING_NOT_NULL = 32;

    static final int FLAG_USE_CHILD_DRAWING_ORDER = 1024;

    private static final int FLAG_NOTIFY_CHILDREN_ON_DRAWABLE_STATE_CHANGE = 65536;

    private static final int FLAG_ADD_STATES_FROM_CHILDREN = 8192;

    public static final int FOCUS_BEFORE_DESCENDANTS = 131072;

    public static final int FOCUS_AFTER_DESCENDANTS = 262144;

    public static final int FOCUS_BLOCK_DESCENDANTS = 393216;

    private static final int FLAG_MASK_FOCUSABILITY = 393216;

    @Internal
    protected static final int FLAG_DISALLOW_INTERCEPT = 524288;

    private static final int FLAG_PREVENT_DISPATCH_ATTACHED_TO_WINDOW = 4194304;

    static final int FLAG_IS_TRANSITION_GROUP = 16777216;

    static final int FLAG_IS_TRANSITION_GROUP_SET = 33554432;

    static final int FLAG_TOUCHSCREEN_BLOCKS_FOCUS = 67108864;

    private static final int[] DESCENDANT_FOCUSABILITY_FLAGS = new int[] { 131072, 262144, 393216 };

    protected static final int CLIP_TO_PADDING_MASK = 34;

    private int mGroupFlags;

    private View[] mChildren;

    private int mChildrenCount;

    View mFocused;

    private View mDefaultFocus;

    View mFocusedInCluster;

    boolean mSuppressLayout = false;

    private boolean mLayoutCalledWhileSuppressed = false;

    private float[] mTempPosition;

    private ViewGroup.TouchTarget mTouchTarget;

    private ViewGroup.HoverTarget mFirstHoverTarget;

    private boolean mHoveredSelf;

    private View mTooltipHoverTarget;

    private boolean mTooltipHoveredSelf;

    private static final ThreadLocal<FloatBuffer> sDebugDrawBuffer = ThreadLocal.withInitial(() -> FloatBuffer.allocate(96));

    private LayoutTransition mTransition;

    private ArrayList<View> mDisappearingChildren;

    private ArrayList<View> mTransitioningViews;

    private ArrayList<View> mVisibilityChangingChildren;

    private ArrayList<View> mPreSortedChildren;

    private int mChildCountWithTransientState = 0;

    private int mNestedScrollAxesTouch;

    private int mNestedScrollAxesNonTouch;

    private IntArrayList mTransientIndices = null;

    private List<View> mTransientViews = null;

    private final ViewGroup.LayoutTransitionListener mLayoutTransitionListener = new ViewGroup.LayoutTransitionListener();

    public ViewGroup(Context context) {
        super(context);
        this.mGroupFlags |= 1;
        this.setDescendantFocusability(131072);
        this.mChildren = new View[12];
        this.mChildrenCount = 0;
    }

    @Internal
    protected void onDebugDraw(@NonNull Canvas canvas) {
        Paint paint = Paint.obtain();
        int childrenCount = this.mChildrenCount;
        View[] children = this.mChildren;
        FloatBuffer positions = null;
        paint.setRGBA(1.0F, 0.0F, 0.0F, 1.0F);
        for (int i = 0; i < childrenCount; i++) {
            View child = children[i];
            if (child.getVisibility() != 8) {
                if (positions == null) {
                    positions = (FloatBuffer) sDebugDrawBuffer.get();
                }
                positions.clear();
                float x1 = (float) child.getLeft() + 0.5F;
                float y1 = (float) child.getTop() + 0.5F;
                float x2 = (float) child.getRight() - 0.5F;
                float y2 = (float) child.getBottom() - 0.5F;
                positions.put(x1).put(y1).put(x2).put(y1).put(x2).put(y2).put(x1).put(y2).put(x1).put(y1);
                canvas.drawMesh(Canvas.VertexMode.LINE_STRIP, positions.flip(), null, null, null, null, paint);
            }
        }
        paint.setRGBA(1.0F, 0.0F, 1.0F, 0.25F);
        this.onDebugDrawMargins(canvas, paint);
        paint.setRGBA(0.25F, 0.5F, 1.0F, 1.0F);
        int lineLength = this.dp(4.0F);
        int lineWidth = this.dp(0.5F);
        for (int ix = 0; ix < childrenCount; ix++) {
            View child = children[ix];
            if (child.getVisibility() != 8) {
                if (positions == null) {
                    positions = (FloatBuffer) sDebugDrawBuffer.get();
                }
                positions.clear();
                int x1 = child.getLeft();
                int y1 = child.getTop();
                int x2 = child.getRight();
                int y2 = child.getBottom();
                fillCorner(x1, y1, lineLength, lineLength, lineWidth, positions);
                fillCorner(x1, y2, lineLength, -lineLength, lineWidth, positions);
                fillCorner(x2, y1, -lineLength, lineLength, lineWidth, positions);
                fillCorner(x2, y2, -lineLength, -lineLength, lineWidth, positions);
                canvas.drawMesh(Canvas.VertexMode.TRIANGLES, positions.flip(), null, null, null, null, paint);
            }
        }
        paint.recycle();
    }

    @Internal
    protected void onDebugDrawMargins(@NonNull Canvas canvas, Paint paint) {
        int childrenCount = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < childrenCount; i++) {
            View child = children[i];
            child.getLayoutParams().onDebugDraw(child, canvas, paint);
        }
    }

    private static void fillRect(int x1, int y1, int x2, int y2, FloatBuffer positions) {
        if (x1 != x2 && y1 != y2) {
            if (x1 > x2) {
                int tmp = x1;
                x1 = x2;
                x2 = tmp;
            }
            if (y1 > y2) {
                int tmp = y1;
                y1 = y2;
                y2 = tmp;
            }
            positions.put((float) x1).put((float) y2).put((float) x2).put((float) y2).put((float) x1).put((float) y1).put((float) x1).put((float) y1).put((float) x2).put((float) y2).put((float) x2).put((float) y1);
        }
    }

    private static void fillCorner(int x1, int y1, int dx, int dy, int lw, FloatBuffer positions) {
        fillRect(x1, y1, x1 + dx, y1 + lw * (dy >= 0 ? 1 : -1), positions);
        fillRect(x1, y1, x1 + lw * (dx >= 0 ? 1 : -1), y1 + dy, positions);
    }

    @Override
    protected void dispatchDraw(@NonNull Canvas canvas) {
        int clipSaveCount = 0;
        boolean clipToPadding = (this.mGroupFlags & 34) == 34;
        if (clipToPadding) {
            clipSaveCount = canvas.save();
            canvas.clipRect((float) (this.mScrollX + this.mPaddingLeft), (float) (this.mScrollY + this.mPaddingTop), (float) (this.mScrollX + this.mRight - this.mLeft - this.mPaddingRight), (float) (this.mScrollY + this.mBottom - this.mTop - this.mPaddingBottom));
        }
        int childrenCount = this.mChildrenCount;
        View[] children = this.mChildren;
        int transientCount = this.mTransientIndices == null ? 0 : this.mTransientIndices.size();
        int transientIndex = transientCount != 0 ? 0 : -1;
        ArrayList<View> preorderedList = this.buildOrderedChildList();
        boolean customOrder = preorderedList == null && this.isChildrenDrawingOrderEnabled();
        for (int i = 0; i < childrenCount; i++) {
            while (transientIndex >= 0 && this.mTransientIndices.getInt(transientIndex) == i) {
                View transientChild = (View) this.mTransientViews.get(transientIndex);
                if ((transientChild.mViewFlags & 12) == 0) {
                    this.drawChild(canvas, transientChild, 0L);
                }
                if (++transientIndex >= transientCount) {
                    transientIndex = -1;
                }
            }
            int childIndex = this.getAndVerifyPreorderedIndex(childrenCount, i, customOrder);
            View child = getAndVerifyPreorderedView(preorderedList, children, childIndex);
            if ((child.mViewFlags & 12) == 0) {
                this.drawChild(canvas, child, 0L);
            }
        }
        while (transientIndex >= 0) {
            View transientChildx = (View) this.mTransientViews.get(transientIndex);
            if ((transientChildx.mViewFlags & 12) == 0) {
                this.drawChild(canvas, transientChildx, 0L);
            }
            if (++transientIndex >= transientCount) {
                break;
            }
        }
        if (preorderedList != null) {
            preorderedList.clear();
        }
        if (this.mDisappearingChildren != null) {
            ArrayList<View> disappearingChildren = this.mDisappearingChildren;
            for (int i = disappearingChildren.size() - 1; i >= 0; i--) {
                View child = (View) disappearingChildren.get(i);
                this.drawChild(canvas, child, 0L);
            }
        }
        if (this.isShowingLayoutBounds()) {
            this.onDebugDraw(canvas);
        }
        if (clipToPadding) {
            canvas.restoreToCount(clipSaveCount);
        }
    }

    protected void drawChild(@NonNull Canvas canvas, @NonNull View child, long drawingTime) {
        child.draw(canvas, this, (this.mGroupFlags & 1) != 0);
    }

    @Override
    public final void layout(int l, int t, int r, int b) {
        if (!this.mSuppressLayout && (this.mTransition == null || !this.mTransition.isChangingLayout())) {
            if (this.mTransition != null) {
                this.mTransition.layoutChange(this);
            }
            super.layout(l, t, r, b);
        } else {
            this.mLayoutCalledWhileSuppressed = true;
        }
    }

    @Override
    protected abstract void onLayout(boolean var1, int var2, int var3, int var4, int var5);

    private int getAndVerifyPreorderedIndex(int childrenCount, int i, boolean customOrder) {
        int childIndex;
        if (customOrder) {
            int childIndex1 = this.getChildDrawingOrder(childrenCount, i);
            if (childIndex1 >= childrenCount) {
                throw new IndexOutOfBoundsException("getChildDrawingOrder() returned invalid index " + childIndex1 + " (child count is " + childrenCount + ")");
            }
            childIndex = childIndex1;
        } else {
            childIndex = i;
        }
        return childIndex;
    }

    @Override
    protected boolean dispatchHoverEvent(@NonNull MotionEvent event) {
        int action = event.getAction();
        boolean intercepted = this.onInterceptHoverEvent(event);
        event.setAction(action);
        boolean handled = false;
        ViewGroup.HoverTarget firstOldHoverTarget = this.mFirstHoverTarget;
        this.mFirstHoverTarget = null;
        if (!intercepted && action != 10 && this.mChildrenCount != 0) {
            float x = event.getX();
            float y = event.getY();
            View[] children = this.mChildren;
            int childrenCount = this.mChildrenCount;
            ArrayList<View> preorderedList = this.buildOrderedChildList();
            boolean customOrder = preorderedList == null && this.isChildrenDrawingOrderEnabled();
            ViewGroup.HoverTarget lastHoverTarget = null;
            for (int i = childrenCount - 1; i >= 0; i--) {
                int childIndex = this.getAndVerifyPreorderedIndex(childrenCount, i, customOrder);
                View child = getAndVerifyPreorderedView(preorderedList, children, childIndex);
                if (child.canReceivePointerEvents() && this.isTransformedTouchPointInView(x, y, child, null)) {
                    ViewGroup.HoverTarget hoverTarget = firstOldHoverTarget;
                    ViewGroup.HoverTarget predecessor = null;
                    boolean wasHovered;
                    while (true) {
                        if (hoverTarget == null) {
                            hoverTarget = ViewGroup.HoverTarget.obtain(child);
                            wasHovered = false;
                            break;
                        }
                        if (hoverTarget.child == child) {
                            if (predecessor != null) {
                                predecessor.next = hoverTarget.next;
                            } else {
                                firstOldHoverTarget = hoverTarget.next;
                            }
                            hoverTarget.next = null;
                            wasHovered = true;
                            break;
                        }
                        predecessor = hoverTarget;
                        hoverTarget = hoverTarget.next;
                    }
                    if (lastHoverTarget != null) {
                        lastHoverTarget.next = hoverTarget;
                    } else {
                        this.mFirstHoverTarget = hoverTarget;
                    }
                    lastHoverTarget = hoverTarget;
                    if (action == 9) {
                        if (!wasHovered) {
                            handled = this.dispatchTransformedGenericPointerEvent(event, child);
                        }
                    } else if (action == 7) {
                        if (!wasHovered) {
                            event.setAction(9);
                            handled = this.dispatchTransformedGenericPointerEvent(event, child);
                            event.setAction(action);
                        }
                        handled |= this.dispatchTransformedGenericPointerEvent(event, child);
                    }
                    if (handled) {
                        break;
                    }
                }
            }
            if (preorderedList != null) {
                preorderedList.clear();
            }
        }
        while (firstOldHoverTarget != null) {
            View child = firstOldHoverTarget.child;
            if (action == 10) {
                handled |= this.dispatchTransformedGenericPointerEvent(event, child);
            } else {
                if (action == 7) {
                    boolean hoverExitPending = event.isHoverExitPending();
                    event.setHoverExitPending(true);
                    this.dispatchTransformedGenericPointerEvent(event, child);
                    event.setHoverExitPending(hoverExitPending);
                }
                event.setAction(10);
                this.dispatchTransformedGenericPointerEvent(event, child);
                event.setAction(action);
            }
            ViewGroup.HoverTarget nextOldHoverTarget = firstOldHoverTarget.next;
            firstOldHoverTarget.recycle();
            firstOldHoverTarget = nextOldHoverTarget;
        }
        boolean newHoveredSelf = !handled && action != 10 && !event.isHoverExitPending();
        if (newHoveredSelf == this.mHoveredSelf) {
            if (newHoveredSelf) {
                handled = super.dispatchHoverEvent(event);
            }
        } else {
            if (this.mHoveredSelf) {
                if (action == 10) {
                    handled |= super.dispatchHoverEvent(event);
                } else {
                    if (action == 7) {
                        super.dispatchHoverEvent(event);
                    }
                    event.setAction(10);
                    super.dispatchHoverEvent(event);
                    event.setAction(action);
                }
                this.mHoveredSelf = false;
            }
            if (newHoveredSelf) {
                if (action == 9) {
                    handled = super.dispatchHoverEvent(event);
                    this.mHoveredSelf = true;
                } else if (action == 7) {
                    event.setAction(9);
                    handled = super.dispatchHoverEvent(event);
                    event.setAction(action);
                    handled |= super.dispatchHoverEvent(event);
                    this.mHoveredSelf = true;
                }
            }
        }
        return handled;
    }

    private void exitHoverTargets() {
        if (this.mHoveredSelf || this.mFirstHoverTarget != null) {
            long now = Core.timeNanos();
            MotionEvent event = MotionEvent.obtain(now, 10, 0.0F, 0.0F, 0);
            this.dispatchHoverEvent(event);
            event.recycle();
        }
    }

    private void cancelHoverTarget(View view) {
        ViewGroup.HoverTarget predecessor = null;
        ViewGroup.HoverTarget target = this.mFirstHoverTarget;
        while (target != null) {
            ViewGroup.HoverTarget next = target.next;
            if (target.child == view) {
                if (predecessor == null) {
                    this.mFirstHoverTarget = next;
                } else {
                    predecessor.next = next;
                }
                target.recycle();
                long now = Core.timeNanos();
                MotionEvent event = MotionEvent.obtain(now, 10, 0.0F, 0.0F, 0);
                view.dispatchHoverEvent(event);
                event.recycle();
                return;
            }
            predecessor = target;
            target = next;
        }
    }

    @Override
    boolean dispatchTooltipHoverEvent(@NonNull MotionEvent event) {
        int action = event.getAction();
        switch(action) {
            case 7:
                View newTarget = null;
                int childrenCount = this.mChildrenCount;
                if (childrenCount != 0) {
                    float x = event.getX();
                    float y = event.getY();
                    ArrayList<View> preorderedList = this.buildOrderedChildList();
                    boolean customOrder = preorderedList == null && this.isChildrenDrawingOrderEnabled();
                    View[] children = this.mChildren;
                    for (int i = childrenCount - 1; i >= 0; i--) {
                        int childIndex = this.getAndVerifyPreorderedIndex(childrenCount, i, customOrder);
                        View child = getAndVerifyPreorderedView(preorderedList, children, childIndex);
                        if (child.canReceivePointerEvents() && this.isTransformedTouchPointInView(x, y, child, null) && this.dispatchTransformedTooltipHoverEvent(event, child)) {
                            newTarget = child;
                            break;
                        }
                    }
                    if (preorderedList != null) {
                        preorderedList.clear();
                    }
                }
                if (this.mTooltipHoverTarget != newTarget) {
                    if (this.mTooltipHoverTarget != null) {
                        event.setAction(10);
                        this.mTooltipHoverTarget.dispatchTooltipHoverEvent(event);
                        event.setAction(action);
                    }
                    this.mTooltipHoverTarget = newTarget;
                }
                if (this.mTooltipHoverTarget != null) {
                    if (this.mTooltipHoveredSelf) {
                        this.mTooltipHoveredSelf = false;
                        event.setAction(10);
                        super.dispatchTooltipHoverEvent(event);
                        event.setAction(action);
                    }
                    return true;
                } else {
                    this.mTooltipHoveredSelf = super.dispatchTooltipHoverEvent(event);
                    return this.mTooltipHoveredSelf;
                }
            case 10:
                if (this.mTooltipHoverTarget != null) {
                    this.mTooltipHoverTarget.dispatchTooltipHoverEvent(event);
                    this.mTooltipHoverTarget = null;
                } else if (this.mTooltipHoveredSelf) {
                    super.dispatchTooltipHoverEvent(event);
                    this.mTooltipHoveredSelf = false;
                }
            case 8:
            case 9:
            default:
                return false;
        }
    }

    boolean dispatchTransformedTooltipHoverEvent(MotionEvent event, View child) {
        boolean result;
        if (!child.hasIdentityMatrix()) {
            MotionEvent transformedEvent = this.getTransformedMotionEvent(event, child);
            result = child.dispatchTooltipHoverEvent(transformedEvent);
            transformedEvent.recycle();
        } else {
            float offsetX = (float) (this.mScrollX - child.mLeft);
            float offsetY = (float) (this.mScrollY - child.mTop);
            event.offsetLocation(offsetX, offsetY);
            result = child.dispatchTooltipHoverEvent(event);
            event.offsetLocation(-offsetX, -offsetY);
        }
        return result;
    }

    private void exitTooltipHoverTargets() {
        if (this.mTooltipHoveredSelf || this.mTooltipHoverTarget != null) {
            long now = Core.timeNanos();
            MotionEvent event = MotionEvent.obtain(now, 10, 0.0F, 0.0F, 0);
            this.dispatchTooltipHoverEvent(event);
            event.recycle();
        }
    }

    boolean dispatchTransformedGenericPointerEvent(@NonNull MotionEvent event, @NonNull View child) {
        boolean handled;
        if (!child.hasIdentityMatrix()) {
            MotionEvent transformedEvent = this.getTransformedMotionEvent(event, child);
            handled = child.dispatchGenericMotionEvent(transformedEvent);
            transformedEvent.recycle();
        } else {
            float offsetX = (float) (this.mScrollX - child.mLeft);
            float offsetY = (float) (this.mScrollY - child.mTop);
            event.offsetLocation(offsetX, offsetY);
            handled = child.dispatchGenericMotionEvent(event);
            event.offsetLocation(-offsetX, -offsetY);
        }
        return handled;
    }

    @NonNull
    private MotionEvent getTransformedMotionEvent(@NonNull MotionEvent event, @NonNull View child) {
        float offsetX = (float) (this.mScrollX - child.mLeft);
        float offsetY = (float) (this.mScrollY - child.mTop);
        MotionEvent transformedEvent = event.copy();
        transformedEvent.offsetLocation(offsetX, offsetY);
        if (!child.hasIdentityMatrix()) {
            transformedEvent.transform(child.getInverseMatrix());
        }
        return transformedEvent;
    }

    public boolean onInterceptHoverEvent(@NonNull MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        return (action == 7 || action == 9) && this.isOnScrollbar(x, y);
    }

    @Override
    protected boolean dispatchGenericPointerEvent(@NonNull MotionEvent event) {
        int childrenCount = this.mChildrenCount;
        if (childrenCount != 0) {
            float x = event.getX();
            float y = event.getY();
            ArrayList<View> preorderedList = this.buildOrderedChildList();
            boolean customOrder = preorderedList == null && this.isChildrenDrawingOrderEnabled();
            View[] children = this.mChildren;
            for (int i = childrenCount - 1; i >= 0; i--) {
                int childIndex = this.getAndVerifyPreorderedIndex(childrenCount, i, customOrder);
                View child = getAndVerifyPreorderedView(preorderedList, children, childIndex);
                if (child.canReceivePointerEvents() && this.isTransformedTouchPointInView(x, y, child, null) && this.dispatchTransformedGenericPointerEvent(event, child)) {
                    if (preorderedList != null) {
                        preorderedList.clear();
                    }
                    return true;
                }
            }
            if (preorderedList != null) {
                preorderedList.clear();
            }
        }
        return super.dispatchGenericPointerEvent(event);
    }

    private static View getAndVerifyPreorderedView(@Nullable ArrayList<View> preorderedList, View[] children, int childIndex) {
        View child;
        if (preorderedList != null) {
            child = (View) preorderedList.get(childIndex);
            if (child == null) {
                throw new RuntimeException("Invalid preorderedList contained null child at index " + childIndex);
            }
        } else {
            child = children[childIndex];
        }
        return child;
    }

    private static boolean resetCancelNextUpFlag(@NonNull View view) {
        if ((view.mPrivateFlags & 67108864) != 0) {
            view.mPrivateFlags &= -67108865;
            return true;
        } else {
            return false;
        }
    }

    private void clearTouchTargets() {
        ViewGroup.TouchTarget target = this.mTouchTarget;
        if (target != null) {
            target.recycle();
            this.mTouchTarget = null;
        }
    }

    private void resetTouchState() {
        this.clearTouchTargets();
        resetCancelNextUpFlag(this);
        this.mGroupFlags &= -524289;
    }

    private void cancelAndClearTouchTargets(@Nullable MotionEvent event) {
        ViewGroup.TouchTarget target = this.mTouchTarget;
        if (target != null) {
            boolean syntheticEvent = false;
            if (event == null) {
                long time = Core.timeNanos();
                event = MotionEvent.obtain(time, 3, 0.0F, 0.0F, 0);
                syntheticEvent = true;
            }
            resetCancelNextUpFlag(target.child);
            this.dispatchTransformedTouchEvent(event, target.child, true);
            this.clearTouchTargets();
            if (syntheticEvent) {
                event.recycle();
            }
        }
    }

    private void cancelTouchTarget(View view) {
        ViewGroup.TouchTarget target = this.mTouchTarget;
        if (target != null && target.child == view) {
            this.mTouchTarget = null;
            target.recycle();
            long now = Core.timeNanos();
            MotionEvent event = MotionEvent.obtain(now, 3, 0.0F, 0.0F, 0);
            view.dispatchTouchEvent(event);
            event.recycle();
        }
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
        boolean handled = false;
        int action = ev.getAction();
        if (action == 0) {
            this.cancelAndClearTouchTargets(ev);
            this.resetTouchState();
        }
        boolean intercepted;
        if (action != 0 && this.mTouchTarget == null) {
            intercepted = true;
        } else {
            boolean allowIntercept = (this.mGroupFlags & 524288) == 0;
            if (allowIntercept) {
                intercepted = this.onInterceptTouchEvent(ev);
                ev.setAction(action);
            } else {
                intercepted = false;
            }
        }
        boolean canceled = resetCancelNextUpFlag(this) || action == 3;
        ViewGroup.TouchTarget newTouchTarget = null;
        boolean dispatchedToNewTarget = false;
        if (!canceled && !intercepted && action == 0 && this.mChildrenCount > 0) {
            int childrenCount = this.mChildrenCount;
            float x = ev.getX();
            float y = ev.getY();
            ArrayList<View> preorderedList = this.buildOrderedChildList();
            boolean customOrder = preorderedList == null && this.isChildrenDrawingOrderEnabled();
            View[] children = this.mChildren;
            for (int i = childrenCount - 1; i >= 0; i--) {
                int childIndex = this.getAndVerifyPreorderedIndex(childrenCount, i, customOrder);
                View child = getAndVerifyPreorderedView(preorderedList, children, childIndex);
                if (child.canReceivePointerEvents() && this.isTransformedTouchPointInView(x, y, child, null)) {
                    resetCancelNextUpFlag(child);
                    if (this.dispatchTransformedTouchEvent(ev, child, false)) {
                        this.mTouchTarget = ViewGroup.TouchTarget.obtain(child);
                        dispatchedToNewTarget = true;
                        break;
                    }
                }
            }
            if (preorderedList != null) {
                preorderedList.clear();
            }
        }
        if (this.mTouchTarget == null) {
            handled = this.dispatchTransformedTouchEvent(ev, null, canceled);
        } else if (dispatchedToNewTarget) {
            handled = true;
        } else {
            ViewGroup.TouchTarget target = this.mTouchTarget;
            boolean cancelChild = resetCancelNextUpFlag(target.child) || intercepted;
            if (this.dispatchTransformedTouchEvent(ev, target.child, cancelChild)) {
                handled = true;
            }
            if (cancelChild) {
                this.mTouchTarget = null;
                target.recycle();
            }
        }
        if (canceled || action == 1) {
            this.resetTouchState();
        }
        return handled;
    }

    public boolean isTransitionGroup() {
        return (this.mGroupFlags & 33554432) != 0 ? (this.mGroupFlags & 16777216) != 0 : this.getBackground() != null || this.getTransitionName() != null;
    }

    public void setTransitionGroup(boolean isTransitionGroup) {
        this.mGroupFlags |= 33554432;
        if (isTransitionGroup) {
            this.mGroupFlags |= 16777216;
        } else {
            this.mGroupFlags &= -16777217;
        }
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        if (disallowIntercept != ((this.mGroupFlags & 524288) != 0)) {
            if (disallowIntercept) {
                this.mGroupFlags |= 524288;
            } else {
                this.mGroupFlags &= -524289;
            }
            if (this.mParent != null) {
                this.mParent.requestDisallowInterceptTouchEvent(disallowIntercept);
            }
        }
    }

    public boolean onInterceptTouchEvent(@NonNull MotionEvent ev) {
        return ev.getAction() == 0 && ev.isButtonPressed(1) && this.isOnScrollbarThumb(ev.getX(), ev.getY());
    }

    @Override
    public boolean requestFocus(int direction, @Nullable Rect previouslyFocusedRect) {
        int descendantFocusability = this.getDescendantFocusability();
        boolean result = switch(descendantFocusability) {
            case 131072 ->
                {
                    boolean took = super.requestFocus(direction, previouslyFocusedRect);
                    ???;
                }
            case 262144 ->
                {
                    boolean took = this.onRequestFocusInDescendants(direction, previouslyFocusedRect);
                    ???;
                }
            case 393216 ->
                super.requestFocus(direction, previouslyFocusedRect);
            default ->
                throw new IllegalStateException("descendant focusability must be one of FOCUS_BEFORE_DESCENDANTS, FOCUS_AFTER_DESCENDANTS, FOCUS_BLOCK_DESCENDANTS but is " + descendantFocusability);
        };
        if (result && !this.isLayoutValid() && (this.mPrivateFlags & 1) == 0) {
            this.mPrivateFlags |= 1;
        }
        return result;
    }

    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        int count = this.mChildrenCount;
        int index;
        int increment;
        int end;
        if ((direction & 2) != 0) {
            index = 0;
            increment = 1;
            end = count;
        } else {
            index = count - 1;
            increment = -1;
            end = -1;
        }
        View[] children = this.mChildren;
        for (int i = index; i != end; i += increment) {
            View child = children[i];
            if ((child.mViewFlags & 12) == 0 && child.requestFocus(direction, previouslyFocusedRect)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean restoreDefaultFocus() {
        return this.mDefaultFocus != null && this.getDescendantFocusability() != 393216 && (this.mDefaultFocus.mViewFlags & 12) == 0 && this.mDefaultFocus.restoreDefaultFocus() ? true : super.restoreDefaultFocus();
    }

    @Override
    boolean restoreFocusInCluster(int direction) {
        if (this.isKeyboardNavigationCluster()) {
            boolean blockedFocus = this.getTouchscreenBlocksFocus();
            boolean var3;
            try {
                this.setTouchscreenBlocksFocusNoRefocus(false);
                var3 = this.restoreFocusInClusterInternal(direction);
            } finally {
                this.setTouchscreenBlocksFocusNoRefocus(blockedFocus);
            }
            return var3;
        } else {
            return this.restoreFocusInClusterInternal(direction);
        }
    }

    private boolean restoreFocusInClusterInternal(int direction) {
        return this.mFocusedInCluster != null && this.getDescendantFocusability() != 393216 && (this.mFocusedInCluster.mViewFlags & 12) == 0 && this.mFocusedInCluster.restoreFocusInCluster(direction) ? true : super.restoreFocusInCluster(direction);
    }

    @Override
    boolean restoreFocusNotInCluster() {
        if (this.mFocusedInCluster != null) {
            return this.restoreFocusInCluster(130);
        } else if (!this.isKeyboardNavigationCluster() && (this.mViewFlags & 12) == 0) {
            int descendentFocusability = this.getDescendantFocusability();
            if (descendentFocusability == 393216) {
                return super.requestFocus(130, null);
            } else if (descendentFocusability == 131072 && super.requestFocus(130, null)) {
                return true;
            } else {
                for (int i = 0; i < this.mChildrenCount; i++) {
                    View child = this.mChildren[i];
                    if (!child.isKeyboardNavigationCluster() && child.restoreFocusNotInCluster()) {
                        return true;
                    }
                }
                return descendentFocusability == 262144 && !this.hasFocusableChild(false) ? super.requestFocus(130, null) : false;
            }
        } else {
            return false;
        }
    }

    @Internal
    @Override
    public void dispatchStartTemporaryDetach() {
        super.dispatchStartTemporaryDetach();
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            children[i].dispatchStartTemporaryDetach();
        }
    }

    @Internal
    @Override
    public void dispatchFinishTemporaryDetach() {
        super.dispatchFinishTemporaryDetach();
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            children[i].dispatchFinishTemporaryDetach();
        }
    }

    @Override
    final void dispatchAttachedToWindow(AttachInfo info, int visibility) {
        this.mGroupFlags |= 4194304;
        super.dispatchAttachedToWindow(info, visibility);
        this.mGroupFlags &= -4194305;
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            View child = children[i];
            child.dispatchAttachedToWindow(info, this.combineVisibility(visibility, child.getVisibility()));
        }
        int transientCount = this.mTransientIndices == null ? 0 : this.mTransientIndices.size();
        for (int i = 0; i < transientCount; i++) {
            View view = (View) this.mTransientViews.get(i);
            view.dispatchAttachedToWindow(info, this.combineVisibility(visibility, view.getVisibility()));
        }
    }

    @Override
    final void dispatchDetachedFromWindow() {
        this.cancelAndClearTouchTargets(null);
        this.exitHoverTargets();
        this.mLayoutCalledWhileSuppressed = false;
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            children[i].dispatchDetachedFromWindow();
        }
        this.clearDisappearingChildren();
        int transientCount = this.mTransientViews == null ? 0 : this.mTransientIndices.size();
        for (int i = 0; i < transientCount; i++) {
            View view = (View) this.mTransientViews.get(i);
            view.dispatchDetachedFromWindow();
        }
        super.dispatchDetachedFromWindow();
    }

    private float[] getTempLocationF() {
        if (this.mTempPosition == null) {
            this.mTempPosition = new float[2];
        }
        return this.mTempPosition;
    }

    boolean dispatchTransformedTouchEvent(@NonNull MotionEvent event, @Nullable View child, boolean cancel) {
        int oldAction = event.getAction();
        if (!cancel && oldAction != 3) {
            boolean handled;
            if (child != null && !child.hasIdentityMatrix()) {
                MotionEvent transformedEvent = event.copy();
                float offsetX = (float) (this.mScrollX - child.mLeft);
                float offsetY = (float) (this.mScrollY - child.mTop);
                transformedEvent.offsetLocation(offsetX, offsetY);
                if (!child.hasIdentityMatrix()) {
                    transformedEvent.transform(child.getInverseMatrix());
                }
                handled = child.dispatchTouchEvent(transformedEvent);
                transformedEvent.recycle();
            } else if (child == null) {
                handled = super.dispatchTouchEvent(event);
            } else {
                float offsetX = (float) (this.mScrollX - child.mLeft);
                float offsetY = (float) (this.mScrollY - child.mTop);
                event.offsetLocation(offsetX, offsetY);
                handled = child.dispatchTouchEvent(event);
                event.offsetLocation(-offsetX, -offsetY);
            }
            return handled;
        } else {
            event.setAction(3);
            boolean handled;
            if (child == null) {
                handled = super.dispatchTouchEvent(event);
            } else {
                handled = child.dispatchTouchEvent(event);
            }
            event.setAction(oldAction);
            return handled;
        }
    }

    boolean isTransformedTouchPointInView(float x, float y, @NonNull View child, @Nullable PointF outLocalPoint) {
        float[] point = this.getTempLocationF();
        point[0] = x;
        point[1] = y;
        this.transformPointToViewLocal(point, child);
        boolean isInView = child.pointInView(point[0], point[1]);
        if (isInView && outLocalPoint != null) {
            outLocalPoint.set(point[0], point[1]);
        }
        return isInView;
    }

    @Internal
    protected void transformPointToViewLocal(@NonNull float[] point, @NonNull View child) {
        point[0] += (float) (this.mScrollX - child.mLeft);
        point[1] += (float) (this.mScrollY - child.mTop);
        if (!child.hasIdentityMatrix()) {
            child.getInverseMatrix().mapPoint(point);
        }
    }

    @Override
    public boolean dispatchKeyEvent(@NonNull KeyEvent event) {
        if ((this.mPrivateFlags & 18) == 18) {
            return super.dispatchKeyEvent(event);
        } else {
            return this.mFocused != null && (this.mFocused.mPrivateFlags & 16) == 16 ? this.mFocused.dispatchKeyEvent(event) : false;
        }
    }

    @Override
    public boolean dispatchKeyShortcutEvent(@NonNull KeyEvent event) {
        if ((this.mPrivateFlags & 18) == 18) {
            return super.dispatchKeyShortcutEvent(event);
        } else {
            return this.mFocused != null && (this.mFocused.mPrivateFlags & 16) == 16 ? this.mFocused.dispatchKeyShortcutEvent(event) : false;
        }
    }

    @Override
    public PointerIcon onResolvePointerIcon(@NonNull MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (!this.isOnScrollbarThumb(x, y) && !this.isDraggingScrollBar()) {
            int childrenCount = this.mChildrenCount;
            if (childrenCount != 0) {
                ArrayList<View> preorderedList = this.buildOrderedChildList();
                boolean customOrder = preorderedList == null && this.isChildrenDrawingOrderEnabled();
                View[] children = this.mChildren;
                for (int i = childrenCount - 1; i >= 0; i--) {
                    int childIndex = this.getAndVerifyPreorderedIndex(childrenCount, i, customOrder);
                    View child = getAndVerifyPreorderedView(preorderedList, children, childIndex);
                    if (child.canReceivePointerEvents() && this.isTransformedTouchPointInView(x, y, child, null)) {
                        PointerIcon pointerIcon = this.dispatchResolvePointerIcon(event, child);
                        if (pointerIcon != null) {
                            if (preorderedList != null) {
                                preorderedList.clear();
                            }
                            return pointerIcon;
                        }
                    }
                }
                if (preorderedList != null) {
                    preorderedList.clear();
                }
            }
            return super.onResolvePointerIcon(event);
        } else {
            return PointerIcon.getSystemIcon(1000);
        }
    }

    @Nullable
    PointerIcon dispatchResolvePointerIcon(MotionEvent event, @NonNull View child) {
        PointerIcon pointerIcon;
        if (!child.hasIdentityMatrix()) {
            MotionEvent transformedEvent = this.getTransformedMotionEvent(event, child);
            pointerIcon = child.onResolvePointerIcon(transformedEvent);
            transformedEvent.recycle();
        } else {
            float offsetX = (float) (this.mScrollX - child.mLeft);
            float offsetY = (float) (this.mScrollY - child.mTop);
            event.offsetLocation(offsetX, offsetY);
            pointerIcon = child.onResolvePointerIcon(event);
            event.offsetLocation(-offsetX, -offsetY);
        }
        return pointerIcon;
    }

    @Internal
    public void addTransientView(View view, int index) {
        if (index >= 0 && view != null) {
            if (view.mParent != null) {
                throw new IllegalStateException("The specified view already has a parent " + view.mParent);
            } else {
                if (this.mTransientIndices == null) {
                    this.mTransientIndices = new IntArrayList();
                    this.mTransientViews = new ArrayList();
                }
                int oldSize = this.mTransientIndices.size();
                if (oldSize > 0) {
                    int insertionIndex = 0;
                    while (insertionIndex < oldSize && index >= this.mTransientIndices.getInt(insertionIndex)) {
                        insertionIndex++;
                    }
                    this.mTransientIndices.add(insertionIndex, index);
                    this.mTransientViews.add(insertionIndex, view);
                } else {
                    this.mTransientIndices.add(index);
                    this.mTransientViews.add(view);
                }
                view.mParent = this;
                if (this.mAttachInfo != null) {
                    view.dispatchAttachedToWindow(this.mAttachInfo, this.mViewFlags & 12);
                }
                this.invalidate();
            }
        }
    }

    @Internal
    public void removeTransientView(View view) {
        if (this.mTransientViews != null) {
            int size = this.mTransientViews.size();
            for (int i = 0; i < size; i++) {
                if (view == this.mTransientViews.get(i)) {
                    this.mTransientViews.remove(i);
                    this.mTransientIndices.removeInt(i);
                    view.mParent = null;
                    if (view.mAttachInfo != null) {
                        view.dispatchDetachedFromWindow();
                    }
                    this.invalidate();
                    return;
                }
            }
        }
    }

    @Internal
    public int getTransientViewCount() {
        return this.mTransientIndices == null ? 0 : this.mTransientIndices.size();
    }

    @Internal
    public int getTransientViewIndex(int position) {
        return position >= 0 && this.mTransientIndices != null && position < this.mTransientIndices.size() ? this.mTransientIndices.getInt(position) : -1;
    }

    @Internal
    public View getTransientView(int position) {
        return this.mTransientViews != null && position < this.mTransientViews.size() ? (View) this.mTransientViews.get(position) : null;
    }

    public void addView(@NonNull View child) {
        this.addView(child, -1);
    }

    public void addView(@NonNull View child, int index) {
        ViewGroup.LayoutParams params = child.getLayoutParams();
        if (params == null) {
            params = this.generateDefaultLayoutParams();
        }
        this.addView(child, index, params);
    }

    public void addView(@NonNull View child, int width, int height) {
        ViewGroup.LayoutParams params = this.generateDefaultLayoutParams();
        params.width = width;
        params.height = height;
        this.addView(child, -1, params);
    }

    @Override
    public void addView(@NonNull View child, @NonNull ViewGroup.LayoutParams params) {
        this.addView(child, -1, params);
    }

    public void addView(@NonNull View child, int index, @NonNull ViewGroup.LayoutParams params) {
        this.requestLayout();
        this.invalidate();
        this.addViewInner(child, index, params, false);
    }

    @Override
    public void updateViewLayout(@NonNull View view, @NonNull ViewGroup.LayoutParams params) {
        if (!this.checkLayoutParams(params)) {
            throw new IllegalArgumentException("Invalid LayoutParams supplied to " + this);
        } else if (view.mParent != this) {
            throw new IllegalArgumentException("Given view not a child of " + this);
        } else {
            view.setLayoutParams(params);
        }
    }

    protected boolean addViewInLayout(@NonNull View child, int index, @NonNull ViewGroup.LayoutParams params) {
        return this.addViewInLayout(child, index, params, false);
    }

    protected boolean addViewInLayout(@NonNull View child, int index, @NonNull ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        child.mParent = null;
        this.addViewInner(child, index, params, preventRequestLayout);
        return true;
    }

    protected void cleanupLayoutState(@NonNull View child) {
        child.mPrivateFlags &= -4097;
    }

    private void addViewInner(@NonNull View child, int index, @NonNull ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        if (this.mTransition != null) {
            this.mTransition.cancel(3);
        }
        if (child.getParent() != null) {
            throw new IllegalStateException("The specified child already has a parent. You must call removeView() on the child's parent first.");
        } else {
            if (this.mTransition != null) {
                this.mTransition.addChild(this, child);
            }
            if (!this.checkLayoutParams(params)) {
                params = this.generateLayoutParams(params);
            }
            if (preventRequestLayout) {
                child.mLayoutParams = params;
            } else {
                child.setLayoutParams(params);
            }
            if (index < 0) {
                index = this.mChildrenCount;
            }
            this.addInArray(child, index);
            if (preventRequestLayout) {
                child.assignParent(this);
            } else {
                child.mParent = this;
            }
            boolean childHasFocus = child.hasFocus();
            if (childHasFocus) {
                this.requestChildFocus(child, child.findFocus());
            }
            AttachInfo attachInfo = this.mAttachInfo;
            if (attachInfo != null && (this.mGroupFlags & 4194304) == 0) {
                child.dispatchAttachedToWindow(this.mAttachInfo, this.mViewFlags & 12);
            }
            if (child.isLayoutDirectionInherited()) {
                child.resetRtlProperties();
            }
            this.onViewAdded(child);
            if ((child.mViewFlags & 4194304) == 4194304) {
                this.mGroupFlags |= 65536;
            }
            if (child.hasTransientState()) {
                this.childHasTransientStateChanged(child, true);
            }
            if (this.mTransientIndices != null) {
                int transientCount = this.mTransientIndices.size();
                for (int i = 0; i < transientCount; i++) {
                    int oldIndex = this.mTransientIndices.getInt(i);
                    if (index <= oldIndex) {
                        this.mTransientIndices.set(i, oldIndex + 1);
                    }
                }
            }
        }
    }

    protected void onViewAdded(View child) {
    }

    protected void onViewRemoved(View child) {
    }

    public int indexOfChild(View child) {
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            if (children[i] == child) {
                return i;
            }
        }
        return -1;
    }

    public View getChildAt(int index) {
        return index >= 0 && index < this.mChildrenCount ? this.mChildren[index] : null;
    }

    public int getChildCount() {
        return this.mChildrenCount;
    }

    private void addInArray(View child, int index) {
        View[] children = this.mChildren;
        int count = this.mChildrenCount;
        int size = children.length;
        if (index == count) {
            if (size == count) {
                this.mChildren = new View[size + 12];
                System.arraycopy(children, 0, this.mChildren, 0, size);
                children = this.mChildren;
            }
            children[this.mChildrenCount++] = child;
        } else {
            if (index >= count) {
                throw new IndexOutOfBoundsException("index=" + index + " count=" + count);
            }
            if (size == count) {
                this.mChildren = new View[size + 12];
                System.arraycopy(children, 0, this.mChildren, 0, index);
                System.arraycopy(children, index, this.mChildren, index + 1, count - index);
                children = this.mChildren;
            } else {
                System.arraycopy(children, index, children, index + 1, count - index);
            }
            children[index] = child;
            this.mChildrenCount++;
        }
    }

    private void removeFromArray(int index) {
        View[] children = this.mChildren;
        if (this.mTransitioningViews == null || !this.mTransitioningViews.contains(children[index])) {
            children[index].mParent = null;
        }
        int count = this.mChildrenCount;
        if (index == count - 1) {
            children[--this.mChildrenCount] = null;
        } else {
            if (index >= count) {
                throw new IndexOutOfBoundsException();
            }
            System.arraycopy(children, index + 1, children, index, count - index - 1);
            children[--this.mChildrenCount] = null;
        }
    }

    private void removeFromArray(int start, int count) {
        View[] children = this.mChildren;
        int childrenCount = this.mChildrenCount;
        start = Math.max(0, start);
        int end = Math.min(childrenCount, start + count);
        if (start != end) {
            if (end == childrenCount) {
                for (int i = start; i < end; i++) {
                    children[i].mParent = null;
                    children[i] = null;
                }
            } else {
                for (int i = start; i < end; i++) {
                    children[i].mParent = null;
                }
                System.arraycopy(children, end, children, start, childrenCount - end);
                for (int i = childrenCount - (end - start); i < childrenCount; i++) {
                    children[i] = null;
                }
            }
            this.mChildrenCount -= end - start;
        }
    }

    @Override
    public void removeView(@NonNull View view) {
        if (this.removeViewInternal(view)) {
            this.requestLayout();
            this.invalidate();
        }
    }

    public void removeViewInLayout(@NonNull View view) {
        this.removeViewInternal(view);
    }

    public void removeViewsInLayout(int start, int count) {
        this.removeViewsInternal(start, count);
    }

    public void removeViewAt(int index) {
        this.removeViewInternal(index, this.getChildAt(index));
        this.requestLayout();
        this.invalidate();
    }

    public void removeViews(int start, int count) {
        this.removeViewsInternal(start, count);
        this.requestLayout();
        this.invalidate();
    }

    private boolean removeViewInternal(View view) {
        int index = this.indexOfChild(view);
        if (index >= 0) {
            this.removeViewInternal(index, view);
            return true;
        } else {
            return false;
        }
    }

    private void removeViewInternal(int index, View view) {
        if (this.mTransition != null) {
            this.mTransition.removeChild(this, view);
        }
        boolean clearChildFocus = false;
        if (view == this.mFocused) {
            view.unFocus(null);
            clearChildFocus = true;
        }
        if (view == this.mFocusedInCluster) {
            this.clearFocusedInCluster(view);
        }
        this.cancelTouchTarget(view);
        this.cancelHoverTarget(view);
        if (this.mTransitioningViews != null && this.mTransitioningViews.contains(view)) {
            this.addDisappearingView(view);
        } else if (view.mAttachInfo != null) {
            view.dispatchDetachedFromWindow();
        }
        if (view.hasTransientState()) {
            this.childHasTransientStateChanged(view, false);
        }
        this.removeFromArray(index);
        if (view == this.mDefaultFocus) {
            this.clearDefaultFocus(view);
        }
        if (clearChildFocus) {
            this.clearChildFocus(view);
            if (!this.rootViewRequestFocus()) {
                this.notifyGlobalFocusCleared(this);
            }
        }
        this.onViewRemoved(view);
        int transientCount = this.mTransientIndices == null ? 0 : this.mTransientIndices.size();
        for (int i = 0; i < transientCount; i++) {
            int oldIndex = this.mTransientIndices.getInt(i);
            if (index < oldIndex) {
                this.mTransientIndices.set(i, oldIndex - 1);
            }
        }
    }

    public void setLayoutTransition(LayoutTransition transition) {
        if (this.mTransition != null) {
            LayoutTransition previousTransition = this.mTransition;
            previousTransition.cancel();
            previousTransition.removeTransitionListener(this.mLayoutTransitionListener);
        }
        this.mTransition = transition;
        if (this.mTransition != null) {
            this.mTransition.addTransitionListener(this.mLayoutTransitionListener);
        }
    }

    public LayoutTransition getLayoutTransition() {
        return this.mTransition;
    }

    private void removeViewsInternal(int start, int count) {
        int end = start + count;
        if (start >= 0 && count >= 0 && end <= this.mChildrenCount) {
            View focused = this.mFocused;
            boolean detach = this.mAttachInfo != null;
            boolean clearChildFocus = false;
            View clearDefaultFocus = null;
            View[] children = this.mChildren;
            for (int i = start; i < end; i++) {
                View view = children[i];
                if (this.mTransition != null) {
                    this.mTransition.removeChild(this, view);
                }
                if (view == focused) {
                    view.unFocus(null);
                    clearChildFocus = true;
                }
                if (view == this.mDefaultFocus) {
                    clearDefaultFocus = view;
                }
                if (view == this.mFocusedInCluster) {
                    this.clearFocusedInCluster(view);
                }
                this.cancelTouchTarget(view);
                this.cancelHoverTarget(view);
                if (this.mTransitioningViews != null && this.mTransitioningViews.contains(view)) {
                    this.addDisappearingView(view);
                } else if (detach) {
                    view.dispatchDetachedFromWindow();
                }
                if (view.hasTransientState()) {
                    this.childHasTransientStateChanged(view, false);
                }
                this.onViewRemoved(view);
            }
            this.removeFromArray(start, count);
            if (clearDefaultFocus != null) {
                this.clearDefaultFocus(clearDefaultFocus);
            }
            if (clearChildFocus) {
                this.clearChildFocus(focused);
                if (!this.rootViewRequestFocus()) {
                    this.notifyGlobalFocusCleared(focused);
                }
            }
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public void removeAllViews() {
        this.removeAllViewsInLayout();
        this.requestLayout();
        this.invalidate();
    }

    public void removeAllViewsInLayout() {
        int count = this.mChildrenCount;
        if (count > 0) {
            View[] children = this.mChildren;
            this.mChildrenCount = 0;
            View focused = this.mFocused;
            boolean detach = this.mAttachInfo != null;
            boolean clearChildFocus = false;
            for (int i = count - 1; i >= 0; i--) {
                View view = children[i];
                if (this.mTransition != null) {
                    this.mTransition.removeChild(this, view);
                }
                if (view == focused) {
                    view.unFocus(null);
                    clearChildFocus = true;
                }
                this.cancelTouchTarget(view);
                this.cancelHoverTarget(view);
                if (this.mTransitioningViews != null && this.mTransitioningViews.contains(view)) {
                    this.addDisappearingView(view);
                } else if (detach) {
                    view.dispatchDetachedFromWindow();
                }
                if (view.hasTransientState()) {
                    this.childHasTransientStateChanged(view, false);
                }
                this.onViewRemoved(view);
                view.mParent = null;
                children[i] = null;
            }
            if (this.mDefaultFocus != null) {
                this.clearDefaultFocus(this.mDefaultFocus);
            }
            if (this.mFocusedInCluster != null) {
                this.clearFocusedInCluster(this.mFocusedInCluster);
            }
            if (clearChildFocus) {
                this.clearChildFocus(focused);
                if (!this.rootViewRequestFocus()) {
                    this.notifyGlobalFocusCleared(focused);
                }
            }
        }
    }

    protected void removeDetachedView(@NonNull View child, boolean animate) {
        if (this.mTransition != null) {
            this.mTransition.removeChild(this, child);
        }
        if (child == this.mFocused) {
            child.clearFocus();
        }
        if (child == this.mDefaultFocus) {
            this.clearDefaultFocus(child);
        }
        if (child == this.mFocusedInCluster) {
            this.clearFocusedInCluster(child);
        }
        this.cancelTouchTarget(child);
        this.cancelHoverTarget(child);
        if (this.mTransitioningViews != null && this.mTransitioningViews.contains(child)) {
            this.addDisappearingView(child);
        } else if (child.mAttachInfo != null) {
            child.dispatchDetachedFromWindow();
        }
        if (child.hasTransientState()) {
            this.childHasTransientStateChanged(child, false);
        }
        this.onViewRemoved(child);
    }

    protected void attachViewToParent(View child, int index, ViewGroup.LayoutParams params) {
        child.mLayoutParams = params;
        if (index < 0) {
            index = this.mChildrenCount;
        }
        this.addInArray(child, index);
        child.mParent = this;
        child.mPrivateFlags = child.mPrivateFlags & -2097153 & -32769 | 32 | -2147483648;
        this.mPrivateFlags |= Integer.MIN_VALUE;
        if (child.hasFocus()) {
            this.requestChildFocus(child, child.findFocus());
        }
        this.dispatchVisibilityAggregated(this.isAttachedToWindow() && this.getWindowVisibility() == 0 && this.isShown());
    }

    protected void detachViewFromParent(View child) {
        this.removeFromArray(this.indexOfChild(child));
    }

    protected void detachViewFromParent(int index) {
        this.removeFromArray(index);
    }

    protected void detachViewsFromParent(int start, int count) {
        this.removeFromArray(Math.max(0, start), Math.min(this.mChildrenCount, count));
    }

    protected void detachAllViewsFromParent() {
        int count = this.mChildrenCount;
        if (count > 0) {
            View[] children = this.mChildren;
            this.mChildrenCount = 0;
            for (int i = count - 1; i >= 0; i--) {
                children[i].mParent = null;
                children[i] = null;
            }
        }
    }

    public int getDescendantFocusability() {
        return this.mGroupFlags & 393216;
    }

    public void setDescendantFocusability(int focusability) {
        switch(focusability) {
            case 131072:
            case 262144:
            case 393216:
                this.mGroupFlags &= -393217;
                this.mGroupFlags |= focusability & 393216;
                return;
            default:
                throw new IllegalArgumentException("must be one of FOCUS_BEFORE_DESCENDANTS, FOCUS_AFTER_DESCENDANTS, FOCUS_BLOCK_DESCENDANTS");
        }
    }

    @Override
    void handleFocusGainInternal(int direction, @Nullable Rect previouslyFocusedRect) {
        if (this.mFocused != null) {
            this.mFocused.unFocus(this);
            this.mFocused = null;
            this.mFocusedInCluster = null;
        }
        super.handleFocusGainInternal(direction, previouslyFocusedRect);
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        if (this.getDescendantFocusability() != 393216) {
            super.unFocus(focused);
            if (this.mFocused != child) {
                if (this.mFocused != null) {
                    this.mFocused.unFocus(focused);
                }
                this.mFocused = child;
            }
            if (this.mParent != null) {
                this.mParent.requestChildFocus(this, focused);
            }
        }
    }

    void setDefaultFocus(View child) {
        if (this.mDefaultFocus == null || !this.mDefaultFocus.isFocusedByDefault()) {
            this.mDefaultFocus = child;
            if (this.mParent instanceof ViewGroup) {
                ((ViewGroup) this.mParent).setDefaultFocus(this);
            }
        }
    }

    void clearDefaultFocus(View child) {
        if (this.mDefaultFocus == child || this.mDefaultFocus == null || !this.mDefaultFocus.isFocusedByDefault()) {
            this.mDefaultFocus = null;
            for (int i = 0; i < this.mChildrenCount; i++) {
                View sibling = this.mChildren[i];
                if (sibling.isFocusedByDefault()) {
                    this.mDefaultFocus = sibling;
                    return;
                }
                if (this.mDefaultFocus == null && sibling.hasDefaultFocus()) {
                    this.mDefaultFocus = sibling;
                }
            }
            if (this.mParent instanceof ViewGroup) {
                ((ViewGroup) this.mParent).clearDefaultFocus(this);
            }
        }
    }

    @Override
    boolean hasDefaultFocus() {
        return this.mDefaultFocus != null || super.hasDefaultFocus();
    }

    void clearFocusedInCluster(View child) {
        if (this.mFocusedInCluster == child) {
            this.clearFocusedInCluster();
        }
    }

    void clearFocusedInCluster() {
        View top = this.findKeyboardNavigationCluster();
        ViewParent parent = this;
        do {
            ((ViewGroup) parent).mFocusedInCluster = null;
            if (parent == top) {
                break;
            }
            parent = parent.getParent();
        } while (parent instanceof ViewGroup);
    }

    @Override
    public void dispatchWindowFocusChanged(boolean hasFocus) {
        super.dispatchWindowFocusChanged(hasFocus);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            children[i].dispatchWindowFocusChanged(hasFocus);
        }
    }

    @Override
    public void addFocusables(@NonNull ArrayList<View> views, int direction, int focusableMode) {
        int focusableCount = views.size();
        int descendantFocusability = this.getDescendantFocusability();
        if (descendantFocusability == 393216) {
            super.addFocusables(views, direction, focusableMode);
        } else {
            if (descendantFocusability == 131072) {
                super.addFocusables(views, direction, focusableMode);
            }
            int count = 0;
            View[] children = new View[this.mChildrenCount];
            for (int i = 0; i < this.mChildrenCount; i++) {
                View child = this.mChildren[i];
                if ((child.mViewFlags & 12) == 0) {
                    children[count++] = child;
                }
            }
            FocusFinder.sort(children, 0, count, this, this.isLayoutRtl());
            for (int ix = 0; ix < count; ix++) {
                children[ix].addFocusables(views, direction, focusableMode);
            }
            if (descendantFocusability == 262144 && focusableCount == views.size()) {
                super.addFocusables(views, direction, focusableMode);
            }
        }
    }

    @Override
    public void addTouchables(@NonNull ArrayList<View> views) {
        super.addTouchables(views);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            View child = children[i];
            if ((child.mViewFlags & 12) == 0) {
                child.addTouchables(views);
            }
        }
    }

    @Override
    public void addKeyboardNavigationClusters(@NonNull Collection<View> views, int direction) {
        int focusableCount = views.size();
        if (this.isKeyboardNavigationCluster()) {
            boolean blockedFocus = this.getTouchscreenBlocksFocus();
            try {
                this.setTouchscreenBlocksFocusNoRefocus(false);
                super.addKeyboardNavigationClusters(views, direction);
            } finally {
                this.setTouchscreenBlocksFocusNoRefocus(blockedFocus);
            }
        } else {
            super.addKeyboardNavigationClusters(views, direction);
        }
        if (focusableCount == views.size()) {
            if (this.getDescendantFocusability() != 393216) {
                int count = 0;
                View[] visibleChildren = new View[this.mChildrenCount];
                for (int i = 0; i < this.mChildrenCount; i++) {
                    View child = this.mChildren[i];
                    if ((child.mViewFlags & 12) == 0) {
                        visibleChildren[count++] = child;
                    }
                }
                FocusFinder.sort(visibleChildren, 0, count, this, this.isLayoutRtl());
                for (int ix = 0; ix < count; ix++) {
                    visibleChildren[ix].addKeyboardNavigationClusters(views, direction);
                }
            }
        }
    }

    public void setTouchscreenBlocksFocus(boolean touchscreenBlocksFocus) {
        if (touchscreenBlocksFocus) {
            this.mGroupFlags |= 67108864;
            if (this.hasFocus() && !this.isKeyboardNavigationCluster()) {
                View focusedChild = this.getDeepestFocusedChild();
                if (!focusedChild.isFocusableInTouchMode()) {
                    View newFocus = this.focusSearch(2);
                    if (newFocus != null) {
                        newFocus.requestFocus();
                    }
                }
            }
        } else {
            this.mGroupFlags &= -67108865;
        }
    }

    private void setTouchscreenBlocksFocusNoRefocus(boolean touchscreenBlocksFocus) {
        if (touchscreenBlocksFocus) {
            this.mGroupFlags |= 67108864;
        } else {
            this.mGroupFlags &= -67108865;
        }
    }

    public boolean getTouchscreenBlocksFocus() {
        return (this.mGroupFlags & 67108864) != 0;
    }

    boolean shouldBlockFocusForTouchscreen() {
        return this.getTouchscreenBlocksFocus() && (!this.isKeyboardNavigationCluster() || !this.hasFocus() && this.findKeyboardNavigationCluster() == this);
    }

    @Override
    public void childHasTransientStateChanged(View child, boolean childHasTransientState) {
        boolean oldHasTransientState = this.hasTransientState();
        if (childHasTransientState) {
            this.mChildCountWithTransientState++;
        } else {
            this.mChildCountWithTransientState--;
        }
        boolean newHasTransientState = this.hasTransientState();
        if (this.mParent != null && oldHasTransientState != newHasTransientState) {
            this.mParent.childHasTransientStateChanged(this, newHasTransientState);
        }
    }

    @Override
    public boolean hasTransientState() {
        return this.mChildCountWithTransientState > 0 || super.hasTransientState();
    }

    @Override
    public void clearChildFocus(View child) {
        this.mFocused = null;
        if (this.mParent != null) {
            this.mParent.clearChildFocus(this);
        }
    }

    @Override
    public void clearFocus() {
        if (this.mFocused == null) {
            super.clearFocus();
        } else {
            View focused = this.mFocused;
            this.mFocused = null;
            focused.clearFocus();
        }
    }

    @Override
    void unFocus(View focused) {
        if (this.mFocused == null) {
            super.unFocus(focused);
        } else {
            this.mFocused.unFocus(focused);
            this.mFocused = null;
        }
    }

    @Override
    boolean hasFocusable(boolean allowAutoFocus, boolean dispatchExplicit) {
        if ((this.mViewFlags & 12) != 0) {
            return false;
        } else if ((allowAutoFocus || this.getFocusable() != 16) && this.isFocusable()) {
            return true;
        } else {
            int descendantFocusability = this.getDescendantFocusability();
            return descendantFocusability != 393216 ? this.hasFocusableChild(dispatchExplicit) : false;
        }
    }

    boolean hasFocusableChild(boolean dispatchExplicit) {
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            View child = children[i];
            if (dispatchExplicit && child.hasExplicitFocusable() || !dispatchExplicit && child.hasFocusable()) {
                return true;
            }
        }
        return false;
    }

    public View getFocusedChild() {
        return this.mFocused;
    }

    View getDeepestFocusedChild() {
        for (View v = this; v != null; v = v instanceof ViewGroup ? ((ViewGroup) v).getFocusedChild() : null) {
            if (v.isFocused()) {
                return v;
            }
        }
        return null;
    }

    @Override
    public boolean hasFocus() {
        return (this.mPrivateFlags & 2) != 0 || this.mFocused != null;
    }

    @Nullable
    @Override
    public View findFocus() {
        if (this.isFocused()) {
            return this;
        } else {
            return this.mFocused != null ? this.mFocused.findFocus() : null;
        }
    }

    public final void offsetDescendantRectToMyCoords(View descendant, Rect rect) {
        this.offsetRectBetweenParentAndChild(descendant, rect, true, false);
    }

    public final void offsetRectIntoDescendantCoords(View descendant, Rect rect) {
        this.offsetRectBetweenParentAndChild(descendant, rect, false, false);
    }

    void offsetRectBetweenParentAndChild(View descendant, Rect rect, boolean offsetFromChildToParent, boolean clipToBounds) {
        if (descendant != this) {
            ViewParent theParent;
            for (theParent = descendant.mParent; theParent instanceof View && theParent != this; theParent = descendant.mParent) {
                if (offsetFromChildToParent) {
                    rect.offset(descendant.mLeft - descendant.mScrollX, descendant.mTop - descendant.mScrollY);
                    if (clipToBounds) {
                        View p = (View) theParent;
                        boolean intersected = rect.intersect(0, 0, p.mRight - p.mLeft, p.mBottom - p.mTop);
                        if (!intersected) {
                            rect.setEmpty();
                        }
                    }
                } else {
                    if (clipToBounds) {
                        View p = (View) theParent;
                        boolean intersected = rect.intersect(0, 0, p.mRight - p.mLeft, p.mBottom - p.mTop);
                        if (!intersected) {
                            rect.setEmpty();
                        }
                    }
                    rect.offset(descendant.mScrollX - descendant.mLeft, descendant.mScrollY - descendant.mTop);
                }
                descendant = (View) theParent;
            }
            if (theParent == this) {
                if (offsetFromChildToParent) {
                    rect.offset(descendant.mLeft - descendant.mScrollX, descendant.mTop - descendant.mScrollY);
                } else {
                    rect.offset(descendant.mScrollX - descendant.mLeft, descendant.mScrollY - descendant.mTop);
                }
            } else {
                throw new IllegalArgumentException("parameter must be a descendant of this view");
            }
        }
    }

    public void offsetChildrenTopAndBottom(int offset) {
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            children[i].offsetTopAndBottom(offset);
        }
    }

    @Override
    public boolean getChildVisibleRect(View child, Rect r, @Nullable Point offset) {
        return this.getChildVisibleRect(child, r, offset, false);
    }

    @Internal
    public boolean getChildVisibleRect(@NonNull View child, Rect r, @Nullable Point offset, boolean forceParentCheck) {
        RectF rect = this.mAttachInfo != null ? this.mAttachInfo.mTmpTransformRect : new RectF();
        rect.set(r);
        if (!child.hasIdentityMatrix()) {
            child.getMatrix().mapRect(rect);
        }
        int dx = child.mLeft - this.mScrollX;
        int dy = child.mTop - this.mScrollY;
        rect.offset((float) dx, (float) dy);
        if (offset != null) {
            if (!child.hasIdentityMatrix()) {
                float[] position = this.mAttachInfo != null ? this.mAttachInfo.mTmpTransformLocation : new float[2];
                position[0] = (float) offset.x;
                position[1] = (float) offset.y;
                child.getMatrix().mapPoint(position);
                offset.x = Math.round(position[0]);
                offset.y = Math.round(position[1]);
            }
            offset.x += dx;
            offset.y += dy;
        }
        int width = this.mRight - this.mLeft;
        int height = this.mBottom - this.mTop;
        boolean rectIsVisible = true;
        if (this.mParent == null || this.mParent instanceof ViewGroup && ((ViewGroup) this.mParent).getClipChildren()) {
            rectIsVisible = rect.intersect(0.0F, 0.0F, (float) width, (float) height);
        }
        if ((forceParentCheck || rectIsVisible) && (this.mGroupFlags & 34) == 34) {
            rectIsVisible = rect.intersect((float) this.mPaddingLeft, (float) this.mPaddingTop, (float) (width - this.mPaddingRight), (float) (height - this.mPaddingBottom));
        }
        r.set((int) Math.floor((double) rect.left), (int) Math.floor((double) rect.top), (int) Math.ceil((double) rect.right), (int) Math.ceil((double) rect.bottom));
        if ((forceParentCheck || rectIsVisible) && this.mParent != null) {
            if (this.mParent instanceof ViewGroup) {
                rectIsVisible = ((ViewGroup) this.mParent).getChildVisibleRect(this, r, offset, forceParentCheck);
            } else {
                rectIsVisible = this.mParent.getChildVisibleRect(this, r, offset);
            }
        }
        return rectIsVisible;
    }

    @Override
    public View focusSearch(View focused, int direction) {
        if (this.isRootNamespace()) {
            return FocusFinder.getInstance().findNextFocus(this, focused, direction);
        } else {
            return this.mParent != null ? this.mParent.focusSearch(focused, direction) : null;
        }
    }

    @Override
    public boolean requestChildRectangleOnScreen(View child, Rect rectangle, boolean immediate) {
        return false;
    }

    @Override
    public void focusableViewAvailable(View v) {
        if (this.mParent != null && this.getDescendantFocusability() != 393216 && (this.mViewFlags & 12) == 0 && (!this.isFocused() || this.getDescendantFocusability() == 262144)) {
            this.mParent.focusableViewAvailable(v);
        }
    }

    @Override
    public boolean showContextMenuForChild(View originalView, float x, float y) {
        return this.mParent != null && this.mParent.showContextMenuForChild(originalView, x, y);
    }

    @Override
    public ActionMode startActionModeForChild(View originalView, ActionMode.Callback callback, int type) {
        return this.mParent != null ? this.mParent.startActionModeForChild(originalView, callback, type) : null;
    }

    public void setClipToPadding(boolean clipToPadding) {
        if (this.hasBooleanFlag(2) != clipToPadding) {
            this.setBooleanFlag(2, clipToPadding);
            this.invalidate();
        }
    }

    public boolean getClipToPadding() {
        return this.hasBooleanFlag(2);
    }

    @Override
    public void dispatchSetSelected(boolean selected) {
        View[] children = this.mChildren;
        int count = this.mChildrenCount;
        for (int i = 0; i < count; i++) {
            children[i].setSelected(selected);
        }
    }

    @Override
    public void dispatchSetActivated(boolean activated) {
        View[] children = this.mChildren;
        int count = this.mChildrenCount;
        for (int i = 0; i < count; i++) {
            children[i].setActivated(activated);
        }
    }

    @Override
    protected void dispatchSetPressed(boolean pressed) {
        View[] children = this.mChildren;
        int count = this.mChildrenCount;
        for (int i = 0; i < count; i++) {
            View child = children[i];
            if (!pressed || !child.isClickable() && !child.isLongClickable()) {
                child.setPressed(pressed);
            }
        }
    }

    @Override
    public void dispatchDrawableHotspotChanged(float x, float y) {
        int count = this.mChildrenCount;
        if (count != 0) {
            View[] children = this.mChildren;
            for (int i = 0; i < count; i++) {
                View child = children[i];
                boolean nonActionable = !child.isClickable() && !child.isLongClickable();
                boolean duplicatesState = (child.mViewFlags & 4194304) != 0;
                if (nonActionable || duplicatesState) {
                    float[] point = this.getTempLocationF();
                    point[0] = x;
                    point[1] = y;
                    this.transformPointToViewLocal(point, child);
                    child.drawableHotspotChanged(point[0], point[1]);
                }
            }
        }
    }

    @Nullable
    @Override
    protected <T extends View> T findViewByPredicateTraversal(@NonNull Predicate<View> predicate, @Nullable View childToSkip) {
        if (predicate.test(this)) {
            return (T) this;
        } else {
            View[] where = this.mChildren;
            int len = this.mChildrenCount;
            for (int i = 0; i < len; i++) {
                View v = where[i];
                if (v != childToSkip && (v.mPrivateFlags & 8) == 0) {
                    v = v.findViewByPredicate(predicate);
                    if (v != null) {
                        return (T) v;
                    }
                }
            }
            return null;
        }
    }

    @Nullable
    @Override
    protected <T extends View> T findViewTraversal(int id) {
        if (id == this.mID) {
            return (T) this;
        } else {
            View[] views = this.mChildren;
            int count = this.mChildrenCount;
            for (int i = 0; i < count; i++) {
                View v = views[i].findViewTraversal(id);
                if (v != null) {
                    return (T) v;
                }
            }
            return null;
        }
    }

    @Override
    public void bringChildToFront(View child) {
        int index = this.indexOfChild(child);
        if (index >= 0) {
            this.removeFromArray(index);
            this.addInArray(child, this.mChildrenCount);
            child.mParent = this;
            this.requestLayout();
            this.invalidate();
        }
    }

    protected boolean hasBooleanFlag(int flag) {
        return (this.mGroupFlags & flag) == flag;
    }

    protected void setBooleanFlag(int flag, boolean value) {
        if (value) {
            this.mGroupFlags |= flag;
        } else {
            this.mGroupFlags &= ~flag;
        }
    }

    protected boolean isChildrenDrawingOrderEnabled() {
        return (this.mGroupFlags & 1024) == 1024;
    }

    protected void setChildrenDrawingOrderEnabled(boolean enabled) {
        this.setBooleanFlag(1024, enabled);
    }

    protected int getChildDrawingOrder(int childCount, int drawingPosition) {
        return drawingPosition;
    }

    public final int getChildDrawingOrder(int drawingPosition) {
        return this.getChildDrawingOrder(this.getChildCount(), drawingPosition);
    }

    private boolean hasChildWithZ() {
        for (int i = 0; i < this.mChildrenCount; i++) {
            if (this.mChildren[i].getZ() != 0.0F) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    ArrayList<View> buildOrderedChildList() {
        int childrenCount = this.mChildrenCount;
        if (childrenCount > 1 && this.hasChildWithZ()) {
            if (this.mPreSortedChildren == null) {
                this.mPreSortedChildren = new ArrayList(childrenCount);
            } else {
                this.mPreSortedChildren.clear();
                this.mPreSortedChildren.ensureCapacity(childrenCount);
            }
            boolean customOrder = this.isChildrenDrawingOrderEnabled();
            for (int i = 0; i < childrenCount; i++) {
                int childIndex = this.getAndVerifyPreorderedIndex(childrenCount, i, customOrder);
                View nextChild = this.mChildren[childIndex];
                float currentZ = nextChild.getZ();
                int insertIndex = i;
                while (insertIndex > 0 && ((View) this.mPreSortedChildren.get(insertIndex - 1)).getZ() > currentZ) {
                    insertIndex--;
                }
                this.mPreSortedChildren.add(insertIndex, nextChild);
            }
            return this.mPreSortedChildren;
        } else {
            return null;
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if ((this.mGroupFlags & 65536) != 0) {
            if ((this.mGroupFlags & 8192) != 0) {
                throw new IllegalStateException("addStateFromChildren cannot be enabled if a child has duplicateParentState set to true");
            }
            View[] children = this.mChildren;
            int count = this.mChildrenCount;
            for (int i = 0; i < count; i++) {
                View child = children[i];
                if ((child.mViewFlags & 4194304) != 0) {
                    child.refreshDrawableState();
                }
            }
        }
    }

    @Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        View[] children = this.mChildren;
        int count = this.mChildrenCount;
        for (int i = 0; i < count; i++) {
            children[i].jumpDrawablesToCurrentState();
        }
    }

    @NonNull
    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        if ((this.mGroupFlags & 8192) == 0) {
            return super.onCreateDrawableState(extraSpace);
        } else {
            int need = 0;
            int n = this.getChildCount();
            for (int i = 0; i < n; i++) {
                int[] childState = this.getChildAt(i).getDrawableState();
                if (childState != null) {
                    need += childState.length;
                }
            }
            int[] state = super.onCreateDrawableState(extraSpace + need);
            for (int ix = 0; ix < n; ix++) {
                int[] childState = this.getChildAt(ix).getDrawableState();
                if (childState != null) {
                    mergeDrawableStates(state, childState);
                }
            }
            return state;
        }
    }

    public void setAddStatesFromChildren(boolean addsStates) {
        if (addsStates) {
            this.mGroupFlags |= 8192;
        } else {
            this.mGroupFlags &= -8193;
        }
        this.refreshDrawableState();
    }

    public boolean addStatesFromChildren() {
        return (this.mGroupFlags & 8192) != 0;
    }

    @Override
    public void childDrawableStateChanged(View child) {
        if ((this.mGroupFlags & 8192) != 0) {
            this.refreshDrawableState();
        }
    }

    @Internal
    public void requestTransitionStart(LayoutTransition transition) {
        ViewRoot viewAncestor = this.mAttachInfo != null ? this.mAttachInfo.mViewRoot : null;
        if (viewAncestor != null) {
            viewAncestor.requestTransitionStart(transition);
        }
    }

    @Internal
    @Override
    public boolean resolveRtlPropertiesIfNeeded() {
        boolean result = super.resolveRtlPropertiesIfNeeded();
        if (result) {
            int count = this.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = this.getChildAt(i);
                if (child.isLayoutDirectionInherited()) {
                    child.resolveRtlPropertiesIfNeeded();
                }
            }
        }
        return result;
    }

    @Internal
    @Override
    public boolean resolveLayoutDirection() {
        boolean result = super.resolveLayoutDirection();
        if (result) {
            int count = this.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = this.getChildAt(i);
                if (child.isLayoutDirectionInherited()) {
                    child.resolveLayoutDirection();
                }
            }
        }
        return result;
    }

    @Internal
    @Override
    public boolean resolveTextDirection() {
        boolean result = super.resolveTextDirection();
        if (result) {
            int count = this.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = this.getChildAt(i);
                if (child.isTextDirectionInherited()) {
                    child.resolveTextDirection();
                }
            }
        }
        return result;
    }

    @Internal
    @Override
    public boolean resolveTextAlignment() {
        boolean result = super.resolveTextAlignment();
        if (result) {
            int count = this.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = this.getChildAt(i);
                if (child.isTextAlignmentInherited()) {
                    child.resolveTextAlignment();
                }
            }
        }
        return result;
    }

    @Internal
    @Override
    public void resolvePadding() {
        super.resolvePadding();
        int count = this.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = this.getChildAt(i);
            if (child.isLayoutDirectionInherited() && !child.isPaddingResolved()) {
                child.resolvePadding();
            }
        }
    }

    @Internal
    @Override
    protected void resolveDrawables() {
        super.resolveDrawables();
        int count = this.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = this.getChildAt(i);
            if (child.isLayoutDirectionInherited() && !child.areDrawablesResolved()) {
                child.resolveDrawables();
            }
        }
    }

    @Internal
    @Override
    public void resolveLayoutParams() {
        super.resolveLayoutParams();
        int count = this.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = this.getChildAt(i);
            child.resolveLayoutParams();
        }
    }

    @Internal
    @Override
    void resetResolvedLayoutDirection() {
        super.resetResolvedLayoutDirection();
        int count = this.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = this.getChildAt(i);
            if (child.isLayoutDirectionInherited()) {
                child.resetResolvedLayoutDirection();
            }
        }
    }

    @Internal
    @Override
    void resetResolvedTextDirection() {
        super.resetResolvedTextDirection();
        int count = this.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = this.getChildAt(i);
            if (child.isTextDirectionInherited()) {
                child.resetResolvedTextDirection();
            }
        }
    }

    @Internal
    @Override
    void resetResolvedTextAlignment() {
        super.resetResolvedTextAlignment();
        int count = this.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = this.getChildAt(i);
            if (child.isTextAlignmentInherited()) {
                child.resetResolvedTextAlignment();
            }
        }
    }

    @Internal
    @Override
    void resetResolvedPadding() {
        super.resetResolvedPadding();
        int count = this.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = this.getChildAt(i);
            if (child.isLayoutDirectionInherited()) {
                child.resetResolvedPadding();
            }
        }
    }

    @Internal
    @Override
    protected void resetResolvedDrawables() {
        super.resetResolvedDrawables();
        int count = this.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = this.getChildAt(i);
            if (child.isLayoutDirectionInherited()) {
                child.resetResolvedDrawables();
            }
        }
    }

    public boolean getClipChildren() {
        return (this.mGroupFlags & 1) != 0;
    }

    public void setClipChildren(boolean clipChildren) {
        boolean previousValue = (this.mGroupFlags & 1) == 1;
        if (clipChildren != previousValue) {
            this.setBooleanFlag(1, clipChildren);
            this.invalidate();
        }
    }

    @Override
    void dispatchCancelPendingInputEvents() {
        super.dispatchCancelPendingInputEvents();
        View[] children = this.mChildren;
        int count = this.mChildrenCount;
        for (int i = 0; i < count; i++) {
            children[i].dispatchCancelPendingInputEvents();
        }
    }

    protected void measureChildren(int widthMeasureSpec, int heightMeasureSpec) {
        int size = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < size; i++) {
            View child = children[i];
            if (child.getVisibility() != 8) {
                this.measureChild(child, widthMeasureSpec, heightMeasureSpec);
            }
        }
    }

    protected void measureChild(@NonNull View child, int parentWidthSpec, int parentHeightSpec) {
        ViewGroup.LayoutParams lp = child.getLayoutParams();
        int childWidthMeasureSpec = getChildMeasureSpec(parentWidthSpec, this.mPaddingLeft + this.mPaddingRight, lp.width);
        int childHeightMeasureSpec = getChildMeasureSpec(parentHeightSpec, this.mPaddingTop + this.mPaddingBottom, lp.height);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    protected void measureChildWithMargins(@NonNull View child, int parentWidthSpec, int widthUsed, int parentHeightSpec, int heightUsed) {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
        int childWidthMeasureSpec = getChildMeasureSpec(parentWidthSpec, this.mPaddingLeft + this.mPaddingRight + lp.leftMargin + lp.rightMargin + widthUsed, lp.width);
        int childHeightMeasureSpec = getChildMeasureSpec(parentHeightSpec, this.mPaddingTop + this.mPaddingBottom + lp.topMargin + lp.bottomMargin + heightUsed, lp.height);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    public static int getChildMeasureSpec(int spec, int padding, int childDimension) {
        int specSize = MeasureSpec.getSize(spec);
        int size = Math.max(0, specSize - padding);
        int resultSize = 0;
        int resultMode = 0;
        switch(MeasureSpec.getMode(spec)) {
            case Integer.MIN_VALUE:
                if (childDimension >= 0) {
                    resultSize = childDimension;
                    resultMode = 1073741824;
                } else if (childDimension == -1) {
                    resultSize = size;
                    resultMode = Integer.MIN_VALUE;
                } else if (childDimension == -2) {
                    resultSize = size;
                    resultMode = Integer.MIN_VALUE;
                }
                break;
            case 0:
                if (childDimension >= 0) {
                    resultSize = childDimension;
                    resultMode = 1073741824;
                } else if (childDimension == -1) {
                    resultSize = size;
                } else if (childDimension == -2) {
                    resultSize = size;
                }
                break;
            case 1073741824:
                if (childDimension >= 0) {
                    resultSize = childDimension;
                    resultMode = 1073741824;
                } else if (childDimension == -1) {
                    resultSize = size;
                    resultMode = 1073741824;
                } else if (childDimension == -2) {
                    resultSize = size;
                    resultMode = Integer.MIN_VALUE;
                }
        }
        return MeasureSpec.makeMeasureSpec(resultSize, resultMode);
    }

    public void clearDisappearingChildren() {
        ArrayList<View> disappearingChildren = this.mDisappearingChildren;
        if (disappearingChildren != null) {
            for (View view : disappearingChildren) {
                if (view.mAttachInfo != null) {
                    view.dispatchDetachedFromWindow();
                }
            }
            disappearingChildren.clear();
            this.invalidate();
        }
    }

    private void addDisappearingView(View v) {
        ArrayList<View> disappearingChildren = this.mDisappearingChildren;
        if (disappearingChildren == null) {
            disappearingChildren = this.mDisappearingChildren = new ArrayList();
        }
        disappearingChildren.add(v);
    }

    boolean isViewTransitioning(View view) {
        return this.mTransitioningViews != null && this.mTransitioningViews.contains(view);
    }

    public void startViewTransition(@NonNull View view) {
        if (view.mParent == this) {
            if (this.mTransitioningViews == null) {
                this.mTransitioningViews = new ArrayList();
            }
            this.mTransitioningViews.add(view);
        }
    }

    public void endViewTransition(View view) {
        if (this.mTransitioningViews != null) {
            this.mTransitioningViews.remove(view);
            ArrayList<View> disappearingChildren = this.mDisappearingChildren;
            if (disappearingChildren != null && disappearingChildren.contains(view)) {
                disappearingChildren.remove(view);
                if (this.mVisibilityChangingChildren != null && this.mVisibilityChangingChildren.contains(view)) {
                    this.mVisibilityChangingChildren.remove(view);
                } else {
                    if (view.mAttachInfo != null) {
                        view.dispatchDetachedFromWindow();
                    }
                    if (view.mParent != null) {
                        view.mParent = null;
                    }
                }
                this.invalidate();
            }
        }
    }

    public void suppressLayout(boolean suppress) {
        this.mSuppressLayout = suppress;
        if (!suppress && this.mLayoutCalledWhileSuppressed) {
            this.requestLayout();
            this.mLayoutCalledWhileSuppressed = false;
        }
    }

    public boolean isLayoutSuppressed() {
        return this.mSuppressLayout;
    }

    @Internal
    @Override
    protected void internalSetPadding(int left, int top, int right, int bottom) {
        super.internalSetPadding(left, top, right, bottom);
        if ((this.mPaddingLeft | this.mPaddingTop | this.mPaddingRight | this.mPaddingBottom) != 0) {
            this.mGroupFlags |= 32;
        } else {
            this.mGroupFlags &= -33;
        }
    }

    @Internal
    protected void onChildVisibilityChanged(View child, int oldVisibility, int newVisibility) {
        if (this.mTransition != null) {
            if (newVisibility == 0) {
                this.mTransition.showChild(this, child, oldVisibility);
            } else {
                this.mTransition.hideChild(this, child, newVisibility);
                if (this.mTransitioningViews != null && this.mTransitioningViews.contains(child)) {
                    if (this.mVisibilityChangingChildren == null) {
                        this.mVisibilityChangingChildren = new ArrayList();
                    }
                    this.mVisibilityChangingChildren.add(child);
                    this.addDisappearingView(child);
                }
            }
        }
    }

    @Override
    protected void dispatchVisibilityChanged(@NonNull View changedView, int visibility) {
        super.dispatchVisibilityChanged(changedView, visibility);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            children[i].dispatchVisibilityChanged(changedView, visibility);
        }
    }

    @Override
    public void dispatchWindowVisibilityChanged(int visibility) {
        super.dispatchWindowVisibilityChanged(visibility);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            children[i].dispatchWindowVisibilityChanged(visibility);
        }
    }

    @Override
    boolean dispatchVisibilityAggregated(boolean isVisible) {
        isVisible = super.dispatchVisibilityAggregated(isVisible);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            if (children[i].getVisibility() == 0) {
                children[i].dispatchVisibilityAggregated(isVisible);
            }
        }
        return isVisible;
    }

    public boolean shouldDelayChildPressedState() {
        return true;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        return false;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        if (type == 1) {
            this.mNestedScrollAxesNonTouch = axes;
        } else {
            this.mNestedScrollAxesTouch = axes;
        }
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        this.stopNestedScroll(type);
        if (type == 1) {
            this.mNestedScrollAxesNonTouch = 0;
        } else {
            this.mNestedScrollAxesTouch = 0;
        }
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        this.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, null, type, consumed);
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        this.dispatchNestedPreScroll(dx, dy, consumed, null, type);
    }

    @Override
    public boolean onNestedFling(@NonNull View target, float velocityX, float velocityY, boolean consumed) {
        return this.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {
        return this.dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public int getNestedScrollAxes() {
        return this.mNestedScrollAxesTouch | this.mNestedScrollAxesNonTouch;
    }

    @Internal
    protected void onSetLayoutParams(View child, ViewGroup.LayoutParams layoutParams) {
        this.requestLayout();
    }

    @NonNull
    protected ViewGroup.LayoutParams generateLayoutParams(@NonNull ViewGroup.LayoutParams p) {
        return p;
    }

    @NonNull
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new ViewGroup.LayoutParams(-2, -2);
    }

    protected boolean checkLayoutParams(@Nullable ViewGroup.LayoutParams p) {
        return p != null;
    }

    private static final class HoverTarget {

        private static final int MAX_RECYCLED = 32;

        private static final Object sRecyclerLock = new Object();

        private static ViewGroup.HoverTarget sRecyclerTop;

        private static int sRecyclerUsed;

        public View child;

        public ViewGroup.HoverTarget next;

        @NonNull
        public static ViewGroup.HoverTarget obtain(@NonNull View child) {
            ViewGroup.HoverTarget target;
            synchronized (sRecyclerLock) {
                if (sRecyclerTop == null) {
                    target = new ViewGroup.HoverTarget();
                } else {
                    target = sRecyclerTop;
                    sRecyclerTop = target.next;
                    sRecyclerUsed--;
                    target.next = null;
                }
            }
            target.child = child;
            return target;
        }

        public void recycle() {
            if (this.child == null) {
                throw new IllegalStateException(this + " already recycled");
            } else {
                synchronized (sRecyclerLock) {
                    if (sRecyclerUsed < 32) {
                        sRecyclerUsed++;
                        this.next = sRecyclerTop;
                        sRecyclerTop = this;
                    } else {
                        this.next = null;
                    }
                    this.child = null;
                }
            }
        }
    }

    public static class LayoutParams {

        public static final int MATCH_PARENT = -1;

        public static final int WRAP_CONTENT = -2;

        public int width;

        public int height;

        public LayoutParams(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public LayoutParams(@NonNull ViewGroup.LayoutParams source) {
            this.width = source.width;
            this.height = source.height;
        }

        @Internal
        LayoutParams() {
        }

        public void resolveLayoutDirection(int layoutDirection) {
        }

        @Internal
        public void onDebugDraw(View view, Canvas canvas, Paint paint) {
        }
    }

    private class LayoutTransitionListener implements LayoutTransition.TransitionListener {

        @Override
        public void startTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {
            if (transitionType == 3) {
                ViewGroup.this.startViewTransition(view);
            }
        }

        @Override
        public void endTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {
            if (ViewGroup.this.mLayoutCalledWhileSuppressed && !transition.isChangingLayout()) {
                ViewGroup.this.requestLayout();
                ViewGroup.this.mLayoutCalledWhileSuppressed = false;
            }
            if (transitionType == 3 && ViewGroup.this.mTransitioningViews != null) {
                ViewGroup.this.endViewTransition(view);
            }
        }
    }

    public static class MarginLayoutParams extends ViewGroup.LayoutParams {

        public int leftMargin;

        public int topMargin;

        public int rightMargin;

        public int bottomMargin;

        private int startMargin = Integer.MIN_VALUE;

        private int endMargin = Integer.MIN_VALUE;

        @Internal
        public static final int DEFAULT_MARGIN_RELATIVE = Integer.MIN_VALUE;

        @Internal
        byte mMarginFlags;

        private static final int LAYOUT_DIRECTION_MASK = 3;

        private static final int LEFT_MARGIN_UNDEFINED_MASK = 4;

        private static final int RIGHT_MARGIN_UNDEFINED_MASK = 8;

        private static final int RTL_COMPATIBILITY_MODE_MASK = 16;

        private static final int NEED_RESOLUTION_MASK = 32;

        private static final int DEFAULT_MARGIN_RESOLVED = 0;

        private static final int UNDEFINED_MARGIN = Integer.MIN_VALUE;

        public MarginLayoutParams(int width, int height) {
            super(width, height);
            this.mMarginFlags = (byte) (this.mMarginFlags | 4);
            this.mMarginFlags = (byte) (this.mMarginFlags | 8);
        }

        public MarginLayoutParams(@NonNull ViewGroup.MarginLayoutParams source) {
            super(source);
            this.leftMargin = source.leftMargin;
            this.topMargin = source.topMargin;
            this.rightMargin = source.rightMargin;
            this.bottomMargin = source.bottomMargin;
            this.startMargin = source.startMargin;
            this.endMargin = source.endMargin;
            this.mMarginFlags = source.mMarginFlags;
        }

        public MarginLayoutParams(ViewGroup.LayoutParams source) {
            super(source);
            this.mMarginFlags = (byte) (this.mMarginFlags | 4);
            this.mMarginFlags = (byte) (this.mMarginFlags | 8);
        }

        @Internal
        public final void copyMarginsFrom(@NonNull ViewGroup.MarginLayoutParams source) {
            this.leftMargin = source.leftMargin;
            this.topMargin = source.topMargin;
            this.rightMargin = source.rightMargin;
            this.bottomMargin = source.bottomMargin;
            this.startMargin = source.startMargin;
            this.endMargin = source.endMargin;
            this.mMarginFlags = source.mMarginFlags;
        }

        public void setMargins(int left, int top, int right, int bottom) {
            this.leftMargin = left;
            this.topMargin = top;
            this.rightMargin = right;
            this.bottomMargin = bottom;
            this.mMarginFlags &= -5;
            this.mMarginFlags &= -9;
            if (this.isMarginRelative()) {
                this.mMarginFlags = (byte) (this.mMarginFlags | 32);
            } else {
                this.mMarginFlags &= -33;
            }
        }

        public void setMarginsRelative(int start, int top, int end, int bottom) {
            this.startMargin = start;
            this.topMargin = top;
            this.endMargin = end;
            this.bottomMargin = bottom;
            this.mMarginFlags = (byte) (this.mMarginFlags | 32);
        }

        public void setMarginStart(int start) {
            this.startMargin = start;
            this.mMarginFlags = (byte) (this.mMarginFlags | 32);
        }

        public int getMarginStart() {
            if (this.startMargin != Integer.MIN_VALUE) {
                return this.startMargin;
            } else {
                if ((this.mMarginFlags & 32) == 32) {
                    this.doResolveMargins();
                }
                return this.isLayoutRtl() ? this.rightMargin : this.leftMargin;
            }
        }

        public void setMarginEnd(int end) {
            this.endMargin = end;
            this.mMarginFlags = (byte) (this.mMarginFlags | 32);
        }

        public int getMarginEnd() {
            if (this.endMargin != Integer.MIN_VALUE) {
                return this.endMargin;
            } else {
                if ((this.mMarginFlags & 32) == 32) {
                    this.doResolveMargins();
                }
                return this.isLayoutRtl() ? this.leftMargin : this.rightMargin;
            }
        }

        public boolean isMarginRelative() {
            return this.startMargin != Integer.MIN_VALUE || this.endMargin != Integer.MIN_VALUE;
        }

        public void setLayoutDirection(int layoutDirection) {
            if (layoutDirection == 0 || layoutDirection == 1) {
                if (layoutDirection != (this.mMarginFlags & 3)) {
                    this.mMarginFlags &= -4;
                    this.mMarginFlags = (byte) (this.mMarginFlags | layoutDirection & 3);
                    if (this.isMarginRelative()) {
                        this.mMarginFlags = (byte) (this.mMarginFlags | 32);
                    } else {
                        this.mMarginFlags &= -33;
                    }
                }
            }
        }

        public int getLayoutDirection() {
            return this.mMarginFlags & 3;
        }

        @Override
        public void resolveLayoutDirection(int layoutDirection) {
            this.setLayoutDirection(layoutDirection);
            if (this.isMarginRelative() && (this.mMarginFlags & 32) == 32) {
                this.doResolveMargins();
            }
        }

        private void doResolveMargins() {
            if ((this.mMarginFlags & 16) == 16) {
                if ((this.mMarginFlags & 4) == 4 && this.startMargin > Integer.MIN_VALUE) {
                    this.leftMargin = this.startMargin;
                }
                if ((this.mMarginFlags & 8) == 8 && this.endMargin > Integer.MIN_VALUE) {
                    this.rightMargin = this.endMargin;
                }
            } else if (this.isLayoutRtl()) {
                this.leftMargin = this.endMargin > Integer.MIN_VALUE ? this.endMargin : 0;
                this.rightMargin = this.startMargin > Integer.MIN_VALUE ? this.startMargin : 0;
            } else {
                this.leftMargin = this.startMargin > Integer.MIN_VALUE ? this.startMargin : 0;
                this.rightMargin = this.endMargin > Integer.MIN_VALUE ? this.endMargin : 0;
            }
            this.mMarginFlags &= -33;
        }

        @Internal
        public boolean isLayoutRtl() {
            return (this.mMarginFlags & 3) == 1;
        }

        @Internal
        @Override
        public void onDebugDraw(View view, Canvas canvas, Paint paint) {
            FloatBuffer positions = ((FloatBuffer) ViewGroup.sDebugDrawBuffer.get()).clear();
            int x2 = view.getLeft();
            int y2 = view.getTop();
            int x3 = view.getRight();
            int y3 = view.getBottom();
            int x1 = x2 - this.leftMargin;
            int y1 = y2 - this.topMargin;
            int x4 = x3 + this.rightMargin;
            int y4 = y3 + this.bottomMargin;
            ViewGroup.fillRect(x1, y1, x4, y2, positions);
            ViewGroup.fillRect(x1, y2, x2, y3, positions);
            ViewGroup.fillRect(x3, y2, x4, y3, positions);
            ViewGroup.fillRect(x1, y3, x4, y4, positions);
            canvas.drawMesh(Canvas.VertexMode.TRIANGLES, positions.flip(), null, null, null, null, paint);
        }
    }

    private static final class TouchTarget {

        private static final Pools.Pool<ViewGroup.TouchTarget> sPool = Pools.newSynchronizedPool(12);

        public View child;

        @NonNull
        public static ViewGroup.TouchTarget obtain(@NonNull View child) {
            ViewGroup.TouchTarget target = sPool.acquire();
            if (target == null) {
                target = new ViewGroup.TouchTarget();
            }
            target.child = child;
            return target;
        }

        public void recycle() {
            if (this.child == null) {
                throw new IllegalStateException(this + " already recycled");
            } else {
                sPool.release(this);
                this.child = null;
            }
        }
    }
}