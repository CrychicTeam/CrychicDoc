package com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg4.es;

import java.nio.ByteBuffer;
import java.util.Collection;

public class ES extends NodeDescriptor {

    private int trackId;

    public ES(int trackId, Collection<Descriptor> children) {
        super(tag(), children);
        this.trackId = trackId;
    }

    public static int tag() {
        return 3;
    }

    @Override
    protected void doWrite(ByteBuffer out) {
        out.putShort((short) this.trackId);
        out.put((byte) 0);
        super.doWrite(out);
    }

    public int getTrackId() {
        return this.trackId;
    }
}