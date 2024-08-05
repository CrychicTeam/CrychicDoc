package dev.architectury.utils.value;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import java.util.function.BooleanSupplier;

public interface BooleanValue extends Value<Boolean>, BooleanSupplier, BooleanConsumer {

    default Boolean get() {
        return this.getAsBoolean();
    }
}