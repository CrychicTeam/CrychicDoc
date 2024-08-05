package com.github.alexthe666.citadel.repack.jcodec.codecs.common.biari;

import java.nio.ByteBuffer;

public class MEncoder {

    private ByteBuffer out;

    private int range = 510;

    private int offset;

    private int onesOutstanding;

    private boolean zeroBorrowed;

    private int outReg;

    private int bitsInOutReg;

    private int[][] models;

    public MEncoder(ByteBuffer out, int[][] models) {
        this.models = models;
        this.out = out;
    }

    public void encodeBin(int model, int bin) {
        int qs = this.range >> 6 & 3;
        int rangeLPS = MConst.rangeLPS[qs][this.models[0][model]];
        this.range -= rangeLPS;
        if (bin != this.models[1][model]) {
            this.offset = this.offset + this.range;
            this.range = rangeLPS;
            if (this.models[0][model] == 0) {
                this.models[1][model] = 1 - this.models[1][model];
            }
            this.models[0][model] = MConst.transitLPS[this.models[0][model]];
        } else if (this.models[0][model] < 62) {
            this.models[0][model]++;
        }
        this.renormalize();
    }

    public void encodeBinBypass(int bin) {
        this.offset <<= 1;
        if (bin == 1) {
            this.offset = this.offset + this.range;
        }
        if ((this.offset & 1024) != 0) {
            this.flushOutstanding(1);
            this.offset &= 1023;
        } else if ((this.offset & 512) != 0) {
            this.offset &= 511;
            this.onesOutstanding++;
        } else {
            this.flushOutstanding(0);
        }
    }

    public void encodeBinFinal(int bin) {
        this.range -= 2;
        if (bin == 0) {
            this.renormalize();
        } else {
            this.offset = this.offset + this.range;
            this.range = 2;
            this.renormalize();
        }
    }

    public void finishEncoding() {
        this.flushOutstanding(this.offset >> 9 & 1);
        this.putBit(this.offset >> 8 & 1);
        this.stuffBits();
    }

    private void renormalize() {
        while (this.range < 256) {
            if (this.offset < 256) {
                this.flushOutstanding(0);
            } else if (this.offset < 512) {
                this.offset &= 255;
                this.onesOutstanding++;
            } else {
                this.offset &= 511;
                this.flushOutstanding(1);
            }
            this.range <<= 1;
            this.offset <<= 1;
        }
    }

    private void flushOutstanding(int hasCarry) {
        if (this.zeroBorrowed) {
            this.putBit(hasCarry);
        }
        for (int trailingBit = 1 - hasCarry; this.onesOutstanding > 0; this.onesOutstanding--) {
            this.putBit(trailingBit);
        }
        this.zeroBorrowed = true;
    }

    private void putBit(int bit) {
        this.outReg = this.outReg << 1 | bit;
        this.bitsInOutReg++;
        if (this.bitsInOutReg == 8) {
            this.out.put((byte) this.outReg);
            this.outReg = 0;
            this.bitsInOutReg = 0;
        }
    }

    private void stuffBits() {
        if (this.bitsInOutReg == 0) {
            this.out.put((byte) -128);
        } else {
            this.outReg = this.outReg << 1 | 1;
            this.outReg = this.outReg << 8 - (this.bitsInOutReg + 1);
            this.out.put((byte) this.outReg);
            this.outReg = 0;
            this.bitsInOutReg = 0;
        }
    }
}