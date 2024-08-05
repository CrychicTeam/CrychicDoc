package com.github.alexthe666.citadel.repack.jaad.aac.syntax;

import com.github.alexthe666.citadel.repack.jaad.aac.AACException;
import com.github.alexthe666.citadel.repack.jaad.aac.Profile;
import com.github.alexthe666.citadel.repack.jaad.aac.SampleFrequency;

public class PCE extends Element {

    private static final int MAX_FRONT_CHANNEL_ELEMENTS = 16;

    private static final int MAX_SIDE_CHANNEL_ELEMENTS = 16;

    private static final int MAX_BACK_CHANNEL_ELEMENTS = 16;

    private static final int MAX_LFE_CHANNEL_ELEMENTS = 4;

    private static final int MAX_ASSOC_DATA_ELEMENTS = 8;

    private static final int MAX_VALID_CC_ELEMENTS = 16;

    private Profile profile;

    private SampleFrequency sampleFrequency;

    private int frontChannelElementsCount;

    private int sideChannelElementsCount;

    private int backChannelElementsCount;

    private int lfeChannelElementsCount;

    private int assocDataElementsCount;

    private int validCCElementsCount;

    private boolean monoMixdown;

    private boolean stereoMixdown;

    private boolean matrixMixdownIDXPresent;

    private int monoMixdownElementNumber;

    private int stereoMixdownElementNumber;

    private int matrixMixdownIDX;

    private boolean pseudoSurround;

    private final PCE.TaggedElement[] frontElements = new PCE.TaggedElement[16];

    private final PCE.TaggedElement[] sideElements = new PCE.TaggedElement[16];

    private final PCE.TaggedElement[] backElements = new PCE.TaggedElement[16];

    private final int[] lfeElementTags = new int[4];

    private final int[] assocDataElementTags = new int[8];

    private final PCE.CCE[] ccElements = new PCE.CCE[16];

    private byte[] commentFieldData;

    public PCE() {
        this.sampleFrequency = SampleFrequency.SAMPLE_FREQUENCY_NONE;
    }

    public void decode(BitStream in) throws AACException {
        this.readElementInstanceTag(in);
        this.profile = Profile.forInt(1 + in.readBits(2));
        this.sampleFrequency = SampleFrequency.forInt(in.readBits(4));
        this.frontChannelElementsCount = in.readBits(4);
        this.sideChannelElementsCount = in.readBits(4);
        this.backChannelElementsCount = in.readBits(4);
        this.lfeChannelElementsCount = in.readBits(2);
        this.assocDataElementsCount = in.readBits(3);
        this.validCCElementsCount = in.readBits(4);
        if (this.monoMixdown = in.readBool()) {
            Constants.LOGGER.warning("mono mixdown present, but not yet supported");
            this.monoMixdownElementNumber = in.readBits(4);
        }
        if (this.stereoMixdown = in.readBool()) {
            Constants.LOGGER.warning("stereo mixdown present, but not yet supported");
            this.stereoMixdownElementNumber = in.readBits(4);
        }
        if (this.matrixMixdownIDXPresent = in.readBool()) {
            Constants.LOGGER.warning("matrix mixdown present, but not yet supported");
            this.matrixMixdownIDX = in.readBits(2);
            this.pseudoSurround = in.readBool();
        }
        this.readTaggedElementArray(this.frontElements, in, this.frontChannelElementsCount);
        this.readTaggedElementArray(this.sideElements, in, this.sideChannelElementsCount);
        this.readTaggedElementArray(this.backElements, in, this.backChannelElementsCount);
        for (int i = 0; i < this.lfeChannelElementsCount; i++) {
            this.lfeElementTags[i] = in.readBits(4);
        }
        for (int var4 = 0; var4 < this.assocDataElementsCount; var4++) {
            this.assocDataElementTags[var4] = in.readBits(4);
        }
        for (int var5 = 0; var5 < this.validCCElementsCount; var5++) {
            this.ccElements[var5] = new PCE.CCE(in.readBool(), in.readBits(4));
        }
        in.byteAlign();
        int commentFieldBytes = in.readBits(8);
        this.commentFieldData = new byte[commentFieldBytes];
        for (int var6 = 0; var6 < commentFieldBytes; var6++) {
            this.commentFieldData[var6] = (byte) in.readBits(8);
        }
    }

    private void readTaggedElementArray(PCE.TaggedElement[] te, BitStream in, int len) throws AACException {
        for (int i = 0; i < len; i++) {
            te[i] = new PCE.TaggedElement(in.readBool(), in.readBits(4));
        }
    }

    public Profile getProfile() {
        return this.profile;
    }

    public SampleFrequency getSampleFrequency() {
        return this.sampleFrequency;
    }

    public int getChannelCount() {
        int count = this.lfeChannelElementsCount + this.assocDataElementsCount;
        for (int n = 0; n < this.frontChannelElementsCount; n++) {
            count += this.frontElements[n].isCPE ? 2 : 1;
        }
        for (int n = 0; n < this.sideChannelElementsCount; n++) {
            count += this.sideElements[n].isCPE ? 2 : 1;
        }
        for (int n = 0; n < this.backChannelElementsCount; n++) {
            count += this.backElements[n].isCPE ? 2 : 1;
        }
        return count;
    }

    public static class CCE {

        private final boolean isIndSW;

        private final int tag;

        public CCE(boolean isIndSW, int tag) {
            this.isIndSW = isIndSW;
            this.tag = tag;
        }

        public boolean isIsIndSW() {
            return this.isIndSW;
        }

        public int getTag() {
            return this.tag;
        }
    }

    public static class TaggedElement {

        private final boolean isCPE;

        private final int tag;

        public TaggedElement(boolean isCPE, int tag) {
            this.isCPE = isCPE;
            this.tag = tag;
        }

        public boolean isIsCPE() {
            return this.isCPE;
        }

        public int getTag() {
            return this.tag;
        }
    }
}