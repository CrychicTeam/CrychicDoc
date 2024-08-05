package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;

public class TrackFragmentRunBox extends FullBox {

    private int sampleCount;

    private boolean dataOffsetPresent;

    private boolean firstSampleFlagsPresent;

    private long dataOffset;

    private long firstSampleFlags;

    private boolean sampleDurationPresent;

    private boolean sampleSizePresent;

    private boolean sampleFlagsPresent;

    private boolean sampleCompositionTimeOffsetPresent;

    private long[] sampleDuration;

    private long[] sampleSize;

    private long[] sampleFlags;

    private long[] sampleCompositionTimeOffset;

    public TrackFragmentRunBox() {
        super("Track Fragment Run Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        this.sampleCount = (int) in.readBytes(4);
        this.dataOffsetPresent = (this.flags & 1) == 1;
        if (this.dataOffsetPresent) {
            this.dataOffset = in.readBytes(4);
        }
        this.firstSampleFlagsPresent = (this.flags & 4) == 4;
        if (this.firstSampleFlagsPresent) {
            this.firstSampleFlags = in.readBytes(4);
        }
        this.sampleDurationPresent = (this.flags & 256) == 256;
        if (this.sampleDurationPresent) {
            this.sampleDuration = new long[this.sampleCount];
        }
        this.sampleSizePresent = (this.flags & 512) == 512;
        if (this.sampleSizePresent) {
            this.sampleSize = new long[this.sampleCount];
        }
        this.sampleFlagsPresent = (this.flags & 1024) == 1024;
        if (this.sampleFlagsPresent) {
            this.sampleFlags = new long[this.sampleCount];
        }
        this.sampleCompositionTimeOffsetPresent = (this.flags & 2048) == 2048;
        if (this.sampleCompositionTimeOffsetPresent) {
            this.sampleCompositionTimeOffset = new long[this.sampleCount];
        }
        for (int i = 0; i < this.sampleCount && this.getLeft(in) > 0L; i++) {
            if (this.sampleDurationPresent) {
                this.sampleDuration[i] = in.readBytes(4);
            }
            if (this.sampleSizePresent) {
                this.sampleSize[i] = in.readBytes(4);
            }
            if (this.sampleFlagsPresent) {
                this.sampleFlags[i] = in.readBytes(4);
            }
            if (this.sampleCompositionTimeOffsetPresent) {
                this.sampleCompositionTimeOffset[i] = in.readBytes(4);
            }
        }
    }

    public int getSampleCount() {
        return this.sampleCount;
    }

    public boolean isDataOffsetPresent() {
        return this.dataOffsetPresent;
    }

    public long getDataOffset() {
        return this.dataOffset;
    }

    public boolean isFirstSampleFlagsPresent() {
        return this.firstSampleFlagsPresent;
    }

    public long getFirstSampleFlags() {
        return this.firstSampleFlags;
    }

    public boolean isSampleDurationPresent() {
        return this.sampleDurationPresent;
    }

    public long[] getSampleDuration() {
        return this.sampleDuration;
    }

    public boolean isSampleSizePresent() {
        return this.sampleSizePresent;
    }

    public long[] getSampleSize() {
        return this.sampleSize;
    }

    public boolean isSampleFlagsPresent() {
        return this.sampleFlagsPresent;
    }

    public long[] getSampleFlags() {
        return this.sampleFlags;
    }

    public boolean isSampleCompositionTimeOffsetPresent() {
        return this.sampleCompositionTimeOffsetPresent;
    }

    public long[] getSampleCompositionTimeOffset() {
        return this.sampleCompositionTimeOffset;
    }
}