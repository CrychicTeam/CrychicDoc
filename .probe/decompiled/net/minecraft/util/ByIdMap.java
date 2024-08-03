package net.minecraft.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

public class ByIdMap {

    private static <T> IntFunction<T> createMap(ToIntFunction<T> toIntFunctionT0, T[] t1) {
        if (t1.length == 0) {
            throw new IllegalArgumentException("Empty value list");
        } else {
            Int2ObjectMap<T> $$2 = new Int2ObjectOpenHashMap();
            for (T $$3 : t1) {
                int $$4 = toIntFunctionT0.applyAsInt($$3);
                T $$5 = (T) $$2.put($$4, $$3);
                if ($$5 != null) {
                    throw new IllegalArgumentException("Duplicate entry on id " + $$4 + ": current=" + $$3 + ", previous=" + $$5);
                }
            }
            return $$2;
        }
    }

    public static <T> IntFunction<T> sparse(ToIntFunction<T> toIntFunctionT0, T[] t1, T t2) {
        IntFunction<T> $$3 = createMap(toIntFunctionT0, t1);
        return p_262932_ -> Objects.requireNonNullElse($$3.apply(p_262932_), t2);
    }

    private static <T> T[] createSortedArray(ToIntFunction<T> toIntFunctionT0, T[] t1) {
        int $$2 = t1.length;
        if ($$2 == 0) {
            throw new IllegalArgumentException("Empty value list");
        } else {
            T[] $$3 = (T[]) t1.clone();
            Arrays.fill($$3, null);
            for (T $$4 : t1) {
                int $$5 = toIntFunctionT0.applyAsInt($$4);
                if ($$5 < 0 || $$5 >= $$2) {
                    throw new IllegalArgumentException("Values are not continous, found index " + $$5 + " for value " + $$4);
                }
                T $$6 = $$3[$$5];
                if ($$6 != null) {
                    throw new IllegalArgumentException("Duplicate entry on id " + $$5 + ": current=" + $$4 + ", previous=" + $$6);
                }
                $$3[$$5] = $$4;
            }
            for (int $$7 = 0; $$7 < $$2; $$7++) {
                if ($$3[$$7] == null) {
                    throw new IllegalArgumentException("Missing value at index: " + $$7);
                }
            }
            return $$3;
        }
    }

    public static <T> IntFunction<T> continuous(ToIntFunction<T> toIntFunctionT0, T[] t1, ByIdMap.OutOfBoundsStrategy byIdMapOutOfBoundsStrategy2) {
        T[] $$3 = (T[]) createSortedArray(toIntFunctionT0, t1);
        int $$4 = $$3.length;
        return switch(byIdMapOutOfBoundsStrategy2) {
            case ZERO ->
                {
                    T $$5 = $$3[0];
                    ???;
                }
            case WRAP ->
                p_262977_ -> $$3[Mth.positiveModulo(p_262977_, $$4)];
            case CLAMP ->
                p_263013_ -> $$3[Mth.clamp(p_263013_, 0, $$4 - 1)];
        };
    }

    public static enum OutOfBoundsStrategy {

        ZERO, WRAP, CLAMP
    }
}