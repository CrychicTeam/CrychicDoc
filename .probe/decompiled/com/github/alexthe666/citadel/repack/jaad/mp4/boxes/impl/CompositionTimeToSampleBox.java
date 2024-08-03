package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;

public class CompositionTimeToSampleBox extends FullBox {

    private long[] sampleCounts;

    private long[] sampleOffsets;

    public CompositionTimeToSampleBox() {
        super("Time To Sample Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        int entryCount = (int) in.readBytes(4);
        this.sampleCounts = new long[entryCount];
        this.sampleOffsets = new long[entryCount];
        for (int i = 0; i < entryCount; i++) {
            this.sampleCounts[i] = in.readBytes(4);
            this.sampleOffsets[i] = in.readBytes(4);
        }
    }

    public long[] getSampleCounts() {
        return this.sampleCounts;
    }

    public long[] getSampleOffsets() {
        return this.sampleOffsets;
    }
}