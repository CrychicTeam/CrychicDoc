package icyllis.arc3d.opengl;

import icyllis.arc3d.engine.BackendRenderTarget;
import javax.annotation.Nonnull;

public final class GLBackendRenderTarget extends BackendRenderTarget {

    private final int mSampleCount;

    private final int mStencilBits;

    private final GLFramebufferInfo mInfo;

    private GLBackendFormat mBackendFormat;

    public GLBackendRenderTarget(int width, int height, int sampleCount, int stencilBits, GLFramebufferInfo info) {
        super(width, height);
        this.mSampleCount = sampleCount;
        this.mStencilBits = stencilBits;
        this.mInfo = info;
        assert sampleCount > 0;
    }

    @Override
    public int getBackend() {
        return 0;
    }

    @Override
    public int getSampleCount() {
        return this.mSampleCount;
    }

    @Override
    public int getStencilBits() {
        return this.mStencilBits;
    }

    @Override
    public boolean getGLFramebufferInfo(GLFramebufferInfo info) {
        info.set(this.mInfo);
        return true;
    }

    @Nonnull
    public GLBackendFormat getBackendFormat() {
        if (this.mBackendFormat == null) {
            this.mBackendFormat = GLBackendFormat.make(this.mInfo.mFormat);
        }
        return this.mBackendFormat;
    }

    @Override
    public boolean isProtected() {
        return false;
    }
}