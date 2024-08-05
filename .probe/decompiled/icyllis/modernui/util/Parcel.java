package icyllis.modernui.util;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.text.TextUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.lwjgl.system.MemoryUtil;

public class Parcel {

    private static final byte VAL_NULL = 0;

    private static final byte VAL_BYTE = 1;

    private static final byte VAL_SHORT = 2;

    private static final byte VAL_INT = 3;

    private static final byte VAL_LONG = 4;

    private static final byte VAL_FLOAT = 5;

    private static final byte VAL_DOUBLE = 6;

    private static final byte VAL_BOOLEAN = 7;

    private static final byte VAL_CHAR = 8;

    private static final byte VAL_BYTE_ARRAY = 9;

    private static final byte VAL_SHORT_ARRAY = 10;

    private static final byte VAL_INT_ARRAY = 11;

    private static final byte VAL_LONG_ARRAY = 12;

    private static final byte VAL_FLOAT_ARRAY = 13;

    private static final byte VAL_DOUBLE_ARRAY = 14;

    private static final byte VAL_BOOLEAN_ARRAY = 15;

    private static final byte VAL_CHAR_ARRAY = 16;

    private static final byte VAL_STRING = 17;

    private static final byte VAL_UUID = 19;

    private static final byte VAL_INSTANT = 20;

    private static final byte VAL_DATA_SET = 64;

    private static final byte VAL_PARCELABLE = 65;

    private static final byte VAL_CHAR_SEQUENCE = 66;

    private static final byte VAL_LIST = 68;

    private static final byte VAL_OBJECT_ARRAY = 118;

    private static final byte VAL_SERIALIZABLE = 127;

    private static final ConcurrentHashMap<ClassLoader, ConcurrentHashMap<String, Parcelable.Creator<?>>> gCreators = new ConcurrentHashMap();

    protected ByteBuffer mNativeBuffer;

    @NonNull
    private static DataSet inflate(@NonNull InputStream stream, @Nullable ClassLoader loader) throws IOException {
        try {
            DataSet var9;
            try (IOStreamParcel in = new IOStreamParcel(new GZIPInputStream(new BufferedInputStream(stream, 4096)), null)) {
                DataSet res = in.readDataSet(loader);
                if (res == null) {
                    throw new IOException("Insufficient data");
                }
                var9 = res;
            }
            return var9;
        } catch (RuntimeException var7) {
            if (var7.getCause() instanceof IOException ioe) {
                throw ioe;
            } else {
                throw var7;
            }
        }
    }

    private static void deflate(@NonNull OutputStream stream, @NonNull DataSet source) throws IOException {
        try {
            try (IOStreamParcel out = new IOStreamParcel(null, new GZIPOutputStream(new BufferedOutputStream(stream, 4096)))) {
                out.writeDataSet(source);
            }
        } catch (RuntimeException var7) {
            if (var7.getCause() instanceof IOException ioe) {
                throw ioe;
            } else {
                throw var7;
            }
        }
    }

    protected void ensureCapacity(int len) {
        if (this.mNativeBuffer == null || this.mNativeBuffer.remaining() < len) {
            long size = (long) ((this.mNativeBuffer == null ? 0 : this.mNativeBuffer.limit()) + len);
            size += size >> 1;
            if (size > 2147483647L) {
                throw new BufferOverflowException();
            } else {
                this.setCapacity((int) Math.max(size, 128L));
            }
        }
    }

    protected void setCapacity(int size) {
        if (this.mNativeBuffer != null && this.mNativeBuffer.capacity() >= size) {
            this.mNativeBuffer.limit(size);
        } else if (this.mNativeBuffer == null) {
            this.mNativeBuffer = MemoryUtil.memAlloc(size);
        } else {
            this.mNativeBuffer = MemoryUtil.memRealloc(this.mNativeBuffer, size);
        }
    }

    public int position() {
        return this.mNativeBuffer == null ? 0 : this.mNativeBuffer.position();
    }

    public void position(int newPosition) {
        this.ensureCapacity(0);
        this.mNativeBuffer.position(newPosition);
    }

    public int limit() {
        return this.mNativeBuffer == null ? 0 : this.mNativeBuffer.limit();
    }

    public void limit(int newLimit) {
        this.ensureCapacity(0);
        this.mNativeBuffer.limit(newLimit);
    }

    public int capacity() {
        return this.mNativeBuffer == null ? 0 : this.mNativeBuffer.capacity();
    }

    public void writeBytes(byte[] src) {
        this.writeBytes(src, 0, src.length);
    }

    public void writeBytes(byte[] src, int off, int len) {
        this.ensureCapacity(len);
        this.mNativeBuffer.put(src, off, len);
    }

    public void writeBoolean(boolean b) {
        this.writeByte(b ? 1 : 0);
    }

    public void writeChar(int v) {
        this.writeShort(v);
    }

    public void writeByte(int v) {
        this.ensureCapacity(1);
        this.mNativeBuffer.put((byte) v);
    }

    public void writeShort(int v) {
        this.ensureCapacity(2);
        this.mNativeBuffer.putShort((short) v);
    }

    public void writeInt(int v) {
        this.ensureCapacity(4);
        this.mNativeBuffer.putInt(v);
    }

    public void writeLong(long v) {
        this.ensureCapacity(8);
        this.mNativeBuffer.putLong(v);
    }

    public void writeFloat(float v) {
        this.writeInt(Float.floatToRawIntBits(v));
    }

    public void writeDouble(double v) {
        this.writeLong(Double.doubleToRawLongBits(v));
    }

    public void readBytes(byte[] dst) {
        this.readBytes(dst, 0, dst.length);
    }

    public void readBytes(byte[] dst, int off, int len) {
        if (this.mNativeBuffer == null) {
            throw new BufferUnderflowException();
        } else {
            this.mNativeBuffer.get(dst, off, len);
        }
    }

    public boolean readBoolean() {
        return this.readByte() != 0;
    }

    public char readChar() {
        return (char) this.readShort();
    }

    public byte readByte() {
        if (this.mNativeBuffer == null) {
            throw new BufferUnderflowException();
        } else {
            return this.mNativeBuffer.get();
        }
    }

    public short readShort() {
        if (this.mNativeBuffer == null) {
            throw new BufferUnderflowException();
        } else {
            return this.mNativeBuffer.getShort();
        }
    }

    public int readInt() {
        if (this.mNativeBuffer == null) {
            throw new BufferUnderflowException();
        } else {
            return this.mNativeBuffer.getInt();
        }
    }

    public long readLong() {
        if (this.mNativeBuffer == null) {
            throw new BufferUnderflowException();
        } else {
            return this.mNativeBuffer.getLong();
        }
    }

    public float readFloat() {
        return Float.intBitsToFloat(this.readInt());
    }

    public double readDouble() {
        return Double.longBitsToDouble(this.readLong());
    }

    public void writeValue(@Nullable Object v) {
        if (v == null) {
            this.writeByte(0);
        } else if (v instanceof String) {
            this.writeByte(17);
            this.writeString((String) v);
        } else if (v instanceof Integer) {
            this.writeByte(3);
            this.writeInt((Integer) v);
        } else if (v instanceof Long) {
            this.writeByte(4);
            this.writeLong((Long) v);
        } else if (v instanceof Float) {
            this.writeByte(5);
            this.writeFloat((Float) v);
        } else if (v instanceof Double) {
            this.writeByte(6);
            this.writeDouble((Double) v);
        } else if (v instanceof Byte) {
            this.writeByte(1);
            this.writeByte((Byte) v);
        } else if (v instanceof Short) {
            this.writeByte(2);
            this.writeShort((Short) v);
        } else if (v instanceof Character) {
            this.writeByte(8);
            this.writeChar((Character) v);
        } else if (v instanceof Boolean) {
            this.writeByte(7);
            this.writeBoolean((Boolean) v);
        } else if (v instanceof UUID) {
            this.writeByte(19);
            this.writeUUID((UUID) v);
        } else if (v instanceof Instant) {
            this.writeByte(20);
            this.writeInstant((Instant) v);
        } else if (v instanceof int[]) {
            this.writeByte(11);
            this.writeIntArray((int[]) v);
        } else if (v instanceof byte[]) {
            this.writeByte(9);
            this.writeByteArray((byte[]) v);
        } else if (v instanceof char[]) {
            this.writeByte(16);
            this.writeCharArray((char[]) v);
        } else if (v instanceof DataSet) {
            this.writeByte(64);
            this.writeDataSet((DataSet) v);
        } else if (v instanceof Parcelable) {
            this.writeByte(65);
            this.writeParcelable((Parcelable) v, 0);
        } else if (v instanceof CharSequence) {
            this.writeByte(66);
            this.writeCharSequence((CharSequence) v);
        } else if (v instanceof List) {
            this.writeByte(68);
            this.writeList((List<?>) v);
        } else if (v instanceof long[]) {
            this.writeByte(12);
            this.writeLongArray((long[]) v);
        } else if (v instanceof short[]) {
            this.writeByte(10);
            this.writeShortArray((short[]) v);
        } else if (v instanceof float[]) {
            this.writeByte(13);
            this.writeFloatArray((float[]) v);
        } else if (v instanceof double[]) {
            this.writeByte(14);
            this.writeDoubleArray((double[]) v);
        } else if (v instanceof boolean[]) {
            this.writeByte(15);
            this.writeBooleanArray((boolean[]) v);
        } else {
            Class<?> clazz = v.getClass();
            if (clazz.isArray() && clazz.getComponentType() == Object.class) {
                this.writeByte(118);
                this.writeArray((Object[]) v);
            } else if (v instanceof Serializable value) {
                this.writeByte(127);
                String name = value.getClass().getName();
                this.writeString(name);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(value);
                    oos.close();
                    this.writeByteArray(baos.toByteArray());
                } catch (IOException var7) {
                    throw new BadParcelableException("Parcelable encountered IOException writing serializable object (name = " + name + ")", var7);
                }
            }
        }
    }

    @Nullable
    public <T> T readValue(@Nullable ClassLoader loader, @Nullable Class<T> clazz, @Nullable Class<?> elemType) {
        byte type = this.readByte();
        Object object = switch(type) {
            case 0 ->
                null;
            case 1 ->
                this.readByte();
            case 2 ->
                this.readShort();
            case 3 ->
                this.readInt();
            case 4 ->
                this.readLong();
            case 5 ->
                this.readFloat();
            case 6 ->
                this.readDouble();
            case 7 ->
                this.readBoolean();
            case 8 ->
                this.readChar();
            case 9 ->
                this.readByteArray();
            case 10 ->
                this.readShortArray();
            case 11 ->
                this.readIntArray();
            case 12 ->
                this.readLongArray();
            case 13 ->
                this.readFloatArray();
            case 14 ->
                this.readDoubleArray();
            case 15 ->
                this.readBooleanArray();
            case 16 ->
                this.readCharArray();
            case 17 ->
                this.readString();
            case 19 ->
                this.readUUID();
            case 20 ->
                this.readInstant();
            case 64 ->
                this.readDataSet(loader);
            case 65 ->
                this.readParcelable0(loader, clazz);
            case 66 ->
                this.readCharSequence();
            case 68 ->
                this.readList(loader, (Class<? extends T>) elemType);
            case 118 ->
                {
                }
            default ->
                throw new BadParcelableException("Unknown value type identifier: " + type);
        };
        if (object != null && clazz != null && !clazz.isInstance(object)) {
            throw new BadParcelableException("Deserialized object " + object + " is not an instance of required class " + clazz.getName() + " provided in the parameter");
        } else {
            return (T) object;
        }
    }

    public final void writeParcelable(@Nullable Parcelable p, int parcelableFlags) {
        if (p == null) {
            this.writeString(null);
        } else {
            this.writeParcelableCreator(p);
            p.writeToParcel(this, parcelableFlags);
        }
    }

    public final void writeParcelableCreator(@NonNull Parcelable p) {
        String name = p.getClass().getName();
        this.writeString(name);
    }

    @Nullable
    public <T> T readParcelable(@Nullable ClassLoader loader, @NonNull Class<T> clazz) {
        return this.readParcelable0(loader, (Class<T>) Objects.requireNonNull(clazz));
    }

    @Nullable
    public <T> T readParcelable0(@Nullable ClassLoader loader, @Nullable Class<T> clazz) {
        Parcelable.Creator<?> creator = this.readParcelableCreator0(loader, clazz);
        if (creator == null) {
            return null;
        } else {
            return (T) (creator instanceof Parcelable.ClassLoaderCreator ? ((Parcelable.ClassLoaderCreator) creator).createFromParcel(this, loader) : creator.createFromParcel(this));
        }
    }

    @Nullable
    public <T> Parcelable.Creator<T> readParcelableCreator(@Nullable ClassLoader loader, @NonNull Class<T> clazz) {
        return this.readParcelableCreator0(loader, (Class<T>) Objects.requireNonNull(clazz));
    }

    @Nullable
    private <T> Parcelable.Creator<T> readParcelableCreator0(@Nullable ClassLoader loader, @Nullable Class<T> clazz) {
        String name = this.readString();
        if (name == null) {
            return null;
        } else {
            ConcurrentHashMap<String, Parcelable.Creator<?>> map = (ConcurrentHashMap<String, Parcelable.Creator<?>>) gCreators.computeIfAbsent(loader, __ -> new ConcurrentHashMap());
            Parcelable.Creator<?> creator = (Parcelable.Creator<?>) map.get(name);
            if (creator != null) {
                if (clazz != null) {
                    Class<?> target = creator.getClass().getEnclosingClass();
                    if (!clazz.isAssignableFrom(target)) {
                        throw new BadParcelableException("Parcelable creator " + name + " is not a subclass of required class " + clazz.getName() + " provided in the parameter");
                    }
                }
                return (Parcelable.Creator<T>) creator;
            } else {
                try {
                    Class<?> target = (loader == null ? Parcel.class.getClassLoader() : loader).loadClass(name);
                    if (!Parcelable.class.isAssignableFrom(target)) {
                        throw new BadParcelableException("Parcelable protocol requires subclassing from Parcelable on class " + name);
                    }
                    if (clazz != null && !clazz.isAssignableFrom(target)) {
                        throw new BadParcelableException("Parcelable creator " + name + " is not a subclass of required class " + clazz.getName() + " provided in the parameter");
                    }
                    Field f = target.getField("CREATOR");
                    if ((f.getModifiers() & 8) == 0) {
                        throw new BadParcelableException("Parcelable protocol requires the CREATOR object to be static on class " + name);
                    }
                    if (!Parcelable.Creator.class.isAssignableFrom(f.getType())) {
                        throw new BadParcelableException("Parcelable protocol requires a Parcelable.Creator object called CREATOR on class " + name);
                    }
                    creator = (Parcelable.Creator<?>) f.get(null);
                } catch (NoSuchFieldException var8) {
                    throw new RuntimeException("Parcelable protocol requires a Parcelable.Creator object called CREATOR on class " + name, var8);
                } catch (IllegalAccessException | ClassNotFoundException var9) {
                    throw new RuntimeException(var9);
                }
                if (creator == null) {
                    throw new BadParcelableException("Parcelable protocol requires a non-null Parcelable.Creator object called CREATOR on class " + name);
                } else {
                    map.put(name, creator);
                    return (Parcelable.Creator<T>) creator;
                }
            }
        }
    }

    public void writeByteArray(@Nullable byte[] b) {
        if (b == null) {
            this.writeInt(-1);
        } else {
            this.writeInt(b.length);
            this.writeBytes(b);
        }
    }

    public void writeByteArray(@Nullable byte[] b, int off, int len) {
        if (b == null) {
            this.writeInt(-1);
        } else {
            this.writeInt(len);
            this.writeBytes(b, off, len);
        }
    }

    @Nullable
    public byte[] readByteArray() {
        int n = this.readInt();
        if (n < 0) {
            return null;
        } else {
            byte[] b = new byte[n];
            this.readBytes(b, 0, n);
            return b;
        }
    }

    public void writeShortArray(@Nullable short[] value) {
        if (value == null) {
            this.writeInt(-1);
        } else {
            this.writeInt(value.length);
            for (short e : value) {
                this.writeShort(e);
            }
        }
    }

    @Nullable
    public short[] readShortArray() {
        int n = this.readInt();
        if (n < 0) {
            return null;
        } else {
            short[] value = new short[n];
            for (int i = 0; i < n; i++) {
                value[i] = this.readShort();
            }
            return value;
        }
    }

    public void writeIntArray(@Nullable int[] value) {
        if (value == null) {
            this.writeInt(-1);
        } else {
            this.writeInt(value.length);
            for (int e : value) {
                this.writeInt(e);
            }
        }
    }

    @Nullable
    public int[] readIntArray() {
        int n = this.readInt();
        if (n < 0) {
            return null;
        } else {
            int[] value = new int[n];
            for (int i = 0; i < n; i++) {
                value[i] = this.readInt();
            }
            return value;
        }
    }

    public void writeLongArray(@Nullable long[] value) {
        if (value == null) {
            this.writeInt(-1);
        } else {
            this.writeInt(value.length);
            for (long e : value) {
                this.writeLong(e);
            }
        }
    }

    @Nullable
    public long[] readLongArray() {
        int n = this.readInt();
        if (n < 0) {
            return null;
        } else {
            long[] value = new long[n];
            for (int i = 0; i < n; i++) {
                value[i] = this.readLong();
            }
            return value;
        }
    }

    public void writeFloatArray(@Nullable float[] value) {
        if (value == null) {
            this.writeInt(-1);
        } else {
            this.writeInt(value.length);
            for (float e : value) {
                this.writeFloat(e);
            }
        }
    }

    @Nullable
    public float[] readFloatArray() {
        int n = this.readInt();
        if (n < 0) {
            return null;
        } else {
            float[] value = new float[n];
            for (int i = 0; i < n; i++) {
                value[i] = this.readFloat();
            }
            return value;
        }
    }

    public void writeDoubleArray(@Nullable double[] value) {
        if (value == null) {
            this.writeInt(-1);
        } else {
            this.writeInt(value.length);
            for (double e : value) {
                this.writeDouble(e);
            }
        }
    }

    @Nullable
    public double[] readDoubleArray() {
        int n = this.readInt();
        if (n < 0) {
            return null;
        } else {
            double[] value = new double[n];
            for (int i = 0; i < n; i++) {
                value[i] = this.readDouble();
            }
            return value;
        }
    }

    public void writeBooleanArray(@Nullable boolean[] value) {
        if (value == null) {
            this.writeInt(-1);
        } else {
            this.writeInt(value.length);
            for (boolean e : value) {
                this.writeBoolean(e);
            }
        }
    }

    @Nullable
    public boolean[] readBooleanArray() {
        int n = this.readInt();
        if (n < 0) {
            return null;
        } else {
            boolean[] value = new boolean[n];
            for (int i = 0; i < n; i++) {
                value[i] = this.readBoolean();
            }
            return value;
        }
    }

    public void writeCharArray(@Nullable char[] value) {
        if (value == null) {
            this.writeInt(-1);
        } else {
            this.writeInt(value.length);
            for (char e : value) {
                this.writeChar(e);
            }
        }
    }

    @Nullable
    public char[] readCharArray() {
        int n = this.readInt();
        if (n < 0) {
            return null;
        } else {
            char[] value = new char[n];
            for (int i = 0; i < n; i++) {
                value[i] = this.readChar();
            }
            return value;
        }
    }

    public void writeArray(@Nullable Object[] a) {
        if (a == null) {
            this.writeInt(-1);
        } else {
            this.writeInt(a.length);
            for (Object e : a) {
                this.writeValue(e);
            }
        }
    }

    @Nullable
    public <T> T[] readArray(@Nullable ClassLoader loader, @NonNull Class<T> clazz) {
        int n = this.readInt();
        if (n < 0) {
            return null;
        } else {
            T[] a = (T[]) (clazz == Object.class ? new Object[n] : Array.newInstance(clazz, n));
            for (int i = 0; i < n; i++) {
                T value = this.readValue(loader, clazz, null);
                a[i] = value;
            }
            return a;
        }
    }

    public void writeString(@Nullable String s) {
        this.writeString16(s);
    }

    public void writeString8(@Nullable String s) {
        if (s == null) {
            this.writeInt(-1);
        } else {
            byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
            this.writeInt(bytes.length);
            this.writeBytes(bytes);
        }
    }

    public void writeString16(@Nullable String s) {
        if (s == null) {
            this.writeInt(-1);
        } else {
            int len = s.length();
            this.writeInt(len);
            for (int i = 0; i < len; i++) {
                this.writeChar(s.charAt(i));
            }
        }
    }

    @Nullable
    public String readString() {
        return this.readString16();
    }

    @Nullable
    public String readString8() {
        int n = this.readInt();
        if (n < 0) {
            return null;
        } else {
            byte[] bytes = new byte[n];
            this.readBytes(bytes, 0, n);
            return new String(bytes, StandardCharsets.UTF_8);
        }
    }

    @Nullable
    public String readString16() {
        int n = this.readInt();
        if (n < 0) {
            return null;
        } else {
            char[] value = new char[n];
            for (int i = 0; i < n; i++) {
                value[i] = this.readChar();
            }
            return new String(value);
        }
    }

    public void writeCharSequence(@Nullable CharSequence cs) {
        TextUtils.writeToParcel(cs, this, 0);
    }

    @Nullable
    public CharSequence readCharSequence() {
        return TextUtils.createFromParcel(this);
    }

    public void writeList(@Nullable List<?> list) {
        if (list == null) {
            this.writeInt(-1);
        } else {
            this.writeInt(list.size());
            for (Object e : list) {
                this.writeValue(e);
            }
        }
    }

    @Nullable
    private <T> List<T> readList(@Nullable ClassLoader loader, @Nullable Class<? extends T> clazz) {
        int n = this.readInt();
        if (n < 0) {
            return null;
        } else {
            ArrayList<T> res = new ArrayList(n);
            while (n-- != 0) {
                res.add(this.readValue(loader, (Class<T>) clazz, null));
            }
            return res;
        }
    }

    public void writeDataSet(@Nullable DataSet source) {
        if (source == null) {
            this.writeInt(-1);
        } else {
            this.writeInt(source.size());
            DataSet.FastEntryIterator it = source.new FastEntryIterator();
            while (it.hasNext()) {
                Entry<String, Object> e = it.next();
                this.writeString((String) e.getKey());
                this.writeValue(e.getValue());
            }
        }
    }

    @Nullable
    public DataSet readDataSet(@Nullable ClassLoader loader) {
        int n = this.readInt();
        if (n < 0) {
            return null;
        } else {
            DataSet res = new DataSet(n);
            while (n-- != 0) {
                res.put(this.readString(), this.readValue(loader, null, null));
            }
            return res;
        }
    }

    public void writeUUID(@NonNull UUID value) {
        this.writeLong(value.getMostSignificantBits());
        this.writeLong(value.getLeastSignificantBits());
    }

    @NonNull
    public UUID readUUID() {
        return new UUID(this.readLong(), this.readLong());
    }

    public void writeInstant(@NonNull Instant value) {
        this.writeLong(value.getEpochSecond());
        this.writeInt(value.getNano());
    }

    @NonNull
    public Instant readInstant() {
        return Instant.ofEpochSecond(this.readLong(), (long) this.readInt());
    }

    @Internal
    public void freeData() {
        if (this.mNativeBuffer != null) {
            MemoryUtil.memFree(this.mNativeBuffer);
            this.mNativeBuffer = null;
        }
    }
}