package icyllis.modernui.widget;

import icyllis.modernui.ModernUI;
import icyllis.modernui.animation.TimeInterpolator;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.graphics.drawable.ShapeDrawable;
import icyllis.modernui.resources.SystemTheme;
import icyllis.modernui.util.LongSparseArray;
import icyllis.modernui.util.SparseArray;
import icyllis.modernui.util.SparseBooleanArray;
import icyllis.modernui.util.StateSet;
import icyllis.modernui.view.ActionMode;
import icyllis.modernui.view.ContextMenu;
import icyllis.modernui.view.KeyEvent;
import icyllis.modernui.view.Menu;
import icyllis.modernui.view.MenuItem;
import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.view.VelocityTracker;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewConfiguration;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.view.ViewParent;
import it.unimi.dsi.fastutil.longs.Long2IntMaps;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2IntMap.Entry;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.ApiStatus.Internal;

public abstract class AbsListView extends AdapterView<ListAdapter> implements Filter.FilterListener {

    private static final Marker MARKER = MarkerManager.getMarker("AbsListView");

    public static final int TRANSCRIPT_MODE_DISABLED = 0;

    public static final int TRANSCRIPT_MODE_NORMAL = 1;

    public static final int TRANSCRIPT_MODE_ALWAYS_SCROLL = 2;

    static final int TOUCH_MODE_REST = -1;

    static final int TOUCH_MODE_DOWN = 0;

    static final int TOUCH_MODE_TAP = 1;

    static final int TOUCH_MODE_DONE_WAITING = 2;

    static final int TOUCH_MODE_SCROLL = 3;

    static final int TOUCH_MODE_FLING = 4;

    static final int TOUCH_MODE_OVERSCROLL = 5;

    static final int TOUCH_MODE_OVERFLING = 6;

    static final int LAYOUT_NORMAL = 0;

    static final int LAYOUT_FORCE_TOP = 1;

    static final int LAYOUT_SET_SELECTION = 2;

    static final int LAYOUT_FORCE_BOTTOM = 3;

    static final int LAYOUT_SPECIFIC = 4;

    static final int LAYOUT_SYNC = 5;

    static final int LAYOUT_MOVE_SELECTION = 6;

    public static final int CHOICE_MODE_NONE = 0;

    public static final int CHOICE_MODE_SINGLE = 1;

    public static final int CHOICE_MODE_MULTIPLE = 2;

    public static final int CHOICE_MODE_MULTIPLE_MODAL = 3;

    int mChoiceMode = 0;

    ActionMode mChoiceActionMode;

    AbsListView.MultiChoiceModeWrapper mMultiChoiceModeCallback;

    int mCheckedItemCount;

    SparseBooleanArray mCheckStates;

    Long2IntOpenHashMap mCheckedIdStates;

    int mLayoutMode = 0;

    AdapterView<ListAdapter>.AdapterDataSetObserver mDataSetObserver;

    ListAdapter mAdapter;

    boolean mAdapterHasStableIds;

    boolean mDrawSelectorOnTop = false;

    Drawable mSelector;

    int mSelectorPosition = -1;

    Rect mSelectorRect = new Rect();

    final AbsListView.RecycleBin mRecycler = new AbsListView.RecycleBin();

    int mSelectionLeftPadding = 0;

    int mSelectionTopPadding = 0;

    int mSelectionRightPadding = 0;

    int mSelectionBottomPadding = 0;

    Rect mListPadding = new Rect();

    int mWidthMeasureSpec = 0;

    View mScrollUp;

    View mScrollDown;

    int mMotionPosition;

    int mMotionViewOriginalTop;

    int mMotionViewNewTop;

    int mMotionX;

    int mMotionY;

    int mTouchMode = -1;

    int mLastY;

    int mMotionCorrection;

    private VelocityTracker mVelocityTracker;

    private AbsListView.FlingRunnable mFlingRunnable;

    AbsListView.PositionScroller mPositionScroller;

    int mSelectedTop = 0;

    boolean mStackFromBottom;

    private AbsListView.OnScrollListener mOnScrollListener;

    private boolean mSmoothScrollbarEnabled = true;

    private Rect mTouchFrame;

    int mResurrectToPosition = -1;

    private ContextMenu.ContextMenuInfo mContextMenuInfo = null;

    int mOverscrollMax;

    static final int OVERSCROLL_LIMIT_DIVISOR = 3;

    private static final int CHECK_POSITION_SEARCH_DISTANCE = 20;

    private static final int TOUCH_MODE_UNKNOWN = -1;

    private static final int TOUCH_MODE_ON = 0;

    private static final int TOUCH_MODE_OFF = 1;

    private int mLastTouchMode = -1;

    private AbsListView.CheckForLongPress mPendingCheckForLongPress;

    private AbsListView.CheckForTap mPendingCheckForTap;

    private AbsListView.CheckForKeyLongPress mPendingCheckForKeyLongPress;

    private AbsListView.PerformClick mPerformClick;

    private Runnable mTouchModeReset;

    private boolean mHasPerformedLongPress;

    private int mTranscriptMode = 0;

    private boolean mIsChildViewEnabled;

    private int[] mSelectorState;

    private int mLastScrollState = 0;

    private int mTouchSlop;

    private float mDensityScale;

    private final float mVerticalScrollFactor;

    Runnable mPositionScrollAfterLayout;

    private final int mMinimumVelocity;

    private final int mMaximumVelocity;

    private float mVelocityScale = 1.0F;

    final boolean[] mIsScrap = new boolean[1];

    private final int[] mScrollOffset = new int[2];

    private final int[] mScrollConsumed = new int[2];

    private final float[] mTmpPoint = new float[2];

    private int mNestedYOffset = 0;

    int mOverscrollDistance;

    int mOverflingDistance;

    @NonNull
    private final EdgeEffect mEdgeGlowTop;

    @NonNull
    private final EdgeEffect mEdgeGlowBottom;

    private int mFirstPositionDistanceGuess;

    private int mLastPositionDistanceGuess;

    private int mDirection = 0;

    private boolean mForceTranscriptScroll;

    private int mLastHandledItemCount;

    private boolean mIsDetaching;

    public AbsListView(Context context) {
        super(context);
        this.mEdgeGlowBottom = new EdgeEffect();
        this.mEdgeGlowTop = new EdgeEffect();
        this.setClickable(true);
        this.setFocusableInTouchMode(true);
        this.setWillNotDraw(false);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        this.mTouchSlop = configuration.getScaledTouchSlop();
        this.mVerticalScrollFactor = configuration.getScaledVerticalScrollFactor();
        this.mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        this.mOverscrollDistance = configuration.getScaledOverscrollDistance();
        this.mOverflingDistance = configuration.getScaledOverflingDistance();
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

    public void setAdapter(@Nullable ListAdapter adapter) {
        if (adapter != null) {
            this.mAdapterHasStableIds = this.mAdapter.hasStableIds();
            if (this.mChoiceMode != 0 && this.mAdapterHasStableIds && this.mCheckedIdStates == null) {
                this.mCheckedIdStates = new Long2IntOpenHashMap();
            }
        }
        this.clearChoices();
    }

    public int getCheckedItemCount() {
        return this.mCheckedItemCount;
    }

    public boolean isItemChecked(int position) {
        return this.mChoiceMode != 0 && this.mCheckStates != null ? this.mCheckStates.get(position) : false;
    }

    public int getCheckedItemPosition() {
        return this.mChoiceMode == 1 && this.mCheckStates != null && this.mCheckStates.size() == 1 ? this.mCheckStates.keyAt(0) : -1;
    }

    public SparseBooleanArray getCheckedItemPositions() {
        return this.mChoiceMode != 0 ? this.mCheckStates : null;
    }

    public long[] getCheckedItemIds() {
        return this.mChoiceMode != 0 && this.mCheckedIdStates != null && this.mAdapter != null ? this.mCheckedIdStates.keySet().toLongArray() : new long[0];
    }

    public void clearChoices() {
        if (this.mCheckStates != null) {
            this.mCheckStates.clear();
        }
        if (this.mCheckedIdStates != null) {
            this.mCheckedIdStates.clear();
        }
        this.mCheckedItemCount = 0;
    }

    public void setItemChecked(int position, boolean value) {
        if (this.mChoiceMode != 0) {
            if (value && this.mChoiceMode == 3 && this.mChoiceActionMode == null) {
                if (this.mMultiChoiceModeCallback == null || !this.mMultiChoiceModeCallback.hasWrappedCallback()) {
                    throw new IllegalStateException("AbsListView: attempted to start selection mode for CHOICE_MODE_MULTIPLE_MODAL but no choice mode callback was supplied. Call setMultiChoiceModeListener to set a callback.");
                }
                this.mChoiceActionMode = this.startActionMode(this.mMultiChoiceModeCallback);
            }
            boolean itemCheckChanged;
            if (this.mChoiceMode != 2 && this.mChoiceMode != 3) {
                boolean updateIds = this.mCheckedIdStates != null && this.mAdapter.hasStableIds();
                itemCheckChanged = this.isItemChecked(position) != value;
                if (value || this.isItemChecked(position)) {
                    this.mCheckStates.clear();
                    if (updateIds) {
                        this.mCheckedIdStates.clear();
                    }
                }
                if (value) {
                    this.mCheckStates.put(position, true);
                    if (updateIds) {
                        this.mCheckedIdStates.put(this.mAdapter.getItemId(position), position);
                    }
                    this.mCheckedItemCount = 1;
                } else if (this.mCheckStates.size() == 0 || !this.mCheckStates.valueAt(0)) {
                    this.mCheckedItemCount = 0;
                }
            } else {
                boolean oldValue = this.mCheckStates.get(position);
                this.mCheckStates.put(position, value);
                if (this.mCheckedIdStates != null && this.mAdapter.hasStableIds()) {
                    if (value) {
                        this.mCheckedIdStates.put(this.mAdapter.getItemId(position), position);
                    } else {
                        this.mCheckedIdStates.remove(this.mAdapter.getItemId(position));
                    }
                }
                itemCheckChanged = oldValue != value;
                if (itemCheckChanged) {
                    if (value) {
                        this.mCheckedItemCount++;
                    } else {
                        this.mCheckedItemCount--;
                    }
                }
                if (this.mChoiceActionMode != null) {
                    long id = this.mAdapter.getItemId(position);
                    this.mMultiChoiceModeCallback.onItemCheckedStateChanged(this.mChoiceActionMode, position, id, value);
                }
            }
            if (!this.mInLayout && !this.mBlockLayoutRequests && itemCheckChanged) {
                this.mDataChanged = true;
                this.rememberSyncState();
                this.requestLayout();
            }
        }
    }

    @Override
    public boolean performItemClick(View view, int position, long id) {
        boolean handled = false;
        boolean dispatchItemClick = true;
        if (this.mChoiceMode != 0) {
            handled = true;
            boolean checkedStateChanged = false;
            if (this.mChoiceMode != 2 && (this.mChoiceMode != 3 || this.mChoiceActionMode == null)) {
                if (this.mChoiceMode == 1) {
                    boolean checked = !this.mCheckStates.get(position, false);
                    if (checked) {
                        this.mCheckStates.clear();
                        this.mCheckStates.put(position, true);
                        if (this.mCheckedIdStates != null && this.mAdapter.hasStableIds()) {
                            this.mCheckedIdStates.clear();
                            this.mCheckedIdStates.put(this.mAdapter.getItemId(position), position);
                        }
                        this.mCheckedItemCount = 1;
                    } else if (this.mCheckStates.size() == 0 || !this.mCheckStates.valueAt(0)) {
                        this.mCheckedItemCount = 0;
                    }
                    checkedStateChanged = true;
                }
            } else {
                boolean checked = !this.mCheckStates.get(position, false);
                this.mCheckStates.put(position, checked);
                if (this.mCheckedIdStates != null && this.mAdapter.hasStableIds()) {
                    if (checked) {
                        this.mCheckedIdStates.put(this.mAdapter.getItemId(position), position);
                    } else {
                        this.mCheckedIdStates.remove(this.mAdapter.getItemId(position));
                    }
                }
                if (checked) {
                    this.mCheckedItemCount++;
                } else {
                    this.mCheckedItemCount--;
                }
                if (this.mChoiceActionMode != null) {
                    this.mMultiChoiceModeCallback.onItemCheckedStateChanged(this.mChoiceActionMode, position, id, checked);
                    dispatchItemClick = false;
                }
                checkedStateChanged = true;
            }
            if (checkedStateChanged) {
                this.updateOnScreenCheckedViews();
            }
        }
        if (dispatchItemClick) {
            handled |= super.performItemClick(view, position, id);
        }
        return handled;
    }

    private void updateOnScreenCheckedViews() {
        int firstPos = this.mFirstPosition;
        int count = this.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = this.getChildAt(i);
            int position = firstPos + i;
            if (child instanceof Checkable) {
                ((Checkable) child).setChecked(this.mCheckStates.get(position));
            } else {
                child.setActivated(this.mCheckStates.get(position));
            }
        }
    }

    public int getChoiceMode() {
        return this.mChoiceMode;
    }

    public void setChoiceMode(int choiceMode) {
        this.mChoiceMode = choiceMode;
        if (this.mChoiceActionMode != null) {
            this.mChoiceActionMode.finish();
            this.mChoiceActionMode = null;
        }
        if (this.mChoiceMode != 0) {
            if (this.mCheckStates == null) {
                this.mCheckStates = new SparseBooleanArray(0);
            }
            if (this.mCheckedIdStates == null && this.mAdapter != null && this.mAdapter.hasStableIds()) {
                this.mCheckedIdStates = new Long2IntOpenHashMap();
            }
            if (this.mChoiceMode == 3) {
                this.clearChoices();
                this.setLongClickable(true);
            }
        }
    }

    public void setMultiChoiceModeListener(AbsListView.MultiChoiceModeListener listener) {
        if (this.mMultiChoiceModeCallback == null) {
            this.mMultiChoiceModeCallback = new AbsListView.MultiChoiceModeWrapper();
        }
        this.mMultiChoiceModeCallback.setWrapped(listener);
    }

    private boolean contentFits() {
        int childCount = this.getChildCount();
        if (childCount == 0) {
            return true;
        } else {
            return childCount != this.mItemCount ? false : this.getChildAt(0).getTop() >= this.mListPadding.top && this.getChildAt(childCount - 1).getBottom() <= this.getHeight() - this.mListPadding.bottom;
        }
    }

    public void setSmoothScrollbarEnabled(boolean enabled) {
        this.mSmoothScrollbarEnabled = enabled;
    }

    public boolean isSmoothScrollbarEnabled() {
        return this.mSmoothScrollbarEnabled;
    }

    public void setOnScrollListener(@Nullable AbsListView.OnScrollListener l) {
        this.mOnScrollListener = l;
        this.invokeOnItemScrollListener();
    }

    void invokeOnItemScrollListener() {
        if (this.mOnScrollListener != null) {
            this.mOnScrollListener.onScroll(this, this.mFirstPosition, this.getChildCount(), this.mItemCount);
        }
        this.onScrollChanged(0, 0, 0, 0);
    }

    @Override
    public void getFocusedRect(@NonNull Rect r) {
        View view = this.getSelectedView();
        if (view != null && view.getParent() == this) {
            view.getFocusedRect(r);
            this.offsetDescendantRectToMyCoords(view, r);
        } else {
            super.getFocusedRect(r);
        }
    }

    private void useDefaultSelector() {
        this.setSelector(new Drawable() {

            @Override
            public void draw(@NonNull Canvas canvas) {
                Paint paint = Paint.obtain();
                paint.setAlpha(25);
                canvas.drawRect(this.getBounds(), paint);
                paint.recycle();
            }
        });
    }

    public boolean isStackFromBottom() {
        return this.mStackFromBottom;
    }

    public void setStackFromBottom(boolean stackFromBottom) {
        if (this.mStackFromBottom != stackFromBottom) {
            this.mStackFromBottom = stackFromBottom;
            this.requestLayoutIfNecessary();
        }
    }

    void requestLayoutIfNecessary() {
        if (this.getChildCount() > 0) {
            this.resetList();
            this.requestLayout();
            this.invalidate();
        }
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (gainFocus && this.mSelectedPosition < 0 && !this.isInTouchMode()) {
            if (!this.isAttachedToWindow() && this.mAdapter != null) {
                this.mDataChanged = true;
                this.mOldItemCount = this.mItemCount;
                this.mItemCount = this.mAdapter.getCount();
            }
            this.resurrectSelection();
        }
    }

    @Override
    public void requestLayout() {
        if (!this.mBlockLayoutRequests && !this.mInLayout) {
            super.requestLayout();
        }
    }

    void resetList() {
        this.removeAllViewsInLayout();
        this.mFirstPosition = 0;
        this.mDataChanged = false;
        this.mPositionScrollAfterLayout = null;
        this.mNeedSync = false;
        this.mOldSelectedPosition = -1;
        this.mOldSelectedRowId = Long.MIN_VALUE;
        this.setSelectedPositionInt(-1);
        this.setNextSelectedPositionInt(-1);
        this.mSelectedTop = 0;
        this.mSelectorPosition = -1;
        this.mSelectorRect.setEmpty();
        this.invalidate();
    }

    @Override
    protected int computeVerticalScrollExtent() {
        int count = this.getChildCount();
        if (count > 0) {
            if (this.mSmoothScrollbarEnabled) {
                int extent = count * 100;
                View view = this.getChildAt(0);
                int top = view.getTop();
                int height = view.getHeight();
                if (height > 0) {
                    extent += top * 100 / height;
                }
                view = this.getChildAt(count - 1);
                int bottom = view.getBottom();
                height = view.getHeight();
                if (height > 0) {
                    extent -= (bottom - this.getHeight()) * 100 / height;
                }
                return extent;
            } else {
                return 1;
            }
        } else {
            return 0;
        }
    }

    @Override
    protected int computeVerticalScrollOffset() {
        int firstPosition = this.mFirstPosition;
        int childCount = this.getChildCount();
        if (firstPosition >= 0 && childCount > 0) {
            if (!this.mSmoothScrollbarEnabled) {
                int count = this.mItemCount;
                int index;
                if (firstPosition == 0) {
                    index = 0;
                } else if (firstPosition + childCount == count) {
                    index = count;
                } else {
                    index = firstPosition + childCount / 2;
                }
                return (int) ((float) firstPosition + (float) childCount * ((float) index / (float) count));
            }
            View view = this.getChildAt(0);
            int top = view.getTop();
            int height = view.getHeight();
            if (height > 0) {
                return Math.max(firstPosition * 100 - top * 100 / height + (int) ((float) this.mScrollY / (float) this.getHeight() * (float) this.mItemCount * 100.0F), 0);
            }
        }
        return 0;
    }

    @Override
    protected int computeVerticalScrollRange() {
        int result;
        if (this.mSmoothScrollbarEnabled) {
            result = Math.max(this.mItemCount * 100, 0);
            if (this.mScrollY != 0) {
                result += Math.abs((int) ((float) this.mScrollY / (float) this.getHeight() * (float) this.mItemCount * 100.0F));
            }
        } else {
            result = this.mItemCount;
        }
        return result;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mSelector == null) {
            this.useDefaultSelector();
        }
        Rect listPadding = this.mListPadding;
        listPadding.left = this.mSelectionLeftPadding + this.mPaddingLeft;
        listPadding.top = this.mSelectionTopPadding + this.mPaddingTop;
        listPadding.right = this.mSelectionRightPadding + this.mPaddingRight;
        listPadding.bottom = this.mSelectionBottomPadding + this.mPaddingBottom;
        if (this.mTranscriptMode == 1) {
            int childCount = this.getChildCount();
            int listBottom = this.getHeight() - this.getPaddingBottom();
            View lastChild = this.getChildAt(childCount - 1);
            int lastBottom = lastChild != null ? lastChild.getBottom() : listBottom;
            this.mForceTranscriptScroll = this.mFirstPosition + childCount >= this.mLastHandledItemCount && lastBottom <= listBottom;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.mInLayout = true;
        int childCount = this.getChildCount();
        if (changed) {
            for (int i = 0; i < childCount; i++) {
                this.getChildAt(i).forceLayout();
            }
            this.mRecycler.markChildrenDirty();
        }
        this.layoutChildren();
        this.mOverscrollMax = (b - t) / 3;
        this.mInLayout = false;
    }

    protected void layoutChildren() {
    }

    void updateScrollIndicators() {
        if (this.mScrollUp != null) {
            this.mScrollUp.setVisibility(this.canScrollUp() ? 0 : 4);
        }
        if (this.mScrollDown != null) {
            this.mScrollDown.setVisibility(this.canScrollDown() ? 0 : 4);
        }
    }

    private boolean canScrollUp() {
        boolean canScrollUp = this.mFirstPosition > 0;
        if (!canScrollUp && this.getChildCount() > 0) {
            View child = this.getChildAt(0);
            canScrollUp = child.getTop() < this.mListPadding.top;
        }
        return canScrollUp;
    }

    private boolean canScrollDown() {
        int count = this.getChildCount();
        boolean canScrollDown = this.mFirstPosition + count < this.mItemCount;
        if (!canScrollDown && count > 0) {
            View child = this.getChildAt(count - 1);
            canScrollDown = child.getBottom() > this.getBottom() - this.mListPadding.bottom;
        }
        return canScrollDown;
    }

    @Override
    public View getSelectedView() {
        return this.mItemCount > 0 && this.mSelectedPosition >= 0 ? this.getChildAt(this.mSelectedPosition - this.mFirstPosition) : null;
    }

    public int getListPaddingTop() {
        return this.mListPadding.top;
    }

    public int getListPaddingBottom() {
        return this.mListPadding.bottom;
    }

    public int getListPaddingLeft() {
        return this.mListPadding.left;
    }

    public int getListPaddingRight() {
        return this.mListPadding.right;
    }

    View obtainView(int position, boolean[] outMetadata) {
        outMetadata[0] = false;
        View transientView = this.mRecycler.getTransientStateView(position);
        if (transientView != null) {
            AbsListView.LayoutParams params = (AbsListView.LayoutParams) transientView.getLayoutParams();
            if (params.viewType == this.mAdapter.getItemViewType(position)) {
                View updatedView = this.mAdapter.getView(position, transientView, this);
                if (updatedView != transientView) {
                    this.setItemViewLayoutParams(updatedView, position);
                    this.mRecycler.addScrapView(updatedView, position);
                }
            }
            outMetadata[0] = true;
            transientView.dispatchFinishTemporaryDetach();
            return transientView;
        } else {
            View scrapView = this.mRecycler.getScrapView(position);
            View child = this.mAdapter.getView(position, scrapView, this);
            if (scrapView != null) {
                if (child != scrapView) {
                    this.mRecycler.addScrapView(scrapView, position);
                } else if (child.isTemporarilyDetached()) {
                    outMetadata[0] = true;
                    child.dispatchFinishTemporaryDetach();
                }
            }
            this.setItemViewLayoutParams(child, position);
            return child;
        }
    }

    private void setItemViewLayoutParams(View child, int position) {
        ViewGroup.LayoutParams vlp = child.getLayoutParams();
        AbsListView.LayoutParams lp;
        if (vlp == null) {
            lp = (AbsListView.LayoutParams) this.generateDefaultLayoutParams();
        } else if (!this.checkLayoutParams(vlp)) {
            lp = (AbsListView.LayoutParams) this.generateLayoutParams(vlp);
        } else {
            lp = (AbsListView.LayoutParams) vlp;
        }
        if (this.mAdapterHasStableIds) {
            lp.itemId = this.mAdapter.getItemId(position);
        }
        lp.viewType = this.mAdapter.getItemViewType(position);
        lp.isEnabled = this.mAdapter.isEnabled(position);
        if (lp != vlp) {
            child.setLayoutParams(lp);
        }
    }

    private boolean isItemClickable(@NonNull View view) {
        return !view.hasExplicitFocusable();
    }

    void positionSelectorLikeTouch(int position, View sel, float x, float y) {
        this.positionSelector(position, sel, true, x, y);
    }

    void positionSelectorLikeFocus(int position, View sel) {
        if (this.mSelector != null && this.mSelectorPosition != position && position != -1) {
            Rect bounds = this.mSelectorRect;
            float x = bounds.exactCenterX();
            float y = bounds.exactCenterY();
            this.positionSelector(position, sel, true, x, y);
        } else {
            this.positionSelector(position, sel);
        }
    }

    void positionSelector(int position, View sel) {
        this.positionSelector(position, sel, false, -1.0F, -1.0F);
    }

    private void positionSelector(int position, View sel, boolean manageHotspot, float x, float y) {
        boolean positionChanged = position != this.mSelectorPosition;
        if (position != -1) {
            this.mSelectorPosition = position;
        }
        Rect selectorRect = this.mSelectorRect;
        selectorRect.set(sel.getLeft(), sel.getTop(), sel.getRight(), sel.getBottom());
        if (sel instanceof AbsListView.SelectionBoundsAdjuster) {
            ((AbsListView.SelectionBoundsAdjuster) sel).adjustListItemSelectionBounds(selectorRect);
        }
        selectorRect.left = selectorRect.left - this.mSelectionLeftPadding;
        selectorRect.top = selectorRect.top - this.mSelectionTopPadding;
        selectorRect.right = selectorRect.right + this.mSelectionRightPadding;
        selectorRect.bottom = selectorRect.bottom + this.mSelectionBottomPadding;
        boolean isChildViewEnabled = sel.isEnabled();
        if (this.mIsChildViewEnabled != isChildViewEnabled) {
            this.mIsChildViewEnabled = isChildViewEnabled;
        }
        Drawable selector = this.mSelector;
        if (selector != null) {
            if (positionChanged) {
                selector.setVisible(false, false);
                selector.setState(StateSet.WILD_CARD);
            }
            selector.setBounds(selectorRect);
            if (positionChanged) {
                if (this.getVisibility() == 0) {
                    selector.setVisible(true, false);
                }
                this.updateSelectorState();
            }
            if (manageHotspot) {
                selector.setHotspot(x, y);
            }
        }
    }

    @Override
    protected void dispatchDraw(@NonNull Canvas canvas) {
        boolean clipToPadding = this.hasBooleanFlag(34);
        if (clipToPadding) {
            canvas.save();
            int scrollX = this.mScrollX;
            int scrollY = this.mScrollY;
            canvas.clipRect((float) (scrollX + this.mPaddingLeft), (float) (scrollY + this.mPaddingTop), (float) (scrollX + this.getWidth() - this.mPaddingRight), (float) (scrollY + this.getHeight() - this.mPaddingBottom));
            this.setBooleanFlag(34, false);
        }
        boolean drawSelectorOnTop = this.mDrawSelectorOnTop;
        if (!drawSelectorOnTop) {
            this.drawSelector(canvas);
        }
        super.dispatchDraw(canvas);
        if (drawSelectorOnTop) {
            this.drawSelector(canvas);
        }
        if (clipToPadding) {
            canvas.restore();
            this.setBooleanFlag(34, true);
        }
    }

    @Override
    protected void internalSetPadding(int left, int top, int right, int bottom) {
        super.internalSetPadding(left, top, right, bottom);
        if (this.isLayoutRequested()) {
            this.handleBoundsChange();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.handleBoundsChange();
    }

    void handleBoundsChange() {
        if (!this.mInLayout) {
            int childCount = this.getChildCount();
            if (childCount > 0) {
                this.mDataChanged = true;
                this.rememberSyncState();
                for (int i = 0; i < childCount; i++) {
                    View child = this.getChildAt(i);
                    ViewGroup.LayoutParams lp = child.getLayoutParams();
                    if (lp == null || lp.width < 1 || lp.height < 1) {
                        child.forceLayout();
                    }
                }
            }
        }
    }

    boolean touchModeDrawsInPressedState() {
        return switch(this.mTouchMode) {
            case 1, 2 ->
                true;
            default ->
                false;
        };
    }

    boolean shouldShowSelector() {
        return this.isFocused() && !this.isInTouchMode() || this.touchModeDrawsInPressedState() && this.isPressed();
    }

    private void drawSelector(Canvas canvas) {
        if (this.shouldDrawSelector()) {
            Drawable selector = this.mSelector;
            selector.setBounds(this.mSelectorRect);
            selector.draw(canvas);
        }
    }

    @Internal
    public final boolean shouldDrawSelector() {
        return !this.mSelectorRect.isEmpty();
    }

    public void setDrawSelectorOnTop(boolean onTop) {
        this.mDrawSelectorOnTop = onTop;
    }

    public boolean isDrawSelectorOnTop() {
        return this.mDrawSelectorOnTop;
    }

    public void setSelector(Drawable sel) {
        if (this.mSelector != null) {
            this.mSelector.setCallback(null);
            this.unscheduleDrawable(this.mSelector);
        }
        this.mSelector = sel;
        Rect padding = new Rect();
        sel.getPadding(padding);
        this.mSelectionLeftPadding = padding.left;
        this.mSelectionTopPadding = padding.top;
        this.mSelectionRightPadding = padding.right;
        this.mSelectionBottomPadding = padding.bottom;
        sel.setCallback(this);
        this.updateSelectorState();
    }

    public Drawable getSelector() {
        return this.mSelector;
    }

    void keyPressed() {
        if (this.isEnabled() && this.isClickable()) {
            Drawable selector = this.mSelector;
            Rect selectorRect = this.mSelectorRect;
            if (selector != null && (this.isFocused() || this.touchModeDrawsInPressedState()) && !selectorRect.isEmpty()) {
                View v = this.getChildAt(this.mSelectedPosition - this.mFirstPosition);
                if (v != null) {
                    if (v.hasExplicitFocusable()) {
                        return;
                    }
                    v.setPressed(true);
                }
                this.setPressed(true);
                boolean longClickable = this.isLongClickable();
                Drawable d = selector.getCurrent();
                if (longClickable && !this.mDataChanged) {
                    if (this.mPendingCheckForKeyLongPress == null) {
                        this.mPendingCheckForKeyLongPress = new AbsListView.CheckForKeyLongPress();
                    }
                    this.mPendingCheckForKeyLongPress.rememberWindowAttachCount();
                    this.postDelayed(this.mPendingCheckForKeyLongPress, (long) ViewConfiguration.getLongPressTimeout());
                }
            }
        }
    }

    public void setScrollIndicators(View up, View down) {
        this.mScrollUp = up;
        this.mScrollDown = down;
    }

    void updateSelectorState() {
        Drawable selector = this.mSelector;
        if (selector != null && selector.isStateful()) {
            if (this.shouldShowSelector()) {
                if (selector.setState(this.getDrawableStateForSelector())) {
                    this.invalidateDrawable(selector);
                }
            } else {
                selector.setState(StateSet.WILD_CARD);
            }
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        this.updateSelectorState();
    }

    private int[] getDrawableStateForSelector() {
        if (this.mIsChildViewEnabled) {
            return super.getDrawableState();
        } else {
            int enabledState = ENABLED_STATE_SET[0];
            int[] state = this.onCreateDrawableState(1);
            int enabledPos = -1;
            for (int i = state.length - 1; i >= 0; i--) {
                if (state[i] == enabledState) {
                    enabledPos = i;
                    break;
                }
            }
            if (enabledPos >= 0) {
                System.arraycopy(state, enabledPos + 1, state, enabledPos, state.length - enabledPos - 1);
            }
            return state;
        }
    }

    @Override
    public boolean verifyDrawable(@NonNull Drawable dr) {
        return this.mSelector == dr || super.verifyDrawable(dr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mAdapter != null && this.mDataSetObserver == null) {
            this.mDataSetObserver = new AdapterView.AdapterDataSetObserver();
            this.mAdapter.registerDataSetObserver(this.mDataSetObserver);
            this.mDataChanged = true;
            this.mOldItemCount = this.mItemCount;
            this.mItemCount = this.mAdapter.getCount();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mIsDetaching = true;
        this.mRecycler.clear();
        if (this.mAdapter != null && this.mDataSetObserver != null) {
            this.mAdapter.unregisterDataSetObserver(this.mDataSetObserver);
            this.mDataSetObserver = null;
        }
        if (this.mFlingRunnable != null) {
            this.removeCallbacks(this.mFlingRunnable);
        }
        if (this.mPositionScroller != null) {
            this.mPositionScroller.stop();
        }
        if (this.mPerformClick != null) {
            this.removeCallbacks(this.mPerformClick);
        }
        if (this.mTouchModeReset != null) {
            this.removeCallbacks(this.mTouchModeReset);
            this.mTouchModeReset.run();
        }
        this.mIsDetaching = false;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        int touchMode = this.isInTouchMode() ? 0 : 1;
        if (!hasWindowFocus) {
            if (this.mFlingRunnable != null) {
                this.removeCallbacks(this.mFlingRunnable);
                this.mFlingRunnable.mSuppressIdleStateChangeCall = false;
                this.mFlingRunnable.endFling();
                if (this.mPositionScroller != null) {
                    this.mPositionScroller.stop();
                }
                if (this.mScrollY != 0) {
                    this.mScrollY = 0;
                    this.finishGlows();
                    this.invalidate();
                }
            }
            if (touchMode == 1) {
                this.mResurrectToPosition = this.mSelectedPosition;
            }
        } else if (touchMode != this.mLastTouchMode && this.mLastTouchMode != -1) {
            if (touchMode == 1) {
                this.resurrectSelection();
            } else {
                this.hideSelector();
                this.mLayoutMode = 0;
                this.layoutChildren();
            }
        }
        this.mLastTouchMode = touchMode;
    }

    ContextMenu.ContextMenuInfo createContextMenuInfo(View view, int position, long id) {
        return new AdapterView.AdapterContextMenuInfo(view, position, id);
    }

    @Override
    public void onCancelPendingInputEvents() {
        super.onCancelPendingInputEvents();
        if (this.mPerformClick != null) {
            this.removeCallbacks(this.mPerformClick);
        }
        if (this.mPendingCheckForTap != null) {
            this.removeCallbacks(this.mPendingCheckForTap);
        }
        if (this.mPendingCheckForLongPress != null) {
            this.removeCallbacks(this.mPendingCheckForLongPress);
        }
        if (this.mPendingCheckForKeyLongPress != null) {
            this.removeCallbacks(this.mPendingCheckForKeyLongPress);
        }
    }

    private boolean performStylusButtonPressAction(MotionEvent ev) {
        if (this.mChoiceMode == 3 && this.mChoiceActionMode == null) {
            View child = this.getChildAt(this.mMotionPosition - this.mFirstPosition);
            if (child != null) {
                int longPressPosition = this.mMotionPosition;
                long longPressId = this.mAdapter.getItemId(this.mMotionPosition);
                if (this.performLongPress(child, longPressPosition, longPressId)) {
                    this.mTouchMode = -1;
                    this.setPressed(false);
                    child.setPressed(false);
                    return true;
                }
            }
        }
        return false;
    }

    boolean performLongPress(View child, int longPressPosition, long longPressId) {
        return this.performLongPress(child, longPressPosition, longPressId, Float.NaN, Float.NaN);
    }

    boolean performLongPress(View child, int longPressPosition, long longPressId, float x, float y) {
        if (this.mChoiceMode == 3) {
            if (this.mChoiceActionMode == null && (this.mChoiceActionMode = this.startActionMode(this.mMultiChoiceModeCallback)) != null) {
                this.setItemChecked(longPressPosition, true);
                this.performHapticFeedback(0);
            }
            return true;
        } else {
            boolean handled = false;
            if (this.mOnItemLongClickListener != null) {
                handled = this.mOnItemLongClickListener.onItemLongClick(this, child, longPressPosition, longPressId);
            }
            if (!handled) {
                this.mContextMenuInfo = this.createContextMenuInfo(child, longPressPosition, longPressId);
                if (!Float.isNaN(x) && !Float.isNaN(y)) {
                    handled = super.showContextMenuForChild(this, x, y);
                } else {
                    handled = super.showContextMenuForChild(this, Float.NaN, Float.NaN);
                }
            }
            if (handled) {
                this.performHapticFeedback(0);
            }
            return handled;
        }
    }

    @Override
    protected ContextMenu.ContextMenuInfo getContextMenuInfo() {
        return this.mContextMenuInfo;
    }

    @Override
    public boolean showContextMenu(float x, float y) {
        int position = this.pointToPosition((int) x, (int) y);
        if (position != -1) {
            long id = this.mAdapter.getItemId(position);
            View child = this.getChildAt(position - this.mFirstPosition);
            if (child != null) {
                this.mContextMenuInfo = this.createContextMenuInfo(child, position, id);
                return super.showContextMenuForChild(this, x, y);
            }
        }
        return super.showContextMenu(x, y);
    }

    @Override
    public boolean showContextMenuForChild(View originalView, float x, float y) {
        int longPressPosition = this.getPositionForView(originalView);
        if (longPressPosition < 0) {
            return false;
        } else {
            long longPressId = this.mAdapter.getItemId(longPressPosition);
            boolean handled = false;
            if (this.mOnItemLongClickListener != null) {
                handled = this.mOnItemLongClickListener.onItemLongClick(this, originalView, longPressPosition, longPressId);
            }
            if (!handled) {
                View child = this.getChildAt(longPressPosition - this.mFirstPosition);
                this.mContextMenuInfo = this.createContextMenuInfo(child, longPressPosition, longPressId);
                handled = super.showContextMenuForChild(originalView, x, y);
            }
            return handled;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == 257 || keyCode == 335) {
            if (!this.isEnabled()) {
                return true;
            }
            if (this.isClickable() && this.isPressed() && this.mSelectedPosition >= 0 && this.mAdapter != null && this.mSelectedPosition < this.mAdapter.getCount()) {
                View view = this.getChildAt(this.mSelectedPosition - this.mFirstPosition);
                if (view != null) {
                    this.performItemClick(view, this.mSelectedPosition, this.mSelectedRowId);
                    view.setPressed(false);
                }
                this.setPressed(false);
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void dispatchSetPressed(boolean pressed) {
    }

    @Override
    public void dispatchDrawableHotspotChanged(float x, float y) {
    }

    public int pointToPosition(int x, int y) {
        Rect frame = this.mTouchFrame;
        if (frame == null) {
            this.mTouchFrame = new Rect();
            frame = this.mTouchFrame;
        }
        int count = this.getChildCount();
        for (int i = count - 1; i >= 0; i--) {
            View child = this.getChildAt(i);
            if (child.getVisibility() == 0) {
                child.getHitRect(frame);
                if (frame.contains(x, y)) {
                    return this.mFirstPosition + i;
                }
            }
        }
        return -1;
    }

    public long pointToRowId(int x, int y) {
        int position = this.pointToPosition(x, y);
        return position >= 0 ? this.mAdapter.getItemId(position) : Long.MIN_VALUE;
    }

    private boolean startScrollIfNeeded(int x, int y, MotionEvent vtev) {
        int deltaY = y - this.mMotionY;
        int distance = Math.abs(deltaY);
        boolean overscroll = this.mScrollY != 0;
        if ((overscroll || distance > this.mTouchSlop) && (this.getNestedScrollAxes() & 2) == 0) {
            if (overscroll) {
                this.mTouchMode = 5;
                this.mMotionCorrection = 0;
            } else {
                this.mTouchMode = 3;
                this.mMotionCorrection = deltaY > 0 ? this.mTouchSlop : -this.mTouchSlop;
            }
            this.removeCallbacks(this.mPendingCheckForLongPress);
            this.setPressed(false);
            View motionView = this.getChildAt(this.mMotionPosition - this.mFirstPosition);
            if (motionView != null) {
                motionView.setPressed(false);
            }
            this.reportScrollStateChange(1);
            ViewParent parent = this.getParent();
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
            this.scrollIfNeeded(x, y, vtev);
            return true;
        } else {
            return false;
        }
    }

    private void scrollIfNeeded(int x, int y, MotionEvent vtev) {
        int rawDeltaY = y - this.mMotionY;
        int scrollOffsetCorrection = 0;
        if (this.mLastY == Integer.MIN_VALUE) {
            rawDeltaY -= this.mMotionCorrection;
        }
        int incrementalDeltaY = this.mLastY != Integer.MIN_VALUE ? y - this.mLastY : rawDeltaY;
        incrementalDeltaY = this.releaseGlow(incrementalDeltaY, x);
        if (this.dispatchNestedPreScroll(0, -incrementalDeltaY, this.mScrollConsumed, this.mScrollOffset, 0)) {
            rawDeltaY += this.mScrollConsumed[1];
            scrollOffsetCorrection = -this.mScrollOffset[1];
            incrementalDeltaY += this.mScrollConsumed[1];
            if (vtev != null) {
                vtev.offsetLocation(0.0F, (float) this.mScrollOffset[1]);
                this.mNestedYOffset = this.mNestedYOffset + this.mScrollOffset[1];
            }
        }
        int lastYCorrection = 0;
        if (this.mTouchMode == 3) {
            if (y != this.mLastY) {
                if (!this.hasBooleanFlag(524288) && Math.abs(rawDeltaY) > this.mTouchSlop) {
                    ViewParent parent = this.getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }
                int motionIndex;
                if (this.mMotionPosition >= 0) {
                    motionIndex = this.mMotionPosition - this.mFirstPosition;
                } else {
                    motionIndex = this.getChildCount() / 2;
                }
                int motionViewPrevTop = 0;
                View motionView = this.getChildAt(motionIndex);
                if (motionView != null) {
                    motionViewPrevTop = motionView.getTop();
                }
                boolean atEdge = false;
                if (incrementalDeltaY != 0) {
                    atEdge = this.trackMotionScroll(rawDeltaY, incrementalDeltaY);
                }
                motionView = this.getChildAt(motionIndex);
                if (motionView != null) {
                    int motionViewRealTop = motionView.getTop();
                    if (atEdge) {
                        int overscroll = -incrementalDeltaY - (motionViewRealTop - motionViewPrevTop);
                        if (this.dispatchNestedScroll(0, overscroll - incrementalDeltaY, 0, overscroll, this.mScrollOffset, 0, this.mScrollConsumed)) {
                            lastYCorrection -= this.mScrollOffset[1];
                            if (vtev != null) {
                                vtev.offsetLocation(0.0F, (float) this.mScrollOffset[1]);
                                this.mNestedYOffset = this.mNestedYOffset + this.mScrollOffset[1];
                            }
                        } else {
                            boolean atOverscrollEdge = this.overScrollBy(0, overscroll, 0, this.mScrollY, 0, 0, 0, this.mOverscrollDistance, true);
                            if (atOverscrollEdge && this.mVelocityTracker != null) {
                                this.mVelocityTracker.clear();
                            }
                            int overscrollMode = this.getOverScrollMode();
                            if (overscrollMode == 0 || overscrollMode == 1 && !this.contentFits()) {
                                if (!atOverscrollEdge) {
                                    this.mDirection = 0;
                                    this.mTouchMode = 5;
                                }
                                if (incrementalDeltaY > 0) {
                                    this.mEdgeGlowTop.onPullDistance((float) (-overscroll) / (float) this.getHeight(), (float) x / (float) this.getWidth());
                                    if (!this.mEdgeGlowBottom.isFinished()) {
                                        this.mEdgeGlowBottom.onRelease();
                                    }
                                    this.invalidateTopGlow();
                                } else {
                                    this.mEdgeGlowBottom.onPullDistance((float) overscroll / (float) this.getHeight(), 1.0F - (float) x / (float) this.getWidth());
                                    if (!this.mEdgeGlowTop.isFinished()) {
                                        this.mEdgeGlowTop.onRelease();
                                    }
                                    this.invalidateBottomGlow();
                                }
                            }
                        }
                    }
                    this.mMotionY = y + lastYCorrection + scrollOffsetCorrection;
                }
                this.mLastY = y + lastYCorrection + scrollOffsetCorrection;
            }
        } else if (this.mTouchMode == 5 && y != this.mLastY) {
            int oldScroll = this.mScrollY;
            int newScroll = oldScroll - incrementalDeltaY;
            int newDirection = y > this.mLastY ? 1 : -1;
            if (this.mDirection == 0) {
                this.mDirection = newDirection;
            }
            int overScrollDistance = -incrementalDeltaY;
            if ((newScroll >= 0 || oldScroll < 0) && (newScroll <= 0 || oldScroll > 0)) {
                incrementalDeltaY = 0;
            } else {
                overScrollDistance = -oldScroll;
                incrementalDeltaY += overScrollDistance;
            }
            if (overScrollDistance != 0) {
                this.overScrollBy(0, overScrollDistance, 0, this.mScrollY, 0, 0, 0, this.mOverscrollDistance, true);
                int overscrollMode = this.getOverScrollMode();
                if (overscrollMode == 0 || overscrollMode == 1 && !this.contentFits()) {
                    if (rawDeltaY > 0) {
                        this.mEdgeGlowTop.onPullDistance((float) overScrollDistance / (float) this.getHeight(), (float) x / (float) this.getWidth());
                        if (!this.mEdgeGlowBottom.isFinished()) {
                            this.mEdgeGlowBottom.onRelease();
                        }
                        this.invalidateTopGlow();
                    } else if (rawDeltaY < 0) {
                        this.mEdgeGlowBottom.onPullDistance((float) (-overScrollDistance) / (float) this.getHeight(), 1.0F - (float) x / (float) this.getWidth());
                        if (!this.mEdgeGlowTop.isFinished()) {
                            this.mEdgeGlowTop.onRelease();
                        }
                        this.invalidateBottomGlow();
                    }
                }
            }
            if (incrementalDeltaY != 0) {
                if (this.mScrollY != 0) {
                    this.mScrollY = 0;
                }
                this.trackMotionScroll(incrementalDeltaY, incrementalDeltaY);
                this.mTouchMode = 3;
                int motionPosition = this.findClosestMotionRow(y);
                this.mMotionCorrection = 0;
                View motionViewx = this.getChildAt(motionPosition - this.mFirstPosition);
                this.mMotionViewOriginalTop = motionViewx != null ? motionViewx.getTop() : 0;
                this.mMotionY = y + scrollOffsetCorrection;
                this.mMotionPosition = motionPosition;
            }
            this.mLastY = y + lastYCorrection + scrollOffsetCorrection;
            this.mDirection = newDirection;
        }
    }

    private int releaseGlow(int deltaY, int x) {
        float consumed = 0.0F;
        if (this.mEdgeGlowTop.getDistance() != 0.0F) {
            consumed = this.mEdgeGlowTop.onPullDistance((float) deltaY / (float) this.getHeight(), (float) x / (float) this.getWidth());
            if (consumed != 0.0F) {
                this.invalidateTopGlow();
            }
        } else if (this.mEdgeGlowBottom.getDistance() != 0.0F) {
            consumed = -this.mEdgeGlowBottom.onPullDistance((float) (-deltaY) / (float) this.getHeight(), 1.0F - (float) x / (float) this.getWidth());
            if (consumed != 0.0F) {
                this.invalidateBottomGlow();
            }
        }
        int pixelsConsumed = Math.round(consumed * (float) this.getHeight());
        return deltaY - pixelsConsumed;
    }

    private boolean isGlowActive() {
        return this.mEdgeGlowBottom.getDistance() != 0.0F || this.mEdgeGlowTop.getDistance() != 0.0F;
    }

    private void invalidateTopGlow() {
        if (this.shouldDisplayEdgeEffects()) {
            this.invalidate();
        }
    }

    private void invalidateBottomGlow() {
        if (this.shouldDisplayEdgeEffects()) {
            this.invalidate();
        }
    }

    public void onTouchModeChanged(boolean isInTouchMode) {
        if (isInTouchMode) {
            this.hideSelector();
            if (this.getHeight() > 0 && this.getChildCount() > 0) {
                this.layoutChildren();
            }
            this.updateSelectorState();
        } else {
            int touchMode = this.mTouchMode;
            if (touchMode == 5 || touchMode == 6) {
                if (this.mFlingRunnable != null) {
                    this.mFlingRunnable.endFling();
                }
                if (this.mPositionScroller != null) {
                    this.mPositionScroller.stop();
                }
                if (this.mScrollY != 0) {
                    this.mScrollY = 0;
                    this.finishGlows();
                    this.invalidate();
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {
        if (this.isEnabled()) {
            if (this.mPositionScroller != null) {
                this.mPositionScroller.stop();
            }
            if (!this.mIsDetaching && this.isAttachedToWindow()) {
                this.startNestedScroll(2, 0);
                this.initVelocityTrackerIfNotExists();
                MotionEvent vtev = ev.copy();
                int action = ev.getAction();
                if (action == 0) {
                    this.mNestedYOffset = 0;
                }
                vtev.offsetLocation(0.0F, (float) this.mNestedYOffset);
                switch(action) {
                    case 0:
                        this.onTouchDown(ev);
                        break;
                    case 1:
                        this.onTouchUp(ev);
                        break;
                    case 2:
                        this.onTouchMove(ev, vtev);
                        break;
                    case 3:
                        this.onTouchCancel();
                }
                if (this.mVelocityTracker != null) {
                    this.mVelocityTracker.addMovement(vtev);
                }
                vtev.recycle();
                return true;
            } else {
                return false;
            }
        } else {
            return this.isClickable() || this.isLongClickable();
        }
    }

    private void onTouchDown(@NonNull MotionEvent ev) {
        this.mHasPerformedLongPress = false;
        this.hideSelector();
        if (this.mTouchMode == 6) {
            if (this.mFlingRunnable != null) {
                this.mFlingRunnable.endFling();
            }
            if (this.mPositionScroller != null) {
                this.mPositionScroller.stop();
            }
            this.mTouchMode = 5;
            this.mMotionX = (int) ev.getX();
            this.mMotionY = (int) ev.getY();
            this.mLastY = this.mMotionY;
            this.mMotionCorrection = 0;
            this.mDirection = 0;
            this.stopEdgeGlowRecede(ev.getX());
        } else {
            int x = (int) ev.getX();
            int y = (int) ev.getY();
            int motionPosition = this.pointToPosition(x, y);
            if (!this.mDataChanged) {
                if (this.mTouchMode == 4) {
                    this.mTouchMode = 3;
                    this.mMotionCorrection = 0;
                    motionPosition = this.findMotionRow(y);
                    if (this.mFlingRunnable != null) {
                        this.mFlingRunnable.flywheelTouch();
                    }
                    this.stopEdgeGlowRecede((float) x);
                } else if (motionPosition >= 0 && this.getAdapter().isEnabled(motionPosition)) {
                    this.mTouchMode = 0;
                    if (this.mPendingCheckForTap == null) {
                        this.mPendingCheckForTap = new AbsListView.CheckForTap();
                    }
                    this.mPendingCheckForTap.x = ev.getX();
                    this.mPendingCheckForTap.y = ev.getY();
                    this.postDelayed(this.mPendingCheckForTap, (long) ViewConfiguration.getTapTimeout());
                }
            }
            if (motionPosition >= 0) {
                View v = this.getChildAt(motionPosition - this.mFirstPosition);
                this.mMotionViewOriginalTop = v.getTop();
            }
            this.mMotionX = x;
            this.mMotionY = y;
            this.mMotionPosition = motionPosition;
            this.mLastY = Integer.MIN_VALUE;
        }
        if (this.mTouchMode == 0 && this.mMotionPosition != -1 && this.performButtonActionOnTouchDown(ev)) {
            this.removeCallbacks(this.mPendingCheckForTap);
        }
    }

    private void stopEdgeGlowRecede(float x) {
        if (this.mEdgeGlowTop.getDistance() != 0.0F) {
            this.mEdgeGlowTop.onPullDistance(0.0F, x / (float) this.getWidth());
        }
        if (this.mEdgeGlowBottom.getDistance() != 0.0F) {
            this.mEdgeGlowBottom.onPullDistance(0.0F, x / (float) this.getWidth());
        }
    }

    private void onTouchMove(@NonNull MotionEvent ev, @NonNull MotionEvent vtev) {
        if (!this.mHasPerformedLongPress) {
            if (this.mDataChanged) {
                this.layoutChildren();
            }
            int y = (int) ev.getY();
            switch(this.mTouchMode) {
                case 0:
                case 1:
                case 2:
                    if (!this.startScrollIfNeeded((int) ev.getX(), y, vtev)) {
                        View motionView = this.getChildAt(this.mMotionPosition - this.mFirstPosition);
                        float x = ev.getX();
                        if (!this.pointInView(x, (float) y, (float) this.mTouchSlop)) {
                            this.setPressed(false);
                            if (motionView != null) {
                                motionView.setPressed(false);
                            }
                            this.removeCallbacks((Runnable) (this.mTouchMode == 0 ? this.mPendingCheckForTap : this.mPendingCheckForLongPress));
                            this.mTouchMode = 2;
                            this.updateSelectorState();
                        } else if (motionView != null) {
                            float[] point = this.mTmpPoint;
                            point[0] = x;
                            point[1] = (float) y;
                            this.transformPointToViewLocal(point, motionView);
                            motionView.drawableHotspotChanged(point[0], point[1]);
                        }
                    }
                    break;
                case 3:
                case 5:
                    this.scrollIfNeeded((int) ev.getX(), y, vtev);
                case 4:
            }
        }
    }

    private void onTouchUp(@NonNull MotionEvent ev) {
        switch(this.mTouchMode) {
            case 0:
            case 1:
            case 2:
                int motionPosition = this.mMotionPosition;
                View child = this.getChildAt(motionPosition - this.mFirstPosition);
                if (child != null) {
                    if (this.mTouchMode != 0) {
                        child.setPressed(false);
                    }
                    float x = ev.getX();
                    boolean inList = x > (float) this.mListPadding.left && x < (float) (this.getWidth() - this.mListPadding.right);
                    if (inList && !child.hasExplicitFocusable()) {
                        if (this.mPerformClick == null) {
                            this.mPerformClick = new AbsListView.PerformClick();
                        }
                        AbsListView.PerformClick performClick = this.mPerformClick;
                        performClick.mClickMotionPosition = motionPosition;
                        performClick.rememberWindowAttachCount();
                        this.mResurrectToPosition = motionPosition;
                        if (this.mTouchMode == 0 || this.mTouchMode == 1) {
                            this.removeCallbacks((Runnable) (this.mTouchMode == 0 ? this.mPendingCheckForTap : this.mPendingCheckForLongPress));
                            this.mLayoutMode = 0;
                            if (!this.mDataChanged && this.mAdapter.isEnabled(motionPosition)) {
                                this.mTouchMode = 1;
                                this.setSelectedPositionInt(this.mMotionPosition);
                                this.layoutChildren();
                                child.setPressed(true);
                                this.positionSelector(this.mMotionPosition, child);
                                this.setPressed(true);
                                if (this.mSelector != null) {
                                    Drawable d = this.mSelector.getCurrent();
                                    this.mSelector.setHotspot(x, ev.getY());
                                }
                                if (this.mTouchModeReset != null) {
                                    this.removeCallbacks(this.mTouchModeReset);
                                }
                                this.mTouchModeReset = () -> {
                                    this.mTouchModeReset = null;
                                    this.mTouchMode = -1;
                                    child.setPressed(false);
                                    this.setPressed(false);
                                    if (!this.mDataChanged && !this.mIsDetaching && this.isAttachedToWindow()) {
                                        performClick.run();
                                    }
                                };
                                this.postDelayed(this.mTouchModeReset, (long) ViewConfiguration.getPressedStateDuration());
                            } else {
                                this.mTouchMode = -1;
                                this.updateSelectorState();
                            }
                            return;
                        }
                        if (!this.mDataChanged && this.mAdapter.isEnabled(motionPosition)) {
                            performClick.run();
                        }
                    }
                }
                this.mTouchMode = -1;
                this.updateSelectorState();
                break;
            case 3:
                int childCount = this.getChildCount();
                if (childCount > 0) {
                    int firstChildTop = this.getChildAt(0).getTop();
                    int lastChildBottom = this.getChildAt(childCount - 1).getBottom();
                    int contentTop = this.mListPadding.top;
                    int contentBottom = this.getHeight() - this.mListPadding.bottom;
                    if (this.mFirstPosition == 0 && firstChildTop >= contentTop && this.mFirstPosition + childCount < this.mItemCount && lastChildBottom <= this.getHeight() - contentBottom) {
                        this.mTouchMode = -1;
                        this.reportScrollStateChange(0);
                    } else {
                        VelocityTracker velocityTracker = this.mVelocityTracker;
                        velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                        int initialVelocity = (int) (velocityTracker.getYVelocity() * this.mVelocityScale);
                        boolean flingVelocity = Math.abs(initialVelocity) > this.mMinimumVelocity;
                        if (flingVelocity && !this.mEdgeGlowTop.isFinished()) {
                            this.mEdgeGlowTop.onAbsorb(initialVelocity);
                        } else if (flingVelocity && !this.mEdgeGlowBottom.isFinished()) {
                            this.mEdgeGlowBottom.onAbsorb(-initialVelocity);
                        } else if (flingVelocity && (this.mFirstPosition != 0 || firstChildTop != contentTop - this.mOverscrollDistance) && (this.mFirstPosition + childCount != this.mItemCount || lastChildBottom != contentBottom + this.mOverscrollDistance)) {
                            if (!this.dispatchNestedPreFling(0.0F, (float) (-initialVelocity))) {
                                if (this.mFlingRunnable == null) {
                                    this.mFlingRunnable = new AbsListView.FlingRunnable();
                                }
                                this.reportScrollStateChange(2);
                                this.mFlingRunnable.start(-initialVelocity);
                                this.dispatchNestedFling(0.0F, (float) (-initialVelocity), true);
                            } else {
                                this.mTouchMode = -1;
                                this.reportScrollStateChange(0);
                            }
                        } else {
                            this.mTouchMode = -1;
                            this.reportScrollStateChange(0);
                            if (this.mFlingRunnable != null) {
                                this.mFlingRunnable.endFling();
                            }
                            if (this.mPositionScroller != null) {
                                this.mPositionScroller.stop();
                            }
                            if (flingVelocity && !this.dispatchNestedPreFling(0.0F, (float) (-initialVelocity))) {
                                this.dispatchNestedFling(0.0F, (float) (-initialVelocity), false);
                            }
                        }
                    }
                } else {
                    this.mTouchMode = -1;
                    this.reportScrollStateChange(0);
                }
            case 4:
            default:
                break;
            case 5:
                if (this.mFlingRunnable == null) {
                    this.mFlingRunnable = new AbsListView.FlingRunnable();
                }
                VelocityTracker velocityTracker = this.mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                int initialVelocity = (int) velocityTracker.getYVelocity();
                this.reportScrollStateChange(2);
                if (Math.abs(initialVelocity) > this.mMinimumVelocity) {
                    this.mFlingRunnable.startOverfling(-initialVelocity);
                } else {
                    this.mFlingRunnable.startSpringback();
                }
        }
        this.setPressed(false);
        if (this.shouldDisplayEdgeEffects()) {
            this.mEdgeGlowTop.onRelease();
            this.mEdgeGlowBottom.onRelease();
        }
        this.invalidate();
        this.removeCallbacks(this.mPendingCheckForLongPress);
        this.recycleVelocityTracker();
    }

    private boolean shouldDisplayEdgeEffects() {
        return this.getOverScrollMode() != 2;
    }

    private void onTouchCancel() {
        switch(this.mTouchMode) {
            case 5:
                if (this.mFlingRunnable == null) {
                    this.mFlingRunnable = new AbsListView.FlingRunnable();
                }
                this.mFlingRunnable.startSpringback();
            case 6:
                break;
            default:
                this.mTouchMode = -1;
                this.setPressed(false);
                View motionView = this.getChildAt(this.mMotionPosition - this.mFirstPosition);
                if (motionView != null) {
                    motionView.setPressed(false);
                }
                this.removeCallbacks(this.mPendingCheckForLongPress);
                this.recycleVelocityTracker();
        }
        if (this.shouldDisplayEdgeEffects()) {
            this.mEdgeGlowTop.onRelease();
            this.mEdgeGlowBottom.onRelease();
        }
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        if (this.mScrollY != scrollY) {
            this.onScrollChanged(this.mScrollX, scrollY, this.mScrollX, this.mScrollY);
            this.mScrollY = scrollY;
            this.awakenScrollBars();
        }
    }

    @Override
    public boolean onGenericMotionEvent(@NonNull MotionEvent event) {
        switch(event.getAction()) {
            case 8:
                float axisValue = event.getAxisValue(9);
                int delta = Math.round(axisValue * this.mVerticalScrollFactor);
                if (delta != 0 && !this.trackMotionScroll(delta, delta)) {
                    return true;
                }
                break;
            case 11:
                int actionButton = event.getActionButton();
                if (actionButton == 2 && (this.mTouchMode == 0 || this.mTouchMode == 1) && this.performStylusButtonPressAction(event)) {
                    this.removeCallbacks(this.mPendingCheckForLongPress);
                    this.removeCallbacks(this.mPendingCheckForTap);
                }
        }
        return super.onGenericMotionEvent(event);
    }

    public void fling(int velocityY) {
        if (this.mFlingRunnable == null) {
            this.mFlingRunnable = new AbsListView.FlingRunnable();
        }
        this.reportScrollStateChange(2);
        this.mFlingRunnable.start(velocityY);
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
        int motionIndex = this.getChildCount() / 2;
        View motionView = this.getChildAt(motionIndex);
        int oldTop = motionView != null ? motionView.getTop() : 0;
        if (motionView == null || this.trackMotionScroll(-dyUnconsumed, -dyUnconsumed)) {
            int myUnconsumed = dyUnconsumed;
            int myConsumed = 0;
            if (motionView != null) {
                myConsumed = motionView.getTop() - oldTop;
                myUnconsumed = dyUnconsumed - myConsumed;
            }
            consumed[1] += myConsumed;
            this.dispatchNestedScroll(0, myConsumed, 0, myUnconsumed, null, type, consumed);
        }
    }

    @Override
    public boolean onNestedFling(@NonNull View target, float velocityX, float velocityY, boolean consumed) {
        int childCount = this.getChildCount();
        if (!consumed && childCount > 0 && this.canScrollList((int) velocityY) && Math.abs(velocityY) > (float) this.mMinimumVelocity) {
            this.reportScrollStateChange(2);
            if (this.mFlingRunnable == null) {
                this.mFlingRunnable = new AbsListView.FlingRunnable();
            }
            if (!this.dispatchNestedPreFling(0.0F, velocityY)) {
                this.mFlingRunnable.start((int) velocityY);
            }
            return true;
        } else {
            return this.dispatchNestedFling(velocityX, velocityY, consumed);
        }
    }

    @Override
    public void onDrawForeground(@NonNull Canvas canvas) {
        super.onDrawForeground(canvas);
        if (this.shouldDisplayEdgeEffects()) {
            int scrollY = this.mScrollY;
            boolean clipToPadding = this.getClipToPadding();
            int width;
            int height;
            int translateX;
            int translateY;
            if (clipToPadding) {
                width = this.getWidth() - this.mPaddingLeft - this.mPaddingRight;
                height = this.getHeight() - this.mPaddingTop - this.mPaddingBottom;
                translateX = this.mPaddingLeft;
                translateY = this.mPaddingTop;
            } else {
                width = this.getWidth();
                height = this.getHeight();
                translateX = 0;
                translateY = 0;
            }
            this.mEdgeGlowTop.setSize(width, height);
            this.mEdgeGlowBottom.setSize(width, height);
            if (!this.mEdgeGlowTop.isFinished()) {
                int restoreCount = canvas.save();
                canvas.clipRect((float) translateX, (float) translateY, (float) (translateX + width), (float) (translateY + this.mEdgeGlowTop.getMaxHeight()));
                int edgeY = Math.min(0, scrollY + this.mFirstPositionDistanceGuess) + translateY;
                canvas.translate((float) translateX, (float) edgeY);
                if (this.mEdgeGlowTop.draw(canvas)) {
                    this.invalidateTopGlow();
                }
                canvas.restoreToCount(restoreCount);
            }
            if (!this.mEdgeGlowBottom.isFinished()) {
                int restoreCount = canvas.save();
                canvas.clipRect((float) translateX, (float) (translateY + height - this.mEdgeGlowBottom.getMaxHeight()), (float) (translateX + width), (float) (translateY + height));
                int edgeX = -width + translateX;
                int edgeY = Math.max(this.getHeight(), scrollY + this.mLastPositionDistanceGuess) - (clipToPadding ? this.mPaddingBottom : 0);
                canvas.translate((float) edgeX, (float) edgeY);
                canvas.rotate(180.0F, (float) width, 0.0F);
                if (this.mEdgeGlowBottom.draw(canvas)) {
                    this.invalidateBottomGlow();
                }
                canvas.restoreToCount(restoreCount);
            }
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
        if (this.mPositionScroller != null) {
            this.mPositionScroller.stop();
        }
        if (!this.mIsDetaching && this.isAttachedToWindow()) {
            switch(action) {
                case 0:
                    int touchMode = this.mTouchMode;
                    if (touchMode == 6 || touchMode == 5) {
                        this.mMotionCorrection = 0;
                        return true;
                    }
                    int x = (int) ev.getX();
                    int y = (int) ev.getY();
                    int motionPosition = this.findMotionRow(y);
                    if (this.isGlowActive()) {
                        touchMode = this.mTouchMode = 4;
                    } else if (touchMode != 4 && motionPosition >= 0) {
                        View v = this.getChildAt(motionPosition - this.mFirstPosition);
                        this.mMotionViewOriginalTop = v.getTop();
                        this.mMotionX = x;
                        this.mMotionY = y;
                        this.mMotionPosition = motionPosition;
                        this.mTouchMode = 0;
                    }
                    this.mLastY = Integer.MIN_VALUE;
                    this.initOrResetVelocityTracker();
                    this.mVelocityTracker.addMovement(ev);
                    this.mNestedYOffset = 0;
                    this.startNestedScroll(2, 0);
                    if (touchMode == 4) {
                        return true;
                    }
                    break;
                case 1:
                case 3:
                    this.mTouchMode = -1;
                    this.recycleVelocityTracker();
                    this.reportScrollStateChange(0);
                    this.stopNestedScroll(0);
                    break;
                case 2:
                    if (this.mTouchMode == 0) {
                        int y = (int) ev.getY();
                        this.initVelocityTrackerIfNotExists();
                        this.mVelocityTracker.addMovement(ev);
                        if (this.startScrollIfNeeded((int) ev.getX(), y, null)) {
                            return true;
                        }
                    }
            }
            return false;
        } else {
            return false;
        }
    }

    @Override
    public void addTouchables(@NonNull ArrayList<View> views) {
        int count = this.getChildCount();
        int firstPosition = this.mFirstPosition;
        ListAdapter adapter = this.mAdapter;
        if (adapter != null) {
            for (int i = 0; i < count; i++) {
                View child = this.getChildAt(i);
                if (adapter.isEnabled(firstPosition + i)) {
                    views.add(child);
                }
                child.addTouchables(views);
            }
        }
    }

    void reportScrollStateChange(int newState) {
        if (newState != this.mLastScrollState && this.mOnScrollListener != null) {
            this.mLastScrollState = newState;
            this.mOnScrollListener.onScrollStateChanged(this, newState);
        }
    }

    public void setFriction(float friction) {
        if (this.mFlingRunnable == null) {
            this.mFlingRunnable = new AbsListView.FlingRunnable();
        }
        this.mFlingRunnable.mScroller.setFriction(friction);
    }

    public void setVelocityScale(float scale) {
        this.mVelocityScale = scale;
    }

    AbsListView.PositionScroller createPositionScroller() {
        return new AbsListView.DefaultPositionScroller();
    }

    public void smoothScrollToPosition(int position) {
        if (this.mPositionScroller == null) {
            this.mPositionScroller = this.createPositionScroller();
        }
        this.mPositionScroller.start(position);
    }

    public void smoothScrollToPositionFromTop(int position, int offset, int duration) {
        if (this.mPositionScroller == null) {
            this.mPositionScroller = this.createPositionScroller();
        }
        this.mPositionScroller.startWithOffset(position, offset, duration);
    }

    public void smoothScrollToPositionFromTop(int position, int offset) {
        if (this.mPositionScroller == null) {
            this.mPositionScroller = this.createPositionScroller();
        }
        this.mPositionScroller.startWithOffset(position, offset);
    }

    public void smoothScrollToPosition(int position, int boundPosition) {
        if (this.mPositionScroller == null) {
            this.mPositionScroller = this.createPositionScroller();
        }
        this.mPositionScroller.start(position, boundPosition);
    }

    public void smoothScrollBy(int distance, int duration) {
        this.smoothScrollBy(distance, duration, false, false);
    }

    void smoothScrollBy(int distance, int duration, boolean linear, boolean suppressEndFlingStateChangeCall) {
        if (this.mFlingRunnable == null) {
            this.mFlingRunnable = new AbsListView.FlingRunnable();
        }
        int firstPos = this.mFirstPosition;
        int childCount = this.getChildCount();
        int lastPos = firstPos + childCount;
        int topLimit = this.getPaddingTop();
        int bottomLimit = this.getHeight() - this.getPaddingBottom();
        if (distance == 0 || this.mItemCount == 0 || childCount == 0 || firstPos == 0 && this.getChildAt(0).getTop() == topLimit && distance < 0 || lastPos == this.mItemCount && this.getChildAt(childCount - 1).getBottom() == bottomLimit && distance > 0) {
            this.mFlingRunnable.endFling();
            if (this.mPositionScroller != null) {
                this.mPositionScroller.stop();
            }
        } else {
            this.reportScrollStateChange(2);
            this.mFlingRunnable.startScroll(distance, duration, linear, suppressEndFlingStateChangeCall);
        }
    }

    void smoothScrollByOffset(int position) {
        int index = -1;
        if (position < 0) {
            index = this.getFirstVisiblePosition();
        } else if (position > 0) {
            index = this.getLastVisiblePosition();
        }
        if (index > -1) {
            View child = this.getChildAt(index - this.getFirstVisiblePosition());
            if (child != null) {
                Rect visibleRect = new Rect();
                if (child.getGlobalVisibleRect(visibleRect)) {
                    int childRectArea = child.getWidth() * child.getHeight();
                    int visibleRectArea = visibleRect.width() * visibleRect.height();
                    float visibleArea = (float) visibleRectArea / (float) childRectArea;
                    float visibleThreshold = 0.75F;
                    if (position < 0 && visibleArea < 0.75F) {
                        index++;
                    } else if (position > 0 && visibleArea < 0.75F) {
                        index--;
                    }
                }
                this.smoothScrollToPosition(Math.max(0, Math.min(this.getCount(), index + position)));
            }
        }
    }

    public void scrollListBy(int y) {
        this.trackMotionScroll(-y, -y);
    }

    public boolean canScrollList(int direction) {
        int childCount = this.getChildCount();
        if (childCount == 0) {
            return false;
        } else {
            int firstPosition = this.mFirstPosition;
            Rect listPadding = this.mListPadding;
            if (direction > 0) {
                int lastBottom = this.getChildAt(childCount - 1).getBottom();
                int lastPosition = firstPosition + childCount;
                return lastPosition < this.mItemCount || lastBottom > this.getHeight() - listPadding.bottom;
            } else {
                int firstTop = this.getChildAt(0).getTop();
                return firstPosition > 0 || firstTop < listPadding.top;
            }
        }
    }

    boolean trackMotionScroll(int deltaY, int incrementalDeltaY) {
        int childCount = this.getChildCount();
        if (childCount == 0) {
            return true;
        } else {
            int firstTop = this.getChildAt(0).getTop();
            int lastBottom = this.getChildAt(childCount - 1).getBottom();
            Rect listPadding = this.mListPadding;
            int effectivePaddingTop = 0;
            int effectivePaddingBottom = 0;
            if (this.hasBooleanFlag(34)) {
                effectivePaddingTop = listPadding.top;
                effectivePaddingBottom = listPadding.bottom;
            }
            int spaceAbove = effectivePaddingTop - firstTop;
            int end = this.getHeight() - effectivePaddingBottom;
            int spaceBelow = lastBottom - end;
            int height = this.getHeight() - this.mPaddingBottom - this.mPaddingTop;
            if (deltaY < 0) {
                deltaY = Math.max(-(height - 1), deltaY);
            } else {
                deltaY = Math.min(height - 1, deltaY);
            }
            if (incrementalDeltaY < 0) {
                incrementalDeltaY = Math.max(-(height - 1), incrementalDeltaY);
            } else {
                incrementalDeltaY = Math.min(height - 1, incrementalDeltaY);
            }
            int firstPosition = this.mFirstPosition;
            if (firstPosition == 0) {
                this.mFirstPositionDistanceGuess = firstTop - listPadding.top;
            } else {
                this.mFirstPositionDistanceGuess += incrementalDeltaY;
            }
            if (firstPosition + childCount == this.mItemCount) {
                this.mLastPositionDistanceGuess = lastBottom + listPadding.bottom;
            } else {
                this.mLastPositionDistanceGuess += incrementalDeltaY;
            }
            boolean cannotScrollDown = firstPosition == 0 && firstTop >= listPadding.top && incrementalDeltaY >= 0;
            boolean cannotScrollUp = firstPosition + childCount == this.mItemCount && lastBottom <= this.getHeight() - listPadding.bottom && incrementalDeltaY <= 0;
            if (!cannotScrollDown && !cannotScrollUp) {
                boolean down = incrementalDeltaY < 0;
                boolean inTouchMode = this.isInTouchMode();
                if (inTouchMode) {
                    this.hideSelector();
                }
                int headerViewsCount = this.getHeaderViewsCount();
                int footerViewsStart = this.mItemCount - this.getFooterViewsCount();
                int start = 0;
                int count = 0;
                if (down) {
                    int top = -incrementalDeltaY;
                    if (this.hasBooleanFlag(34)) {
                        top += listPadding.top;
                    }
                    for (int i = 0; i < childCount; i++) {
                        View child = this.getChildAt(i);
                        if (child.getBottom() >= top) {
                            break;
                        }
                        count++;
                        int position = firstPosition + i;
                        if (position >= headerViewsCount && position < footerViewsStart) {
                            this.mRecycler.addScrapView(child, position);
                        }
                    }
                } else {
                    int bottom = this.getHeight() - incrementalDeltaY;
                    if (this.hasBooleanFlag(34)) {
                        bottom -= listPadding.bottom;
                    }
                    for (int i = childCount - 1; i >= 0; i--) {
                        View childx = this.getChildAt(i);
                        if (childx.getTop() <= bottom) {
                            break;
                        }
                        start = i;
                        count++;
                        int position = firstPosition + i;
                        if (position >= headerViewsCount && position < footerViewsStart) {
                            this.mRecycler.addScrapView(childx, position);
                        }
                    }
                }
                this.mMotionViewNewTop = this.mMotionViewOriginalTop + deltaY;
                this.mBlockLayoutRequests = true;
                if (count > 0) {
                    this.detachViewsFromParent(start, count);
                    this.mRecycler.removeSkippedScrap();
                }
                if (!this.awakenScrollBars()) {
                    this.invalidate();
                }
                this.offsetChildrenTopAndBottom(incrementalDeltaY);
                if (down) {
                    this.mFirstPosition += count;
                }
                int absIncrementalDeltaY = Math.abs(incrementalDeltaY);
                if (spaceAbove < absIncrementalDeltaY || spaceBelow < absIncrementalDeltaY) {
                    this.fillGap(down);
                }
                this.mRecycler.fullyDetachScrapViews();
                boolean selectorOnScreen = false;
                if (!inTouchMode && this.mSelectedPosition != -1) {
                    int childIndex = this.mSelectedPosition - this.mFirstPosition;
                    if (childIndex >= 0 && childIndex < this.getChildCount()) {
                        this.positionSelector(this.mSelectedPosition, this.getChildAt(childIndex));
                        selectorOnScreen = true;
                    }
                } else if (this.mSelectorPosition != -1) {
                    int childIndex = this.mSelectorPosition - this.mFirstPosition;
                    if (childIndex >= 0 && childIndex < this.getChildCount()) {
                        this.positionSelector(this.mSelectorPosition, this.getChildAt(childIndex));
                        selectorOnScreen = true;
                    }
                }
                if (!selectorOnScreen) {
                    this.mSelectorRect.setEmpty();
                }
                this.mBlockLayoutRequests = false;
                this.invokeOnItemScrollListener();
                return false;
            } else {
                return incrementalDeltaY != 0;
            }
        }
    }

    int getHeaderViewsCount() {
        return 0;
    }

    int getFooterViewsCount() {
        return 0;
    }

    abstract void fillGap(boolean var1);

    void hideSelector() {
        if (this.mSelectedPosition != -1) {
            if (this.mLayoutMode != 4) {
                this.mResurrectToPosition = this.mSelectedPosition;
            }
            if (this.mNextSelectedPosition >= 0 && this.mNextSelectedPosition != this.mSelectedPosition) {
                this.mResurrectToPosition = this.mNextSelectedPosition;
            }
            this.setSelectedPositionInt(-1);
            this.setNextSelectedPositionInt(-1);
            this.mSelectedTop = 0;
        }
    }

    int reconcileSelectedPosition() {
        int position = this.mSelectedPosition;
        if (position < 0) {
            position = this.mResurrectToPosition;
        }
        position = Math.max(0, position);
        return Math.min(position, this.mItemCount - 1);
    }

    abstract int findMotionRow(int var1);

    int findClosestMotionRow(int y) {
        int childCount = this.getChildCount();
        if (childCount == 0) {
            return -1;
        } else {
            int motionRow = this.findMotionRow(y);
            return motionRow != -1 ? motionRow : this.mFirstPosition + childCount - 1;
        }
    }

    public void invalidateViews() {
        this.mDataChanged = true;
        this.rememberSyncState();
        this.requestLayout();
        this.invalidate();
    }

    boolean resurrectSelectionIfNeeded() {
        if (this.mSelectedPosition < 0 && this.resurrectSelection()) {
            this.updateSelectorState();
            return true;
        } else {
            return false;
        }
    }

    abstract void setSelectionInt(int var1);

    boolean resurrectSelection() {
        int childCount = this.getChildCount();
        if (childCount <= 0) {
            return false;
        } else {
            int selectedTop = 0;
            int childrenTop = this.mListPadding.top;
            int childrenBottom = this.getHeight() - this.mListPadding.bottom;
            int firstPosition = this.mFirstPosition;
            int toPosition = this.mResurrectToPosition;
            boolean down = true;
            int selectedPos;
            if (toPosition >= firstPosition && toPosition < firstPosition + childCount) {
                selectedPos = toPosition;
                View selected = this.getChildAt(toPosition - this.mFirstPosition);
                selectedTop = selected.getTop();
                int selectedBottom = selected.getBottom();
                if (selectedTop < childrenTop) {
                    selectedTop = childrenTop;
                } else if (selectedBottom > childrenBottom) {
                    selectedTop = childrenBottom - selected.getMeasuredHeight();
                }
            } else if (toPosition < firstPosition) {
                selectedPos = firstPosition;
                for (int i = 0; i < childCount; i++) {
                    View v = this.getChildAt(i);
                    int top = v.getTop();
                    if (i == 0) {
                        selectedTop = top;
                    }
                    if (top >= childrenTop) {
                        selectedPos = firstPosition + i;
                        selectedTop = top;
                        break;
                    }
                }
            } else {
                int itemCount = this.mItemCount;
                down = false;
                selectedPos = firstPosition + childCount - 1;
                for (int i = childCount - 1; i >= 0; i--) {
                    View vx = this.getChildAt(i);
                    int topx = vx.getTop();
                    int bottom = vx.getBottom();
                    if (i == childCount - 1) {
                        selectedTop = topx;
                    }
                    if (bottom <= childrenBottom) {
                        selectedPos = firstPosition + i;
                        selectedTop = topx;
                        break;
                    }
                }
            }
            this.mResurrectToPosition = -1;
            this.removeCallbacks(this.mFlingRunnable);
            if (this.mPositionScroller != null) {
                this.mPositionScroller.stop();
            }
            this.mTouchMode = -1;
            this.mSpecificTop = selectedTop;
            selectedPos = this.lookForSelectablePosition(selectedPos, down);
            if (selectedPos >= firstPosition && selectedPos <= this.getLastVisiblePosition()) {
                this.mLayoutMode = 4;
                this.updateSelectorState();
                this.setSelectionInt(selectedPos);
                this.invokeOnItemScrollListener();
            } else {
                selectedPos = -1;
            }
            this.reportScrollStateChange(0);
            return selectedPos >= 0;
        }
    }

    void confirmCheckedPositionsById() {
        this.mCheckStates.clear();
        boolean checkedCountChanged = false;
        ObjectIterator<Entry> it = Long2IntMaps.fastIterator(this.mCheckedIdStates);
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            long id = entry.getLongKey();
            int lastPos = entry.getIntValue();
            long lastPosId = this.mAdapter.getItemId(lastPos);
            if (id != lastPosId) {
                int start = Math.max(0, lastPos - 20);
                int end = Math.min(lastPos + 20, this.mItemCount);
                boolean found = false;
                for (int searchPos = start; searchPos < end; searchPos++) {
                    long searchId = this.mAdapter.getItemId(searchPos);
                    if (id == searchId) {
                        found = true;
                        this.mCheckStates.put(searchPos, true);
                        entry.setValue(searchPos);
                        break;
                    }
                }
                if (!found) {
                    it.remove();
                    this.mCheckedItemCount--;
                    checkedCountChanged = true;
                    if (this.mChoiceActionMode != null && this.mMultiChoiceModeCallback != null) {
                        this.mMultiChoiceModeCallback.onItemCheckedStateChanged(this.mChoiceActionMode, lastPos, id, false);
                    }
                }
            } else {
                this.mCheckStates.put(lastPos, true);
            }
        }
        if (checkedCountChanged && this.mChoiceActionMode != null) {
            this.mChoiceActionMode.invalidate();
        }
    }

    @Override
    protected void handleDataChanged() {
        int count = this.mItemCount;
        int lastHandledItemCount = this.mLastHandledItemCount;
        this.mLastHandledItemCount = this.mItemCount;
        if (this.mChoiceMode != 0 && this.mAdapter != null && this.mAdapter.hasStableIds()) {
            this.confirmCheckedPositionsById();
        }
        this.mRecycler.clearTransientStateViews();
        if (count > 0) {
            if (this.mNeedSync) {
                this.mNeedSync = false;
                if (this.mTranscriptMode == 2) {
                    this.mLayoutMode = 3;
                    return;
                }
                if (this.mTranscriptMode == 1) {
                    if (this.mForceTranscriptScroll) {
                        this.mForceTranscriptScroll = false;
                        this.mLayoutMode = 3;
                        return;
                    }
                    int childCount = this.getChildCount();
                    int listBottom = this.getHeight() - this.getPaddingBottom();
                    View lastChild = this.getChildAt(childCount - 1);
                    int lastBottom = lastChild != null ? lastChild.getBottom() : listBottom;
                    if (this.mFirstPosition + childCount >= lastHandledItemCount && lastBottom <= listBottom) {
                        this.mLayoutMode = 3;
                        return;
                    }
                    this.awakenScrollBars();
                }
                switch(this.mSyncMode) {
                    case 0:
                        if (this.isInTouchMode()) {
                            this.mLayoutMode = 5;
                            this.mSyncPosition = Math.min(Math.max(0, this.mSyncPosition), count - 1);
                            return;
                        }
                        int newPos = this.findSyncPosition();
                        if (newPos >= 0) {
                            int selectablePos = this.lookForSelectablePosition(newPos, true);
                            if (selectablePos == newPos) {
                                this.mSyncPosition = newPos;
                                if (this.mSyncHeight == (long) this.getHeight()) {
                                    this.mLayoutMode = 5;
                                } else {
                                    this.mLayoutMode = 2;
                                }
                                this.setNextSelectedPositionInt(newPos);
                                return;
                            }
                        }
                        break;
                    case 1:
                        this.mLayoutMode = 5;
                        this.mSyncPosition = Math.min(Math.max(0, this.mSyncPosition), count - 1);
                        return;
                }
            }
            if (!this.isInTouchMode()) {
                int newPos = this.getSelectedItemPosition();
                if (newPos >= count) {
                    newPos = count - 1;
                }
                if (newPos < 0) {
                    newPos = 0;
                }
                int selectablePos = this.lookForSelectablePosition(newPos, true);
                if (selectablePos >= 0) {
                    this.setNextSelectedPositionInt(selectablePos);
                    return;
                }
                selectablePos = this.lookForSelectablePosition(newPos, false);
                if (selectablePos >= 0) {
                    this.setNextSelectedPositionInt(selectablePos);
                    return;
                }
            } else if (this.mResurrectToPosition >= 0) {
                return;
            }
        }
        this.mLayoutMode = this.mStackFromBottom ? 3 : 1;
        this.mSelectedPosition = -1;
        this.mSelectedRowId = Long.MIN_VALUE;
        this.mNextSelectedPosition = -1;
        this.mNextSelectedRowId = Long.MIN_VALUE;
        this.mNeedSync = false;
        this.mSelectorPosition = -1;
        this.checkSelectionChanged();
    }

    static int getDistance(@NonNull Rect source, @NonNull Rect dest, int direction) {
        int sX;
        int sY;
        int dX;
        int dY;
        switch(direction) {
            case 1:
            case 2:
                sX = source.right + source.width() / 2;
                sY = source.top + source.height() / 2;
                dX = dest.left + dest.width() / 2;
                dY = dest.top + dest.height() / 2;
                break;
            case 17:
                sX = source.left;
                sY = source.top + source.height() / 2;
                dX = dest.right;
                dY = dest.top + dest.height() / 2;
                break;
            case 33:
                sX = source.left + source.width() / 2;
                sY = source.top;
                dX = dest.left + dest.width() / 2;
                dY = dest.bottom;
                break;
            case 66:
                sX = source.right;
                sY = source.top + source.height() / 2;
                dX = dest.left;
                dY = dest.top + dest.height() / 2;
                break;
            case 130:
                sX = source.left + source.width() / 2;
                sY = source.bottom;
                dX = dest.left + dest.width() / 2;
                dY = dest.top;
                break;
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT, FOCUS_FORWARD, FOCUS_BACKWARD}.");
        }
        int deltaX = dX - sX;
        int deltaY = dY - sY;
        return deltaY * deltaY + deltaX * deltaX;
    }

    @Override
    public void onFilterComplete(int count) {
        if (this.mSelectedPosition < 0 && count > 0) {
            this.mResurrectToPosition = -1;
            this.resurrectSelection();
        }
    }

    @NonNull
    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new AbsListView.LayoutParams(-1, -2, 0);
    }

    @NonNull
    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(@NonNull ViewGroup.LayoutParams p) {
        return new AbsListView.LayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof AbsListView.LayoutParams;
    }

    public void setTranscriptMode(int mode) {
        this.mTranscriptMode = mode;
    }

    public int getTranscriptMode() {
        return this.mTranscriptMode;
    }

    public void reclaimViews(@NonNull List<View> views) {
        int childCount = this.getChildCount();
        AbsListView.RecyclerListener listener = this.mRecycler.mRecyclerListener;
        for (int i = 0; i < childCount; i++) {
            View child = this.getChildAt(i);
            AbsListView.LayoutParams lp = (AbsListView.LayoutParams) child.getLayoutParams();
            if (lp != null && this.mRecycler.shouldRecycleViewType(lp.viewType)) {
                views.add(child);
                if (listener != null) {
                    listener.onMovedToScrapHeap(child);
                }
            }
        }
        this.mRecycler.reclaimScrapViews(views);
        this.removeAllViewsInLayout();
    }

    private void finishGlows() {
        if (this.shouldDisplayEdgeEffects()) {
            this.mEdgeGlowTop.finish();
            this.mEdgeGlowBottom.finish();
        }
    }

    public void setEdgeEffectColor(int color) {
        this.setTopEdgeEffectColor(color);
        this.setBottomEdgeEffectColor(color);
    }

    public void setBottomEdgeEffectColor(int color) {
        this.mEdgeGlowBottom.setColor(color);
        this.invalidateBottomGlow();
    }

    public void setTopEdgeEffectColor(int color) {
        this.mEdgeGlowTop.setColor(color);
        this.invalidateTopGlow();
    }

    public int getTopEdgeEffectColor() {
        return this.mEdgeGlowTop.getColor();
    }

    public int getBottomEdgeEffectColor() {
        return this.mEdgeGlowBottom.getColor();
    }

    public void setRecyclerListener(@Nullable AbsListView.RecyclerListener listener) {
        this.mRecycler.mRecyclerListener = listener;
    }

    int getHeightForPosition(int position) {
        int firstVisiblePosition = this.getFirstVisiblePosition();
        int childCount = this.getChildCount();
        int index = position - firstVisiblePosition;
        if (index >= 0 && index < childCount) {
            View view = this.getChildAt(index);
            return view.getHeight();
        } else {
            View view = this.obtainView(position, this.mIsScrap);
            view.measure(this.mWidthMeasureSpec, 0);
            int height = view.getMeasuredHeight();
            this.mRecycler.addScrapView(view, position);
            return height;
        }
    }

    public void setSelectionFromTop(int position, int y) {
        if (this.mAdapter != null) {
            if (!this.isInTouchMode()) {
                position = this.lookForSelectablePosition(position, true);
                if (position >= 0) {
                    this.setNextSelectedPositionInt(position);
                }
            } else {
                this.mResurrectToPosition = position;
            }
            if (position >= 0) {
                this.mLayoutMode = 4;
                this.mSpecificTop = this.mListPadding.top + y;
                if (this.mNeedSync) {
                    this.mSyncPosition = position;
                    this.mSyncRowId = this.mAdapter.getItemId(position);
                }
                if (this.mPositionScroller != null) {
                    this.mPositionScroller.stop();
                }
                this.requestLayout();
            }
        }
    }

    private class CheckForKeyLongPress extends AbsListView.WindowRunnable implements Runnable {

        public void run() {
            if (AbsListView.this.isPressed() && AbsListView.this.mSelectedPosition >= 0) {
                int index = AbsListView.this.mSelectedPosition - AbsListView.this.mFirstPosition;
                View v = AbsListView.this.getChildAt(index);
                if (!AbsListView.this.mDataChanged) {
                    boolean handled = false;
                    if (this.sameWindow()) {
                        handled = AbsListView.this.performLongPress(v, AbsListView.this.mSelectedPosition, AbsListView.this.mSelectedRowId);
                    }
                    if (handled) {
                        AbsListView.this.setPressed(false);
                        v.setPressed(false);
                    }
                } else {
                    AbsListView.this.setPressed(false);
                    if (v != null) {
                        v.setPressed(false);
                    }
                }
            }
        }
    }

    private class CheckForLongPress extends AbsListView.WindowRunnable implements Runnable {

        private float mX = Float.NaN;

        private float mY = Float.NaN;

        private void setCoords(float x, float y) {
            this.mX = x;
            this.mY = y;
        }

        public void run() {
            int motionPosition = AbsListView.this.mMotionPosition;
            View child = AbsListView.this.getChildAt(motionPosition - AbsListView.this.mFirstPosition);
            if (child != null) {
                int longPressPosition = AbsListView.this.mMotionPosition;
                long longPressId = AbsListView.this.mAdapter.getItemId(AbsListView.this.mMotionPosition);
                boolean handled = false;
                if (this.sameWindow() && !AbsListView.this.mDataChanged) {
                    if (!Float.isNaN(this.mX) && !Float.isNaN(this.mY)) {
                        handled = AbsListView.this.performLongPress(child, longPressPosition, longPressId, this.mX, this.mY);
                    } else {
                        handled = AbsListView.this.performLongPress(child, longPressPosition, longPressId);
                    }
                }
                if (handled) {
                    AbsListView.this.mHasPerformedLongPress = true;
                    AbsListView.this.mTouchMode = -1;
                    AbsListView.this.setPressed(false);
                    child.setPressed(false);
                } else {
                    AbsListView.this.mTouchMode = 2;
                }
            }
        }
    }

    private final class CheckForTap implements Runnable {

        float x;

        float y;

        public void run() {
            if (AbsListView.this.mTouchMode == 0) {
                AbsListView.this.mTouchMode = 1;
                View child = AbsListView.this.getChildAt(AbsListView.this.mMotionPosition - AbsListView.this.mFirstPosition);
                if (child != null && !child.hasExplicitFocusable()) {
                    AbsListView.this.mLayoutMode = 0;
                    if (!AbsListView.this.mDataChanged) {
                        float[] point = AbsListView.this.mTmpPoint;
                        point[0] = this.x;
                        point[1] = this.y;
                        AbsListView.this.transformPointToViewLocal(point, child);
                        child.drawableHotspotChanged(point[0], point[1]);
                        child.setPressed(true);
                        AbsListView.this.setPressed(true);
                        AbsListView.this.layoutChildren();
                        AbsListView.this.positionSelector(AbsListView.this.mMotionPosition, child);
                        AbsListView.this.refreshDrawableState();
                        int longPressTimeout = ViewConfiguration.getLongPressTimeout();
                        boolean longClickable = AbsListView.this.isLongClickable();
                        if (AbsListView.this.mSelector != null) {
                            Drawable d = AbsListView.this.mSelector.getCurrent();
                            AbsListView.this.mSelector.setHotspot(this.x, this.y);
                        }
                        if (longClickable) {
                            if (AbsListView.this.mPendingCheckForLongPress == null) {
                                AbsListView.this.mPendingCheckForLongPress = AbsListView.this.new CheckForLongPress();
                            }
                            AbsListView.this.mPendingCheckForLongPress.setCoords(this.x, this.y);
                            AbsListView.this.mPendingCheckForLongPress.rememberWindowAttachCount();
                            AbsListView.this.postDelayed(AbsListView.this.mPendingCheckForLongPress, (long) longPressTimeout);
                        } else {
                            AbsListView.this.mTouchMode = 2;
                        }
                    } else {
                        AbsListView.this.mTouchMode = 2;
                    }
                }
            }
        }
    }

    class DefaultPositionScroller implements AbsListView.PositionScroller, Runnable {

        private static final int SCROLL_DURATION = 200;

        private static final int MOVE_DOWN_POS = 1;

        private static final int MOVE_UP_POS = 2;

        private static final int MOVE_DOWN_BOUND = 3;

        private static final int MOVE_UP_BOUND = 4;

        private static final int MOVE_OFFSET = 5;

        private int mMode;

        private int mTargetPos;

        private int mBoundPos;

        private int mLastSeenPos;

        private int mScrollDuration;

        private final int mExtraScroll = 0;

        private int mOffsetFromTop;

        @Override
        public void start(int position) {
            this.stop();
            if (AbsListView.this.mDataChanged) {
                AbsListView.this.mPositionScrollAfterLayout = () -> this.start(position);
            } else {
                int childCount = AbsListView.this.getChildCount();
                if (childCount != 0) {
                    int firstPos = AbsListView.this.mFirstPosition;
                    int lastPos = firstPos + childCount - 1;
                    int clampedPosition = Math.max(0, Math.min(AbsListView.this.getCount() - 1, position));
                    int viewTravelCount;
                    if (clampedPosition < firstPos) {
                        viewTravelCount = firstPos - clampedPosition + 1;
                        this.mMode = 2;
                    } else {
                        if (clampedPosition <= lastPos) {
                            this.scrollToVisible(clampedPosition, -1);
                            return;
                        }
                        viewTravelCount = clampedPosition - lastPos + 1;
                        this.mMode = 1;
                    }
                    if (viewTravelCount > 0) {
                        this.mScrollDuration = 200 / viewTravelCount;
                    } else {
                        this.mScrollDuration = 200;
                    }
                    this.mTargetPos = clampedPosition;
                    this.mBoundPos = -1;
                    this.mLastSeenPos = -1;
                    AbsListView.this.postOnAnimation(this);
                }
            }
        }

        @Override
        public void start(int position, int boundPosition) {
            this.stop();
            if (boundPosition == -1) {
                this.start(position);
            } else if (AbsListView.this.mDataChanged) {
                AbsListView.this.mPositionScrollAfterLayout = () -> this.start(position, boundPosition);
            } else {
                int childCount = AbsListView.this.getChildCount();
                if (childCount != 0) {
                    int firstPos = AbsListView.this.mFirstPosition;
                    int lastPos = firstPos + childCount - 1;
                    int clampedPosition = Math.max(0, Math.min(AbsListView.this.getCount() - 1, position));
                    int viewTravelCount;
                    if (clampedPosition < firstPos) {
                        int boundPosFromLast = lastPos - boundPosition;
                        if (boundPosFromLast < 1) {
                            return;
                        }
                        int posTravel = firstPos - clampedPosition + 1;
                        int boundTravel = boundPosFromLast - 1;
                        if (boundTravel < posTravel) {
                            viewTravelCount = boundTravel;
                            this.mMode = 4;
                        } else {
                            viewTravelCount = posTravel;
                            this.mMode = 2;
                        }
                    } else {
                        if (clampedPosition <= lastPos) {
                            this.scrollToVisible(clampedPosition, boundPosition);
                            return;
                        }
                        int boundPosFromFirst = boundPosition - firstPos;
                        if (boundPosFromFirst < 1) {
                            return;
                        }
                        int posTravel = clampedPosition - lastPos + 1;
                        int boundTravel = boundPosFromFirst - 1;
                        if (boundTravel < posTravel) {
                            viewTravelCount = boundTravel;
                            this.mMode = 3;
                        } else {
                            viewTravelCount = posTravel;
                            this.mMode = 1;
                        }
                    }
                    if (viewTravelCount > 0) {
                        this.mScrollDuration = 200 / viewTravelCount;
                    } else {
                        this.mScrollDuration = 200;
                    }
                    this.mTargetPos = clampedPosition;
                    this.mBoundPos = boundPosition;
                    this.mLastSeenPos = -1;
                    AbsListView.this.postOnAnimation(this);
                }
            }
        }

        @Override
        public void startWithOffset(int position, int offset) {
            this.startWithOffset(position, offset, 200);
        }

        @Override
        public void startWithOffset(int position, int offset, int duration) {
            this.stop();
            if (AbsListView.this.mDataChanged) {
                AbsListView.this.mPositionScrollAfterLayout = () -> this.startWithOffset(position, offset, duration);
            } else {
                int childCount = AbsListView.this.getChildCount();
                if (childCount != 0) {
                    offset += AbsListView.this.getPaddingTop();
                    this.mTargetPos = Math.max(0, Math.min(AbsListView.this.getCount() - 1, position));
                    this.mOffsetFromTop = offset;
                    this.mBoundPos = -1;
                    this.mLastSeenPos = -1;
                    this.mMode = 5;
                    int firstPos = AbsListView.this.mFirstPosition;
                    int lastPos = firstPos + childCount - 1;
                    int viewTravelCount;
                    if (this.mTargetPos < firstPos) {
                        viewTravelCount = firstPos - this.mTargetPos;
                    } else {
                        if (this.mTargetPos <= lastPos) {
                            int targetTop = AbsListView.this.getChildAt(this.mTargetPos - firstPos).getTop();
                            AbsListView.this.smoothScrollBy(targetTop - offset, duration, true, false);
                            return;
                        }
                        viewTravelCount = this.mTargetPos - lastPos;
                    }
                    float screenTravelCount = (float) viewTravelCount / (float) childCount;
                    this.mScrollDuration = screenTravelCount < 1.0F ? duration : (int) ((float) duration / screenTravelCount);
                    AbsListView.this.postOnAnimation(this);
                }
            }
        }

        private void scrollToVisible(int targetPos, int boundPos) {
            int firstPos = AbsListView.this.mFirstPosition;
            int childCount = AbsListView.this.getChildCount();
            int lastPos = firstPos + childCount - 1;
            int paddedTop = AbsListView.this.mListPadding.top;
            int paddedBottom = AbsListView.this.getHeight() - AbsListView.this.mListPadding.bottom;
            if (targetPos < firstPos || targetPos > lastPos) {
                ModernUI.LOGGER.warn(AbsListView.MARKER, "scrollToVisible called with targetPos " + targetPos + " not visible [" + firstPos + ", " + lastPos + "]");
            }
            if (boundPos < firstPos || boundPos > lastPos) {
                boundPos = -1;
            }
            View targetChild = AbsListView.this.getChildAt(targetPos - firstPos);
            int targetTop = targetChild.getTop();
            int targetBottom = targetChild.getBottom();
            int scrollBy = 0;
            if (targetBottom > paddedBottom) {
                scrollBy = targetBottom - paddedBottom;
            }
            if (targetTop < paddedTop) {
                scrollBy = targetTop - paddedTop;
            }
            if (scrollBy != 0) {
                if (boundPos >= 0) {
                    View boundChild = AbsListView.this.getChildAt(boundPos - firstPos);
                    int boundTop = boundChild.getTop();
                    int boundBottom = boundChild.getBottom();
                    int absScroll = Math.abs(scrollBy);
                    if (scrollBy < 0 && boundBottom + absScroll > paddedBottom) {
                        scrollBy = Math.max(0, boundBottom - paddedBottom);
                    } else if (scrollBy > 0 && boundTop - absScroll < paddedTop) {
                        scrollBy = Math.min(0, boundTop - paddedTop);
                    }
                }
                AbsListView.this.smoothScrollBy(scrollBy, 200);
            }
        }

        @Override
        public void stop() {
            AbsListView.this.removeCallbacks(this);
        }

        public void run() {
            int listHeight = AbsListView.this.getHeight();
            int firstPos = AbsListView.this.mFirstPosition;
            switch(this.mMode) {
                case 1:
                    int lastViewIndexx = AbsListView.this.getChildCount() - 1;
                    int lastPosxx = firstPos + lastViewIndexx;
                    if (lastViewIndexx < 0) {
                        return;
                    }
                    if (lastPosxx == this.mLastSeenPos) {
                        AbsListView.this.postOnAnimation(this);
                        return;
                    }
                    View lastView = AbsListView.this.getChildAt(lastViewIndexx);
                    int lastViewHeight = lastView.getHeight();
                    int lastViewTop = lastView.getTop();
                    int lastViewPixelsShowing = listHeight - lastViewTop;
                    int extraScroll = lastPosxx < AbsListView.this.mItemCount - 1 ? Math.max(AbsListView.this.mListPadding.bottom, this.mExtraScroll) : AbsListView.this.mListPadding.bottom;
                    int scrollBy = lastViewHeight - lastViewPixelsShowing + extraScroll;
                    AbsListView.this.smoothScrollBy(scrollBy, this.mScrollDuration, true, lastPosxx < this.mTargetPos);
                    this.mLastSeenPos = lastPosxx;
                    if (lastPosxx < this.mTargetPos) {
                        AbsListView.this.postOnAnimation(this);
                    }
                    break;
                case 2:
                    if (firstPos == this.mLastSeenPos) {
                        AbsListView.this.postOnAnimation(this);
                        return;
                    }
                    View firstView = AbsListView.this.getChildAt(0);
                    if (firstView == null) {
                        return;
                    }
                    int firstViewTop = firstView.getTop();
                    int extraScroll = firstPos > 0 ? Math.max(this.mExtraScroll, AbsListView.this.mListPadding.top) : AbsListView.this.mListPadding.top;
                    AbsListView.this.smoothScrollBy(firstViewTop - extraScroll, this.mScrollDuration, true, firstPos > this.mTargetPos);
                    this.mLastSeenPos = firstPos;
                    if (firstPos > this.mTargetPos) {
                        AbsListView.this.postOnAnimation(this);
                    }
                    break;
                case 3:
                    int nextViewIndex = 1;
                    int childCountx = AbsListView.this.getChildCount();
                    if (firstPos == this.mBoundPos || childCountx <= 1 || firstPos + childCountx >= AbsListView.this.mItemCount) {
                        AbsListView.this.reportScrollStateChange(0);
                        return;
                    }
                    int nextPos = firstPos + 1;
                    if (nextPos == this.mLastSeenPos) {
                        AbsListView.this.postOnAnimation(this);
                        return;
                    }
                    View nextView = AbsListView.this.getChildAt(1);
                    int nextViewHeight = nextView.getHeight();
                    int nextViewTop = nextView.getTop();
                    int extraScroll = Math.max(AbsListView.this.mListPadding.bottom, this.mExtraScroll);
                    if (nextPos < this.mBoundPos) {
                        AbsListView.this.smoothScrollBy(Math.max(0, nextViewHeight + nextViewTop - extraScroll), this.mScrollDuration, true, true);
                        this.mLastSeenPos = nextPos;
                        AbsListView.this.postOnAnimation(this);
                    } else if (nextViewTop > extraScroll) {
                        AbsListView.this.smoothScrollBy(nextViewTop - extraScroll, this.mScrollDuration, true, false);
                    } else {
                        AbsListView.this.reportScrollStateChange(0);
                    }
                    break;
                case 4:
                    int lastViewIndex = AbsListView.this.getChildCount() - 2;
                    if (lastViewIndex < 0) {
                        return;
                    }
                    int lastPosx = firstPos + lastViewIndex;
                    if (lastPosx == this.mLastSeenPos) {
                        AbsListView.this.postOnAnimation(this);
                        return;
                    }
                    View lastView = AbsListView.this.getChildAt(lastViewIndex);
                    int lastViewHeight = lastView.getHeight();
                    int lastViewTop = lastView.getTop();
                    int lastViewPixelsShowing = listHeight - lastViewTop;
                    int extraScroll = Math.max(AbsListView.this.mListPadding.top, this.mExtraScroll);
                    this.mLastSeenPos = lastPosx;
                    if (lastPosx > this.mBoundPos) {
                        AbsListView.this.smoothScrollBy(-(lastViewPixelsShowing - extraScroll), this.mScrollDuration, true, true);
                        AbsListView.this.postOnAnimation(this);
                    } else {
                        int bottom = listHeight - extraScroll;
                        int lastViewBottom = lastViewTop + lastViewHeight;
                        if (bottom > lastViewBottom) {
                            AbsListView.this.smoothScrollBy(-(bottom - lastViewBottom), this.mScrollDuration, true, false);
                        } else {
                            AbsListView.this.reportScrollStateChange(0);
                        }
                    }
                    break;
                case 5:
                    if (this.mLastSeenPos == firstPos) {
                        AbsListView.this.postOnAnimation(this);
                        return;
                    }
                    this.mLastSeenPos = firstPos;
                    int childCount = AbsListView.this.getChildCount();
                    if (childCount <= 0) {
                        return;
                    }
                    int position = this.mTargetPos;
                    int lastPos = firstPos + childCount - 1;
                    View firstChild = AbsListView.this.getChildAt(0);
                    int firstChildHeight = firstChild.getHeight();
                    View lastChild = AbsListView.this.getChildAt(childCount - 1);
                    int lastChildHeight = lastChild.getHeight();
                    float firstPositionVisiblePart = (float) firstChildHeight == 0.0F ? 1.0F : (float) (firstChildHeight + firstChild.getTop()) / (float) firstChildHeight;
                    float lastPositionVisiblePart = (float) lastChildHeight == 0.0F ? 1.0F : (float) (lastChildHeight + AbsListView.this.getHeight() - lastChild.getBottom()) / (float) lastChildHeight;
                    float viewTravelCount = 0.0F;
                    if (position < firstPos) {
                        viewTravelCount = (float) (firstPos - position) + (1.0F - firstPositionVisiblePart) + 1.0F;
                    } else if (position > lastPos) {
                        viewTravelCount = (float) (position - lastPos) + (1.0F - lastPositionVisiblePart);
                    }
                    float screenTravelCount = viewTravelCount / (float) childCount;
                    float modifier = Math.min(Math.abs(screenTravelCount), 1.0F);
                    if (position < firstPos) {
                        int distance = (int) ((float) (-AbsListView.this.getHeight()) * modifier);
                        int duration = (int) ((float) this.mScrollDuration * modifier);
                        AbsListView.this.smoothScrollBy(distance, duration, true, true);
                        AbsListView.this.postOnAnimation(this);
                    } else if (position > lastPos) {
                        int distance = (int) ((float) AbsListView.this.getHeight() * modifier);
                        int duration = (int) ((float) this.mScrollDuration * modifier);
                        AbsListView.this.smoothScrollBy(distance, duration, true, true);
                        AbsListView.this.postOnAnimation(this);
                    } else {
                        int targetTop = AbsListView.this.getChildAt(position - firstPos).getTop();
                        int distance = targetTop - this.mOffsetFromTop;
                        int duration = (int) ((float) this.mScrollDuration * ((float) Math.abs(distance) / (float) AbsListView.this.getHeight()));
                        AbsListView.this.smoothScrollBy(distance, duration, true, false);
                    }
            }
        }
    }

    private class FlingRunnable implements Runnable {

        private final OverScroller mScroller;

        private int mLastFlingY;

        private boolean mSuppressIdleStateChangeCall;

        private final Runnable mCheckFlywheel = new Runnable() {

            public void run() {
                VelocityTracker vt = AbsListView.this.mVelocityTracker;
                if (vt != null) {
                    vt.computeCurrentVelocity(1000, (float) AbsListView.this.mMaximumVelocity);
                    float yvel = -vt.getYVelocity();
                    if (Math.abs(yvel) >= (float) AbsListView.this.mMinimumVelocity && FlingRunnable.this.mScroller.isScrollingInDirection(0.0F, yvel)) {
                        AbsListView.this.postDelayed(this, 40L);
                    } else {
                        FlingRunnable.this.endFling();
                        AbsListView.this.mTouchMode = 3;
                        AbsListView.this.reportScrollStateChange(1);
                    }
                }
            }
        };

        private static final int FLYWHEEL_TIMEOUT = 40;

        FlingRunnable() {
            this.mScroller = new OverScroller();
        }

        void start(int initialVelocity) {
            int initialY = initialVelocity < 0 ? Integer.MAX_VALUE : 0;
            this.mLastFlingY = initialY;
            this.mScroller.setInterpolator(null);
            this.mScroller.fling(0, initialY, 0, initialVelocity, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE);
            AbsListView.this.mTouchMode = 4;
            this.mSuppressIdleStateChangeCall = false;
            AbsListView.this.removeCallbacks(this);
            AbsListView.this.postOnAnimation(this);
        }

        void startSpringback() {
            this.mSuppressIdleStateChangeCall = false;
            if (this.mScroller.springBack(0, AbsListView.this.mScrollY, 0, 0, 0, 0)) {
                AbsListView.this.mTouchMode = 6;
                AbsListView.this.invalidate();
                AbsListView.this.postOnAnimation(this);
            } else {
                AbsListView.this.mTouchMode = -1;
                AbsListView.this.reportScrollStateChange(0);
            }
        }

        void startOverfling(int initialVelocity) {
            this.mScroller.fling(0, AbsListView.this.mScrollY, 0, initialVelocity, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, AbsListView.this.getHeight());
            AbsListView.this.mTouchMode = 6;
            this.mSuppressIdleStateChangeCall = false;
            AbsListView.this.invalidate();
            AbsListView.this.postOnAnimation(this);
        }

        void edgeReached(int delta) {
            this.mScroller.notifyVerticalEdgeReached(AbsListView.this.mScrollY, 0, AbsListView.this.mOverflingDistance);
            int overscrollMode = AbsListView.this.getOverScrollMode();
            if (overscrollMode == 0 || overscrollMode == 1 && !AbsListView.this.contentFits()) {
                AbsListView.this.mTouchMode = 6;
                int vel = (int) this.mScroller.getCurrVelocity();
                if (delta > 0) {
                    AbsListView.this.mEdgeGlowTop.onAbsorb(vel);
                } else {
                    AbsListView.this.mEdgeGlowBottom.onAbsorb(vel);
                }
            } else {
                AbsListView.this.mTouchMode = -1;
                if (AbsListView.this.mPositionScroller != null) {
                    AbsListView.this.mPositionScroller.stop();
                }
            }
            AbsListView.this.invalidate();
            AbsListView.this.postOnAnimation(this);
        }

        void startScroll(int distance, int duration, boolean linear, boolean suppressEndFlingStateChangeCall) {
            int initialY = distance < 0 ? Integer.MAX_VALUE : 0;
            this.mLastFlingY = initialY;
            this.mScroller.setInterpolator(linear ? TimeInterpolator.LINEAR : null);
            this.mScroller.startScroll(0, initialY, 0, distance, duration);
            AbsListView.this.mTouchMode = 4;
            this.mSuppressIdleStateChangeCall = suppressEndFlingStateChangeCall;
            AbsListView.this.postOnAnimation(this);
        }

        void endFling() {
            AbsListView.this.mTouchMode = -1;
            AbsListView.this.removeCallbacks(this);
            AbsListView.this.removeCallbacks(this.mCheckFlywheel);
            if (!this.mSuppressIdleStateChangeCall) {
                AbsListView.this.reportScrollStateChange(0);
            }
            this.mScroller.abortAnimation();
        }

        void flywheelTouch() {
            AbsListView.this.postDelayed(this.mCheckFlywheel, 40L);
        }

        public void run() {
            switch(AbsListView.this.mTouchMode) {
                case 3:
                    if (this.mScroller.isFinished()) {
                        return;
                    }
                case 4:
                    if (AbsListView.this.mDataChanged) {
                        AbsListView.this.layoutChildren();
                    }
                    if (AbsListView.this.mItemCount == 0 || AbsListView.this.getChildCount() == 0) {
                        this.endFling();
                        return;
                    }
                    OverScroller scrollerx = this.mScroller;
                    boolean more = scrollerx.computeScrollOffset();
                    int y = scrollerx.getCurrY();
                    int delta = this.mLastFlingY - y;
                    if (delta > 0) {
                        AbsListView.this.mMotionPosition = AbsListView.this.mFirstPosition;
                        View firstView = AbsListView.this.getChildAt(0);
                        AbsListView.this.mMotionViewOriginalTop = firstView.getTop();
                        delta = Math.min(AbsListView.this.getHeight() - AbsListView.this.mPaddingBottom - AbsListView.this.mPaddingTop - 1, delta);
                    } else {
                        int offsetToLast = AbsListView.this.getChildCount() - 1;
                        AbsListView.this.mMotionPosition = AbsListView.this.mFirstPosition + offsetToLast;
                        View lastView = AbsListView.this.getChildAt(offsetToLast);
                        AbsListView.this.mMotionViewOriginalTop = lastView.getTop();
                        delta = Math.max(-(AbsListView.this.getHeight() - AbsListView.this.mPaddingBottom - AbsListView.this.mPaddingTop - 1), delta);
                    }
                    View motionView = AbsListView.this.getChildAt(AbsListView.this.mMotionPosition - AbsListView.this.mFirstPosition);
                    int oldTop = 0;
                    if (motionView != null) {
                        oldTop = motionView.getTop();
                    }
                    boolean atEdge = AbsListView.this.trackMotionScroll(delta, delta);
                    boolean atEnd = atEdge && delta != 0;
                    if (atEnd) {
                        if (motionView != null) {
                            int overshoot = -(delta - (motionView.getTop() - oldTop));
                            AbsListView.this.overScrollBy(0, overshoot, 0, AbsListView.this.mScrollY, 0, 0, 0, AbsListView.this.mOverflingDistance, false);
                        }
                        if (more) {
                            this.edgeReached(delta);
                        }
                    } else if (more) {
                        if (atEdge) {
                            AbsListView.this.invalidate();
                        }
                        this.mLastFlingY = y;
                        AbsListView.this.postOnAnimation(this);
                    } else {
                        this.endFling();
                    }
                    break;
                case 5:
                default:
                    this.endFling();
                    return;
                case 6:
                    OverScroller scroller = this.mScroller;
                    if (scroller.computeScrollOffset()) {
                        int scrollY = AbsListView.this.mScrollY;
                        int currY = scroller.getCurrY();
                        int deltaY = currY - scrollY;
                        if (AbsListView.this.overScrollBy(0, deltaY, 0, scrollY, 0, 0, 0, AbsListView.this.mOverflingDistance, false)) {
                            boolean crossDown = scrollY <= 0 && currY > 0;
                            boolean crossUp = scrollY >= 0 && currY < 0;
                            if (!crossDown && !crossUp) {
                                this.startSpringback();
                            } else {
                                int velocity = (int) scroller.getCurrVelocity();
                                if (crossUp) {
                                    velocity = -velocity;
                                }
                                scroller.abortAnimation();
                                this.start(velocity);
                            }
                        } else {
                            AbsListView.this.invalidate();
                            AbsListView.this.postOnAnimation(this);
                        }
                    } else {
                        this.endFling();
                    }
            }
        }
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {

        int viewType;

        boolean recycledHeaderFooter;

        boolean forceAdd;

        int scrappedFromPosition;

        long itemId = -1L;

        boolean isEnabled;

        public LayoutParams(int w, int h) {
            super(w, h);
        }

        public LayoutParams(int w, int h, int viewType) {
            super(w, h);
            this.viewType = viewType;
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

    public interface MultiChoiceModeListener extends ActionMode.Callback {

        void onItemCheckedStateChanged(@NonNull ActionMode var1, int var2, long var3, boolean var5);
    }

    class MultiChoiceModeWrapper implements AbsListView.MultiChoiceModeListener {

        private AbsListView.MultiChoiceModeListener mWrapped;

        public void setWrapped(AbsListView.MultiChoiceModeListener wrapped) {
            this.mWrapped = wrapped;
        }

        public boolean hasWrappedCallback() {
            return this.mWrapped != null;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            if (this.mWrapped.onCreateActionMode(mode, menu)) {
                AbsListView.this.setLongClickable(false);
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return this.mWrapped.onPrepareActionMode(mode, menu);
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return this.mWrapped.onActionItemClicked(mode, item);
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            this.mWrapped.onDestroyActionMode(mode);
            AbsListView.this.mChoiceActionMode = null;
            AbsListView.this.clearChoices();
            AbsListView.this.mDataChanged = true;
            AbsListView.this.rememberSyncState();
            AbsListView.this.requestLayout();
            AbsListView.this.setLongClickable(true);
        }

        @Override
        public void onItemCheckedStateChanged(@NonNull ActionMode mode, int position, long id, boolean checked) {
            this.mWrapped.onItemCheckedStateChanged(mode, position, id, checked);
            if (AbsListView.this.getCheckedItemCount() == 0) {
                mode.finish();
            }
        }
    }

    public interface OnScrollListener {

        int SCROLL_STATE_IDLE = 0;

        int SCROLL_STATE_TOUCH_SCROLL = 1;

        int SCROLL_STATE_FLING = 2;

        void onScrollStateChanged(AbsListView var1, int var2);

        void onScroll(AbsListView var1, int var2, int var3, int var4);
    }

    private class PerformClick extends AbsListView.WindowRunnable implements Runnable {

        int mClickMotionPosition;

        public void run() {
            if (!AbsListView.this.mDataChanged) {
                ListAdapter adapter = AbsListView.this.mAdapter;
                int motionPosition = this.mClickMotionPosition;
                if (adapter != null && AbsListView.this.mItemCount > 0 && motionPosition != -1 && motionPosition < adapter.getCount() && this.sameWindow() && adapter.isEnabled(motionPosition)) {
                    View view = AbsListView.this.getChildAt(motionPosition - AbsListView.this.mFirstPosition);
                    if (view != null) {
                        AbsListView.this.performItemClick(view, motionPosition, adapter.getItemId(motionPosition));
                    }
                }
            }
        }
    }

    interface PositionScroller {

        void start(int var1);

        void start(int var1, int var2);

        void startWithOffset(int var1, int var2);

        void startWithOffset(int var1, int var2, int var3);

        void stop();
    }

    class RecycleBin {

        @Nullable
        private AbsListView.RecyclerListener mRecyclerListener;

        private int mFirstActivePosition;

        private View[] mActiveViews = new View[0];

        private ArrayList<View>[] mScrapViews;

        private int mViewTypeCount;

        private ArrayList<View> mCurrentScrap;

        private ArrayList<View> mSkippedScrap;

        private SparseArray<View> mTransientStateViews;

        private LongSparseArray<View> mTransientStateViewsById;

        public void setViewTypeCount(int viewTypeCount) {
            if (viewTypeCount < 1) {
                throw new IllegalArgumentException("Can't have a viewTypeCount < 1");
            } else {
                ArrayList<View>[] scrapViews = new ArrayList[viewTypeCount];
                for (int i = 0; i < viewTypeCount; i++) {
                    scrapViews[i] = new ArrayList();
                }
                this.mViewTypeCount = viewTypeCount;
                this.mCurrentScrap = scrapViews[0];
                this.mScrapViews = scrapViews;
            }
        }

        public void markChildrenDirty() {
            if (this.mViewTypeCount == 1) {
                for (View view : this.mCurrentScrap) {
                    view.forceLayout();
                }
            } else {
                int typeCount = this.mViewTypeCount;
                for (int i = 0; i < typeCount; i++) {
                    for (View view : this.mScrapViews[i]) {
                        view.forceLayout();
                    }
                }
            }
            if (this.mTransientStateViews != null) {
                int count = this.mTransientStateViews.size();
                for (int i = 0; i < count; i++) {
                    this.mTransientStateViews.valueAt(i).forceLayout();
                }
            }
            if (this.mTransientStateViewsById != null) {
                int count = this.mTransientStateViewsById.size();
                for (int i = 0; i < count; i++) {
                    this.mTransientStateViewsById.valueAt(i).forceLayout();
                }
            }
        }

        public boolean shouldRecycleViewType(int viewType) {
            return viewType >= 0;
        }

        void clear() {
            if (this.mViewTypeCount == 1) {
                ArrayList<View> scrap = this.mCurrentScrap;
                this.clearScrap(scrap);
            } else {
                int typeCount = this.mViewTypeCount;
                for (int i = 0; i < typeCount; i++) {
                    ArrayList<View> scrap = this.mScrapViews[i];
                    this.clearScrap(scrap);
                }
            }
            this.clearTransientStateViews();
        }

        void fillActiveViews(int childCount, int firstActivePosition) {
            if (this.mActiveViews.length < childCount) {
                this.mActiveViews = new View[childCount];
            }
            this.mFirstActivePosition = firstActivePosition;
            View[] activeViews = this.mActiveViews;
            for (int i = 0; i < childCount; i++) {
                View child = AbsListView.this.getChildAt(i);
                AbsListView.LayoutParams lp = (AbsListView.LayoutParams) child.getLayoutParams();
                if (lp != null && lp.viewType != -2) {
                    activeViews[i] = child;
                    lp.scrappedFromPosition = firstActivePosition + i;
                }
            }
        }

        @Nullable
        View getActiveView(int position) {
            int index = position - this.mFirstActivePosition;
            View[] activeViews = this.mActiveViews;
            if (index >= 0 && index < activeViews.length) {
                View match = activeViews[index];
                activeViews[index] = null;
                return match;
            } else {
                return null;
            }
        }

        @Nullable
        View getTransientStateView(int position) {
            if (AbsListView.this.mAdapter != null && AbsListView.this.mAdapterHasStableIds && this.mTransientStateViewsById != null) {
                long id = AbsListView.this.mAdapter.getItemId(position);
                View result = this.mTransientStateViewsById.get(id);
                this.mTransientStateViewsById.remove(id);
                return result;
            } else {
                if (this.mTransientStateViews != null) {
                    int index = this.mTransientStateViews.indexOfKey(position);
                    if (index >= 0) {
                        View result = this.mTransientStateViews.valueAt(index);
                        this.mTransientStateViews.removeAt(index);
                        return result;
                    }
                }
                return null;
            }
        }

        void clearTransientStateViews() {
            SparseArray<View> viewsByPos = this.mTransientStateViews;
            if (viewsByPos != null) {
                int N = viewsByPos.size();
                for (int i = 0; i < N; i++) {
                    this.removeDetachedView(viewsByPos.valueAt(i));
                }
                viewsByPos.clear();
            }
            LongSparseArray<View> viewsById = this.mTransientStateViewsById;
            if (viewsById != null) {
                int N = viewsById.size();
                for (int i = 0; i < N; i++) {
                    this.removeDetachedView(viewsById.valueAt(i));
                }
                viewsById.clear();
            }
        }

        @Nullable
        View getScrapView(int position) {
            int whichScrap = AbsListView.this.mAdapter.getItemViewType(position);
            if (whichScrap < 0) {
                return null;
            } else if (this.mViewTypeCount == 1) {
                return this.retrieveFromScrap(this.mCurrentScrap, position);
            } else {
                return whichScrap < this.mScrapViews.length ? this.retrieveFromScrap(this.mScrapViews[whichScrap], position) : null;
            }
        }

        void addScrapView(@NonNull View scrap, int position) {
            AbsListView.LayoutParams lp = (AbsListView.LayoutParams) scrap.getLayoutParams();
            if (lp != null) {
                lp.scrappedFromPosition = position;
                int viewType = lp.viewType;
                if (!this.shouldRecycleViewType(viewType)) {
                    if (viewType != -2) {
                        this.getSkippedScrap().add(scrap);
                    }
                } else {
                    scrap.dispatchStartTemporaryDetach();
                    boolean scrapHasTransientState = scrap.hasTransientState();
                    if (scrapHasTransientState) {
                        if (AbsListView.this.mAdapter != null && AbsListView.this.mAdapterHasStableIds) {
                            if (this.mTransientStateViewsById == null) {
                                this.mTransientStateViewsById = new LongSparseArray<>();
                            }
                            this.mTransientStateViewsById.put(lp.itemId, scrap);
                        } else if (!AbsListView.this.mDataChanged) {
                            if (this.mTransientStateViews == null) {
                                this.mTransientStateViews = new SparseArray<>();
                            }
                            this.mTransientStateViews.put(position, scrap);
                        } else {
                            this.clearScrapForRebind(scrap);
                            this.getSkippedScrap().add(scrap);
                        }
                    } else {
                        this.clearScrapForRebind(scrap);
                        if (this.mViewTypeCount == 1) {
                            this.mCurrentScrap.add(scrap);
                        } else {
                            this.mScrapViews[viewType].add(scrap);
                        }
                        if (this.mRecyclerListener != null) {
                            this.mRecyclerListener.onMovedToScrapHeap(scrap);
                        }
                    }
                }
            }
        }

        @NonNull
        private ArrayList<View> getSkippedScrap() {
            if (this.mSkippedScrap == null) {
                this.mSkippedScrap = new ArrayList();
            }
            return this.mSkippedScrap;
        }

        void removeSkippedScrap() {
            if (this.mSkippedScrap != null) {
                int count = this.mSkippedScrap.size();
                for (int i = 0; i < count; i++) {
                    this.removeDetachedView((View) this.mSkippedScrap.get(i));
                }
                this.mSkippedScrap.clear();
            }
        }

        void scrapActiveViews() {
            View[] activeViews = this.mActiveViews;
            boolean hasListener = this.mRecyclerListener != null;
            boolean multipleScraps = this.mViewTypeCount > 1;
            ArrayList<View> scrapViews = this.mCurrentScrap;
            int count = activeViews.length;
            for (int i = count - 1; i >= 0; i--) {
                View victim = activeViews[i];
                if (victim != null) {
                    AbsListView.LayoutParams lp = (AbsListView.LayoutParams) victim.getLayoutParams();
                    int whichScrap = lp.viewType;
                    activeViews[i] = null;
                    if (victim.hasTransientState()) {
                        victim.dispatchStartTemporaryDetach();
                        if (AbsListView.this.mAdapter != null && AbsListView.this.mAdapterHasStableIds) {
                            if (this.mTransientStateViewsById == null) {
                                this.mTransientStateViewsById = new LongSparseArray<>();
                            }
                            long id = AbsListView.this.mAdapter.getItemId(this.mFirstActivePosition + i);
                            this.mTransientStateViewsById.put(id, victim);
                        } else if (!AbsListView.this.mDataChanged) {
                            if (this.mTransientStateViews == null) {
                                this.mTransientStateViews = new SparseArray<>();
                            }
                            this.mTransientStateViews.put(this.mFirstActivePosition + i, victim);
                        } else if (whichScrap != -2) {
                            this.removeDetachedView(victim);
                        }
                    } else if (!this.shouldRecycleViewType(whichScrap)) {
                        if (whichScrap != -2) {
                            this.removeDetachedView(victim);
                        }
                    } else {
                        if (multipleScraps) {
                            scrapViews = this.mScrapViews[whichScrap];
                        }
                        lp.scrappedFromPosition = this.mFirstActivePosition + i;
                        this.removeDetachedView(victim);
                        scrapViews.add(victim);
                        if (hasListener) {
                            this.mRecyclerListener.onMovedToScrapHeap(victim);
                        }
                    }
                }
            }
            this.pruneScrapViews();
        }

        void fullyDetachScrapViews() {
            int viewTypeCount = this.mViewTypeCount;
            ArrayList<View>[] scrapViews = this.mScrapViews;
            for (int i = 0; i < viewTypeCount; i++) {
                ArrayList<View> scrapPile = scrapViews[i];
                for (int j = scrapPile.size() - 1; j >= 0; j--) {
                    View view = (View) scrapPile.get(j);
                    if (view.isTemporarilyDetached()) {
                        this.removeDetachedView(view);
                    }
                }
            }
        }

        private void pruneScrapViews() {
            int maxViews = this.mActiveViews.length;
            int viewTypeCount = this.mViewTypeCount;
            ArrayList<View>[] scrapViews = this.mScrapViews;
            for (int i = 0; i < viewTypeCount; i++) {
                ArrayList<View> scrapPile = scrapViews[i];
                int size = scrapPile.size();
                while (size > maxViews) {
                    scrapPile.remove(--size);
                }
            }
            SparseArray<View> transViewsByPos = this.mTransientStateViews;
            if (transViewsByPos != null) {
                for (int i = 0; i < transViewsByPos.size(); i++) {
                    View v = transViewsByPos.valueAt(i);
                    if (!v.hasTransientState()) {
                        this.removeDetachedView(v);
                        transViewsByPos.removeAt(i);
                        i--;
                    }
                }
            }
            LongSparseArray<View> transViewsById = this.mTransientStateViewsById;
            if (transViewsById != null) {
                for (int ix = 0; ix < transViewsById.size(); ix++) {
                    View v = transViewsById.valueAt(ix);
                    if (!v.hasTransientState()) {
                        this.removeDetachedView(v);
                        transViewsById.removeAt(ix);
                        ix--;
                    }
                }
            }
        }

        void reclaimScrapViews(@NonNull List<View> views) {
            if (this.mViewTypeCount == 1) {
                views.addAll(this.mCurrentScrap);
            } else {
                int viewTypeCount = this.mViewTypeCount;
                ArrayList<View>[] scrapViews = this.mScrapViews;
                for (int i = 0; i < viewTypeCount; i++) {
                    ArrayList<View> scrapPile = scrapViews[i];
                    views.addAll(scrapPile);
                }
            }
        }

        @Nullable
        private View retrieveFromScrap(@NonNull ArrayList<View> scrapViews, int position) {
            int size = scrapViews.size();
            if (size > 0) {
                for (int i = size - 1; i >= 0; i--) {
                    View view = (View) scrapViews.get(i);
                    AbsListView.LayoutParams params = (AbsListView.LayoutParams) view.getLayoutParams();
                    if (AbsListView.this.mAdapterHasStableIds) {
                        long id = AbsListView.this.mAdapter.getItemId(position);
                        if (id == params.itemId) {
                            return (View) scrapViews.remove(i);
                        }
                    } else if (params.scrappedFromPosition == position) {
                        View scrap = (View) scrapViews.remove(i);
                        this.clearScrapForRebind(scrap);
                        return scrap;
                    }
                }
                View scrap = (View) scrapViews.remove(size - 1);
                this.clearScrapForRebind(scrap);
                return scrap;
            } else {
                return null;
            }
        }

        private void clearScrap(@NonNull ArrayList<View> scrap) {
            int scrapCount = scrap.size();
            for (int j = 0; j < scrapCount; j++) {
                this.removeDetachedView((View) scrap.remove(scrapCount - 1 - j));
            }
        }

        private void clearScrapForRebind(@NonNull View view) {
        }

        private void removeDetachedView(View child) {
            AbsListView.this.removeDetachedView(child, false);
        }
    }

    @FunctionalInterface
    public interface RecyclerListener {

        void onMovedToScrapHeap(@NonNull View var1);
    }

    public interface SelectionBoundsAdjuster {

        void adjustListItemSelectionBounds(Rect var1);
    }

    private class WindowRunnable {

        private int mOriginalAttachCount;

        public void rememberWindowAttachCount() {
            this.mOriginalAttachCount = AbsListView.this.getWindowAttachCount();
        }

        public boolean sameWindow() {
            return AbsListView.this.getWindowAttachCount() == this.mOriginalAttachCount;
        }
    }
}