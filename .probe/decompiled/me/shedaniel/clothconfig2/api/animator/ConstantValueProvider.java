package me.shedaniel.clothconfig2.api.animator;

import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
final class ConstantValueProvider<T> implements ValueProvider<T> {

    private final T value;

    public ConstantValueProvider(T value) {
        this.value = value;
    }

    @Override
    public T value() {
        return this.value;
    }

    @Override
    public T target() {
        return this.value;
    }

    @Override
    public void completeImmediately() {
    }

    @Override
    public void update(double delta) {
    }
}