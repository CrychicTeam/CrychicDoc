package icyllis.arc3d.vulkan;

public final class VulkanImageInfo extends VulkanAllocation {

    public long mImage = 0L;

    public int mImageLayout = 0;

    public int mImageTiling = 0;

    public int mFormat = 0;

    public int mSharingMode = 0;

    public int mImageUsageFlags = 0;

    public int mSampleCount = 1;

    public int mLevelCount = 0;

    public int mCurrentQueueFamily = -1;

    public int mMemoryHandle = -1;

    public boolean mProtected = false;

    public void set(VulkanImageInfo info) {
        super.set(info);
        this.mImage = info.mImage;
        this.mImageLayout = info.mImageLayout;
        this.mImageTiling = info.mImageTiling;
        this.mFormat = info.mFormat;
        this.mSharingMode = info.mSharingMode;
        this.mImageUsageFlags = info.mImageUsageFlags;
        this.mSampleCount = info.mSampleCount;
        this.mLevelCount = info.mLevelCount;
        this.mCurrentQueueFamily = info.mCurrentQueueFamily;
        this.mMemoryHandle = info.mMemoryHandle;
        this.mProtected = info.mProtected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || this.getClass() != o.getClass()) {
            return false;
        } else if (!super.equals(o)) {
            return false;
        } else {
            VulkanImageInfo that = (VulkanImageInfo) o;
            if (this.mImage != that.mImage) {
                return false;
            } else if (this.mImageLayout != that.mImageLayout) {
                return false;
            } else if (this.mImageTiling != that.mImageTiling) {
                return false;
            } else if (this.mFormat != that.mFormat) {
                return false;
            } else if (this.mSharingMode != that.mSharingMode) {
                return false;
            } else if (this.mImageUsageFlags != that.mImageUsageFlags) {
                return false;
            } else if (this.mSampleCount != that.mSampleCount) {
                return false;
            } else if (this.mLevelCount != that.mLevelCount) {
                return false;
            } else if (this.mCurrentQueueFamily != that.mCurrentQueueFamily) {
                return false;
            } else {
                return this.mMemoryHandle != that.mMemoryHandle ? false : this.mProtected == that.mProtected;
            }
        }
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (this.mImage ^ this.mImage >>> 32);
        result = 31 * result + this.mImageLayout;
        result = 31 * result + this.mImageTiling;
        result = 31 * result + this.mFormat;
        result = 31 * result + this.mSharingMode;
        result = 31 * result + this.mImageUsageFlags;
        result = 31 * result + this.mSampleCount;
        result = 31 * result + this.mLevelCount;
        result = 31 * result + this.mCurrentQueueFamily;
        result = 31 * result + this.mMemoryHandle;
        return 31 * result + (this.mProtected ? 1 : 0);
    }
}