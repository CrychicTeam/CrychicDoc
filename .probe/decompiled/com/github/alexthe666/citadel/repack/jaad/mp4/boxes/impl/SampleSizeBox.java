package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;
import java.util.Arrays;

public class SampleSizeBox extends FullBox {

    private long sampleCount;

    private long[] sampleSizes;

    public SampleSizeBox() {
        super("Sample Size Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        boolean compact = this.type == 1937013298L;
        int sampleSize;
        if (compact) {
            in.skipBytes(3L);
            sampleSize = in.read();
        } else {
            sampleSize = (int) in.readBytes(4);
        }
        this.sampleCount = in.readBytes(4);
        this.sampleSizes = new long[(int) this.sampleCount];
        if (compact) {
            if (sampleSize == 4) {
                for (int i = 0; (long) i < this.sampleCount; i += 2) {
                    int x = in.read();
                    this.sampleSizes[i] = (long) (x >> 4 & 15);
                    this.sampleSizes[i + 1] = (long) (x & 15);
                }
            } else {
                this.readSizes(in, sampleSize / 8);
            }
        } else if (sampleSize == 0) {
            this.readSizes(in, 4);
        } else {
            Arrays.fill(this.sampleSizes, (long) sampleSize);
        }
    }

    private void readSizes(MP4InputStream in, int len) throws IOException {
        for (int i = 0; (long) i < this.sampleCount; i++) {
            this.sampleSizes[i] = in.readBytes(len);
        }
    }

    public int getSampleCount() {
        return (int) this.sampleCount;
    }

    public long[] getSampleSizes() {
        return this.sampleSizes;
    }
}