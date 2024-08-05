package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.NetherFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.lighting.LightEngine;

public class NyliumBlock extends Block implements BonemealableBlock {

    protected NyliumBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    private static boolean canBeNylium(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        BlockPos $$3 = blockPos2.above();
        BlockState $$4 = levelReader1.m_8055_($$3);
        int $$5 = LightEngine.getLightBlockInto(levelReader1, blockState0, blockPos2, $$4, $$3, Direction.UP, $$4.m_60739_(levelReader1, $$3));
        return $$5 < levelReader1.m_7469_();
    }

    @Override
    public void randomTick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if (!canBeNylium(blockState0, serverLevel1, blockPos2)) {
            serverLevel1.m_46597_(blockPos2, Blocks.NETHERRACK.defaultBlockState());
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader0, BlockPos blockPos1, BlockState blockState2, boolean boolean3) {
        return levelReader0.m_8055_(blockPos1.above()).m_60795_();
    }

    @Override
    public boolean isBonemealSuccess(Level level0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        BlockState $$4 = serverLevel0.m_8055_(blockPos2);
        BlockPos $$5 = blockPos2.above();
        ChunkGenerator $$6 = serverLevel0.getChunkSource().getGenerator();
        Registry<ConfiguredFeature<?, ?>> $$7 = serverLevel0.m_9598_().registryOrThrow(Registries.CONFIGURED_FEATURE);
        if ($$4.m_60713_(Blocks.CRIMSON_NYLIUM)) {
            this.place($$7, NetherFeatures.CRIMSON_FOREST_VEGETATION_BONEMEAL, serverLevel0, $$6, randomSource1, $$5);
        } else if ($$4.m_60713_(Blocks.WARPED_NYLIUM)) {
            this.place($$7, NetherFeatures.WARPED_FOREST_VEGETATION_BONEMEAL, serverLevel0, $$6, randomSource1, $$5);
            this.place($$7, NetherFeatures.NETHER_SPROUTS_BONEMEAL, serverLevel0, $$6, randomSource1, $$5);
            if (randomSource1.nextInt(8) == 0) {
                this.place($$7, NetherFeatures.TWISTING_VINES_BONEMEAL, serverLevel0, $$6, randomSource1, $$5);
            }
        }
    }

    private void place(Registry<ConfiguredFeature<?, ?>> registryConfiguredFeature0, ResourceKey<ConfiguredFeature<?, ?>> resourceKeyConfiguredFeature1, ServerLevel serverLevel2, ChunkGenerator chunkGenerator3, RandomSource randomSource4, BlockPos blockPos5) {
        registryConfiguredFeature0.getHolder(resourceKeyConfiguredFeature1).ifPresent(p_255920_ -> ((ConfiguredFeature) p_255920_.value()).place(serverLevel2, chunkGenerator3, randomSource4, blockPos5));
    }
}