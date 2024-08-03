package fuzs.puzzleslib.api.event.v1.data;

import fuzs.puzzleslib.impl.event.data.EventDefaultedValue;
import fuzs.puzzleslib.impl.event.data.ValueDefaultedValue;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public interface DefaultedValue<T> extends MutableValue<T> {

    static <T> DefaultedValue<T> fromValue(T value) {
        return new ValueDefaultedValue<>(value);
    }

    static <T> DefaultedValue<T> fromEvent(Consumer<T> consumer, Supplier<T> supplier, Supplier<T> defaultSupplier) {
        return new EventDefaultedValue<>(consumer, supplier, defaultSupplier);
    }

    T getAsDefault();

    Optional<T> getAsOptional();

    default void applyDefault() {
        this.accept(this.getAsDefault());
    }

    default void mapDefault(UnaryOperator<T> operator) {
        this.applyDefault();
        this.map(operator);
    }
}