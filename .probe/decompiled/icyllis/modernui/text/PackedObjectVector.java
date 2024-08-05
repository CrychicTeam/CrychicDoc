package icyllis.modernui.text;

import icyllis.modernui.util.GrowingArrayUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrays;

public class PackedObjectVector<E> {

    private final int mColumns;

    private int mRows;

    private int mRowGapStart;

    private int mRowGapLength;

    private Object[] mValues;

    public PackedObjectVector(int columns) {
        this.mColumns = columns;
        this.mValues = ObjectArrays.EMPTY_ARRAY;
        this.mRows = 0;
        this.mRowGapStart = 0;
    }

    public E getValue(int row, int column) {
        if (row >= this.mRowGapStart) {
            row += this.mRowGapLength;
        }
        return (E) this.mValues[row * this.mColumns + column];
    }

    public void setValue(int row, int column, E value) {
        if (row >= this.mRowGapStart) {
            row += this.mRowGapLength;
        }
        this.mValues[row * this.mColumns + column] = value;
    }

    public void insertAt(int row, E[] values) {
        this.moveRowGapTo(row);
        if (this.mRowGapLength == 0) {
            this.growBuffer();
        }
        this.mRowGapStart++;
        this.mRowGapLength--;
        if (values == null) {
            for (int i = 0; i < this.mColumns; i++) {
                this.setValue(row, i, null);
            }
        } else {
            for (int i = 0; i < this.mColumns; i++) {
                this.setValue(row, i, values[i]);
            }
        }
    }

    public void deleteAt(int row, int count) {
        this.moveRowGapTo(row + count);
        this.mRowGapStart -= count;
        this.mRowGapLength += count;
        if (this.mRowGapLength > this.size() * 2) {
        }
    }

    public int size() {
        return this.mRows - this.mRowGapLength;
    }

    public int width() {
        return this.mColumns;
    }

    private void growBuffer() {
        Object[] newValues = new Object[GrowingArrayUtils.growSize(this.size()) * this.mColumns];
        int newSize = newValues.length / this.mColumns;
        int after = this.mRows - (this.mRowGapStart + this.mRowGapLength);
        System.arraycopy(this.mValues, 0, newValues, 0, this.mColumns * this.mRowGapStart);
        System.arraycopy(this.mValues, (this.mRows - after) * this.mColumns, newValues, (newSize - after) * this.mColumns, after * this.mColumns);
        this.mRowGapLength = this.mRowGapLength + (newSize - this.mRows);
        this.mRows = newSize;
        this.mValues = newValues;
    }

    private void moveRowGapTo(int where) {
        if (where != this.mRowGapStart) {
            if (where > this.mRowGapStart) {
                int moving = where + this.mRowGapLength - (this.mRowGapStart + this.mRowGapLength);
                for (int i = this.mRowGapStart + this.mRowGapLength; i < this.mRowGapStart + this.mRowGapLength + moving; i++) {
                    int dstRow = i - (this.mRowGapStart + this.mRowGapLength) + this.mRowGapStart;
                    if (this.mColumns >= 0) {
                        System.arraycopy(this.mValues, i * this.mColumns, this.mValues, dstRow * this.mColumns, this.mColumns);
                    }
                }
            } else {
                int moving = this.mRowGapStart - where;
                for (int ix = where + moving - 1; ix >= where; ix--) {
                    int dstRow = ix - where + this.mRowGapStart + this.mRowGapLength - moving;
                    if (this.mColumns >= 0) {
                        System.arraycopy(this.mValues, ix * this.mColumns, this.mValues, dstRow * this.mColumns, this.mColumns);
                    }
                }
            }
            this.mRowGapStart = where;
        }
    }
}