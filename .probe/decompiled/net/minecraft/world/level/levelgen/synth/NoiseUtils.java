package net.minecraft.world.level.levelgen.synth;

import java.util.Locale;

public class NoiseUtils {

    public static double biasTowardsExtreme(double double0, double double1) {
        return double0 + Math.sin(Math.PI * double0) * double1 / Math.PI;
    }

    public static void parityNoiseOctaveConfigString(StringBuilder stringBuilder0, double double1, double double2, double double3, byte[] byte4) {
        stringBuilder0.append(String.format(Locale.ROOT, "xo=%.3f, yo=%.3f, zo=%.3f, p0=%d, p255=%d", (float) double1, (float) double2, (float) double3, byte4[0], byte4[255]));
    }

    public static void parityNoiseOctaveConfigString(StringBuilder stringBuilder0, double double1, double double2, double double3, int[] int4) {
        stringBuilder0.append(String.format(Locale.ROOT, "xo=%.3f, yo=%.3f, zo=%.3f, p0=%d, p255=%d", (float) double1, (float) double2, (float) double3, int4[0], int4[255]));
    }
}