package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode;

import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode.aso.Mapper;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.SliceHeader;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;

public class MBlockDecoderIntra16x16 extends MBlockDecoderBase {

    private Mapper mapper;

    public MBlockDecoderIntra16x16(Mapper mapper, SliceHeader sh, DeblockerInput di, int poc, DecoderState decoderState) {
        super(sh, di, poc, decoderState);
        this.mapper = mapper;
    }

    public void decode(MBlock mBlock, Picture mb) {
        int mbX = this.mapper.getMbX(mBlock.mbIdx);
        int mbY = this.mapper.getMbY(mBlock.mbIdx);
        int address = this.mapper.getAddress(mBlock.mbIdx);
        boolean leftAvailable = this.mapper.leftAvailable(mBlock.mbIdx);
        boolean topAvailable = this.mapper.topAvailable(mBlock.mbIdx);
        this.s.qp = (this.s.qp + mBlock.mbQPDelta + 52) % 52;
        this.di.mbQps[0][address] = this.s.qp;
        this.residualLumaI16x16(mBlock, leftAvailable, topAvailable, mbX, mbY);
        Intra16x16PredictionBuilder.predictWithMode(mBlock.luma16x16Mode, mBlock.ac[0], leftAvailable, topAvailable, this.s.leftRow[0], this.s.topLine[0], this.s.topLeft[0], mbX << 4, mb.getPlaneData(0));
        this.decodeChroma(mBlock, mbX, mbY, leftAvailable, topAvailable, mb, this.s.qp);
        this.di.mbTypes[address] = mBlock.curMbType;
        MBlockDecoderUtils.collectPredictors(this.s, mb, mbX);
        MBlockDecoderUtils.saveMvsIntra(this.di, mbX, mbY);
        MBlockDecoderUtils.saveVectIntra(this.s, this.mapper.getMbX(mBlock.mbIdx));
    }

    private void residualLumaI16x16(MBlock mBlock, boolean leftAvailable, boolean topAvailable, int mbX, int mbY) {
        CoeffTransformer.invDC4x4(mBlock.dc);
        int[] scalingList = this.getScalingList(0);
        CoeffTransformer.dequantizeDC4x4(mBlock.dc, this.s.qp, scalingList);
        CoeffTransformer.reorderDC4x4(mBlock.dc);
        for (int i = 0; i < 16; i++) {
            if ((mBlock.cbpLuma() & 1 << (i >> 2)) != 0) {
                CoeffTransformer.dequantizeAC(mBlock.ac[0][i], this.s.qp, scalingList);
            }
            mBlock.ac[0][i][0] = mBlock.dc[i];
            CoeffTransformer.idct4x4(mBlock.ac[0][i]);
        }
    }
}