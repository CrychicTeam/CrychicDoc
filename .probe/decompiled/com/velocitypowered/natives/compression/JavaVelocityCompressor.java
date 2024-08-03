package com.velocitypowered.natives.compression;

import com.google.common.base.Preconditions;
import com.velocitypowered.natives.util.BufferPreference;
import io.netty.buffer.ByteBuf;
import java.nio.ByteBuffer;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class JavaVelocityCompressor implements VelocityCompressor {

    public static final VelocityCompressorFactory FACTORY = JavaVelocityCompressor::new;

    private final Deflater deflater;

    private final Inflater inflater;

    private boolean disposed = false;

    private JavaVelocityCompressor(int level) {
        this.deflater = new Deflater(level);
        this.inflater = new Inflater();
    }

    @Override
    public void inflate(ByteBuf source, ByteBuf destination, int uncompressedSize) throws DataFormatException {
        this.ensureNotDisposed();
        Preconditions.checkArgument(source.nioBufferCount() == 1, "source has multiple backing buffers");
        Preconditions.checkArgument(destination.nioBufferCount() == 1, "destination has multiple backing buffers");
        int origIdx = source.readerIndex();
        this.inflater.setInput(source.nioBuffer());
        try {
            while (!this.inflater.finished() && this.inflater.getBytesWritten() < (long) uncompressedSize) {
                if (!destination.isWritable()) {
                    destination.ensureWritable(8192);
                }
                ByteBuffer destNioBuf = destination.nioBuffer(destination.writerIndex(), destination.writableBytes());
                int produced = this.inflater.inflate(destNioBuf);
                destination.writerIndex(destination.writerIndex() + produced);
            }
            if (!this.inflater.finished()) {
                throw new DataFormatException("Received a deflate stream that was too large, wanted " + uncompressedSize);
            }
            source.readerIndex(origIdx + this.inflater.getTotalIn());
        } finally {
            this.inflater.reset();
        }
    }

    @Override
    public void deflate(ByteBuf source, ByteBuf destination) throws DataFormatException {
        this.ensureNotDisposed();
        Preconditions.checkArgument(source.nioBufferCount() == 1, "source has multiple backing buffers");
        Preconditions.checkArgument(destination.nioBufferCount() == 1, "destination has multiple backing buffers");
        int origIdx = source.readerIndex();
        this.deflater.setInput(source.nioBuffer());
        this.deflater.finish();
        while (!this.deflater.finished()) {
            if (!destination.isWritable()) {
                destination.ensureWritable(8192);
            }
            ByteBuffer destNioBuf = destination.nioBuffer(destination.writerIndex(), destination.writableBytes());
            int produced = this.deflater.deflate(destNioBuf);
            destination.writerIndex(destination.writerIndex() + produced);
        }
        source.readerIndex(origIdx + this.deflater.getTotalIn());
        this.deflater.reset();
    }

    @Override
    public void close() {
        this.disposed = true;
        this.deflater.end();
        this.inflater.end();
    }

    private void ensureNotDisposed() {
        Preconditions.checkState(!this.disposed, "Object already disposed");
    }

    @Override
    public BufferPreference preferredBufferType() {
        return BufferPreference.DIRECT_PREFERRED;
    }
}