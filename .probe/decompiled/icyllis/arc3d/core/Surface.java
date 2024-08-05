package icyllis.arc3d.core;

import icyllis.arc3d.engine.BackendFormat;
import icyllis.arc3d.engine.BackendRenderTarget;
import icyllis.arc3d.engine.BackendTexture;
import icyllis.arc3d.engine.Caps;
import icyllis.arc3d.engine.RecordingContext;
import icyllis.arc3d.engine.RenderTargetProxy;
import icyllis.arc3d.engine.SurfaceDevice;
import icyllis.arc3d.engine.SurfaceProvider;
import javax.annotation.Nullable;

public class Surface extends RefCnt {

    @SharedPtr
    private Device mDevice;

    private Canvas mCachedCanvas;

    public Surface(@SharedPtr Device device) {
        this.mDevice = device;
    }

    @Nullable
    public static Surface makeFromBackendTexture(RecordingContext context, BackendTexture backendTexture, int origin, int sampleCount, int colorType, Runnable releaseCallback) {
        if (context != null && sampleCount >= 1 && colorType != 0) {
            if (!validateBackendTexture(context.getCaps(), backendTexture, sampleCount, colorType, true)) {
                if (releaseCallback != null) {
                    releaseCallback.run();
                }
                return null;
            } else {
                return null;
            }
        } else {
            if (releaseCallback != null) {
                releaseCallback.run();
            }
            return null;
        }
    }

    @Nullable
    public static Surface makeRenderTarget(RecordingContext context, ImageInfo imageInfo, int origin, int sampleCount, boolean mipmapped, boolean budgeted) {
        return context != null && imageInfo != null && sampleCount >= 1 ? null : null;
    }

    @Nullable
    public static Surface wrapBackendRenderTarget(RecordingContext rContext, BackendRenderTarget backendRenderTarget, int origin, int colorType, ColorSpace colorSpace) {
        if (colorType == 0) {
            return null;
        } else {
            SurfaceProvider provider = rContext.getSurfaceProvider();
            RenderTargetProxy rtProxy = provider.wrapBackendRenderTarget(backendRenderTarget, null);
            if (rtProxy == null) {
                return null;
            } else {
                SurfaceDevice dev = SurfaceDevice.make(rContext, colorType, colorSpace, rtProxy, origin, false);
                return dev == null ? null : new Surface(dev);
            }
        }
    }

    private static boolean validateBackendTexture(Caps caps, BackendTexture backendTexture, int sampleCount, int colorType, boolean texturable) {
        if (backendTexture == null) {
            return false;
        } else {
            BackendFormat backendFormat = backendTexture.getBackendFormat();
            if (!caps.isFormatCompatible(colorType, backendFormat)) {
                return false;
            } else {
                return caps.isFormatRenderable(colorType, backendFormat, sampleCount) ? false : !texturable || caps.isFormatTexturable(backendFormat);
            }
        }
    }

    @Override
    protected void deallocate() {
        this.mDevice.unref();
        this.mDevice = null;
        if (this.mCachedCanvas != null) {
            this.mCachedCanvas.mSurface = null;
            this.mCachedCanvas.close();
            this.mCachedCanvas = null;
        }
    }

    public int getWidth() {
        return this.mDevice.width();
    }

    public int getHeight() {
        return this.mDevice.height();
    }

    public ImageInfo getImageInfo() {
        return this.mDevice.imageInfo();
    }

    public RecordingContext getRecordingContext() {
        return this.mDevice.getRecordingContext();
    }

    public Canvas getCanvas() {
        if (this.mCachedCanvas == null) {
            this.mCachedCanvas = new Canvas(this.mDevice);
            this.mCachedCanvas.mSurface = this;
        }
        return this.mCachedCanvas;
    }
}