package net.minecraft.util;

import java.util.function.Consumer;

@FunctionalInterface
public interface AbortableIterationConsumer<T> {

    AbortableIterationConsumer.Continuation accept(T var1);

    static <T> AbortableIterationConsumer<T> forConsumer(Consumer<T> consumerT0) {
        return p_261916_ -> {
            consumerT0.accept(p_261916_);
            return AbortableIterationConsumer.Continuation.CONTINUE;
        };
    }

    public static enum Continuation {

        CONTINUE, ABORT;

        public boolean shouldAbort() {
            return this == ABORT;
        }
    }
}