package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import java.nio.ByteBuffer;

public class TimeToSampleBox extends FullBox {

    private TimeToSampleBox.TimeToSampleEntry[] entries;

    public TimeToSampleBox(Header atom) {
        super(atom);
    }

    public static String fourcc() {
        return "stts";
    }

    public static TimeToSampleBox createTimeToSampleBox(TimeToSampleBox.TimeToSampleEntry[] timeToSamples) {
        TimeToSampleBox box = new TimeToSampleBox(new Header(fourcc()));
        box.entries = timeToSamples;
        return box;
    }

    @Override
    public void parse(ByteBuffer input) {
        super.parse(input);
        int foo = input.getInt();
        this.entries = new TimeToSampleBox.TimeToSampleEntry[foo];
        for (int i = 0; i < foo; i++) {
            this.entries[i] = new TimeToSampleBox.TimeToSampleEntry(input.getInt(), input.getInt());
        }
    }

    public TimeToSampleBox.TimeToSampleEntry[] getEntries() {
        return this.entries;
    }

    @Override
    public void doWrite(ByteBuffer out) {
        super.doWrite(out);
        out.putInt(this.entries.length);
        for (int i = 0; i < this.entries.length; i++) {
            TimeToSampleBox.TimeToSampleEntry timeToSampleEntry = this.entries[i];
            out.putInt(timeToSampleEntry.getSampleCount());
            out.putInt(timeToSampleEntry.getSampleDuration());
        }
    }

    @Override
    public int estimateSize() {
        return 16 + this.entries.length * 8;
    }

    public void setEntries(TimeToSampleBox.TimeToSampleEntry[] entries) {
        this.entries = entries;
    }

    public static class TimeToSampleEntry {

        int sampleCount;

        int sampleDuration;

        public TimeToSampleEntry(int sampleCount, int sampleDuration) {
            this.sampleCount = sampleCount;
            this.sampleDuration = sampleDuration;
        }

        public int getSampleCount() {
            return this.sampleCount;
        }

        public int getSampleDuration() {
            return this.sampleDuration;
        }

        public void setSampleDuration(int sampleDuration) {
            this.sampleDuration = sampleDuration;
        }

        public void setSampleCount(int sampleCount) {
            this.sampleCount = sampleCount;
        }

        public long getSegmentDuration() {
            return (long) (this.sampleCount * this.sampleDuration);
        }
    }
}