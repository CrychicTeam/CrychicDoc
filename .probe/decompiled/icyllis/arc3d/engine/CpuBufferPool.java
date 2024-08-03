package icyllis.arc3d.engine;

import icyllis.arc3d.core.RefCnt;
import icyllis.arc3d.core.SharedPtr;

public class CpuBufferPool {

    private final CpuBuffer[] mBuffers;

    public CpuBufferPool(int maxCount) {
        this.mBuffers = new CpuBuffer[maxCount];
    }

    @SharedPtr
    public CpuBuffer makeBuffer(int size) {
        assert size > 0;
        CpuBuffer result = null;
        if (size <= 131072) {
            int i;
            for (i = 0; i < this.mBuffers.length && this.mBuffers[i] != null; i++) {
                assert this.mBuffers[i].size() == 131072;
                if (this.mBuffers[i].unique()) {
                    result = this.mBuffers[i];
                }
            }
            if (result == null && i < this.mBuffers.length) {
                this.mBuffers[i] = result = new CpuBuffer(131072);
            }
        }
        return result == null ? new CpuBuffer(size) : RefCnt.create(result);
    }

    public void releaseAll() {
        for (int i = 0; i < this.mBuffers.length && this.mBuffers[i] != null; i++) {
            this.mBuffers[i].unref();
            this.mBuffers[i] = null;
        }
    }
}