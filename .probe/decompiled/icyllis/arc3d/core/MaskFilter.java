package icyllis.arc3d.core;

public class MaskFilter {

    public void computeFastBounds(Rect2f src, Rect2f dst) {
        src.roundOut(dst);
    }
}