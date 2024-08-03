package fuzs.puzzleslib.api.event.v1.data;

import fuzs.puzzleslib.impl.event.data.EventMutableInt;
import fuzs.puzzleslib.impl.event.data.ValueMutableInt;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;

public interface MutableInt extends IntConsumer, IntSupplier {

    static MutableInt fromValue(int value) {
        return new ValueMutableInt(value);
    }

    static MutableInt fromEvent(IntConsumer consumer, IntSupplier supplier) {
        return new EventMutableInt(consumer, supplier);
    }

    default void mapInt(IntUnaryOperator operator) {
        this.accept(operator.applyAsInt(this.getAsInt()));
    }
}