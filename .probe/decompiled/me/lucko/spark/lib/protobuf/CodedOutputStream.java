package me.lucko.spark.lib.protobuf;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class CodedOutputStream extends ByteOutput {

    private static final Logger logger = Logger.getLogger(CodedOutputStream.class.getName());

    private static final boolean HAS_UNSAFE_ARRAY_OPERATIONS = UnsafeUtil.hasUnsafeArrayOperations();

    CodedOutputStreamWriter wrapper;

    @Deprecated
    public static final int LITTLE_ENDIAN_32_SIZE = 4;

    public static final int DEFAULT_BUFFER_SIZE = 4096;

    private boolean serializationDeterministic;

    static int computePreferredBufferSize(int dataLength) {
        return dataLength > 4096 ? 4096 : dataLength;
    }

    public static CodedOutputStream newInstance(OutputStream output) {
        return newInstance(output, 4096);
    }

    public static CodedOutputStream newInstance(OutputStream output, int bufferSize) {
        return new CodedOutputStream.OutputStreamEncoder(output, bufferSize);
    }

    public static CodedOutputStream newInstance(byte[] flatArray) {
        return newInstance(flatArray, 0, flatArray.length);
    }

    public static CodedOutputStream newInstance(byte[] flatArray, int offset, int length) {
        return new CodedOutputStream.ArrayEncoder(flatArray, offset, length);
    }

    public static CodedOutputStream newInstance(ByteBuffer buffer) {
        if (buffer.hasArray()) {
            return new CodedOutputStream.HeapNioEncoder(buffer);
        } else if (buffer.isDirect() && !buffer.isReadOnly()) {
            return CodedOutputStream.UnsafeDirectNioEncoder.isSupported() ? newUnsafeInstance(buffer) : newSafeInstance(buffer);
        } else {
            throw new IllegalArgumentException("ByteBuffer is read-only");
        }
    }

    static CodedOutputStream newUnsafeInstance(ByteBuffer buffer) {
        return new CodedOutputStream.UnsafeDirectNioEncoder(buffer);
    }

    static CodedOutputStream newSafeInstance(ByteBuffer buffer) {
        return new CodedOutputStream.SafeDirectNioEncoder(buffer);
    }

    public void useDeterministicSerialization() {
        this.serializationDeterministic = true;
    }

    boolean isSerializationDeterministic() {
        return this.serializationDeterministic;
    }

    @Deprecated
    public static CodedOutputStream newInstance(ByteBuffer byteBuffer, int unused) {
        return newInstance(byteBuffer);
    }

    static CodedOutputStream newInstance(ByteOutput byteOutput, int bufferSize) {
        if (bufferSize < 0) {
            throw new IllegalArgumentException("bufferSize must be positive");
        } else {
            return new CodedOutputStream.ByteOutputEncoder(byteOutput, bufferSize);
        }
    }

    private CodedOutputStream() {
    }

    public abstract void writeTag(int var1, int var2) throws IOException;

    public abstract void writeInt32(int var1, int var2) throws IOException;

    public abstract void writeUInt32(int var1, int var2) throws IOException;

    public final void writeSInt32(int fieldNumber, int value) throws IOException {
        this.writeUInt32(fieldNumber, encodeZigZag32(value));
    }

    public abstract void writeFixed32(int var1, int var2) throws IOException;

    public final void writeSFixed32(int fieldNumber, int value) throws IOException {
        this.writeFixed32(fieldNumber, value);
    }

    public final void writeInt64(int fieldNumber, long value) throws IOException {
        this.writeUInt64(fieldNumber, value);
    }

    public abstract void writeUInt64(int var1, long var2) throws IOException;

    public final void writeSInt64(int fieldNumber, long value) throws IOException {
        this.writeUInt64(fieldNumber, encodeZigZag64(value));
    }

    public abstract void writeFixed64(int var1, long var2) throws IOException;

    public final void writeSFixed64(int fieldNumber, long value) throws IOException {
        this.writeFixed64(fieldNumber, value);
    }

    public final void writeFloat(int fieldNumber, float value) throws IOException {
        this.writeFixed32(fieldNumber, Float.floatToRawIntBits(value));
    }

    public final void writeDouble(int fieldNumber, double value) throws IOException {
        this.writeFixed64(fieldNumber, Double.doubleToRawLongBits(value));
    }

    public abstract void writeBool(int var1, boolean var2) throws IOException;

    public final void writeEnum(int fieldNumber, int value) throws IOException {
        this.writeInt32(fieldNumber, value);
    }

    public abstract void writeString(int var1, String var2) throws IOException;

    public abstract void writeBytes(int var1, ByteString var2) throws IOException;

    public abstract void writeByteArray(int var1, byte[] var2) throws IOException;

    public abstract void writeByteArray(int var1, byte[] var2, int var3, int var4) throws IOException;

    public abstract void writeByteBuffer(int var1, ByteBuffer var2) throws IOException;

    public final void writeRawByte(byte value) throws IOException {
        this.write(value);
    }

    public final void writeRawByte(int value) throws IOException {
        this.write((byte) value);
    }

    public final void writeRawBytes(byte[] value) throws IOException {
        this.write(value, 0, value.length);
    }

    public final void writeRawBytes(byte[] value, int offset, int length) throws IOException {
        this.write(value, offset, length);
    }

    public final void writeRawBytes(ByteString value) throws IOException {
        value.writeTo(this);
    }

    public abstract void writeRawBytes(ByteBuffer var1) throws IOException;

    public abstract void writeMessage(int var1, MessageLite var2) throws IOException;

    abstract void writeMessage(int var1, MessageLite var2, Schema var3) throws IOException;

    public abstract void writeMessageSetExtension(int var1, MessageLite var2) throws IOException;

    public abstract void writeRawMessageSetExtension(int var1, ByteString var2) throws IOException;

    public abstract void writeInt32NoTag(int var1) throws IOException;

    public abstract void writeUInt32NoTag(int var1) throws IOException;

    public final void writeSInt32NoTag(int value) throws IOException {
        this.writeUInt32NoTag(encodeZigZag32(value));
    }

    public abstract void writeFixed32NoTag(int var1) throws IOException;

    public final void writeSFixed32NoTag(int value) throws IOException {
        this.writeFixed32NoTag(value);
    }

    public final void writeInt64NoTag(long value) throws IOException {
        this.writeUInt64NoTag(value);
    }

    public abstract void writeUInt64NoTag(long var1) throws IOException;

    public final void writeSInt64NoTag(long value) throws IOException {
        this.writeUInt64NoTag(encodeZigZag64(value));
    }

    public abstract void writeFixed64NoTag(long var1) throws IOException;

    public final void writeSFixed64NoTag(long value) throws IOException {
        this.writeFixed64NoTag(value);
    }

    public final void writeFloatNoTag(float value) throws IOException {
        this.writeFixed32NoTag(Float.floatToRawIntBits(value));
    }

    public final void writeDoubleNoTag(double value) throws IOException {
        this.writeFixed64NoTag(Double.doubleToRawLongBits(value));
    }

    public final void writeBoolNoTag(boolean value) throws IOException {
        this.write((byte) (value ? 1 : 0));
    }

    public final void writeEnumNoTag(int value) throws IOException {
        this.writeInt32NoTag(value);
    }

    public abstract void writeStringNoTag(String var1) throws IOException;

    public abstract void writeBytesNoTag(ByteString var1) throws IOException;

    public final void writeByteArrayNoTag(byte[] value) throws IOException {
        this.writeByteArrayNoTag(value, 0, value.length);
    }

    public abstract void writeMessageNoTag(MessageLite var1) throws IOException;

    abstract void writeMessageNoTag(MessageLite var1, Schema var2) throws IOException;

    @Override
    public abstract void write(byte var1) throws IOException;

    @Override
    public abstract void write(byte[] var1, int var2, int var3) throws IOException;

    @Override
    public abstract void writeLazy(byte[] var1, int var2, int var3) throws IOException;

    @Override
    public abstract void write(ByteBuffer var1) throws IOException;

    @Override
    public abstract void writeLazy(ByteBuffer var1) throws IOException;

    public static int computeInt32Size(int fieldNumber, int value) {
        return computeTagSize(fieldNumber) + computeInt32SizeNoTag(value);
    }

    public static int computeUInt32Size(int fieldNumber, int value) {
        return computeTagSize(fieldNumber) + computeUInt32SizeNoTag(value);
    }

    public static int computeSInt32Size(int fieldNumber, int value) {
        return computeTagSize(fieldNumber) + computeSInt32SizeNoTag(value);
    }

    public static int computeFixed32Size(int fieldNumber, int value) {
        return computeTagSize(fieldNumber) + computeFixed32SizeNoTag(value);
    }

    public static int computeSFixed32Size(int fieldNumber, int value) {
        return computeTagSize(fieldNumber) + computeSFixed32SizeNoTag(value);
    }

    public static int computeInt64Size(int fieldNumber, long value) {
        return computeTagSize(fieldNumber) + computeInt64SizeNoTag(value);
    }

    public static int computeUInt64Size(int fieldNumber, long value) {
        return computeTagSize(fieldNumber) + computeUInt64SizeNoTag(value);
    }

    public static int computeSInt64Size(int fieldNumber, long value) {
        return computeTagSize(fieldNumber) + computeSInt64SizeNoTag(value);
    }

    public static int computeFixed64Size(int fieldNumber, long value) {
        return computeTagSize(fieldNumber) + computeFixed64SizeNoTag(value);
    }

    public static int computeSFixed64Size(int fieldNumber, long value) {
        return computeTagSize(fieldNumber) + computeSFixed64SizeNoTag(value);
    }

    public static int computeFloatSize(int fieldNumber, float value) {
        return computeTagSize(fieldNumber) + computeFloatSizeNoTag(value);
    }

    public static int computeDoubleSize(int fieldNumber, double value) {
        return computeTagSize(fieldNumber) + computeDoubleSizeNoTag(value);
    }

    public static int computeBoolSize(int fieldNumber, boolean value) {
        return computeTagSize(fieldNumber) + computeBoolSizeNoTag(value);
    }

    public static int computeEnumSize(int fieldNumber, int value) {
        return computeTagSize(fieldNumber) + computeEnumSizeNoTag(value);
    }

    public static int computeStringSize(int fieldNumber, String value) {
        return computeTagSize(fieldNumber) + computeStringSizeNoTag(value);
    }

    public static int computeBytesSize(int fieldNumber, ByteString value) {
        return computeTagSize(fieldNumber) + computeBytesSizeNoTag(value);
    }

    public static int computeByteArraySize(int fieldNumber, byte[] value) {
        return computeTagSize(fieldNumber) + computeByteArraySizeNoTag(value);
    }

    public static int computeByteBufferSize(int fieldNumber, ByteBuffer value) {
        return computeTagSize(fieldNumber) + computeByteBufferSizeNoTag(value);
    }

    public static int computeLazyFieldSize(int fieldNumber, LazyFieldLite value) {
        return computeTagSize(fieldNumber) + computeLazyFieldSizeNoTag(value);
    }

    public static int computeMessageSize(int fieldNumber, MessageLite value) {
        return computeTagSize(fieldNumber) + computeMessageSizeNoTag(value);
    }

    static int computeMessageSize(int fieldNumber, MessageLite value, Schema schema) {
        return computeTagSize(fieldNumber) + computeMessageSizeNoTag(value, schema);
    }

    public static int computeMessageSetExtensionSize(int fieldNumber, MessageLite value) {
        return computeTagSize(1) * 2 + computeUInt32Size(2, fieldNumber) + computeMessageSize(3, value);
    }

    public static int computeRawMessageSetExtensionSize(int fieldNumber, ByteString value) {
        return computeTagSize(1) * 2 + computeUInt32Size(2, fieldNumber) + computeBytesSize(3, value);
    }

    public static int computeLazyFieldMessageSetExtensionSize(int fieldNumber, LazyFieldLite value) {
        return computeTagSize(1) * 2 + computeUInt32Size(2, fieldNumber) + computeLazyFieldSize(3, value);
    }

    public static int computeTagSize(int fieldNumber) {
        return computeUInt32SizeNoTag(WireFormat.makeTag(fieldNumber, 0));
    }

    public static int computeInt32SizeNoTag(int value) {
        return value >= 0 ? computeUInt32SizeNoTag(value) : 10;
    }

    public static int computeUInt32SizeNoTag(int value) {
        if ((value & -128) == 0) {
            return 1;
        } else if ((value & -16384) == 0) {
            return 2;
        } else if ((value & -2097152) == 0) {
            return 3;
        } else {
            return (value & -268435456) == 0 ? 4 : 5;
        }
    }

    public static int computeSInt32SizeNoTag(int value) {
        return computeUInt32SizeNoTag(encodeZigZag32(value));
    }

    public static int computeFixed32SizeNoTag(int unused) {
        return 4;
    }

    public static int computeSFixed32SizeNoTag(int unused) {
        return 4;
    }

    public static int computeInt64SizeNoTag(long value) {
        return computeUInt64SizeNoTag(value);
    }

    public static int computeUInt64SizeNoTag(long value) {
        if ((value & -128L) == 0L) {
            return 1;
        } else if (value < 0L) {
            return 10;
        } else {
            int n = 2;
            if ((value & -34359738368L) != 0L) {
                n += 4;
                value >>>= 28;
            }
            if ((value & -2097152L) != 0L) {
                n += 2;
                value >>>= 14;
            }
            if ((value & -16384L) != 0L) {
                n++;
            }
            return n;
        }
    }

    public static int computeSInt64SizeNoTag(long value) {
        return computeUInt64SizeNoTag(encodeZigZag64(value));
    }

    public static int computeFixed64SizeNoTag(long unused) {
        return 8;
    }

    public static int computeSFixed64SizeNoTag(long unused) {
        return 8;
    }

    public static int computeFloatSizeNoTag(float unused) {
        return 4;
    }

    public static int computeDoubleSizeNoTag(double unused) {
        return 8;
    }

    public static int computeBoolSizeNoTag(boolean unused) {
        return 1;
    }

    public static int computeEnumSizeNoTag(int value) {
        return computeInt32SizeNoTag(value);
    }

    public static int computeStringSizeNoTag(String value) {
        int length;
        try {
            length = Utf8.encodedLength(value);
        } catch (Utf8.UnpairedSurrogateException var4) {
            byte[] bytes = value.getBytes(Internal.UTF_8);
            length = bytes.length;
        }
        return computeLengthDelimitedFieldSize(length);
    }

    public static int computeLazyFieldSizeNoTag(LazyFieldLite value) {
        return computeLengthDelimitedFieldSize(value.getSerializedSize());
    }

    public static int computeBytesSizeNoTag(ByteString value) {
        return computeLengthDelimitedFieldSize(value.size());
    }

    public static int computeByteArraySizeNoTag(byte[] value) {
        return computeLengthDelimitedFieldSize(value.length);
    }

    public static int computeByteBufferSizeNoTag(ByteBuffer value) {
        return computeLengthDelimitedFieldSize(value.capacity());
    }

    public static int computeMessageSizeNoTag(MessageLite value) {
        return computeLengthDelimitedFieldSize(value.getSerializedSize());
    }

    static int computeMessageSizeNoTag(MessageLite value, Schema schema) {
        return computeLengthDelimitedFieldSize(((AbstractMessageLite) value).getSerializedSize(schema));
    }

    static int computeLengthDelimitedFieldSize(int fieldLength) {
        return computeUInt32SizeNoTag(fieldLength) + fieldLength;
    }

    public static int encodeZigZag32(int n) {
        return n << 1 ^ n >> 31;
    }

    public static long encodeZigZag64(long n) {
        return n << 1 ^ n >> 63;
    }

    public abstract void flush() throws IOException;

    public abstract int spaceLeft();

    public final void checkNoSpaceLeft() {
        if (this.spaceLeft() != 0) {
            throw new IllegalStateException("Did not write as much data as expected.");
        }
    }

    public abstract int getTotalBytesWritten();

    abstract void writeByteArrayNoTag(byte[] var1, int var2, int var3) throws IOException;

    final void inefficientWriteStringNoTag(String value, Utf8.UnpairedSurrogateException cause) throws IOException {
        logger.log(Level.WARNING, "Converting ill-formed UTF-16. Your Protocol Buffer will not round trip correctly!", cause);
        byte[] bytes = value.getBytes(Internal.UTF_8);
        try {
            this.writeUInt32NoTag(bytes.length);
            this.writeLazy(bytes, 0, bytes.length);
        } catch (IndexOutOfBoundsException var5) {
            throw new CodedOutputStream.OutOfSpaceException(var5);
        }
    }

    @Deprecated
    public final void writeGroup(int fieldNumber, MessageLite value) throws IOException {
        this.writeTag(fieldNumber, 3);
        this.writeGroupNoTag(value);
        this.writeTag(fieldNumber, 4);
    }

    @Deprecated
    final void writeGroup(int fieldNumber, MessageLite value, Schema schema) throws IOException {
        this.writeTag(fieldNumber, 3);
        this.writeGroupNoTag(value, schema);
        this.writeTag(fieldNumber, 4);
    }

    @Deprecated
    public final void writeGroupNoTag(MessageLite value) throws IOException {
        value.writeTo(this);
    }

    @Deprecated
    final void writeGroupNoTag(MessageLite value, Schema schema) throws IOException {
        schema.writeTo(value, this.wrapper);
    }

    @Deprecated
    public static int computeGroupSize(int fieldNumber, MessageLite value) {
        return computeTagSize(fieldNumber) * 2 + value.getSerializedSize();
    }

    @Deprecated
    static int computeGroupSize(int fieldNumber, MessageLite value, Schema schema) {
        return computeTagSize(fieldNumber) * 2 + computeGroupSizeNoTag(value, schema);
    }

    @Deprecated
    @InlineMe(replacement = "value.getSerializedSize()")
    public static int computeGroupSizeNoTag(MessageLite value) {
        return value.getSerializedSize();
    }

    @Deprecated
    static int computeGroupSizeNoTag(MessageLite value, Schema schema) {
        return ((AbstractMessageLite) value).getSerializedSize(schema);
    }

    @Deprecated
    @InlineMe(replacement = "this.writeUInt32NoTag(value)")
    public final void writeRawVarint32(int value) throws IOException {
        this.writeUInt32NoTag(value);
    }

    @Deprecated
    @InlineMe(replacement = "this.writeUInt64NoTag(value)")
    public final void writeRawVarint64(long value) throws IOException {
        this.writeUInt64NoTag(value);
    }

    @Deprecated
    @InlineMe(replacement = "CodedOutputStream.computeUInt32SizeNoTag(value)", imports = { "me.lucko.spark.lib.protobuf.CodedOutputStream" })
    public static int computeRawVarint32Size(int value) {
        return computeUInt32SizeNoTag(value);
    }

    @Deprecated
    @InlineMe(replacement = "CodedOutputStream.computeUInt64SizeNoTag(value)", imports = { "me.lucko.spark.lib.protobuf.CodedOutputStream" })
    public static int computeRawVarint64Size(long value) {
        return computeUInt64SizeNoTag(value);
    }

    @Deprecated
    @InlineMe(replacement = "this.writeFixed32NoTag(value)")
    public final void writeRawLittleEndian32(int value) throws IOException {
        this.writeFixed32NoTag(value);
    }

    @Deprecated
    @InlineMe(replacement = "this.writeFixed64NoTag(value)")
    public final void writeRawLittleEndian64(long value) throws IOException {
        this.writeFixed64NoTag(value);
    }

    private abstract static class AbstractBufferedEncoder extends CodedOutputStream {

        final byte[] buffer;

        final int limit;

        int position;

        int totalBytesWritten;

        AbstractBufferedEncoder(int bufferSize) {
            if (bufferSize < 0) {
                throw new IllegalArgumentException("bufferSize must be >= 0");
            } else {
                this.buffer = new byte[Math.max(bufferSize, 20)];
                this.limit = this.buffer.length;
            }
        }

        @Override
        public final int spaceLeft() {
            throw new UnsupportedOperationException("spaceLeft() can only be called on CodedOutputStreams that are writing to a flat array or ByteBuffer.");
        }

        @Override
        public final int getTotalBytesWritten() {
            return this.totalBytesWritten;
        }

        final void buffer(byte value) {
            this.buffer[this.position++] = value;
            this.totalBytesWritten++;
        }

        final void bufferTag(int fieldNumber, int wireType) {
            this.bufferUInt32NoTag(WireFormat.makeTag(fieldNumber, wireType));
        }

        final void bufferInt32NoTag(int value) {
            if (value >= 0) {
                this.bufferUInt32NoTag(value);
            } else {
                this.bufferUInt64NoTag((long) value);
            }
        }

        final void bufferUInt32NoTag(int value) {
            if (CodedOutputStream.HAS_UNSAFE_ARRAY_OPERATIONS) {
                long originalPos = (long) this.position;
                while ((value & -128) != 0) {
                    UnsafeUtil.putByte(this.buffer, (long) (this.position++), (byte) (value & 127 | 128));
                    value >>>= 7;
                }
                UnsafeUtil.putByte(this.buffer, (long) (this.position++), (byte) value);
                int delta = (int) ((long) this.position - originalPos);
                this.totalBytesWritten += delta;
            } else {
                while ((value & -128) != 0) {
                    this.buffer[this.position++] = (byte) (value & 127 | 128);
                    this.totalBytesWritten++;
                    value >>>= 7;
                }
                this.buffer[this.position++] = (byte) value;
                this.totalBytesWritten++;
            }
        }

        final void bufferUInt64NoTag(long value) {
            if (CodedOutputStream.HAS_UNSAFE_ARRAY_OPERATIONS) {
                long originalPos = (long) this.position;
                while ((value & -128L) != 0L) {
                    UnsafeUtil.putByte(this.buffer, (long) (this.position++), (byte) ((int) value & 127 | 128));
                    value >>>= 7;
                }
                UnsafeUtil.putByte(this.buffer, (long) (this.position++), (byte) ((int) value));
                int delta = (int) ((long) this.position - originalPos);
                this.totalBytesWritten += delta;
            } else {
                while ((value & -128L) != 0L) {
                    this.buffer[this.position++] = (byte) ((int) value & 127 | 128);
                    this.totalBytesWritten++;
                    value >>>= 7;
                }
                this.buffer[this.position++] = (byte) ((int) value);
                this.totalBytesWritten++;
            }
        }

        final void bufferFixed32NoTag(int value) {
            this.buffer[this.position++] = (byte) (value & 0xFF);
            this.buffer[this.position++] = (byte) (value >> 8 & 0xFF);
            this.buffer[this.position++] = (byte) (value >> 16 & 0xFF);
            this.buffer[this.position++] = (byte) (value >> 24 & 0xFF);
            this.totalBytesWritten += 4;
        }

        final void bufferFixed64NoTag(long value) {
            this.buffer[this.position++] = (byte) ((int) (value & 255L));
            this.buffer[this.position++] = (byte) ((int) (value >> 8 & 255L));
            this.buffer[this.position++] = (byte) ((int) (value >> 16 & 255L));
            this.buffer[this.position++] = (byte) ((int) (value >> 24 & 255L));
            this.buffer[this.position++] = (byte) ((int) (value >> 32) & 0xFF);
            this.buffer[this.position++] = (byte) ((int) (value >> 40) & 0xFF);
            this.buffer[this.position++] = (byte) ((int) (value >> 48) & 0xFF);
            this.buffer[this.position++] = (byte) ((int) (value >> 56) & 0xFF);
            this.totalBytesWritten += 8;
        }
    }

    private static class ArrayEncoder extends CodedOutputStream {

        private final byte[] buffer;

        private final int offset;

        private final int limit;

        private int position;

        ArrayEncoder(byte[] buffer, int offset, int length) {
            if (buffer == null) {
                throw new NullPointerException("buffer");
            } else if ((offset | length | buffer.length - (offset + length)) < 0) {
                throw new IllegalArgumentException(String.format("Array range is invalid. Buffer.length=%d, offset=%d, length=%d", buffer.length, offset, length));
            } else {
                this.buffer = buffer;
                this.offset = offset;
                this.position = offset;
                this.limit = offset + length;
            }
        }

        @Override
        public final void writeTag(int fieldNumber, int wireType) throws IOException {
            this.writeUInt32NoTag(WireFormat.makeTag(fieldNumber, wireType));
        }

        @Override
        public final void writeInt32(int fieldNumber, int value) throws IOException {
            this.writeTag(fieldNumber, 0);
            this.writeInt32NoTag(value);
        }

        @Override
        public final void writeUInt32(int fieldNumber, int value) throws IOException {
            this.writeTag(fieldNumber, 0);
            this.writeUInt32NoTag(value);
        }

        @Override
        public final void writeFixed32(int fieldNumber, int value) throws IOException {
            this.writeTag(fieldNumber, 5);
            this.writeFixed32NoTag(value);
        }

        @Override
        public final void writeUInt64(int fieldNumber, long value) throws IOException {
            this.writeTag(fieldNumber, 0);
            this.writeUInt64NoTag(value);
        }

        @Override
        public final void writeFixed64(int fieldNumber, long value) throws IOException {
            this.writeTag(fieldNumber, 1);
            this.writeFixed64NoTag(value);
        }

        @Override
        public final void writeBool(int fieldNumber, boolean value) throws IOException {
            this.writeTag(fieldNumber, 0);
            this.write((byte) (value ? 1 : 0));
        }

        @Override
        public final void writeString(int fieldNumber, String value) throws IOException {
            this.writeTag(fieldNumber, 2);
            this.writeStringNoTag(value);
        }

        @Override
        public final void writeBytes(int fieldNumber, ByteString value) throws IOException {
            this.writeTag(fieldNumber, 2);
            this.writeBytesNoTag(value);
        }

        @Override
        public final void writeByteArray(int fieldNumber, byte[] value) throws IOException {
            this.writeByteArray(fieldNumber, value, 0, value.length);
        }

        @Override
        public final void writeByteArray(int fieldNumber, byte[] value, int offset, int length) throws IOException {
            this.writeTag(fieldNumber, 2);
            this.writeByteArrayNoTag(value, offset, length);
        }

        @Override
        public final void writeByteBuffer(int fieldNumber, ByteBuffer value) throws IOException {
            this.writeTag(fieldNumber, 2);
            this.writeUInt32NoTag(value.capacity());
            this.writeRawBytes(value);
        }

        @Override
        public final void writeBytesNoTag(ByteString value) throws IOException {
            this.writeUInt32NoTag(value.size());
            value.writeTo(this);
        }

        @Override
        public final void writeByteArrayNoTag(byte[] value, int offset, int length) throws IOException {
            this.writeUInt32NoTag(length);
            this.write(value, offset, length);
        }

        @Override
        public final void writeRawBytes(ByteBuffer value) throws IOException {
            if (value.hasArray()) {
                this.write(value.array(), value.arrayOffset(), value.capacity());
            } else {
                ByteBuffer duplicated = value.duplicate();
                duplicated.clear();
                this.write(duplicated);
            }
        }

        @Override
        public final void writeMessage(int fieldNumber, MessageLite value) throws IOException {
            this.writeTag(fieldNumber, 2);
            this.writeMessageNoTag(value);
        }

        @Override
        final void writeMessage(int fieldNumber, MessageLite value, Schema schema) throws IOException {
            this.writeTag(fieldNumber, 2);
            this.writeUInt32NoTag(((AbstractMessageLite) value).getSerializedSize(schema));
            schema.writeTo(value, this.wrapper);
        }

        @Override
        public final void writeMessageSetExtension(int fieldNumber, MessageLite value) throws IOException {
            this.writeTag(1, 3);
            this.writeUInt32(2, fieldNumber);
            this.writeMessage(3, value);
            this.writeTag(1, 4);
        }

        @Override
        public final void writeRawMessageSetExtension(int fieldNumber, ByteString value) throws IOException {
            this.writeTag(1, 3);
            this.writeUInt32(2, fieldNumber);
            this.writeBytes(3, value);
            this.writeTag(1, 4);
        }

        @Override
        public final void writeMessageNoTag(MessageLite value) throws IOException {
            this.writeUInt32NoTag(value.getSerializedSize());
            value.writeTo(this);
        }

        @Override
        final void writeMessageNoTag(MessageLite value, Schema schema) throws IOException {
            this.writeUInt32NoTag(((AbstractMessageLite) value).getSerializedSize(schema));
            schema.writeTo(value, this.wrapper);
        }

        @Override
        public final void write(byte value) throws IOException {
            try {
                this.buffer[this.position++] = value;
            } catch (IndexOutOfBoundsException var3) {
                throw new CodedOutputStream.OutOfSpaceException(String.format("Pos: %d, limit: %d, len: %d", this.position, this.limit, 1), var3);
            }
        }

        @Override
        public final void writeInt32NoTag(int value) throws IOException {
            if (value >= 0) {
                this.writeUInt32NoTag(value);
            } else {
                this.writeUInt64NoTag((long) value);
            }
        }

        @Override
        public final void writeUInt32NoTag(int value) throws IOException {
            try {
                while ((value & -128) != 0) {
                    this.buffer[this.position++] = (byte) (value & 127 | 128);
                    value >>>= 7;
                }
                this.buffer[this.position++] = (byte) value;
            } catch (IndexOutOfBoundsException var3) {
                throw new CodedOutputStream.OutOfSpaceException(String.format("Pos: %d, limit: %d, len: %d", this.position, this.limit, 1), var3);
            }
        }

        @Override
        public final void writeFixed32NoTag(int value) throws IOException {
            try {
                this.buffer[this.position++] = (byte) (value & 0xFF);
                this.buffer[this.position++] = (byte) (value >> 8 & 0xFF);
                this.buffer[this.position++] = (byte) (value >> 16 & 0xFF);
                this.buffer[this.position++] = (byte) (value >> 24 & 0xFF);
            } catch (IndexOutOfBoundsException var3) {
                throw new CodedOutputStream.OutOfSpaceException(String.format("Pos: %d, limit: %d, len: %d", this.position, this.limit, 1), var3);
            }
        }

        @Override
        public final void writeUInt64NoTag(long value) throws IOException {
            if (CodedOutputStream.HAS_UNSAFE_ARRAY_OPERATIONS && this.spaceLeft() >= 10) {
                while ((value & -128L) != 0L) {
                    UnsafeUtil.putByte(this.buffer, (long) (this.position++), (byte) ((int) value & 127 | 128));
                    value >>>= 7;
                }
                UnsafeUtil.putByte(this.buffer, (long) (this.position++), (byte) ((int) value));
            } else {
                try {
                    while ((value & -128L) != 0L) {
                        this.buffer[this.position++] = (byte) ((int) value & 127 | 128);
                        value >>>= 7;
                    }
                    this.buffer[this.position++] = (byte) ((int) value);
                } catch (IndexOutOfBoundsException var4) {
                    throw new CodedOutputStream.OutOfSpaceException(String.format("Pos: %d, limit: %d, len: %d", this.position, this.limit, 1), var4);
                }
            }
        }

        @Override
        public final void writeFixed64NoTag(long value) throws IOException {
            try {
                this.buffer[this.position++] = (byte) ((int) value & 0xFF);
                this.buffer[this.position++] = (byte) ((int) (value >> 8) & 0xFF);
                this.buffer[this.position++] = (byte) ((int) (value >> 16) & 0xFF);
                this.buffer[this.position++] = (byte) ((int) (value >> 24) & 0xFF);
                this.buffer[this.position++] = (byte) ((int) (value >> 32) & 0xFF);
                this.buffer[this.position++] = (byte) ((int) (value >> 40) & 0xFF);
                this.buffer[this.position++] = (byte) ((int) (value >> 48) & 0xFF);
                this.buffer[this.position++] = (byte) ((int) (value >> 56) & 0xFF);
            } catch (IndexOutOfBoundsException var4) {
                throw new CodedOutputStream.OutOfSpaceException(String.format("Pos: %d, limit: %d, len: %d", this.position, this.limit, 1), var4);
            }
        }

        @Override
        public final void write(byte[] value, int offset, int length) throws IOException {
            try {
                System.arraycopy(value, offset, this.buffer, this.position, length);
                this.position += length;
            } catch (IndexOutOfBoundsException var5) {
                throw new CodedOutputStream.OutOfSpaceException(String.format("Pos: %d, limit: %d, len: %d", this.position, this.limit, length), var5);
            }
        }

        @Override
        public final void writeLazy(byte[] value, int offset, int length) throws IOException {
            this.write(value, offset, length);
        }

        @Override
        public final void write(ByteBuffer value) throws IOException {
            int length = value.remaining();
            try {
                value.get(this.buffer, this.position, length);
                this.position += length;
            } catch (IndexOutOfBoundsException var4) {
                throw new CodedOutputStream.OutOfSpaceException(String.format("Pos: %d, limit: %d, len: %d", this.position, this.limit, length), var4);
            }
        }

        @Override
        public final void writeLazy(ByteBuffer value) throws IOException {
            this.write(value);
        }

        @Override
        public final void writeStringNoTag(String value) throws IOException {
            int oldPosition = this.position;
            try {
                int maxLength = value.length() * 3;
                int maxLengthVarIntSize = computeUInt32SizeNoTag(maxLength);
                int minLengthVarIntSize = computeUInt32SizeNoTag(value.length());
                if (minLengthVarIntSize == maxLengthVarIntSize) {
                    this.position = oldPosition + minLengthVarIntSize;
                    int newPosition = Utf8.encode(value, this.buffer, this.position, this.spaceLeft());
                    this.position = oldPosition;
                    int length = newPosition - oldPosition - minLengthVarIntSize;
                    this.writeUInt32NoTag(length);
                    this.position = newPosition;
                } else {
                    int length = Utf8.encodedLength(value);
                    this.writeUInt32NoTag(length);
                    this.position = Utf8.encode(value, this.buffer, this.position, this.spaceLeft());
                }
            } catch (Utf8.UnpairedSurrogateException var8) {
                this.position = oldPosition;
                this.inefficientWriteStringNoTag(value, var8);
            } catch (IndexOutOfBoundsException var9) {
                throw new CodedOutputStream.OutOfSpaceException(var9);
            }
        }

        @Override
        public void flush() {
        }

        @Override
        public final int spaceLeft() {
            return this.limit - this.position;
        }

        @Override
        public final int getTotalBytesWritten() {
            return this.position - this.offset;
        }
    }

    private static final class ByteOutputEncoder extends CodedOutputStream.AbstractBufferedEncoder {

        private final ByteOutput out;

        ByteOutputEncoder(ByteOutput out, int bufferSize) {
            super(bufferSize);
            if (out == null) {
                throw new NullPointerException("out");
            } else {
                this.out = out;
            }
        }

        @Override
        public void writeTag(int fieldNumber, int wireType) throws IOException {
            this.writeUInt32NoTag(WireFormat.makeTag(fieldNumber, wireType));
        }

        @Override
        public void writeInt32(int fieldNumber, int value) throws IOException {
            this.flushIfNotAvailable(20);
            this.bufferTag(fieldNumber, 0);
            this.bufferInt32NoTag(value);
        }

        @Override
        public void writeUInt32(int fieldNumber, int value) throws IOException {
            this.flushIfNotAvailable(20);
            this.bufferTag(fieldNumber, 0);
            this.bufferUInt32NoTag(value);
        }

        @Override
        public void writeFixed32(int fieldNumber, int value) throws IOException {
            this.flushIfNotAvailable(14);
            this.bufferTag(fieldNumber, 5);
            this.bufferFixed32NoTag(value);
        }

        @Override
        public void writeUInt64(int fieldNumber, long value) throws IOException {
            this.flushIfNotAvailable(20);
            this.bufferTag(fieldNumber, 0);
            this.bufferUInt64NoTag(value);
        }

        @Override
        public void writeFixed64(int fieldNumber, long value) throws IOException {
            this.flushIfNotAvailable(18);
            this.bufferTag(fieldNumber, 1);
            this.bufferFixed64NoTag(value);
        }

        @Override
        public void writeBool(int fieldNumber, boolean value) throws IOException {
            this.flushIfNotAvailable(11);
            this.bufferTag(fieldNumber, 0);
            this.buffer((byte) (value ? 1 : 0));
        }

        @Override
        public void writeString(int fieldNumber, String value) throws IOException {
            this.writeTag(fieldNumber, 2);
            this.writeStringNoTag(value);
        }

        @Override
        public void writeBytes(int fieldNumber, ByteString value) throws IOException {
            this.writeTag(fieldNumber, 2);
            this.writeBytesNoTag(value);
        }

        @Override
        public void writeByteArray(int fieldNumber, byte[] value) throws IOException {
            this.writeByteArray(fieldNumber, value, 0, value.length);
        }

        @Override
        public void writeByteArray(int fieldNumber, byte[] value, int offset, int length) throws IOException {
            this.writeTag(fieldNumber, 2);
            this.writeByteArrayNoTag(value, offset, length);
        }

        @Override
        public void writeByteBuffer(int fieldNumber, ByteBuffer value) throws IOException {
            this.writeTag(fieldNumber, 2);
            this.writeUInt32NoTag(value.capacity());
            this.writeRawBytes(value);
        }

        @Override
        public void writeBytesNoTag(ByteString value) throws IOException {
            this.writeUInt32NoTag(value.size());
            value.writeTo(this);
        }

        @Override
        public void writeByteArrayNoTag(byte[] value, int offset, int length) throws IOException {
            this.writeUInt32NoTag(length);
            this.write(value, offset, length);
        }

        @Override
        public void writeRawBytes(ByteBuffer value) throws IOException {
            if (value.hasArray()) {
                this.write(value.array(), value.arrayOffset(), value.capacity());
            } else {
                ByteBuffer duplicated = value.duplicate();
                duplicated.clear();
                this.write(duplicated);
            }
        }

        @Override
        public void writeMessage(int fieldNumber, MessageLite value) throws IOException {
            this.writeTag(fieldNumber, 2);
            this.writeMessageNoTag(value);
        }

        @Override
        void writeMessage(int fieldNumber, MessageLite value, Schema schema) throws IOException {
            this.writeTag(fieldNumber, 2);
            this.writeMessageNoTag(value, schema);
        }

        @Override
        public void writeMessageSetExtension(int fieldNumber, MessageLite value) throws IOException {
            this.writeTag(1, 3);
            this.writeUInt32(2, fieldNumber);
            this.writeMessage(3, value);
            this.writeTag(1, 4);
        }

        @Override
        public void writeRawMessageSetExtension(int fieldNumber, ByteString value) throws IOException {
            this.writeTag(1, 3);
            this.writeUInt32(2, fieldNumber);
            this.writeBytes(3, value);
            this.writeTag(1, 4);
        }

        @Override
        public void writeMessageNoTag(MessageLite value) throws IOException {
            this.writeUInt32NoTag(value.getSerializedSize());
            value.writeTo(this);
        }

        @Override
        void writeMessageNoTag(MessageLite value, Schema schema) throws IOException {
            this.writeUInt32NoTag(((AbstractMessageLite) value).getSerializedSize(schema));
            schema.writeTo(value, this.wrapper);
        }

        @Override
        public void write(byte value) throws IOException {
            if (this.position == this.limit) {
                this.doFlush();
            }
            this.buffer(value);
        }

        @Override
        public void writeInt32NoTag(int value) throws IOException {
            if (value >= 0) {
                this.writeUInt32NoTag(value);
            } else {
                this.writeUInt64NoTag((long) value);
            }
        }

        @Override
        public void writeUInt32NoTag(int value) throws IOException {
            this.flushIfNotAvailable(5);
            this.bufferUInt32NoTag(value);
        }

        @Override
        public void writeFixed32NoTag(int value) throws IOException {
            this.flushIfNotAvailable(4);
            this.bufferFixed32NoTag(value);
        }

        @Override
        public void writeUInt64NoTag(long value) throws IOException {
            this.flushIfNotAvailable(10);
            this.bufferUInt64NoTag(value);
        }

        @Override
        public void writeFixed64NoTag(long value) throws IOException {
            this.flushIfNotAvailable(8);
            this.bufferFixed64NoTag(value);
        }

        @Override
        public void writeStringNoTag(String value) throws IOException {
            int maxLength = value.length() * 3;
            int maxLengthVarIntSize = computeUInt32SizeNoTag(maxLength);
            if (maxLengthVarIntSize + maxLength > this.limit) {
                byte[] encodedBytes = new byte[maxLength];
                int actualLength = Utf8.encode(value, encodedBytes, 0, maxLength);
                this.writeUInt32NoTag(actualLength);
                this.writeLazy(encodedBytes, 0, actualLength);
            } else {
                if (maxLengthVarIntSize + maxLength > this.limit - this.position) {
                    this.doFlush();
                }
                int oldPosition = this.position;
                try {
                    int minLengthVarIntSize = computeUInt32SizeNoTag(value.length());
                    if (minLengthVarIntSize == maxLengthVarIntSize) {
                        this.position = oldPosition + minLengthVarIntSize;
                        int newPosition = Utf8.encode(value, this.buffer, this.position, this.limit - this.position);
                        this.position = oldPosition;
                        int length = newPosition - oldPosition - minLengthVarIntSize;
                        this.bufferUInt32NoTag(length);
                        this.position = newPosition;
                        this.totalBytesWritten += length;
                    } else {
                        int length = Utf8.encodedLength(value);
                        this.bufferUInt32NoTag(length);
                        this.position = Utf8.encode(value, this.buffer, this.position, length);
                        this.totalBytesWritten += length;
                    }
                } catch (Utf8.UnpairedSurrogateException var8) {
                    this.totalBytesWritten = this.totalBytesWritten - (this.position - oldPosition);
                    this.position = oldPosition;
                    this.inefficientWriteStringNoTag(value, var8);
                } catch (IndexOutOfBoundsException var9) {
                    throw new CodedOutputStream.OutOfSpaceException(var9);
                }
            }
        }

        @Override
        public void flush() throws IOException {
            if (this.position > 0) {
                this.doFlush();
            }
        }

        @Override
        public void write(byte[] value, int offset, int length) throws IOException {
            this.flush();
            this.out.write(value, offset, length);
            this.totalBytesWritten += length;
        }

        @Override
        public void writeLazy(byte[] value, int offset, int length) throws IOException {
            this.flush();
            this.out.writeLazy(value, offset, length);
            this.totalBytesWritten += length;
        }

        @Override
        public void write(ByteBuffer value) throws IOException {
            this.flush();
            int length = value.remaining();
            this.out.write(value);
            this.totalBytesWritten += length;
        }

        @Override
        public void writeLazy(ByteBuffer value) throws IOException {
            this.flush();
            int length = value.remaining();
            this.out.writeLazy(value);
            this.totalBytesWritten += length;
        }

        private void flushIfNotAvailable(int requiredSize) throws IOException {
            if (this.limit - this.position < requiredSize) {
                this.doFlush();
            }
        }

        private void doFlush() throws IOException {
            this.out.write(this.buffer, 0, this.position);
            this.position = 0;
        }
    }

    private static final class HeapNioEncoder extends CodedOutputStream.ArrayEncoder {

        private final ByteBuffer byteBuffer;

        private int initialPosition;

        HeapNioEncoder(ByteBuffer byteBuffer) {
            super(byteBuffer.array(), byteBuffer.arrayOffset() + byteBuffer.position(), byteBuffer.remaining());
            this.byteBuffer = byteBuffer;
            this.initialPosition = byteBuffer.position();
        }

        @Override
        public void flush() {
            this.byteBuffer.position(this.initialPosition + this.getTotalBytesWritten());
        }
    }

    public static class OutOfSpaceException extends IOException {

        private static final long serialVersionUID = -6947486886997889499L;

        private static final String MESSAGE = "CodedOutputStream was writing to a flat byte array and ran out of space.";

        OutOfSpaceException() {
            super("CodedOutputStream was writing to a flat byte array and ran out of space.");
        }

        OutOfSpaceException(String explanationMessage) {
            super("CodedOutputStream was writing to a flat byte array and ran out of space.: " + explanationMessage);
        }

        OutOfSpaceException(Throwable cause) {
            super("CodedOutputStream was writing to a flat byte array and ran out of space.", cause);
        }

        OutOfSpaceException(String explanationMessage, Throwable cause) {
            super("CodedOutputStream was writing to a flat byte array and ran out of space.: " + explanationMessage, cause);
        }
    }

    private static final class OutputStreamEncoder extends CodedOutputStream.AbstractBufferedEncoder {

        private final OutputStream out;

        OutputStreamEncoder(OutputStream out, int bufferSize) {
            super(bufferSize);
            if (out == null) {
                throw new NullPointerException("out");
            } else {
                this.out = out;
            }
        }

        @Override
        public void writeTag(int fieldNumber, int wireType) throws IOException {
            this.writeUInt32NoTag(WireFormat.makeTag(fieldNumber, wireType));
        }

        @Override
        public void writeInt32(int fieldNumber, int value) throws IOException {
            this.flushIfNotAvailable(20);
            this.bufferTag(fieldNumber, 0);
            this.bufferInt32NoTag(value);
        }

        @Override
        public void writeUInt32(int fieldNumber, int value) throws IOException {
            this.flushIfNotAvailable(20);
            this.bufferTag(fieldNumber, 0);
            this.bufferUInt32NoTag(value);
        }

        @Override
        public void writeFixed32(int fieldNumber, int value) throws IOException {
            this.flushIfNotAvailable(14);
            this.bufferTag(fieldNumber, 5);
            this.bufferFixed32NoTag(value);
        }

        @Override
        public void writeUInt64(int fieldNumber, long value) throws IOException {
            this.flushIfNotAvailable(20);
            this.bufferTag(fieldNumber, 0);
            this.bufferUInt64NoTag(value);
        }

        @Override
        public void writeFixed64(int fieldNumber, long value) throws IOException {
            this.flushIfNotAvailable(18);
            this.bufferTag(fieldNumber, 1);
            this.bufferFixed64NoTag(value);
        }

        @Override
        public void writeBool(int fieldNumber, boolean value) throws IOException {
            this.flushIfNotAvailable(11);
            this.bufferTag(fieldNumber, 0);
            this.buffer((byte) (value ? 1 : 0));
        }

        @Override
        public void writeString(int fieldNumber, String value) throws IOException {
            this.writeTag(fieldNumber, 2);
            this.writeStringNoTag(value);
        }

        @Override
        public void writeBytes(int fieldNumber, ByteString value) throws IOException {
            this.writeTag(fieldNumber, 2);
            this.writeBytesNoTag(value);
        }

        @Override
        public void writeByteArray(int fieldNumber, byte[] value) throws IOException {
            this.writeByteArray(fieldNumber, value, 0, value.length);
        }

        @Override
        public void writeByteArray(int fieldNumber, byte[] value, int offset, int length) throws IOException {
            this.writeTag(fieldNumber, 2);
            this.writeByteArrayNoTag(value, offset, length);
        }

        @Override
        public void writeByteBuffer(int fieldNumber, ByteBuffer value) throws IOException {
            this.writeTag(fieldNumber, 2);
            this.writeUInt32NoTag(value.capacity());
            this.writeRawBytes(value);
        }

        @Override
        public void writeBytesNoTag(ByteString value) throws IOException {
            this.writeUInt32NoTag(value.size());
            value.writeTo(this);
        }

        @Override
        public void writeByteArrayNoTag(byte[] value, int offset, int length) throws IOException {
            this.writeUInt32NoTag(length);
            this.write(value, offset, length);
        }

        @Override
        public void writeRawBytes(ByteBuffer value) throws IOException {
            if (value.hasArray()) {
                this.write(value.array(), value.arrayOffset(), value.capacity());
            } else {
                ByteBuffer duplicated = value.duplicate();
                duplicated.clear();
                this.write(duplicated);
            }
        }

        @Override
        public void writeMessage(int fieldNumber, MessageLite value) throws IOException {
            this.writeTag(fieldNumber, 2);
            this.writeMessageNoTag(value);
        }

        @Override
        void writeMessage(int fieldNumber, MessageLite value, Schema schema) throws IOException {
            this.writeTag(fieldNumber, 2);
            this.writeMessageNoTag(value, schema);
        }

        @Override
        public void writeMessageSetExtension(int fieldNumber, MessageLite value) throws IOException {
            this.writeTag(1, 3);
            this.writeUInt32(2, fieldNumber);
            this.writeMessage(3, value);
            this.writeTag(1, 4);
        }

        @Override
        public void writeRawMessageSetExtension(int fieldNumber, ByteString value) throws IOException {
            this.writeTag(1, 3);
            this.writeUInt32(2, fieldNumber);
            this.writeBytes(3, value);
            this.writeTag(1, 4);
        }

        @Override
        public void writeMessageNoTag(MessageLite value) throws IOException {
            this.writeUInt32NoTag(value.getSerializedSize());
            value.writeTo(this);
        }

        @Override
        void writeMessageNoTag(MessageLite value, Schema schema) throws IOException {
            this.writeUInt32NoTag(((AbstractMessageLite) value).getSerializedSize(schema));
            schema.writeTo(value, this.wrapper);
        }

        @Override
        public void write(byte value) throws IOException {
            if (this.position == this.limit) {
                this.doFlush();
            }
            this.buffer(value);
        }

        @Override
        public void writeInt32NoTag(int value) throws IOException {
            if (value >= 0) {
                this.writeUInt32NoTag(value);
            } else {
                this.writeUInt64NoTag((long) value);
            }
        }

        @Override
        public void writeUInt32NoTag(int value) throws IOException {
            this.flushIfNotAvailable(5);
            this.bufferUInt32NoTag(value);
        }

        @Override
        public void writeFixed32NoTag(int value) throws IOException {
            this.flushIfNotAvailable(4);
            this.bufferFixed32NoTag(value);
        }

        @Override
        public void writeUInt64NoTag(long value) throws IOException {
            this.flushIfNotAvailable(10);
            this.bufferUInt64NoTag(value);
        }

        @Override
        public void writeFixed64NoTag(long value) throws IOException {
            this.flushIfNotAvailable(8);
            this.bufferFixed64NoTag(value);
        }

        @Override
        public void writeStringNoTag(String value) throws IOException {
            try {
                int maxLength = value.length() * 3;
                int maxLengthVarIntSize = computeUInt32SizeNoTag(maxLength);
                if (maxLengthVarIntSize + maxLength > this.limit) {
                    byte[] encodedBytes = new byte[maxLength];
                    int actualLength = Utf8.encode(value, encodedBytes, 0, maxLength);
                    this.writeUInt32NoTag(actualLength);
                    this.writeLazy(encodedBytes, 0, actualLength);
                    return;
                }
                if (maxLengthVarIntSize + maxLength > this.limit - this.position) {
                    this.doFlush();
                }
                int minLengthVarIntSize = computeUInt32SizeNoTag(value.length());
                int oldPosition = this.position;
                try {
                    int length;
                    if (minLengthVarIntSize == maxLengthVarIntSize) {
                        this.position = oldPosition + minLengthVarIntSize;
                        int newPosition = Utf8.encode(value, this.buffer, this.position, this.limit - this.position);
                        this.position = oldPosition;
                        length = newPosition - oldPosition - minLengthVarIntSize;
                        this.bufferUInt32NoTag(length);
                        this.position = newPosition;
                    } else {
                        length = Utf8.encodedLength(value);
                        this.bufferUInt32NoTag(length);
                        this.position = Utf8.encode(value, this.buffer, this.position, length);
                    }
                    this.totalBytesWritten += length;
                } catch (Utf8.UnpairedSurrogateException var8) {
                    this.totalBytesWritten = this.totalBytesWritten - (this.position - oldPosition);
                    this.position = oldPosition;
                    throw var8;
                } catch (ArrayIndexOutOfBoundsException var9) {
                    throw new CodedOutputStream.OutOfSpaceException(var9);
                }
            } catch (Utf8.UnpairedSurrogateException var10) {
                this.inefficientWriteStringNoTag(value, var10);
            }
        }

        @Override
        public void flush() throws IOException {
            if (this.position > 0) {
                this.doFlush();
            }
        }

        @Override
        public void write(byte[] value, int offset, int length) throws IOException {
            if (this.limit - this.position >= length) {
                System.arraycopy(value, offset, this.buffer, this.position, length);
                this.position += length;
                this.totalBytesWritten += length;
            } else {
                int bytesWritten = this.limit - this.position;
                System.arraycopy(value, offset, this.buffer, this.position, bytesWritten);
                offset += bytesWritten;
                length -= bytesWritten;
                this.position = this.limit;
                this.totalBytesWritten += bytesWritten;
                this.doFlush();
                if (length <= this.limit) {
                    System.arraycopy(value, offset, this.buffer, 0, length);
                    this.position = length;
                } else {
                    this.out.write(value, offset, length);
                }
                this.totalBytesWritten += length;
            }
        }

        @Override
        public void writeLazy(byte[] value, int offset, int length) throws IOException {
            this.write(value, offset, length);
        }

        @Override
        public void write(ByteBuffer value) throws IOException {
            int length = value.remaining();
            if (this.limit - this.position >= length) {
                value.get(this.buffer, this.position, length);
                this.position += length;
                this.totalBytesWritten += length;
            } else {
                int bytesWritten = this.limit - this.position;
                value.get(this.buffer, this.position, bytesWritten);
                length -= bytesWritten;
                this.position = this.limit;
                this.totalBytesWritten += bytesWritten;
                this.doFlush();
                while (length > this.limit) {
                    value.get(this.buffer, 0, this.limit);
                    this.out.write(this.buffer, 0, this.limit);
                    length -= this.limit;
                    this.totalBytesWritten = this.totalBytesWritten + this.limit;
                }
                value.get(this.buffer, 0, length);
                this.position = length;
                this.totalBytesWritten += length;
            }
        }

        @Override
        public void writeLazy(ByteBuffer value) throws IOException {
            this.write(value);
        }

        private void flushIfNotAvailable(int requiredSize) throws IOException {
            if (this.limit - this.position < requiredSize) {
                this.doFlush();
            }
        }

        private void doFlush() throws IOException {
            this.out.write(this.buffer, 0, this.position);
            this.position = 0;
        }
    }

    private static final class SafeDirectNioEncoder extends CodedOutputStream {

        private final ByteBuffer originalBuffer;

        private final ByteBuffer buffer;

        private final int initialPosition;

        SafeDirectNioEncoder(ByteBuffer buffer) {
            this.originalBuffer = buffer;
            this.buffer = buffer.duplicate().order(ByteOrder.LITTLE_ENDIAN);
            this.initialPosition = buffer.position();
        }

        @Override
        public void writeTag(int fieldNumber, int wireType) throws IOException {
            this.writeUInt32NoTag(WireFormat.makeTag(fieldNumber, wireType));
        }

        @Override
        public void writeInt32(int fieldNumber, int value) throws IOException {
            this.writeTag(fieldNumber, 0);
            this.writeInt32NoTag(value);
        }

        @Override
        public void writeUInt32(int fieldNumber, int value) throws IOException {
            this.writeTag(fieldNumber, 0);
            this.writeUInt32NoTag(value);
        }

        @Override
        public void writeFixed32(int fieldNumber, int value) throws IOException {
            this.writeTag(fieldNumber, 5);
            this.writeFixed32NoTag(value);
        }

        @Override
        public void writeUInt64(int fieldNumber, long value) throws IOException {
            this.writeTag(fieldNumber, 0);
            this.writeUInt64NoTag(value);
        }

        @Override
        public void writeFixed64(int fieldNumber, long value) throws IOException {
            this.writeTag(fieldNumber, 1);
            this.writeFixed64NoTag(value);
        }

        @Override
        public void writeBool(int fieldNumber, boolean value) throws IOException {
            this.writeTag(fieldNumber, 0);
            this.write((byte) (value ? 1 : 0));
        }

        @Override
        public void writeString(int fieldNumber, String value) throws IOException {
            this.writeTag(fieldNumber, 2);
            this.writeStringNoTag(value);
        }

        @Override
        public void writeBytes(int fieldNumber, ByteString value) throws IOException {
            this.writeTag(fieldNumber, 2);
            this.writeBytesNoTag(value);
        }

        @Override
        public void writeByteArray(int fieldNumber, byte[] value) throws IOException {
            this.writeByteArray(fieldNumber, value, 0, value.length);
        }

        @Override
        public void writeByteArray(int fieldNumber, byte[] value, int offset, int length) throws IOException {
            this.writeTag(fieldNumber, 2);
            this.writeByteArrayNoTag(value, offset, length);
        }

        @Override
        public void writeByteBuffer(int fieldNumber, ByteBuffer value) throws IOException {
            this.writeTag(fieldNumber, 2);
            this.writeUInt32NoTag(value.capacity());
            this.writeRawBytes(value);
        }

        @Override
        public void writeMessage(int fieldNumber, MessageLite value) throws IOException {
            this.writeTag(fieldNumber, 2);
            this.writeMessageNoTag(value);
        }

        @Override
        void writeMessage(int fieldNumber, MessageLite value, Schema schema) throws IOException {
            this.writeTag(fieldNumber, 2);
            this.writeMessageNoTag(value, schema);
        }

        @Override
        public void writeMessageSetExtension(int fieldNumber, MessageLite value) throws IOException {
            this.writeTag(1, 3);
            this.writeUInt32(2, fieldNumber);
            this.writeMessage(3, value);
            this.writeTag(1, 4);
        }

        @Override
        public void writeRawMessageSetExtension(int fieldNumber, ByteString value) throws IOException {
            this.writeTag(1, 3);
            this.writeUInt32(2, fieldNumber);
            this.writeBytes(3, value);
            this.writeTag(1, 4);
        }

        @Override
        public void writeMessageNoTag(MessageLite value) throws IOException {
            this.writeUInt32NoTag(value.getSerializedSize());
            value.writeTo(this);
        }

        @Override
        void writeMessageNoTag(MessageLite value, Schema schema) throws IOException {
            this.writeUInt32NoTag(((AbstractMessageLite) value).getSerializedSize(schema));
            schema.writeTo(value, this.wrapper);
        }

        @Override
        public void write(byte value) throws IOException {
            try {
                this.buffer.put(value);
            } catch (BufferOverflowException var3) {
                throw new CodedOutputStream.OutOfSpaceException(var3);
            }
        }

        @Override
        public void writeBytesNoTag(ByteString value) throws IOException {
            this.writeUInt32NoTag(value.size());
            value.writeTo(this);
        }

        @Override
        public void writeByteArrayNoTag(byte[] value, int offset, int length) throws IOException {
            this.writeUInt32NoTag(length);
            this.write(value, offset, length);
        }

        @Override
        public void writeRawBytes(ByteBuffer value) throws IOException {
            if (value.hasArray()) {
                this.write(value.array(), value.arrayOffset(), value.capacity());
            } else {
                ByteBuffer duplicated = value.duplicate();
                duplicated.clear();
                this.write(duplicated);
            }
        }

        @Override
        public void writeInt32NoTag(int value) throws IOException {
            if (value >= 0) {
                this.writeUInt32NoTag(value);
            } else {
                this.writeUInt64NoTag((long) value);
            }
        }

        @Override
        public void writeUInt32NoTag(int value) throws IOException {
            try {
                while ((value & -128) != 0) {
                    this.buffer.put((byte) (value & 127 | 128));
                    value >>>= 7;
                }
                this.buffer.put((byte) value);
            } catch (BufferOverflowException var3) {
                throw new CodedOutputStream.OutOfSpaceException(var3);
            }
        }

        @Override
        public void writeFixed32NoTag(int value) throws IOException {
            try {
                this.buffer.putInt(value);
            } catch (BufferOverflowException var3) {
                throw new CodedOutputStream.OutOfSpaceException(var3);
            }
        }

        @Override
        public void writeUInt64NoTag(long value) throws IOException {
            try {
                while ((value & -128L) != 0L) {
                    this.buffer.put((byte) ((int) value & 127 | 128));
                    value >>>= 7;
                }
                this.buffer.put((byte) ((int) value));
            } catch (BufferOverflowException var4) {
                throw new CodedOutputStream.OutOfSpaceException(var4);
            }
        }

        @Override
        public void writeFixed64NoTag(long value) throws IOException {
            try {
                this.buffer.putLong(value);
            } catch (BufferOverflowException var4) {
                throw new CodedOutputStream.OutOfSpaceException(var4);
            }
        }

        @Override
        public void write(byte[] value, int offset, int length) throws IOException {
            try {
                this.buffer.put(value, offset, length);
            } catch (IndexOutOfBoundsException var5) {
                throw new CodedOutputStream.OutOfSpaceException(var5);
            } catch (BufferOverflowException var6) {
                throw new CodedOutputStream.OutOfSpaceException(var6);
            }
        }

        @Override
        public void writeLazy(byte[] value, int offset, int length) throws IOException {
            this.write(value, offset, length);
        }

        @Override
        public void write(ByteBuffer value) throws IOException {
            try {
                this.buffer.put(value);
            } catch (BufferOverflowException var3) {
                throw new CodedOutputStream.OutOfSpaceException(var3);
            }
        }

        @Override
        public void writeLazy(ByteBuffer value) throws IOException {
            this.write(value);
        }

        @Override
        public void writeStringNoTag(String value) throws IOException {
            int startPos = this.buffer.position();
            try {
                int maxEncodedSize = value.length() * 3;
                int maxLengthVarIntSize = computeUInt32SizeNoTag(maxEncodedSize);
                int minLengthVarIntSize = computeUInt32SizeNoTag(value.length());
                if (minLengthVarIntSize == maxLengthVarIntSize) {
                    int startOfBytes = this.buffer.position() + minLengthVarIntSize;
                    this.buffer.position(startOfBytes);
                    this.encode(value);
                    int endOfBytes = this.buffer.position();
                    this.buffer.position(startPos);
                    this.writeUInt32NoTag(endOfBytes - startOfBytes);
                    this.buffer.position(endOfBytes);
                } else {
                    int length = Utf8.encodedLength(value);
                    this.writeUInt32NoTag(length);
                    this.encode(value);
                }
            } catch (Utf8.UnpairedSurrogateException var8) {
                this.buffer.position(startPos);
                this.inefficientWriteStringNoTag(value, var8);
            } catch (IllegalArgumentException var9) {
                throw new CodedOutputStream.OutOfSpaceException(var9);
            }
        }

        @Override
        public void flush() {
            this.originalBuffer.position(this.buffer.position());
        }

        @Override
        public int spaceLeft() {
            return this.buffer.remaining();
        }

        @Override
        public int getTotalBytesWritten() {
            return this.buffer.position() - this.initialPosition;
        }

        private void encode(String value) throws IOException {
            try {
                Utf8.encodeUtf8(value, this.buffer);
            } catch (IndexOutOfBoundsException var3) {
                throw new CodedOutputStream.OutOfSpaceException(var3);
            }
        }
    }

    private static final class UnsafeDirectNioEncoder extends CodedOutputStream {

        private final ByteBuffer originalBuffer;

        private final ByteBuffer buffer;

        private final long address;

        private final long initialPosition;

        private final long limit;

        private final long oneVarintLimit;

        private long position;

        UnsafeDirectNioEncoder(ByteBuffer buffer) {
            this.originalBuffer = buffer;
            this.buffer = buffer.duplicate().order(ByteOrder.LITTLE_ENDIAN);
            this.address = UnsafeUtil.addressOffset(buffer);
            this.initialPosition = this.address + (long) buffer.position();
            this.limit = this.address + (long) buffer.limit();
            this.oneVarintLimit = this.limit - 10L;
            this.position = this.initialPosition;
        }

        static boolean isSupported() {
            return UnsafeUtil.hasUnsafeByteBufferOperations();
        }

        @Override
        public void writeTag(int fieldNumber, int wireType) throws IOException {
            this.writeUInt32NoTag(WireFormat.makeTag(fieldNumber, wireType));
        }

        @Override
        public void writeInt32(int fieldNumber, int value) throws IOException {
            this.writeTag(fieldNumber, 0);
            this.writeInt32NoTag(value);
        }

        @Override
        public void writeUInt32(int fieldNumber, int value) throws IOException {
            this.writeTag(fieldNumber, 0);
            this.writeUInt32NoTag(value);
        }

        @Override
        public void writeFixed32(int fieldNumber, int value) throws IOException {
            this.writeTag(fieldNumber, 5);
            this.writeFixed32NoTag(value);
        }

        @Override
        public void writeUInt64(int fieldNumber, long value) throws IOException {
            this.writeTag(fieldNumber, 0);
            this.writeUInt64NoTag(value);
        }

        @Override
        public void writeFixed64(int fieldNumber, long value) throws IOException {
            this.writeTag(fieldNumber, 1);
            this.writeFixed64NoTag(value);
        }

        @Override
        public void writeBool(int fieldNumber, boolean value) throws IOException {
            this.writeTag(fieldNumber, 0);
            this.write((byte) (value ? 1 : 0));
        }

        @Override
        public void writeString(int fieldNumber, String value) throws IOException {
            this.writeTag(fieldNumber, 2);
            this.writeStringNoTag(value);
        }

        @Override
        public void writeBytes(int fieldNumber, ByteString value) throws IOException {
            this.writeTag(fieldNumber, 2);
            this.writeBytesNoTag(value);
        }

        @Override
        public void writeByteArray(int fieldNumber, byte[] value) throws IOException {
            this.writeByteArray(fieldNumber, value, 0, value.length);
        }

        @Override
        public void writeByteArray(int fieldNumber, byte[] value, int offset, int length) throws IOException {
            this.writeTag(fieldNumber, 2);
            this.writeByteArrayNoTag(value, offset, length);
        }

        @Override
        public void writeByteBuffer(int fieldNumber, ByteBuffer value) throws IOException {
            this.writeTag(fieldNumber, 2);
            this.writeUInt32NoTag(value.capacity());
            this.writeRawBytes(value);
        }

        @Override
        public void writeMessage(int fieldNumber, MessageLite value) throws IOException {
            this.writeTag(fieldNumber, 2);
            this.writeMessageNoTag(value);
        }

        @Override
        void writeMessage(int fieldNumber, MessageLite value, Schema schema) throws IOException {
            this.writeTag(fieldNumber, 2);
            this.writeMessageNoTag(value, schema);
        }

        @Override
        public void writeMessageSetExtension(int fieldNumber, MessageLite value) throws IOException {
            this.writeTag(1, 3);
            this.writeUInt32(2, fieldNumber);
            this.writeMessage(3, value);
            this.writeTag(1, 4);
        }

        @Override
        public void writeRawMessageSetExtension(int fieldNumber, ByteString value) throws IOException {
            this.writeTag(1, 3);
            this.writeUInt32(2, fieldNumber);
            this.writeBytes(3, value);
            this.writeTag(1, 4);
        }

        @Override
        public void writeMessageNoTag(MessageLite value) throws IOException {
            this.writeUInt32NoTag(value.getSerializedSize());
            value.writeTo(this);
        }

        @Override
        void writeMessageNoTag(MessageLite value, Schema schema) throws IOException {
            this.writeUInt32NoTag(((AbstractMessageLite) value).getSerializedSize(schema));
            schema.writeTo(value, this.wrapper);
        }

        @Override
        public void write(byte value) throws IOException {
            if (this.position >= this.limit) {
                throw new CodedOutputStream.OutOfSpaceException(String.format("Pos: %d, limit: %d, len: %d", this.position, this.limit, 1));
            } else {
                UnsafeUtil.putByte(this.position++, value);
            }
        }

        @Override
        public void writeBytesNoTag(ByteString value) throws IOException {
            this.writeUInt32NoTag(value.size());
            value.writeTo(this);
        }

        @Override
        public void writeByteArrayNoTag(byte[] value, int offset, int length) throws IOException {
            this.writeUInt32NoTag(length);
            this.write(value, offset, length);
        }

        @Override
        public void writeRawBytes(ByteBuffer value) throws IOException {
            if (value.hasArray()) {
                this.write(value.array(), value.arrayOffset(), value.capacity());
            } else {
                ByteBuffer duplicated = value.duplicate();
                duplicated.clear();
                this.write(duplicated);
            }
        }

        @Override
        public void writeInt32NoTag(int value) throws IOException {
            if (value >= 0) {
                this.writeUInt32NoTag(value);
            } else {
                this.writeUInt64NoTag((long) value);
            }
        }

        @Override
        public void writeUInt32NoTag(int value) throws IOException {
            if (this.position > this.oneVarintLimit) {
                while (this.position < this.limit) {
                    if ((value & -128) == 0) {
                        UnsafeUtil.putByte(this.position++, (byte) value);
                        return;
                    }
                    UnsafeUtil.putByte(this.position++, (byte) (value & 127 | 128));
                    value >>>= 7;
                }
                throw new CodedOutputStream.OutOfSpaceException(String.format("Pos: %d, limit: %d, len: %d", this.position, this.limit, 1));
            } else {
                while ((value & -128) != 0) {
                    UnsafeUtil.putByte(this.position++, (byte) (value & 127 | 128));
                    value >>>= 7;
                }
                UnsafeUtil.putByte(this.position++, (byte) value);
            }
        }

        @Override
        public void writeFixed32NoTag(int value) throws IOException {
            this.buffer.putInt(this.bufferPos(this.position), value);
            this.position += 4L;
        }

        @Override
        public void writeUInt64NoTag(long value) throws IOException {
            if (this.position > this.oneVarintLimit) {
                while (this.position < this.limit) {
                    if ((value & -128L) == 0L) {
                        UnsafeUtil.putByte(this.position++, (byte) ((int) value));
                        return;
                    }
                    UnsafeUtil.putByte(this.position++, (byte) ((int) value & 127 | 128));
                    value >>>= 7;
                }
                throw new CodedOutputStream.OutOfSpaceException(String.format("Pos: %d, limit: %d, len: %d", this.position, this.limit, 1));
            } else {
                while ((value & -128L) != 0L) {
                    UnsafeUtil.putByte(this.position++, (byte) ((int) value & 127 | 128));
                    value >>>= 7;
                }
                UnsafeUtil.putByte(this.position++, (byte) ((int) value));
            }
        }

        @Override
        public void writeFixed64NoTag(long value) throws IOException {
            this.buffer.putLong(this.bufferPos(this.position), value);
            this.position += 8L;
        }

        @Override
        public void write(byte[] value, int offset, int length) throws IOException {
            if (value != null && offset >= 0 && length >= 0 && value.length - length >= offset && this.limit - (long) length >= this.position) {
                UnsafeUtil.copyMemory(value, (long) offset, this.position, (long) length);
                this.position += (long) length;
            } else if (value == null) {
                throw new NullPointerException("value");
            } else {
                throw new CodedOutputStream.OutOfSpaceException(String.format("Pos: %d, limit: %d, len: %d", this.position, this.limit, length));
            }
        }

        @Override
        public void writeLazy(byte[] value, int offset, int length) throws IOException {
            this.write(value, offset, length);
        }

        @Override
        public void write(ByteBuffer value) throws IOException {
            try {
                int length = value.remaining();
                this.repositionBuffer(this.position);
                this.buffer.put(value);
                this.position += (long) length;
            } catch (BufferOverflowException var3) {
                throw new CodedOutputStream.OutOfSpaceException(var3);
            }
        }

        @Override
        public void writeLazy(ByteBuffer value) throws IOException {
            this.write(value);
        }

        @Override
        public void writeStringNoTag(String value) throws IOException {
            long prevPos = this.position;
            try {
                int maxEncodedSize = value.length() * 3;
                int maxLengthVarIntSize = computeUInt32SizeNoTag(maxEncodedSize);
                int minLengthVarIntSize = computeUInt32SizeNoTag(value.length());
                if (minLengthVarIntSize == maxLengthVarIntSize) {
                    int stringStart = this.bufferPos(this.position) + minLengthVarIntSize;
                    this.buffer.position(stringStart);
                    Utf8.encodeUtf8(value, this.buffer);
                    int length = this.buffer.position() - stringStart;
                    this.writeUInt32NoTag(length);
                    this.position += (long) length;
                } else {
                    int length = Utf8.encodedLength(value);
                    this.writeUInt32NoTag(length);
                    this.repositionBuffer(this.position);
                    Utf8.encodeUtf8(value, this.buffer);
                    this.position += (long) length;
                }
            } catch (Utf8.UnpairedSurrogateException var9) {
                this.position = prevPos;
                this.repositionBuffer(this.position);
                this.inefficientWriteStringNoTag(value, var9);
            } catch (IllegalArgumentException var10) {
                throw new CodedOutputStream.OutOfSpaceException(var10);
            } catch (IndexOutOfBoundsException var11) {
                throw new CodedOutputStream.OutOfSpaceException(var11);
            }
        }

        @Override
        public void flush() {
            this.originalBuffer.position(this.bufferPos(this.position));
        }

        @Override
        public int spaceLeft() {
            return (int) (this.limit - this.position);
        }

        @Override
        public int getTotalBytesWritten() {
            return (int) (this.position - this.initialPosition);
        }

        private void repositionBuffer(long pos) {
            this.buffer.position(this.bufferPos(pos));
        }

        private int bufferPos(long pos) {
            return (int) (pos - this.address);
        }
    }
}