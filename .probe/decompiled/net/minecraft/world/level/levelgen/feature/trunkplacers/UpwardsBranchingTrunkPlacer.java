package net.minecraft.world.level.levelgen.feature.trunkplacers;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;

public class UpwardsBranchingTrunkPlacer extends TrunkPlacer {

    public static final Codec<UpwardsBranchingTrunkPlacer> CODEC = RecordCodecBuilder.create(p_259008_ -> m_70305_(p_259008_).and(p_259008_.group(IntProvider.POSITIVE_CODEC.fieldOf("extra_branch_steps").forGetter(p_226242_ -> p_226242_.extraBranchSteps), Codec.floatRange(0.0F, 1.0F).fieldOf("place_branch_per_log_probability").forGetter(p_226240_ -> p_226240_.placeBranchPerLogProbability), IntProvider.NON_NEGATIVE_CODEC.fieldOf("extra_branch_length").forGetter(p_226238_ -> p_226238_.extraBranchLength), RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("can_grow_through").forGetter(p_226234_ -> p_226234_.canGrowThrough))).apply(p_259008_, UpwardsBranchingTrunkPlacer::new));

    private final IntProvider extraBranchSteps;

    private final float placeBranchPerLogProbability;

    private final IntProvider extraBranchLength;

    private final HolderSet<Block> canGrowThrough;

    public UpwardsBranchingTrunkPlacer(int int0, int int1, int int2, IntProvider intProvider3, float float4, IntProvider intProvider5, HolderSet<Block> holderSetBlock6) {
        super(int0, int1, int2);
        this.extraBranchSteps = intProvider3;
        this.placeBranchPerLogProbability = float4;
        this.extraBranchLength = intProvider5;
        this.canGrowThrough = holderSetBlock6;
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return TrunkPlacerType.UPWARDS_BRANCHING_TRUNK_PLACER;
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader levelSimulatedReader0, BiConsumer<BlockPos, BlockState> biConsumerBlockPosBlockState1, RandomSource randomSource2, int int3, BlockPos blockPos4, TreeConfiguration treeConfiguration5) {
        List<FoliagePlacer.FoliageAttachment> $$6 = Lists.newArrayList();
        BlockPos.MutableBlockPos $$7 = new BlockPos.MutableBlockPos();
        for (int $$8 = 0; $$8 < int3; $$8++) {
            int $$9 = blockPos4.m_123342_() + $$8;
            if (this.m_226187_(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, $$7.set(blockPos4.m_123341_(), $$9, blockPos4.m_123343_()), treeConfiguration5) && $$8 < int3 - 1 && randomSource2.nextFloat() < this.placeBranchPerLogProbability) {
                Direction $$10 = Direction.Plane.HORIZONTAL.getRandomDirection(randomSource2);
                int $$11 = this.extraBranchLength.sample(randomSource2);
                int $$12 = Math.max(0, $$11 - this.extraBranchLength.sample(randomSource2) - 1);
                int $$13 = this.extraBranchSteps.sample(randomSource2);
                this.placeBranch(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, int3, treeConfiguration5, $$6, $$7, $$9, $$10, $$12, $$13);
            }
            if ($$8 == int3 - 1) {
                $$6.add(new FoliagePlacer.FoliageAttachment($$7.set(blockPos4.m_123341_(), $$9 + 1, blockPos4.m_123343_()), 0, false));
            }
        }
        return $$6;
    }

    private void placeBranch(LevelSimulatedReader levelSimulatedReader0, BiConsumer<BlockPos, BlockState> biConsumerBlockPosBlockState1, RandomSource randomSource2, int int3, TreeConfiguration treeConfiguration4, List<FoliagePlacer.FoliageAttachment> listFoliagePlacerFoliageAttachment5, BlockPos.MutableBlockPos blockPosMutableBlockPos6, int int7, Direction direction8, int int9, int int10) {
        int $$11 = int7 + int9;
        int $$12 = blockPosMutableBlockPos6.m_123341_();
        int $$13 = blockPosMutableBlockPos6.m_123343_();
        int $$14 = int9;
        while ($$14 < int3 && int10 > 0) {
            if ($$14 >= 1) {
                int $$15 = int7 + $$14;
                $$12 += direction8.getStepX();
                $$13 += direction8.getStepZ();
                $$11 = $$15;
                if (this.m_226187_(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, blockPosMutableBlockPos6.set($$12, $$15, $$13), treeConfiguration4)) {
                    $$11 = $$15 + 1;
                }
                listFoliagePlacerFoliageAttachment5.add(new FoliagePlacer.FoliageAttachment(blockPosMutableBlockPos6.immutable(), 0, false));
            }
            $$14++;
            int10--;
        }
        if ($$11 - int7 > 1) {
            BlockPos $$16 = new BlockPos($$12, $$11, $$13);
            listFoliagePlacerFoliageAttachment5.add(new FoliagePlacer.FoliageAttachment($$16, 0, false));
            listFoliagePlacerFoliageAttachment5.add(new FoliagePlacer.FoliageAttachment($$16.below(2), 0, false));
        }
    }

    @Override
    protected boolean validTreePos(LevelSimulatedReader levelSimulatedReader0, BlockPos blockPos1) {
        return super.validTreePos(levelSimulatedReader0, blockPos1) || levelSimulatedReader0.isStateAtPosition(blockPos1, p_226232_ -> p_226232_.m_204341_(this.canGrowThrough));
    }
}