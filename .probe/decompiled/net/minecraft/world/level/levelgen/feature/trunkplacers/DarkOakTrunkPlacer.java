package net.minecraft.world.level.levelgen.feature.trunkplacers;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;

public class DarkOakTrunkPlacer extends TrunkPlacer {

    public static final Codec<DarkOakTrunkPlacer> CODEC = RecordCodecBuilder.create(p_70090_ -> m_70305_(p_70090_).apply(p_70090_, DarkOakTrunkPlacer::new));

    public DarkOakTrunkPlacer(int int0, int int1, int int2) {
        super(int0, int1, int2);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return TrunkPlacerType.DARK_OAK_TRUNK_PLACER;
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader levelSimulatedReader0, BiConsumer<BlockPos, BlockState> biConsumerBlockPosBlockState1, RandomSource randomSource2, int int3, BlockPos blockPos4, TreeConfiguration treeConfiguration5) {
        List<FoliagePlacer.FoliageAttachment> $$6 = Lists.newArrayList();
        BlockPos $$7 = blockPos4.below();
        m_226169_(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, $$7, treeConfiguration5);
        m_226169_(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, $$7.east(), treeConfiguration5);
        m_226169_(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, $$7.south(), treeConfiguration5);
        m_226169_(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, $$7.south().east(), treeConfiguration5);
        Direction $$8 = Direction.Plane.HORIZONTAL.getRandomDirection(randomSource2);
        int $$9 = int3 - randomSource2.nextInt(4);
        int $$10 = 2 - randomSource2.nextInt(3);
        int $$11 = blockPos4.m_123341_();
        int $$12 = blockPos4.m_123342_();
        int $$13 = blockPos4.m_123343_();
        int $$14 = $$11;
        int $$15 = $$13;
        int $$16 = $$12 + int3 - 1;
        for (int $$17 = 0; $$17 < int3; $$17++) {
            if ($$17 >= $$9 && $$10 > 0) {
                $$14 += $$8.getStepX();
                $$15 += $$8.getStepZ();
                $$10--;
            }
            int $$18 = $$12 + $$17;
            BlockPos $$19 = new BlockPos($$14, $$18, $$15);
            if (TreeFeature.isAirOrLeaves(levelSimulatedReader0, $$19)) {
                this.m_226187_(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, $$19, treeConfiguration5);
                this.m_226187_(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, $$19.east(), treeConfiguration5);
                this.m_226187_(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, $$19.south(), treeConfiguration5);
                this.m_226187_(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, $$19.east().south(), treeConfiguration5);
            }
        }
        $$6.add(new FoliagePlacer.FoliageAttachment(new BlockPos($$14, $$16, $$15), 0, true));
        for (int $$20 = -1; $$20 <= 2; $$20++) {
            for (int $$21 = -1; $$21 <= 2; $$21++) {
                if (($$20 < 0 || $$20 > 1 || $$21 < 0 || $$21 > 1) && randomSource2.nextInt(3) <= 0) {
                    int $$22 = randomSource2.nextInt(3) + 2;
                    for (int $$23 = 0; $$23 < $$22; $$23++) {
                        this.m_226187_(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, new BlockPos($$11 + $$20, $$16 - $$23 - 1, $$13 + $$21), treeConfiguration5);
                    }
                    $$6.add(new FoliagePlacer.FoliageAttachment(new BlockPos($$14 + $$20, $$16, $$15 + $$21), 0, false));
                }
            }
        }
        return $$6;
    }
}