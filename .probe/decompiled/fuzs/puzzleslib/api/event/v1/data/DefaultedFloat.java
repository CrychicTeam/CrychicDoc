package fuzs.puzzleslib.api.event.v1.data;

import fuzs.puzzleslib.impl.event.data.EventDefaultedFloat;
import fuzs.puzzleslib.impl.event.data.ValueDefaultedFloat;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public interface DefaultedFloat extends MutableFloat {

    static DefaultedFloat fromValue(float value) {
        return new ValueDefaultedFloat(value);
    }

    static DefaultedFloat fromEvent(Consumer<Float> consumer, Supplier<Float> supplier, Supplier<Float> defaultSupplier) {
        return new EventDefaultedFloat(consumer, supplier, defaultSupplier);
    }

    float getAsDefaultFloat();

    Optional<Float> getAsOptionalFloat();

    default void applyDefaultFloat() {
        this.accept(this.getAsDefaultFloat());
    }

    default void mapDefaultFloat(UnaryOperator<Float> operator) {
        this.applyDefaultFloat();
        this.mapFloat(operator);
    }
}