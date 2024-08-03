package com.github.alexthe666.citadel.repack.jaad.mp4.api.codec;

import com.github.alexthe666.citadel.repack.jaad.mp4.api.DecoderInfo;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.sampleentries.codec.AVCSpecificBox;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.sampleentries.codec.CodecSpecificBox;

public class AVCDecoderInfo extends DecoderInfo {

    private AVCSpecificBox box;

    public AVCDecoderInfo(CodecSpecificBox box) {
        this.box = (AVCSpecificBox) box;
    }

    public int getConfigurationVersion() {
        return this.box.getConfigurationVersion();
    }

    public int getProfile() {
        return this.box.getProfile();
    }

    public byte getProfileCompatibility() {
        return this.box.getProfileCompatibility();
    }

    public int getLevel() {
        return this.box.getLevel();
    }

    public int getLengthSize() {
        return this.box.getLengthSize();
    }

    public byte[][] getSequenceParameterSetNALUnits() {
        return this.box.getSequenceParameterSetNALUnits();
    }

    public byte[][] getPictureParameterSetNALUnits() {
        return this.box.getPictureParameterSetNALUnits();
    }
}