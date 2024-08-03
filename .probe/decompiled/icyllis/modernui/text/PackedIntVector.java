package icyllis.modernui.text;

import icyllis.modernui.util.GrowingArrayUtils;

public class PackedIntVector {

    private final int mColumns;

    private int mRows;

    private int mRowGapStart;

    private int mRowGapLength;

    private int[] mValues;

    private final int[] mValueGap;

    public PackedIntVector(int columns) {
        this.mColumns = columns;
        this.mRows = 0;
        this.mRowGapStart = 0;
        this.mValues = null;
        this.mValueGap = new int[2 * columns];
    }

    public int getValue(int row, int column) {
        int columns = this.mColumns;
        if ((row | column) >= 0 && row < this.size() && column < columns) {
            if (row >= this.mRowGapStart) {
                row += this.mRowGapLength;
            }
            int value = this.mValues[row * columns + column];
            int[] valuegap = this.mValueGap;
            if (row >= valuegap[column]) {
                value += valuegap[column + columns];
            }
            return value;
        } else {
            throw new IndexOutOfBoundsException(row + ", " + column);
        }
    }

    public void setValue(int row, int column, int value) {
        if ((row | column) >= 0 && row < this.size() && column < this.mColumns) {
            if (row >= this.mRowGapStart) {
                row += this.mRowGapLength;
            }
            int[] valuegap = this.mValueGap;
            if (row >= valuegap[column]) {
                value -= valuegap[column + this.mColumns];
            }
            this.mValues[row * this.mColumns + column] = value;
        } else {
            throw new IndexOutOfBoundsException(row + ", " + column);
        }
    }

    private void setValueInternal(int row, int column, int value) {
        if (row >= this.mRowGapStart) {
            row += this.mRowGapLength;
        }
        int[] valuegap = this.mValueGap;
        if (row >= valuegap[column]) {
            value -= valuegap[column + this.mColumns];
        }
        this.mValues[row * this.mColumns + column] = value;
    }

    public void adjustValuesBelow(int startRow, int column, int delta) {
        if ((startRow | column) >= 0 && startRow <= this.size() && column < this.width()) {
            if (startRow >= this.mRowGapStart) {
                startRow += this.mRowGapLength;
            }
            this.moveValueGapTo(column, startRow);
            this.mValueGap[column + this.mColumns] = this.mValueGap[column + this.mColumns] + delta;
        } else {
            throw new IndexOutOfBoundsException(startRow + ", " + column);
        }
    }

    public void insertAt(int row, int[] values) {
        if (row >= 0 && row <= this.size()) {
            if (values != null && values.length < this.width()) {
                throw new IndexOutOfBoundsException("value count " + values.length);
            } else {
                this.moveRowGapTo(row);
                if (this.mRowGapLength == 0) {
                    this.growBuffer();
                }
                this.mRowGapStart++;
                this.mRowGapLength--;
                if (values == null) {
                    for (int i = this.mColumns - 1; i >= 0; i--) {
                        this.setValueInternal(row, i, 0);
                    }
                } else {
                    for (int i = this.mColumns - 1; i >= 0; i--) {
                        this.setValueInternal(row, i, values[i]);
                    }
                }
            }
        } else {
            throw new IndexOutOfBoundsException("row " + row);
        }
    }

    public void deleteAt(int row, int count) {
        if ((row | count) >= 0 && row + count <= this.size()) {
            this.moveRowGapTo(row + count);
            this.mRowGapStart -= count;
            this.mRowGapLength += count;
        } else {
            throw new IndexOutOfBoundsException(row + ", " + count);
        }
    }

    public int size() {
        return this.mRows - this.mRowGapLength;
    }

    public int width() {
        return this.mColumns;
    }

    private void growBuffer() {
        int columns = this.mColumns;
        int[] newvalues = new int[GrowingArrayUtils.growSize(this.size()) * columns];
        int newsize = newvalues.length / columns;
        int[] valuegap = this.mValueGap;
        int rowgapstart = this.mRowGapStart;
        int after = this.mRows - (rowgapstart + this.mRowGapLength);
        if (this.mValues != null) {
            System.arraycopy(this.mValues, 0, newvalues, 0, columns * rowgapstart);
            System.arraycopy(this.mValues, (this.mRows - after) * columns, newvalues, (newsize - after) * columns, after * columns);
        }
        for (int i = 0; i < columns; i++) {
            if (valuegap[i] >= rowgapstart) {
                valuegap[i] += newsize - this.mRows;
                if (valuegap[i] < rowgapstart) {
                    valuegap[i] = rowgapstart;
                }
            }
        }
        this.mRowGapLength = this.mRowGapLength + (newsize - this.mRows);
        this.mRows = newsize;
        this.mValues = newvalues;
    }

    private void moveValueGapTo(int column, int where) {
        int[] valuegap = this.mValueGap;
        int[] values = this.mValues;
        int columns = this.mColumns;
        if (where != valuegap[column]) {
            if (where > valuegap[column]) {
                for (int i = valuegap[column]; i < where; i++) {
                    values[i * columns + column] = values[i * columns + column] + valuegap[column + columns];
                }
            } else {
                for (int i = where; i < valuegap[column]; i++) {
                    values[i * columns + column] = values[i * columns + column] - valuegap[column + columns];
                }
            }
            valuegap[column] = where;
        }
    }

    private void moveRowGapTo(int where) {
        if (where != this.mRowGapStart) {
            if (where > this.mRowGapStart) {
                int moving = where + this.mRowGapLength - (this.mRowGapStart + this.mRowGapLength);
                int columns = this.mColumns;
                int[] valuegap = this.mValueGap;
                int[] values = this.mValues;
                int gapend = this.mRowGapStart + this.mRowGapLength;
                for (int i = gapend; i < gapend + moving; i++) {
                    int destrow = i - gapend + this.mRowGapStart;
                    for (int j = 0; j < columns; j++) {
                        int val = values[i * columns + j];
                        if (i >= valuegap[j]) {
                            val += valuegap[j + columns];
                        }
                        if (destrow >= valuegap[j]) {
                            val -= valuegap[j + columns];
                        }
                        values[destrow * columns + j] = val;
                    }
                }
            } else {
                int moving = this.mRowGapStart - where;
                int columns = this.mColumns;
                int[] valuegap = this.mValueGap;
                int[] values = this.mValues;
                int gapend = this.mRowGapStart + this.mRowGapLength;
                for (int i = where + moving - 1; i >= where; i--) {
                    int destrow = i - where + gapend - moving;
                    for (int j = 0; j < columns; j++) {
                        int valx = values[i * columns + j];
                        if (i >= valuegap[j]) {
                            valx += valuegap[j + columns];
                        }
                        if (destrow >= valuegap[j]) {
                            valx -= valuegap[j + columns];
                        }
                        values[destrow * columns + j] = valx;
                    }
                }
            }
            this.mRowGapStart = where;
        }
    }
}