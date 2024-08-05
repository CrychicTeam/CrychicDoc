package me.lucko.spark.lib.protobuf;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.Set;
import java.util.Map.Entry;

public final class Internal {

    static final Charset US_ASCII = Charset.forName("US-ASCII");

    static final Charset UTF_8 = Charset.forName("UTF-8");

    static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");

    private static final int DEFAULT_BUFFER_SIZE = 4096;

    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    public static final ByteBuffer EMPTY_BYTE_BUFFER = ByteBuffer.wrap(EMPTY_BYTE_ARRAY);

    public static final CodedInputStream EMPTY_CODED_INPUT_STREAM = CodedInputStream.newInstance(EMPTY_BYTE_ARRAY);

    private Internal() {
    }

    static <T> T checkNotNull(T obj) {
        if (obj == null) {
            throw new NullPointerException();
        } else {
            return obj;
        }
    }

    static <T> T checkNotNull(T obj, String message) {
        if (obj == null) {
            throw new NullPointerException(message);
        } else {
            return obj;
        }
    }

    public static String stringDefaultValue(String bytes) {
        return new String(bytes.getBytes(ISO_8859_1), UTF_8);
    }

    public static ByteString bytesDefaultValue(String bytes) {
        return ByteString.copyFrom(bytes.getBytes(ISO_8859_1));
    }

    public static byte[] byteArrayDefaultValue(String bytes) {
        return bytes.getBytes(ISO_8859_1);
    }

    public static ByteBuffer byteBufferDefaultValue(String bytes) {
        return ByteBuffer.wrap(byteArrayDefaultValue(bytes));
    }

    public static ByteBuffer copyByteBuffer(ByteBuffer source) {
        ByteBuffer temp = source.duplicate();
        temp.clear();
        ByteBuffer result = ByteBuffer.allocate(temp.capacity());
        result.put(temp);
        result.clear();
        return result;
    }

    public static boolean isValidUtf8(ByteString byteString) {
        return byteString.isValidUtf8();
    }

    public static boolean isValidUtf8(byte[] byteArray) {
        return Utf8.isValidUtf8(byteArray);
    }

    public static byte[] toByteArray(String value) {
        return value.getBytes(UTF_8);
    }

    public static String toStringUtf8(byte[] bytes) {
        return new String(bytes, UTF_8);
    }

    public static int hashLong(long n) {
        return (int) (n ^ n >>> 32);
    }

    public static int hashBoolean(boolean b) {
        return b ? 1231 : 1237;
    }

    public static int hashEnum(Internal.EnumLite e) {
        return e.getNumber();
    }

    public static int hashEnumList(List<? extends Internal.EnumLite> list) {
        int hash = 1;
        for (Internal.EnumLite e : list) {
            hash = 31 * hash + hashEnum(e);
        }
        return hash;
    }

    public static boolean equals(List<byte[]> a, List<byte[]> b) {
        if (a.size() != b.size()) {
            return false;
        } else {
            for (int i = 0; i < a.size(); i++) {
                if (!Arrays.equals((byte[]) a.get(i), (byte[]) b.get(i))) {
                    return false;
                }
            }
            return true;
        }
    }

    public static int hashCode(List<byte[]> list) {
        int hash = 1;
        for (byte[] bytes : list) {
            hash = 31 * hash + hashCode(bytes);
        }
        return hash;
    }

    public static int hashCode(byte[] bytes) {
        return hashCode(bytes, 0, bytes.length);
    }

    static int hashCode(byte[] bytes, int offset, int length) {
        int h = partialHash(length, bytes, offset, length);
        return h == 0 ? 1 : h;
    }

    static int partialHash(int h, byte[] bytes, int offset, int length) {
        for (int i = offset; i < offset + length; i++) {
            h = h * 31 + bytes[i];
        }
        return h;
    }

    public static boolean equalsByteBuffer(ByteBuffer a, ByteBuffer b) {
        return a.capacity() != b.capacity() ? false : a.duplicate().clear().equals(b.duplicate().clear());
    }

    public static boolean equalsByteBuffer(List<ByteBuffer> a, List<ByteBuffer> b) {
        if (a.size() != b.size()) {
            return false;
        } else {
            for (int i = 0; i < a.size(); i++) {
                if (!equalsByteBuffer((ByteBuffer) a.get(i), (ByteBuffer) b.get(i))) {
                    return false;
                }
            }
            return true;
        }
    }

    public static int hashCodeByteBuffer(List<ByteBuffer> list) {
        int hash = 1;
        for (ByteBuffer bytes : list) {
            hash = 31 * hash + hashCodeByteBuffer(bytes);
        }
        return hash;
    }

    public static int hashCodeByteBuffer(ByteBuffer bytes) {
        if (bytes.hasArray()) {
            int h = partialHash(bytes.capacity(), bytes.array(), bytes.arrayOffset(), bytes.capacity());
            return h == 0 ? 1 : h;
        } else {
            int bufferSize = bytes.capacity() > 4096 ? 4096 : bytes.capacity();
            byte[] buffer = new byte[bufferSize];
            ByteBuffer duplicated = bytes.duplicate();
            duplicated.clear();
            int h = bytes.capacity();
            while (duplicated.remaining() > 0) {
                int length = duplicated.remaining() <= bufferSize ? duplicated.remaining() : bufferSize;
                duplicated.get(buffer, 0, length);
                h = partialHash(h, buffer, 0, length);
            }
            return h == 0 ? 1 : h;
        }
    }

    public static <T extends MessageLite> T getDefaultInstance(Class<T> clazz) {
        try {
            java.lang.reflect.Method method = clazz.getMethod("getDefaultInstance");
            return (T) method.invoke(method);
        } catch (Exception var2) {
            throw new RuntimeException("Failed to get default instance for " + clazz, var2);
        }
    }

    static Object mergeMessage(Object destination, Object source) {
        return ((MessageLite) destination).toBuilder().mergeFrom((MessageLite) source).buildPartial();
    }

    public interface BooleanList extends Internal.ProtobufList<Boolean> {

        boolean getBoolean(int var1);

        void addBoolean(boolean var1);

        boolean setBoolean(int var1, boolean var2);

        Internal.BooleanList mutableCopyWithCapacity(int var1);
    }

    public interface DoubleList extends Internal.ProtobufList<Double> {

        double getDouble(int var1);

        void addDouble(double var1);

        double setDouble(int var1, double var2);

        Internal.DoubleList mutableCopyWithCapacity(int var1);
    }

    public interface EnumLite {

        int getNumber();
    }

    public interface EnumLiteMap<T extends Internal.EnumLite> {

        T findValueByNumber(int var1);
    }

    public interface EnumVerifier {

        boolean isInRange(int var1);
    }

    public interface FloatList extends Internal.ProtobufList<Float> {

        float getFloat(int var1);

        void addFloat(float var1);

        float setFloat(int var1, float var2);

        Internal.FloatList mutableCopyWithCapacity(int var1);
    }

    public interface IntList extends Internal.ProtobufList<Integer> {

        int getInt(int var1);

        void addInt(int var1);

        int setInt(int var1, int var2);

        Internal.IntList mutableCopyWithCapacity(int var1);
    }

    public static class ListAdapter<F, T> extends AbstractList<T> {

        private final List<F> fromList;

        private final Internal.ListAdapter.Converter<F, T> converter;

        public ListAdapter(List<F> fromList, Internal.ListAdapter.Converter<F, T> converter) {
            this.fromList = fromList;
            this.converter = converter;
        }

        public T get(int index) {
            return this.converter.convert((F) this.fromList.get(index));
        }

        public int size() {
            return this.fromList.size();
        }

        public interface Converter<F, T> {

            T convert(F var1);
        }
    }

    public interface LongList extends Internal.ProtobufList<Long> {

        long getLong(int var1);

        void addLong(long var1);

        long setLong(int var1, long var2);

        Internal.LongList mutableCopyWithCapacity(int var1);
    }

    public static class MapAdapter<K, V, RealValue> extends AbstractMap<K, V> {

        private final Map<K, RealValue> realMap;

        private final Internal.MapAdapter.Converter<RealValue, V> valueConverter;

        public static <T extends Internal.EnumLite> Internal.MapAdapter.Converter<Integer, T> newEnumConverter(final Internal.EnumLiteMap<T> enumMap, final T unrecognizedValue) {
            return new Internal.MapAdapter.Converter<Integer, T>() {

                public T doForward(Integer value) {
                    T result = enumMap.findValueByNumber(value);
                    return result == null ? unrecognizedValue : result;
                }

                public Integer doBackward(T value) {
                    return value.getNumber();
                }
            };
        }

        public MapAdapter(Map<K, RealValue> realMap, Internal.MapAdapter.Converter<RealValue, V> valueConverter) {
            this.realMap = realMap;
            this.valueConverter = valueConverter;
        }

        public V get(Object key) {
            RealValue result = (RealValue) this.realMap.get(key);
            return result == null ? null : this.valueConverter.doForward(result);
        }

        public V put(K key, V value) {
            RealValue oldValue = (RealValue) this.realMap.put(key, this.valueConverter.doBackward(value));
            return oldValue == null ? null : this.valueConverter.doForward(oldValue);
        }

        public Set<Entry<K, V>> entrySet() {
            return new Internal.MapAdapter.SetAdapter(this.realMap.entrySet());
        }

        public interface Converter<A, B> {

            B doForward(A var1);

            A doBackward(B var1);
        }

        private class EntryAdapter implements Entry<K, V> {

            private final Entry<K, RealValue> realEntry;

            public EntryAdapter(Entry<K, RealValue> realEntry) {
                this.realEntry = realEntry;
            }

            public K getKey() {
                return (K) this.realEntry.getKey();
            }

            public V getValue() {
                return MapAdapter.this.valueConverter.doForward((RealValue) this.realEntry.getValue());
            }

            public V setValue(V value) {
                RealValue oldValue = (RealValue) this.realEntry.setValue(MapAdapter.this.valueConverter.doBackward(value));
                return oldValue == null ? null : MapAdapter.this.valueConverter.doForward(oldValue);
            }

            public boolean equals(Object o) {
                if (o == this) {
                    return true;
                } else if (!(o instanceof Entry)) {
                    return false;
                } else {
                    Entry<?, ?> other = (Entry<?, ?>) o;
                    return this.getKey().equals(other.getKey()) && this.getValue().equals(this.getValue());
                }
            }

            public int hashCode() {
                return this.realEntry.hashCode();
            }
        }

        private class IteratorAdapter implements Iterator<Entry<K, V>> {

            private final Iterator<Entry<K, RealValue>> realIterator;

            public IteratorAdapter(Iterator<Entry<K, RealValue>> realIterator) {
                this.realIterator = realIterator;
            }

            public boolean hasNext() {
                return this.realIterator.hasNext();
            }

            public Entry<K, V> next() {
                return MapAdapter.this.new EntryAdapter((Entry<K, RealValue>) this.realIterator.next());
            }

            public void remove() {
                this.realIterator.remove();
            }
        }

        private class SetAdapter extends AbstractSet<Entry<K, V>> {

            private final Set<Entry<K, RealValue>> realSet;

            public SetAdapter(Set<Entry<K, RealValue>> realSet) {
                this.realSet = realSet;
            }

            public Iterator<Entry<K, V>> iterator() {
                return MapAdapter.this.new IteratorAdapter(this.realSet.iterator());
            }

            public int size() {
                return this.realSet.size();
            }
        }
    }

    public interface ProtobufList<E> extends List<E>, RandomAccess {

        void makeImmutable();

        boolean isModifiable();

        Internal.ProtobufList<E> mutableCopyWithCapacity(int var1);
    }
}