package icyllis.modernui.util;

import icyllis.modernui.ModernUI;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.graphics.MathUtil;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.Consumer;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public final class DataSet implements Map<String, Object>, Parcelable {

    private static final Marker MARKER = MarkerManager.getMarker("DataSet");

    @NonNull
    public static final Parcelable.ClassLoaderCreator<DataSet> CREATOR = Parcel::readDataSet;

    private static final int DEFAULT_INITIAL_SIZE = 16;

    private static final float DEFAULT_LOAD_FACTOR = 0.75F;

    private String[] mKey;

    private Object[] mValue;

    private int mHead = -1;

    private int mTail = -1;

    private long[] mLink;

    private int mSize;

    private int mThreshold;

    private Set<Entry<String, Object>> mEntries;

    private Set<String> mKeys;

    private Collection<Object> mValues;

    public DataSet() {
        this.mKey = new String[16];
        this.mValue = new Object[16];
        this.mLink = new long[16];
        this.mThreshold = 12;
    }

    DataSet(int n) {
        n = (int) Math.ceil((double) ((float) n / 0.75F));
        n = Math.max(n, 16);
        if (n > 1073741824) {
            throw new AssertionError(n);
        } else {
            n = MathUtil.ceilPow2(n);
            this.mKey = new String[n];
            this.mValue = new Object[n];
            this.mLink = new long[n];
            this.mThreshold = (int) ((float) n * 0.75F);
        }
    }

    static int hash(@NonNull Object key) {
        int h;
        return (h = key.hashCode() * -1640531535) ^ h >>> 16;
    }

    public int size() {
        return this.mSize;
    }

    public boolean isEmpty() {
        return this.mSize == 0;
    }

    private int find(String key) {
        Objects.requireNonNull(key);
        String[] keys = this.mKey;
        int mask = keys.length - 1;
        String k;
        int pos;
        if ((k = keys[pos = hash(key) & mask]) == null) {
            return -pos - 1;
        } else if (key.equals(k)) {
            return pos;
        } else {
            while ((k = keys[pos = pos + 1 & mask]) != null) {
                if (key.equals(k)) {
                    return pos;
                }
            }
            return -pos - 1;
        }
    }

    public boolean containsKey(Object key) {
        Objects.requireNonNull(key);
        String[] keys = this.mKey;
        int mask = keys.length - 1;
        String k;
        int pos;
        if ((k = keys[pos = hash(key) & mask]) == null) {
            return false;
        } else if (k != key && !key.equals(k)) {
            while ((k = keys[pos = pos + 1 & mask]) != null) {
                if (k == key || key.equals(k)) {
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }

    public boolean containsValue(Object value) {
        Object[] v = this.mValue;
        String[] k = this.mKey;
        int i = k.length;
        while (i-- != 0) {
            if (k[i] != null && Objects.equals(value, v[i])) {
                return true;
            }
        }
        return false;
    }

    public Object get(Object key) {
        Objects.requireNonNull(key);
        String[] keys = this.mKey;
        int mask = keys.length - 1;
        String k;
        int pos;
        if ((k = keys[pos = hash(key) & mask]) == null) {
            return null;
        } else if (k != key && !key.equals(k)) {
            while ((k = keys[pos = pos + 1 & mask]) != null) {
                if (k == key || key.equals(k)) {
                    return this.mValue[pos];
                }
            }
            return null;
        } else {
            return this.mValue[pos];
        }
    }

    public Object getOrDefault(Object key, Object defaultValue) {
        Object o = this.get(key);
        return o == null ? defaultValue : o;
    }

    @Nullable
    public Object putIfAbsent(String key, Object value) {
        int pos = this.find(key);
        if (pos >= 0) {
            return this.mValue[pos];
        } else {
            this.insert(-pos - 1, key, value);
            return null;
        }
    }

    public <T> T getValue(String key) {
        Object o = this.get(key);
        if (o == null) {
            return null;
        } else {
            try {
                return (T) o;
            } catch (ClassCastException var4) {
                typeWarning(key, o, "<T>", var4);
                return null;
            }
        }
    }

    public <T> T getValue(String key, Class<T> clazz) {
        Object o = this.get(key);
        if (o == null) {
            return null;
        } else {
            try {
                return (T) o;
            } catch (ClassCastException var5) {
                typeWarning(key, o, clazz.getName(), var5);
                return null;
            }
        }
    }

    public boolean getBoolean(String key) {
        return this.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        Object o = this.get(key);
        if (o == null) {
            return defaultValue;
        } else {
            try {
                return (Boolean) o;
            } catch (ClassCastException var5) {
                typeWarning(key, o, "Boolean", defaultValue, var5);
                return defaultValue;
            }
        }
    }

    public byte getByte(String key) {
        return this.getByte(key, (byte) 0);
    }

    public byte getByte(String key, byte defaultValue) {
        Object o = this.get(key);
        if (o == null) {
            return defaultValue;
        } else {
            try {
                return (Byte) o;
            } catch (ClassCastException var5) {
                typeWarning(key, o, "Byte", defaultValue, var5);
                return defaultValue;
            }
        }
    }

    public char getChar(String key) {
        return this.getChar(key, '0');
    }

    public char getChar(String key, char defaultValue) {
        Object o = this.get(key);
        if (o == null) {
            return defaultValue;
        } else {
            try {
                return (Character) o;
            } catch (ClassCastException var5) {
                typeWarning(key, o, "Character", defaultValue, var5);
                return defaultValue;
            }
        }
    }

    public short getShort(String key) {
        return this.getShort(key, (short) 0);
    }

    public short getShort(String key, short defaultValue) {
        Object o = this.get(key);
        if (o == null) {
            return defaultValue;
        } else {
            try {
                return (Short) o;
            } catch (ClassCastException var5) {
                typeWarning(key, o, "Short", defaultValue, var5);
                return defaultValue;
            }
        }
    }

    public int getInt(String key) {
        return this.getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        Object o = this.get(key);
        if (o == null) {
            return defaultValue;
        } else {
            try {
                return (Integer) o;
            } catch (ClassCastException var5) {
                typeWarning(key, o, "Integer", defaultValue, var5);
                return defaultValue;
            }
        }
    }

    public long getLong(String key) {
        return this.getLong(key, 0L);
    }

    public long getLong(String key, long defaultValue) {
        Object o = this.get(key);
        if (o == null) {
            return defaultValue;
        } else {
            try {
                return (Long) o;
            } catch (ClassCastException var6) {
                typeWarning(key, o, "Long", defaultValue, var6);
                return defaultValue;
            }
        }
    }

    public float getFloat(String key) {
        return this.getFloat(key, 0.0F);
    }

    public float getFloat(String key, float defaultValue) {
        Object o = this.get(key);
        if (o == null) {
            return defaultValue;
        } else {
            try {
                return (Float) o;
            } catch (ClassCastException var5) {
                typeWarning(key, o, "Float", defaultValue, var5);
                return defaultValue;
            }
        }
    }

    public double getDouble(String key) {
        return this.getDouble(key, 0.0);
    }

    public double getDouble(String key, double defaultValue) {
        Object o = this.get(key);
        if (o == null) {
            return defaultValue;
        } else {
            try {
                return (Double) o;
            } catch (ClassCastException var6) {
                typeWarning(key, o, "Double", defaultValue, var6);
                return defaultValue;
            }
        }
    }

    public byte[] getByteArray(String key) {
        Object o = this.get(key);
        if (o == null) {
            return null;
        } else {
            try {
                return (byte[]) o;
            } catch (ClassCastException var4) {
                typeWarning(key, o, "byte[]", var4);
                return null;
            }
        }
    }

    public short[] getShortArray(String key) {
        Object o = this.get(key);
        if (o == null) {
            return null;
        } else {
            try {
                return (short[]) o;
            } catch (ClassCastException var4) {
                typeWarning(key, o, "short[]", var4);
                return null;
            }
        }
    }

    public int[] getIntArray(String key) {
        Object o = this.get(key);
        if (o == null) {
            return null;
        } else {
            try {
                return (int[]) o;
            } catch (ClassCastException var4) {
                typeWarning(key, o, "int[]", var4);
                return null;
            }
        }
    }

    public long[] getLongArray(String key) {
        Object o = this.get(key);
        if (o == null) {
            return null;
        } else {
            try {
                return (long[]) o;
            } catch (ClassCastException var4) {
                typeWarning(key, o, "long[]", var4);
                return null;
            }
        }
    }

    public float[] getFloatArray(String key) {
        Object o = this.get(key);
        if (o == null) {
            return null;
        } else {
            try {
                return (float[]) o;
            } catch (ClassCastException var4) {
                typeWarning(key, o, "float[]", var4);
                return null;
            }
        }
    }

    public double[] getDoubleArray(String key) {
        Object o = this.get(key);
        if (o == null) {
            return null;
        } else {
            try {
                return (double[]) o;
            } catch (ClassCastException var4) {
                typeWarning(key, o, "double[]", var4);
                return null;
            }
        }
    }

    public String getString(String key) {
        Object o = this.get(key);
        if (o == null) {
            return null;
        } else {
            try {
                return (String) o;
            } catch (ClassCastException var4) {
                typeWarning(key, o, "String", var4);
                return null;
            }
        }
    }

    @NonNull
    public String getString(String key, String defaultValue) {
        Object o = this.get(key);
        if (o == null) {
            return defaultValue;
        } else {
            try {
                return (String) o;
            } catch (ClassCastException var5) {
                typeWarning(key, o, "String", defaultValue, var5);
                return defaultValue;
            }
        }
    }

    public UUID getUUID(String key) {
        Object o = this.get(key);
        if (o == null) {
            return null;
        } else {
            try {
                return (UUID) o;
            } catch (ClassCastException var4) {
                typeWarning(key, o, "UUID", var4);
                return null;
            }
        }
    }

    @NonNull
    public UUID getUUID(String key, UUID defaultValue) {
        Object o = this.get(key);
        if (o == null) {
            return defaultValue;
        } else {
            try {
                return (UUID) o;
            } catch (ClassCastException var5) {
                typeWarning(key, o, "UUID", defaultValue, var5);
                return defaultValue;
            }
        }
    }

    public <T> List<T> getList(String key) {
        Object o = this.get(key);
        if (o == null) {
            return null;
        } else {
            try {
                return (List<T>) o;
            } catch (ClassCastException var4) {
                typeWarning(key, o, "List<T>", var4);
                return null;
            }
        }
    }

    public DataSet getDataSet(String key) {
        Object o = this.get(key);
        if (o == null) {
            return null;
        } else {
            try {
                return (DataSet) o;
            } catch (ClassCastException var4) {
                typeWarning(key, o, "DataSet", var4);
                return null;
            }
        }
    }

    private void insert(int pos, String key, Object value) {
        this.mKey[pos] = key;
        this.mValue[pos] = value;
        if (this.mSize == 0) {
            this.mHead = this.mTail = pos;
            this.mLink[pos] = -1L;
        } else {
            this.mLink[this.mTail] = this.mLink[this.mTail] ^ (this.mLink[this.mTail] ^ (long) pos & 4294967295L) & 4294967295L;
            this.mLink[pos] = ((long) this.mTail & 4294967295L) << 32 | 4294967295L;
            this.mTail = pos;
        }
        if (this.mSize++ >= this.mThreshold) {
            int cap = this.mKey.length;
            if (cap > 1073741824) {
                throw new IllegalStateException("hashtable is too large");
            }
            this.rehash(cap << 1);
        }
    }

    private void rehash(int cap) {
        String[] key = this.mKey;
        Object[] value = this.mValue;
        int mask = cap - 1;
        String[] newKey = new String[cap];
        Object[] newValue = new Object[cap];
        int i = this.mHead;
        int prev = -1;
        int newPrev = -1;
        long[] link = this.mLink;
        long[] newLink = new long[cap];
        this.mHead = -1;
        int j = this.mSize;
        while (j-- != 0) {
            int pos;
            if (key[i] == null) {
                pos = cap;
            } else {
                pos = hash(key[i]) & mask;
                while (newKey[pos] != null) {
                    pos = pos + 1 & mask;
                }
            }
            newKey[pos] = key[i];
            newValue[pos] = value[i];
            if (prev != -1) {
                newLink[newPrev] ^= (newLink[newPrev] ^ (long) pos & 4294967295L) & 4294967295L;
                newLink[pos] ^= (newLink[pos] ^ ((long) newPrev & 4294967295L) << 32) & -4294967296L;
                newPrev = pos;
            } else {
                newPrev = this.mHead = pos;
                newLink[pos] = -1L;
            }
            int t = i;
            i = (int) link[i];
            prev = t;
        }
        this.mLink = newLink;
        this.mTail = newPrev;
        if (newPrev != -1) {
            newLink[newPrev] |= 4294967295L;
        }
        this.mThreshold = (int) ((float) cap * 0.75F);
        this.mKey = newKey;
        this.mValue = newValue;
    }

    @Nullable
    public Object put(String key, Object value) {
        if (value == this) {
            throw new IllegalArgumentException("closed loop");
        } else {
            int pos = this.find(key);
            if (pos < 0) {
                this.insert(-pos - 1, key, value);
                return null;
            } else {
                Object oldValue = this.mValue[pos];
                this.mValue[pos] = value;
                return oldValue;
            }
        }
    }

    public void putByte(String key, byte value) {
        this.put(key, value);
    }

    public void putShort(String key, short value) {
        this.put(key, value);
    }

    public void putInt(String key, int value) {
        this.put(key, value);
    }

    public void putLong(String key, long value) {
        this.put(key, value);
    }

    public void putFloat(String key, float value) {
        this.put(key, value);
    }

    public void putDouble(String key, double value) {
        this.put(key, value);
    }

    public void putBoolean(String key, boolean value) {
        this.put(key, value);
    }

    public void putString(String key, String value) {
        this.put(key, value);
    }

    public void putUUID(String key, UUID value) {
        this.put(key, value);
    }

    public void putList(String key, List<?> value) {
        this.put(key, value);
    }

    public void putDataSet(String key, DataSet value) {
        this.put(key, value);
    }

    public void putByteArray(String key, byte[] value) {
        this.put(key, value);
    }

    public void putShortArray(String key, short[] value) {
        this.put(key, value);
    }

    public void putIntArray(String key, int[] value) {
        this.put(key, value);
    }

    public void putLongArray(String key, long[] value) {
        this.put(key, value);
    }

    public void putFloatArray(String key, float[] value) {
        this.put(key, value);
    }

    public void putDoubleArray(String key, double[] value) {
        this.put(key, value);
    }

    private void updateLinks(int i) {
        if (this.mSize == 0) {
            this.mHead = this.mTail = -1;
        } else if (this.mHead == i) {
            this.mHead = (int) this.mLink[i];
            if (this.mHead >= 0) {
                this.mLink[this.mHead] = this.mLink[this.mHead] | -4294967296L;
            }
        } else if (this.mTail == i) {
            this.mTail = (int) (this.mLink[i] >>> 32);
            if (this.mTail >= 0) {
                this.mLink[this.mTail] = this.mLink[this.mTail] | 4294967295L;
            }
        } else {
            long link = this.mLink[i];
            int prev = (int) (link >>> 32);
            int next = (int) link;
            this.mLink[prev] = this.mLink[prev] ^ (this.mLink[prev] ^ link & 4294967295L) & 4294967295L;
            this.mLink[next] = this.mLink[next] ^ (this.mLink[next] ^ link & -4294967296L) & -4294967296L;
        }
    }

    private void updateLinks(int x, int y) {
        if (this.mSize == 1) {
            this.mHead = this.mTail = y;
            this.mLink[y] = -1L;
        } else if (this.mHead == x) {
            this.mHead = y;
            int next = (int) this.mLink[x];
            this.mLink[next] = this.mLink[next] ^ (this.mLink[next] ^ ((long) y & 4294967295L) << 32) & -4294967296L;
            this.mLink[y] = this.mLink[x];
        } else if (this.mTail == x) {
            this.mTail = y;
            int prev = (int) (this.mLink[x] >>> 32);
            this.mLink[prev] = this.mLink[prev] ^ (this.mLink[prev] ^ (long) y & 4294967295L) & 4294967295L;
            this.mLink[y] = this.mLink[x];
        } else {
            long link = this.mLink[x];
            int prev = (int) (link >>> 32);
            int next = (int) link;
            this.mLink[prev] = this.mLink[prev] ^ (this.mLink[prev] ^ (long) y & 4294967295L) & 4294967295L;
            this.mLink[next] = this.mLink[next] ^ (this.mLink[next] ^ ((long) y & 4294967295L) << 32) & -4294967296L;
            this.mLink[y] = link;
        }
    }

    private Object removeEntry(int pos) {
        Object value = this.mValue[pos];
        this.mValue[pos] = null;
        this.mSize--;
        this.updateLinks(pos);
        this.shiftKeys(pos);
        if (this.mSize < this.mThreshold / 4 && this.mKey.length > 16) {
            this.rehash(this.mKey.length / 2);
        }
        return value;
    }

    private void shiftKeys(int pos) {
        String[] key = this.mKey;
        int mask = key.length - 1;
        label30: while (true) {
            int prev = pos;
            String k;
            for (pos = pos + 1 & mask; (k = key[pos]) != null; pos = pos + 1 & mask) {
                int i = hash(k) & mask;
                if (prev <= pos ? prev >= i || i > pos : prev >= i && i > pos) {
                    key[prev] = k;
                    this.mValue[prev] = this.mValue[pos];
                    this.updateLinks(pos, prev);
                    continue label30;
                }
            }
            key[prev] = null;
            this.mValue[prev] = null;
            return;
        }
    }

    public Object remove(Object key) {
        Objects.requireNonNull(key);
        String[] keys = this.mKey;
        int mask = keys.length - 1;
        String k;
        int pos;
        if ((k = keys[pos = hash(key) & mask]) == null) {
            return null;
        } else if (k != key && !key.equals(k)) {
            while ((k = keys[pos = pos + 1 & mask]) != null) {
                if (k == key || key.equals(k)) {
                    return this.removeEntry(pos);
                }
            }
            return null;
        } else {
            return this.removeEntry(pos);
        }
    }

    public Object removeFirst() {
        if (this.mSize == 0) {
            throw new NoSuchElementException();
        } else {
            int pos = this.mHead;
            this.mHead = (int) this.mLink[pos];
            if (this.mHead >= 0) {
                this.mLink[this.mHead] = this.mLink[this.mHead] | -4294967296L;
            }
            this.mSize--;
            Object v = this.mValue[pos];
            this.shiftKeys(pos);
            if (this.mSize < this.mThreshold / 4 && this.mKey.length > 16) {
                this.rehash(this.mKey.length / 2);
            }
            return v;
        }
    }

    public Object removeLast() {
        if (this.mSize == 0) {
            throw new NoSuchElementException();
        } else {
            int pos = this.mTail;
            this.mTail = (int) (this.mLink[pos] >>> 32);
            if (this.mTail >= 0) {
                this.mLink[this.mTail] = this.mLink[this.mTail] | 4294967295L;
            }
            this.mSize--;
            Object v = this.mValue[pos];
            this.shiftKeys(pos);
            if (this.mSize < this.mThreshold / 4 && this.mKey.length > 16) {
                this.rehash(this.mKey.length / 2);
            }
            return v;
        }
    }

    public void putAll(@NonNull Map<? extends String, ?> map) {
        int capacity = (int) Math.min(1073741824L, 1L << -Long.numberOfLeadingZeros((long) Math.ceil((double) ((float) (this.mSize + map.size()) / 0.75F)) - 1L));
        if (capacity > this.mKey.length) {
            this.rehash(capacity);
        }
        for (Entry<? extends String, ?> e : map.entrySet()) {
            this.put((String) e.getKey(), e.getValue());
        }
    }

    public void clear() {
        if (this.mSize != 0) {
            this.mSize = 0;
            Arrays.fill(this.mKey, null);
            Arrays.fill(this.mValue, null);
            this.mHead = this.mTail = -1;
        }
    }

    @NonNull
    public Set<String> keySet() {
        if (this.mKeys == null) {
            this.mKeys = new DataSet.KeySet();
        }
        return this.mKeys;
    }

    @NonNull
    public Collection<Object> values() {
        if (this.mValues == null) {
            this.mValues = new DataSet.Values();
        }
        return this.mValues;
    }

    @NonNull
    public Set<Entry<String, Object>> entrySet() {
        if (this.mEntries == null) {
            this.mEntries = new DataSet.MapEntrySet();
        }
        return this.mEntries;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeDataSet(this);
    }

    public int hashCode() {
        int h = 0;
        int j = this.mSize;
        for (int i = 0; j-- != 0; i++) {
            while (this.mKey[i] == null) {
                i++;
            }
            h += this.mKey[i].hashCode() ^ Objects.hashCode(this.mValue[i]);
        }
        return h;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Map<?, ?> m)) {
            return false;
        } else if (m.size() != this.size()) {
            return false;
        } else {
            try {
                DataSet.FastEntryIterator it = new DataSet.FastEntryIterator();
                while (it.hasNext()) {
                    Entry<String, Object> e = it.next();
                    String key = (String) e.getKey();
                    Object value = e.getValue();
                    if (value == null) {
                        if (m.get(key) != null || !m.containsKey(key)) {
                            return false;
                        }
                    } else if (!value.equals(m.get(key))) {
                        return false;
                    }
                }
                return true;
            } catch (NullPointerException | ClassCastException var7) {
                return false;
            }
        }
    }

    public String toString() {
        if (this.isEmpty()) {
            return "{}";
        } else {
            StringBuilder s = new StringBuilder();
            s.append('{');
            DataSet.FastEntryIterator it = new DataSet.FastEntryIterator();
            while (true) {
                s.append(it.next());
                if (!it.hasNext()) {
                    return s.append('}').toString();
                }
                s.append(',').append(' ');
            }
        }
    }

    static void typeWarning(int key, Object value, String className, ClassCastException e) {
        typeWarning(key, value, className, "<null>", e);
    }

    static void typeWarning(int key, Object value, String className, Object defaultValue, ClassCastException e) {
        ModernUI.LOGGER.warn(MARKER, "Key {} expected {} but value was a {}. The default value {} was returned.", key, className, value.getClass().getName(), defaultValue);
        ModernUI.LOGGER.warn(MARKER, "Attempt to cast generated internal exception", e);
    }

    static void typeWarning(String key, Object value, String className, ClassCastException e) {
        typeWarning(key, value, className, "<null>", e);
    }

    static void typeWarning(String key, Object value, String className, Object defaultValue, ClassCastException e) {
        ModernUI.LOGGER.warn(MARKER, "Key {} expected {} but value was a {}. The default value {} was returned.", key, className, value.getClass().getName(), defaultValue);
        ModernUI.LOGGER.warn(MARKER, "Attempt to cast generated internal exception", e);
    }

    private final class EntryIterator extends DataSet.MapIterator<Consumer<? super Entry<String, Object>>> implements ListIterator<Entry<String, Object>> {

        private DataSet.MapEntry mEntry;

        public EntryIterator() {
        }

        public EntryIterator(String from) {
            super(from);
        }

        void accept(Consumer<? super Entry<String, Object>> action, int index) {
            action.accept(DataSet.this.new MapEntry(index));
        }

        public DataSet.MapEntry next() {
            return this.mEntry = DataSet.this.new MapEntry(this.nextEntry());
        }

        public DataSet.MapEntry previous() {
            return this.mEntry = DataSet.this.new MapEntry(this.previousEntry());
        }

        @Override
        public void remove() {
            super.remove();
            this.mEntry.index = -1;
        }

        public void set(Entry<String, Object> e) {
            throw new UnsupportedOperationException();
        }

        public void add(Entry<String, Object> e) {
            throw new UnsupportedOperationException();
        }
    }

    final class FastEntryIterator extends DataSet.MapIterator<Consumer<? super Entry<String, Object>>> implements ListIterator<Entry<String, Object>> {

        private final DataSet.MapEntry mEntry;

        public FastEntryIterator() {
            this.mEntry = DataSet.this.new MapEntry();
        }

        public FastEntryIterator(String from) {
            super(from);
            this.mEntry = DataSet.this.new MapEntry();
        }

        void accept(Consumer<? super Entry<String, Object>> action, int index) {
            this.mEntry.index = index;
            action.accept(this.mEntry);
        }

        public Entry<String, Object> next() {
            this.mEntry.index = this.nextEntry();
            return this.mEntry;
        }

        public Entry<String, Object> previous() {
            this.mEntry.index = this.previousEntry();
            return this.mEntry;
        }

        public void set(Entry<String, Object> e) {
            throw new UnsupportedOperationException();
        }

        public void add(Entry<String, Object> e) {
            throw new UnsupportedOperationException();
        }
    }

    private final class KeyIterator extends DataSet.MapIterator<Consumer<? super String>> implements ListIterator<String> {

        public KeyIterator() {
        }

        public KeyIterator(String k) {
            super(k);
        }

        void accept(Consumer<? super String> action, int index) {
            action.accept(DataSet.this.mKey[index]);
        }

        public String previous() {
            return DataSet.this.mKey[this.previousEntry()];
        }

        public void set(String s) {
            throw new UnsupportedOperationException();
        }

        public void add(String s) {
            throw new UnsupportedOperationException();
        }

        public String next() {
            return DataSet.this.mKey[this.nextEntry()];
        }
    }

    private final class KeySet extends AbstractSet<String> {

        @NonNull
        public Iterator<String> iterator() {
            return DataSet.this.new KeyIterator();
        }

        public int size() {
            return DataSet.this.mSize;
        }

        public void forEach(Consumer<? super String> action) {
            int i = DataSet.this.mSize;
            int next = DataSet.this.mHead;
            while (i-- != 0) {
                int curr = next;
                next = (int) DataSet.this.mLink[next];
                action.accept(DataSet.this.mKey[curr]);
            }
        }

        public boolean contains(Object o) {
            return DataSet.this.containsKey(o);
        }

        public boolean remove(Object o) {
            return DataSet.this.remove(o) != null;
        }

        public void clear() {
            DataSet.this.clear();
        }
    }

    private final class MapEntry implements Entry<String, Object> {

        int index;

        MapEntry() {
        }

        MapEntry(int index) {
            this.index = index;
        }

        public String getKey() {
            return DataSet.this.mKey[this.index];
        }

        public Object getValue() {
            return DataSet.this.mValue[this.index];
        }

        public Object setValue(Object newValue) {
            Object oldValue = DataSet.this.mValue[this.index];
            DataSet.this.mValue[this.index] = newValue;
            return oldValue;
        }

        public int hashCode() {
            return Objects.hashCode(DataSet.this.mKey[this.index]) ^ Objects.hashCode(DataSet.this.mValue[this.index]);
        }

        public boolean equals(Object o) {
            if (o instanceof Entry<?, ?> e && Objects.equals(DataSet.this.mKey[this.index], e.getKey()) && Objects.equals(DataSet.this.mValue[this.index], e.getValue())) {
                return true;
            }
            return false;
        }

        @NonNull
        public String toString() {
            return DataSet.this.mKey[this.index] + "=" + DataSet.this.mValue[this.index];
        }
    }

    private final class MapEntrySet extends AbstractSet<Entry<String, Object>> {

        @NonNull
        public Iterator<Entry<String, Object>> iterator() {
            return DataSet.this.new EntryIterator();
        }

        public int size() {
            return DataSet.this.mSize;
        }

        public void clear() {
            DataSet.this.clear();
        }
    }

    private abstract class MapIterator<ACTION> {

        int prev = -1;

        int next = -1;

        int curr = -1;

        int index = -1;

        MapIterator() {
            this.next = DataSet.this.mHead;
            this.index = 0;
        }

        MapIterator(String key) {
            Objects.requireNonNull(key);
            if (Objects.equals(key, DataSet.this.mKey[DataSet.this.mTail])) {
                this.prev = DataSet.this.mTail;
                this.index = DataSet.this.mSize;
            } else {
                String[] keys = DataSet.this.mKey;
                int mask = keys.length - 1;
                String k;
                for (int pos = DataSet.hash(key) & mask; (k = keys[pos]) != null; pos = pos + 1 & mask) {
                    if (key.equals(k)) {
                        this.next = (int) DataSet.this.mLink[pos];
                        this.prev = pos;
                        return;
                    }
                }
                throw new NoSuchElementException("The key " + key + " does not belong to this map.");
            }
        }

        abstract void accept(ACTION var1, int var2);

        public boolean hasNext() {
            return this.next != -1;
        }

        public boolean hasPrevious() {
            return this.prev != -1;
        }

        private void forward0() {
            if (this.index < 0) {
                if (this.prev == -1) {
                    this.index = 0;
                } else if (this.next == -1) {
                    this.index = DataSet.this.mSize;
                } else {
                    int pos = DataSet.this.mTail;
                    for (this.index = 1; pos != this.prev; this.index++) {
                        pos = (int) DataSet.this.mLink[pos];
                    }
                }
            }
        }

        public int nextIndex() {
            this.forward0();
            return this.index;
        }

        public int previousIndex() {
            this.forward0();
            return this.index - 1;
        }

        public int nextEntry() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            } else {
                this.curr = this.next;
                this.next = (int) DataSet.this.mLink[this.curr];
                this.prev = this.curr;
                if (this.index >= 0) {
                    this.index++;
                }
                return this.curr;
            }
        }

        public int previousEntry() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            } else {
                this.curr = this.prev;
                this.prev = (int) (DataSet.this.mLink[this.curr] >>> 32);
                this.next = this.curr;
                if (this.index >= 0) {
                    this.index--;
                }
                return this.curr;
            }
        }

        public void forEachRemaining(ACTION action) {
            while (this.hasNext()) {
                this.curr = this.next;
                this.next = (int) DataSet.this.mLink[this.curr];
                this.prev = this.curr;
                if (this.index >= 0) {
                    this.index++;
                }
                this.accept(action, this.curr);
            }
        }

        public void remove() {
            this.forward0();
            if (this.curr == -1) {
                throw new IllegalStateException();
            } else {
                if (this.curr == this.prev) {
                    this.index--;
                    this.prev = (int) (DataSet.this.mLink[this.curr] >>> 32);
                } else {
                    this.next = (int) DataSet.this.mLink[this.curr];
                }
                DataSet.this.mSize--;
                if (this.prev == -1) {
                    DataSet.this.mHead = this.next;
                } else {
                    DataSet.this.mLink[this.prev] = DataSet.this.mLink[this.prev] ^ (DataSet.this.mLink[this.prev] ^ (long) this.next & 4294967295L) & 4294967295L;
                }
                if (this.next == -1) {
                    DataSet.this.mTail = this.prev;
                } else {
                    DataSet.this.mLink[this.next] = DataSet.this.mLink[this.next] ^ (DataSet.this.mLink[this.next] ^ ((long) this.prev & 4294967295L) << 32) & -4294967296L;
                }
                int pos = this.curr;
                this.curr = -1;
                String[] keys = DataSet.this.mKey;
                int mask = keys.length - 1;
                label57: while (true) {
                    int last = pos;
                    String k;
                    for (pos = pos + 1 & mask; (k = keys[pos]) != null; pos = pos + 1 & mask) {
                        int slot = DataSet.hash(k) & mask;
                        if (last <= pos ? last >= slot || slot > pos : last >= slot && slot > pos) {
                            keys[last] = k;
                            DataSet.this.mValue[last] = DataSet.this.mValue[pos];
                            if (this.next == pos) {
                                this.next = last;
                            }
                            if (this.prev == pos) {
                                this.prev = last;
                            }
                            DataSet.this.updateLinks(pos, last);
                            continue label57;
                        }
                    }
                    keys[last] = null;
                    DataSet.this.mValue[last] = null;
                    return;
                }
            }
        }

        public int skip(int n) {
            int i = n;
            while (i-- != 0 && this.hasNext()) {
                this.nextEntry();
            }
            return n - i - 1;
        }

        public int back(int n) {
            int i = n;
            while (i-- != 0 && this.hasPrevious()) {
                this.previousEntry();
            }
            return n - i - 1;
        }
    }

    private final class ValueIterator extends DataSet.MapIterator<Consumer<? super Object>> implements ListIterator<Object> {

        public ValueIterator() {
        }

        void accept(Consumer<? super Object> action, int index) {
            action.accept(DataSet.this.mValue[index]);
        }

        public Object previous() {
            return DataSet.this.mValue[this.previousEntry()];
        }

        public void set(Object o) {
            throw new UnsupportedOperationException();
        }

        public void add(Object o) {
            throw new UnsupportedOperationException();
        }

        public Object next() {
            return DataSet.this.mValue[this.nextEntry()];
        }
    }

    private final class Values extends AbstractCollection<Object> {

        @NonNull
        public Iterator<Object> iterator() {
            return DataSet.this.new ValueIterator();
        }

        public int size() {
            return DataSet.this.mSize;
        }

        public void forEach(Consumer<? super Object> action) {
            int i = DataSet.this.mSize;
            int next = DataSet.this.mHead;
            while (i-- != 0) {
                int curr = next;
                next = (int) DataSet.this.mLink[next];
                action.accept(DataSet.this.mValue[curr]);
            }
        }

        public boolean contains(Object o) {
            return DataSet.this.containsValue(o);
        }

        public void clear() {
            DataSet.this.clear();
        }
    }
}