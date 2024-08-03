package net.minecraft.world.level;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.lighting.LevelLightEngine;

public interface BlockAndTintGetter extends BlockGetter {

    float getShade(Direction var1, boolean var2);

    LevelLightEngine getLightEngine();

    int getBlockTint(BlockPos var1, ColorResolver var2);

    default int getBrightness(LightLayer lightLayer0, BlockPos blockPos1) {
        return this.getLightEngine().getLayerListener(lightLayer0).getLightValue(blockPos1);
    }

    default int getRawBrightness(BlockPos blockPos0, int int1) {
        return this.getLightEngine().getRawBrightness(blockPos0, int1);
    }

    default boolean canSeeSky(BlockPos blockPos0) {
        return this.getBrightness(LightLayer.SKY, blockPos0) >= this.m_7469_();
    }
}