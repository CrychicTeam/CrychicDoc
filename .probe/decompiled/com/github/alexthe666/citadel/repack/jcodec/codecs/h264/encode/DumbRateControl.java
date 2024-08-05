package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.encode;

import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.SliceType;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Size;

public class DumbRateControl implements RateControl {

    private static final int QP = 20;

    private int bitsPerMb;

    private int totalQpDelta;

    private boolean justSwitched;

    @Override
    public int accept(int bits) {
        if (bits >= this.bitsPerMb) {
            this.totalQpDelta++;
            this.justSwitched = true;
            return 1;
        } else if (this.totalQpDelta > 0 && !this.justSwitched && this.bitsPerMb - bits > this.bitsPerMb >> 3) {
            this.totalQpDelta--;
            this.justSwitched = true;
            return -1;
        } else {
            this.justSwitched = false;
            return 0;
        }
    }

    @Override
    public int startPicture(Size sz, int maxSize, SliceType sliceType) {
        int totalMb = (sz.getWidth() + 15 >> 4) * (sz.getHeight() + 15 >> 4);
        this.bitsPerMb = (maxSize << 3) / totalMb;
        this.totalQpDelta = 0;
        this.justSwitched = false;
        return 20 + (sliceType == SliceType.P ? 6 : 0);
    }

    @Override
    public int initialQpDelta() {
        return 0;
    }
}