package net.minecraft.server.packs.resources;

import java.util.concurrent.CompletableFuture;

public interface ReloadInstance {

    CompletableFuture<?> done();

    float getActualProgress();

    default boolean isDone() {
        return this.done().isDone();
    }

    default void checkExceptions() {
        CompletableFuture<?> $$0 = this.done();
        if ($$0.isCompletedExceptionally()) {
            $$0.join();
        }
    }
}