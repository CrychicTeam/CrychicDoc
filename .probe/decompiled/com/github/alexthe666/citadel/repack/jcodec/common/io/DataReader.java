package com.github.alexthe666.citadel.repack.jcodec.common.io;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DataReader implements Closeable {

    private static final int DEFAULT_BUFFER_SIZE = 1048576;

    private SeekableByteChannel channel;

    private ByteBuffer buffer;

    public static DataReader createDataReader(SeekableByteChannel channel, ByteOrder order) {
        return new DataReader(channel, order, 1048576);
    }

    public DataReader(SeekableByteChannel channel, ByteOrder order, int bufferSize) {
        this.channel = channel;
        this.buffer = ByteBuffer.allocate(bufferSize);
        this.buffer.limit(0);
        this.buffer.order(order);
    }

    public int readFully3(byte[] b, int off, int len) throws IOException {
        int initOff = off;
        while (len > 0) {
            this.fetchIfNeeded(len);
            if (this.buffer.remaining() == 0) {
                break;
            }
            int toRead = Math.min(this.buffer.remaining(), len);
            this.buffer.get(b, off, toRead);
            off += toRead;
            len -= toRead;
        }
        return off - initOff;
    }

    public int skipBytes(int n) throws IOException {
        long oldPosition = this.position();
        if (n < this.buffer.remaining()) {
            this.buffer.position(this.buffer.position() + n);
        } else {
            this.setPosition(oldPosition + (long) n);
        }
        return (int) (this.position() - oldPosition);
    }

    public byte readByte() throws IOException {
        this.fetchIfNeeded(1);
        return this.buffer.get();
    }

    public short readShort() throws IOException {
        this.fetchIfNeeded(2);
        return this.buffer.getShort();
    }

    public char readChar() throws IOException {
        this.fetchIfNeeded(2);
        return this.buffer.getChar();
    }

    public int readInt() throws IOException {
        this.fetchIfNeeded(4);
        return this.buffer.getInt();
    }

    public long readLong() throws IOException {
        this.fetchIfNeeded(8);
        return this.buffer.getLong();
    }

    public float readFloat() throws IOException {
        this.fetchIfNeeded(4);
        return this.buffer.getFloat();
    }

    public double readDouble() throws IOException {
        this.fetchIfNeeded(8);
        return this.buffer.getDouble();
    }

    public long position() throws IOException {
        return this.channel.position() - (long) this.buffer.limit() + (long) this.buffer.position();
    }

    public long setPosition(long newPos) throws IOException {
        int relative = (int) (newPos - (this.channel.position() - (long) this.buffer.limit()));
        if (relative >= 0 && relative < this.buffer.limit()) {
            this.buffer.position(relative);
        } else {
            this.buffer.limit(0);
            this.channel.setPosition(newPos);
        }
        return this.position();
    }

    public void close() throws IOException {
        this.channel.close();
    }

    private void fetchIfNeeded(int length) throws IOException {
        if (this.buffer.remaining() < length) {
            moveRemainderToTheStart(this.buffer);
            this.channel.read(this.buffer);
            this.buffer.flip();
        }
    }

    private static void moveRemainderToTheStart(ByteBuffer readBuf) {
        int rem = readBuf.remaining();
        for (int i = 0; i < rem; i++) {
            readBuf.put(i, readBuf.get());
        }
        readBuf.clear();
        readBuf.position(rem);
    }

    public long size() throws IOException {
        return this.channel.size();
    }

    public int readFully(byte[] b) throws IOException {
        return this.readFully3(b, 0, b.length);
    }
}