package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.sampleentries;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import java.io.IOException;

public class AudioSampleEntry extends SampleEntry {

    private int channelCount;

    private int sampleSize;

    private int sampleRate;

    public AudioSampleEntry(String name) {
        super(name);
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        in.skipBytes(8L);
        this.channelCount = (int) in.readBytes(2);
        this.sampleSize = (int) in.readBytes(2);
        in.skipBytes(2L);
        in.skipBytes(2L);
        this.sampleRate = (int) in.readBytes(2);
        in.skipBytes(2L);
        this.readChildren(in);
    }

    public int getChannelCount() {
        return this.channelCount;
    }

    public int getSampleRate() {
        return this.sampleRate;
    }

    public int getSampleSize() {
        return this.sampleSize;
    }
}