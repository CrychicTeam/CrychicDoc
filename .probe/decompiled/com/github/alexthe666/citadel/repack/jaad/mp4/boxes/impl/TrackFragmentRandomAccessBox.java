package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;

public class TrackFragmentRandomAccessBox extends FullBox {

    private long trackID;

    private int entryCount;

    private long[] times;

    private long[] moofOffsets;

    private long[] trafNumbers;

    private long[] trunNumbers;

    private long[] sampleNumbers;

    public TrackFragmentRandomAccessBox() {
        super("Track Fragment Random Access Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        this.trackID = in.readBytes(4);
        long l = in.readBytes(4);
        int trafNumberLen = (int) (l >> 4 & 3L) + 1;
        int trunNumberLen = (int) (l >> 2 & 3L) + 1;
        int sampleNumberLen = (int) (l & 3L) + 1;
        this.entryCount = (int) in.readBytes(4);
        int len = this.version == 1 ? 8 : 4;
        for (int i = 0; i < this.entryCount; i++) {
            this.times[i] = in.readBytes(len);
            this.moofOffsets[i] = in.readBytes(len);
            this.trafNumbers[i] = in.readBytes(trafNumberLen);
            this.trunNumbers[i] = in.readBytes(trunNumberLen);
            this.sampleNumbers[i] = in.readBytes(sampleNumberLen);
        }
    }

    public long getTrackID() {
        return this.trackID;
    }

    public int getEntryCount() {
        return this.entryCount;
    }

    public long[] getTimes() {
        return this.times;
    }

    public long[] getMoofOffsets() {
        return this.moofOffsets;
    }

    public long[] getTrafNumbers() {
        return this.trafNumbers;
    }

    public long[] getTrunNumbers() {
        return this.trunNumbers;
    }

    public long[] getSampleNumbers() {
        return this.sampleNumbers;
    }
}