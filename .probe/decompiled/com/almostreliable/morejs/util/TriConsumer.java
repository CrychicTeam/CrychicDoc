package com.almostreliable.morejs.util;

@FunctionalInterface
public interface TriConsumer<T1, T2, T3> {

    void accept(T1 var1, T2 var2, T3 var3);
}