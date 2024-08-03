package com.rekindled.embers.util;

public class EmberGenUtil {

    public static int offX = 0;

    public static int offZ = 0;

    public static float getEmberDensity(long seed, int x, int z) {
        float emberVelocity = 10.0F;
        return (getEmberStability(seed, x, z) + (float) Math.pow((double) ((80.0F * NoiseGenUtil.getOctave(seed, x + (int) ((float) offX * emberVelocity), z + (int) ((float) offZ * emberVelocity), 112) + 20.0F * NoiseGenUtil.getOctave(seed, x + (int) ((float) offX * emberVelocity), z + (int) ((float) offZ * emberVelocity), 68) + 6.0F * NoiseGenUtil.getOctave(seed, x + (int) ((float) offX * emberVelocity), z + (int) ((float) offZ * emberVelocity), 34) + 4.0F * NoiseGenUtil.getOctave(seed, x + (int) ((float) offX * emberVelocity), z + (int) ((float) offZ * emberVelocity), 21) + 2.0F * NoiseGenUtil.getOctave(seed, x + (int) ((float) offX * emberVelocity), z + (int) ((float) offZ * emberVelocity), 11) + 1.0F * NoiseGenUtil.getOctave(seed, x + (int) ((float) offX * emberVelocity), z + (int) ((float) offZ * emberVelocity), 4)) / 93.0F), 1.6F)) * 0.5F;
    }

    public static float getEmberStability(long seed, int x, int z) {
        return 1.0F - (float) Math.pow((double) ((32.0F * NoiseGenUtil.getOctave(seed, x, z, 120) + 16.0F * NoiseGenUtil.getOctave(seed, x, z, 76) + 6.0F * NoiseGenUtil.getOctave(seed, x, z, 45) + 3.0F * NoiseGenUtil.getOctave(seed, x, z, 21) + 1.0F * NoiseGenUtil.getOctave(seed, x, z, 5)) / 58.0F), 3.0);
    }
}