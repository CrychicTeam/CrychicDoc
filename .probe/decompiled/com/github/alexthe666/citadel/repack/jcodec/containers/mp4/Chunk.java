package com.github.alexthe666.citadel.repack.jcodec.containers.mp4;

public class Chunk {

    private long offset;

    private long startTv;

    private int sampleCount;

    private int sampleSize;

    private int[] sampleSizes;

    private int sampleDur;

    private int[] sampleDurs;

    private int entry;

    public Chunk(long offset, long startTv, int sampleCount, int sampleSize, int[] sampleSizes, int sampleDur, int[] sampleDurs, int entry) {
        this.offset = offset;
        this.startTv = startTv;
        this.sampleCount = sampleCount;
        this.sampleSize = sampleSize;
        this.sampleSizes = sampleSizes;
        this.sampleDur = sampleDur;
        this.sampleDurs = sampleDurs;
        this.entry = entry;
    }

    public long getOffset() {
        return this.offset;
    }

    public long getStartTv() {
        return this.startTv;
    }

    public int getSampleCount() {
        return this.sampleCount;
    }

    public int getSampleSize() {
        return this.sampleSize;
    }

    public int[] getSampleSizes() {
        return this.sampleSizes;
    }

    public int getSampleDur() {
        return this.sampleDur;
    }

    public int[] getSampleDurs() {
        return this.sampleDurs;
    }

    public int getEntry() {
        return this.entry;
    }

    public int getDuration() {
        if (this.sampleDur > 0) {
            return this.sampleDur * this.sampleCount;
        } else {
            int sum = 0;
            for (int j = 0; j < this.sampleDurs.length; j++) {
                int i = this.sampleDurs[j];
                sum += i;
            }
            return sum;
        }
    }

    public long getSize() {
        if (this.sampleSize > 0) {
            return (long) (this.sampleSize * this.sampleCount);
        } else {
            long sum = 0L;
            for (int j = 0; j < this.sampleSizes.length; j++) {
                int i = this.sampleSizes[j];
                sum += (long) i;
            }
            return sum;
        }
    }
}