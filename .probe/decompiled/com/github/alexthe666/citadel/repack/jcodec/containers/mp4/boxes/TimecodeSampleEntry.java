package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import java.nio.ByteBuffer;

public class TimecodeSampleEntry extends SampleEntry {

    private static final String TMCD = "tmcd";

    public static final int FLAG_DROPFRAME = 1;

    public static final int FLAG_24HOURMAX = 2;

    public static final int FLAG_NEGATIVETIMEOK = 4;

    public static final int FLAG_COUNTER = 8;

    private int flags;

    private int timescale;

    private int frameDuration;

    private byte numFrames;

    public static TimecodeSampleEntry createTimecodeSampleEntry(int flags, int timescale, int frameDuration, int numFrames) {
        TimecodeSampleEntry tmcd = new TimecodeSampleEntry(new Header("tmcd"));
        tmcd.flags = flags;
        tmcd.timescale = timescale;
        tmcd.frameDuration = frameDuration;
        tmcd.numFrames = (byte) numFrames;
        return tmcd;
    }

    public TimecodeSampleEntry(Header header) {
        super(header);
    }

    @Override
    public void parse(ByteBuffer input) {
        super.parse(input);
        NIOUtils.skip(input, 4);
        this.flags = input.getInt();
        this.timescale = input.getInt();
        this.frameDuration = input.getInt();
        this.numFrames = input.get();
        NIOUtils.skip(input, 1);
    }

    @Override
    protected void doWrite(ByteBuffer out) {
        super.doWrite(out);
        out.putInt(0);
        out.putInt(this.flags);
        out.putInt(this.timescale);
        out.putInt(this.frameDuration);
        out.put(this.numFrames);
        out.put((byte) -49);
    }

    public int getFlags() {
        return this.flags;
    }

    public int getTimescale() {
        return this.timescale;
    }

    public int getFrameDuration() {
        return this.frameDuration;
    }

    public byte getNumFrames() {
        return this.numFrames;
    }

    public boolean isDropFrame() {
        return (this.flags & 1) != 0;
    }
}