package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;

public class SampleToChunkBox extends FullBox {

    private long[] firstChunks;

    private long[] samplesPerChunk;

    private long[] sampleDescriptionIndex;

    public SampleToChunkBox() {
        super("Sample To Chunk Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        int entryCount = (int) in.readBytes(4);
        this.firstChunks = new long[entryCount];
        this.samplesPerChunk = new long[entryCount];
        this.sampleDescriptionIndex = new long[entryCount];
        for (int i = 0; i < entryCount; i++) {
            this.firstChunks[i] = in.readBytes(4);
            this.samplesPerChunk[i] = in.readBytes(4);
            this.sampleDescriptionIndex[i] = in.readBytes(4);
        }
    }

    public long[] getFirstChunks() {
        return this.firstChunks;
    }

    public long[] getSamplesPerChunk() {
        return this.samplesPerChunk;
    }

    public long[] getSampleDescriptionIndex() {
        return this.sampleDescriptionIndex;
    }
}