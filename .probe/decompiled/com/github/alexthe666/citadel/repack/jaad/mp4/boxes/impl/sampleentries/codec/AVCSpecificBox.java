package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.sampleentries.codec;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import java.io.IOException;

public class AVCSpecificBox extends CodecSpecificBox {

    private int configurationVersion;

    private int profile;

    private int level;

    private int lengthSize;

    private byte profileCompatibility;

    private byte[][] sequenceParameterSetNALUnit;

    private byte[][] pictureParameterSetNALUnit;

    public AVCSpecificBox() {
        super("AVC Specific Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        this.configurationVersion = in.read();
        this.profile = in.read();
        this.profileCompatibility = (byte) in.read();
        this.level = in.read();
        this.lengthSize = (in.read() & 3) + 1;
        int sequenceParameterSets = in.read() & 31;
        this.sequenceParameterSetNALUnit = new byte[sequenceParameterSets][];
        for (int i = 0; i < sequenceParameterSets; i++) {
            int len = (int) in.readBytes(2);
            this.sequenceParameterSetNALUnit[i] = new byte[len];
            in.readBytes(this.sequenceParameterSetNALUnit[i]);
        }
        int pictureParameterSets = in.read();
        this.pictureParameterSetNALUnit = new byte[pictureParameterSets][];
        for (int i = 0; i < pictureParameterSets; i++) {
            int len = (int) in.readBytes(2);
            this.pictureParameterSetNALUnit[i] = new byte[len];
            in.readBytes(this.pictureParameterSetNALUnit[i]);
        }
    }

    public int getConfigurationVersion() {
        return this.configurationVersion;
    }

    public int getProfile() {
        return this.profile;
    }

    public byte getProfileCompatibility() {
        return this.profileCompatibility;
    }

    public int getLevel() {
        return this.level;
    }

    public int getLengthSize() {
        return this.lengthSize;
    }

    public byte[][] getSequenceParameterSetNALUnits() {
        return this.sequenceParameterSetNALUnit;
    }

    public byte[][] getPictureParameterSetNALUnits() {
        return this.pictureParameterSetNALUnit;
    }
}