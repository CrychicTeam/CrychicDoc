package icyllis.arc3d.opengl;

import icyllis.arc3d.core.SharedPtr;
import icyllis.arc3d.engine.Attachment;
import icyllis.arc3d.engine.BackendFormat;
import icyllis.arc3d.engine.DataUtils;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class GLAttachment extends Attachment {

    private int mRenderbuffer;

    private final int mFormat;

    private BackendFormat mBackendFormat;

    private final long mMemorySize;

    private GLAttachment(GLDevice device, int width, int height, int sampleCount, int format, int renderbuffer) {
        super(device, width, height, sampleCount);
        this.mRenderbuffer = renderbuffer;
        this.mFormat = format;
        this.mMemorySize = DataUtils.numBlocks(GLCore.glFormatCompressionType(format), width, height) * (long) GLCore.glFormatBytesPerBlock(format) * (long) sampleCount;
        this.registerWithCache(true);
    }

    @Nullable
    @SharedPtr
    public static GLAttachment makeStencil(GLDevice device, int width, int height, int sampleCount, int format) {
        assert sampleCount > 0 && GLCore.glFormatStencilBits(format) > 0;
        int renderbuffer = GLCore.glGenRenderbuffers();
        if (renderbuffer == 0) {
            return null;
        } else {
            GLCore.glBindRenderbuffer(36161, renderbuffer);
            if (device.getCaps().skipErrorChecks()) {
                if (sampleCount > 1) {
                    GLCore.glRenderbufferStorageMultisample(36161, sampleCount, format, width, height);
                } else {
                    GLCore.glRenderbufferStorage(36161, format, width, height);
                }
            } else {
                GLCore.glClearErrors();
                if (sampleCount > 1) {
                    GLCore.glRenderbufferStorageMultisample(36161, sampleCount, format, width, height);
                } else {
                    GLCore.glRenderbufferStorage(36161, format, width, height);
                }
                if (GLCore.glGetError() != 0) {
                    GLCore.glDeleteRenderbuffers(renderbuffer);
                    return null;
                }
            }
            return new GLAttachment(device, width, height, sampleCount, format, renderbuffer);
        }
    }

    @Nullable
    @SharedPtr
    public static GLAttachment makeColor(GLDevice device, int width, int height, int sampleCount, int format) {
        assert sampleCount > 1;
        int renderbuffer = GLCore.glGenRenderbuffers();
        if (renderbuffer == 0) {
            return null;
        } else {
            GLCore.glBindRenderbuffer(36161, renderbuffer);
            int internalFormat = device.getCaps().getRenderbufferInternalFormat(format);
            if (device.getCaps().skipErrorChecks()) {
                GLCore.glRenderbufferStorageMultisample(36161, sampleCount, internalFormat, width, height);
            } else {
                GLCore.glClearErrors();
                GLCore.glRenderbufferStorageMultisample(36161, sampleCount, internalFormat, width, height);
                if (GLCore.glGetError() != 0) {
                    GLCore.glDeleteRenderbuffers(renderbuffer);
                    return null;
                }
            }
            return new GLAttachment(device, width, height, sampleCount, format, renderbuffer);
        }
    }

    @Nonnull
    @SharedPtr
    public static GLAttachment makeWrapped(GLDevice device, int width, int height, int sampleCount, int format, int renderbuffer) {
        assert sampleCount > 0;
        return new GLAttachment(device, width, height, sampleCount, format, renderbuffer);
    }

    @Nonnull
    @Override
    public BackendFormat getBackendFormat() {
        if (this.mBackendFormat == null) {
            this.mBackendFormat = GLBackendFormat.make(this.mFormat);
        }
        return this.mBackendFormat;
    }

    public int getRenderbufferID() {
        return this.mRenderbuffer;
    }

    public int getFormat() {
        return this.mFormat;
    }

    @Override
    public long getMemorySize() {
        return this.mMemorySize;
    }

    @Override
    protected void onRelease() {
        if (this.mRenderbuffer != 0) {
            GLCore.glDeleteRenderbuffers(this.mRenderbuffer);
        }
        this.mRenderbuffer = 0;
    }

    @Override
    protected void onDiscard() {
        this.mRenderbuffer = 0;
    }

    public String toString() {
        return "GLAttachment{mRenderbuffer=" + this.mRenderbuffer + ", mFormat=" + GLCore.glFormatName(this.mFormat) + ", mMemorySize=" + this.mMemorySize + ", mWidth=" + this.mWidth + ", mHeight=" + this.mHeight + ", mSampleCount=" + this.mSampleCount + "}";
    }
}