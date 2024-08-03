package icyllis.arc3d.core;

public class RasterDevice extends Device {

    public RasterDevice(ImageInfo info) {
        super(info);
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