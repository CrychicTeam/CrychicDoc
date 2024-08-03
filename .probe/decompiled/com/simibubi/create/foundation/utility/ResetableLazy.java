package com.simibubi.create.foundation.utility;

import java.util.function.Supplier;
import net.minecraftforge.common.util.NonNullSupplier;

public class ResetableLazy<T> implements Supplier<T> {

    private final NonNullSupplier<T> supplier;

    private T value;

    public ResetableLazy(NonNullSupplier<T> supplier) {
        this.supplier = supplier;
    }

    public T get() {
        if (this.value == null) {
            this.value = this.supplier.get();
        }
        return this.value;
    }

    public void reset() {
        this.value = null;
    }

    public static <T> ResetableLazy<T> of(NonNullSupplier<T> supplier) {
        return new ResetableLazy<>(supplier);
    }
}