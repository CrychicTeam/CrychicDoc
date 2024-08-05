package com.almostreliable.morejs.core;

import com.almostreliable.morejs.MoreJS;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

public class ReloadListener implements PreparableReloadListener {

    @Override
    public CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier barrier, ResourceManager manager, ProfilerFiller preparationProfile, ProfilerFiller reloadProfile, Executor backgroundExecutor, Executor gameExecutor) {
        return CompletableFuture.runAsync(() -> {
        }, gameExecutor).thenCompose(barrier::m_6769_).thenAccept($ -> MoreJS.PLATFORM.getTradingManager().reload());
    }

    @Override
    public String getName() {
        return "morejs:reloadlistener";
    }
}