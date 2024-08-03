package net.minecraft.world.level.levelgen;

import com.google.common.annotations.VisibleForTesting;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

public interface PositionalRandomFactory {

    default RandomSource at(BlockPos blockPos0) {
        return this.at(blockPos0.m_123341_(), blockPos0.m_123342_(), blockPos0.m_123343_());
    }

    default RandomSource fromHashOf(ResourceLocation resourceLocation0) {
        return this.fromHashOf(resourceLocation0.toString());
    }

    RandomSource fromHashOf(String var1);

    RandomSource at(int var1, int var2, int var3);

    @VisibleForTesting
    void parityConfigString(StringBuilder var1);
}