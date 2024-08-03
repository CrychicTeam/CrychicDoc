package com.github.alexthe666.citadel.repack.jcodec.common.model;

import com.github.alexthe666.citadel.repack.jcodec.common.StringUtils;

public class TapeTimecode {

    public static final TapeTimecode ZERO_TAPE_TIMECODE = new TapeTimecode((short) 0, (byte) 0, (byte) 0, (byte) 0, false, 0);

    private final short hour;

    private final byte minute;

    private final byte second;

    private final byte frame;

    private final boolean dropFrame;

    private final int tapeFps;

    public TapeTimecode(short hour, byte minute, byte second, byte frame, boolean dropFrame, int tapeFps) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.frame = frame;
        this.dropFrame = dropFrame;
        this.tapeFps = tapeFps;
    }

    public short getHour() {
        return this.hour;
    }

    public byte getMinute() {
        return this.minute;
    }

    public byte getSecond() {
        return this.second;
    }

    public byte getFrame() {
        return this.frame;
    }

    public boolean isDropFrame() {
        return this.dropFrame;
    }

    public int getTapeFps() {
        return this.tapeFps;
    }

    public String toString() {
        return StringUtils.zeroPad2(this.hour) + ":" + StringUtils.zeroPad2(this.minute) + ":" + StringUtils.zeroPad2(this.second) + (this.dropFrame ? ";" : ":") + StringUtils.zeroPad2(this.frame);
    }

    public static TapeTimecode tapeTimecode(long frame, boolean dropFrame, int tapeFps) {
        if (dropFrame) {
            long D = frame / 17982L;
            long M = frame % 17982L;
            frame += 18L * D + 2L * ((M - 2L) / 1798L);
        }
        long sec = frame / (long) tapeFps;
        return new TapeTimecode((short) ((int) (sec / 3600L)), (byte) ((int) (sec / 60L % 60L)), (byte) ((int) (sec % 60L)), (byte) ((int) (frame % (long) tapeFps)), dropFrame, tapeFps);
    }
}