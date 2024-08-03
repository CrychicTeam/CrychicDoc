package com.github.alexthe666.citadel.repack.jcodec.common.io;

import java.io.IOException;
import java.nio.ByteBuffer;

public class ByteBufferSeekableByteChannel implements SeekableByteChannel {

    private ByteBuffer backing;

    private boolean open;

    private int contentLength;

    public ByteBufferSeekableByteChannel(ByteBuffer backing, int contentLength) {
        this.backing = backing;
        this.contentLength = contentLength;
        this.open = true;
    }

    public static ByteBufferSeekableByteChannel writeToByteBuffer(ByteBuffer buf) {
        return new ByteBufferSeekableByteChannel(buf, 0);
    }

    public static ByteBufferSeekableByteChannel readFromByteBuffer(ByteBuffer buf) {
        return new ByteBufferSeekableByteChannel(buf, buf.remaining());
    }

    public boolean isOpen() {
        return this.open;
    }

    public void close() throws IOException {
        this.open = false;
    }

    public int read(ByteBuffer dst) throws IOException {
        if (this.backing.hasRemaining() && this.contentLength > 0) {
            int toRead = Math.min(this.backing.remaining(), dst.remaining());
            toRead = Math.min(toRead, this.contentLength);
            dst.put(NIOUtils.read(this.backing, toRead));
            this.contentLength = Math.max(this.contentLength, this.backing.position());
            return toRead;
        } else {
            return -1;
        }
    }

    public int write(ByteBuffer src) throws IOException {
        int toWrite = Math.min(this.backing.remaining(), src.remaining());
        this.backing.put(NIOUtils.read(src, toWrite));
        this.contentLength = Math.max(this.contentLength, this.backing.position());
        return toWrite;
    }

    @Override
    public long position() throws IOException {
        return (long) this.backing.position();
    }

    @Override
    public SeekableByteChannel setPosition(long newPosition) throws IOException {
        this.backing.position((int) newPosition);
        this.contentLength = Math.max(this.contentLength, this.backing.position());
        return this;
    }

    @Override
    public long size() throws IOException {
        return (long) this.contentLength;
    }

    @Override
    public SeekableByteChannel truncate(long size) throws IOException {
        this.contentLength = (int) size;
        return this;
    }

    public ByteBuffer getContents() {
        ByteBuffer contents = this.backing.duplicate();
        contents.position(0);
        contents.limit(this.contentLength);
        return contents;
    }
}