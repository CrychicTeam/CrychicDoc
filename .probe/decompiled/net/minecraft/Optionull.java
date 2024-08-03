package net.minecraft;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nullable;

public class Optionull {

    @Nullable
    public static <T, R> R map(@Nullable T t0, Function<T, R> functionTR1) {
        return (R) (t0 == null ? null : functionTR1.apply(t0));
    }

    public static <T, R> R mapOrDefault(@Nullable T t0, Function<T, R> functionTR1, R r2) {
        return (R) (t0 == null ? r2 : functionTR1.apply(t0));
    }

    public static <T, R> R mapOrElse(@Nullable T t0, Function<T, R> functionTR1, Supplier<R> supplierR2) {
        return (R) (t0 == null ? supplierR2.get() : functionTR1.apply(t0));
    }

    @Nullable
    public static <T> T first(Collection<T> collectionT0) {
        Iterator<T> $$1 = collectionT0.iterator();
        return (T) ($$1.hasNext() ? $$1.next() : null);
    }

    public static <T> T firstOrDefault(Collection<T> collectionT0, T t1) {
        Iterator<T> $$2 = collectionT0.iterator();
        return (T) ($$2.hasNext() ? $$2.next() : t1);
    }

    public static <T> T firstOrElse(Collection<T> collectionT0, Supplier<T> supplierT1) {
        Iterator<T> $$2 = collectionT0.iterator();
        return (T) ($$2.hasNext() ? $$2.next() : supplierT1.get());
    }

    public static <T> boolean isNullOrEmpty(@Nullable T[] t0) {
        return t0 == null || t0.length == 0;
    }

    public static boolean isNullOrEmpty(@Nullable boolean[] boolean0) {
        return boolean0 == null || boolean0.length == 0;
    }

    public static boolean isNullOrEmpty(@Nullable byte[] byte0) {
        return byte0 == null || byte0.length == 0;
    }

    public static boolean isNullOrEmpty(@Nullable char[] char0) {
        return char0 == null || char0.length == 0;
    }

    public static boolean isNullOrEmpty(@Nullable short[] short0) {
        return short0 == null || short0.length == 0;
    }

    public static boolean isNullOrEmpty(@Nullable int[] int0) {
        return int0 == null || int0.length == 0;
    }

    public static boolean isNullOrEmpty(@Nullable long[] long0) {
        return long0 == null || long0.length == 0;
    }

    public static boolean isNullOrEmpty(@Nullable float[] float0) {
        return float0 == null || float0.length == 0;
    }

    public static boolean isNullOrEmpty(@Nullable double[] double0) {
        return double0 == null || double0.length == 0;
    }
}