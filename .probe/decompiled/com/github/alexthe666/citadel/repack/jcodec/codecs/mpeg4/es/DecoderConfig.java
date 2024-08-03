package com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg4.es;

import java.nio.ByteBuffer;
import java.util.Collection;

public class DecoderConfig extends NodeDescriptor {

    private int objectType;

    private int bufSize;

    private int maxBitrate;

    private int avgBitrate;

    public DecoderConfig(int objectType, int bufSize, int maxBitrate, int avgBitrate, Collection<Descriptor> children) {
        super(tag(), children);
        this.objectType = objectType;
        this.bufSize = bufSize;
        this.maxBitrate = maxBitrate;
        this.avgBitrate = avgBitrate;
    }

    @Override
    protected void doWrite(ByteBuffer out) {
        out.put((byte) this.objectType);
        out.put((byte) 21);
        out.put((byte) (this.bufSize >> 16));
        out.putShort((short) this.bufSize);
        out.putInt(this.maxBitrate);
        out.putInt(this.avgBitrate);
        super.doWrite(out);
    }

    public static int tag() {
        return 4;
    }

    public int getObjectType() {
        return this.objectType;
    }

    public int getBufSize() {
        return this.bufSize;
    }

    public int getMaxBitrate() {
        return this.maxBitrate;
    }

    public int getAvgBitrate() {
        return this.avgBitrate;
    }
}