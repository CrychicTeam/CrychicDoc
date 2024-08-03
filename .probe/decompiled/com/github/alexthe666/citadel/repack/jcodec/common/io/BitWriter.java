package com.github.alexthe666.citadel.repack.jcodec.common.io;

import java.nio.ByteBuffer;

public class BitWriter {

    private final ByteBuffer buf;

    private int curInt;

    private int _curBit;

    private int initPos;

    public BitWriter(ByteBuffer buf) {
        this.buf = buf;
        this.initPos = buf.position();
    }

    public BitWriter fork() {
        BitWriter fork = new BitWriter(this.buf.duplicate());
        fork._curBit = this._curBit;
        fork.curInt = this.curInt;
        fork.initPos = this.initPos;
        return fork;
    }

    public void flush() {
        int toWrite = this._curBit + 7 >> 3;
        for (int i = 0; i < toWrite; i++) {
            this.buf.put((byte) (this.curInt >>> 24));
            this.curInt <<= 8;
        }
    }

    private final void putInt(int i) {
        this.buf.put((byte) (i >>> 24));
        this.buf.put((byte) (i >> 16));
        this.buf.put((byte) (i >> 8));
        this.buf.put((byte) i);
    }

    public final void writeNBit(int value, int n) {
        if (n > 32) {
            throw new IllegalArgumentException("Max 32 bit to write");
        } else if (n != 0) {
            value &= -1 >>> 32 - n;
            if (32 - this._curBit >= n) {
                this.curInt = this.curInt | value << 32 - this._curBit - n;
                this._curBit += n;
                if (this._curBit == 32) {
                    this.putInt(this.curInt);
                    this._curBit = 0;
                    this.curInt = 0;
                }
            } else {
                int secPart = n - (32 - this._curBit);
                this.curInt |= value >>> secPart;
                this.putInt(this.curInt);
                this.curInt = value << 32 - secPart;
                this._curBit = secPart;
            }
        }
    }

    public void write1Bit(int bit) {
        this.curInt = this.curInt | bit << 32 - this._curBit - 1;
        this._curBit++;
        if (this._curBit == 32) {
            this.putInt(this.curInt);
            this._curBit = 0;
            this.curInt = 0;
        }
    }

    public int curBit() {
        return this._curBit & 7;
    }

    public int position() {
        return (this.buf.position() - this.initPos << 3) + this._curBit;
    }

    public ByteBuffer getBuffer() {
        return this.buf;
    }
}