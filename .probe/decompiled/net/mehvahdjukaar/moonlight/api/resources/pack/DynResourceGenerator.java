package net.mehvahdjukaar.moonlight.api.resources.pack;

import com.google.common.base.Stopwatch;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;
import net.mehvahdjukaar.moonlight.api.events.EarlyPackReloadEvent;
import net.mehvahdjukaar.moonlight.api.events.MoonlightEventsHelper;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.StaticResource;
import net.mehvahdjukaar.moonlight.core.Moonlight;
import net.mehvahdjukaar.moonlight.core.misc.FilteredResManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

public abstract class DynResourceGenerator<T extends DynamicResourcePack> implements PreparableReloadListener {

    public final T dynamicPack;

    protected final String modId;

    private boolean hasBeenInitialized;

    protected DynResourceGenerator(T pack, String modId) {
        this.dynamicPack = pack;
        this.modId = modId;
    }

    public void register() {
        this.dynamicPack.registerPack();
        MoonlightEventsHelper.addListener(this::onEarlyReload, (Class<T>) EarlyPackReloadEvent.class);
    }

    public abstract Logger getLogger();

    public T getPack() {
        return this.dynamicPack;
    }

    public abstract boolean dependsOnLoadedPacks();

    public abstract void regenerateDynamicAssets(ResourceManager var1);

    @Override
    public final CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier stage, ResourceManager manager, ProfilerFiller workerProfiler, ProfilerFiller mainProfiler, Executor workerExecutor, Executor mainExecutor) {
        if (PlatHelper.isModLoadingValid()) {
            this.onNormalReload(manager);
        } else {
            Moonlight.LOGGER.error("Cowardly refusing generate assets for a broken mod state");
        }
        return CompletableFuture.supplyAsync(() -> null, workerExecutor).thenCompose(stage::m_6769_).thenAcceptAsync(noResult -> {
        }, mainExecutor);
    }

    protected void onNormalReload(ResourceManager manager) {
    }

    protected void onEarlyReload(EarlyPackReloadEvent event) {
        if (event.type() == this.dynamicPack.packType) {
            try {
                this.reloadResources(event.manager());
            } catch (Exception var3) {
                Moonlight.LOGGER.error("An error occurred while trying to generate dynamic assets for {}:", this.dynamicPack, var3);
            }
        }
    }

    protected final void reloadResources(ResourceManager manager) {
        Stopwatch watch = Stopwatch.createStarted();
        boolean resourcePackSupport = this.dependsOnLoadedPacks();
        if (!this.hasBeenInitialized) {
            this.hasBeenInitialized = true;
            if (this.dynamicPack instanceof DynamicTexturePack tp) {
                tp.addPackLogo();
            }
            if (!resourcePackSupport) {
                PackRepository repository = this.getRepository();
                if (repository != null) {
                    Moonlight.CAN_EARLY_RELOAD_HACK.set(false);
                    FilteredResManager vanillaManager = FilteredResManager.including(repository, this.dynamicPack.packType, "vanilla", "mod_resources");
                    Moonlight.CAN_EARLY_RELOAD_HACK.set(true);
                    this.regenerateDynamicAssets(vanillaManager);
                    vanillaManager.close();
                } else {
                    this.regenerateDynamicAssets(manager);
                }
            }
        }
        if (resourcePackSupport) {
            PackRepository repository = this.getRepository();
            if (repository != null && this.hasBeenInitialized && !this.dynamicPack.clearOnReload) {
                Moonlight.CAN_EARLY_RELOAD_HACK.set(false);
                FilteredResManager nonSelfManager = FilteredResManager.excluding(repository, this.dynamicPack.packType, this.dynamicPack.packId());
                Moonlight.CAN_EARLY_RELOAD_HACK.set(true);
                this.regenerateDynamicAssets(nonSelfManager);
                nonSelfManager.close();
            }
            this.regenerateDynamicAssets(manager);
        }
        this.getLogger().info("Generated runtime {} for pack {} ({}) in: {} ms" + (this.dynamicPack.generateDebugResources ? " (debug resource dump on)" : ""), this.dynamicPack.getPackType(), this.dynamicPack.packId(), this.modId, watch.elapsed().toMillis());
    }

    @Nullable
    protected abstract PackRepository getRepository();

    public boolean alreadyHasAssetAtLocation(ResourceManager manager, ResourceLocation res, ResType type) {
        return this.alreadyHasAssetAtLocation(manager, type.getPath(res));
    }

    public boolean alreadyHasAssetAtLocation(ResourceManager manager, ResourceLocation res) {
        Optional<Resource> resource = manager.m_213713_(res);
        return resource.filter(value -> !value.sourcePackId().equals(this.dynamicPack.packId())).isPresent();
    }

    public void addSimilarJsonResource(ResourceManager manager, StaticResource resource, String keyword, String replaceWith) throws NoSuchElementException {
        this.addSimilarJsonResource(manager, resource, s -> s.replace(keyword, replaceWith));
    }

    public void addSimilarJsonResource(ResourceManager manager, StaticResource resource, Function<String, String> textTransform) throws NoSuchElementException {
        this.addSimilarJsonResource(manager, resource, textTransform, textTransform);
    }

    public void addSimilarJsonResource(ResourceManager manager, StaticResource resource, Function<String, String> textTransform, Function<String, String> pathTransform) throws NoSuchElementException {
        ResourceLocation fullPath = resource.location;
        StringBuilder builder = new StringBuilder();
        String[] partial = fullPath.getPath().split("/");
        for (int i = 0; i < partial.length; i++) {
            if (i != 0) {
                builder.append("/");
            }
            if (i == partial.length - 1) {
                builder.append((String) pathTransform.apply(partial[i]));
            } else {
                builder.append(partial[i]);
            }
        }
        ResourceLocation newRes = new ResourceLocation(this.modId, builder.toString());
        if (!this.alreadyHasAssetAtLocation(manager, newRes)) {
            String fullText = new String(resource.data, StandardCharsets.UTF_8);
            fullText = (String) textTransform.apply(fullText);
            this.dynamicPack.addBytes(newRes, fullText.getBytes());
        }
    }

    public void addResourceIfNotPresent(ResourceManager manager, StaticResource resource) {
        if (!this.alreadyHasAssetAtLocation(manager, resource.location)) {
            this.dynamicPack.addResource(resource);
        }
    }
}