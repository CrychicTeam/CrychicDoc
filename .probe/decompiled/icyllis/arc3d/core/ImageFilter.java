package icyllis.arc3d.core;

public class ImageFilter {

    public boolean canComputeFastBounds() {
        return true;
    }

    public void computeFastBounds(Rect2f src, Rect2f dst) {
        dst.set(src);
    }
}