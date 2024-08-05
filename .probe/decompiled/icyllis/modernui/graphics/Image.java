package icyllis.modernui.graphics;

import icyllis.arc3d.core.ImageInfo;
import icyllis.arc3d.core.SharedPtr;
import icyllis.arc3d.engine.Caps;
import icyllis.arc3d.engine.RecordingContext;
import icyllis.arc3d.engine.SurfaceView;
import icyllis.arc3d.engine.TextureProxy;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.core.Core;
import java.lang.ref.Cleaner.Cleanable;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.ApiStatus.Internal;

public class Image implements AutoCloseable {

    private final ImageInfo mInfo;

    private final RecordingContext mContext;

    private Image.ViewReference mView;

    private Image(ImageInfo info, RecordingContext context, @SharedPtr TextureProxy proxy, short swizzle) {
        this.mInfo = info;
        this.mContext = context;
        this.mView = new Image.ViewReference(this, proxy, swizzle);
    }

    @Nullable
    public static Image createTextureFromBitmap(Bitmap bitmap) {
        return bitmap != null && !bitmap.isClosed() ? createTextureFromBitmap((RecordingContext) (Core.isOnUiThread() ? Core.requireUiRecordingContext() : Core.requireDirectContext()), bitmap) : null;
    }

    @Nullable
    public static Image createTextureFromBitmap(@NonNull RecordingContext rContext, @NonNull Bitmap bitmap) {
        Caps caps = rContext.getCaps();
        int ct = bitmap.getFormat() == Bitmap.Format.RGB_888 ? 5 : bitmap.getColorType();
        if (caps.getDefaultBackendFormat(ct, false) == null) {
            return null;
        } else {
            int flags = 1;
            if (bitmap.getWidth() > 1 || bitmap.getHeight() > 1) {
                flags |= 4;
            }
            TextureProxy proxy = rContext.getSurfaceProvider().createTextureFromPixels(bitmap.getPixelMap(), bitmap.getPixelRef(), ct, flags);
            if (proxy == null) {
                return null;
            } else {
                short swizzle = caps.getReadSwizzle(proxy.getBackendFormat(), ct);
                return new Image(bitmap.getInfo(), rContext, proxy, swizzle);
            }
        }
    }

    @Nullable
    public static Image create(@NonNull String namespace, @NonNull String entry) {
        return ImageStore.getInstance().getOrCreate(namespace, "textures/" + entry);
    }

    public ImageInfo getInfo() {
        return this.mInfo;
    }

    public int getWidth() {
        return this.mInfo.width();
    }

    public int getHeight() {
        return this.mInfo.height();
    }

    @Internal
    public SurfaceView asTextureView() {
        return this.mView;
    }

    @Experimental
    public void close() {
        if (this.mView != null) {
            this.mView.mCleanup.clean();
            this.mView = null;
        }
    }

    public boolean isClosed() {
        return this.mView == null;
    }

    private static class ViewReference extends SurfaceView implements Runnable {

        final Cleanable mCleanup;

        ViewReference(Image owner, @SharedPtr TextureProxy proxy, short swizzle) {
            super(proxy, 0, swizzle);
            this.mCleanup = Core.registerCleanup(owner, this);
        }

        public void run() {
            Core.executeOnRenderThread(() -> super.close());
        }

        @Override
        public void close() {
            throw new UnsupportedOperationException();
        }
    }
}