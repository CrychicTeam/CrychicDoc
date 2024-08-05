package fuzs.puzzleslib.impl.core.resources;

import fuzs.puzzleslib.impl.PuzzlesLib;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

public class ForwardingResourceManagerReloadListener extends ForwardingReloadListener<ResourceManagerReloadListener> implements ResourceManagerReloadListener {

    public ForwardingResourceManagerReloadListener(ResourceLocation identifier, Supplier<Collection<ResourceManagerReloadListener>> supplier) {
        super(identifier, supplier);
    }

    @Override
    public CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
        return ResourceManagerReloadListener.super.reload(preparationBarrier, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor);
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        for (ResourceManagerReloadListener reloadListener : this.reloadListeners()) {
            try {
                reloadListener.onResourceManagerReload(resourceManager);
            } catch (Exception var5) {
                PuzzlesLib.LOGGER.error("Unable to reload listener {}", reloadListener.m_7812_(), var5);
            }
        }
    }
}