package com.mojang.blaze3d.platform;

import java.nio.ByteBuffer;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.MemoryUtil.MemoryAllocator;

public class MemoryTracker {

    private static final MemoryAllocator ALLOCATOR = MemoryUtil.getAllocator(false);

    public static ByteBuffer create(int int0) {
        long $$1 = ALLOCATOR.malloc((long) int0);
        if ($$1 == 0L) {
            throw new OutOfMemoryError("Failed to allocate " + int0 + " bytes");
        } else {
            return MemoryUtil.memByteBuffer($$1, int0);
        }
    }

    public static ByteBuffer resize(ByteBuffer byteBuffer0, int int1) {
        long $$2 = ALLOCATOR.realloc(MemoryUtil.memAddress0(byteBuffer0), (long) int1);
        if ($$2 == 0L) {
            throw new OutOfMemoryError("Failed to resize buffer from " + byteBuffer0.capacity() + " bytes to " + int1 + " bytes");
        } else {
            return MemoryUtil.memByteBuffer($$2, int1);
        }
    }
}