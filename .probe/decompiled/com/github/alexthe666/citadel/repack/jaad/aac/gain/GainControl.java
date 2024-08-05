package com.github.alexthe666.citadel.repack.jaad.aac.gain;

import com.github.alexthe666.citadel.repack.jaad.aac.AACException;
import com.github.alexthe666.citadel.repack.jaad.aac.syntax.BitStream;
import com.github.alexthe666.citadel.repack.jaad.aac.syntax.ICSInfo;
import java.util.Arrays;

public class GainControl implements GCConstants {

    private final int frameLen;

    private final int lbLong;

    private final int lbShort;

    private final IMDCT imdct;

    private final IPQF ipqf;

    private final float[] buffer1;

    private final float[] function;

    private final float[][] buffer2;

    private final float[][] overlap;

    private int maxBand;

    private int[][][] level;

    private int[][][] levelPrev;

    private int[][][] location;

    private int[][][] locationPrev;

    public GainControl(int frameLen) {
        this.frameLen = frameLen;
        this.lbLong = frameLen / 4;
        this.lbShort = this.lbLong / 8;
        this.imdct = new IMDCT(frameLen);
        this.ipqf = new IPQF();
        this.levelPrev = new int[0][][];
        this.locationPrev = new int[0][][];
        this.buffer1 = new float[frameLen / 2];
        this.buffer2 = new float[4][this.lbLong];
        this.function = new float[this.lbLong * 2];
        this.overlap = new float[4][this.lbLong * 2];
    }

    public void decode(BitStream in, ICSInfo.WindowSequence winSeq) throws AACException {
        this.maxBand = in.readBits(2) + 1;
        int locBits2 = 0;
        int wdLen;
        int locBits;
        byte var11;
        switch(winSeq) {
            case ONLY_LONG_SEQUENCE:
                wdLen = 1;
                locBits = 5;
                var11 = 5;
                break;
            case EIGHT_SHORT_SEQUENCE:
                wdLen = 8;
                locBits = 2;
                var11 = 2;
                break;
            case LONG_START_SEQUENCE:
                wdLen = 2;
                locBits = 4;
                var11 = 2;
                break;
            case LONG_STOP_SEQUENCE:
                wdLen = 2;
                locBits = 4;
                var11 = 5;
                break;
            default:
                return;
        }
        this.level = new int[this.maxBand][wdLen][];
        this.location = new int[this.maxBand][wdLen][];
        for (int bd = 1; bd < this.maxBand; bd++) {
            for (int wd = 0; wd < wdLen; wd++) {
                int len = in.readBits(3);
                this.level[bd][wd] = new int[len];
                this.location[bd][wd] = new int[len];
                for (int k = 0; k < len; k++) {
                    this.level[bd][wd][k] = in.readBits(4);
                    int bits = wd == 0 ? locBits : var11;
                    this.location[bd][wd][k] = in.readBits(bits);
                }
            }
        }
    }

    public void process(float[] data, int winShape, int winShapePrev, ICSInfo.WindowSequence winSeq) throws AACException {
        this.imdct.process(data, this.buffer1, winShape, winShapePrev, winSeq);
        for (int i = 0; i < 4; i++) {
            this.compensate(this.buffer1, this.buffer2, winSeq, i);
        }
        this.ipqf.process(this.buffer2, this.frameLen, this.maxBand, data);
    }

    private void compensate(float[] in, float[][] out, ICSInfo.WindowSequence winSeq, int band) {
        if (winSeq.equals(ICSInfo.WindowSequence.EIGHT_SHORT_SEQUENCE)) {
            for (int k = 0; k < 8; k++) {
                this.calculateFunctionData(this.lbShort * 2, band, winSeq, k);
                for (int j = 0; j < this.lbShort * 2; j++) {
                    int a = band * this.lbLong * 2 + k * this.lbShort * 2 + j;
                    in[a] *= this.function[j];
                }
                for (int var9 = 0; var9 < this.lbShort; var9++) {
                    int a = var9 + this.lbLong * 7 / 16 + this.lbShort * k;
                    int b = band * this.lbLong * 2 + k * this.lbShort * 2 + var9;
                    this.overlap[band][a] = this.overlap[band][a] + in[b];
                }
                for (int var10 = 0; var10 < this.lbShort; var10++) {
                    int a = var10 + this.lbLong * 7 / 16 + this.lbShort * (k + 1);
                    int b = band * this.lbLong * 2 + k * this.lbShort * 2 + this.lbShort + var10;
                    this.overlap[band][a] = in[b];
                }
                this.locationPrev[band][0] = Arrays.copyOf(this.location[band][k], this.location[band][k].length);
                this.levelPrev[band][0] = Arrays.copyOf(this.level[band][k], this.level[band][k].length);
            }
            System.arraycopy(this.overlap[band], 0, out[band], 0, this.lbLong);
            System.arraycopy(this.overlap[band], this.lbLong, this.overlap[band], 0, this.lbLong);
        } else {
            this.calculateFunctionData(this.lbLong * 2, band, winSeq, 0);
            for (int j = 0; j < this.lbLong * 2; j++) {
                in[band * this.lbLong * 2 + j] = in[band * this.lbLong * 2 + j] * this.function[j];
            }
            for (int var12 = 0; var12 < this.lbLong; var12++) {
                out[band][var12] = this.overlap[band][var12] + in[band * this.lbLong * 2 + var12];
            }
            for (int var13 = 0; var13 < this.lbLong; var13++) {
                this.overlap[band][var13] = in[band * this.lbLong * 2 + this.lbLong + var13];
            }
            int lastBlock = winSeq.equals(ICSInfo.WindowSequence.ONLY_LONG_SEQUENCE) ? 1 : 0;
            this.locationPrev[band][0] = Arrays.copyOf(this.location[band][lastBlock], this.location[band][lastBlock].length);
            this.levelPrev[band][0] = Arrays.copyOf(this.level[band][lastBlock], this.level[band][lastBlock].length);
        }
    }

    private void calculateFunctionData(int samples, int band, ICSInfo.WindowSequence winSeq, int blockID) {
        int[] locA = new int[10];
        float[] levA = new float[10];
        float[] modFunc = new float[samples];
        float[] buf1 = new float[samples / 2];
        float[] buf2 = new float[samples / 2];
        float[] buf3 = new float[samples / 2];
        int maxLocGain0 = 0;
        int maxLocGain1 = 0;
        int maxLocGain2 = 0;
        switch(winSeq) {
            case ONLY_LONG_SEQUENCE:
            case EIGHT_SHORT_SEQUENCE:
                maxLocGain0 = maxLocGain1 = samples / 2;
                maxLocGain2 = 0;
                break;
            case LONG_START_SEQUENCE:
                maxLocGain0 = samples / 2;
                maxLocGain1 = samples * 7 / 32;
                maxLocGain2 = samples / 16;
                break;
            case LONG_STOP_SEQUENCE:
                maxLocGain0 = samples / 16;
                maxLocGain1 = samples * 7 / 32;
                maxLocGain2 = samples / 2;
        }
        this.calculateFMD(band, 0, true, maxLocGain0, samples, locA, levA, buf1);
        int block = winSeq.equals(ICSInfo.WindowSequence.EIGHT_SHORT_SEQUENCE) ? blockID : 0;
        float secLevel = this.calculateFMD(band, block, false, maxLocGain1, samples, locA, levA, buf2);
        if (winSeq.equals(ICSInfo.WindowSequence.LONG_START_SEQUENCE) || winSeq.equals(ICSInfo.WindowSequence.LONG_STOP_SEQUENCE)) {
            this.calculateFMD(band, 1, false, maxLocGain2, samples, locA, levA, buf3);
        }
        int flatLen = 0;
        if (winSeq.equals(ICSInfo.WindowSequence.LONG_STOP_SEQUENCE)) {
            flatLen = samples / 2 - maxLocGain0 - maxLocGain1;
            for (int i = 0; i < flatLen; i++) {
                modFunc[i] = 1.0F;
            }
        }
        if (winSeq.equals(ICSInfo.WindowSequence.ONLY_LONG_SEQUENCE) || winSeq.equals(ICSInfo.WindowSequence.EIGHT_SHORT_SEQUENCE)) {
            levA[0] = 1.0F;
        }
        for (int i = 0; i < maxLocGain0; i++) {
            modFunc[i + flatLen] = levA[0] * secLevel * buf1[i];
        }
        for (int var19 = 0; var19 < maxLocGain1; var19++) {
            modFunc[var19 + flatLen + maxLocGain0] = levA[0] * buf2[var19];
        }
        if (winSeq.equals(ICSInfo.WindowSequence.LONG_START_SEQUENCE)) {
            for (int var20 = 0; var20 < maxLocGain2; var20++) {
                modFunc[var20 + maxLocGain0 + maxLocGain1] = buf3[var20];
            }
            flatLen = samples / 2 - maxLocGain1 - maxLocGain2;
            for (int var21 = 0; var21 < flatLen; var21++) {
                modFunc[var21 + maxLocGain0 + maxLocGain1 + maxLocGain2] = 1.0F;
            }
        } else if (winSeq.equals(ICSInfo.WindowSequence.LONG_STOP_SEQUENCE)) {
            for (int var22 = 0; var22 < maxLocGain2; var22++) {
                modFunc[var22 + flatLen + maxLocGain0 + maxLocGain1] = buf3[var22];
            }
        }
        for (int var23 = 0; var23 < samples; var23++) {
            this.function[var23] = 1.0F / modFunc[var23];
        }
    }

    private float calculateFMD(int bd, int wd, boolean prev, int maxLocGain, int samples, int[] loc, float[] lev, float[] fmd) {
        int[] m = new int[samples / 2];
        int[] lct = prev ? this.locationPrev[bd][wd] : this.location[bd][wd];
        int[] lvl = prev ? this.levelPrev[bd][wd] : this.level[bd][wd];
        int length = lct.length;
        for (int i = 0; i < length; i++) {
            loc[i + 1] = 8 * lct[i];
            int lngain = this.getGainChangePointID(lvl[i]);
            if (lngain < 0) {
                lev[i + 1] = 1.0F / (float) Math.pow(2.0, (double) (-lngain));
            } else {
                lev[i + 1] = (float) Math.pow(2.0, (double) lngain);
            }
        }
        loc[0] = 0;
        if (length == 0) {
            lev[0] = 1.0F;
        } else {
            lev[0] = lev[1];
        }
        float secLevel = lev[0];
        loc[length + 1] = maxLocGain;
        lev[length + 1] = 1.0F;
        for (int var17 = 0; var17 < maxLocGain; var17++) {
            m[var17] = 0;
            for (int j = 0; j <= length + 1; j++) {
                if (loc[j] <= var17) {
                    m[var17] = j;
                }
            }
        }
        for (int var18 = 0; var18 < maxLocGain; var18++) {
            if (var18 >= loc[m[var18]] && var18 <= loc[m[var18]] + 7) {
                fmd[var18] = this.interpolateGain(lev[m[var18]], lev[m[var18] + 1], var18 - loc[m[var18]]);
            } else {
                fmd[var18] = lev[m[var18] + 1];
            }
        }
        return secLevel;
    }

    private int getGainChangePointID(int lngain) {
        for (int i = 0; i < 16; i++) {
            if (lngain == LN_GAIN[i]) {
                return i;
            }
        }
        return 0;
    }

    private float interpolateGain(float alev0, float alev1, int iloc) {
        float a0 = (float) (Math.log((double) alev0) / Math.log(2.0));
        float a1 = (float) (Math.log((double) alev1) / Math.log(2.0));
        return (float) Math.pow(2.0, (double) (((float) (8 - iloc) * a0 + (float) iloc * a1) / 8.0F));
    }
}