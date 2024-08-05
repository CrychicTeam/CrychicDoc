package net.minecraftforge.common.data;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.Tags;

public final class ForgeFluidTagsProvider extends FluidTagsProvider {

    public ForgeFluidTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, "forge", existingFileHelper);
    }

    @Override
    public void addTags(HolderLookup.Provider lookupProvider) {
        this.m_206424_(Tags.Fluids.MILK).m_176839_(ForgeMod.MILK.getId()).addOptional(ForgeMod.FLOWING_MILK.getId());
    }

    @Override
    public String getName() {
        return "Forge Fluid Tags";
    }
}