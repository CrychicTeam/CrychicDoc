package icyllis.arc3d.vulkan;

import icyllis.arc3d.engine.GpuBuffer;

public final class VulkanBuffer extends GpuBuffer {

    public VulkanBuffer(VulkanDevice device) {
        super(device, 0, 0);
    }

    @Override
    protected void onRelease() {
    }

    @Override
    protected void onDiscard() {
    }

    @Override
    protected long onLock(int mode, int offset, int size) {
        return 0L;
    }

    @Override
    protected void onUnlock(int mode, int offset, int size) {
    }

    @Override
    public boolean isLocked() {
        return false;
    }

    @Override
    public long getLockedBuffer() {
        return 0L;
    }

    @Override
    protected boolean onUpdateData(int offset, int size, long data) {
        return false;
    }
}