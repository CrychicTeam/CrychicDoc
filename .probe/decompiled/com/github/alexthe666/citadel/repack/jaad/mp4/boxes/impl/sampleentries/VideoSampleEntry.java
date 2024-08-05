package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.sampleentries;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import java.io.IOException;

public class VideoSampleEntry extends SampleEntry {

    private int width;

    private int height;

    private double horizontalResolution;

    private double verticalResolution;

    private int frameCount;

    private int depth;

    private String compressorName;

    public VideoSampleEntry(String name) {
        super(name);
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        in.skipBytes(2L);
        in.skipBytes(2L);
        in.skipBytes(4L);
        in.skipBytes(4L);
        in.skipBytes(4L);
        this.width = (int) in.readBytes(2);
        this.height = (int) in.readBytes(2);
        this.horizontalResolution = in.readFixedPoint(16, 16);
        this.verticalResolution = in.readFixedPoint(16, 16);
        in.skipBytes(4L);
        this.frameCount = (int) in.readBytes(2);
        int len = in.read();
        this.compressorName = in.readString(len);
        in.skipBytes((long) (31 - len));
        this.depth = (int) in.readBytes(2);
        in.skipBytes(2L);
        this.readChildren(in);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public double getHorizontalResolution() {
        return this.horizontalResolution;
    }

    public double getVerticalResolution() {
        return this.verticalResolution;
    }

    public int getFrameCount() {
        return this.frameCount;
    }

    public String getCompressorName() {
        return this.compressorName;
    }

    public int getDepth() {
        return this.depth;
    }
}