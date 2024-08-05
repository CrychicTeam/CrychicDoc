package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.sampleentries.codec;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import java.io.IOException;

public class AMRSpecificBox extends CodecSpecificBox {

    private int modeSet;

    private int modeChangePeriod;

    private int framesPerSample;

    public AMRSpecificBox() {
        super("AMR Specific Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        this.decodeCommon(in);
        this.modeSet = (int) in.readBytes(2);
        this.modeChangePeriod = in.read();
        this.framesPerSample = in.read();
    }

    public int getModeSet() {
        return this.modeSet;
    }

    public int getModeChangePeriod() {
        return this.modeChangePeriod;
    }

    public int getFramesPerSample() {
        return this.framesPerSample;
    }
}