package dev.ftb.mods.ftblibrary.util;

import java.util.NoSuchElementException;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;

public class OptionalBoolean {

    public static final OptionalBoolean EMPTY = new OptionalBoolean(null);

    public static final OptionalBoolean TRUE = new OptionalBoolean(true);

    public static final OptionalBoolean FALSE = new OptionalBoolean(false);

    private final Boolean value;

    public static OptionalBoolean ofNullable(@Nullable Boolean v) {
        return v == null ? EMPTY : of(v);
    }

    public static OptionalBoolean of(boolean v) {
        return v ? TRUE : FALSE;
    }

    private OptionalBoolean(@Nullable Boolean v) {
        this.value = v;
    }

    public boolean orElse(boolean b) {
        return this.value == null ? b : this.value;
    }

    public boolean get() {
        if (this.value == null) {
            throw new NoSuchElementException("No value present");
        } else {
            return this.value;
        }
    }

    public boolean isPresent() {
        return this.value != null;
    }

    public void ifPresent(OptionalBoolean.Consumer consumer) {
        if (this.value != null) {
            consumer.accept(this.value);
        }
    }

    public boolean orElseGet(BooleanSupplier other) {
        return this.value != null ? this.value : other.getAsBoolean();
    }

    public <X extends Throwable> boolean orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (this.value != null) {
            return this.value;
        } else {
            throw (Throwable) exceptionSupplier.get();
        }
    }

    @FunctionalInterface
    public interface Consumer {

        void accept(boolean var1);
    }
}