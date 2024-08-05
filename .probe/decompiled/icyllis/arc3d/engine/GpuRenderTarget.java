package icyllis.arc3d.engine;

import icyllis.arc3d.core.SharedPtr;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class GpuRenderTarget extends ManagedResource implements IGpuSurface {

    private final int mWidth;

    private final int mHeight;

    private final int mSampleCount;

    @SharedPtr
    protected Attachment mStencilBuffer;

    protected int mSurfaceFlags = 8;

    protected GpuRenderTarget(GpuDevice device, int width, int height, int sampleCount) {
        super(device);
        this.mWidth = width;
        this.mHeight = height;
        this.mSampleCount = sampleCount;
    }

    @Override
    public final int getWidth() {
        return this.mWidth;
    }

    @Override
    public final int getHeight() {
        return this.mHeight;
    }

    @Override
    public final int getSampleCount() {
        return this.mSampleCount;
    }

    @Nonnull
    @Override
    public abstract BackendFormat getBackendFormat();

    @Nonnull
    public abstract BackendRenderTarget getBackendRenderTarget();

    @Nullable
    @Override
    public abstract GpuTexture asTexture();

    @Override
    public final GpuRenderTarget asRenderTarget() {
        return this;
    }

    @Override
    public int getSurfaceFlags() {
        return this.mSurfaceFlags;
    }

    public final Attachment getStencilBuffer() {
        return this.mStencilBuffer;
    }

    public final int getStencilBits() {
        return this.mStencilBuffer != null ? this.mStencilBuffer.getBackendFormat().getStencilBits() : 0;
    }

    @Override
    protected void deallocate() {
        if (this.mStencilBuffer != null) {
            this.mStencilBuffer.unref();
        }
        this.mStencilBuffer = null;
    }

    protected abstract boolean canAttachStencil();

    protected abstract void attachStencilBuffer(@SharedPtr Attachment var1);
}