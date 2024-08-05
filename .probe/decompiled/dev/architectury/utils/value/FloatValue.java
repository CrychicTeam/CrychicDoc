package dev.architectury.utils.value;

import it.unimi.dsi.fastutil.floats.FloatConsumer;

public interface FloatValue extends Value<Float>, FloatSupplier, FloatConsumer {

    default Float get() {
        return this.getAsFloat();
    }
}