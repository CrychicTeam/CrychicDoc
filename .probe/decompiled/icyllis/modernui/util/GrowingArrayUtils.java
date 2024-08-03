package icyllis.modernui.util;

import java.lang.reflect.Array;
import javax.annotation.Nonnull;

public final class GrowingArrayUtils {

    private GrowingArrayUtils() {
    }

    public static int growSize(int currentSize) {
        return currentSize <= 4 ? 8 : currentSize * 2;
    }

    @Nonnull
    public static <T> T[] append(@Nonnull T[] array, int currentSize, T element) {
        assert currentSize <= array.length;
        if (currentSize >= array.length) {
            Class<? extends Object[]> newType = array.getClass();
            int newLength = growSize(currentSize);
            T[] newArray = (T[]) (newType == Object[].class ? new Object[newLength] : (Object[]) Array.newInstance(newType.getComponentType(), newLength));
            System.arraycopy(array, 0, newArray, 0, currentSize);
            array = newArray;
        }
        array[currentSize] = element;
        return array;
    }

    @Nonnull
    public static int[] append(@Nonnull int[] array, int currentSize, int element) {
        assert currentSize <= array.length;
        if (currentSize >= array.length) {
            int[] newArray = new int[growSize(currentSize)];
            System.arraycopy(array, 0, newArray, 0, currentSize);
            array = newArray;
        }
        array[currentSize] = element;
        return array;
    }

    @Nonnull
    public static long[] append(@Nonnull long[] array, int currentSize, long element) {
        assert currentSize <= array.length;
        if (currentSize >= array.length) {
            long[] newArray = new long[growSize(currentSize)];
            System.arraycopy(array, 0, newArray, 0, currentSize);
            array = newArray;
        }
        array[currentSize] = element;
        return array;
    }

    @Nonnull
    public static boolean[] append(@Nonnull boolean[] array, int currentSize, boolean element) {
        assert currentSize <= array.length;
        if (currentSize >= array.length) {
            boolean[] newArray = new boolean[growSize(currentSize)];
            System.arraycopy(array, 0, newArray, 0, currentSize);
            array = newArray;
        }
        array[currentSize] = element;
        return array;
    }

    @Nonnull
    public static <T> T[] insert(@Nonnull T[] array, int currentSize, int index, T element) {
        assert currentSize <= array.length;
        if (currentSize < array.length) {
            System.arraycopy(array, index, array, index + 1, currentSize - index);
            array[index] = element;
            return array;
        } else {
            Class<? extends Object[]> newType = array.getClass();
            int newLength = growSize(currentSize);
            T[] newArray = (T[]) (newType == Object[].class ? new Object[newLength] : (Object[]) Array.newInstance(newType.getComponentType(), newLength));
            System.arraycopy(array, 0, newArray, 0, index);
            newArray[index] = element;
            System.arraycopy(array, index, newArray, index + 1, array.length - index);
            return newArray;
        }
    }

    @Nonnull
    public static int[] insert(@Nonnull int[] array, int currentSize, int index, int element) {
        assert currentSize <= array.length;
        if (currentSize < array.length) {
            System.arraycopy(array, index, array, index + 1, currentSize - index);
            array[index] = element;
            return array;
        } else {
            int[] newArray = new int[growSize(currentSize)];
            System.arraycopy(array, 0, newArray, 0, index);
            newArray[index] = element;
            System.arraycopy(array, index, newArray, index + 1, array.length - index);
            return newArray;
        }
    }

    @Nonnull
    public static long[] insert(@Nonnull long[] array, int currentSize, int index, long element) {
        assert currentSize <= array.length;
        if (currentSize < array.length) {
            System.arraycopy(array, index, array, index + 1, currentSize - index);
            array[index] = element;
            return array;
        } else {
            long[] newArray = new long[growSize(currentSize)];
            System.arraycopy(array, 0, newArray, 0, index);
            newArray[index] = element;
            System.arraycopy(array, index, newArray, index + 1, array.length - index);
            return newArray;
        }
    }

    @Nonnull
    public static boolean[] insert(@Nonnull boolean[] array, int currentSize, int index, boolean element) {
        assert currentSize <= array.length;
        if (currentSize < array.length) {
            System.arraycopy(array, index, array, index + 1, currentSize - index);
            array[index] = element;
            return array;
        } else {
            boolean[] newArray = new boolean[growSize(currentSize)];
            System.arraycopy(array, 0, newArray, 0, index);
            newArray[index] = element;
            System.arraycopy(array, index, newArray, index + 1, array.length - index);
            return newArray;
        }
    }
}