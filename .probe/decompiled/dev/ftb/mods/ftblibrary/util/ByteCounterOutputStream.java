package dev.ftb.mods.ftblibrary.util;

import java.io.OutputStream;

public class ByteCounterOutputStream extends OutputStream {

    private long size = 0L;

    public void write(int b) {
        this.size++;
    }

    public void write(byte[] b) {
        this.size += (long) b.length;
    }

    public void write(byte[] b, int off, int len) {
        this.size += (long) len;
    }

    public long getSize() {
        return this.size;
    }
}