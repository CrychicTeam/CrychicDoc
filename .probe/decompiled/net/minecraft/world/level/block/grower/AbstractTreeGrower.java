package net.minecraft.world.level.block.grower;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public abstract class AbstractTreeGrower {

    @Nullable
    protected abstract ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource var1, boolean var2);

    public boolean growTree(ServerLevel serverLevel0, ChunkGenerator chunkGenerator1, BlockPos blockPos2, BlockState blockState3, RandomSource randomSource4) {
        ResourceKey<ConfiguredFeature<?, ?>> $$5 = this.getConfiguredFeature(randomSource4, this.hasFlowers(serverLevel0, blockPos2));
        if ($$5 == null) {
            return false;
        } else {
            Holder<ConfiguredFeature<?, ?>> $$6 = (Holder<ConfiguredFeature<?, ?>>) serverLevel0.m_9598_().registryOrThrow(Registries.CONFIGURED_FEATURE).getHolder($$5).orElse(null);
            if ($$6 == null) {
                return false;
            } else {
                ConfiguredFeature<?, ?> $$7 = $$6.value();
                BlockState $$8 = serverLevel0.m_6425_(blockPos2).createLegacyBlock();
                serverLevel0.m_7731_(blockPos2, $$8, 4);
                if ($$7.place(serverLevel0, chunkGenerator1, randomSource4, blockPos2)) {
                    if (serverLevel0.m_8055_(blockPos2) == $$8) {
                        serverLevel0.sendBlockUpdated(blockPos2, blockState3, $$8, 2);
                    }
                    return true;
                } else {
                    serverLevel0.m_7731_(blockPos2, blockState3, 4);
                    return false;
                }
            }
        }
    }

    private boolean hasFlowers(LevelAccessor levelAccessor0, BlockPos blockPos1) {
        for (BlockPos $$2 : BlockPos.MutableBlockPos.m_121940_(blockPos1.below().north(2).west(2), blockPos1.above().south(2).east(2))) {
            if (levelAccessor0.m_8055_($$2).m_204336_(BlockTags.FLOWERS)) {
                return true;
            }
        }
        return false;
    }
}