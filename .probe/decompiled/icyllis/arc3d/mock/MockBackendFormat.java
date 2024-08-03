package icyllis.arc3d.mock;

import icyllis.arc3d.core.ImageInfo;
import icyllis.arc3d.engine.BackendFormat;
import javax.annotation.Nonnull;

public class MockBackendFormat extends BackendFormat {

    private final int mColorType;

    private final int mCompressionType;

    private final boolean mIsStencilFormat;

    public MockBackendFormat(int colorType, int compressionType, boolean isStencilFormat) {
        this.mColorType = colorType;
        this.mCompressionType = compressionType;
        this.mIsStencilFormat = isStencilFormat;
    }

    @Nonnull
    public static MockBackendFormat make(int colorType, int compressionType) {
        return make(colorType, compressionType, false);
    }

    @Nonnull
    public static MockBackendFormat make(int colorType, int compressionType, boolean isStencilFormat) {
        return new MockBackendFormat(colorType, compressionType, isStencilFormat);
    }

    @Override
    public int getBackend() {
        return 2;
    }

    @Override
    public boolean isExternal() {
        return false;
    }

    @Override
    public int getChannelFlags() {
        return ImageInfo.colorTypeChannelFlags(this.mColorType);
    }

    @Nonnull
    @Override
    public BackendFormat makeInternal() {
        return this;
    }

    @Override
    public boolean isSRGB() {
        return this.mCompressionType == 0 && this.mColorType == 8;
    }

    @Override
    public int getCompressionType() {
        return this.mCompressionType;
    }

    @Override
    public int getBytesPerBlock() {
        if (this.mCompressionType != 0) {
            return 8;
        } else {
            return this.mIsStencilFormat ? 4 : ImageInfo.bytesPerPixel(this.mColorType);
        }
    }

    @Override
    public int getStencilBits() {
        return this.mIsStencilFormat ? 8 : 0;
    }

    @Override
    public int getFormatKey() {
        return this.mColorType;
    }
}