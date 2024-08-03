package dev.xkmc.l2library.capability.conditionals;

public interface TokenProvider<T extends ConditionalToken, C extends Context> {

    T getData(C var1);

    TokenKey<T> getKey();
}