package icyllis.arc3d.engine;

import icyllis.arc3d.core.ColorSpace;
import icyllis.arc3d.core.ImageInfo;

public class SurfaceContext implements AutoCloseable {

    protected final RecordingContext mContext;

    protected final SurfaceView mReadView;

    private final ImageInfo mImageInfo;

    public SurfaceContext(RecordingContext context, SurfaceView readView, int colorType, int alphaType, ColorSpace colorSpace) {
        assert !context.isDiscarded();
        this.mContext = context;
        this.mReadView = readView;
        this.mImageInfo = new ImageInfo(this.getWidth(), this.getHeight(), colorType, alphaType, colorSpace);
    }

    public final RecordingContext getContext() {
        return this.mContext;
    }

    public final SurfaceView getReadView() {
        return this.mReadView;
    }

    public final ImageInfo getImageInfo() {
        return this.mImageInfo;
    }

    public final int getColorType() {
        return this.mImageInfo.colorType();
    }

    public final int getAlphaType() {
        return this.mImageInfo.alphaType();
    }

    public final int getWidth() {
        return this.mReadView.getWidth();
    }

    public final int getHeight() {
        return this.mReadView.getHeight();
    }

    public final boolean isMipmapped() {
        return this.mReadView.isMipmapped();
    }

    public final int getOrigin() {
        return this.mReadView.mOrigin;
    }

    public final short getReadSwizzle() {
        return this.mReadView.mSwizzle;
    }

    public final Caps getCaps() {
        return this.mContext.getCaps();
    }

    protected final RenderTaskManager getDrawingManager() {
        return this.mContext.getRenderTaskManager();
    }

    public void close() {
        this.mReadView.close();
    }
}