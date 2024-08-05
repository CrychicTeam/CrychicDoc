package icyllis.arc3d.opengl;

import icyllis.arc3d.core.SharedPtr;
import icyllis.arc3d.engine.Attachment;
import icyllis.arc3d.engine.BackendFormat;
import icyllis.arc3d.engine.BackendRenderTarget;
import icyllis.arc3d.engine.GpuRenderTarget;
import icyllis.arc3d.engine.GpuResource;
import javax.annotation.Nonnull;

public final class GLRenderTarget extends GpuRenderTarget {

    private final int mFormat;

    private GLTexture mColorBuffer;

    @SharedPtr
    private GLAttachment mMultisampleColorBuffer;

    private int mSampleFramebuffer;

    private int mResolveFramebuffer;

    private boolean mRebindStencilBuffer;

    private final boolean mOwnership;

    private BackendFormat mBackendFormat;

    private BackendRenderTarget mBackendRenderTarget;

    GLRenderTarget(GLDevice device, int width, int height, int format, int sampleCount, int framebuffer, int msaaFramebuffer, GLTexture colorBuffer, GLAttachment msaaColorBuffer) {
        super(device, width, height, sampleCount);
        assert sampleCount > 0;
        this.mFormat = format;
        this.mSampleFramebuffer = sampleCount > 1 ? msaaFramebuffer : framebuffer;
        this.mResolveFramebuffer = framebuffer;
        this.mOwnership = true;
        this.mColorBuffer = colorBuffer;
        this.mMultisampleColorBuffer = msaaColorBuffer;
        this.mSurfaceFlags = this.mSurfaceFlags | colorBuffer.getSurfaceFlags();
    }

    private GLRenderTarget(GLDevice device, int width, int height, int format, int sampleCount, int framebuffer, boolean ownership, @SharedPtr GLAttachment stencilBuffer) {
        super(device, width, height, sampleCount);
        assert sampleCount > 0;
        assert framebuffer != 0 || !ownership;
        this.mFormat = format;
        this.mSampleFramebuffer = framebuffer;
        this.mResolveFramebuffer = 0;
        this.mOwnership = ownership;
        this.mStencilBuffer = stencilBuffer;
        if (framebuffer == 0) {
            this.mSurfaceFlags |= 256;
        }
    }

    @Nonnull
    @SharedPtr
    public static GLRenderTarget makeWrapped(GLDevice device, int width, int height, int format, int sampleCount, int framebuffer, int stencilBits, boolean ownership) {
        assert sampleCount > 0;
        assert framebuffer != 0 || !ownership;
        GLAttachment stencilBuffer = null;
        if (stencilBits > 0) {
            int stencilFormat = switch(stencilBits) {
                case 8 ->
                    '裰';
                case 16 ->
                    '赉';
                default ->
                    '\u0000';
            };
            stencilBuffer = GLAttachment.makeWrapped(device, width, height, sampleCount, stencilFormat, 0);
        }
        return new GLRenderTarget(device, width, height, format, sampleCount, framebuffer, ownership, stencilBuffer);
    }

    public int getFormat() {
        return this.mFormat;
    }

    public int getSampleFramebuffer() {
        return this.mSampleFramebuffer;
    }

    public int getResolveFramebuffer() {
        return this.mResolveFramebuffer;
    }

    public void bindStencil() {
        if (this.mRebindStencilBuffer) {
            int framebuffer = this.mSampleFramebuffer;
            GLAttachment stencilBuffer = (GLAttachment) this.mStencilBuffer;
            if (stencilBuffer != null) {
                GLCore.glNamedFramebufferRenderbuffer(framebuffer, 36128, 36161, stencilBuffer.getRenderbufferID());
                if (GLCore.glFormatIsPackedDepthStencil(stencilBuffer.getFormat())) {
                    GLCore.glNamedFramebufferRenderbuffer(framebuffer, 36096, 36161, stencilBuffer.getRenderbufferID());
                } else {
                    GLCore.glNamedFramebufferRenderbuffer(framebuffer, 36096, 36161, 0);
                }
            } else {
                GLCore.glNamedFramebufferRenderbuffer(framebuffer, 36128, 36161, 0);
                GLCore.glNamedFramebufferRenderbuffer(framebuffer, 36096, 36161, 0);
            }
            this.mRebindStencilBuffer = false;
        }
    }

    @Nonnull
    @Override
    public BackendFormat getBackendFormat() {
        if (this.mBackendFormat == null) {
            this.mBackendFormat = GLBackendFormat.make(this.mFormat);
        }
        return this.mBackendFormat;
    }

    public GLTexture asTexture() {
        return this.mColorBuffer;
    }

    @Nonnull
    @Override
    public BackendRenderTarget getBackendRenderTarget() {
        if (this.mBackendRenderTarget == null) {
            GLFramebufferInfo info = new GLFramebufferInfo();
            info.mFramebuffer = this.mSampleFramebuffer;
            info.mFormat = this.mFormat;
            this.mBackendRenderTarget = new GLBackendRenderTarget(this.getWidth(), this.getHeight(), this.getSampleCount(), this.getStencilBits(), info);
        }
        return this.mBackendRenderTarget;
    }

    @Override
    protected boolean canAttachStencil() {
        return this.mOwnership;
    }

    @Override
    protected void attachStencilBuffer(@SharedPtr Attachment stencilBuffer) {
        if (stencilBuffer != null || this.mStencilBuffer != null) {
            if (this.mStencilBuffer != stencilBuffer) {
                this.mRebindStencilBuffer = true;
            }
            this.mStencilBuffer = GpuResource.move(this.mStencilBuffer, stencilBuffer);
        }
    }

    @Override
    protected void deallocate() {
        super.deallocate();
        if (this.mOwnership) {
            if (this.mSampleFramebuffer != 0) {
                GLCore.glDeleteFramebuffers(this.mSampleFramebuffer);
            }
            if (this.mSampleFramebuffer != this.mResolveFramebuffer) {
                assert this.mResolveFramebuffer != 0;
                GLCore.glDeleteFramebuffers(this.mResolveFramebuffer);
            }
        }
        this.mSampleFramebuffer = 0;
        this.mResolveFramebuffer = 0;
    }

    public String toString() {
        return "GLRenderTarget{mRenderFramebuffer=" + this.mSampleFramebuffer + ", mResolveFramebuffer=" + this.mResolveFramebuffer + ", mFormat=" + GLCore.glFormatName(this.mFormat) + ", mSampleCount=" + this.getSampleCount() + ", mMultisampleColorBuffer=" + this.mMultisampleColorBuffer + ", mOwnership=" + this.mOwnership + ", mBackendFormat=" + this.mBackendFormat + "}";
    }
}