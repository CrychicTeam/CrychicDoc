package com.github.alexthe666.citadel.repack.jaad.aac.gain;

import com.github.alexthe666.citadel.repack.jaad.aac.AACException;
import com.github.alexthe666.citadel.repack.jaad.aac.syntax.ICSInfo;

class IMDCT implements GCConstants, IMDCTTables, Windows {

    private static final float[][] LONG_WINDOWS = new float[][] { SINE_256, KBD_256 };

    private static final float[][] SHORT_WINDOWS = new float[][] { SINE_32, KBD_32 };

    private final int frameLen;

    private final int shortFrameLen;

    private final int lbLong;

    private final int lbShort;

    private final int lbMid;

    IMDCT(int frameLen) {
        this.frameLen = frameLen;
        this.lbLong = frameLen / 4;
        this.shortFrameLen = frameLen / 8;
        this.lbShort = this.shortFrameLen / 4;
        this.lbMid = (this.lbLong - this.lbShort) / 2;
    }

    void process(float[] in, float[] out, int winShape, int winShapePrev, ICSInfo.WindowSequence winSeq) throws AACException {
        float[] buf = new float[this.frameLen];
        if (winSeq.equals(ICSInfo.WindowSequence.EIGHT_SHORT_SEQUENCE)) {
            for (int b = 0; b < 4; b++) {
                for (int j = 0; j < 8; j++) {
                    for (int i = 0; i < this.lbShort; i++) {
                        if (b % 2 == 0) {
                            buf[this.lbLong * b + this.lbShort * j + i] = in[this.shortFrameLen * j + this.lbShort * b + i];
                        } else {
                            buf[this.lbLong * b + this.lbShort * j + i] = in[this.shortFrameLen * j + this.lbShort * b + this.lbShort - 1 - i];
                        }
                    }
                }
            }
        } else {
            for (int b = 0; b < 4; b++) {
                for (int ix = 0; ix < this.lbLong; ix++) {
                    if (b % 2 == 0) {
                        buf[this.lbLong * b + ix] = in[this.lbLong * b + ix];
                    } else {
                        buf[this.lbLong * b + ix] = in[this.lbLong * b + this.lbLong - 1 - ix];
                    }
                }
            }
        }
        for (int var11 = 0; var11 < 4; var11++) {
            this.process2(buf, out, winSeq, winShape, winShapePrev, var11);
        }
    }

    private void process2(float[] in, float[] out, ICSInfo.WindowSequence winSeq, int winShape, int winShapePrev, int band) throws AACException {
        float[] bufIn = new float[this.lbLong];
        float[] bufOut = new float[this.lbLong * 2];
        float[] window = new float[this.lbLong * 2];
        float[] window1 = new float[this.lbShort * 2];
        float[] window2 = new float[this.lbShort * 2];
        switch(winSeq) {
            case ONLY_LONG_SEQUENCE:
                for (int i = 0; i < this.lbLong; i++) {
                    window[i] = LONG_WINDOWS[winShapePrev][i];
                    window[this.lbLong * 2 - 1 - i] = LONG_WINDOWS[winShape][i];
                }
                break;
            case EIGHT_SHORT_SEQUENCE:
                for (int i = 0; i < this.lbShort; i++) {
                    window1[i] = SHORT_WINDOWS[winShapePrev][i];
                    window1[this.lbShort * 2 - 1 - i] = SHORT_WINDOWS[winShape][i];
                    window2[i] = SHORT_WINDOWS[winShape][i];
                    window2[this.lbShort * 2 - 1 - i] = SHORT_WINDOWS[winShape][i];
                }
                break;
            case LONG_START_SEQUENCE:
                for (int i = 0; i < this.lbLong; i++) {
                    window[i] = LONG_WINDOWS[winShapePrev][i];
                }
                for (int var19 = 0; var19 < this.lbMid; var19++) {
                    window[var19 + this.lbLong] = 1.0F;
                }
                for (int var20 = 0; var20 < this.lbShort; var20++) {
                    window[var20 + this.lbMid + this.lbLong] = SHORT_WINDOWS[winShape][this.lbShort - 1 - var20];
                }
                for (int var21 = 0; var21 < this.lbMid; var21++) {
                    window[var21 + this.lbMid + this.lbLong + this.lbShort] = 0.0F;
                }
                break;
            case LONG_STOP_SEQUENCE:
                for (int i = 0; i < this.lbMid; i++) {
                    window[i] = 0.0F;
                }
                for (int var15 = 0; var15 < this.lbShort; var15++) {
                    window[var15 + this.lbMid] = SHORT_WINDOWS[winShapePrev][var15];
                }
                for (int var16 = 0; var16 < this.lbMid; var16++) {
                    window[var16 + this.lbMid + this.lbShort] = 1.0F;
                }
                for (int var17 = 0; var17 < this.lbLong; var17++) {
                    window[var17 + this.lbMid + this.lbShort + this.lbMid] = LONG_WINDOWS[winShape][this.lbLong - 1 - var17];
                }
        }
        if (winSeq.equals(ICSInfo.WindowSequence.EIGHT_SHORT_SEQUENCE)) {
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < this.lbShort; k++) {
                    bufIn[k] = in[band * this.lbLong + j * this.lbShort + k];
                }
                if (j == 0) {
                    System.arraycopy(window1, 0, window, 0, this.lbShort * 2);
                } else {
                    System.arraycopy(window2, 0, window, 0, this.lbShort * 2);
                }
                this.imdct(bufIn, bufOut, window, this.lbShort);
                for (int var26 = 0; var26 < this.lbShort * 2; var26++) {
                    out[band * this.lbLong * 2 + j * this.lbShort * 2 + var26] = bufOut[var26] / 32.0F;
                }
            }
        } else {
            for (int j = 0; j < this.lbLong; j++) {
                bufIn[j] = in[band * this.lbLong + j];
            }
            this.imdct(bufIn, bufOut, window, this.lbLong);
            for (int var25 = 0; var25 < this.lbLong * 2; var25++) {
                out[band * this.lbLong * 2 + var25] = bufOut[var25] / 256.0F;
            }
        }
    }

    private void imdct(float[] in, float[] out, float[] window, int n) throws AACException {
        int n2 = n / 2;
        float[][] table;
        float[][] table2;
        if (n == 256) {
            table = IMDCT_TABLE_256;
            table2 = IMDCT_POST_TABLE_256;
        } else {
            if (n != 32) {
                throw new AACException("gain control: unexpected IMDCT length");
            }
            table = IMDCT_TABLE_32;
            table2 = IMDCT_POST_TABLE_32;
        }
        float[] tmp = new float[n];
        for (int i = 0; i < n2; i++) {
            tmp[i] = in[2 * i];
        }
        for (int var11 = n2; var11 < n; var11++) {
            tmp[var11] = -in[2 * n - 1 - 2 * var11];
        }
        float[][] buf = new float[n2][2];
        for (int var12 = 0; var12 < n2; var12++) {
            buf[var12][0] = table[var12][0] * tmp[2 * var12] - table[var12][1] * tmp[2 * var12 + 1];
            buf[var12][1] = table[var12][0] * tmp[2 * var12 + 1] + table[var12][1] * tmp[2 * var12];
        }
        FFT.process(buf, n2);
        for (int var13 = 0; var13 < n2; var13++) {
            tmp[var13] = table2[var13][0] * buf[var13][0] + table2[var13][1] * buf[n2 - 1 - var13][0] + table2[var13][2] * buf[var13][1] + table2[var13][3] * buf[n2 - 1 - var13][1];
            tmp[n - 1 - var13] = table2[var13][2] * buf[var13][0] - table2[var13][3] * buf[n2 - 1 - var13][0] - table2[var13][0] * buf[var13][1] + table2[var13][1] * buf[n2 - 1 - var13][1];
        }
        System.arraycopy(tmp, n2, out, 0, n2);
        for (int var14 = n2; var14 < n * 3 / 2; var14++) {
            out[var14] = -tmp[n * 3 / 2 - 1 - var14];
        }
        for (int var15 = n * 3 / 2; var15 < n * 2; var15++) {
            out[var15] = -tmp[var15 - n * 3 / 2];
        }
        for (int var16 = 0; var16 < n; var16++) {
            out[var16] *= window[var16];
        }
    }
}