package net.mehvahdjukaar.moonlight.api.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

@Deprecated(forRemoval = true)
public abstract class GenericSimpleResourceReloadListener implements PreparableReloadListener {

    private final String pathSuffix;

    private final int suffixLength;

    private final String directory;

    protected GenericSimpleResourceReloadListener(String path, String suffix) {
        this.directory = path;
        this.pathSuffix = suffix;
        this.suffixLength = this.pathSuffix.length();
    }

    @Override
    public final CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier stage, ResourceManager manager, ProfilerFiller workerProfiler, ProfilerFiller mainProfiler, Executor workerExecutor, Executor mainExecutor) {
        List<ResourceLocation> list = this.prepare(manager, mainProfiler);
        this.apply(list, manager, workerProfiler);
        return CompletableFuture.supplyAsync(() -> null, workerExecutor).thenCompose(stage::m_6769_).thenAcceptAsync(noResult -> {
        }, mainExecutor);
    }

    protected List<ResourceLocation> prepare(ResourceManager manager, ProfilerFiller profilerFiller) {
        List<ResourceLocation> list = new ArrayList();
        int i = this.directory.length() + 1;
        for (Entry<ResourceLocation, Resource> entry : manager.listResources(this.directory, l -> l.getPath().endsWith(this.pathSuffix)).entrySet()) {
            ResourceLocation resourcelocation = (ResourceLocation) entry.getKey();
            String s = resourcelocation.getPath();
            list.add(new ResourceLocation(resourcelocation.getNamespace(), s.substring(i, s.length() - this.suffixLength)));
        }
        return list;
    }

    public abstract void apply(List<ResourceLocation> var1, ResourceManager var2, ProfilerFiller var3);
}