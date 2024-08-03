package icyllis.arc3d.engine;

import javax.annotation.Nonnull;

public abstract class Attachment extends GpuResource {

    protected final int mWidth;

    protected final int mHeight;

    protected final int mSampleCount;

    protected Attachment(GpuDevice device, int width, int height, int sampleCount) {
        super(device);
        assert width > 0 && height > 0 && sampleCount > 0;
        this.mWidth = width;
        this.mHeight = height;
        this.mSampleCount = sampleCount;
    }

    public final int getWidth() {
        return this.mWidth;
    }

    public final int getHeight() {
        return this.mHeight;
    }

    public final int getSampleCount() {
        return this.mSampleCount;
    }

    @Nonnull
    public abstract BackendFormat getBackendFormat();

    public final boolean isFormatCompressed() {
        return this.getBackendFormat().isCompressed();
    }
}