package net.minecraft.data.advancements;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

public class AdvancementProvider implements DataProvider {

    private final PackOutput.PathProvider pathProvider;

    private final List<AdvancementSubProvider> subProviders;

    private final CompletableFuture<HolderLookup.Provider> registries;

    public AdvancementProvider(PackOutput packOutput0, CompletableFuture<HolderLookup.Provider> completableFutureHolderLookupProvider1, List<AdvancementSubProvider> listAdvancementSubProvider2) {
        this.pathProvider = packOutput0.createPathProvider(PackOutput.Target.DATA_PACK, "advancements");
        this.subProviders = listAdvancementSubProvider2;
        this.registries = completableFutureHolderLookupProvider1;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput0) {
        return this.registries.thenCompose(p_255484_ -> {
            Set<ResourceLocation> $$2 = new HashSet();
            List<CompletableFuture<?>> $$3 = new ArrayList();
            Consumer<Advancement> $$4 = p_253397_ -> {
                if (!$$2.add(p_253397_.getId())) {
                    throw new IllegalStateException("Duplicate advancement " + p_253397_.getId());
                } else {
                    Path $$4x = this.pathProvider.json(p_253397_.getId());
                    $$3.add(DataProvider.saveStable(cachedOutput0, p_253397_.deconstruct().serializeToJson(), $$4x));
                }
            };
            for (AdvancementSubProvider $$5 : this.subProviders) {
                $$5.generate(p_255484_, $$4);
            }
            return CompletableFuture.allOf((CompletableFuture[]) $$3.toArray(CompletableFuture[]::new));
        });
    }

    @Override
    public final String getName() {
        return "Advancements";
    }
}