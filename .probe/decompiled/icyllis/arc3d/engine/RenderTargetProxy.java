package icyllis.arc3d.engine;

import icyllis.arc3d.core.SharedPtr;
import javax.annotation.Nullable;
import org.jetbrains.annotations.VisibleForTesting;

@VisibleForTesting
public final class RenderTargetProxy extends SurfaceProxy {

    @SharedPtr
    private GpuRenderTarget mRenderTarget;

    private int mSampleCount;

    RenderTargetProxy(BackendFormat format, int width, int height, int surfaceFlags) {
        super(format, width, height, surfaceFlags);
        assert this.hashCode() == System.identityHashCode(this);
    }

    RenderTargetProxy(GpuRenderTarget renderTarget, int surfaceFlags) {
        super(renderTarget, surfaceFlags);
        this.mRenderTarget = renderTarget;
        this.mSampleCount = renderTarget.getSampleCount();
    }

    @Override
    protected void deallocate() {
        this.mRenderTarget = move(this.mRenderTarget);
    }

    @Override
    public boolean isLazy() {
        return this.mRenderTarget == null && this.mLazyInstantiateCallback != null;
    }

    @Override
    public int getBackingWidth() {
        assert !this.isLazyMost();
        if (this.mRenderTarget != null) {
            return this.mRenderTarget.getWidth();
        } else {
            return (this.mSurfaceFlags & 2) != 0 ? ISurface.getApproxSize(this.mWidth) : this.mWidth;
        }
    }

    @Override
    public int getBackingHeight() {
        assert !this.isLazyMost();
        if (this.mRenderTarget != null) {
            return this.mRenderTarget.getHeight();
        } else {
            return (this.mSurfaceFlags & 2) != 0 ? ISurface.getApproxSize(this.mHeight) : this.mHeight;
        }
    }

    @Override
    public int getSampleCount() {
        return this.mSampleCount;
    }

    @Override
    public Object getBackingUniqueID() {
        return this.mRenderTarget != null ? this.mRenderTarget : this.mUniqueID;
    }

    @Override
    public boolean isInstantiated() {
        return this.mRenderTarget != null;
    }

    @Override
    public boolean instantiate(ResourceProvider resourceProvider) {
        return this.isLazy() ? false : this.mRenderTarget != null;
    }

    @Override
    public void clear() {
        assert this.mRenderTarget != null;
        this.mRenderTarget.unref();
        this.mRenderTarget = null;
    }

    @Override
    public boolean shouldSkipAllocator() {
        return (this.mSurfaceFlags & 64) != 0 ? true : this.mRenderTarget != null;
    }

    @Override
    public boolean isBackingWrapped() {
        return this.mRenderTarget != null;
    }

    @Nullable
    @Override
    public IGpuSurface getGpuSurface() {
        return this.mRenderTarget;
    }

    @Nullable
    @Override
    public GpuRenderTarget getGpuRenderTarget() {
        return this.mRenderTarget != null ? this.mRenderTarget.asRenderTarget() : null;
    }

    @Override
    public boolean doLazyInstantiation(ResourceProvider resourceProvider) {
        assert this.isLazy();
        GpuRenderTarget surface = null;
        boolean releaseCallback = false;
        int width = this.isLazyMost() ? -1 : this.getWidth();
        int height = this.isLazyMost() ? -1 : this.getHeight();
        SurfaceProxy.LazyCallbackResult result = this.mLazyInstantiateCallback.onLazyInstantiate(resourceProvider, this.mFormat, width, height, this.getSampleCount(), this.mSurfaceFlags, "");
        if (result != null) {
            surface = (GpuRenderTarget) result.mSurface;
            releaseCallback = result.mReleaseCallback;
        }
        if (surface == null) {
            this.mWidth = this.mHeight = 0;
            return false;
        } else {
            if (this.isLazyMost()) {
                this.mWidth = surface.getWidth();
                this.mHeight = surface.getHeight();
            }
            assert this.getWidth() <= surface.getWidth();
            assert this.getHeight() <= surface.getHeight();
            this.mRenderTarget = move(this.mRenderTarget, surface);
            if (releaseCallback) {
                this.mLazyInstantiateCallback = null;
            }
            return true;
        }
    }
}