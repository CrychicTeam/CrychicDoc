package icyllis.arc3d.vulkan;

import icyllis.arc3d.engine.DirectContext;
import icyllis.arc3d.engine.GpuDevice;
import org.lwjgl.vulkan.VkDevice;

public abstract class VulkanDevice extends GpuDevice {

    private VkDevice mDevice;

    private boolean mProtectedContext;

    private int mQueueIndex;

    public VulkanDevice(DirectContext context) {
        super(context, null);
    }

    public VkDevice device() {
        return this.mDevice;
    }

    public int getQueueIndex() {
        return this.mQueueIndex;
    }

    public boolean isProtectedContext() {
        return this.mProtectedContext;
    }
}