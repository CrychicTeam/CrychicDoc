package net.minecraft.data.info;

import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
import org.slf4j.Logger;

public class BiomeParametersDumpReport implements DataProvider {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final Path topPath;

    private final CompletableFuture<HolderLookup.Provider> registries;

    private static final MapCodec<ResourceKey<Biome>> ENTRY_CODEC = ResourceKey.codec(Registries.BIOME).fieldOf("biome");

    private static final Codec<Climate.ParameterList<ResourceKey<Biome>>> CODEC = Climate.ParameterList.codec(ENTRY_CODEC).fieldOf("biomes").codec();

    public BiomeParametersDumpReport(PackOutput packOutput0, CompletableFuture<HolderLookup.Provider> completableFutureHolderLookupProvider1) {
        this.topPath = packOutput0.getOutputFolder(PackOutput.Target.REPORTS).resolve("biome_parameters");
        this.registries = completableFutureHolderLookupProvider1;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput0) {
        return this.registries.thenCompose(p_274755_ -> {
            DynamicOps<JsonElement> $$2 = RegistryOps.create(JsonOps.INSTANCE, p_274755_);
            List<CompletableFuture<?>> $$3 = new ArrayList();
            MultiNoiseBiomeSourceParameterList.knownPresets().forEach((p_274759_, p_274760_) -> $$3.add(dumpValue(this.createPath(p_274759_.id()), cachedOutput0, $$2, CODEC, p_274760_)));
            return CompletableFuture.allOf((CompletableFuture[]) $$3.toArray(CompletableFuture[]::new));
        });
    }

    private static <E> CompletableFuture<?> dumpValue(Path path0, CachedOutput cachedOutput1, DynamicOps<JsonElement> dynamicOpsJsonElement2, Encoder<E> encoderE3, E e4) {
        Optional<JsonElement> $$5 = encoderE3.encodeStart(dynamicOpsJsonElement2, e4).resultOrPartial(p_236195_ -> LOGGER.error("Couldn't serialize element {}: {}", path0, p_236195_));
        return $$5.isPresent() ? DataProvider.saveStable(cachedOutput1, (JsonElement) $$5.get(), path0) : CompletableFuture.completedFuture(null);
    }

    private Path createPath(ResourceLocation resourceLocation0) {
        return this.topPath.resolve(resourceLocation0.getNamespace()).resolve(resourceLocation0.getPath() + ".json");
    }

    @Override
    public final String getName() {
        return "Biome Parameters";
    }
}