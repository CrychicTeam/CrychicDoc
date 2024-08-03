package icyllis.modernui.widget;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.graphics.drawable.ShapeDrawable;
import icyllis.modernui.graphics.drawable.StateListDrawable;
import icyllis.modernui.material.MaterialDrawable;
import icyllis.modernui.util.StateSet;
import icyllis.modernui.util.TypedValue;
import icyllis.modernui.view.ContextMenu;
import icyllis.modernui.view.View;
import java.nio.FloatBuffer;

public class ExpandableListView extends ListView {

    public static final int PACKED_POSITION_TYPE_GROUP = 0;

    public static final int PACKED_POSITION_TYPE_CHILD = 1;

    public static final int PACKED_POSITION_TYPE_NULL = 2;

    public static final long PACKED_POSITION_VALUE_NULL = 4294967295L;

    private static final long PACKED_POSITION_MASK_CHILD = 4294967295L;

    private static final long PACKED_POSITION_MASK_GROUP = 9223372032559808512L;

    private static final long PACKED_POSITION_MASK_TYPE = Long.MIN_VALUE;

    private static final long PACKED_POSITION_SHIFT_GROUP = 32L;

    private static final long PACKED_POSITION_SHIFT_TYPE = 63L;

    private static final long PACKED_POSITION_INT_MASK_CHILD = 4294967295L;

    private static final long PACKED_POSITION_INT_MASK_GROUP = 2147483647L;

    private ExpandableListConnector mConnector;

    private ExpandableListAdapter mAdapter;

    private int mIndicatorLeft;

    private int mIndicatorRight;

    private int mIndicatorStart;

    private int mIndicatorEnd;

    private int mChildIndicatorLeft;

    private int mChildIndicatorRight;

    private int mChildIndicatorStart;

    private int mChildIndicatorEnd;

    public static final int CHILD_INDICATOR_INHERIT = -1;

    private static final int INDICATOR_UNDEFINED = -2;

    private Drawable mGroupIndicator;

    private Drawable mChildIndicator;

    private static final int[] GROUP_EXPANDED_STATE_SET = new int[] { 16842920 };

    private static final int[] GROUP_EMPTY_STATE_SET = new int[] { 16842921 };

    private static final int[] GROUP_EXPANDED_EMPTY_STATE_SET = new int[] { 16842920, 16842921 };

    private static final int[][] GROUP_STATE_SETS = new int[][] { EMPTY_STATE_SET, GROUP_EXPANDED_STATE_SET, GROUP_EMPTY_STATE_SET, GROUP_EXPANDED_EMPTY_STATE_SET };

    private static final int[] CHILD_LAST_STATE_SET = new int[] { 16842918 };

    private Drawable mChildDivider;

    private final Rect mIndicatorRect = new Rect();

    private ExpandableListView.OnGroupCollapseListener mOnGroupCollapseListener;

    private ExpandableListView.OnGroupExpandListener mOnGroupExpandListener;

    private ExpandableListView.OnGroupClickListener mOnGroupClickListener;

    private ExpandableListView.OnChildClickListener mOnChildClickListener;

    public ExpandableListView(Context context) {
        super(context);
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(GROUP_EXPANDED_STATE_SET, new ExpandableListView.DefaultGroupIndicator(context, false));
        drawable.addState(StateSet.WILD_CARD, new ExpandableListView.DefaultGroupIndicator(context, true));
        this.mGroupIndicator = drawable;
        this.mChildIndicator = null;
        this.mIndicatorLeft = this.dp(3.0F);
        this.mIndicatorRight = 0;
        if (this.mIndicatorRight == 0 && this.mGroupIndicator != null) {
            this.mIndicatorRight = this.mIndicatorLeft + this.mGroupIndicator.getIntrinsicWidth();
        }
        this.mChildIndicatorLeft = -1;
        this.mChildIndicatorRight = -1;
        ShapeDrawable drawablex = new ShapeDrawable();
        drawablex.setColor(553648127);
        drawablex.setSize(-1, this.dp(1.0F));
        this.setDivider(drawablex);
        ShapeDrawable drawablexx = new ShapeDrawable();
        drawablexx.setColor(553648127);
        drawablexx.setSize(-1, this.dp(1.0F));
        this.mChildDivider = drawablexx;
        this.mIndicatorStart = this.dp(3.0F);
        this.mIndicatorEnd = -2;
        this.mChildIndicatorStart = -1;
        this.mChildIndicatorEnd = -1;
    }

    @Override
    public void onRtlPropertiesChanged(int layoutDirection) {
        this.resolveIndicator();
        this.resolveChildIndicator();
    }

    private void resolveIndicator() {
        boolean isLayoutRtl = this.isLayoutRtl();
        if (isLayoutRtl) {
            if (this.mIndicatorStart >= 0) {
                this.mIndicatorRight = this.mIndicatorStart;
            }
            if (this.mIndicatorEnd >= 0) {
                this.mIndicatorLeft = this.mIndicatorEnd;
            }
        } else {
            if (this.mIndicatorStart >= 0) {
                this.mIndicatorLeft = this.mIndicatorStart;
            }
            if (this.mIndicatorEnd >= 0) {
                this.mIndicatorRight = this.mIndicatorEnd;
            }
        }
        if (this.mIndicatorRight == 0 && this.mGroupIndicator != null) {
            this.mIndicatorRight = this.mIndicatorLeft + this.mGroupIndicator.getIntrinsicWidth();
        }
    }

    private void resolveChildIndicator() {
        boolean isLayoutRtl = this.isLayoutRtl();
        if (isLayoutRtl) {
            if (this.mChildIndicatorStart >= -1) {
                this.mChildIndicatorRight = this.mChildIndicatorStart;
            }
            if (this.mChildIndicatorEnd >= -1) {
                this.mChildIndicatorLeft = this.mChildIndicatorEnd;
            }
        } else {
            if (this.mChildIndicatorStart >= -1) {
                this.mChildIndicatorLeft = this.mChildIndicatorStart;
            }
            if (this.mChildIndicatorEnd >= -1) {
                this.mChildIndicatorRight = this.mChildIndicatorEnd;
            }
        }
    }

    @Override
    protected void dispatchDraw(@NonNull Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.mChildIndicator != null || this.mGroupIndicator != null) {
            int saveCount = 0;
            boolean clipToPadding = this.hasBooleanFlag(34);
            if (clipToPadding) {
                saveCount = canvas.save();
                int scrollX = this.mScrollX;
                int scrollY = this.mScrollY;
                canvas.clipRect((float) (scrollX + this.mPaddingLeft), (float) (scrollY + this.mPaddingTop), (float) (scrollX + this.getWidth() - this.mPaddingRight), (float) (scrollY + this.getHeight() - this.mPaddingBottom));
            }
            int headerViewsCount = this.getHeaderViewsCount();
            int lastChildFlPos = this.mItemCount - this.getFooterViewsCount() - headerViewsCount - 1;
            int myB = this.getBottom();
            int lastItemType = -4;
            Rect indicatorRect = this.mIndicatorRect;
            int childCount = this.getChildCount();
            int i = 0;
            for (int childFlPos = this.mFirstPosition - headerViewsCount; i < childCount; childFlPos++) {
                if (childFlPos >= 0) {
                    if (childFlPos > lastChildFlPos) {
                        break;
                    }
                    View item = this.getChildAt(i);
                    int t = item.getTop();
                    int b = item.getBottom();
                    if (b >= 0 && t <= myB) {
                        ExpandableListConnector.PositionMetadata pos = this.mConnector.getUnflattenedPos(childFlPos);
                        boolean isLayoutRtl = this.isLayoutRtl();
                        int width = this.getWidth();
                        if (pos.position.type != lastItemType) {
                            if (pos.position.type == 1) {
                                indicatorRect.left = this.mChildIndicatorLeft == -1 ? this.mIndicatorLeft : this.mChildIndicatorLeft;
                                indicatorRect.right = this.mChildIndicatorRight == -1 ? this.mIndicatorRight : this.mChildIndicatorRight;
                            } else {
                                indicatorRect.left = this.mIndicatorLeft;
                                indicatorRect.right = this.mIndicatorRight;
                            }
                            if (isLayoutRtl) {
                                int temp = indicatorRect.left;
                                indicatorRect.left = width - indicatorRect.right;
                                indicatorRect.right = width - temp;
                                indicatorRect.left = indicatorRect.left - this.mPaddingRight;
                                indicatorRect.right = indicatorRect.right - this.mPaddingRight;
                            } else {
                                indicatorRect.left = indicatorRect.left + this.mPaddingLeft;
                                indicatorRect.right = indicatorRect.right + this.mPaddingLeft;
                            }
                            lastItemType = pos.position.type;
                        }
                        if (indicatorRect.left != indicatorRect.right) {
                            if (this.mStackFromBottom) {
                                indicatorRect.top = t;
                                indicatorRect.bottom = b;
                            } else {
                                indicatorRect.top = t;
                                indicatorRect.bottom = b;
                            }
                            Drawable indicator = this.getIndicator(pos);
                            if (indicator != null) {
                                indicator.setBounds(indicatorRect);
                                indicator.draw(canvas);
                            }
                        }
                        pos.recycle();
                    }
                }
                i++;
            }
            if (clipToPadding) {
                canvas.restoreToCount(saveCount);
            }
        }
    }

    private Drawable getIndicator(ExpandableListConnector.PositionMetadata pos) {
        Drawable indicator;
        if (pos.position.type == 2) {
            indicator = this.mGroupIndicator;
            if (indicator != null && indicator.isStateful()) {
                boolean isEmpty = pos.groupMetadata == null || pos.groupMetadata.lastChildFlPos == pos.groupMetadata.flPos;
                int stateSetIndex = (pos.isExpanded() ? 1 : 0) | (isEmpty ? 2 : 0);
                indicator.setState(GROUP_STATE_SETS[stateSetIndex]);
            }
        } else {
            indicator = this.mChildIndicator;
            if (indicator != null && indicator.isStateful()) {
                int[] stateSet = pos.position.flatListPos == pos.groupMetadata.lastChildFlPos ? CHILD_LAST_STATE_SET : EMPTY_STATE_SET;
                indicator.setState(stateSet);
            }
        }
        return indicator;
    }

    public void setChildDivider(Drawable childDivider) {
        this.mChildDivider = childDivider;
    }

    @Override
    void drawDivider(Canvas canvas, Rect bounds, int childIndex) {
        int flatListPosition = childIndex + this.mFirstPosition;
        if (flatListPosition >= 0) {
            int adjustedPosition = this.getFlatPositionForConnector(flatListPosition);
            ExpandableListConnector.PositionMetadata pos = this.mConnector.getUnflattenedPos(adjustedPosition);
            if (pos.position.type == 1 || pos.isExpanded() && pos.groupMetadata.lastChildFlPos != pos.groupMetadata.flPos) {
                Drawable divider = this.mChildDivider;
                divider.setBounds(bounds);
                divider.draw(canvas);
                pos.recycle();
                return;
            }
            pos.recycle();
        }
        super.drawDivider(canvas, bounds, flatListPosition);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        throw new RuntimeException("For ExpandableListView, use setAdapter(ExpandableListAdapter) instead of setAdapter(ListAdapter)");
    }

    @Override
    public ListAdapter getAdapter() {
        return super.getAdapter();
    }

    @Override
    public void setOnItemClickListener(AdapterView.OnItemClickListener l) {
        super.setOnItemClickListener(l);
    }

    public void setAdapter(ExpandableListAdapter adapter) {
        this.mAdapter = adapter;
        if (adapter != null) {
            this.mConnector = new ExpandableListConnector(adapter);
        } else {
            this.mConnector = null;
        }
        super.setAdapter((ListAdapter) this.mConnector);
    }

    public ExpandableListAdapter getExpandableListAdapter() {
        return this.mAdapter;
    }

    private boolean isHeaderOrFooterPosition(int position) {
        int footerViewsStart = this.mItemCount - this.getFooterViewsCount();
        return position < this.getHeaderViewsCount() || position >= footerViewsStart;
    }

    private int getFlatPositionForConnector(int flatListPosition) {
        return flatListPosition - this.getHeaderViewsCount();
    }

    private int getAbsoluteFlatPosition(int flatListPosition) {
        return flatListPosition + this.getHeaderViewsCount();
    }

    @Override
    public boolean performItemClick(View v, int position, long id) {
        if (this.isHeaderOrFooterPosition(position)) {
            return super.performItemClick(v, position, id);
        } else {
            int adjustedPosition = this.getFlatPositionForConnector(position);
            return this.handleItemClick(v, adjustedPosition, id);
        }
    }

    boolean handleItemClick(View v, int position, long id) {
        ExpandableListConnector.PositionMetadata posMetadata = this.mConnector.getUnflattenedPos(position);
        id = this.getChildOrGroupId(posMetadata.position);
        boolean returnValue;
        if (posMetadata.position.type == 2) {
            if (this.mOnGroupClickListener != null && this.mOnGroupClickListener.onGroupClick(this, v, posMetadata.position.groupPos, id)) {
                posMetadata.recycle();
                return true;
            }
            if (posMetadata.isExpanded()) {
                this.mConnector.collapseGroup(posMetadata);
                this.playSoundEffect(0);
                if (this.mOnGroupCollapseListener != null) {
                    this.mOnGroupCollapseListener.onGroupCollapse(posMetadata.position.groupPos);
                }
            } else {
                this.mConnector.expandGroup(posMetadata);
                this.playSoundEffect(0);
                if (this.mOnGroupExpandListener != null) {
                    this.mOnGroupExpandListener.onGroupExpand(posMetadata.position.groupPos);
                }
                int groupPos = posMetadata.position.groupPos;
                int groupFlatPos = posMetadata.position.flatListPos;
                int shiftedGroupPosition = groupFlatPos + this.getHeaderViewsCount();
                this.smoothScrollToPosition(shiftedGroupPosition + this.mAdapter.getChildrenCount(groupPos), shiftedGroupPosition);
            }
            returnValue = true;
        } else {
            if (this.mOnChildClickListener != null) {
                this.playSoundEffect(0);
                return this.mOnChildClickListener.onChildClick(this, v, posMetadata.position.groupPos, posMetadata.position.childPos, id);
            }
            returnValue = false;
        }
        posMetadata.recycle();
        return returnValue;
    }

    public boolean expandGroup(int groupPos) {
        return this.expandGroup(groupPos, false);
    }

    public boolean expandGroup(int groupPos, boolean animate) {
        ExpandableListPosition elGroupPos = ExpandableListPosition.obtain(2, groupPos, -1, -1);
        ExpandableListConnector.PositionMetadata pm = this.mConnector.getFlattenedPos(elGroupPos);
        elGroupPos.recycle();
        boolean retValue = this.mConnector.expandGroup(pm);
        if (this.mOnGroupExpandListener != null) {
            this.mOnGroupExpandListener.onGroupExpand(groupPos);
        }
        if (animate) {
            int groupFlatPos = pm.position.flatListPos;
            int shiftedGroupPosition = groupFlatPos + this.getHeaderViewsCount();
            this.smoothScrollToPosition(shiftedGroupPosition + this.mAdapter.getChildrenCount(groupPos), shiftedGroupPosition);
        }
        pm.recycle();
        return retValue;
    }

    public boolean collapseGroup(int groupPos) {
        boolean retValue = this.mConnector.collapseGroup(groupPos);
        if (this.mOnGroupCollapseListener != null) {
            this.mOnGroupCollapseListener.onGroupCollapse(groupPos);
        }
        return retValue;
    }

    public void setOnGroupCollapseListener(ExpandableListView.OnGroupCollapseListener onGroupCollapseListener) {
        this.mOnGroupCollapseListener = onGroupCollapseListener;
    }

    public void setOnGroupExpandListener(ExpandableListView.OnGroupExpandListener onGroupExpandListener) {
        this.mOnGroupExpandListener = onGroupExpandListener;
    }

    public void setOnGroupClickListener(ExpandableListView.OnGroupClickListener onGroupClickListener) {
        this.mOnGroupClickListener = onGroupClickListener;
    }

    public void setOnChildClickListener(ExpandableListView.OnChildClickListener onChildClickListener) {
        this.mOnChildClickListener = onChildClickListener;
    }

    public long getExpandableListPosition(int flatListPosition) {
        if (this.isHeaderOrFooterPosition(flatListPosition)) {
            return 4294967295L;
        } else {
            int adjustedPosition = this.getFlatPositionForConnector(flatListPosition);
            ExpandableListConnector.PositionMetadata pm = this.mConnector.getUnflattenedPos(adjustedPosition);
            long packedPos = pm.position.getPackedPosition();
            pm.recycle();
            return packedPos;
        }
    }

    public int getFlatListPosition(long packedPosition) {
        ExpandableListPosition elPackedPos = ExpandableListPosition.obtainPosition(packedPosition);
        ExpandableListConnector.PositionMetadata pm = this.mConnector.getFlattenedPos(elPackedPos);
        elPackedPos.recycle();
        int flatListPosition = pm.position.flatListPos;
        pm.recycle();
        return this.getAbsoluteFlatPosition(flatListPosition);
    }

    public long getSelectedPosition() {
        int selectedPos = this.getSelectedItemPosition();
        return this.getExpandableListPosition(selectedPos);
    }

    public long getSelectedId() {
        long packedPos = this.getSelectedPosition();
        if (packedPos == 4294967295L) {
            return -1L;
        } else {
            int groupPos = getPackedPositionGroup(packedPos);
            return getPackedPositionType(packedPos) == 0 ? this.mAdapter.getGroupId(groupPos) : this.mAdapter.getChildId(groupPos, getPackedPositionChild(packedPos));
        }
    }

    public void setSelectedGroup(int groupPosition) {
        ExpandableListPosition elGroupPos = ExpandableListPosition.obtainGroupPosition(groupPosition);
        ExpandableListConnector.PositionMetadata pm = this.mConnector.getFlattenedPos(elGroupPos);
        elGroupPos.recycle();
        int absoluteFlatPosition = this.getAbsoluteFlatPosition(pm.position.flatListPos);
        super.setSelection(absoluteFlatPosition);
        pm.recycle();
    }

    public boolean setSelectedChild(int groupPosition, int childPosition, boolean shouldExpandGroup) {
        ExpandableListPosition elChildPos = ExpandableListPosition.obtainChildPosition(groupPosition, childPosition);
        ExpandableListConnector.PositionMetadata flatChildPos = this.mConnector.getFlattenedPos(elChildPos);
        if (flatChildPos == null) {
            if (!shouldExpandGroup) {
                return false;
            }
            this.expandGroup(groupPosition);
            flatChildPos = this.mConnector.getFlattenedPos(elChildPos);
            if (flatChildPos == null) {
                throw new IllegalStateException("Could not find child");
            }
        }
        int absoluteFlatPosition = this.getAbsoluteFlatPosition(flatChildPos.position.flatListPos);
        super.setSelection(absoluteFlatPosition);
        elChildPos.recycle();
        flatChildPos.recycle();
        return true;
    }

    public boolean isGroupExpanded(int groupPosition) {
        return this.mConnector.isGroupExpanded(groupPosition);
    }

    public static int getPackedPositionType(long packedPosition) {
        if (packedPosition == 4294967295L) {
            return 2;
        } else {
            return (packedPosition & Long.MIN_VALUE) == Long.MIN_VALUE ? 1 : 0;
        }
    }

    public static int getPackedPositionGroup(long packedPosition) {
        return packedPosition == 4294967295L ? -1 : (int) ((packedPosition & 9223372032559808512L) >> 32);
    }

    public static int getPackedPositionChild(long packedPosition) {
        if (packedPosition == 4294967295L) {
            return -1;
        } else {
            return (packedPosition & Long.MIN_VALUE) != Long.MIN_VALUE ? -1 : (int) (packedPosition & 4294967295L);
        }
    }

    public static long getPackedPositionForChild(int groupPosition, int childPosition) {
        return Long.MIN_VALUE | ((long) groupPosition & 2147483647L) << 32 | (long) childPosition & 4294967295L;
    }

    public static long getPackedPositionForGroup(int groupPosition) {
        return ((long) groupPosition & 2147483647L) << 32;
    }

    @Override
    ContextMenu.ContextMenuInfo createContextMenuInfo(View view, int flatListPosition, long id) {
        if (this.isHeaderOrFooterPosition(flatListPosition)) {
            return new AdapterView.AdapterContextMenuInfo(view, flatListPosition, id);
        } else {
            int adjustedPosition = this.getFlatPositionForConnector(flatListPosition);
            ExpandableListConnector.PositionMetadata pm = this.mConnector.getUnflattenedPos(adjustedPosition);
            ExpandableListPosition pos = pm.position;
            id = this.getChildOrGroupId(pos);
            long packedPosition = pos.getPackedPosition();
            pm.recycle();
            return new ExpandableListView.ExpandableListContextMenuInfo(view, packedPosition, id);
        }
    }

    private long getChildOrGroupId(ExpandableListPosition position) {
        return position.type == 1 ? this.mAdapter.getChildId(position.groupPos, position.childPos) : this.mAdapter.getGroupId(position.groupPos);
    }

    public void setChildIndicator(Drawable childIndicator) {
        this.mChildIndicator = childIndicator;
    }

    public void setChildIndicatorBounds(int left, int right) {
        this.mChildIndicatorLeft = left;
        this.mChildIndicatorRight = right;
        this.resolveChildIndicator();
    }

    public void setChildIndicatorBoundsRelative(int start, int end) {
        this.mChildIndicatorStart = start;
        this.mChildIndicatorEnd = end;
        this.resolveChildIndicator();
    }

    public void setGroupIndicator(Drawable groupIndicator) {
        this.mGroupIndicator = groupIndicator;
        if (this.mIndicatorRight == 0 && this.mGroupIndicator != null) {
            this.mIndicatorRight = this.mIndicatorLeft + this.mGroupIndicator.getIntrinsicWidth();
        }
    }

    public void setIndicatorBounds(int left, int right) {
        this.mIndicatorLeft = left;
        this.mIndicatorRight = right;
        this.resolveIndicator();
    }

    public void setIndicatorBoundsRelative(int start, int end) {
        this.mIndicatorStart = start;
        this.mIndicatorEnd = end;
        this.resolveIndicator();
    }

    private static class DefaultGroupIndicator extends MaterialDrawable {

        private final boolean mDown;

        private final int mSize;

        private final FloatBuffer mPoints;

        public DefaultGroupIndicator(Context context, boolean down) {
            this.mDown = down;
            this.mSize = (int) TypedValue.applyDimension(1, 24.0F, context.getResources().getDisplayMetrics());
            this.mPoints = FloatBuffer.allocate(6);
        }

        @Override
        public void draw(@NonNull Canvas canvas) {
            Paint paint = Paint.obtain();
            paint.setColor(this.mColor);
            paint.setAlpha(ShapeDrawable.modulateAlpha(paint.getAlpha(), this.mAlpha));
            if (paint.getAlpha() != 0) {
                Rect bounds = this.getBounds();
                canvas.translate((float) bounds.x(), (float) bounds.y());
                canvas.drawTriangleListMesh(this.mPoints, null, paint);
                canvas.translate((float) (-bounds.x()), (float) (-bounds.y()));
            }
            paint.recycle();
        }

        @Override
        protected void onBoundsChange(@NonNull Rect bounds) {
            this.buildArrowPoints((float) bounds.width(), (float) bounds.height());
        }

        private void buildArrowPoints(float w, float h) {
            if (this.mDown) {
                this.mPoints.put(0.75F * w).put(0.33333334F * h).put(0.25F * w).put(0.33333334F * h).put(0.5F * w).put(0.6666667F * h).flip();
            } else {
                this.mPoints.put(0.75F * w).put(0.6666667F * h).put(0.5F * w).put(0.33333334F * h).put(0.25F * w).put(0.6666667F * h).flip();
            }
        }

        @Override
        public int getIntrinsicWidth() {
            return this.mSize;
        }

        @Override
        public int getIntrinsicHeight() {
            return this.mSize;
        }
    }

    public static class ExpandableListContextMenuInfo implements ContextMenu.ContextMenuInfo {

        public View targetView;

        public long packedPosition;

        public long id;

        public ExpandableListContextMenuInfo(View targetView, long packedPosition, long id) {
            this.targetView = targetView;
            this.packedPosition = packedPosition;
            this.id = id;
        }
    }

    public interface OnChildClickListener {

        boolean onChildClick(ExpandableListView var1, View var2, int var3, int var4, long var5);
    }

    public interface OnGroupClickListener {

        boolean onGroupClick(ExpandableListView var1, View var2, int var3, long var4);
    }

    public interface OnGroupCollapseListener {

        void onGroupCollapse(int var1);
    }

    public interface OnGroupExpandListener {

        void onGroupExpand(int var1);
    }
}