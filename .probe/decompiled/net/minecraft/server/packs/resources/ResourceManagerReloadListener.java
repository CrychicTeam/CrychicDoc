package net.minecraft.server.packs.resources;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.util.Unit;
import net.minecraft.util.profiling.ProfilerFiller;

public interface ResourceManagerReloadListener extends PreparableReloadListener {

    @Override
    default CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier preparableReloadListenerPreparationBarrier0, ResourceManager resourceManager1, ProfilerFiller profilerFiller2, ProfilerFiller profilerFiller3, Executor executor4, Executor executor5) {
        return preparableReloadListenerPreparationBarrier0.wait(Unit.INSTANCE).thenRunAsync(() -> {
            profilerFiller3.startTick();
            profilerFiller3.push("listener");
            this.onResourceManagerReload(resourceManager1);
            profilerFiller3.pop();
            profilerFiller3.endTick();
        }, executor5);
    }

    void onResourceManagerReload(ResourceManager var1);
}