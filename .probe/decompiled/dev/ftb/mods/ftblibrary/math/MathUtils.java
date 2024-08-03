package dev.ftb.mods.ftblibrary.math;

import java.util.Random;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class MathUtils {

    public static final Random RAND = new Random();

    public static final float[] NORMALS_X = new float[] { 0.0F, 0.0F, 0.0F, 0.0F, -1.0F, 1.0F };

    public static final float[] NORMALS_Y = new float[] { -1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F };

    public static final float[] NORMALS_Z = new float[] { 0.0F, 0.0F, -1.0F, 1.0F, 0.0F, 0.0F };

    public static final int[] ROTATION_X = new int[] { 90, 270, 0, 0, 0, 0 };

    public static final int[] ROTATION_Y = new int[] { 0, 0, 180, 0, 90, 270 };

    private static final int CACHED_SPIRAL_POINTS_SIZE = 81;

    private static XZ[] CACHED_SPIRAL_POINTS = null;

    public static double sq(double value) {
        return value * value;
    }

    public static double sqrt(double value) {
        return value != 0.0 && value != 1.0 ? Math.sqrt(value) : value;
    }

    public static double sqrt2sq(double x, double y) {
        return sqrt(sq(x) + sq(y));
    }

    public static double sqrt3sq(double x, double y, double z) {
        return sqrt(sq(x) + sq(y) + sq(z));
    }

    public static double distSq(double x1, double y1, double z1, double x2, double y2, double z2) {
        return x1 == x2 && y1 == y2 && z1 == z2 ? 0.0 : sq(x2 - x1) + sq(y2 - y1) + sq(z2 - z1);
    }

    public static double dist(double x1, double y1, double z1, double x2, double y2, double z2) {
        return sqrt(distSq(x1, y1, z1, x2, y2, z2));
    }

    public static double distSq(double x1, double y1, double x2, double y2) {
        return sq(x2 - x1) + sq(y2 - y1);
    }

    public static double dist(double x1, double y1, double x2, double y2) {
        return sqrt(distSq(x1, y1, x2, y2));
    }

    public static int chunk(int i) {
        return i >> 4;
    }

    public static int chunk(double d) {
        return chunk(Mth.floor(d));
    }

    public static boolean canParseInt(@Nullable String string) {
        if (string != null && !string.isEmpty()) {
            try {
                Integer.parseInt(string);
                return true;
            } catch (Exception var2) {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean canParseDouble(@Nullable String string) {
        if (string != null && !string.isEmpty()) {
            try {
                Double.parseDouble(string);
                return true;
            } catch (Exception var2) {
                return false;
            }
        } else {
            return false;
        }
    }

    public static float lerp(float min, float max, float value) {
        return min + (max - min) * value;
    }

    public static double lerp(double min, double max, double value) {
        return min + (max - min) * value;
    }

    public static Vec3 lerp(double x1, double y1, double z1, double x2, double y2, double z2, double value) {
        return new Vec3(lerp(x1, x2, value), lerp(y1, y2, value), lerp(z1, z2, value));
    }

    public static Vec3 lerp(Vec3 v1, Vec3 v2, double value) {
        return lerp(v1.x, v1.y, v1.z, v2.x, v2.y, v2.z, value);
    }

    public static double map(double min1, double max1, double min2, double max2, double value) {
        return lerp(min2, max2, (value - min1) / (max1 - min1));
    }

    public static double mod(double i, double n) {
        i %= n;
        return i < 0.0 ? i + n : i;
    }

    public static int mod(int i, int n) {
        i %= n;
        return i < 0 ? i + n : i;
    }

    public static XZ getSpiralPoint(int index) {
        if (index < 0) {
            index = 0;
        }
        if (index >= 81) {
            return getSpiralPoint0(index);
        } else {
            if (CACHED_SPIRAL_POINTS == null) {
                CACHED_SPIRAL_POINTS = new XZ[81];
                for (int i = 0; i < 81; i++) {
                    CACHED_SPIRAL_POINTS[i] = getSpiralPoint0(i);
                }
            }
            return CACHED_SPIRAL_POINTS[index];
        }
    }

    public static XZ getSpiralPoint0(int index) {
        if (index <= 0) {
            return XZ.of(0, 0);
        } else {
            int x = 0;
            int z = 0;
            int dx = 0;
            int dz = 1;
            int segmentLength = 1;
            int segmentPassed = 0;
            for (int n = 0; n < index; n++) {
                x += dx;
                z += dz;
                if (++segmentPassed == segmentLength) {
                    segmentPassed = 0;
                    int buffer = dz;
                    dz = -dx;
                    dx = buffer;
                    if (buffer == 0) {
                        segmentLength++;
                    }
                }
            }
            return XZ.of(x, z);
        }
    }

    public static long clamp(long f, long g, long h) {
        return f < g ? g : Math.min(f, h);
    }
}