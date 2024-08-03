package net.minecraft.world.level.block;

import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class GrassBlock extends SpreadingSnowyDirtBlock implements BonemealableBlock {

    public GrassBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
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
        BlockPos $$4 = blockPos2.above();
        BlockState $$5 = Blocks.GRASS.defaultBlockState();
        Optional<Holder.Reference<PlacedFeature>> $$6 = serverLevel0.m_9598_().registryOrThrow(Registries.PLACED_FEATURE).getHolder(VegetationPlacements.GRASS_BONEMEAL);
        label49: for (int $$7 = 0; $$7 < 128; $$7++) {
            BlockPos $$8 = $$4;
            for (int $$9 = 0; $$9 < $$7 / 16; $$9++) {
                $$8 = $$8.offset(randomSource1.nextInt(3) - 1, (randomSource1.nextInt(3) - 1) * randomSource1.nextInt(3) / 2, randomSource1.nextInt(3) - 1);
                if (!serverLevel0.m_8055_($$8.below()).m_60713_(this) || serverLevel0.m_8055_($$8).m_60838_(serverLevel0, $$8)) {
                    continue label49;
                }
            }
            BlockState $$10 = serverLevel0.m_8055_($$8);
            if ($$10.m_60713_($$5.m_60734_()) && randomSource1.nextInt(10) == 0) {
                ((BonemealableBlock) $$5.m_60734_()).performBonemeal(serverLevel0, randomSource1, $$8, $$10);
            }
            if ($$10.m_60795_()) {
                Holder<PlacedFeature> $$12;
                if (randomSource1.nextInt(8) == 0) {
                    List<ConfiguredFeature<?, ?>> $$11 = ((Biome) serverLevel0.m_204166_($$8).value()).getGenerationSettings().getFlowerFeatures();
                    if ($$11.isEmpty()) {
                        continue;
                    }
                    $$12 = ((RandomPatchConfiguration) ((ConfiguredFeature) $$11.get(0)).config()).feature();
                } else {
                    if (!$$6.isPresent()) {
                        continue;
                    }
                    $$12 = (Holder<PlacedFeature>) $$6.get();
                }
                $$12.value().place(serverLevel0, serverLevel0.getChunkSource().getGenerator(), randomSource1, $$8);
            }
        }
    }
}