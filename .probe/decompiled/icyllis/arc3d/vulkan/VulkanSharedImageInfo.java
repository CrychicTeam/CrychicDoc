package icyllis.arc3d.vulkan;

public final class VulkanSharedImageInfo {

    private volatile int mLayout;

    private volatile int mQueueFamilyIndex;

    public VulkanSharedImageInfo(VulkanImageInfo info) {
        this(info.mImageLayout, info.mCurrentQueueFamily);
    }

    public VulkanSharedImageInfo(int layout, int queueFamilyIndex) {
        this.mLayout = layout;
        this.mQueueFamilyIndex = queueFamilyIndex;
    }

    public void setImageLayout(int layout) {
        this.mLayout = layout;
    }

    public int getImageLayout() {
        return this.mLayout;
    }

    public void setQueueFamilyIndex(int queueFamilyIndex) {
        this.mQueueFamilyIndex = queueFamilyIndex;
    }

    public int getQueueFamilyIndex() {
        return this.mQueueFamilyIndex;
    }
}