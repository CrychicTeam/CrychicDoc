package com.github.alexthe666.citadel.repack.jaad.aac.syntax;

import com.github.alexthe666.citadel.repack.jaad.aac.AACException;
import com.github.alexthe666.citadel.repack.jaad.aac.DecoderConfig;

class SCE_LFE extends Element {

    private final ICStream ics;

    SCE_LFE(DecoderConfig config) {
        this.ics = new ICStream(config);
    }

    void decode(BitStream in, DecoderConfig conf) throws AACException {
        this.readElementInstanceTag(in);
        this.ics.decode(in, false, conf);
    }

    public ICStream getICStream() {
        return this.ics;
    }
}