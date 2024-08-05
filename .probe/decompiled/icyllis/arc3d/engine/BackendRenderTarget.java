package icyllis.arc3d.engine;

import icyllis.arc3d.opengl.GLFramebufferInfo;
import icyllis.arc3d.vulkan.VulkanImageInfo;
import javax.annotation.Nonnull;

public abstract class BackendRenderTarget {

    private final int mWidth;

    private final int mHeight;

    public BackendRenderTarget(int width, int height) {
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

    public abstract int getSampleCount();

    public abstract int getStencilBits();

    public boolean getGLFramebufferInfo(GLFramebufferInfo info) {
        return false;
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
}