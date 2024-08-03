package net.minecraft.tags;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

public class TagManager implements PreparableReloadListener {

    private static final Map<ResourceKey<? extends Registry<?>>, String> CUSTOM_REGISTRY_DIRECTORIES = Map.of(Registries.BLOCK, "tags/blocks", Registries.ENTITY_TYPE, "tags/entity_types", Registries.FLUID, "tags/fluids", Registries.GAME_EVENT, "tags/game_events", Registries.ITEM, "tags/items");

    private final RegistryAccess registryAccess;

    private List<TagManager.LoadResult<?>> results = List.of();

    public TagManager(RegistryAccess registryAccess0) {
        this.registryAccess = registryAccess0;
    }

    public List<TagManager.LoadResult<?>> getResult() {
        return this.results;
    }

    public static String getTagDir(ResourceKey<? extends Registry<?>> resourceKeyExtendsRegistry0) {
        String $$1 = (String) CUSTOM_REGISTRY_DIRECTORIES.get(resourceKeyExtendsRegistry0);
        return $$1 != null ? $$1 : "tags/" + resourceKeyExtendsRegistry0.location().getPath();
    }

    @Override
    public CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier preparableReloadListenerPreparationBarrier0, ResourceManager resourceManager1, ProfilerFiller profilerFiller2, ProfilerFiller profilerFiller3, Executor executor4, Executor executor5) {
        List<? extends CompletableFuture<? extends TagManager.LoadResult<?>>> $$6 = this.registryAccess.registries().map(p_203927_ -> this.createLoader(resourceManager1, executor4, p_203927_)).toList();
        return CompletableFuture.allOf((CompletableFuture[]) $$6.toArray(CompletableFuture[]::new)).thenCompose(preparableReloadListenerPreparationBarrier0::m_6769_).thenAcceptAsync(p_203917_ -> this.results = (List<TagManager.LoadResult<?>>) $$6.stream().map(CompletableFuture::join).collect(Collectors.toUnmodifiableList()), executor5);
    }

    private <T> CompletableFuture<TagManager.LoadResult<T>> createLoader(ResourceManager resourceManager0, Executor executor1, RegistryAccess.RegistryEntry<T> registryAccessRegistryEntryT2) {
        ResourceKey<? extends Registry<T>> $$3 = registryAccessRegistryEntryT2.key();
        Registry<T> $$4 = registryAccessRegistryEntryT2.value();
        TagLoader<Holder<T>> $$5 = new TagLoader<>(p_258247_ -> $$4.getHolder(ResourceKey.create($$3, p_258247_)), getTagDir($$3));
        return CompletableFuture.supplyAsync(() -> new TagManager.LoadResult<>($$3, $$5.loadAndBuild(resourceManager0)), executor1);
    }

    public static record LoadResult<T>(ResourceKey<? extends Registry<T>> f_203928_, Map<ResourceLocation, Collection<Holder<T>>> f_203929_) {

        private final ResourceKey<? extends Registry<T>> key;

        private final Map<ResourceLocation, Collection<Holder<T>>> tags;

        public LoadResult(ResourceKey<? extends Registry<T>> f_203928_, Map<ResourceLocation, Collection<Holder<T>>> f_203929_) {
            this.key = f_203928_;
            this.tags = f_203929_;
        }
    }
}