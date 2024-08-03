package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.encode;

import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.SliceType;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Size;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.MathUtil;

public class H264FixedRateControl implements RateControl {

    private static final int INIT_QP = 26;

    private int balance;

    private int perMb;

    private int curQp;

    public H264FixedRateControl(int bitsPer256) {
        this.perMb = bitsPer256;
        this.curQp = 26;
    }

    @Override
    public int startPicture(Size sz, int maxSize, SliceType sliceType) {
        return 26 + (sliceType == SliceType.P ? 4 : 0);
    }

    @Override
    public int initialQpDelta() {
        int qpDelta = this.balance < 0 ? (this.balance < -(this.perMb >> 1) ? 2 : 1) : (this.balance > this.perMb ? (this.balance > this.perMb << 2 ? -2 : -1) : 0);
        int prevQp = this.curQp;
        this.curQp = MathUtil.clip(this.curQp + qpDelta, 12, 30);
        return this.curQp - prevQp;
    }

    @Override
    public int accept(int bits) {
        this.balance = this.balance + (this.perMb - bits);
        return 0;
    }

    public void reset() {
        this.balance = 0;
        this.curQp = 26;
    }

    public int calcFrameSize(int nMB) {
        return (256 + nMB * (this.perMb + 9) >> 3) + (nMB >> 6);
    }

    public void setRate(int rate) {
        this.perMb = rate;
    }
}