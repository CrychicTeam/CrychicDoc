package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode;

import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.H264Const;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.H264Utils;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode.aso.Mapper;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.Frame;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.SliceHeader;
import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;

public class MBlockDecoderInter extends MBlockDecoderBase {

    private Mapper mapper;

    public MBlockDecoderInter(Mapper mapper, SliceHeader sh, DeblockerInput di, int poc, DecoderState decoderState) {
        super(sh, di, poc, decoderState);
        this.mapper = mapper;
    }

    public void decode16x16(MBlock mBlock, Picture mb, Frame[][] refs, H264Const.PartPred p0) {
        int mbX = this.mapper.getMbX(mBlock.mbIdx);
        int mbY = this.mapper.getMbY(mBlock.mbIdx);
        boolean leftAvailable = this.mapper.leftAvailable(mBlock.mbIdx);
        boolean topAvailable = this.mapper.topAvailable(mBlock.mbIdx);
        boolean topLeftAvailable = this.mapper.topLeftAvailable(mBlock.mbIdx);
        boolean topRightAvailable = this.mapper.topRightAvailable(mBlock.mbIdx);
        int address = this.mapper.getAddress(mBlock.mbIdx);
        int xx = mbX << 2;
        for (int list = 0; list < 2; list++) {
            this.predictInter16x16(mBlock, this.mbb[list], refs, mbX, mbY, leftAvailable, topAvailable, topLeftAvailable, topRightAvailable, mBlock.x, xx, list, p0);
        }
        PredictionMerger.mergePrediction(this.sh, mBlock.x.mv0R(0), mBlock.x.mv1R(0), p0, 0, this.mbb[0].getPlaneData(0), this.mbb[1].getPlaneData(0), 0, 16, 16, 16, mb.getPlaneData(0), refs, this.poc);
        mBlock.partPreds[0] = mBlock.partPreds[1] = mBlock.partPreds[2] = mBlock.partPreds[3] = p0;
        this.predictChromaInter(refs, mBlock.x, mbX << 3, mbY << 3, 1, mb, mBlock.partPreds);
        this.predictChromaInter(refs, mBlock.x, mbX << 3, mbY << 3, 2, mb, mBlock.partPreds);
        this.residualInter(mBlock, refs, leftAvailable, topAvailable, mbX, mbY, this.mapper.getAddress(mBlock.mbIdx));
        MBlockDecoderUtils.saveMvs(this.di, mBlock.x, mbX, mbY);
        MBlockDecoderUtils.mergeResidual(mb, mBlock.ac, mBlock.transform8x8Used ? H264Const.COMP_BLOCK_8x8_LUT : H264Const.COMP_BLOCK_4x4_LUT, mBlock.transform8x8Used ? H264Const.COMP_POS_8x8_LUT : H264Const.COMP_POS_4x4_LUT);
        MBlockDecoderUtils.collectPredictors(this.s, mb, mbX);
        this.di.mbTypes[address] = mBlock.curMbType;
    }

    private void predictInter8x16(MBlock mBlock, Picture mb, Picture[][] references, int mbX, int mbY, boolean leftAvailable, boolean topAvailable, boolean tlAvailable, boolean trAvailable, H264Utils.MvList x, int list, H264Const.PartPred p0, H264Const.PartPred p1) {
        int xx = mbX << 2;
        int mvX1 = 0;
        int mvY1 = 0;
        int r1 = -1;
        int mvX2 = 0;
        int mvY2 = 0;
        int r2 = -1;
        if (H264Const.usesList(p0, list)) {
            int mvpX1 = this.calcMVPrediction8x16Left(this.s.mvLeft.getMv(0, list), this.s.mvTop.getMv(mbX << 2, list), this.s.mvTop.getMv((mbX << 2) + 2, list), this.s.mvTopLeft.getMv(0, list), leftAvailable, topAvailable, topAvailable, tlAvailable, mBlock.pb168x168.refIdx1[list], 0);
            int mvpY1 = this.calcMVPrediction8x16Left(this.s.mvLeft.getMv(0, list), this.s.mvTop.getMv(mbX << 2, list), this.s.mvTop.getMv((mbX << 2) + 2, list), this.s.mvTopLeft.getMv(0, list), leftAvailable, topAvailable, topAvailable, tlAvailable, mBlock.pb168x168.refIdx1[list], 1);
            mvX1 = mBlock.pb168x168.mvdX1[list] + mvpX1;
            mvY1 = mBlock.pb168x168.mvdY1[list] + mvpY1;
            MBlockDecoderUtils.debugPrint("MVP: (%d, %d), MVD: (%d, %d), MV: (%d,%d,%d)", mvpX1, mvpY1, mBlock.pb168x168.mvdX1[list], mBlock.pb168x168.mvdY1[list], mvX1, mvY1, mBlock.pb168x168.refIdx1[list]);
            this.interpolator.getBlockLuma(references[list][mBlock.pb168x168.refIdx1[list]], mb, 0, (mbX << 6) + mvX1, (mbY << 6) + mvY1, 8, 16);
            r1 = mBlock.pb168x168.refIdx1[list];
        }
        int v1 = H264Utils.Mv.packMv(mvX1, mvY1, r1);
        if (H264Const.usesList(p1, list)) {
            int mvpX2 = this.calcMVPrediction8x16Right(v1, this.s.mvTop.getMv((mbX << 2) + 2, list), this.s.mvTop.getMv((mbX << 2) + 4, list), this.s.mvTop.getMv((mbX << 2) + 1, list), true, topAvailable, trAvailable, topAvailable, mBlock.pb168x168.refIdx2[list], 0);
            int mvpY2 = this.calcMVPrediction8x16Right(v1, this.s.mvTop.getMv((mbX << 2) + 2, list), this.s.mvTop.getMv((mbX << 2) + 4, list), this.s.mvTop.getMv((mbX << 2) + 1, list), true, topAvailable, trAvailable, topAvailable, mBlock.pb168x168.refIdx2[list], 1);
            mvX2 = mBlock.pb168x168.mvdX2[list] + mvpX2;
            mvY2 = mBlock.pb168x168.mvdY2[list] + mvpY2;
            MBlockDecoderUtils.debugPrint("MVP: (" + mvpX2 + ", " + mvpY2 + "), MVD: (" + mBlock.pb168x168.mvdX2[list] + ", " + mBlock.pb168x168.mvdY2[list] + "), MV: (" + mvX2 + "," + mvY2 + "," + mBlock.pb168x168.refIdx2[list] + ")");
            this.interpolator.getBlockLuma(references[list][mBlock.pb168x168.refIdx2[list]], mb, 8, (mbX << 6) + 32 + mvX2, (mbY << 6) + mvY2, 8, 16);
            r2 = mBlock.pb168x168.refIdx2[list];
        }
        int v2 = H264Utils.Mv.packMv(mvX2, mvY2, r2);
        this.s.mvTopLeft.setMv(0, list, this.s.mvTop.getMv(xx + 3, list));
        MBlockDecoderUtils.saveVect(this.s.mvTop, list, xx, xx + 2, v1);
        MBlockDecoderUtils.saveVect(this.s.mvTop, list, xx + 2, xx + 4, v2);
        MBlockDecoderUtils.saveVect(this.s.mvLeft, list, 0, 4, v2);
        for (int i = 0; i < 16; i += 4) {
            x.setMv(i, list, v1);
            x.setMv(i + 1, list, v1);
            x.setMv(i + 2, list, v2);
            x.setMv(i + 3, list, v2);
        }
    }

    private void predictInter16x8(MBlock mBlock, Picture mb, Picture[][] references, int mbX, int mbY, boolean leftAvailable, boolean topAvailable, boolean tlAvailable, boolean trAvailable, int xx, H264Utils.MvList x, H264Const.PartPred p0, H264Const.PartPred p1, int list) {
        int mvX1 = 0;
        int mvY1 = 0;
        int mvX2 = 0;
        int mvY2 = 0;
        int r1 = -1;
        int r2 = -1;
        if (H264Const.usesList(p0, list)) {
            int mvpX1 = this.calcMVPrediction16x8Top(this.s.mvLeft.getMv(0, list), this.s.mvTop.getMv(mbX << 2, list), this.s.mvTop.getMv((mbX << 2) + 4, list), this.s.mvTopLeft.getMv(0, list), leftAvailable, topAvailable, trAvailable, tlAvailable, mBlock.pb168x168.refIdx1[list], 0);
            int mvpY1 = this.calcMVPrediction16x8Top(this.s.mvLeft.getMv(0, list), this.s.mvTop.getMv(mbX << 2, list), this.s.mvTop.getMv((mbX << 2) + 4, list), this.s.mvTopLeft.getMv(0, list), leftAvailable, topAvailable, trAvailable, tlAvailable, mBlock.pb168x168.refIdx1[list], 1);
            mvX1 = mBlock.pb168x168.mvdX1[list] + mvpX1;
            mvY1 = mBlock.pb168x168.mvdY1[list] + mvpY1;
            MBlockDecoderUtils.debugPrint("MVP: (%d, %d), MVD: (%d, %d), MV: (%d,%d,%d)", mvpX1, mvpY1, mBlock.pb168x168.mvdX1[list], mBlock.pb168x168.mvdY1[list], mvX1, mvY1, mBlock.pb168x168.refIdx1[list]);
            this.interpolator.getBlockLuma(references[list][mBlock.pb168x168.refIdx1[list]], mb, 0, (mbX << 6) + mvX1, (mbY << 6) + mvY1, 16, 8);
            r1 = mBlock.pb168x168.refIdx1[list];
        }
        int v1 = H264Utils.Mv.packMv(mvX1, mvY1, r1);
        if (H264Const.usesList(p1, list)) {
            int mvpX2 = this.calcMVPrediction16x8Bottom(this.s.mvLeft.getMv(2, list), v1, MBlockDecoderUtils.NULL_VECTOR, this.s.mvLeft.getMv(1, list), leftAvailable, true, false, leftAvailable, mBlock.pb168x168.refIdx2[list], 0);
            int mvpY2 = this.calcMVPrediction16x8Bottom(this.s.mvLeft.getMv(2, list), v1, MBlockDecoderUtils.NULL_VECTOR, this.s.mvLeft.getMv(1, list), leftAvailable, true, false, leftAvailable, mBlock.pb168x168.refIdx2[list], 1);
            mvX2 = mBlock.pb168x168.mvdX2[list] + mvpX2;
            mvY2 = mBlock.pb168x168.mvdY2[list] + mvpY2;
            MBlockDecoderUtils.debugPrint("MVP: (%d, %d), MVD: (%d, %d), MV: (%d,%d,%d)", mvpX2, mvpY2, mBlock.pb168x168.mvdX2[list], mBlock.pb168x168.mvdY2[list], mvX2, mvY2, mBlock.pb168x168.refIdx2[list]);
            this.interpolator.getBlockLuma(references[list][mBlock.pb168x168.refIdx2[list]], mb, 128, (mbX << 6) + mvX2, (mbY << 6) + 32 + mvY2, 16, 8);
            r2 = mBlock.pb168x168.refIdx2[list];
        }
        int v2 = H264Utils.Mv.packMv(mvX2, mvY2, r2);
        this.s.mvTopLeft.setMv(0, list, this.s.mvTop.getMv(xx + 3, list));
        MBlockDecoderUtils.saveVect(this.s.mvLeft, list, 0, 2, v1);
        MBlockDecoderUtils.saveVect(this.s.mvLeft, list, 2, 4, v2);
        MBlockDecoderUtils.saveVect(this.s.mvTop, list, xx, xx + 4, v2);
        for (int i = 0; i < 8; i++) {
            x.setMv(i, list, v1);
        }
        for (int i = 8; i < 16; i++) {
            x.setMv(i, list, v2);
        }
    }

    public void decode16x8(MBlock mBlock, Picture mb, Frame[][] refs, H264Const.PartPred p0, H264Const.PartPred p1) {
        int mbX = this.mapper.getMbX(mBlock.mbIdx);
        int mbY = this.mapper.getMbY(mBlock.mbIdx);
        boolean leftAvailable = this.mapper.leftAvailable(mBlock.mbIdx);
        boolean topAvailable = this.mapper.topAvailable(mBlock.mbIdx);
        boolean topLeftAvailable = this.mapper.topLeftAvailable(mBlock.mbIdx);
        boolean topRightAvailable = this.mapper.topRightAvailable(mBlock.mbIdx);
        int address = this.mapper.getAddress(mBlock.mbIdx);
        int xx = mbX << 2;
        for (int list = 0; list < 2; list++) {
            this.predictInter16x8(mBlock, this.mbb[list], refs, mbX, mbY, leftAvailable, topAvailable, topLeftAvailable, topRightAvailable, xx, mBlock.x, p0, p1, list);
        }
        PredictionMerger.mergePrediction(this.sh, mBlock.x.mv0R(0), mBlock.x.mv1R(0), p0, 0, this.mbb[0].getPlaneData(0), this.mbb[1].getPlaneData(0), 0, 16, 16, 8, mb.getPlaneData(0), refs, this.poc);
        PredictionMerger.mergePrediction(this.sh, mBlock.x.mv0R(8), mBlock.x.mv1R(8), p1, 0, this.mbb[0].getPlaneData(0), this.mbb[1].getPlaneData(0), 128, 16, 16, 8, mb.getPlaneData(0), refs, this.poc);
        mBlock.partPreds[0] = mBlock.partPreds[1] = p0;
        mBlock.partPreds[2] = mBlock.partPreds[3] = p1;
        this.predictChromaInter(refs, mBlock.x, mbX << 3, mbY << 3, 1, mb, mBlock.partPreds);
        this.predictChromaInter(refs, mBlock.x, mbX << 3, mbY << 3, 2, mb, mBlock.partPreds);
        this.residualInter(mBlock, refs, leftAvailable, topAvailable, mbX, mbY, this.mapper.getAddress(mBlock.mbIdx));
        MBlockDecoderUtils.saveMvs(this.di, mBlock.x, mbX, mbY);
        MBlockDecoderUtils.mergeResidual(mb, mBlock.ac, mBlock.transform8x8Used ? H264Const.COMP_BLOCK_8x8_LUT : H264Const.COMP_BLOCK_4x4_LUT, mBlock.transform8x8Used ? H264Const.COMP_POS_8x8_LUT : H264Const.COMP_POS_4x4_LUT);
        MBlockDecoderUtils.collectPredictors(this.s, mb, mbX);
        this.di.mbTypes[address] = mBlock.curMbType;
    }

    public void decode8x16(MBlock mBlock, Picture mb, Frame[][] refs, H264Const.PartPred p0, H264Const.PartPred p1) {
        int mbX = this.mapper.getMbX(mBlock.mbIdx);
        int mbY = this.mapper.getMbY(mBlock.mbIdx);
        boolean leftAvailable = this.mapper.leftAvailable(mBlock.mbIdx);
        boolean topAvailable = this.mapper.topAvailable(mBlock.mbIdx);
        boolean topLeftAvailable = this.mapper.topLeftAvailable(mBlock.mbIdx);
        boolean topRightAvailable = this.mapper.topRightAvailable(mBlock.mbIdx);
        int address = this.mapper.getAddress(mBlock.mbIdx);
        for (int list = 0; list < 2; list++) {
            this.predictInter8x16(mBlock, this.mbb[list], refs, mbX, mbY, leftAvailable, topAvailable, topLeftAvailable, topRightAvailable, mBlock.x, list, p0, p1);
        }
        PredictionMerger.mergePrediction(this.sh, mBlock.x.mv0R(0), mBlock.x.mv1R(0), p0, 0, this.mbb[0].getPlaneData(0), this.mbb[1].getPlaneData(0), 0, 16, 8, 16, mb.getPlaneData(0), refs, this.poc);
        PredictionMerger.mergePrediction(this.sh, mBlock.x.mv0R(2), mBlock.x.mv1R(2), p1, 0, this.mbb[0].getPlaneData(0), this.mbb[1].getPlaneData(0), 8, 16, 8, 16, mb.getPlaneData(0), refs, this.poc);
        mBlock.partPreds[0] = mBlock.partPreds[2] = p0;
        mBlock.partPreds[1] = mBlock.partPreds[3] = p1;
        this.predictChromaInter(refs, mBlock.x, mbX << 3, mbY << 3, 1, mb, mBlock.partPreds);
        this.predictChromaInter(refs, mBlock.x, mbX << 3, mbY << 3, 2, mb, mBlock.partPreds);
        this.residualInter(mBlock, refs, leftAvailable, topAvailable, mbX, mbY, this.mapper.getAddress(mBlock.mbIdx));
        MBlockDecoderUtils.saveMvs(this.di, mBlock.x, mbX, mbY);
        MBlockDecoderUtils.mergeResidual(mb, mBlock.ac, mBlock.transform8x8Used ? H264Const.COMP_BLOCK_8x8_LUT : H264Const.COMP_BLOCK_4x4_LUT, mBlock.transform8x8Used ? H264Const.COMP_POS_8x8_LUT : H264Const.COMP_POS_4x4_LUT);
        MBlockDecoderUtils.collectPredictors(this.s, mb, mbX);
        this.di.mbTypes[address] = mBlock.curMbType;
    }

    void predictInter16x16(MBlock mBlock, Picture mb, Picture[][] references, int mbX, int mbY, boolean leftAvailable, boolean topAvailable, boolean tlAvailable, boolean trAvailable, H264Utils.MvList x, int xx, int list, H264Const.PartPred curPred) {
        int mvX = 0;
        int mvY = 0;
        int r = -1;
        if (H264Const.usesList(curPred, list)) {
            int mvpX = MBlockDecoderUtils.calcMVPredictionMedian(this.s.mvLeft.getMv(0, list), this.s.mvTop.getMv(mbX << 2, list), this.s.mvTop.getMv((mbX << 2) + 4, list), this.s.mvTopLeft.getMv(0, list), leftAvailable, topAvailable, trAvailable, tlAvailable, mBlock.pb16x16.refIdx[list], 0);
            int mvpY = MBlockDecoderUtils.calcMVPredictionMedian(this.s.mvLeft.getMv(0, list), this.s.mvTop.getMv(mbX << 2, list), this.s.mvTop.getMv((mbX << 2) + 4, list), this.s.mvTopLeft.getMv(0, list), leftAvailable, topAvailable, trAvailable, tlAvailable, mBlock.pb16x16.refIdx[list], 1);
            mvX = mBlock.pb16x16.mvdX[list] + mvpX;
            mvY = mBlock.pb16x16.mvdY[list] + mvpY;
            MBlockDecoderUtils.debugPrint("MVP: (%d, %d), MVD: (%d, %d), MV: (%d,%d,%d)", mvpX, mvpY, mBlock.pb16x16.mvdX[list], mBlock.pb16x16.mvdY[list], mvX, mvY, mBlock.pb16x16.refIdx[list]);
            r = mBlock.pb16x16.refIdx[list];
            this.interpolator.getBlockLuma(references[list][r], mb, 0, (mbX << 6) + mvX, (mbY << 6) + mvY, 16, 16);
        }
        int v = H264Utils.Mv.packMv(mvX, mvY, r);
        this.s.mvTopLeft.setMv(0, list, this.s.mvTop.getMv(xx + 3, list));
        MBlockDecoderUtils.saveVect(this.s.mvTop, list, xx, xx + 4, v);
        MBlockDecoderUtils.saveVect(this.s.mvLeft, list, 0, 4, v);
        for (int i = 0; i < 16; i++) {
            x.setMv(i, list, v);
        }
    }

    private void residualInter(MBlock mBlock, Frame[][] refs, boolean leftAvailable, boolean topAvailable, int mbX, int mbY, int mbAddr) {
        if (mBlock.cbpLuma() > 0 || mBlock.cbpChroma() > 0) {
            this.s.qp = (this.s.qp + mBlock.mbQPDelta + 52) % 52;
        }
        this.di.mbQps[0][mbAddr] = this.s.qp;
        this.residualLuma(mBlock, leftAvailable, topAvailable, mbX, mbY);
        if (this.s.chromaFormat != ColorSpace.MONO) {
            int qp1 = calcQpChroma(this.s.qp, this.s.chromaQpOffset[0]);
            int qp2 = calcQpChroma(this.s.qp, this.s.chromaQpOffset[1]);
            this.decodeChromaResidual(mBlock, leftAvailable, topAvailable, mbX, mbY, qp1, qp2);
            this.di.mbQps[1][mbAddr] = qp1;
            this.di.mbQps[2][mbAddr] = qp2;
        }
        this.di.tr8x8Used[mbAddr] = mBlock.transform8x8Used;
    }

    public int calcMVPrediction16x8Top(int a, int b, int c, int d, boolean aAvb, boolean bAvb, boolean cAvb, boolean dAvb, int refIdx, int comp) {
        return bAvb && H264Utils.Mv.mvRef(b) == refIdx ? H264Utils.Mv.mvC(b, comp) : MBlockDecoderUtils.calcMVPredictionMedian(a, b, c, d, aAvb, bAvb, cAvb, dAvb, refIdx, comp);
    }

    public int calcMVPrediction16x8Bottom(int a, int b, int c, int d, boolean aAvb, boolean bAvb, boolean cAvb, boolean dAvb, int refIdx, int comp) {
        return aAvb && H264Utils.Mv.mvRef(a) == refIdx ? H264Utils.Mv.mvC(a, comp) : MBlockDecoderUtils.calcMVPredictionMedian(a, b, c, d, aAvb, bAvb, cAvb, dAvb, refIdx, comp);
    }

    public int calcMVPrediction8x16Left(int a, int b, int c, int d, boolean aAvb, boolean bAvb, boolean cAvb, boolean dAvb, int refIdx, int comp) {
        return aAvb && H264Utils.Mv.mvRef(a) == refIdx ? H264Utils.Mv.mvC(a, comp) : MBlockDecoderUtils.calcMVPredictionMedian(a, b, c, d, aAvb, bAvb, cAvb, dAvb, refIdx, comp);
    }

    public int calcMVPrediction8x16Right(int a, int b, int c, int d, boolean aAvb, boolean bAvb, boolean cAvb, boolean dAvb, int refIdx, int comp) {
        int lc = cAvb ? c : (dAvb ? d : MBlockDecoderUtils.NULL_VECTOR);
        return H264Utils.Mv.mvRef(lc) == refIdx ? H264Utils.Mv.mvC(lc, comp) : MBlockDecoderUtils.calcMVPredictionMedian(a, b, c, d, aAvb, bAvb, cAvb, dAvb, refIdx, comp);
    }
}