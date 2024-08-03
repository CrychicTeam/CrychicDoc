package net.minecraft.server.packs.resources;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.util.profiling.ProfilerFiller;

public abstract class SimplePreparableReloadListener<T> implements PreparableReloadListener {

    @Override
    public final CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier preparableReloadListenerPreparationBarrier0, ResourceManager resourceManager1, ProfilerFiller profilerFiller2, ProfilerFiller profilerFiller3, Executor executor4, Executor executor5) {
        return CompletableFuture.supplyAsync(() -> this.prepare(resourceManager1, profilerFiller2), executor4).thenCompose(preparableReloadListenerPreparationBarrier0::m_6769_).thenAcceptAsync(p_10792_ -> this.apply((T) p_10792_, resourceManager1, profilerFiller3), executor5);
    }

    protected abstract T prepare(ResourceManager var1, ProfilerFiller var2);

    protected abstract void apply(T var1, ResourceManager var2, ProfilerFiller var3);
}