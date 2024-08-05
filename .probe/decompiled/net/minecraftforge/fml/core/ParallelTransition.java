package net.minecraftforge.fml.core;

import cpw.mods.modlauncher.api.LamdbaExceptionUtils;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraftforge.fml.IModStateTransition;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingStage;
import net.minecraftforge.fml.ThreadSelector;
import net.minecraftforge.fml.IModStateTransition.EventGenerator;
import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;

record ParallelTransition(ModLoadingStage stage, Class<? extends ParallelDispatchEvent> event) implements IModStateTransition {

    public Supplier<Stream<EventGenerator<?>>> eventFunctionStream() {
        return () -> Stream.of(EventGenerator.fromFunction(LamdbaExceptionUtils.rethrowFunction(mc -> (ParallelDispatchEvent) this.event.getConstructor(ModContainer.class, ModLoadingStage.class).newInstance(mc, this.stage))));
    }

    public ThreadSelector threadSelector() {
        return ThreadSelector.PARALLEL;
    }

    public BiFunction<Executor, CompletableFuture<Void>, CompletableFuture<Void>> finalActivityGenerator() {
        return (e, prev) -> prev.thenApplyAsync(t -> {
            this.stage.getDeferredWorkQueue().runTasks();
            return t;
        }, e);
    }

    public BiFunction<Executor, ? extends EventGenerator<?>, CompletableFuture<Void>> preDispatchHook() {
        return (t, f) -> CompletableFuture.completedFuture(null);
    }

    public BiFunction<Executor, ? extends EventGenerator<?>, CompletableFuture<Void>> postDispatchHook() {
        return (t, f) -> CompletableFuture.completedFuture(null);
    }
}