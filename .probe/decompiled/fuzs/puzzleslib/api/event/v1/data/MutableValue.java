package fuzs.puzzleslib.api.event.v1.data;

import fuzs.puzzleslib.impl.event.data.EventMutableValue;
import fuzs.puzzleslib.impl.event.data.ValueMutableValue;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public interface MutableValue<T> extends Consumer<T>, Supplier<T> {

    static <T> MutableValue<T> fromValue(T value) {
        return new ValueMutableValue<>(value);
    }

    static <T> MutableValue<T> fromEvent(Consumer<T> consumer, Supplier<T> supplier) {
        return new EventMutableValue<>(consumer, supplier);
    }

    default void map(UnaryOperator<T> operator) {
        this.accept(operator.apply(this.get()));
    }
}