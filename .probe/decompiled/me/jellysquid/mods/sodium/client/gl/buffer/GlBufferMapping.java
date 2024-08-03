package me.jellysquid.mods.sodium.client.gl.buffer;

import java.nio.ByteBuffer;
import org.lwjgl.system.MemoryUtil;

public class GlBufferMapping {

    private final GlBuffer buffer;

    private final ByteBuffer map;

    protected boolean disposed;

    public GlBufferMapping(GlBuffer buffer, ByteBuffer map) {
        this.buffer = buffer;
        this.map = map;
    }

    public void write(ByteBuffer data, int writeOffset) {
        MemoryUtil.memCopy(MemoryUtil.memAddress(data), MemoryUtil.memAddress(this.map, writeOffset), (long) data.remaining());
    }

    public GlBuffer getBufferObject() {
        return this.buffer;
    }

    public void dispose() {
        this.disposed = true;
    }

    public boolean isDisposed() {
        return this.disposed;
    }

    public ByteBuffer getMemoryBuffer() {
        return this.map;
    }
}