package dev.architectury.utils.value;

import it.unimi.dsi.fastutil.doubles.DoubleConsumer;
import java.util.function.DoubleSupplier;

public interface DoubleValue extends Value<Double>, DoubleSupplier, DoubleConsumer {

    default Double get() {
        return this.getAsDouble();
    }
}