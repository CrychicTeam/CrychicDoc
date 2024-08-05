package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;

public class SampleDependencyTypeBox extends FullBox {

    private int[] sampleDependsOn;

    private int[] sampleIsDependedOn;

    private int[] sampleHasRedundancy;

    public SampleDependencyTypeBox() {
        super("Sample Dependency Type Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        long sampleCount = -1L;
        if (this.parent.hasChild(1937011578L)) {
            sampleCount = (long) ((SampleSizeBox) this.parent.getChild(1937011578L)).getSampleCount();
        }
        this.sampleHasRedundancy = new int[(int) sampleCount];
        this.sampleIsDependedOn = new int[(int) sampleCount];
        this.sampleDependsOn = new int[(int) sampleCount];
        for (int i = 0; (long) i < sampleCount; i++) {
            byte b = (byte) in.read();
            this.sampleHasRedundancy[i] = b & 3;
            this.sampleIsDependedOn[i] = b >> 2 & 3;
            this.sampleDependsOn[i] = b >> 4 & 3;
        }
    }

    public int[] getSampleDependsOn() {
        return this.sampleDependsOn;
    }

    public int[] getSampleIsDependedOn() {
        return this.sampleIsDependedOn;
    }

    public int[] getSampleHasRedundancy() {
        return this.sampleHasRedundancy;
    }
}