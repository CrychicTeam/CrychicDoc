package com.github.alexthe666.citadel.repack.jcodec.common.io;

import java.nio.ByteBuffer;

public class BitReader {

    private int deficit = -1;

    private int curInt = -1;

    private ByteBuffer bb;

    private int initPos;

    public static BitReader createBitReader(ByteBuffer bb) {
        BitReader r = new BitReader(bb);
        r.curInt = r.readInt();
        r.deficit = 0;
        return r;
    }

    private BitReader(ByteBuffer bb) {
        this.bb = bb;
        this.initPos = bb.position();
    }

    public BitReader fork() {
        BitReader fork = new BitReader(this.bb.duplicate());
        fork.initPos = 0;
        fork.curInt = this.curInt;
        fork.deficit = this.deficit;
        return fork;
    }

    public final int readInt() {
        if (this.bb.remaining() >= 4) {
            this.deficit -= 32;
            return (this.bb.get() & 0xFF) << 24 | (this.bb.get() & 0xFF) << 16 | (this.bb.get() & 0xFF) << 8 | this.bb.get() & 0xFF;
        } else {
            return this.readIntSafe();
        }
    }

    private int readIntSafe() {
        this.deficit = this.deficit - (this.bb.remaining() << 3);
        int res = 0;
        if (this.bb.hasRemaining()) {
            res |= this.bb.get() & 255;
        }
        res <<= 8;
        if (this.bb.hasRemaining()) {
            res |= this.bb.get() & 255;
        }
        res <<= 8;
        if (this.bb.hasRemaining()) {
            res |= this.bb.get() & 255;
        }
        res <<= 8;
        if (this.bb.hasRemaining()) {
            res |= this.bb.get() & 255;
        }
        return res;
    }

    public int read1Bit() {
        int ret = this.curInt >>> 31;
        this.curInt <<= 1;
        this.deficit++;
        if (this.deficit == 32) {
            this.curInt = this.readInt();
        }
        return ret;
    }

    public int readNBitSigned(int n) {
        int v = this.readNBit(n);
        return this.read1Bit() == 0 ? v : -v;
    }

    public int readNBit(int n) {
        if (n > 32) {
            throw new IllegalArgumentException("Can not read more then 32 bit");
        } else {
            int ret = 0;
            if (n + this.deficit > 31) {
                ret |= this.curInt >>> this.deficit;
                n -= 32 - this.deficit;
                ret <<= n;
                this.deficit = 32;
                this.curInt = this.readInt();
            }
            if (n != 0) {
                ret |= this.curInt >>> 32 - n;
                this.curInt <<= n;
                this.deficit += n;
            }
            return ret;
        }
    }

    public boolean moreData() {
        int remaining = this.bb.remaining() + 4 - (this.deficit + 7 >> 3);
        return remaining > 1 || remaining == 1 && this.curInt != 0;
    }

    public int remaining() {
        return (this.bb.remaining() << 3) + 32 - this.deficit;
    }

    public final boolean isByteAligned() {
        return (this.deficit & 7) == 0;
    }

    public int skip(int bits) {
        int left = bits;
        if (bits + this.deficit > 31) {
            left = bits - (32 - this.deficit);
            this.deficit = 32;
            if (left > 31) {
                int skip = Math.min(left >> 3, this.bb.remaining());
                this.bb.position(this.bb.position() + skip);
                left -= skip << 3;
            }
            this.curInt = this.readInt();
        }
        this.deficit += left;
        this.curInt <<= left;
        return bits;
    }

    public int skipFast(int bits) {
        this.deficit += bits;
        this.curInt <<= bits;
        return bits;
    }

    public int bitsToAlign() {
        return (this.deficit & 7) > 0 ? 8 - (this.deficit & 7) : 0;
    }

    public int align() {
        return (this.deficit & 7) > 0 ? this.skip(8 - (this.deficit & 7)) : 0;
    }

    public int check24Bits() {
        if (this.deficit > 16) {
            this.deficit -= 16;
            this.curInt = this.curInt | this.nextIgnore16() << this.deficit;
        }
        if (this.deficit > 8) {
            this.deficit -= 8;
            this.curInt = this.curInt | this.nextIgnore() << this.deficit;
        }
        return this.curInt >>> 8;
    }

    public int check16Bits() {
        if (this.deficit > 16) {
            this.deficit -= 16;
            this.curInt = this.curInt | this.nextIgnore16() << this.deficit;
        }
        return this.curInt >>> 16;
    }

    public int readFast16(int n) {
        if (n == 0) {
            return 0;
        } else {
            if (this.deficit > 16) {
                this.deficit -= 16;
                this.curInt = this.curInt | this.nextIgnore16() << this.deficit;
            }
            int ret = this.curInt >>> 32 - n;
            this.deficit += n;
            this.curInt <<= n;
            return ret;
        }
    }

    public int checkNBit(int n) {
        if (n > 24) {
            throw new IllegalArgumentException("Can not check more then 24 bit");
        } else {
            return this.checkNBitDontCare(n);
        }
    }

    public int checkNBitDontCare(int n) {
        while (this.deficit + n > 32) {
            this.deficit -= 8;
            this.curInt = this.curInt | this.nextIgnore() << this.deficit;
        }
        return this.curInt >>> 32 - n;
    }

    private int nextIgnore16() {
        return this.bb.remaining() > 1 ? this.bb.getShort() & 65535 : (this.bb.hasRemaining() ? (this.bb.get() & 0xFF) << 8 : 0);
    }

    private int nextIgnore() {
        return this.bb.hasRemaining() ? this.bb.get() & 0xFF : 0;
    }

    public int curBit() {
        return this.deficit & 7;
    }

    public boolean lastByte() {
        return this.bb.remaining() + 4 - (this.deficit >> 3) <= 1;
    }

    public void terminate() {
        int putBack = 32 - this.deficit >> 3;
        this.bb.position(this.bb.position() - putBack);
    }

    public int position() {
        return (this.bb.position() - this.initPos - 4 << 3) + this.deficit;
    }

    public void stop() {
        this.bb.position(this.bb.position() - (32 - this.deficit >> 3));
    }

    public int checkAllBits() {
        return this.curInt;
    }

    public boolean readBool() {
        return this.read1Bit() == 1;
    }
}