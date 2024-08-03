package dev.architectury.utils.value;

import it.unimi.dsi.fastutil.ints.IntConsumer;
import java.util.function.IntSupplier;

public interface IntValue extends Value<Integer>, IntSupplier, IntConsumer {

    default Integer get() {
        return this.getAsInt();
    }
}