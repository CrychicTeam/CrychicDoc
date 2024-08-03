package net.minecraft.data.loot;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.RandomSequence;
import net.minecraft.world.level.levelgen.RandomSupport;
import net.minecraft.world.level.storage.loot.LootDataId;
import net.minecraft.world.level.storage.loot.LootDataResolver;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.slf4j.Logger;

public class LootTableProvider implements DataProvider {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final PackOutput.PathProvider pathProvider;

    private final Set<ResourceLocation> requiredTables;

    private final List<LootTableProvider.SubProviderEntry> subProviders;

    public LootTableProvider(PackOutput packOutput0, Set<ResourceLocation> setResourceLocation1, List<LootTableProvider.SubProviderEntry> listLootTableProviderSubProviderEntry2) {
        this.pathProvider = packOutput0.createPathProvider(PackOutput.Target.DATA_PACK, "loot_tables");
        this.subProviders = listLootTableProviderSubProviderEntry2;
        this.requiredTables = setResourceLocation1;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput0) {
        final Map<ResourceLocation, LootTable> $$1 = Maps.newHashMap();
        Map<RandomSupport.Seed128bit, ResourceLocation> $$2 = new Object2ObjectOpenHashMap();
        this.subProviders.forEach(p_288263_ -> ((LootTableSubProvider) p_288263_.provider().get()).generate((p_288259_, p_288260_) -> {
            ResourceLocation $$5x = (ResourceLocation) $$2.put(RandomSequence.seedForKey(p_288259_), p_288259_);
            if ($$5x != null) {
                Util.logAndPauseIfInIde("Loot table random sequence seed collision on " + $$5x + " and " + p_288259_);
            }
            p_288260_.setRandomSequence(p_288259_);
            if ($$1.put(p_288259_, p_288260_.setParamSet(p_288263_.paramSet).build()) != null) {
                throw new IllegalStateException("Duplicate loot table " + p_288259_);
            }
        }));
        ValidationContext $$3 = new ValidationContext(LootContextParamSets.ALL_PARAMS, new LootDataResolver() {

            @Nullable
            @Override
            public <T> T getElement(LootDataId<T> p_279283_) {
                return (T) (p_279283_.type() == LootDataType.TABLE ? $$1.get(p_279283_.location()) : null);
            }
        });
        for (ResourceLocation $$5 : Sets.difference(this.requiredTables, $$1.keySet())) {
            $$3.reportProblem("Missing built-in table: " + $$5);
        }
        $$1.forEach((p_278897_, p_278898_) -> p_278898_.validate($$3.setParams(p_278898_.getParamSet()).enterElement("{" + p_278897_ + "}", new LootDataId<>(LootDataType.TABLE, p_278897_))));
        Multimap<String, String> $$6 = $$3.getProblems();
        if (!$$6.isEmpty()) {
            $$6.forEach((p_124446_, p_124447_) -> LOGGER.warn("Found validation problem in {}: {}", p_124446_, p_124447_));
            throw new IllegalStateException("Failed to validate loot tables, see logs");
        } else {
            return CompletableFuture.allOf((CompletableFuture[]) $$1.entrySet().stream().map(p_278900_ -> {
                ResourceLocation $$2x = (ResourceLocation) p_278900_.getKey();
                LootTable $$3x = (LootTable) p_278900_.getValue();
                Path $$4 = this.pathProvider.json($$2x);
                return DataProvider.saveStable(cachedOutput0, LootDataType.TABLE.parser().toJsonTree($$3x), $$4);
            }).toArray(CompletableFuture[]::new));
        }
    }

    @Override
    public final String getName() {
        return "Loot Tables";
    }

    public static record SubProviderEntry(Supplier<LootTableSubProvider> f_243941_, LootContextParamSet f_244144_) {

        private final Supplier<LootTableSubProvider> provider;

        private final LootContextParamSet paramSet;

        public SubProviderEntry(Supplier<LootTableSubProvider> f_243941_, LootContextParamSet f_244144_) {
            this.provider = f_243941_;
            this.paramSet = f_244144_;
        }
    }
}