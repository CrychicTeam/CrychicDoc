package icyllis.arc3d.vulkan;

import icyllis.arc3d.engine.BackendFormat;
import icyllis.arc3d.engine.BackendRenderTarget;
import javax.annotation.Nonnull;

public final class VkBackendRenderTarget extends BackendRenderTarget {

    private static final int DEFAULT_USAGE_FLAGS = 19;

    private final VulkanImageInfo mInfo;

    final VulkanSharedImageInfo mState;

    private VkBackendFormat mBackendFormat;

    public VkBackendRenderTarget(int width, int height, VulkanImageInfo info) {
        this(width, height, info, new VulkanSharedImageInfo(info.mImageLayout, info.mCurrentQueueFamily));
    }

    VkBackendRenderTarget(int width, int height, VulkanImageInfo info, VulkanSharedImageInfo state) {
        super(width, height);
        if (info.mImageUsageFlags == 0) {
            info.mImageUsageFlags = 19;
        }
        this.mInfo = info;
        this.mState = state;
        assert info.mSampleCount >= 1;
    }

    @Override
    public int getBackend() {
        return 1;
    }

    @Override
    public int getSampleCount() {
        return this.mInfo.mSampleCount;
    }

    @Override
    public int getStencilBits() {
        return 0;
    }

    @Override
    public boolean getVkImageInfo(VulkanImageInfo info) {
        info.set(this.mInfo);
        info.mImageLayout = this.mState.getImageLayout();
        info.mCurrentQueueFamily = this.mState.getQueueFamilyIndex();
        return true;
    }

    @Override
    public void setVkImageLayout(int layout) {
        this.mState.setImageLayout(layout);
    }

    @Override
    public void setVkQueueFamilyIndex(int queueFamilyIndex) {
        this.mState.setQueueFamilyIndex(queueFamilyIndex);
    }

    @Nonnull
    @Override
    public BackendFormat getBackendFormat() {
        if (this.mBackendFormat == null) {
            this.mBackendFormat = VkBackendFormat.make(this.mInfo.mFormat, false);
        }
        return this.mBackendFormat;
    }

    @Override
    public boolean isProtected() {
        return this.mInfo.mProtected;
    }
}