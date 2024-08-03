package io.github.lightman314.lightmanscurrency.util;

import net.minecraft.core.BlockPos;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class MathUtil {

    public static Vector3f getXP() {
        return new Vector3f(1.0F, 0.0F, 0.0F);
    }

    public static Vector3f getYP() {
        return new Vector3f(0.0F, 1.0F, 0.0F);
    }

    public static Vector3f getZP() {
        return new Vector3f(0.0F, 0.0F, 1.0F);
    }

    public static Quaternionf fromAxisAngleDegree(Vector3f axis, float degree) {
        return new Quaternionf().fromAxisAngleDeg(axis, degree);
    }

    public static Vector3f VectorMult(Vector3f vector, float num) {
        return new Vector3f(vector.x() * num, vector.y() * num, vector.z() * num);
    }

    public static Vector3f VectorAdd(Vector3f... vectors) {
        float x = 0.0F;
        float y = 0.0F;
        float z = 0.0F;
        for (Vector3f vector : vectors) {
            x += vector.x();
            y += vector.y();
            z += vector.z();
        }
        return new Vector3f(x, y, z);
    }

    public static int clamp(int value, int min, int max) {
        if (min > max) {
            int temp = min;
            min = max;
            max = temp;
        }
        if (value < min) {
            value = min;
        } else if (value > max) {
            value = max;
        }
        return value;
    }

    public static float clamp(float value, float min, float max) {
        if (min > max) {
            float temp = min;
            min = max;
            max = temp;
        }
        if (value < min) {
            value = min;
        } else if (value > max) {
            value = max;
        }
        return value;
    }

    public static double clamp(double value, double min, double max) {
        if (min > max) {
            double temp = min;
            min = max;
            max = temp;
        }
        if (value < min) {
            value = min;
        } else if (value > max) {
            value = max;
        }
        return value;
    }

    public static long clamp(long value, long min, long max) {
        if (min > max) {
            long temp = min;
            min = max;
            max = temp;
        }
        if (value < min) {
            value = min;
        } else if (value > max) {
            value = max;
        }
        return value;
    }

    public static long SafeDivide(long a, long b, long divideByZero) {
        return b == 0L ? divideByZero : a / b;
    }

    public static int DivideByAndRoundUp(int a, int b) {
        int result = a / b;
        if ((double) a / (double) b % 1.0 != 0.0) {
            result++;
        }
        return result;
    }

    public static boolean WithinBounds(BlockPos queryPos, BlockPos corner1, BlockPos corner2) {
        return WithinBounds(queryPos.m_123341_(), corner1.m_123341_(), corner2.m_123341_()) && WithinBounds(queryPos.m_123342_(), corner1.m_123342_(), corner2.m_123342_()) && WithinBounds(queryPos.m_123343_(), corner1.m_123343_(), corner2.m_123343_());
    }

    public static boolean WithinBounds(int query, int val1, int val2) {
        int min;
        int max;
        if (val1 > val2) {
            max = val1;
            min = val2;
        } else {
            min = val1;
            max = val2;
        }
        return query <= max && query >= min;
    }
}