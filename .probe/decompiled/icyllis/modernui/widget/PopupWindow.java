package icyllis.modernui.widget;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.app.Activity;
import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.graphics.drawable.StateListDrawable;
import icyllis.modernui.transition.Transition;
import icyllis.modernui.transition.TransitionListener;
import icyllis.modernui.transition.TransitionManager;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.KeyEvent;
import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.view.ViewTreeObserver;
import icyllis.modernui.view.WindowManager;
import java.lang.ref.WeakReference;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PopupWindow {

    private static final int DEFAULT_ANCHORED_GRAVITY = 8388659;

    private final int[] mTmpDrawingLocation = new int[2];

    private final int[] mTmpScreenLocation = new int[2];

    private final int[] mTmpAppLocation = new int[2];

    private final Rect mTempRect = new Rect();

    private Context mContext;

    private WindowManager mWindowManager;

    boolean mIsShowing;

    boolean mIsTransitioningToDismiss;

    boolean mIsDropdown;

    private PopupWindow.DecorView mDecorView;

    private View mBackgroundView;

    private View mContentView;

    private boolean mFocusable;

    private boolean mTouchable = true;

    private boolean mOutsideTouchable = false;

    private boolean mClippingEnabled = true;

    private boolean mClipToScreen;

    private boolean mNotTouchModal;

    private View.OnTouchListener mTouchInterceptor;

    private int mWidth = -2;

    private int mHeight = -2;

    private float mElevation;

    private Drawable mBackground;

    private Drawable mAboveAnchorBackgroundDrawable;

    private Drawable mBelowAnchorBackgroundDrawable;

    private Transition mEnterTransition;

    private Transition mExitTransition;

    private Rect mEpicenterBounds;

    private boolean mAboveAnchor;

    private int mWindowLayoutType = 1000;

    private PopupWindow.OnDismissListener mOnDismissListener;

    private int mGravity = 0;

    private static final int[] ABOVE_ANCHOR_STATE_SET = new int[] { 16842922 };

    private final View.OnAttachStateChangeListener mOnAnchorDetachedListener = new View.OnAttachStateChangeListener() {

        @Override
        public void onViewAttachedToWindow(View v) {
            PopupWindow.this.alignToAnchor();
        }

        @Override
        public void onViewDetachedFromWindow(View v) {
        }
    };

    private final View.OnAttachStateChangeListener mOnAnchorRootDetachedListener = new View.OnAttachStateChangeListener() {

        @Override
        public void onViewAttachedToWindow(View v) {
        }

        @Override
        public void onViewDetachedFromWindow(View v) {
            PopupWindow.this.mIsAnchorRootAttached = false;
        }
    };

    private WeakReference<View> mAnchor;

    private WeakReference<View> mAnchorRoot;

    private boolean mIsAnchorRootAttached;

    private final ViewTreeObserver.OnScrollChangedListener mOnScrollChangedListener = this::alignToAnchor;

    private final View.OnLayoutChangeListener mOnLayoutChangeListener = (v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> this.alignToAnchor();

    private int mAnchorXOff;

    private int mAnchorYOff;

    private int mAnchoredGravity;

    private boolean mOverlapAnchor;

    private boolean mPopupViewInitialLayoutDirectionInherited;

    public PopupWindow() {
        this(null, 0, 0);
    }

    public PopupWindow(View contentView) {
        this(contentView, 0, 0);
    }

    public PopupWindow(int width, int height) {
        this(null, width, height);
    }

    public PopupWindow(View contentView, int width, int height) {
        this(contentView, width, height, false);
    }

    public PopupWindow(View contentView, int width, int height, boolean focusable) {
        if (contentView != null) {
            this.mContext = contentView.getContext();
            this.mWindowManager = ((Activity) this.mContext).getWindowManager();
        }
        this.setContentView(contentView);
        this.setWidth(width);
        this.setHeight(height);
        this.setFocusable(focusable);
    }

    public void setEnterTransition(@Nullable Transition enterTransition) {
        this.mEnterTransition = enterTransition;
    }

    @Nullable
    public Transition getEnterTransition() {
        return this.mEnterTransition;
    }

    public void setExitTransition(@Nullable Transition exitTransition) {
        this.mExitTransition = exitTransition;
    }

    @Nullable
    public Transition getExitTransition() {
        return this.mExitTransition;
    }

    @Nullable
    public Rect getEpicenterBounds() {
        return this.mEpicenterBounds != null ? this.mEpicenterBounds.copy() : null;
    }

    public void setEpicenterBounds(@Nullable Rect bounds) {
        this.mEpicenterBounds = bounds != null ? bounds.copy() : null;
    }

    @Nullable
    public Drawable getBackground() {
        return this.mBackground;
    }

    public void setBackgroundDrawable(@Nullable Drawable background) {
        this.mBackground = background;
        if (this.mBackground instanceof StateListDrawable stateList) {
            int aboveAnchorStateIndex = stateList.findStateDrawableIndex(ABOVE_ANCHOR_STATE_SET);
            int count = stateList.getStateCount();
            int belowAnchorStateIndex = -1;
            for (int i = 0; i < count; i++) {
                if (i != aboveAnchorStateIndex) {
                    belowAnchorStateIndex = i;
                    break;
                }
            }
            if (aboveAnchorStateIndex != -1 && belowAnchorStateIndex != -1) {
                this.mAboveAnchorBackgroundDrawable = stateList.getStateDrawable(aboveAnchorStateIndex);
                this.mBelowAnchorBackgroundDrawable = stateList.getStateDrawable(belowAnchorStateIndex);
            } else {
                this.mBelowAnchorBackgroundDrawable = null;
                this.mAboveAnchorBackgroundDrawable = null;
            }
        }
    }

    public float getElevation() {
        return this.mElevation;
    }

    public void setElevation(float elevation) {
        this.mElevation = elevation;
    }

    public View getContentView() {
        return this.mContentView;
    }

    public void setContentView(View contentView) {
        if (!this.mIsShowing) {
            this.mContentView = contentView;
            if (this.mContext == null && this.mContentView != null) {
                this.mContext = this.mContentView.getContext();
            }
            if (this.mWindowManager == null && this.mContentView != null) {
                this.mWindowManager = ((Activity) this.mContext).getWindowManager();
            }
        }
    }

    public void setTouchInterceptor(@Nullable View.OnTouchListener l) {
        this.mTouchInterceptor = l;
    }

    public boolean isFocusable() {
        return this.mFocusable;
    }

    public void setFocusable(boolean focusable) {
        this.mFocusable = focusable;
    }

    public boolean isTouchable() {
        return this.mTouchable;
    }

    public void setTouchable(boolean touchable) {
        this.mTouchable = touchable;
    }

    public boolean isOutsideTouchable() {
        return this.mOutsideTouchable;
    }

    public void setOutsideTouchable(boolean touchable) {
        this.mOutsideTouchable = touchable;
    }

    public boolean isClippingEnabled() {
        return this.mClippingEnabled;
    }

    public void setClippingEnabled(boolean enabled) {
        this.mClippingEnabled = enabled;
    }

    public boolean isClippedToScreen() {
        return this.mClipToScreen;
    }

    public void setIsClippedToScreen(boolean enabled) {
        this.mClipToScreen = enabled;
    }

    public void setWindowLayoutType(int layoutType) {
        this.mWindowLayoutType = layoutType;
    }

    public int getWindowLayoutType() {
        return this.mWindowLayoutType;
    }

    public boolean isTouchModal() {
        return !this.mNotTouchModal;
    }

    public void setTouchModal(boolean touchModal) {
        this.mNotTouchModal = !touchModal;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public void setHeight(int height) {
        this.mHeight = height;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public void setWidth(int width) {
        this.mWidth = width;
    }

    public void setOverlapAnchor(boolean overlapAnchor) {
        this.mOverlapAnchor = overlapAnchor;
    }

    public boolean getOverlapAnchor() {
        return this.mOverlapAnchor;
    }

    public boolean isShowing() {
        return this.mIsShowing;
    }

    public void showAtLocation(@Nonnull View parent, int gravity, int x, int y) {
        if (!this.mIsShowing && this.mContentView != null) {
            TransitionManager.endTransitions(this.mDecorView);
            this.detachFromAnchor();
            this.mIsShowing = true;
            this.mIsDropdown = false;
            this.mGravity = gravity;
            WindowManager.LayoutParams p = this.createPopupLayoutParams();
            p.gravity = gravity;
            p.x = x;
            p.y = y;
            this.preparePopup();
            this.invokePopup(p);
        }
    }

    public final void showAsDropDown(@Nonnull View anchor) {
        this.showAsDropDown(anchor, 0, 0, 8388659);
    }

    public final void showAsDropDown(@Nonnull View anchor, int xOff, int yOff) {
        this.showAsDropDown(anchor, xOff, yOff, 8388659);
    }

    public void showAsDropDown(@Nonnull View anchor, int xOff, int yOff, int gravity) {
        if (!this.mIsShowing && this.mContentView != null) {
            TransitionManager.endTransitions(this.mDecorView);
            this.attachToAnchor(anchor, xOff, yOff, gravity);
            this.mIsShowing = true;
            this.mIsDropdown = true;
            WindowManager.LayoutParams p = this.createPopupLayoutParams();
            this.preparePopup();
            boolean aboveAnchor = this.findDropDownPosition(anchor, p, xOff, yOff, p.width, p.height, gravity, true);
            this.updateAboveAnchor(aboveAnchor);
            this.invokePopup(p);
        }
    }

    final void updateAboveAnchor(boolean aboveAnchor) {
        if (aboveAnchor != this.mAboveAnchor) {
            this.mAboveAnchor = aboveAnchor;
            if (this.mBackground != null && this.mBackgroundView != null) {
                if (this.mAboveAnchorBackgroundDrawable != null) {
                    if (this.mAboveAnchor) {
                        this.mBackgroundView.setBackground(this.mAboveAnchorBackgroundDrawable);
                    } else {
                        this.mBackgroundView.setBackground(this.mBelowAnchorBackgroundDrawable);
                    }
                } else {
                    this.mBackgroundView.refreshDrawableState();
                }
            }
        }
    }

    public boolean isAboveAnchor() {
        return this.mAboveAnchor;
    }

    private void preparePopup() {
        if (this.mDecorView != null) {
            this.mDecorView.cancelTransitions();
        }
        if (this.mBackground != null) {
            this.mBackgroundView = this.createBackgroundView(this.mContentView);
            this.mBackgroundView.setBackground(this.mBackground);
        } else {
            this.mBackgroundView = this.mContentView;
        }
        this.mDecorView = this.createDecorView(this.mBackgroundView);
        this.mDecorView.setIsRootNamespace(true);
        this.mBackgroundView.setElevation(this.mElevation);
        this.mDecorView.setFocusable(this.mFocusable);
        this.mPopupViewInitialLayoutDirectionInherited = this.mContentView.getRawLayoutDirection() == 2;
    }

    @Nonnull
    private PopupWindow.BackgroundView createBackgroundView(@Nonnull View contentView) {
        ViewGroup.LayoutParams layoutParams = this.mContentView.getLayoutParams();
        int height;
        if (layoutParams != null && layoutParams.height == -2) {
            height = -2;
        } else {
            height = -1;
        }
        PopupWindow.BackgroundView backgroundView = new PopupWindow.BackgroundView(this.mContext);
        backgroundView.addView(contentView, -1, height);
        return backgroundView;
    }

    @Nonnull
    private PopupWindow.DecorView createDecorView(View contentView) {
        ViewGroup.LayoutParams layoutParams = this.mContentView.getLayoutParams();
        int height;
        if (layoutParams != null && layoutParams.height == -2) {
            height = -2;
        } else {
            height = -1;
        }
        PopupWindow.DecorView decorView = new PopupWindow.DecorView(this.mContext);
        decorView.addView(contentView, -1, height);
        decorView.setClipChildren(false);
        decorView.setClipToPadding(false);
        return decorView;
    }

    private void invokePopup(@Nonnull WindowManager.LayoutParams p) {
        PopupWindow.DecorView decorView = this.mDecorView;
        this.setLayoutDirectionFromAnchor();
        this.mWindowManager.addView(decorView, p);
        if (this.mEnterTransition != null) {
            decorView.requestEnterTransition(this.mEnterTransition);
        }
    }

    private void setLayoutDirectionFromAnchor() {
        if (this.mAnchor != null) {
            View anchor = (View) this.mAnchor.get();
            if (anchor != null && this.mPopupViewInitialLayoutDirectionInherited) {
                this.mDecorView.setLayoutDirection(anchor.getLayoutDirection());
            }
        }
    }

    private int computeGravity() {
        int gravity = this.mGravity == 0 ? 8388659 : this.mGravity;
        if (this.mIsDropdown && (this.mClipToScreen || this.mClippingEnabled)) {
            gravity |= 268435456;
        }
        return gravity;
    }

    @Nonnull
    final WindowManager.LayoutParams createPopupLayoutParams() {
        WindowManager.LayoutParams p = new WindowManager.LayoutParams();
        p.gravity = this.computeGravity();
        p.flags = this.computeFlags(p.flags);
        p.type = this.mWindowLayoutType;
        p.height = this.mHeight;
        p.width = this.mWidth;
        return p;
    }

    private int computeFlags(int curFlags) {
        curFlags &= -9;
        if (!this.mFocusable) {
            curFlags |= 8;
        }
        if (this.mNotTouchModal) {
            curFlags |= 32;
        }
        return curFlags;
    }

    boolean findDropDownPosition(@Nonnull View anchor, WindowManager.LayoutParams outParams, int xOffset, int yOffset, int width, int height, int gravity, boolean allowScroll) {
        int anchorHeight = anchor.getHeight();
        int anchorWidth = anchor.getWidth();
        if (this.mOverlapAnchor) {
            yOffset -= anchorHeight;
        }
        int[] appScreenLocation = this.mTmpAppLocation;
        View appRootView = anchor.getRootView();
        appRootView.getLocationInWindow(appScreenLocation);
        int[] screenLocation = this.mTmpScreenLocation;
        anchor.getLocationInWindow(screenLocation);
        int[] drawingLocation = this.mTmpDrawingLocation;
        drawingLocation[0] = screenLocation[0] - appScreenLocation[0];
        drawingLocation[1] = screenLocation[1] - appScreenLocation[1];
        outParams.x = drawingLocation[0] + xOffset;
        outParams.y = drawingLocation[1] + anchorHeight + yOffset;
        outParams.gravity = this.computeGravity();
        outParams.width = width;
        outParams.height = height;
        int hGrav = Gravity.getAbsoluteGravity(gravity, anchor.getLayoutDirection()) & 7;
        if (hGrav == 5) {
            outParams.x -= width - anchorWidth;
        }
        boolean fitsVertical = this.tryFitVertical(outParams, yOffset, height, anchorHeight, drawingLocation[1], screenLocation[1], appScreenLocation[1], appScreenLocation[1] + appRootView.getHeight(), false);
        boolean fitsHorizontal = this.tryFitHorizontal(outParams, width, drawingLocation[0], screenLocation[0], appScreenLocation[0], appScreenLocation[0] + appRootView.getWidth(), false);
        if (!fitsVertical || !fitsHorizontal) {
            int scrollX = anchor.getScrollX();
            int scrollY = anchor.getScrollY();
            Rect r = new Rect(scrollX, scrollY, scrollX + width + xOffset, scrollY + height + anchorHeight + yOffset);
            if (allowScroll && anchor.requestRectangleOnScreen(r, true)) {
                anchor.getLocationInWindow(screenLocation);
                drawingLocation[0] = screenLocation[0] - appScreenLocation[0];
                drawingLocation[1] = screenLocation[1] - appScreenLocation[1];
                outParams.x = drawingLocation[0] + xOffset;
                outParams.y = drawingLocation[1] + anchorHeight + yOffset;
                if (hGrav == 5) {
                    outParams.x -= width - anchorWidth;
                }
            }
            this.tryFitVertical(outParams, yOffset, height, anchorHeight, drawingLocation[1], screenLocation[1], appScreenLocation[1], appScreenLocation[1] + appRootView.getHeight(), this.mClipToScreen);
            this.tryFitHorizontal(outParams, width, drawingLocation[0], screenLocation[0], appScreenLocation[0], appScreenLocation[0] + appRootView.getWidth(), this.mClipToScreen);
        }
        return outParams.y < drawingLocation[1];
    }

    private boolean tryFitVertical(@Nonnull WindowManager.LayoutParams outParams, int yOffset, int height, int anchorHeight, int drawingLocationY, int screenLocationY, int displayFrameTop, int displayFrameBottom, boolean allowResize) {
        int winOffsetY = screenLocationY - drawingLocationY;
        int anchorTopInScreen = outParams.y + winOffsetY;
        int spaceBelow = displayFrameBottom - anchorTopInScreen;
        if (anchorTopInScreen >= displayFrameTop && height <= spaceBelow) {
            return true;
        } else {
            int spaceAbove = anchorTopInScreen - anchorHeight - displayFrameTop;
            if (height <= spaceAbove) {
                if (this.mOverlapAnchor) {
                    yOffset += anchorHeight;
                }
                outParams.y = drawingLocationY - height + yOffset;
                return true;
            } else {
                return this.positionInDisplayVertical(outParams, height, drawingLocationY, screenLocationY, displayFrameTop, displayFrameBottom, allowResize);
            }
        }
    }

    private boolean positionInDisplayVertical(@Nonnull WindowManager.LayoutParams outParams, int height, int drawingLocationY, int screenLocationY, int displayFrameTop, int displayFrameBottom, boolean canResize) {
        boolean fitsInDisplay = true;
        int winOffsetY = screenLocationY - drawingLocationY;
        outParams.y += winOffsetY;
        outParams.height = height;
        int bottom = outParams.y + height;
        if (bottom > displayFrameBottom) {
            outParams.y -= bottom - displayFrameBottom;
        }
        if (outParams.y < displayFrameTop) {
            outParams.y = displayFrameTop;
            int displayFrameHeight = displayFrameBottom - displayFrameTop;
            if (canResize && height > displayFrameHeight) {
                outParams.height = displayFrameHeight;
            } else {
                fitsInDisplay = false;
            }
        }
        outParams.y -= winOffsetY;
        return fitsInDisplay;
    }

    private boolean tryFitHorizontal(@Nonnull WindowManager.LayoutParams outParams, int width, int drawingLocationX, int screenLocationX, int displayFrameLeft, int displayFrameRight, boolean allowResize) {
        int winOffsetX = screenLocationX - drawingLocationX;
        int anchorLeftInScreen = outParams.x + winOffsetX;
        int spaceRight = displayFrameRight - anchorLeftInScreen;
        return anchorLeftInScreen >= displayFrameLeft && width <= spaceRight ? true : this.positionInDisplayHorizontal(outParams, width, drawingLocationX, screenLocationX, displayFrameLeft, displayFrameRight, allowResize);
    }

    private boolean positionInDisplayHorizontal(@Nonnull WindowManager.LayoutParams outParams, int width, int drawingLocationX, int screenLocationX, int displayFrameLeft, int displayFrameRight, boolean canResize) {
        boolean fitsInDisplay = true;
        int winOffsetX = screenLocationX - drawingLocationX;
        outParams.x += winOffsetX;
        int right = outParams.x + width;
        if (right > displayFrameRight) {
            outParams.x -= right - displayFrameRight;
        }
        if (outParams.x < displayFrameLeft) {
            outParams.x = displayFrameLeft;
            int displayFrameWidth = displayFrameRight - displayFrameLeft;
            if (canResize && width > displayFrameWidth) {
                outParams.width = displayFrameWidth;
            } else {
                fitsInDisplay = false;
            }
        }
        outParams.x -= winOffsetX;
        return fitsInDisplay;
    }

    public int getMaxAvailableHeight(@Nonnull View anchor) {
        return this.getMaxAvailableHeight(anchor, 0);
    }

    public int getMaxAvailableHeight(@Nonnull View anchor, int yOffset) {
        View appView = anchor.getRootView();
        int[] anchorPos = this.mTmpDrawingLocation;
        anchor.getLocationInWindow(anchorPos);
        int bottomEdge = appView.getBottom();
        int distanceToBottom;
        if (this.mOverlapAnchor) {
            distanceToBottom = bottomEdge - anchorPos[1] - yOffset;
        } else {
            distanceToBottom = bottomEdge - (anchorPos[1] + anchor.getHeight()) - yOffset;
        }
        int distanceToTop = anchorPos[1] - appView.getTop() + yOffset;
        int returnedHeight = Math.max(distanceToBottom, distanceToTop);
        if (this.mBackground != null) {
            this.mBackground.getPadding(this.mTempRect);
            returnedHeight -= this.mTempRect.top + this.mTempRect.bottom;
        }
        return returnedHeight;
    }

    public void dismiss() {
        if (this.mIsShowing && !this.mIsTransitioningToDismiss) {
            final PopupWindow.DecorView decorView = this.mDecorView;
            final View contentView = this.mContentView;
            if (!(contentView.getParent() instanceof ViewGroup contentHolder)) {
                contentHolder = null;
            }
            decorView.cancelTransitions();
            this.mIsShowing = false;
            this.mIsTransitioningToDismiss = true;
            Transition exitTransition = this.mExitTransition;
            if (exitTransition != null && decorView.isLaidOut() && (this.mIsAnchorRootAttached || this.mAnchorRoot == null)) {
                decorView.setFocusable(false);
                View anchorRoot = this.mAnchorRoot != null ? (View) this.mAnchorRoot.get() : null;
                Rect epicenter = this.getTransitionEpicenter();
                decorView.startExitTransition(exitTransition, anchorRoot, epicenter, new TransitionListener() {

                    @Override
                    public void onTransitionEnd(@Nonnull Transition transition) {
                        PopupWindow.this.dismissImmediate(decorView, contentHolder, contentView);
                    }
                });
            } else {
                this.dismissImmediate(decorView, contentHolder, contentView);
            }
            this.detachFromAnchor();
            if (this.mOnDismissListener != null) {
                this.mOnDismissListener.onDismiss();
            }
        }
    }

    @Nullable
    protected final Rect getTransitionEpicenter() {
        View anchor = this.mAnchor != null ? (View) this.mAnchor.get() : null;
        View decor = this.mDecorView;
        if (anchor != null && decor != null) {
            int[] anchorLocation = this.mTmpScreenLocation;
            anchor.getLocationInWindow(anchorLocation);
            int[] popupLocation = this.mTmpAppLocation;
            this.mDecorView.getLocationInWindow(popupLocation);
            Rect bounds = new Rect(0, 0, anchor.getWidth(), anchor.getHeight());
            bounds.offset(anchorLocation[0] - popupLocation[0], anchorLocation[1] - popupLocation[1]);
            if (this.mEpicenterBounds != null) {
                int offsetX = bounds.left;
                int offsetY = bounds.top;
                bounds.set(this.mEpicenterBounds);
                bounds.offset(offsetX, offsetY);
            }
            return null;
        } else {
            return null;
        }
    }

    private void dismissImmediate(@Nonnull View decorView, @Nullable ViewGroup contentHolder, View contentView) {
        if (decorView.getParent() != null) {
            this.mWindowManager.removeView(decorView);
        }
        if (contentHolder != null) {
            contentHolder.removeView(contentView);
        }
        this.mDecorView = null;
        this.mBackgroundView = null;
        this.mIsTransitioningToDismiss = false;
    }

    public void setOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
    }

    protected final PopupWindow.OnDismissListener getOnDismissListener() {
        return this.mOnDismissListener;
    }

    public void update() {
        if (this.mIsShowing && this.mContentView != null) {
            WindowManager.LayoutParams p = this.getDecorViewLayoutParams();
            boolean update = false;
            int newFlags = this.computeFlags(p.flags);
            if (newFlags != p.flags) {
                p.flags = newFlags;
                update = true;
            }
            int newGravity = this.computeGravity();
            if (newGravity != p.gravity) {
                p.gravity = newGravity;
                update = true;
            }
            if (update) {
                this.update(this.mAnchor != null ? (View) this.mAnchor.get() : null, p);
            }
        }
    }

    protected void update(View anchor, ViewGroup.LayoutParams params) {
        this.setLayoutDirectionFromAnchor();
        this.mWindowManager.updateViewLayout(this.mDecorView, params);
    }

    public void update(int width, int height) {
        WindowManager.LayoutParams p = this.getDecorViewLayoutParams();
        this.update(p.x, p.y, width, height, false);
    }

    public void update(int x, int y, int width, int height) {
        this.update(x, y, width, height, false);
    }

    public void update(int x, int y, int width, int height, boolean force) {
        if (width >= 0) {
            this.setWidth(width);
        }
        if (height >= 0) {
            this.setHeight(height);
        }
        if (this.mIsShowing && this.mContentView != null) {
            WindowManager.LayoutParams p = this.getDecorViewLayoutParams();
            boolean update = force;
            if (width != -1 && p.width != this.mWidth) {
                p.width = this.mWidth;
                update = true;
            }
            if (height != -1 && p.height != this.mHeight) {
                p.height = this.mHeight;
                update = true;
            }
            if (p.x != x) {
                p.x = x;
                update = true;
            }
            if (p.y != y) {
                p.y = y;
                update = true;
            }
            int newFlags = this.computeFlags(p.flags);
            if (newFlags != p.flags) {
                p.flags = newFlags;
                update = true;
            }
            int newGravity = this.computeGravity();
            if (newGravity != p.gravity) {
                p.gravity = newGravity;
                update = true;
            }
            View anchor = null;
            if (this.mAnchor != null && this.mAnchor.get() != null) {
                anchor = (View) this.mAnchor.get();
            }
            if (update) {
                this.update(anchor, p);
            }
        }
    }

    protected boolean hasContentView() {
        return this.mContentView != null;
    }

    protected boolean hasDecorView() {
        return this.mDecorView != null;
    }

    protected WindowManager.LayoutParams getDecorViewLayoutParams() {
        return (WindowManager.LayoutParams) this.mDecorView.getLayoutParams();
    }

    public void update(View anchor, int width, int height) {
        this.update(anchor, false, 0, 0, width, height);
    }

    public void update(View anchor, int xoff, int yoff, int width, int height) {
        this.update(anchor, true, xoff, yoff, width, height);
    }

    private void update(View anchor, boolean updateLocation, int xoff, int yoff, int width, int height) {
        if (this.isShowing() && this.hasContentView()) {
            WeakReference<View> oldAnchor = this.mAnchor;
            int gravity = this.mAnchoredGravity;
            boolean needsUpdate = updateLocation && (this.mAnchorXOff != xoff || this.mAnchorYOff != yoff);
            if (oldAnchor != null && oldAnchor.get() == anchor && (!needsUpdate || this.mIsDropdown)) {
                if (needsUpdate) {
                    this.mAnchorXOff = xoff;
                    this.mAnchorYOff = yoff;
                }
            } else {
                this.attachToAnchor(anchor, xoff, yoff, gravity);
            }
            WindowManager.LayoutParams p = this.getDecorViewLayoutParams();
            int oldGravity = p.gravity;
            int oldWidth = p.width;
            int oldHeight = p.height;
            int oldX = p.x;
            int oldY = p.y;
            if (width < 0) {
                width = this.mWidth;
            }
            if (height < 0) {
                height = this.mHeight;
            }
            boolean aboveAnchor = this.findDropDownPosition(anchor, p, this.mAnchorXOff, this.mAnchorYOff, width, height, gravity, true);
            this.updateAboveAnchor(aboveAnchor);
            boolean paramsChanged = oldGravity != p.gravity || oldX != p.x || oldY != p.y || oldWidth != p.width || oldHeight != p.height;
            int newWidth = width < 0 ? width : p.width;
            int newHeight = height < 0 ? height : p.height;
            this.update(p.x, p.y, newWidth, newHeight, paramsChanged);
        }
    }

    void detachFromAnchor() {
        View anchor = this.getAnchor();
        if (anchor != null) {
            ViewTreeObserver treeObserver = anchor.getViewTreeObserver();
            treeObserver.removeOnScrollChangedListener(this.mOnScrollChangedListener);
            anchor.removeOnAttachStateChangeListener(this.mOnAnchorDetachedListener);
        }
        View anchorRoot = this.mAnchorRoot != null ? (View) this.mAnchorRoot.get() : null;
        if (anchorRoot != null) {
            anchorRoot.removeOnAttachStateChangeListener(this.mOnAnchorRootDetachedListener);
            anchorRoot.removeOnLayoutChangeListener(this.mOnLayoutChangeListener);
        }
        this.mAnchor = null;
        this.mAnchorRoot = null;
        this.mIsAnchorRootAttached = false;
    }

    void attachToAnchor(@Nonnull View anchor, int xOff, int yOff, int gravity) {
        this.detachFromAnchor();
        ViewTreeObserver treeObserver = anchor.getViewTreeObserver();
        treeObserver.addOnScrollChangedListener(this.mOnScrollChangedListener);
        anchor.addOnAttachStateChangeListener(this.mOnAnchorDetachedListener);
        View anchorRoot = anchor.getRootView();
        anchorRoot.addOnAttachStateChangeListener(this.mOnAnchorRootDetachedListener);
        anchorRoot.addOnLayoutChangeListener(this.mOnLayoutChangeListener);
        this.mAnchor = new WeakReference(anchor);
        this.mAnchorRoot = new WeakReference(anchorRoot);
        this.mIsAnchorRootAttached = anchorRoot.isAttachedToWindow();
        this.mAnchorXOff = xOff;
        this.mAnchorYOff = yOff;
        this.mAnchoredGravity = gravity;
    }

    @Nullable
    protected View getAnchor() {
        return this.mAnchor != null ? (View) this.mAnchor.get() : null;
    }

    private void alignToAnchor() {
        View anchor = this.mAnchor != null ? (View) this.mAnchor.get() : null;
        if (anchor != null && anchor.isAttachedToWindow() && this.hasDecorView()) {
            WindowManager.LayoutParams p = this.getDecorViewLayoutParams();
            this.updateAboveAnchor(this.findDropDownPosition(anchor, p, this.mAnchorXOff, this.mAnchorYOff, p.width, p.height, this.mAnchoredGravity, false));
            this.update(p.x, p.y, -1, -1, true);
        }
    }

    private class BackgroundView extends FrameLayout {

        public BackgroundView(Context context) {
            super(context);
        }

        @Nonnull
        @Override
        protected int[] onCreateDrawableState(int extraSpace) {
            if (PopupWindow.this.mAboveAnchor) {
                int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
                View.mergeDrawableStates(drawableState, PopupWindow.ABOVE_ANCHOR_STATE_SET);
                return drawableState;
            } else {
                return super.onCreateDrawableState(extraSpace);
            }
        }
    }

    private class DecorView extends FrameLayout {

        private final View.OnAttachStateChangeListener mOnAnchorRootDetachedListener = new View.OnAttachStateChangeListener() {

            @Override
            public void onViewAttachedToWindow(View v) {
            }

            @Override
            public void onViewDetachedFromWindow(@Nonnull View v) {
                v.removeOnAttachStateChangeListener(this);
                if (DecorView.this.isAttachedToWindow()) {
                    TransitionManager.endTransitions(DecorView.this);
                }
            }
        };

        private Runnable mCleanupAfterExit;

        public DecorView(Context context) {
            super(context);
        }

        @Override
        public boolean onInterceptTouchEvent(@NonNull MotionEvent ev) {
            return PopupWindow.this.mTouchInterceptor != null && PopupWindow.this.mTouchInterceptor.onTouch(this, ev) ? true : super.onInterceptTouchEvent(ev);
        }

        @Override
        public boolean onTouchEvent(@NonNull MotionEvent event) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            if (event.getAction() != 0 || x >= 0 && x < this.getWidth() && y >= 0 && y < this.getHeight()) {
                if (event.getAction() == 4) {
                    PopupWindow.this.dismiss();
                    return true;
                } else {
                    return super.onTouchEvent(event);
                }
            } else {
                PopupWindow.this.dismiss();
                return true;
            }
        }

        @Override
        public boolean dispatchKeyEvent(@Nonnull KeyEvent event) {
            if (event.getKeyCode() == 256) {
                if (this.getKeyDispatcherState() == null) {
                    return super.dispatchKeyEvent(event);
                }
                if (event.getAction() == 0 && event.getRepeatCount() == 0) {
                    KeyEvent.DispatcherState state = this.getKeyDispatcherState();
                    state.startTracking(event, this);
                    return true;
                }
                if (event.getAction() == 1) {
                    KeyEvent.DispatcherState state = this.getKeyDispatcherState();
                    if (state.isTracking(event) && !event.isCanceled()) {
                        PopupWindow.this.dismiss();
                        return true;
                    }
                }
            }
            return super.dispatchKeyEvent(event);
        }

        public void requestEnterTransition(@Nullable Transition transition) {
            ViewTreeObserver observer = this.getViewTreeObserver();
            if (transition != null) {
                final Transition enterTransition = transition.clone();
                observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        ViewTreeObserver observer = DecorView.this.getViewTreeObserver();
                        observer.removeOnGlobalLayoutListener(this);
                        Rect epicenter = PopupWindow.this.getTransitionEpicenter();
                        enterTransition.setEpicenterCallback(t -> epicenter);
                        DecorView.this.startEnterTransition(enterTransition);
                    }
                });
            }
        }

        private void startEnterTransition(@Nonnull Transition enterTransition) {
            int count = this.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = this.getChildAt(i);
                enterTransition.addTarget(child);
                child.setTransitionVisibility(4);
            }
            TransitionManager.beginDelayedTransition(this, enterTransition);
            for (int i = 0; i < count; i++) {
                View child = this.getChildAt(i);
                child.setTransitionVisibility(0);
            }
        }

        public void startExitTransition(@Nonnull Transition transition, @Nullable View anchorRoot, @Nullable Rect epicenter, @Nonnull TransitionListener listener) {
            if (anchorRoot != null) {
                anchorRoot.addOnAttachStateChangeListener(this.mOnAnchorRootDetachedListener);
            }
            this.mCleanupAfterExit = () -> {
                listener.onTransitionEnd(transition);
                if (anchorRoot != null) {
                    anchorRoot.removeOnAttachStateChangeListener(this.mOnAnchorRootDetachedListener);
                }
                this.mCleanupAfterExit = null;
            };
            Transition exitTransition = transition.clone();
            exitTransition.addListener(new TransitionListener() {

                @Override
                public void onTransitionEnd(@Nonnull Transition t) {
                    t.removeListener(this);
                    if (DecorView.this.mCleanupAfterExit != null) {
                        DecorView.this.mCleanupAfterExit.run();
                    }
                }
            });
            exitTransition.setEpicenterCallback(t -> epicenter);
            int count = this.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = this.getChildAt(i);
                exitTransition.addTarget(child);
            }
            TransitionManager.beginDelayedTransition(this, exitTransition);
            for (int i = 0; i < count; i++) {
                View child = this.getChildAt(i);
                child.setVisibility(4);
            }
        }

        public void cancelTransitions() {
            TransitionManager.endTransitions(this);
            if (this.mCleanupAfterExit != null) {
                this.mCleanupAfterExit.run();
            }
        }
    }

    @FunctionalInterface
    public interface OnDismissListener {

        void onDismiss();
    }
}