package me.jellysquid.mods.sodium.client.util.sorting;

import com.mojang.blaze3d.vertex.VertexSorting;
import org.joml.Vector3f;

public class VertexSorters {

    public static VertexSorting sortByDistance(Vector3f origin) {
        return new VertexSorters.SortByDistance(origin);
    }

    private abstract static class AbstractVertexSorter implements VertexSorting {

        @Override
        public final int[] sort(Vector3f[] positions) {
            return this.mergeSort(positions);
        }

        private int[] mergeSort(Vector3f[] positions) {
            float[] keys = new float[positions.length];
            for (int index = 0; index < positions.length; index++) {
                keys[index] = this.getKey(positions[index]);
            }
            return MergeSort.mergeSort(keys);
        }

        protected abstract float getKey(Vector3f var1);
    }

    private static class SortByDistance extends VertexSorters.AbstractVertexSorter {

        private final Vector3f origin;

        private SortByDistance(Vector3f origin) {
            this.origin = origin;
        }

        @Override
        protected float getKey(Vector3f position) {
            return this.origin.distanceSquared(position);
        }
    }
}