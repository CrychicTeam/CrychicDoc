package net.minecraft.world.level.levelgen.feature.trunkplacers;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;

public class BendingTrunkPlacer extends TrunkPlacer {

    public static final Codec<BendingTrunkPlacer> CODEC = RecordCodecBuilder.create(p_161786_ -> m_70305_(p_161786_).and(p_161786_.group(ExtraCodecs.POSITIVE_INT.optionalFieldOf("min_height_for_leaves", 1).forGetter(p_161788_ -> p_161788_.minHeightForLeaves), IntProvider.codec(1, 64).fieldOf("bend_length").forGetter(p_161784_ -> p_161784_.bendLength))).apply(p_161786_, BendingTrunkPlacer::new));

    private final int minHeightForLeaves;

    private final IntProvider bendLength;

    public BendingTrunkPlacer(int int0, int int1, int int2, int int3, IntProvider intProvider4) {
        super(int0, int1, int2);
        this.minHeightForLeaves = int3;
        this.bendLength = intProvider4;
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return TrunkPlacerType.BENDING_TRUNK_PLACER;
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader levelSimulatedReader0, BiConsumer<BlockPos, BlockState> biConsumerBlockPosBlockState1, RandomSource randomSource2, int int3, BlockPos blockPos4, TreeConfiguration treeConfiguration5) {
        Direction $$6 = Direction.Plane.HORIZONTAL.getRandomDirection(randomSource2);
        int $$7 = int3 - 1;
        BlockPos.MutableBlockPos $$8 = blockPos4.mutable();
        BlockPos $$9 = $$8.m_7495_();
        m_226169_(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, $$9, treeConfiguration5);
        List<FoliagePlacer.FoliageAttachment> $$10 = Lists.newArrayList();
        for (int $$11 = 0; $$11 <= $$7; $$11++) {
            if ($$11 + 1 >= $$7 + randomSource2.nextInt(2)) {
                $$8.move($$6);
            }
            if (TreeFeature.validTreePos(levelSimulatedReader0, $$8)) {
                this.m_226187_(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, $$8, treeConfiguration5);
            }
            if ($$11 >= this.minHeightForLeaves) {
                $$10.add(new FoliagePlacer.FoliageAttachment($$8.immutable(), 0, false));
            }
            $$8.move(Direction.UP);
        }
        int $$12 = this.bendLength.sample(randomSource2);
        for (int $$13 = 0; $$13 <= $$12; $$13++) {
            if (TreeFeature.validTreePos(levelSimulatedReader0, $$8)) {
                this.m_226187_(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, $$8, treeConfiguration5);
            }
            $$10.add(new FoliagePlacer.FoliageAttachment($$8.immutable(), 0, false));
            $$8.move($$6);
        }
        return $$10;
    }
}