package icyllis.modernui.graphics;

import icyllis.arc3d.engine.DirectContext;
import icyllis.arc3d.engine.GpuResource;
import icyllis.arc3d.opengl.GLAttachment;
import icyllis.arc3d.opengl.GLBackendFormat;
import icyllis.arc3d.opengl.GLCore;
import icyllis.arc3d.opengl.GLDevice;
import icyllis.arc3d.opengl.GLTexture;
import icyllis.modernui.core.Core;
import java.nio.FloatBuffer;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.lwjgl.BufferUtils;

public final class GLSurface implements AutoCloseable {

    public static final int NUM_RENDER_TARGETS = 3;

    private final FloatBuffer mClearColor = BufferUtils.createFloatBuffer(4);

    private final GLTexture[] mColorAttachments = new GLTexture[3];

    private GLAttachment mStencilAttachment;

    private int mBackingWidth;

    private int mBackingHeight;

    private int mFramebuffer;

    public int get() {
        if (this.mFramebuffer == 0) {
            this.mFramebuffer = GLCore.glGenFramebuffers();
        }
        return this.mFramebuffer;
    }

    public void bind() {
        GLCore.glBindFramebuffer(36160, this.get());
    }

    public void bindDraw() {
        GLCore.glBindFramebuffer(36009, this.get());
    }

    public void bindRead() {
        GLCore.glBindFramebuffer(36008, this.get());
    }

    public void clearColorBuffer() {
        GLCore.glClearBufferfv(6144, 0, this.mClearColor);
    }

    public void clearStencilBuffer() {
        GLCore.glClearBufferfi(34041, 0, 1.0F, 0);
    }

    public void setDrawBuffer(int buffer) {
        GLCore.glDrawBuffer(buffer);
    }

    public void setReadBuffer(int buffer) {
        GLCore.glReadBuffer(buffer);
    }

    public int getBackingWidth() {
        return this.mBackingWidth;
    }

    public int getBackingHeight() {
        return this.mBackingHeight;
    }

    @Nonnull
    public GLTexture getAttachedTexture(int attachment) {
        return (GLTexture) Objects.requireNonNull(this.mColorAttachments[attachment - 36064]);
    }

    public void makeBuffers(int width, int height, boolean exact) {
        if (width > 0 && height > 0) {
            if (exact) {
                if (this.mBackingWidth == width && this.mBackingHeight == height) {
                    return;
                }
            } else if (this.mBackingWidth >= width && this.mBackingHeight >= height) {
                return;
            }
            this.mBackingWidth = width;
            this.mBackingHeight = height;
            DirectContext dContext = Core.requireDirectContext();
            for (int i = 0; i < 3; i++) {
                if (this.mColorAttachments[i] != null) {
                    this.mColorAttachments[i].unref();
                }
                this.mColorAttachments[i] = (GLTexture) dContext.getResourceProvider().createTexture(width, height, GLBackendFormat.make(32856), 1, 1, null);
                Objects.requireNonNull(this.mColorAttachments[i], "Failed to create G-buffer " + i);
                GLCore.glFramebufferTexture(36160, 36064 + i, this.mColorAttachments[i].getHandle(), 0);
            }
            if (this.mStencilAttachment != null) {
                this.mStencilAttachment.unref();
            }
            this.mStencilAttachment = GLAttachment.makeStencil((GLDevice) dContext.getDevice(), width, height, 1, 36168);
            Objects.requireNonNull(this.mStencilAttachment, "Failed to create depth/stencil");
            GLCore.glFramebufferRenderbuffer(36160, 36128, 36161, this.mStencilAttachment.getRenderbufferID());
            int status = GLCore.glCheckFramebufferStatus(36160);
            if (status != 36053) {
                throw new IllegalStateException("Framebuffer is not complete: " + status);
            }
        }
    }

    public void close() {
        if (this.mFramebuffer != 0) {
            GLCore.glDeleteFramebuffers(this.mFramebuffer);
        }
        this.mFramebuffer = 0;
        for (int i = 0; i < 3; i++) {
            this.mColorAttachments[i] = GpuResource.move(this.mColorAttachments[i]);
        }
        this.mStencilAttachment = GpuResource.move(this.mStencilAttachment);
    }
}