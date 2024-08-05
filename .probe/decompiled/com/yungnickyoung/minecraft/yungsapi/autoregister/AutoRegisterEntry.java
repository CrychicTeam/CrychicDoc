package com.yungnickyoung.minecraft.yungsapi.autoregister;

import java.util.function.Supplier;
import org.jetbrains.annotations.ApiStatus.Internal;

public abstract class AutoRegisterEntry<T> {

    T cachedEntry;

    Supplier<T> entrySupplier;

    public AutoRegisterEntry(Supplier<T> entrySupplier) {
        this.entrySupplier = entrySupplier;
    }

    @Internal
    public Supplier<T> getSupplier() {
        return this.entrySupplier;
    }

    @Internal
    public void setSupplier(Supplier<T> entrySupplier) {
        this.entrySupplier = entrySupplier;
    }

    public T get() {
        if (this.cachedEntry != null) {
            return this.cachedEntry;
        } else {
            T entry = (T) this.entrySupplier.get();
            this.cachedEntry = entry;
            return entry;
        }
    }
}