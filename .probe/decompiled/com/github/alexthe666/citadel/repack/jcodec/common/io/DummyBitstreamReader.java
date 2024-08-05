package com.github.alexthe666.citadel.repack.jcodec.common.io;

import java.io.IOException;
import java.io.InputStream;

public class DummyBitstreamReader {

    private InputStream is;

    private int curByte;

    private int nextByte;

    private int secondByte;

    int nBit;

    protected static int bitsRead;

    int cnt = 0;

    public DummyBitstreamReader(InputStream is) throws IOException {
        this.is = is;
        this.curByte = is.read();
        this.nextByte = is.read();
        this.secondByte = is.read();
    }

    public int read1Bit() throws IOException {
        return this.read1BitInt();
    }

    public int read1BitInt() throws IOException {
        if (this.nBit == 8) {
            this.advance();
            if (this.curByte == -1) {
                return -1;
            }
        }
        int res = this.curByte >> 7 - this.nBit & 1;
        this.nBit++;
        bitsRead++;
        return res;
    }

    public int readNBit(int n) throws IOException {
        if (n > 32) {
            throw new IllegalArgumentException("Can not read more then 32 bit");
        } else {
            int val = 0;
            for (int i = 0; i < n; i++) {
                val <<= 1;
                val |= this.read1BitInt();
            }
            return val;
        }
    }

    private final void advance1() throws IOException {
        this.curByte = this.nextByte;
        this.nextByte = this.secondByte;
        this.secondByte = this.is.read();
    }

    private final void advance() throws IOException {
        this.advance1();
        this.nBit = 0;
    }

    public int readByte() throws IOException {
        if (this.nBit > 0) {
            this.advance();
        }
        int res = this.curByte;
        this.advance();
        return res;
    }

    public boolean moreRBSPData() throws IOException {
        if (this.nBit == 8) {
            this.advance();
        }
        int tail = 1 << 8 - this.nBit - 1;
        int mask = (tail << 1) - 1;
        boolean hasTail = (this.curByte & mask) == tail;
        return this.curByte != -1 && (this.nextByte != -1 || !hasTail);
    }

    public long getBitPosition() {
        return (long) (bitsRead * 8 + this.nBit % 8);
    }

    public boolean moreData() throws IOException {
        if (this.nBit == 8) {
            this.advance();
        }
        if (this.curByte == -1) {
            return false;
        } else if (this.nextByte == -1 || this.nextByte == 0 && this.secondByte == -1) {
            int mask = (1 << 8 - this.nBit) - 1;
            return (this.curByte & mask) != 0;
        } else {
            return true;
        }
    }

    public long readRemainingByte() throws IOException {
        return (long) this.readNBit(8 - this.nBit);
    }

    public int peakNextBits(int n) throws IOException {
        if (n > 8) {
            throw new IllegalArgumentException("N should be less then 8");
        } else {
            if (this.nBit == 8) {
                this.advance();
                if (this.curByte == -1) {
                    return -1;
                }
            }
            int[] bits = new int[16 - this.nBit];
            int cnt = 0;
            for (int i = this.nBit; i < 8; i++) {
                bits[cnt++] = this.curByte >> 7 - i & 1;
            }
            for (int i = 0; i < 8; i++) {
                bits[cnt++] = this.nextByte >> 7 - i & 1;
            }
            int result = 0;
            for (int i = 0; i < n; i++) {
                result <<= 1;
                result |= bits[i];
            }
            return result;
        }
    }

    public boolean isByteAligned() {
        return this.nBit % 8 == 0;
    }

    public void close() throws IOException {
        this.is.close();
    }

    public int getCurBit() {
        return this.nBit;
    }

    public final int skip(int bits) throws IOException {
        this.nBit += bits;
        int was;
        for (was = this.nBit; this.nBit >= 8 && this.curByte != -1; this.nBit -= 8) {
            this.advance1();
        }
        return was - this.nBit;
    }

    public int align() throws IOException {
        int n = 8 - this.nBit & 7;
        this.skip(8 - this.nBit & 7);
        return n;
    }

    public int checkNBit(int n) throws IOException {
        return this.peakNextBits(n);
    }

    public int curBit() {
        return this.nBit;
    }

    public boolean lastByte() throws IOException {
        return this.nextByte == -1 && this.secondByte == -1;
    }
}