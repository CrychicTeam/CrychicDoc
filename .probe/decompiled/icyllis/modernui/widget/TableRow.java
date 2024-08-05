package icyllis.modernui.widget;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.core.Context;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.MeasureSpec;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;

public class TableRow extends LinearLayout {

    private int mNumColumns = 0;

    private int[] mColumnWidths;

    private int[] mConstrainedColumnWidths;

    private Int2IntOpenHashMap mColumnToChildIndex;

    public TableRow(Context context) {
        super(context);
    }

    void setColumnCollapsed(int columnIndex, boolean collapsed) {
        View child = this.getVirtualChildAt(columnIndex);
        if (child != null) {
            child.setVisibility(collapsed ? 8 : 0);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.measureHorizontal(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        this.layoutHorizontal(l, t, r, b);
    }

    @Override
    public View getVirtualChildAt(int i) {
        if (this.mColumnToChildIndex == null) {
            this.mapIndexAndColumns();
        }
        int deflectedIndex = this.mColumnToChildIndex.getOrDefault(i, -1);
        return deflectedIndex != -1 ? this.getChildAt(deflectedIndex) : null;
    }

    @Override
    public int getVirtualChildCount() {
        if (this.mColumnToChildIndex == null) {
            this.mapIndexAndColumns();
        }
        return this.mNumColumns;
    }

    private void mapIndexAndColumns() {
        if (this.mColumnToChildIndex == null) {
            int virtualCount = 0;
            int count = this.getChildCount();
            this.mColumnToChildIndex = new Int2IntOpenHashMap();
            Int2IntOpenHashMap columnToChild = this.mColumnToChildIndex;
            for (int i = 0; i < count; i++) {
                View child = this.getChildAt(i);
                TableRow.LayoutParams layoutParams = (TableRow.LayoutParams) child.getLayoutParams();
                if (layoutParams.column >= virtualCount) {
                    virtualCount = layoutParams.column;
                }
                for (int j = 0; j < layoutParams.span; j++) {
                    columnToChild.put(virtualCount++, i);
                }
            }
            this.mNumColumns = virtualCount;
        }
    }

    @Override
    int measureNullChild(int childIndex) {
        return this.mConstrainedColumnWidths[childIndex];
    }

    @Override
    void measureChildBeforeLayout(View child, int childIndex, int widthMeasureSpec, int totalWidth, int heightMeasureSpec, int totalHeight) {
        if (this.mConstrainedColumnWidths != null) {
            TableRow.LayoutParams lp = (TableRow.LayoutParams) child.getLayoutParams();
            int measureMode = 1073741824;
            int columnWidth = 0;
            int span = lp.span;
            int[] constrainedColumnWidths = this.mConstrainedColumnWidths;
            for (int i = 0; i < span; i++) {
                columnWidth += constrainedColumnWidths[childIndex + i];
            }
            int gravity = lp.gravity;
            boolean isHorizontalGravity = Gravity.isHorizontal(gravity);
            if (isHorizontalGravity) {
                measureMode = Integer.MIN_VALUE;
            }
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(Math.max(0, columnWidth - lp.leftMargin - lp.rightMargin), measureMode);
            int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, this.mPaddingTop + this.mPaddingBottom + lp.topMargin + lp.bottomMargin + totalHeight, lp.height);
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            if (isHorizontalGravity) {
                int childWidth = child.getMeasuredWidth();
                lp.mOffset[1] = columnWidth - childWidth;
                int layoutDirection = this.getLayoutDirection();
                int absoluteGravity = Gravity.getAbsoluteGravity(gravity, layoutDirection);
                switch(absoluteGravity & 7) {
                    case 1:
                        lp.mOffset[0] = lp.mOffset[1] / 2;
                    case 2:
                    case 3:
                    case 4:
                    default:
                        break;
                    case 5:
                        lp.mOffset[0] = lp.mOffset[1];
                }
            } else {
                lp.mOffset[0] = lp.mOffset[1] = 0;
            }
        } else {
            super.measureChildBeforeLayout(child, childIndex, widthMeasureSpec, totalWidth, heightMeasureSpec, totalHeight);
        }
    }

    @Override
    int getChildrenSkipCount(View child, int index) {
        TableRow.LayoutParams layoutParams = (TableRow.LayoutParams) child.getLayoutParams();
        return layoutParams.span - 1;
    }

    @Override
    int getLocationOffset(View child) {
        return ((TableRow.LayoutParams) child.getLayoutParams()).mOffset[0];
    }

    @Override
    int getNextLocationOffset(View child) {
        return ((TableRow.LayoutParams) child.getLayoutParams()).mOffset[1];
    }

    int[] getColumnsWidths(int widthMeasureSpec, int heightMeasureSpec) {
        int numColumns = this.getVirtualChildCount();
        if (this.mColumnWidths == null || numColumns != this.mColumnWidths.length) {
            this.mColumnWidths = new int[numColumns];
        }
        int[] columnWidths = this.mColumnWidths;
        for (int i = 0; i < numColumns; i++) {
            View child = this.getVirtualChildAt(i);
            if (child != null && child.getVisibility() != 8) {
                TableRow.LayoutParams layoutParams = (TableRow.LayoutParams) child.getLayoutParams();
                if (layoutParams.span == 1) {
                    int spec = switch(layoutParams.width) {
                        case -2 ->
                            getChildMeasureSpec(widthMeasureSpec, 0, -2);
                        case -1 ->
                            MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), 0);
                        default ->
                            MeasureSpec.makeMeasureSpec(layoutParams.width, 1073741824);
                    };
                    child.measure(spec, spec);
                    int width = child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
                    columnWidths[i] = width;
                } else {
                    columnWidths[i] = 0;
                }
            } else {
                columnWidths[i] = 0;
            }
        }
        return columnWidths;
    }

    void setColumnsWidthConstraints(int[] columnWidths) {
        if (columnWidths != null && columnWidths.length >= this.getVirtualChildCount()) {
            this.mConstrainedColumnWidths = columnWidths;
        } else {
            throw new IllegalArgumentException("columnWidths should be >= getVirtualChildCount()");
        }
    }

    @NonNull
    protected TableRow.LayoutParams generateDefaultLayoutParams() {
        return new TableRow.LayoutParams();
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof TableRow.LayoutParams;
    }

    @NonNull
    protected LinearLayout.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new TableRow.LayoutParams(p);
    }

    public static class LayoutParams extends LinearLayout.LayoutParams {

        public int column;

        public int span;

        private static final int LOCATION = 0;

        private static final int LOCATION_NEXT = 1;

        private int[] mOffset = new int[2];

        public LayoutParams(int w, int h) {
            super(w, h);
            this.column = -1;
            this.span = 1;
        }

        public LayoutParams(int w, int h, float initWeight) {
            super(w, h, initWeight);
            this.column = -1;
            this.span = 1;
        }

        public LayoutParams() {
            super(-1, -2);
            this.column = -1;
            this.span = 1;
        }

        public LayoutParams(int column) {
            this();
            this.column = column;
        }

        public LayoutParams(ViewGroup.LayoutParams p) {
            super(p);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }
    }
}