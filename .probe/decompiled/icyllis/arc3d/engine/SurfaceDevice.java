package icyllis.arc3d.engine;

import icyllis.arc3d.core.ColorSpace;
import icyllis.arc3d.core.Device;
import icyllis.arc3d.core.ImageInfo;
import icyllis.arc3d.core.Paint;
import icyllis.arc3d.core.Rect2f;
import icyllis.arc3d.core.Rect2ic;
import icyllis.arc3d.core.SharedPtr;

public final class SurfaceDevice extends Device {

    private ClipStack mClipStack;

    private SurfaceDevice(SurfaceDrawContext context, ImageInfo info, boolean clear) {
        super(info);
    }

    @SharedPtr
    private static SurfaceDevice make(SurfaceDrawContext sdc, int alphaType, boolean clear) {
        if (sdc == null) {
            return null;
        } else if (alphaType != 2 && alphaType != 1) {
            return null;
        } else {
            RecordingContext rContext = sdc.getContext();
            if (rContext.isDiscarded()) {
                return null;
            } else {
                int colorType = Engine.colorTypeToPublic(sdc.getColorType());
                if (rContext.isSurfaceCompatible(colorType)) {
                    ImageInfo info = new ImageInfo(sdc.getWidth(), sdc.getHeight(), colorType, alphaType, null);
                    return new SurfaceDevice(sdc, info, clear);
                } else {
                    return null;
                }
            }
        }
    }

    @SharedPtr
    public static SurfaceDevice make(RecordingContext rContext, int colorType, int alphaType, ColorSpace colorSpace, int width, int height, int sampleCount, int surfaceFlags, int origin, boolean clear) {
        if (rContext == null) {
            return null;
        } else {
            SurfaceDrawContext sdc = SurfaceDrawContext.make(rContext, colorType, colorSpace, width, height, sampleCount, surfaceFlags, origin);
            return make(sdc, alphaType, clear);
        }
    }

    @SharedPtr
    public static SurfaceDevice make(RecordingContext rContext, int colorType, ColorSpace colorSpace, SurfaceProxy proxy, int origin, boolean clear) {
        if (rContext == null) {
            return null;
        } else {
            SurfaceDrawContext sdc = SurfaceDrawContext.make(rContext, colorType, colorSpace, proxy, origin);
            return make(sdc, 2, clear);
        }
    }

    @Override
    public boolean clipIsAA() {
        return false;
    }

    @Override
    public boolean clipIsWideOpen() {
        return false;
    }

    @Override
    protected int getClipType() {
        return 0;
    }

    @Override
    protected Rect2ic getClipBounds() {
        return null;
    }

    @Override
    public void drawPaint(Paint paint) {
    }

    @Override
    public void drawRect(Rect2f r, Paint paint) {
    }
}