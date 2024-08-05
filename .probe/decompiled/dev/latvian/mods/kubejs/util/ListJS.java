package dev.latvian.mods.kubejs.util;

import com.google.gson.JsonArray;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.Nullable;

public interface ListJS {

    @Nullable
    static List<?> of(@Nullable Object o) {
        if (o instanceof List) {
            return (List<?>) o;
        } else if (!(o instanceof Iterable<?> itr)) {
            return ofArray(o);
        } else {
            ArrayList<Object> list = new ArrayList(itr instanceof Collection<?> c ? c.size() : 4);
            for (Object o1 : itr) {
                list.add(o1);
            }
            return list;
        }
    }

    static List<?> orEmpty(@Nullable Object o) {
        List<?> l = of(o);
        return l == null ? List.of() : l;
    }

    static List<?> orSelf(@Nullable Object o) {
        List<?> l = of(o);
        if (l != null) {
            return l;
        } else {
            ArrayList<Object> list = new ArrayList(1);
            if (o != null) {
                list.add(o);
            }
            return list;
        }
    }

    @Nullable
    static List<?> ofArray(@Nullable Object array) {
        if (array instanceof Object[]) {
            return new ArrayList(Arrays.asList((Object[]) array));
        } else if (array instanceof int[]) {
            return of((int[]) array);
        } else if (array instanceof byte[]) {
            return of((byte[]) array);
        } else if (array instanceof short[]) {
            return of((short[]) array);
        } else if (array instanceof long[]) {
            return of((long[]) array);
        } else if (array instanceof float[]) {
            return of((float[]) array);
        } else if (array instanceof double[]) {
            return of((double[]) array);
        } else if (array instanceof char[]) {
            return of((char[]) array);
        } else if (array != null && array.getClass().isArray()) {
            int length = Array.getLength(array);
            ArrayList<Object> list = new ArrayList(length);
            for (int i = 0; i < length; i++) {
                list.add(Array.get(array, i));
            }
            return list;
        } else {
            return null;
        }
    }

    static List<Byte> of(byte[] array) {
        ArrayList<Byte> list = new ArrayList(array.length);
        for (byte v : array) {
            list.add(v);
        }
        return list;
    }

    static List<Short> of(short[] array) {
        ArrayList<Short> list = new ArrayList(array.length);
        for (short v : array) {
            list.add(v);
        }
        return list;
    }

    static List<Integer> of(int[] array) {
        ArrayList<Integer> list = new ArrayList(array.length);
        for (int v : array) {
            list.add(v);
        }
        return list;
    }

    static List<Long> of(long[] array) {
        ArrayList<Long> list = new ArrayList(array.length);
        for (long v : array) {
            list.add(v);
        }
        return list;
    }

    static List<Float> of(float[] array) {
        ArrayList<Float> list = new ArrayList(array.length);
        for (float v : array) {
            list.add(v);
        }
        return list;
    }

    static List<Double> of(double[] array) {
        ArrayList<Double> list = new ArrayList(array.length);
        for (double v : array) {
            list.add(v);
        }
        return list;
    }

    static List<Character> of(char[] array) {
        ArrayList<Character> list = new ArrayList(array.length);
        for (char v : array) {
            list.add(v);
        }
        return list;
    }

    @Nullable
    static Set<?> ofSet(@Nullable Object o) {
        if (o instanceof Set) {
            return (Set<?>) o;
        } else if (o instanceof Collection<?> c) {
            return new LinkedHashSet(c);
        } else if (!(o instanceof Iterable<?> itr)) {
            List<?> list = of(o);
            return list == null ? null : new LinkedHashSet(list);
        } else {
            HashSet<Object> set = new HashSet();
            for (Object o1 : itr) {
                set.add(o1);
            }
            return set;
        }
    }

    @Nullable
    static JsonArray json(@Nullable Object array) {
        if (array instanceof JsonArray) {
            return (JsonArray) array;
        } else if (array instanceof CharSequence) {
            try {
                return (JsonArray) JsonIO.GSON.fromJson(array.toString(), JsonArray.class);
            } catch (Exception var6) {
                return null;
            }
        } else if (!(array instanceof Iterable<?> itr)) {
            return null;
        } else {
            JsonArray json = new JsonArray();
            for (Object o1 : itr) {
                json.add(JsonIO.of(o1));
            }
            return json;
        }
    }
}