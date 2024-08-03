package com.github.alexthe666.citadel.repack.jaad.aac.filterbank;

import com.github.alexthe666.citadel.repack.jaad.aac.AACException;
import com.github.alexthe666.citadel.repack.jaad.aac.syntax.Constants;
import com.github.alexthe666.citadel.repack.jaad.aac.syntax.ICSInfo;

public class FilterBank implements Constants, SineWindows, KBDWindows {

    private final float[][] LONG_WINDOWS;

    private final float[][] SHORT_WINDOWS;

    private final int length;

    private final int shortLen;

    private final int mid;

    private final int trans;

    private final MDCT mdctShort;

    private final MDCT mdctLong;

    private final float[] buf;

    private final float[][] overlaps;

    public FilterBank(boolean smallFrames, int channels) throws AACException {
        if (smallFrames) {
            this.length = 960;
            this.shortLen = 120;
            this.LONG_WINDOWS = new float[][] { SINE_960, KBD_960 };
            this.SHORT_WINDOWS = new float[][] { SINE_120, KBD_120 };
        } else {
            this.length = 1024;
            this.shortLen = 128;
            this.LONG_WINDOWS = new float[][] { SINE_1024, KBD_1024 };
            this.SHORT_WINDOWS = new float[][] { SINE_128, KBD_128 };
        }
        this.mid = (this.length - this.shortLen) / 2;
        this.trans = this.shortLen / 2;
        this.mdctShort = new MDCT(this.shortLen * 2);
        this.mdctLong = new MDCT(this.length * 2);
        this.overlaps = new float[channels][this.length];
        this.buf = new float[2 * this.length];
    }

    public void process(ICSInfo.WindowSequence windowSequence, int windowShape, int windowShapePrev, float[] in, float[] out, int channel) {
        float[] overlap = this.overlaps[channel];
        switch(windowSequence) {
            case ONLY_LONG_SEQUENCE:
                this.mdctLong.process(in, 0, this.buf, 0);
                for (int i = 0; i < this.length; i++) {
                    out[i] = overlap[i] + this.buf[i] * this.LONG_WINDOWS[windowShapePrev][i];
                }
                for (int var22 = 0; var22 < this.length; var22++) {
                    overlap[var22] = this.buf[this.length + var22] * this.LONG_WINDOWS[windowShape][this.length - 1 - var22];
                }
                break;
            case LONG_START_SEQUENCE:
                this.mdctLong.process(in, 0, this.buf, 0);
                for (int i = 0; i < this.length; i++) {
                    out[i] = overlap[i] + this.buf[i] * this.LONG_WINDOWS[windowShapePrev][i];
                }
                for (int var18 = 0; var18 < this.mid; var18++) {
                    overlap[var18] = this.buf[this.length + var18];
                }
                for (int var19 = 0; var19 < this.shortLen; var19++) {
                    overlap[this.mid + var19] = this.buf[this.length + this.mid + var19] * this.SHORT_WINDOWS[windowShape][this.shortLen - var19 - 1];
                }
                for (int var20 = 0; var20 < this.mid; var20++) {
                    overlap[this.mid + this.shortLen + var20] = 0.0F;
                }
                break;
            case EIGHT_SHORT_SEQUENCE:
                for (int i = 0; i < 8; i++) {
                    this.mdctShort.process(in, i * this.shortLen, this.buf, 2 * i * this.shortLen);
                }
                for (int var13 = 0; var13 < this.mid; var13++) {
                    out[var13] = overlap[var13];
                }
                for (int var14 = 0; var14 < this.shortLen; var14++) {
                    out[this.mid + var14] = overlap[this.mid + var14] + this.buf[var14] * this.SHORT_WINDOWS[windowShapePrev][var14];
                    out[this.mid + 1 * this.shortLen + var14] = overlap[this.mid + this.shortLen * 1 + var14] + this.buf[this.shortLen * 1 + var14] * this.SHORT_WINDOWS[windowShape][this.shortLen - 1 - var14] + this.buf[this.shortLen * 2 + var14] * this.SHORT_WINDOWS[windowShape][var14];
                    out[this.mid + 2 * this.shortLen + var14] = overlap[this.mid + this.shortLen * 2 + var14] + this.buf[this.shortLen * 3 + var14] * this.SHORT_WINDOWS[windowShape][this.shortLen - 1 - var14] + this.buf[this.shortLen * 4 + var14] * this.SHORT_WINDOWS[windowShape][var14];
                    out[this.mid + 3 * this.shortLen + var14] = overlap[this.mid + this.shortLen * 3 + var14] + this.buf[this.shortLen * 5 + var14] * this.SHORT_WINDOWS[windowShape][this.shortLen - 1 - var14] + this.buf[this.shortLen * 6 + var14] * this.SHORT_WINDOWS[windowShape][var14];
                    if (var14 < this.trans) {
                        out[this.mid + 4 * this.shortLen + var14] = overlap[this.mid + this.shortLen * 4 + var14] + this.buf[this.shortLen * 7 + var14] * this.SHORT_WINDOWS[windowShape][this.shortLen - 1 - var14] + this.buf[this.shortLen * 8 + var14] * this.SHORT_WINDOWS[windowShape][var14];
                    }
                }
                for (int var15 = 0; var15 < this.shortLen; var15++) {
                    if (var15 >= this.trans) {
                        overlap[this.mid + 4 * this.shortLen + var15 - this.length] = this.buf[this.shortLen * 7 + var15] * this.SHORT_WINDOWS[windowShape][this.shortLen - 1 - var15] + this.buf[this.shortLen * 8 + var15] * this.SHORT_WINDOWS[windowShape][var15];
                    }
                    overlap[this.mid + 5 * this.shortLen + var15 - this.length] = this.buf[this.shortLen * 9 + var15] * this.SHORT_WINDOWS[windowShape][this.shortLen - 1 - var15] + this.buf[this.shortLen * 10 + var15] * this.SHORT_WINDOWS[windowShape][var15];
                    overlap[this.mid + 6 * this.shortLen + var15 - this.length] = this.buf[this.shortLen * 11 + var15] * this.SHORT_WINDOWS[windowShape][this.shortLen - 1 - var15] + this.buf[this.shortLen * 12 + var15] * this.SHORT_WINDOWS[windowShape][var15];
                    overlap[this.mid + 7 * this.shortLen + var15 - this.length] = this.buf[this.shortLen * 13 + var15] * this.SHORT_WINDOWS[windowShape][this.shortLen - 1 - var15] + this.buf[this.shortLen * 14 + var15] * this.SHORT_WINDOWS[windowShape][var15];
                    overlap[this.mid + 8 * this.shortLen + var15 - this.length] = this.buf[this.shortLen * 15 + var15] * this.SHORT_WINDOWS[windowShape][this.shortLen - 1 - var15];
                }
                for (int var16 = 0; var16 < this.mid; var16++) {
                    overlap[this.mid + this.shortLen + var16] = 0.0F;
                }
                break;
            case LONG_STOP_SEQUENCE:
                this.mdctLong.process(in, 0, this.buf, 0);
                for (int i = 0; i < this.mid; i++) {
                    out[i] = overlap[i];
                }
                for (int var9 = 0; var9 < this.shortLen; var9++) {
                    out[this.mid + var9] = overlap[this.mid + var9] + this.buf[this.mid + var9] * this.SHORT_WINDOWS[windowShapePrev][var9];
                }
                for (int var10 = 0; var10 < this.mid; var10++) {
                    out[this.mid + this.shortLen + var10] = overlap[this.mid + this.shortLen + var10] + this.buf[this.mid + this.shortLen + var10];
                }
                for (int var11 = 0; var11 < this.length; var11++) {
                    overlap[var11] = this.buf[this.length + var11] * this.LONG_WINDOWS[windowShape][this.length - 1 - var11];
                }
        }
    }

    public void processLTP(ICSInfo.WindowSequence windowSequence, int windowShape, int windowShapePrev, float[] in, float[] out) {
        switch(windowSequence) {
            case ONLY_LONG_SEQUENCE:
                for (int i = this.length - 1; i >= 0; i--) {
                    this.buf[i] = in[i] * this.LONG_WINDOWS[windowShapePrev][i];
                    this.buf[i + this.length] = in[i + this.length] * this.LONG_WINDOWS[windowShape][this.length - 1 - i];
                }
                break;
            case LONG_START_SEQUENCE:
                for (int i = 0; i < this.length; i++) {
                    this.buf[i] = in[i] * this.LONG_WINDOWS[windowShapePrev][i];
                }
                for (int var11 = 0; var11 < this.mid; var11++) {
                    this.buf[var11 + this.length] = in[var11 + this.length];
                }
                for (int var12 = 0; var12 < this.shortLen; var12++) {
                    this.buf[var12 + this.length + this.mid] = in[var12 + this.length + this.mid] * this.SHORT_WINDOWS[windowShape][this.shortLen - 1 - var12];
                }
                for (int var13 = 0; var13 < this.mid; var13++) {
                    this.buf[var13 + this.length + this.mid + this.shortLen] = 0.0F;
                }
            case EIGHT_SHORT_SEQUENCE:
            default:
                break;
            case LONG_STOP_SEQUENCE:
                for (int i = 0; i < this.mid; i++) {
                    this.buf[i] = 0.0F;
                }
                for (int var7 = 0; var7 < this.shortLen; var7++) {
                    this.buf[var7 + this.mid] = in[var7 + this.mid] * this.SHORT_WINDOWS[windowShapePrev][var7];
                }
                for (int var8 = 0; var8 < this.mid; var8++) {
                    this.buf[var8 + this.mid + this.shortLen] = in[var8 + this.mid + this.shortLen];
                }
                for (int var9 = 0; var9 < this.length; var9++) {
                    this.buf[var9 + this.length] = in[var9 + this.length] * this.LONG_WINDOWS[windowShape][this.length - 1 - var9];
                }
        }
        this.mdctLong.processForward(this.buf, out);
    }

    public float[] getOverlap(int channel) {
        return this.overlaps[channel];
    }
}