package com.github.alexthe666.citadel.repack.jaad.mp4.api.codec;

import com.github.alexthe666.citadel.repack.jaad.mp4.api.DecoderInfo;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.sampleentries.codec.AC3SpecificBox;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.sampleentries.codec.CodecSpecificBox;

public class AC3DecoderInfo extends DecoderInfo {

    private AC3SpecificBox box;

    public AC3DecoderInfo(CodecSpecificBox box) {
        this.box = (AC3SpecificBox) box;
    }

    public boolean isLfeon() {
        return this.box.isLfeon();
    }

    public int getFscod() {
        return this.box.getFscod();
    }

    public int getBsmod() {
        return this.box.getBsmod();
    }

    public int getBsid() {
        return this.box.getBsid();
    }

    public int getBitRateCode() {
        return this.box.getBitRateCode();
    }

    public int getAcmod() {
        return this.box.getAcmod();
    }
}