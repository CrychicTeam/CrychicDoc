package icyllis.modernui.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public class IOStreamParcel extends Parcel implements AutoCloseable {

    private final InputStream mIn;

    private final OutputStream mOut;

    private final byte[] mTmpBuffer = new byte[8];

    public IOStreamParcel(InputStream in, OutputStream out) {
        this.mIn = in;
        this.mOut = out;
    }

    @Override
    protected void ensureCapacity(int len) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void position(int newPosition) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void limit(int newLimit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeBytes(byte[] src, int off, int len) {
        try {
            this.mOut.write(src, off, len);
        } catch (IOException var5) {
            throw new RuntimeException(var5);
        }
    }

    @Override
    public void writeByte(int v) {
        try {
            this.mOut.write(v);
        } catch (IOException var3) {
            throw new RuntimeException(var3);
        }
    }

    @Override
    public void writeShort(int v) {
        this.mTmpBuffer[0] = (byte) (v >>> 8);
        this.mTmpBuffer[1] = (byte) v;
        try {
            this.mOut.write(this.mTmpBuffer, 0, 2);
        } catch (IOException var3) {
            throw new RuntimeException(var3);
        }
    }

    @Override
    public void writeInt(int v) {
        this.mTmpBuffer[0] = (byte) (v >>> 24);
        this.mTmpBuffer[1] = (byte) (v >>> 16);
        this.mTmpBuffer[2] = (byte) (v >>> 8);
        this.mTmpBuffer[3] = (byte) v;
        try {
            this.mOut.write(this.mTmpBuffer, 0, 4);
        } catch (IOException var3) {
            throw new RuntimeException(var3);
        }
    }

    @Override
    public void writeLong(long v) {
        this.mTmpBuffer[0] = (byte) ((int) (v >>> 56));
        this.mTmpBuffer[1] = (byte) ((int) (v >>> 48));
        this.mTmpBuffer[2] = (byte) ((int) (v >>> 40));
        this.mTmpBuffer[3] = (byte) ((int) (v >>> 32));
        this.mTmpBuffer[4] = (byte) ((int) (v >>> 24));
        this.mTmpBuffer[5] = (byte) ((int) (v >>> 16));
        this.mTmpBuffer[6] = (byte) ((int) (v >>> 8));
        this.mTmpBuffer[7] = (byte) ((int) v);
        try {
            this.mOut.write(this.mTmpBuffer, 0, 8);
        } catch (IOException var4) {
            throw new RuntimeException(var4);
        }
    }

    @Override
    public void readBytes(byte[] dst, int off, int len) {
        try {
            int n = 0;
            while (n < len) {
                int count = this.mIn.read(dst, off + n, len - n);
                if (count < 0) {
                    throw new RuntimeException("Not enough data");
                }
                n += count;
            }
        } catch (IOException var6) {
            throw new RuntimeException(var6);
        }
    }

    @Override
    public byte readByte() {
        try {
            int ch = this.mIn.read();
            if (ch < 0) {
                throw new RuntimeException("Not enough data");
            } else {
                return (byte) ch;
            }
        } catch (IOException var2) {
            throw new RuntimeException(var2);
        }
    }

    @Override
    public short readShort() {
        try {
            int ch1 = this.mIn.read();
            int ch2 = this.mIn.read();
            if ((ch1 | ch2) < 0) {
                throw new RuntimeException("Not enough data");
            } else {
                return (short) ((ch1 << 8) + ch2);
            }
        } catch (IOException var3) {
            throw new RuntimeException(var3);
        }
    }

    @Override
    public int readInt() {
        try {
            int ch1 = this.mIn.read();
            int ch2 = this.mIn.read();
            int ch3 = this.mIn.read();
            int ch4 = this.mIn.read();
            if ((ch1 | ch2 | ch3 | ch4) < 0) {
                throw new RuntimeException("Not enough data");
            } else {
                return (ch1 << 24) + (ch2 << 16) + (ch3 << 8) + ch4;
            }
        } catch (IOException var5) {
            throw new RuntimeException(var5);
        }
    }

    @Override
    public long readLong() {
        this.readBytes(this.mTmpBuffer, 0, 8);
        return ((long) this.mTmpBuffer[0] << 56) + ((long) (this.mTmpBuffer[1] & 255) << 48) + ((long) (this.mTmpBuffer[2] & 255) << 40) + ((long) (this.mTmpBuffer[3] & 255) << 32) + ((long) (this.mTmpBuffer[4] & 255) << 24) + (long) ((this.mTmpBuffer[5] & 255) << 16) + (long) ((this.mTmpBuffer[6] & 255) << 8) + (long) (this.mTmpBuffer[7] & 255);
    }

    public void close() throws IOException {
        this.mIn.close();
        this.mOut.close();
    }
}