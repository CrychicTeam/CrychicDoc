package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode;

import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.H264Const;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.H264Utils;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode.aso.Mapper;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.Frame;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.SliceHeader;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.SliceType;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;
import java.util.Arrays;

public class MBlockDecoderInter8x8 extends MBlockDecoderBase {

    private Mapper mapper;

    private MBlockDecoderBDirect bDirectDecoder;

    public MBlockDecoderInter8x8(Mapper mapper, MBlockDecoderBDirect bDirectDecoder, SliceHeader sh, DeblockerInput di, int poc, DecoderState decoderState) {
        super(sh, di, poc, decoderState);
        this.mapper = mapper;
        this.bDirectDecoder = bDirectDecoder;
    }

    public void decode(MBlock mBlock, Frame[][] references, Picture mb, SliceType sliceType, boolean ref0) {
        int mbX = this.mapper.getMbX(mBlock.mbIdx);
        int mbY = this.mapper.getMbY(mBlock.mbIdx);
        boolean leftAvailable = this.mapper.leftAvailable(mBlock.mbIdx);
        boolean topAvailable = this.mapper.topAvailable(mBlock.mbIdx);
        int mbAddr = this.mapper.getAddress(mBlock.mbIdx);
        boolean topLeftAvailable = this.mapper.topLeftAvailable(mBlock.mbIdx);
        boolean topRightAvailable = this.mapper.topRightAvailable(mBlock.mbIdx);
        if (sliceType == SliceType.P) {
            this.predict8x8P(mBlock, references[0], mb, ref0, mbX, mbY, leftAvailable, topAvailable, topLeftAvailable, topRightAvailable, mBlock.x, mBlock.partPreds);
        } else {
            this.predict8x8B(mBlock, references, mb, ref0, mbX, mbY, leftAvailable, topAvailable, topLeftAvailable, topRightAvailable, mBlock.x, mBlock.partPreds);
        }
        this.predictChromaInter(references, mBlock.x, mbX << 3, mbY << 3, 1, mb, mBlock.partPreds);
        this.predictChromaInter(references, mBlock.x, mbX << 3, mbY << 3, 2, mb, mBlock.partPreds);
        if (mBlock.cbpLuma() > 0 || mBlock.cbpChroma() > 0) {
            this.s.qp = (this.s.qp + mBlock.mbQPDelta + 52) % 52;
        }
        this.di.mbQps[0][mbAddr] = this.s.qp;
        this.residualLuma(mBlock, leftAvailable, topAvailable, mbX, mbY);
        MBlockDecoderUtils.saveMvs(this.di, mBlock.x, mbX, mbY);
        int qp1 = calcQpChroma(this.s.qp, this.s.chromaQpOffset[0]);
        int qp2 = calcQpChroma(this.s.qp, this.s.chromaQpOffset[1]);
        this.decodeChromaResidual(mBlock, leftAvailable, topAvailable, mbX, mbY, qp1, qp2);
        this.di.mbQps[1][mbAddr] = qp1;
        this.di.mbQps[2][mbAddr] = qp2;
        MBlockDecoderUtils.mergeResidual(mb, mBlock.ac, mBlock.transform8x8Used ? H264Const.COMP_BLOCK_8x8_LUT : H264Const.COMP_BLOCK_4x4_LUT, mBlock.transform8x8Used ? H264Const.COMP_POS_8x8_LUT : H264Const.COMP_POS_4x4_LUT);
        MBlockDecoderUtils.collectPredictors(this.s, mb, mbX);
        this.di.mbTypes[mbAddr] = mBlock.curMbType;
        this.di.tr8x8Used[mbAddr] = mBlock.transform8x8Used;
    }

    private void predict8x8P(MBlock mBlock, Picture[] references, Picture mb, boolean ref0, int mbX, int mbY, boolean leftAvailable, boolean topAvailable, boolean tlAvailable, boolean topRightAvailable, H264Utils.MvList x, H264Const.PartPred[] pp) {
        this.decodeSubMb8x8(mBlock, 0, mBlock.pb8x8.subMbTypes[0], references, mbX << 6, mbY << 6, this.s.mvTopLeft.getMv(0, 0), this.s.mvTop.getMv(mbX << 2, 0), this.s.mvTop.getMv((mbX << 2) + 1, 0), this.s.mvTop.getMv((mbX << 2) + 2, 0), this.s.mvLeft.getMv(0, 0), this.s.mvLeft.getMv(1, 0), tlAvailable, topAvailable, topAvailable, leftAvailable, mBlock.x, 0, 1, 4, 5, mBlock.pb8x8.refIdx[0][0], mb, 0, 0);
        this.decodeSubMb8x8(mBlock, 1, mBlock.pb8x8.subMbTypes[1], references, (mbX << 6) + 32, mbY << 6, this.s.mvTop.getMv((mbX << 2) + 1, 0), this.s.mvTop.getMv((mbX << 2) + 2, 0), this.s.mvTop.getMv((mbX << 2) + 3, 0), this.s.mvTop.getMv((mbX << 2) + 4, 0), x.getMv(1, 0), x.getMv(5, 0), topAvailable, topAvailable, topRightAvailable, true, x, 2, 3, 6, 7, mBlock.pb8x8.refIdx[0][1], mb, 8, 0);
        this.decodeSubMb8x8(mBlock, 2, mBlock.pb8x8.subMbTypes[2], references, mbX << 6, (mbY << 6) + 32, this.s.mvLeft.getMv(1, 0), x.getMv(4, 0), x.getMv(5, 0), x.getMv(6, 0), this.s.mvLeft.getMv(2, 0), this.s.mvLeft.getMv(3, 0), leftAvailable, true, true, leftAvailable, x, 8, 9, 12, 13, mBlock.pb8x8.refIdx[0][2], mb, 128, 0);
        this.decodeSubMb8x8(mBlock, 3, mBlock.pb8x8.subMbTypes[3], references, (mbX << 6) + 32, (mbY << 6) + 32, x.getMv(5, 0), x.getMv(6, 0), x.getMv(7, 0), MBlockDecoderUtils.NULL_VECTOR, x.getMv(9, 0), x.getMv(13, 0), true, true, false, true, x, 10, 11, 14, 15, mBlock.pb8x8.refIdx[0][3], mb, 136, 0);
        for (int i = 0; i < 4; i++) {
            int blk4x4 = H264Const.BLK8x8_BLOCKS[i][0];
            PredictionMerger.weightPrediction(this.sh, x.mv0R(blk4x4), 0, mb.getPlaneData(0), H264Const.BLK_8x8_MB_OFF_LUMA[i], 16, 8, 8, mb.getPlaneData(0));
        }
        MBlockDecoderUtils.savePrediction8x8(this.s, mbX, x);
        Arrays.fill(pp, H264Const.PartPred.L0);
    }

    private void predict8x8B(MBlock mBlock, Frame[][] refs, Picture mb, boolean ref0, int mbX, int mbY, boolean leftAvailable, boolean topAvailable, boolean tlAvailable, boolean topRightAvailable, H264Utils.MvList x, H264Const.PartPred[] p) {
        for (int i = 0; i < 4; i++) {
            p[i] = H264Const.bPartPredModes[mBlock.pb8x8.subMbTypes[i]];
        }
        for (int i = 0; i < 4; i++) {
            if (p[i] == H264Const.PartPred.Direct) {
                this.bDirectDecoder.predictBDirect(refs, mbX, mbY, leftAvailable, topAvailable, tlAvailable, topRightAvailable, x, p, mb, H264Const.ARRAY[i]);
            }
        }
        for (int list = 0; list < 2; list++) {
            if (H264Const.usesList(H264Const.bPartPredModes[mBlock.pb8x8.subMbTypes[0]], list)) {
                this.decodeSubMb8x8(mBlock, 0, H264Const.bSubMbTypes[mBlock.pb8x8.subMbTypes[0]], refs[list], mbX << 6, mbY << 6, this.s.mvTopLeft.getMv(0, list), this.s.mvTop.getMv(mbX << 2, list), this.s.mvTop.getMv((mbX << 2) + 1, list), this.s.mvTop.getMv((mbX << 2) + 2, list), this.s.mvLeft.getMv(0, list), this.s.mvLeft.getMv(1, list), tlAvailable, topAvailable, topAvailable, leftAvailable, x, 0, 1, 4, 5, mBlock.pb8x8.refIdx[list][0], this.mbb[list], 0, list);
            }
            if (H264Const.usesList(H264Const.bPartPredModes[mBlock.pb8x8.subMbTypes[1]], list)) {
                this.decodeSubMb8x8(mBlock, 1, H264Const.bSubMbTypes[mBlock.pb8x8.subMbTypes[1]], refs[list], (mbX << 6) + 32, mbY << 6, this.s.mvTop.getMv((mbX << 2) + 1, list), this.s.mvTop.getMv((mbX << 2) + 2, list), this.s.mvTop.getMv((mbX << 2) + 3, list), this.s.mvTop.getMv((mbX << 2) + 4, list), x.getMv(1, list), x.getMv(5, list), topAvailable, topAvailable, topRightAvailable, true, x, 2, 3, 6, 7, mBlock.pb8x8.refIdx[list][1], this.mbb[list], 8, list);
            }
            if (H264Const.usesList(H264Const.bPartPredModes[mBlock.pb8x8.subMbTypes[2]], list)) {
                this.decodeSubMb8x8(mBlock, 2, H264Const.bSubMbTypes[mBlock.pb8x8.subMbTypes[2]], refs[list], mbX << 6, (mbY << 6) + 32, this.s.mvLeft.getMv(1, list), x.getMv(4, list), x.getMv(5, list), x.getMv(6, list), this.s.mvLeft.getMv(2, list), this.s.mvLeft.getMv(3, list), leftAvailable, true, true, leftAvailable, x, 8, 9, 12, 13, mBlock.pb8x8.refIdx[list][2], this.mbb[list], 128, list);
            }
            if (H264Const.usesList(H264Const.bPartPredModes[mBlock.pb8x8.subMbTypes[3]], list)) {
                this.decodeSubMb8x8(mBlock, 3, H264Const.bSubMbTypes[mBlock.pb8x8.subMbTypes[3]], refs[list], (mbX << 6) + 32, (mbY << 6) + 32, x.getMv(5, list), x.getMv(6, list), x.getMv(7, list), MBlockDecoderUtils.NULL_VECTOR, x.getMv(9, list), x.getMv(13, list), true, true, false, true, x, 10, 11, 14, 15, mBlock.pb8x8.refIdx[list][3], this.mbb[list], 136, list);
            }
        }
        for (int ix = 0; ix < 4; ix++) {
            int blk4x4 = H264Const.BLK8x8_BLOCKS[ix][0];
            PredictionMerger.mergePrediction(this.sh, x.mv0R(blk4x4), x.mv1R(blk4x4), H264Const.bPartPredModes[mBlock.pb8x8.subMbTypes[ix]], 0, this.mbb[0].getPlaneData(0), this.mbb[1].getPlaneData(0), H264Const.BLK_8x8_MB_OFF_LUMA[ix], 16, 8, 8, mb.getPlaneData(0), refs, this.poc);
        }
        MBlockDecoderUtils.savePrediction8x8(this.s, mbX, x);
    }

    private void decodeSubMb8x8(MBlock mBlock, int partNo, int subMbType, Picture[] references, int offX, int offY, int tl, int t0, int t1, int tr, int l0, int l1, boolean tlAvb, boolean tAvb, boolean trAvb, boolean lAvb, H264Utils.MvList x, int i00, int i01, int i10, int i11, int refIdx, Picture mb, int off, int list) {
        switch(subMbType) {
            case 0:
                this.decodeSub8x8(mBlock, partNo, references, offX, offY, tl, t0, tr, l0, tlAvb, tAvb, trAvb, lAvb, x, i00, i01, i10, i11, refIdx, mb, off, list);
                break;
            case 1:
                this.decodeSub8x4(mBlock, partNo, references, offX, offY, tl, t0, tr, l0, l1, tlAvb, tAvb, trAvb, lAvb, x, i00, i01, i10, i11, refIdx, mb, off, list);
                break;
            case 2:
                this.decodeSub4x8(mBlock, partNo, references, offX, offY, tl, t0, t1, tr, l0, tlAvb, tAvb, trAvb, lAvb, x, i00, i01, i10, i11, refIdx, mb, off, list);
                break;
            case 3:
                this.decodeSub4x4(mBlock, partNo, references, offX, offY, tl, t0, t1, tr, l0, l1, tlAvb, tAvb, trAvb, lAvb, x, i00, i01, i10, i11, refIdx, mb, off, list);
        }
    }

    private void decodeSub8x8(MBlock mBlock, int partNo, Picture[] references, int offX, int offY, int tl, int t0, int tr, int l0, boolean tlAvb, boolean tAvb, boolean trAvb, boolean lAvb, H264Utils.MvList x, int i00, int i01, int i10, int i11, int refIdx, Picture mb, int off, int list) {
        int mvpX = MBlockDecoderUtils.calcMVPredictionMedian(l0, t0, tr, tl, lAvb, tAvb, trAvb, tlAvb, refIdx, 0);
        int mvpY = MBlockDecoderUtils.calcMVPredictionMedian(l0, t0, tr, tl, lAvb, tAvb, trAvb, tlAvb, refIdx, 1);
        int mv = H264Utils.Mv.packMv(mBlock.pb8x8.mvdX1[list][partNo] + mvpX, mBlock.pb8x8.mvdY1[list][partNo] + mvpY, refIdx);
        x.setMv(i00, list, mv);
        x.setMv(i01, list, mv);
        x.setMv(i10, list, mv);
        x.setMv(i11, list, mv);
        MBlockDecoderUtils.debugPrint("MVP: (%d, %d), MVD: (%d, %d), MV: (%d,%d,%d)", mvpX, mvpY, mBlock.pb8x8.mvdX1[list][partNo], mBlock.pb8x8.mvdY1[list][partNo], H264Utils.Mv.mvX(mv), H264Utils.Mv.mvY(mv), refIdx);
        this.interpolator.getBlockLuma(references[refIdx], mb, off, offX + H264Utils.Mv.mvX(mv), offY + H264Utils.Mv.mvY(mv), 8, 8);
    }

    private void decodeSub8x4(MBlock mBlock, int partNo, Picture[] references, int offX, int offY, int tl, int t0, int tr, int l0, int l1, boolean tlAvb, boolean tAvb, boolean trAvb, boolean lAvb, H264Utils.MvList x, int i00, int i01, int i10, int i11, int refIdx, Picture mb, int off, int list) {
        int mvpX1 = MBlockDecoderUtils.calcMVPredictionMedian(l0, t0, tr, tl, lAvb, tAvb, trAvb, tlAvb, refIdx, 0);
        int mvpY1 = MBlockDecoderUtils.calcMVPredictionMedian(l0, t0, tr, tl, lAvb, tAvb, trAvb, tlAvb, refIdx, 1);
        int mv1 = H264Utils.Mv.packMv(mBlock.pb8x8.mvdX1[list][partNo] + mvpX1, mBlock.pb8x8.mvdY1[list][partNo] + mvpY1, refIdx);
        x.setMv(i00, list, mv1);
        x.setMv(i01, list, mv1);
        MBlockDecoderUtils.debugPrint("MVP: (%d, %d), MVD: (%d, %d), MV: (%d,%d,%d)", mvpX1, mvpY1, mBlock.pb8x8.mvdX1[list][partNo], mBlock.pb8x8.mvdY1[list][partNo], H264Utils.Mv.mvX(mv1), H264Utils.Mv.mvY(mv1), refIdx);
        int mvpX2 = MBlockDecoderUtils.calcMVPredictionMedian(l1, mv1, MBlockDecoderUtils.NULL_VECTOR, l0, lAvb, true, false, lAvb, refIdx, 0);
        int mvpY2 = MBlockDecoderUtils.calcMVPredictionMedian(l1, mv1, MBlockDecoderUtils.NULL_VECTOR, l0, lAvb, true, false, lAvb, refIdx, 1);
        int mv2 = H264Utils.Mv.packMv(mBlock.pb8x8.mvdX2[list][partNo] + mvpX2, mBlock.pb8x8.mvdY2[list][partNo] + mvpY2, refIdx);
        x.setMv(i10, list, mv2);
        x.setMv(i11, list, mv2);
        MBlockDecoderUtils.debugPrint("MVP: (%d, %d), MVD: (%d, %d), MV: (%d,%d,%d)", mvpX2, mvpY2, mBlock.pb8x8.mvdX2[list][partNo], mBlock.pb8x8.mvdY2[list][partNo], H264Utils.Mv.mvX(mv2), H264Utils.Mv.mvY(mv2), refIdx);
        this.interpolator.getBlockLuma(references[refIdx], mb, off, offX + H264Utils.Mv.mvX(mv1), offY + H264Utils.Mv.mvY(mv1), 8, 4);
        this.interpolator.getBlockLuma(references[refIdx], mb, off + mb.getWidth() * 4, offX + H264Utils.Mv.mvX(mv2), offY + H264Utils.Mv.mvY(mv2) + 16, 8, 4);
    }

    private void decodeSub4x8(MBlock mBlock, int partNo, Picture[] references, int offX, int offY, int tl, int t0, int t1, int tr, int l0, boolean tlAvb, boolean tAvb, boolean trAvb, boolean lAvb, H264Utils.MvList x, int i00, int i01, int i10, int i11, int refIdx, Picture mb, int off, int list) {
        int mvpX1 = MBlockDecoderUtils.calcMVPredictionMedian(l0, t0, t1, tl, lAvb, tAvb, tAvb, tlAvb, refIdx, 0);
        int mvpY1 = MBlockDecoderUtils.calcMVPredictionMedian(l0, t0, t1, tl, lAvb, tAvb, tAvb, tlAvb, refIdx, 1);
        int mv1 = H264Utils.Mv.packMv(mBlock.pb8x8.mvdX1[list][partNo] + mvpX1, mBlock.pb8x8.mvdY1[list][partNo] + mvpY1, refIdx);
        x.setMv(i00, list, mv1);
        x.setMv(i10, list, mv1);
        MBlockDecoderUtils.debugPrint("MVP: (%d, %d), MVD: (%d, %d), MV: (%d,%d,%d)", mvpX1, mvpY1, mBlock.pb8x8.mvdX1[list][partNo], mBlock.pb8x8.mvdY1[list][partNo], H264Utils.Mv.mvX(mv1), H264Utils.Mv.mvY(mv1), refIdx);
        int mvpX2 = MBlockDecoderUtils.calcMVPredictionMedian(mv1, t1, tr, t0, true, tAvb, trAvb, tAvb, refIdx, 0);
        int mvpY2 = MBlockDecoderUtils.calcMVPredictionMedian(mv1, t1, tr, t0, true, tAvb, trAvb, tAvb, refIdx, 1);
        int mv2 = H264Utils.Mv.packMv(mBlock.pb8x8.mvdX2[list][partNo] + mvpX2, mBlock.pb8x8.mvdY2[list][partNo] + mvpY2, refIdx);
        x.setMv(i01, list, mv2);
        x.setMv(i11, list, mv2);
        MBlockDecoderUtils.debugPrint("MVP: (%d, %d), MVD: (%d, %d), MV: (%d,%d,%d)", mvpX2, mvpY2, mBlock.pb8x8.mvdX2[list][partNo], mBlock.pb8x8.mvdY2[list][partNo], H264Utils.Mv.mvX(mv2), H264Utils.Mv.mvY(mv2), refIdx);
        this.interpolator.getBlockLuma(references[refIdx], mb, off, offX + H264Utils.Mv.mvX(mv1), offY + H264Utils.Mv.mvY(mv1), 4, 8);
        this.interpolator.getBlockLuma(references[refIdx], mb, off + 4, offX + H264Utils.Mv.mvX(mv2) + 16, offY + H264Utils.Mv.mvY(mv2), 4, 8);
    }

    private void decodeSub4x4(MBlock mBlock, int partNo, Picture[] references, int offX, int offY, int tl, int t0, int t1, int tr, int l0, int l1, boolean tlAvb, boolean tAvb, boolean trAvb, boolean lAvb, H264Utils.MvList x, int i00, int i01, int i10, int i11, int refIdx, Picture mb, int off, int list) {
        int mvpX1 = MBlockDecoderUtils.calcMVPredictionMedian(l0, t0, t1, tl, lAvb, tAvb, tAvb, tlAvb, refIdx, 0);
        int mvpY1 = MBlockDecoderUtils.calcMVPredictionMedian(l0, t0, t1, tl, lAvb, tAvb, tAvb, tlAvb, refIdx, 1);
        int mv1 = H264Utils.Mv.packMv(mBlock.pb8x8.mvdX1[list][partNo] + mvpX1, mBlock.pb8x8.mvdY1[list][partNo] + mvpY1, refIdx);
        x.setMv(i00, list, mv1);
        MBlockDecoderUtils.debugPrint("MVP: (%d, %d), MVD: (%d, %d), MV: (%d,%d,%d)", mvpX1, mvpY1, mBlock.pb8x8.mvdX1[list][partNo], mBlock.pb8x8.mvdY1[list][partNo], H264Utils.Mv.mvX(mv1), H264Utils.Mv.mvY(mv1), refIdx);
        int mvpX2 = MBlockDecoderUtils.calcMVPredictionMedian(mv1, t1, tr, t0, true, tAvb, trAvb, tAvb, refIdx, 0);
        int mvpY2 = MBlockDecoderUtils.calcMVPredictionMedian(mv1, t1, tr, t0, true, tAvb, trAvb, tAvb, refIdx, 1);
        int mv2 = H264Utils.Mv.packMv(mBlock.pb8x8.mvdX2[list][partNo] + mvpX2, mBlock.pb8x8.mvdY2[list][partNo] + mvpY2, refIdx);
        x.setMv(i01, list, mv2);
        MBlockDecoderUtils.debugPrint("MVP: (%d, %d), MVD: (%d, %d), MV: (%d,%d,%d)", mvpX2, mvpY2, mBlock.pb8x8.mvdX2[list][partNo], mBlock.pb8x8.mvdY2[list][partNo], H264Utils.Mv.mvX(mv2), H264Utils.Mv.mvY(mv2), refIdx);
        int mvpX3 = MBlockDecoderUtils.calcMVPredictionMedian(l1, mv1, mv2, l0, lAvb, true, true, lAvb, refIdx, 0);
        int mvpY3 = MBlockDecoderUtils.calcMVPredictionMedian(l1, mv1, mv2, l0, lAvb, true, true, lAvb, refIdx, 1);
        int mv3 = H264Utils.Mv.packMv(mBlock.pb8x8.mvdX3[list][partNo] + mvpX3, mBlock.pb8x8.mvdY3[list][partNo] + mvpY3, refIdx);
        x.setMv(i10, list, mv3);
        MBlockDecoderUtils.debugPrint("MVP: (%d, %d), MVD: (%d, %d), MV: (%d,%d,%d)", mvpX3, mvpY3, mBlock.pb8x8.mvdX3[list][partNo], mBlock.pb8x8.mvdY3[list][partNo], H264Utils.Mv.mvX(mv3), H264Utils.Mv.mvY(mv3), refIdx);
        int mvpX4 = MBlockDecoderUtils.calcMVPredictionMedian(mv3, mv2, MBlockDecoderUtils.NULL_VECTOR, mv1, true, true, false, true, refIdx, 0);
        int mvpY4 = MBlockDecoderUtils.calcMVPredictionMedian(mv3, mv2, MBlockDecoderUtils.NULL_VECTOR, mv1, true, true, false, true, refIdx, 1);
        int mv4 = H264Utils.Mv.packMv(mBlock.pb8x8.mvdX4[list][partNo] + mvpX4, mBlock.pb8x8.mvdY4[list][partNo] + mvpY4, refIdx);
        x.setMv(i11, list, mv4);
        MBlockDecoderUtils.debugPrint("MVP: (%d, %d), MVD: (%d, %d), MV: (%d,%d,%d)", mvpX4, mvpY4, mBlock.pb8x8.mvdX4[list][partNo], mBlock.pb8x8.mvdY4[list][partNo], H264Utils.Mv.mvX(mv4), H264Utils.Mv.mvY(mv4), refIdx);
        this.interpolator.getBlockLuma(references[refIdx], mb, off, offX + H264Utils.Mv.mvX(mv1), offY + H264Utils.Mv.mvY(mv1), 4, 4);
        this.interpolator.getBlockLuma(references[refIdx], mb, off + 4, offX + H264Utils.Mv.mvX(mv2) + 16, offY + H264Utils.Mv.mvY(mv2), 4, 4);
        this.interpolator.getBlockLuma(references[refIdx], mb, off + mb.getWidth() * 4, offX + H264Utils.Mv.mvX(mv3), offY + H264Utils.Mv.mvY(mv3) + 16, 4, 4);
        this.interpolator.getBlockLuma(references[refIdx], mb, off + mb.getWidth() * 4 + 4, offX + H264Utils.Mv.mvX(mv4) + 16, offY + H264Utils.Mv.mvY(mv4) + 16, 4, 4);
    }
}