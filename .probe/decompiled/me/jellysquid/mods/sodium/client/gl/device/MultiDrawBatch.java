package me.jellysquid.mods.sodium.client.gl.device;

import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.Pointer;

public final class MultiDrawBatch {

    public final long pElementPointer;

    public final long pElementCount;

    public final long pBaseVertex;

    private final int capacity;

    public int size;

    public MultiDrawBatch(int capacity) {
        this.pElementPointer = MemoryUtil.nmemAlignedAlloc(32L, (long) capacity * (long) Pointer.POINTER_SIZE);
        MemoryUtil.memSet(this.pElementPointer, 0, (long) capacity * (long) Pointer.POINTER_SIZE);
        this.pElementCount = MemoryUtil.nmemAlignedAlloc(32L, (long) capacity * 4L);
        this.pBaseVertex = MemoryUtil.nmemAlignedAlloc(32L, (long) capacity * 4L);
        this.capacity = capacity;
    }

    public int size() {
        return this.size;
    }

    public int capacity() {
        return this.capacity;
    }

    public void clear() {
        this.size = 0;
    }

    public void delete() {
        MemoryUtil.nmemAlignedFree(this.pElementPointer);
        MemoryUtil.nmemAlignedFree(this.pElementCount);
        MemoryUtil.nmemAlignedFree(this.pBaseVertex);
    }

    public boolean isEmpty() {
        return this.size <= 0;
    }

    public int getIndexBufferSize() {
        int elements = 0;
        for (int index = 0; index < this.size; index++) {
            elements = Math.max(elements, MemoryUtil.memGetInt(this.pElementCount + (long) index * 4L));
        }
        return elements;
    }
}