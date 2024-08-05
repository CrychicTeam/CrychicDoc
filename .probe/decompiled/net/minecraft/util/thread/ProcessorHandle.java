package net.minecraft.util.thread;

import com.mojang.datafixers.util.Either;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public interface ProcessorHandle<Msg> extends AutoCloseable {

    String name();

    void tell(Msg var1);

    default void close() {
    }

    default <Source> CompletableFuture<Source> ask(Function<? super ProcessorHandle<Source>, ? extends Msg> functionSuperProcessorHandleSourceExtendsMsg0) {
        CompletableFuture<Source> $$1 = new CompletableFuture();
        Msg $$2 = (Msg) functionSuperProcessorHandleSourceExtendsMsg0.apply(of("ask future procesor handle", $$1::complete));
        this.tell($$2);
        return $$1;
    }

    default <Source> CompletableFuture<Source> askEither(Function<? super ProcessorHandle<Either<Source, Exception>>, ? extends Msg> functionSuperProcessorHandleEitherSourceExceptionExtendsMsg0) {
        CompletableFuture<Source> $$1 = new CompletableFuture();
        Msg $$2 = (Msg) functionSuperProcessorHandleEitherSourceExceptionExtendsMsg0.apply(of("ask future procesor handle", p_18719_ -> {
            p_18719_.ifLeft($$1::complete);
            p_18719_.ifRight($$1::completeExceptionally);
        }));
        this.tell($$2);
        return $$1;
    }

    static <Msg> ProcessorHandle<Msg> of(final String string0, final Consumer<Msg> consumerMsg1) {
        return new ProcessorHandle<Msg>() {

            @Override
            public String name() {
                return string0;
            }

            @Override
            public void tell(Msg p_18731_) {
                consumerMsg1.accept(p_18731_);
            }

            public String toString() {
                return string0;
            }
        };
    }
}