package me.jellysquid.mods.sodium.client.util.sorting;

public class InsertionSort extends AbstractSort {

    public static void insertionSort(int[] indices, int fromIndex, int toIndex, float[] keys) {
        int index = fromIndex;
        while (++index < toIndex) {
            int t = indices[index];
            int j = index;
            for (int u = indices[index - 1]; keys[u] < keys[t]; u = indices[j - 1]) {
                indices[j] = u;
                if (fromIndex == j - 1) {
                    j--;
                    break;
                }
                j--;
            }
            indices[j] = t;
        }
    }
}