package com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12;

public class MPEGPredQuad extends MPEGPredOct {

    public MPEGPredQuad(MPEGPred other) {
        super(other);
    }

    @Override
    public void predictPlane(byte[] ref, int refX, int refY, int refW, int refH, int refVertStep, int refVertOff, int[] tgt, int tgtY, int tgtW, int tgtH, int tgtVertStep) {
        super.predictPlane(ref, refX, refY, refW, refH, refVertStep, refVertOff, tgt, tgtY, tgtW << 1, tgtH << 1, tgtVertStep);
    }
}