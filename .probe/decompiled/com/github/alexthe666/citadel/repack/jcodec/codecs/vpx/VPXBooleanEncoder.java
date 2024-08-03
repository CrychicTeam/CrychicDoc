package com.github.alexthe666.citadel.repack.jcodec.codecs.vpx;

import java.nio.ByteBuffer;

public class VPXBooleanEncoder {

    private ByteBuffer out;

    private int lowvalue;

    private int range;

    private int count;

    public VPXBooleanEncoder(ByteBuffer out) {
        this.out = out;
        this.lowvalue = 0;
        this.range = 255;
        this.count = -24;
    }

    public void writeBit(int prob, int bb) {
        int split = 1 + ((this.range - 1) * prob >> 8);
        if (bb != 0) {
            this.lowvalue += split;
            this.range -= split;
        } else {
            this.range = split;
        }
        int shift = VPXConst.vp8Norm[this.range];
        this.range <<= shift;
        this.count += shift;
        if (this.count >= 0) {
            int offset = shift - this.count;
            if ((this.lowvalue << offset - 1 & -2147483648) != 0) {
                int x;
                for (x = this.out.position() - 1; x >= 0 && this.out.get(x) == -1; x--) {
                    this.out.put(x, (byte) 0);
                }
                this.out.put(x, (byte) ((this.out.get(x) & 255) + 1));
            }
            this.out.put((byte) (this.lowvalue >> 24 - offset));
            this.lowvalue <<= offset;
            shift = this.count;
            this.lowvalue &= 16777215;
            this.count -= 8;
        }
        this.lowvalue <<= shift;
    }

    public void stop() {
        for (int i = 0; i < 32; i++) {
            this.writeBit(128, 0);
        }
    }

    public int position() {
        return this.out.position() + (this.count + 24 >> 3);
    }
}