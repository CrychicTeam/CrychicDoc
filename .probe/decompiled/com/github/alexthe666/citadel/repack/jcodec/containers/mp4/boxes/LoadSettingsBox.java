package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import java.nio.ByteBuffer;

public class LoadSettingsBox extends Box {

    private int preloadStartTime;

    private int preloadDuration;

    private int preloadFlags;

    private int defaultHints;

    public static String fourcc() {
        return "load";
    }

    public LoadSettingsBox(Header header) {
        super(header);
    }

    @Override
    public void parse(ByteBuffer input) {
        this.preloadStartTime = input.getInt();
        this.preloadDuration = input.getInt();
        this.preloadFlags = input.getInt();
        this.defaultHints = input.getInt();
    }

    @Override
    protected void doWrite(ByteBuffer out) {
        out.putInt(this.preloadStartTime);
        out.putInt(this.preloadDuration);
        out.putInt(this.preloadFlags);
        out.putInt(this.defaultHints);
    }

    @Override
    public int estimateSize() {
        return 24;
    }

    public int getPreloadStartTime() {
        return this.preloadStartTime;
    }

    public int getPreloadDuration() {
        return this.preloadDuration;
    }

    public int getPreloadFlags() {
        return this.preloadFlags;
    }

    public int getDefaultHints() {
        return this.defaultHints;
    }
}