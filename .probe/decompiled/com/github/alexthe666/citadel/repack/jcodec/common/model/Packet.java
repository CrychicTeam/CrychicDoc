package com.github.alexthe666.citadel.repack.jcodec.common.model;

import java.nio.ByteBuffer;
import java.util.Comparator;

public class Packet {

    public ByteBuffer data;

    public long pts;

    public int timescale;

    public long duration;

    public long frameNo;

    public Packet.FrameType frameType;

    public TapeTimecode tapeTimecode;

    public int displayOrder;

    public static final Comparator<Packet> FRAME_ASC = new Comparator<Packet>() {

        public int compare(Packet o1, Packet o2) {
            if (o1 == null && o2 == null) {
                return 0;
            } else if (o1 == null) {
                return -1;
            } else if (o2 == null) {
                return 1;
            } else {
                return o1.frameNo < o2.frameNo ? -1 : (o1.frameNo == o2.frameNo ? 0 : 1);
            }
        }
    };

    public static Packet createPacket(ByteBuffer data, long pts, int timescale, long duration, long frameNo, Packet.FrameType frameType, TapeTimecode tapeTimecode) {
        return new Packet(data, pts, timescale, duration, frameNo, frameType, tapeTimecode, 0);
    }

    public static Packet createPacketWithData(Packet other, ByteBuffer data) {
        return new Packet(data, other.pts, other.timescale, other.duration, other.frameNo, other.frameType, other.tapeTimecode, other.displayOrder);
    }

    public Packet(ByteBuffer data, long pts, int timescale, long duration, long frameNo, Packet.FrameType frameType, TapeTimecode tapeTimecode, int displayOrder) {
        this.data = data;
        this.pts = pts;
        this.timescale = timescale;
        this.duration = duration;
        this.frameNo = frameNo;
        this.frameType = frameType;
        this.tapeTimecode = tapeTimecode;
        this.displayOrder = displayOrder;
    }

    public ByteBuffer getData() {
        return this.data.duplicate();
    }

    public long getPts() {
        return this.pts;
    }

    public int getTimescale() {
        return this.timescale;
    }

    public long getDuration() {
        return this.duration;
    }

    public long getFrameNo() {
        return this.frameNo;
    }

    public void setTimescale(int timescale) {
        this.timescale = timescale;
    }

    public TapeTimecode getTapeTimecode() {
        return this.tapeTimecode;
    }

    public void setTapeTimecode(TapeTimecode tapeTimecode) {
        this.tapeTimecode = tapeTimecode;
    }

    public int getDisplayOrder() {
        return this.displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Packet.FrameType getFrameType() {
        return this.frameType;
    }

    public void setFrameType(Packet.FrameType frameType) {
        this.frameType = frameType;
    }

    public RationalLarge getPtsR() {
        return RationalLarge.R(this.pts, (long) this.timescale);
    }

    public double getPtsD() {
        return (double) this.pts / (double) this.timescale;
    }

    public double getDurationD() {
        return (double) this.duration / (double) this.timescale;
    }

    public void setData(ByteBuffer data) {
        this.data = data;
    }

    public void setPts(long pts) {
        this.pts = pts;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean isKeyFrame() {
        return this.frameType == Packet.FrameType.KEY;
    }

    public static enum FrameType {

        KEY, INTER, UNKNOWN
    }
}