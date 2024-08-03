package icyllis.arc3d.engine.tessellate;

import icyllis.arc3d.core.MathUtil;

public class WangsFormula {

    private static final float N2_P2_F = 0.0625F;

    private static final float N3_P2_F = 0.5625F;

    public static float quadratic_p4(float precision, float x0, float y0, float x1, float y1, float x2, float y2) {
        float Mx = x2 - 2.0F * x1 + x0;
        float My = y2 - 2.0F * y1 + y0;
        return (Mx * Mx + My * My) * 0.0625F * precision * precision;
    }

    public static float quadratic(float precision, float x0, float y0, float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.sqrt((double) quadratic_p4(precision, x0, y0, x1, y1, x2, y2)));
    }

    public static int quadratic_log2(float precision, float x0, float y0, float x1, float y1, float x2, float y2) {
        return MathUtil.ceilLog16(quadratic_p4(precision, x0, y0, x1, y1, x2, y2));
    }

    public static float cubic_p4(float precision, float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3) {
        float Mx0 = x2 - 2.0F * x1 + x0;
        float My0 = y2 - 2.0F * y1 + y0;
        float Mx1 = x3 - 2.0F * x2 + x1;
        float My1 = y3 - 2.0F * y2 + y1;
        return Math.max(Mx0 * Mx0 + My0 * My0, Mx1 * Mx1 + My1 * My1) * 0.5625F * precision * precision;
    }

    public static float cubic(float precision, float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3) {
        return (float) Math.sqrt(Math.sqrt((double) cubic_p4(precision, x0, y0, x1, y1, x2, y2, x3, y3)));
    }

    public static int cubic_log2(float precision, float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3) {
        return MathUtil.ceilLog16(cubic_p4(precision, x0, y0, x1, y1, x2, y2, x3, y3));
    }

    public static float worst_cubic_p4(float precision, float devWidth, float devHeight) {
        return 4.0F * 0.5625F * precision * precision * (devWidth * devWidth + devHeight * devHeight);
    }

    public static float worst_cubic(float precision, float devWidth, float devHeight) {
        return (float) Math.sqrt(Math.sqrt((double) worst_cubic_p4(precision, devWidth, devHeight)));
    }

    public static int worst_cubic_log2(float precision, float devWidth, float devHeight) {
        return MathUtil.ceilLog16(worst_cubic_p4(precision, devWidth, devHeight));
    }

    protected WangsFormula() {
        throw new UnsupportedOperationException();
    }
}