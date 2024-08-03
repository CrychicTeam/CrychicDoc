package fuzs.puzzleslib.api.event.v1.data;

import fuzs.puzzleslib.impl.event.data.EventMutableBoolean;
import fuzs.puzzleslib.impl.event.data.ValueMutableBoolean;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public interface MutableBoolean extends BooleanSupplier {

    static MutableBoolean fromValue(boolean value) {
        return new ValueMutableBoolean(value);
    }

    static MutableBoolean fromEvent(Consumer<Boolean> consumer, Supplier<Boolean> supplier) {
        return new EventMutableBoolean(consumer, supplier);
    }

    void accept(boolean var1);

    default void mapBoolean(UnaryOperator<Boolean> operator) {
        this.accept((Boolean) operator.apply(this.getAsBoolean()));
    }
}