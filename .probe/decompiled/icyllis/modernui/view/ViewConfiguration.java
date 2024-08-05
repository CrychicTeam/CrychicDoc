package icyllis.modernui.view;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.core.Context;
import icyllis.modernui.resources.Resources;
import icyllis.modernui.util.DisplayMetrics;
import icyllis.modernui.util.SparseArray;
import org.jetbrains.annotations.ApiStatus.Internal;

public class ViewConfiguration {

    public static final int SCROLL_BAR_SIZE = 8;

    private static final int FADING_EDGE_LENGTH = 12;

    private static final int SCROLL_BAR_FADE_DURATION = 500;

    private static final int SCROLL_BAR_DEFAULT_DELAY = 600;

    private static final int PRESSED_STATE_DURATION = 64;

    private static final int DEFAULT_LONG_PRESS_TIMEOUT = 1000;

    private static final int TAP_TIMEOUT = 100;

    private static final int EDGE_SLOP = 12;

    public static final int TOUCH_SLOP = 4;

    public static final int MIN_SCROLLBAR_TOUCH_TARGET = 16;

    public static final int MINIMUM_FLING_VELOCITY = 50;

    public static final int MAXIMUM_FLING_VELOCITY = 8000;

    public static final int OVERSCROLL_DISTANCE = 0;

    public static final int OVERFLING_DISTANCE = 12;

    public static final float HORIZONTAL_SCROLL_FACTOR = 64.0F;

    public static final float VERTICAL_SCROLL_FACTOR = 64.0F;

    private static final int LONG_PRESS_TOOLTIP_HIDE_TIMEOUT = 1500;

    private static final int HOVER_TOOLTIP_SHOW_TIMEOUT = 500;

    private static final int HOVER_TOOLTIP_HIDE_TIMEOUT = 30000;

    private final int mEdgeSlop;

    private final int mFadingEdgeLength;

    private final int mMinimumFlingVelocity;

    private final int mMaximumFlingVelocity;

    private final int mScrollbarSize;

    private final int mTouchSlop;

    private volatile int mMinScalingSpan;

    private final int mHoverSlop;

    private final int mMinScrollbarTouchTarget;

    private volatile int mDoubleTapTouchSlop;

    private volatile int mPagingTouchSlop;

    private volatile int mDoubleTapSlop;

    private volatile int mWindowTouchSlop;

    private final int mOverscrollDistance;

    private final int mOverflingDistance;

    private final float mVerticalScrollFactor;

    private final float mHorizontalScrollFactor;

    static final SparseArray<ViewConfiguration> sConfigurations = new SparseArray<>(2);

    ViewConfiguration(@NonNull Context context) {
        Resources res = context.getResources();
        DisplayMetrics metrics = res.getDisplayMetrics();
        float density = metrics.density;
        this.mEdgeSlop = (int) (density * 12.0F + 0.5F);
        this.mFadingEdgeLength = (int) (density * 12.0F + 0.5F);
        this.mScrollbarSize = (int) (density * 8.0F + 0.5F);
        this.mTouchSlop = (int) (density * 4.0F + 0.5F);
        this.mHoverSlop = this.mTouchSlop;
        this.mMinScrollbarTouchTarget = (int) (density * 16.0F + 0.5F);
        this.mMinimumFlingVelocity = (int) (density * 50.0F + 0.5F);
        this.mMaximumFlingVelocity = (int) (density * 8000.0F + 0.5F);
        this.mVerticalScrollFactor = (float) ((int) (density * 64.0F + 0.5F));
        this.mHorizontalScrollFactor = (float) ((int) (density * 64.0F + 0.5F));
        this.mOverscrollDistance = (int) (density * 0.0F + 0.5F);
        this.mOverflingDistance = (int) (density * 12.0F + 0.5F);
    }

    @NonNull
    public static ViewConfiguration get(@NonNull Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int density = metrics.densityDpi;
        ViewConfiguration configuration = sConfigurations.get(density);
        if (configuration == null) {
            configuration = new ViewConfiguration(context);
            sConfigurations.put(density, configuration);
        }
        return configuration;
    }

    public static int getScrollBarFadeDuration() {
        return 500;
    }

    public static int getScrollDefaultDelay() {
        return 600;
    }

    public static int getPressedStateDuration() {
        return 64;
    }

    public static int getLongPressTimeout() {
        return 1000;
    }

    public static int getTapTimeout() {
        return 100;
    }

    public int getScaledScrollbarSize() {
        return this.mScrollbarSize;
    }

    public int getScaledEdgeSlop() {
        return this.mEdgeSlop;
    }

    public int getScaledFadingEdgeLength() {
        return this.mFadingEdgeLength;
    }

    public int getScaledTouchSlop() {
        return this.mTouchSlop;
    }

    public int getScaledHoverSlop() {
        return this.mHoverSlop;
    }

    public int getScaledMinScrollbarTouchTarget() {
        return this.mMinScrollbarTouchTarget;
    }

    public int getScaledMinimumFlingVelocity() {
        return this.mMinimumFlingVelocity;
    }

    public int getScaledMaximumFlingVelocity() {
        return this.mMaximumFlingVelocity;
    }

    public int getScaledOverscrollDistance() {
        return this.mOverscrollDistance;
    }

    public int getScaledOverflingDistance() {
        return this.mOverflingDistance;
    }

    public float getScaledVerticalScrollFactor() {
        return this.mVerticalScrollFactor;
    }

    public float getScaledHorizontalScrollFactor() {
        return this.mHorizontalScrollFactor;
    }

    @Internal
    public static int getLongPressTooltipHideTimeout() {
        return 1500;
    }

    @Internal
    public static int getHoverTooltipShowTimeout() {
        return 500;
    }

    @Internal
    public static int getHoverTooltipHideTimeout() {
        return 30000;
    }
}