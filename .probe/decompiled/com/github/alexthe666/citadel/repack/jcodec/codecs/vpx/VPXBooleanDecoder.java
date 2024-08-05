package com.github.alexthe666.citadel.repack.jcodec.codecs.vpx;

import java.nio.ByteBuffer;

public class VPXBooleanDecoder {

    int bit_count;

    ByteBuffer input;

    int offset;

    int range;

    int value;

    long callCounter = 0L;

    private String debugName;

    public VPXBooleanDecoder(ByteBuffer input, int offset) {
        this.input = input;
        this.offset = offset;
        this.initBoolDecoder();
    }

    protected VPXBooleanDecoder() {
    }

    void initBoolDecoder() {
        this.value = 0;
        this.value = (this.input.get() & 255) << 8;
        this.offset++;
        this.range = 255;
        this.bit_count = 0;
    }

    public int readBitEq() {
        return this.readBit(128);
    }

    public int readBit(int probability) {
        int bit = 0;
        int range = this.range;
        int value = this.value;
        int split = 1 + ((range - 1) * probability >> 8);
        int bigsplit = split << 8;
        this.callCounter++;
        range = split;
        if (value >= bigsplit) {
            range = this.range - split;
            value -= bigsplit;
            bit = 1;
        }
        int count = this.bit_count;
        int shift = leadingZeroCountInByte((byte) range);
        range <<= shift;
        value <<= shift;
        count -= shift;
        if (count <= 0) {
            value |= (this.input.get() & 255) << -count;
            this.offset++;
            count += 8;
        }
        this.bit_count = count;
        this.value = value;
        this.range = range;
        return bit;
    }

    public int decodeInt(int sizeInBits) {
        int v = 0;
        while (sizeInBits-- > 0) {
            v = v << 1 | this.readBit(128);
        }
        return v;
    }

    public int readTree(int[] tree, int[] probability) {
        int i = 0;
        while ((i = tree[i + this.readBit(probability[i >> 1])]) > 0) {
        }
        return -i;
    }

    public int readTree3(int[] tree, int prob0, int prob1) {
        int i = 0;
        if ((i = tree[i + this.readBit(prob0)]) > 0) {
            while ((i = tree[i + this.readBit(prob1)]) > 0) {
            }
        }
        return -i;
    }

    public int readTreeSkip(int[] t, int[] p, int skip_branches) {
        int i = skip_branches * 2;
        while ((i = t[i + this.readBit(p[i >> 1])]) > 0) {
        }
        return -i;
    }

    public void seek() {
        this.input.position(this.offset);
    }

    public String toString() {
        return "bc: " + this.value;
    }

    public static int getBitInBytes(byte[] bs, int i) {
        int byteIndex = i >> 3;
        int bitIndex = i & 7;
        return bs[byteIndex] >> 7 - bitIndex & 1;
    }

    public static int getBitsInBytes(byte[] bytes, int idx, int len) {
        int val = 0;
        for (int i = 0; i < len; i++) {
            val = val << 1 | getBitInBytes(bytes, idx + i);
        }
        return val;
    }

    public static int leadingZeroCountInByte(byte b) {
        int i = b & 255;
        return i < 128 && i != 0 ? Integer.numberOfLeadingZeros(b) - 24 : 0;
    }
}