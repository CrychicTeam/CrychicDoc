package net.minecraft.data.registries;

import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.JsonOps;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import org.slf4j.Logger;

public class RegistriesDatapackGenerator implements DataProvider {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final PackOutput output;

    private final CompletableFuture<HolderLookup.Provider> registries;

    public RegistriesDatapackGenerator(PackOutput packOutput0, CompletableFuture<HolderLookup.Provider> completableFutureHolderLookupProvider1) {
        this.registries = completableFutureHolderLookupProvider1;
        this.output = packOutput0;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput0) {
        return this.registries.thenCompose(p_256533_ -> {
            DynamicOps<JsonElement> $$2 = RegistryOps.create(JsonOps.INSTANCE, p_256533_);
            return CompletableFuture.allOf((CompletableFuture[]) RegistryDataLoader.WORLDGEN_REGISTRIES.stream().flatMap(p_256552_ -> this.dumpRegistryCap(cachedOutput0, p_256533_, $$2, p_256552_).stream()).toArray(CompletableFuture[]::new));
        });
    }

    private <T> Optional<CompletableFuture<?>> dumpRegistryCap(CachedOutput cachedOutput0, HolderLookup.Provider holderLookupProvider1, DynamicOps<JsonElement> dynamicOpsJsonElement2, RegistryDataLoader.RegistryData<T> registryDataLoaderRegistryDataT3) {
        ResourceKey<? extends Registry<T>> $$4 = registryDataLoaderRegistryDataT3.key();
        return holderLookupProvider1.lookup($$4).map(p_255847_ -> {
            PackOutput.PathProvider $$5 = this.output.createPathProvider(PackOutput.Target.DATA_PACK, $$4.location().getPath());
            return CompletableFuture.allOf((CompletableFuture[]) p_255847_.m_214062_().map(p_256105_ -> dumpValue($$5.json(p_256105_.key().location()), cachedOutput0, dynamicOpsJsonElement2, registryDataLoaderRegistryDataT3.elementCodec(), p_256105_.value())).toArray(CompletableFuture[]::new));
        });
    }

    private static <E> CompletableFuture<?> dumpValue(Path path0, CachedOutput cachedOutput1, DynamicOps<JsonElement> dynamicOpsJsonElement2, Encoder<E> encoderE3, E e4) {
        Optional<JsonElement> $$5 = encoderE3.encodeStart(dynamicOpsJsonElement2, e4).resultOrPartial(p_255999_ -> LOGGER.error("Couldn't serialize element {}: {}", path0, p_255999_));
        return $$5.isPresent() ? DataProvider.saveStable(cachedOutput1, (JsonElement) $$5.get(), path0) : CompletableFuture.completedFuture(null);
    }

    @Override
    public final String getName() {
        return "Registries";
    }
}