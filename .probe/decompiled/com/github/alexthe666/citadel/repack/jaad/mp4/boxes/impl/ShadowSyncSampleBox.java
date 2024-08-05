package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;

public class ShadowSyncSampleBox extends FullBox {

    private long[][] sampleNumbers;

    public ShadowSyncSampleBox() {
        super("Shadow Sync Sample Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        int entryCount = (int) in.readBytes(4);
        this.sampleNumbers = new long[entryCount][2];
        for (int i = 0; i < entryCount; i++) {
            this.sampleNumbers[i][0] = in.readBytes(4);
            this.sampleNumbers[i][1] = in.readBytes(4);
        }
    }

    public long[][] getSampleNumbers() {
        return this.sampleNumbers;
    }
}