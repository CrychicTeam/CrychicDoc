package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode;

import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode.aso.Mapper;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;

public class MBlockDecoderIPCM {

    private Mapper mapper;

    private DecoderState s;

    public MBlockDecoderIPCM(Mapper mapper, DecoderState decoderState) {
        this.mapper = mapper;
        this.s = decoderState;
    }

    public void decode(MBlock mBlock, Picture mb) {
        int mbX = this.mapper.getMbX(mBlock.mbIdx);
        MBlockDecoderUtils.collectPredictors(this.s, mb, mbX);
        MBlockDecoderUtils.saveVectIntra(this.s, this.mapper.getMbX(mBlock.mbIdx));
    }
}