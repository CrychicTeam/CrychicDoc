package icyllis.arc3d.engine;

import icyllis.arc3d.core.Rect2i;
import icyllis.arc3d.core.Rect2ic;
import javax.annotation.Nullable;
import org.jetbrains.annotations.VisibleForTesting;

@VisibleForTesting
public final class RenderTextureProxy extends TextureProxy {

    private final int mSampleCount;

    private final Rect2i mResolveRect = new Rect2i();

    RenderTextureProxy(BackendFormat format, int width, int height, int sampleCount, int surfaceFlags) {
        super(format, width, height, surfaceFlags);
        this.mSampleCount = sampleCount;
    }

    RenderTextureProxy(BackendFormat format, int width, int height, int sampleCount, int surfaceFlags, SurfaceProxy.LazyInstantiateCallback callback) {
        super(format, width, height, surfaceFlags, callback);
        this.mSampleCount = sampleCount;
    }

    @Override
    public int getSampleCount() {
        return this.mSampleCount;
    }

    @Nullable
    @Override
    public GpuRenderTarget getGpuRenderTarget() {
        return this.mGpuTexture != null ? this.mGpuTexture.asRenderTarget() : null;
    }

    @Override
    public void setResolveRect(int left, int top, int right, int bottom) {
        assert this.isManualMSAAResolve();
        if (left == 0 && top == 0 && right == 0 && bottom == 0) {
            this.mResolveRect.setEmpty();
        } else {
            assert right > left && bottom > top;
            assert left >= 0 && right <= this.getBackingWidth() && top >= 0 && bottom <= this.getBackingHeight();
            this.mResolveRect.join(left, top, right, bottom);
        }
    }

    @Override
    public boolean needsResolve() {
        assert this.mResolveRect.isEmpty() || this.isManualMSAAResolve();
        return this.isManualMSAAResolve() && !this.mResolveRect.isEmpty();
    }

    @Override
    public Rect2ic getResolveRect() {
        assert this.isManualMSAAResolve();
        return this.mResolveRect;
    }
}