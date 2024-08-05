package me.jellysquid.mods.sodium.client.model.light.smooth;

import me.jellysquid.mods.sodium.client.model.light.data.ArrayLightDataCache;
import me.jellysquid.mods.sodium.client.model.light.data.LightDataAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

class AoFaceData {

    public final int[] lm = new int[4];

    public final float[] ao = new float[4];

    public final float[] bl = new float[4];

    public final float[] sl = new float[4];

    private int flags;

    public void initLightData(LightDataAccess cache, BlockPos pos, Direction direction, boolean offset) {
        int x = pos.m_123341_();
        int y = pos.m_123342_();
        int z = pos.m_123343_();
        int adjX;
        int adjY;
        int adjZ;
        if (offset) {
            adjX = x + direction.getStepX();
            adjY = y + direction.getStepY();
            adjZ = z + direction.getStepZ();
        } else {
            adjX = x;
            adjY = y;
            adjZ = z;
        }
        int adjWord = cache.get(adjX, adjY, adjZ);
        int calm;
        boolean caem;
        if (offset && ArrayLightDataCache.unpackFO(adjWord)) {
            int originWord = cache.get(x, y, z);
            calm = ArrayLightDataCache.getLightmap(originWord);
            caem = ArrayLightDataCache.unpackEM(originWord);
        } else {
            calm = ArrayLightDataCache.getLightmap(adjWord);
            caem = ArrayLightDataCache.unpackEM(adjWord);
        }
        float caao = ArrayLightDataCache.unpackAO(adjWord);
        Direction[] faces = AoNeighborInfo.get(direction).faces;
        int e0 = cache.get(adjX, adjY, adjZ, faces[0]);
        int e0lm = ArrayLightDataCache.getLightmap(e0);
        float e0ao = ArrayLightDataCache.unpackAO(e0);
        boolean e0op = ArrayLightDataCache.unpackOP(e0);
        boolean e0em = ArrayLightDataCache.unpackEM(e0);
        int e1 = cache.get(adjX, adjY, adjZ, faces[1]);
        int e1lm = ArrayLightDataCache.getLightmap(e1);
        float e1ao = ArrayLightDataCache.unpackAO(e1);
        boolean e1op = ArrayLightDataCache.unpackOP(e1);
        boolean e1em = ArrayLightDataCache.unpackEM(e1);
        int e2 = cache.get(adjX, adjY, adjZ, faces[2]);
        int e2lm = ArrayLightDataCache.getLightmap(e2);
        float e2ao = ArrayLightDataCache.unpackAO(e2);
        boolean e2op = ArrayLightDataCache.unpackOP(e2);
        boolean e2em = ArrayLightDataCache.unpackEM(e2);
        int e3 = cache.get(adjX, adjY, adjZ, faces[3]);
        int e3lm = ArrayLightDataCache.getLightmap(e3);
        float e3ao = ArrayLightDataCache.unpackAO(e3);
        boolean e3op = ArrayLightDataCache.unpackOP(e3);
        boolean e3em = ArrayLightDataCache.unpackEM(e3);
        int c0lm;
        float c0ao;
        boolean c0em;
        if (e2op && e0op) {
            c0lm = e0lm;
            c0ao = e0ao;
            c0em = e0em;
        } else {
            int d0 = cache.get(adjX, adjY, adjZ, faces[0], faces[2]);
            c0lm = ArrayLightDataCache.getLightmap(d0);
            c0ao = ArrayLightDataCache.unpackAO(d0);
            c0em = ArrayLightDataCache.unpackEM(d0);
        }
        float c1ao;
        boolean c1em;
        int c1lm;
        if (e3op && e0op) {
            c1lm = e0lm;
            c1ao = e0ao;
            c1em = e0em;
        } else {
            int d1 = cache.get(adjX, adjY, adjZ, faces[0], faces[3]);
            c1lm = ArrayLightDataCache.getLightmap(d1);
            c1ao = ArrayLightDataCache.unpackAO(d1);
            c1em = ArrayLightDataCache.unpackEM(d1);
        }
        float c2ao;
        boolean c2em;
        int c2lm;
        if (e2op && e1op) {
            c2lm = e1lm;
            c2ao = e1ao;
            c2em = e1em;
        } else {
            int d2 = cache.get(adjX, adjY, adjZ, faces[1], faces[2]);
            c2lm = ArrayLightDataCache.getLightmap(d2);
            c2ao = ArrayLightDataCache.unpackAO(d2);
            c2em = ArrayLightDataCache.unpackEM(d2);
        }
        float c3ao;
        boolean c3em;
        int c3lm;
        if (e3op && e1op) {
            c3lm = e1lm;
            c3ao = e1ao;
            c3em = e1em;
        } else {
            int d3 = cache.get(adjX, adjY, adjZ, faces[1], faces[3]);
            c3lm = ArrayLightDataCache.getLightmap(d3);
            c3ao = ArrayLightDataCache.unpackAO(d3);
            c3em = ArrayLightDataCache.unpackEM(d3);
        }
        float[] ao = this.ao;
        ao[0] = (e3ao + e0ao + c1ao + caao) * 0.25F;
        ao[1] = (e2ao + e0ao + c0ao + caao) * 0.25F;
        ao[2] = (e2ao + e1ao + c2ao + caao) * 0.25F;
        ao[3] = (e3ao + e1ao + c3ao + caao) * 0.25F;
        int[] cb = this.lm;
        cb[0] = calculateCornerBrightness(e3lm, e0lm, c1lm, calm, e3em, e0em, c1em, caem);
        cb[1] = calculateCornerBrightness(e2lm, e0lm, c0lm, calm, e2em, e0em, c0em, caem);
        cb[2] = calculateCornerBrightness(e2lm, e1lm, c2lm, calm, e2em, e1em, c2em, caem);
        cb[3] = calculateCornerBrightness(e3lm, e1lm, c3lm, calm, e3em, e1em, c3em, caem);
        this.flags |= 1;
    }

    public void unpackLightData() {
        int[] lm = this.lm;
        float[] bl = this.bl;
        float[] sl = this.sl;
        bl[0] = unpackBlockLight(lm[0]);
        bl[1] = unpackBlockLight(lm[1]);
        bl[2] = unpackBlockLight(lm[2]);
        bl[3] = unpackBlockLight(lm[3]);
        sl[0] = unpackSkyLight(lm[0]);
        sl[1] = unpackSkyLight(lm[1]);
        sl[2] = unpackSkyLight(lm[2]);
        sl[3] = unpackSkyLight(lm[3]);
        this.flags |= 2;
    }

    public float getBlendedSkyLight(float[] w) {
        return weightedSum(this.sl, w);
    }

    public float getBlendedBlockLight(float[] w) {
        return weightedSum(this.bl, w);
    }

    public float getBlendedShade(float[] w) {
        return weightedSum(this.ao, w);
    }

    private static float weightedSum(float[] v, float[] w) {
        float t0 = v[0] * w[0];
        float t1 = v[1] * w[1];
        float t2 = v[2] * w[2];
        float t3 = v[3] * w[3];
        return t0 + t1 + t2 + t3;
    }

    private static float unpackSkyLight(int i) {
        return (float) (i >> 16 & 0xFF);
    }

    private static float unpackBlockLight(int i) {
        return (float) (i & 0xFF);
    }

    private static int calculateCornerBrightness(int a, int b, int c, int d, boolean aem, boolean bem, boolean cem, boolean dem) {
        if (a == 0 || b == 0 || c == 0 || d == 0) {
            int min = minNonZero(minNonZero(a, b), minNonZero(c, d));
            a = Math.max(a, min);
            b = Math.max(b, min);
            c = Math.max(c, min);
            d = Math.max(d, min);
        }
        if (aem) {
            a = 15728880;
        }
        if (bem) {
            b = 15728880;
        }
        if (cem) {
            c = 15728880;
        }
        if (dem) {
            d = 15728880;
        }
        return a + b + c + d >> 2 & 16711935;
    }

    private static int minNonZero(int a, int b) {
        if (a == 0) {
            return b;
        } else {
            return b == 0 ? a : Math.min(a, b);
        }
    }

    public boolean hasLightData() {
        return (this.flags & 1) != 0;
    }

    public boolean hasUnpackedLightData() {
        return (this.flags & 2) != 0;
    }

    public void reset() {
        this.flags = 0;
    }
}