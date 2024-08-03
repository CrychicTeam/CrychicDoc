package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;

public class AppleLosslessBox extends FullBox {

    private long maxSamplePerFrame;

    private long maxCodedFrameSize;

    private long bitRate;

    private long sampleRate;

    private int sampleSize;

    private int historyMult;

    private int initialHistory;

    private int kModifier;

    private int channels;

    public AppleLosslessBox() {
        super("Apple Lossless Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        this.maxSamplePerFrame = in.readBytes(4);
        in.skipBytes(1L);
        this.sampleSize = in.read();
        this.historyMult = in.read();
        this.initialHistory = in.read();
        this.kModifier = in.read();
        this.channels = in.read();
        in.skipBytes(2L);
        this.maxCodedFrameSize = in.readBytes(4);
        this.bitRate = in.readBytes(4);
        this.sampleRate = in.readBytes(4);
    }

    public long getMaxSamplePerFrame() {
        return this.maxSamplePerFrame;
    }

    public int getSampleSize() {
        return this.sampleSize;
    }

    public int getHistoryMult() {
        return this.historyMult;
    }

    public int getInitialHistory() {
        return this.initialHistory;
    }

    public int getkModifier() {
        return this.kModifier;
    }

    public int getChannels() {
        return this.channels;
    }

    public long getMaxCodedFrameSize() {
        return this.maxCodedFrameSize;
    }

    public long getBitRate() {
        return this.bitRate;
    }

    public long getSampleRate() {
        return this.sampleRate;
    }
}