package net.minecraft.util;

import java.util.Locale;
import java.util.UUID;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;
import net.minecraft.Util;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.math.NumberUtils;

public class Mth {

    private static final long UUID_VERSION = 61440L;

    private static final long UUID_VERSION_TYPE_4 = 16384L;

    private static final long UUID_VARIANT = -4611686018427387904L;

    private static final long UUID_VARIANT_2 = Long.MIN_VALUE;

    public static final float PI = (float) Math.PI;

    public static final float HALF_PI = (float) (Math.PI / 2);

    public static final float TWO_PI = (float) (Math.PI * 2);

    public static final float DEG_TO_RAD = (float) (Math.PI / 180.0);

    public static final float RAD_TO_DEG = 180.0F / (float) Math.PI;

    public static final float EPSILON = 1.0E-5F;

    public static final float SQRT_OF_TWO = sqrt(2.0F);

    private static final float SIN_SCALE = 10430.378F;

    private static final float[] SIN = Util.make(new float[65536], p_14077_ -> {
        for (int $$1x = 0; $$1x < p_14077_.length; $$1x++) {
            p_14077_[$$1x] = (float) Math.sin((double) $$1x * Math.PI * 2.0 / 65536.0);
        }
    });

    private static final RandomSource RANDOM = RandomSource.createThreadSafe();

    private static final int[] MULTIPLY_DE_BRUIJN_BIT_POSITION = new int[] { 0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9 };

    private static final double ONE_SIXTH = 0.16666666666666666;

    private static final int FRAC_EXP = 8;

    private static final int LUT_SIZE = 257;

    private static final double FRAC_BIAS = Double.longBitsToDouble(4805340802404319232L);

    private static final double[] ASIN_TAB = new double[257];

    private static final double[] COS_TAB = new double[257];

    public static float sin(float float0) {
        return SIN[(int) (float0 * 10430.378F) & 65535];
    }

    public static float cos(float float0) {
        return SIN[(int) (float0 * 10430.378F + 16384.0F) & 65535];
    }

    public static float sqrt(float float0) {
        return (float) Math.sqrt((double) float0);
    }

    public static int floor(float float0) {
        int $$1 = (int) float0;
        return float0 < (float) $$1 ? $$1 - 1 : $$1;
    }

    public static int floor(double double0) {
        int $$1 = (int) double0;
        return double0 < (double) $$1 ? $$1 - 1 : $$1;
    }

    public static long lfloor(double double0) {
        long $$1 = (long) double0;
        return double0 < (double) $$1 ? $$1 - 1L : $$1;
    }

    public static float abs(float float0) {
        return Math.abs(float0);
    }

    public static int abs(int int0) {
        return Math.abs(int0);
    }

    public static int ceil(float float0) {
        int $$1 = (int) float0;
        return float0 > (float) $$1 ? $$1 + 1 : $$1;
    }

    public static int ceil(double double0) {
        int $$1 = (int) double0;
        return double0 > (double) $$1 ? $$1 + 1 : $$1;
    }

    public static int clamp(int int0, int int1, int int2) {
        return Math.min(Math.max(int0, int1), int2);
    }

    public static float clamp(float float0, float float1, float float2) {
        return float0 < float1 ? float1 : Math.min(float0, float2);
    }

    public static double clamp(double double0, double double1, double double2) {
        return double0 < double1 ? double1 : Math.min(double0, double2);
    }

    public static double clampedLerp(double double0, double double1, double double2) {
        if (double2 < 0.0) {
            return double0;
        } else {
            return double2 > 1.0 ? double1 : lerp(double2, double0, double1);
        }
    }

    public static float clampedLerp(float float0, float float1, float float2) {
        if (float2 < 0.0F) {
            return float0;
        } else {
            return float2 > 1.0F ? float1 : lerp(float2, float0, float1);
        }
    }

    public static double absMax(double double0, double double1) {
        if (double0 < 0.0) {
            double0 = -double0;
        }
        if (double1 < 0.0) {
            double1 = -double1;
        }
        return Math.max(double0, double1);
    }

    public static int floorDiv(int int0, int int1) {
        return Math.floorDiv(int0, int1);
    }

    public static int nextInt(RandomSource randomSource0, int int1, int int2) {
        return int1 >= int2 ? int1 : randomSource0.nextInt(int2 - int1 + 1) + int1;
    }

    public static float nextFloat(RandomSource randomSource0, float float1, float float2) {
        return float1 >= float2 ? float1 : randomSource0.nextFloat() * (float2 - float1) + float1;
    }

    public static double nextDouble(RandomSource randomSource0, double double1, double double2) {
        return double1 >= double2 ? double1 : randomSource0.nextDouble() * (double2 - double1) + double1;
    }

    public static boolean equal(float float0, float float1) {
        return Math.abs(float1 - float0) < 1.0E-5F;
    }

    public static boolean equal(double double0, double double1) {
        return Math.abs(double1 - double0) < 1.0E-5F;
    }

    public static int positiveModulo(int int0, int int1) {
        return Math.floorMod(int0, int1);
    }

    public static float positiveModulo(float float0, float float1) {
        return (float0 % float1 + float1) % float1;
    }

    public static double positiveModulo(double double0, double double1) {
        return (double0 % double1 + double1) % double1;
    }

    public static boolean isMultipleOf(int int0, int int1) {
        return int0 % int1 == 0;
    }

    public static int wrapDegrees(int int0) {
        int $$1 = int0 % 360;
        if ($$1 >= 180) {
            $$1 -= 360;
        }
        if ($$1 < -180) {
            $$1 += 360;
        }
        return $$1;
    }

    public static float wrapDegrees(float float0) {
        float $$1 = float0 % 360.0F;
        if ($$1 >= 180.0F) {
            $$1 -= 360.0F;
        }
        if ($$1 < -180.0F) {
            $$1 += 360.0F;
        }
        return $$1;
    }

    public static double wrapDegrees(double double0) {
        double $$1 = double0 % 360.0;
        if ($$1 >= 180.0) {
            $$1 -= 360.0;
        }
        if ($$1 < -180.0) {
            $$1 += 360.0;
        }
        return $$1;
    }

    public static float degreesDifference(float float0, float float1) {
        return wrapDegrees(float1 - float0);
    }

    public static float degreesDifferenceAbs(float float0, float float1) {
        return abs(degreesDifference(float0, float1));
    }

    public static float rotateIfNecessary(float float0, float float1, float float2) {
        float $$3 = degreesDifference(float0, float1);
        float $$4 = clamp($$3, -float2, float2);
        return float1 - $$4;
    }

    public static float approach(float float0, float float1, float float2) {
        float2 = abs(float2);
        return float0 < float1 ? clamp(float0 + float2, float0, float1) : clamp(float0 - float2, float1, float0);
    }

    public static float approachDegrees(float float0, float float1, float float2) {
        float $$3 = degreesDifference(float0, float1);
        return approach(float0, float0 + $$3, float2);
    }

    public static int getInt(String string0, int int1) {
        return NumberUtils.toInt(string0, int1);
    }

    public static int smallestEncompassingPowerOfTwo(int int0) {
        int $$1 = int0 - 1;
        $$1 |= $$1 >> 1;
        $$1 |= $$1 >> 2;
        $$1 |= $$1 >> 4;
        $$1 |= $$1 >> 8;
        $$1 |= $$1 >> 16;
        return $$1 + 1;
    }

    public static boolean isPowerOfTwo(int int0) {
        return int0 != 0 && (int0 & int0 - 1) == 0;
    }

    public static int ceillog2(int int0) {
        int0 = isPowerOfTwo(int0) ? int0 : smallestEncompassingPowerOfTwo(int0);
        return MULTIPLY_DE_BRUIJN_BIT_POSITION[(int) ((long) int0 * 125613361L >> 27) & 31];
    }

    public static int log2(int int0) {
        return ceillog2(int0) - (isPowerOfTwo(int0) ? 0 : 1);
    }

    public static int color(float float0, float float1, float float2) {
        return FastColor.ARGB32.color(0, floor(float0 * 255.0F), floor(float1 * 255.0F), floor(float2 * 255.0F));
    }

    public static float frac(float float0) {
        return float0 - (float) floor(float0);
    }

    public static double frac(double double0) {
        return double0 - (double) lfloor(double0);
    }

    @Deprecated
    public static long getSeed(Vec3i vecI0) {
        return getSeed(vecI0.getX(), vecI0.getY(), vecI0.getZ());
    }

    @Deprecated
    public static long getSeed(int int0, int int1, int int2) {
        long $$3 = (long) (int0 * 3129871) ^ (long) int2 * 116129781L ^ (long) int1;
        $$3 = $$3 * $$3 * 42317861L + $$3 * 11L;
        return $$3 >> 16;
    }

    public static UUID createInsecureUUID(RandomSource randomSource0) {
        long $$1 = randomSource0.nextLong() & -61441L | 16384L;
        long $$2 = randomSource0.nextLong() & 4611686018427387903L | Long.MIN_VALUE;
        return new UUID($$1, $$2);
    }

    public static UUID createInsecureUUID() {
        return createInsecureUUID(RANDOM);
    }

    public static double inverseLerp(double double0, double double1, double double2) {
        return (double0 - double1) / (double2 - double1);
    }

    public static float inverseLerp(float float0, float float1, float float2) {
        return (float0 - float1) / (float2 - float1);
    }

    public static boolean rayIntersectsAABB(Vec3 vec0, Vec3 vec1, AABB aABB2) {
        double $$3 = (aABB2.minX + aABB2.maxX) * 0.5;
        double $$4 = (aABB2.maxX - aABB2.minX) * 0.5;
        double $$5 = vec0.x - $$3;
        if (Math.abs($$5) > $$4 && $$5 * vec1.x >= 0.0) {
            return false;
        } else {
            double $$6 = (aABB2.minY + aABB2.maxY) * 0.5;
            double $$7 = (aABB2.maxY - aABB2.minY) * 0.5;
            double $$8 = vec0.y - $$6;
            if (Math.abs($$8) > $$7 && $$8 * vec1.y >= 0.0) {
                return false;
            } else {
                double $$9 = (aABB2.minZ + aABB2.maxZ) * 0.5;
                double $$10 = (aABB2.maxZ - aABB2.minZ) * 0.5;
                double $$11 = vec0.z - $$9;
                if (Math.abs($$11) > $$10 && $$11 * vec1.z >= 0.0) {
                    return false;
                } else {
                    double $$12 = Math.abs(vec1.x);
                    double $$13 = Math.abs(vec1.y);
                    double $$14 = Math.abs(vec1.z);
                    double $$15 = vec1.y * $$11 - vec1.z * $$8;
                    if (Math.abs($$15) > $$7 * $$14 + $$10 * $$13) {
                        return false;
                    } else {
                        $$15 = vec1.z * $$5 - vec1.x * $$11;
                        if (Math.abs($$15) > $$4 * $$14 + $$10 * $$12) {
                            return false;
                        } else {
                            $$15 = vec1.x * $$8 - vec1.y * $$5;
                            return Math.abs($$15) < $$4 * $$13 + $$7 * $$12;
                        }
                    }
                }
            }
        }
    }

    public static double atan2(double double0, double double1) {
        double $$2 = double1 * double1 + double0 * double0;
        if (Double.isNaN($$2)) {
            return Double.NaN;
        } else {
            boolean $$3 = double0 < 0.0;
            if ($$3) {
                double0 = -double0;
            }
            boolean $$4 = double1 < 0.0;
            if ($$4) {
                double1 = -double1;
            }
            boolean $$5 = double0 > double1;
            if ($$5) {
                double $$6 = double1;
                double1 = double0;
                double0 = $$6;
            }
            double $$7 = fastInvSqrt($$2);
            double1 *= $$7;
            double0 *= $$7;
            double $$8 = FRAC_BIAS + double0;
            int $$9 = (int) Double.doubleToRawLongBits($$8);
            double $$10 = ASIN_TAB[$$9];
            double $$11 = COS_TAB[$$9];
            double $$12 = $$8 - FRAC_BIAS;
            double $$13 = double0 * $$11 - double1 * $$12;
            double $$14 = (6.0 + $$13 * $$13) * $$13 * 0.16666666666666666;
            double $$15 = $$10 + $$14;
            if ($$5) {
                $$15 = (Math.PI / 2) - $$15;
            }
            if ($$4) {
                $$15 = Math.PI - $$15;
            }
            if ($$3) {
                $$15 = -$$15;
            }
            return $$15;
        }
    }

    public static float invSqrt(float float0) {
        return org.joml.Math.invsqrt(float0);
    }

    public static double invSqrt(double double0) {
        return org.joml.Math.invsqrt(double0);
    }

    @Deprecated
    public static double fastInvSqrt(double double0) {
        double $$1 = 0.5 * double0;
        long $$2 = Double.doubleToRawLongBits(double0);
        $$2 = 6910469410427058090L - ($$2 >> 1);
        double0 = Double.longBitsToDouble($$2);
        return double0 * (1.5 - $$1 * double0 * double0);
    }

    public static float fastInvCubeRoot(float float0) {
        int $$1 = Float.floatToIntBits(float0);
        $$1 = 1419967116 - $$1 / 3;
        float $$2 = Float.intBitsToFloat($$1);
        $$2 = 0.6666667F * $$2 + 1.0F / (3.0F * $$2 * $$2 * float0);
        return 0.6666667F * $$2 + 1.0F / (3.0F * $$2 * $$2 * float0);
    }

    public static int hsvToRgb(float float0, float float1, float float2) {
        int $$3 = (int) (float0 * 6.0F) % 6;
        float $$4 = float0 * 6.0F - (float) $$3;
        float $$5 = float2 * (1.0F - float1);
        float $$6 = float2 * (1.0F - $$4 * float1);
        float $$7 = float2 * (1.0F - (1.0F - $$4) * float1);
        float $$8;
        float $$9;
        float $$10;
        switch($$3) {
            case 0:
                $$8 = float2;
                $$9 = $$7;
                $$10 = $$5;
                break;
            case 1:
                $$8 = $$6;
                $$9 = float2;
                $$10 = $$5;
                break;
            case 2:
                $$8 = $$5;
                $$9 = float2;
                $$10 = $$7;
                break;
            case 3:
                $$8 = $$5;
                $$9 = $$6;
                $$10 = float2;
                break;
            case 4:
                $$8 = $$7;
                $$9 = $$5;
                $$10 = float2;
                break;
            case 5:
                $$8 = float2;
                $$9 = $$5;
                $$10 = $$6;
                break;
            default:
                throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + float0 + ", " + float1 + ", " + float2);
        }
        return FastColor.ARGB32.color(0, clamp((int) ($$8 * 255.0F), 0, 255), clamp((int) ($$9 * 255.0F), 0, 255), clamp((int) ($$10 * 255.0F), 0, 255));
    }

    public static int murmurHash3Mixer(int int0) {
        int0 ^= int0 >>> 16;
        int0 *= -2048144789;
        int0 ^= int0 >>> 13;
        int0 *= -1028477387;
        return int0 ^ int0 >>> 16;
    }

    public static int binarySearch(int int0, int int1, IntPredicate intPredicate2) {
        int $$3 = int1 - int0;
        while ($$3 > 0) {
            int $$4 = $$3 / 2;
            int $$5 = int0 + $$4;
            if (intPredicate2.test($$5)) {
                $$3 = $$4;
            } else {
                int0 = $$5 + 1;
                $$3 -= $$4 + 1;
            }
        }
        return int0;
    }

    public static int lerpInt(float float0, int int1, int int2) {
        return int1 + floor(float0 * (float) (int2 - int1));
    }

    public static float lerp(float float0, float float1, float float2) {
        return float1 + float0 * (float2 - float1);
    }

    public static double lerp(double double0, double double1, double double2) {
        return double1 + double0 * (double2 - double1);
    }

    public static double lerp2(double double0, double double1, double double2, double double3, double double4, double double5) {
        return lerp(double1, lerp(double0, double2, double3), lerp(double0, double4, double5));
    }

    public static double lerp3(double double0, double double1, double double2, double double3, double double4, double double5, double double6, double double7, double double8, double double9, double double10) {
        return lerp(double2, lerp2(double0, double1, double3, double4, double5, double6), lerp2(double0, double1, double7, double8, double9, double10));
    }

    public static float catmullrom(float float0, float float1, float float2, float float3, float float4) {
        return 0.5F * (2.0F * float2 + (float3 - float1) * float0 + (2.0F * float1 - 5.0F * float2 + 4.0F * float3 - float4) * float0 * float0 + (3.0F * float2 - float1 - 3.0F * float3 + float4) * float0 * float0 * float0);
    }

    public static double smoothstep(double double0) {
        return double0 * double0 * double0 * (double0 * (double0 * 6.0 - 15.0) + 10.0);
    }

    public static double smoothstepDerivative(double double0) {
        return 30.0 * double0 * double0 * (double0 - 1.0) * (double0 - 1.0);
    }

    public static int sign(double double0) {
        if (double0 == 0.0) {
            return 0;
        } else {
            return double0 > 0.0 ? 1 : -1;
        }
    }

    public static float rotLerp(float float0, float float1, float float2) {
        return float1 + float0 * wrapDegrees(float2 - float1);
    }

    public static float triangleWave(float float0, float float1) {
        return (Math.abs(float0 % float1 - float1 * 0.5F) - float1 * 0.25F) / (float1 * 0.25F);
    }

    public static float square(float float0) {
        return float0 * float0;
    }

    public static double square(double double0) {
        return double0 * double0;
    }

    public static int square(int int0) {
        return int0 * int0;
    }

    public static long square(long long0) {
        return long0 * long0;
    }

    public static double clampedMap(double double0, double double1, double double2, double double3, double double4) {
        return clampedLerp(double3, double4, inverseLerp(double0, double1, double2));
    }

    public static float clampedMap(float float0, float float1, float float2, float float3, float float4) {
        return clampedLerp(float3, float4, inverseLerp(float0, float1, float2));
    }

    public static double map(double double0, double double1, double double2, double double3, double double4) {
        return lerp(inverseLerp(double0, double1, double2), double3, double4);
    }

    public static float map(float float0, float float1, float float2, float float3, float float4) {
        return lerp(inverseLerp(float0, float1, float2), float3, float4);
    }

    public static double wobble(double double0) {
        return double0 + (2.0 * RandomSource.create((long) floor(double0 * 3000.0)).nextDouble() - 1.0) * 1.0E-7 / 2.0;
    }

    public static int roundToward(int int0, int int1) {
        return positiveCeilDiv(int0, int1) * int1;
    }

    public static int positiveCeilDiv(int int0, int int1) {
        return -Math.floorDiv(-int0, int1);
    }

    public static int randomBetweenInclusive(RandomSource randomSource0, int int1, int int2) {
        return randomSource0.nextInt(int2 - int1 + 1) + int1;
    }

    public static float randomBetween(RandomSource randomSource0, float float1, float float2) {
        return randomSource0.nextFloat() * (float2 - float1) + float1;
    }

    public static float normal(RandomSource randomSource0, float float1, float float2) {
        return float1 + (float) randomSource0.nextGaussian() * float2;
    }

    public static double lengthSquared(double double0, double double1) {
        return double0 * double0 + double1 * double1;
    }

    public static double length(double double0, double double1) {
        return Math.sqrt(lengthSquared(double0, double1));
    }

    public static double lengthSquared(double double0, double double1, double double2) {
        return double0 * double0 + double1 * double1 + double2 * double2;
    }

    public static double length(double double0, double double1, double double2) {
        return Math.sqrt(lengthSquared(double0, double1, double2));
    }

    public static int quantize(double double0, int int1) {
        return floor(double0 / (double) int1) * int1;
    }

    public static IntStream outFromOrigin(int int0, int int1, int int2) {
        return outFromOrigin(int0, int1, int2, 1);
    }

    public static IntStream outFromOrigin(int int0, int int1, int int2, int int3) {
        if (int1 > int2) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "upperbound %d expected to be > lowerBound %d", int2, int1));
        } else if (int3 < 1) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "steps expected to be >= 1, was %d", int3));
        } else {
            return int0 >= int1 && int0 <= int2 ? IntStream.iterate(int0, p_216282_ -> {
                int $$4 = Math.abs(int0 - p_216282_);
                return int0 - $$4 >= int1 || int0 + $$4 <= int2;
            }, p_216260_ -> {
                boolean $$5 = p_216260_ <= int0;
                int $$6 = Math.abs(int0 - p_216260_);
                boolean $$7 = int0 + $$6 + int3 <= int2;
                if (!$$5 || !$$7) {
                    int $$8 = int0 - $$6 - ($$5 ? int3 : 0);
                    if ($$8 >= int1) {
                        return $$8;
                    }
                }
                return int0 + $$6 + int3;
            }) : IntStream.empty();
        }
    }

    static {
        for (int $$0 = 0; $$0 < 257; $$0++) {
            double $$1 = (double) $$0 / 256.0;
            double $$2 = Math.asin($$1);
            COS_TAB[$$0] = Math.cos($$2);
            ASIN_TAB[$$0] = $$2;
        }
    }
}