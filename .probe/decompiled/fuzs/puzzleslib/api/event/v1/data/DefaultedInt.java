package fuzs.puzzleslib.api.event.v1.data;

import fuzs.puzzleslib.impl.event.data.EventDefaultedInt;
import fuzs.puzzleslib.impl.event.data.ValueDefaultedInt;
import java.util.OptionalInt;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;

public interface DefaultedInt extends MutableInt {

    static DefaultedInt fromValue(int value) {
        return new ValueDefaultedInt(value);
    }

    static DefaultedInt fromEvent(IntConsumer consumer, IntSupplier supplier, IntSupplier defaultSupplier) {
        return new EventDefaultedInt(consumer, supplier, defaultSupplier);
    }

    int getAsDefaultInt();

    OptionalInt getAsOptionalInt();

    default void applyDefaultInt() {
        this.accept(this.getAsDefaultInt());
    }

    default void mapDefaultInt(IntUnaryOperator operator) {
        this.applyDefaultInt();
        this.mapInt(operator);
    }
}