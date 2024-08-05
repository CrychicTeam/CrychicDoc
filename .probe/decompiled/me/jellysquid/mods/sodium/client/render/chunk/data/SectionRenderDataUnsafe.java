package me.jellysquid.mods.sodium.client.render.chunk.data;

import org.lwjgl.system.MemoryUtil;

public class SectionRenderDataUnsafe {

    private static final long OFFSET_SLICE_MASK = 0L;

    private static final long OFFSET_SLICE_RANGES = 8L;

    private static final long DATA_PER_FACING_SIZE = 12L;

    private static final long NUM_FACINGS = 7L;

    private static final long STRIDE = 92L;

    public static long allocateHeap(int count) {
        return MemoryUtil.nmemCalloc((long) count, 92L);
    }

    public static void freeHeap(long pointer) {
        MemoryUtil.nmemFree(pointer);
    }

    public static void clear(long pointer) {
        MemoryUtil.memSet(pointer, 0, 92L);
    }

    public static long heapPointer(long ptr, int index) {
        return ptr + (long) index * 92L;
    }

    public static void setSliceMask(long ptr, int value) {
        MemoryUtil.memPutInt(ptr + 0L, value);
    }

    public static int getSliceMask(long ptr) {
        return MemoryUtil.memGetInt(ptr + 0L);
    }

    public static void setVertexOffset(long ptr, int facing, int value) {
        MemoryUtil.memPutInt(ptr + 8L + (long) facing * 12L + 0L, value);
    }

    public static int getVertexOffset(long ptr, int facing) {
        return MemoryUtil.memGetInt(ptr + 8L + (long) facing * 12L + 0L);
    }

    public static void setElementCount(long ptr, int facing, int value) {
        MemoryUtil.memPutInt(ptr + 8L + (long) facing * 12L + 4L, value);
    }

    public static int getElementCount(long ptr, int facing) {
        return MemoryUtil.memGetInt(ptr + 8L + (long) facing * 12L + 4L);
    }

    public static void setIndexOffset(long ptr, int facing, int value) {
        MemoryUtil.memPutInt(ptr + 8L + (long) facing * 12L + 8L, value);
    }

    public static int getIndexOffset(long ptr, int facing) {
        return MemoryUtil.memGetInt(ptr + 8L + (long) facing * 12L + 8L);
    }
}