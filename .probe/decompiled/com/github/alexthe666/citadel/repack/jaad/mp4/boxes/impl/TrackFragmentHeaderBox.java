package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;

public class TrackFragmentHeaderBox extends FullBox {

    private long trackID;

    private boolean baseDataOffsetPresent;

    private boolean sampleDescriptionIndexPresent;

    private boolean defaultSampleDurationPresent;

    private boolean defaultSampleSizePresent;

    private boolean defaultSampleFlagsPresent;

    private boolean durationIsEmpty;

    private long baseDataOffset;

    private long sampleDescriptionIndex;

    private long defaultSampleDuration;

    private long defaultSampleSize;

    private long defaultSampleFlags;

    public TrackFragmentHeaderBox() {
        super("Track Fragment Header Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        this.trackID = in.readBytes(4);
        this.baseDataOffsetPresent = (this.flags & 1) == 1;
        this.baseDataOffset = this.baseDataOffsetPresent ? in.readBytes(8) : 0L;
        this.sampleDescriptionIndexPresent = (this.flags & 2) == 2;
        this.sampleDescriptionIndex = this.sampleDescriptionIndexPresent ? in.readBytes(4) : 0L;
        this.defaultSampleDurationPresent = (this.flags & 8) == 8;
        this.defaultSampleDuration = this.defaultSampleDurationPresent ? in.readBytes(4) : 0L;
        this.defaultSampleSizePresent = (this.flags & 16) == 16;
        this.defaultSampleSize = this.defaultSampleSizePresent ? in.readBytes(4) : 0L;
        this.defaultSampleFlagsPresent = (this.flags & 32) == 32;
        this.defaultSampleFlags = this.defaultSampleFlagsPresent ? in.readBytes(4) : 0L;
        this.durationIsEmpty = (this.flags & 65536) == 65536;
    }

    public long getTrackID() {
        return this.trackID;
    }

    public boolean isBaseDataOffsetPresent() {
        return this.baseDataOffsetPresent;
    }

    public long getBaseDataOffset() {
        return this.baseDataOffset;
    }

    public boolean isSampleDescriptionIndexPresent() {
        return this.sampleDescriptionIndexPresent;
    }

    public long getSampleDescriptionIndex() {
        return this.sampleDescriptionIndex;
    }

    public boolean isDefaultSampleDurationPresent() {
        return this.defaultSampleDurationPresent;
    }

    public long getDefaultSampleDuration() {
        return this.defaultSampleDuration;
    }

    public boolean isDefaultSampleSizePresent() {
        return this.defaultSampleSizePresent;
    }

    public long getDefaultSampleSize() {
        return this.defaultSampleSize;
    }

    public boolean isDefaultSampleFlagsPresent() {
        return this.defaultSampleFlagsPresent;
    }

    public long getDefaultSampleFlags() {
        return this.defaultSampleFlags;
    }

    public boolean isDurationIsEmpty() {
        return this.durationIsEmpty;
    }
}