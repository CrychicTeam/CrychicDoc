package icyllis.arc3d.engine;

import icyllis.arc3d.core.Point;
import icyllis.arc3d.engine.tessellate.WangsFormula;

public class PathUtils {

    public static final float DEFAULT_TOLERANCE = 0.25F;

    public static final int MAX_CHOPS_PER_CURVE = 10;

    public static final int MAX_POINTS_PER_CURVE = 1024;

    private static final float MIN_CURVE_TOLERANCE = 1.0E-4F;

    public static int countQuadraticPoints(float x0, float y0, float x1, float y1, float x2, float y2, float tol) {
        assert tol >= 1.0E-4F;
        int chops = WangsFormula.quadratic_log2(1.0F / tol, x0, y0, x1, y1, x2, y2);
        return 1 << Math.min(chops, 10);
    }

    public static int generateQuadraticPoints(float p0x, float p0y, float p1x, float p1y, float p2x, float p2y, float tolSq, float[] dst, int off, int rem) {
        if (rem >= 4 && !(Point.distanceToLineSegmentBetweenSq(p1x, p1y, p0x, p0y, p2x, p2y) < tolSq)) {
            float q0x = (p0x + p1x) * 0.5F;
            float q0y = (p0y + p1y) * 0.5F;
            float q1x = (p1x + p2x) * 0.5F;
            float q1y = (p1y + p2y) * 0.5F;
            float r0x = (q0x + q1x) * 0.5F;
            float r0y = (q0y + q1y) * 0.5F;
            rem >>= 1;
            int ret = off + generateQuadraticPoints(p0x, p0y, q0x, q0y, r0x, r0y, tolSq, dst, off, rem);
            ret += generateQuadraticPoints(r0x, r0y, q1x, q1y, p2x, p2y, tolSq, dst, ret, rem);
            return ret - off;
        } else {
            dst[off] = p2x;
            dst[off + 1] = p2y;
            return 2;
        }
    }

    public static int countCubicPoints(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3, float tol) {
        assert tol >= 1.0E-4F;
        int chops = WangsFormula.cubic_log2(1.0F / tol, x0, y0, x1, y1, x2, y2, x3, y3);
        return 1 << Math.min(chops, 10);
    }

    public static int generateCubicPoints(float p0x, float p0y, float p1x, float p1y, float p2x, float p2y, float p3x, float p3y, float tolSq, float[] dst, int off, int rem) {
        if (rem >= 4 && (!(Point.distanceToLineSegmentBetweenSq(p1x, p1y, p0x, p0y, p3x, p3y) < tolSq) || !(Point.distanceToLineSegmentBetweenSq(p2x, p2y, p0x, p0y, p3x, p3y) < tolSq))) {
            float q0x = (p0x + p1x) * 0.5F;
            float q0y = (p0y + p1y) * 0.5F;
            float q1x = (p1x + p2x) * 0.5F;
            float q1y = (p1y + p2y) * 0.5F;
            float q2x = (p2x + p3x) * 0.5F;
            float q2y = (p2y + p3y) * 0.5F;
            float r0x = (q0x + q1x) * 0.5F;
            float r0y = (q0y + q1y) * 0.5F;
            float r1x = (q1x + q2x) * 0.5F;
            float r1y = (q1y + q2y) * 0.5F;
            float s0x = (r0x + r1x) * 0.5F;
            float s0y = (r0y + r1y) * 0.5F;
            rem >>= 1;
            int ret = off + generateCubicPoints(p0x, p0y, q0x, q0y, r0x, r0y, s0x, s0y, tolSq, dst, off, rem);
            ret += generateCubicPoints(s0x, s0y, r1x, r1y, q2x, q2y, p3x, p3y, tolSq, dst, ret, rem);
            return ret - off;
        } else {
            dst[off] = p3x;
            dst[off + 1] = p3y;
            return 2;
        }
    }

    protected PathUtils() {
        throw new UnsupportedOperationException();
    }
}