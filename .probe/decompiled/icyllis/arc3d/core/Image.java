package icyllis.arc3d.core;

import icyllis.arc3d.engine.RecordingContext;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public abstract class Image extends RefCnt {

    protected final ImageInfo mInfo;

    protected Image(@Nonnull ImageInfo info) {
        if (info.isEmpty()) {
            throw new IllegalArgumentException();
        } else {
            this.mInfo = info;
        }
    }

    @Nonnull
    public ImageInfo getInfo() {
        return this.mInfo;
    }

    public int getWidth() {
        return this.mInfo.width();
    }

    public int getHeight() {
        return this.mInfo.height();
    }

    public int getColorType() {
        return this.mInfo.colorType();
    }

    public int getAlphaType() {
        return this.mInfo.alphaType();
    }

    @Nullable
    public ColorSpace getColorSpace() {
        return this.mInfo.colorSpace();
    }

    @Nullable
    @Internal
    public RecordingContext getContext() {
        return null;
    }

    public abstract boolean isValid(@Nullable RecordingContext var1);

    @Internal
    public boolean isRasterBacked() {
        return false;
    }

    @Internal
    public boolean isTextureBacked() {
        return false;
    }

    public long getTextureMemorySize() {
        return 0L;
    }
}