package snownee.kiwi.util;

import io.netty.buffer.ByteBuf;

public class BitBufferHelper {

    private ByteBuf source;

    public final boolean write;

    private byte bits;

    private int bufferedByte;

    public BitBufferHelper(ByteBuf source, boolean write) {
        this.source = source;
        this.write = write;
    }

    public void source(ByteBuf source) {
        this.bits = 0;
        this.bufferedByte = 0;
        this.source = source;
    }

    private byte readBit() {
        if (this.bits == 0) {
            this.bufferedByte = this.source.readByte();
        }
        int ret = this.bufferedByte >> 7 - this.bits & 1;
        if (++this.bits == 8) {
            this.end();
        }
        return (byte) ret;
    }

    private void writeBit(int i) {
        if (i != 0) {
            this.bufferedByte = this.bufferedByte | 1 << 7 - this.bits;
        }
        if (++this.bits == 8) {
            this.end();
        }
    }

    public void end() {
        if (this.write && this.bits != 0) {
            this.source.writeByte(this.bufferedByte);
        }
        this.bits = 0;
        this.bufferedByte = 0;
    }

    public boolean readBoolean() {
        return this.readBit() == 1;
    }

    public void writeBoolean(boolean bool) {
        this.writeBit(bool ? 1 : 0);
    }

    public int readBits(int size) {
        int ret = 0;
        for (int j = size - 1; j >= 0; j--) {
            int i = this.readBit();
            ret |= i << j;
        }
        return ret;
    }

    public void writeBits(int i, int size) {
        for (int j = size - 1; j >= 0; j--) {
            this.writeBit(i >> j & 1);
        }
    }
}