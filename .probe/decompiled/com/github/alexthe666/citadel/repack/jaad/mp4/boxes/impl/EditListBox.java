package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;

public class EditListBox extends FullBox {

    private long[] segmentDuration;

    private long[] mediaTime;

    private double[] mediaRate;

    public EditListBox() {
        super("Edit List Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        int entryCount = (int) in.readBytes(4);
        int len = this.version == 1 ? 8 : 4;
        this.segmentDuration = new long[entryCount];
        this.mediaTime = new long[entryCount];
        this.mediaRate = new double[entryCount];
        for (int i = 0; i < entryCount; i++) {
            this.segmentDuration[i] = in.readBytes(len);
            this.mediaTime[i] = in.readBytes(len);
            this.mediaRate[i] = in.readFixedPoint(16, 16);
        }
    }

    public long[] getSegmentDuration() {
        return this.segmentDuration;
    }

    public long[] getMediaTime() {
        return this.mediaTime;
    }

    public double[] getMediaRate() {
        return this.mediaRate;
    }
}