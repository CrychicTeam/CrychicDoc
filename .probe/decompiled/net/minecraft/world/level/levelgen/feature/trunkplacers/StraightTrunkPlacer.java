package net.minecraft.world.level.levelgen.feature.trunkplacers;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;

public class StraightTrunkPlacer extends TrunkPlacer {

    public static final Codec<StraightTrunkPlacer> CODEC = RecordCodecBuilder.create(p_70261_ -> m_70305_(p_70261_).apply(p_70261_, StraightTrunkPlacer::new));

    public StraightTrunkPlacer(int int0, int int1, int int2) {
        super(int0, int1, int2);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return TrunkPlacerType.STRAIGHT_TRUNK_PLACER;
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader levelSimulatedReader0, BiConsumer<BlockPos, BlockState> biConsumerBlockPosBlockState1, RandomSource randomSource2, int int3, BlockPos blockPos4, TreeConfiguration treeConfiguration5) {
        m_226169_(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, blockPos4.below(), treeConfiguration5);
        for (int $$6 = 0; $$6 < int3; $$6++) {
            this.m_226187_(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, blockPos4.above($$6), treeConfiguration5);
        }
        return ImmutableList.of(new FoliagePlacer.FoliageAttachment(blockPos4.above(int3), 0, false));
    }
}