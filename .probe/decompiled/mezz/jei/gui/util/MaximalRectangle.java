package mezz.jei.gui.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.common.util.MathUtil;

public class MaximalRectangle {

    private final ImmutableRect2i area;

    private final int rows;

    private final int columns;

    private final int samplingScale;

    private final boolean[][] blockedAreas;

    public static Stream<ImmutableRect2i> getLargestRectangles(ImmutableRect2i area, Collection<ImmutableRect2i> exclusionAreas, int samplingScale) {
        exclusionAreas = (Collection<ImmutableRect2i>) exclusionAreas.stream().filter(area::intersects).collect(Collectors.toUnmodifiableSet());
        if (exclusionAreas.isEmpty()) {
            return Stream.of(area);
        } else {
            MaximalRectangle maximalRectangle = new MaximalRectangle(area, samplingScale, exclusionAreas);
            return maximalRectangle.getLargestRectangles();
        }
    }

    private MaximalRectangle(ImmutableRect2i area, int samplingScale, Collection<ImmutableRect2i> exclusionAreas) {
        this.area = area;
        this.samplingScale = samplingScale;
        this.rows = area.getHeight() / samplingScale;
        this.columns = area.getWidth() / samplingScale;
        this.blockedAreas = new boolean[this.rows][this.columns];
        for (int row = 0; row < this.rows; row++) {
            for (int column = 0; column < this.columns; column++) {
                ImmutableRect2i rect = this.getRect(row, column, 1, 1);
                boolean intersects = MathUtil.intersects(exclusionAreas, rect);
                this.blockedAreas[row][column] = intersects;
            }
        }
    }

    private ImmutableRect2i getRect(int row, int column, int width, int height) {
        return width != 0 && height != 0 ? new ImmutableRect2i(this.area.getX() + column * this.samplingScale, this.area.getY() + row * this.samplingScale, width * this.samplingScale, height * this.samplingScale) : ImmutableRect2i.EMPTY;
    }

    private Stream<ImmutableRect2i> getLargestRectangles() {
        if (this.rows != 0 && this.columns != 0) {
            int[] heights = new int[this.columns];
            int[] leftIndexes = new int[this.columns];
            int[] rightIndexes = new int[this.columns];
            Arrays.fill(rightIndexes, this.columns);
            return IntStream.range(0, this.rows).boxed().flatMap(row -> {
                int currentLeftIndex = 0;
                for (int column = 0; column < this.columns; column++) {
                    if (this.blockedAreas[row][column]) {
                        heights[column] = 0;
                        leftIndexes[column] = 0;
                        currentLeftIndex = column + 1;
                    } else {
                        heights[column]++;
                        leftIndexes[column] = Math.max(leftIndexes[column], currentLeftIndex);
                    }
                }
                int currentRightIndex = this.columns;
                for (int columnx = this.columns - 1; columnx >= 0; columnx--) {
                    if (this.blockedAreas[row][columnx]) {
                        rightIndexes[columnx] = this.columns;
                        currentRightIndex = columnx;
                    } else {
                        rightIndexes[columnx] = Math.min(rightIndexes[columnx], currentRightIndex);
                    }
                }
                return IntStream.range(0, this.columns).mapToObj(columnxx -> {
                    int rightIndex = rightIndexes[columnxx];
                    int leftIndex = leftIndexes[columnxx];
                    int width = rightIndex - leftIndex;
                    int height = heights[columnxx];
                    return this.getRect(row - height + 1, leftIndex, width, height);
                }).filter(r -> !r.isEmpty());
            });
        } else {
            return Stream.empty();
        }
    }
}