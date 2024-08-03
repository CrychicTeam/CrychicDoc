package icyllis.modernui.view;

import icyllis.modernui.animation.LayoutTransition;
import icyllis.modernui.annotation.MainThread;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.annotation.UiThread;
import icyllis.modernui.core.Choreographer;
import icyllis.modernui.core.Core;
import icyllis.modernui.core.Handler;
import icyllis.modernui.core.Looper;
import icyllis.modernui.core.Message;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Point;
import icyllis.modernui.graphics.Rect;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BooleanSupplier;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public abstract class ViewRoot implements ViewParent, AttachInfo.Callbacks {

    protected static final Marker MARKER = MarkerManager.getMarker("ViewRoot");

    private final AttachInfo mAttachInfo;

    private static final int MSG_INVALIDATE = 1;

    protected static final int MSG_PROCESS_INPUT_EVENTS = 19;

    private static final int MSG_INVALIDATE_WORLD = 22;

    private final ConcurrentLinkedQueue<InputEvent> mInputEvents = new ConcurrentLinkedQueue();

    protected boolean mTraversalScheduled;

    int mTraversalBarrier;

    private boolean mWillDrawSoon;

    private boolean mIsDrawing;

    private boolean mLayoutRequested;

    private boolean mInvalidated;

    private boolean mKeepInvalidated;

    private boolean mInLayout = false;

    ArrayList<View> mLayoutRequesters = new ArrayList();

    boolean mHandlingLayoutInLayoutRequest = false;

    private boolean hasDragOperation;

    boolean mProcessInputEventsScheduled;

    protected final Object mRenderLock = new Object();

    private int mPointerIconType = 0;

    protected View mView;

    private int mWidth;

    private int mHeight;

    public final Handler mHandler;

    public final Choreographer mChoreographer;

    private ArrayList<LayoutTransition> mPendingTransitions;

    final Rect mTempRect = new Rect();

    final Runnable mTraversalRunnable = this::doTraversal;

    final ViewRoot.InvalidateOnAnimationRunnable mInvalidateOnAnimationRunnable = new ViewRoot.InvalidateOnAnimationRunnable();

    protected ViewRoot() {
        this.mHandler = new Handler(Looper.myLooper(), this::handleMessage);
        this.mChoreographer = Choreographer.getInstance();
        this.mAttachInfo = new AttachInfo(this, this.mHandler, this);
        try {
            Class.forName("icyllis.modernui.text.BoringLayout");
            Class.forName("icyllis.modernui.graphics.text.CharArrayIterator");
            Class.forName("icyllis.modernui.text.CharSequenceIterator");
            Class.forName("icyllis.modernui.text.Directions");
            Class.forName("icyllis.modernui.text.DynamicLayout");
            Class.forName("icyllis.modernui.graphics.font.GlyphManager");
            Class.forName("icyllis.modernui.graphics.text.GraphemeBreak");
            Class.forName("icyllis.modernui.text.Layout");
            Class.forName("icyllis.modernui.graphics.text.LayoutCache");
            Class.forName("icyllis.modernui.graphics.text.LayoutPiece");
            Class.forName("icyllis.modernui.graphics.text.LineBreaker");
            Class.forName("icyllis.modernui.text.MeasuredParagraph");
            Class.forName("icyllis.modernui.graphics.text.MeasuredText");
            Class.forName("icyllis.modernui.text.PrecomputedText");
            Class.forName("icyllis.modernui.text.Selection");
            Class.forName("icyllis.modernui.text.SpannableString");
            Class.forName("icyllis.modernui.text.SpannableStringBuilder");
            Class.forName("icyllis.modernui.text.SpannableStringInternal");
            Class.forName("icyllis.modernui.text.StaticLayout");
            Class.forName("icyllis.modernui.text.TabStops");
            Class.forName("icyllis.modernui.text.TextDirectionHeuristics");
            Class.forName("icyllis.modernui.text.TextLine");
            Class.forName("icyllis.modernui.text.TextPaint");
            Class.forName("icyllis.modernui.text.TextUtils");
            Class.forName("icyllis.modernui.text.Typeface");
            Class.forName("icyllis.modernui.widget.Editor");
            Class.forName("icyllis.modernui.widget.LinearLayout");
            Class.forName("icyllis.modernui.widget.ScrollView");
            Class.forName("icyllis.modernui.widget.TextView");
        } catch (ClassNotFoundException var2) {
            throw new RuntimeException(var2);
        }
    }

    protected boolean handleMessage(@NonNull Message msg) {
        switch(msg.what) {
            case 1:
                ((View) msg.obj).invalidate();
                break;
            case 19:
                this.mProcessInputEventsScheduled = false;
                this.doProcessInputEvents();
                break;
            case 22:
                if (this.mView != null) {
                    this.invalidateWorld(this.mView);
                }
        }
        return true;
    }

    public void setView(@NonNull View view) {
        synchronized (this) {
            if (this.mView == null) {
                this.mView = view;
                this.mAttachInfo.mRootView = view;
                this.mAttachInfo.mWindowVisibility = 0;
                view.assignParent(this);
                view.dispatchAttachedToWindow(this.mAttachInfo, 0);
                view.dispatchWindowVisibilityChanged(0);
            }
        }
    }

    public void setFrame(int width, int height) {
        if (width != this.mWidth || height != this.mHeight) {
            this.mWidth = width;
            this.mHeight = height;
            this.requestLayout();
        }
    }

    public View getView() {
        return this.mView;
    }

    boolean startDragAndDrop(@NonNull View view, @Nullable Object data, @Nullable View.DragShadow shadow, int flags) {
        this.hasDragOperation = true;
        return true;
    }

    @Override
    public boolean requestChildRectangleOnScreen(View child, Rect rectangle, boolean immediate) {
        return false;
    }

    private void scheduleProcessInputEvents() {
        if (!this.mProcessInputEventsScheduled) {
            this.mProcessInputEventsScheduled = true;
            Message msg = this.mHandler.obtainMessage(19);
            msg.setAsynchronous(true);
            this.mHandler.sendMessage(msg);
        }
    }

    @UiThread
    protected void scheduleTraversals() {
        if (!this.mTraversalScheduled) {
            this.mTraversalScheduled = true;
            this.mTraversalBarrier = this.mHandler.getQueue().postSyncBarrier();
            this.mChoreographer.postCallback(2, this.mTraversalRunnable, null);
        }
    }

    @UiThread
    protected void unscheduleTraversals() {
        if (this.mTraversalScheduled) {
            this.mTraversalScheduled = false;
            this.mHandler.getQueue().removeSyncBarrier(this.mTraversalBarrier);
            this.mChoreographer.removeCallbacks(2, this.mTraversalRunnable, null);
        }
    }

    @UiThread
    protected void doTraversal() {
        if (this.mTraversalScheduled) {
            this.mTraversalScheduled = false;
            this.mHandler.getQueue().removeSyncBarrier(this.mTraversalBarrier);
            this.performTraversal();
        }
    }

    private void performTraversal() {
        View host = this.mView;
        if (host != null) {
            this.mWillDrawSoon = true;
            int width = this.mWidth;
            int height = this.mHeight;
            if (this.mLayoutRequested || width != host.getMeasuredWidth() || height != host.getMeasuredHeight()) {
                int widthSpec = MeasureSpec.makeMeasureSpec(width, 1073741824);
                int heightSpec = MeasureSpec.makeMeasureSpec(height, 1073741824);
                host.measure(widthSpec, heightSpec);
                this.mInLayout = true;
                host.layout(0, 0, host.getMeasuredWidth(), host.getMeasuredHeight());
                this.mInLayout = false;
                int numViewsRequestingLayout = this.mLayoutRequesters.size();
                if (numViewsRequestingLayout > 0) {
                    ArrayList<View> validLayoutRequesters = this.getValidLayoutRequesters(this.mLayoutRequesters, false);
                    if (validLayoutRequesters != null) {
                        this.mHandlingLayoutInLayoutRequest = true;
                        for (View view : validLayoutRequesters) {
                            view.requestLayout();
                        }
                        host.measure(widthSpec, heightSpec);
                        this.mInLayout = true;
                        host.layout(0, 0, host.getMeasuredWidth(), host.getMeasuredHeight());
                        this.mHandlingLayoutInLayoutRequest = false;
                        validLayoutRequesters = this.getValidLayoutRequesters(this.mLayoutRequesters, true);
                        if (validLayoutRequesters != null) {
                            this.mHandler.post(() -> {
                                for (View viewx : validLayoutRequesters) {
                                    viewx.requestLayout();
                                }
                            });
                        }
                    }
                }
                this.mInLayout = false;
                this.mLayoutRequested = false;
                this.mAttachInfo.mTreeObserver.dispatchOnGlobalLayout();
            }
            this.mWillDrawSoon = false;
            boolean cancelDraw = this.mAttachInfo.mTreeObserver.dispatchOnPreDraw();
            synchronized (this.mRenderLock) {
                if (!cancelDraw) {
                    if (this.mPendingTransitions != null && this.mPendingTransitions.size() > 0) {
                        for (LayoutTransition pendingTransition : this.mPendingTransitions) {
                            pendingTransition.startChangingAnimations();
                        }
                        this.mPendingTransitions.clear();
                    }
                    if (this.mAttachInfo.mViewScrollChanged) {
                        this.mAttachInfo.mViewScrollChanged = false;
                        this.mAttachInfo.mTreeObserver.dispatchOnScrollChanged();
                    }
                    if (this.mInvalidated) {
                        this.mIsDrawing = true;
                        Canvas canvas = this.beginDrawLocked(width, height);
                        if (canvas != null) {
                            host.draw(canvas);
                            this.endDrawLocked(canvas);
                        }
                        this.mIsDrawing = false;
                        if (this.mKeepInvalidated) {
                            this.mKeepInvalidated = false;
                        } else {
                            this.mInvalidated = false;
                        }
                    }
                } else {
                    this.scheduleTraversals();
                }
            }
        }
    }

    private ArrayList<View> getValidLayoutRequesters(@NonNull ArrayList<View> layoutRequesters, boolean secondLayoutRequests) {
        ArrayList<View> validLayoutRequesters = null;
        for (View view : layoutRequesters) {
            if (view != null && view.mAttachInfo != null && view.mParent != null && (secondLayoutRequests || (view.mPrivateFlags & 4096) == 4096)) {
                boolean gone = false;
                parent = view;
                while (parent != null) {
                    if ((parent.mViewFlags & 12) == 8) {
                        gone = true;
                        break;
                    }
                    if (!(parent.mParent instanceof View parent)) {
                        parent = null;
                    }
                }
                if (!gone) {
                    if (validLayoutRequesters == null) {
                        validLayoutRequesters = new ArrayList();
                    }
                    validLayoutRequesters.add(view);
                }
            }
        }
        if (!secondLayoutRequests) {
            for (View viewx : layoutRequesters) {
                while (viewx != null && (viewx.mPrivateFlags & 4096) != 0) {
                    viewx.mPrivateFlags &= -4097;
                    if (viewx.mParent instanceof View) {
                        viewx = (View) viewx.mParent;
                    } else {
                        viewx = null;
                    }
                }
            }
        }
        layoutRequesters.clear();
        return validLayoutRequesters;
    }

    boolean isInLayout() {
        return this.mInLayout;
    }

    boolean requestLayoutDuringLayout(@NonNull View view) {
        if (view.mParent != null && view.mAttachInfo != null) {
            if (!this.mLayoutRequesters.contains(view)) {
                this.mLayoutRequesters.add(view);
            }
            return !this.mHandlingLayoutInLayoutRequest;
        } else {
            return true;
        }
    }

    @Nullable
    protected abstract Canvas beginDrawLocked(int var1, int var2);

    protected abstract void endDrawLocked(@NonNull Canvas var1);

    @MainThread
    public void enqueueInputEvent(@NonNull InputEvent event) {
        this.mInputEvents.offer(event);
        this.scheduleProcessInputEvents();
    }

    private void doProcessInputEvents() {
        InputEvent e;
        if (this.mView != null) {
            while ((e = (InputEvent) this.mInputEvents.poll()) != null) {
                try {
                    if (e instanceof KeyEvent event) {
                        if (!this.mView.dispatchKeyEvent(event)) {
                            int groupNavigationDirection = 0;
                            if (event.getAction() == 0 && event.getKeyCode() == 258) {
                                if (event.hasModifiers(5)) {
                                    groupNavigationDirection = 1;
                                } else if (event.hasModifiers(4)) {
                                    groupNavigationDirection = 2;
                                }
                            }
                            if ((event.getAction() != 0 || event.hasNoModifiers() || event.getRepeatCount() != 0 || KeyEvent.isModifierKey(event.getKeyCode()) || groupNavigationDirection != 0 || !this.mView.dispatchKeyShortcutEvent(event)) && (event.getAction() != 0 || (groupNavigationDirection != 0 ? !this.performKeyboardGroupNavigation(groupNavigationDirection) : !this.performFocusNavigation(event)))) {
                                this.onKeyEvent(event);
                            }
                        }
                        continue;
                    }
                    MotionEvent ev = (MotionEvent) e;
                    if (!this.dispatchTouchEvent(ev)) {
                        boolean handled = this.mView.dispatchPointerEvent(ev);
                        if (ev.getAction() == 9 || ev.getAction() == 10) {
                            this.mPointerIconType = 0;
                        }
                        if (ev.getAction() != 10 && !this.updatePointerIcon(ev) && ev.getAction() == 7) {
                            this.mPointerIconType = 0;
                        }
                        this.maybeUpdateTooltip(ev);
                        if (!handled) {
                            this.onTouchEvent(ev);
                        }
                        continue;
                    }
                } finally {
                    e.recycle();
                }
                return;
            }
        } else {
            this.mInputEvents.clear();
        }
    }

    private boolean performFocusNavigation(@NonNull KeyEvent event) {
        int direction = 0;
        switch(event.getKeyCode()) {
            case 258:
                if (event.hasNoModifiers()) {
                    direction = 2;
                } else if (event.hasModifiers(1)) {
                    direction = 1;
                }
            case 259:
            case 260:
            case 261:
            default:
                break;
            case 262:
                if (event.hasNoModifiers()) {
                    direction = 66;
                }
                break;
            case 263:
                if (event.hasNoModifiers()) {
                    direction = 17;
                }
                break;
            case 264:
                if (event.hasNoModifiers()) {
                    direction = 130;
                }
                break;
            case 265:
                if (event.hasNoModifiers()) {
                    direction = 33;
                }
        }
        if (direction != 0) {
            View focused = this.mView.findFocus();
            if (focused != null) {
                View v = focused.focusSearch(direction);
                if (v != null && v != focused) {
                    focused.getFocusedRect(this.mTempRect);
                    if (this.mView instanceof ViewGroup) {
                        ((ViewGroup) this.mView).offsetDescendantRectToMyCoords(focused, this.mTempRect);
                        ((ViewGroup) this.mView).offsetRectIntoDescendantCoords(v, this.mTempRect);
                    }
                    if (v.requestFocus(direction, this.mTempRect)) {
                        boolean isFastScrolling = event.getRepeatCount() > 0;
                        return true;
                    }
                }
            } else if (this.mView.restoreDefaultFocus()) {
                return true;
            }
        }
        return false;
    }

    private boolean performKeyboardGroupNavigation(int direction) {
        View focused = this.mView.findFocus();
        if (focused == null && this.mView.restoreDefaultFocus()) {
            return true;
        } else {
            View cluster = focused == null ? this.keyboardNavigationClusterSearch(null, direction) : focused.keyboardNavigationClusterSearch(null, direction);
            int realDirection = direction;
            if (direction == 2 || direction == 1) {
                realDirection = 130;
            }
            if (cluster != null && cluster.isRootNamespace()) {
                if (cluster.restoreFocusNotInCluster()) {
                    return true;
                }
                cluster = this.keyboardNavigationClusterSearch(null, direction);
            }
            return cluster != null && cluster.restoreFocusInCluster(realDirection);
        }
    }

    protected boolean dispatchTouchEvent(MotionEvent event) {
        return false;
    }

    protected void onTouchEvent(MotionEvent event) {
    }

    protected void onKeyEvent(KeyEvent event) {
    }

    public void loadSystemProperties(BooleanSupplier debugLayoutSupplier) {
        this.mHandler.post(() -> {
            boolean layout = debugLayoutSupplier.getAsBoolean();
            if (layout != this.mAttachInfo.mDebugLayout) {
                this.mAttachInfo.mDebugLayout = layout;
                if (!this.mHandler.hasMessages(22)) {
                    this.mHandler.sendEmptyMessageDelayed(22, 200L);
                }
            }
        });
    }

    void performDragEvent(DragEvent event) {
        if (this.hasDragOperation) {
        }
    }

    void invalidate() {
        Core.checkUiThread();
        this.mInvalidated = true;
        if (!this.mWillDrawSoon) {
            if (this.mIsDrawing) {
                this.mKeepInvalidated = true;
            }
            this.scheduleTraversals();
        }
    }

    void invalidateWorld(@NonNull View view) {
        view.invalidate();
        if (view instanceof ViewGroup parent) {
            for (int i = 0; i < parent.getChildCount(); i++) {
                this.invalidateWorld(parent.getChildAt(i));
            }
        }
    }

    public void dispatchInvalidateDelayed(View view, long delayMilliseconds) {
        Message msg = this.mHandler.obtainMessage(1, view);
        this.mHandler.sendMessageDelayed(msg, delayMilliseconds);
    }

    public void dispatchInvalidateOnAnimation(View view) {
        this.mInvalidateOnAnimationRunnable.addView(view);
    }

    public void cancelInvalidate(View view) {
        this.mHandler.removeMessages(1, view);
        this.mInvalidateOnAnimationRunnable.removeView(view);
    }

    private boolean updatePointerIcon(@NonNull MotionEvent e) {
        if (this.mView == null) {
            return false;
        } else {
            PointerIcon pointerIcon = this.mView.onResolvePointerIcon(e);
            int pointerType = pointerIcon != null ? pointerIcon.getType() : 0;
            if (this.mPointerIconType != pointerType) {
                this.mPointerIconType = pointerType;
                this.applyPointerIcon(pointerType);
            }
            return true;
        }
    }

    protected void applyPointerIcon(int pointerType) {
    }

    private void maybeUpdateTooltip(MotionEvent event) {
        if (event.getPointerCount() == 1) {
            int action = event.getActionMasked();
            if (action == 9 || action == 7 || action == 10) {
                if (this.mView != null) {
                    this.mView.dispatchTooltipHoverEvent(event);
                }
            }
        }
    }

    public static boolean isViewDescendantOf(View child, View parent) {
        if (child == parent) {
            return true;
        } else {
            ViewParent theParent = child.getParent();
            return theParent instanceof ViewGroup && isViewDescendantOf((View) theParent, parent);
        }
    }

    @Nullable
    @Override
    public ViewParent getParent() {
        return null;
    }

    @Override
    public boolean getChildVisibleRect(View child, Rect r, @Nullable Point offset) {
        if (child != this.mView) {
            throw new RuntimeException();
        } else {
            return r.intersect(0, 0, this.mWidth, this.mHeight);
        }
    }

    @Override
    public void requestLayout() {
        if (!this.mHandlingLayoutInLayoutRequest) {
            Core.checkUiThread();
            this.mLayoutRequested = true;
            this.scheduleTraversals();
        }
    }

    @Override
    public boolean isLayoutRequested() {
        return this.mLayoutRequested;
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        Core.checkUiThread();
        this.scheduleTraversals();
    }

    @Override
    public void clearChildFocus(View child) {
        Core.checkUiThread();
        this.scheduleTraversals();
    }

    @Override
    public View focusSearch(View focused, int direction) {
        Core.checkUiThread();
        return !(this.mView instanceof ViewGroup) ? null : FocusFinder.getInstance().findNextFocus((ViewGroup) this.mView, focused, direction);
    }

    @Override
    public View keyboardNavigationClusterSearch(View currentCluster, int direction) {
        Core.checkUiThread();
        return FocusFinder.getInstance().findNextKeyboardNavigationCluster(this.mView, currentCluster, direction);
    }

    @Override
    public void childHasTransientStateChanged(View child, boolean hasTransientState) {
    }

    @Override
    public void bringChildToFront(View child) {
    }

    @Override
    public void focusableViewAvailable(View v) {
        Core.checkUiThread();
        if (this.mView != null && !this.mView.hasFocus()) {
            View focused = this.mView.findFocus();
            if (focused instanceof ViewGroup group && group.getDescendantFocusability() == 262144 && isViewDescendantOf(v, focused)) {
                v.requestFocus();
            }
        }
    }

    @Override
    public boolean canResolveLayoutDirection() {
        return true;
    }

    @Override
    public boolean isLayoutDirectionResolved() {
        return true;
    }

    @Override
    public int getLayoutDirection() {
        return 0;
    }

    @Override
    public boolean canResolveTextDirection() {
        return true;
    }

    @Override
    public boolean isTextDirectionResolved() {
        return true;
    }

    @Override
    public int getTextDirection() {
        return 1;
    }

    @Override
    public boolean canResolveTextAlignment() {
        return true;
    }

    @Override
    public boolean isTextAlignmentResolved() {
        return true;
    }

    @Override
    public int getTextAlignment() {
        return 1;
    }

    @Override
    public boolean showContextMenuForChild(View originalView, float x, float y) {
        return false;
    }

    @Override
    public ActionMode startActionModeForChild(View originalView, ActionMode.Callback callback, int type) {
        return null;
    }

    @Override
    public void createContextMenu(ContextMenu menu) {
    }

    @Override
    public void childDrawableStateChanged(View child) {
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        return false;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
    }

    @Override
    public boolean onNestedFling(@NonNull View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        return 0;
    }

    public void requestTransitionStart(LayoutTransition transition) {
        if (this.mPendingTransitions == null || !this.mPendingTransitions.contains(transition)) {
            if (this.mPendingTransitions == null) {
                this.mPendingTransitions = new ArrayList();
            }
            this.mPendingTransitions.add(transition);
        }
    }

    final class InvalidateOnAnimationRunnable implements Runnable {

        private boolean mPosted;

        private final ArrayList<View> mViews = new ArrayList();

        private View[] mTempViews;

        public void addView(View view) {
            synchronized (this) {
                this.mViews.add(view);
                this.postIfNeededLocked();
            }
        }

        public void removeView(View view) {
            synchronized (this) {
                this.mViews.remove(view);
                if (this.mPosted && this.mViews.isEmpty()) {
                    ViewRoot.this.mChoreographer.removeCallbacks(1, this, null);
                    this.mPosted = false;
                }
            }
        }

        public void run() {
            int viewCount;
            synchronized (this) {
                this.mPosted = false;
                viewCount = this.mViews.size();
                if (viewCount != 0) {
                    this.mTempViews = (View[]) this.mViews.toArray(this.mTempViews != null ? this.mTempViews : new View[viewCount]);
                    this.mViews.clear();
                }
            }
            for (int i = 0; i < viewCount; i++) {
                this.mTempViews[i].invalidate();
                this.mTempViews[i] = null;
            }
        }

        private void postIfNeededLocked() {
            if (!this.mPosted) {
                ViewRoot.this.mChoreographer.postCallback(1, this, null);
                this.mPosted = true;
            }
        }
    }
}