package fuzs.puzzleslib.api.event.v1.data;

import fuzs.puzzleslib.impl.event.data.EventDefaultedDouble;
import fuzs.puzzleslib.impl.event.data.ValueDefaultedDouble;
import java.util.OptionalDouble;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleUnaryOperator;

public interface DefaultedDouble extends MutableDouble {

    static DefaultedDouble fromValue(double value) {
        return new ValueDefaultedDouble(value);
    }

    static DefaultedDouble fromEvent(DoubleConsumer consumer, DoubleSupplier supplier, DoubleSupplier defaultSupplier) {
        return new EventDefaultedDouble(consumer, supplier, defaultSupplier);
    }

    double getAsDefaultDouble();

    OptionalDouble getAsOptionalDouble();

    default void applyDefaultDouble() {
        this.accept(this.getAsDefaultDouble());
    }

    default void mapDefaultDouble(DoubleUnaryOperator operator) {
        this.applyDefaultDouble();
        this.mapDouble(operator);
    }
}