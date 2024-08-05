package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode;

import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.H264Const;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode.aso.Mapper;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.SliceHeader;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;

public class MBlockDecoderIntraNxN extends MBlockDecoderBase {

    private Mapper mapper;

    private Intra8x8PredictionBuilder prediction8x8Builder;

    public MBlockDecoderIntraNxN(Mapper mapper, SliceHeader sh, DeblockerInput di, int poc, DecoderState decoderState) {
        super(sh, di, poc, decoderState);
        this.mapper = mapper;
        this.prediction8x8Builder = new Intra8x8PredictionBuilder();
    }

    public void decode(MBlock mBlock, Picture mb) {
        int mbX = this.mapper.getMbX(mBlock.mbIdx);
        int mbY = this.mapper.getMbY(mBlock.mbIdx);
        int mbAddr = this.mapper.getAddress(mBlock.mbIdx);
        boolean leftAvailable = this.mapper.leftAvailable(mBlock.mbIdx);
        boolean topAvailable = this.mapper.topAvailable(mBlock.mbIdx);
        boolean topLeftAvailable = this.mapper.topLeftAvailable(mBlock.mbIdx);
        boolean topRightAvailable = this.mapper.topRightAvailable(mBlock.mbIdx);
        if (mBlock.cbpLuma() > 0 || mBlock.cbpChroma() > 0) {
            this.s.qp = (this.s.qp + mBlock.mbQPDelta + 52) % 52;
        }
        this.di.mbQps[0][mbAddr] = this.s.qp;
        this.residualLuma(mBlock, leftAvailable, topAvailable, mbX, mbY);
        if (!mBlock.transform8x8Used) {
            for (int i = 0; i < 16; i++) {
                int blkX = (i & 3) << 2;
                int blkY = i & -4;
                int bi = H264Const.BLK_INV_MAP[i];
                boolean trAvailable = (bi == 0 || bi == 1 || bi == 4) && topAvailable || bi == 5 && topRightAvailable || bi == 2 || bi == 6 || bi == 8 || bi == 9 || bi == 10 || bi == 12 || bi == 14;
                Intra4x4PredictionBuilder.predictWithMode(mBlock.lumaModes[bi], mBlock.ac[0][bi], blkX == 0 ? leftAvailable : true, blkY == 0 ? topAvailable : true, trAvailable, this.s.leftRow[0], this.s.topLine[0], this.s.topLeft[0], mbX << 4, blkX, blkY, mb.getPlaneData(0));
            }
        } else {
            for (int i = 0; i < 4; i++) {
                int blkX = (i & 1) << 1;
                int blkY = i & 2;
                boolean trAvailable = i == 0 && topAvailable || i == 1 && topRightAvailable || i == 2;
                boolean tlAvailable = i == 0 ? topLeftAvailable : (i == 1 ? topAvailable : (i == 2 ? leftAvailable : true));
                this.prediction8x8Builder.predictWithMode(mBlock.lumaModes[i], mBlock.ac[0][i], blkX == 0 ? leftAvailable : true, blkY == 0 ? topAvailable : true, tlAvailable, trAvailable, this.s.leftRow[0], this.s.topLine[0], this.s.topLeft[0], mbX << 4, blkX << 2, blkY << 2, mb.getPlaneData(0));
            }
        }
        this.decodeChroma(mBlock, mbX, mbY, leftAvailable, topAvailable, mb, this.s.qp);
        this.di.mbTypes[mbAddr] = mBlock.curMbType;
        this.di.tr8x8Used[mbAddr] = mBlock.transform8x8Used;
        MBlockDecoderUtils.collectChromaPredictors(this.s, mb, mbX);
        MBlockDecoderUtils.saveMvsIntra(this.di, mbX, mbY);
        MBlockDecoderUtils.saveVectIntra(this.s, this.mapper.getMbX(mBlock.mbIdx));
    }
}