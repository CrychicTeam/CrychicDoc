package com.github.alexthe666.citadel.repack.jcodec.containers.mps;

import com.github.alexthe666.citadel.repack.jcodec.common.model.Packet;
import com.github.alexthe666.citadel.repack.jcodec.common.model.TapeTimecode;
import java.nio.ByteBuffer;

public class MPEGPacket extends Packet {

    private long offset;

    private ByteBuffer seq;

    private int gop;

    private int timecode;

    public MPEGPacket(ByteBuffer data, long pts, int timescale, long duration, long frameNo, Packet.FrameType keyFrame, TapeTimecode tapeTimecode) {
        super(data, pts, timescale, duration, frameNo, keyFrame, tapeTimecode, 0);
    }

    public long getOffset() {
        return this.offset;
    }

    public ByteBuffer getSeq() {
        return this.seq;
    }

    public int getGOP() {
        return this.gop;
    }

    public int getTimecode() {
        return this.timecode;
    }
}