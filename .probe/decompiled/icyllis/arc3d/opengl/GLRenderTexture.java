package icyllis.arc3d.opengl;

import icyllis.arc3d.core.RefCnt;
import icyllis.arc3d.core.SharedPtr;
import icyllis.arc3d.engine.BackendFormat;
import icyllis.arc3d.engine.GpuTexture;
import java.util.function.Function;
import javax.annotation.Nonnull;

public final class GLRenderTexture extends GLTexture {

    @SharedPtr
    private GLRenderTarget mRenderTarget;

    GLRenderTexture(GLDevice device, int width, int height, GLTextureInfo info, BackendFormat format, boolean budgeted, Function<GLTexture, GLRenderTarget> function) {
        super(device, width, height, info, format, budgeted, false);
        this.mRenderTarget = (GLRenderTarget) function.apply(this);
        this.mFlags |= 8;
        this.registerWithCache(budgeted);
    }

    @Override
    public int getSampleCount() {
        return this.mRenderTarget.getSampleCount();
    }

    @Nonnull
    public GLRenderTarget asRenderTarget() {
        return this.mRenderTarget;
    }

    @Override
    protected void onRelease() {
        this.mRenderTarget = RefCnt.move(this.mRenderTarget);
        super.onRelease();
    }

    @Override
    protected void onDiscard() {
        this.mRenderTarget = RefCnt.move(this.mRenderTarget);
        super.onDiscard();
    }

    @Override
    protected GpuTexture.ScratchKey computeScratchKey() {
        return new GpuTexture.ScratchKey().compute(this.getBackendFormat(), this.mWidth, this.mHeight, this.getSampleCount(), this.mFlags);
    }

    @Override
    public String toString() {
        return "GLRenderTexture{mWidth=" + this.mWidth + ", mHeight=" + this.mHeight + ", mDestroyed=" + this.isDestroyed() + ", mLabel=" + this.getLabel() + ", mMemorySize=" + this.getMemorySize() + ", mRenderTarget=" + this.mRenderTarget + "}";
    }
}