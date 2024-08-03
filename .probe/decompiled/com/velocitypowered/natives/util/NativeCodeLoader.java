package com.velocitypowered.natives.util;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class NativeCodeLoader<T> implements Supplier<T> {

    private final NativeCodeLoader.Variant<T> selected;

    static final BooleanSupplier ALWAYS = () -> true;

    NativeCodeLoader(List<NativeCodeLoader.Variant<T>> variants) {
        this.selected = getVariant(variants);
    }

    public T get() {
        return this.selected.constructed;
    }

    private static <T> NativeCodeLoader.Variant<T> getVariant(List<NativeCodeLoader.Variant<T>> variants) {
        for (NativeCodeLoader.Variant<T> variant : variants) {
            T got = variant.get();
            if (got != null) {
                return variant;
            }
        }
        throw new IllegalArgumentException("Can't find any suitable variants");
    }

    public String getLoadedVariant() {
        return this.selected.name;
    }

    private static enum Status {

        NOT_AVAILABLE, POSSIBLY_AVAILABLE, SETUP, SETUP_FAILURE
    }

    static class Variant<T> {

        private NativeCodeLoader.Status status;

        private final Runnable setup;

        private final String name;

        private final Supplier<T> object;

        private T constructed;

        Variant(BooleanSupplier possiblyAvailable, Runnable setup, String name, T object) {
            this(possiblyAvailable, setup, name, (Supplier<T>) (() -> object));
        }

        Variant(BooleanSupplier possiblyAvailable, Runnable setup, String name, Supplier<T> object) {
            this.status = possiblyAvailable.getAsBoolean() ? NativeCodeLoader.Status.POSSIBLY_AVAILABLE : NativeCodeLoader.Status.NOT_AVAILABLE;
            this.setup = setup;
            this.name = name;
            this.object = object;
        }

        @Nullable
        public T get() {
            if (this.status == NativeCodeLoader.Status.NOT_AVAILABLE || this.status == NativeCodeLoader.Status.SETUP_FAILURE) {
                return null;
            } else {
                if (this.status == NativeCodeLoader.Status.POSSIBLY_AVAILABLE) {
                    try {
                        this.setup.run();
                        this.constructed = (T) this.object.get();
                        this.status = NativeCodeLoader.Status.SETUP;
                    } catch (Exception var2) {
                        this.status = NativeCodeLoader.Status.SETUP_FAILURE;
                        return null;
                    }
                }
                return this.constructed;
            }
        }
    }
}