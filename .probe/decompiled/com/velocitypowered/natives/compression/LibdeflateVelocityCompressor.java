package com.velocitypowered.natives.compression;

import com.google.common.base.Preconditions;
import com.velocitypowered.natives.util.BufferPreference;
import io.netty.buffer.ByteBuf;
import java.util.zip.DataFormatException;

public class LibdeflateVelocityCompressor implements VelocityCompressor {

    public static final VelocityCompressorFactory FACTORY = LibdeflateVelocityCompressor::new;

    private final long inflateCtx;

    private final long deflateCtx;

    private boolean disposed = false;

    private LibdeflateVelocityCompressor(int level) {
        int correctedLevel = level == -1 ? 6 : level;
        if (correctedLevel <= 12 && correctedLevel >= 1) {
            this.inflateCtx = NativeZlibInflate.init();
            this.deflateCtx = NativeZlibDeflate.init(correctedLevel);
        } else {
            throw new IllegalArgumentException("Invalid compression level " + level);
        }
    }

    @Override
    public void inflate(ByteBuf source, ByteBuf destination, int uncompressedSize) throws DataFormatException {
        this.ensureNotDisposed();
        destination.ensureWritable(uncompressedSize);
        long sourceAddress = source.memoryAddress() + (long) source.readerIndex();
        long destinationAddress = destination.memoryAddress() + (long) destination.writerIndex();
        NativeZlibInflate.process(this.inflateCtx, sourceAddress, source.readableBytes(), destinationAddress, uncompressedSize);
        destination.writerIndex(destination.writerIndex() + uncompressedSize);
    }

    @Override
    public void deflate(ByteBuf source, ByteBuf destination) throws DataFormatException {
        this.ensureNotDisposed();
        while (true) {
            long sourceAddress = source.memoryAddress() + (long) source.readerIndex();
            long destinationAddress = destination.memoryAddress() + (long) destination.writerIndex();
            int produced = NativeZlibDeflate.process(this.deflateCtx, sourceAddress, source.readableBytes(), destinationAddress, destination.writableBytes());
            if (produced > 0) {
                destination.writerIndex(destination.writerIndex() + produced);
                return;
            }
            if (produced != 0) {
                throw new DataFormatException("libdeflate returned unknown code " + produced);
            }
            destination.capacity(destination.capacity() * 2);
        }
    }

    private void ensureNotDisposed() {
        Preconditions.checkState(!this.disposed, "Object already disposed");
    }

    @Override
    public void close() {
        if (!this.disposed) {
            NativeZlibInflate.free(this.inflateCtx);
            NativeZlibDeflate.free(this.deflateCtx);
        }
        this.disposed = true;
    }

    @Override
    public BufferPreference preferredBufferType() {
        return BufferPreference.DIRECT_REQUIRED;
    }
}