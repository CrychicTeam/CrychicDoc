package net.minecraft.client.gui.narration;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Unit;

public class NarrationThunk<T> {

    private final T contents;

    private final BiConsumer<Consumer<String>, T> converter;

    public static final NarrationThunk<?> EMPTY = new NarrationThunk<>(Unit.INSTANCE, (p_169171_, p_169172_) -> {
    });

    private NarrationThunk(T t0, BiConsumer<Consumer<String>, T> biConsumerConsumerStringT1) {
        this.contents = t0;
        this.converter = biConsumerConsumerStringT1;
    }

    public static NarrationThunk<?> from(String string0) {
        return new NarrationThunk<>(string0, Consumer::accept);
    }

    public static NarrationThunk<?> from(Component component0) {
        return new NarrationThunk<>(component0, (p_169174_, p_169175_) -> p_169174_.accept(p_169175_.getString()));
    }

    public static NarrationThunk<?> from(List<Component> listComponent0) {
        return new NarrationThunk<>(listComponent0, (p_169166_, p_169167_) -> listComponent0.stream().map(Component::getString).forEach(p_169166_));
    }

    public void getText(Consumer<String> consumerString0) {
        this.converter.accept(consumerString0, this.contents);
    }

    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else {
            return !(object0 instanceof NarrationThunk<?> $$1) ? false : $$1.converter == this.converter && $$1.contents.equals(this.contents);
        }
    }

    public int hashCode() {
        int $$0 = this.contents.hashCode();
        return 31 * $$0 + this.converter.hashCode();
    }
}