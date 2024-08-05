package fuzs.puzzleslib.api.event.v1.data;

import fuzs.puzzleslib.impl.event.data.EventDefaultedBoolean;
import fuzs.puzzleslib.impl.event.data.ValueDefaultedBoolean;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public interface DefaultedBoolean extends MutableBoolean {

    static DefaultedBoolean fromValue(boolean value) {
        return new ValueDefaultedBoolean(value);
    }

    static DefaultedBoolean fromEvent(Consumer<Boolean> consumer, Supplier<Boolean> supplier, Supplier<Boolean> defaultSupplier) {
        return new EventDefaultedBoolean(consumer, supplier, defaultSupplier);
    }

    boolean getAsDefaultBoolean();

    Optional<Boolean> getAsOptionalBoolean();

    default void applyDefaultBoolean() {
        this.accept(this.getAsDefaultBoolean());
    }

    default void mapDefaultBoolean(UnaryOperator<Boolean> operator) {
        this.applyDefaultBoolean();
        this.mapBoolean(operator);
    }
}