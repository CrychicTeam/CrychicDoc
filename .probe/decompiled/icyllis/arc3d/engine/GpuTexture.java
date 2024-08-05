package icyllis.arc3d.engine;

import icyllis.arc3d.core.RefCnt;
import icyllis.arc3d.core.SharedPtr;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class GpuTexture extends GpuResource implements IGpuSurface {

    protected final int mWidth;

    protected final int mHeight;

    protected int mFlags;

    private boolean mMipmapsDirty;

    @SharedPtr
    private ReleaseCallback mReleaseCallback;

    protected GpuTexture(GpuDevice device, int width, int height) {
        super(device);
        assert width > 0 && height > 0;
        this.mWidth = width;
        this.mHeight = height;
    }

    @Override
    public final int getWidth() {
        return this.mWidth;
    }

    @Override
    public final int getHeight() {
        return this.mHeight;
    }

    public final boolean isMipmapped() {
        return (this.mFlags & 4) != 0;
    }

    public final boolean isReadOnly() {
        return (this.mFlags & 32) != 0;
    }

    public final boolean isProtected() {
        return (this.mFlags & 16) != 0;
    }

    @Override
    public final int getSurfaceFlags() {
        int flags = this.mFlags;
        if (this.getBudgetType() == 0) {
            flags |= 1;
        }
        return flags;
    }

    @Override
    public GpuTexture asTexture() {
        return this;
    }

    public abstract boolean isExternal();

    @Nonnull
    public abstract BackendTexture getBackendTexture();

    public final boolean isMipmapsDirty() {
        assert this.isMipmapped();
        return this.mMipmapsDirty && this.isMipmapped();
    }

    public final void setMipmapsDirty(boolean mipmapsDirty) {
        assert this.isMipmapped();
        this.mMipmapsDirty = mipmapsDirty;
    }

    public abstract int getMaxMipmapLevel();

    public void setReleaseCallback(@SharedPtr ReleaseCallback callback) {
        this.mReleaseCallback = RefCnt.move(this.mReleaseCallback, callback);
    }

    @Override
    protected void onRelease() {
        if (this.mReleaseCallback != null) {
            this.mReleaseCallback.unref();
        }
        this.mReleaseCallback = null;
    }

    @Override
    protected void onDiscard() {
        if (this.mReleaseCallback != null) {
            this.mReleaseCallback.unref();
        }
        this.mReleaseCallback = null;
    }

    @Nullable
    protected GpuTexture.ScratchKey computeScratchKey() {
        BackendFormat format = this.getBackendFormat();
        if (format.isCompressed()) {
            return null;
        } else {
            assert this.getBudgetType() != 2;
            return new GpuTexture.ScratchKey().compute(format, this.mWidth, this.mHeight, 1, this.mFlags);
        }
    }

    public static long computeSize(BackendFormat format, int width, int height, int sampleCount, boolean mipmapped, boolean approx) {
        assert width > 0 && height > 0;
        assert sampleCount > 0;
        assert sampleCount == 1 || !mipmapped;
        if (format.isExternal()) {
            return 0L;
        } else {
            if (approx) {
                width = ISurface.getApproxSize(width);
                height = ISurface.getApproxSize(height);
            }
            long size = DataUtils.numBlocks(format.getCompressionType(), width, height) * (long) format.getBytesPerBlock();
            assert size > 0L;
            if (mipmapped) {
                size = (size << 2) / 3L;
            } else {
                size *= (long) sampleCount;
            }
            assert size > 0L;
            return size;
        }
    }

    public static long computeSize(BackendFormat format, int width, int height, int sampleCount, int levelCount) {
        return computeSize(format, width, height, sampleCount, levelCount, false);
    }

    public static long computeSize(BackendFormat format, int width, int height, int sampleCount, int levelCount, boolean approx) {
        assert width > 0 && height > 0;
        assert sampleCount > 0 && levelCount > 0;
        assert sampleCount == 1 || levelCount == 1;
        if (format.isExternal()) {
            return 0L;
        } else {
            if (approx) {
                width = ISurface.getApproxSize(width);
                height = ISurface.getApproxSize(height);
            }
            long size = DataUtils.numBlocks(format.getCompressionType(), width, height) * (long) format.getBytesPerBlock();
            assert size > 0L;
            if (levelCount > 1) {
                size = (size - (size >> (levelCount << 1)) << 2) / 3L;
            } else {
                size *= (long) sampleCount;
            }
            assert size > 0L;
            return size;
        }
    }

    public static final class ScratchKey implements IScratchKey {

        public int mWidth;

        public int mHeight;

        public int mFormat;

        public int mFlags;

        @Nonnull
        public GpuTexture.ScratchKey compute(BackendFormat format, int width, int height, int sampleCount, int surfaceFlags) {
            assert width > 0 && height > 0;
            assert !format.isCompressed();
            this.mWidth = width;
            this.mHeight = height;
            this.mFormat = format.getFormatKey();
            this.mFlags = surfaceFlags & 28 | sampleCount << 16;
            return this;
        }

        @Override
        public int hashCode() {
            int result = this.mWidth;
            result = 31 * result + this.mHeight;
            result = 31 * result + this.mFormat;
            return 31 * result + this.mFlags;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else {
                if (o instanceof GpuTexture.ScratchKey key && this.mWidth == key.mWidth && this.mHeight == key.mHeight && this.mFormat == key.mFormat && this.mFlags == key.mFlags) {
                    return true;
                }
                return false;
            }
        }
    }
}