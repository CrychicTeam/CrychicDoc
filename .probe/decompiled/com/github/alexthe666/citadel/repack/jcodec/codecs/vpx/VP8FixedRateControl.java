package com.github.alexthe666.citadel.repack.jcodec.codecs.vpx;

public class VP8FixedRateControl implements RateControl {

    private int rate;

    public VP8FixedRateControl(int rate) {
        this.rate = rate;
    }

    @Override
    public int[] getSegmentQps() {
        return null;
    }

    @Override
    public int getSegment() {
        return 0;
    }

    @Override
    public void report(int bits) {
    }

    @Override
    public void reset() {
    }
}