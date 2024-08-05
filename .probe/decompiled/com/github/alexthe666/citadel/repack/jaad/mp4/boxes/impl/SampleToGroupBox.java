package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;

public class SampleToGroupBox extends FullBox {

    private long groupingType;

    private long[] sampleCount;

    private long[] groupDescriptionIndex;

    public SampleToGroupBox() {
        super("Sample To Group Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        this.groupingType = in.readBytes(4);
        int entryCount = (int) in.readBytes(4);
        this.sampleCount = new long[entryCount];
        this.groupDescriptionIndex = new long[entryCount];
        for (int i = 0; i < entryCount; i++) {
            this.sampleCount[i] = in.readBytes(4);
            this.groupDescriptionIndex[i] = in.readBytes(4);
        }
    }

    public long getGroupingType() {
        return this.groupingType;
    }

    public long[] getSampleCount() {
        return this.sampleCount;
    }

    public long[] getGroupDescriptionIndex() {
        return this.groupDescriptionIndex;
    }
}