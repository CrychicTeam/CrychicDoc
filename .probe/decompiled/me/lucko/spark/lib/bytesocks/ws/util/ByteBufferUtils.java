package me.lucko.spark.lib.bytesocks.ws.util;

import java.nio.ByteBuffer;

public class ByteBufferUtils {

    private ByteBufferUtils() {
    }

    public static int transferByteBuffer(ByteBuffer source, ByteBuffer dest) {
        if (source != null && dest != null) {
            int fremain = source.remaining();
            int toremain = dest.remaining();
            if (fremain > toremain) {
                int limit = Math.min(fremain, toremain);
                source.limit(limit);
                dest.put(source);
                return limit;
            } else {
                dest.put(source);
                return fremain;
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static ByteBuffer getEmptyByteBuffer() {
        return ByteBuffer.allocate(0);
    }
}