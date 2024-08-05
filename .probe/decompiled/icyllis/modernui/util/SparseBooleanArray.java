package icyllis.modernui.util;

import it.unimi.dsi.fastutil.booleans.BooleanArrays;
import it.unimi.dsi.fastutil.ints.IntArrays;
import java.util.Arrays;
import java.util.Objects;
import javax.annotation.Nullable;

public class SparseBooleanArray implements Cloneable {

    private int[] mKeys;

    private boolean[] mValues;

    private int mSize;

    public SparseBooleanArray() {
        this(10);
    }

    public SparseBooleanArray(int initialCapacity) {
        if (initialCapacity == 0) {
            this.mKeys = IntArrays.EMPTY_ARRAY;
            this.mValues = BooleanArrays.EMPTY_ARRAY;
        } else {
            if (initialCapacity <= 0) {
                throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
            }
            this.mKeys = new int[initialCapacity];
            this.mValues = new boolean[this.mKeys.length];
        }
    }

    public boolean get(int key) {
        return this.get(key, false);
    }

    public boolean get(int key, boolean defaultValue) {
        int i = Arrays.binarySearch(this.mKeys, 0, this.mSize, key);
        return i < 0 ? defaultValue : this.mValues[i];
    }

    public void delete(int key) {
        int i = Arrays.binarySearch(this.mKeys, 0, this.mSize, key);
        if (i >= 0) {
            System.arraycopy(this.mKeys, i + 1, this.mKeys, i, this.mSize - (i + 1));
            System.arraycopy(this.mValues, i + 1, this.mValues, i, this.mSize - (i + 1));
            this.mSize--;
        }
    }

    public void removeAt(int index) {
        Objects.checkIndex(index, this.mSize);
        System.arraycopy(this.mKeys, index + 1, this.mKeys, index, this.mSize - (index + 1));
        System.arraycopy(this.mValues, index + 1, this.mValues, index, this.mSize - (index + 1));
        this.mSize--;
    }

    public void put(int key, boolean value) {
        int i = Arrays.binarySearch(this.mKeys, 0, this.mSize, key);
        if (i >= 0) {
            this.mValues[i] = value;
        } else {
            i = ~i;
            this.mKeys = GrowingArrayUtils.insert(this.mKeys, this.mSize, i, key);
            this.mValues = GrowingArrayUtils.insert(this.mValues, this.mSize, i, value);
            this.mSize++;
        }
    }

    public int size() {
        return this.mSize;
    }

    public int keyAt(int index) {
        Objects.checkIndex(index, this.mSize);
        return this.mKeys[index];
    }

    public boolean valueAt(int index) {
        Objects.checkIndex(index, this.mSize);
        return this.mValues[index];
    }

    public void setValueAt(int index, boolean value) {
        Objects.checkIndex(index, this.mSize);
        this.mValues[index] = value;
    }

    public void setKeyAt(int index, int key) {
        Objects.checkIndex(index, this.mSize);
        this.mKeys[index] = key;
    }

    public int indexOfKey(int key) {
        return Arrays.binarySearch(this.mKeys, 0, this.mSize, key);
    }

    public int indexOfValue(boolean value) {
        for (int i = 0; i < this.mSize; i++) {
            if (this.mValues[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public void clear() {
        this.mSize = 0;
    }

    public void append(int key, boolean value) {
        if (this.mSize != 0 && key <= this.mKeys[this.mSize - 1]) {
            this.put(key, value);
        } else {
            this.mKeys = GrowingArrayUtils.append(this.mKeys, this.mSize, key);
            this.mValues = GrowingArrayUtils.append(this.mValues, this.mSize, value);
            this.mSize++;
        }
    }

    public int hashCode() {
        int hashCode = this.mSize;
        for (int i = 0; i < this.mSize; i++) {
            hashCode = 31 * hashCode + this.mKeys[i] | (this.mValues[i] ? 1 : 0);
        }
        return hashCode;
    }

    public boolean equals(@Nullable Object that) {
        if (this == that) {
            return true;
        } else if (that instanceof SparseBooleanArray other) {
            if (this.mSize != other.mSize) {
                return false;
            } else {
                for (int i = 0; i < this.mSize; i++) {
                    if (this.mKeys[i] != other.mKeys[i]) {
                        return false;
                    }
                    if (this.mValues[i] != other.mValues[i]) {
                        return false;
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }

    public String toString() {
        if (this.size() <= 0) {
            return "{}";
        } else {
            StringBuilder buffer = new StringBuilder(this.mSize * 28);
            buffer.append('{');
            for (int i = 0; i < this.mSize; i++) {
                if (i > 0) {
                    buffer.append(", ");
                }
                int key = this.keyAt(i);
                buffer.append(key);
                buffer.append('=');
                boolean value = this.valueAt(i);
                buffer.append(value);
            }
            buffer.append('}');
            return buffer.toString();
        }
    }

    public SparseBooleanArray clone() {
        try {
            SparseBooleanArray clone = (SparseBooleanArray) super.clone();
            clone.mKeys = (int[]) this.mKeys.clone();
            clone.mValues = (boolean[]) this.mValues.clone();
            return clone;
        } catch (CloneNotSupportedException var2) {
            throw new InternalError(var2);
        }
    }
}