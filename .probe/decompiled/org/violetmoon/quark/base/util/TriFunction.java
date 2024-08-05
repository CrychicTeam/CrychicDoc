package org.violetmoon.quark.base.util;

@FunctionalInterface
public interface TriFunction<R, T, U, V> {

    R apply(T var1, U var2, V var3);
}