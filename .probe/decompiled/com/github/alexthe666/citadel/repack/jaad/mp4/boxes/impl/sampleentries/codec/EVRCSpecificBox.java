package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.sampleentries.codec;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import java.io.IOException;

public class EVRCSpecificBox extends CodecSpecificBox {

    private int framesPerSample;

    public EVRCSpecificBox() {
        super("EVCR Specific Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        this.decodeCommon(in);
        this.framesPerSample = in.read();
    }

    public int getFramesPerSample() {
        return this.framesPerSample;
    }
}