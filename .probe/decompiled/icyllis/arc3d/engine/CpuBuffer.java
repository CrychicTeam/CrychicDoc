package icyllis.arc3d.engine;

import icyllis.arc3d.core.RefCnt;
import org.lwjgl.system.MemoryUtil;

public final class CpuBuffer extends RefCnt {

    private final int mSize;

    private final long mData;

    public CpuBuffer(int size) {
        assert size > 0;
        this.mSize = size;
        this.mData = MemoryUtil.nmemAllocChecked((long) size);
    }

    public int size() {
        return this.mSize;
    }

    public long data() {
        return this.mData;
    }

    @Override
    protected void deallocate() {
        MemoryUtil.nmemFree(this.mData);
    }
}