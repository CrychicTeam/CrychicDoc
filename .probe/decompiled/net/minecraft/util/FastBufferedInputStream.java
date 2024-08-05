package net.minecraft.util;

import java.io.IOException;
import java.io.InputStream;

public class FastBufferedInputStream extends InputStream {

    private static final int DEFAULT_BUFFER_SIZE = 8192;

    private final InputStream in;

    private final byte[] buffer;

    private int limit;

    private int position;

    public FastBufferedInputStream(InputStream inputStream0) {
        this(inputStream0, 8192);
    }

    public FastBufferedInputStream(InputStream inputStream0, int int1) {
        this.in = inputStream0;
        this.buffer = new byte[int1];
    }

    public int read() throws IOException {
        if (this.position >= this.limit) {
            this.fill();
            if (this.position >= this.limit) {
                return -1;
            }
        }
        return Byte.toUnsignedInt(this.buffer[this.position++]);
    }

    public int read(byte[] byte0, int int1, int int2) throws IOException {
        int $$3 = this.bytesInBuffer();
        if ($$3 <= 0) {
            if (int2 >= this.buffer.length) {
                return this.in.read(byte0, int1, int2);
            }
            this.fill();
            $$3 = this.bytesInBuffer();
            if ($$3 <= 0) {
                return -1;
            }
        }
        if (int2 > $$3) {
            int2 = $$3;
        }
        System.arraycopy(this.buffer, this.position, byte0, int1, int2);
        this.position += int2;
        return int2;
    }

    public long skip(long long0) throws IOException {
        if (long0 <= 0L) {
            return 0L;
        } else {
            long $$1 = (long) this.bytesInBuffer();
            if ($$1 <= 0L) {
                return this.in.skip(long0);
            } else {
                if (long0 > $$1) {
                    long0 = $$1;
                }
                this.position = (int) ((long) this.position + long0);
                return long0;
            }
        }
    }

    public int available() throws IOException {
        return this.bytesInBuffer() + this.in.available();
    }

    public void close() throws IOException {
        this.in.close();
    }

    private int bytesInBuffer() {
        return this.limit - this.position;
    }

    private void fill() throws IOException {
        this.limit = 0;
        this.position = 0;
        int $$0 = this.in.read(this.buffer, 0, this.buffer.length);
        if ($$0 > 0) {
            this.limit = $$0;
        }
    }
}