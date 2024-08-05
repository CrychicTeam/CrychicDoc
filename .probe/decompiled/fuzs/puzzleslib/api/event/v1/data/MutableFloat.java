package fuzs.puzzleslib.api.event.v1.data;

import fuzs.puzzleslib.impl.event.data.EventMutableFloat;
import fuzs.puzzleslib.impl.event.data.ValueMutableFloat;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public interface MutableFloat {

    static MutableFloat fromValue(float value) {
        return new ValueMutableFloat(value);
    }

    static MutableFloat fromEvent(Consumer<Float> consumer, Supplier<Float> supplier) {
        return new EventMutableFloat(consumer, supplier);
    }

    void accept(float var1);

    float getAsFloat();

    default void mapFloat(UnaryOperator<Float> operator) {
        this.accept((Float) operator.apply(this.getAsFloat()));
    }
}