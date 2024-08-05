package com.github.alexthe666.citadel.repack.jaad.aac.syntax;

import com.github.alexthe666.citadel.repack.jaad.aac.AACException;
import com.github.alexthe666.citadel.repack.jaad.aac.DecoderConfig;
import com.github.alexthe666.citadel.repack.jaad.aac.huffman.Huffman;

class CCE extends Element implements Constants {

    public static final int BEFORE_TNS = 0;

    public static final int AFTER_TNS = 1;

    public static final int AFTER_IMDCT = 2;

    private static final float[] CCE_SCALE = new float[] { 1.0905077F, 1.1892071F, 1.4142135F, 2.0F };

    private final ICStream ics;

    private int couplingPoint;

    private int coupledCount;

    private final boolean[] channelPair;

    private final int[] idSelect;

    private final int[] chSelect;

    private final float[][] gain;

    CCE(DecoderConfig config) {
        this.ics = new ICStream(config);
        this.channelPair = new boolean[8];
        this.idSelect = new int[8];
        this.chSelect = new int[8];
        this.gain = new float[16][120];
    }

    int getCouplingPoint() {
        return this.couplingPoint;
    }

    int getCoupledCount() {
        return this.coupledCount;
    }

    boolean isChannelPair(int index) {
        return this.channelPair[index];
    }

    int getIDSelect(int index) {
        return this.idSelect[index];
    }

    int getCHSelect(int index) {
        return this.chSelect[index];
    }

    void decode(BitStream in, DecoderConfig conf) throws AACException {
        this.readElementInstanceTag(in);
        this.couplingPoint = 2 * in.readBit();
        this.coupledCount = in.readBits(3);
        int gainCount = 0;
        for (int i = 0; i <= this.coupledCount; i++) {
            gainCount++;
            this.channelPair[i] = in.readBool();
            this.idSelect[i] = in.readBits(4);
            if (this.channelPair[i]) {
                this.chSelect[i] = in.readBits(2);
                if (this.chSelect[i] == 3) {
                    gainCount++;
                }
            } else {
                this.chSelect[i] = 2;
            }
        }
        this.couplingPoint = this.couplingPoint + in.readBit();
        this.couplingPoint = this.couplingPoint | this.couplingPoint >> 1;
        boolean sign = in.readBool();
        double scale = (double) CCE_SCALE[in.readBits(2)];
        this.ics.decode(in, false, conf);
        ICSInfo info = this.ics.getInfo();
        int windowGroupCount = info.getWindowGroupCount();
        int maxSFB = info.getMaxSFB();
        int[] sfbCB = this.ics.getSfbCB();
        for (int var20 = 0; var20 < gainCount; var20++) {
            int idx = 0;
            int cge = 1;
            int xg = 0;
            float gainCache = 1.0F;
            if (var20 > 0) {
                cge = this.couplingPoint == 2 ? 1 : in.readBit();
                xg = cge == 0 ? 0 : Huffman.decodeScaleFactor(in) - 60;
                gainCache = (float) Math.pow(scale, (double) (-xg));
            }
            if (this.couplingPoint == 2) {
                this.gain[var20][0] = gainCache;
            } else {
                for (int g = 0; g < windowGroupCount; g++) {
                    for (int sfb = 0; sfb < maxSFB; idx++) {
                        if (sfbCB[idx] != 0) {
                            if (cge == 0) {
                                int t = Huffman.decodeScaleFactor(in) - 60;
                                if (t != 0) {
                                    int s = 1;
                                    t = xg += t;
                                    if (!sign) {
                                        s -= 2 * (t & 1);
                                        t >>= 1;
                                    }
                                    gainCache = (float) (Math.pow(scale, (double) (-t)) * (double) s);
                                }
                            }
                            this.gain[var20][idx] = gainCache;
                        }
                        sfb++;
                    }
                }
            }
        }
    }

    void process() {
    }

    void applyIndependentCoupling(int index, float[] data) {
        double g = (double) this.gain[index][0];
        float[] iqData = this.ics.getInvQuantData();
        for (int i = 0; i < data.length; i++) {
            data[i] = (float) ((double) data[i] + g * (double) iqData[i]);
        }
    }

    void applyDependentCoupling(int index, float[] data) {
        ICSInfo info = this.ics.getInfo();
        int[] swbOffsets = info.getSWBOffsets();
        int windowGroupCount = info.getWindowGroupCount();
        int maxSFB = info.getMaxSFB();
        int[] sfbCB = this.ics.getSfbCB();
        float[] iqData = this.ics.getInvQuantData();
        int srcOff = 0;
        int dstOff = 0;
        int idx = 0;
        for (int g = 0; g < windowGroupCount; g++) {
            int len = info.getWindowGroupLength(g);
            for (int sfb = 0; sfb < maxSFB; idx++) {
                if (sfbCB[idx] != 0) {
                    float x = this.gain[index][idx];
                    for (int group = 0; group < len; group++) {
                        for (int k = swbOffsets[sfb]; k < swbOffsets[sfb + 1]; k++) {
                            data[dstOff + group * 128 + k] = data[dstOff + group * 128 + k] + x * iqData[srcOff + group * 128 + k];
                        }
                    }
                }
                sfb++;
            }
            dstOff += len * 128;
            srcOff += len * 128;
        }
    }
}