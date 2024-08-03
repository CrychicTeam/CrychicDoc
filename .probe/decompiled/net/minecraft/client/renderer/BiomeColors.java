package net.minecraft.client.renderer;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.biome.Biome;

public class BiomeColors {

    public static final ColorResolver GRASS_COLOR_RESOLVER = Biome::m_47464_;

    public static final ColorResolver FOLIAGE_COLOR_RESOLVER = (p_108808_, p_108809_, p_108810_) -> p_108808_.getFoliageColor();

    public static final ColorResolver WATER_COLOR_RESOLVER = (p_108801_, p_108802_, p_108803_) -> p_108801_.getWaterColor();

    private static int getAverageColor(BlockAndTintGetter blockAndTintGetter0, BlockPos blockPos1, ColorResolver colorResolver2) {
        return blockAndTintGetter0.getBlockTint(blockPos1, colorResolver2);
    }

    public static int getAverageGrassColor(BlockAndTintGetter blockAndTintGetter0, BlockPos blockPos1) {
        return getAverageColor(blockAndTintGetter0, blockPos1, GRASS_COLOR_RESOLVER);
    }

    public static int getAverageFoliageColor(BlockAndTintGetter blockAndTintGetter0, BlockPos blockPos1) {
        return getAverageColor(blockAndTintGetter0, blockPos1, FOLIAGE_COLOR_RESOLVER);
    }

    public static int getAverageWaterColor(BlockAndTintGetter blockAndTintGetter0, BlockPos blockPos1) {
        return getAverageColor(blockAndTintGetter0, blockPos1, WATER_COLOR_RESOLVER);
    }
}