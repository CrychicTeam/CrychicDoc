package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;

public class HintMediaHeaderBox extends FullBox {

    private long maxPDUsize;

    private long avgPDUsize;

    private long maxBitrate;

    private long avgBitrate;

    public HintMediaHeaderBox() {
        super("Hint Media Header Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        this.maxPDUsize = in.readBytes(2);
        this.avgPDUsize = in.readBytes(2);
        this.maxBitrate = in.readBytes(4);
        this.avgBitrate = in.readBytes(4);
        in.skipBytes(4L);
    }

    public long getMaxPDUsize() {
        return this.maxPDUsize;
    }

    public long getAveragePDUsize() {
        return this.avgPDUsize;
    }

    public long getMaxBitrate() {
        return this.maxBitrate;
    }

    public long getAverageBitrate() {
        return this.avgBitrate;
    }
}