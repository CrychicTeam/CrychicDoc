package icyllis.modernui.widget;

import icyllis.modernui.ModernUI;
import icyllis.modernui.animation.TimeInterpolator;
import icyllis.modernui.annotation.CallSuper;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.core.Context;
import icyllis.modernui.core.Core;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.util.DataSetObserver;
import icyllis.modernui.view.FocusFinder;
import icyllis.modernui.view.KeyEvent;
import icyllis.modernui.view.MeasureSpec;
import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.view.VelocityTracker;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewConfiguration;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.view.ViewParent;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class ViewPager extends ViewGroup {

    private static final Marker MARKER = MarkerManager.getMarker("ViewPager");

    private static final int DEFAULT_OFFSCREEN_PAGES = 1;

    private static final int MAX_SETTLE_DURATION = 600;

    private static final int MIN_DISTANCE_FOR_FLING = 25;

    private static final int DEFAULT_GUTTER_SIZE = 16;

    private static final int MIN_FLING_VELOCITY = 400;

    private int mExpectedAdapterCount;

    private final ArrayList<ViewPager.ItemInfo> mItems = new ArrayList();

    private final ViewPager.ItemInfo mTempItem = new ViewPager.ItemInfo();

    private final Rect mTempRect = new Rect();

    PagerAdapter mAdapter;

    int mCurItem;

    private OverScroller mScroller;

    private boolean mIsScrollStarted;

    private ViewPager.PagerObserver mObserver;

    private int mPageMargin;

    private Drawable mMarginDrawable;

    private int mTopPageBounds;

    private int mBottomPageBounds;

    private float mFirstOffset = -Float.MAX_VALUE;

    private float mLastOffset = Float.MAX_VALUE;

    private boolean mInLayout;

    private boolean mPopulatePending;

    private int mOffscreenPageLimit = 1;

    private boolean mIsBeingDragged;

    private boolean mIsUnableToDrag;

    private int mDefaultGutterSize;

    private int mGutterSize;

    private int mTouchSlop;

    private float mLastMotionX;

    private float mLastMotionY;

    private float mInitialMotionX;

    private float mInitialMotionY;

    private int mActivePointerId = -1;

    private static final int INVALID_POINTER = -1;

    private VelocityTracker mVelocityTracker;

    private int mMinimumVelocity;

    private int mMaximumVelocity;

    private int mFlingDistance;

    private int mCloseEnough;

    private static final int CLOSE_ENOUGH = 2;

    private boolean mFakeDragging;

    private long mFakeDragBeginTime;

    private EdgeEffect mLeftEdge;

    private EdgeEffect mRightEdge;

    private boolean mFirstLayout = true;

    private boolean mCalledSuper;

    private int mDecorChildCount;

    private List<ViewPager.OnPageChangeListener> mOnPageChangeListeners;

    private ViewPager.OnPageChangeListener mInternalPageChangeListener;

    private List<ViewPager.OnAdapterChangeListener> mAdapterChangeListeners;

    private ViewPager.PageTransformer mPageTransformer;

    private int mPageTransformerLayerType;

    private static final int DRAW_ORDER_DEFAULT = 0;

    private static final int DRAW_ORDER_FORWARD = 1;

    private static final int DRAW_ORDER_REVERSE = 2;

    private int mDrawingOrder;

    private ArrayList<View> mDrawingOrderedChildren;

    private static final ViewPager.ViewPositionComparator sPositionComparator = new ViewPager.ViewPositionComparator();

    public static final int SCROLL_STATE_IDLE = 0;

    public static final int SCROLL_STATE_DRAGGING = 1;

    public static final int SCROLL_STATE_SETTLING = 2;

    private final Runnable mEndScrollRunnable = () -> {
        this.setScrollState(0);
        this.populate();
    };

    private int mScrollState = 0;

    public ViewPager(Context context) {
        super(context);
        this.initViewPager(context);
    }

    void initViewPager(Context context) {
        this.setWillNotDraw(false);
        this.setDescendantFocusability(262144);
        this.setFocusable(true);
        this.mScroller = new OverScroller(TimeInterpolator.DECELERATE_QUINTIC);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        float density = 1.0F;
        this.mTouchSlop = 8;
        this.mMinimumVelocity = 400;
        this.mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        this.mLeftEdge = new EdgeEffect();
        this.mRightEdge = new EdgeEffect();
        this.mFlingDistance = 25;
        this.mCloseEnough = 2;
        this.mDefaultGutterSize = 16;
    }

    @Override
    protected void onDetachedFromWindow() {
        this.removeCallbacks(this.mEndScrollRunnable);
        if (this.mScroller != null && !this.mScroller.isFinished()) {
            this.mScroller.abortAnimation();
        }
        super.onDetachedFromWindow();
    }

    void setScrollState(int newState) {
        if (this.mScrollState != newState) {
            this.mScrollState = newState;
            if (this.mPageTransformer != null) {
                this.enableLayers(newState != 0);
            }
            this.dispatchOnScrollStateChanged(newState);
        }
    }

    public void setAdapter(@Nullable PagerAdapter adapter) {
        if (this.mAdapter != null) {
            this.mAdapter.setViewPagerObserver(null);
            this.mAdapter.startUpdate(this);
            for (int i = 0; i < this.mItems.size(); i++) {
                ViewPager.ItemInfo ii = (ViewPager.ItemInfo) this.mItems.get(i);
                this.mAdapter.destroyItem(this, ii.position, ii.object);
            }
            this.mAdapter.finishUpdate(this);
            this.mItems.clear();
            this.removeNonDecorViews();
            this.mCurItem = 0;
            this.scrollTo(0, 0);
        }
        PagerAdapter oldAdapter = this.mAdapter;
        this.mAdapter = adapter;
        this.mExpectedAdapterCount = 0;
        if (this.mAdapter != null) {
            if (this.mObserver == null) {
                this.mObserver = new ViewPager.PagerObserver();
            }
            this.mAdapter.setViewPagerObserver(this.mObserver);
            this.mPopulatePending = false;
            boolean wasFirstLayout = this.mFirstLayout;
            this.mFirstLayout = true;
            this.mExpectedAdapterCount = this.mAdapter.getCount();
            if (!wasFirstLayout) {
                this.populate();
            } else {
                this.requestLayout();
            }
        }
        if (this.mAdapterChangeListeners != null && !this.mAdapterChangeListeners.isEmpty()) {
            int i = 0;
            for (int count = this.mAdapterChangeListeners.size(); i < count; i++) {
                ((ViewPager.OnAdapterChangeListener) this.mAdapterChangeListeners.get(i)).onAdapterChanged(this, oldAdapter, adapter);
            }
        }
    }

    private void removeNonDecorViews() {
        for (int i = 0; i < this.getChildCount(); i++) {
            View child = this.getChildAt(i);
            ViewPager.LayoutParams lp = (ViewPager.LayoutParams) child.getLayoutParams();
            if (!lp.isDecor) {
                this.removeViewAt(i);
                i--;
            }
        }
    }

    @Nullable
    public PagerAdapter getAdapter() {
        return this.mAdapter;
    }

    public void addOnAdapterChangeListener(@NonNull ViewPager.OnAdapterChangeListener listener) {
        if (this.mAdapterChangeListeners == null) {
            this.mAdapterChangeListeners = new ArrayList();
        }
        this.mAdapterChangeListeners.add(listener);
    }

    public void removeOnAdapterChangeListener(@NonNull ViewPager.OnAdapterChangeListener listener) {
        if (this.mAdapterChangeListeners != null) {
            this.mAdapterChangeListeners.remove(listener);
        }
    }

    private int getClientWidth() {
        return this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight();
    }

    public void setCurrentItem(int item) {
        this.mPopulatePending = false;
        this.setCurrentItemInternal(item, !this.mFirstLayout, false);
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        this.mPopulatePending = false;
        this.setCurrentItemInternal(item, smoothScroll, false);
    }

    public int getCurrentItem() {
        return this.mCurItem;
    }

    void setCurrentItemInternal(int item, boolean smoothScroll, boolean always) {
        this.setCurrentItemInternal(item, smoothScroll, always, 0);
    }

    void setCurrentItemInternal(int item, boolean smoothScroll, boolean always, int velocity) {
        if (this.mAdapter != null && this.mAdapter.getCount() > 0) {
            if (always || this.mCurItem != item || this.mItems.size() == 0) {
                if (item < 0) {
                    item = 0;
                } else if (item >= this.mAdapter.getCount()) {
                    item = this.mAdapter.getCount() - 1;
                }
                int pageLimit = this.mOffscreenPageLimit;
                if (item > this.mCurItem + pageLimit || item < this.mCurItem - pageLimit) {
                    for (int i = 0; i < this.mItems.size(); i++) {
                        ((ViewPager.ItemInfo) this.mItems.get(i)).scrolling = true;
                    }
                }
                boolean dispatchSelected = this.mCurItem != item;
                if (this.mFirstLayout) {
                    this.mCurItem = item;
                    if (dispatchSelected) {
                        this.dispatchOnPageSelected(item);
                    }
                    this.requestLayout();
                } else {
                    this.populate(item);
                    this.scrollToItem(item, smoothScroll, velocity, dispatchSelected);
                }
            }
        }
    }

    private void scrollToItem(int item, boolean smoothScroll, int velocity, boolean dispatchSelected) {
        ViewPager.ItemInfo curInfo = this.infoForPosition(item);
        int destX = 0;
        if (curInfo != null) {
            int width = this.getClientWidth();
            destX = (int) ((float) width * Math.max(this.mFirstOffset, Math.min(curInfo.offset, this.mLastOffset)));
        }
        if (smoothScroll) {
            this.smoothScrollTo(destX, 0, velocity);
            if (dispatchSelected) {
                this.dispatchOnPageSelected(item);
            }
        } else {
            if (dispatchSelected) {
                this.dispatchOnPageSelected(item);
            }
            this.completeScroll(false);
            this.scrollTo(destX, 0);
            this.pageScrolled(destX);
        }
    }

    public void addOnPageChangeListener(@NonNull ViewPager.OnPageChangeListener listener) {
        if (this.mOnPageChangeListeners == null) {
            this.mOnPageChangeListeners = new ArrayList();
        }
        this.mOnPageChangeListeners.add(listener);
    }

    public void removeOnPageChangeListener(@NonNull ViewPager.OnPageChangeListener listener) {
        if (this.mOnPageChangeListeners != null) {
            this.mOnPageChangeListeners.remove(listener);
        }
    }

    public void clearOnPageChangeListeners() {
        if (this.mOnPageChangeListeners != null) {
            this.mOnPageChangeListeners.clear();
        }
    }

    public void setPageTransformer(boolean reverseDrawingOrder, @Nullable ViewPager.PageTransformer transformer) {
        this.setPageTransformer(reverseDrawingOrder, transformer, 1);
    }

    public void setPageTransformer(boolean reverseDrawingOrder, @Nullable ViewPager.PageTransformer transformer, int pageLayerType) {
        boolean hasTransformer = transformer != null;
        boolean needsPopulate = hasTransformer == (this.mPageTransformer == null);
        this.mPageTransformer = transformer;
        this.setChildrenDrawingOrderEnabled(hasTransformer);
        if (hasTransformer) {
            this.mDrawingOrder = reverseDrawingOrder ? 2 : 1;
            this.mPageTransformerLayerType = pageLayerType;
        } else {
            this.mDrawingOrder = 0;
        }
        if (needsPopulate) {
            this.populate();
        }
    }

    ViewPager.OnPageChangeListener setInternalPageChangeListener(ViewPager.OnPageChangeListener listener) {
        ViewPager.OnPageChangeListener oldListener = this.mInternalPageChangeListener;
        this.mInternalPageChangeListener = listener;
        return oldListener;
    }

    public int getOffscreenPageLimit() {
        return this.mOffscreenPageLimit;
    }

    public void setOffscreenPageLimit(int limit) {
        if (limit < 1) {
            limit = 1;
        }
        if (limit != this.mOffscreenPageLimit) {
            this.mOffscreenPageLimit = limit;
            this.populate();
        }
    }

    public void setPageMargin(int marginPixels) {
        int oldMargin = this.mPageMargin;
        this.mPageMargin = marginPixels;
        int width = this.getWidth();
        this.recomputeScrollPosition(width, width, marginPixels, oldMargin);
        this.requestLayout();
    }

    public int getPageMargin() {
        return this.mPageMargin;
    }

    public void setPageMarginDrawable(@Nullable Drawable d) {
        this.mMarginDrawable = d;
        if (d != null) {
            this.refreshDrawableState();
        }
        this.setWillNotDraw(d == null);
        this.invalidate();
    }

    public void setEdgeEffectColor(int color) {
        this.setLeftEdgeEffectColor(color);
        this.setRightEdgeEffectColor(color);
    }

    public void setLeftEdgeEffectColor(int color) {
        this.mLeftEdge.setColor(color);
    }

    public void setRightEdgeEffectColor(int color) {
        this.mRightEdge.setColor(color);
    }

    public int getLeftEdgeEffectColor() {
        return this.mLeftEdge.getColor();
    }

    public int getRightEdgeEffectColor() {
        return this.mRightEdge.getColor();
    }

    @Override
    protected boolean verifyDrawable(@NonNull Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.mMarginDrawable;
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable d = this.mMarginDrawable;
        if (d != null && d.isStateful()) {
            d.setState(this.getDrawableState());
        }
    }

    float distanceInfluenceForSnapDuration(float f) {
        f -= 0.5F;
        f *= 0.47123894F;
        return (float) Math.sin((double) f);
    }

    void smoothScrollTo(int x, int y) {
        this.smoothScrollTo(x, y, 0);
    }

    void smoothScrollTo(int x, int y, int velocity) {
        if (this.getChildCount() != 0) {
            boolean wasScrolling = this.mScroller != null && !this.mScroller.isFinished();
            int sx;
            if (wasScrolling) {
                sx = this.mIsScrollStarted ? this.mScroller.getCurrX() : this.mScroller.getStartX();
                this.mScroller.abortAnimation();
            } else {
                sx = this.getScrollX();
            }
            int sy = this.getScrollY();
            int dx = x - sx;
            int dy = y - sy;
            if (dx == 0 && dy == 0) {
                this.completeScroll(false);
                this.populate();
                this.setScrollState(0);
            } else {
                this.setScrollState(2);
                int width = this.getClientWidth();
                int halfWidth = width / 2;
                float distanceRatio = Math.min(1.0F, 1.0F * (float) Math.abs(dx) / (float) width);
                float distance = (float) halfWidth + (float) halfWidth * this.distanceInfluenceForSnapDuration(distanceRatio);
                velocity = Math.abs(velocity);
                int duration;
                if (velocity > 0) {
                    duration = 4 * Math.round(1000.0F * Math.abs(distance / (float) velocity));
                } else {
                    float pageWidth = (float) width * this.mAdapter.getPageWidth(this.mCurItem);
                    float pageDelta = (float) Math.abs(dx) / (pageWidth + (float) this.mPageMargin);
                    duration = (int) ((pageDelta + 1.0F) * 100.0F);
                }
                duration = Math.min(duration, 600);
                this.mIsScrollStarted = false;
                this.mScroller.startScroll(sx, sy, dx, dy, duration);
                this.postInvalidateOnAnimation();
            }
        }
    }

    ViewPager.ItemInfo addNewItem(int position, int index) {
        ViewPager.ItemInfo ii = new ViewPager.ItemInfo();
        ii.position = position;
        ii.object = this.mAdapter.instantiateItem(this, position);
        ii.widthFactor = this.mAdapter.getPageWidth(position);
        if (index >= 0 && index < this.mItems.size()) {
            this.mItems.add(index, ii);
        } else {
            this.mItems.add(ii);
        }
        return ii;
    }

    void dataSetChanged() {
        int adapterCount = this.mAdapter.getCount();
        this.mExpectedAdapterCount = adapterCount;
        boolean needPopulate = this.mItems.size() < this.mOffscreenPageLimit * 2 + 1 && this.mItems.size() < adapterCount;
        int newCurrItem = this.mCurItem;
        boolean isUpdating = false;
        for (int i = 0; i < this.mItems.size(); i++) {
            ViewPager.ItemInfo ii = (ViewPager.ItemInfo) this.mItems.get(i);
            int newPos = this.mAdapter.getItemPosition(ii.object);
            if (newPos != -1) {
                if (newPos == -2) {
                    this.mItems.remove(i);
                    i--;
                    if (!isUpdating) {
                        this.mAdapter.startUpdate(this);
                        isUpdating = true;
                    }
                    this.mAdapter.destroyItem(this, ii.position, ii.object);
                    needPopulate = true;
                    if (this.mCurItem == ii.position) {
                        newCurrItem = Math.max(0, Math.min(this.mCurItem, adapterCount - 1));
                    }
                } else if (ii.position != newPos) {
                    if (ii.position == this.mCurItem) {
                        newCurrItem = newPos;
                    }
                    ii.position = newPos;
                    needPopulate = true;
                }
            }
        }
        if (isUpdating) {
            this.mAdapter.finishUpdate(this);
        }
        this.mItems.sort(null);
        if (needPopulate) {
            int childCount = this.getChildCount();
            for (int ix = 0; ix < childCount; ix++) {
                View child = this.getChildAt(ix);
                ViewPager.LayoutParams lp = (ViewPager.LayoutParams) child.getLayoutParams();
                if (!lp.isDecor) {
                    lp.widthFactor = 0.0F;
                }
            }
            this.setCurrentItemInternal(newCurrItem, false, true);
            this.requestLayout();
        }
    }

    void populate() {
        this.populate(this.mCurItem);
    }

    void populate(int newCurrentItem) {
        ViewPager.ItemInfo oldCurInfo = null;
        if (this.mCurItem != newCurrentItem) {
            oldCurInfo = this.infoForPosition(this.mCurItem);
            this.mCurItem = newCurrentItem;
        }
        if (this.mAdapter == null) {
            this.sortChildDrawingOrder();
        } else if (this.mPopulatePending) {
            this.sortChildDrawingOrder();
        } else {
            this.mAdapter.startUpdate(this);
            int pageLimit = this.mOffscreenPageLimit;
            int startPos = Math.max(0, this.mCurItem - pageLimit);
            int N = this.mAdapter.getCount();
            int endPos = Math.min(N - 1, this.mCurItem + pageLimit);
            if (N != this.mExpectedAdapterCount) {
                throw new IllegalStateException("The application's PagerAdapter changed the adapter's contents without calling PagerAdapter#notifyDataSetChanged! Expected adapter item count: " + this.mExpectedAdapterCount + ", found: " + N + " Pager class: " + this.getClass() + " Problematic adapter: " + this.mAdapter.getClass());
            } else {
                int curIndex = -1;
                ViewPager.ItemInfo curItem = null;
                for (curIndex = 0; curIndex < this.mItems.size(); curIndex++) {
                    ViewPager.ItemInfo ii = (ViewPager.ItemInfo) this.mItems.get(curIndex);
                    if (ii.position >= this.mCurItem) {
                        if (ii.position == this.mCurItem) {
                            curItem = ii;
                        }
                        break;
                    }
                }
                if (curItem == null && N > 0) {
                    curItem = this.addNewItem(this.mCurItem, curIndex);
                }
                if (curItem != null) {
                    float extraWidthLeft = 0.0F;
                    int itemIndex = curIndex - 1;
                    ViewPager.ItemInfo ii = itemIndex >= 0 ? (ViewPager.ItemInfo) this.mItems.get(itemIndex) : null;
                    int clientWidth = this.getClientWidth();
                    float leftWidthNeeded = clientWidth <= 0 ? 0.0F : 2.0F - curItem.widthFactor + (float) this.getPaddingLeft() / (float) clientWidth;
                    for (int pos = this.mCurItem - 1; pos >= 0; pos--) {
                        if (extraWidthLeft >= leftWidthNeeded && pos < startPos) {
                            if (ii == null) {
                                break;
                            }
                            if (pos == ii.position && !ii.scrolling) {
                                this.mItems.remove(itemIndex);
                                this.mAdapter.destroyItem(this, pos, ii.object);
                                itemIndex--;
                                curIndex--;
                                ii = itemIndex >= 0 ? (ViewPager.ItemInfo) this.mItems.get(itemIndex) : null;
                            }
                        } else if (ii != null && pos == ii.position) {
                            extraWidthLeft += ii.widthFactor;
                            itemIndex--;
                            ii = itemIndex >= 0 ? (ViewPager.ItemInfo) this.mItems.get(itemIndex) : null;
                        } else {
                            ii = this.addNewItem(pos, itemIndex + 1);
                            extraWidthLeft += ii.widthFactor;
                            curIndex++;
                            ii = itemIndex >= 0 ? (ViewPager.ItemInfo) this.mItems.get(itemIndex) : null;
                        }
                    }
                    float extraWidthRight = curItem.widthFactor;
                    itemIndex = curIndex + 1;
                    if (extraWidthRight < 2.0F) {
                        ii = itemIndex < this.mItems.size() ? (ViewPager.ItemInfo) this.mItems.get(itemIndex) : null;
                        float rightWidthNeeded = clientWidth <= 0 ? 0.0F : (float) this.getPaddingRight() / (float) clientWidth + 2.0F;
                        for (int posx = this.mCurItem + 1; posx < N; posx++) {
                            if (extraWidthRight >= rightWidthNeeded && posx > endPos) {
                                if (ii == null) {
                                    break;
                                }
                                if (posx == ii.position && !ii.scrolling) {
                                    this.mItems.remove(itemIndex);
                                    this.mAdapter.destroyItem(this, posx, ii.object);
                                    ii = itemIndex < this.mItems.size() ? (ViewPager.ItemInfo) this.mItems.get(itemIndex) : null;
                                }
                            } else if (ii != null && posx == ii.position) {
                                extraWidthRight += ii.widthFactor;
                                itemIndex++;
                                ii = itemIndex < this.mItems.size() ? (ViewPager.ItemInfo) this.mItems.get(itemIndex) : null;
                            } else {
                                ii = this.addNewItem(posx, itemIndex);
                                itemIndex++;
                                extraWidthRight += ii.widthFactor;
                                ii = itemIndex < this.mItems.size() ? (ViewPager.ItemInfo) this.mItems.get(itemIndex) : null;
                            }
                        }
                    }
                    this.calculatePageOffsets(curItem, curIndex, oldCurInfo);
                    this.mAdapter.setPrimaryItem(this, this.mCurItem, curItem.object);
                }
                this.mAdapter.finishUpdate(this);
                int childCount = this.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View child = this.getChildAt(i);
                    ViewPager.LayoutParams lp = (ViewPager.LayoutParams) child.getLayoutParams();
                    lp.childIndex = i;
                    if (!lp.isDecor && lp.widthFactor == 0.0F) {
                        ViewPager.ItemInfo ii = this.infoForChild(child);
                        if (ii != null) {
                            lp.widthFactor = ii.widthFactor;
                            lp.position = ii.position;
                        }
                    }
                }
                this.sortChildDrawingOrder();
                if (this.hasFocus()) {
                    View currentFocused = this.findFocus();
                    ViewPager.ItemInfo ii = currentFocused != null ? this.infoForAnyChild(currentFocused) : null;
                    if (ii == null || ii.position != this.mCurItem) {
                        for (int ix = 0; ix < this.getChildCount(); ix++) {
                            View child = this.getChildAt(ix);
                            ii = this.infoForChild(child);
                            if (ii != null && ii.position == this.mCurItem && child.requestFocus(2)) {
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private void sortChildDrawingOrder() {
        if (this.mDrawingOrder != 0) {
            if (this.mDrawingOrderedChildren == null) {
                this.mDrawingOrderedChildren = new ArrayList();
            } else {
                this.mDrawingOrderedChildren.clear();
            }
            int childCount = this.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = this.getChildAt(i);
                this.mDrawingOrderedChildren.add(child);
            }
            this.mDrawingOrderedChildren.sort(sPositionComparator);
        }
    }

    private void calculatePageOffsets(ViewPager.ItemInfo curItem, int curIndex, ViewPager.ItemInfo oldCurInfo) {
        int N = this.mAdapter.getCount();
        int width = this.getClientWidth();
        float marginOffset = width > 0 ? (float) this.mPageMargin / (float) width : 0.0F;
        if (oldCurInfo != null) {
            int oldCurPosition = oldCurInfo.position;
            if (oldCurPosition < curItem.position) {
                int itemIndex = 0;
                ViewPager.ItemInfo ii = null;
                float offset = oldCurInfo.offset + oldCurInfo.widthFactor + marginOffset;
                for (int pos = oldCurPosition + 1; pos <= curItem.position && itemIndex < this.mItems.size(); pos++) {
                    ii = (ViewPager.ItemInfo) this.mItems.get(itemIndex);
                    while (pos > ii.position && itemIndex < this.mItems.size() - 1) {
                        ii = (ViewPager.ItemInfo) this.mItems.get(++itemIndex);
                    }
                    while (pos < ii.position) {
                        offset += this.mAdapter.getPageWidth(pos) + marginOffset;
                        pos++;
                    }
                    ii.offset = offset;
                    offset += ii.widthFactor + marginOffset;
                }
            } else if (oldCurPosition > curItem.position) {
                int itemIndex = this.mItems.size() - 1;
                ViewPager.ItemInfo ii = null;
                float offset = oldCurInfo.offset;
                for (int pos = oldCurPosition - 1; pos >= curItem.position && itemIndex >= 0; pos--) {
                    ii = (ViewPager.ItemInfo) this.mItems.get(itemIndex);
                    while (pos < ii.position && itemIndex > 0) {
                        ii = (ViewPager.ItemInfo) this.mItems.get(--itemIndex);
                    }
                    while (pos > ii.position) {
                        offset -= this.mAdapter.getPageWidth(pos) + marginOffset;
                        pos--;
                    }
                    offset -= ii.widthFactor + marginOffset;
                    ii.offset = offset;
                }
            }
        }
        int itemCount = this.mItems.size();
        float offset = curItem.offset;
        int pos = curItem.position - 1;
        this.mFirstOffset = curItem.position == 0 ? curItem.offset : -Float.MAX_VALUE;
        this.mLastOffset = curItem.position == N - 1 ? curItem.offset + curItem.widthFactor - 1.0F : Float.MAX_VALUE;
        for (int i = curIndex - 1; i >= 0; pos--) {
            ViewPager.ItemInfo ii = (ViewPager.ItemInfo) this.mItems.get(i);
            while (pos > ii.position) {
                offset -= this.mAdapter.getPageWidth(pos--) + marginOffset;
            }
            offset -= ii.widthFactor + marginOffset;
            ii.offset = offset;
            if (ii.position == 0) {
                this.mFirstOffset = offset;
            }
            i--;
        }
        offset = curItem.offset + curItem.widthFactor + marginOffset;
        pos = curItem.position + 1;
        for (int i = curIndex + 1; i < itemCount; pos++) {
            ViewPager.ItemInfo ii = (ViewPager.ItemInfo) this.mItems.get(i);
            while (pos < ii.position) {
                offset += this.mAdapter.getPageWidth(pos++) + marginOffset;
            }
            if (ii.position == N - 1) {
                this.mLastOffset = offset + ii.widthFactor - 1.0F;
            }
            ii.offset = offset;
            offset += ii.widthFactor + marginOffset;
            i++;
        }
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        int index = this.mDrawingOrder == 2 ? childCount - 1 - i : i;
        return ((ViewPager.LayoutParams) ((View) this.mDrawingOrderedChildren.get(index)).getLayoutParams()).childIndex;
    }

    @Override
    public void addView(@NonNull View child, int index, @NonNull ViewGroup.LayoutParams params) {
        if (!this.checkLayoutParams(params)) {
            params = this.generateLayoutParams(params);
        }
        ViewPager.LayoutParams lp = (ViewPager.LayoutParams) params;
        lp.isDecor = lp.isDecor | isDecorView(child);
        if (this.mInLayout) {
            if (lp.isDecor) {
                throw new IllegalStateException("Cannot add pager decor view during layout");
            }
            lp.needsMeasure = true;
            this.addViewInLayout(child, index, params);
        } else {
            super.addView(child, index, params);
        }
    }

    private static boolean isDecorView(@NonNull View view) {
        Class<?> clazz = view.getClass();
        return clazz.getAnnotation(ViewPager.DecorView.class) != null;
    }

    @Override
    public void removeView(@NonNull View view) {
        if (this.mInLayout) {
            this.removeViewInLayout(view);
        } else {
            super.removeView(view);
        }
    }

    ViewPager.ItemInfo infoForChild(View child) {
        for (int i = 0; i < this.mItems.size(); i++) {
            ViewPager.ItemInfo ii = (ViewPager.ItemInfo) this.mItems.get(i);
            if (this.mAdapter.isViewFromObject(child, ii.object)) {
                return ii;
            }
        }
        return null;
    }

    ViewPager.ItemInfo infoForAnyChild(View childx) {
        ViewParent parent;
        while ((parent = childx.getParent()) != this) {
            if (!(parent instanceof View childx)) {
                return null;
            }
        }
        return this.infoForChild(childx);
    }

    ViewPager.ItemInfo infoForPosition(int position) {
        for (int i = 0; i < this.mItems.size(); i++) {
            ViewPager.ItemInfo ii = (ViewPager.ItemInfo) this.mItems.get(i);
            if (ii.position == position) {
                return ii;
            }
        }
        return null;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        int measuredWidth = this.getMeasuredWidth();
        int maxGutterSize = measuredWidth / 10;
        this.mGutterSize = Math.min(maxGutterSize, this.mDefaultGutterSize);
        int childWidthSize = measuredWidth - this.getPaddingLeft() - this.getPaddingRight();
        int childHeightSize = this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom();
        int size = this.getChildCount();
        for (int i = 0; i < size; i++) {
            View child = this.getChildAt(i);
            if (child.getVisibility() != 8) {
                ViewPager.LayoutParams lp = (ViewPager.LayoutParams) child.getLayoutParams();
                if (lp != null && lp.isDecor) {
                    int hgrav = lp.gravity & 7;
                    int vgrav = lp.gravity & 112;
                    int widthMode = Integer.MIN_VALUE;
                    int heightMode = Integer.MIN_VALUE;
                    boolean consumeVertical = vgrav == 48 || vgrav == 80;
                    boolean consumeHorizontal = hgrav == 3 || hgrav == 5;
                    if (consumeVertical) {
                        widthMode = 1073741824;
                    } else if (consumeHorizontal) {
                        heightMode = 1073741824;
                    }
                    int widthSize = childWidthSize;
                    int heightSize = childHeightSize;
                    if (lp.width != -2) {
                        widthMode = 1073741824;
                        if (lp.width != -1) {
                            widthSize = lp.width;
                        }
                    }
                    if (lp.height != -2) {
                        heightMode = 1073741824;
                        if (lp.height != -1) {
                            heightSize = lp.height;
                        }
                    }
                    int widthSpec = MeasureSpec.makeMeasureSpec(widthSize, widthMode);
                    int heightSpec = MeasureSpec.makeMeasureSpec(heightSize, heightMode);
                    child.measure(widthSpec, heightSpec);
                    if (consumeVertical) {
                        childHeightSize -= child.getMeasuredHeight();
                    } else if (consumeHorizontal) {
                        childWidthSize -= child.getMeasuredWidth();
                    }
                }
            }
        }
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, 1073741824);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize, 1073741824);
        this.mInLayout = true;
        this.populate();
        this.mInLayout = false;
        size = this.getChildCount();
        for (int ix = 0; ix < size; ix++) {
            View child = this.getChildAt(ix);
            if (child.getVisibility() != 8) {
                ViewPager.LayoutParams lp = (ViewPager.LayoutParams) child.getLayoutParams();
                if (lp == null || !lp.isDecor) {
                    int widthSpec = MeasureSpec.makeMeasureSpec((int) ((float) childWidthSize * lp.widthFactor), 1073741824);
                    child.measure(widthSpec, childHeightMeasureSpec);
                }
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw) {
            this.recomputeScrollPosition(w, oldw, this.mPageMargin, this.mPageMargin);
        }
    }

    private void recomputeScrollPosition(int width, int oldWidth, int margin, int oldMargin) {
        if (oldWidth <= 0 || this.mItems.isEmpty()) {
            ViewPager.ItemInfo ii = this.infoForPosition(this.mCurItem);
            float scrollOffset = ii != null ? Math.min(ii.offset, this.mLastOffset) : 0.0F;
            int scrollPos = (int) (scrollOffset * (float) (width - this.getPaddingLeft() - this.getPaddingRight()));
            if (scrollPos != this.getScrollX()) {
                this.completeScroll(false);
                this.scrollTo(scrollPos, this.getScrollY());
            }
        } else if (!this.mScroller.isFinished()) {
            this.mScroller.setFinalX(this.getCurrentItem() * this.getClientWidth());
        } else {
            int widthWithMargin = width - this.getPaddingLeft() - this.getPaddingRight() + margin;
            int oldWidthWithMargin = oldWidth - this.getPaddingLeft() - this.getPaddingRight() + oldMargin;
            int xpos = this.getScrollX();
            float pageOffset = (float) xpos / (float) oldWidthWithMargin;
            int newOffsetPixels = (int) (pageOffset * (float) widthWithMargin);
            this.scrollTo(newOffsetPixels, this.getScrollY());
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = this.getChildCount();
        int width = r - l;
        int height = b - t;
        int paddingLeft = this.getPaddingLeft();
        int paddingTop = this.getPaddingTop();
        int paddingRight = this.getPaddingRight();
        int paddingBottom = this.getPaddingBottom();
        int scrollX = this.getScrollX();
        int decorCount = 0;
        for (int i = 0; i < count; i++) {
            View child = this.getChildAt(i);
            if (child.getVisibility() != 8) {
                ViewPager.LayoutParams lp = (ViewPager.LayoutParams) child.getLayoutParams();
                if (lp.isDecor) {
                    int hgrav = lp.gravity & 7;
                    int vgrav = lp.gravity & 112;
                    int childLeft;
                    switch(hgrav) {
                        case 1:
                            childLeft = Math.max((width - child.getMeasuredWidth()) / 2, paddingLeft);
                            break;
                        case 2:
                        case 4:
                        default:
                            childLeft = paddingLeft;
                            break;
                        case 3:
                            childLeft = paddingLeft;
                            paddingLeft += child.getMeasuredWidth();
                            break;
                        case 5:
                            childLeft = width - paddingRight - child.getMeasuredWidth();
                            paddingRight += child.getMeasuredWidth();
                    }
                    int childTop;
                    switch(vgrav) {
                        case 16:
                            childTop = Math.max((height - child.getMeasuredHeight()) / 2, paddingTop);
                            break;
                        case 48:
                            childTop = paddingTop;
                            paddingTop += child.getMeasuredHeight();
                            break;
                        case 80:
                            childTop = height - paddingBottom - child.getMeasuredHeight();
                            paddingBottom += child.getMeasuredHeight();
                            break;
                        default:
                            childTop = paddingTop;
                    }
                    childLeft += scrollX;
                    child.layout(childLeft, childTop, childLeft + child.getMeasuredWidth(), childTop + child.getMeasuredHeight());
                    decorCount++;
                }
            }
        }
        int childWidth = width - paddingLeft - paddingRight;
        for (int ix = 0; ix < count; ix++) {
            View child = this.getChildAt(ix);
            if (child.getVisibility() != 8) {
                ViewPager.LayoutParams lp = (ViewPager.LayoutParams) child.getLayoutParams();
                ViewPager.ItemInfo ii;
                if (!lp.isDecor && (ii = this.infoForChild(child)) != null) {
                    int loff = (int) ((float) childWidth * ii.offset);
                    int childLeft = paddingLeft + loff;
                    if (lp.needsMeasure) {
                        lp.needsMeasure = false;
                        int widthSpec = MeasureSpec.makeMeasureSpec((int) ((float) childWidth * lp.widthFactor), 1073741824);
                        int heightSpec = MeasureSpec.makeMeasureSpec(height - paddingTop - paddingBottom, 1073741824);
                        child.measure(widthSpec, heightSpec);
                    }
                    child.layout(childLeft, paddingTop, childLeft + child.getMeasuredWidth(), paddingTop + child.getMeasuredHeight());
                }
            }
        }
        this.mTopPageBounds = paddingTop;
        this.mBottomPageBounds = height - paddingBottom;
        this.mDecorChildCount = decorCount;
        if (this.mFirstLayout) {
            this.scrollToItem(this.mCurItem, false, 0, false);
        }
        this.mFirstLayout = false;
    }

    @Override
    public void computeScroll() {
        this.mIsScrollStarted = true;
        if (!this.mScroller.isFinished() && this.mScroller.computeScrollOffset()) {
            int oldX = this.getScrollX();
            int oldY = this.getScrollY();
            int x = this.mScroller.getCurrX();
            int y = this.mScroller.getCurrY();
            if (oldX != x || oldY != y) {
                this.scrollTo(x, y);
                if (!this.pageScrolled(x)) {
                    this.mScroller.abortAnimation();
                    this.scrollTo(0, y);
                }
            }
            this.postInvalidateOnAnimation();
        } else {
            this.completeScroll(true);
        }
    }

    private boolean pageScrolled(int xpos) {
        if (this.mItems.size() == 0) {
            if (this.mFirstLayout) {
                return false;
            } else {
                this.mCalledSuper = false;
                this.onPageScrolled(0, 0.0F, 0);
                if (!this.mCalledSuper) {
                    throw new IllegalStateException("onPageScrolled did not call superclass implementation");
                } else {
                    return false;
                }
            }
        } else {
            ViewPager.ItemInfo ii = this.infoForCurrentScrollPosition();
            int width = this.getClientWidth();
            int widthWithMargin = width + this.mPageMargin;
            float marginOffset = (float) this.mPageMargin / (float) width;
            int currentPage = ii.position;
            float pageOffset = ((float) xpos / (float) width - ii.offset) / (ii.widthFactor + marginOffset);
            int offsetPixels = (int) (pageOffset * (float) widthWithMargin);
            this.mCalledSuper = false;
            this.onPageScrolled(currentPage, pageOffset, offsetPixels);
            if (!this.mCalledSuper) {
                throw new IllegalStateException("onPageScrolled did not call superclass implementation");
            } else {
                return true;
            }
        }
    }

    @CallSuper
    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        if (this.mDecorChildCount > 0) {
            int scrollX = this.getScrollX();
            int paddingLeft = this.getPaddingLeft();
            int paddingRight = this.getPaddingRight();
            int width = this.getWidth();
            int childCount = this.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = this.getChildAt(i);
                ViewPager.LayoutParams lp = (ViewPager.LayoutParams) child.getLayoutParams();
                if (lp.isDecor) {
                    int hgrav = lp.gravity & 7;
                    int childLeft;
                    switch(hgrav) {
                        case 1:
                            childLeft = Math.max((width - child.getMeasuredWidth()) / 2, paddingLeft);
                            break;
                        case 2:
                        case 4:
                        default:
                            childLeft = paddingLeft;
                            break;
                        case 3:
                            childLeft = paddingLeft;
                            paddingLeft += child.getWidth();
                            break;
                        case 5:
                            childLeft = width - paddingRight - child.getMeasuredWidth();
                            paddingRight += child.getMeasuredWidth();
                    }
                    childLeft += scrollX;
                    int childOffset = childLeft - child.getLeft();
                    if (childOffset != 0) {
                        child.offsetLeftAndRight(childOffset);
                    }
                }
            }
        }
        this.dispatchOnPageScrolled(position, offset, offsetPixels);
        if (this.mPageTransformer != null) {
            int scrollX = this.getScrollX();
            int childCount = this.getChildCount();
            for (int ix = 0; ix < childCount; ix++) {
                View child = this.getChildAt(ix);
                ViewPager.LayoutParams lp = (ViewPager.LayoutParams) child.getLayoutParams();
                if (!lp.isDecor) {
                    float transformPos = (float) (child.getLeft() - scrollX) / (float) this.getClientWidth();
                    this.mPageTransformer.transformPage(child, transformPos);
                }
            }
        }
        this.mCalledSuper = true;
    }

    private void dispatchOnPageScrolled(int position, float offset, int offsetPixels) {
        if (this.mOnPageChangeListeners != null) {
            int i = 0;
            for (int z = this.mOnPageChangeListeners.size(); i < z; i++) {
                ViewPager.OnPageChangeListener listener = (ViewPager.OnPageChangeListener) this.mOnPageChangeListeners.get(i);
                if (listener != null) {
                    listener.onPageScrolled(position, offset, offsetPixels);
                }
            }
        }
        if (this.mInternalPageChangeListener != null) {
            this.mInternalPageChangeListener.onPageScrolled(position, offset, offsetPixels);
        }
    }

    private void dispatchOnPageSelected(int position) {
        if (this.mOnPageChangeListeners != null) {
            int i = 0;
            for (int z = this.mOnPageChangeListeners.size(); i < z; i++) {
                ViewPager.OnPageChangeListener listener = (ViewPager.OnPageChangeListener) this.mOnPageChangeListeners.get(i);
                if (listener != null) {
                    listener.onPageSelected(position);
                }
            }
        }
        if (this.mInternalPageChangeListener != null) {
            this.mInternalPageChangeListener.onPageSelected(position);
        }
    }

    private void dispatchOnScrollStateChanged(int state) {
        if (this.mOnPageChangeListeners != null) {
            int i = 0;
            for (int z = this.mOnPageChangeListeners.size(); i < z; i++) {
                ViewPager.OnPageChangeListener listener = (ViewPager.OnPageChangeListener) this.mOnPageChangeListeners.get(i);
                if (listener != null) {
                    listener.onPageScrollStateChanged(state);
                }
            }
        }
        if (this.mInternalPageChangeListener != null) {
            this.mInternalPageChangeListener.onPageScrollStateChanged(state);
        }
    }

    private void completeScroll(boolean postEvents) {
        boolean needPopulate = this.mScrollState == 2;
        if (needPopulate) {
            boolean wasScrolling = !this.mScroller.isFinished();
            if (wasScrolling) {
                this.mScroller.abortAnimation();
                int oldX = this.getScrollX();
                int oldY = this.getScrollY();
                int x = this.mScroller.getCurrX();
                int y = this.mScroller.getCurrY();
                if (oldX != x || oldY != y) {
                    this.scrollTo(x, y);
                    if (x != oldX) {
                        this.pageScrolled(x);
                    }
                }
            }
        }
        this.mPopulatePending = false;
        for (int i = 0; i < this.mItems.size(); i++) {
            ViewPager.ItemInfo ii = (ViewPager.ItemInfo) this.mItems.get(i);
            if (ii.scrolling) {
                needPopulate = true;
                ii.scrolling = false;
            }
        }
        if (needPopulate) {
            if (postEvents) {
                this.postOnAnimation(this.mEndScrollRunnable);
            } else {
                this.mEndScrollRunnable.run();
            }
        }
    }

    private boolean isGutterDrag(float x, float dx) {
        return x < (float) this.mGutterSize && dx > 0.0F || x > (float) (this.getWidth() - this.mGutterSize) && dx < 0.0F;
    }

    private void enableLayers(boolean enable) {
        int childCount = this.getChildCount();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action != 3 && action != 1) {
            if (action != 0) {
                if (this.mIsBeingDragged) {
                    return true;
                }
                if (this.mIsUnableToDrag) {
                    return false;
                }
            }
            switch(action) {
                case 0:
                    this.mLastMotionX = this.mInitialMotionX = ev.getX();
                    this.mLastMotionY = this.mInitialMotionY = ev.getY();
                    this.mActivePointerId = 0;
                    this.mIsUnableToDrag = false;
                    this.mIsScrollStarted = true;
                    this.mScroller.computeScrollOffset();
                    if (this.mScrollState == 2 && Math.abs(this.mScroller.getFinalX() - this.mScroller.getCurrX()) > this.mCloseEnough) {
                        this.mScroller.abortAnimation();
                        this.mPopulatePending = false;
                        this.populate();
                        this.mIsBeingDragged = true;
                        this.requestParentDisallowInterceptTouchEvent(true);
                        this.setScrollState(1);
                    } else {
                        this.completeScroll(false);
                        this.mIsBeingDragged = false;
                    }
                    break;
                case 2:
                    int activePointerId = this.mActivePointerId;
                    if (activePointerId != -1) {
                        float x = ev.getX();
                        float dx = x - this.mLastMotionX;
                        float xDiff = Math.abs(dx);
                        float y = ev.getY();
                        float yDiff = Math.abs(y - this.mInitialMotionY);
                        if (dx != 0.0F && !this.isGutterDrag(this.mLastMotionX, dx) && this.canScroll(this, false, (int) dx, (int) x, (int) y)) {
                            this.mLastMotionX = x;
                            this.mLastMotionY = y;
                            this.mIsUnableToDrag = true;
                            return false;
                        }
                        if (xDiff > (float) this.mTouchSlop && xDiff * 0.5F > yDiff) {
                            this.mIsBeingDragged = true;
                            this.requestParentDisallowInterceptTouchEvent(true);
                            this.setScrollState(1);
                            this.mLastMotionX = dx > 0.0F ? this.mInitialMotionX + (float) this.mTouchSlop : this.mInitialMotionX - (float) this.mTouchSlop;
                            this.mLastMotionY = y;
                        } else if (yDiff > (float) this.mTouchSlop) {
                            this.mIsUnableToDrag = true;
                        }
                        if (this.mIsBeingDragged && this.performDrag(x)) {
                            this.postInvalidateOnAnimation();
                        }
                    }
            }
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }
            this.mVelocityTracker.addMovement(ev);
            return this.mIsBeingDragged;
        } else {
            this.resetTouch();
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {
        if (this.mFakeDragging) {
            return true;
        } else if (this.mAdapter != null && this.mAdapter.getCount() != 0) {
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }
            this.mVelocityTracker.addMovement(ev);
            int action = ev.getAction();
            boolean needsInvalidate = false;
            switch(action) {
                case 0:
                    this.mScroller.abortAnimation();
                    this.mPopulatePending = false;
                    this.populate();
                    this.mLastMotionX = this.mInitialMotionX = ev.getX();
                    this.mLastMotionY = this.mInitialMotionY = ev.getY();
                    this.mActivePointerId = 0;
                    break;
                case 1:
                    if (this.mIsBeingDragged) {
                        VelocityTracker velocityTracker = this.mVelocityTracker;
                        velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                        int initialVelocity = (int) velocityTracker.getXVelocity();
                        this.mPopulatePending = true;
                        int width = this.getClientWidth();
                        int scrollX = this.getScrollX();
                        ViewPager.ItemInfo ii = this.infoForCurrentScrollPosition();
                        float marginOffset = (float) this.mPageMargin / (float) width;
                        int currentPage = ii.position;
                        float pageOffset = ((float) scrollX / (float) width - ii.offset) / (ii.widthFactor + marginOffset);
                        float x = ev.getX();
                        int totalDelta = (int) (x - this.mInitialMotionX);
                        int nextPage = this.determineTargetPage(currentPage, pageOffset, initialVelocity, totalDelta);
                        this.setCurrentItemInternal(nextPage, true, true, initialVelocity);
                        needsInvalidate = this.resetTouch();
                    }
                    break;
                case 2:
                    if (!this.mIsBeingDragged) {
                        float x = ev.getX();
                        float xDiff = Math.abs(x - this.mLastMotionX);
                        float y = ev.getY();
                        float yDiff = Math.abs(y - this.mLastMotionY);
                        if (xDiff > (float) this.mTouchSlop && xDiff > yDiff) {
                            this.mIsBeingDragged = true;
                            this.requestParentDisallowInterceptTouchEvent(true);
                            this.mLastMotionX = x - this.mInitialMotionX > 0.0F ? this.mInitialMotionX + (float) this.mTouchSlop : this.mInitialMotionX - (float) this.mTouchSlop;
                            this.mLastMotionY = y;
                            this.setScrollState(1);
                            ViewParent parent = this.getParent();
                            if (parent != null) {
                                parent.requestDisallowInterceptTouchEvent(true);
                            }
                        }
                    }
                    if (this.mIsBeingDragged) {
                        float x = ev.getX();
                        needsInvalidate = this.performDrag(x);
                    }
                    break;
                case 3:
                    if (this.mIsBeingDragged) {
                        this.scrollToItem(this.mCurItem, true, 0, false);
                        needsInvalidate = this.resetTouch();
                    }
            }
            if (needsInvalidate) {
                this.postInvalidateOnAnimation();
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean resetTouch() {
        this.mActivePointerId = -1;
        this.endDrag();
        this.mLeftEdge.onRelease();
        this.mRightEdge.onRelease();
        return this.mLeftEdge.isFinished() || this.mRightEdge.isFinished();
    }

    private void requestParentDisallowInterceptTouchEvent(boolean disallowIntercept) {
        ViewParent parent = this.getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(disallowIntercept);
        }
    }

    private boolean performDrag(float x) {
        boolean needsInvalidate = false;
        float deltaX = this.mLastMotionX - x;
        this.mLastMotionX = x;
        float oldScrollX = (float) this.getScrollX();
        float scrollX = oldScrollX + deltaX;
        int width = this.getClientWidth();
        float leftBound = (float) width * this.mFirstOffset;
        float rightBound = (float) width * this.mLastOffset;
        boolean leftAbsolute = true;
        boolean rightAbsolute = true;
        ViewPager.ItemInfo firstItem = (ViewPager.ItemInfo) this.mItems.get(0);
        ViewPager.ItemInfo lastItem = (ViewPager.ItemInfo) this.mItems.get(this.mItems.size() - 1);
        if (firstItem.position != 0) {
            leftAbsolute = false;
            leftBound = firstItem.offset * (float) width;
        }
        if (lastItem.position != this.mAdapter.getCount() - 1) {
            rightAbsolute = false;
            rightBound = lastItem.offset * (float) width;
        }
        if (scrollX < leftBound) {
            if (leftAbsolute) {
                float over = leftBound - scrollX;
                this.mLeftEdge.onPull(Math.abs(over) / (float) width);
                needsInvalidate = true;
            }
            scrollX = leftBound;
        } else if (scrollX > rightBound) {
            if (rightAbsolute) {
                float over = scrollX - rightBound;
                this.mRightEdge.onPull(Math.abs(over) / (float) width);
                needsInvalidate = true;
            }
            scrollX = rightBound;
        }
        this.mLastMotionX += scrollX - (float) ((int) scrollX);
        this.scrollTo((int) scrollX, this.getScrollY());
        this.pageScrolled((int) scrollX);
        return needsInvalidate;
    }

    private ViewPager.ItemInfo infoForCurrentScrollPosition() {
        int width = this.getClientWidth();
        float scrollOffset = width > 0 ? (float) this.getScrollX() / (float) width : 0.0F;
        float marginOffset = width > 0 ? (float) this.mPageMargin / (float) width : 0.0F;
        int lastPos = -1;
        float lastOffset = 0.0F;
        float lastWidth = 0.0F;
        boolean first = true;
        ViewPager.ItemInfo lastItem = null;
        for (int i = 0; i < this.mItems.size(); i++) {
            ViewPager.ItemInfo ii = (ViewPager.ItemInfo) this.mItems.get(i);
            if (!first && ii.position != lastPos + 1) {
                ii = this.mTempItem;
                ii.offset = lastOffset + lastWidth + marginOffset;
                ii.position = lastPos + 1;
                ii.widthFactor = this.mAdapter.getPageWidth(ii.position);
                i--;
            }
            float offset = ii.offset;
            float rightBound = offset + ii.widthFactor + marginOffset;
            if (!first && !(scrollOffset >= offset)) {
                return lastItem;
            }
            if (scrollOffset < rightBound || i == this.mItems.size() - 1) {
                return ii;
            }
            first = false;
            lastPos = ii.position;
            lastOffset = offset;
            lastWidth = ii.widthFactor;
            lastItem = ii;
        }
        return lastItem;
    }

    private int determineTargetPage(int currentPage, float pageOffset, int velocity, int deltaX) {
        int targetPage;
        if (Math.abs(deltaX) > this.mFlingDistance && Math.abs(velocity) > this.mMinimumVelocity) {
            targetPage = velocity > 0 ? currentPage : currentPage + 1;
        } else {
            float truncator = currentPage >= this.mCurItem ? 0.4F : 0.6F;
            targetPage = currentPage + (int) (pageOffset + truncator);
        }
        if (this.mItems.size() > 0) {
            ViewPager.ItemInfo firstItem = (ViewPager.ItemInfo) this.mItems.get(0);
            ViewPager.ItemInfo lastItem = (ViewPager.ItemInfo) this.mItems.get(this.mItems.size() - 1);
            targetPage = Math.max(firstItem.position, Math.min(targetPage, lastItem.position));
        }
        return targetPage;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        super.draw(canvas);
        boolean needsInvalidate = false;
        int overScrollMode = this.getOverScrollMode();
        if (overScrollMode == 0 || overScrollMode == 1 && this.mAdapter != null && this.mAdapter.getCount() > 1) {
            if (!this.mLeftEdge.isFinished()) {
                int restoreCount = canvas.save();
                int height = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
                int width = this.getWidth();
                canvas.rotate(270.0F);
                canvas.translate((float) (-height + this.getPaddingTop()), this.mFirstOffset * (float) width);
                this.mLeftEdge.setSize(height, width);
                needsInvalidate |= this.mLeftEdge.draw(canvas);
                canvas.restoreToCount(restoreCount);
            }
            if (!this.mRightEdge.isFinished()) {
                int restoreCount = canvas.save();
                int width = this.getWidth();
                int height = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
                canvas.rotate(90.0F);
                canvas.translate((float) (-this.getPaddingTop()), -(this.mLastOffset + 1.0F) * (float) width);
                this.mRightEdge.setSize(height, width);
                needsInvalidate |= this.mRightEdge.draw(canvas);
                canvas.restoreToCount(restoreCount);
            }
        } else {
            this.mLeftEdge.finish();
            this.mRightEdge.finish();
        }
        if (needsInvalidate) {
            this.postInvalidateOnAnimation();
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (this.mPageMargin > 0 && this.mMarginDrawable != null && this.mItems.size() > 0 && this.mAdapter != null) {
            int scrollX = this.getScrollX();
            int width = this.getWidth();
            float marginOffset = (float) this.mPageMargin / (float) width;
            int itemIndex = 0;
            ViewPager.ItemInfo ii = (ViewPager.ItemInfo) this.mItems.get(0);
            float offset = ii.offset;
            int itemCount = this.mItems.size();
            int firstPos = ii.position;
            int lastPos = ((ViewPager.ItemInfo) this.mItems.get(itemCount - 1)).position;
            for (int pos = firstPos; pos < lastPos; pos++) {
                while (pos > ii.position && itemIndex < itemCount) {
                    ii = (ViewPager.ItemInfo) this.mItems.get(++itemIndex);
                }
                float drawAt;
                if (pos == ii.position) {
                    drawAt = (ii.offset + ii.widthFactor) * (float) width;
                    offset = ii.offset + ii.widthFactor + marginOffset;
                } else {
                    float widthFactor = this.mAdapter.getPageWidth(pos);
                    drawAt = (offset + widthFactor) * (float) width;
                    offset += widthFactor + marginOffset;
                }
                if (drawAt + (float) this.mPageMargin > (float) scrollX) {
                    this.mMarginDrawable.setBounds(Math.round(drawAt), this.mTopPageBounds, Math.round(drawAt + (float) this.mPageMargin), this.mBottomPageBounds);
                    this.mMarginDrawable.draw(canvas);
                }
                if (drawAt > (float) (scrollX + width)) {
                    break;
                }
            }
        }
    }

    public boolean beginFakeDrag() {
        if (this.mIsBeingDragged) {
            return false;
        } else {
            this.mFakeDragging = true;
            this.setScrollState(1);
            this.mInitialMotionX = this.mLastMotionX = 0.0F;
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            } else {
                this.mVelocityTracker.clear();
            }
            long time = Core.timeNanos();
            MotionEvent ev = MotionEvent.obtain(time, 0, 0.0F, 0.0F, 0);
            this.mVelocityTracker.addMovement(ev);
            ev.recycle();
            this.mFakeDragBeginTime = time;
            return true;
        }
    }

    public void endFakeDrag() {
        if (!this.mFakeDragging) {
            throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
        } else {
            if (this.mAdapter != null) {
                VelocityTracker velocityTracker = this.mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                int initialVelocity = (int) velocityTracker.getXVelocity();
                this.mPopulatePending = true;
                int width = this.getClientWidth();
                int scrollX = this.getScrollX();
                ViewPager.ItemInfo ii = this.infoForCurrentScrollPosition();
                int currentPage = ii.position;
                float pageOffset = ((float) scrollX / (float) width - ii.offset) / ii.widthFactor;
                int totalDelta = (int) (this.mLastMotionX - this.mInitialMotionX);
                int nextPage = this.determineTargetPage(currentPage, pageOffset, initialVelocity, totalDelta);
                this.setCurrentItemInternal(nextPage, true, true, initialVelocity);
            }
            this.endDrag();
            this.mFakeDragging = false;
        }
    }

    public void fakeDragBy(float xOffset) {
        if (!this.mFakeDragging) {
            throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
        } else if (this.mAdapter != null) {
            this.mLastMotionX += xOffset;
            float oldScrollX = (float) this.getScrollX();
            float scrollX = oldScrollX - xOffset;
            int width = this.getClientWidth();
            float leftBound = (float) width * this.mFirstOffset;
            float rightBound = (float) width * this.mLastOffset;
            ViewPager.ItemInfo firstItem = (ViewPager.ItemInfo) this.mItems.get(0);
            ViewPager.ItemInfo lastItem = (ViewPager.ItemInfo) this.mItems.get(this.mItems.size() - 1);
            if (firstItem.position != 0) {
                leftBound = firstItem.offset * (float) width;
            }
            if (lastItem.position != this.mAdapter.getCount() - 1) {
                rightBound = lastItem.offset * (float) width;
            }
            if (scrollX < leftBound) {
                scrollX = leftBound;
            } else if (scrollX > rightBound) {
                scrollX = rightBound;
            }
            this.mLastMotionX += scrollX - (float) ((int) scrollX);
            this.scrollTo((int) scrollX, this.getScrollY());
            this.pageScrolled((int) scrollX);
            long time = Core.timeNanos();
            MotionEvent ev = MotionEvent.obtain(time, 2, this.mLastMotionX, 0.0F, 0);
            this.mVelocityTracker.addMovement(ev);
            ev.recycle();
        }
    }

    public boolean isFakeDragging() {
        return this.mFakeDragging;
    }

    private void endDrag() {
        this.mIsBeingDragged = false;
        this.mIsUnableToDrag = false;
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        if (this.mAdapter == null) {
            return false;
        } else {
            int width = this.getClientWidth();
            int scrollX = this.getScrollX();
            if (direction < 0) {
                return scrollX > (int) ((float) width * this.mFirstOffset);
            } else {
                return direction > 0 ? scrollX < (int) ((float) width * this.mLastOffset) : false;
            }
        }
    }

    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v instanceof ViewGroup group) {
            int scrollX = v.getScrollX();
            int scrollY = v.getScrollY();
            int count = group.getChildCount();
            for (int i = count - 1; i >= 0; i--) {
                View child = group.getChildAt(i);
                if (x + scrollX >= child.getLeft() && x + scrollX < child.getRight() && y + scrollY >= child.getTop() && y + scrollY < child.getBottom() && this.canScroll(child, true, dx, x + scrollX - child.getLeft(), y + scrollY - child.getTop())) {
                    return true;
                }
            }
        }
        return checkV && v.canScrollHorizontally(-dx);
    }

    @Override
    public boolean dispatchKeyEvent(@NonNull KeyEvent event) {
        return super.dispatchKeyEvent(event) || this.executeKeyEvent(event);
    }

    public boolean executeKeyEvent(@NonNull KeyEvent event) {
        boolean handled = false;
        if (event.getAction() == 0) {
            switch(event.getKeyCode()) {
                case 258:
                    if (event.hasNoModifiers()) {
                        handled = this.arrowScroll(2);
                    } else if (event.hasModifiers(1)) {
                        handled = this.arrowScroll(1);
                    }
                    break;
                case 262:
                    if (event.hasModifiers(4)) {
                        handled = this.pageRight();
                    } else {
                        handled = this.arrowScroll(66);
                    }
                    break;
                case 263:
                    if (event.hasModifiers(4)) {
                        handled = this.pageLeft();
                    } else {
                        handled = this.arrowScroll(17);
                    }
            }
        }
        return handled;
    }

    public boolean arrowScroll(int direction) {
        View currentFocused = this.findFocus();
        if (currentFocused == this) {
            currentFocused = null;
        } else if (currentFocused != null) {
            boolean isChild = false;
            for (ViewParent parent = currentFocused.getParent(); parent instanceof ViewGroup; parent = parent.getParent()) {
                if (parent == this) {
                    isChild = true;
                    break;
                }
            }
            if (!isChild) {
                StringBuilder sb = new StringBuilder();
                sb.append(currentFocused.getClass().getSimpleName());
                for (ViewParent parentx = currentFocused.getParent(); parentx instanceof ViewGroup; parentx = parentx.getParent()) {
                    sb.append(" => ").append(parentx.getClass().getSimpleName());
                }
                ModernUI.LOGGER.warn(MARKER, "arrowScroll tried to find focus based on non-child current focused view " + sb);
                currentFocused = null;
            }
        }
        boolean handled = false;
        View nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused, direction);
        if (nextFocused != null && nextFocused != currentFocused) {
            if (direction == 17) {
                int nextLeft = this.getChildRectInPagerCoordinates(this.mTempRect, nextFocused).left;
                int currLeft = this.getChildRectInPagerCoordinates(this.mTempRect, currentFocused).left;
                if (currentFocused != null && nextLeft >= currLeft) {
                    handled = this.pageLeft();
                } else {
                    handled = nextFocused.requestFocus();
                }
            } else if (direction == 66) {
                int nextLeft = this.getChildRectInPagerCoordinates(this.mTempRect, nextFocused).left;
                int currLeft = this.getChildRectInPagerCoordinates(this.mTempRect, currentFocused).left;
                if (currentFocused != null && nextLeft <= currLeft) {
                    handled = this.pageRight();
                } else {
                    handled = nextFocused.requestFocus();
                }
            }
        } else if (direction == 17 || direction == 1) {
            handled = this.pageLeft();
        } else if (direction == 66 || direction == 2) {
            handled = this.pageRight();
        }
        if (handled) {
        }
        return handled;
    }

    private Rect getChildRectInPagerCoordinates(Rect outRect, View child) {
        if (outRect == null) {
            outRect = new Rect();
        }
        if (child == null) {
            outRect.set(0, 0, 0, 0);
            return outRect;
        } else {
            outRect.left = child.getLeft();
            outRect.right = child.getRight();
            outRect.top = child.getTop();
            outRect.bottom = child.getBottom();
            ViewParent parent = child.getParent();
            while (parent != this && parent instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) parent;
                outRect.left = outRect.left + group.getLeft();
                outRect.right = outRect.right + group.getRight();
                outRect.top = outRect.top + group.getTop();
                outRect.bottom = outRect.bottom + group.getBottom();
                parent = group.getParent();
            }
            return outRect;
        }
    }

    boolean pageLeft() {
        if (this.mCurItem > 0) {
            this.setCurrentItem(this.mCurItem - 1, true);
            return true;
        } else {
            return false;
        }
    }

    boolean pageRight() {
        if (this.mAdapter != null && this.mCurItem < this.mAdapter.getCount() - 1) {
            this.setCurrentItem(this.mCurItem + 1, true);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void addFocusables(@NonNull ArrayList<View> views, int direction, int focusableMode) {
        int focusableCount = views.size();
        int descendantFocusability = this.getDescendantFocusability();
        if (descendantFocusability != 393216) {
            for (int i = 0; i < this.getChildCount(); i++) {
                View child = this.getChildAt(i);
                if (child.getVisibility() == 0) {
                    ViewPager.ItemInfo ii = this.infoForChild(child);
                    if (ii != null && ii.position == this.mCurItem) {
                        child.addFocusables(views, direction, focusableMode);
                    }
                }
            }
        }
        if (descendantFocusability != 262144 || focusableCount == views.size()) {
            if (!this.isFocusable()) {
                return;
            }
            if ((focusableMode & 1) == 1 && this.isInTouchMode() && !this.isFocusableInTouchMode()) {
                return;
            }
            views.add(this);
        }
    }

    @Override
    public void addTouchables(@NonNull ArrayList<View> views) {
        for (int i = 0; i < this.getChildCount(); i++) {
            View child = this.getChildAt(i);
            if (child.getVisibility() == 0) {
                ViewPager.ItemInfo ii = this.infoForChild(child);
                if (ii != null && ii.position == this.mCurItem) {
                    child.addTouchables(views);
                }
            }
        }
    }

    @Override
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        int count = this.getChildCount();
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
        for (int i = index; i != end; i += increment) {
            View child = this.getChildAt(i);
            if (child.getVisibility() == 0) {
                ViewPager.ItemInfo ii = this.infoForChild(child);
                if (ii != null && ii.position == this.mCurItem && child.requestFocus(direction, previouslyFocusedRect)) {
                    return true;
                }
            }
        }
        return false;
    }

    @NonNull
    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new ViewPager.LayoutParams();
    }

    @NonNull
    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(@NonNull ViewGroup.LayoutParams p) {
        return this.generateDefaultLayoutParams();
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof ViewPager.LayoutParams && super.checkLayoutParams(p);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE })
    @Inherited
    public @interface DecorView {
    }

    static class ItemInfo implements Comparable<ViewPager.ItemInfo> {

        Object object;

        int position;

        boolean scrolling;

        float widthFactor;

        float offset;

        public int compareTo(@NonNull ViewPager.ItemInfo o) {
            return this.position - o.position;
        }
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {

        public boolean isDecor;

        public int gravity;

        float widthFactor = 0.0F;

        boolean needsMeasure;

        int position;

        int childIndex;

        public LayoutParams() {
            super(-1, -1);
        }
    }

    public interface OnAdapterChangeListener {

        void onAdapterChanged(@NonNull ViewPager var1, @Nullable PagerAdapter var2, @Nullable PagerAdapter var3);
    }

    public interface OnPageChangeListener {

        default void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        default void onPageSelected(int position) {
        }

        default void onPageScrollStateChanged(int state) {
        }
    }

    public interface PageTransformer {

        void transformPage(@NonNull View var1, float var2);
    }

    private class PagerObserver implements DataSetObserver {

        PagerObserver() {
        }

        @Override
        public void onChanged() {
            ViewPager.this.dataSetChanged();
        }

        @Override
        public void onInvalidated() {
            ViewPager.this.dataSetChanged();
        }
    }

    static class ViewPositionComparator implements Comparator<View> {

        public int compare(View lhs, View rhs) {
            ViewPager.LayoutParams llp = (ViewPager.LayoutParams) lhs.getLayoutParams();
            ViewPager.LayoutParams rlp = (ViewPager.LayoutParams) rhs.getLayoutParams();
            if (llp.isDecor != rlp.isDecor) {
                return llp.isDecor ? 1 : -1;
            } else {
                return llp.position - rlp.position;
            }
        }
    }
}