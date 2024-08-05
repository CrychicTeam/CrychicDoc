package com.rekindled.embers.util;

import java.util.Random;

public class NoiseGenUtil {

    static Random random = new Random();

    public static float getNoise(long seed, int x, int y) {
        random.setSeed((long) simple_hash(new int[] { (int) seed, (int) (seed << 32), (int) Math.signum((float) y) * 512 + 512, (int) Math.signum((float) x) * 512 + 512, x, y }, 6));
        return random.nextFloat();
    }

    public static long getSeed(int seed, int x, int y) {
        return (long) simple_hash(new int[] { seed, (int) Math.signum((float) y) * 512 + 512, (int) Math.signum((float) x) * 512 + 512, x, y }, 6);
    }

    static int simple_hash(int[] is, int count) {
        int hash = 80238287;
        for (int i = 0; i < count; i++) {
            hash = hash << 4 ^ hash >> 28 ^ is[i] * 5449 % 130651;
        }
        return hash % 75327403;
    }

    public static float fastSin(float x) {
        if ((double) x < -3.14159265) {
            x = (float) ((double) x + 6.28318531);
        } else if ((double) x > 3.14159265) {
            x = (float) ((double) x - 6.28318531);
        }
        return x < 0.0F ? (float) (1.27323954 * (double) x + 0.405284735 * (double) x * (double) x) : (float) (1.27323954 * (double) x - 0.405284735 * (double) x * (double) x);
    }

    public static float fastCos(float x) {
        if ((double) x < -3.14159265) {
            x = (float) ((double) x + 6.28318531);
        } else if ((double) x > 3.14159265) {
            x = (float) ((double) x - 6.28318531);
        }
        x = (float) ((double) x + 1.57079632);
        if ((double) x > 3.14159265) {
            x = (float) ((double) x - 6.28318531);
        }
        return x < 0.0F ? (float) (1.27323954 * (double) x + 0.405284735 * (double) x * (double) x) : (float) (1.27323954 * (double) x - 0.405284735 * (double) x * (double) x);
    }

    public static float interpolate(float s, float e, float t) {
        float t2 = (1.0F - fastCos(t * (float) Math.PI)) / 2.0F;
        return s * (1.0F - t2) + e * t2;
    }

    public static float bilinear(float ul, float ur, float dr, float dl, float t1, float t2) {
        return interpolate(interpolate(ul, ur, t1), interpolate(dl, dr, t1), t2);
    }

    public static float getOctave(long seed, int x, int y, int dimen) {
        return bilinear(getNoise(seed, (int) Math.floor((double) ((float) x / (float) dimen)) * dimen, (int) Math.floor((double) ((float) y / (float) dimen)) * dimen), getNoise(seed, (int) Math.floor((double) ((float) x / (float) dimen)) * dimen + dimen, (int) Math.floor((double) ((float) y / (float) dimen)) * dimen), getNoise(seed, (int) Math.floor((double) ((float) x / (float) dimen)) * dimen + dimen, (int) Math.floor((double) ((float) y / (float) dimen)) * dimen + dimen), getNoise(seed, (int) Math.floor((double) ((float) x / (float) dimen)) * dimen, (int) Math.floor((double) ((float) y / (float) dimen)) * dimen + dimen), Math.abs((float) ((double) x - Math.floor((double) ((float) x / (float) dimen)) * (double) dimen) / (float) dimen), Math.abs((float) ((double) y - Math.floor((double) ((float) y / (float) dimen)) * (double) dimen) / (float) dimen));
    }
}