package me.jellysquid.mods.sodium.client.util.sorting;

public class MergeSort extends AbstractSort {

    private static final int INSERTION_SORT_THRESHOLD = 16;

    public static int[] mergeSort(float[] keys) {
        int[] indices = createIndexBuffer(keys.length);
        mergeSort(indices, keys);
        return indices;
    }

    private static void mergeSort(int[] indices, float[] keys) {
        mergeSort(indices, keys, 0, indices.length, null);
    }

    private static void mergeSort(int[] indices, float[] keys, int fromIndex, int toIndex, int[] supp) {
        int len = toIndex - fromIndex;
        if (len < 16) {
            InsertionSort.insertionSort(indices, fromIndex, toIndex, keys);
        } else {
            if (supp == null) {
                supp = (int[]) indices.clone();
            }
            int mid = fromIndex + toIndex >>> 1;
            mergeSort(supp, keys, fromIndex, mid, indices);
            mergeSort(supp, keys, mid, toIndex, indices);
            if (keys[supp[mid]] <= keys[supp[mid - 1]]) {
                System.arraycopy(supp, fromIndex, indices, fromIndex, len);
            } else {
                int i = fromIndex;
                int p = fromIndex;
                for (int q = mid; i < toIndex; i++) {
                    if (q < toIndex && (p >= mid || !(keys[supp[q]] <= keys[supp[p]]))) {
                        indices[i] = supp[q++];
                    } else {
                        indices[i] = supp[p++];
                    }
                }
            }
        }
    }
}