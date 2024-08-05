package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.CaveFeatures;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class MossBlock extends Block implements BonemealableBlock {

    public MossBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
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
        serverLevel0.m_9598_().registry(Registries.CONFIGURED_FEATURE).flatMap(p_258973_ -> p_258973_.getHolder(CaveFeatures.MOSS_PATCH_BONEMEAL)).ifPresent(p_255669_ -> ((ConfiguredFeature) p_255669_.value()).place(serverLevel0, serverLevel0.getChunkSource().getGenerator(), randomSource1, blockPos2.above()));
    }
}