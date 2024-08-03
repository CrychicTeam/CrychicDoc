package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode;

import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.H264Const;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.H264Utils;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode.aso.Mapper;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.Frame;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.SliceHeader;
import com.github.alexthe666.citadel.repack.jcodec.common.logging.Logger;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.MathUtil;

public class MBlockDecoderBDirect extends MBlockDecoderBase {

    private Mapper mapper;

    public MBlockDecoderBDirect(Mapper mapper, SliceHeader sh, DeblockerInput di, int poc, DecoderState decoderState) {
        super(sh, di, poc, decoderState);
        this.mapper = mapper;
    }

    public void decode(MBlock mBlock, Picture mb, Frame[][] references) {
        int mbX = this.mapper.getMbX(mBlock.mbIdx);
        int mbY = this.mapper.getMbY(mBlock.mbIdx);
        boolean lAvb = this.mapper.leftAvailable(mBlock.mbIdx);
        boolean tAvb = this.mapper.topAvailable(mBlock.mbIdx);
        int mbAddr = this.mapper.getAddress(mBlock.mbIdx);
        boolean tlAvb = this.mapper.topLeftAvailable(mBlock.mbIdx);
        boolean trAvb = this.mapper.topRightAvailable(mBlock.mbIdx);
        this.predictBDirect(references, mbX, mbY, lAvb, tAvb, tlAvb, trAvb, mBlock.x, mBlock.partPreds, mb, H264Const.identityMapping4);
        this.predictChromaInter(references, mBlock.x, mbX << 3, mbY << 3, 1, mb, mBlock.partPreds);
        this.predictChromaInter(references, mBlock.x, mbX << 3, mbY << 3, 2, mb, mBlock.partPreds);
        if (mBlock.cbpLuma() > 0 || mBlock.cbpChroma() > 0) {
            this.s.qp = (this.s.qp + mBlock.mbQPDelta + 52) % 52;
        }
        this.di.mbQps[0][mbAddr] = this.s.qp;
        this.residualLuma(mBlock, lAvb, tAvb, mbX, mbY);
        MBlockDecoderUtils.savePrediction8x8(this.s, mbX, mBlock.x);
        MBlockDecoderUtils.saveMvs(this.di, mBlock.x, mbX, mbY);
        int qp1 = calcQpChroma(this.s.qp, this.s.chromaQpOffset[0]);
        int qp2 = calcQpChroma(this.s.qp, this.s.chromaQpOffset[1]);
        this.decodeChromaResidual(mBlock, lAvb, tAvb, mbX, mbY, qp1, qp2);
        this.di.mbQps[1][mbAddr] = qp1;
        this.di.mbQps[2][mbAddr] = qp2;
        MBlockDecoderUtils.mergeResidual(mb, mBlock.ac, mBlock.transform8x8Used ? H264Const.COMP_BLOCK_8x8_LUT : H264Const.COMP_BLOCK_4x4_LUT, mBlock.transform8x8Used ? H264Const.COMP_POS_8x8_LUT : H264Const.COMP_POS_4x4_LUT);
        MBlockDecoderUtils.collectPredictors(this.s, mb, mbX);
        this.di.mbTypes[mbAddr] = mBlock.curMbType;
        this.di.tr8x8Used[mbAddr] = mBlock.transform8x8Used;
    }

    public void predictBDirect(Frame[][] refs, int mbX, int mbY, boolean lAvb, boolean tAvb, boolean tlAvb, boolean trAvb, H264Utils.MvList x, H264Const.PartPred[] pp, Picture mb, int[] blocks) {
        if (this.sh.directSpatialMvPredFlag) {
            this.predictBSpatialDirect(refs, mbX, mbY, lAvb, tAvb, tlAvb, trAvb, x, pp, mb, blocks);
        } else {
            this.predictBTemporalDirect(refs, mbX, mbY, lAvb, tAvb, tlAvb, trAvb, x, pp, mb, blocks);
        }
    }

    private void predictBTemporalDirect(Frame[][] refs, int mbX, int mbY, boolean lAvb, boolean tAvb, boolean tlAvb, boolean trAvb, H264Utils.MvList x, H264Const.PartPred[] pp, Picture mb, int[] blocks8x8) {
        for (int i = 0; i < blocks8x8.length; i++) {
            int blk8x8 = blocks8x8[i];
            int blk4x4_0 = H264Const.BLK8x8_BLOCKS[blk8x8][0];
            pp[blk8x8] = H264Const.PartPred.Bi;
            if (!this.sh.sps.direct8x8InferenceFlag) {
                int[] js = H264Const.BLK8x8_BLOCKS[blk8x8];
                for (int j = 0; j < js.length; j++) {
                    int blk4x4 = js[j];
                    this.predTemp4x4(refs, mbX, mbY, x, blk4x4);
                    int blkIndX = blk4x4 & 3;
                    int blkIndY = blk4x4 >> 2;
                    MBlockDecoderUtils.debugPrint("DIRECT_4x4 [%d, %d]: (%d,%d,%d), (%d,%d,%d)", blkIndY, blkIndX, x.mv0X(blk4x4), x.mv0Y(blk4x4), x.mv0R(blk4x4), x.mv1X(blk4x4), x.mv1Y(blk4x4), x.mv1R(blk4x4));
                    int blkPredX = (mbX << 6) + (blkIndX << 4);
                    int blkPredY = (mbY << 6) + (blkIndY << 4);
                    this.interpolator.getBlockLuma(refs[0][x.mv0R(blk4x4)], this.mbb[0], H264Const.BLK_4x4_MB_OFF_LUMA[blk4x4], blkPredX + x.mv0X(blk4x4), blkPredY + x.mv0Y(blk4x4), 4, 4);
                    this.interpolator.getBlockLuma(refs[1][0], this.mbb[1], H264Const.BLK_4x4_MB_OFF_LUMA[blk4x4], blkPredX + x.mv1X(blk4x4), blkPredY + x.mv1Y(blk4x4), 4, 4);
                }
            } else {
                int blk4x4Pred = H264Const.BLK_INV_MAP[blk8x8 * 5];
                this.predTemp4x4(refs, mbX, mbY, x, blk4x4Pred);
                this.propagatePred(x, blk8x8, blk4x4Pred);
                int blkIndX = blk4x4_0 & 3;
                int blkIndY = blk4x4_0 >> 2;
                MBlockDecoderUtils.debugPrint("DIRECT_8x8 [%d, %d]: (%d,%d,%d), (%d,%d)", blkIndY, blkIndX, x.mv0X(blk4x4_0), x.mv0Y(blk4x4_0), x.mv0R(blk4x4_0), x.mv1X(blk4x4_0), x.mv1Y(blk4x4_0), x.mv1R(blk4x4_0));
                int blkPredX = (mbX << 6) + (blkIndX << 4);
                int blkPredY = (mbY << 6) + (blkIndY << 4);
                this.interpolator.getBlockLuma(refs[0][x.mv0R(blk4x4_0)], this.mbb[0], H264Const.BLK_4x4_MB_OFF_LUMA[blk4x4_0], blkPredX + x.mv0X(blk4x4_0), blkPredY + x.mv0Y(blk4x4_0), 8, 8);
                this.interpolator.getBlockLuma(refs[1][0], this.mbb[1], H264Const.BLK_4x4_MB_OFF_LUMA[blk4x4_0], blkPredX + x.mv1X(blk4x4_0), blkPredY + x.mv1Y(blk4x4_0), 8, 8);
            }
            PredictionMerger.mergePrediction(this.sh, x.mv0R(blk4x4_0), x.mv1R(blk4x4_0), H264Const.PartPred.Bi, 0, this.mbb[0].getPlaneData(0), this.mbb[1].getPlaneData(0), H264Const.BLK_4x4_MB_OFF_LUMA[blk4x4_0], 16, 8, 8, mb.getPlaneData(0), refs, this.poc);
        }
    }

    private void predTemp4x4(Frame[][] refs, int mbX, int mbY, H264Utils.MvList x, int blk4x4) {
        int mbWidth = this.sh.sps.picWidthInMbsMinus1 + 1;
        Frame picCol = refs[1][0];
        int blkIndX = blk4x4 & 3;
        int blkIndY = blk4x4 >> 2;
        int blkPosX = (mbX << 2) + blkIndX;
        int blkPosY = (mbY << 2) + blkIndY;
        int mvCol = picCol.getMvs().getMv(blkPosX, blkPosY, 0);
        Frame refL0;
        int refIdxL0;
        if (H264Utils.Mv.mvRef(mvCol) == -1) {
            mvCol = picCol.getMvs().getMv(blkPosX, blkPosY, 1);
            if (H264Utils.Mv.mvRef(mvCol) == -1) {
                refIdxL0 = 0;
                refL0 = refs[0][0];
            } else {
                refL0 = picCol.getRefsUsed()[mbY * mbWidth + mbX][1][H264Utils.Mv.mvRef(mvCol)];
                refIdxL0 = this.findPic(refs[0], refL0);
            }
        } else {
            refL0 = picCol.getRefsUsed()[mbY * mbWidth + mbX][0][H264Utils.Mv.mvRef(mvCol)];
            refIdxL0 = this.findPic(refs[0], refL0);
        }
        int td = MathUtil.clip(picCol.getPOC() - refL0.getPOC(), -128, 127);
        if (refL0.isShortTerm() && td != 0) {
            int tb = MathUtil.clip(this.poc - refL0.getPOC(), -128, 127);
            int tx = (16384 + Math.abs(td / 2)) / td;
            int dsf = MathUtil.clip(tb * tx + 32 >> 6, -1024, 1023);
            x.setPair(blk4x4, H264Utils.Mv.packMv(dsf * H264Utils.Mv.mvX(mvCol) + 128 >> 8, dsf * H264Utils.Mv.mvY(mvCol) + 128 >> 8, refIdxL0), H264Utils.Mv.packMv(x.mv0X(blk4x4) - H264Utils.Mv.mvX(mvCol), x.mv0Y(blk4x4) - H264Utils.Mv.mvY(mvCol), 0));
        } else {
            x.setPair(blk4x4, H264Utils.Mv.packMv(H264Utils.Mv.mvX(mvCol), H264Utils.Mv.mvY(mvCol), refIdxL0), 0);
        }
    }

    private int findPic(Frame[] frames, Frame refL0) {
        for (int i = 0; i < frames.length; i++) {
            if (frames[i] == refL0) {
                return i;
            }
        }
        Logger.error("RefPicList0 shall contain refPicCol");
        return 0;
    }

    private void predictBSpatialDirect(Frame[][] refs, int mbX, int mbY, boolean lAvb, boolean tAvb, boolean tlAvb, boolean trAvb, H264Utils.MvList x, H264Const.PartPred[] pp, Picture mb, int[] blocks8x8) {
        int a0 = this.s.mvLeft.getMv(0, 0);
        int a1 = this.s.mvLeft.getMv(0, 1);
        int b0 = this.s.mvTop.getMv(mbX << 2, 0);
        int b1 = this.s.mvTop.getMv(mbX << 2, 1);
        int c0 = this.s.mvTop.getMv((mbX << 2) + 4, 0);
        int c1 = this.s.mvTop.getMv((mbX << 2) + 4, 1);
        int d0 = this.s.mvTopLeft.getMv(0, 0);
        int d1 = this.s.mvTopLeft.getMv(0, 1);
        int refIdxL0 = this.calcRef(a0, b0, c0, d0, lAvb, tAvb, tlAvb, trAvb, mbX);
        int refIdxL1 = this.calcRef(a1, b1, c1, d1, lAvb, tAvb, tlAvb, trAvb, mbX);
        if (refIdxL0 < 0 && refIdxL1 < 0) {
            for (int i = 0; i < blocks8x8.length; i++) {
                int blk8x8 = blocks8x8[i];
                int[] js = H264Const.BLK8x8_BLOCKS[blk8x8];
                for (int j = 0; j < js.length; j++) {
                    int blk4x4 = js[j];
                    x.setPair(blk4x4, 0, 0);
                }
                pp[blk8x8] = H264Const.PartPred.Bi;
                int blkOffX = (blk8x8 & 1) << 5;
                int blkOffY = blk8x8 >> 1 << 5;
                this.interpolator.getBlockLuma(refs[0][0], this.mbb[0], H264Const.BLK_8x8_MB_OFF_LUMA[blk8x8], (mbX << 6) + blkOffX, (mbY << 6) + blkOffY, 8, 8);
                this.interpolator.getBlockLuma(refs[1][0], this.mbb[1], H264Const.BLK_8x8_MB_OFF_LUMA[blk8x8], (mbX << 6) + blkOffX, (mbY << 6) + blkOffY, 8, 8);
                PredictionMerger.mergePrediction(this.sh, 0, 0, H264Const.PartPred.Bi, 0, this.mbb[0].getPlaneData(0), this.mbb[1].getPlaneData(0), H264Const.BLK_8x8_MB_OFF_LUMA[blk8x8], 16, 8, 8, mb.getPlaneData(0), refs, this.poc);
                MBlockDecoderUtils.debugPrint("DIRECT_8x8 [%d, %d]: (0,0,0), (0,0,0)", blk8x8 & 2, blk8x8 << 1 & 2);
            }
        } else {
            int mvX0 = MBlockDecoderUtils.calcMVPredictionMedian(a0, b0, c0, d0, lAvb, tAvb, trAvb, tlAvb, refIdxL0, 0);
            int mvY0 = MBlockDecoderUtils.calcMVPredictionMedian(a0, b0, c0, d0, lAvb, tAvb, trAvb, tlAvb, refIdxL0, 1);
            int mvX1 = MBlockDecoderUtils.calcMVPredictionMedian(a1, b1, c1, d1, lAvb, tAvb, trAvb, tlAvb, refIdxL1, 0);
            int mvY1 = MBlockDecoderUtils.calcMVPredictionMedian(a1, b1, c1, d1, lAvb, tAvb, trAvb, tlAvb, refIdxL1, 1);
            Frame col = refs[1][0];
            H264Const.PartPred partPred = refIdxL0 >= 0 && refIdxL1 >= 0 ? H264Const.PartPred.Bi : (refIdxL0 >= 0 ? H264Const.PartPred.L0 : H264Const.PartPred.L1);
            for (int i = 0; i < blocks8x8.length; i++) {
                int blk8x8 = blocks8x8[i];
                int blk4x4_0 = H264Const.BLK8x8_BLOCKS[blk8x8][0];
                if (!this.sh.sps.direct8x8InferenceFlag) {
                    int[] js = H264Const.BLK8x8_BLOCKS[blk8x8];
                    for (int j = 0; j < js.length; j++) {
                        int blk4x4 = js[j];
                        this.pred4x4(mbX, mbY, x, pp, refIdxL0, refIdxL1, mvX0, mvY0, mvX1, mvY1, col, partPred, blk4x4);
                        int blkIndX = blk4x4 & 3;
                        int blkIndY = blk4x4 >> 2;
                        MBlockDecoderUtils.debugPrint("DIRECT_4x4 [%d, %d]: (%d,%d,%d), (%d,%d," + refIdxL1 + ")", blkIndY, blkIndX, x.mv0X(blk4x4), x.mv0Y(blk4x4), refIdxL0, x.mv1X(blk4x4), x.mv1Y(blk4x4));
                        int blkPredX = (mbX << 6) + (blkIndX << 4);
                        int blkPredY = (mbY << 6) + (blkIndY << 4);
                        if (refIdxL0 >= 0) {
                            this.interpolator.getBlockLuma(refs[0][refIdxL0], this.mbb[0], H264Const.BLK_4x4_MB_OFF_LUMA[blk4x4], blkPredX + x.mv0X(blk4x4), blkPredY + x.mv0Y(blk4x4), 4, 4);
                        }
                        if (refIdxL1 >= 0) {
                            this.interpolator.getBlockLuma(refs[1][refIdxL1], this.mbb[1], H264Const.BLK_4x4_MB_OFF_LUMA[blk4x4], blkPredX + x.mv1X(blk4x4), blkPredY + x.mv1Y(blk4x4), 4, 4);
                        }
                    }
                } else {
                    int blk4x4Pred = H264Const.BLK_INV_MAP[blk8x8 * 5];
                    this.pred4x4(mbX, mbY, x, pp, refIdxL0, refIdxL1, mvX0, mvY0, mvX1, mvY1, col, partPred, blk4x4Pred);
                    this.propagatePred(x, blk8x8, blk4x4Pred);
                    int blkIndXx = blk4x4_0 & 3;
                    int blkIndYx = blk4x4_0 >> 2;
                    MBlockDecoderUtils.debugPrint("DIRECT_8x8 [%d, %d]: (%d,%d,%d), (%d,%d,%d)", blkIndYx, blkIndXx, x.mv0X(blk4x4_0), x.mv0Y(blk4x4_0), refIdxL0, x.mv1X(blk4x4_0), x.mv1Y(blk4x4_0), refIdxL1);
                    int blkPredXx = (mbX << 6) + (blkIndXx << 4);
                    int blkPredYx = (mbY << 6) + (blkIndYx << 4);
                    if (refIdxL0 >= 0) {
                        this.interpolator.getBlockLuma(refs[0][refIdxL0], this.mbb[0], H264Const.BLK_4x4_MB_OFF_LUMA[blk4x4_0], blkPredXx + x.mv0X(blk4x4_0), blkPredYx + x.mv0Y(blk4x4_0), 8, 8);
                    }
                    if (refIdxL1 >= 0) {
                        this.interpolator.getBlockLuma(refs[1][refIdxL1], this.mbb[1], H264Const.BLK_4x4_MB_OFF_LUMA[blk4x4_0], blkPredXx + x.mv1X(blk4x4_0), blkPredYx + x.mv1Y(blk4x4_0), 8, 8);
                    }
                }
                PredictionMerger.mergePrediction(this.sh, x.mv0R(blk4x4_0), x.mv1R(blk4x4_0), refIdxL0 >= 0 ? (refIdxL1 >= 0 ? H264Const.PartPred.Bi : H264Const.PartPred.L0) : H264Const.PartPred.L1, 0, this.mbb[0].getPlaneData(0), this.mbb[1].getPlaneData(0), H264Const.BLK_4x4_MB_OFF_LUMA[blk4x4_0], 16, 8, 8, mb.getPlaneData(0), refs, this.poc);
            }
        }
    }

    private int calcRef(int a0, int b0, int c0, int d0, boolean lAvb, boolean tAvb, boolean tlAvb, boolean trAvb, int mbX) {
        return this.minPos(this.minPos(lAvb ? H264Utils.Mv.mvRef(a0) : -1, tAvb ? H264Utils.Mv.mvRef(b0) : -1), trAvb ? H264Utils.Mv.mvRef(c0) : (tlAvb ? H264Utils.Mv.mvRef(d0) : -1));
    }

    private void propagatePred(H264Utils.MvList x, int blk8x8, int blk4x4Pred) {
        int b0 = H264Const.BLK8x8_BLOCKS[blk8x8][0];
        int b1 = H264Const.BLK8x8_BLOCKS[blk8x8][1];
        int b2 = H264Const.BLK8x8_BLOCKS[blk8x8][2];
        int b3 = H264Const.BLK8x8_BLOCKS[blk8x8][3];
        x.copyPair(b0, x, blk4x4Pred);
        x.copyPair(b1, x, blk4x4Pred);
        x.copyPair(b2, x, blk4x4Pred);
        x.copyPair(b3, x, blk4x4Pred);
    }

    private void pred4x4(int mbX, int mbY, H264Utils.MvList x, H264Const.PartPred[] pp, int refL0, int refL1, int mvX0, int mvY0, int mvX1, int mvY1, Frame col, H264Const.PartPred partPred, int blk4x4) {
        int blkIndX = blk4x4 & 3;
        int blkIndY = blk4x4 >> 2;
        int blkPosX = (mbX << 2) + blkIndX;
        int blkPosY = (mbY << 2) + blkIndY;
        int mvCol = col.getMvs().getMv(blkPosX, blkPosY, 0);
        if (H264Utils.Mv.mvRef(mvCol) == -1) {
            mvCol = col.getMvs().getMv(blkPosX, blkPosY, 1);
        }
        boolean colZero = col.isShortTerm() && H264Utils.Mv.mvRef(mvCol) == 0 && MathUtil.abs(H264Utils.Mv.mvX(mvCol)) >> 1 == 0 && MathUtil.abs(H264Utils.Mv.mvY(mvCol)) >> 1 == 0;
        int x0 = H264Utils.Mv.packMv(0, 0, refL0);
        int x1 = H264Utils.Mv.packMv(0, 0, refL1);
        if (refL0 > 0 || !colZero) {
            x0 = H264Utils.Mv.packMv(mvX0, mvY0, refL0);
        }
        if (refL1 > 0 || !colZero) {
            x1 = H264Utils.Mv.packMv(mvX1, mvY1, refL1);
        }
        x.setPair(blk4x4, x0, x1);
        pp[H264Const.BLK_8x8_IND[blk4x4]] = partPred;
    }

    private int minPos(int a, int b) {
        return a >= 0 && b >= 0 ? Math.min(a, b) : Math.max(a, b);
    }
}