package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import java.nio.ByteBuffer;

public class ClipRegionBox extends Box {

    private short rgnSize;

    private short y;

    private short x;

    private short height;

    private short width;

    public static String fourcc() {
        return "crgn";
    }

    public static ClipRegionBox createClipRegionBox(short x, short y, short width, short height) {
        ClipRegionBox b = new ClipRegionBox(new Header(fourcc()));
        b.rgnSize = 10;
        b.x = x;
        b.y = y;
        b.width = width;
        b.height = height;
        return b;
    }

    public ClipRegionBox(Header atom) {
        super(atom);
    }

    @Override
    public void parse(ByteBuffer input) {
        this.rgnSize = input.getShort();
        this.y = input.getShort();
        this.x = input.getShort();
        this.height = input.getShort();
        this.width = input.getShort();
    }

    @Override
    protected void doWrite(ByteBuffer out) {
        out.putShort(this.rgnSize);
        out.putShort(this.y);
        out.putShort(this.x);
        out.putShort(this.height);
        out.putShort(this.width);
    }

    @Override
    public int estimateSize() {
        return 18;
    }

    public short getRgnSize() {
        return this.rgnSize;
    }

    public short getY() {
        return this.y;
    }

    public short getX() {
        return this.x;
    }

    public short getHeight() {
        return this.height;
    }

    public short getWidth() {
        return this.width;
    }
}