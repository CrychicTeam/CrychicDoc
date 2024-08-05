package com.github.alexthe666.citadel.repack.jcodec.containers.mp4;

import com.github.alexthe666.citadel.repack.jcodec.common.model.Packet;
import com.github.alexthe666.citadel.repack.jcodec.common.model.TapeTimecode;
import java.nio.ByteBuffer;

public class MP4Packet extends Packet {

    private long mediaPts;

    private int entryNo;

    private long fileOff;

    private int size;

    private boolean psync;

    public static MP4Packet createMP4PacketWithTimecode(MP4Packet other, TapeTimecode timecode) {
        return createMP4Packet(other.data, other.pts, other.timescale, other.duration, other.frameNo, other.frameType, timecode, other.displayOrder, other.mediaPts, other.entryNo);
    }

    public static MP4Packet createMP4PacketWithData(MP4Packet other, ByteBuffer frm) {
        return createMP4Packet(frm, other.pts, other.timescale, other.duration, other.frameNo, other.frameType, other.tapeTimecode, other.displayOrder, other.mediaPts, other.entryNo);
    }

    public static MP4Packet createMP4Packet(ByteBuffer data, long pts, int timescale, long duration, long frameNo, Packet.FrameType iframe, TapeTimecode tapeTimecode, int displayOrder, long mediaPts, int entryNo) {
        return new MP4Packet(data, pts, timescale, duration, frameNo, iframe, tapeTimecode, displayOrder, mediaPts, entryNo, 0L, 0, false);
    }

    public MP4Packet(ByteBuffer data, long pts, int timescale, long duration, long frameNo, Packet.FrameType iframe, TapeTimecode tapeTimecode, int displayOrder, long mediaPts, int entryNo, long fileOff, int size, boolean psync) {
        super(data, pts, timescale, duration, frameNo, iframe, tapeTimecode, displayOrder);
        this.mediaPts = mediaPts;
        this.entryNo = entryNo;
        this.fileOff = fileOff;
        this.size = size;
        this.psync = psync;
    }

    public int getEntryNo() {
        return this.entryNo;
    }

    public long getMediaPts() {
        return this.mediaPts;
    }

    public long getFileOff() {
        return this.fileOff;
    }

    public int getSize() {
        return this.size;
    }

    public boolean isPsync() {
        return this.psync;
    }
}