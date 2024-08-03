package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode;

import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.H264Const;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.H264Utils;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode.aso.Mapper;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.Frame;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.SliceHeader;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.SliceType;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;
import java.util.Arrays;

public class MBlockSkipDecoder extends MBlockDecoderBase {

    private Mapper mapper;

    private MBlockDecoderBDirect bDirectDecoder;

    public MBlockSkipDecoder(Mapper mapper, MBlockDecoderBDirect bDirectDecoder, SliceHeader sh, DeblockerInput di, int poc, DecoderState sharedState) {
        super(sh, di, poc, sharedState);
        this.mapper = mapper;
        this.bDirectDecoder = bDirectDecoder;
    }

    public void decodeSkip(MBlock mBlock, Frame[][] refs, Picture mb, SliceType sliceType) {
        int mbX = this.mapper.getMbX(mBlock.mbIdx);
        int mbY = this.mapper.getMbY(mBlock.mbIdx);
        int mbAddr = this.mapper.getAddress(mBlock.mbIdx);
        if (sliceType == SliceType.P) {
            this.predictPSkip(refs, mbX, mbY, this.mapper.leftAvailable(mBlock.mbIdx), this.mapper.topAvailable(mBlock.mbIdx), this.mapper.topLeftAvailable(mBlock.mbIdx), this.mapper.topRightAvailable(mBlock.mbIdx), mBlock.x, mb);
            Arrays.fill(mBlock.partPreds, H264Const.PartPred.L0);
        } else {
            this.bDirectDecoder.predictBDirect(refs, mbX, mbY, this.mapper.leftAvailable(mBlock.mbIdx), this.mapper.topAvailable(mBlock.mbIdx), this.mapper.topLeftAvailable(mBlock.mbIdx), this.mapper.topRightAvailable(mBlock.mbIdx), mBlock.x, mBlock.partPreds, mb, H264Const.identityMapping4);
            MBlockDecoderUtils.savePrediction8x8(this.s, mbX, mBlock.x);
        }
        this.decodeChromaSkip(refs, mBlock.x, mBlock.partPreds, mbX, mbY, mb);
        MBlockDecoderUtils.collectPredictors(this.s, mb, mbX);
        MBlockDecoderUtils.saveMvs(this.di, mBlock.x, mbX, mbY);
        this.di.mbTypes[mbAddr] = mBlock.curMbType;
        this.di.mbQps[0][mbAddr] = this.s.qp;
        this.di.mbQps[1][mbAddr] = calcQpChroma(this.s.qp, this.s.chromaQpOffset[0]);
        this.di.mbQps[2][mbAddr] = calcQpChroma(this.s.qp, this.s.chromaQpOffset[1]);
    }

    public void predictPSkip(Frame[][] refs, int mbX, int mbY, boolean lAvb, boolean tAvb, boolean tlAvb, boolean trAvb, H264Utils.MvList x, Picture mb) {
        int mvX = 0;
        int mvY = 0;
        if (lAvb && tAvb) {
            int b = this.s.mvTop.getMv(mbX << 2, 0);
            int a = this.s.mvLeft.getMv(0, 0);
            if (a != 0 && b != 0) {
                mvX = MBlockDecoderUtils.calcMVPredictionMedian(a, b, this.s.mvTop.getMv((mbX << 2) + 4, 0), this.s.mvTopLeft.getMv(0, 0), lAvb, tAvb, trAvb, tlAvb, 0, 0);
                mvY = MBlockDecoderUtils.calcMVPredictionMedian(a, b, this.s.mvTop.getMv((mbX << 2) + 4, 0), this.s.mvTopLeft.getMv(0, 0), lAvb, tAvb, trAvb, tlAvb, 0, 1);
            }
        }
        int xx = mbX << 2;
        this.s.mvTopLeft.copyPair(0, this.s.mvTop, xx + 3);
        MBlockDecoderUtils.saveVect(this.s.mvTop, 0, xx, xx + 4, H264Utils.Mv.packMv(mvX, mvY, 0));
        MBlockDecoderUtils.saveVect(this.s.mvLeft, 0, 0, 4, H264Utils.Mv.packMv(mvX, mvY, 0));
        MBlockDecoderUtils.saveVect(this.s.mvTop, 1, xx, xx + 4, MBlockDecoderUtils.NULL_VECTOR);
        MBlockDecoderUtils.saveVect(this.s.mvLeft, 1, 0, 4, MBlockDecoderUtils.NULL_VECTOR);
        for (int i = 0; i < 16; i++) {
            x.setMv(i, 0, H264Utils.Mv.packMv(mvX, mvY, 0));
        }
        this.interpolator.getBlockLuma(refs[0][0], mb, 0, (mbX << 6) + mvX, (mbY << 6) + mvY, 16, 16);
        PredictionMerger.mergePrediction(this.sh, 0, 0, H264Const.PartPred.L0, 0, mb.getPlaneData(0), null, 0, 16, 16, 16, mb.getPlaneData(0), refs, this.poc);
    }

    public void decodeChromaSkip(Frame[][] reference, H264Utils.MvList vectors, H264Const.PartPred[] pp, int mbX, int mbY, Picture mb) {
        this.predictChromaInter(reference, vectors, mbX << 3, mbY << 3, 1, mb, pp);
        this.predictChromaInter(reference, vectors, mbX << 3, mbY << 3, 2, mb, pp);
    }
}