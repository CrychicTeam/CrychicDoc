package com.github.alexthe666.citadel.repack.jaad.aac.syntax;

import com.github.alexthe666.citadel.repack.jaad.aac.AACException;

public class BitStream {

    private static final int WORD_BITS = 32;

    private static final int WORD_BYTES = 4;

    private static final int BYTE_MASK = 255;

    private byte[] buffer;

    private int pos;

    private int cache;

    protected int bitsCached;

    protected int position;

    public BitStream() {
    }

    public BitStream(byte[] data) {
        this.setData(data);
    }

    public void destroy() {
        this.reset();
        this.buffer = null;
    }

    public final void setData(byte[] data) {
        this.reset();
        int size = data.length;
        int shift = size % 4;
        this.bitsCached = 8 * shift;
        for (int i = 0; i < shift; i++) {
            byte c = data[i];
            this.cache <<= 8;
            this.cache |= 255 & c;
        }
        size -= shift;
        if (this.buffer == null || this.buffer.length != size) {
            this.buffer = new byte[size];
        }
        System.arraycopy(data, shift, this.buffer, 0, this.buffer.length);
    }

    public void byteAlign() throws AACException {
        int toFlush = this.bitsCached & 7;
        if (toFlush > 0) {
            this.skipBits(toFlush);
        }
    }

    public final void reset() {
        this.pos = 0;
        this.bitsCached = 0;
        this.cache = 0;
        this.position = 0;
    }

    public int getPosition() {
        return this.position;
    }

    public int getBitsLeft() {
        return 8 * (this.buffer.length - this.pos) + this.bitsCached;
    }

    protected int readCache(boolean peek) throws AACException {
        if (this.pos > this.buffer.length - 4) {
            throw new AACException("end of stream", true);
        } else {
            int i = (this.buffer[this.pos] & 255) << 24 | (this.buffer[this.pos + 1] & 255) << 16 | (this.buffer[this.pos + 2] & 255) << 8 | this.buffer[this.pos + 3] & 255;
            if (!peek) {
                this.pos += 4;
            }
            return i;
        }
    }

    public int readBits(int n) throws AACException {
        int result;
        if (this.bitsCached >= n) {
            this.bitsCached -= n;
            result = this.cache >> this.bitsCached & this.maskBits(n);
            this.position += n;
        } else {
            this.position += n;
            int c = this.cache & this.maskBits(this.bitsCached);
            int left = n - this.bitsCached;
            this.cache = this.readCache(false);
            this.bitsCached = 32 - left;
            result = this.cache >> this.bitsCached & this.maskBits(left) | c << left;
        }
        return result;
    }

    public int readBit() throws AACException {
        int i;
        if (this.bitsCached > 0) {
            this.bitsCached--;
            i = this.cache >> this.bitsCached & 1;
            this.position++;
        } else {
            this.cache = this.readCache(false);
            this.bitsCached = 31;
            this.position++;
            i = this.cache >> this.bitsCached & 1;
        }
        return i;
    }

    public boolean readBool() throws AACException {
        return (this.readBit() & 1) != 0;
    }

    public int peekBits(int n) throws AACException {
        int ret;
        if (this.bitsCached >= n) {
            ret = this.cache >> this.bitsCached - n & this.maskBits(n);
        } else {
            int c = this.cache & this.maskBits(this.bitsCached);
            n -= this.bitsCached;
            ret = this.readCache(true) >> 32 - n & this.maskBits(n) | c << n;
        }
        return ret;
    }

    public int peekBit() throws AACException {
        int ret;
        if (this.bitsCached > 0) {
            ret = this.cache >> this.bitsCached - 1 & 1;
        } else {
            int word = this.readCache(true);
            ret = word >> 31 & 1;
        }
        return ret;
    }

    public void skipBits(int n) throws AACException {
        this.position += n;
        if (n <= this.bitsCached) {
            this.bitsCached -= n;
        } else {
            n -= this.bitsCached;
            while (n >= 32) {
                n -= 32;
                this.readCache(false);
            }
            if (n > 0) {
                this.cache = this.readCache(false);
                this.bitsCached = 32 - n;
            } else {
                this.cache = 0;
                this.bitsCached = 0;
            }
        }
    }

    public void skipBit() throws AACException {
        this.position++;
        if (this.bitsCached > 0) {
            this.bitsCached--;
        } else {
            this.cache = this.readCache(false);
            this.bitsCached = 31;
        }
    }

    public int maskBits(int n) {
        int i;
        if (n == 32) {
            i = -1;
        } else {
            i = (1 << n) - 1;
        }
        return i;
    }
}