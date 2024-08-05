package icyllis.arc3d.core;

import org.jetbrains.annotations.Contract;

public class Point {

    @Contract(pure = true)
    public static boolean isDegenerate(float dx, float dy) {
        return !Float.isFinite(dx) || !Float.isFinite(dy) || dx == 0.0F && dy == 0.0F;
    }

    @Contract(pure = true)
    public static boolean equals(float x1, float y1, float x2, float y2) {
        return isDegenerate(x1 - x2, y1 - y2);
    }

    @Contract(pure = true)
    public static boolean isApproxEqual(float x1, float y1, float x2, float y2, float tolerance) {
        assert tolerance >= 0.0F;
        return Math.abs(x2 - x1) <= tolerance && Math.abs(y2 - y1) <= tolerance;
    }

    @Contract(pure = true)
    public static float dotProduct(float ax, float ay, float bx, float by) {
        return ax * bx + ay * by;
    }

    @Contract(pure = true)
    public static float crossProduct(float ax, float ay, float bx, float by) {
        return ax * by - ay * bx;
    }

    public static boolean normalize(float[] pos, int off) {
        return setLength(pos, off, 1.0F);
    }

    public static boolean setLength(float[] pos, int off, float length) {
        double x = (double) pos[off];
        double y = (double) pos[off + 1];
        double dmag = Math.sqrt(x * x + y * y);
        double dscale = (double) length / dmag;
        float newX = (float) (x * dscale);
        float newY = (float) (y * dscale);
        if (isDegenerate(newX, newY)) {
            return false;
        } else {
            pos[off] = newX;
            pos[off + 1] = newY;
            return true;
        }
    }

    @Contract(pure = true)
    public static float length(float x, float y) {
        return (float) Math.sqrt((double) x * (double) x + (double) y * (double) y);
    }

    @Contract(pure = true)
    public static float lengthSq(float x, float y) {
        return x * x + y * y;
    }

    @Contract(pure = true)
    public static float distanceTo(float ax, float ay, float bx, float by) {
        return length(ax - bx, ay - by);
    }

    @Contract(pure = true)
    public static float distanceToSq(float ax, float ay, float bx, float by) {
        float dx = ax - bx;
        float dy = ay - by;
        return dx * dx + dy * dy;
    }

    public static float distanceToLineBetweenSq(float px, float py, float ax, float ay, float bx, float by) {
        float ux = bx - ax;
        float uy = by - ay;
        float vx = px - ax;
        float vy = py - ay;
        float uLengthSq = ux * ux + uy * uy;
        float det = ux * vy - uy * vx;
        float temp = det / uLengthSq * det;
        return !Float.isFinite(temp) ? vx * vx + vy * vy : temp;
    }

    public static float distanceToLineBetween(float px, float py, float ax, float ay, float bx, float by) {
        return (float) Math.sqrt((double) distanceToLineBetweenSq(px, py, ax, ay, bx, by));
    }

    public static float distanceToLineSegmentBetweenSq(float px, float py, float ax, float ay, float bx, float by) {
        float ux = bx - ax;
        float uy = by - ay;
        float vx = px - ax;
        float vy = py - ay;
        float uDotV = ux * vx + uy * vy;
        if (uDotV <= 0.0F) {
            return vx * vx + vy * vy;
        } else {
            float uLengthSq = ux * ux + uy * uy;
            if (uDotV >= uLengthSq) {
                return distanceToSq(bx, by, px, py);
            } else {
                float det = ux * vy - uy * vx;
                float temp = det / uLengthSq * det;
                return !Float.isFinite(temp) ? vx * vx + vy * vy : temp;
            }
        }
    }

    public static float distanceToLineSegmentBetween(float px, float py, float ax, float ay, float bx, float by) {
        return (float) Math.sqrt((double) distanceToLineSegmentBetweenSq(px, py, ax, ay, bx, by));
    }
}