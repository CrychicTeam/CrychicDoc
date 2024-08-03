package icyllis.arc3d.engine;

import icyllis.arc3d.core.SharedPtr;

public class SurfaceView implements AutoCloseable {

    @SharedPtr
    SurfaceProxy mProxy;

    int mOrigin;

    short mSwizzle;

    public SurfaceView(@SharedPtr SurfaceProxy proxy) {
        this.mProxy = proxy;
        this.mOrigin = 0;
        this.mSwizzle = 12816;
    }

    public SurfaceView(@SharedPtr SurfaceProxy proxy, int origin, short swizzle) {
        this.mProxy = proxy;
        this.mOrigin = origin;
        this.mSwizzle = swizzle;
    }

    public int getWidth() {
        return this.mProxy.getWidth();
    }

    public int getHeight() {
        return this.mProxy.getHeight();
    }

    public boolean isMipmapped() {
        TextureProxy proxy = this.mProxy.asTexture();
        return proxy != null && proxy.isMipmapped();
    }

    public SurfaceProxy getSurface() {
        return this.mProxy;
    }

    @SharedPtr
    public SurfaceProxy refSurface() {
        this.mProxy.ref();
        return this.mProxy;
    }

    @SharedPtr
    public SurfaceProxy detachSurface() {
        SurfaceProxy surfaceProxy = this.mProxy;
        this.mProxy = null;
        return surfaceProxy;
    }

    public int getOrigin() {
        return this.mOrigin;
    }

    public short getSwizzle() {
        return this.mSwizzle;
    }

    public void concat(short swizzle) {
        this.mSwizzle = Swizzle.concat(this.mSwizzle, swizzle);
    }

    public void reset() {
        if (this.mProxy != null) {
            this.mProxy.unref();
        }
        this.mProxy = null;
        this.mOrigin = 0;
        this.mSwizzle = 12816;
    }

    public void close() {
        if (this.mProxy != null) {
            this.mProxy.unref();
        }
        this.mProxy = null;
    }
}