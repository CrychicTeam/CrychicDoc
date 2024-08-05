package me.lucko.spark.lib.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.InvalidMarkException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

final class NioByteString extends ByteString.LeafByteString {

    private final ByteBuffer buffer;

    NioByteString(ByteBuffer buffer) {
        Internal.checkNotNull(buffer, "buffer");
        this.buffer = buffer.slice().order(ByteOrder.nativeOrder());
    }

    private Object writeReplace() {
        return ByteString.copyFrom(this.buffer.slice());
    }

    private void readObject(ObjectInputStream in) throws IOException {
        throw new InvalidObjectException("NioByteString instances are not to be serialized directly");
    }

    @Override
    public byte byteAt(int index) {
        try {
            return this.buffer.get(index);
        } catch (ArrayIndexOutOfBoundsException var3) {
            throw var3;
        } catch (IndexOutOfBoundsException var4) {
            throw new ArrayIndexOutOfBoundsException(var4.getMessage());
        }
    }

    @Override
    public byte internalByteAt(int index) {
        return this.byteAt(index);
    }

    @Override
    public int size() {
        return this.buffer.remaining();
    }

    @Override
    public ByteString substring(int beginIndex, int endIndex) {
        try {
            ByteBuffer slice = this.slice(beginIndex, endIndex);
            return new NioByteString(slice);
        } catch (ArrayIndexOutOfBoundsException var4) {
            throw var4;
        } catch (IndexOutOfBoundsException var5) {
            throw new ArrayIndexOutOfBoundsException(var5.getMessage());
        }
    }

    @Override
    protected void copyToInternal(byte[] target, int sourceOffset, int targetOffset, int numberToCopy) {
        ByteBuffer slice = this.buffer.slice();
        slice.position(sourceOffset);
        slice.get(target, targetOffset, numberToCopy);
    }

    @Override
    public void copyTo(ByteBuffer target) {
        target.put(this.buffer.slice());
    }

    @Override
    public void writeTo(OutputStream out) throws IOException {
        out.write(this.toByteArray());
    }

    @Override
    boolean equalsRange(ByteString other, int offset, int length) {
        return this.substring(0, length).equals(other.substring(offset, offset + length));
    }

    @Override
    void writeToInternal(OutputStream out, int sourceOffset, int numberToWrite) throws IOException {
        if (this.buffer.hasArray()) {
            int bufferOffset = this.buffer.arrayOffset() + this.buffer.position() + sourceOffset;
            out.write(this.buffer.array(), bufferOffset, numberToWrite);
        } else {
            ByteBufferWriter.write(this.slice(sourceOffset, sourceOffset + numberToWrite), out);
        }
    }

    @Override
    void writeTo(ByteOutput output) throws IOException {
        output.writeLazy(this.buffer.slice());
    }

    @Override
    public ByteBuffer asReadOnlyByteBuffer() {
        return this.buffer.asReadOnlyBuffer();
    }

    @Override
    public List<ByteBuffer> asReadOnlyByteBufferList() {
        return Collections.singletonList(this.asReadOnlyByteBuffer());
    }

    @Override
    protected String toStringInternal(Charset charset) {
        byte[] bytes;
        int offset;
        int length;
        if (this.buffer.hasArray()) {
            bytes = this.buffer.array();
            offset = this.buffer.arrayOffset() + this.buffer.position();
            length = this.buffer.remaining();
        } else {
            bytes = this.toByteArray();
            offset = 0;
            length = bytes.length;
        }
        return new String(bytes, offset, length, charset);
    }

    @Override
    public boolean isValidUtf8() {
        return Utf8.isValidUtf8(this.buffer);
    }

    @Override
    protected int partialIsValidUtf8(int state, int offset, int length) {
        return Utf8.partialIsValidUtf8(state, this.buffer, offset, offset + length);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (!(other instanceof ByteString)) {
            return false;
        } else {
            ByteString otherString = (ByteString) other;
            if (this.size() != otherString.size()) {
                return false;
            } else if (this.size() == 0) {
                return true;
            } else if (other instanceof NioByteString) {
                return this.buffer.equals(((NioByteString) other).buffer);
            } else {
                return other instanceof RopeByteString ? other.equals(this) : this.buffer.equals(otherString.asReadOnlyByteBuffer());
            }
        }
    }

    @Override
    protected int partialHash(int h, int offset, int length) {
        for (int i = offset; i < offset + length; i++) {
            h = h * 31 + this.buffer.get(i);
        }
        return h;
    }

    @Override
    public InputStream newInput() {
        return new InputStream() {

            private final ByteBuffer buf = NioByteString.this.buffer.slice();

            public void mark(int readlimit) {
                this.buf.mark();
            }

            public boolean markSupported() {
                return true;
            }

            public void reset() throws IOException {
                try {
                    this.buf.reset();
                } catch (InvalidMarkException var2) {
                    throw new IOException(var2);
                }
            }

            public int available() throws IOException {
                return this.buf.remaining();
            }

            public int read() throws IOException {
                return !this.buf.hasRemaining() ? -1 : this.buf.get() & 0xFF;
            }

            public int read(byte[] bytes, int off, int len) throws IOException {
                if (!this.buf.hasRemaining()) {
                    return -1;
                } else {
                    len = Math.min(len, this.buf.remaining());
                    this.buf.get(bytes, off, len);
                    return len;
                }
            }
        };
    }

    @Override
    public CodedInputStream newCodedInput() {
        return CodedInputStream.newInstance(this.buffer, true);
    }

    private ByteBuffer slice(int beginIndex, int endIndex) {
        if (beginIndex >= this.buffer.position() && endIndex <= this.buffer.limit() && beginIndex <= endIndex) {
            ByteBuffer slice = this.buffer.slice();
            slice.position(beginIndex - this.buffer.position());
            slice.limit(endIndex - this.buffer.position());
            return slice;
        } else {
            throw new IllegalArgumentException(String.format("Invalid indices [%d, %d]", beginIndex, endIndex));
        }
    }
}