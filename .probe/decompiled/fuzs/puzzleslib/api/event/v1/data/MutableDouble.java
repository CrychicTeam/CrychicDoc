package fuzs.puzzleslib.api.event.v1.data;

import fuzs.puzzleslib.impl.event.data.EventMutableDouble;
import fuzs.puzzleslib.impl.event.data.ValueMutableDouble;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleUnaryOperator;

public interface MutableDouble extends DoubleConsumer, DoubleSupplier {

    static MutableDouble fromValue(double value) {
        return new ValueMutableDouble(value);
    }

    static MutableDouble fromEvent(DoubleConsumer consumer, DoubleSupplier supplier) {
        return new EventMutableDouble(consumer, supplier);
    }

    default void mapDouble(DoubleUnaryOperator operator) {
        this.accept(operator.applyAsDouble(this.getAsDouble()));
    }
}