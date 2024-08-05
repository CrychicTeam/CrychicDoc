package icyllis.arc3d.engine;

import icyllis.arc3d.opengl.GLTextureInfo;
import icyllis.arc3d.vulkan.VulkanImageInfo;
import javax.annotation.Nonnull;

public abstract class BackendTexture {

    protected final int mWidth;

    protected final int mHeight;

    protected BackendTexture(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
    }

    public abstract int getBackend();

    public final int getWidth() {
        return this.mWidth;
    }

    public final int getHeight() {
        return this.mHeight;
    }

    public abstract boolean isExternal();

    public abstract boolean isMipmapped();

    public boolean getGLTextureInfo(GLTextureInfo info) {
        return false;
    }

    public void glTextureParametersModified() {
    }

    public boolean getVkImageInfo(VulkanImageInfo info) {
        return false;
    }

    public void setVkImageLayout(int layout) {
    }

    public void setVkQueueFamilyIndex(int queueFamilyIndex) {
    }

    @Nonnull
    public abstract BackendFormat getBackendFormat();

    public abstract boolean isProtected();

    public abstract boolean isSameTexture(BackendTexture var1);
}