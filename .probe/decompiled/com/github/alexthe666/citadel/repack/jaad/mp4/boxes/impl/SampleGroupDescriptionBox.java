package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.samplegroupentries.SampleGroupDescriptionEntry;
import java.io.IOException;

public class SampleGroupDescriptionBox extends FullBox {

    private long groupingType;

    private long defaultLength;

    private long descriptionLength;

    private SampleGroupDescriptionEntry[] entries;

    public SampleGroupDescriptionBox() {
        super("Sample Group Description Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        this.groupingType = in.readBytes(4);
        this.defaultLength = this.version == 1 ? in.readBytes(4) : 0L;
        int entryCount = (int) in.readBytes(4);
    }

    public long getGroupingType() {
        return this.groupingType;
    }

    public long getDefaultLength() {
        return this.defaultLength;
    }

    public long getDescriptionLength() {
        return this.descriptionLength;
    }
}