package me.jellysquid.mods.lithium.common.shapes.pairs;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.world.phys.shapes.IndexMerger;

public final class LithiumDoublePairList implements IndexMerger {

    private final double[] merged;

    private final int[] indicesFirst;

    private final int[] indicesSecond;

    private final DoubleArrayList pairs;

    public LithiumDoublePairList(DoubleList aPoints, DoubleList bPoints, boolean flag1, boolean flag2) {
        int size = aPoints.size() + bPoints.size();
        this.merged = new double[size];
        this.indicesFirst = new int[size];
        this.indicesSecond = new int[size];
        this.pairs = DoubleArrayList.wrap(this.merged);
        this.merge(getArray(aPoints), getArray(bPoints), aPoints.size(), bPoints.size(), flag1, flag2);
    }

    private void merge(double[] aPoints, double[] bPoints, int aSize, int bSize, boolean flag1, boolean flag2) {
        int aIdx = 0;
        int bIdx = 0;
        double prev = 0.0;
        int a1 = 0;
        int a2 = 0;
        while (true) {
            boolean aWithinBounds = aIdx < aSize;
            boolean bWithinBounds = bIdx < bSize;
            if (!aWithinBounds && !bWithinBounds) {
                if (a2 == 0) {
                    this.merged[a2++] = Math.min(aPoints[aSize - 1], bPoints[bSize - 1]);
                }
                this.pairs.size(a2);
                return;
            }
            boolean flip = aWithinBounds && (!bWithinBounds || aPoints[aIdx] < bPoints[bIdx] + 1.0E-7);
            double value;
            if (flip) {
                value = aPoints[aIdx++];
            } else {
                value = bPoints[bIdx++];
            }
            if ((aIdx != 0 && aWithinBounds || flip || flag2) && (bIdx != 0 && bWithinBounds || !flip || flag1)) {
                if (a2 == 0 || prev < value - 1.0E-7) {
                    this.indicesFirst[a1] = aIdx - 1;
                    this.indicesSecond[a1] = bIdx - 1;
                    this.merged[a2] = value;
                    a1++;
                    a2++;
                    prev = value;
                } else if (a2 > 0) {
                    this.indicesFirst[a1 - 1] = aIdx - 1;
                    this.indicesSecond[a1 - 1] = bIdx - 1;
                }
            }
        }
    }

    @Override
    public boolean forMergedIndexes(IndexMerger.IndexConsumer predicate) {
        int l = this.pairs.size() - 1;
        for (int i = 0; i < l; i++) {
            if (!predicate.merge(this.indicesFirst[i], this.indicesSecond[i], i)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int size() {
        return this.pairs.size();
    }

    @Override
    public DoubleList getList() {
        return this.pairs;
    }

    private static double[] getArray(DoubleList list) {
        if (list instanceof DoubleArrayList) {
            return ((DoubleArrayList) list).elements();
        } else {
            double[] points = new double[list.size()];
            for (int i = 0; i < points.length; i++) {
                points[i] = list.getDouble(i);
            }
            return points;
        }
    }
}