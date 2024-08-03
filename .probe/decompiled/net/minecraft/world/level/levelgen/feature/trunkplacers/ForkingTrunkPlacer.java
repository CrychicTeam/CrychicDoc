package net.minecraft.world.level.levelgen.feature.trunkplacers;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;

public class ForkingTrunkPlacer extends TrunkPlacer {

    public static final Codec<ForkingTrunkPlacer> CODEC = RecordCodecBuilder.create(p_70161_ -> m_70305_(p_70161_).apply(p_70161_, ForkingTrunkPlacer::new));

    public ForkingTrunkPlacer(int int0, int int1, int int2) {
        super(int0, int1, int2);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return TrunkPlacerType.FORKING_TRUNK_PLACER;
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader levelSimulatedReader0, BiConsumer<BlockPos, BlockState> biConsumerBlockPosBlockState1, RandomSource randomSource2, int int3, BlockPos blockPos4, TreeConfiguration treeConfiguration5) {
        m_226169_(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, blockPos4.below(), treeConfiguration5);
        List<FoliagePlacer.FoliageAttachment> $$6 = Lists.newArrayList();
        Direction $$7 = Direction.Plane.HORIZONTAL.getRandomDirection(randomSource2);
        int $$8 = int3 - randomSource2.nextInt(4) - 1;
        int $$9 = 3 - randomSource2.nextInt(3);
        BlockPos.MutableBlockPos $$10 = new BlockPos.MutableBlockPos();
        int $$11 = blockPos4.m_123341_();
        int $$12 = blockPos4.m_123343_();
        OptionalInt $$13 = OptionalInt.empty();
        for (int $$14 = 0; $$14 < int3; $$14++) {
            int $$15 = blockPos4.m_123342_() + $$14;
            if ($$14 >= $$8 && $$9 > 0) {
                $$11 += $$7.getStepX();
                $$12 += $$7.getStepZ();
                $$9--;
            }
            if (this.m_226187_(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, $$10.set($$11, $$15, $$12), treeConfiguration5)) {
                $$13 = OptionalInt.of($$15 + 1);
            }
        }
        if ($$13.isPresent()) {
            $$6.add(new FoliagePlacer.FoliageAttachment(new BlockPos($$11, $$13.getAsInt(), $$12), 1, false));
        }
        $$11 = blockPos4.m_123341_();
        $$12 = blockPos4.m_123343_();
        Direction $$16 = Direction.Plane.HORIZONTAL.getRandomDirection(randomSource2);
        if ($$16 != $$7) {
            int $$17 = $$8 - randomSource2.nextInt(2) - 1;
            int $$18 = 1 + randomSource2.nextInt(3);
            $$13 = OptionalInt.empty();
            for (int $$19 = $$17; $$19 < int3 && $$18 > 0; $$18--) {
                if ($$19 >= 1) {
                    int $$20 = blockPos4.m_123342_() + $$19;
                    $$11 += $$16.getStepX();
                    $$12 += $$16.getStepZ();
                    if (this.m_226187_(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, $$10.set($$11, $$20, $$12), treeConfiguration5)) {
                        $$13 = OptionalInt.of($$20 + 1);
                    }
                }
                $$19++;
            }
            if ($$13.isPresent()) {
                $$6.add(new FoliagePlacer.FoliageAttachment(new BlockPos($$11, $$13.getAsInt(), $$12), 0, false));
            }
        }
        return $$6;
    }
}