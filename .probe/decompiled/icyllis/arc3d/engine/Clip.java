package icyllis.arc3d.engine;

import icyllis.arc3d.core.Rect2f;
import icyllis.arc3d.core.Rect2fc;
import icyllis.arc3d.core.Rect2i;

public abstract class Clip {

    public static final int CLIPPED = 0;

    public static final int NOT_CLIPPED = 1;

    public static final int CLIPPED_OUT = 2;

    public static final float kBoundsTolerance = 0.001F;

    public static final float kHalfPixelRoundingTolerance = 0.05F;

    public abstract int apply(SurfaceDrawContext var1, boolean var2, ClipResult var3, Rect2f var4);

    public abstract void getConservativeBounds(Rect2i var1);

    public static void getPixelBounds(Rect2fc bounds, boolean aa, boolean exterior, Rect2i out) {
        if (bounds.isEmpty()) {
            out.setEmpty();
        } else {
            if (exterior) {
                out.set(roundLow(aa, bounds.left()), roundLow(aa, bounds.top()), roundHigh(aa, bounds.right()), roundHigh(aa, bounds.bottom()));
            } else {
                out.set(roundHigh(aa, bounds.left()), roundHigh(aa, bounds.top()), roundLow(aa, bounds.right()), roundLow(aa, bounds.bottom()));
            }
        }
    }

    private static int roundLow(boolean aa, float v) {
        v += 0.001F;
        return aa ? (int) Math.floor((double) v) : Math.round(v - 0.05F);
    }

    private static int roundHigh(boolean aa, float v) {
        v -= 0.001F;
        return aa ? (int) Math.ceil((double) v) : Math.round(v + 0.05F);
    }
}