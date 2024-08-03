package icyllis.modernui.widget;

import icyllis.modernui.ModernUI;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.view.MeasureSpec;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus.Internal;

public class GridLayout extends ViewGroup {

    public static final int HORIZONTAL = 0;

    public static final int VERTICAL = 1;

    public static final int UNDEFINED = Integer.MIN_VALUE;

    public static final int ALIGN_BOUNDS = 0;

    public static final int ALIGN_MARGINS = 1;

    static final int MAX_SIZE = 100000;

    static final int DEFAULT_CONTAINER_MARGIN = 0;

    static final int UNINITIALIZED_HASH = 0;

    private static final int DEFAULT_ORIENTATION = 0;

    private static final int DEFAULT_COUNT = Integer.MIN_VALUE;

    private static final boolean DEFAULT_USE_DEFAULT_MARGINS = false;

    private static final boolean DEFAULT_ORDER_PRESERVED = true;

    private static final int DEFAULT_ALIGNMENT_MODE = 1;

    final GridLayout.Axis mHorizontalAxis = new GridLayout.Axis(true);

    final GridLayout.Axis mVerticalAxis = new GridLayout.Axis(false);

    int mOrientation = 0;

    boolean mUseDefaultMargins = false;

    int mAlignmentMode = 1;

    int mDefaultGap;

    int mLastLayoutParamsHashCode = 0;

    static final GridLayout.Alignment UNDEFINED_ALIGNMENT = new GridLayout.Alignment() {

        @Override
        int getGravityOffset(View view, int cellDelta) {
            return Integer.MIN_VALUE;
        }

        @Override
        public int getAlignmentValue(View view, int viewSize, int mode) {
            return Integer.MIN_VALUE;
        }
    };

    private static final GridLayout.Alignment LEADING = new GridLayout.Alignment() {

        @Override
        int getGravityOffset(View view, int cellDelta) {
            return 0;
        }

        @Override
        public int getAlignmentValue(View view, int viewSize, int mode) {
            return 0;
        }
    };

    private static final GridLayout.Alignment TRAILING = new GridLayout.Alignment() {

        @Override
        int getGravityOffset(View view, int cellDelta) {
            return cellDelta;
        }

        @Override
        public int getAlignmentValue(View view, int viewSize, int mode) {
            return viewSize;
        }
    };

    public static final GridLayout.Alignment TOP = LEADING;

    public static final GridLayout.Alignment BOTTOM = TRAILING;

    public static final GridLayout.Alignment START = LEADING;

    public static final GridLayout.Alignment END = TRAILING;

    public static final GridLayout.Alignment LEFT = createSwitchingAlignment(START, END);

    public static final GridLayout.Alignment RIGHT = createSwitchingAlignment(END, START);

    public static final GridLayout.Alignment CENTER = new GridLayout.Alignment() {

        @Override
        int getGravityOffset(View view, int cellDelta) {
            return cellDelta >> 1;
        }

        @Override
        public int getAlignmentValue(View view, int viewSize, int mode) {
            return viewSize >> 1;
        }
    };

    public static final GridLayout.Alignment BASELINE = new GridLayout.Alignment() {

        @Override
        int getGravityOffset(View view, int cellDelta) {
            return 0;
        }

        @Override
        public int getAlignmentValue(View view, int viewSize, int mode) {
            if (view.getVisibility() == 8) {
                return 0;
            } else {
                int baseline = view.getBaseline();
                return baseline == -1 ? Integer.MIN_VALUE : baseline;
            }
        }

        @Override
        public GridLayout.Bounds getBounds() {
            return new GridLayout.Bounds() {

                private int size;

                @Override
                protected void reset() {
                    super.reset();
                    this.size = Integer.MIN_VALUE;
                }

                @Override
                protected void include(int before, int after) {
                    super.include(before, after);
                    this.size = Math.max(this.size, before + after);
                }

                @Override
                protected int size(boolean min) {
                    return Math.max(super.size(min), this.size);
                }

                @Override
                protected int getOffset(GridLayout gl, View c, GridLayout.Alignment a, int size, boolean hrz) {
                    return Math.max(0, super.getOffset(gl, c, a, size, hrz));
                }
            };
        }
    };

    public static final GridLayout.Alignment FILL = new GridLayout.Alignment() {

        @Override
        int getGravityOffset(View view, int cellDelta) {
            return 0;
        }

        @Override
        public int getAlignmentValue(View view, int viewSize, int mode) {
            return Integer.MIN_VALUE;
        }

        @Override
        public int getSizeInCell(View view, int viewSize, int cellSize) {
            return cellSize;
        }
    };

    private static final int INFLEXIBLE = 0;

    private static final int CAN_STRETCH = 2;

    public GridLayout(Context context) {
        super(context);
        this.mDefaultGap = this.dp(8.0F);
        this.setRowCount(Integer.MIN_VALUE);
        this.setColumnCount(Integer.MIN_VALUE);
        this.setOrientation(0);
        this.setUseDefaultMargins(false);
        this.setAlignmentMode(1);
        this.setRowOrderPreserved(true);
        this.setColumnOrderPreserved(true);
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public void setOrientation(int orientation) {
        if (this.mOrientation != orientation) {
            this.mOrientation = orientation;
            this.invalidateStructure();
            this.requestLayout();
        }
    }

    public int getRowCount() {
        return this.mVerticalAxis.getCount();
    }

    public void setRowCount(int rowCount) {
        this.mVerticalAxis.setCount(rowCount);
        this.invalidateStructure();
        this.requestLayout();
    }

    public int getColumnCount() {
        return this.mHorizontalAxis.getCount();
    }

    public void setColumnCount(int columnCount) {
        this.mHorizontalAxis.setCount(columnCount);
        this.invalidateStructure();
        this.requestLayout();
    }

    public boolean getUseDefaultMargins() {
        return this.mUseDefaultMargins;
    }

    public void setUseDefaultMargins(boolean useDefaultMargins) {
        this.mUseDefaultMargins = useDefaultMargins;
        this.requestLayout();
    }

    public int getAlignmentMode() {
        return this.mAlignmentMode;
    }

    public void setAlignmentMode(int alignmentMode) {
        this.mAlignmentMode = alignmentMode;
        this.requestLayout();
    }

    public boolean isRowOrderPreserved() {
        return this.mVerticalAxis.isOrderPreserved();
    }

    public void setRowOrderPreserved(boolean rowOrderPreserved) {
        this.mVerticalAxis.setOrderPreserved(rowOrderPreserved);
        this.invalidateStructure();
        this.requestLayout();
    }

    public boolean isColumnOrderPreserved() {
        return this.mHorizontalAxis.isOrderPreserved();
    }

    public void setColumnOrderPreserved(boolean columnOrderPreserved) {
        this.mHorizontalAxis.setOrderPreserved(columnOrderPreserved);
        this.invalidateStructure();
        this.requestLayout();
    }

    static int max2(int[] a, int valueIfEmpty) {
        int result = valueIfEmpty;
        for (int j : a) {
            result = Math.max(result, j);
        }
        return result;
    }

    static <T> T[] append(T[] a, T[] b) {
        T[] result = (T[]) Array.newInstance(a.getClass().getComponentType(), a.length + b.length);
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    static GridLayout.Alignment getAlignment(int gravity, boolean horizontal) {
        int mask = horizontal ? 8388615 : 112;
        int shift = horizontal ? 0 : 4;
        int flags = (gravity & mask) >> shift;
        return switch(flags) {
            case 1 ->
                CENTER;
            case 3 ->
                horizontal ? LEFT : TOP;
            case 5 ->
                horizontal ? RIGHT : BOTTOM;
            case 7 ->
                FILL;
            case 8388611 ->
                START;
            case 8388613 ->
                END;
            default ->
                UNDEFINED_ALIGNMENT;
        };
    }

    private int getDefaultMargin(View c, boolean horizontal, boolean leading) {
        return this.mDefaultGap / 2;
    }

    private int getDefaultMargin(View c, boolean isAtEdge, boolean horizontal, boolean leading) {
        return this.getDefaultMargin(c, horizontal, leading);
    }

    private int getDefaultMargin(View c, GridLayout.LayoutParams p, boolean horizontal, boolean leading) {
        if (!this.mUseDefaultMargins) {
            return 0;
        } else {
            GridLayout.Spec spec = horizontal ? p.columnSpec : p.rowSpec;
            GridLayout.Axis axis = horizontal ? this.mHorizontalAxis : this.mVerticalAxis;
            GridLayout.Interval span = spec.span;
            boolean leading1 = (horizontal && this.isLayoutRtl()) != leading;
            boolean isAtEdge = leading1 ? span.min == 0 : span.max == axis.getCount();
            return this.getDefaultMargin(c, isAtEdge, horizontal, leading);
        }
    }

    int getMargin1(View view, boolean horizontal, boolean leading) {
        GridLayout.LayoutParams lp = this.getLayoutParams(view);
        int margin = horizontal ? (leading ? lp.leftMargin : lp.rightMargin) : (leading ? lp.topMargin : lp.bottomMargin);
        return margin == Integer.MIN_VALUE ? this.getDefaultMargin(view, lp, horizontal, leading) : margin;
    }

    private int getMargin(View view, boolean horizontal, boolean leading) {
        if (this.mAlignmentMode == 1) {
            return this.getMargin1(view, horizontal, leading);
        } else {
            GridLayout.Axis axis = horizontal ? this.mHorizontalAxis : this.mVerticalAxis;
            int[] margins = leading ? axis.getLeadingMargins() : axis.getTrailingMargins();
            GridLayout.LayoutParams lp = this.getLayoutParams(view);
            GridLayout.Spec spec = horizontal ? lp.columnSpec : lp.rowSpec;
            int index = leading ? spec.span.min : spec.span.max;
            return margins[index];
        }
    }

    private int getTotalMargin(View child, boolean horizontal) {
        return this.getMargin(child, horizontal, true) + this.getMargin(child, horizontal, false);
    }

    private static boolean fits(int[] a, int value, int start, int end) {
        if (end > a.length) {
            return false;
        } else {
            for (int i = start; i < end; i++) {
                if (a[i] > value) {
                    return false;
                }
            }
            return true;
        }
    }

    private static void procrusteanFill(int[] a, int start, int end, int value) {
        int length = a.length;
        Arrays.fill(a, Math.min(start, length), Math.min(end, length), value);
    }

    private static void setCellGroup(GridLayout.LayoutParams lp, int row, int rowSpan, int col, int colSpan) {
        lp.setRowSpecSpan(new GridLayout.Interval(row, row + rowSpan));
        lp.setColumnSpecSpan(new GridLayout.Interval(col, col + colSpan));
    }

    private static int clip(GridLayout.Interval minorRange, boolean minorWasDefined, int count) {
        int size = minorRange.size();
        if (count == 0) {
            return size;
        } else {
            int min = minorWasDefined ? Math.min(minorRange.min, count) : 0;
            return Math.min(size, count - min);
        }
    }

    private void validateLayoutParams() {
        boolean horizontal = this.mOrientation == 0;
        GridLayout.Axis axis = horizontal ? this.mHorizontalAxis : this.mVerticalAxis;
        int count = axis.definedCount != Integer.MIN_VALUE ? axis.definedCount : 0;
        int major = 0;
        int minor = 0;
        int[] maxSizes = new int[count];
        int i = 0;
        for (int N = this.getChildCount(); i < N; i++) {
            GridLayout.LayoutParams lp = (GridLayout.LayoutParams) this.getChildAt(i).getLayoutParams();
            GridLayout.Spec majorSpec = horizontal ? lp.rowSpec : lp.columnSpec;
            GridLayout.Interval majorRange = majorSpec.span;
            boolean majorWasDefined = majorSpec.startDefined;
            int majorSpan = majorRange.size();
            if (majorWasDefined) {
                major = majorRange.min;
            }
            GridLayout.Spec minorSpec = horizontal ? lp.columnSpec : lp.rowSpec;
            GridLayout.Interval minorRange = minorSpec.span;
            boolean minorWasDefined = minorSpec.startDefined;
            int minorSpan = clip(minorRange, minorWasDefined, count);
            if (minorWasDefined) {
                minor = minorRange.min;
            }
            if (count != 0) {
                if (!majorWasDefined || !minorWasDefined) {
                    while (!fits(maxSizes, major, minor, minor + minorSpan)) {
                        if (minorWasDefined) {
                            major++;
                        } else if (minor + minorSpan <= count) {
                            minor++;
                        } else {
                            minor = 0;
                            major++;
                        }
                    }
                }
                procrusteanFill(maxSizes, minor, minor + minorSpan, major + majorSpan);
            }
            if (horizontal) {
                setCellGroup(lp, major, majorSpan, minor, minorSpan);
            } else {
                setCellGroup(lp, minor, minorSpan, major, majorSpan);
            }
            minor += minorSpan;
        }
    }

    private void invalidateStructure() {
        this.mLastLayoutParamsHashCode = 0;
        this.mHorizontalAxis.invalidateStructure();
        this.mVerticalAxis.invalidateStructure();
        this.invalidateValues();
    }

    private void invalidateValues() {
        if (this.mHorizontalAxis != null && this.mVerticalAxis != null) {
            this.mHorizontalAxis.invalidateValues();
            this.mVerticalAxis.invalidateValues();
        }
    }

    @Internal
    @Override
    protected void onSetLayoutParams(View child, ViewGroup.LayoutParams layoutParams) {
        super.onSetLayoutParams(child, layoutParams);
        if (!this.checkLayoutParams(layoutParams)) {
            handleInvalidParams("supplied LayoutParams are of the wrong type");
        }
        this.invalidateStructure();
    }

    final GridLayout.LayoutParams getLayoutParams(View c) {
        return (GridLayout.LayoutParams) c.getLayoutParams();
    }

    private static void handleInvalidParams(String msg) {
        throw new IllegalArgumentException(msg + ". ");
    }

    private void checkLayoutParams(GridLayout.LayoutParams lp, boolean horizontal) {
        String groupName = horizontal ? "column" : "row";
        GridLayout.Spec spec = horizontal ? lp.columnSpec : lp.rowSpec;
        GridLayout.Interval span = spec.span;
        if (span.min != Integer.MIN_VALUE && span.min < 0) {
            handleInvalidParams(groupName + " indices must be positive");
        }
        GridLayout.Axis axis = horizontal ? this.mHorizontalAxis : this.mVerticalAxis;
        int count = axis.definedCount;
        if (count != Integer.MIN_VALUE) {
            if (span.max > count) {
                handleInvalidParams(groupName + " indices (start + span) mustn't exceed the " + groupName + " count");
            }
            if (span.size() > count) {
                handleInvalidParams(groupName + " span mustn't exceed the " + groupName + " count");
            }
        }
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        if (p instanceof GridLayout.LayoutParams lp) {
            this.checkLayoutParams(lp, true);
            this.checkLayoutParams(lp, false);
            return true;
        } else {
            return false;
        }
    }

    @NonNull
    protected GridLayout.LayoutParams generateDefaultLayoutParams() {
        return new GridLayout.LayoutParams();
    }

    @NonNull
    protected GridLayout.LayoutParams generateLayoutParams(@NonNull ViewGroup.LayoutParams lp) {
        if (lp instanceof GridLayout.LayoutParams) {
            return new GridLayout.LayoutParams((GridLayout.LayoutParams) lp);
        } else {
            return lp instanceof ViewGroup.MarginLayoutParams ? new GridLayout.LayoutParams((ViewGroup.MarginLayoutParams) lp) : new GridLayout.LayoutParams(lp);
        }
    }

    private void drawLine(Canvas graphics, int x1, int y1, int x2, int y2, Paint paint) {
        if (this.isLayoutRtl()) {
            int width = this.getWidth();
            graphics.drawLine((float) (width - x1), (float) y1, (float) (width - x2), (float) y2, paint);
        } else {
            graphics.drawLine((float) x1, (float) y1, (float) x2, (float) y2, paint);
        }
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        this.invalidateStructure();
    }

    @Override
    public void onViewRemoved(View child) {
        super.onViewRemoved(child);
        this.invalidateStructure();
    }

    @Internal
    @Override
    protected void onChildVisibilityChanged(View child, int oldVisibility, int newVisibility) {
        super.onChildVisibilityChanged(child, oldVisibility, newVisibility);
        if (oldVisibility == 8 || newVisibility == 8) {
            this.invalidateStructure();
        }
    }

    private int computeLayoutParamsHashCode() {
        int result = 1;
        int i = 0;
        for (int N = this.getChildCount(); i < N; i++) {
            View c = this.getChildAt(i);
            if (c.getVisibility() != 8) {
                GridLayout.LayoutParams lp = (GridLayout.LayoutParams) c.getLayoutParams();
                result = 31 * result + lp.hashCode();
            }
        }
        return result;
    }

    private void consistencyCheck() {
        if (this.mLastLayoutParamsHashCode == 0) {
            this.validateLayoutParams();
            this.mLastLayoutParamsHashCode = this.computeLayoutParamsHashCode();
        } else if (this.mLastLayoutParamsHashCode != this.computeLayoutParamsHashCode()) {
            ModernUI.LOGGER.warn("The fields of some layout parameters were modified in between layout operations. Check the javadoc for GridLayout.LayoutParams#rowSpec.");
            this.invalidateStructure();
            this.consistencyCheck();
        }
    }

    private void measureChildWithMargins2(View child, int parentWidthSpec, int parentHeightSpec, int childWidth, int childHeight) {
        int childWidthSpec = getChildMeasureSpec(parentWidthSpec, this.getTotalMargin(child, true), childWidth);
        int childHeightSpec = getChildMeasureSpec(parentHeightSpec, this.getTotalMargin(child, false), childHeight);
        child.measure(childWidthSpec, childHeightSpec);
    }

    private void measureChildrenWithMargins(int widthSpec, int heightSpec, boolean firstPass) {
        int i = 0;
        for (int N = this.getChildCount(); i < N; i++) {
            View c = this.getChildAt(i);
            if (c.getVisibility() != 8) {
                GridLayout.LayoutParams lp = this.getLayoutParams(c);
                if (firstPass) {
                    this.measureChildWithMargins2(c, widthSpec, heightSpec, lp.width, lp.height);
                } else {
                    boolean horizontal = this.mOrientation == 0;
                    GridLayout.Spec spec = horizontal ? lp.columnSpec : lp.rowSpec;
                    if (spec.getAbsoluteAlignment(horizontal) == FILL) {
                        GridLayout.Interval span = spec.span;
                        GridLayout.Axis axis = horizontal ? this.mHorizontalAxis : this.mVerticalAxis;
                        int[] locations = axis.getLocations();
                        int cellSize = locations[span.max] - locations[span.min];
                        int viewSize = cellSize - this.getTotalMargin(c, horizontal);
                        if (horizontal) {
                            this.measureChildWithMargins2(c, widthSpec, heightSpec, viewSize, lp.height);
                        } else {
                            this.measureChildWithMargins2(c, widthSpec, heightSpec, lp.width, viewSize);
                        }
                    }
                }
            }
        }
    }

    static int adjust(int measureSpec, int delta) {
        return MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(measureSpec + delta), MeasureSpec.getMode(measureSpec));
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        this.consistencyCheck();
        this.invalidateValues();
        int hPadding = this.getPaddingLeft() + this.getPaddingRight();
        int vPadding = this.getPaddingTop() + this.getPaddingBottom();
        int widthSpecSansPadding = adjust(widthSpec, -hPadding);
        int heightSpecSansPadding = adjust(heightSpec, -vPadding);
        this.measureChildrenWithMargins(widthSpecSansPadding, heightSpecSansPadding, true);
        int widthSansPadding;
        int heightSansPadding;
        if (this.mOrientation == 0) {
            widthSansPadding = this.mHorizontalAxis.getMeasure(widthSpecSansPadding);
            this.measureChildrenWithMargins(widthSpecSansPadding, heightSpecSansPadding, false);
            heightSansPadding = this.mVerticalAxis.getMeasure(heightSpecSansPadding);
        } else {
            heightSansPadding = this.mVerticalAxis.getMeasure(heightSpecSansPadding);
            this.measureChildrenWithMargins(widthSpecSansPadding, heightSpecSansPadding, false);
            widthSansPadding = this.mHorizontalAxis.getMeasure(widthSpecSansPadding);
        }
        int measuredWidth = Math.max(widthSansPadding + hPadding, this.getSuggestedMinimumWidth());
        int measuredHeight = Math.max(heightSansPadding + vPadding, this.getSuggestedMinimumHeight());
        this.setMeasuredDimension(resolveSizeAndState(measuredWidth, widthSpec, 0), resolveSizeAndState(measuredHeight, heightSpec, 0));
    }

    private int getMeasurement(View c, boolean horizontal) {
        return horizontal ? c.getMeasuredWidth() : c.getMeasuredHeight();
    }

    final int getMeasurementIncludingMargin(View c, boolean horizontal) {
        return c.getVisibility() == 8 ? 0 : this.getMeasurement(c, horizontal) + this.getTotalMargin(c, horizontal);
    }

    @Override
    public void requestLayout() {
        super.requestLayout();
        this.invalidateValues();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        this.consistencyCheck();
        int targetWidth = right - left;
        int targetHeight = bottom - top;
        int paddingLeft = this.getPaddingLeft();
        int paddingTop = this.getPaddingTop();
        int paddingRight = this.getPaddingRight();
        int paddingBottom = this.getPaddingBottom();
        this.mHorizontalAxis.layout(targetWidth - paddingLeft - paddingRight);
        this.mVerticalAxis.layout(targetHeight - paddingTop - paddingBottom);
        int[] hLocations = this.mHorizontalAxis.getLocations();
        int[] vLocations = this.mVerticalAxis.getLocations();
        int i = 0;
        for (int N = this.getChildCount(); i < N; i++) {
            View c = this.getChildAt(i);
            if (c.getVisibility() != 8) {
                GridLayout.LayoutParams lp = this.getLayoutParams(c);
                GridLayout.Spec columnSpec = lp.columnSpec;
                GridLayout.Spec rowSpec = lp.rowSpec;
                GridLayout.Interval colSpan = columnSpec.span;
                GridLayout.Interval rowSpan = rowSpec.span;
                int x1 = hLocations[colSpan.min];
                int y1 = vLocations[rowSpan.min];
                int x2 = hLocations[colSpan.max];
                int y2 = vLocations[rowSpan.max];
                int cellWidth = x2 - x1;
                int cellHeight = y2 - y1;
                int pWidth = this.getMeasurement(c, true);
                int pHeight = this.getMeasurement(c, false);
                GridLayout.Alignment hAlign = columnSpec.getAbsoluteAlignment(true);
                GridLayout.Alignment vAlign = rowSpec.getAbsoluteAlignment(false);
                GridLayout.Bounds boundsX = this.mHorizontalAxis.getGroupBounds().getValue(i);
                GridLayout.Bounds boundsY = this.mVerticalAxis.getGroupBounds().getValue(i);
                int gravityOffsetX = hAlign.getGravityOffset(c, cellWidth - boundsX.size(true));
                int gravityOffsetY = vAlign.getGravityOffset(c, cellHeight - boundsY.size(true));
                int leftMargin = this.getMargin(c, true, true);
                int topMargin = this.getMargin(c, false, true);
                int rightMargin = this.getMargin(c, true, false);
                int bottomMargin = this.getMargin(c, false, false);
                int sumMarginsX = leftMargin + rightMargin;
                int sumMarginsY = topMargin + bottomMargin;
                int alignmentOffsetX = boundsX.getOffset(this, c, hAlign, pWidth + sumMarginsX, true);
                int alignmentOffsetY = boundsY.getOffset(this, c, vAlign, pHeight + sumMarginsY, false);
                int width = hAlign.getSizeInCell(c, pWidth, cellWidth - sumMarginsX);
                int height = vAlign.getSizeInCell(c, pHeight, cellHeight - sumMarginsY);
                int dx = x1 + gravityOffsetX + alignmentOffsetX;
                int cx = !this.isLayoutRtl() ? paddingLeft + leftMargin + dx : targetWidth - width - paddingRight - rightMargin - dx;
                int cy = paddingTop + y1 + gravityOffsetY + alignmentOffsetY + topMargin;
                if (width != c.getMeasuredWidth() || height != c.getMeasuredHeight()) {
                    c.measure(MeasureSpec.makeMeasureSpec(width, 1073741824), MeasureSpec.makeMeasureSpec(height, 1073741824));
                }
                c.layout(cx, cy, cx + width, cy + height);
            }
        }
    }

    public static GridLayout.Spec spec(int start, int size, GridLayout.Alignment alignment, float weight) {
        return new GridLayout.Spec(start != Integer.MIN_VALUE, start, size, alignment, weight);
    }

    public static GridLayout.Spec spec(int start, GridLayout.Alignment alignment, float weight) {
        return spec(start, 1, alignment, weight);
    }

    public static GridLayout.Spec spec(int start, int size, float weight) {
        return spec(start, size, UNDEFINED_ALIGNMENT, weight);
    }

    public static GridLayout.Spec spec(int start, float weight) {
        return spec(start, 1, weight);
    }

    public static GridLayout.Spec spec(int start, int size, GridLayout.Alignment alignment) {
        return spec(start, size, alignment, 0.0F);
    }

    public static GridLayout.Spec spec(int start, GridLayout.Alignment alignment) {
        return spec(start, 1, alignment);
    }

    public static GridLayout.Spec spec(int start, int size) {
        return spec(start, size, UNDEFINED_ALIGNMENT);
    }

    public static GridLayout.Spec spec(int start) {
        return spec(start, 1);
    }

    private static GridLayout.Alignment createSwitchingAlignment(GridLayout.Alignment ltr, GridLayout.Alignment rtl) {
        return new GridLayout.Alignment() {

            @Override
            int getGravityOffset(View view, int cellDelta) {
                return (!view.isLayoutRtl() ? ltr : rtl).getGravityOffset(view, cellDelta);
            }

            @Override
            public int getAlignmentValue(View view, int viewSize, int mode) {
                return (!view.isLayoutRtl() ? ltr : rtl).getAlignmentValue(view, viewSize, mode);
            }
        };
    }

    static boolean canStretch(int flexibility) {
        return (flexibility & 2) != 0;
    }

    public abstract static class Alignment {

        Alignment() {
        }

        abstract int getGravityOffset(View var1, int var2);

        abstract int getAlignmentValue(View var1, int var2, int var3);

        int getSizeInCell(View view, int viewSize, int cellSize) {
            return viewSize;
        }

        GridLayout.Bounds getBounds() {
            return new GridLayout.Bounds();
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @Internal
    public @interface AlignmentMode {
    }

    static final class Arc {

        public final GridLayout.Interval span;

        public final GridLayout.MutableInt value;

        public boolean valid = true;

        public Arc(GridLayout.Interval span, GridLayout.MutableInt value) {
            this.span = span;
            this.value = value;
        }

        public String toString() {
            return this.span + " " + (!this.valid ? "+>" : "->") + " " + this.value;
        }
    }

    static final class Assoc<K, V> extends ArrayList<K> {

        private final Class<K> keyType;

        private final Class<V> valueType;

        private final ArrayList<V> values = new ArrayList();

        private Assoc(Class<K> keyType, Class<V> valueType) {
            this.keyType = keyType;
            this.valueType = valueType;
        }

        public static <K, V> GridLayout.Assoc<K, V> of(Class<K> keyType, Class<V> valueType) {
            return new GridLayout.Assoc<>(keyType, valueType);
        }

        public void put(K key, V value) {
            this.add(key);
            this.values.add(value);
        }

        public GridLayout.PackedMap<K, V> pack() {
            int N = this.size();
            K[] keys = (K[]) ((Object[]) Array.newInstance(this.keyType, N));
            V[] values = (V[]) ((Object[]) Array.newInstance(this.valueType, N));
            for (int i = 0; i < N; i++) {
                keys[i] = (K) this.get(i);
                values[i] = (V) this.values.get(i);
            }
            return new GridLayout.PackedMap<>(keys, values);
        }
    }

    final class Axis {

        private static final int NEW = 0;

        private static final int PENDING = 1;

        private static final int COMPLETE = 2;

        public final boolean horizontal;

        public int definedCount = Integer.MIN_VALUE;

        private int maxIndex = Integer.MIN_VALUE;

        GridLayout.PackedMap<GridLayout.Spec, GridLayout.Bounds> groupBounds;

        public boolean groupBoundsValid = false;

        GridLayout.PackedMap<GridLayout.Interval, GridLayout.MutableInt> forwardLinks;

        public boolean forwardLinksValid = false;

        GridLayout.PackedMap<GridLayout.Interval, GridLayout.MutableInt> backwardLinks;

        public boolean backwardLinksValid = false;

        public int[] leadingMargins;

        public boolean leadingMarginsValid = false;

        public int[] trailingMargins;

        public boolean trailingMarginsValid = false;

        public GridLayout.Arc[] arcs;

        public boolean arcsValid = false;

        public int[] locations;

        public boolean locationsValid = false;

        public boolean hasWeights;

        public boolean hasWeightsValid = false;

        public int[] deltas;

        boolean orderPreserved = true;

        private final GridLayout.MutableInt parentMin = new GridLayout.MutableInt(0);

        private final GridLayout.MutableInt parentMax = new GridLayout.MutableInt(-100000);

        private Axis(boolean horizontal) {
            this.horizontal = horizontal;
        }

        private int calculateMaxIndex() {
            int result = -1;
            int i = 0;
            for (int N = GridLayout.this.getChildCount(); i < N; i++) {
                View c = GridLayout.this.getChildAt(i);
                GridLayout.LayoutParams params = GridLayout.this.getLayoutParams(c);
                GridLayout.Spec spec = this.horizontal ? params.columnSpec : params.rowSpec;
                GridLayout.Interval span = spec.span;
                result = Math.max(result, span.min);
                result = Math.max(result, span.max);
                result = Math.max(result, span.size());
            }
            return result == -1 ? Integer.MIN_VALUE : result;
        }

        private int getMaxIndex() {
            if (this.maxIndex == Integer.MIN_VALUE) {
                this.maxIndex = Math.max(0, this.calculateMaxIndex());
            }
            return this.maxIndex;
        }

        public int getCount() {
            return Math.max(this.definedCount, this.getMaxIndex());
        }

        public void setCount(int count) {
            if (count != Integer.MIN_VALUE && count < this.getMaxIndex()) {
                GridLayout.handleInvalidParams((this.horizontal ? "column" : "row") + "Count must be greater than or equal to the maximum of all grid indices (and spans) defined in the LayoutParams of each child");
            }
            this.definedCount = count;
        }

        public boolean isOrderPreserved() {
            return this.orderPreserved;
        }

        public void setOrderPreserved(boolean orderPreserved) {
            this.orderPreserved = orderPreserved;
            this.invalidateStructure();
        }

        private GridLayout.PackedMap<GridLayout.Spec, GridLayout.Bounds> createGroupBounds() {
            GridLayout.Assoc<GridLayout.Spec, GridLayout.Bounds> assoc = GridLayout.Assoc.of(GridLayout.Spec.class, GridLayout.Bounds.class);
            int i = 0;
            for (int N = GridLayout.this.getChildCount(); i < N; i++) {
                View c = GridLayout.this.getChildAt(i);
                GridLayout.LayoutParams lp = GridLayout.this.getLayoutParams(c);
                GridLayout.Spec spec = this.horizontal ? lp.columnSpec : lp.rowSpec;
                GridLayout.Bounds bounds = spec.getAbsoluteAlignment(this.horizontal).getBounds();
                assoc.put(spec, bounds);
            }
            return assoc.pack();
        }

        private void computeGroupBounds() {
            GridLayout.Bounds[] values = this.groupBounds.values;
            for (GridLayout.Bounds value : values) {
                value.reset();
            }
            int i = 0;
            for (int N = GridLayout.this.getChildCount(); i < N; i++) {
                View c = GridLayout.this.getChildAt(i);
                GridLayout.LayoutParams lp = GridLayout.this.getLayoutParams(c);
                GridLayout.Spec spec = this.horizontal ? lp.columnSpec : lp.rowSpec;
                int size = GridLayout.this.getMeasurementIncludingMargin(c, this.horizontal) + (spec.weight == 0.0F ? 0 : this.getDeltas()[i]);
                this.groupBounds.getValue(i).include(GridLayout.this, c, spec, this, size);
            }
        }

        public GridLayout.PackedMap<GridLayout.Spec, GridLayout.Bounds> getGroupBounds() {
            if (this.groupBounds == null) {
                this.groupBounds = this.createGroupBounds();
            }
            if (!this.groupBoundsValid) {
                this.computeGroupBounds();
                this.groupBoundsValid = true;
            }
            return this.groupBounds;
        }

        private GridLayout.PackedMap<GridLayout.Interval, GridLayout.MutableInt> createLinks(boolean min) {
            GridLayout.Assoc<GridLayout.Interval, GridLayout.MutableInt> result = GridLayout.Assoc.of(GridLayout.Interval.class, GridLayout.MutableInt.class);
            GridLayout.Spec[] keys = this.getGroupBounds().keys;
            for (GridLayout.Spec key : keys) {
                GridLayout.Interval span = min ? key.span : key.span.inverse();
                result.put(span, new GridLayout.MutableInt());
            }
            return result.pack();
        }

        private void computeLinks(GridLayout.PackedMap<GridLayout.Interval, GridLayout.MutableInt> links, boolean min) {
            GridLayout.MutableInt[] spans = links.values;
            for (GridLayout.MutableInt span : spans) {
                span.reset();
            }
            GridLayout.Bounds[] bounds = this.getGroupBounds().values;
            for (int i = 0; i < bounds.length; i++) {
                int size = bounds[i].size(min);
                GridLayout.MutableInt valueHolder = links.getValue(i);
                valueHolder.value = Math.max(valueHolder.value, min ? size : -size);
            }
        }

        private GridLayout.PackedMap<GridLayout.Interval, GridLayout.MutableInt> getForwardLinks() {
            if (this.forwardLinks == null) {
                this.forwardLinks = this.createLinks(true);
            }
            if (!this.forwardLinksValid) {
                this.computeLinks(this.forwardLinks, true);
                this.forwardLinksValid = true;
            }
            return this.forwardLinks;
        }

        private GridLayout.PackedMap<GridLayout.Interval, GridLayout.MutableInt> getBackwardLinks() {
            if (this.backwardLinks == null) {
                this.backwardLinks = this.createLinks(false);
            }
            if (!this.backwardLinksValid) {
                this.computeLinks(this.backwardLinks, false);
                this.backwardLinksValid = true;
            }
            return this.backwardLinks;
        }

        private void include(List<GridLayout.Arc> arcs, GridLayout.Interval key, GridLayout.MutableInt size, boolean ignoreIfAlreadyPresent) {
            if (key.size() != 0) {
                if (ignoreIfAlreadyPresent) {
                    for (GridLayout.Arc arc : arcs) {
                        GridLayout.Interval span = arc.span;
                        if (span.equals(key)) {
                            return;
                        }
                    }
                }
                arcs.add(new GridLayout.Arc(key, size));
            }
        }

        private void include(List<GridLayout.Arc> arcs, GridLayout.Interval key, GridLayout.MutableInt size) {
            this.include(arcs, key, size, true);
        }

        GridLayout.Arc[][] groupArcsByFirstVertex(GridLayout.Arc[] arcs) {
            int N = this.getCount() + 1;
            GridLayout.Arc[][] result = new GridLayout.Arc[N][];
            int[] sizes = new int[N];
            for (GridLayout.Arc arc : arcs) {
                sizes[arc.span.min]++;
            }
            for (int i = 0; i < sizes.length; i++) {
                result[i] = new GridLayout.Arc[sizes[i]];
            }
            Arrays.fill(sizes, 0);
            for (GridLayout.Arc arc : arcs) {
                int i = arc.span.min;
                result[i][sizes[i]++] = arc;
            }
            return result;
        }

        private GridLayout.Arc[] topologicalSort(GridLayout.Arc[] arcs) {
            class Sorter {

                final GridLayout.Arc[] result = new GridLayout.Arc[arcs.length];

                int cursor = GridLayout.Axis.super.result.length - 1;

                final GridLayout.Arc[][] arcsByVertex = Axis.this.groupArcsByFirstVertex(arcs);

                final int[] visited = new int[Axis.this.getCount() + 1];

                void walk(int loc) {
                    switch(this.visited[loc]) {
                        case 0:
                            this.visited[loc] = 1;
                            for (GridLayout.Arc arc : this.arcsByVertex[loc]) {
                                this.walk(arc.span.max);
                                this.result[this.cursor--] = arc;
                            }
                            this.visited[loc] = 2;
                            break;
                        case 1:
                            assert false;
                        case 2:
                    }
                }

                GridLayout.Arc[] sort() {
                    int loc = 0;
                    for (int N = this.arcsByVertex.length; loc < N; loc++) {
                        this.walk(loc);
                    }
                    assert this.cursor == -1;
                    return this.result;
                }
            }
            return new Sorter().sort();
        }

        private GridLayout.Arc[] topologicalSort(List<GridLayout.Arc> arcs) {
            return this.topologicalSort((GridLayout.Arc[]) arcs.toArray(new GridLayout.Arc[arcs.size()]));
        }

        private void addComponentSizes(List<GridLayout.Arc> result, GridLayout.PackedMap<GridLayout.Interval, GridLayout.MutableInt> links) {
            for (int i = 0; i < ((GridLayout.Interval[]) links.keys).length; i++) {
                GridLayout.Interval key = links.keys[i];
                this.include(result, key, links.values[i], false);
            }
        }

        private GridLayout.Arc[] createArcs() {
            List<GridLayout.Arc> mins = new ArrayList();
            List<GridLayout.Arc> maxs = new ArrayList();
            this.addComponentSizes(mins, this.getForwardLinks());
            this.addComponentSizes(maxs, this.getBackwardLinks());
            if (this.orderPreserved) {
                for (int i = 0; i < this.getCount(); i++) {
                    this.include(mins, new GridLayout.Interval(i, i + 1), new GridLayout.MutableInt(0));
                }
            }
            int N = this.getCount();
            this.include(mins, new GridLayout.Interval(0, N), this.parentMin, false);
            this.include(maxs, new GridLayout.Interval(N, 0), this.parentMax, false);
            GridLayout.Arc[] sMins = this.topologicalSort(mins);
            GridLayout.Arc[] sMaxs = this.topologicalSort(maxs);
            return GridLayout.append(sMins, sMaxs);
        }

        private void computeArcs() {
            this.getForwardLinks();
            this.getBackwardLinks();
        }

        public GridLayout.Arc[] getArcs() {
            if (this.arcs == null) {
                this.arcs = this.createArcs();
            }
            if (!this.arcsValid) {
                this.computeArcs();
                this.arcsValid = true;
            }
            return this.arcs;
        }

        private boolean relax(int[] locations, GridLayout.Arc entry) {
            if (!entry.valid) {
                return false;
            } else {
                GridLayout.Interval span = entry.span;
                int u = span.min;
                int v = span.max;
                int value = entry.value.value;
                int candidate = locations[u] + value;
                if (candidate > locations[v]) {
                    locations[v] = candidate;
                    return true;
                } else {
                    return false;
                }
            }
        }

        private void init(int[] locations) {
            Arrays.fill(locations, 0);
        }

        private String arcsToString(List<GridLayout.Arc> arcs) {
            String var = this.horizontal ? "x" : "y";
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for (GridLayout.Arc arc : arcs) {
                if (first) {
                    first = false;
                } else {
                    result = result.append(", ");
                }
                int src = arc.span.min;
                int dst = arc.span.max;
                int value = arc.value.value;
                result.append(src < dst ? var + dst + "-" + var + src + ">=" + value : var + src + "-" + var + dst + "<=" + -value);
            }
            return result.toString();
        }

        private void logError(String axisName, GridLayout.Arc[] arcs, boolean[] culprits0) {
            List<GridLayout.Arc> culprits = new ArrayList();
            List<GridLayout.Arc> removed = new ArrayList();
            for (int c = 0; c < arcs.length; c++) {
                GridLayout.Arc arc = arcs[c];
                if (culprits0[c]) {
                    culprits.add(arc);
                }
                if (!arc.valid) {
                    removed.add(arc);
                }
            }
            ModernUI.LOGGER.warn(axisName + " constraints: " + this.arcsToString(culprits) + " are inconsistent; permanently removing: " + this.arcsToString(removed) + ". ");
        }

        private boolean solve(GridLayout.Arc[] arcs, int[] locations) {
            return this.solve(arcs, locations, true);
        }

        private boolean solve(GridLayout.Arc[] arcs, int[] locations, boolean modifyOnError) {
            String axisName = this.horizontal ? "horizontal" : "vertical";
            int N = this.getCount() + 1;
            boolean[] originalCulprits = null;
            for (int p = 0; p < arcs.length; p++) {
                this.init(locations);
                for (int i = 0; i < N; i++) {
                    boolean changed = false;
                    for (GridLayout.Arc arc : arcs) {
                        changed |= this.relax(locations, arc);
                    }
                    if (!changed) {
                        if (originalCulprits != null) {
                            this.logError(axisName, arcs, originalCulprits);
                        }
                        return true;
                    }
                }
                if (!modifyOnError) {
                    return false;
                }
                boolean[] culprits = new boolean[arcs.length];
                for (int i = 0; i < N; i++) {
                    int j = 0;
                    for (int length = arcs.length; j < length; j++) {
                        culprits[j] |= this.relax(locations, arcs[j]);
                    }
                }
                if (p == 0) {
                    originalCulprits = culprits;
                }
                for (int i = 0; i < arcs.length; i++) {
                    if (culprits[i]) {
                        GridLayout.Arc arc = arcs[i];
                        if (arc.span.min >= arc.span.max) {
                            arc.valid = false;
                            break;
                        }
                    }
                }
            }
            return true;
        }

        private void computeMargins(boolean leading) {
            int[] margins = leading ? this.leadingMargins : this.trailingMargins;
            int i = 0;
            for (int N = GridLayout.this.getChildCount(); i < N; i++) {
                View c = GridLayout.this.getChildAt(i);
                if (c.getVisibility() != 8) {
                    GridLayout.LayoutParams lp = GridLayout.this.getLayoutParams(c);
                    GridLayout.Spec spec = this.horizontal ? lp.columnSpec : lp.rowSpec;
                    GridLayout.Interval span = spec.span;
                    int index = leading ? span.min : span.max;
                    margins[index] = Math.max(margins[index], GridLayout.this.getMargin1(c, this.horizontal, leading));
                }
            }
        }

        public int[] getLeadingMargins() {
            if (this.leadingMargins == null) {
                this.leadingMargins = new int[this.getCount() + 1];
            }
            if (!this.leadingMarginsValid) {
                this.computeMargins(true);
                this.leadingMarginsValid = true;
            }
            return this.leadingMargins;
        }

        public int[] getTrailingMargins() {
            if (this.trailingMargins == null) {
                this.trailingMargins = new int[this.getCount() + 1];
            }
            if (!this.trailingMarginsValid) {
                this.computeMargins(false);
                this.trailingMarginsValid = true;
            }
            return this.trailingMargins;
        }

        private boolean solve(int[] a) {
            return this.solve(this.getArcs(), a);
        }

        private boolean computeHasWeights() {
            int i = 0;
            for (int N = GridLayout.this.getChildCount(); i < N; i++) {
                View child = GridLayout.this.getChildAt(i);
                if (child.getVisibility() != 8) {
                    GridLayout.LayoutParams lp = GridLayout.this.getLayoutParams(child);
                    GridLayout.Spec spec = this.horizontal ? lp.columnSpec : lp.rowSpec;
                    if (spec.weight != 0.0F) {
                        return true;
                    }
                }
            }
            return false;
        }

        private boolean hasWeights() {
            if (!this.hasWeightsValid) {
                this.hasWeights = this.computeHasWeights();
                this.hasWeightsValid = true;
            }
            return this.hasWeights;
        }

        public int[] getDeltas() {
            if (this.deltas == null) {
                this.deltas = new int[GridLayout.this.getChildCount()];
            }
            return this.deltas;
        }

        private void shareOutDelta(int totalDelta, float totalWeight) {
            Arrays.fill(this.deltas, 0);
            int i = 0;
            for (int N = GridLayout.this.getChildCount(); i < N; i++) {
                View c = GridLayout.this.getChildAt(i);
                if (c.getVisibility() != 8) {
                    GridLayout.LayoutParams lp = GridLayout.this.getLayoutParams(c);
                    GridLayout.Spec spec = this.horizontal ? lp.columnSpec : lp.rowSpec;
                    float weight = spec.weight;
                    if (weight != 0.0F) {
                        int delta = Math.round(weight * (float) totalDelta / totalWeight);
                        this.deltas[i] = delta;
                        totalDelta -= delta;
                        totalWeight -= weight;
                    }
                }
            }
        }

        private void solveAndDistributeSpace(int[] a) {
            Arrays.fill(this.getDeltas(), 0);
            this.solve(a);
            int deltaMax = this.parentMin.value * GridLayout.this.getChildCount() + 1;
            if (deltaMax >= 2) {
                int deltaMin = 0;
                float totalWeight = this.calculateTotalWeight();
                int validDelta = -1;
                boolean validSolution = true;
                while (deltaMin < deltaMax) {
                    int delta = (int) (((long) deltaMin + (long) deltaMax) / 2L);
                    this.invalidateValues();
                    this.shareOutDelta(delta, totalWeight);
                    validSolution = this.solve(this.getArcs(), a, false);
                    if (validSolution) {
                        validDelta = delta;
                        deltaMin = delta + 1;
                    } else {
                        deltaMax = delta;
                    }
                }
                if (validDelta > 0 && !validSolution) {
                    this.invalidateValues();
                    this.shareOutDelta(validDelta, totalWeight);
                    this.solve(a);
                }
            }
        }

        private float calculateTotalWeight() {
            float totalWeight = 0.0F;
            int i = 0;
            for (int N = GridLayout.this.getChildCount(); i < N; i++) {
                View c = GridLayout.this.getChildAt(i);
                if (c.getVisibility() != 8) {
                    GridLayout.LayoutParams lp = GridLayout.this.getLayoutParams(c);
                    GridLayout.Spec spec = this.horizontal ? lp.columnSpec : lp.rowSpec;
                    totalWeight += spec.weight;
                }
            }
            return totalWeight;
        }

        private void computeLocations(int[] a) {
            if (!this.hasWeights()) {
                this.solve(a);
            } else {
                this.solveAndDistributeSpace(a);
            }
            if (!this.orderPreserved) {
                int a0 = a[0];
                int i = 0;
                for (int N = a.length; i < N; i++) {
                    a[i] -= a0;
                }
            }
        }

        public int[] getLocations() {
            if (this.locations == null) {
                int N = this.getCount() + 1;
                this.locations = new int[N];
            }
            if (!this.locationsValid) {
                this.computeLocations(this.locations);
                this.locationsValid = true;
            }
            return this.locations;
        }

        private int size(int[] locations) {
            return locations[this.getCount()];
        }

        private void setParentConstraints(int min, int max) {
            this.parentMin.value = min;
            this.parentMax.value = -max;
            this.locationsValid = false;
        }

        private int getMeasure(int min, int max) {
            this.setParentConstraints(min, max);
            return this.size(this.getLocations());
        }

        public int getMeasure(int measureSpec) {
            int mode = MeasureSpec.getMode(measureSpec);
            int size = MeasureSpec.getSize(measureSpec);
            switch(mode) {
                case Integer.MIN_VALUE:
                    return this.getMeasure(0, size);
                case 0:
                    return this.getMeasure(0, 100000);
                case 1073741824:
                    return this.getMeasure(size, size);
                default:
                    assert false;
                    return 0;
            }
        }

        public void layout(int size) {
            this.setParentConstraints(size, size);
            this.getLocations();
        }

        public void invalidateStructure() {
            this.maxIndex = Integer.MIN_VALUE;
            this.groupBounds = null;
            this.forwardLinks = null;
            this.backwardLinks = null;
            this.leadingMargins = null;
            this.trailingMargins = null;
            this.arcs = null;
            this.locations = null;
            this.deltas = null;
            this.hasWeightsValid = false;
            this.invalidateValues();
        }

        public void invalidateValues() {
            this.groupBoundsValid = false;
            this.forwardLinksValid = false;
            this.backwardLinksValid = false;
            this.leadingMarginsValid = false;
            this.trailingMarginsValid = false;
            this.arcsValid = false;
            this.locationsValid = false;
        }
    }

    static class Bounds {

        public int before;

        public int after;

        public int flexibility;

        private Bounds() {
            this.reset();
        }

        protected void reset() {
            this.before = Integer.MIN_VALUE;
            this.after = Integer.MIN_VALUE;
            this.flexibility = 2;
        }

        protected void include(int before, int after) {
            this.before = Math.max(this.before, before);
            this.after = Math.max(this.after, after);
        }

        protected int size(boolean min) {
            return !min && GridLayout.canStretch(this.flexibility) ? 100000 : this.before + this.after;
        }

        protected int getOffset(GridLayout gl, View c, GridLayout.Alignment a, int size, boolean horizontal) {
            return this.before - a.getAlignmentValue(c, size, 0);
        }

        protected final void include(GridLayout gl, View c, GridLayout.Spec spec, GridLayout.Axis axis, int size) {
            this.flexibility = this.flexibility & spec.getFlexibility();
            boolean horizontal = axis.horizontal;
            GridLayout.Alignment alignment = spec.getAbsoluteAlignment(axis.horizontal);
            int before = alignment.getAlignmentValue(c, size, 0);
            this.include(before, size - before);
        }

        public String toString() {
            return "Bounds{before=" + this.before + ", after=" + this.after + "}";
        }
    }

    static record Interval(int min, int max) {

        int size() {
            return this.max - this.min;
        }

        @NonNull
        GridLayout.Interval inverse() {
            return new GridLayout.Interval(this.max, this.min);
        }

        public String toString() {
            return "[" + this.min + ", " + this.max + "]";
        }
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {

        private static final int DEFAULT_WIDTH = -2;

        private static final int DEFAULT_HEIGHT = -2;

        private static final int DEFAULT_MARGIN = Integer.MIN_VALUE;

        private static final int DEFAULT_ROW = Integer.MIN_VALUE;

        private static final int DEFAULT_COLUMN = Integer.MIN_VALUE;

        private static final GridLayout.Interval DEFAULT_SPAN = new GridLayout.Interval(Integer.MIN_VALUE, -2147483647);

        private static final int DEFAULT_SPAN_SIZE = DEFAULT_SPAN.size();

        public GridLayout.Spec rowSpec = GridLayout.Spec.UNDEFINED;

        public GridLayout.Spec columnSpec = GridLayout.Spec.UNDEFINED;

        private LayoutParams(int width, int height, int left, int top, int right, int bottom, GridLayout.Spec rowSpec, GridLayout.Spec columnSpec) {
            super(width, height);
            this.setMargins(left, top, right, bottom);
            this.rowSpec = rowSpec;
            this.columnSpec = columnSpec;
        }

        public LayoutParams(GridLayout.Spec rowSpec, GridLayout.Spec columnSpec) {
            this(-2, -2, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, rowSpec, columnSpec);
        }

        public LayoutParams() {
            this(GridLayout.Spec.UNDEFINED, GridLayout.Spec.UNDEFINED);
        }

        public LayoutParams(ViewGroup.LayoutParams params) {
            super(params);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams params) {
            super(params);
        }

        public LayoutParams(GridLayout.LayoutParams source) {
            super((ViewGroup.MarginLayoutParams) source);
            this.rowSpec = source.rowSpec;
            this.columnSpec = source.columnSpec;
        }

        public void setGravity(int gravity) {
            this.rowSpec = this.rowSpec.copyWriteAlignment(GridLayout.getAlignment(gravity, false));
            this.columnSpec = this.columnSpec.copyWriteAlignment(GridLayout.getAlignment(gravity, true));
        }

        final void setRowSpecSpan(GridLayout.Interval span) {
            this.rowSpec = this.rowSpec.copyWriteSpan(span);
        }

        final void setColumnSpecSpan(GridLayout.Interval span) {
            this.columnSpec = this.columnSpec.copyWriteSpan(span);
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (o != null && this.getClass() == o.getClass()) {
                GridLayout.LayoutParams that = (GridLayout.LayoutParams) o;
                return !this.columnSpec.equals(that.columnSpec) ? false : this.rowSpec.equals(that.rowSpec);
            } else {
                return false;
            }
        }

        public int hashCode() {
            int result = this.rowSpec.hashCode();
            return 31 * result + this.columnSpec.hashCode();
        }
    }

    static final class MutableInt {

        public int value;

        public MutableInt() {
            this.reset();
        }

        public MutableInt(int value) {
            this.value = value;
        }

        public void reset() {
            this.value = Integer.MIN_VALUE;
        }

        public String toString() {
            return Integer.toString(this.value);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @Internal
    public @interface Orientation {
    }

    static final class PackedMap<K, V> {

        public final int[] index;

        public final K[] keys;

        public final V[] values;

        private PackedMap(K[] keys, V[] values) {
            this.index = createIndex(keys);
            this.keys = (K[]) compact(keys, this.index);
            this.values = compact(values, this.index);
        }

        public V getValue(int i) {
            return this.values[this.index[i]];
        }

        private static <K> int[] createIndex(K[] keys) {
            int size = keys.length;
            int[] result = new int[size];
            Map<K, Integer> keyToIndex = new HashMap();
            for (int i = 0; i < size; i++) {
                K key = keys[i];
                Integer index = (Integer) keyToIndex.computeIfAbsent(key, k -> keyToIndex.size());
                result[i] = index;
            }
            return result;
        }

        private static <K> K[] compact(K[] a, int[] index) {
            int size = a.length;
            Class<?> componentType = a.getClass().getComponentType();
            K[] result = (K[]) Array.newInstance(componentType, GridLayout.max2(index, -1) + 1);
            for (int i = 0; i < size; i++) {
                result[index[i]] = a[i];
            }
            return result;
        }
    }

    public static class Spec {

        static final GridLayout.Spec UNDEFINED = GridLayout.spec(Integer.MIN_VALUE);

        static final float DEFAULT_WEIGHT = 0.0F;

        final boolean startDefined;

        final GridLayout.Interval span;

        final GridLayout.Alignment alignment;

        final float weight;

        private Spec(boolean startDefined, GridLayout.Interval span, GridLayout.Alignment alignment, float weight) {
            this.startDefined = startDefined;
            this.span = span;
            this.alignment = alignment;
            this.weight = weight;
        }

        private Spec(boolean startDefined, int start, int size, GridLayout.Alignment alignment, float weight) {
            this(startDefined, new GridLayout.Interval(start, start + size), alignment, weight);
        }

        private GridLayout.Alignment getAbsoluteAlignment(boolean horizontal) {
            if (this.alignment != GridLayout.UNDEFINED_ALIGNMENT) {
                return this.alignment;
            } else if (this.weight == 0.0F) {
                return horizontal ? GridLayout.START : GridLayout.BASELINE;
            } else {
                return GridLayout.FILL;
            }
        }

        final GridLayout.Spec copyWriteSpan(GridLayout.Interval span) {
            return new GridLayout.Spec(this.startDefined, span, this.alignment, this.weight);
        }

        final GridLayout.Spec copyWriteAlignment(GridLayout.Alignment alignment) {
            return new GridLayout.Spec(this.startDefined, this.span, alignment, this.weight);
        }

        final int getFlexibility() {
            return this.alignment == GridLayout.UNDEFINED_ALIGNMENT && this.weight == 0.0F ? 0 : 2;
        }

        public boolean equals(Object that) {
            if (this == that) {
                return true;
            } else if (that != null && this.getClass() == that.getClass()) {
                GridLayout.Spec spec = (GridLayout.Spec) that;
                return !this.alignment.equals(spec.alignment) ? false : this.span.equals(spec.span);
            } else {
                return false;
            }
        }

        public int hashCode() {
            int result = this.span.hashCode();
            return 31 * result + this.alignment.hashCode();
        }
    }
}