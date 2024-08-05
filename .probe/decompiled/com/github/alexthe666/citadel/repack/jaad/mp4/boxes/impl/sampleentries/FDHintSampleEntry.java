package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.sampleentries;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import java.io.IOException;

public class FDHintSampleEntry extends SampleEntry {

    private int hintTrackVersion;

    private int highestCompatibleVersion;

    private int partitionEntryID;

    private double fecOverhead;

    public FDHintSampleEntry() {
        super("FD Hint Sample Entry");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        this.hintTrackVersion = (int) in.readBytes(2);
        this.highestCompatibleVersion = (int) in.readBytes(2);
        this.partitionEntryID = (int) in.readBytes(2);
        this.fecOverhead = in.readFixedPoint(8, 8);
        this.readChildren(in);
    }

    public int getPartitionEntryID() {
        return this.partitionEntryID;
    }

    public double getFECOverhead() {
        return this.fecOverhead;
    }
}