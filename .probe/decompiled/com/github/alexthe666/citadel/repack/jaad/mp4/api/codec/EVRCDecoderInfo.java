package com.github.alexthe666.citadel.repack.jaad.mp4.api.codec;

import com.github.alexthe666.citadel.repack.jaad.mp4.api.DecoderInfo;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.sampleentries.codec.CodecSpecificBox;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.sampleentries.codec.EVRCSpecificBox;

public class EVRCDecoderInfo extends DecoderInfo {

    private EVRCSpecificBox box;

    public EVRCDecoderInfo(CodecSpecificBox box) {
        this.box = (EVRCSpecificBox) box;
    }

    public int getDecoderVersion() {
        return this.box.getDecoderVersion();
    }

    public long getVendor() {
        return this.box.getVendor();
    }

    public int getFramesPerSample() {
        return this.box.getFramesPerSample();
    }
}