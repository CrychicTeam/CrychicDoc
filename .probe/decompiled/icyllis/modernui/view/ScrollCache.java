package icyllis.modernui.view;

import icyllis.modernui.graphics.Rect;

final class ScrollCache implements Runnable {

    public static final int OFF = 0;

    public static final int ON = 1;

    public static final int FADING = 2;

    private final View mHost;

    int mState = 0;

    ScrollBar mScrollBar;

    int mScrollBarSize;

    int mScrollBarMinTouchTarget;

    boolean mFadeScrollBars = true;

    long mFadeStartTime;

    public int fadingEdgeLength;

    int mDefaultDelayBeforeFade;

    int mFadeDuration;

    final Rect mScrollBarBounds = new Rect();

    final Rect mScrollBarTouchBounds = new Rect();

    static final int NOT_DRAGGING = 0;

    static final int DRAGGING_VERTICAL_SCROLL_BAR = 1;

    static final int DRAGGING_HORIZONTAL_SCROLL_BAR = 2;

    int mScrollBarDraggingState = 0;

    float mScrollBarDraggingPos;

    ScrollCache(View host) {
        this.mHost = host;
        ViewConfiguration cfg = ViewConfiguration.get(host.getContext());
        this.mScrollBarSize = cfg.getScaledScrollbarSize();
        this.mScrollBarMinTouchTarget = cfg.getScaledMinScrollbarTouchTarget();
        this.mDefaultDelayBeforeFade = ViewConfiguration.getScrollDefaultDelay();
        this.mFadeDuration = ViewConfiguration.getScrollBarFadeDuration();
    }

    public void run() {
        this.mState = 2;
        this.mHost.invalidate();
    }

    public static int getThumbLength(int size, int thickness, int extent, int range) {
        int minLength = thickness * 2;
        int length = Math.round((float) size * (float) extent / (float) range);
        if (length < minLength) {
            length = minLength;
        }
        return length;
    }

    public static int getThumbOffset(int size, int thumbLength, int extent, int range, int offset) {
        int thumbOffset = Math.round((float) (size - thumbLength) * (float) offset / (float) (range - extent));
        if (thumbOffset > size - thumbLength) {
            thumbOffset = size - thumbLength;
        }
        return thumbOffset;
    }
}