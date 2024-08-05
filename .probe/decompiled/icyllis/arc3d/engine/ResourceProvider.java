package icyllis.arc3d.engine;

import icyllis.arc3d.core.ImageInfo;
import icyllis.arc3d.core.SharedPtr;
import javax.annotation.Nullable;

public class ResourceProvider {

    private final GpuDevice mDevice;

    private final DirectContext mContext;

    private final GpuTexture.ScratchKey mTextureScratchKey = new GpuTexture.ScratchKey();

    protected ResourceProvider(GpuDevice device, DirectContext context) {
        this.mDevice = device;
        this.mContext = context;
    }

    @Nullable
    @SharedPtr
    public final <T extends GpuResource> T findByUniqueKey(IUniqueKey key) {
        assert this.mDevice.getContext().isOwnerThread();
        return (T) (this.mDevice.getContext().isDiscarded() ? null : this.mContext.getResourceCache().findAndRefUniqueResource(key));
    }

    @Nullable
    @SharedPtr
    public final GpuTexture createTexture(int width, int height, BackendFormat format, int sampleCount, int surfaceFlags, String label) {
        assert this.mDevice.getContext().isOwnerThread();
        if (this.mDevice.getContext().isDiscarded()) {
            return null;
        } else {
            assert !format.isCompressed();
            if (!this.mDevice.getCaps().validateSurfaceParams(width, height, format, sampleCount, surfaceFlags)) {
                return null;
            } else {
                if ((surfaceFlags & 2) != 0) {
                    width = ISurface.getApproxSize(width);
                    height = ISurface.getApproxSize(height);
                    surfaceFlags &= 24;
                    surfaceFlags |= 1;
                }
                GpuTexture texture = this.findAndRefScratchTexture(width, height, format, sampleCount, surfaceFlags, label);
                if (texture != null) {
                    if ((surfaceFlags & 1) == 0) {
                        texture.makeBudgeted(false);
                    }
                    return texture;
                } else {
                    return this.mDevice.createTexture(width, height, format, sampleCount, surfaceFlags, label);
                }
            }
        }
    }

    @Nullable
    @SharedPtr
    public final GpuTexture createTexture(int width, int height, BackendFormat format, int sampleCount, int surfaceFlags, int dstColorType, int srcColorType, int rowBytes, long pixels, String label) {
        assert this.mDevice.getContext().isOwnerThread();
        if (this.mDevice.getContext().isDiscarded()) {
            return null;
        } else if (srcColorType != 0 && dstColorType != 0) {
            int minRowBytes = width * ImageInfo.bytesPerPixel(srcColorType);
            int actualRowBytes = rowBytes > 0 ? rowBytes : minRowBytes;
            if (actualRowBytes < minRowBytes) {
                return null;
            } else {
                int actualColorType = (int) this.mDevice.getCaps().getSupportedWriteColorType(dstColorType, format, srcColorType);
                if (actualColorType != srcColorType) {
                    return null;
                } else {
                    GpuTexture texture = this.createTexture(width, height, format, sampleCount, surfaceFlags, label);
                    if (texture == null) {
                        return null;
                    } else if (pixels == 0L) {
                        return texture;
                    } else {
                        boolean result = this.mDevice.writePixels(texture, 0, 0, width, height, dstColorType, actualColorType, actualRowBytes, pixels);
                        assert result;
                        return texture;
                    }
                }
            }
        } else {
            return null;
        }
    }

    @Nullable
    @SharedPtr
    public final GpuTexture findAndRefScratchTexture(IScratchKey key, String label) {
        assert this.mDevice.getContext().isOwnerThread();
        assert !this.mDevice.getContext().isDiscarded();
        assert key != null;
        GpuResource resource = this.mContext.getResourceCache().findAndRefScratchResource(key);
        if (resource != null) {
            this.mDevice.getStats().incNumScratchTexturesReused();
            if (label != null) {
                resource.setLabel(label);
            }
            return (GpuTexture) resource;
        } else {
            return null;
        }
    }

    @Nullable
    @SharedPtr
    public final GpuTexture findAndRefScratchTexture(int width, int height, BackendFormat format, int sampleCount, int surfaceFlags, String label) {
        assert this.mDevice.getContext().isOwnerThread();
        assert !this.mDevice.getContext().isDiscarded();
        assert !format.isCompressed();
        assert this.mDevice.getCaps().validateSurfaceParams(width, height, format, sampleCount, surfaceFlags);
        return this.findAndRefScratchTexture(this.mTextureScratchKey.compute(format, width, height, sampleCount, surfaceFlags), label);
    }

    @Nullable
    @SharedPtr
    public final GpuTexture wrapRenderableBackendTexture(BackendTexture texture, int sampleCount, boolean ownership) {
        return this.mDevice.getContext().isDiscarded() ? null : this.mDevice.wrapRenderableBackendTexture(texture, sampleCount, ownership);
    }

    @Nullable
    @SharedPtr
    public final GpuRenderTarget wrapBackendRenderTarget(BackendRenderTarget backendRenderTarget) {
        return this.mDevice.getContext().isDiscarded() ? null : this.mDevice.wrapBackendRenderTarget(backendRenderTarget);
    }

    @Nullable
    @SharedPtr
    public final GpuBuffer createBuffer(int size, int usage) {
        return this.mDevice.getContext().isDiscarded() ? null : this.mDevice.createBuffer(size, usage);
    }

    public final void assignUniqueKeyToResource(IUniqueKey key, GpuResource resource) {
        assert this.mDevice.getContext().isOwnerThread();
        if (!this.mDevice.getContext().isDiscarded() && resource != null) {
            resource.setUniqueKey(key);
        }
    }
}