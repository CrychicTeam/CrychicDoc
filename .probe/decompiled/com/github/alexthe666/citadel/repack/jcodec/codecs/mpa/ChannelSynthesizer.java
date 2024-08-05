package com.github.alexthe666.citadel.repack.jcodec.codecs.mpa;

class ChannelSynthesizer {

    private float[][] v = new float[2][512];

    private int pos;

    private float scalefactor;

    private int current;

    public ChannelSynthesizer(int channelnumber, float factor) {
        this.scalefactor = factor;
        this.pos = 15;
    }

    private static void distributeSamples(int pos, float[] dest, float[] next, float[] s) {
        for (int i = 0; i < 16; i++) {
            dest[(i << 4) + pos] = s[i];
        }
        for (int i = 1; i < 17; i++) {
            next[(i << 4) + pos] = s[15 + i];
        }
        dest[256 + pos] = 0.0F;
        next[0 + pos] = -s[0];
        for (int i = 0; i < 15; i++) {
            dest[272 + (i << 4) + pos] = -s[15 - i];
        }
        for (int i = 0; i < 15; i++) {
            next[272 + (i << 4) + pos] = s[30 - i];
        }
    }

    public void synthesize(float[] coeffs, short[] out, int off) {
        MpaPqmf.computeButterfly(this.pos, coeffs);
        int next = ~this.current & 1;
        distributeSamples(this.pos, this.v[this.current], this.v[next], coeffs);
        MpaPqmf.computeFilter(this.pos, this.v[this.current], out, off, this.scalefactor);
        this.pos = this.pos + 1 & 15;
        this.current = next;
    }
}