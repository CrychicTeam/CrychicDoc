package com.github.alexthe666.citadel.repack.jaad.mp4.api;

public class Frame implements Comparable<Frame> {

    private final Type type;

    private final long offset;

    private final long size;

    private final double time;

    private byte[] data;

    Frame(Type type, long offset, long size, double time) {
        this.type = type;
        this.offset = offset;
        this.size = size;
        this.time = time;
    }

    public Type getType() {
        return this.type;
    }

    public long getOffset() {
        return this.offset;
    }

    public long getSize() {
        return this.size;
    }

    public double getTime() {
        return this.time;
    }

    public int compareTo(Frame f) {
        double d = this.time - f.time;
        return d < 0.0 ? -1 : (d > 0.0 ? 1 : 0);
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return this.data;
    }
}