package me.jellysquid.mods.sodium.client.util.sorting;

public class AbstractSort {

    protected static int[] createIndexBuffer(int length) {
        int[] indices = new int[length];
        int i = 0;
        while (i < length) {
            indices[i] = i++;
        }
        return indices;
    }
}