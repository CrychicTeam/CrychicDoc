package icyllis.arc3d.vulkan;

import icyllis.arc3d.engine.BackendFormat;
import icyllis.arc3d.engine.BackendTexture;
import icyllis.arc3d.engine.GpuRenderTarget;
import icyllis.arc3d.engine.GpuTexture;
import javax.annotation.Nonnull;

public final class VulkanTexture extends GpuTexture {

    public VulkanTexture(VulkanDevice device, int width, int height) {
        super(device, width, height);
    }

    @Override
    public long getMemorySize() {
        return 0L;
    }

    @Override
    protected void onRelease() {
    }

    @Override
    protected void onDiscard() {
    }

    @Override
    public int getSampleCount() {
        return 0;
    }

    @Nonnull
    @Override
    public BackendFormat getBackendFormat() {
        return null;
    }

    @Override
    public GpuRenderTarget asRenderTarget() {
        return null;
    }

    @Override
    public boolean isExternal() {
        return false;
    }

    @Nonnull
    @Override
    public BackendTexture getBackendTexture() {
        return null;
    }

    @Override
    public int getMaxMipmapLevel() {
        return 0;
    }
}