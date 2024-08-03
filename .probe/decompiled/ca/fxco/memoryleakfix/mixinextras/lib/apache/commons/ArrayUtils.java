package ca.fxco.memoryleakfix.mixinextras.lib.apache.commons;

import java.lang.reflect.Array;

public class ArrayUtils {

    public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];

    public static final String[] EMPTY_STRING_ARRAY = new String[0];

    public static final long[] EMPTY_LONG_ARRAY = new long[0];

    public static final Long[] EMPTY_LONG_OBJECT_ARRAY = new Long[0];

    public static final int[] EMPTY_INT_ARRAY = new int[0];

    public static final Integer[] EMPTY_INTEGER_OBJECT_ARRAY = new Integer[0];

    public static final short[] EMPTY_SHORT_ARRAY = new short[0];

    public static final Short[] EMPTY_SHORT_OBJECT_ARRAY = new Short[0];

    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    public static final Byte[] EMPTY_BYTE_OBJECT_ARRAY = new Byte[0];

    public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];

    public static final Double[] EMPTY_DOUBLE_OBJECT_ARRAY = new Double[0];

    public static final float[] EMPTY_FLOAT_ARRAY = new float[0];

    public static final Float[] EMPTY_FLOAT_OBJECT_ARRAY = new Float[0];

    public static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];

    public static final Boolean[] EMPTY_BOOLEAN_OBJECT_ARRAY = new Boolean[0];

    public static final char[] EMPTY_CHAR_ARRAY = new char[0];

    public static final Character[] EMPTY_CHARACTER_OBJECT_ARRAY = new Character[0];

    public static <T> T[] clone(T[] array) {
        return (T[]) (array == null ? null : (Object[]) array.clone());
    }

    public static int[] clone(int[] array) {
        return array == null ? null : (int[]) array.clone();
    }

    public static <T> T[] subarray(T[] array, int startIndexInclusive, int endIndexExclusive) {
        if (array == null) {
            return null;
        } else {
            if (startIndexInclusive < 0) {
                startIndexInclusive = 0;
            }
            if (endIndexExclusive > array.length) {
                endIndexExclusive = array.length;
            }
            int newSize = endIndexExclusive - startIndexInclusive;
            Class<?> type = array.getClass().getComponentType();
            if (newSize <= 0) {
                T[] emptyArray = (T[]) Array.newInstance(type, 0);
                return emptyArray;
            } else {
                T[] subarray = (T[]) Array.newInstance(type, newSize);
                System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
                return subarray;
            }
        }
    }

    public static int getLength(Object array) {
        return array == null ? 0 : Array.getLength(array);
    }

    public static <T> T[] addAll(T[] array1, T... array2) {
        if (array1 == null) {
            return (T[]) clone(array2);
        } else if (array2 == null) {
            return (T[]) clone(array1);
        } else {
            Class<?> type1 = array1.getClass().getComponentType();
            T[] joinedArray = (T[]) Array.newInstance(type1, array1.length + array2.length);
            System.arraycopy(array1, 0, joinedArray, 0, array1.length);
            try {
                System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
                return joinedArray;
            } catch (ArrayStoreException var6) {
                Class<?> type2 = array2.getClass().getComponentType();
                if (!type1.isAssignableFrom(type2)) {
                    throw new IllegalArgumentException("Cannot store " + type2.getName() + " in an array of " + type1.getName(), var6);
                } else {
                    throw var6;
                }
            }
        }
    }

    public static int[] addAll(int[] array1, int... array2) {
        if (array1 == null) {
            return clone(array2);
        } else if (array2 == null) {
            return clone(array1);
        } else {
            int[] joinedArray = new int[array1.length + array2.length];
            System.arraycopy(array1, 0, joinedArray, 0, array1.length);
            System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
            return joinedArray;
        }
    }

    public static <T> T[] add(T[] array, T element) {
        Class<?> type;
        if (array != null) {
            type = array.getClass();
        } else {
            if (element == null) {
                throw new IllegalArgumentException("Arguments cannot both be null");
            }
            type = element.getClass();
        }
        T[] newArray = (T[]) copyArrayGrow1(array, type);
        newArray[newArray.length - 1] = element;
        return newArray;
    }

    private static Object copyArrayGrow1(Object array, Class<?> newArrayComponentType) {
        if (array != null) {
            int arrayLength = Array.getLength(array);
            Object newArray = Array.newInstance(array.getClass().getComponentType(), arrayLength + 1);
            System.arraycopy(array, 0, newArray, 0, arrayLength);
            return newArray;
        } else {
            return Array.newInstance(newArrayComponentType, 1);
        }
    }

    public static <T> T[] add(T[] array, int index, T element) {
        Class<?> clss = null;
        if (array != null) {
            clss = array.getClass().getComponentType();
        } else {
            if (element == null) {
                throw new IllegalArgumentException("Array and element cannot both be null");
            }
            clss = element.getClass();
        }
        return (T[]) ((Object[]) add(array, index, element, clss));
    }

    private static Object add(Object array, int index, Object element, Class<?> clss) {
        if (array == null) {
            if (index != 0) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Length: 0");
            } else {
                Object joinedArray = Array.newInstance(clss, 1);
                Array.set(joinedArray, 0, element);
                return joinedArray;
            }
        } else {
            int length = Array.getLength(array);
            if (index <= length && index >= 0) {
                Object result = Array.newInstance(clss, length + 1);
                System.arraycopy(array, 0, result, 0, index);
                Array.set(result, index, element);
                if (index < length) {
                    System.arraycopy(array, index, result, index + 1, length - index);
                }
                return result;
            } else {
                throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
            }
        }
    }

    public static <T> T[] remove(T[] array, int index) {
        return (T[]) ((Object[]) remove(array, index));
    }

    public static int[] remove(int[] array, int index) {
        return (int[]) remove(array, index);
    }

    private static Object remove(Object array, int index) {
        int length = getLength(array);
        if (index >= 0 && index < length) {
            Object result = Array.newInstance(array.getClass().getComponentType(), length - 1);
            System.arraycopy(array, 0, result, 0, index);
            if (index < length - 1) {
                System.arraycopy(array, index + 1, result, index, length - index - 1);
            }
            return result;
        } else {
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
        }
    }
}