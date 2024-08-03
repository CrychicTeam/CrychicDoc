package icyllis.arc3d.engine;

import icyllis.arc3d.core.RefCnt;
import icyllis.arc3d.core.SharedPtr;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public abstract class SurfaceProxy extends RefCnt implements ISurface {

    final BackendFormat mFormat;

    int mWidth;

    int mHeight;

    int mSurfaceFlags;

    SurfaceProxy.LazyInstantiateCallback mLazyInstantiateCallback;

    final Object mUniqueID;

    int mTaskTargetCount = 0;

    boolean mIsDeferredListTarget = false;

    SurfaceProxy(BackendFormat format, int width, int height, int surfaceFlags) {
        assert format != null;
        this.mFormat = format;
        this.mWidth = width;
        this.mHeight = height;
        this.mSurfaceFlags = surfaceFlags;
        if (format.isExternal()) {
            this.mSurfaceFlags |= 32;
        }
        this.mUniqueID = this;
    }

    SurfaceProxy(@SharedPtr IGpuSurface surface, int surfaceFlags) {
        assert surface != null;
        this.mFormat = surface.getBackendFormat();
        this.mWidth = surface.getWidth();
        this.mHeight = surface.getHeight();
        this.mSurfaceFlags = surface.getSurfaceFlags() | surfaceFlags;
        assert (this.mSurfaceFlags & 2) == 0;
        this.mUniqueID = surface;
    }

    public abstract boolean isLazy();

    public final boolean isLazyMost() {
        boolean result = this.mWidth < 0;
        assert result == this.mHeight < 0;
        assert !result || this.isLazy();
        return result;
    }

    @Override
    public final int getWidth() {
        assert !this.isLazyMost();
        return this.mWidth;
    }

    @Override
    public final int getHeight() {
        assert !this.isLazyMost();
        return this.mHeight;
    }

    public abstract int getBackingWidth();

    public abstract int getBackingHeight();

    public final boolean isExact() {
        assert !this.isLazyMost();
        return (this.mSurfaceFlags & 2) == 0 ? true : this.mWidth == ISurface.getApproxSize(this.mWidth) && this.mHeight == ISurface.getApproxSize(this.mHeight);
    }

    @Override
    public abstract int getSampleCount();

    @Nonnull
    @Override
    public final BackendFormat getBackendFormat() {
        return this.mFormat;
    }

    public final Object getUniqueID() {
        return this.mUniqueID;
    }

    public abstract Object getBackingUniqueID();

    public abstract boolean isInstantiated();

    public abstract boolean instantiate(ResourceProvider var1);

    public abstract void clear();

    public abstract boolean shouldSkipAllocator();

    public abstract boolean isBackingWrapped();

    public final void isUsedAsTaskTarget() {
        this.mTaskTargetCount++;
    }

    public final int getTaskTargetCount() {
        return this.mTaskTargetCount;
    }

    @Nullable
    public abstract IGpuSurface getGpuSurface();

    @Nullable
    public GpuTexture getGpuTexture() {
        return null;
    }

    @Nullable
    public GpuRenderTarget getGpuRenderTarget() {
        return null;
    }

    public final boolean isBudgeted() {
        return (this.mSurfaceFlags & 1) != 0;
    }

    public final boolean isReadOnly() {
        return (this.mSurfaceFlags & 32) != 0;
    }

    public final boolean isProtected() {
        return (this.mSurfaceFlags & 16) != 0;
    }

    public final boolean isManualMSAAResolve() {
        return (this.mSurfaceFlags & 512) != 0;
    }

    public final boolean wrapsGLDefaultFB() {
        return (this.mSurfaceFlags & 256) != 0;
    }

    public final boolean wrapsVkSecondaryCB() {
        return (this.mSurfaceFlags & 1024) != 0;
    }

    public final boolean isDeferredListTarget() {
        return this.mIsDeferredListTarget;
    }

    @Internal
    public void setIsDeferredListTarget() {
        this.mIsDeferredListTarget = true;
    }

    @Internal
    public final boolean isUserExact() {
        return (this.mSurfaceFlags & 2) == 0;
    }

    public TextureProxy asTexture() {
        return null;
    }

    public long getMemorySize() {
        return 0L;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Internal
    public abstract boolean doLazyInstantiation(ResourceProvider var1);

    public static class LazyCallbackResult {

        @SharedPtr
        public IGpuSurface mSurface;

        public boolean mSyncTargetKey = true;

        public boolean mReleaseCallback = true;

        public LazyCallbackResult() {
        }

        public LazyCallbackResult(@SharedPtr IGpuSurface surface) {
            this.mSurface = surface;
        }

        public LazyCallbackResult(@SharedPtr IGpuSurface surface, boolean syncTargetKey, boolean releaseCallback) {
            this.mSurface = surface;
            this.mSyncTargetKey = syncTargetKey;
            this.mReleaseCallback = releaseCallback;
        }
    }

    @FunctionalInterface
    public interface LazyInstantiateCallback extends AutoCloseable {

        SurfaceProxy.LazyCallbackResult onLazyInstantiate(ResourceProvider var1, BackendFormat var2, int var3, int var4, int var5, int var6, String var7);

        default void close() {
        }
    }
}