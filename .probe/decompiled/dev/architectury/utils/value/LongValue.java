package dev.architectury.utils.value;

import it.unimi.dsi.fastutil.longs.LongConsumer;
import java.util.function.LongSupplier;

public interface LongValue extends Value<Long>, LongSupplier, LongConsumer {

    default Long get() {
        return this.getAsLong();
    }
}