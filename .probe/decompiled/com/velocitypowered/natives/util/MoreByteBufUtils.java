package com.velocitypowered.natives.util;

import com.velocitypowered.natives.Native;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class MoreByteBufUtils {

    private MoreByteBufUtils() {
        throw new AssertionError();
    }

    public static ByteBuf ensureCompatible(ByteBufAllocator alloc, Native nativeStuff, ByteBuf buf) {
        if (isCompatible(nativeStuff, buf)) {
            return buf.retain();
        } else {
            ByteBuf newBuf = preferredBuffer(alloc, nativeStuff, buf.readableBytes());
            newBuf.writeBytes(buf);
            return newBuf;
        }
    }

    private static boolean isCompatible(Native nativeStuff, ByteBuf buf) {
        BufferPreference preferred = nativeStuff.preferredBufferType();
        switch(preferred) {
            case DIRECT_PREFERRED:
            case HEAP_PREFERRED:
                return true;
            case DIRECT_REQUIRED:
                return buf.hasMemoryAddress();
            case HEAP_REQUIRED:
                return buf.hasArray();
            default:
                throw new AssertionError("Preferred buffer type unknown");
        }
    }

    public static ByteBuf preferredBuffer(ByteBufAllocator alloc, Native nativeStuff, int initialCapacity) {
        switch(nativeStuff.preferredBufferType()) {
            case DIRECT_PREFERRED:
            case DIRECT_REQUIRED:
                return alloc.directBuffer(initialCapacity);
            case HEAP_PREFERRED:
            case HEAP_REQUIRED:
                return alloc.heapBuffer(initialCapacity);
            default:
                throw new AssertionError("Preferred buffer type unknown");
        }
    }
}