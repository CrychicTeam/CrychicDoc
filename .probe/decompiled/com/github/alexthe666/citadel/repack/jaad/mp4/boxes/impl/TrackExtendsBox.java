package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;

public class TrackExtendsBox extends FullBox {

    private long trackID;

    private long defaultSampleDescriptionIndex;

    private long defaultSampleDuration;

    private long defaultSampleSize;

    private long defaultSampleFlags;

    public TrackExtendsBox() {
        super("Track Extends Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        this.trackID = in.readBytes(4);
        this.defaultSampleDescriptionIndex = in.readBytes(4);
        this.defaultSampleDuration = in.readBytes(4);
        this.defaultSampleSize = in.readBytes(4);
        this.defaultSampleFlags = in.readBytes(4);
    }

    public long getTrackID() {
        return this.trackID;
    }

    public long getDefaultSampleDescriptionIndex() {
        return this.defaultSampleDescriptionIndex;
    }

    public long getDefaultSampleDuration() {
        return this.defaultSampleDuration;
    }

    public long getDefaultSampleSize() {
        return this.defaultSampleSize;
    }

    public int getSampleDependsOn() {
        return (int) (this.defaultSampleFlags >> 24 & 3L);
    }

    public int getSampleIsDependedOn() {
        return (int) (this.defaultSampleFlags >> 22 & 3L);
    }

    public int getSampleHasRedundancy() {
        return (int) (this.defaultSampleFlags >> 20 & 3L);
    }

    public int getSamplePaddingValue() {
        return (int) (this.defaultSampleFlags >> 17 & 7L);
    }

    public boolean isSampleDifferenceSample() {
        return (this.defaultSampleFlags >> 16 & 1L) == 1L;
    }

    public int getSampleDegradationPriority() {
        return (int) (this.defaultSampleFlags & 65535L);
    }
}