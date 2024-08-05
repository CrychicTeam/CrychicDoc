package icyllis.arc3d.vulkan;

import icyllis.arc3d.engine.BackendFormat;
import icyllis.arc3d.engine.BackendTexture;
import javax.annotation.Nonnull;

public final class VkBackendTexture extends BackendTexture {

    private static final int DEFAULT_USAGE_FLAGS = 23;

    private final VulkanImageInfo mInfo;

    final VulkanSharedImageInfo mState;

    private final BackendFormat mBackendFormat;

    public VkBackendTexture(int width, int height, VulkanImageInfo info) {
        this(width, height, info, new VulkanSharedImageInfo(info), VkBackendFormat.make(info.mFormat, info.mMemoryHandle != -1 || info.mImageTiling == 1000158000));
    }

    VkBackendTexture(int width, int height, VulkanImageInfo info, VulkanSharedImageInfo state, BackendFormat backendFormat) {
        super(width, height);
        if (info.mImageUsageFlags == 0) {
            info.mImageUsageFlags = 23;
        }
        this.mInfo = info;
        this.mState = state;
        this.mBackendFormat = backendFormat;
    }

    @Override
    public int getBackend() {
        return 1;
    }

    @Override
    public boolean isExternal() {
        return this.mBackendFormat.isExternal();
    }

    @Override
    public boolean isMipmapped() {
        return this.mInfo.mLevelCount > 1;
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
        return this.mBackendFormat;
    }

    @Override
    public boolean isProtected() {
        return this.mInfo.mProtected;
    }

    @Override
    public boolean isSameTexture(BackendTexture texture) {
        return texture instanceof VkBackendTexture t ? this.mInfo.mImage == t.mInfo.mImage : false;
    }
}