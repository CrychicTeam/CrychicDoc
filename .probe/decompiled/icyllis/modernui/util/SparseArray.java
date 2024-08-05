package icyllis.modernui.util;

import it.unimi.dsi.fastutil.ints.IntArrays;
import it.unimi.dsi.fastutil.objects.ObjectArrays;
import java.util.Arrays;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SparseArray<E> implements Cloneable {

    private static final Object DELETED = new Object();

    private boolean mGarbage = false;

    private int[] mKeys;

    private Object[] mValues;

    private int mSize;

    public SparseArray() {
        this(10);
    }

    public SparseArray(int initialCapacity) {
        if (initialCapacity == 0) {
            this.mKeys = IntArrays.EMPTY_ARRAY;
            this.mValues = ObjectArrays.EMPTY_ARRAY;
        } else {
            if (initialCapacity <= 0) {
                throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
            }
            this.mKeys = new int[initialCapacity];
            this.mValues = new Object[initialCapacity];
        }
    }

    public E get(int key) {
        return this.get(key, null);
    }

    public E get(int key, E defaultValue) {
        int i = Arrays.binarySearch(this.mKeys, 0, this.mSize, key);
        return (E) (i >= 0 && this.mValues[i] != DELETED ? this.mValues[i] : defaultValue);
    }

    public void delete(int key) {
        int i = Arrays.binarySearch(this.mKeys, 0, this.mSize, key);
        if (i >= 0 && this.mValues[i] != DELETED) {
            this.mValues[i] = DELETED;
            this.mGarbage = true;
        }
    }

    public E remove(int key) {
        int i = Arrays.binarySearch(this.mKeys, 0, this.mSize, key);
        if (i >= 0 && this.mValues[i] != DELETED) {
            E old = (E) this.mValues[i];
            this.mValues[i] = DELETED;
            this.mGarbage = true;
            return old;
        } else {
            return null;
        }
    }

    public boolean remove(int key, Object value) {
        int index = this.indexOfKey(key);
        if (index >= 0) {
            E mapValue = this.valueAt(index);
            if (Objects.equals(value, mapValue)) {
                this.deleteAt(index);
                return true;
            }
        }
        return false;
    }

    public void deleteAt(int index) {
        Objects.checkIndex(index, this.mSize);
        if (this.mValues[index] != DELETED) {
            this.mValues[index] = DELETED;
            this.mGarbage = true;
        }
    }

    public E removeAt(int index) {
        Objects.checkIndex(index, this.mSize);
        if (this.mValues[index] != DELETED) {
            E old = (E) this.mValues[index];
            this.mValues[index] = DELETED;
            this.mGarbage = true;
            return old;
        } else {
            return null;
        }
    }

    public void removeAtRange(int index, int size) {
        Objects.checkFromIndexSize(index, size, this.mSize);
        for (; size-- > 0; index++) {
            if (this.mValues[index] != DELETED) {
                this.mValues[index] = DELETED;
                this.mGarbage = true;
            }
        }
    }

    @Nullable
    public E replace(int key, E value) {
        int index = this.indexOfKey(key);
        if (index >= 0) {
            E oldValue = (E) this.mValues[index];
            this.mValues[index] = value;
            return oldValue;
        } else {
            return null;
        }
    }

    public boolean replace(int key, E oldValue, E newValue) {
        int index = this.indexOfKey(key);
        if (index >= 0) {
            Object mapValue = this.mValues[index];
            if (Objects.equals(oldValue, mapValue)) {
                this.mValues[index] = newValue;
                return true;
            }
        }
        return false;
    }

    private void gc() {
        int n = this.mSize;
        int o = 0;
        int[] keys = this.mKeys;
        Object[] values = this.mValues;
        for (int i = 0; i < n; i++) {
            Object val = values[i];
            if (val != DELETED) {
                if (i != o) {
                    keys[o] = keys[i];
                    values[o] = val;
                    values[i] = null;
                }
                o++;
            }
        }
        this.mGarbage = false;
        this.mSize = o;
    }

    @Nullable
    public E put(int key, E value) {
        int i = Arrays.binarySearch(this.mKeys, 0, this.mSize, key);
        if (i >= 0) {
            E oldValue = (E) this.mValues[i];
            this.mValues[i] = value;
            return oldValue;
        } else {
            i = ~i;
            if (i < this.mSize && this.mValues[i] == DELETED) {
                this.mKeys[i] = key;
                this.mValues[i] = value;
                return null;
            } else {
                if (this.mGarbage && this.mSize >= this.mKeys.length) {
                    this.gc();
                    i = ~Arrays.binarySearch(this.mKeys, 0, this.mSize, key);
                }
                this.mKeys = GrowingArrayUtils.insert(this.mKeys, this.mSize, i, key);
                this.mValues = GrowingArrayUtils.insert(this.mValues, this.mSize, i, value);
                this.mSize++;
                return null;
            }
        }
    }

    public void putAll(@Nonnull SparseArray<? extends E> other) {
        int i = 0;
        for (int size = other.size(); i < size; i++) {
            this.put(other.keyAt(i), (E) other.valueAt(i));
        }
    }

    @Nullable
    public E putIfAbsent(int key, E value) {
        E mapValue = this.get(key);
        if (mapValue == null) {
            this.put(key, value);
        }
        return mapValue;
    }

    public int size() {
        if (this.mGarbage) {
            this.gc();
        }
        return this.mSize;
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public int keyAt(int index) {
        Objects.checkIndex(index, this.mSize);
        if (this.mGarbage) {
            this.gc();
        }
        return this.mKeys[index];
    }

    public E valueAt(int index) {
        Objects.checkIndex(index, this.mSize);
        if (this.mGarbage) {
            this.gc();
        }
        return (E) this.mValues[index];
    }

    public void setValueAt(int index, E value) {
        Objects.checkIndex(index, this.mSize);
        if (this.mGarbage) {
            this.gc();
        }
        this.mValues[index] = value;
    }

    public int indexOfKey(int key) {
        if (this.mGarbage) {
            this.gc();
        }
        return Arrays.binarySearch(this.mKeys, 0, this.mSize, key);
    }

    public int indexOfValue(E value) {
        if (this.mGarbage) {
            this.gc();
        }
        for (int i = 0; i < this.mSize; i++) {
            if (this.mValues[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public int indexOfValueByValue(E value) {
        if (this.mGarbage) {
            this.gc();
        }
        for (int i = 0; i < this.mSize; i++) {
            if (value == null) {
                if (this.mValues[i] == null) {
                    return i;
                }
            } else if (value.equals(this.mValues[i])) {
                return i;
            }
        }
        return -1;
    }

    public boolean containsKey(int key) {
        return this.indexOfKey(key) >= 0;
    }

    public boolean containsValue(E value) {
        return this.indexOfValue(value) >= 0;
    }

    public void clear() {
        int n = this.mSize;
        Object[] values = this.mValues;
        for (int i = 0; i < n; i++) {
            values[i] = null;
        }
        this.mSize = 0;
        this.mGarbage = false;
    }

    public void append(int key, E value) {
        if (this.mSize != 0 && key <= this.mKeys[this.mSize - 1]) {
            this.put(key, value);
        } else {
            if (this.mGarbage && this.mSize >= this.mKeys.length) {
                this.gc();
            }
            this.mKeys = GrowingArrayUtils.append(this.mKeys, this.mSize, key);
            this.mValues = GrowingArrayUtils.append(this.mValues, this.mSize, value);
            this.mSize++;
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
                Object value = this.valueAt(i);
                if (value != this) {
                    buffer.append(value);
                } else {
                    buffer.append("(this Map)");
                }
            }
            buffer.append('}');
            return buffer.toString();
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            SparseArray<?> other = (SparseArray<?>) o;
            int size = this.size();
            if (size != other.size()) {
                return false;
            } else {
                for (int index = 0; index < size; index++) {
                    int key = this.keyAt(index);
                    if (!Objects.equals(this.valueAt(index), other.get(key))) {
                        return false;
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        int hash = 0;
        int size = this.size();
        for (int index = 0; index < size; index++) {
            int key = this.keyAt(index);
            E value = this.valueAt(index);
            hash = 31 * hash + Integer.hashCode(key);
            hash = 31 * hash + Objects.hashCode(value);
        }
        return hash;
    }

    public SparseArray<E> clone() {
        try {
            SparseArray<E> clone = (SparseArray<E>) super.clone();
            clone.mKeys = (int[]) this.mKeys.clone();
            clone.mValues = (Object[]) this.mValues.clone();
            return clone;
        } catch (CloneNotSupportedException var2) {
            throw new InternalError(var2);
        }
    }
}