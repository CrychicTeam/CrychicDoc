package me.shedaniel.clothconfig2.api.animator;

public interface ValueProvider<T> {

    static <T> ValueProvider<T> constant(T value) {
        return new ConstantValueProvider<>(value);
    }

    T value();

    T target();

    void completeImmediately();

    void update(double var1);
}