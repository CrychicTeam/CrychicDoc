package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;

public class SubSampleInformationBox extends FullBox {

    private long[] sampleDelta;

    private long[][] subsampleSize;

    private int[][] subsamplePriority;

    private boolean[][] discardable;

    public SubSampleInformationBox() {
        super("Sub Sample Information Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        int len = this.version == 1 ? 4 : 2;
        int entryCount = (int) in.readBytes(4);
        this.sampleDelta = new long[entryCount];
        this.subsampleSize = new long[entryCount][];
        this.subsamplePriority = new int[entryCount][];
        this.discardable = new boolean[entryCount][];
        for (int i = 0; i < entryCount; i++) {
            this.sampleDelta[i] = in.readBytes(4);
            int subsampleCount = (int) in.readBytes(2);
            this.subsampleSize[i] = new long[subsampleCount];
            this.subsamplePriority[i] = new int[subsampleCount];
            this.discardable[i] = new boolean[subsampleCount];
            for (int j = 0; j < subsampleCount; j++) {
                this.subsampleSize[i][j] = in.readBytes(len);
                this.subsamplePriority[i][j] = in.read();
                this.discardable[i][j] = (in.read() & 1) == 1;
                in.skipBytes(4L);
            }
        }
    }

    public long[] getSampleDelta() {
        return this.sampleDelta;
    }

    public long[][] getSubsampleSize() {
        return this.subsampleSize;
    }

    public int[][] getSubsamplePriority() {
        return this.subsamplePriority;
    }

    public boolean[][] getDiscardable() {
        return this.discardable;
    }
}