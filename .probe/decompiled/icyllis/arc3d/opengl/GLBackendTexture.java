package icyllis.arc3d.opengl;

import icyllis.arc3d.engine.BackendFormat;
import icyllis.arc3d.engine.BackendTexture;
import javax.annotation.Nonnull;

public final class GLBackendTexture extends BackendTexture {

    private final GLTextureInfo mInfo;

    final GLTextureParameters mParams;

    private final BackendFormat mBackendFormat;

    public GLBackendTexture(int width, int height, GLTextureInfo info) {
        this(width, height, info, new GLTextureParameters(), GLBackendFormat.make(info.format, info.memoryHandle != -1));
        assert info.format != 0;
        this.glTextureParametersModified();
    }

    GLBackendTexture(int width, int height, GLTextureInfo info, GLTextureParameters params, BackendFormat backendFormat) {
        super(width, height);
        this.mInfo = info;
        this.mParams = params;
        this.mBackendFormat = backendFormat;
    }

    @Override
    public int getBackend() {
        return 0;
    }

    @Override
    public boolean isExternal() {
        return this.mBackendFormat.isExternal();
    }

    @Override
    public boolean isMipmapped() {
        return this.mInfo.levels > 1;
    }

    @Override
    public boolean getGLTextureInfo(GLTextureInfo info) {
        info.set(this.mInfo);
        return true;
    }

    @Override
    public void glTextureParametersModified() {
        this.mParams.invalidate();
    }

    @Nonnull
    @Override
    public BackendFormat getBackendFormat() {
        return this.mBackendFormat;
    }

    @Override
    public boolean isProtected() {
        return false;
    }

    @Override
    public boolean isSameTexture(BackendTexture texture) {
        return texture instanceof GLBackendTexture t ? this.mInfo.handle == t.mInfo.handle : false;
    }

    public String toString() {
        return "{mBackend=OpenGL, mInfo=" + this.mInfo + ", mParams=" + this.mParams + ", mBackendFormat=" + this.mBackendFormat + "}";
    }
}