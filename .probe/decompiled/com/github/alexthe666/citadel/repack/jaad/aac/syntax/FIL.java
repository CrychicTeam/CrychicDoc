package com.github.alexthe666.citadel.repack.jaad.aac.syntax;

import com.github.alexthe666.citadel.repack.jaad.aac.AACException;
import com.github.alexthe666.citadel.repack.jaad.aac.SampleFrequency;

class FIL extends Element implements Constants {

    private static final int TYPE_FILL = 0;

    private static final int TYPE_FILL_DATA = 1;

    private static final int TYPE_EXT_DATA_ELEMENT = 2;

    private static final int TYPE_DYNAMIC_RANGE = 11;

    private static final int TYPE_SBR_DATA = 13;

    private static final int TYPE_SBR_DATA_CRC = 14;

    private final boolean downSampledSBR;

    private FIL.DynamicRangeInfo dri;

    FIL(boolean downSampledSBR) {
        this.downSampledSBR = downSampledSBR;
    }

    void decode(BitStream in, Element prev, SampleFrequency sf, boolean sbrEnabled, boolean smallFrames) throws AACException {
        int count = in.readBits(4);
        if (count == 15) {
            count += in.readBits(8) - 1;
        }
        count *= 8;
        int cpy = count;
        int pos = in.getPosition();
        while (count > 0) {
            count = this.decodeExtensionPayload(in, count, prev, sf, sbrEnabled, smallFrames);
        }
        int pos2 = in.getPosition() - pos;
        int bitsLeft = cpy - pos2;
        if (bitsLeft > 0) {
            in.skipBits(pos2);
        } else if (bitsLeft < 0) {
            throw new AACException("FIL element overread: " + bitsLeft);
        }
    }

    private int decodeExtensionPayload(BitStream in, int count, Element prev, SampleFrequency sf, boolean sbrEnabled, boolean smallFrames) throws AACException {
        int type = in.readBits(4);
        int ret = count - 4;
        return switch(type) {
            case 11 ->
                this.decodeDynamicRangeInfo(in, ret);
            case 13, 14 ->
                {
                    if (sbrEnabled) {
                    } else {
                        in.skipBits(ret);
                        ret = 0;
                    }
                }
            default ->
                {
                }
        };
    }

    private int decodeDynamicRangeInfo(BitStream in, int count) throws AACException {
        if (this.dri == null) {
            this.dri = new FIL.DynamicRangeInfo();
        }
        int ret = count;
        int bandCount = 1;
        if (this.dri.pceTagPresent = in.readBool()) {
            this.dri.pceInstanceTag = in.readBits(4);
            this.dri.tagReservedBits = in.readBits(4);
        }
        if (this.dri.excludedChannelsPresent = in.readBool()) {
            ret = count - this.decodeExcludedChannels(in);
        }
        if (this.dri.bandsPresent = in.readBool()) {
            this.dri.bandsIncrement = in.readBits(4);
            this.dri.interpolationScheme = in.readBits(4);
            ret -= 8;
            bandCount += this.dri.bandsIncrement;
            this.dri.bandTop = new int[bandCount];
            for (int i = 0; i < bandCount; i++) {
                this.dri.bandTop[i] = in.readBits(8);
                ret -= 8;
            }
        }
        if (this.dri.progRefLevelPresent = in.readBool()) {
            this.dri.progRefLevel = in.readBits(7);
            this.dri.progRefLevelReservedBits = in.readBits(1);
            ret -= 8;
        }
        this.dri.dynRngSgn = new boolean[bandCount];
        this.dri.dynRngCtl = new int[bandCount];
        for (int i = 0; i < bandCount; i++) {
            this.dri.dynRngSgn[i] = in.readBool();
            this.dri.dynRngCtl[i] = in.readBits(7);
            ret -= 8;
        }
        return ret;
    }

    private int decodeExcludedChannels(BitStream in) throws AACException {
        int exclChs = 0;
        do {
            for (int i = 0; i < 7; i++) {
                this.dri.excludeMask[exclChs] = in.readBool();
                exclChs++;
            }
        } while (exclChs < 57 && in.readBool());
        return exclChs / 7 * 8;
    }

    public static class DynamicRangeInfo {

        private static final int MAX_NBR_BANDS = 7;

        private final boolean[] excludeMask = new boolean[7];

        private final boolean[] additionalExcludedChannels = new boolean[7];

        private boolean pceTagPresent;

        private int pceInstanceTag;

        private int tagReservedBits;

        private boolean excludedChannelsPresent;

        private boolean bandsPresent;

        private int bandsIncrement;

        private int interpolationScheme;

        private int[] bandTop;

        private boolean progRefLevelPresent;

        private int progRefLevel;

        private int progRefLevelReservedBits;

        private boolean[] dynRngSgn;

        private int[] dynRngCtl;
    }
}