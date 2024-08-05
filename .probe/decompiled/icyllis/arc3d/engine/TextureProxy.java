package icyllis.arc3d.engine;

import icyllis.arc3d.core.Rect2ic;
import icyllis.arc3d.core.SharedPtr;
import java.util.Objects;
import javax.annotation.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public class TextureProxy extends SurfaceProxy implements IScratchKey {

    boolean mIsPromiseProxy = false;

    @SharedPtr
    GpuTexture mGpuTexture;

    boolean mMipmapsDirty = true;

    boolean mSyncTargetKey = true;

    IUniqueKey mUniqueKey;

    SurfaceProvider mSurfaceProvider;

    public TextureProxy(BackendFormat format, int width, int height, int surfaceFlags) {
        super(format, width, height, surfaceFlags);
        assert width > 0 && height > 0;
    }

    public TextureProxy(BackendFormat format, int width, int height, int surfaceFlags, SurfaceProxy.LazyInstantiateCallback callback) {
        super(format, width, height, surfaceFlags);
        this.mLazyInstantiateCallback = (SurfaceProxy.LazyInstantiateCallback) Objects.requireNonNull(callback);
        assert width < 0 && height < 0 && (surfaceFlags & 2) != 0 || width > 0 && height > 0;
    }

    public TextureProxy(@SharedPtr GpuTexture texture, int surfaceFlags) {
        super(texture, surfaceFlags);
        this.mMipmapsDirty = texture.isMipmapped() && texture.isMipmapsDirty();
        assert (this.mSurfaceFlags & 2) == 0;
        assert this.mFormat.isExternal() == texture.isExternal();
        assert texture.isMipmapped() == ((this.mSurfaceFlags & 4) != 0);
        assert texture.getBudgetType() == 0 == ((this.mSurfaceFlags & 1) != 0);
        assert !texture.isExternal() || (this.mSurfaceFlags & 32) != 0;
        assert texture.getBudgetType() == 0 == this.isBudgeted();
        assert !texture.isExternal() || this.isReadOnly();
        this.mGpuTexture = texture;
        if (texture.getUniqueKey() != null) {
            assert texture.getContext() != null;
            this.mSurfaceProvider = texture.getContext().getSurfaceProvider();
            this.mSurfaceProvider.adoptUniqueKeyFromSurface(this, texture);
        }
    }

    @Override
    protected void deallocate() {
        this.mGpuTexture = GpuResource.move(this.mGpuTexture);
        if (this.mLazyInstantiateCallback != null) {
            this.mLazyInstantiateCallback.close();
            this.mLazyInstantiateCallback = null;
        }
        if (this.mUniqueKey != null && this.mSurfaceProvider != null) {
            this.mSurfaceProvider.processInvalidUniqueKey(this.mUniqueKey, this, false);
        } else {
            assert this.mSurfaceProvider == null;
        }
    }

    @Override
    public boolean isLazy() {
        return this.mGpuTexture == null && this.mLazyInstantiateCallback != null;
    }

    @Override
    public int getBackingWidth() {
        assert !this.isLazyMost();
        if (this.mGpuTexture != null) {
            return this.mGpuTexture.getWidth();
        } else {
            return (this.mSurfaceFlags & 2) != 0 ? ISurface.getApproxSize(this.mWidth) : this.mWidth;
        }
    }

    @Override
    public int getBackingHeight() {
        assert !this.isLazyMost();
        if (this.mGpuTexture != null) {
            return this.mGpuTexture.getHeight();
        } else {
            return (this.mSurfaceFlags & 2) != 0 ? ISurface.getApproxSize(this.mHeight) : this.mHeight;
        }
    }

    @Override
    public int getSampleCount() {
        return 1;
    }

    @Override
    public Object getBackingUniqueID() {
        return this.mGpuTexture != null ? this.mGpuTexture : this.mUniqueID;
    }

    @Override
    public boolean isInstantiated() {
        return this.mGpuTexture != null;
    }

    @Override
    public boolean instantiate(ResourceProvider resourceProvider) {
        if (this.isLazy()) {
            return false;
        } else if (this.mGpuTexture == null) {
            assert (this.mSurfaceFlags & 4) == 0 || (this.mSurfaceFlags & 2) == 0;
            GpuTexture texture = resourceProvider.createTexture(this.mWidth, this.mHeight, this.mFormat, this.getSampleCount(), this.mSurfaceFlags, "");
            if (texture == null) {
                return false;
            } else {
                if (this.mUniqueKey != null) {
                    resourceProvider.assignUniqueKeyToResource(this.mUniqueKey, texture);
                }
                assert this.mGpuTexture == null;
                assert texture.getBackendFormat().equals(this.mFormat);
                this.mGpuTexture = texture;
                return true;
            }
        } else {
            assert this.mUniqueKey == null || this.mGpuTexture.mUniqueKey != null && this.mGpuTexture.mUniqueKey.equals(this.mUniqueKey);
            return true;
        }
    }

    @Override
    public void clear() {
        assert this.mGpuTexture != null;
        this.mGpuTexture.unref();
        this.mGpuTexture = null;
    }

    @Override
    public final boolean shouldSkipAllocator() {
        if ((this.mSurfaceFlags & 64) != 0) {
            return true;
        } else {
            return this.mGpuTexture == null ? false : this.mGpuTexture.getScratchKey() == null;
        }
    }

    @Nullable
    public final IUniqueKey getUniqueKey() {
        return this.mUniqueKey;
    }

    public void setResolveRect(int left, int top, int right, int bottom) {
        throw new UnsupportedOperationException();
    }

    public boolean needsResolve() {
        throw new UnsupportedOperationException();
    }

    public Rect2ic getResolveRect() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isBackingWrapped() {
        return this.mGpuTexture != null && this.mGpuTexture.isWrapped();
    }

    @Nullable
    @Override
    public IGpuSurface getGpuSurface() {
        return this.mGpuTexture;
    }

    @Nullable
    @Override
    public GpuTexture getGpuTexture() {
        return this.mGpuTexture;
    }

    @Override
    public long getMemorySize() {
        return GpuTexture.computeSize(this.mFormat, this.mWidth, this.mHeight, this.getSampleCount(), (this.mSurfaceFlags & 4) != 0, (this.mSurfaceFlags & 2) != 0);
    }

    public final boolean isPromiseProxy() {
        return this.mIsPromiseProxy;
    }

    public boolean isMipmapped() {
        return this.mGpuTexture != null ? this.mGpuTexture.isMipmapped() : (this.mSurfaceFlags & 4) != 0;
    }

    public final boolean isMipmapsDirty() {
        return this.mMipmapsDirty && this.isUserMipmapped();
    }

    public final void setMipmapsDirty(boolean dirty) {
        assert this.isUserMipmapped();
        this.mMipmapsDirty = dirty;
    }

    public final boolean isUserMipmapped() {
        return (this.mSurfaceFlags & 4) != 0;
    }

    public final boolean hasRestrictedSampling() {
        return this.mFormat.isExternal();
    }

    @Override
    public int hashCode() {
        int result = this.getBackingWidth();
        result = 31 * result + this.getBackingHeight();
        result = 31 * result + this.mFormat.getFormatKey();
        return 31 * result + (this.mSurfaceFlags & 24 | (this.isMipmapped() ? 4 : 0));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof GpuTexture.ScratchKey key) {
            return key.mWidth == this.getBackingWidth() && key.mHeight == this.getBackingHeight() && key.mFormat == this.mFormat.getFormatKey() && key.mFlags == (this.mSurfaceFlags & 24 | (this.isMipmapped() ? 4 : 0));
        } else {
            return !(o instanceof TextureProxy proxy) ? false : proxy.getBackingWidth() == this.getBackingWidth() && proxy.getBackingHeight() == this.getBackingHeight() && proxy.mFormat.getFormatKey() == this.mFormat.getFormatKey() && proxy.isMipmapped() == this.isMipmapped() && (proxy.mSurfaceFlags & 24) == (this.mSurfaceFlags & 24);
        }
    }

    @Override
    public TextureProxy asTexture() {
        return this;
    }

    @Internal
    public final void makeUserExact(boolean allocatedCaseOnly) {
        assert !this.isLazyMost();
        if ((this.mSurfaceFlags & 2) != 0) {
            GpuTexture texture = this.getGpuTexture();
            if (texture != null) {
                this.mWidth = texture.getWidth();
                this.mHeight = texture.getHeight();
            } else if (!allocatedCaseOnly) {
                this.mSurfaceFlags &= -3;
            }
        }
    }

    @Internal
    public final void setLazyDimension(int width, int height) {
        assert this.isLazyMost();
        assert width > 0 && height > 0;
        this.mWidth = width;
        this.mHeight = height;
    }

    @SharedPtr
    GpuTexture createGpuTexture(ResourceProvider resourceProvider) {
        assert (this.mSurfaceFlags & 4) == 0 || (this.mSurfaceFlags & 2) == 0;
        assert !this.isLazy();
        assert this.mGpuTexture == null;
        return resourceProvider.createTexture(this.mWidth, this.mHeight, this.mFormat, this.getSampleCount(), this.mSurfaceFlags, "");
    }

    @Override
    public final boolean doLazyInstantiation(ResourceProvider resourceProvider) {
        assert this.isLazy();
        GpuTexture textureResource = null;
        if (this.mUniqueKey != null) {
            textureResource = resourceProvider.findByUniqueKey(this.mUniqueKey);
        }
        boolean syncTargetKey = true;
        boolean releaseCallback = false;
        if (textureResource == null) {
            int width = this.isLazyMost() ? -1 : this.getWidth();
            int height = this.isLazyMost() ? -1 : this.getHeight();
            SurfaceProxy.LazyCallbackResult result = this.mLazyInstantiateCallback.onLazyInstantiate(resourceProvider, this.mFormat, width, height, this.getSampleCount(), this.mSurfaceFlags, "");
            if (result != null) {
                textureResource = (GpuTexture) result.mSurface;
                syncTargetKey = result.mSyncTargetKey;
                releaseCallback = result.mReleaseCallback;
            }
        }
        if (textureResource == null) {
            this.mWidth = this.mHeight = 0;
            return false;
        } else {
            if (this.isLazyMost()) {
                this.mWidth = textureResource.getWidth();
                this.mHeight = textureResource.getHeight();
            }
            assert this.getWidth() <= textureResource.getWidth();
            assert this.getHeight() <= textureResource.getHeight();
            this.mSyncTargetKey = syncTargetKey;
            if (syncTargetKey) {
                if (this.mUniqueKey != null) {
                    if (textureResource.getUniqueKey() == null) {
                        resourceProvider.assignUniqueKeyToResource(this.mUniqueKey, textureResource);
                    } else {
                        assert textureResource.getUniqueKey().equals(this.mUniqueKey);
                    }
                } else {
                    assert textureResource.getUniqueKey() == null;
                }
            }
            assert this.mGpuTexture == null;
            this.mGpuTexture = textureResource;
            if (releaseCallback) {
                this.mLazyInstantiateCallback.close();
                this.mLazyInstantiateCallback = null;
            }
            return true;
        }
    }

    @Internal
    public void setIsPromiseProxy() {
        this.mIsPromiseProxy = true;
    }
}