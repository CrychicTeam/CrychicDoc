package com.github.alexthe666.citadel.repack.jaad.mp4.api.codec;

import com.github.alexthe666.citadel.repack.jaad.mp4.api.DecoderInfo;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.sampleentries.codec.AMRSpecificBox;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.sampleentries.codec.CodecSpecificBox;

public class AMRDecoderInfo extends DecoderInfo {

    private AMRSpecificBox box;

    public AMRDecoderInfo(CodecSpecificBox box) {
        this.box = (AMRSpecificBox) box;
    }

    public int getDecoderVersion() {
        return this.box.getDecoderVersion();
    }

    public long getVendor() {
        return this.box.getVendor();
    }

    public int getModeSet() {
        return this.box.getModeSet();
    }

    public int getModeChangePeriod() {
        return this.box.getModeChangePeriod();
    }

    public int getFramesPerSample() {
        return this.box.getFramesPerSample();
    }
}