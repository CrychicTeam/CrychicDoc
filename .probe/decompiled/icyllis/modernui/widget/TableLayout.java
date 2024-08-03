package icyllis.modernui.widget;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.core.Context;
import icyllis.modernui.util.SparseBooleanArray;
import icyllis.modernui.view.MeasureSpec;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import java.util.regex.Pattern;

public class TableLayout extends LinearLayout {

    private int[] mMaxWidths;

    private SparseBooleanArray mStretchableColumns;

    private SparseBooleanArray mShrinkableColumns;

    private SparseBooleanArray mCollapsedColumns;

    private boolean mShrinkAllColumns;

    private boolean mStretchAllColumns;

    private boolean mInitialized;

    public TableLayout(Context context) {
        super(context);
        this.initTableLayout();
    }

    private static SparseBooleanArray parseColumns(String sequence) {
        SparseBooleanArray columns = new SparseBooleanArray();
        Pattern pattern = Pattern.compile("\\s*,\\s*");
        String[] columnDefs = pattern.split(sequence);
        for (String columnIdentifier : columnDefs) {
            try {
                int columnIndex = Integer.parseInt(columnIdentifier);
                if (columnIndex >= 0) {
                    columns.put(columnIndex, true);
                }
            } catch (NumberFormatException var9) {
            }
        }
        return columns;
    }

    private void initTableLayout() {
        if (this.mCollapsedColumns == null) {
            this.mCollapsedColumns = new SparseBooleanArray();
        }
        if (this.mStretchableColumns == null) {
            this.mStretchableColumns = new SparseBooleanArray();
        }
        if (this.mShrinkableColumns == null) {
            this.mShrinkableColumns = new SparseBooleanArray();
        }
        this.setOrientation(1);
        this.mInitialized = true;
    }

    @Override
    protected void onViewAdded(View child) {
        super.onViewAdded(child);
        this.trackCollapsedColumns(child);
    }

    private void requestRowsLayout() {
        if (this.mInitialized) {
            int count = this.getChildCount();
            for (int i = 0; i < count; i++) {
                this.getChildAt(i).requestLayout();
            }
        }
    }

    @Override
    public void requestLayout() {
        if (this.mInitialized) {
            int count = this.getChildCount();
            for (int i = 0; i < count; i++) {
                this.getChildAt(i).forceLayout();
            }
        }
        super.requestLayout();
    }

    public boolean isShrinkAllColumns() {
        return this.mShrinkAllColumns;
    }

    public void setShrinkAllColumns(boolean shrinkAllColumns) {
        this.mShrinkAllColumns = shrinkAllColumns;
    }

    public boolean isStretchAllColumns() {
        return this.mStretchAllColumns;
    }

    public void setStretchAllColumns(boolean stretchAllColumns) {
        this.mStretchAllColumns = stretchAllColumns;
    }

    public void setColumnCollapsed(int columnIndex, boolean isCollapsed) {
        this.mCollapsedColumns.put(columnIndex, isCollapsed);
        int count = this.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = this.getChildAt(i);
            if (view instanceof TableRow) {
                ((TableRow) view).setColumnCollapsed(columnIndex, isCollapsed);
            }
        }
        this.requestRowsLayout();
    }

    public boolean isColumnCollapsed(int columnIndex) {
        return this.mCollapsedColumns.get(columnIndex);
    }

    public void setColumnStretchable(int columnIndex, boolean isStretchable) {
        this.mStretchableColumns.put(columnIndex, isStretchable);
        this.requestRowsLayout();
    }

    public boolean isColumnStretchable(int columnIndex) {
        return this.mStretchAllColumns || this.mStretchableColumns.get(columnIndex);
    }

    public void setColumnShrinkable(int columnIndex, boolean isShrinkable) {
        this.mShrinkableColumns.put(columnIndex, isShrinkable);
        this.requestRowsLayout();
    }

    public boolean isColumnShrinkable(int columnIndex) {
        return this.mShrinkAllColumns || this.mShrinkableColumns.get(columnIndex);
    }

    private void trackCollapsedColumns(View child) {
        if (child instanceof TableRow row) {
            SparseBooleanArray collapsedColumns = this.mCollapsedColumns;
            int count = collapsedColumns.size();
            for (int i = 0; i < count; i++) {
                int columnIndex = collapsedColumns.keyAt(i);
                boolean isCollapsed = collapsedColumns.valueAt(i);
                if (isCollapsed) {
                    row.setColumnCollapsed(columnIndex, true);
                }
            }
        }
    }

    @Override
    public void addView(@NonNull View child) {
        super.addView(child);
        this.requestRowsLayout();
    }

    @Override
    public void addView(@NonNull View child, int index) {
        super.addView(child, index);
        this.requestRowsLayout();
    }

    @Override
    public void addView(@NonNull View child, @NonNull ViewGroup.LayoutParams params) {
        super.addView(child, params);
        this.requestRowsLayout();
    }

    @Override
    public void addView(@NonNull View child, int index, @NonNull ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        this.requestRowsLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.measureVertical(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        this.layoutVertical(l, t, r, b);
    }

    @Override
    void measureChildBeforeLayout(View child, int childIndex, int widthMeasureSpec, int totalWidth, int heightMeasureSpec, int totalHeight) {
        if (child instanceof TableRow) {
            ((TableRow) child).setColumnsWidthConstraints(this.mMaxWidths);
        }
        super.measureChildBeforeLayout(child, childIndex, widthMeasureSpec, totalWidth, heightMeasureSpec, totalHeight);
    }

    @Override
    void measureVertical(int widthMeasureSpec, int heightMeasureSpec) {
        this.findLargestCells(widthMeasureSpec, heightMeasureSpec);
        this.shrinkAndStretchColumns(widthMeasureSpec);
        super.measureVertical(widthMeasureSpec, heightMeasureSpec);
    }

    private void findLargestCells(int widthMeasureSpec, int heightMeasureSpec) {
        boolean firstRow = true;
        int count = this.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = this.getChildAt(i);
            if (child.getVisibility() != 8 && child instanceof TableRow) {
                TableRow row = (TableRow) child;
                ViewGroup.LayoutParams layoutParams = row.getLayoutParams();
                layoutParams.height = -2;
                int[] widths = row.getColumnsWidths(widthMeasureSpec, heightMeasureSpec);
                int newLength = widths.length;
                if (!firstRow) {
                    int length = this.mMaxWidths.length;
                    int difference = newLength - length;
                    if (difference > 0) {
                        int[] oldMaxWidths = this.mMaxWidths;
                        this.mMaxWidths = new int[newLength];
                        System.arraycopy(oldMaxWidths, 0, this.mMaxWidths, 0, oldMaxWidths.length);
                        System.arraycopy(widths, oldMaxWidths.length, this.mMaxWidths, oldMaxWidths.length, difference);
                    }
                    int[] maxWidths = this.mMaxWidths;
                    length = Math.min(length, newLength);
                    for (int j = 0; j < length; j++) {
                        maxWidths[j] = Math.max(maxWidths[j], widths[j]);
                    }
                } else {
                    if (this.mMaxWidths == null || this.mMaxWidths.length != newLength) {
                        this.mMaxWidths = new int[newLength];
                    }
                    System.arraycopy(widths, 0, this.mMaxWidths, 0, newLength);
                    firstRow = false;
                }
            }
        }
    }

    private void shrinkAndStretchColumns(int widthMeasureSpec) {
        if (this.mMaxWidths != null) {
            int totalWidth = 0;
            for (int width : this.mMaxWidths) {
                totalWidth += width;
            }
            int size = MeasureSpec.getSize(widthMeasureSpec) - this.mPaddingLeft - this.mPaddingRight;
            if (totalWidth <= size || !this.mShrinkAllColumns && this.mShrinkableColumns.size() <= 0) {
                if (totalWidth < size && (this.mStretchAllColumns || this.mStretchableColumns.size() > 0)) {
                    this.mutateColumnsWidth(this.mStretchableColumns, this.mStretchAllColumns, size, totalWidth);
                }
            } else {
                this.mutateColumnsWidth(this.mShrinkableColumns, this.mShrinkAllColumns, size, totalWidth);
            }
        }
    }

    private void mutateColumnsWidth(SparseBooleanArray columns, boolean allColumns, int size, int totalWidth) {
        int skipped = 0;
        int[] maxWidths = this.mMaxWidths;
        int length = maxWidths.length;
        int count = allColumns ? length : columns.size();
        int totalExtraSpace = size - totalWidth;
        int extraSpace = totalExtraSpace / count;
        int nbChildren = this.getChildCount();
        for (int i = 0; i < nbChildren; i++) {
            View child = this.getChildAt(i);
            if (child instanceof TableRow) {
                child.forceLayout();
            }
        }
        if (!allColumns) {
            for (int ix = 0; ix < count; ix++) {
                int column = columns.keyAt(ix);
                if (columns.valueAt(ix)) {
                    if (column < length) {
                        maxWidths[column] += extraSpace;
                    } else {
                        skipped++;
                    }
                }
            }
            if (skipped > 0 && skipped < count) {
                extraSpace = skipped * extraSpace / (count - skipped);
                for (int ixx = 0; ixx < count; ixx++) {
                    int column = columns.keyAt(ixx);
                    if (columns.valueAt(ixx) && column < length) {
                        if (extraSpace > maxWidths[column]) {
                            maxWidths[column] = 0;
                        } else {
                            maxWidths[column] += extraSpace;
                        }
                    }
                }
            }
        } else {
            for (int ixxx = 0; ixxx < count; ixxx++) {
                maxWidths[ixxx] += extraSpace;
            }
        }
    }

    @NonNull
    protected TableLayout.LayoutParams generateDefaultLayoutParams() {
        return new TableLayout.LayoutParams();
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof TableLayout.LayoutParams;
    }

    @NonNull
    protected LinearLayout.LayoutParams generateLayoutParams(@NonNull ViewGroup.LayoutParams p) {
        return new TableLayout.LayoutParams(p);
    }

    public static class LayoutParams extends LinearLayout.LayoutParams {

        public LayoutParams(int w, int h) {
            super(-1, h);
        }

        public LayoutParams(int w, int h, float initWeight) {
            super(-1, h, initWeight);
        }

        public LayoutParams() {
            super(-1, -2);
        }

        public LayoutParams(ViewGroup.LayoutParams p) {
            super(p);
            this.width = -1;
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
            this.width = -1;
            if (source instanceof TableLayout.LayoutParams) {
                this.weight = ((TableLayout.LayoutParams) source).weight;
            }
        }
    }
}