package icyllis.arc3d.opengl;

import icyllis.arc3d.core.Kernel32;
import icyllis.arc3d.engine.BackendFormat;
import icyllis.arc3d.engine.BackendTexture;
import icyllis.arc3d.engine.GpuTexture;
import javax.annotation.Nonnull;
import org.lwjgl.opengl.EXTMemoryObject;
import org.lwjgl.system.Platform;

public class GLTexture extends GpuTexture {

    private GLTextureInfo mInfo;

    private final GLBackendTexture mBackendTexture;

    private final boolean mOwnership;

    private final long mMemorySize;

    GLTexture(GLDevice device, int width, int height, GLTextureInfo info, BackendFormat format, boolean budgeted, boolean register) {
        super(device, width, height);
        assert info.handle != 0;
        assert GLCore.glFormatIsSupported(format.getGLFormat());
        this.mInfo = info;
        this.mBackendTexture = new GLBackendTexture(width, height, info, new GLTextureParameters(), format);
        this.mOwnership = true;
        if (GLCore.glFormatIsCompressed(format.getGLFormat()) || format.isExternal()) {
            this.mFlags |= 32;
        }
        if (this.mBackendTexture.isMipmapped()) {
            this.mFlags |= 4;
        }
        this.mMemorySize = computeSize(format, width, height, 1, info.levels);
        if (register) {
            this.registerWithCache(budgeted);
        }
    }

    public GLTexture(GLDevice device, int width, int height, GLTextureInfo info, GLTextureParameters params, BackendFormat format, int ioType, boolean cacheable, boolean ownership) {
        super(device, width, height);
        assert info.handle != 0;
        assert GLCore.glFormatIsSupported(format.getGLFormat());
        this.mInfo = info;
        this.mBackendTexture = new GLBackendTexture(width, height, info, params, format);
        this.mOwnership = ownership;
        assert ioType == 0 || format.isCompressed();
        if (ioType == 0 || format.isExternal()) {
            this.mFlags |= 32;
        }
        if (this.mBackendTexture.isMipmapped()) {
            this.mFlags |= 4;
        }
        this.mMemorySize = computeSize(format, width, height, 1, info.levels);
        this.registerWithCacheWrapped(cacheable);
    }

    @Nonnull
    @Override
    public BackendFormat getBackendFormat() {
        return this.mBackendTexture.getBackendFormat();
    }

    public int getHandle() {
        return this.mInfo.handle;
    }

    public int getGLFormat() {
        return this.getBackendFormat().getGLFormat();
    }

    @Nonnull
    public GLTextureParameters getParameters() {
        return this.mBackendTexture.mParams;
    }

    @Override
    public boolean isExternal() {
        return this.mBackendTexture.isExternal();
    }

    @Nonnull
    @Override
    public BackendTexture getBackendTexture() {
        return this.mBackendTexture;
    }

    @Override
    public int getMaxMipmapLevel() {
        return this.mInfo.levels - 1;
    }

    @Override
    public long getMemorySize() {
        return this.mMemorySize;
    }

    @Override
    protected void onSetLabel(@Nonnull String label) {
        if (this.getDevice().getCaps().hasDebugSupport()) {
            assert this.mInfo != null;
            if (label.isEmpty()) {
                GLCore.nglObjectLabel(5890, this.mInfo.handle, 0, 0L);
            } else {
                label = label.substring(0, Math.min(label.length(), this.getDevice().getCaps().maxLabelLength()));
                GLCore.glObjectLabel(5890, this.mInfo.handle, label);
            }
        }
    }

    @Override
    protected void onRelease() {
        GLTextureInfo info = this.mInfo;
        if (this.mOwnership) {
            if (info.handle != 0) {
                GLCore.glDeleteTextures(info.handle);
            }
            if (info.memoryObject != 0) {
                EXTMemoryObject.glDeleteMemoryObjectsEXT(info.memoryObject);
            }
            if (info.memoryHandle != -1 && Platform.get() == Platform.WINDOWS) {
                Kernel32.CloseHandle((long) info.memoryHandle);
            }
        }
        this.mInfo = null;
        super.onRelease();
    }

    @Override
    protected void onDiscard() {
        this.mInfo = null;
        super.onDiscard();
    }

    protected GLDevice getDevice() {
        return (GLDevice) super.getDevice();
    }

    public String toString() {
        return "GLTexture{mWidth=" + this.mWidth + ", mHeight=" + this.mHeight + ", mBackendTexture=" + this.mBackendTexture + ", mDestroyed=" + this.isDestroyed() + ", mOwnership=" + this.mOwnership + ", mLabel=" + this.getLabel() + ", mMemorySize=" + this.mMemorySize + "}";
    }
}