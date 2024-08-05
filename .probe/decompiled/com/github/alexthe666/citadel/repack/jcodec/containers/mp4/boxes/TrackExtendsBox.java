package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import java.nio.ByteBuffer;

public class TrackExtendsBox extends FullBox {

    private int trackId;

    private int defaultSampleDescriptionIndex;

    private int defaultSampleDuration;

    private int defaultSampleBytes;

    private int defaultSampleFlags;

    public TrackExtendsBox(Header atom) {
        super(atom);
    }

    public static String fourcc() {
        return "trex";
    }

    @Override
    public void parse(ByteBuffer input) {
        super.parse(input);
        this.trackId = input.getInt();
        this.defaultSampleDescriptionIndex = input.getInt();
        this.defaultSampleDuration = input.getInt();
        this.defaultSampleBytes = input.getInt();
        this.defaultSampleFlags = input.getInt();
    }

    @Override
    protected void doWrite(ByteBuffer out) {
        super.doWrite(out);
        out.putInt(this.trackId);
        out.putInt(this.defaultSampleDescriptionIndex);
        out.putInt(this.defaultSampleDuration);
        out.putInt(this.defaultSampleBytes);
        out.putInt(this.defaultSampleFlags);
    }

    @Override
    public int estimateSize() {
        return 32;
    }

    public int getTrackId() {
        return this.trackId;
    }

    public void setTrackId(int trackId) {
        this.trackId = trackId;
    }

    public int getDefaultSampleDescriptionIndex() {
        return this.defaultSampleDescriptionIndex;
    }

    public void setDefaultSampleDescriptionIndex(int defaultSampleDescriptionIndex) {
        this.defaultSampleDescriptionIndex = defaultSampleDescriptionIndex;
    }

    public int getDefaultSampleDuration() {
        return this.defaultSampleDuration;
    }

    public void setDefaultSampleDuration(int defaultSampleDuration) {
        this.defaultSampleDuration = defaultSampleDuration;
    }

    public int getDefaultSampleBytes() {
        return this.defaultSampleBytes;
    }

    public void setDefaultSampleBytes(int defaultSampleBytes) {
        this.defaultSampleBytes = defaultSampleBytes;
    }

    public int getDefaultSampleFlags() {
        return this.defaultSampleFlags;
    }

    public void setDefaultSampleFlags(int defaultSampleFlags) {
        this.defaultSampleFlags = defaultSampleFlags;
    }

    public static TrackExtendsBox createTrackExtendsBox() {
        return new TrackExtendsBox(new Header(fourcc()));
    }
}