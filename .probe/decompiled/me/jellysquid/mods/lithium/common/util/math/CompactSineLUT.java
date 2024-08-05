package me.jellysquid.mods.lithium.common.util.math;

import net.minecraft.util.Mth;

public class CompactSineLUT {

    private static final int[] SINE_TABLE_INT = new int[16385];

    private static final float SINE_TABLE_MIDPOINT;

    public static float sin(float f) {
        return lookup((int) (f * 10430.378F) & 65535);
    }

    public static float cos(float f) {
        return lookup((int) (f * 10430.378F + 16384.0F) & 65535);
    }

    private static float lookup(int index) {
        if (index == 32768) {
            return SINE_TABLE_MIDPOINT;
        } else {
            int neg = (index & 32768) << 16;
            int mask = index << 17 >> 31;
            int pos = (32769 & mask) + (index ^ mask);
            pos &= 32767;
            return Float.intBitsToFloat(SINE_TABLE_INT[pos] ^ neg);
        }
    }

    static {
        for (int i = 0; i < SINE_TABLE_INT.length; i++) {
            SINE_TABLE_INT[i] = Float.floatToRawIntBits(Mth.SIN[i]);
        }
        SINE_TABLE_MIDPOINT = Mth.SIN[Mth.SIN.length / 2];
        for (int i = 0; i < Mth.SIN.length; i++) {
            float expected = Mth.SIN[i];
            float value = lookup(i);
            if (expected != value) {
                throw new IllegalArgumentException(String.format("LUT error at index %d (expected: %s, found: %s)", i, expected, value));
            }
        }
    }
}