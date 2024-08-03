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

public class GiantTrunkPlacer extends TrunkPlacer {

    public static final Codec<GiantTrunkPlacer> CODEC = RecordCodecBuilder.create(p_70189_ -> m_70305_(p_70189_).apply(p_70189_, GiantTrunkPlacer::new));

    public GiantTrunkPlacer(int int0, int int1, int int2) {
        super(int0, int1, int2);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return TrunkPlacerType.GIANT_TRUNK_PLACER;
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader levelSimulatedReader0, BiConsumer<BlockPos, BlockState> biConsumerBlockPosBlockState1, RandomSource randomSource2, int int3, BlockPos blockPos4, TreeConfiguration treeConfiguration5) {
        BlockPos $$6 = blockPos4.below();
        m_226169_(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, $$6, treeConfiguration5);
        m_226169_(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, $$6.east(), treeConfiguration5);
        m_226169_(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, $$6.south(), treeConfiguration5);
        m_226169_(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, $$6.south().east(), treeConfiguration5);
        BlockPos.MutableBlockPos $$7 = new BlockPos.MutableBlockPos();
        for (int $$8 = 0; $$8 < int3; $$8++) {
            this.placeLogIfFreeWithOffset(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, $$7, treeConfiguration5, blockPos4, 0, $$8, 0);
            if ($$8 < int3 - 1) {
                this.placeLogIfFreeWithOffset(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, $$7, treeConfiguration5, blockPos4, 1, $$8, 0);
                this.placeLogIfFreeWithOffset(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, $$7, treeConfiguration5, blockPos4, 1, $$8, 1);
                this.placeLogIfFreeWithOffset(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, $$7, treeConfiguration5, blockPos4, 0, $$8, 1);
            }
        }
        return ImmutableList.of(new FoliagePlacer.FoliageAttachment(blockPos4.above(int3), 0, true));
    }

    private void placeLogIfFreeWithOffset(LevelSimulatedReader levelSimulatedReader0, BiConsumer<BlockPos, BlockState> biConsumerBlockPosBlockState1, RandomSource randomSource2, BlockPos.MutableBlockPos blockPosMutableBlockPos3, TreeConfiguration treeConfiguration4, BlockPos blockPos5, int int6, int int7, int int8) {
        blockPosMutableBlockPos3.setWithOffset(blockPos5, int6, int7, int8);
        this.m_226163_(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, blockPosMutableBlockPos3, treeConfiguration4);
    }
}