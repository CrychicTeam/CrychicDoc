package icyllis.modernui.util;

import icyllis.modernui.ModernUI;
import it.unimi.dsi.fastutil.ints.IntArrays;
import it.unimi.dsi.fastutil.objects.ObjectArrays;
import java.lang.reflect.Array;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.ApiStatus.Internal;

public class ArrayMap<K, V> extends AbstractMap<K, V> implements Map<K, V> {

    private static final Marker MARKER = MarkerManager.getMarker("ArrayMap");

    private static final int BASE_SIZE = 4;

    private static final int CACHE_SIZE = 10;

    private static Object[] mBaseCache;

    private static int mBaseCacheSize;

    private static Object[] mTwiceBaseCache;

    private static int mTwiceBaseCacheSize;

    private static final Object sBaseCacheLock = new Object();

    private static final Object sTwiceBaseCacheLock = new Object();

    private final boolean mIdentityHashCode;

    int[] mHashes;

    Object[] mArray;

    int mSize;

    ArrayMap<K, V>.EntrySet mEntrySet;

    ArrayMap<K, V>.KeySet mKeySet;

    ArrayMap<K, V>.ValuesCollection mValues;

    public ArrayMap() {
        this(0, false);
    }

    public ArrayMap(int initialCapacity) {
        this(initialCapacity, false);
    }

    @Internal
    public ArrayMap(int initialCapacity, boolean identityHashCode) {
        this.mIdentityHashCode = identityHashCode;
        if (initialCapacity == 0) {
            this.mHashes = IntArrays.EMPTY_ARRAY;
            this.mArray = ObjectArrays.EMPTY_ARRAY;
        } else {
            if (initialCapacity <= 0) {
                throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
            }
            this.allocArrays(initialCapacity);
        }
    }

    public ArrayMap(@Nonnull Map<K, V> map) {
        this(0, false);
        this.putAll(map);
    }

    public void clear() {
        if (this.mSize > 0) {
            int[] hashes = this.mHashes;
            Object[] array = this.mArray;
            int size = this.mSize;
            this.mHashes = IntArrays.EMPTY_ARRAY;
            this.mArray = ObjectArrays.EMPTY_ARRAY;
            this.mSize = 0;
            freeArrays(hashes, array, size);
        }
        if (this.mSize > 0) {
            throw new ConcurrentModificationException();
        }
    }

    @Internal
    public void erase() {
        if (this.mSize > 0) {
            int N = this.mSize << 1;
            Object[] array = this.mArray;
            for (int i = 0; i < N; i++) {
                array[i] = null;
            }
            this.mSize = 0;
        }
    }

    public void ensureCapacity(int minimumCapacity) {
        int size = this.mSize;
        if (this.mHashes.length < minimumCapacity) {
            int[] hashes = this.mHashes;
            Object[] array = this.mArray;
            this.allocArrays(minimumCapacity);
            if (this.mSize > 0) {
                System.arraycopy(hashes, 0, this.mHashes, 0, size);
                System.arraycopy(array, 0, this.mArray, 0, size << 1);
            }
            freeArrays(hashes, array, size);
        }
        if (this.mSize != size) {
            throw new ConcurrentModificationException();
        }
    }

    public boolean containsKey(Object key) {
        return this.indexOfKey(key) >= 0;
    }

    public int indexOfKey(Object key) {
        return key == null ? this.indexOfNull() : this.indexOf(key, this.mIdentityHashCode ? System.identityHashCode(key) : key.hashCode());
    }

    public int indexOfValue(Object value) {
        int N = this.mSize * 2;
        Object[] array = this.mArray;
        if (value == null) {
            for (int i = 1; i < N; i += 2) {
                if (array[i] == null) {
                    return i >> 1;
                }
            }
        } else {
            for (int ix = 1; ix < N; ix += 2) {
                if (value.equals(array[ix])) {
                    return ix >> 1;
                }
            }
        }
        return -1;
    }

    public boolean containsValue(Object value) {
        return this.indexOfValue(value) >= 0;
    }

    public V get(Object key) {
        int index = this.indexOfKey(key);
        return (V) (index >= 0 ? this.mArray[(index << 1) + 1] : null);
    }

    public K keyAt(int index) {
        if (index >= this.mSize) {
            throw new ArrayIndexOutOfBoundsException(index);
        } else {
            return (K) this.mArray[index << 1];
        }
    }

    public V valueAt(int index) {
        if (index >= this.mSize) {
            throw new ArrayIndexOutOfBoundsException(index);
        } else {
            return (V) this.mArray[(index << 1) + 1];
        }
    }

    public V setValueAt(int index, V value) {
        if (index >= this.mSize) {
            throw new ArrayIndexOutOfBoundsException(index);
        } else {
            index = (index << 1) + 1;
            V old = (V) this.mArray[index];
            this.mArray[index] = value;
            return old;
        }
    }

    public boolean isEmpty() {
        return this.mSize == 0;
    }

    @Nullable
    public V put(K key, V value) {
        int oldSize = this.mSize;
        int hash;
        int index;
        if (key == null) {
            hash = 0;
            index = this.indexOfNull();
        } else {
            hash = this.mIdentityHashCode ? System.identityHashCode(key) : key.hashCode();
            index = this.indexOf(key, hash);
        }
        if (index >= 0) {
            index = (index << 1) + 1;
            V old = (V) this.mArray[index];
            this.mArray[index] = value;
            return old;
        } else {
            index = ~index;
            if (oldSize >= this.mHashes.length) {
                int n = oldSize >= 8 ? oldSize + (oldSize >> 1) : (oldSize >= 4 ? 8 : 4);
                int[] hashes = this.mHashes;
                Object[] array = this.mArray;
                this.allocArrays(n);
                if (oldSize != this.mSize) {
                    throw new ConcurrentModificationException();
                }
                if (this.mHashes.length > 0) {
                    System.arraycopy(hashes, 0, this.mHashes, 0, hashes.length);
                    System.arraycopy(array, 0, this.mArray, 0, array.length);
                }
                freeArrays(hashes, array, oldSize);
            }
            if (index < oldSize) {
                System.arraycopy(this.mHashes, index, this.mHashes, index + 1, oldSize - index);
                System.arraycopy(this.mArray, index << 1, this.mArray, index + 1 << 1, this.mSize - index << 1);
            }
            if (oldSize == this.mSize && index < this.mHashes.length) {
                this.mHashes[index] = hash;
                this.mArray[index << 1] = key;
                this.mArray[(index << 1) + 1] = value;
                this.mSize++;
                return null;
            } else {
                throw new ConcurrentModificationException();
            }
        }
    }

    @Internal
    public void append(K key, V value) {
        int index = this.mSize;
        int hash = key == null ? 0 : (this.mIdentityHashCode ? System.identityHashCode(key) : key.hashCode());
        if (index >= this.mHashes.length) {
            throw new IllegalStateException("Array is full");
        } else if (index > 0 && this.mHashes[index - 1] > hash) {
            ModernUI.LOGGER.warn(MARKER, "New hash " + hash + " is before end of array hash " + this.mHashes[index - 1] + " at index " + index + " key " + key, new RuntimeException("here").fillInStackTrace());
            this.put(key, value);
        } else {
            this.mSize = index + 1;
            this.mHashes[index] = hash;
            index <<= 1;
            this.mArray[index] = key;
            this.mArray[index + 1] = value;
        }
    }

    @Internal
    public void validate() {
        int size = this.mSize;
        if (size > 1) {
            int baseHash = this.mHashes[0];
            int baseIndex = 0;
            for (int i = 1; i < size; i++) {
                int hash = this.mHashes[i];
                if (hash != baseHash) {
                    baseHash = hash;
                    baseIndex = i;
                } else {
                    Object cur = this.mArray[i << 1];
                    for (int j = i - 1; j >= baseIndex; j--) {
                        Object prev = this.mArray[j << 1];
                        if (cur == prev) {
                            throw new IllegalArgumentException("Duplicate key in ArrayMap: " + cur);
                        }
                        if (cur != null && cur.equals(prev)) {
                            throw new IllegalArgumentException("Duplicate key in ArrayMap: " + cur);
                        }
                    }
                }
            }
        }
    }

    public void forEach(BiConsumer<? super K, ? super V> action) {
        Objects.requireNonNull(action);
        int size = this.mSize;
        for (int i = 0; i < size; i++) {
            if (size != this.mSize) {
                throw new ConcurrentModificationException();
            }
            action.accept(this.keyAt(i), this.valueAt(i));
        }
    }

    public void putAll(@Nonnull Map<? extends K, ? extends V> map) {
        if (map instanceof ArrayMap<? extends K, ? extends V> array) {
            int size = array.mSize;
            this.ensureCapacity(this.mSize + size);
            if (this.mSize == 0) {
                if (size > 0) {
                    System.arraycopy(array.mHashes, 0, this.mHashes, 0, size);
                    System.arraycopy(array.mArray, 0, this.mArray, 0, size << 1);
                    this.mSize = size;
                }
            } else {
                for (int i = 0; i < size; i++) {
                    this.put((K) array.keyAt(i), (V) array.valueAt(i));
                }
            }
        } else {
            this.ensureCapacity(this.mSize + map.size());
            for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
                this.put((K) entry.getKey(), (V) entry.getValue());
            }
        }
    }

    public V remove(Object key) {
        int index = this.indexOfKey(key);
        return index >= 0 ? this.removeAt(index) : null;
    }

    public V removeAt(int index) {
        if (index >= this.mSize) {
            throw new ArrayIndexOutOfBoundsException(index);
        } else {
            Object old = this.mArray[(index << 1) + 1];
            int oldSize = this.mSize;
            int newSize;
            if (oldSize <= 1) {
                int[] hashes = this.mHashes;
                Object[] array = this.mArray;
                this.mHashes = IntArrays.EMPTY_ARRAY;
                this.mArray = ObjectArrays.EMPTY_ARRAY;
                freeArrays(hashes, array, oldSize);
                newSize = 0;
            } else {
                newSize = oldSize - 1;
                if (this.mHashes.length > 8 && this.mSize < this.mHashes.length / 3) {
                    int n = oldSize > 8 ? oldSize + (oldSize >> 1) : 8;
                    int[] hashes = this.mHashes;
                    Object[] array = this.mArray;
                    this.allocArrays(n);
                    if (oldSize != this.mSize) {
                        throw new ConcurrentModificationException();
                    }
                    if (index > 0) {
                        System.arraycopy(hashes, 0, this.mHashes, 0, index);
                        System.arraycopy(array, 0, this.mArray, 0, index << 1);
                    }
                    if (index < newSize) {
                        System.arraycopy(hashes, index + 1, this.mHashes, index, newSize - index);
                        System.arraycopy(array, index + 1 << 1, this.mArray, index << 1, newSize - index << 1);
                    }
                } else {
                    if (index < newSize) {
                        System.arraycopy(this.mHashes, index + 1, this.mHashes, index, newSize - index);
                        System.arraycopy(this.mArray, index + 1 << 1, this.mArray, index << 1, newSize - index << 1);
                    }
                    this.mArray[newSize << 1] = null;
                    this.mArray[(newSize << 1) + 1] = null;
                }
            }
            if (oldSize != this.mSize) {
                throw new ConcurrentModificationException();
            } else {
                this.mSize = newSize;
                return (V) old;
            }
        }
    }

    public int size() {
        return this.mSize;
    }

    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof Map<?, ?> map)) {
            return false;
        } else if (this.size() != map.size()) {
            return false;
        } else {
            try {
                for (int i = 0; i < this.mSize; i++) {
                    K key = this.keyAt(i);
                    V mine = this.valueAt(i);
                    Object theirs = map.get(key);
                    if (mine == null) {
                        if (theirs != null || !map.containsKey(key)) {
                            return false;
                        }
                    } else if (!mine.equals(theirs)) {
                        return false;
                    }
                }
                return true;
            } catch (ClassCastException | NullPointerException var7) {
                return false;
            }
        }
    }

    public int hashCode() {
        int[] hashes = this.mHashes;
        Object[] array = this.mArray;
        int result = 0;
        int i = 0;
        int v = 1;
        for (int s = this.mSize; i < s; v += 2) {
            Object value = array[v];
            result += hashes[i] ^ (value == null ? 0 : value.hashCode());
            i++;
        }
        return result;
    }

    @Nonnull
    public String toString() {
        if (this.isEmpty()) {
            return "{}";
        } else {
            StringBuilder buffer = new StringBuilder(this.mSize * 28);
            buffer.append('{');
            for (int i = 0; i < this.mSize; i++) {
                if (i > 0) {
                    buffer.append(", ");
                }
                Object key = this.keyAt(i);
                if (key != this) {
                    buffer.append(key);
                } else {
                    buffer.append("(this Map)");
                }
                buffer.append('=');
                Object value = this.valueAt(i);
                if (value != this) {
                    buffer.append(deepToString(value));
                } else {
                    buffer.append("(this Map)");
                }
            }
            buffer.append('}');
            return buffer.toString();
        }
    }

    private static String deepToString(Object value) {
        if (value != null && value.getClass().isArray()) {
            if (value.getClass() == boolean[].class) {
                return Arrays.toString((boolean[]) value);
            } else if (value.getClass() == byte[].class) {
                return Arrays.toString((byte[]) value);
            } else if (value.getClass() == char[].class) {
                return Arrays.toString((char[]) value);
            } else if (value.getClass() == double[].class) {
                return Arrays.toString((double[]) value);
            } else if (value.getClass() == float[].class) {
                return Arrays.toString((float[]) value);
            } else if (value.getClass() == int[].class) {
                return Arrays.toString((int[]) value);
            } else if (value.getClass() == long[].class) {
                return Arrays.toString((long[]) value);
            } else {
                return value.getClass() == short[].class ? Arrays.toString((short[]) value) : Arrays.deepToString((Object[]) value);
            }
        } else {
            return String.valueOf(value);
        }
    }

    public boolean containsAll(@Nonnull Collection<?> collection) {
        for (Object o : collection) {
            if (!this.containsKey(o)) {
                return false;
            }
        }
        return true;
    }

    public boolean removeAll(@Nonnull Collection<?> collection) {
        int oldSize = this.mSize;
        for (Object o : collection) {
            this.remove(o);
        }
        return oldSize != this.mSize;
    }

    public boolean retainAll(@Nonnull Collection<?> collection) {
        int oldSize = this.mSize;
        Iterator<K> it = new ArrayMap.ArrayIterator<>(0);
        while (it.hasNext()) {
            if (!collection.contains(it.next())) {
                it.remove();
            }
        }
        return oldSize != this.mSize;
    }

    public Set<Entry<K, V>> entrySet() {
        if (this.mEntrySet == null) {
            this.mEntrySet = new ArrayMap.EntrySet();
        }
        return this.mEntrySet;
    }

    public Set<K> keySet() {
        if (this.mKeySet == null) {
            this.mKeySet = new ArrayMap.KeySet();
        }
        return this.mKeySet;
    }

    public Collection<V> values() {
        if (this.mValues == null) {
            this.mValues = new ArrayMap.ValuesCollection();
        }
        return this.mValues;
    }

    int indexOf(@Nonnull Object key, int hash) {
        int N = this.mSize;
        if (N == 0) {
            return -1;
        } else {
            int index;
            try {
                index = Arrays.binarySearch(this.mHashes, 0, N, hash);
            } catch (ArrayIndexOutOfBoundsException var7) {
                throw new ConcurrentModificationException();
            }
            if (index < 0) {
                return index;
            } else if (key.equals(this.mArray[index << 1])) {
                return index;
            } else {
                int end;
                for (end = index + 1; end < N && this.mHashes[end] == hash; end++) {
                    if (key.equals(this.mArray[end << 1])) {
                        return end;
                    }
                }
                for (int i = index - 1; i >= 0 && this.mHashes[i] == hash; i--) {
                    if (key.equals(this.mArray[i << 1])) {
                        return i;
                    }
                }
                return ~end;
            }
        }
    }

    int indexOfNull() {
        int N = this.mSize;
        if (N == 0) {
            return -1;
        } else {
            int index;
            try {
                index = Arrays.binarySearch(this.mHashes, 0, N, 0);
            } catch (ArrayIndexOutOfBoundsException var5) {
                throw new ConcurrentModificationException();
            }
            if (index < 0) {
                return index;
            } else if (null == this.mArray[index << 1]) {
                return index;
            } else {
                int end;
                for (end = index + 1; end < N && this.mHashes[end] == 0; end++) {
                    if (null == this.mArray[end << 1]) {
                        return end;
                    }
                }
                for (int i = index - 1; i >= 0 && this.mHashes[i] == 0; i--) {
                    if (null == this.mArray[i << 1]) {
                        return i;
                    }
                }
                return ~end;
            }
        }
    }

    private void allocArrays(int size) {
        if (size == 8) {
            synchronized (sTwiceBaseCacheLock) {
                if (mTwiceBaseCache != null) {
                    Object[] array = mTwiceBaseCache;
                    this.mArray = array;
                    try {
                        mTwiceBaseCache = (Object[]) array[0];
                        this.mHashes = (int[]) array[1];
                        if (this.mHashes != null) {
                            array[0] = array[1] = null;
                            mTwiceBaseCacheSize--;
                            return;
                        }
                    } catch (ClassCastException var7) {
                    }
                    ModernUI.LOGGER.fatal(MARKER, "Found corrupt ArrayMap cache: [0]=" + array[0] + " [1]=" + array[1]);
                    mTwiceBaseCache = null;
                    mTwiceBaseCacheSize = 0;
                }
            }
        } else if (size == 4) {
            synchronized (sBaseCacheLock) {
                if (mBaseCache != null) {
                    Object[] array = mBaseCache;
                    this.mArray = array;
                    try {
                        mBaseCache = (Object[]) array[0];
                        this.mHashes = (int[]) array[1];
                        if (this.mHashes != null) {
                            array[0] = array[1] = null;
                            mBaseCacheSize--;
                            return;
                        }
                    } catch (ClassCastException var9) {
                    }
                    ModernUI.LOGGER.fatal(MARKER, "Found corrupt ArrayMap cache: [0]=" + array[0] + " [1]=" + array[1]);
                    mBaseCache = null;
                    mBaseCacheSize = 0;
                }
            }
        }
        this.mHashes = new int[size];
        this.mArray = new Object[size << 1];
    }

    private static void freeArrays(@Nonnull int[] hashes, @Nonnull Object[] array, int size) {
        if (hashes.length == 8) {
            synchronized (sTwiceBaseCacheLock) {
                if (mTwiceBaseCacheSize < 10) {
                    array[0] = mTwiceBaseCache;
                    array[1] = hashes;
                    for (int i = (size << 1) - 1; i >= 2; i--) {
                        array[i] = null;
                    }
                    mTwiceBaseCache = array;
                    mTwiceBaseCacheSize++;
                }
            }
        } else if (hashes.length == 4) {
            synchronized (sBaseCacheLock) {
                if (mBaseCacheSize < 10) {
                    array[0] = mBaseCache;
                    array[1] = hashes;
                    for (int i = (size << 1) - 1; i >= 2; i--) {
                        array[i] = null;
                    }
                    mBaseCache = array;
                    mBaseCacheSize++;
                }
            }
        }
    }

    final class ArrayIterator<T> implements Iterator<T> {

        final int mOffset;

        int mIndex;

        boolean mCanRemove = false;

        ArrayIterator(int offset) {
            this.mOffset = offset;
        }

        public boolean hasNext() {
            return this.mIndex < ArrayMap.this.mSize;
        }

        public T next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            } else {
                Object res = ArrayMap.this.mArray[(this.mIndex << 1) + this.mOffset];
                this.mIndex++;
                this.mCanRemove = true;
                return (T) res;
            }
        }

        public void remove() {
            if (!this.mCanRemove) {
                throw new IllegalStateException();
            } else {
                this.mIndex--;
                ArrayMap.this.mSize--;
                this.mCanRemove = false;
                ArrayMap.this.removeAt(this.mIndex);
            }
        }
    }

    final class EntrySet extends AbstractSet<Entry<K, V>> {

        public boolean add(Entry<K, V> kvEntry) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(@Nonnull Collection<? extends Entry<K, V>> collection) {
            int oldSize = ArrayMap.this.mSize;
            for (Entry<K, V> entry : collection) {
                ArrayMap.this.put((K) entry.getKey(), (V) entry.getValue());
            }
            return oldSize != ArrayMap.this.mSize;
        }

        public void clear() {
            ArrayMap.this.clear();
        }

        public boolean contains(Object o) {
            if (o instanceof Entry<?, ?> e) {
                int index = ArrayMap.this.indexOfKey(e.getKey());
                return index < 0 ? false : Objects.equals(ArrayMap.this.mArray[(index << 1) + 1], e.getValue());
            } else {
                return false;
            }
        }

        public boolean isEmpty() {
            return ArrayMap.this.mSize == 0;
        }

        @Nonnull
        public Iterator<Entry<K, V>> iterator() {
            return ArrayMap.this.new MapIterator();
        }

        public boolean remove(Object object) {
            throw new UnsupportedOperationException();
        }

        public boolean removeAll(@Nonnull Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        public boolean retainAll(@Nonnull Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        public int size() {
            return ArrayMap.this.mSize;
        }

        public Object[] toArray() {
            throw new UnsupportedOperationException();
        }

        public <T> T[] toArray(@Nonnull T[] array) {
            throw new UnsupportedOperationException();
        }

        public boolean equals(@Nullable Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof Set)) {
                return false;
            } else {
                Collection<?> c = (Collection<?>) o;
                if (c.size() != this.size()) {
                    return false;
                } else {
                    try {
                        return this.containsAll(c);
                    } catch (NullPointerException | ClassCastException var4) {
                        return false;
                    }
                }
            }
        }

        public int hashCode() {
            int result = 0;
            for (int i = ArrayMap.this.mSize - 1; i >= 0; i--) {
                Object key = ArrayMap.this.mArray[i << 1];
                Object value = ArrayMap.this.mArray[(i << 1) + 1];
                result += (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
            }
            return result;
        }
    }

    final class KeySet extends AbstractSet<K> {

        public boolean add(K object) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(@Nonnull Collection<? extends K> collection) {
            throw new UnsupportedOperationException();
        }

        public void clear() {
            ArrayMap.this.clear();
        }

        public boolean contains(Object object) {
            return ArrayMap.this.indexOfKey(object) >= 0;
        }

        public boolean containsAll(@Nonnull Collection<?> collection) {
            return ArrayMap.this.containsAll(collection);
        }

        public boolean isEmpty() {
            return ArrayMap.this.mSize == 0;
        }

        @Nonnull
        public Iterator<K> iterator() {
            return ArrayMap.this.new ArrayIterator<>(0);
        }

        public boolean remove(Object object) {
            int index = ArrayMap.this.indexOfKey(object);
            if (index >= 0) {
                ArrayMap.this.removeAt(index);
                return true;
            } else {
                return false;
            }
        }

        public boolean removeAll(@Nonnull Collection<?> collection) {
            return ArrayMap.this.removeAll(collection);
        }

        public boolean retainAll(@Nonnull Collection<?> collection) {
            return ArrayMap.this.retainAll(collection);
        }

        public int size() {
            return ArrayMap.this.mSize;
        }

        @Nonnull
        public Object[] toArray() {
            int N = ArrayMap.this.mSize;
            Object[] result = new Object[N];
            for (int i = 0; i < N; i++) {
                result[i] = ArrayMap.this.mArray[i << 1];
            }
            return result;
        }

        @Nonnull
        public <T> T[] toArray(@Nonnull T[] array) {
            int N = ArrayMap.this.mSize;
            if (array.length < N) {
                array = (T[]) Array.newInstance(array.getClass().getComponentType(), N);
            }
            for (int i = 0; i < N; i++) {
                array[i] = (T) ArrayMap.this.mArray[i << 1];
            }
            if (array.length > N) {
                array[N] = null;
            }
            return array;
        }

        public boolean equals(@Nullable Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof Set)) {
                return false;
            } else {
                Collection<?> c = (Collection<?>) o;
                if (c.size() != this.size()) {
                    return false;
                } else {
                    try {
                        return this.containsAll(c);
                    } catch (NullPointerException | ClassCastException var4) {
                        return false;
                    }
                }
            }
        }

        public int hashCode() {
            int result = 0;
            for (int i = ArrayMap.this.mSize - 1; i >= 0; i--) {
                Object obj = ArrayMap.this.mArray[i << 1];
                result += obj == null ? 0 : obj.hashCode();
            }
            return result;
        }
    }

    final class MapIterator implements Iterator<Entry<K, V>>, Entry<K, V> {

        int mEnd;

        int mIndex;

        boolean mEntryValid = false;

        MapIterator() {
            this.mEnd = ArrayMap.this.mSize - 1;
            this.mIndex = -1;
        }

        public boolean hasNext() {
            return this.mIndex < this.mEnd;
        }

        public Entry<K, V> next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            } else {
                this.mIndex++;
                this.mEntryValid = true;
                return this;
            }
        }

        public void remove() {
            if (!this.mEntryValid) {
                throw new IllegalStateException();
            } else {
                ArrayMap.this.removeAt(this.mIndex);
                this.mIndex--;
                this.mEnd--;
                this.mEntryValid = false;
            }
        }

        public K getKey() {
            if (!this.mEntryValid) {
                throw new IllegalStateException("This container does not support retaining Map.Entry objects");
            } else {
                return (K) ArrayMap.this.mArray[this.mIndex << 1];
            }
        }

        public V getValue() {
            if (!this.mEntryValid) {
                throw new IllegalStateException("This container does not support retaining Map.Entry objects");
            } else {
                return (V) ArrayMap.this.mArray[(this.mIndex << 1) + 1];
            }
        }

        public V setValue(V object) {
            if (!this.mEntryValid) {
                throw new IllegalStateException("This container does not support retaining Map.Entry objects");
            } else {
                return ArrayMap.this.setValueAt(this.mIndex, object);
            }
        }

        public boolean equals(Object o) {
            if (!this.mEntryValid) {
                throw new IllegalStateException("This container does not support retaining Map.Entry objects");
            } else {
                return !(o instanceof Entry<?, ?> e) ? false : Objects.equals(e.getKey(), ArrayMap.this.mArray[this.mIndex << 1]) && Objects.equals(e.getValue(), ArrayMap.this.mArray[(this.mIndex << 1) + 1]);
            }
        }

        public int hashCode() {
            if (!this.mEntryValid) {
                throw new IllegalStateException("This container does not support retaining Map.Entry objects");
            } else {
                Object key = ArrayMap.this.mArray[this.mIndex << 1];
                Object value = ArrayMap.this.mArray[(this.mIndex << 1) + 1];
                return (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
            }
        }

        @Nonnull
        public String toString() {
            return this.getKey() + "=" + this.getValue();
        }
    }

    final class ValuesCollection extends AbstractCollection<V> {

        public boolean add(V object) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(@Nonnull Collection<? extends V> collection) {
            throw new UnsupportedOperationException();
        }

        public void clear() {
            ArrayMap.this.clear();
        }

        public boolean contains(Object object) {
            return ArrayMap.this.indexOfValue(object) >= 0;
        }

        public boolean containsAll(@Nonnull Collection<?> collection) {
            for (Object o : collection) {
                if (!this.contains(o)) {
                    return false;
                }
            }
            return true;
        }

        public boolean isEmpty() {
            return ArrayMap.this.mSize == 0;
        }

        @Nonnull
        public Iterator<V> iterator() {
            return ArrayMap.this.new ArrayIterator<>(1);
        }

        public boolean remove(Object object) {
            int index = ArrayMap.this.indexOfValue(object);
            if (index >= 0) {
                ArrayMap.this.removeAt(index);
                return true;
            } else {
                return false;
            }
        }

        public boolean removeAll(@Nonnull Collection<?> collection) {
            int size = ArrayMap.this.mSize;
            boolean changed = false;
            for (int i = 0; i < size; i++) {
                Object cur = ArrayMap.this.mArray[(i << 1) + 1];
                if (collection.contains(cur)) {
                    ArrayMap.this.removeAt(i);
                    i--;
                    size--;
                    changed = true;
                }
            }
            return changed;
        }

        public boolean retainAll(@Nonnull Collection<?> collection) {
            int size = ArrayMap.this.mSize;
            boolean changed = false;
            for (int i = 0; i < size; i++) {
                Object cur = ArrayMap.this.mArray[(i << 1) + 1];
                if (!collection.contains(cur)) {
                    ArrayMap.this.removeAt(i);
                    i--;
                    size--;
                    changed = true;
                }
            }
            return changed;
        }

        public int size() {
            return ArrayMap.this.mSize;
        }

        @Nonnull
        public Object[] toArray() {
            int size = ArrayMap.this.mSize;
            Object[] result = new Object[size];
            for (int i = 0; i < size; i++) {
                result[i] = ArrayMap.this.mArray[(i << 1) + 1];
            }
            return result;
        }

        @Nonnull
        public <T> T[] toArray(@Nonnull T[] array) {
            int size = ArrayMap.this.mSize;
            if (array.length < size) {
                array = (T[]) Array.newInstance(array.getClass().getComponentType(), size);
            }
            for (int i = 0; i < size; i++) {
                array[i] = (T) ArrayMap.this.mArray[(i << 1) + 1];
            }
            if (array.length > size) {
                array[size] = null;
            }
            return array;
        }
    }
}