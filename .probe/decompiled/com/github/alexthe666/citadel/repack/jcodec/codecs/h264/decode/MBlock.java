package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode;

import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.H264Const;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.H264Utils;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.MBType;
import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import java.util.Arrays;

public class MBlock {

    public int chromaPredictionMode;

    public int mbQPDelta;

    public int[] dc;

    public int[][][] ac;

    public boolean transform8x8Used;

    public int[] lumaModes;

    public int[] dc1;

    public int[] dc2;

    public int _cbp;

    public int mbType;

    public MBType curMbType;

    public MBlock.PB16x16 pb16x16;

    public MBlock.PB168x168 pb168x168;

    public MBlock.PB8x8 pb8x8 = new MBlock.PB8x8();

    public MBlock.IPCM ipcm;

    public int mbIdx;

    public boolean fieldDecoding;

    public MBType prevMbType;

    public int luma16x16Mode;

    public H264Utils.MvList x;

    public H264Const.PartPred[] partPreds;

    public boolean skipped;

    public int[] nCoeff;

    public MBlock(ColorSpace chromaFormat) {
        this.pb16x16 = new MBlock.PB16x16();
        this.pb168x168 = new MBlock.PB168x168();
        this.dc = new int[16];
        this.ac = new int[][][] { new int[16][64], new int[4][16], new int[4][16] };
        this.lumaModes = new int[16];
        this.nCoeff = new int[16];
        this.dc1 = new int[16 >> chromaFormat.compWidth[1] >> chromaFormat.compHeight[1]];
        this.dc2 = new int[16 >> chromaFormat.compWidth[2] >> chromaFormat.compHeight[2]];
        this.ipcm = new MBlock.IPCM(chromaFormat);
        this.x = new H264Utils.MvList(16);
        this.partPreds = new H264Const.PartPred[4];
    }

    public int cbpLuma() {
        return this._cbp & 15;
    }

    public int cbpChroma() {
        return this._cbp >> 4;
    }

    public void cbp(int cbpLuma, int cbpChroma) {
        this._cbp = cbpLuma & 15 | cbpChroma << 4;
    }

    public void clear() {
        this.chromaPredictionMode = 0;
        this.mbQPDelta = 0;
        Arrays.fill(this.dc, 0);
        for (int i = 0; i < this.ac.length; i++) {
            int[][] aci = this.ac[i];
            for (int j = 0; j < aci.length; j++) {
                Arrays.fill(aci[j], 0);
            }
        }
        this.transform8x8Used = false;
        Arrays.fill(this.lumaModes, 0);
        Arrays.fill(this.dc1, 0);
        Arrays.fill(this.dc2, 0);
        Arrays.fill(this.nCoeff, 0);
        this._cbp = 0;
        this.mbType = 0;
        this.pb16x16.clean();
        this.pb168x168.clean();
        this.pb8x8.clean();
        if (this.curMbType == MBType.I_PCM) {
            this.ipcm.clean();
        }
        this.mbIdx = 0;
        this.fieldDecoding = false;
        this.prevMbType = null;
        this.luma16x16Mode = 0;
        this.skipped = false;
        this.curMbType = null;
        this.x.clear();
        this.partPreds[0] = this.partPreds[1] = this.partPreds[2] = this.partPreds[3] = null;
    }

    static class IPCM {

        public int[] samplesLuma = new int[256];

        public int[] samplesChroma;

        public IPCM(ColorSpace chromaFormat) {
            int MbWidthC = 16 >> chromaFormat.compWidth[1];
            int MbHeightC = 16 >> chromaFormat.compHeight[1];
            this.samplesChroma = new int[2 * MbWidthC * MbHeightC];
        }

        public void clean() {
            Arrays.fill(this.samplesLuma, 0);
            Arrays.fill(this.samplesChroma, 0);
        }
    }

    static class PB168x168 {

        public int[] refIdx1 = new int[2];

        public int[] refIdx2 = new int[2];

        public int[] mvdX1 = new int[2];

        public int[] mvdY1 = new int[2];

        public int[] mvdX2 = new int[2];

        public int[] mvdY2 = new int[2];

        public PB168x168() {
        }

        public void clean() {
            this.refIdx1[0] = this.refIdx1[1] = 0;
            this.refIdx2[0] = this.refIdx2[1] = 0;
            this.mvdX1[0] = this.mvdX1[1] = 0;
            this.mvdY1[0] = this.mvdY1[1] = 0;
            this.mvdX2[0] = this.mvdX2[1] = 0;
            this.mvdY2[0] = this.mvdY2[1] = 0;
        }
    }

    static class PB16x16 {

        public int[] refIdx = new int[2];

        public int[] mvdX = new int[2];

        public int[] mvdY = new int[2];

        public PB16x16() {
        }

        public void clean() {
            this.refIdx[0] = this.refIdx[1] = 0;
            this.mvdX[0] = this.mvdX[1] = 0;
            this.mvdY[0] = this.mvdY[1] = 0;
        }
    }

    static class PB8x8 {

        public int[][] refIdx = new int[2][4];

        public int[] subMbTypes = new int[4];

        public int[][] mvdX1 = new int[2][4];

        public int[][] mvdY1 = new int[2][4];

        public int[][] mvdX2 = new int[2][4];

        public int[][] mvdY2 = new int[2][4];

        public int[][] mvdX3 = new int[2][4];

        public int[][] mvdY3 = new int[2][4];

        public int[][] mvdX4 = new int[2][4];

        public int[][] mvdY4 = new int[2][4];

        public PB8x8() {
        }

        public void clean() {
            this.mvdX1[0][0] = this.mvdX1[0][1] = this.mvdX1[0][2] = this.mvdX1[0][3] = 0;
            this.mvdX2[0][0] = this.mvdX2[0][1] = this.mvdX2[0][2] = this.mvdX2[0][3] = 0;
            this.mvdX3[0][0] = this.mvdX3[0][1] = this.mvdX3[0][2] = this.mvdX3[0][3] = 0;
            this.mvdX4[0][0] = this.mvdX4[0][1] = this.mvdX4[0][2] = this.mvdX4[0][3] = 0;
            this.mvdY1[0][0] = this.mvdY1[0][1] = this.mvdY1[0][2] = this.mvdY1[0][3] = 0;
            this.mvdY2[0][0] = this.mvdY2[0][1] = this.mvdY2[0][2] = this.mvdY2[0][3] = 0;
            this.mvdY3[0][0] = this.mvdY3[0][1] = this.mvdY3[0][2] = this.mvdY3[0][3] = 0;
            this.mvdY4[0][0] = this.mvdY4[0][1] = this.mvdY4[0][2] = this.mvdY4[0][3] = 0;
            this.mvdX1[1][0] = this.mvdX1[1][1] = this.mvdX1[1][2] = this.mvdX1[1][3] = 0;
            this.mvdX2[1][0] = this.mvdX2[1][1] = this.mvdX2[1][2] = this.mvdX2[1][3] = 0;
            this.mvdX3[1][0] = this.mvdX3[1][1] = this.mvdX3[1][2] = this.mvdX3[1][3] = 0;
            this.mvdX4[1][0] = this.mvdX4[1][1] = this.mvdX4[1][2] = this.mvdX4[1][3] = 0;
            this.mvdY1[1][0] = this.mvdY1[1][1] = this.mvdY1[1][2] = this.mvdY1[1][3] = 0;
            this.mvdY2[1][0] = this.mvdY2[1][1] = this.mvdY2[1][2] = this.mvdY2[1][3] = 0;
            this.mvdY3[1][0] = this.mvdY3[1][1] = this.mvdY3[1][2] = this.mvdY3[1][3] = 0;
            this.mvdY4[1][0] = this.mvdY4[1][1] = this.mvdY4[1][2] = this.mvdY4[1][3] = 0;
            this.subMbTypes[0] = this.subMbTypes[1] = this.subMbTypes[2] = this.subMbTypes[3] = 0;
            this.refIdx[0][0] = this.refIdx[0][1] = this.refIdx[0][2] = this.refIdx[0][3] = 0;
            this.refIdx[1][0] = this.refIdx[1][1] = this.refIdx[1][2] = this.refIdx[1][3] = 0;
        }
    }
}