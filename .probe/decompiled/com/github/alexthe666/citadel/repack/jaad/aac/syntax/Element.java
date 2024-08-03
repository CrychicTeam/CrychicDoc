package com.github.alexthe666.citadel.repack.jaad.aac.syntax;

import com.github.alexthe666.citadel.repack.jaad.aac.AACException;
import com.github.alexthe666.citadel.repack.jaad.aac.SampleFrequency;
import com.github.alexthe666.citadel.repack.jaad.aac.sbr.SBR;

public abstract class Element implements Constants {

    private int elementInstanceTag;

    private SBR sbr;

    protected void readElementInstanceTag(BitStream in) throws AACException {
        this.elementInstanceTag = in.readBits(4);
    }

    public int getElementInstanceTag() {
        return this.elementInstanceTag;
    }

    void decodeSBR(BitStream in, SampleFrequency sf, int count, boolean stereo, boolean crc, boolean downSampled, boolean smallFrames) throws AACException {
        if (this.sbr == null) {
            int fq = sf.getFrequency();
            if (fq < 24000 && !downSampled) {
                sf = SampleFrequency.forFrequency(2 * fq);
            }
            this.sbr = new SBR(smallFrames, stereo, sf, downSampled);
        }
        this.sbr.decode(in, count, crc);
    }

    boolean isSBRPresent() {
        return this.sbr != null;
    }

    SBR getSBR() {
        return this.sbr;
    }
}