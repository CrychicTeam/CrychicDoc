package net.minecraftforge.event;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.ModLoader;

public class AddReloadListenerEvent extends Event {

    private final List<PreparableReloadListener> listeners = new ArrayList();

    private final ReloadableServerResources serverResources;

    private final RegistryAccess registryAccess;

    public AddReloadListenerEvent(ReloadableServerResources serverResources, RegistryAccess registryAccess) {
        this.serverResources = serverResources;
        this.registryAccess = registryAccess;
    }

    public void addListener(PreparableReloadListener listener) {
        this.listeners.add(new AddReloadListenerEvent.WrappedStateAwareListener(listener));
    }

    public List<PreparableReloadListener> getListeners() {
        return ImmutableList.copyOf(this.listeners);
    }

    public ReloadableServerResources getServerResources() {
        return this.serverResources;
    }

    public ICondition.IContext getConditionContext() {
        return this.serverResources.getConditionContext();
    }

    public RegistryAccess getRegistryAccess() {
        return this.registryAccess;
    }

    private static class WrappedStateAwareListener implements PreparableReloadListener {

        private final PreparableReloadListener wrapped;

        private WrappedStateAwareListener(PreparableReloadListener wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier stage, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
            return ModLoader.isLoadingStateValid() ? this.wrapped.reload(stage, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor) : CompletableFuture.completedFuture(null);
        }
    }
}