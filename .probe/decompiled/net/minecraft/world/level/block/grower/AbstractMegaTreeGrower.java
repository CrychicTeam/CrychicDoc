package net.minecraft.world.level.block.grower;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public abstract class AbstractMegaTreeGrower extends AbstractTreeGrower {

    @Override
    public boolean growTree(ServerLevel serverLevel0, ChunkGenerator chunkGenerator1, BlockPos blockPos2, BlockState blockState3, RandomSource randomSource4) {
        for (int $$5 = 0; $$5 >= -1; $$5--) {
            for (int $$6 = 0; $$6 >= -1; $$6--) {
                if (isTwoByTwoSapling(blockState3, serverLevel0, blockPos2, $$5, $$6)) {
                    return this.placeMega(serverLevel0, chunkGenerator1, blockPos2, blockState3, randomSource4, $$5, $$6);
                }
            }
        }
        return super.growTree(serverLevel0, chunkGenerator1, blockPos2, blockState3, randomSource4);
    }

    @Nullable
    protected abstract ResourceKey<ConfiguredFeature<?, ?>> getConfiguredMegaFeature(RandomSource var1);

    public boolean placeMega(ServerLevel serverLevel0, ChunkGenerator chunkGenerator1, BlockPos blockPos2, BlockState blockState3, RandomSource randomSource4, int int5, int int6) {
        ResourceKey<ConfiguredFeature<?, ?>> $$7 = this.getConfiguredMegaFeature(randomSource4);
        if ($$7 == null) {
            return false;
        } else {
            Holder<ConfiguredFeature<?, ?>> $$8 = (Holder<ConfiguredFeature<?, ?>>) serverLevel0.m_9598_().registryOrThrow(Registries.CONFIGURED_FEATURE).getHolder($$7).orElse(null);
            if ($$8 == null) {
                return false;
            } else {
                ConfiguredFeature<?, ?> $$9 = $$8.value();
                BlockState $$10 = Blocks.AIR.defaultBlockState();
                serverLevel0.m_7731_(blockPos2.offset(int5, 0, int6), $$10, 4);
                serverLevel0.m_7731_(blockPos2.offset(int5 + 1, 0, int6), $$10, 4);
                serverLevel0.m_7731_(blockPos2.offset(int5, 0, int6 + 1), $$10, 4);
                serverLevel0.m_7731_(blockPos2.offset(int5 + 1, 0, int6 + 1), $$10, 4);
                if ($$9.place(serverLevel0, chunkGenerator1, randomSource4, blockPos2.offset(int5, 0, int6))) {
                    return true;
                } else {
                    serverLevel0.m_7731_(blockPos2.offset(int5, 0, int6), blockState3, 4);
                    serverLevel0.m_7731_(blockPos2.offset(int5 + 1, 0, int6), blockState3, 4);
                    serverLevel0.m_7731_(blockPos2.offset(int5, 0, int6 + 1), blockState3, 4);
                    serverLevel0.m_7731_(blockPos2.offset(int5 + 1, 0, int6 + 1), blockState3, 4);
                    return false;
                }
            }
        }
    }

    public static boolean isTwoByTwoSapling(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, int int3, int int4) {
        Block $$5 = blockState0.m_60734_();
        return blockGetter1.getBlockState(blockPos2.offset(int3, 0, int4)).m_60713_($$5) && blockGetter1.getBlockState(blockPos2.offset(int3 + 1, 0, int4)).m_60713_($$5) && blockGetter1.getBlockState(blockPos2.offset(int3, 0, int4 + 1)).m_60713_($$5) && blockGetter1.getBlockState(blockPos2.offset(int3 + 1, 0, int4 + 1)).m_60713_($$5);
    }
}