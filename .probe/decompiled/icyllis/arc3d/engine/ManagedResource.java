package icyllis.arc3d.engine;

import icyllis.arc3d.core.RefCnt;

public abstract class ManagedResource extends RefCnt {

    private final GpuDevice mDevice;

    public ManagedResource(GpuDevice device) {
        this.mDevice = device;
    }

    protected GpuDevice getDevice() {
        return this.mDevice;
    }
}