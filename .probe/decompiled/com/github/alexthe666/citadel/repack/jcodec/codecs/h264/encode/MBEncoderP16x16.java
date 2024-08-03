package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.encode;

import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.H264Const;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode.BlockInterpolator;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode.CoeffTransformer;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.CAVLC;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.MBType;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.SeqParameterSet;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.write.CAVLCWriter;
import com.github.alexthe666.citadel.repack.jcodec.common.SaveRestore;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitWriter;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;
import java.util.Arrays;

public class MBEncoderP16x16 implements SaveRestore {

    private CAVLC[] cavlc;

    private SeqParameterSet sps;

    private Picture ref;

    private MotionEstimator me;

    private int[] mvTopX;

    private int[] mvTopY;

    private int mvLeftX;

    private int mvLeftY;

    private int mvTopLeftX;

    private int mvTopLeftY;

    private int[] mvTopXSave;

    private int[] mvTopYSave;

    private int mvLeftXSave;

    private int mvLeftYSave;

    private int mvTopLeftXSave;

    private int mvTopLeftYSave;

    private BlockInterpolator interpolator;

    public MBEncoderP16x16(SeqParameterSet sps, Picture ref, CAVLC[] cavlc, MotionEstimator me) {
        this.sps = sps;
        this.cavlc = cavlc;
        this.ref = ref;
        this.me = me;
        this.mvTopX = new int[sps.picWidthInMbsMinus1 + 1];
        this.mvTopY = new int[sps.picWidthInMbsMinus1 + 1];
        this.mvTopXSave = new int[sps.picWidthInMbsMinus1 + 1];
        this.mvTopYSave = new int[sps.picWidthInMbsMinus1 + 1];
        this.interpolator = new BlockInterpolator();
    }

    @Override
    public void save() {
        for (int i = 0; i < this.cavlc.length; i++) {
            this.cavlc[i].save();
        }
        System.arraycopy(this.mvTopX, 0, this.mvTopXSave, 0, this.mvTopX.length);
        System.arraycopy(this.mvTopY, 0, this.mvTopYSave, 0, this.mvTopY.length);
        this.mvLeftXSave = this.mvLeftX;
        this.mvLeftYSave = this.mvLeftY;
        this.mvTopLeftXSave = this.mvTopLeftX;
        this.mvTopLeftYSave = this.mvTopLeftY;
    }

    @Override
    public void restore() {
        for (int i = 0; i < this.cavlc.length; i++) {
            this.cavlc[i].restore();
        }
        int[] tmp = this.mvTopX;
        this.mvTopX = this.mvTopXSave;
        this.mvTopXSave = tmp;
        tmp = this.mvTopY;
        this.mvTopY = this.mvTopYSave;
        this.mvTopYSave = tmp;
        this.mvLeftX = this.mvLeftXSave;
        this.mvLeftY = this.mvLeftYSave;
        this.mvTopLeftX = this.mvTopLeftXSave;
        this.mvTopLeftY = this.mvTopLeftYSave;
    }

    public void encodeMacroblock(Picture pic, int mbX, int mbY, BitWriter out, EncodedMB outMB, EncodedMB leftOutMB, EncodedMB topOutMB, int qp, int qpDelta) {
        if (this.sps.numRefFrames > 1) {
            int refIdx = this.decideRef();
            CAVLCWriter.writeTE(out, refIdx, this.sps.numRefFrames - 1);
        }
        boolean trAvb = mbY > 0 && mbX < this.sps.picWidthInMbsMinus1;
        boolean tlAvb = mbX > 0 && mbY > 0;
        int mvpx = this.median(this.mvLeftX, this.mvTopX[mbX], trAvb ? this.mvTopX[mbX + 1] : 0, tlAvb ? this.mvTopLeftX : 0, mbX > 0, mbY > 0, trAvb, tlAvb);
        int mvpy = this.median(this.mvLeftY, this.mvTopY[mbX], trAvb ? this.mvTopY[mbX + 1] : 0, tlAvb ? this.mvTopLeftY : 0, mbX > 0, mbY > 0, trAvb, tlAvb);
        int[] mv = this.mvEstimate(pic, mbX, mbY, mvpx, mvpy);
        this.mvTopLeftX = this.mvTopX[mbX];
        this.mvTopLeftY = this.mvTopY[mbX];
        this.mvTopX[mbX] = mv[0];
        this.mvTopY[mbX] = mv[1];
        this.mvLeftX = mv[0];
        this.mvLeftY = mv[1];
        CAVLCWriter.writeSE(out, mv[0] - mvpx);
        CAVLCWriter.writeSE(out, mv[1] - mvpy);
        Picture mbRef = Picture.create(16, 16, this.sps.chromaFormatIdc);
        int[][] mb = new int[][] { new int[256], new int[64], new int[64] };
        this.interpolator.getBlockLuma(this.ref, mbRef, 0, (mbX << 6) + mv[0], (mbY << 6) + mv[1], 16, 16);
        BlockInterpolator.getBlockChroma(this.ref.getPlaneData(1), this.ref.getPlaneWidth(1), this.ref.getPlaneHeight(1), mbRef.getPlaneData(1), 0, mbRef.getPlaneWidth(1), (mbX << 6) + mv[0], (mbY << 6) + mv[1], 8, 8);
        BlockInterpolator.getBlockChroma(this.ref.getPlaneData(2), this.ref.getPlaneWidth(2), this.ref.getPlaneHeight(2), mbRef.getPlaneData(2), 0, mbRef.getPlaneWidth(2), (mbX << 6) + mv[0], (mbY << 6) + mv[1], 8, 8);
        MBEncoderHelper.takeSubtract(pic.getPlaneData(0), pic.getPlaneWidth(0), pic.getPlaneHeight(0), mbX << 4, mbY << 4, mb[0], mbRef.getPlaneData(0), 16, 16);
        MBEncoderHelper.takeSubtract(pic.getPlaneData(1), pic.getPlaneWidth(1), pic.getPlaneHeight(1), mbX << 3, mbY << 3, mb[1], mbRef.getPlaneData(1), 8, 8);
        MBEncoderHelper.takeSubtract(pic.getPlaneData(2), pic.getPlaneWidth(2), pic.getPlaneHeight(2), mbX << 3, mbY << 3, mb[2], mbRef.getPlaneData(2), 8, 8);
        int codedBlockPattern = this.getCodedBlockPattern();
        CAVLCWriter.writeUE(out, H264Const.CODED_BLOCK_PATTERN_INTER_COLOR_INV[codedBlockPattern]);
        CAVLCWriter.writeSE(out, qpDelta);
        this.luma(pic, mb[0], mbX, mbY, out, qp, outMB.getNc());
        this.chroma(pic, mb[1], mb[2], mbX, mbY, out, qp);
        MBEncoderHelper.putBlk(outMB.getPixels().getPlaneData(0), mb[0], mbRef.getPlaneData(0), 4, 0, 0, 16, 16);
        MBEncoderHelper.putBlk(outMB.getPixels().getPlaneData(1), mb[1], mbRef.getPlaneData(1), 3, 0, 0, 8, 8);
        MBEncoderHelper.putBlk(outMB.getPixels().getPlaneData(2), mb[2], mbRef.getPlaneData(2), 3, 0, 0, 8, 8);
        Arrays.fill(outMB.getMx(), mv[0]);
        Arrays.fill(outMB.getMy(), mv[1]);
        outMB.setType(MBType.P_16x16);
        outMB.setQp(qp);
        new MBDeblocker().deblockMBP(outMB, leftOutMB, topOutMB);
    }

    public int median(int a, int b, int c, int d, boolean aAvb, boolean bAvb, boolean cAvb, boolean dAvb) {
        if (!cAvb) {
            c = d;
            cAvb = dAvb;
        }
        if (aAvb && !bAvb && !cAvb) {
            c = a;
            b = a;
            cAvb = aAvb;
            bAvb = aAvb;
        }
        a = aAvb ? a : 0;
        b = bAvb ? b : 0;
        c = cAvb ? c : 0;
        return a + b + c - Math.min(Math.min(a, b), c) - Math.max(Math.max(a, b), c);
    }

    private int getCodedBlockPattern() {
        return 47;
    }

    private int[] mvEstimate(Picture pic, int mbX, int mbY, int mvpx, int mvpy) {
        byte[] patch = new byte[256];
        MBEncoderHelper.take(pic.getPlaneData(0), pic.getPlaneWidth(0), pic.getPlaneHeight(0), mbX << 4, mbY << 4, patch, 16, 16);
        return this.me.estimate(this.ref, patch, mbX, mbY, mvpx, mvpy);
    }

    private int decideRef() {
        return 0;
    }

    private void luma(Picture pic, int[] pix, int mbX, int mbY, BitWriter out, int qp, int[] nc) {
        int[][] ac = new int[16][16];
        for (int i = 0; i < ac.length; i++) {
            for (int j = 0; j < H264Const.PIX_MAP_SPLIT_4x4[i].length; j++) {
                ac[i][j] = pix[H264Const.PIX_MAP_SPLIT_4x4[i][j]];
            }
            CoeffTransformer.fdct4x4(ac[i]);
        }
        this.writeAC(0, mbX, mbY, out, mbX << 2, mbY << 2, ac, qp);
        for (int i = 0; i < ac.length; i++) {
            CoeffTransformer.dequantizeAC(ac[i], qp, null);
            CoeffTransformer.idct4x4(ac[i]);
            for (int j = 0; j < H264Const.PIX_MAP_SPLIT_4x4[i].length; j++) {
                pix[H264Const.PIX_MAP_SPLIT_4x4[i][j]] = ac[i][j];
            }
        }
    }

    private void chroma(Picture pic, int[] pix1, int[] pix2, int mbX, int mbY, BitWriter out, int qp) {
        int[][] ac1 = new int[4][16];
        int[][] ac2 = new int[4][16];
        for (int i = 0; i < ac1.length; i++) {
            for (int j = 0; j < H264Const.PIX_MAP_SPLIT_2x2[i].length; j++) {
                ac1[i][j] = pix1[H264Const.PIX_MAP_SPLIT_2x2[i][j]];
            }
        }
        for (int i = 0; i < ac2.length; i++) {
            for (int j = 0; j < H264Const.PIX_MAP_SPLIT_2x2[i].length; j++) {
                ac2[i][j] = pix2[H264Const.PIX_MAP_SPLIT_2x2[i][j]];
            }
        }
        MBEncoderI16x16.chromaResidual(pic, mbX, mbY, out, qp, ac1, ac2, this.cavlc[1], this.cavlc[2], MBType.P_16x16, MBType.P_16x16);
        for (int i = 0; i < ac1.length; i++) {
            for (int j = 0; j < H264Const.PIX_MAP_SPLIT_2x2[i].length; j++) {
                pix1[H264Const.PIX_MAP_SPLIT_2x2[i][j]] = ac1[i][j];
            }
        }
        for (int i = 0; i < ac2.length; i++) {
            for (int j = 0; j < H264Const.PIX_MAP_SPLIT_2x2[i].length; j++) {
                pix2[H264Const.PIX_MAP_SPLIT_2x2[i][j]] = ac2[i][j];
            }
        }
    }

    private void writeAC(int comp, int mbX, int mbY, BitWriter out, int mbLeftBlk, int mbTopBlk, int[][] ac, int qp) {
        for (int i = 0; i < ac.length; i++) {
            int blkI = H264Const.BLK_INV_MAP[i];
            CoeffTransformer.quantizeAC(ac[blkI], qp);
            this.cavlc[comp].writeACBlock(out, mbLeftBlk + H264Const.MB_BLK_OFF_LEFT[i], mbTopBlk + H264Const.MB_BLK_OFF_TOP[i], MBType.P_16x16, MBType.P_16x16, ac[blkI], H264Const.totalZeros16, 0, 16, CoeffTransformer.zigzag4x4);
        }
    }
}