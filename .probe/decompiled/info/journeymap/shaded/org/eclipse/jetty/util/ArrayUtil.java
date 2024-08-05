package info.journeymap.shaded.org.eclipse.jetty.util;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayUtil implements Cloneable, Serializable {

    public static <T> T[] removeFromArray(T[] array, Object item) {
        if (item != null && array != null) {
            int i = array.length;
            while (i-- > 0) {
                if (item.equals(array[i])) {
                    Class<?> c = array == null ? item.getClass() : array.getClass().getComponentType();
                    T[] na = (T[]) Array.newInstance(c, Array.getLength(array) - 1);
                    if (i > 0) {
                        System.arraycopy(array, 0, na, 0, i);
                    }
                    if (i + 1 < array.length) {
                        System.arraycopy(array, i + 1, na, i, array.length - (i + 1));
                    }
                    return na;
                }
            }
            return array;
        } else {
            return array;
        }
    }

    public static <T> T[] add(T[] array1, T[] array2) {
        if (array1 == null || array1.length == 0) {
            return array2;
        } else if (array2 != null && array2.length != 0) {
            T[] na = (T[]) Arrays.copyOf(array1, array1.length + array2.length);
            System.arraycopy(array2, 0, na, array1.length, array2.length);
            return na;
        } else {
            return array1;
        }
    }

    public static <T> T[] addToArray(T[] array, T item, Class<?> type) {
        if (array == null) {
            if (type == null && item != null) {
                type = item.getClass();
            }
            T[] na = (T[]) Array.newInstance(type, 1);
            na[0] = item;
            return na;
        } else {
            T[] na = (T[]) Arrays.copyOf(array, array.length + 1);
            na[array.length] = item;
            return na;
        }
    }

    public static <T> T[] prependToArray(T item, T[] array, Class<?> type) {
        if (array == null) {
            if (type == null && item != null) {
                type = item.getClass();
            }
            T[] na = (T[]) Array.newInstance(type, 1);
            na[0] = item;
            return na;
        } else {
            Class<?> c = array.getClass().getComponentType();
            T[] na = (T[]) Array.newInstance(c, Array.getLength(array) + 1);
            System.arraycopy(array, 0, na, 1, array.length);
            na[0] = item;
            return na;
        }
    }

    public static <E> List<E> asMutableList(E[] array) {
        return array != null && array.length != 0 ? new ArrayList(Arrays.asList(array)) : new ArrayList();
    }

    public static <T> T[] removeNulls(T[] array) {
        for (T t : array) {
            if (t == null) {
                List<T> list = new ArrayList();
                for (T t2 : array) {
                    if (t2 != null) {
                        list.add(t2);
                    }
                }
                return (T[]) list.toArray(Arrays.copyOf(array, list.size()));
            }
        }
        return array;
    }
}