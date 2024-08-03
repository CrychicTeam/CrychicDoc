package me.jellysquid.mods.sodium.client.model.color;

import java.util.Arrays;
import me.jellysquid.mods.sodium.client.model.quad.ModelQuadView;
import me.jellysquid.mods.sodium.client.model.quad.blender.BlendedColorProvider;
import me.jellysquid.mods.sodium.client.world.WorldSlice;
import me.jellysquid.mods.sodium.client.world.biome.BiomeColorSource;
import net.caffeinemc.mods.sodium.api.util.ColorARGB;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;

public class DefaultColorProviders {

    public static ColorProvider<BlockState> adapt(BlockColor provider) {
        return new DefaultColorProviders.VanillaAdapter(provider);
    }

    public static ColorProvider<FluidState> getFluidProvider() {
        return new DefaultColorProviders.ForgeFluidAdapter();
    }

    @Deprecated(forRemoval = true)
    public static class FoliageColorProvider<T> extends BlendedColorProvider<T> {

        public static final ColorProvider<BlockState> BLOCKS = new DefaultColorProviders.FoliageColorProvider<>();

        private FoliageColorProvider() {
        }

        @Override
        protected int getColor(WorldSlice world, int x, int y, int z) {
            return world.getColor(BiomeColorSource.FOLIAGE, x, y, z);
        }
    }

    private static class ForgeFluidAdapter implements ColorProvider<FluidState> {

        public void getColors(WorldSlice view, BlockPos pos, FluidState state, ModelQuadView quad, int[] output) {
            if (view != null && state != null) {
                Arrays.fill(output, ColorARGB.toABGR(IClientFluidTypeExtensions.of(state).getTintColor(state, view, pos)));
            } else {
                Arrays.fill(output, -1);
            }
        }
    }

    @Deprecated(forRemoval = true)
    public static class GrassColorProvider<T> extends BlendedColorProvider<T> {

        public static final ColorProvider<BlockState> BLOCKS = new DefaultColorProviders.GrassColorProvider<>();

        private GrassColorProvider() {
        }

        @Override
        protected int getColor(WorldSlice world, int x, int y, int z) {
            return world.getColor(BiomeColorSource.GRASS, x, y, z);
        }
    }

    private static class VanillaAdapter implements ColorProvider<BlockState> {

        private final BlockColor provider;

        private VanillaAdapter(BlockColor provider) {
            this.provider = provider;
        }

        public void getColors(WorldSlice view, BlockPos pos, BlockState state, ModelQuadView quad, int[] output) {
            Arrays.fill(output, ColorARGB.toABGR(this.provider.getColor(state, view, pos, quad.getColorIndex())));
        }
    }

    public static class VertexBlendedBiomeColorAdapter<T> extends BlendedColorProvider<T> {

        private final DefaultColorProviders.VertexBlendedBiomeColorAdapter.VanillaBiomeColor vanillaGetter;

        public VertexBlendedBiomeColorAdapter(DefaultColorProviders.VertexBlendedBiomeColorAdapter.VanillaBiomeColor vanillaGetter) {
            this.vanillaGetter = vanillaGetter;
        }

        @Override
        protected int getColor(WorldSlice world, BlockPos pos) {
            return this.vanillaGetter.getAverageColor(world, pos);
        }

        @FunctionalInterface
        public interface VanillaBiomeColor {

            int getAverageColor(BlockAndTintGetter var1, BlockPos var2);
        }
    }

    @Deprecated(forRemoval = true)
    public static class WaterColorProvider<T> extends BlendedColorProvider<T> {

        public static final ColorProvider<BlockState> BLOCKS = new DefaultColorProviders.WaterColorProvider<>();

        public static final ColorProvider<FluidState> FLUIDS = new DefaultColorProviders.WaterColorProvider<>();

        private WaterColorProvider() {
        }

        @Override
        protected int getColor(WorldSlice world, int x, int y, int z) {
            return world.getColor(BiomeColorSource.WATER, x, y, z);
        }
    }
}