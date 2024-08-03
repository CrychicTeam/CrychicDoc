package icyllis.arc3d.engine;

import icyllis.arc3d.core.PixelMap;
import icyllis.arc3d.core.PixelRef;
import icyllis.arc3d.core.RefCnt;
import icyllis.arc3d.core.SharedPtr;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Objects;
import javax.annotation.Nullable;

public final class SurfaceProvider {

    private final RecordingContext mContext;

    private final DirectContext mDirect;

    private final Object2ObjectOpenHashMap<Object, TextureProxy> mUniquelyKeyedProxies;

    SurfaceProvider(RecordingContext context) {
        this.mContext = context;
        if (context instanceof DirectContext) {
            this.mDirect = (DirectContext) context;
        } else {
            this.mDirect = null;
        }
        this.mUniquelyKeyedProxies = new Object2ObjectOpenHashMap();
    }

    public boolean assignUniqueKeyToProxy(IUniqueKey key, TextureProxy proxy) {
        assert key != null;
        if (!this.mContext.isDiscarded() && proxy != null) {
            assert this.isDeferredProvider() == ((proxy.mSurfaceFlags & 128) != 0);
            assert this.mDirect == null || this.mDirect.getResourceCache().findAndRefUniqueResource(key) == null;
            assert !this.mUniquelyKeyedProxies.containsKey(key);
            this.mUniquelyKeyedProxies.put(key, proxy);
            return true;
        } else {
            return false;
        }
    }

    public void adoptUniqueKeyFromSurface(TextureProxy proxy, GpuTexture texture) {
    }

    public void processInvalidUniqueKey(Object key, TextureProxy proxy, boolean invalidateResource) {
    }

    @Nullable
    @SharedPtr
    public TextureProxy createTexture(BackendFormat format, int width, int height, int surfaceFlags) {
        assert this.mContext.isOwnerThread();
        if (this.mContext.isDiscarded()) {
            return null;
        } else if (format.isCompressed()) {
            return null;
        } else if (!this.mContext.getCaps().validateSurfaceParams(width, height, format, 1, surfaceFlags)) {
            return null;
        } else {
            if (this.isDeferredProvider()) {
                surfaceFlags |= 128;
            } else {
                assert (surfaceFlags & 128) == 0;
            }
            return new TextureProxy(format, width, height, surfaceFlags);
        }
    }

    @Nullable
    @SharedPtr
    public TextureProxy createTextureFromPixels(PixelMap pixelMap, PixelRef pixelRef, int dstColorType, int surfaceFlags) {
        this.mContext.checkOwnerThread();
        assert (surfaceFlags & 2) == 0 || (surfaceFlags & 4) == 0;
        if (this.mContext.isDiscarded()) {
            return null;
        } else if (!pixelMap.getInfo().isValid()) {
            return null;
        } else if (!pixelRef.isImmutable()) {
            return null;
        } else {
            BackendFormat format = this.mContext.getCaps().getDefaultBackendFormat(dstColorType, false);
            if (format == null) {
                return null;
            } else {
                int srcColorType = pixelMap.getColorType();
                int width = pixelMap.getWidth();
                int height = pixelMap.getHeight();
                TextureProxy texture = this.createLazyTexture(format, width, height, surfaceFlags, new SurfaceProvider.PixelsCallback(pixelRef, srcColorType, dstColorType));
                if (texture == null) {
                    return null;
                } else {
                    if (!this.isDeferredProvider()) {
                        texture.doLazyInstantiation(this.mDirect.getResourceProvider());
                    }
                    return texture;
                }
            }
        }
    }

    @Nullable
    @SharedPtr
    public RenderTextureProxy createRenderTexture(BackendFormat format, int width, int height, int sampleCount, int surfaceFlags) {
        assert this.mContext.isOwnerThread();
        if (this.mContext.isDiscarded()) {
            return null;
        } else if (format.isCompressed()) {
            return null;
        } else if (!this.mContext.getCaps().validateSurfaceParams(width, height, format, sampleCount, surfaceFlags)) {
            return null;
        } else {
            if (this.isDeferredProvider()) {
                surfaceFlags |= 128;
            } else {
                assert (surfaceFlags & 128) == 0;
            }
            return new RenderTextureProxy(format, width, height, sampleCount, surfaceFlags);
        }
    }

    @Nullable
    @SharedPtr
    public RenderTextureProxy wrapRenderableBackendTexture(BackendTexture texture, int sampleCount, boolean ownership, boolean cacheable, Runnable releaseCallback) {
        if (this.mContext.isDiscarded()) {
            return null;
        } else if (this.mDirect == null) {
            return null;
        } else {
            sampleCount = this.mContext.getCaps().getRenderTargetSampleCount(sampleCount, texture.getBackendFormat());
            assert sampleCount > 0;
            return null;
        }
    }

    @Nullable
    @SharedPtr
    public RenderTargetProxy wrapBackendRenderTarget(BackendRenderTarget backendRenderTarget, Runnable rcReleaseCB) {
        if (this.mContext.isDiscarded()) {
            return null;
        } else if (this.mDirect == null) {
            return null;
        } else {
            GpuRenderTarget fsr = this.mDirect.getResourceProvider().wrapBackendRenderTarget(backendRenderTarget);
            return fsr == null ? null : new RenderTargetProxy(fsr, 0);
        }
    }

    @Nullable
    @SharedPtr
    public TextureProxy createLazyTexture(BackendFormat format, int width, int height, int surfaceFlags, SurfaceProxy.LazyInstantiateCallback callback) {
        this.mContext.checkOwnerThread();
        if (this.mContext.isDiscarded()) {
            return null;
        } else {
            assert width <= 0 && height <= 0 || width > 0 && height > 0;
            Objects.requireNonNull(callback);
            if (format == null || format.getBackend() != this.mContext.getBackend()) {
                return null;
            } else if (width <= this.mContext.getCaps().maxTextureSize() && height <= this.mContext.getCaps().maxTextureSize()) {
                if (this.isDeferredProvider()) {
                    surfaceFlags |= 128;
                } else {
                    assert (surfaceFlags & 128) == 0;
                }
                return new TextureProxy(format, width, height, surfaceFlags, callback);
            } else {
                return null;
            }
        }
    }

    public boolean isDeferredProvider() {
        return this.mDirect == null;
    }

    private static final class PixelsCallback implements SurfaceProxy.LazyInstantiateCallback {

        private PixelRef mPixelRef;

        private final int mSrcColorType;

        private final int mDstColorType;

        public PixelsCallback(PixelRef pixelRef, int srcColorType, int dstColorType) {
            this.mPixelRef = RefCnt.create(pixelRef);
            this.mSrcColorType = srcColorType;
            this.mDstColorType = dstColorType;
        }

        @Override
        public SurfaceProxy.LazyCallbackResult onLazyInstantiate(ResourceProvider provider, BackendFormat format, int width, int height, int sampleCount, int surfaceFlags, String label) {
            assert this.mPixelRef.getBase() == null;
            GpuTexture texture = provider.createTexture(width, height, format, sampleCount, surfaceFlags, this.mDstColorType, this.mSrcColorType, this.mPixelRef.getRowStride(), this.mPixelRef.getAddress(), label);
            this.close();
            return new SurfaceProxy.LazyCallbackResult(texture);
        }

        @Override
        public void close() {
            this.mPixelRef = RefCnt.move(this.mPixelRef);
        }
    }
}