package net.minecraft.data.tags;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class FluidTagsProvider extends IntrinsicHolderTagsProvider<Fluid> {

    public FluidTagsProvider(PackOutput packOutput0, CompletableFuture<HolderLookup.Provider> completableFutureHolderLookupProvider1) {
        super(packOutput0, Registries.FLUID, completableFutureHolderLookupProvider1, p_256474_ -> p_256474_.builtInRegistryHolder().key());
    }

    @Override
    protected void addTags(HolderLookup.Provider holderLookupProvider0) {
        this.m_206424_(FluidTags.WATER).add(Fluids.WATER, Fluids.FLOWING_WATER);
        this.m_206424_(FluidTags.LAVA).add(Fluids.LAVA, Fluids.FLOWING_LAVA);
    }
}