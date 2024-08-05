package com.github.alexthe666.citadel.repack.jaad.mp4.api.codec;

import com.github.alexthe666.citadel.repack.jaad.mp4.api.DecoderInfo;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.sampleentries.codec.CodecSpecificBox;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.sampleentries.codec.H263SpecificBox;

public class H263DecoderInfo extends DecoderInfo {

    private H263SpecificBox box;

    public H263DecoderInfo(CodecSpecificBox box) {
        this.box = (H263SpecificBox) box;
    }

    public int getDecoderVersion() {
        return this.box.getDecoderVersion();
    }

    public long getVendor() {
        return this.box.getVendor();
    }

    public int getLevel() {
        return this.box.getLevel();
    }

    public int getProfile() {
        return this.box.getProfile();
    }
}