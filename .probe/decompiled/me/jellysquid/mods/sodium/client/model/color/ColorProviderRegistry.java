package me.jellysquid.mods.sodium.client.model.color;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap.Entry;
import me.jellysquid.mods.sodium.client.model.color.interop.BlockColorsExtended;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class ColorProviderRegistry {

    private final Reference2ReferenceMap<Block, ColorProvider<BlockState>> blocks = new Reference2ReferenceOpenHashMap();

    private final Reference2ReferenceMap<Fluid, ColorProvider<FluidState>> fluids = new Reference2ReferenceOpenHashMap();

    private final ReferenceSet<Block> overridenBlocks;

    public ColorProviderRegistry(BlockColors blockColors) {
        Reference2ReferenceMap<Block, BlockColor> providers = BlockColorsExtended.getProviders(blockColors);
        ObjectIterator var3 = providers.reference2ReferenceEntrySet().iterator();
        while (var3.hasNext()) {
            Entry<Block, BlockColor> entry = (Entry<Block, BlockColor>) var3.next();
            this.blocks.put((Block) entry.getKey(), DefaultColorProviders.adapt((BlockColor) entry.getValue()));
        }
        this.overridenBlocks = BlockColorsExtended.getOverridenVanillaBlocks(blockColors);
        this.installOverrides();
    }

    private void installOverrides() {
        this.registerBlocks(new DefaultColorProviders.VertexBlendedBiomeColorAdapter<>(BiomeColors::m_108793_), Blocks.GRASS_BLOCK, Blocks.FERN, Blocks.GRASS, Blocks.POTTED_FERN, Blocks.PINK_PETALS, Blocks.SUGAR_CANE, Blocks.LARGE_FERN, Blocks.TALL_GRASS);
        this.registerBlocks(new DefaultColorProviders.VertexBlendedBiomeColorAdapter<>(BiomeColors::m_108804_), Blocks.OAK_LEAVES, Blocks.JUNGLE_LEAVES, Blocks.ACACIA_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.VINE, Blocks.MANGROVE_LEAVES);
        DefaultColorProviders.VertexBlendedBiomeColorAdapter.VanillaBiomeColor waterGetter = (getter, pos) -> BiomeColors.getAverageWaterColor(getter, pos) | 0xFF000000;
        this.registerBlocks(new DefaultColorProviders.VertexBlendedBiomeColorAdapter<>(waterGetter), Blocks.WATER, Blocks.BUBBLE_COLUMN);
        this.registerFluids(new DefaultColorProviders.VertexBlendedBiomeColorAdapter<>(waterGetter), Fluids.WATER, Fluids.FLOWING_WATER);
    }

    private void registerBlocks(ColorProvider<BlockState> resolver, Block... blocks) {
        for (Block block : blocks) {
            if (!this.overridenBlocks.contains(block)) {
                this.blocks.put(block, resolver);
            }
        }
    }

    private void registerFluids(ColorProvider<FluidState> resolver, Fluid... fluids) {
        for (Fluid fluid : fluids) {
            this.fluids.put(fluid, resolver);
        }
    }

    public ColorProvider<BlockState> getColorProvider(Block block) {
        return (ColorProvider<BlockState>) this.blocks.get(block);
    }

    public ColorProvider<FluidState> getColorProvider(Fluid fluid) {
        return (ColorProvider<FluidState>) this.fluids.get(fluid);
    }
}