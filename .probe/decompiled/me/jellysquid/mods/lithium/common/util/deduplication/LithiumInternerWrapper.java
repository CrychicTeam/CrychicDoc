package me.jellysquid.mods.lithium.common.util.deduplication;

public interface LithiumInternerWrapper<T> {

    T getCanonical(T var1);

    void deleteCanonical(T var1);
}