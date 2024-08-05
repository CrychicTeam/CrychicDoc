package com.github.alexthe666.citadel.repack.jcodec.common.model;

import com.github.alexthe666.citadel.repack.jcodec.common.AudioFormat;
import java.nio.ByteBuffer;

public class AudioBuffer {

    protected ByteBuffer data;

    protected AudioFormat format;

    protected int nFrames;

    public AudioBuffer(ByteBuffer data, AudioFormat format, int nFrames) {
        this.data = data;
        this.format = format;
        this.nFrames = nFrames;
    }

    public ByteBuffer getData() {
        return this.data;
    }

    public AudioFormat getFormat() {
        return this.format;
    }

    public int getNFrames() {
        return this.nFrames;
    }
}