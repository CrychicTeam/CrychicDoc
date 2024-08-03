package com.github.alexthe666.citadel.repack.jaad.spi.javasound;

import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

abstract class AsynchronousAudioInputStream extends AudioInputStream implements CircularBuffer.Trigger {

    private byte[] singleByte;

    protected final CircularBuffer buffer = new CircularBuffer(this);

    AsynchronousAudioInputStream(InputStream in, AudioFormat format, long length) throws IOException {
        super(in, format, length);
    }

    public int read() throws IOException {
        int i = -1;
        if (this.singleByte == null) {
            this.singleByte = new byte[1];
        }
        if (this.buffer.read(this.singleByte, 0, 1) == -1) {
            i = -1;
        } else {
            i = this.singleByte[0] & 255;
        }
        return i;
    }

    public int read(byte[] b) throws IOException {
        return this.buffer.read(b, 0, b.length);
    }

    public int read(byte[] b, int off, int len) throws IOException {
        return this.buffer.read(b, off, len);
    }

    public long skip(long len) throws IOException {
        int l = (int) len;
        byte[] b = new byte[l];
        while (l > 0) {
            l -= this.buffer.read(b, 0, l);
        }
        return len;
    }

    public int available() throws IOException {
        return this.buffer.availableRead();
    }

    public void close() throws IOException {
        this.buffer.close();
    }

    public boolean markSupported() {
        return false;
    }

    public void mark(int limit) {
    }

    public void reset() throws IOException {
        throw new IOException("mark not supported");
    }
}