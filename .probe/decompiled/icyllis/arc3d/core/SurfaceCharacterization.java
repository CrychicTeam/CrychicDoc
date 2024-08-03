package icyllis.arc3d.core;

import icyllis.arc3d.engine.BackendFormat;
import icyllis.arc3d.engine.BackendTexture;
import icyllis.arc3d.engine.Caps;
import icyllis.arc3d.engine.SharedContextInfo;
import icyllis.arc3d.vulkan.VulkanImageInfo;
import javax.annotation.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public final class SurfaceCharacterization {

    private final SharedContextInfo mContextInfo;

    private final long mCacheMaxResourceBytes;

    private final ImageInfo mImageInfo;

    private final BackendFormat mBackendFormat;

    private final int mOrigin;

    private final int mSampleCount;

    private final boolean mTexturable;

    private final boolean mMipmapped;

    private final boolean mGLWrapDefaultFramebuffer;

    private final boolean mVkSupportInputAttachment;

    private final boolean mVkSecondaryCommandBuffer;

    private final boolean mIsProtected;

    @Internal
    public SurfaceCharacterization(SharedContextInfo contextInfo, long cacheMaxResourceBytes, ImageInfo imageInfo, BackendFormat backendFormat, int origin, int sampleCount, boolean texturable, boolean mipmapped, boolean glWrapDefaultFramebuffer, boolean vkSupportInputAttachment, boolean vkSecondaryCommandBuffer, boolean isProtected) {
        this.mContextInfo = contextInfo;
        this.mCacheMaxResourceBytes = cacheMaxResourceBytes;
        this.mImageInfo = imageInfo;
        this.mBackendFormat = backendFormat;
        this.mOrigin = origin;
        this.mSampleCount = sampleCount;
        this.mTexturable = texturable;
        this.mMipmapped = mipmapped;
        this.mGLWrapDefaultFramebuffer = glWrapDefaultFramebuffer;
        this.mVkSupportInputAttachment = vkSupportInputAttachment;
        this.mVkSecondaryCommandBuffer = vkSecondaryCommandBuffer;
        this.mIsProtected = isProtected;
        assert this.validate();
    }

    private boolean validate() {
        Caps caps = this.mContextInfo.getCaps();
        int ct = this.getColorType();
        assert this.mSampleCount > 0 && caps.isFormatRenderable(ct, this.mBackendFormat, this.mSampleCount);
        assert caps.isFormatCompatible(ct, this.mBackendFormat);
        assert !this.mMipmapped || this.mTexturable;
        assert !this.mTexturable || !this.mGLWrapDefaultFramebuffer;
        int backend = this.mBackendFormat.getBackend();
        assert !this.mGLWrapDefaultFramebuffer || backend == 0;
        assert !this.mVkSecondaryCommandBuffer && !this.mVkSupportInputAttachment || backend == 1;
        assert !this.mVkSecondaryCommandBuffer || !this.mVkSupportInputAttachment;
        assert !this.mTexturable || !this.mVkSecondaryCommandBuffer;
        return true;
    }

    @Nullable
    public SurfaceCharacterization createResized(int width, int height) {
        Caps caps = this.mContextInfo.getCaps();
        if (caps == null) {
            return null;
        } else {
            return width > 0 && height > 0 && width <= caps.maxRenderTargetSize() && height <= caps.maxRenderTargetSize() ? new SurfaceCharacterization(this.mContextInfo, this.mCacheMaxResourceBytes, this.mImageInfo.makeWH(width, height), this.mBackendFormat, this.mOrigin, this.mSampleCount, this.mTexturable, this.mMipmapped, this.mGLWrapDefaultFramebuffer, this.mVkSupportInputAttachment, this.mVkSecondaryCommandBuffer, this.mIsProtected) : null;
        }
    }

    @Nullable
    public SurfaceCharacterization createBackendFormat(int colorType, BackendFormat backendFormat) {
        return backendFormat == null ? null : new SurfaceCharacterization(this.mContextInfo, this.mCacheMaxResourceBytes, this.mImageInfo.makeColorType(colorType), backendFormat, this.mOrigin, this.mSampleCount, this.mTexturable, this.mMipmapped, this.mGLWrapDefaultFramebuffer, this.mVkSupportInputAttachment, this.mVkSecondaryCommandBuffer, this.mIsProtected);
    }

    @Nullable
    public SurfaceCharacterization createDefaultFramebuffer(boolean useDefaultFramebuffer) {
        return !this.mTexturable && !this.mVkSupportInputAttachment && !this.mVkSecondaryCommandBuffer ? new SurfaceCharacterization(this.mContextInfo, this.mCacheMaxResourceBytes, this.mImageInfo, this.mBackendFormat, this.mOrigin, this.mSampleCount, false, this.mMipmapped, useDefaultFramebuffer, false, false, this.mIsProtected) : null;
    }

    @Internal
    public SharedContextInfo getContextInfo() {
        return this.mContextInfo;
    }

    public long getCacheMaxResourceBytes() {
        return this.mCacheMaxResourceBytes;
    }

    public ImageInfo getImageInfo() {
        return this.mImageInfo;
    }

    public BackendFormat getBackendFormat() {
        return this.mBackendFormat;
    }

    public int getOrigin() {
        return this.mOrigin;
    }

    public int getWidth() {
        return this.mImageInfo.width();
    }

    public int getHeight() {
        return this.mImageInfo.height();
    }

    public int getColorType() {
        return this.mImageInfo.colorType();
    }

    public int getSampleCount() {
        return this.mSampleCount;
    }

    public boolean isTexturable() {
        return this.mTexturable;
    }

    public boolean isMipmapped() {
        return this.mMipmapped;
    }

    public boolean glWrapDefaultFramebuffer() {
        return this.mGLWrapDefaultFramebuffer;
    }

    public boolean vkSupportInputAttachment() {
        return this.mVkSupportInputAttachment;
    }

    public boolean vkSecondaryCommandBuffer() {
        return this.mVkSecondaryCommandBuffer;
    }

    public boolean isProtected() {
        return this.mIsProtected;
    }

    public boolean isCompatible(BackendTexture texture) {
        if (this.mGLWrapDefaultFramebuffer) {
            return false;
        } else if (this.mVkSecondaryCommandBuffer) {
            return false;
        } else if (this.mIsProtected != texture.isProtected()) {
            return false;
        } else if (this.mMipmapped && !texture.isMipmapped()) {
            return false;
        } else if (this.getWidth() != texture.getWidth() || this.getHeight() != texture.getHeight()) {
            return false;
        } else if (!this.mBackendFormat.equals(texture.getBackendFormat())) {
            return false;
        } else if (!this.mVkSupportInputAttachment) {
            return true;
        } else if (texture.getBackend() != 1) {
            return false;
        } else {
            VulkanImageInfo vkInfo = new VulkanImageInfo();
            return !texture.getVkImageInfo(vkInfo) ? false : (vkInfo.mImageUsageFlags & 128) != 0;
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            SurfaceCharacterization that = (SurfaceCharacterization) o;
            if (this.mCacheMaxResourceBytes != that.mCacheMaxResourceBytes) {
                return false;
            } else if (this.mOrigin != that.mOrigin) {
                return false;
            } else if (this.mSampleCount != that.mSampleCount) {
                return false;
            } else if (this.mTexturable != that.mTexturable) {
                return false;
            } else if (this.mMipmapped != that.mMipmapped) {
                return false;
            } else if (this.mGLWrapDefaultFramebuffer != that.mGLWrapDefaultFramebuffer) {
                return false;
            } else if (this.mVkSupportInputAttachment != that.mVkSupportInputAttachment) {
                return false;
            } else if (this.mVkSecondaryCommandBuffer != that.mVkSecondaryCommandBuffer) {
                return false;
            } else if (this.mIsProtected != that.mIsProtected) {
                return false;
            } else if (!this.mContextInfo.equals(that.mContextInfo)) {
                return false;
            } else {
                return !this.mImageInfo.equals(that.mImageInfo) ? false : this.mBackendFormat.equals(that.mBackendFormat);
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.mContextInfo.hashCode();
        result = 31 * result + (int) (this.mCacheMaxResourceBytes ^ this.mCacheMaxResourceBytes >>> 32);
        result = 31 * result + this.mImageInfo.hashCode();
        result = 31 * result + this.mBackendFormat.hashCode();
        result = 31 * result + this.mOrigin;
        result = 31 * result + this.mSampleCount;
        result = 31 * result + (this.mTexturable ? 1 : 0);
        result = 31 * result + (this.mMipmapped ? 1 : 0);
        result = 31 * result + (this.mGLWrapDefaultFramebuffer ? 1 : 0);
        result = 31 * result + (this.mVkSupportInputAttachment ? 1 : 0);
        result = 31 * result + (this.mVkSecondaryCommandBuffer ? 1 : 0);
        return 31 * result + (this.mIsProtected ? 1 : 0);
    }
}